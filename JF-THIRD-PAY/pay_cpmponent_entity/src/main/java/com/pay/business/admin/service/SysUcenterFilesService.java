package com.pay.business.admin.service;

import com.core.teamwork.base.service.BaseService;
import com.pay.business.admin.entity.SysUcenterFiles;
import com.pay.business.admin.mapper.SysUcenterFilesMapper;

/**
 * @author cyl
 * @version 
 */
public interface SysUcenterFilesService extends BaseService<SysUcenterFiles,SysUcenterFilesMapper>  {

	
	SysUcenterFiles selectMD5(String fileMD5);
}
