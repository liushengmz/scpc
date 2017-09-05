package com.pay.business.merchant.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2Channel;
import com.pay.business.merchant.entity.Payv2ChannelRate;
import com.pay.business.merchant.entity.Payv2ChannelWay;
import com.pay.business.merchant.mapper.Payv2ChannelMapper;
import com.pay.business.merchant.mapper.Payv2ChannelRateMapper;
import com.pay.business.merchant.mapper.Payv2ChannelWayMapper;
import com.pay.business.merchant.service.Payv2ChannelService;
import com.pay.business.payv2.mapper.Payv2AgentPlatformMapper;
import com.pay.business.payv2.mapper.Payv2PlatformWayMapper;
import com.pay.business.util.ParameterEunm;

/**
 * @author cyl
 * @version 
 */
@Service("payv2ChannelService")
public class Payv2ChannelServiceImpl extends BaseServiceImpl<Payv2Channel, Payv2ChannelMapper> implements Payv2ChannelService {
	// 注入当前dao对象
    @Autowired
    private Payv2ChannelMapper payv2ChannelMapper;
    @Autowired
    private Payv2AgentPlatformMapper payv2AgentPlatformMapper;
    @Autowired 
    private Payv2PlatformWayMapper payv2PlatformWayMapper;
    @Autowired 
    private Payv2ChannelRateMapper payv2ChannelRateMapper;
    @Autowired 
    private Payv2ChannelWayMapper payv2ChannelWayMapper;

    public Payv2ChannelServiceImpl() {
        setMapperClass(Payv2ChannelMapper.class, Payv2Channel.class);
    }
    
 	public Payv2Channel selectSingle(Payv2Channel t) {
		return payv2ChannelMapper.selectSingle(t);
	}

	public List<Payv2Channel> selectByObject(Payv2Channel t) {
		return payv2ChannelMapper.selectByObject(t);
	}
	/**
	 * 渠道商登录
	 */
	public Payv2Channel loginAdmin(String userName, String password) {
			Payv2Channel admin = new Payv2Channel();
	        admin.setChannelStatus(1);
	        admin.setIsDelete(2);
	        admin.setChannelLoginName(userName);
	        admin.setChannelLoginPwd(password);
	        return admin = payv2ChannelMapper.selectSingle(admin);
	}

	public PageObject<Payv2Channel> payv2ChannelList(
			Map<String, Object> map) {
		int totalData = payv2ChannelMapper.getCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2Channel> list = payv2ChannelMapper.pageQueryByObject(pageHelper.getMap());
		PageObject<Payv2Channel> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	/**
	 * 增加渠道商、代理平台、支付通道
	 * @throws Exception 
	 */
	public Map<String,Object> addChannel(Map<String, Object> map) throws Exception {
		Map<String,Object> resultMap = new HashMap<>();
		// payWayList 格式：1-2-2,3-0-4,5-6-1
		if (map.get("payWayList") != null && !map.get("payWayList").toString().equals("")) {
			Payv2Channel pc = new Payv2Channel();
			pc.setChannelLoginName(map.get("channelLoginName").toString());
			
			List<Payv2Channel> list = payv2ChannelMapper.selectByObject(pc);
			if (list != null && list.size() > 0) {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "添加渠道商失败,渠道商账号已存在！");
				return resultMap;
			}
			pc = M2O(map);
			payv2ChannelMapper.insertByEntity(pc);
			
			
			String payWayList = map.get("payWayList").toString();
			// 解析
			String a[] = payWayList.split(",");
			//添加支付通道
			boolean isOk = addPayWay(a, pc.getId());
			if (isOk) {
				//成功
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);	
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, null);	
			}
		} else {
			//支付通道不能为空
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "支付通道不能为空！");
		}
		return resultMap;
	}
	
	/**
	 * 更新渠道商、支付通道
	 * @throws Exception 
	 */
	public Map<String,Object> updateChannel(Map<String, Object> map,Payv2Channel payv2Channel) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		
		Map<String, Object> param = new HashMap<>();
		if (!map.get("channelLoginName").toString().equals(payv2Channel.getChannelLoginName())) {
			param.put("channelLoginName", map.get("channelLoginName"));
			List<Payv2Channel> list = payv2ChannelMapper.selectByObject(M2O(param));
			if (list != null && list.size() > 0) {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "修改失败,渠道商账号已存在！");
				return resultMap;
			}
		}
		
		// payWayList 格式：1-2-2,3-0-4,5-6-1
		if (map.get("payWayList") != null && !map.get("payWayList").toString().equals("")) {
			Payv2ChannelWay pcw = new Payv2ChannelWay();
			pcw.setChannelId(payv2Channel.getId());
			// 先删除所有支付通道，再添加
			payv2ChannelRateMapper.deleteByChannelId(payv2Channel.getId());
			payv2ChannelWayMapper.deleteByEntity(pcw);
			
			String payWayList = map.get("payWayList").toString();
			// 解析
			String a[] = payWayList.split(",");
			//添加支付通道
			boolean isOk =addPayWay(a, payv2Channel.getId());
			if (isOk) {
				// 修改商户信息数据提交
				payv2ChannelMapper.updateByEntity(M2O(map));
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);
			} else {
				// 排序填写有问题
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE, null);	
				return resultMap;
			}
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, "支付通道不能为空！");
			return resultMap;
		}
		
		return resultMap;
	}
	
	/**
	 * @throws Exception 
	* @Title: addPayWay 
	* @Description: 添加通道
	* @param @param a
	* @param @param companyId    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public boolean addPayWay(String a[], Long channel) throws Exception {
		boolean isOk = true;
		Payv2ChannelWay pcw = null;
		Map<Long,Long> waySet = new HashMap<>();
		for (String string : a) {
			String b[] = string.split("-");
			// 支付通道
			Long payWayId = Long.valueOf(b[0]);
			// 支付通道路由 0表示没有
			Long rateId = Long.valueOf(b[1]);
			// 通道费
			Double payWayRate = Double.valueOf(b[2]);
			
			Long channelWayId = null;
			
			Date date = new Date();
			if(!waySet.containsKey(payWayId)){
				pcw = new Payv2ChannelWay();
				pcw.setChannelId(channel);
				pcw.setPayWayId(payWayId);
				pcw.setCreateTime(date);
				payv2ChannelWayMapper.insertByEntity(pcw);
				waySet.put(payWayId, pcw.getId());
				channelWayId = pcw.getId();
			}else{
				channelWayId = waySet.get(payWayId);
			}
			
			if(channelWayId==null){
				isOk = false;
				throw new Exception("添加错误");
			}
			Payv2ChannelRate pcr = new Payv2ChannelRate();
			pcr.setChannelWayId(channelWayId);
			pcr.setCreateTime(date);
			pcr.setPayWayRate(payWayRate);
			pcr.setRateId(rateId);
			
			payv2ChannelRateMapper.insertByEntity(pcr);
			
		}
		return isOk;
	}
}
