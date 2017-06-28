package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.ApiOrderModel;
import com.system.util.StringUtil;

public class RecordDao
{
	/*
	public void addRecord(ApiOrderModel model)
	{
		String sql = "insert into daily_log.tbl_api_order_" + StringUtil.getMonthFormat() + " (trone_order_id,api_id,"
				+ "imsi,imei,mobile,lac,cid,ExtrData,sdkversion,packagename,ip,clientip,nettype,sp_linkid,sp_exField,cp_verifyCode,FirstDate,port,msg,api_exdata,status,is_hidden)"
				+ " values(" + model.getTroneOrderId() + "," + model.getApiOrderId() + ",'" + model.getImsi() + "','" 
				+ model.getImei() + "','" + model.getMobile() + "'," + model.getLac() + "," + model.getCid() + ",'" + model.getExtrData() 
				+ "','" + model.getSdkVersion() + "','" + model.getPackageName() + "','" + model.getIp()+ "','" + model.getClientIp() + "','" 
				+ model.getNetType() + "','" + model.getSpLinkId() + "','" + model.getSpExField() + "','" + model.getCpVerifyCode() + "',now(),'" 
				+ model.getPort() + "','" + model.getMsg() + "','" + model.getApiExdata() + "'," + model.getStatus() + ","+ model.getIsHidden() +")";
		
		new JdbcControl().execute(sql);
	}
	*/
	
	
	public void addRecord(ApiOrderModel model)
	{
		String sql = "insert into daily_log.tbl_api_order_" + StringUtil.getMonthFormat() + " (trone_order_id,api_id,imsi,imei,mobile,lac,"
				+ "cid,ExtrData,sdkversion,packagename,ip,clientip,nettype,"
				+ "sp_linkid,sp_exField,cp_verifyCode,FirstDate,port,"
				+ "msg,api_exdata,status,is_hidden,trone_id,extra_param,iccid,user_agent,fake_mobile,city) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?)";
		
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		
		params.put(1, model.getTroneOrderId());
		params.put(2, model.getApiOrderId());
		params.put(3, model.getImsi());
		params.put(4, model.getImei());
		params.put(5, model.getMobile());
		params.put(6, model.getLac());
		params.put(7, model.getCid());
		params.put(8, model.getExtrData());
		params.put(9, model.getSdkVersion());
		params.put(10, model.getPackageName());
		params.put(11, model.getIp());
		params.put(12, model.getClientIp());
		params.put(13, model.getNetType());
		params.put(14, model.getSpLinkId());
		params.put(15, model.getSpExField());
		params.put(16, model.getCpVerifyCode());
		params.put(17, model.getPort());
		params.put(18, model.getMsg());
		params.put(19, model.getApiExdata());
		params.put(20, model.getStatus());
		params.put(21, model.getIsHidden());
		params.put(22, model.getTroneId());
		params.put(23, model.getExtraParams());
		params.put(24, model.getIccid());
		params.put(25, model.getUserAgent());
		params.put(26, StringUtil.getString(model.getFakeMobile(), ""));
		params.put(27, model.getCityId());
		
		model.setId(new JdbcControl().insertWithGenKey(sql, params));
	}
	
	
	public void updateRecord(ApiOrderModel model)
	{
		
		if(!StringUtil.isNullOrEmpty(model.getMsg()))
		{
			if(model.getMsg().length()>=200)
			{
				model.setMsg(model.getMsg().substring(0, 200));
			}
		}
		
		String sql = "update daily_log.tbl_api_order_" + StringUtil.getMonthFormat() + " set sp_linkid = ? ,sp_exField = ?,cp_verifyCode = ?,port = ?,"
				+ "msg=?,api_exdata=?,status=?,is_hidden=? where id = ?";
		
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, model.getSpLinkId());
		params.put(2, model.getSpExField());
		params.put(3, model.getCpVerifyCode());
		params.put(4, model.getPort());
		params.put(5, model.getMsg());
		params.put(6, model.getApiExdata());
		params.put(7, model.getStatus());
		params.put(8, model.getIsHidden());
		params.put(9, model.getId());
		
		new JdbcControl().execute(sql, params);
		
	}
	
	public void updateVeryCode(ApiOrderModel model,String tableName)
	{
		String sql = "update daily_log.tbl_api_order_" + tableName + " set cp_verifyCode = ?,SecondDate = now(), status = ? , sp_linkid = ? , api_exdata  = ?  where id = ? ";
		
		Map<Integer,Object> params = new HashMap<Integer, Object>();
		
		params.put(1,model.getCpVerifyCode());
		params.put(2, model.getStatus());
		params.put(3, model.getSpLinkId());
		params.put(4, model.getApiExdata());
		params.put(5, model.getId());
		
		new JdbcControl().execute(sql, params);
	}
	
	public ApiOrderModel getApiOrderById(String month,String id)
	{
		String sql = "select * from daily_log.tbl_api_order_" + month +" where id = " + id;
		
		return (ApiOrderModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					ApiOrderModel model = new ApiOrderModel();
					model.setId(rs.getInt("id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setTroneOrderId(rs.getInt("trone_order_id"));
					model.setApiOrderId(rs.getInt("api_id"));
					model.setImei(StringUtil.getString(rs.getString("imei"), ""));
					model.setImsi(StringUtil.getString(rs.getString("imsi"), ""));
					model.setMobile(StringUtil.getString(rs.getString("mobile"), ""));
					model.setLac(rs.getInt("lac"));
					model.setCid(rs.getInt("cid"));
					model.setExtrData(StringUtil.getString(rs.getString("ExtrData"), ""));
					model.setSdkVersion(StringUtil.getString(rs.getString("sdkversion"), ""));
					model.setPackageName(StringUtil.getString(rs.getString("packagename"), ""));
					model.setIp(StringUtil.getString(rs.getString("ip"), ""));
					model.setClientIp(StringUtil.getString(rs.getString("clientip"), ""));
					model.setNetType(StringUtil.getString(rs.getString("nettype"), ""));
					model.setSpLinkId(StringUtil.getString(rs.getString("sp_linkid"), ""));
					model.setSpExField(StringUtil.getString(rs.getString("sp_exField"), ""));
					model.setCpVerifyCode(StringUtil.getString(rs.getString("cp_verifyCode"), ""));
					model.setPort(StringUtil.getString(rs.getString("port"), ""));
					model.setMsg(StringUtil.getString(rs.getString("msg"), ""));
					model.setApiExdata(StringUtil.getString(rs.getString("api_exdata"), ""));
					model.setStatus(rs.getInt("status"));
					model.setExtraParams(StringUtil.getString(rs.getString("extra_param"), ""));
					model.setIsHidden(rs.getInt("is_hidden"));
					model.setIccid(StringUtil.getString(rs.getString("iccid"), ""));
					model.setUserAgent(StringUtil.getString(rs.getString("user_agent"), ""));
					
					return model;
				}
				
				return null;
			}
		});
	}
	
	
	public static void main(String[] args)
	{
		System.out.println("1234567".substring(0, 6));
	}
}
