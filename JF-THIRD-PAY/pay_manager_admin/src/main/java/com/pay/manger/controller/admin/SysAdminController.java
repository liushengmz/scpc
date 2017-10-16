package com.pay.manger.controller.admin;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.encrypt.PasswordEncoder;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.admin.entity.SysUcenterAdmin;
import com.pay.business.admin.entity.SysUcenterApps;
import com.pay.business.admin.mapper.SysUcenterAdminMapper;
import com.pay.business.admin.service.SysUcenterAdminService;
import com.pay.business.admin.service.SysUcenterAppsService;
import com.pay.business.admin.service.SysUcenterFunctionService;
import com.pay.business.util.ParameterEunm;

/**
 * @ClassName: SysAdminController
 * @Description: 管理系统控制器
 * @author buyuer
 * @date 2015年11月7日 上午10:33:23
 * 
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


    /**
     * 
     * @author buyuer
     * @Title: login
     * @Description: 登录方法
     */
    @RequestMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 此处默认有值
        try {
            String username = map.get("userName").toString();
            String password = map.get("password").toString();
            System.out.println(username + "---" + password);
            String code = map.get("code").toString();
            String oldCode = getSessionAttr("code").toString();
             if (code != null && code.equalsIgnoreCase(oldCode)) 
             {
            // MD5加密
            password = PasswordEncoder.getPassword(password);
            System.out.println("password:" + password);
            
            SysUcenterAdmin admin = adminService.loginAdmin(username, password);
            
            if (admin != null) {
                setAdmin(admin);
                resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, "admin/menu.do");
            }else{
                resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, "请输入正确账号和密码!");
            }
//             UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//             Subject currentUser = SecurityUtils.getSubject();
//
//             if (!currentUser.isAuthenticated()) {
//             token.setRememberMe(true);
//             currentUser.login(token);
//             }

            
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
     * 
     * @author buyuer
     * @Title: menu_admin
     * @Description:跳转到菜单中心
     */
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public ModelAndView menu_admin(@RequestParam Map<String, Object> map, ModelAndView modelAndView) {
        ModelAndView mv = null;
        try {
            SysUcenterAdmin admin = getAdmin();
            if (admin.getAdmType().equals("SUPER")) {
                mv = new ModelAndView("center/index_admin");
            } else {
                // 此处查询用户的权限列表
                mv = getMenuNormal(mv, admin.getId());
            }
        } catch (Exception e) {
            LOGGER.error("出现异常", e);
        }
        return mv;
    }

    private ModelAndView getMenuNormal(ModelAndView view, Long userId) {
        view = new ModelAndView("center/index");
        List<Map<String, Object>> functions = sysUcenterFunctionService.listFunctionForNormal(userId, ReadPro.getValue("app_code"));
        view.addObject("functions", functions);
        return view;
    }

    /**
     * 
     * @author buyuer
     * @Title: appList
     * @Description: 应用列表
     */
    @RequestMapping("/goapplist")
    public ModelAndView goapplist(@RequestParam Map<String, Object> map) {
        ModelAndView mv = new ModelAndView("app/app-list");
        PageObject<SysUcenterApps> pageObject = appService.Pagequery(map);
        mv.addObject("list", pageObject); 
        return mv;
    }

    /**
     * 
     * @author buyuer
     * @Title: goAddApp
     * @Description: 去添加应用页面
     */
    @RequestMapping("/goaddapp")
    public ModelAndView goAddApp(@RequestParam(value="id", required = false) Integer id) {
        ModelAndView mv = null;
        if (id != null) {
            mv = new ModelAndView("app/app-edit");
            SysUcenterApps apps = appService.findById(id);
            mv.addObject("app", apps);
        } else {
            mv = new ModelAndView("app/app-add");
        }

        return mv;
    }

    /**
     * 
     * @author buyuer
     * @Title: doAddApp
     * @Description: 保存应用
     */
    @ResponseBody
    @RequestMapping("/addapp")
    public Map<String, Object> doAddApp(@RequestParam Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            map.put("createUserBy", getAdmin().getId());
            appService.add(map);
            resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
        } catch (Exception e) {
            resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
            LOGGER.error(e.getMessage());
        }
        return resultMap;
    }

    /**
     * 
     * @author buyuer
     * @Title: doAddApp
     * @Description: 修改应用
     */
    @ResponseBody
    @RequestMapping("/updapp")
    public Map<String, Object> updapp(@RequestParam Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            map.put("updateUserBy", getAdmin().getId());
            appService.update(map);
            resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
        } catch (Exception e) {
            resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
            LOGGER.error(e.getMessage());
        }
        return resultMap;
    }

    /**
     * 
     * @author buyuer
     * @Title: doAddApp
     * @Description: 修改应用
     */
    @ResponseBody
    @RequestMapping("/delapp")
    public Map<String, Object> delapp(@RequestParam Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            appService.delete(map);
            resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
        } catch (Exception e) {
            resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
            LOGGER.error(e.getMessage());
        }
        return resultMap;
    }

    /**
     * 
     * @author buyuer
     * @Title: toAdminList
     * @Description: 去管理员列表
     */
    @RequestMapping("/goadminlist")
    public ModelAndView toAdminList(@RequestParam Map<String, Object> map) {
        ModelAndView view = new ModelAndView("admin/adm-list");
        PageObject<SysUcenterAdmin> list = adminService.Pagequery(map);
        view.addObject("list", list);
        return view;
    }

    /**
     * 
     * @author buyuer
     * @Title: goAddadm
     * @Description: 去管理员新增页面
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
     * @Description: 添加管理员
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
     * @Description: 修改管理员信息
     */
    @ResponseBody
    @RequestMapping("/updadm")
    public Map<String, Object> updAdm(@RequestParam Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
        	String passWrod=String.valueOf(map.get("admPassword"));
        	//如果前端传的加密值与预设的加密值一样；证明用户没有修改密码；故设为null;
        	if(passWrod.equals("4c6e33e575da86adc6df9d85790c75ce")){
        		map.put("admPassword", null);
        	}
            adminService.update(map);
            resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
        } catch (Exception e) {
            LOGGER.error("服务器异常", e);
            resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
        }
        return resultMap;
    }
    /**
     * 
     * testRedis 
     * 获取今天支付通道实时金额流水
     * @param map
     * @param response
     * @return    设定文件 
     * Map<String,Object>    返回类型
     */
    @ResponseBody
	@RequestMapping("/flowingWater")
	public Map<String, Object> testRedis(@RequestParam Map<String, String> map,HttpServletResponse response) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	
		Map<String, Object> resultMap = new HashMap<>();
		
		Date as = new Date();
		
		String time = DateUtil.DateToString(as, "yyyyMMdd");
		
		Map<String,String> s =RedisDBUtil.redisDao.getStringMapAll(time);
		
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, s);
		return resultMap;
	}
    
    /**
	 * 天付宝回调
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/testCallBack")
	public void tfbCallBack(@RequestParam Map<String, String> map,HttpServletRequest request,HttpServletResponse response) throws Exception{
		PrintWriter out = response.getWriter();
		out.write("success");
		if(out!=null){
			out.close();
		}
	}
	
	public static void main(String[] args)
	{
		System.out.println(com.core.teamwork.base.util.encrypt.PasswordEncoder.getPassword("123456"));
	}
	
}
