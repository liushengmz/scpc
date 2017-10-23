package com.pay.business.payv2.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.mapper.Payv2BussCompanyAppMapper;
import com.pay.business.payv2.entity.Payv2AppVersion;
import com.pay.business.payv2.mapper.Payv2AppVersionMapper;
import com.pay.business.payv2.service.Payv2AppVersionService;

/**
 * @author cyl
 * @version 
 */
@Service("payv2AppVersionService")
public class Payv2AppVersionServiceImpl extends BaseServiceImpl<Payv2AppVersion, Payv2AppVersionMapper> implements Payv2AppVersionService {
	// 注入当前dao对象
    @Autowired
    private Payv2AppVersionMapper payv2AppVersionMapper;
    @Autowired
    private Payv2BussCompanyAppMapper payv2BussCompanyAppMapper;

    public Payv2AppVersionServiceImpl() {
        setMapperClass(Payv2AppVersionMapper.class, Payv2AppVersion.class);
    }
    
 	public Payv2AppVersion selectSingle(Payv2AppVersion t) {
		return payv2AppVersionMapper.selectSingle(t);
	}

	public List<Payv2AppVersion> selectByObject(Payv2AppVersion t) {
		return payv2AppVersionMapper.selectByObject(t);
	}

	public PageObject<Payv2AppVersion> payv2AppVersionList(
			Map<String, Object> map) {
		int totalData = payv2AppVersionMapper.getCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2AppVersion> list = payv2AppVersionMapper.pageQueryByObject(pageHelper.getMap());
		PageObject<Payv2AppVersion> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	public void upOnSale(Map<String, Object> map) {
		//获取现在的版本信息
		Payv2AppVersion payv2AppVersion = payv2AppVersionMapper.selectSingle(M2O(map));
		Long id = payv2AppVersion.getId();
		Payv2AppVersion payv2App2 = new Payv2AppVersion();
		payv2App2.setAppId(payv2AppVersion.getAppId());
		payv2App2.setAppType(payv2AppVersion.getAppType());
		payv2App2.setStatus(3);
		//获取以前上架的版本信息
		List<Payv2AppVersion> list = payv2AppVersionMapper.selectByObject(payv2App2);
		if(list != null && list.size() > 0){
			
			for(Payv2AppVersion version : list){
				if(version.getId() == id){//更新现在的版本为最新版本
					version.setStatus(3);
					version.setUpdateTime(new Date());
					payv2AppVersionMapper.updateByEntity(version);//更新现在的版本为最新版本
				}else{
					version.setStatus(4);
					version.setUpdateTime(new Date());
					payv2AppVersionMapper.updateByEntity(version);//把以前的版本下架
				}
			}
		}else{
			
			payv2AppVersion.setStatus(3);
			payv2AppVersion.setUpdateTime(new Date());
			payv2AppVersionMapper.updateByEntity(payv2AppVersion);//更新现在的版本为最新版本
			
		}
		
	}

	/**
	 * 数据加载等待被上架
	 */
	public Payv2AppVersion toUpOnSale(Map<String, Object> map) {
		Payv2AppVersion payv2AppVersion = payv2AppVersionMapper.selectSingle(M2O(map));
		Payv2AppVersion payv2AppV2 = new Payv2AppVersion();
		payv2AppV2.setAppId(payv2AppVersion.getAppId());
		payv2AppV2.setAppType(payv2AppVersion.getAppType());
		payv2AppV2.setStatus(3);
		//得到现在上线的版本，如果没有就取将要上线的版本
		payv2AppV2 = payv2AppVersionMapper.selectLatestByObject(payv2AppV2);
		if(payv2AppV2 != null){
			payv2AppVersion.setCurrentVersion(payv2AppV2.getAppVersion());
		}else{
			payv2AppVersion.setCurrentVersion(payv2AppVersion.getAppVersion());
		}
		return payv2AppVersion;
	}

	/**
	 * 加载版本信息方便下载复制
	 */
	public Payv2AppVersion toLoadDetailForDownload(Map<String, Object> map) {
		Payv2AppVersion payv2AppVersion = payv2AppVersionMapper.selectSingle(M2O(map));
		if(payv2AppVersion != null){
			Payv2BussCompanyApp app = new Payv2BussCompanyApp();
			app.setId(payv2AppVersion.getAppId());
			app = payv2BussCompanyAppMapper.selectSingle(app);
			if(app != null){
				payv2AppVersion.setAppName(app.getAppName());
			}
		}
		return payv2AppVersion;
	}
	
}
