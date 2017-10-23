package com.pay.business.admin.mapper;

import java.util.List;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.admin.entity.SysUcenterPermission;

/**
 * @author buyuer
 * @version
 */
public interface SysUcenterPermissionMapper extends BaseMapper<SysUcenterPermission> {

    /**
     * 
     * @author buyuer
     * @Title: selectFunByRole
     * @Description: 根据角色查询权限列表
     * @return
     */
    List<Long> selectFunByRole(Long id);
}