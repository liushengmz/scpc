package com.pay.company.controller.online;

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
import com.pay.business.payv2.mapper.Payv2SdkVersionMapper;
import com.pay.business.payv2.service.Payv2SdkVersionService;
import com.pay.business.util.ParameterEunm;
import com.pay.company.controller.admin.BaseManagerController;

/**
 * @ClassName: Payv2SdkVersionController
 * @Description:SDK管理
 * @author mofan
 * @date 2017年2月17日 09:36:42
 */
@Controller
@RequestMapping("/online/payv2SdkVersion/*")
public class Payv2SdkVersionController extends BaseManagerController<Payv2SdkVersion, Payv2SdkVersionMapper> {

	private static final Logger logger = Logger.getLogger(Payv2SdkVersionController.class);
	@Autowired
	Payv2SdkVersionService payv2SdkVersionService;

	/**
	 * 获取sdk下载版本地址
	 * 
	 * @param map
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("getpayv2SdkVersion")
	public Map<String, Object> getpayv2SdkVersion(@RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("sdkType") != null) {
			map.put("isOnline", 1);//上线
			Payv2SdkVersion version = payv2SdkVersionService.getVersion(map);
			if (version != null) {
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, version);
			} else {
				String sdkType = map.get("sdkType").toString();
				resultMap.put("resultCode", 201);
				if ("1".equals(sdkType)) {
					resultMap.put("message", "android SDK 未上传！");
					logger.info("android SDK 未上传！");
				} else if ("2".equals(sdkType)) {
					resultMap.put("message", "IOS SDK 未上传！");
					logger.info("IOS SDK 未上传！");
				} else if ("3".equals(sdkType)) {
					resultMap.put("message", "server SDK 未上传！");
					logger.info("server SDK 未上传！");
				} else if ("4".equals(sdkType)) {
					resultMap.put("message", "H5 SDK 未上传！");
					logger.info("H5 SDK 未上传！");
				}
			}
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE, null);
		}
		return resultMap;
	}

}
