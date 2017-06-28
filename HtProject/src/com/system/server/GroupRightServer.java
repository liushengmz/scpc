package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.GroupRightDao;
import com.system.model.GroupRightModel;

public class GroupRightServer {
	
	public Map<String, Object> load(int pageIndex,String group){
		return new GroupRightDao().load(pageIndex,group);
	}
	
	public String loadNameById(int id){
		return new GroupRightDao().loadNameById(id);
	}
	
	public int loadIdByName(String name){
		return new GroupRightDao().loadIdByName(name);
	}
	
	public List<GroupRightModel> loadGroup(){
		return new GroupRightDao().loadGroup();
	}
	
	public boolean addGroupRight(GroupRightModel model){
		return new GroupRightDao().addGroupRight(model);
	}
	
	public GroupRightModel load(int id){
		return new GroupRightDao().loadById(id);
	}
	
	public boolean updateGroup(GroupRightModel model){
		return new GroupRightDao().updateGroup(model);
	}
	
	public boolean deleteGroup(int id){
		return new GroupRightDao().deleteGroup(id);
	}
}
