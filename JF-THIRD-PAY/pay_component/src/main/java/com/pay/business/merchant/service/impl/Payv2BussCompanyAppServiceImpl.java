package com.pay.business.merchant.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.cache.annotations.Cacheable;
import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.IdUtils;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.mapper.Payv2BussCompanyAppMapper;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.payv2.entity.Pavy2PlatformApp;
import com.pay.business.payv2.entity.Payv2BussAppSupportPayWay;
import com.pay.business.payv2.entity.Payv2PlatformWay;
import com.pay.business.payv2.mapper.Pavy2PlatformAppMapper;
import com.pay.business.payv2.mapper.Payv2BussAppSupportPayWayMapper;
import com.pay.business.payv2.mapper.Payv2PlatformWayMapper;

/**
 * @author cyl
 * @version
 */
@Service("payv2BussCompanyAppService")
public class Payv2BussCompanyAppServiceImpl extends BaseServiceImpl<Payv2BussCompanyApp, Payv2BussCompanyAppMapper> implements Payv2BussCompanyAppService {
	// 注入当前dao对象
	@Autowired
	private Payv2BussCompanyAppMapper payv2BussCompanyAppMapper;
	@Autowired
	private Payv2BussCompanyMapper payv2BussCompanyMapper;
	@Autowired
	private Pavy2PlatformAppMapper pavy2PlatformAppMapper;
	@Autowired
	Payv2PlatformWayMapper payv2PlatformWayMapper;
	@Autowired
	Payv2BussAppSupportPayWayMapper payv2BussAppSupportPayWayMapper;
	
	public Payv2BussCompanyAppServiceImpl() {
		setMapperClass(Payv2BussCompanyAppMapper.class, Payv2BussCompanyApp.class);
	}

	public Payv2BussCompanyApp selectSingle(Payv2BussCompanyApp t) {
		return payv2BussCompanyAppMapper.selectSingle(t);
	}

	public List<Payv2BussCompanyApp> selectByObject(Payv2BussCompanyApp t) {
		return payv2BussCompanyAppMapper.selectByObject(t);
	}
	public List<Payv2BussCompanyApp> selectByObject2(Map<String, Object> map) {
		return payv2BussCompanyAppMapper.selectByObject2(map);
	}
	public PageObject<Payv2BussCompanyApp> payv2BussCompanyAppList(Map<String, Object> map) {
		if (map.get("platform") != null) {
			String platform = map.get("platform").toString();
			if ("1".equals(platform)) {
				map.put("isIos", "1");
			} else if ("2".equals(platform)) {
				map.put("isAndroid", "1");
			} else if ("3".equals(platform)) {
				map.put("isWeb", "1");
			}
		}
		int totalData = payv2BussCompanyAppMapper.getLinkCount(map);
		map.put("sortName", "create_time");
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2BussCompanyApp> list = payv2BussCompanyAppMapper.pageLinkQueryByObject(pageHelper.getMap());
		for (Payv2BussCompanyApp app : list) {
			if(app.getCompanyId() != null){
				Payv2BussCompany company = new Payv2BussCompany();
				company.setId(app.getCompanyId());
				company = payv2BussCompanyMapper.selectSingle(company);
				if (company != null) {
					app.setCompanyName(company.getCompanyName());
				}
			}
			//得到支付方式个数
			Long appId = app.getId();
			Payv2BussAppSupportPayWay payv2BussAppSupportPayWay = new Payv2BussAppSupportPayWay();
			payv2BussAppSupportPayWay.setAppId(appId);
			payv2BussAppSupportPayWay.setMerchantType(1);
			payv2BussAppSupportPayWay.setIsDelete(2);
			List<Payv2BussAppSupportPayWay> supportList = payv2BussAppSupportPayWayMapper.selectByObject(payv2BussAppSupportPayWay);
			app.setAppSupportPayWayNumber(supportList.size());
		}
		PageObject<Payv2BussCompanyApp> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	/**
	 * 根据代理平台ID获取该平台的线上商户
	 * @param map
	 * @return
	 */
	public Map<String, Object> getAppIds(Map<String, Object> map) {
		Map<String, Object> reqmap = new HashMap<String, Object>();
		Long platformId = Long.valueOf(map.get("platformId").toString());
		List<Long> appIds = new ArrayList<Long>();
		String platformType = map.get("platformAppType").toString();
		if ("1".equals(platformType)) {// 全部的APP
			Payv2PlatformWay platformWay = new Payv2PlatformWay();
			platformWay.setPlatformId(platformId);
			List<Payv2PlatformWay> list = payv2PlatformWayMapper
					.selectByObject(platformWay);
			List<Long> paywayIds = new ArrayList<Long>();
			for (Payv2PlatformWay way : list) {
				paywayIds.add(way.getPayWayId());
			}
			if (paywayIds.size() > 0) {
				Payv2BussAppSupportPayWay payWay = new Payv2BussAppSupportPayWay();
				payWay.setPayWayIds(paywayIds);
				List<Payv2BussAppSupportPayWay> pwayList = payv2BussAppSupportPayWayMapper
						.selectByPayWayIds(payWay);
				for (Payv2BussAppSupportPayWay way : pwayList) {
					appIds.add(way.getAppId());
				}

			}
		} else if ("2".equals(platformType)) { // 已添加的APP
			Pavy2PlatformApp pavy2PlatformApp = new Pavy2PlatformApp();
			pavy2PlatformApp.setPlatformId(platformId);
			pavy2PlatformApp.setChannelId(Long.valueOf(map.get("channelId") == null ? "" : map.get("channelId").toString()));
			List<Pavy2PlatformApp> list = pavy2PlatformAppMapper
					.selectByObject(pavy2PlatformApp);

			for (Pavy2PlatformApp platformApp : list) {
				appIds.add(platformApp.getAppId());
			}

		}
		if (appIds.size() > 0) {
			reqmap.put("appIds", appIds);
			if (map.get("appName") != null) {
				reqmap.put("appName", map.get("appName").toString());
			}
			if (map.get("curPage") != null) {
				reqmap.put("curPage", map.get("curPage").toString());
			}

		}
		return reqmap;
	}
	
	
	public PageObject<Payv2BussCompanyApp> selectByAppIds(Map<String, Object> map) {

		map = getAppIds(map);
		if(map.get("appIds") == null){
			PageObject<Payv2BussCompanyApp> pageObject = new PageObject(0, 1, 10);
			pageObject.setDataList(new ArrayList());
			return pageObject;
		}
		int totalData = payv2BussCompanyAppMapper.selectCountByAppIds(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2BussCompanyApp> list = payv2BussCompanyAppMapper.selectByAppIds(pageHelper.getMap());
		for (Payv2BussCompanyApp app : list) {
			Payv2BussCompany company = new Payv2BussCompany();
			company.setId(app.getCompanyId());
			company = payv2BussCompanyMapper.selectSingle(company);
			if (company != null) {
				app.setCompanyName(company.getCompanyName());
			}
			Pavy2PlatformApp platformApp = new Pavy2PlatformApp();
			platformApp.setAppId(app.getId());
			platformApp.setStatus(2);//已添加
			platformApp = pavy2PlatformAppMapper.selectSingle(platformApp);
			if (platformApp != null) {//1未添加2.已添加3.已取消
				app.setIsAddPlatform(2);
			}else{
				app.setIsAddPlatform(1);
			}
			
		}
		PageObject<Payv2BussCompanyApp> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	public String updateNewAppSecret(Long id, Long companyId) {
		Payv2BussCompanyApp t = new Payv2BussCompanyApp();
		t.setId(id);
		if(companyId != null){
			t.setCompanyId(companyId);
		}
		String app_secret = IdUtils.createRandomString(40);
		t.setAppSecret(app_secret);
		payv2BussCompanyAppMapper.updateByEntity(t);
		return app_secret;
	}

	/**
	 * 检查appKey是否有效
	 * 先查询app审核是否通过，再查询app所属商户状态是否通过
	 * @param map
	 * @return
	 */
	@Cacheable(
			region = "payv2BussCompanyAppService",
			namespace = "checkApp",
			fieldsKey = {"#appKey"},
			expire = 300
			)
	public Payv2BussCompanyApp checkApp(String appKey) {
		Map<String,Object> map = new HashMap<>();
		map.put("appKey", appKey);
		map.put("appStatus", 2);	//app审核通过状态
		map.put("isDelete", 2);		//未删除
		map.put("companyStatus", 2);	//商户审核通过状态
		return payv2BussCompanyAppMapper.queryByAppKey(map);
	}

	
	public List<Payv2BussCompanyApp> queryAppIdByCompanyId(Map<String, Object> map) {
		return 	payv2BussCompanyAppMapper.queryAppIdByCompanyId(map);
	}

}
