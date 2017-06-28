package com.system.dao.xy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 翔天数据资费查询功能Dao
 */
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.model.xy.XyUserCpsModel;
import com.system.util.StringUtil;



public class XyUserDao {
	/**
	 * 该方法用于按特定条件查询数据库数据
	 * 关联表为：tbl_xypay_summer,tbl_xy_app,tbl_xy_channel
	 * 变量：sqlCount，用于计算条目数；query，用于关联查询；limit，用于分页查询
	 * @param startDate 数据起始时间
	 * @param endDate   数据结束时间
	 * @param appKey    应用ID
	 * @param channelKey渠道ID
	 * @param pageIndex 最大条目数
	 * @return 返回值为一个MAP
	 */
	public Map<String, Object> loadUserData(String startDate,String endDate,
			String appKey,String channelKey,int pageIndex){
		String sqlCount = " count(*) ";  
		String query = " a.*,c.appname "; 
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1)+","+Constant.PAGE_SIZE; 
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING+" FROM "
				+ "game_log.`tbl_xypay_summer` a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel b ON a.`channelid` = b.channel "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_app c ON a.`appkey` = c.appkey "
				+ "WHERE 1=1";
		sql+=" AND fee_date>='"+startDate+"' AND fee_date<='"+endDate+"' "; //用于拼接查询的计费时间的时间范围
		if(!StringUtil.isNullOrEmpty(appKey)){ //用于拼接模糊查询语句，模糊查询应用KEY
			sql+=" AND a.appkey LIKE '%"+appKey+"%' ";
		}
		if(!StringUtil.isNullOrEmpty(channelKey)){
			sql+=" AND a.channelid LIKE '%"+channelKey+"%' ";
		}
		sql+=" ORDER BY a.status,a.fee_date,a.appkey,a.channelid";
		
		final Map<String, Object> map = new HashMap<String, Object>();
		
		
		int count = (Integer)new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, sqlCount),
				new QueryCallBack() {
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException {
				if(rs.next()){
					return rs.getInt(1);
				}
				return 0;
			}
		});
		
		map.put("rows", count);
		
		count = (Integer)new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, 
				 " sum(data_rows), sum(amount),sum(show_amount) "),new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next()){
							map.put("data_rows", rs.getInt(1));
							map.put("amount", rs.getDouble(2));
							map.put("show_amount", rs.getDouble(3));
						}
						return 0;
					}
		});
		
		
		map.put("list", new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query)+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<XyUserCpsModel> list = new ArrayList<XyUserCpsModel>();
						
						XyUserCpsModel model = null;
						
						while (rs.next()) {
							model = new XyUserCpsModel();
							model.setId(rs.getInt("id"));
							model.setFee_date(rs.getString("fee_date"));
							model.setAppKey(rs.getString("appkey"));
							model.setChannelKey(rs.getString("channelid"));
							model.setAppName(rs.getString("appname"));
							model.setDataRows(rs.getInt("data_rows"));
							model.setAmount(rs.getDouble("amount"));
							model.setShowAmount(rs.getDouble("show_amount"));
							model.setStatus(rs.getInt("status"));
							list.add(model);
						}
						return list;
					}
				}));
		return map;
	}
	
	public boolean updateQdData(int id,double showDataRows)
	{
		String sql = "update game_log.`tbl_xypay_summer` set show_amount = " 
	            + showDataRows + " where id = " + id;
		return new JdbcGameControl().execute(sql);
	}
}
