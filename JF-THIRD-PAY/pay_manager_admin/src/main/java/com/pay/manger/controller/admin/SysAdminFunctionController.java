package com.pay.manger.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.admin.entity.SysUcenterApps;
import com.pay.business.admin.entity.SysUcenterFunction;
import com.pay.business.admin.mapper.SysUcenterFunctionMapper;
import com.pay.business.admin.service.SysUcenterAppsService;
import com.pay.business.admin.service.SysUcenterFunctionService;
import com.pay.business.util.ParameterEunm;

/** 
 * @ClassName: SysAdminFunctionController
 * @Description: 菜单管理
 * @author buyuer
 * @date 2015年11月10日 下午8:18:07
 * 
 */
@RequestMapping("/function/*")
@Controller
public class SysAdminFunctionController extends BaseManagerController<SysUcenterFunction, SysUcenterFunctionMapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysAdminController.class);
    
    @Autowired
    private SysUcenterFunctionService functionService;
    
    @Autowired
    private SysUcenterAppsService appService;
    
    /**
     * @author buyuer
     * @Title: toList
     * @Description: 去菜单列表页面
     */
    @RequestMapping("/gomenulist")
    public ModelAndView toList(@RequestParam Map<String, Object> map) {
    	System.out.println(map);
        ModelAndView view = new ModelAndView("/menu/menu-list");
        PageObject<SysUcenterFunction> list = functionService.listFunctionForAdmin(map);
        
        //查询所有app
        List<SysUcenterApps> apps = appService.findAppsAll();
        view.addObject("funList", list);
        view.addObject("apps", apps);
        view.addAllObjects(map);
        return view;
    }
    
    
    /**
     * @author buyuer
     * @Title: goAddadm
     * @Description: 去菜单新增页面
     */
    @RequestMapping("/goaddmenu")
    public ModelAndView goAddaMenu(@RequestParam(value ="id", required = false) Long id,@RequestParam(value ="appId", required = false) Integer appId ){
        ModelAndView view = null;
        if (id != null) {
            SysUcenterFunction function = functionService.findById(getAdmin().getId(), id);
            view = new ModelAndView("/menu/menu-edit");
            view.addObject("fun", function);
        }else{
            view = new ModelAndView("/menu/menu-add");
            view.addObject("appId", appId);
        }
        //获取已有菜单列表
        List<SysUcenterFunction> funList = functionService.findMenuListByAppId(appId);
        view.addObject("funList", funList);
        return view;
    }
    
    /**
     * 
     * @author buyuer
     * @Title: startAdm
     * @Description: 增加菜单
     */
    @ResponseBody
    @RequestMapping("/addmenu")
    public Map<String, Object> addMenu(@RequestParam Map<String, Object> map){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            functionService.addMenu(getAdmin().getId(), map);
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
     * @Description: 启用菜单
     */
    @ResponseBody
    @RequestMapping("/startmenu")
    public Map<String, Object> startMenu(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            functionService.startFunction(getAdmin().getId(), id);
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
     * @Description: 禁用菜单
     */
    @ResponseBody
    @RequestMapping("/stopmenu")
    public Map<String, Object> stopMenu(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            functionService.stopFunction(getAdmin().getId(), id);
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
     * @Description: 删除菜单
     */
    @ResponseBody
    @RequestMapping("/updmenu")
    public Map<String, Object> updMenu(@RequestParam Map<String, Object> map){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            functionService.updMenu(getAdmin().getId(), map);
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
     * @Description: 删除菜单
     */
    @ResponseBody
    @RequestMapping("/delmenu")
    public Map<String, Object> delMenu(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            functionService.delFunction(id);
            resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
        } catch (Exception e) {
            LOGGER.error("服务器异常", e);
            resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
        }
        return resultMap;
    }
    
    
}

