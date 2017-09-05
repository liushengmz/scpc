package com.pay.business.record.service.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.Payv2PayOrderRefund;
import com.pay.business.order.mapper.Payv2PayOrderMapper;
import com.pay.business.order.mapper.Payv2PayOrderRefundMapper;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.mapper.Payv2PayWayRateMapper;
import com.pay.business.record.entity.PayDataRecord;
import com.pay.business.record.entity.Payv2StatisticsDayCompanyTime;
import com.pay.business.record.mapper.PayDataRecordMapper;
import com.pay.business.record.service.PayDataRecordService;
import com.pay.business.util.ListUtils;

/**
* @Title: PayDataRecordServiceImpl.java 
* @Package com.pay.business.payv2.service.impl 
* @Description: 每小时统计数据实现类 
* @author ZHOULIBO   
* @date 2017年8月2日 上午10:06:17 
* @version V1.0
*/
@Service("payDataRecordService")
public class PayDataRecordServiceImpl extends BaseServiceImpl<PayDataRecord, PayDataRecordMapper> implements PayDataRecordService {
	// 注入当前dao对象
    @Autowired
    private PayDataRecordMapper payDataRecordMapper;
    @Autowired
    private Payv2PayOrderMapper payv2PayOrderMapper;//订单表
    @Autowired
    private Payv2PayOrderRefundMapper payv2PayOrderRefundMapper;//退款表
    @Autowired
    private Payv2PayWayRateMapper payv2PayWayRateMapper;//通道表
    public PayDataRecordServiceImpl() {
        setMapperClass(PayDataRecordMapper.class, PayDataRecord.class);
    }
    /**
     * 每小时去统计数据
     */
	public void setStatisticsOrderByHour(Map<String, Object> map) throws Exception {
		/**
		 * 成功
		 */
		// 获取订单表数据：按时间段获取
		map.put("payStatus", 1);
		List<Payv2PayOrder> orderList = payv2PayOrderMapper.selectOrderByHour(map);
		PayDataRecord payDataRecord = null;
		Payv2PayWayRate payv2PayWayRate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date recordTime = sdf.parse(map.get("timeBegin").toString());
		for (Payv2PayOrder payv2PayOrder : orderList) {
			payDataRecord = new PayDataRecord();
			payDataRecord.setAppId(payv2PayOrder.getAppId());
			payDataRecord.setChannelId(payv2PayOrder.getChannelId());
			payDataRecord.setCompanyId(payv2PayOrder.getCompanyId());
			payDataRecord.setCreateTime(sdf.parse(map.get("timeEnd").toString()));
			payDataRecord.setPayAmount(payv2PayOrder.getPayDiscountMoney());
			payDataRecord.setPayCount(payv2PayOrder.getOrderCount());
			// 交易净额=支付金额-退款金额：此处先进行支付金额直接添加进去；在查询退款的时候进行减法操作
			payDataRecord.setPayNet(payv2PayOrder.getPayDiscountMoney());
			// 手续费
			payDataRecord.setRateAmount(payv2PayOrder.getPayWayMoney());
			// 获取通道id
			Long rateId = payv2PayOrder.getRateId();
			if (rateId != null) {
				payv2PayWayRate = new Payv2PayWayRate();
				payv2PayWayRate.setId(rateId);
				// 查询通道信息
				payv2PayWayRate = payv2PayWayRateMapper.selectSingle(payv2PayWayRate);
				if (payv2PayWayRate != null) {
					// 成本手续费
					double rateCost = payv2PayOrder.getCostRateMoney();
					payDataRecord.setRateCost(rateCost);
					// 通道ID
					payDataRecord.setRateId(rateId);
					// 手续费利润：手续费-成本手续费
					double rateProfit = payv2PayOrder.getPayWayMoney() - rateCost;
					payDataRecord.setRateProfit(rateProfit);
					// 记录时间
					payDataRecord.setRecordTime(recordTime);
					// 平台id
					payDataRecord.setWayId(payv2PayOrder.getPayWayId());
					payDataRecordMapper.insertByEntity(payDataRecord);
					System.out.println("每小时统计交易对账插入成功");
				}
			}
		}
		/**
		 * 退款
		 */
		map.remove("payStatus");
		// 获取退款订单列表详情
		List<Payv2PayOrderRefund> orderRefundList = payv2PayOrderRefundMapper.selectRefundByHour(map);
		for (Payv2PayOrderRefund payv2PayOrderRefund : orderRefundList) {
			Long appId = payv2PayOrderRefund.getAppId();
			Long channelId = payv2PayOrderRefund.getChannelId();
			Long wayId = payv2PayOrderRefund.getPayWayId();
			Long rateId = payv2PayOrderRefund.getRateId();
			Long companyId = payv2PayOrderRefund.getCompanyId();
			payDataRecord = new PayDataRecord();
			payDataRecord.setAppId(appId);
			payDataRecord.setChannelId(channelId);
			payDataRecord.setWayId(wayId);
			payDataRecord.setRateId(rateId);
			payDataRecord.setCompanyId(companyId);
			payDataRecord.setRecordTime(recordTime);
			payDataRecord = payDataRecordMapper.selectSingle(payDataRecord);
			if (payDataRecord != null) {
				// 退款金额
				payDataRecord.setRefundAmount(payv2PayOrderRefund.getRefundMoney());
				// 退款订单数
				payDataRecord.setRefundCount(payv2PayOrderRefund.getRefundCount());
				// 修改 交易净额=支付金额-退款金额
				double payNet = payDataRecord.getPayAmount() - payv2PayOrderRefund.getRefundMoney();
				payDataRecord.setPayNet(payNet);
				// 修改
				payDataRecordMapper.updateByEntity(payDataRecord);
			} else {
				payDataRecord = new PayDataRecord();
				payDataRecord.setAppId(appId);
				payDataRecord.setChannelId(channelId);
				payDataRecord.setWayId(wayId);
				payDataRecord.setRateId(rateId);
				payDataRecord.setCompanyId(companyId);
				payDataRecord.setCreateTime(sdf.parse(map.get("timeEnd").toString()));
				// 记录时间
				payDataRecord.setRecordTime(recordTime);
				// 支付金额
				payDataRecord.setPayAmount(0.00);
				// 支付订单数
				payDataRecord.setPayCount(0);
				// 交易净额=支付金额-退款金额：此处先进行支付金额直接添加进去；在查询退款的时候进行减法操作
				payDataRecord.setPayNet(0.00);
				// 手续费
				payDataRecord.setRateAmount(0.00);
				// 成本手续费
				payDataRecord.setRateCost(0.00);
				// 手续费利润
				payDataRecord.setRateProfit(0.00);
				// 退款金额
				payDataRecord.setRefundAmount(payv2PayOrderRefund.getRefundMoney());
				// 退款订单数
				payDataRecord.setRefundCount(payv2PayOrderRefund.getRefundCount());
				// 插入
				payDataRecordMapper.insertByEntity(payDataRecord);
				System.out.println("每小时统计退款对账插入成功!");
			}
		}
		/**
		 * 未支付
		 */
		// 统计未支付的订单数与金额
		// 未支付状态
		map.put("payStatusNo", 3);
		List<Payv2PayOrder> orderNOList = payv2PayOrderMapper.selectOrderByHour(map);
		for (Payv2PayOrder payv2PayOrder : orderNOList) {
			Long appId = payv2PayOrder.getAppId();
			Long channelId = payv2PayOrder.getChannelId();
			Long wayId = payv2PayOrder.getPayWayId();
			Long rateId = payv2PayOrder.getRateId();
			Long companyId = payv2PayOrder.getCompanyId();
			payDataRecord = new PayDataRecord();
			payDataRecord.setAppId(appId);
			payDataRecord.setChannelId(channelId);
			payDataRecord.setWayId(wayId);
			payDataRecord.setRateId(rateId);
			payDataRecord.setCompanyId(companyId);
			payDataRecord.setRecordTime(recordTime);
			payDataRecord = payDataRecordMapper.selectSingle(payDataRecord);
			if (payDataRecord != null) {
				// 未支付金额
				payDataRecord.setNoPayAmount(payv2PayOrder.getPayMoney());
				// 未支付订单数
				payDataRecord.setNoPayCount(payv2PayOrder.getOrderCount());
				// 修改
				payDataRecordMapper.updateByEntity(payDataRecord);
			} else {
				payDataRecord = new PayDataRecord();
				payDataRecord.setAppId(appId);
				payDataRecord.setChannelId(channelId);
				payDataRecord.setWayId(wayId);
				payDataRecord.setRateId(rateId);
				payDataRecord.setCompanyId(companyId);
				payDataRecord.setCreateTime(sdf.parse(map.get("timeEnd").toString()));
				// 记录时间
				payDataRecord.setRecordTime(recordTime);
				// 未支付金额
				payDataRecord.setNoPayAmount(payv2PayOrder.getPayMoney());
				// 未支付订单数
				payDataRecord.setNoPayCount(payv2PayOrder.getOrderCount());
				// 插入
				payDataRecordMapper.insertByEntity(payDataRecord);
				System.out.println("每小时统计未支付对账插入成功!");
			}
		}
		System.out.println("每小时统计订单数据结束，统计时间为：" + map.get("timeEnd").toString());
	}
	
	public PayDataRecord curxData(Map<String, Object> map) {		
		PayDataRecord curxData = payDataRecordMapper.curxData(map);
		if(curxData == null){
			curxData = new PayDataRecord();
			curxData.setPayAmount(0.00);
			curxData.setPayCount(0);
			curxData.setRefundAmount(0.00);
			curxData.setRefundCount(0);
			curxData.setPayNet(0.00);
			curxData.setRateAmount(0.00);
		}else{
			if(curxData.getPayAmount() == null){
				curxData.setPayAmount(0.00);
			}
			if(curxData.getPayCount() == null){
				curxData.setPayCount(0);
			}
			if(curxData.getRefundAmount() == null){
				curxData.setRefundAmount(0.00);
			}
			if(curxData.getRefundCount() == null){
				curxData.setRefundCount(0);
			}
			if(curxData.getPayNet() == null){
				curxData.setPayNet(0.00);
			}
			if(curxData.getRateAmount() == null){
				curxData.setRateAmount(0.00);
			}
		}
		return curxData;
	}
	
	public PayDataRecord curxDataNow(Map<String, Object> map) {
		//查询到6个试试关键数据，并将数据放到数据统计对象中（PayDataRecord）
		Map<String, Object> successData = payv2PayOrderMapper.successData(map);
		Map<String, Object> failData = payv2PayOrderMapper.failData(map);
		PayDataRecord payDataRecord = new PayDataRecord();
		payDataRecord.setPayAmount(Double.parseDouble(successData.get("allSuccessMoney").toString()));//
		payDataRecord.setPayCount(Integer.valueOf(successData.get("allSuccessCount").toString()) );
		payDataRecord.setRefundAmount(Double.parseDouble(successData.get("allRefundMoney").toString()));
		payDataRecord.setRefundCount(Integer.valueOf(successData.get("allRefundCount").toString()));
		payDataRecord.setPayNet(Payv2DayCompanyClearServiceImpl.sub(payDataRecord.getPayAmount(), payDataRecord.getRefundAmount()));
		payDataRecord.setRateAmount(Double.parseDouble(successData.get("allPayWayMoney").toString()));
		payDataRecord.setNoPayAmount(Double.parseDouble(failData.get("allFailMoney").toString()));
		payDataRecord.setNoPayCount(Integer.valueOf(failData.get("allFailCount").toString()));
		return payDataRecord;
	}
	public List<PayDataRecord> groupByHour(Map<String, Object> map) throws ParseException {
		// 调用dao层接口，查询数据，如果dateType为1，查询实时，为2，查询统计
		List<PayDataRecord> resList = new  ArrayList<PayDataRecord>();
		Integer dateType = NumberUtils.createInteger(map.get("dateType").toString());
		if(dateType == 1){
			//　查询实时
			List<Map<String, Object>> nowList= payv2PayOrderMapper.groupByHour(map);
			for(Map<String, Object> nowDataMap : nowList) {
				PayDataRecord payDataRecord = new PayDataRecord();
				payDataRecord.setPayAmount(Double.parseDouble(nowDataMap.get("allSuccessMoney").toString()));
				payDataRecord.setPayCount(Integer.valueOf(nowDataMap.get("allSuccessCount").toString()));
				payDataRecord.setRecordTime(DateUtil.StringToDate(nowDataMap.get("payTime").toString(), DateStyle.YYYY_MM_DD));
				resList.add(payDataRecord);
			}
		}else{
			resList = payDataRecordMapper.groupByHour(map);
		}
		// 判断返回结果是否为空，如果为空，为空添加模拟数据
		PayDataRecord payDataRecord = null;
		Date recordTime = null;
		if("1".equals(map.get("dateType").toString())){
			recordTime = new Date();
		}else{
			recordTime = DateUtil.addDay(new Date(), -1);
		}
		if (resList == null || resList.size() == 0) {
			for (int i = 0; i < 24; i++) {
				resList.add(setHourDataRecord(payDataRecord,0.00,0,recordTime,i));
			}
		}
		// 不为空，判断返回结果是否有24条，若不足，添加模拟数据
		// 将resList中的统计时间的小时拿出来，如果不包含当前时刻，添加新的对象
		if(resList.size() < 24){			
			int resultSize = resList.size();
			// 先将res的所有日期存入map , 然后就不用每次都去获取每个对象
			Map<String, String> tempMap = new HashMap<>();
			for (int i = 0; i < resultSize; i++) {
				String hour = resList.get(i).getRecordTime().getHours()+"";
				tempMap.put(hour, "");
			}			
			for (int i = 0; i < 24; i++) {
				String hour = i+"";
				if(!tempMap.containsKey(hour)){
					resList.add(setHourDataRecord(payDataRecord,0.00,0,recordTime,i));
				}				
			}
		}
		ListUtils.sort(resList, true, false, "recordTime");
		return resList;
	}
	
	public List<PayDataRecord> groupByDay(Map<String, Object> map) throws ParseException {
		// 调用dao层接口，查询数据，如果为2，3，4查询统计数据		
		List<PayDataRecord> resList = new  ArrayList<PayDataRecord>();
		Integer dateType = NumberUtils.createInteger(map.get("dateType").toString());
		if(dateType == 2||dateType == 3||dateType ==4){
			resList= payDataRecordMapper.groupByDay(map);
		}else if(dateType == 5){
			// 如果为5，判断时间的结束日期是否小于当前日期(精确到小时)，如果小于查统计 ,否则查实时
			Boolean bool = comparDate(map.get("endTime").toString());
			if(bool){
				//查统计
				resList= payDataRecordMapper.groupByDay(map);
			}else{
				//查实时
				List<Map<String, Object>> nowList= payv2PayOrderMapper.groupByDay(map);
				for(Map<String, Object> nowDataMap : nowList) {
					PayDataRecord payDataRecord = new PayDataRecord();
					payDataRecord.setPayAmount(Double.parseDouble(nowDataMap.get("allSuccessMoney").toString()));
					payDataRecord.setPayCount(Integer.valueOf(nowDataMap.get("allSuccessCount").toString()));
					payDataRecord.setRecordTime(DateUtil.StringToDate(nowDataMap.get("payTime").toString(), DateStyle.YYYY_MM_DD));
					resList.add(payDataRecord);
				}
			}
		}
		// 判断返回结果是否为空，如果为空，为空添加模拟数据
		Integer queryType = NumberUtils.createInteger(map.get("dateType").toString());
		if (resList == null || resList.size() == 0) {
			PayDataRecord payDataRecord = null;
			if (queryType == 3 || queryType == 4) {
				int fos = queryType == 3 ? 7 : 30;
				Date date = new Date();
				for (int i = 1; i < fos; i++) {
					BigInteger b = new BigInteger(String.valueOf(i));
					Date addDay = DateUtil.addDay(date, b.negate().intValue());
					resList.add(setDayDataRecord(payDataRecord, 0.00, 0, addDay));
				}
			} else {
				String startTime = map.get("startTime").toString();
				String endTime = map.get("endTime").toString();
				startTime = startTime.substring(0, 10);
				endTime = endTime.substring(0, 10);
				// 自定义时间
				Date[] dateArrays = getDateArrays(DateUtil.StringToDate(startTime, DateStyle.YYYY_MM_DD),
						DateUtil.StringToDate(endTime, DateStyle.YYYY_MM_DD));
				for (Date date : dateArrays) {
					resList.add(setDayDataRecord(payDataRecord, 0.00, 0, date));
				}
			}
		}
		// 不为空，判断返回结果是否有足够的数据（7条，30条，自定义时间差的条目数），若不足，添加模拟数据
		int len = 0;
		Date[] dateArrays = null;
		if (queryType == 3 && resList.size() < 7) {
			len = 7;
		} else if (queryType == 4 && resList.size() < 30) {
			len = 30;
		} else if (queryType == 5) {
			String startTime = map.get("startTime").toString();
			String endTime = map.get("endTime").toString();
			startTime = startTime.substring(0, 10);
			endTime = endTime.substring(0, 10);
			// 自定义时间
			dateArrays = getDateArrays(DateUtil.StringToDate(startTime, DateStyle.YYYY_MM_DD), DateUtil.StringToDate(endTime, DateStyle.YYYY_MM_DD));
		if (resList.size() < dateArrays.length) {
				len = dateArrays.length;
			}
		}
		int resultSize = resList.size();
		// 先将res的所有日期存入map , 然后就不用每次都去获取每个对象
		Map<String, String> tempMap = new HashMap<>();
		for (int i = 0; i < resultSize; i++) {
			String trim = DateUtil.DateToString(resList.get(i).getRecordTime(),DateStyle.YYYY_MM_DD).trim();
			tempMap.put(trim, "");
		}
		if(len > 0){
			PayDataRecord payDataRecord = null;
			if (dateArrays != null) {
				for (Date dates : dateArrays) {
					String oldDate = DateUtil.DateToString(dates, DateStyle.YYYY_MM_DD).trim();
					if (!tempMap.containsKey(oldDate)) {
						// 对不存在的日期 进行添加
						resList.add(setDayDataRecord(payDataRecord, 0.00, 0, dates));
					}
				}
			}else{
				Date date = new Date();
				for (int i = 0; i < len; i++) {
					// 计算
					BigInteger b = new BigInteger(String.valueOf(i + 1));
					Date addDay = DateUtil.addDay(date, b.negate().intValue());
					String oldDate = DateUtil.DateToString(addDay, DateStyle.YYYY_MM_DD).trim();
					// 如果
					if (!tempMap.containsKey(oldDate)) {
						// 对不存在的日期 进行添加
						resList.add(setDayDataRecord(payDataRecord, 0.00, 0, addDay));
					}
				}			
			}
		} 
		ListUtils.sort(resList, true, true, "recordTime");
		return resList;
	}
	/**
	  * 判断一个日期与当前时间的先后，为true参数日期在前，false在后
	  * 
	 * @param date1
	 * @return boolean
	 */
	public static boolean comparDate(String date1) {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        Boolean bool = false;
	        try {
	            Date dt1 = df.parse(date1);
	            Date dt2 = new Date();
	            if (dt1.getTime() > dt2.getTime()) {
	            	bool =  false;
	            } else if (dt1.getTime() <= dt2.getTime()) {
	            	bool =  true;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return bool;
	    }
	public PayDataRecord setHourDataRecord(PayDataRecord payDataRecord,Double payAmount,Integer payCount,Date recordTime,int i) throws ParseException {
		payDataRecord = new PayDataRecord();
		payDataRecord.setPayAmount(payAmount);
		payDataRecord.setPayCount(payCount);		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String day = sdf.format(recordTime);
		String hour = "";
		if(i<10){
			hour = "0"+i+":00:00";
		}else{
			hour = i+":00:00";
		}
		String time = day + hour;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
		recordTime = sdf1.parse(time);
		payDataRecord.setRecordTime(recordTime);		 
		return payDataRecord;		
	}
	
	public PayDataRecord setDayDataRecord(PayDataRecord payDataRecord,Double payAmount,Integer payCount,Date recordTime){
		payDataRecord = new PayDataRecord();
		payDataRecord.setPayAmount(payAmount);
		payDataRecord.setPayCount(payCount);
		payDataRecord.setRecordTime(recordTime);		 
		return payDataRecord;		
	}
	
	public static Date[] getDateArrays(Date start, Date end) {
		return getDateArrays(start, end, Calendar.DAY_OF_YEAR);
	}
	
	public static Date[] getDateArrays(Date start, Date end, int calendarType) {
		ArrayList<Date> ret = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		Date tmpDate = calendar.getTime();
		long endTime = end.getTime();
		while (tmpDate.before(end) || tmpDate.getTime() == endTime) {
			ret.add(calendar.getTime());
			calendar.add(calendarType, 1);
			tmpDate = calendar.getTime();
		}
		Date[] dates = new Date[ret.size()];
		return ret.toArray(dates);
	}
	
}
