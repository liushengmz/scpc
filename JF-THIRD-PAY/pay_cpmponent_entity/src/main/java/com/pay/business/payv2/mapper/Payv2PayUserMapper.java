package com.pay.business.payv2.mapper;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.payv2.entity.Payv2PayUser;

/**
 * @author cyl
 * @version 
 */
public interface Payv2PayUserMapper extends BaseMapper<Payv2PayUser>{
	public void insertBySelect(Payv2PayUser ppu);
}