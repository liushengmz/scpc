package com.pay.company.controller.offline;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.encrypt.MD5;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.payv2.entity.Payv2ProvincesCity;
import com.pay.business.payv2.service.Payv2ProvincesCityService;
import com.pay.business.util.ParameterEunm;
import com.pay.company.controller.admin.BaseManagerController;

@Controller
@RequestMapping("/offline/bussCompanyAccount/*")
public class Payv2BussCompanyAccountController extends BaseManagerController<Payv2BussCompany, Payv2BussCompanyMapper>{

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private Payv2ProvincesCityService payv2ProvincesCityService;
	
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	
	/**
	 * 修改密码
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("editBussCompanyPassword")
	@ResponseBody
	public Map<String,Object> editBussCompanyPassword(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"oldPassword","nowPassword","nowPasswordCry"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数错误,或者为空");
		}
		Payv2BussCompany admin = getAdmin();
		if(admin==null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		String oldPassword = map.get("oldPassword").toString();
		resultMap.put("id", admin.getId());
		try {
			Payv2BussCompany detail = payv2BussCompanyService.detail(resultMap);
			resultMap.clear();
			if(detail==null || !(detail.getPassWord().equals(admin.getPassWord())) || !(admin.getPassWord().equals(oldPassword))){
				return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"原密码输入错误");
			}
			String nowPassword = map.get("nowPassword").toString();//明文
			String nowPasswordCry = map.get("nowPasswordCry").toString();
			String md5Code = MD5.GetMD5Code(MD5.GetMD5Code(nowPassword));
			if(!nowPasswordCry.equals(md5Code)){
				return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"新密码设置错误");
			}
			Payv2BussCompany company = new Payv2BussCompany();
			company.setPassWord(nowPasswordCry);
			company.setId(detail.getId());
			payv2BussCompanyService.update(company);
			detail.setPassWord(nowPasswordCry);
			detail.setCurrentUserStatus(admin.getCurrentUserStatus());
			setAdmin(detail);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("商户后台-修改密码异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 账户信息
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("accountInfo")
	@ResponseBody
	public Map<String,Object> accountInfo(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		try {
			String companyRangeProvince = getCompanyRangeName(admin.getCompanyRangeProvince());
			admin.setCompanyRangeProvinceName(companyRangeProvince);
			String companyRangeCity = getCompanyRangeName(admin.getCompanyRangeCity());
			admin.setCompanyRangeCityName(companyRangeCity);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, admin);
		} catch (Exception e) {
			logger.error("获取线下账户信息异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		
		return resultMap;
	}
	
	/**
	 * 修改账户信息
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("editAccount")
	@ResponseBody
	public Map<String,Object> editAccount(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Payv2BussCompany admin = isOffLine();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		Long id = admin.getId();
		try {
			Payv2BussCompany m2o = payv2BussCompanyService.M2O(map);
			m2o.setId(id);
			payv2ProvincesCityService.update(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			logger.error("修改线下账户信息异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 获取省或者市的名称
	 * @param companyRange(只要是唯一ID 不分省还是市)
	 * @return
	 */
	public String getCompanyRangeName(Long companyRange){
		Map<String, Object> map = new HashMap<>();
		map.put("id",companyRange);
		Payv2ProvincesCity detail = payv2ProvincesCityService.detail(map);
		return detail.getName();
	}
}
