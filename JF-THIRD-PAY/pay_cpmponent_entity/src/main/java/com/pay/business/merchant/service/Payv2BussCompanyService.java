package com.pay.business.merchant.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;

/**
 * @author cyl
 * @version 
 */
public interface Payv2BussCompanyService extends BaseService<Payv2BussCompany,Payv2BussCompanyMapper>  {

	public Payv2BussCompany selectSingle(Payv2BussCompany t);
	
	public List<Payv2BussCompany> selectByObject(Payv2BussCompany t);
	
	public List<Payv2BussCompany> selectForCompanyApp(Payv2BussCompany t);
	public List<Payv2BussCompany> selectForCompanyShop(Payv2BussCompany t);
	
	/**
	 * 商户列表
	 * @param map
	 * @return
	 */
	public PageObject<Payv2BussCompany> companyList(Map<String,Object> map);
	
	public Payv2BussCompany getpayv2BussCompanyInfo(Payv2BussCompany payv2BussCompany);

	/**
	 * 查看用户是否存在
	 * @param userName
	 * @return
	 */
	public int getCountByUserName(String userName);

	/**
	 * 登录
	 * @param userName
	 * @param password 经过md5加密后的
	 * @return
	 */
	public Payv2BussCompany login(String userName, String password);
	
	/**
	 * 渠道商商户查询 根据渠道商名称或者了联系人姓名模糊查询
	 * @param t
	 * @return
	 */
	public PageObject<Payv2BussCompany> selectBussCompanyByNameLike(Map<String,Object> map);
	
	/**
	 * 录入商户
	 * @param map
	 */
	public Map<String,Object> scan(Map<String,Object> map);
	
	/**
	 * 关联商户
	 * @param map
	 */
	public Map<String, Object> connected(Map<String,Object> map);
	/**
	 * @Title: lockPayPassWord 
	 * @Description:累加商户支付密码错误次数
	 * @param @param companyId
	 * @param @param type
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public int lockPayPassWord(Long companyId,int type) throws Exception;
	/**
	 * @Title: lockNumber 
	 * @Description:查询支付密码错误次数 
	 * @param @param companyId
	 * @param @param type
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public int lockNumber(Long companyId, int type) throws Exception;
	/**
	 * 
	 * @Title: delKey 
	 * @Description: 删除redis记录
	 * @param @param companyId
	 * @param @param type
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void delKey(Long companyId, int type) throws Exception;
	
	/**
	 * 根据商户id查询（缓存）
	 * @param companyId
	 * @return
	 */
	public Payv2BussCompany getComById(Long id);
}
