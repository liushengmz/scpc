package com.system.server;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.system.cache.BaseDataCache;
import com.system.cache.SysConfigCache;
import com.system.constant.FlowConstant;
import com.system.model.BusinessResponModel;
import com.system.model.CpModel;
import com.system.model.RedisToBusinessCpOrderModel;
import com.system.model.ToBusinessChildOrderModel;
import com.system.model.ToBusinessOrderModel;
import com.system.util.Base64UTF;
import com.system.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 关于缓存的解决：
 * 1、增加一张数据临时表，只存储三天的数据
 * 2、增加数据的时候，直接往临时表和月表里增加数据（月表在前，临时表在后，临时表数据记录月表的月份和ID）
 * 3、把新增的数据写进REDIS，并设置过期时间为24小时，记录临时表中ID，月表中的月份和ID
 * 4、对于数据的更新，要保持月表、临时表、缓存中的数据一致
 * 5、缓存数据只缓存24小时内的所有定单
 * 6、假设数据进来后，没有在缓存中找到重复的定单，则以正常数据逻辑进行处理
 * 7、处理完以后往月表里写入，如果写入失败，则检查是否有重复的定单，如果有，直接返回定单重复的标识
 */

/**
 * TO B 系统
 * @author Andy.Chen
 *
 */
public class ToBusinessRequestServerV1
{
	private static Logger logger = Logger.getLogger(ToBusinessRequestServerV1.class);
	
	public static BusinessResponModel handleBusinessRequest(String queryData,String ip)
	{
		BusinessResponModel response = new BusinessResponModel();
		response.setOrderId("");
		
		String oriJsonData = Base64UTF.decode(queryData);
		
		if(StringUtil.isNullOrEmpty(oriJsonData))
		{
			setResponseStatus(response,FlowConstant.TO_B_REQUEST_PARAM_ERROR);
			return response;
		}
		
		JSONObject joParam = JSONObject.fromObject(oriJsonData);
		
		if(joParam==null)
		{
			setResponseStatus(response,FlowConstant.TO_B_REQUEST_PARAM_ERROR);
			return response;
		}
		
		//验签方式  md5(CP方的CPID + orderId + 我方提供的加密KEY) 
		int cpId = joParam.getInt("cpId");
		String orderId = joParam.getString("orderId");
		String sign = joParam.getString("sign");
		
		if(cpId<=0 || StringUtil.isNullOrEmpty(orderId) || StringUtil.isNullOrEmpty(sign))
		{
			setResponseStatus(response, FlowConstant.TO_B_REQUEST_LACK_PARAMS);
			return response;
		}
		
		response.setOrderId(orderId);
		
		int realCpId = cpId - FlowConstant.FLOW_SYS_CP_CODE_BASE_COUNT;
		
		CpModel cpModel = BaseDataCache.loadCpById(realCpId);
		
		if(cpModel==null)
		{
			setResponseStatus(response, FlowConstant.TO_B_REQUEST_NOT_COMPANY);
			return response;
		}
		
		//这里为什么用cpId而不是用realCpId是因为CP方拿到的是我们加过数的CPID
		String wSign = StringUtil.getMd5String(cpId + orderId + cpModel.getSignKey(), 32);
		
		if(!sign.equalsIgnoreCase(wSign))
		{
			setResponseStatus(response, FlowConstant.TO_B_REQUEST_SIGN_ERROR);
			return response;
		}
		
		boolean isIpAuthFail = true;
		
		for(String tmpIp : cpModel.getIpList())
		{
			if(ip.equals(tmpIp))
			{
				isIpAuthFail = false;
				break;
			}
		}
		
		if(isIpAuthFail)
		{
			setResponseStatus(response, FlowConstant.TO_B_REQUERY_IP_AUTH_FAIL);
			return response;
		}
		
		//开始取子定单，初版只取成功转化成子订单的数据，不成功的不管
		List<ToBusinessChildOrderModel> childList = getChildOrderListFromJson(joParam);
		
		//先填充请求实体类，给后面处理
		ToBusinessOrderModel orderModel = new ToBusinessOrderModel();
		orderModel.setCpId(realCpId);
		orderModel.setOrderId(orderId);
		orderModel.setOrderList(childList);
		
		if(childList.isEmpty())
		{
			setResponseStatus(response, FlowConstant.TO_B_REQUEST_CHILD_ORDER_EMPTY); 
			return response;
		}
		
		//缓存中已存在相同的定单号
		if(RedisServer.existCpOrderId(realCpId, orderId))
		{
			setResponseStatus(response, FlowConstant.TO_B_REQUEST_ORDER_REPEAT); 
			return response;
		}
		
		//返回结果前执行以下逻辑：先往月表填充数据，填充失败则检查定单号是否重复，是，则返回定单重复，否，则返回未知错误（需要系统处理）
		//填充成功后，往临时数据库中填充数据
		//最后往REDIS填充数据
		
		ToBusinessDataServer dataServer = new ToBusinessDataServer();
		
		String curMonth = StringUtil.getMonthFormat();
		
		orderModel.setId(dataServer.recordCpOrder(orderModel,curMonth));
		
		if(orderModel.getId()<=0)
		{
			//定单号重复
			if(dataServer.queryCpOrder(orderModel,curMonth)>0)
			{
				setResponseStatus(response, FlowConstant.TO_B_REQUEST_ORDER_REPEAT); 
			}
			else//未知错误
			{
				setResponseStatus(response, FlowConstant.TO_B_REQUEST_UN_KNOW_ERROR); 
			}
			return response;
		}
		
		//获取CP定单存储在临时表中的ID,防止以后更新定单的时候需要用到这个ID，暂不进行检查是否存储成功
		int cpOrderTempId = dataServer.saveCpQueryToTempTable(orderModel,curMonth);
		
		RedisToBusinessCpOrderModel redisCpOrderModel = new RedisToBusinessCpOrderModel();
		redisCpOrderModel.setTempTableId(cpOrderTempId);
		redisCpOrderModel.setMonthTableId(orderModel.getId());
		redisCpOrderModel.setMonthName(curMonth);
		redisCpOrderModel.setCpId(orderModel.getCpId());
		redisCpOrderModel.setOrderId(orderModel.getOrderId());
		redisCpOrderModel.setListSize(orderModel.getOrderList().size());
		
		RedisServer.setCpOrder(redisCpOrderModel);
		
		setResponseStatus(response,FlowConstant.TO_B_REQUEST_SUCCESS);
		logger.info("end handle cp[" + realCpId + "] order[" + orderId + "]'s order ");
		
		ToBusinessChildTransferServer childServer = new ToBusinessChildTransferServer();
		childServer.setChildList(orderModel.getOrderList());
		childServer.setCpId(realCpId);
		new Thread(childServer).start();
		
		logger.info("start handle cp[" + realCpId + "] order[" + orderId + "]'s child list");
		
		return response;
	}
	
	/**
	 * 从JSONARRAY中获取CP订单的子订单
	 * @param ja
	 * @return
	 */
	private static List<ToBusinessChildOrderModel> getChildOrderListFromJson(JSONObject joParent)
	{
		List<ToBusinessChildOrderModel> list = new ArrayList<ToBusinessChildOrderModel>();
		
		JSONArray ja = null;
		try
		{
			ja = joParent.getJSONArray("orderList");
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		
		if(ja==null || ja.size()<=0)
			return list;
		
		for(int i=0; i<ja.size(); i++)
		{
			JSONObject jo = ja.getJSONObject(i);
			try
			{
				ToBusinessChildOrderModel model = new ToBusinessChildOrderModel();
				
				model.setFlowSize(jo.getInt("flowSize"));
				model.setMobile(StringUtil.getString(jo.getString("mobile"),""));
				model.setRang(jo.getInt("rang"));
				model.setSubOrderId(StringUtil.getString(jo.getString("subOrderId"),""));
				
				list.add(model);
			}
			catch(Exception ex)
			{
				logger.error("getChildOrderListFromJson error source (" + jo.toString() + ")");
				logger.error(ex.getMessage());
			}
		}
		
		
		return list;
	}
	
	
	private static void setResponseStatus(BusinessResponModel response,int resultCode)
	{
		response.setResultCode(resultCode);
		
		response.setResultMsg(SysConfigCache.loadResultCodeByFlag(response.getResultCode()).getCodeName());
		
		logger.info("return result code:" + resultCode  + "-->msg:" + response.getResultMsg() + "-->orderId:" + response.getOrderId());
	}
}
