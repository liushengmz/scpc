package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.GroupRightModel;
import com.system.util.StringUtil;

public class GroupRightDao {
	
	public Map<String, Object> load(int pageIndex,String group){
		
		String query = "";
		
		if(!StringUtil.isNullOrEmpty(group)){
			query += " AND b.name LIKE '%"+group+"%' ";
		}
		
		String sqlcount = " count(*) ";
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING+" "
				+ " FROM " + Constant.DB_DAILY_CONFIG + ".tbl_group_group a LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_group b ON a.`group_id`=b.`id` where 1=1 "+query
				+ " ORDER BY a.create_date ASC ";
		
		
		
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		int count = (Integer)control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, sqlcount),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				});
		
		result.put("rows", count);
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.`id`,b.`name`,a.`group_list` list,a.`remark` ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<GroupRightModel> list = new ArrayList<GroupRightModel>();
						GroupRightModel model = null;
						while (rs.next()) {
							model = new GroupRightModel();
							model.setId(rs.getInt("id"));
							model.setName(rs.getString("name"));
							model.setGroupList(rs.getString("list"));
							model.setRemark(rs.getString("remark"));
							list.add(model);
						}
						return list;
					}
				}));
		
		
		
		return result;
	}
	
	public GroupRightModel loadById(int id){
		
		String sql = "SELECT a.`group_id` groupid,a.`group_list` list,a.remark "
				+ " FROM " + Constant.DB_DAILY_CONFIG + ".tbl_group_group a LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_group b ON a.`group_id`=b.`id` where a.id="+id;
		
		return (GroupRightModel)new JdbcControl().query(sql, new QueryCallBack() {
			GroupRightModel model = null;
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException {
				if(rs.next()){
					model = new GroupRightModel();
					model.setGroupId(rs.getInt("groupid"));
					model.setGroupList(rs.getString("list"));
					model.setRemark(rs.getString("remark"));
				}
				return model;
			}
		});
	}
	
	public String loadNameById(int id){
		String sql = "SELECT name FROM " + Constant.DB_DAILY_CONFIG + ".tbl_group WHERE id="+id;
		
		return (String)new JdbcControl().query(sql, new QueryCallBack() {
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException {
				if(rs.next())
					return rs.getString("name");
				return null;
			}
		});
	}
	
	public int loadIdByName(String name){
		String sql = "SELECT id FROM " + Constant.DB_DAILY_CONFIG + ".tbl_group WHERE name LIKE '%"+name+"%'";
		
		return (Integer)new JdbcControl().query(sql, new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt("id");
						return null;
					}
				});
	}
	
	@SuppressWarnings("unchecked")
	public List<GroupRightModel> loadGroup(){
		String sql = "SELECT id,NAME FROM " + Constant.DB_DAILY_CONFIG + ".tbl_group";
		
		
		return (List<GroupRightModel>)new JdbcControl().query(sql, new QueryCallBack() {
			List<GroupRightModel> list = new ArrayList<GroupRightModel>();
			GroupRightModel model = null;
			public Object onCallBack(ResultSet rs) throws SQLException {
				while (rs.next()) {
					model = new GroupRightModel();
					model.setGroupId(rs.getInt("id"));
					model.setName(rs.getString("name"));
					list.add(model);
				}
				return list;
			}
		});
	}
	
	public boolean addGroupRight(GroupRightModel model){
		String sql = "INSERT INTO `" + com.system.constant.Constant.DB_DAILY_CONFIG + "`.`tbl_group_group`(`group_id`,`group_list`,`remark`) "
				+ "VALUE("+model.getGroupId()+",'"+model.getGroupList()+"','"+model.getRemark()+"')";
		
		return new JdbcControl().execute(sql);
	}
	
	
	public boolean updateGroup(GroupRightModel model){
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_group_group` SET "
				+ "`group_id`="+model.getGroupId()+","
				+ "`group_list`='"+model.getGroupList()+"',"
				+ "`remark`='"+model.getRemark()+"' WHERE id="+model.getId();
		return new JdbcControl().execute(sql);
	}
	
	public boolean deleteGroup(int id){
		String sql = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_group_group` WHERE id="+id;
		return new JdbcControl().execute(sql);
	}
	
	public static void main(String[] args) {
		GroupRightDao dao = new GroupRightDao();
		System.out.println("test: "+dao.loadNameById(3));
		System.out.println("test2: "+dao.loadIdByName("SP"));
		GroupRightModel model = dao.loadById(3);
		System.out.println(model.getGroupId()+","+model.getGroupList());
	}
}
