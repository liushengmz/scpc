package com.system.server;

import org.apache.log4j.Logger;

import com.system.cache.BaseDataCache;
import com.system.cache.LocateCache;
import com.system.cache.SysConfigCache;
import com.system.constant.FlowConstant;
import com.system.dao.SingleCpOrderDao;
import com.system.model.BasePriceModel;
import com.system.model.CpModel;
import com.system.model.CpRatioModel;
import com.system.model.CpTroneModel;
import com.system.model.CpUserOrderResponseModel;
import com.system.model.PhoneLocateModel;
import com.system.model.RedisCpSingleOrderModel;
import com.system.model.TroneModel;
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
		
		String serverOrderId = StringUtil.getMd5String(realCpId + "_" + orderId, 16).toUpperCase();
		
		boolean isClientOrderRepeat = RedisServer.existSingleCpOrder(serverOrderId);
		
		if(isClientOrderRepeat)
		{
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_ORDER_REPEAT);
			return response;
		}
		
		//以上就是检查用户的合法性
		
		String mobile = joParam.getString("mobile");
		int rang = joParam.getInt("rang");
		int flowSize = joParam.getInt("flowSize");
		int timeType = joParam.getInt("timeType");
		

		//flowSize 和 timeType 后面再来检查
		
		if(StringUtil.isNullOrEmpty(mobile) || flowSize<=0 || rang>1 )
		{
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_LACK_PARAMS);
			return response;
		}		
		
		if(mobile.length()!=11)
		{
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_MOBILE_ERROR);
			return response;
		}
		
		PhoneLocateModel phoneModel = LocateCache.getPhoneLocateModelByPhone(mobile.substring(0, 7));
		
		if(phoneModel==null)
		{
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_UN_KNOW_MOBILE);
			return response;
		}
		
		BasePriceModel basePrice = BaseDataCache.loadBasePrice(phoneModel.getOperator(), flowSize);
		
		if(basePrice==null)
		{
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_NOT_MATCH_FLOW_SIZE);
			return response;
		}
		
		//以上是检查数据的合法性
		
		CpRatioModel cpRatioModel = BaseDataCache.getCpRatio(realCpId, phoneModel.getOperator(), phoneModel.getProvinceId());
		
		if(cpRatioModel==null)
		{
			setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_UN_COMPANY_PROVINCE);
			return response;
		}
		
		//数据查找逻辑
		//1、根据 CPID PROVINCEID OPERATOR 等条件  查找已分配的 最优通道
		//2、如果 没有已分配的通道，根据关系去上游通道去查找最优通道
		
		CpTroneModel cpTroneModel = BaseDataCache.loadCpTrone(phoneModel.getProvinceId(),phoneModel.getOperator(),flowSize,rang,timeType);
		
		TroneModel troneModel = null;
		
		if(cpTroneModel==null)//没有在已配置的通道中找到则继续去未配置的通道里面找
		{
			troneModel = BaseDataCache.loadTroneModel(
					phoneModel.getProvinceId(), phoneModel.getOperator(),flowSize,rang,
					timeType, cpRatioModel.getRatio());
			if(troneModel==null)
			{
				setResponseStatus(response, FlowConstant.CP_SINGLE_ORDER_REQUEST_NO_SUITABLE_TRONE);
				return response;
			}
		}
		
		//如果这个不为空，则说明通过未配置的通道找到了，那么就要建立上下游的关系了
		if(troneModel!=null)
		{
			//如果通道不存在则增加通道
			if(!BaseDataCache.isExistCpTrone(realCpId, troneModel.getId()))
			{
				int cpTroneId =  CpServer.addCpTrone(realCpId, troneModel.getId(), cpRatioModel.getRatio());
				
				if(cpTroneId>0)
				{
					BaseDataCache.addCpTroneHideCache(cpTroneId, realCpId, troneModel.getId());
					
					cpTroneModel = BaseDataCache.getCpTroneModelById(cpTroneId);
				}
			}
		}
		
		//能跑到这里来说明 cpTroneModel 这个对象就不会为空的了，然后就要开始以下逻辑
		//1、检查CP的余额是否足够
		//2、写临时表定单、月表定单、REDIS数据缓存
		//3、通知上游进行充值	
		
		int cpRemainSum = BaseDataCache.getCpCurrency(realCpId);
		
		if(cpRemainSum < cpTroneModel.getPrice()*cpTroneModel.getCpRatio()/1000)
		{
			setResponseStatus(response,FlowConstant.CP_SINGLE_ORDER_REQUEST_NOT_SUFFICIENT_FUNDS);
			return response;
		}
		
		RedisCpSingleOrderModel redisModel = new RedisCpSingleOrderModel();
		
		redisModel.setBasePriceId(basePrice.getId());
		redisModel.setClientOrderId(orderId);
		redisModel.setCpId(realCpId);
		redisModel.setCpRatio(cpTroneModel.getCpRatio());
		redisModel.setCpTroneId(cpTroneModel.getId());
		redisModel.setFlowSize(flowSize);
		redisModel.setMobile(mobile);
		redisModel.setMonthName(StringUtil.getMonthFormat());
		redisModel.setOperator(phoneModel.getOperator());
		redisModel.setPrice(cpTroneModel.getPrice());
		redisModel.setRang(rang);
		redisModel.setSendSms(cpTroneModel.getSendSms());
		redisModel.setServerOrderId(serverOrderId);
		redisModel.setSpId(cpTroneModel.getSpId());
		redisModel.setSpRatio(cpTroneModel.getSpRatio());
		redisModel.setSpTroneId(cpTroneModel.getTroneId());
		redisModel.setTimeType(timeType);
		redisModel.setTroneId(cpTroneModel.getTroneId());
		redisModel.setStatus(1);
		
		SingleCpOrderDao dao = new SingleCpOrderDao();
		
		int monthTableId = dao.addSingleCpOrderToMonthTable(redisModel);
		
		if(monthTableId<=0)
		{
			if(dao.isExistCpOrder(redisModel.getMonthName(), realCpId, orderId))
			{
				setResponseStatus(response,FlowConstant.CP_SINGLE_ORDER_REQUEST_ORDER_REPEAT);
				return response;
			}
			else
			{
				setResponseStatus(response,FlowConstant.CP_SINGLE_ORDER_REQUEST_UN_KNOW_ERROR);
				return response;
			}
		}
		
		redisModel.setMonthTableId(monthTableId);
		
		int tempTableId = dao.addSingleCpOrderToTempTable(redisModel);
		
		redisModel.setTempTableId(tempTableId);
		
		RedisServer.setSingleCpOrder(redisModel);
		
		setResponseStatus(response,FlowConstant.CP_SINGLE_ORDER_REQUEST_SUCCESS);
		
		return response;
	}
	
	
	private static void setResponseStatus(CpUserOrderResponseModel response,int resultCode)
	{
		response.setResultCode(resultCode);
		
		response.setResultMsg(SysConfigCache.loadResultCodeByFlag(response.getResultCode()).getCodeName());
		
		logger.info("return result code:" + resultCode  + "-->msg:" + response.getResultMsg() + "-->orderId:" + response.getOrderId());
	}
}
