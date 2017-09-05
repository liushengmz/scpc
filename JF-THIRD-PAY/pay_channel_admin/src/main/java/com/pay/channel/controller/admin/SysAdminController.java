package com.pay.channel.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.encrypt.MD5;
import com.core.teamwork.base.util.encrypt.PasswordEncoder;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.admin.entity.SysUcenterAdmin;
import com.pay.business.admin.mapper.SysUcenterAdminMapper;
import com.pay.business.admin.service.SysUcenterAdminService;
import com.pay.business.admin.service.SysUcenterAppsService;
import com.pay.business.admin.service.SysUcenterFunctionService;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.service.Payv2ChannelService;
import com.pay.business.util.ParameterEunm;

/**
 * @ClassName: SysAdminController
 * @Description:渠道商管理（用户）控制器
 * @author zhoulibo
 * @date 2016年12月5日 下午2:34:55
 */
@Controller
@RequestMapping("/admin/*")
public class SysAdminController extends BaseManagerController<SysUcenterAdmin, SysUcenterAdminMapper> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysAdminController.class);

	@Autowired
	private SysUcenterAdminService adminService;

	@Autowired
	private SysUcenterAppsService appService;

	@Autowired
	private SysUcenterFunctionService sysUcenterFunctionService;

	@Autowired
	private Payv2ChannelService payv2ChannelService;

	/**
	 * @Title: login
	 * @Description:登录方法
	 * @param map
	 * @param request
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2016年12月5日 下午2:33:27
	 * @throws
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Map<String, Object> login(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 此处默认有值
		try {
			String username = map.get("userName").toString();
			String password = map.get("password").toString();
			String code = map.get("code").toString();
			String oldCode = getSessionAttr("code").toString();
			if (code != null && code.equalsIgnoreCase(oldCode)) {
				// MD5加密
				password =MD5.GetMD5Code(password);

				// SysUcenterAdmin admin = adminService.loginAdmin(username, password);
				Payv2Channel admin = payv2ChannelService.loginAdmin(username, password);
				if (admin != null) {
					setAdmin(admin);
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, "admin/menu.do");
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, "请输入正确账号和密码!");
				}
				// UsernamePasswordToken token = new UsernamePasswordToken(username, password);
				// Subject currentUser = SecurityUtils.getSubject();
				//
				// if (!currentUser.isAuthenticated()) {
				// token.setRememberMe(true);
				// currentUser.login(token);
				// }

			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, "请输入正确验证码!");
			}
		} catch (Exception e) {
			LOGGER.error("登录失败", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, "请输入正确账号和密码!");
		}
		return resultMap;
	}

	/**
	 * 登出
	 * 
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public ModelAndView logout() {
		ModelAndView andView = new ModelAndView("login");
		getSession().invalidate();
		return andView;
	}

	/**
	 * @Title: menu_admin
	 * @Description: 跳转到菜单中心
	 * @param map
	 * @param modelAndView
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月5日 下午2:34:33
	 * @throws
	 */
	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	public ModelAndView menu_admin(@RequestParam Map<String, Object> map, ModelAndView modelAndView) {
		ModelAndView mv = null;
		try {
			Payv2Channel admin = getAdmin();
			/* if (admin.getAdmType().equals("SUPER")) { */
			mv = new ModelAndView("center/index_admin");
			/* } else { */
			// 此处查询用户的权限列表
			mv = getMenuNormal(mv, admin.getId());
			/* } */
		} catch (Exception e) {
			LOGGER.error("出现异常", e);
		}
		return mv;
	}

	private ModelAndView getMenuNormal(ModelAndView view, Long userId) {
		view = new ModelAndView("center/index_admin");
		// List<Map<String, Object>> functions = sysUcenterFunctionService.listFunctionForNormal(userId, ReadPro.getValue("app_code"));
		// view.addObject("functions", functions);
		return view;
	}

	/**
	 * @Title: toAdminList
	 * @Description:获取渠道商管理员列表
	 * @param map
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月5日 下午2:36:23
	 * @throws
	 */
	@RequestMapping("/goadminlist")
	public ModelAndView toAdminList(@RequestParam Map<String, Object> map) {
		ModelAndView view = new ModelAndView("admin/adm-list");
		PageObject<Payv2Channel> list = payv2ChannelService.Pagequery(map);
		view.addObject("list", list);
		return view;
	}

	/**
	 * @Title: goAddadm
	 * @Description:去渠道商新增页面
	 * @param id
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2016年12月5日 下午2:38:12
	 * @throws
	 */
	@RequestMapping("/goaddadm")
	public ModelAndView goAddadm(@RequestParam(value = "id", required = false) Long id) {
		ModelAndView view = null;
		if (id != null) {
			view = new ModelAndView("admin/adm-edit");
			SysUcenterAdmin admin = adminService.findById(id);
			view.addObject("adm", admin);
		} else {
			view = new ModelAndView("admin/adm-add");
		}
		return view;
	}

	/**
	 * 
	 * @author buyuer
	 * @Title: startAdm
	 * @Description: 启用管理员
	 */
	@ResponseBody
	@RequestMapping("/startadm")
	public Map<String, Object> startAdm(@RequestParam("id") Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			adminService.startAdmin(id);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			LOGGER.error("服务器异常", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 
	 * @author buyuer
	 * @Title: startAdm
	 * @Description: 禁用管理员
	 */
	@ResponseBody
	@RequestMapping("/stopadm")
	public Map<String, Object> stopAdm(@RequestParam("id") Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			adminService.stopAdmin(id);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			LOGGER.error("服务器异常", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 
	 * @author buyuer
	 * @Title: startAdm
	 * @Description: 删除管理员
	 */
	@ResponseBody
	@RequestMapping("/deladm")
	public Map<String, Object> delAdm(@RequestParam("id") Long id) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			adminService.delAdmin(id);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			LOGGER.error("服务器异常", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 
	 * @author buyuer
	 * @Title: startAdm
	 * @Description: 删除管理员
	 */
	@ResponseBody
	@RequestMapping("/addadm")
	public Map<String, Object> addAdm(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			adminService.addAdmin(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			LOGGER.error("服务器异常", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 
	 * @author buyuer
	 * @Title: startAdm
	 * @Description: 删除管理员
	 */
	@ResponseBody
	@RequestMapping("/updadm")
	public Map<String, Object> updAdm(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			adminService.update(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
		} catch (Exception e) {
			LOGGER.error("服务器异常", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	* @Title: interfaceLogin 
	* @Description:接口登录
	* @param map
	* @param request
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2017年2月17日 上午10:24:38 
	* @throws
	*/
	@RequestMapping("/interfaceLogin")
	@ResponseBody
	public Map<String, Object> interfaceLogin(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 此处默认有值
		try {
			String username = map.get("userName").toString();
			String password = map.get("password").toString();
			// MD5加密
			password = PasswordEncoder.getPassword(password);
			Payv2Channel admin = payv2ChannelService.loginAdmin(username, password);
			if (admin != null) {
				setAdmin(admin);
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,null);
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, "请输入正确账号和密码!");
			}
		} catch (Exception e) {
			LOGGER.error("登录失败", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, "请输入正确账号和密码!");
		}
		return resultMap;
	}
	/**
	* @Title: interfaceLogout 
	* @Description:接口退出
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2017年2月17日 上午10:34:40 
	* @throws
	*/
	@ResponseBody
	@RequestMapping(value = "/interfaceLogout")
	public Map<String,Object> interfaceLogout() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		getSession().invalidate();
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE,null);
		return resultMap;
	}
}
