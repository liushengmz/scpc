package com.pay.business.admin.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.admin.entity.SysUcenterRole;
import com.pay.business.admin.mapper.SysUcenterRoleMapper;
import com.pay.business.admin.service.SysUcenterRoleService;

/**
 * @author buyuer
 * @version
 */
@Service("sysUcenterRoleService")
public class SysUcenterRoleServiceImpl extends BaseServiceImpl<SysUcenterRole, SysUcenterRoleMapper> implements SysUcenterRoleService {
    // 注入当前dao对象
    @Autowired
    private SysUcenterRoleMapper sysUcenterRoleMapper;

    public SysUcenterRoleServiceImpl() {
        setMapperClass(SysUcenterRoleMapper.class, SysUcenterRole.class);
    }

    /*
     * (非 Javadoc) <p>Title: addRole</p> <p>Description: </p>
     * 
     * @param map
     * 
     * @param userId
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterRoleService#addRole(java.util.Map, java.lang.Long)
     */
    public void addRole(Map<String, Object> map, Long userId) {
        SysUcenterRole role = M2O(map);
        role.setCreateTime(new Date());
        role.setCreateUserBy(userId);
        sysUcenterRoleMapper.insertByEntity(role);
    }

    /* (非 Javadoc)
     * <p>Title: findById</p>
     * <p>Description: </p>
     * @param id
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterRoleService#findById(java.lang.Long)
    */
    public SysUcenterRole findById(Long id) {
        SysUcenterRole role = new SysUcenterRole();
        role.setId(id);
        return sysUcenterRoleMapper.selectSingle(role);
    }

    /* (非 Javadoc)
     * <p>Title: updRole</p>
     * <p>Description: </p>
     * @param map
     * @param userId
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterRoleService#updRole(java.util.Map, java.lang.Long)
    */
    public void updRole(Map<String, Object> map, Long userId) {
        SysUcenterRole role = M2O(map);
        role.setUpdateTime(new Date());
        role.setUpdateUserBy(userId);
        sysUcenterRoleMapper.updateByEntity(role);
    }

    /* (非 Javadoc)
     * <p>Title: stopRole</p>
     * <p>Description: </p>
     * @param id
     * @param userId
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterRoleService#stopRole(java.lang.Long, java.lang.Long)
    */
    public void stopRole(Long id, Long userId) {
        SysUcenterRole role = new SysUcenterRole();
        role.setId(id);
        role.setRolStatus(1);
        role.setUpdateTime(new Date());
        role.setUpdateUserBy(userId);
        sysUcenterRoleMapper.updateByEntity(role);
    }

    /* (非 Javadoc)
     * <p>Title: startRole</p>
     * <p>Description: </p>
     * @param id
     * @param userId
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterRoleService#startRole(java.lang.Long, java.lang.Long)
    */

    public void startRole(Long id, Long userId) {
        SysUcenterRole role = new SysUcenterRole();
        role.setId(id);
        role.setRolStatus(0);
        role.setUpdateTime(new Date());
        role.setUpdateUserBy(userId);
        sysUcenterRoleMapper.updateByEntity(role);
    }

    /* (非 Javadoc)
     * <p>Title: delRole</p>
     * <p>Description: </p>
     * @param id
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterRoleService#delRole(java.lang.Long)
    */
    public void delRole(Long id) {
        SysUcenterRole role = new SysUcenterRole();
        role.setId(id);
        sysUcenterRoleMapper.deleteByEntity(role);
    }
    
    /* (非 Javadoc)
     * <p>Title: pageQueryByObject_Map</p>
     * <p>Description: </p>
     * @param map
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterRoleService#pageQueryByObject_Map(java.util.Map)
    */
    @SuppressWarnings("unchecked")

    public PageObject<Map<String, Object>> pageQueryByObject_Map(Map<String, Object> map) {
        // TODO Auto-generated method stub
        int totalData = sysUcenterRoleMapper.getCount_Map(map);
        PageHelper pageHelper = new PageHelper(totalData, map);
        List<Map<String, Object>> list = sysUcenterRoleMapper.pageQueryByObject_Map(pageHelper.getMap());
        PageObject<Map<String, Object>> pageObject = pageHelper.getPageObject();
        pageObject.setDataList(list);
        return pageObject;
    }
    
    /* (非 Javadoc)
     * <p>Title: getRoleAll</p>
     * <p>Description: </p>
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterRoleService#getRoleAll()
    */

    public List<Map<String, Object>> getRoleAll() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("rolStatus", 0);
        return sysUcenterRoleMapper.queryByObject_Map(paramMap);
    }
    
}
