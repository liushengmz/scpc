package com.pay.channel.controller.payv2;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.payv2.entity.Payv2BussCompany;
import com.pay.business.payv2.entity.Payv2BussCompanyShop;
import com.pay.business.payv2.entity.Payv2BussTrade;
import com.pay.business.payv2.entity.Payv2Channel;
import com.pay.business.payv2.service.Payv2BussCompanyService;
import com.pay.business.payv2.service.Payv2BussCompanyShopService;
import com.pay.business.payv2.service.Payv2BussTradeService;
import com.pay.business.util.ParameterEunm;
import com.pay.channel.annotation.LoginValidate;
import com.pay.channel.controller.base.InterfaceBaseController;

/**
* @ClassName: Payv2BussCompanyController 
* @Description:商户控制
* @author mofan
* @date 2017年2月23日 上午9:21:52
*/
@Controller
@RequestMapping("/payv2BussCompany/*")
public class Payv2BussCompanyController extends InterfaceBaseController{

	private static final Logger logger = Logger.getLogger(Payv2BussCompanyController.class);
	
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;
	@Autowired
	private Payv2BussTradeService payv2BussTradeService;
	
    /**
     * 扫一扫商户二维码
     * @param map
     * @return
     */
	@LoginValidate
    @ResponseBody
    @RequestMapping("/scanQRcode")
    public Map<String,Object> scanQRcode(@RequestParam Map<String, Object> map,HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	try {
    		//1.获取shopkey
    		String urlString = map.get("qrRequestUrl").toString();
    		boolean isNotNull = ObjectUtil.checkObject(new String[] {
    				"qrRequestUrl"}, map);
    		int exist = urlString.indexOf("?shopKey");
    		if(!isNotNull || exist == -1){
    			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE, null);
    			return resultMap;
    		}
    		String shopKey = urlString.substring(urlString.indexOf("=")+1,urlString.length());
    		
    		if (shopKey != null && shopKey.length() > 0) {
    			//2.获取店铺信息，判断店铺是否已经绑定商户； 如果已经绑定返回绑定数据, 如果没有绑定跳转绑定页面
    			Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
    			payv2BussCompanyShop.setShopKey(shopKey);
    			payv2BussCompanyShop = payv2BussCompanyShopService.selectSingle(payv2BussCompanyShop);
				if (payv2BussCompanyShop != null) {
					
					initTrade(resultMap);//初始化行业信息
					if(payv2BussCompanyShop.getCompanyId() != null && payv2BussCompanyShop.getShopStatus().intValue() == 2){//审核通过
						Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
						payv2BussCompany.setId(payv2BussCompanyShop.getCompanyId());
						payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
						if(payv2BussCompany != null){
							if(payv2BussCompany.getCompanyStatus().intValue() == 1){//未审核
								resultMap.put("turnType", 3);//未审核，跳出提示，不能录入
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
							}else if(payv2BussCompany.getCompanyStatus().intValue() == 2){//审核通过
								initReturnPara(resultMap,payv2BussCompany);
								resultMap.put("turnType", 1);//编辑商户
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
								
							}else if(payv2BussCompany.getCompanyStatus().intValue() == 3){//审核未通过
								resultMap.put("turnType", 4);//审核不通过，重新录入
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
							}else{
								//终止
							}
						}else{
							resultMap.put("turnType", 1);//编辑商户
							resultMap = ReMessage.resultBack(ParameterEunm.COMPANY_NOT_EXIST, resultMap);
						}
						
					}else{
						resultMap.put("turnType", 2);//绑定商户
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
					}

				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.SHOP_NOT_EXIST, null);
				}
	    		
    		}else{
    			logger.info("渠道商商户扫一扫异常........");
    			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
    			
    		}
		} catch (Exception e) {
			logger.error("渠道商商户扫一扫异常.......");
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
	
	/**
	 * 初始化行业信息
	 * @param resultMap
	 * @param payv2BussCompany
	 */
	public void initTrade(Map<String,Object> resultMap){
		Payv2BussTrade bussTrade = new Payv2BussTrade();
		bussTrade.setParentId(-1l);
		List<Payv2BussTrade> trList = payv2BussTradeService.selectBySort(bussTrade);
		for(Payv2BussTrade trade : trList){
			long parentId = trade.getId();
			Payv2BussTrade reqTrade = new Payv2BussTrade();
			reqTrade.setParentId(parentId);
			trade.setList(payv2BussTradeService.selectByObject(reqTrade));
		}
		resultMap.put("tradeList", trList);//行业列表 
	}
    
	/**
	 * 初始化
	 * @param resultMap
	 * @param payv2BussCompany
	 */
	public void initReturnPara(Map<String,Object> resultMap,Payv2BussCompany payv2BussCompany){
		resultMap.put("contactsName", payv2BussCompany.getContactsName());//联系人名称
		resultMap.put("contactsPhone", payv2BussCompany.getContactsPhone());//联系人手机号
		resultMap.put("accountCard", payv2BussCompany.getAccountCard());//收款账号（卡号）
		resultMap.put("wechatAccountCard", payv2BussCompany.getWechatAccountCard());//微信账号
		resultMap.put("alipayAccountCard", payv2BussCompany.getAlipayAccountCard());//支付宝收款账号
		resultMap.put("accountBank", payv2BussCompany.getAccountBank());//开户银行
		resultMap.put("companyName", payv2BussCompany.getCompanyName());//公司名称
		resultMap.put("companyTrade", payv2BussCompany.getCompanyTrade());//公司行业 
		resultMap.put("companyTradeVar", payv2BussCompany.getCompanyTradeVar());//公司行业字符串
		resultMap.put("companyId", payv2BussCompany.getId());//公司行业 
		resultMap.put("companyAddr", payv2BussCompany.getCompanyAddr());//公司所在区域地址
		resultMap.put("licenseAddr", payv2BussCompany.getLicenseAddr());//营业执照注册地址
		resultMap.put("legalName", payv2BussCompany.getLegalName());//法人姓名
		resultMap.put("legalIdCard", payv2BussCompany.getLegalIdCard());//法人身份证号
		resultMap.put("licenseNum", payv2BussCompany.getLicenseNum());//营业执照号
		resultMap.put("licensePic", payv2BussCompany.getLicensePic());//执业执照
		resultMap.put("organizationCode", payv2BussCompany.getOrganizationCode());//组织机构代码
		resultMap.put("organizationCodeUrl", payv2BussCompany.getOrganizationCodeUrl());//组织机构代码照片
		resultMap.put("accountName", payv2BussCompany.getAccountName());//开户人姓名
		resultMap.put("otherInformation", payv2BussCompany.getOtherInformation());//开户人姓名
	}
	
    /**
     * 查询商户
     * @param map
     * @return
     */
	@LoginValidate
    @ResponseBody
    @RequestMapping("/selectBussCompany")
    public Map<String,Object> selectBussCompany(@RequestParam Map<String, Object> map,HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	try {
    		if(map.get("companyName") != null){
    			Payv2Channel channel = getSessionUser();
    			map.put("channelId", channel.getId());//渠道商ID
    			PageObject<Payv2BussCompany> list = payv2BussCompanyService.selectBussCompanyByNameLike(map);
    			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, list);
    		}else{
    			logger.error("渠道商商户查询异常.......查询条件不能为空,请重新输入！.............");
    			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null,"查询条件不能为空,请重新输入！");
    		}
		} catch (Exception e) {
			logger.error("渠道商商户查询异常.......");
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
    
    /**
     * 录入商户
     * @param map
     * @return
     */
	@LoginValidate
    @ResponseBody
    @RequestMapping("/scanBussCompany")
    public Map<String,Object> scanBussCompany(@RequestParam Map<String, Object> map,HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	try {
    		if(map.get("qrRequestUrl") != null){
    			
    			map.put("createTime", new Date());
    			map.put("isDelete", "2");//未删除
    			Payv2Channel channel = getSessionUser();
    			map.put("channelId", channel.getId());//渠道商ID
    			payv2BussCompanyService.scan(map);
    			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
    		}else{
    			logger.error("渠道商录入商户异常.......参数不正确,请重新输入！");
    			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null,"参数不正确,请重新输入！");
    		}
		} catch (Exception e) {
			logger.error("渠道商录入商户异常.......");
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
    
    /**
     * 关联商户
     * @param map
     * @return
     */
	@LoginValidate
    @ResponseBody
    @RequestMapping("/connectedBussCompany")
    public Map<String,Object> connectedBussCompany(@RequestParam Map<String, Object> map,HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	try {
    		if(map.get("companyId") != null && map.get("qrRequestUrl") != null){
    			Payv2Channel channel = getSessionUser();
    			map.put("channelId", channel.getId());//渠道商ID
    			map.put("updateTime", new Date());
    			map.put("id", map.get("companyId").toString());
    			resultMap = payv2BussCompanyService.connected(map);
    		}else{
    			logger.error("渠道商关联商户异常.......商户参数错误！");
    			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null,"商户参数错误！");
    		}
    		
		} catch (Exception e) {
			logger.error("渠道商关联商户异常.......");
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }
    
    /**
     * 编辑商户信息提交
     * @param map
     * @return
     */
	@LoginValidate
    @ResponseBody
    @RequestMapping("/updateBussCompany")
    public Map<String,Object> updateBussCompany(@RequestParam Map<String, Object> map,HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	try {
    		if(map.get("companyId") != null){
    			map.put("id", map.get("companyId").toString());
    			map.put("updateTime", new Date());
    			payv2BussCompanyService.update(map);
    			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
    		}else{
    			logger.error("编辑商户信息提交异常.......参数不完整.......");
    			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE, null);
    		}
		} catch (Exception e) {
			logger.error("编辑商户信息提交异常.......");
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
    	return resultMap;
    }

}
