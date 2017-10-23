package com.pay.business.admin.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.admin.entity.SysUcenterFunction;
import com.pay.business.admin.mapper.SysUcenterFunctionMapper;
import com.pay.business.admin.mapper.SysUcenterRoleMapper;
import com.pay.business.admin.service.SysUcenterFunctionService;

/**
 * @author buyuer
 * @version
 */
@Service("sysUcenterFunctionService")
public class SysUcenterFunctionServiceImpl extends BaseServiceImpl<SysUcenterFunction, SysUcenterFunctionMapper> implements SysUcenterFunctionService {

    // 注入当前dao对象
    @Autowired
    private SysUcenterFunctionMapper functionMapper;

    @Autowired
    private SysUcenterRoleMapper roleMapper;
    public SysUcenterFunctionServiceImpl() {
        setMapperClass(SysUcenterFunctionMapper.class, SysUcenterFunction.class);
    }

    /*
     * (非 Javadoc) <p>Title: listFunctionForAdmin</p> <p>Description: </p>
     * 
     * @param map
     * 
     * @return
     * 
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterFunctionService#listFunctionForAdmin(java.util.Map)
     */

    public PageObject<SysUcenterFunction> listFunctionForAdmin(Map<String, Object> map) {
//        map.put("fid", -1);
        map.put("funStatus", 0);
        map.put("sortName", "fun_sort");
        map.put("sortOrder", "desc");
        return super.Pagequery(map);
    }
    /* (非 Javadoc)
     * <p>Title: listFunctionForNormal</p>
     * <p>Description: </p>
     * @param userId
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterFunctionService#listFunctionForNormal(java.lang.Long)
    */

    public List<Map<String, Object>> listFunctionForNormal(Long userId, String appsCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", userId);
        map.put("appsCode", appsCode);
        return functionMapper.selectFunByUser(map);
    }

    /* (非 Javadoc)
     * <p>Title: findById</p>
     * <p>Description: </p>
     * @param id
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterFunctionService#findById(java.lang.Long)
    */

    public SysUcenterFunction findById(Long userId, Long id) {
        SysUcenterFunction function = new SysUcenterFunction();
        function.setId(id);
        return functionMapper.selectSingle(function);
    }

    /* (非 Javadoc)
     * <p>Title: startFunction</p>
     * <p>Description: </p>
     * @param id
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterFunctionService#startFunction(java.lang.Long)
    */

    public void startFunction(Long userId, Long id) {
        SysUcenterFunction function = new SysUcenterFunction();
        function.setId(id);
        function.setFunStatus(0);
        function.setUpdateTime(new Date());
        function.setUpdateUserBy(userId);
        functionMapper.updateByEntity(function);
        
        //开启子元素
        function = new SysUcenterFunction();
        function.setFid(id);
        function.setFunStatus(0);
        function.setUpdateTime(new Date());
        function.setUpdateUserBy(userId);
        functionMapper.updateFunction(function);
        
    }

    /* (非 Javadoc)
     * <p>Title: stopFunction</p>
     * <p>Description: </p>
     * @param id
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterFunctionService#stopFunction(java.lang.Long)
    */

    public void stopFunction(Long userId, Long id) {
        SysUcenterFunction function = new SysUcenterFunction();
        function.setId(id);
        function.setFunStatus(1);
        function.setUpdateTime(new Date());
        function.setUpdateUserBy(userId);
        functionMapper.updateByEntity(function);   
        
        //锁定子元素
        function = new SysUcenterFunction();
        function.setFid(id);
        function.setFunStatus(1);
        function.setUpdateTime(new Date());
        function.setUpdateUserBy(userId);
        functionMapper.updateFunction(function);   
    }

    /* (非 Javadoc)
     * <p>Title: delFunction</p>
     * <p>Description: </p>
     * @param id
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterFunctionService#delFunction(java.lang.Long)
    */

    public void delFunction(Long id) {
        
        SysUcenterFunction function = new SysUcenterFunction();
        //删除子元素
        function.setId(id);
        functionMapper.deleteByEntity(function);
        
        //删除子元素
        function = new SysUcenterFunction();
        function.setFid(id);
        functionMapper.deleteByEntity(function);
        
    }
    
    /* (非 Javadoc)
     * <p>Title: addMenu</p>
     * <p>Description: </p>
     * @param userId
     * @param map
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterFunctionService#addMenu(java.lang.Long, java.util.Map)
    */

    public void addMenu(Long userId, Map<String, Object> map) {
        SysUcenterFunction function = M2O(map);
        function.setCreateTime(new Date());
        function.setCreateUserBy(userId);
        functionMapper.insertByEntity(function);
    }
    
    /* (非 Javadoc)
     * <p>Title: updMenu</p>
     * <p>Description: </p>
     * @param userId
     * @param map
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterFunctionService#updMenu(java.lang.Long, java.util.Map)
    */

    public void updMenu(Long userId, Map<String, Object> map) {
        SysUcenterFunction function = M2O(map);
        function.setUpdateUserBy(userId);
        function.setUpdateTime(new Date());
        functionMapper.updateByEntity(function);
    }
    
    /* (非 Javadoc)
     * <p>Title: findMenuListByAppId</p>
     * <p>Description: </p>
     * @param appId
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterFunctionService#findMenuListByAppId(java.lang.Integer)
    */

    public List<SysUcenterFunction> findMenuListByAppId(Integer appId) {
        SysUcenterFunction  function = new SysUcenterFunction();
        function.setAppId(appId);
        function.setFunStatus(0);
        return functionMapper.selectByObject(function);
    }
    
    /* (非 Javadoc)
     * <p>Title: selectFunList</p>
     * <p>Description: </p>
     * @return
     * @see cn.iyizhan.teamwork.manager.service.SysUcenterFunctionService#selectFunList()
    */
    public List<Map<String, Object>> selectFunList() {
        return functionMapper.selectFunList();
    }
    
}
