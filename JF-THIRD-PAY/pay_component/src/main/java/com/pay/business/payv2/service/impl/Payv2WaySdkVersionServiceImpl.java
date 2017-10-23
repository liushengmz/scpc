package com.pay.business.payv2.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payv2.entity.Payv2WaySdkVersion;
import com.pay.business.payv2.mapper.Payv2WaySdkVersionMapper;
import com.pay.business.payv2.service.Payv2WaySdkVersionService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.mapper.Payv2PayWayMapper;

/**
 * @author cyl
 * @version 
 */
@Service("payv2WaySdkVersionService")
public class Payv2WaySdkVersionServiceImpl extends BaseServiceImpl<Payv2WaySdkVersion, Payv2WaySdkVersionMapper> implements Payv2WaySdkVersionService {
	// 注入当前dao对象
    @Autowired
    private Payv2WaySdkVersionMapper payv2WaySdkVersionMapper;
    @Autowired
    private Payv2PayWayMapper payv2PayWayMapper;

    public Payv2WaySdkVersionServiceImpl() {
        setMapperClass(Payv2WaySdkVersionMapper.class, Payv2WaySdkVersion.class);
    }
    
 	public Payv2WaySdkVersion selectSingle(Payv2WaySdkVersion t) {
		return payv2WaySdkVersionMapper.selectSingle(t);
	}

	public List<Payv2WaySdkVersion> selectByObject(Payv2WaySdkVersion t) {
		return payv2WaySdkVersionMapper.selectByObject(t);
	}

	public PageObject<Payv2WaySdkVersion> payv2WaySdkVersionList(
			Map<String, Object> map) {
		int totalData = payv2WaySdkVersionMapper.getCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2WaySdkVersion> list = payv2WaySdkVersionMapper.pageQueryByObject(pageHelper.getMap());
		for(Payv2WaySdkVersion waysdk : list){
			Payv2PayWay way = new Payv2PayWay();
			way.setId(waysdk.getPayWayId());
			way = payv2PayWayMapper.selectSingle(way);
			if(way != null){
				waysdk.setPayWayName(way.getWayName());
			}
		}
		PageObject<Payv2WaySdkVersion> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	public Payv2WaySdkVersion getLastVersion(Map<String, Object> map) {
		return payv2WaySdkVersionMapper.getLastVersion(M2O(map));
	}
	
	
}
