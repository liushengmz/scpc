package com.pay.business.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.pay.business.admin.entity.SysUcenterFiles;
import com.pay.business.admin.mapper.SysUcenterFilesMapper;
import com.pay.business.admin.service.SysUcenterFilesService;

/**
 * @author cyl
 * @version 
 */
@Service("sysUcenterFilesService")
public class SysUcenterFilesServiceImpl extends BaseServiceImpl<SysUcenterFiles, SysUcenterFilesMapper> implements SysUcenterFilesService {
	// 注入当前dao对象
    @Autowired
    private SysUcenterFilesMapper sysUcenterFilesMapper;

    public SysUcenterFilesServiceImpl() {
        setMapperClass(SysUcenterFilesMapper.class, SysUcenterFiles.class);
    }

	/* (non-Javadoc)
	 * @see com.pay.business.admin.service.SysUcenterFilesService#selectMD5(java.lang.String)
	 */
	public SysUcenterFiles selectMD5(String fileMD5) {
		SysUcenterFiles sysUcenterFiles = sysUcenterFilesMapper.selectMD5(fileMD5);
		return sysUcenterFiles;
	}
}
