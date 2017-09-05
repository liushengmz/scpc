package com.pay.business.payv2.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payv2.entity.Payv2AppDiscount;
import com.pay.business.payv2.mapper.Payv2AppDiscountMapper;
import com.pay.business.payv2.service.Payv2AppDiscountService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2AppDiscountService")
public class Payv2AppDiscountServiceImpl extends BaseServiceImpl<Payv2AppDiscount, Payv2AppDiscountMapper> implements Payv2AppDiscountService {
	// 注入当前dao对象
    @Autowired
    private Payv2AppDiscountMapper payv2AppDiscountMapper;

    public Payv2AppDiscountServiceImpl() {
        setMapperClass(Payv2AppDiscountMapper.class, Payv2AppDiscount.class);
    }
    
 	public Payv2AppDiscount selectSingle(Payv2AppDiscount t) {
		return payv2AppDiscountMapper.selectSingle(t);
	}

	public List<Payv2AppDiscount> selectByObject(Payv2AppDiscount t) {
		return payv2AppDiscountMapper.selectByObject(t);
	}
	public PageObject<Payv2AppDiscount> getPageObject2(Map<String, Object> map){
		map.put("sortName", "create_time");
		map.put("sortOrder", "DESC");
		int totalData=payv2AppDiscountMapper.getCount2(map);
		PageHelper helper=new PageHelper(totalData,map);
		List<Payv2AppDiscount> orderList=	payv2AppDiscountMapper.pageQueryByObject2(helper.getMap());
		PageObject<Payv2AppDiscount> pageList=helper.getPageObject();
		pageList.setDataList(orderList);
		return pageList;
	}
}
