package com.system.server;

import java.util.Map;

import com.system.dao.SystemUserDao;
import com.system.model.SystemUserModel;

public class SystemUserServer {
	public Map<String, Object> load(int pageIndex,int groupId,String userName,String nickName,int userId){
		return new SystemUserDao().loadUser(pageIndex,groupId,userName,nickName,userId);
	}
	
	public String loadUserGroup(int userId){
		return new SystemUserDao().loadUserGroup(userId);
	}
	
	public boolean addUser(SystemUserModel model){
		return new SystemUserDao().adduser(model);
	}
	
	public SystemUserModel loadUserById(int id){
		return new SystemUserDao().loadUserById(id);
	}
	
	public boolean updataUser(SystemUserModel model){
		return new SystemUserDao().updateUser(model);
	}
	
	public boolean deleteUser(int id){
		return new SystemUserDao().deleteUser(id);
	}
}
