package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.GroupDao;
import com.system.model.GroupModel;
import com.system.model.UserModel;

public class GroupServer
{
	public Map<String, Object> loadGroup(int pageIndex)
	{
		return new GroupDao().loadGroup(pageIndex);
	}
	
	public Map<String, Object> loadGroup(int pageIndex,String name)
	{
		return new GroupDao().loadGroup(pageIndex, name);
	}
	
	public List<GroupModel> loadAllGroup()
	{
		return new GroupDao().loadAllGroup();
	}
	
	public List<GroupModel> loadRightGroupByUserId(int userId)
	{
		return new GroupDao().loadRightGroupByUserId(userId);
	}
	
	public GroupModel loadGroupById(int id)
	{
		return new GroupDao().loadGroupById(id);
	}
	
	public boolean updateGroup(GroupModel model)
	{
		return new GroupDao().updateGroup(model);
	}
	
	public boolean addGroup(GroupModel model)
	{
		return new GroupDao().addGroup(model);
	}
	
	public List<Integer> loadRightByGroupId(int id)
	{
		return new GroupDao().loadRightByGroupId(id);
	}
	
	public void updateGroupRight(int groupId,List<Integer> right)
	{
		GroupDao dao = new GroupDao();
		dao.delGroupRightById(groupId);
		if(right!=null && !right.isEmpty())
			dao.addGroupRight(groupId, right);
	}
	public List<UserModel> loadGroupUsersById(int id){
		return new GroupDao().loadGroupUsersById(id);
	} 
	public void updateGroupUser(int groupId,List<Integer> userId)
	{
		GroupDao dao = new GroupDao();
		dao.delGroupUserById(groupId);
		if(userId!=null && !userId.isEmpty())
			dao.addGroupUser(groupId, userId);
	}
}
