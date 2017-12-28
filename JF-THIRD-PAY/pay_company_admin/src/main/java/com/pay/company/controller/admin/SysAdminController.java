package com.pay.company.controller.admin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
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
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.payv2.service.SysConfigSmsService;
import com.pay.business.util.ParameterEunm;

@Controller
@RequestMapping("/sys/*")
public class SysAdminController extends BaseManagerController<Payv2BussCompany, Payv2BussCompanyMapper>{
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	
	@Autowired
	private SysConfigSmsService configSmsService;// 发送短信表
	
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	
	private static final String IMG_CODE_SESSION = "code";//验证码session属性key
	
	private static final int SMS_TYPE_ID = 8; // 忘记密码-找回密码
	
	private static final int APP_SECRET_KEY_ID = 9; // 密钥找回的
	
	private static final int SMS_PROJECT_ID = 1;//项目ID
	
	
	/**
	 * 验证图片验证码
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("asynValidateImgCode")
	@ResponseBody
	public Map<String,Object> asynValidateImgCode(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"imgCode"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数错误,或不允许为空");
		}
		String imgCode = map.get("imgCode").toString();
		Object sessionCode = getSessionAttr(IMG_CODE_SESSION);
		if(sessionCode==null || !sessionCode.toString().equalsIgnoreCase(imgCode)){
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_SMS_CODE,null,"验证码错误或者session失效");
		}else{
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,null);
		}
		return resultMap;
	}
	
	/**
	 * 发送短信验证码
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("sendSmsCode")
	@ResponseBody
	public Map<String,Object> sendSmsCode(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"useType"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数错误,或不允许为空");
		}
		Integer useType = NumberUtils.createInteger(map.get("useType").toString());//使用类型 1为密钥验证 2为这里用的短信验证(忘记密码)
		
		try {
			Payv2BussCompany detail = null;
			if(useType==1){
				detail = getAdmin();
			}else if(useType==2){
				String userName = map.get("userName").toString();
				resultMap.put("userName",userName);
				detail = payv2BussCompanyService.detail(resultMap);
				resultMap.clear();
			}else{
				return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数错误,或不允许为空");
			}
			if(detail == null || detail.getIsDelete()==1){
				return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"该商户不存在,或已被删除");
			}
			int status = 0;
			if(useType==1){
				status = configSmsService.createCode(detail.getUserName(), detail.getId(),APP_SECRET_KEY_ID,SMS_PROJECT_ID);
			}else{
				status = configSmsService.createCode(detail.getUserName(), detail.getId(),SMS_TYPE_ID,SMS_PROJECT_ID);	
			}
			if (status == 0) {
				// 发送成功
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} else if (status == 1 || status == 2 || status == 3) {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null,"错误次数超限，锁定60秒");
			} else if (status >= 4) {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"错误次数超限，锁定600秒");
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"短信验证码发送失败,请稍候重试");
			}
		} catch (Exception e) {
			logger.error("商户后台-发送短信验证码异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null,"服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 验证短信验证码
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("asynValidateSmsCode")
	@ResponseBody
	public Map<String,Object> asynValidateSmsCode(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"smsCode","useType"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数错误,或不允许为空");
		}
		String smsCode = map.get("smsCode").toString();
		Integer useType = NumberUtils.createInteger(map.get("useType").toString());//使用类型 1为密钥验证 2为这里用的短信验证(忘记密码)
		try {
			if(useType==1){
				Payv2BussCompany admin = getAdmin();
				if(admin==null || admin.getLicenseType() == null){
            		resultMap = ReMessage.resultBack(ParameterEunm.USER_AND_PASSWORD_MISMATCH,null,"帐号和密码不匹配");
            	}else if(admin.getIsDelete() == 1 || admin.getCompanyStatus() == 4){
            		resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"对不起,该商户已被删除或停止合作,请联系工作人员");
            	}else{
            		String appId = map.get("appId").toString();
            		boolean isOk = configSmsService.checkSms(admin.getUserName(), admin.getId(),
    						APP_SECRET_KEY_ID,smsCode,SMS_PROJECT_ID);
            		if(!isOk){
    					//验证失败
    					resultMap = ReMessage.resultBack(ParameterEunm.ERROR_SMS_CODE,null,"验证码错误");
    				}else{
    					Map<String,Object> paramMap = new HashMap<>();
    					//生成新的密钥
    					String updateNewAppSecret = payv2BussCompanyAppService.updateNewAppSecret(NumberUtils.createLong(appId), admin.getId());
    					paramMap.put("newAppSecret", updateNewAppSecret);
    					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,paramMap);
    				}
            	}
			}else if(useType==2){
				String userName = map.get("userName").toString();
				resultMap.put("userName", userName);
				Payv2BussCompany detail = payv2BussCompanyService.detail(resultMap);
				resultMap.clear();
				//查询数据库 短信验证码
				boolean isOk = configSmsService.checkSms(detail.getUserName(), detail.getId(),
						SMS_TYPE_ID,smsCode,SMS_PROJECT_ID);
				if(!isOk){
					//验证失败
					resultMap = ReMessage.resultBack(ParameterEunm.ERROR_SMS_CODE,null,"验证码错误");
				}else{
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,null);
				}
			}else{
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数错误,或不允许为空");
			}
		}catch(Exception e){
			logger.error("商户后台-验证短信验证码异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null,"服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/**
	 * 忘记密码-修改密码
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("forgetPassWord")
	@ResponseBody
	public Map<String,Object> forgetPassWord(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"userName","imgCode","smsCode","nowPassword","nowPasswordCry"},map);
		if (!isNotNull) {
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数错误,或不允许为空");
		}
		String userName = map.get("userName").toString();
		String imgCode = map.get("imgCode").toString();
		String smsCode = map.get("smsCode").toString();
		String nowPassword = map.get("nowPassword").toString();
		String nowPasswordCry = map.get("nowPasswordCry").toString();
		resultMap.put("userName", userName);
		try {
			Payv2BussCompany detail = payv2BussCompanyService.detail(resultMap);
			resultMap.clear();
			if(detail == null || detail.getIsDelete()==1){
				return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"该商户不存在,或已被删除");
			}
			String md5Code = MD5.GetMD5Code(MD5.GetMD5Code(nowPassword));
			if(!nowPasswordCry.equals(md5Code)){
				return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"新密码设置错误");
			}
			if(!imgCode.equalsIgnoreCase(String.valueOf(getSessionAttr(IMG_CODE_SESSION)))){
				return resultMap = ReMessage.resultBack(ParameterEunm.ERROR_SMS_CODE,null,"图片验证码错误");
			}
			//查询数据库 短信验证码
			boolean isOk = configSmsService.checkSms(detail.getUserName(), detail.getId(),SMS_TYPE_ID,smsCode,SMS_PROJECT_ID);
			if(!isOk){
				//验证失败
				return resultMap = ReMessage.resultBack(ParameterEunm.ERROR_SMS_CODE,null,"短信验证码错误");
			}
			//更新密码
			Payv2BussCompany company = new Payv2BussCompany();
			company.setId(detail.getId());
			company.setPassWord(nowPasswordCry);
			payv2BussCompanyService.update(company);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,null);
		} catch (Exception e) {
			logger.error("商户后台-忘记密码异常:", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null,"服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	/*public String getFilePath(HttpServletRequest request){
		String imgDir = getImgDir(request.getSession().getServletContext().getRealPath("/"));
		mkDir(imgDir);
		String imgFilePath = getImgFilePath();
		String filePath = imgDir + File.separator + imgFilePath;
		return filePath;
	}
	
	public String getImgFilePath(){
		String id = getSession().getId();
		return id + IMG_CODE_SESSION + ".jpeg";
	}
	
	public String getImgDir(String basePath){
		return basePath + IMG_CODE_TEMP_DIR;
	}
	
	public void mkDir(String dirPath){
		File file = new File(dirPath);
		if(!file.exists()){
			file.mkdir();
		}
	}*/
	
	public void deleteFile(String filePath){
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**
	 * 验证手机号是否注册
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("asynValidatePhoneRegister")
	@ResponseBody
	public Map<String,Object> asynValidatePhoneRegister(@RequestParam Map<String,Object> map,HttpServletRequest request){
		String resultMsgNull = "参数不允许为空";
		String resultMsgError = "服务器异常,请稍后再试";
		String resultUserExist = "用户已经存在";
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"userName"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,resultMsgNull);
		}
		//查询账户是否存在
		String userName = map.get("userName").toString();
		try {
			int count = payv2BussCompanyService.getCountByUserName(userName);
			if(count>0){
				resultMap = ReMessage.resultBack(ParameterEunm.USER_EXIST,null,resultUserExist);
			}else{
				resultMap = ReMessage.resultBack(ParameterEunm.USER_NO_EXIST,null);
			}
		} catch (Exception e) {
			logger.error("支付集商户后台手机验证异常:"+map.toString(), e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null,resultMsgError);
		}
		return resultMap;
	}

	/**
	 * @Title: login
	 * @Description: 登录
	 * @param @param map
	 * @param @param request
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@RequestMapping("login")
	@ResponseBody
	public Map<String, Object> login(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		String resultMsgNull = "参数不允许为空";
		String resultMsgError = "服务器异常,请稍后再试";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "userName", "password" }, map);
		if (!isNotNull) {
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, resultMsgNull);
		}
		String userName = map.get("userName").toString();
		String password = map.get("password").toString();
		// 密码为md5,由前端加密后与数据库比对
		try {
			int count = payv2BussCompanyService.getCountByUserName(userName);
			if (count > 0) {
				Payv2BussCompany bussCompany = payv2BussCompanyService.login(userName, password);
				if (bussCompany == null || bussCompany.getLicenseType() == null) {
					resultMap = ReMessage.resultBack(ParameterEunm.USER_AND_PASSWORD_MISMATCH, null, "帐号和密码不匹配");
				} else if (bussCompany.getIsDelete() == 1 || bussCompany.getCompanyStatus() == 4) {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "对不起,该商户已被删除或停止合作,请联系工作人员");
				} else if (bussCompany.getCompanyStatus() == 1 || bussCompany.getCompanyStatus() == 3) {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "对不起,您的账户未审核 或 未审核通过,请联系工作人员");
				} else {
					// 用户存在
					// 如果商户类型为线上又是线下,那么先默认进入线上的,
					Map<String, Object> paramMap = new HashMap<>();
					Integer licenseType = bussCompany.getLicenseType();
					paramMap.put("companyName", bussCompany.getCompanyName());
					paramMap.put("email", bussCompany.getContactsMail());
					if (licenseType == 1) {// 线上
						bussCompany.setCurrentUserStatus(1);
						paramMap.put("isTabSwitch", 1); // 为1 代表不能切换
						paramMap.put("currentUserStatus", 1);// 为1是线上 代表进入线上页面
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, paramMap);// 存在
																									// 跳转
					} else if (licenseType == 2) {
						bussCompany.setCurrentUserStatus(2);
						paramMap.put("isTabSwitch", 1); // 为1 代表不能切换
						paramMap.put("currentUserStatus", 2);// 为2是线下 代表进入线下页面
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, paramMap);// 存在
																									// 跳转
					} else if (licenseType == 3) {
						bussCompany.setCurrentUserStatus(1);
						paramMap.put("isTabSwitch", 2); // 为2 代表可以切换
						paramMap.put("tabSwitchName", "线下应用");// 可以切换 指的切换的名字
						paramMap.put("currentUserStatus", 1);// 为1是线上 代表进入线上页面
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, paramMap);// 存在
																									// 跳转
					} else {
						return resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null, resultMsgError);
					}
					/*
					 * String filePath = getFilePath(request);
					 * deleteFile(filePath);
					 */
					
					HttpSession andySession = request.getSession();
					andySession.setAttribute("userName", userName);
					andySession.setAttribute("bussCompanyId", bussCompany.getId());
					
					setAdmin(bussCompany);
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.USER_NO_EXIST, null, "用户不存在");// 用户不存在
			}
		} catch (Exception e) {
			logger.error("支付集商户后台登录异常:" + map.toString(), e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null, resultMsgError);
		}
		return resultMap;
	}

	/**
	 * @Title: loginOut
	 * @Description: 退出登录
	 * @param @param map
	 * @param @param request
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@RequestMapping("loginOut")
	@ResponseBody
	public Map<String, Object> loginOut(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Payv2BussCompany admin = getAdmin();
			if (admin == null) {
				return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN, null, "用户Session不存在或失效,请重新登录");
			}
			HttpSession session = getSession();
			session.setAttribute("admin", null);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null, "服务器正在裸奔,请稍候重试");
		}
		return resultMap;
	}
	
	@RequestMapping("tabSwitch")
	@ResponseBody
	public Map<String, Object> tabSwitch(@RequestParam Map<String, Object> map,HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<>();
		Payv2BussCompany admin = getAdmin();
		if(admin == null){
			return resultMap = ReMessage.resultBack(ParameterEunm.NOT_LOGIN,null,"用户Session不存在或失效,请重新登录");
		}
		Integer licenseType = admin.getLicenseType();
		Integer currentUserStatus = admin.getCurrentUserStatus();
		Map<String,Object> paramMap = new HashMap<>();
		if(currentUserStatus==1 && licenseType==3){
			//如果当前为线上那么切换到线下
			admin.setCurrentUserStatus(2);//切换为线下应用
			setAdmin(admin);
			paramMap.put("isTabSwitch",2); //为2 代表可以切换
			paramMap.put("tabSwitchName","线上应用");//去过可以切换 那么切换的名字是什么
			paramMap.put("currentUserStatus", 2);//为1是线上 代表进入线上页面
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,paramMap);
		}else if(currentUserStatus==2 && licenseType==3){
			admin.setCurrentUserStatus(1);//切换为线上应用
			setAdmin(admin);
			paramMap.put("isTabSwitch",2); //为2 代表可以切换
			paramMap.put("tabSwitchName","线下应用");//去过可以切换 那么切换的名字是什么
			paramMap.put("currentUserStatus", 1);//为2是线下 代表进入线下页面
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,paramMap);
		}else{
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"数据不正确");//存在 跳转
		}
		return resultMap;
	}
}
