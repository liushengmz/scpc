package com.pay.business.admin.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.admin.entity.SysUcenterAdminRole;
import com.pay.business.admin.mapper.SysUcenterAdminRoleMapper;

/**
 * @author sine
 * @version
 */
public interface SysUcenterAdminRoleService extends BaseService<SysUcenterAdminRole, SysUcenterAdminRoleMapper> {

    
    /**
     * @author buyuer
     * @Title: addAdminRole
     * @Description: 增加角色
     */
    void addAdminRole(Map<String, Object> map, Long userId);
    
    /**
     * @author buyuer
     * @Title: findRoleByUser
     * @Description: 查询当前用户的角色
     */
    List<Long> findRoleByUser(Long userId);
    
    void delRoleByUser(Long userId);
    
}
