package com.pay.business.admin.service;

import java.util.List;
import java.util.Map;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.admin.entity.SysUcenterApps;
import com.pay.business.admin.mapper.SysUcenterAppsMapper;

/**
 * @author sine
 * @version 
 */
public interface SysUcenterAppsService extends BaseService<SysUcenterApps,SysUcenterAppsMapper> {

    
    /**
     * 
     * @author buyuer
     * @Title: getCount
     * @Description: 获取总数
     */
    Integer getCount(Map<String, Object> map);
    
    /**
     * 
     * @author buyuer
     * @Title: findById
     * @Description: 根据ID查询项目
     * @param id 项目ID
     */
    SysUcenterApps findById(Integer id);
    
    /**
     * 
     * @author buyuer
     * @Title: findAppsAll
     * @Description:查询所有的app
     */
    List<SysUcenterApps> findAppsAll();
    
    /**
     * 
     * @author buyuer
     * @Title: findByCode
     * @Description: 根据应用code 查询应用信息
     * @param appCode 应用code
     */
    SysUcenterApps findSysApp();
    
    /**
     * 
     * @author buyuer
     * @Title: findByCode
     * @Description: 根据应用code 查询应用信息
     * @param code 应用code
     */
    SysUcenterApps findByCode(String code);
}
