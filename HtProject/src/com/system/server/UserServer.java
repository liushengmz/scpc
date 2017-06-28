package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.UserDao;
import com.system.model.UserModel;
import com.system.util.StringUtil;

public class UserServer
{
	public boolean updateUserWithoutPwd(UserModel model)
	{
		return new UserDao().updateUserWithoutPwd(model);
	}
	
	public boolean updateUserWithPwd(UserModel model)
	{
		model.setPassword(StringUtil.getMd5String(model.getPassword(), 32));
		return new UserDao().updateUserWithPwd(model);
	}
	
	public UserModel getUserModelById(int id)
	{
		return new UserDao().getUserModelById(id);
	}
	
	public Map<String, Object> loadUser(int pageIndex,int groupId)
	{
		return new UserDao().loadUser(pageIndex, groupId);
	}
	
	public Map<String, Object> loadUser(int pageIndex,int groupId,String userName,String nickName)
	{
		return new UserDao().loadUser(pageIndex, groupId,userName,nickName);
	}
	
	public Map<String, Object> loadUser(int pageIndex,int groupId,String userName,String nickName,int userId)
	{
		return new UserDao().loadUser(pageIndex, groupId,userName,nickName,userId);
	}
	
	public boolean updateUser(UserModel model)
	{
		model.setPassword(StringUtil.getMd5String(model.getPassword(), 32));
		return new UserDao().updateUser(model);
	}
	
	public void addUser(UserModel model)
	{
		model.setPassword(StringUtil.getMd5String(model.getPassword(), 32));
		new UserDao().addUser(model);
	}
	
	public void delUser(int id)
	{
		UserDao dao = new UserDao();
		dao.delUserInGroup(id);
		dao.delUser(id);
	}
	
	public List<Integer> loadUserGroup(int id)
	{
		return new UserDao().loadUserGroup(id);
	}
	
	public List<UserModel> loadUserByGroupId(int groupId)
	{
		return new UserDao().loadUserByGroupId(groupId);
	}
	
	public void updateUserGroup(int userId,List<Integer> group)
	{
		UserDao dao = new UserDao();
		dao.delUserInGroup(userId);
		dao.updateUserGroup(group, userId);
	}
}
