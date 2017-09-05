package com.pay.business.payv2.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.cache.annotations.Cacheable;
import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.payv2.entity.Payv2PayUser;
import com.pay.business.payv2.mapper.Payv2PayUserMapper;
import com.pay.business.payv2.service.Payv2PayUserService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2PayUserService")
public class Payv2PayUserServiceImpl extends BaseServiceImpl<Payv2PayUser, Payv2PayUserMapper> implements Payv2PayUserService {
	// 注入当前dao对象
    @Autowired
    private Payv2PayUserMapper payv2PayUserMapper;

    public Payv2PayUserServiceImpl() {
        setMapperClass(Payv2PayUserMapper.class, Payv2PayUser.class);
    }
    
 	public Payv2PayUser selectSingle(Payv2PayUser t) {
		return payv2PayUserMapper.selectSingle(t);
	}

	public List<Payv2PayUser> selectByObject(Payv2PayUser t) {
		return payv2PayUserMapper.selectByObject(t);
	}

	//根据用户唯一标识判断是否存在 不存在，就新增
	public Payv2PayUser selectByToken(String token,Long appId) {
		Payv2PayUser ppu = new Payv2PayUser();
		ppu.setAppId(appId);
		ppu.setUserDeviceToken(token);
		//先查询
		ppu = payv2PayUserMapper.selectSingle(ppu);
		if(ppu==null){
			Payv2PayUser ppu1 = new Payv2PayUser();
			ppu1.setAppId(appId);
			ppu1.setUserDeviceToken(token);
			ppu1.setCreateTime(new Date());
			//插入，sql语句是如果存在就不插入再进行查询（并发情况）
			payv2PayUserMapper.insertBySelect(ppu1);
			if(ppu1.getId()==null){
				ppu1.setCreateTime(null);
				ppu = payv2PayUserMapper.selectSingle(ppu1);
			}else{
				return ppu1;
			}
		}
		return ppu;
	}

	/**
	 * 查询对象（缓存）
	 * @param t
	 * @return
	 */
	public Payv2PayUser selectSingle1(Payv2PayUser t) {
		return payv2PayUserMapper.selectSingle(t);
	}
}
