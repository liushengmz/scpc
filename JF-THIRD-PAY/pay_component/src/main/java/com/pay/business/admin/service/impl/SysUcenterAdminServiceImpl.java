package com.pay.business.admin.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.encrypt.PasswordEncoder;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.admin.entity.SysUcenterAdmin;
import com.pay.business.admin.mapper.SysUcenterAdminMapper;
import com.pay.business.admin.service.SysUcenterAdminService;

/**
 * @author buyuer
 * @version
 */
@Service("sysUcenterAdminService")
public class SysUcenterAdminServiceImpl extends BaseServiceImpl<SysUcenterAdmin, SysUcenterAdminMapper> implements SysUcenterAdminService {

    // 注入当前dao对象
    @Autowired
    private SysUcenterAdminMapper sysUcenterAdminMapper;

    public SysUcenterAdminServiceImpl() {
        setMapperClass(SysUcenterAdminMapper.class, SysUcenterAdmin.class);
    }

    /*
     * (非 Javadoc) <p>Title: getAdmin</p> <p>Description: </p>
     * 
     * @param userName
     * 
     * @return
     * 
     * @see cn.iyizhan.treamwork.service.manager.SysUcenterAdminService#getAdmin(java.lang.String)
     */

    public SysUcenterAdmin getAdminByUserName(String userName) {
        SysUcenterAdmin admin = new SysUcenterAdmin();
        admin.setAdmStatus(0);
        admin.setAdmName(userName);
        return sysUcenterAdminMapper.selectSingle(admin);
    }
    
    /* (非 Javadoc)
     * <p>Title: getCount</p>
     * <p>Description: </p>
     * @param map
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAdminService#getCount(java.util.Map)
    */

    public Integer getCount(Map<String, Object> map) {
        return sysUcenterAdminMapper.getCount(map);
    }

    /* (非 Javadoc)
     * <p>Title: startAdmin</p>
     * <p>Description: </p>
     * @param id
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAdminService#startAdmin(java.lang.Long)
    */

    public void startAdmin(Long id) {
        SysUcenterAdmin admin = new SysUcenterAdmin();
        admin.setId(id);
        admin.setAdmStatus(0);
        admin.setUpdateTime(new Date());
        sysUcenterAdminMapper.updateByEntity(admin);
    }

    /* (非 Javadoc)
     * <p>Title: stopAdmin</p>
     * <p>Description: </p>
     * @param id
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAdminService#stopAdmin(java.lang.Long)
    */

    public void stopAdmin(Long id) {
        SysUcenterAdmin admin = new SysUcenterAdmin();
        admin.setId(id);
        admin.setAdmStatus(1);
        admin.setUpdateTime(new Date());
        sysUcenterAdminMapper.updateByEntity(admin);
    }

    /* (非 Javadoc)
     * <p>Title: delAdmin</p>
     * <p>Description: </p>
     * @param id
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAdminService#delAdmin(java.lang.Long)
    */

    public void delAdmin(Long id) {
        SysUcenterAdmin admin = new SysUcenterAdmin();
        admin.setId(id);
        sysUcenterAdminMapper.deleteByEntity(admin);
    }
    
    /* (非 Javadoc)
     * <p>Title: add</p>
     * <p>Description: </p>
     * @param map
     * @return
     * @throws Exception
     * @see cn.iyizhan.teamwork.base.service.impl.BaseServiceImpl#add(java.util.Map)
    */

    public SysUcenterAdmin add(Map<String, Object> map) throws Exception {
        SysUcenterAdmin admin = M2O(map);
        admin.setAdmStatus(0);
        admin.setCreateTime(new Date());
        return super.add(admin);
    }
    /* (非 Javadoc)
     * <p>Title: findById</p>
     * <p>Description: </p>
     * @param id
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAdminService#findById(java.lang.Long)
    */

    public SysUcenterAdmin findById(Long id) {
        SysUcenterAdmin admin = new SysUcenterAdmin();
        admin.setId(id);
        return sysUcenterAdminMapper.selectSingle(admin);
    }
    
    /* (非 Javadoc)
     * <p>Title: addAdmin</p>
     * <p>Description: </p>
     * @param map
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAdminService#addAdmin(java.util.Map)
    */

    public void addAdmin(Map<String, Object> map) {
        SysUcenterAdmin admin = M2O(map);
        String pass = admin.getAdmPassword();
        pass = PasswordEncoder.getPassword(pass);
        admin.setAdmPassword(pass);
        admin.setAdmType("NORMAL");
        admin.setAdmStatus(0);
        admin.setCreateTime(new Date());
        sysUcenterAdminMapper.insertByEntity(admin);
    }
    
    /* (非 Javadoc)
     * <p>Title: update</p>
     * <p>Description: </p>
     * @param map
     * @throws Exception
     * @see cn.iyizhan.teamwork.base.service.impl.BaseServiceImpl#update(java.util.Map)
    */

    public void update(Map<String, Object> map) throws Exception {
        SysUcenterAdmin admin = M2O(map);
        String pass = admin.getAdmPassword();
        if (StringUtils.isNoneBlank(pass)) {
            pass = PasswordEncoder.getPassword(pass);
            admin.setAdmPassword(pass);
        }
        admin.setUpdateTime(new Date());
        super.update(admin);
    }
    
    /* (非 Javadoc)
     * <p>Title: Pagequery</p>
     * <p>Description: </p>
     * @param map
     * @return
     * @see cn.iyizhan.teamwork.base.service.impl.BaseServiceImpl#Pagequery(java.util.Map)
    */

    public PageObject<SysUcenterAdmin> Pagequery(Map<String, Object> map) {
        map.put("admType", "NORMAL");
        return super.Pagequery(map);
    }
    
    /* (非 Javadoc)
     * <p>Title: loginAdmin</p>
     * <p>Description: </p>
     * @param userName
     * @param password
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterAdminService#loginAdmin(java.lang.String, java.lang.String)
    */

    public SysUcenterAdmin loginAdmin(String userName, String password) {
        SysUcenterAdmin admin = new SysUcenterAdmin();
        admin.setAdmName(userName);
        admin.setAdmPassword(password);
        admin.setAdmStatus(0);
        return admin = sysUcenterAdminMapper.selectSingle(admin);
    }
}
