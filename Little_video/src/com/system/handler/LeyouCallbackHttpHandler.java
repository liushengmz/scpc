
package com.system.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.system.util.StringUtil;

public class LeyouCallbackHttpHandler extends baseCallbackFilter
{

	@Override
	void OnRequest(HttpServletRequest request, HttpServletResponse response)
	{
		int price;
		String orderId, order3th;
		order3th = request.getParameter("payid");
		orderId = request.getParameter("orderid");
		price = StringUtil.getInteger(request.getParameter("amount"), -1);
		if (price == -1)
		{
			write(response, "error price");
			return;
		}
		if (super.ProceModel(orderId, order3th, price))
		{
			write(response, "{\"rescode\":0}");
		}
		else
		{
			write(response, "error " + super.getErrMsg());
		}
	}

}
