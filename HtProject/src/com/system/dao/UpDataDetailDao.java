package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.util.StringUtil;
import com.system.vo.DetailDataVo;
import com.system.vo.UpDetailDataVo;

public class UpDataDetailDao {
	@SuppressWarnings("unchecked")
	public List<UpDetailDataVo> loadUpDetailDataByCondition(String table,String startDate,String endDate,int spId,int cpId,int spTroneId,int chkType,String keyWord)
	{
		
		String sql="SELECT sp.short_name as sp_short_name, api.id, trone.trone_name,trone.price,trone.orders,trone.trone_num,";
		sql+="cp.short_name as cp_short_name,cp.id cp_id,api.trone_id,trone_order_id,api_id, imei,imsi ,mobile,";
		sql+="pro.name,FirstDate,SecondDate,cp_verifyCode,api.status,sp_Linkid,api.api_exdata,";
		sql+="api.sp_exField,api.clientip  ,api.port,api.msg ,api.lac,api.cid,api.packagename,sp.id as sp_id,spTrone.name AS sp_trone_name,st.description";
		sql+=" FROM( SELECT * FROM daily_log.tbl_api_order_"+table+" where firstDate>='"+startDate+"' AND firstDate<='"+endDate+"' ORDER BY id DESC LIMIT 1000) api";
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone trone ON trone.id=api.trone_id";
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order cptrone ON cptrone.id=api.trone_order_id";
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp cp ON cp.id=cptrone.cp_id";
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_city city ON city.id=api.city";
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_province pro ON pro.id=city.province_id";
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp sp ON sp.id= trone.sp_id ";
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_api_order_status st ON api.status=st.code";
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone spTrone ON spTrone.id=trone.sp_trone_id WHERE 1=1";
		//sql+=" AND firstDate>='"+startDate+"' AND firstDate<='"+endDate+"'";
		//spId
		if(spId>0)
		{
			sql += " AND sp.id = " + spId;
		}
		if(cpId>0)
		{
			sql += " AND cp.id = " + cpId;
		}
		if(spTroneId>0)
		{
			sql += " AND trone.sp_trone_id = " + spTroneId;
		}
		if(!StringUtil.isNullOrEmpty(keyWord)){
			String chkTypeStr="mobile";
			if(chkType==1){
				chkTypeStr="mobile";
			}
			if(chkType==2){
				chkTypeStr="imei";
			}
			if(chkType==3){
				chkTypeStr="imsi";
			}
			if(chkType==4){
				chkTypeStr="trone_order_id";
			}
			sql+=" AND "+chkTypeStr+" in ("+ keyWord +")";
		}
		
		
		sql += " order by firstDate desc limit 0,1000";
		
		return (List<UpDetailDataVo>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<UpDetailDataVo> list = new ArrayList<UpDetailDataVo>();
				
				while(rs.next())
				{
					UpDetailDataVo vo = new UpDetailDataVo();
					vo.setSpName(StringUtil.getString(rs.getString("sp_short_name"), ""));
					vo.setId(rs.getInt("id"));
					vo.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					vo.setProvinceName(StringUtil.getString(rs.getString("name"), ""));
					vo.setImei(StringUtil.getString(rs.getString("imei"), ""));
					vo.setImsi(StringUtil.getString(rs.getString("imsi"), ""));
					vo.setMobile(StringUtil.getString(rs.getString("mobile"), ""));
					vo.setPrice(rs.getFloat("price"));
					vo.setCpName(StringUtil.getString(rs.getString("cp_short_name"), ""));
					vo.setFirstDate(StringUtil.getString(rs.getString("FirstDate"), ""));
					vo.setOrder(StringUtil.getString(rs.getString("orders"), ""));
					vo.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					vo.setPayCode(StringUtil.getString(rs.getString("trone_order_id"), ""));
					vo.setApiId(rs.getInt("api_id"));
					vo.setStatus(rs.getInt("status"));
					vo.setStatusName(StringUtil.getString(rs.getString("description"), ""));
					list.add(vo);
				}
				
				return list;
			}
		});
	}

}
