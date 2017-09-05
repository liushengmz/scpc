package com.system.util;

import java.util.ArrayList;
import java.util.List;

import com.system.model.ToBusinessChildOrderModel;
import com.system.model.ToBusinessOrderModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 根据下游的请求数据转成对象
 * @author Andy.Chen
 *
 */
public class JsonToClass
{
	public static ToBusinessChildOrderModel jsonToBusinesChildOrderModel(JSONObject jo)
	{
		
		ToBusinessChildOrderModel model = new ToBusinessChildOrderModel();
		
		model.setMobile(StringUtil.getString(jo.getString("mobile"),""));
		model.setSubOrderId(StringUtil.getString(jo.getString("suborderid"), ""));
		model.setFlowSize(jo.getInt("flowsize"));
		model.setRang(jo.getInt("rang"));
		
		return model;
	}
	
	public static ToBusinessOrderModel jsonToBusinessOrderModel(JSONObject jo)
	{
		ToBusinessOrderModel model = new ToBusinessOrderModel();
		
		model.setCpId(jo.getInt("cpid"));
		model.setOrderId(StringUtil.getString(jo.getString("orderid"), ""));
		model.setSign(StringUtil.getString(jo.getString("sign"), ""));
		
		List<ToBusinessChildOrderModel> childList = new ArrayList<ToBusinessChildOrderModel>();
		model.setOrderList(childList);
		
		JSONArray ja = jo.getJSONArray("orderdata");
		
		if(ja!=null && ja.size() > 0)
		{
			for(int i=0; i<ja.size(); i++)
			{
				JSONObject joChild = ja.getJSONObject(i);
				
				if(joChild!=null)
					childList.add(jsonToBusinesChildOrderModel(joChild));
			}
		}
		
		return model;
	}
}
