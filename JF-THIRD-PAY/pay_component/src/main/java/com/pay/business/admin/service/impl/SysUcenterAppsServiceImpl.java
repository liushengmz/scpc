package com.pay.business.admin.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.admin.entity.SysUcenterApps;
import com.pay.business.admin.mapper.SysUcenterAppsMapper;
import com.pay.business.admin.service.SysUcenterAppsService;

/**
 * @author buyuer
 * @version
 */
@Service("sysUcenterAppsService")
public class SysUcenterAppsServiceImpl extends BaseServiceImpl<SysUcenterApps, SysUcenterAppsMapper> implements SysUcenterAppsService {

    // 注入当前dao对象
    @Autowired
    private SysUcenterAppsMapper sysUcenterAppsDao;

    @Value("${app_code}")
    private String app_code;

    public SysUcenterAppsServiceImpl() {
        setMapperClass(SysUcenterAppsMapper.class, SysUcenterApps.class);
    }

    /*
     * (非 Javadoc) <p>Title: add</p> <p>Description: </p>
     * 
     * @param map
     * 
     * @return
     * 
     * @throws Exception
     * 
     * @see cn.iyizhan.teamwork.base.service.impl.BaseServiceImpl#add(java.util.Map)
     */
    @Override
    public SysUcenterApps add(Map<String, Object> map) throws Exception {
        SysUcenterApps sysUcenterApps = M2O(map);
        sysUcenterApps.setCreateTime(new Date());
        return super.add(sysUcenterApps);
    }

    /*
     * (非 Javadoc) <p>Title: getCount</p> <p>Description: </p>
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAppsService#getCount()
     */
    public Integer getCount(Map<String, Object> map) {
        return sysUcenterAppsDao.getCount(map);
    }

    /*
     * (非 Javadoc) <p>Title: findById</p> <p>Description: </p>
     * 
     * @param id
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAppsService#findById(java.lang.Long)
     */
    public SysUcenterApps findById(Integer id) {
        SysUcenterApps apps = new SysUcenterApps();
        apps.setId(id);
        return sysUcenterAppsDao.selectSingle(apps);
    }

    /*
     * (非 Javadoc) <p>Title: findAppsAll</p> <p>Description: </p>
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAppsService#findAppsAll()
     */
    public List<SysUcenterApps> findAppsAll() {
        SysUcenterApps apps = new SysUcenterApps();
        return sysUcenterAppsDao.selectByObject(apps);
    }

    /*
     * (非 Javadoc) <p>Title: findByCode</p> <p>Description: </p>
     * 
     * @param appCode
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAppsService#findByCode(java.lang.String)
     */
    public SysUcenterApps findSysApp() {
        return findByCode(app_code);
    }

    /*
     * (非 Javadoc) <p>Title: findByCode</p> <p>Description: </p>
     * 
     * @param code
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAppsService#findByCode(java.lang.String)
     */
    public SysUcenterApps findByCode(String code) {
        SysUcenterApps apps = new SysUcenterApps();
        apps.setAppKey(code);
        return sysUcenterAppsDao.selectSingle(apps);
    }

}
