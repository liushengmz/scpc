
package com.system.handler;

import java.io.IOException;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.system.cache.LvChannelCache;
import com.system.model.LvChannelModel;
import com.system.model.LvMrModel;
import com.system.model.LvRequestModel;
import com.system.server.LvMrServer;
import com.system.server.LvRequestServer;
import com.system.server.LvUserServer;
import com.system.util.StringUtil;

public abstract class baseCallbackFilter implements Filter
{

	String _errMsg;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		OnRequest((HttpServletRequest) request, (HttpServletResponse) response);
	}

	@Override
	public void destroy()
	{
	}

	abstract void OnRequest(HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 处理同步数据
	 * 
	 * @param orderId
	 *            系统产生的订单号 *必须填
	 * @param order3th
	 *            第三方支持订单号
	 * @param price
	 *            实支付金额，SP同步无金额时，填写-1
	 * @return
	 */
	boolean ProceModel(String orderId, String order3th, int price)
	{
		if (StringUtil.isNullOrEmpty(orderId))
		{
			setErrMsg("orderId is empty");
			return false;
		}

		LvMrServer mrSvr = new LvMrServer();
		if (mrSvr.existed(orderId))
		{
			setErrMsg("orderId existed");
			return false;
		}

		LvRequestModel orderInfo = new LvRequestServer().getRequest(orderId);

		if (orderInfo == null)
		{
			setErrMsg("orderId not found");
			return false;
		}

		LvMrModel mr = new LvMrModel();
		mr.setOrderId(orderId);
		mr.setChannel(orderInfo.getChannel());
		mr.setAppkey(orderInfo.getAppkey());
		mr.setLevelId(orderInfo.getLevel());
		mr.setOrderId(orderInfo.getOrderid());
		mr.setPayOrderId(order3th);
		mr.setPayType(orderInfo.getPayType());
		mr.setPayTypeId(orderInfo.getPayTypeId());

		mr.setPrice(price == -1 ? orderInfo.getPrice() : price);
		HoldProce(mr);

		mrSvr.insert(mr);
		if (mr.getId() == 0)
		{
			setErrMsg(" db error");
			return false;
		}
		new LvRequestServer().updateStatus(orderId, 4, true);
		new LvUserServer().UpdateLevel(orderInfo.getImei(),
				orderInfo.getLevel(), false);
		setErrMsg("ok");
		return true;
	}

	/**
	 * 扣量操作，直接更新status
	 * 
	 * @param mr
	 *            回调
	 * @return 是否正常处理
	 * 
	 */
	private boolean HoldProce(LvMrModel mr)
	{
		mr.setStatus(1);
		LvChannelModel chn = LvChannelCache
				.getDataByChannelAndKey(mr.getChannel(), mr.getAppkey());
		if (chn == null)
			return false;
		Random rnd = new Random();
		int r = rnd.nextInt(100);
		System.out.println(String.format(
				"[lv]: hold proce: setting value:%d, rnd value:%d",
				chn.getHoldPercent(), r));

		if (r < chn.getHoldPercent())
			mr.setStatus(0);
		return true;
	}

	public void write(ServletResponse response, String msg)
	{
		try
		{
			response.getWriter().write(msg);
		}
		catch (IOException e)
		{
		}
		System.out.println("blc->" + msg);
	}

	public String getErrMsg()
	{
		return _errMsg;
	}

	public void setErrMsg(String _errMsg)
	{
		this._errMsg = _errMsg;
	}

}
