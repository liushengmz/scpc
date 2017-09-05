package com.pay.business.record.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.payway.mapper.Payv2PayWayRateMapper;
import com.pay.business.record.entity.Payv2BussWayDetail;
import com.pay.business.record.mapper.Payv2BussWayDetailMapper;
import com.pay.business.record.service.Payv2BussWayDetailService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2BussWayDetailService")
public class Payv2BussWayDetailServiceImpl extends BaseServiceImpl<Payv2BussWayDetail, Payv2BussWayDetailMapper> implements Payv2BussWayDetailService {
	// 注入当前dao对象
    @Autowired
    private Payv2BussWayDetailMapper payv2BussWayDetailMapper;
    
    @Autowired
    private Payv2PayWayRateMapper payv2PayWayRateMapper;
    
    @Autowired
    private Payv2BussCompanyMapper payv2BussCompanyMapper;

    public Payv2BussWayDetailServiceImpl() {
        setMapperClass(Payv2BussWayDetailMapper.class, Payv2BussWayDetail.class);
    }

	
	public Map<Long, Payv2BussWayDetail> mouthBussWayDetaiList(Map<String, Object> map) {
		
		List<Payv2BussWayDetail> bussDetailList =  payv2BussWayDetailMapper.bussWayDetaiList(map);
		
		//商户对应的金额数据
		Map<Long, Payv2BussWayDetail> map2=new HashMap<Long, Payv2BussWayDetail>();
		
		for(Payv2BussWayDetail payv2BussWayDetail : bussDetailList){
			if(map2.containsKey(payv2BussWayDetail.getPayWayId())){
				//将个金额字段相加
				Payv2BussWayDetail allBussWayDetail=map2.get(payv2BussWayDetail.getPayWayId());
				allBussWayDetail.setSuccessCount(allBussWayDetail.getSuccessCount()+payv2BussWayDetail.getSuccessCount());//订单交易笔数
				allBussWayDetail.setBussMoney(allBussWayDetail.getBussMoney()+payv2BussWayDetail.getBussMoney());//交易支付净额
				allBussWayDetail.setSuccessMoney(allBussWayDetail.getSuccessMoney()+payv2BussWayDetail.getSuccessMoney());//订单交易金额
				if(payv2BussWayDetail.getRefundCount()!=null){
					allBussWayDetail.setRefundCount(allBussWayDetail.getRefundCount()+payv2BussWayDetail.getRefundCount());//退款笔数
				}				
				if(payv2BussWayDetail.getRefundMoney()!=null){
					allBussWayDetail.setRefundMoney(allBussWayDetail.getRefundMoney()+payv2BussWayDetail.getRefundMoney());//退款金额
				}
				
				allBussWayDetail.setRateProfit(allBussWayDetail.getRateProfit()+(payv2BussWayDetail.getBussWayRateMoney()-payv2BussWayDetail.getCostRateMoney()));//手续费利润
				allBussWayDetail.setCostRateMoney(allBussWayDetail.getCostRateMoney()+payv2BussWayDetail.getCostRateMoney());//成本手续费
				allBussWayDetail.setBussWayRateMoney(allBussWayDetail.getBussWayRateMoney()+payv2BussWayDetail.getBussWayRateMoney());//商户手续费
				allBussWayDetail.setAccountsMonry(allBussWayDetail.getAccountsMonry()+(payv2BussWayDetail.getBussMoney()-payv2BussWayDetail.getBussWayRateMoney()));//结算金额
			}else{
				Payv2BussWayDetail allBussWayDetail = new Payv2BussWayDetail();
				allBussWayDetail.setRateName(payv2BussWayDetail.getRateName());
				allBussWayDetail.setSuccessCount(payv2BussWayDetail.getSuccessCount());//订单交易笔数
				allBussWayDetail.setSuccessMoney(payv2BussWayDetail.getSuccessMoney());//订单交易金额				
				if(payv2BussWayDetail.getRefundCount()!=null){
					allBussWayDetail.setRefundCount(payv2BussWayDetail.getRefundCount());//退款笔数
				}else{
					allBussWayDetail.setRefundCount(0);//退款笔数
				}
				
				if(payv2BussWayDetail.getRefundMoney()!=null){
					allBussWayDetail.setRefundMoney(payv2BussWayDetail.getRefundMoney());//退款金额
				}else{
					allBussWayDetail.setRefundMoney(0.00);
				}
				allBussWayDetail.setRateProfit(payv2BussWayDetail.getBussWayRateMoney()-payv2BussWayDetail.getCostRateMoney());//手续费利润
				allBussWayDetail.setBussMoney(payv2BussWayDetail.getBussMoney());//交易支付净额
				allBussWayDetail.setCostRate(payv2BussWayDetail.getCostRate());//成本手续费率
				allBussWayDetail.setCostRateMoney(payv2BussWayDetail.getCostRateMoney());//成本手续费
				allBussWayDetail.setBussWayRate(payv2BussWayDetail.getBussWayRate());//商户手续费率
				allBussWayDetail.setBussWayRateMoney(payv2BussWayDetail.getBussWayRateMoney());//商户手续费
				allBussWayDetail.setAccountsMonry(payv2BussWayDetail.getBussMoney()-payv2BussWayDetail.getBussWayRateMoney());//结算金额
				map2.put(payv2BussWayDetail.getPayWayId(), allBussWayDetail);
			}		

		}
		return map2;
	}


	
	public Map<String, Object> dayBussWayDetaiList(Map<String, Object> map) {
		Map<String, Object> map2 = new HashMap<String, Object>();
		List<Payv2BussWayDetail> bussDetailList =  payv2BussWayDetailMapper.bussWayDetaiList(map);
		
		Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
		for(Payv2BussWayDetail payv2BussWayDetail : bussDetailList){
			payv2BussWayDetail.setRateProfit(payv2BussWayDetail.getBussWayRateMoney()-payv2BussWayDetail.getCostRateMoney());
			payv2BussWayDetail.setAccountsMonry(payv2BussWayDetail.getBussMoney()-payv2BussWayDetail.getBussWayRateMoney());
		}
		if(map.get("companyId") != null){
			payv2BussCompany.setId(Long.parseLong((String) map.get("companyId")));
			payv2BussCompany = payv2BussCompanyMapper.selectSingle(payv2BussCompany);			
		}
		map2.put("bussDetailList", bussDetailList);
		map2.put("payv2BussCompany", payv2BussCompany);
		return map2;
	}


	
	public List<Payv2BussWayDetail> queryByCom(Map<String, Object> map) {
		List<Payv2BussWayDetail> bussDetailList =  payv2BussWayDetailMapper.bussWayDetaiList(map);
		
		return bussDetailList;
	}

 
}
