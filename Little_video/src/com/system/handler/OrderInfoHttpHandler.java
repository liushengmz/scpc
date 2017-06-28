
package com.system.handler;

import java.util.Calendar;
import java.util.Date;

import com.system.api.AppOrderRequestModel;
import com.system.api.AppOrderResponseModel;
import com.system.api.BaseRequest;
import com.system.api.baseResponse;
import com.system.cache.LvChannelCache;
import com.system.cache.LvLevelCache;
import com.system.model.LvChannelModel;
import com.system.model.LvLevelModel;
import com.system.model.LvRequestModel;
import com.system.model.LvUserModel;
import com.system.server.LvRequestServer;
import com.system.server.LvUserServer;
import com.system.util.StringUtil;

public class OrderInfoHttpHandler extends BaseFilter
{

	private AppOrderResponseModel	result;
	static int						loopId	= 998;

	@Override
	protected baseResponse ProcessReuqest(String s)
	{

		AppOrderRequestModel m = BaseRequest.ParseJson(s,
				AppOrderRequestModel.class);

		result = new AppOrderResponseModel();

		if (m.getMethod() == com.system.constant.Constant.ORDER_METHOD_CREATE)
		{
			CreateOrder(m);
			return result;
		}
		else if (m
				.getMethod() == com.system.constant.Constant.ORDER_METHOD_UPDATE)
		{
			UpdateOrder(m);
			return new baseResponse();
		}

		return result;
	}

	private void UpdateOrder(AppOrderRequestModel m)
	{
		// if (m.getPayStatus() != 1)
		// return;
		String orderId = m.getOrderId();

		new LvRequestServer().updateStatus(orderId, m.getPayStatus(), false);
	}

	private void CreateOrder(AppOrderRequestModel m)
	{

		String imei = m.getImei();
		if (StringUtil.isNullOrEmpty(imei)
				|| StringUtil.isNullOrEmpty(m.getAppkey())
				|| StringUtil.isNullOrEmpty(m.getChannel()))
		{
			result.setStatus(com.system.constant.Constant.ERROR_MISS_PARAMETER);
			return;
		}
		LvUserModel user = new LvUserServer().getUserByImsi(m.getImei());
		if (user == null)
		{
			result.setStatus(com.system.constant.Constant.ERROR_UNKONW_USER);
			return;
		}

		LvChannelModel channel = LvChannelCache
				.getDataByChannelAndKey(m.getChannel(), m.getAppkey());
		if (channel == null)
		{
			result.setStatus(com.system.constant.Constant.ERROR_NO_PAY_CHANNEL);
			return;
		}
		// System.out.println(m.getLevelId() +" <--"+ user.getLevel());
		if (m.getLevelId() <= user.getLevel())
		{
			result.setStatus(com.system.constant.Constant.ERROR_ALREADY_PAID);
			return;
		}
		if (m.getLevelId() - user.getLevel() != 1)
		{
			result.setStatus(com.system.constant.Constant.ERROR_SKIP_LEVEL);
			return;
		}

		LvLevelModel levelInfo = LvLevelCache
				.getLvLevelByLevelId(m.getLevelId());
		if (levelInfo == null)
		{
			result.setStatus(
					com.system.constant.Constant.ERROR_UNKONW_PARAMETER);
			return;
		}
		String orderId = CreateOrderId();

		LvRequestModel order = new LvRequestModel();
		order.setImei(m.getImei());
		order.setOrderid(orderId);
		order.setPayType(m.getPayType());
		order.setPrice(levelInfo.getPrice());
		order.setLevel(m.getLevelId());
		order.setAppkey(m.getAppkey());
		order.setChannel(m.getChannel());
		order.setPayTypeId(
				m.getPayType() == 1 ? channel.getWxPay() : channel.getAliPay());
		new LvRequestServer().Insert(order);
		if (order.getId() < 1)
		{
			result.setStatus(com.system.constant.Constant.ERROR_DBASE_BUSY);
			return;
		}
		result.setCreateDate(new Date().getTime());
		result.setLevelId(levelInfo.getLevel());
		result.setPrice(levelInfo.getPrice());
		result.setLevelName(levelInfo.getRemark());
		result.setOrderId(orderId);
		result.setSdkId(order.getPayTypeId());
		result.setStatus(com.system.constant.Constant.ERROR_SUCCESS);
		System.out.println("done!");
	}

	private synchronized static String CreateOrderId()
	{ // 9999999
		Calendar cal = Calendar.getInstance();
		long ticks = (System.currentTimeMillis() % 100000000000L) * 100;
		ticks += loopId++;
		String order = String.format("%02d%X", cal.get(Calendar.MONTH) + 1,
				ticks);
		loopId = loopId % 100;
		return order;
	}

}
