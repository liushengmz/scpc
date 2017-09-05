package com.pay.business.merchant.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.merchant.entity.Payv2BussCompany;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussCompanyMapper extends BaseMapper<Payv2BussCompany>{

	public List<Payv2BussCompany> selectForCompanyApp(Payv2BussCompany t);
	public List<Payv2BussCompany> selectForCompanyShop(Payv2BussCompany t);
	
	/**
	 * 查询用户是否存在
	 * @param userName
	 * @return
	 */
	public int getCountByUserName(String userName);
	
	/**
	 * 登录
	 * @param userName
	 * @param password
	 * @return
	 */
	public Payv2BussCompany login(@Param("userName") String userName,@Param("password") String password);
	
	public Integer selectBussCompanyByNameLikeCount(Map<String, Object> map);
	/**
	 * 渠道商商户查询 根据渠道商名称或者了联系人姓名模糊查询
	 * @param t
	 * @return
	 */
	public List<Payv2BussCompany> selectBussCompanyByNameLike(Map<String,Object> map);
	public int getCountBySearch(Map<String, Object> map);
	public List<Payv2BussCompany> pageQueryByObjectBySearch(
			HashMap<String, Object> map);
}