package com.pay.business.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.order.entity.Payv2PayOrderGroup;
import com.pay.business.order.mapper.Payv2PayOrderGroupMapper;
import com.pay.business.order.service.Payv2PayOrderGroupService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2PayOrderGroupService")
public class Payv2PayOrderGroupServiceImpl extends BaseServiceImpl<Payv2PayOrderGroup, Payv2PayOrderGroupMapper> implements Payv2PayOrderGroupService {
	// 注入当前dao对象
    @Autowired
    private Payv2PayOrderGroupMapper payv2PayOrderGroupMapper;

    public Payv2PayOrderGroupServiceImpl() {
        setMapperClass(Payv2PayOrderGroupMapper.class, Payv2PayOrderGroup.class);
    }

	public List<Payv2PayOrderGroup> getGroupList(Map<String,Object> map) {
		return payv2PayOrderGroupMapper.selectByObject2(map);
	}
	/**
	 * 订单分组获取ID
	 */
	@Override
	public Long getGroup(Map<String, Object> map, Payv2BussCompanyApp pbca) {
		if (!map.containsKey("groupValue")) {
			return null;
		}
		Payv2PayOrderGroup ppog = new Payv2PayOrderGroup();
		ppog.setGroupValue(map.get("groupValue").toString());
		ppog.setAppId(pbca.getId());
		ppog = payv2PayOrderGroupMapper.selectSingle(ppog);
		if (ppog == null) {
			ppog = new Payv2PayOrderGroup();
			ppog.setGroupValue(map.get("groupValue").toString());
			ppog.setAppId(pbca.getId());
			ppog.setCreateTime(new Date());
			payv2PayOrderGroupMapper.insertByEntity(ppog);

		}
		return ppog.getId();
	}
}
