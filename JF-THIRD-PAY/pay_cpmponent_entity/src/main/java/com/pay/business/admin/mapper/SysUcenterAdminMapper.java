package com.pay.business.admin.mapper;

import com.core.teamwork.base.mapper.BaseMapper;
import com.pay.business.admin.entity.SysUcenterAdmin;

/**
 * @author buyuer
 * @version 
 */
public interface SysUcenterAdminMapper extends BaseMapper<SysUcenterAdmin>{

    /* (非 Javadoc)
     * <p>Title: selectSingle</p>
     * <p>Description: </p>
     * @param t
     * @return
     * @see cn.iyizhan.treamwork.mapper.BaseMapper#selectSingle(java.lang.Object)
    */
    public SysUcenterAdmin selectSingle(SysUcenterAdmin t);
}