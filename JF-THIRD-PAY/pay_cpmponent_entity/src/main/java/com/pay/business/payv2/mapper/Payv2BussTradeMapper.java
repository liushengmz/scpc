package com.pay.business.payv2.mapper;

import java.util.List;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.payv2.entity.Payv2BussTrade;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussTradeMapper extends BaseMapper<Payv2BussTrade>{

	public List<Payv2BussTrade> selectBySort(Payv2BussTrade t);
}