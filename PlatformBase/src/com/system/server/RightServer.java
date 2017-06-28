package com.system.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.system.cache.RightConfigCacheMgr;
import com.system.model.Menu2Model;
import com.system.model.UserModel;
import com.system.model.UserRightModel;
import com.system.util.StringUtil;

public class RightServer
{
	public static int login(HttpSession session,String userName,String password)
	{
		String md5Pwd = StringUtil.getMd5String(password, 32);
		for(UserModel model : RightConfigCacheMgr.userListCache)
		{
			if(model.getName().equalsIgnoreCase(userName)&&model.getPassword().equals(md5Pwd))
			{
				if(existUserRight(model))
				{
					session.setAttribute("user", model);
					return 1;
				}
				else
					return 2;
			}
		}
		
		return -1;
	}
	
	public static boolean existUserRight(UserModel model)
	{
		for(UserRightModel rightModel : RightConfigCacheMgr.userRightCache)
		{
			if(rightModel.getUser()!=null && rightModel.getUser().equals(model))
			{
				if(rightModel.getMenu2List().size()>0)
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	public static UserRightModel loadUserRightModel(UserModel model)
	{
		for(UserRightModel rightModel : RightConfigCacheMgr.userRightCache)
		{
			if(rightModel.getUser()!=null && rightModel.getUser().equals(model))
			{
				return rightModel;
			}
		}
		return null;
	}
	
	public static LinkedHashMap<String, List<Menu2Model>> loadUserLeftMenu(UserModel user,int headId)
	{
		UserRightModel rightModel = loadUserRightModel(user);
		
		if(rightModel==null)
			return null;
		
		int menuHeadId = headId==0 ? rightModel.getMenuHeadList().get(0).getId() : headId;
		
		List<Menu2Model> listMenu2 = new ArrayList<Menu2Model>();
		
		for(Menu2Model model : rightModel.getMenu2List())
		{
			if(model.getMenuHeadId()==menuHeadId)
			{
				listMenu2.add(model);
			}
		}
		
		LinkedHashMap<String, List<Menu2Model>> rightMap = new LinkedHashMap<String, List<Menu2Model>>();
		
		for(Menu2Model model : listMenu2)
		{
			if(rightMap.keySet().contains(model.getMenu1Name()))
			{
				rightMap.get(model.getMenu1Name()).add(model);
			}
			else
			{
				List<Menu2Model> list = new ArrayList<Menu2Model>();
				list.add(model);
				rightMap.put(model.getMenu1Name(), list);
			}
		}
		
		return rightMap;
	}
	
	public static boolean hadRight(UserModel user,String url)
	{
		UserRightModel rightModel = loadUserRightModel(user);
		if(rightModel==null)
			return false;
		
		for(Menu2Model model : rightModel.getMenu2List())
		{
			if(model.getUrl().contains(url) || model.getActionUrl().contains(url))
				return true;
		}
		return false;
	}
	
	public static void updateUserInfo(int userId,UserModel model,boolean updatePwd)
	{
		for(UserModel tmp : RightConfigCacheMgr.userListCache)
		{
			if(tmp.getId()==model.getId())
			{
				if(updatePwd)
					tmp.setPassword(StringUtil.getMd5String(model.getPassword(),32));
				
				tmp.setNickName(model.getNickName());
				tmp.setPhone(model.getPhone());
				tmp.setQq(model.getQq());
				tmp.setMail(model.getMail());
				break;
			}
		}
	}
	
}
