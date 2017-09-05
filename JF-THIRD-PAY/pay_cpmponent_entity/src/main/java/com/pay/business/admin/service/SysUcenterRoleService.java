package com.pay.business.admin.service;


import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.admin.entity.SysUcenterRole;
import com.pay.business.admin.mapper.SysUcenterRoleMapper;

/**
 * @author sine
 * @version 
 */
public interface SysUcenterRoleService extends BaseService<SysUcenterRole,SysUcenterRoleMapper>  {

    /**
     * 
     * @author buyuer
     * @Title: addRole
     * @Description: 创建角色
     * @param map 数据
     * @param userId 创建人id
     */
    void addRole(Map<String, Object> map, Long userId);
    
    /**
     * 
     * @author buyuer
     * @Title: findById
     * @Description: 根据id查询
     * @param id
     * @return
     */
    SysUcenterRole findById(Long id);
    
    /**
     * 
     * @author buyuer
     * @Title: updRole
     * @Description: 修改
     * @param map
     */
    void updRole(Map<String, Object> map, Long userId);
    
    /**
     * 
     * @author buyuer
     * @Title: stopRole
     * @Description: 禁用角色
     * @param id 角色id
     * @param userId 用户id
     */
    void stopRole(Long id, Long userId);
    
    /**
     * 
     * @author buyuer
     * @Title: startRole
     * @Description: 启用角色
     * @param id 将角色id
     * @param userId 用户id
     */
    void startRole(Long id, Long userId);
    
    /**
     * 
     * @author buyuer
     * @Title: delRole
     * @Description: 删除角色
     * @param id 角色id
     */
    void delRole(Long id);
    
    /**
     * 
     * @author buyuer
     * @Title: pageQueryByObject_Map
     * @Description: 分页查询返回map
     */
    PageObject<Map<String, Object>> pageQueryByObject_Map(Map<String, Object> map); 
    
    /**
     * 
     * @author buyuer
     * @Title: getRoleAll
     * @Description:获取全部角色
     */
    List<Map<String, Object>> getRoleAll();
}
