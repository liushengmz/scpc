package com.pay.business.record.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.record.entity.Payv2BillDown;
import com.pay.business.record.mapper.Payv2BillDownMapper;
import com.pay.business.record.service.Payv2BillDownService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2BillDownService")
public class Payv2BillDownServiceImpl extends BaseServiceImpl<Payv2BillDown, Payv2BillDownMapper> implements Payv2BillDownService {
	// 注入当前dao对象
    @Autowired
    private Payv2BillDownMapper payv2BillDownMapper;

    public Payv2BillDownServiceImpl() {
        setMapperClass(Payv2BillDownMapper.class, Payv2BillDown.class);
    }

    /**
	 * 查询对账文件下载地址
	 * @param billTime
	 * @param appId
	 * @return
	 */
	public String billDownUrl(String billTime, Long appId) {
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("clearTime", billTime);
		paramMap.put("appId", appId);
		Payv2BillDown payv2BillDown = payv2BillDownMapper.selectByAppId(paramMap);
		if(payv2BillDown==null){
			return "";
		}
		return payv2BillDown.getUrl();
	}
    
 
}
