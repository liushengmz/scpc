package com.pay.channel.controller.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.util.ParameterEunm;
import com.pay.channel.controller.base.BaseController;

@Controller
@RequestMapping("/channelApp/*")
public class Payv2ChannelAppConrtroller extends BaseController{

	private Logger logger = Logger.getLogger(getClass());
	
	public Map<String,Object> login(@RequestParam Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[]{"phone","password"}, map);
		if(!isNotNull){
			return resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null,"参数不允许为空");
		}
		
		return null;
	}
}
