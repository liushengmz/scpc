package com.pay.business.payv2.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.mapper.Payv2ChannelMapper;
import com.pay.business.payv2.entity.Payv2AgentPlatform;
import com.pay.business.payv2.entity.Payv2PlatformWay;
import com.pay.business.payv2.mapper.Payv2AgentPlatformMapper;
import com.pay.business.payv2.mapper.Payv2PlatformWayMapper;
import com.pay.business.payv2.service.Payv2AgentPlatformService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2AgentPlatformService")
public class Payv2AgentPlatformServiceImpl extends BaseServiceImpl<Payv2AgentPlatform, Payv2AgentPlatformMapper> implements Payv2AgentPlatformService {
	// 注入当前dao对象
    @Autowired
    private Payv2AgentPlatformMapper payv2AgentPlatformMapper;
    @Autowired
    private Payv2ChannelMapper payv2ChannelMapper;
    @Autowired
    private Payv2PlatformWayMapper payv2PlatformWayMapper;

    public Payv2AgentPlatformServiceImpl() {
        setMapperClass(Payv2AgentPlatformMapper.class, Payv2AgentPlatform.class);
    }
    
 	public Payv2AgentPlatform selectSingle(Payv2AgentPlatform t) {
		return payv2AgentPlatformMapper.selectSingle(t);
	}

	public List<Payv2AgentPlatform> selectByObject(Payv2AgentPlatform t) {
		return payv2AgentPlatformMapper.selectByObject(t);
	}

	public PageObject<Payv2AgentPlatform> getPayv2AgentPlatformList(
			Map<String, Object> map) {
		int totalData = payv2AgentPlatformMapper.getCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2AgentPlatform> list = payv2AgentPlatformMapper.pageQueryByObject(pageHelper.getMap());
		Payv2Channel payv2Channel = null;
		for(Payv2AgentPlatform agentPlatform : list){
			payv2Channel = new Payv2Channel();
			payv2Channel.setId(agentPlatform.getChannelId());
			payv2Channel = payv2ChannelMapper.selectSingle(payv2Channel);
			if(payv2Channel != null){
				agentPlatform.setChannelName(payv2Channel.getChannelName());
			}
			Payv2PlatformWay pway = new Payv2PlatformWay();
			pway.setPlatformId(agentPlatform.getId());
			List<Payv2PlatformWay> list2 = payv2PlatformWayMapper.selectByObject(pway);
			agentPlatform.setSupportPayWayNumber(list2.size());
		}
		PageObject<Payv2AgentPlatform> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}
	
	
}
