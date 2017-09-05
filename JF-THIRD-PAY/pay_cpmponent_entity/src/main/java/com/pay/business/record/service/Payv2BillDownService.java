package com.pay.business.record.service;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.record.entity.Payv2BillDown;
import com.pay.business.record.mapper.Payv2BillDownMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BillDownService extends BaseService<Payv2BillDown,Payv2BillDownMapper>  {
	/**
	 * 查询对账文件下载地址
	 * @param billTime
	 * @param appId
	 * @return
	 */
	public String billDownUrl(String billTime,Long appId);
}
