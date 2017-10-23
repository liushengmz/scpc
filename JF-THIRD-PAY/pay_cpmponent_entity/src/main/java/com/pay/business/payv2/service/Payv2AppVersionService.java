package com.pay.business.payv2.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.payv2.entity.Payv2AppVersion;
import com.pay.business.payv2.mapper.Payv2AppVersionMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2AppVersionService extends BaseService<Payv2AppVersion,Payv2AppVersionMapper>  {

	public Payv2AppVersion selectSingle(Payv2AppVersion t);
	
	public List<Payv2AppVersion> selectByObject(Payv2AppVersion t);
	
	/**
	 * 线上应用版本管理列表
	 * @param map
	 * @return
	 */
	public PageObject<Payv2AppVersion> payv2AppVersionList(Map<String, Object> map);
	
	
	/**
	 * 跳转上架页面
	 * @param map
	 * @return
	 */
	public Payv2AppVersion toUpOnSale(Map<String, Object> map);
	
	/**
	 * 上架
	 * @param map
	 */
	public void upOnSale(Map<String, Object> map);
	
	/**
	 * 跳转上架页面
	 * @param map
	 * @return
	 */
	public Payv2AppVersion toLoadDetailForDownload(Map<String, Object> map);
}
