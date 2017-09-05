package com.system.server;

import org.apache.log4j.Logger;

import com.system.cache.BaseDataCache;
import com.system.cache.SysConfigCache;
import com.system.constant.FlowConstant;
import com.system.model.CpModel;
import com.system.model.CpUserOrderResponseModel;
import com.system.util.Base64UTF;
import com.system.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 处理CP单个充值请求
 * @author Andy.Chen
 *
 */
public class SingleUserOrderServerV1
{
	
	/**
	 * 处理逻辑
	 * 1、检查用户的合法性
	 * 2、检查充值数据的合法性
	 * 3、检查tbl_f_cp_ratio中折扣的准确性，如果不存在则没有合作
	 * 4、检查tbl_f_cp_trone表中配置的SP通道，如果没有对应的SP通道，则进行自由查找模式
	 * 5、进入自由查找模式，规则是：查找上游所有的合适的通道，并选择利润最大的通道进行充值
	 * 6、存储所有数据，更新REDIS，并通知充值
	 */
	
	private static Logger logger = Logger.getLogger(ToBusinessRequestServerV1.class);
	
	public static CpUserOrderResponseModel handleUserOrder(String queryData,String ip)
	{
		CpUserOrderResponseModel response = new CpUserOrderResponseModel();
		
		String oriJsonData = Base64UTF.decode(queryData);
		
		if(StringUtil.isNullOrEmpty(oriJsonData))
		{
			setResponseStatus(response,FlowConstant.CP_SINGLE_ORDER_REQUEST_PARAM_ERROR);
			return response;
		}
		
		JSONObject joParam = JSONObject.fromObject(oriJsonData);
		
		if(joParam==null)
		{
			setResponseStatus(response,FlowConstant.CP_SINGLE_ORDER_REQUEST_PARAM_ERROR);
			return response;
		}
		
		//验签方式  md5(CP方的CPID + orderId + 我方提供的加密KEY) 
		int cpId = joParam.getInt("cpId");
		String orderId = joParam.getString("orderId");
		String sign = joParam.getString("sign");
		
		if(cpId<=0 || StringUtil.isNullOrEmpty(orderId) || StringUtil.isNullOrEmpty(sign))
		{
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_LACK_PARAMS);
			return response;
		}
		
		response.setOrderId(orderId);
		
		int realCpId = cpId - FlowConstant.FLOW_SYS_CP_CODE_BASE_COUNT;
		
		CpModel cpModel = BaseDataCache.loadCpById(realCpId);
		
		if(cpModel==null)
		{
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_NOT_COMPANY);
			return response;
		}
		
		//这里为什么用cpId而不是用realCpId是因为CP方拿到的是我们加过数的CPID
		String wSign = StringUtil.getMd5String(cpId + orderId + cpModel.getSignKey(), 32);
		
		if(!sign.equalsIgnoreCase(wSign))
		{
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_SIGN_ERROR);
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
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_IP_AUTH_FAIL);
			return response;
		}
		
		//以上就是检查用户的合法性
		
		String mobile = joParam.getString("mobile");
		int rang = joParam.getInt("rang");
		int flowSize = joParam.getInt("flowSize");

		if(StringUtil.isNullOrEmpty(mobile) || flowSize<=0 || rang>1)
		{
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_LACK_PARAMS);
			return response;
		}
		
		
		
		return response;
	}
	
	private static void setResponseStatus(CpUserOrderResponseModel response,int resultCode)
	{
		response.setResultCode(resultCode);
		
		response.setResultMsg(SysConfigCache.loadResultCodeByFlag(response.getResultCode()).getCodeName());
		
		logger.info("return result code:" + resultCode  + "-->msg:" + response.getResultMsg() + "-->orderId:" + response.getOrderId());
	}
}
