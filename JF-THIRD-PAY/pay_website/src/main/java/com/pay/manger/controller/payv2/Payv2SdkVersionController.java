package com.pay.manger.controller.payv2;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.payv2.entity.Payv2SdkVersion;
import com.pay.business.payv2.service.Payv2SdkVersionService;
import com.pay.business.util.ParameterEunm;

/**
* @ClassName: Payv2SdkVersionController 
* @Description:版本更新
* @author qiuguojie
* @date 2016年12月9日 下午8:21:52
*/
@Controller
@RequestMapping("/sdkVersion/*")
public class Payv2SdkVersionController{
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private Payv2SdkVersionService payv2SdkVersionService;
	/**
	* @Description:支付信息   线下扫码支付
	* @param request
	* @param response
	* @param map
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2016年9月2日 下午4:37:51 
	* @throws
	*/
	/*@ResponseBody
	@RequestMapping(value = "/getVersion")
	public Map<String,Object> getVersion(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> map){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			map.put("isOnline", 1);//上线
			map.put("sdkType", 1);//安卓
			Payv2SdkVersion psv = payv2SdkVersionService.getVersion(map);
			if(psv!=null){
				resultMap.put("sdkVersionCode", psv.getSdkVersionCode());
				resultMap.put("sdkFileUrl", psv.getSdkFileUrl());
				resultMap.put("sdkMd5", psv.getSdkMd5());
				
				resultMap.put("resourceVersionCode", psv.getResourceVersionCode());
				resultMap.put("resourceFileUrl", psv.getResourceFileUrl());
				resultMap.put("resourceMd5", psv.getResourceMd5());
			}
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}	
		return resultMap;
	}*/
}
