package com.pay.business.record.mapper;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.record.entity.Payv2CompanyQuota;

/**
 * @author cyl
 * @version 
 */
public interface Payv2CompanyQuotaMapper extends BaseMapper<Payv2CompanyQuota>{
	
	/**
	 * 获取所属公司的商户支付通道
	 * @param payv2CompanyQuota
	 * @return
	 */
	List<Payv2CompanyQuota> selectPayv2CompanyQuotaForCompany(Map<String,Object> map);
	
	/**
	 * 新增（已存在不新增insert IGNORE）
	 * @param payv2CompanyQuota
	 */
	public void insertSelect(Payv2CompanyQuota payv2CompanyQuota);
	
	/**
	 * 修改通道总限额记录
	 * @param payv2CompanyQuota
	 */
	public void updateTotal(Payv2CompanyQuota payv2CompanyQuota);
	
	/**
	 * 查询每个通道总限额
	 * @return
	 */
	public List<Payv2CompanyQuota> selectTotal(Map<String,Object> map);

}