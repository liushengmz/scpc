package com.pay.business.admin.mapper;

import java.util.List;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.admin.entity.SysUcenterAdminRole;

/**
 * @author buyuer
 * @version 
 */
public interface SysUcenterAdminRoleMapper extends BaseMapper<SysUcenterAdminRole>{

    /**
     * @author buyuer
     * @Title: findRoleByUser
     * @Description: 查询当前用户的角色
     */
    List<Long> findRoleByUser(Long userId);
}