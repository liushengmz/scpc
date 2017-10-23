package com.pay.business.admin.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.admin.entity.SysUcenterPermission;
import com.pay.business.admin.mapper.SysUcenterPermissionMapper;
import com.pay.business.admin.service.SysUcenterPermissionService;

/**
 * @author buyuer
 * @version
 */
@Service("sysUcenterPermissionService")
public class SysUcenterPermissionServiceImpl extends BaseServiceImpl<SysUcenterPermission, SysUcenterPermissionMapper> implements SysUcenterPermissionService {
    // 注入当前dao对象
    @Autowired
    private SysUcenterPermissionMapper sysUcenterPermissionMapper;

    public SysUcenterPermissionServiceImpl() {
        setMapperClass(SysUcenterPermissionMapper.class, SysUcenterPermission.class);
    }

    /*
     * (非 Javadoc) <p>Title: addPermissToRole</p> <p>Description: </p>
     * 
     * @param map
     * 
     * @param userId
     * 
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterPermissionService#addPermissToRole(java.util.Map, java.lang.Long)
     */
    public void addPermissToRole(Map<String, Object> map, Long userId) {
        SysUcenterPermission permission = null;
        String roles = map.get("roles").toString();
        Long rolId = Long.valueOf(map.get("rolId").toString());
        String prm_remark = map.get("prm_remark") != null ? map.get("prm_remark").toString() : null;
        Set<String> roSet = new HashSet<String>(Arrays.asList(roles.split(",")));
        //先删除原有的数据
        permission= new SysUcenterPermission();
        permission.setRolId(rolId);
        sysUcenterPermissionMapper.deleteByEntity(permission);
        for (String fun : roSet) {
            if (StringUtils.isNoneBlank(fun)) {
                permission= new SysUcenterPermission();
                permission.setFunId(Long.valueOf(fun));
                permission.setPrmRemark(prm_remark);
                permission.setRolId(rolId);
                permission.setCreateTime(new Date());
                permission.setCreateUserBy(userId);
                sysUcenterPermissionMapper.insertByEntity(permission);
            }
        }
        
    }
    
    /* (非 Javadoc)
     * <p>Title: selectFunByRole</p>
     * <p>Description: </p>
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterPermissionService#selectFunByRole()
    */
    public List<Long> selectFunByRole(Long id) {
        
        return sysUcenterPermissionMapper.selectFunByRole(id);
    }

}
