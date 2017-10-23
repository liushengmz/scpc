package com.pay.business.admin.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.admin.entity.SysUcenterFunction;
import com.pay.business.admin.mapper.SysUcenterFunctionMapper;

/**
 * @author sine
 * @version 
 */
public interface SysUcenterFunctionService extends BaseService<SysUcenterFunction, SysUcenterFunctionMapper> {

    
    /**
     * 
     * @author buyuer
     * @Title: listFunctionForAdmin
     * @Description: 管理员查询菜单
     */
    PageObject<SysUcenterFunction> listFunctionForAdmin(Map<String, Object> map);
    
    /**
     * 
     * @author buyuer
     * @Title: addMenu
     * @Description: 新增菜单
     * @param userId
     * @param map
     */
    void addMenu(Long userId, Map<String, Object> map);
    /**
     * 
     * @author buyuer
     * @Title: listFunctionForNormal
     * @Description: 根据userId 获取用户菜单权限
     */
    List<Map<String, Object>> listFunctionForNormal(Long userId, String appsCode);
    
    /**
     * @author buyuer
     * @Title: findById
     * @Description: 根据id查询菜单信息
     * @param id 菜单id
     */
    SysUcenterFunction findById(Long userId, Long id);
    
    /**
     * @author buyuer
     * @Title: startFunction
     * @Description: 解锁菜单
     * @param id 菜单ID
     */
    void startFunction(Long userId, Long id);
    
    /**
     * @author buyuer
     * @Title: stopFunction
     * @Description: 锁定菜单
     * @param id 菜单ID
     */
    void stopFunction(Long userId, Long id);
    
    /**
     * 
     * @author buyuer
     * @Title: updMenu
     * @Description: 修改菜单
     */
    void updMenu(Long userId, Map<String, Object> map);
    /**
     * @author buyuer
     * @Title: delFunction
     * @Description: 删除菜单
     * @param id 菜单ID
     */
    void delFunction(Long id);
    
    /**
     * 
     * @author buyuer
     * @Title: findMenuListByAppId
     * @Description: 根据应用id查询菜单列表
     * @param appId 应用id
     */
    List<SysUcenterFunction> findMenuListByAppId(Integer appId);
    
    /**
     * 
     * @author buyuer
     * @Title: selectFunList
     * @Description: 查询所有权限列表
     */
    List<Map<String, Object>> selectFunList();
    
}
