<%@page import="com.system.util.Base64UTF"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.RightServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int type = StringUtil.getInteger(request.getParameter("type"), -1);

	String query = StringUtil.getString(request.getParameter("query"),"");

	int userId = StringUtil.getInteger(request.getParameter("id"), -1);
	
	if(type==6)
	{
		new UserServer().delUser(userId);
		response.sendRedirect("user.jsp");
		return;
	}
	
	if(type==7)
	{
		String[] groups = request.getParameterValues("groupid");
		List<Integer> list = new ArrayList<Integer>();
		
		if(groups!=null)
		{
			for(String group : groups)
			{
				list.add(StringUtil.getInteger(group, -1));
			}
		}
		
		new UserServer().updateUserGroup(userId, list);
		
		response.sendRedirect("usergroup.jsp?id=" + userId + "&msg=1&query=" + query);
		
		return;
	}

	//匹配原始密码
	if(type==1)
	{
		out.clear();
		String pwd = StringUtil.getString(request.getParameter("pwd"), "");
		UserModel model = new UserServer().getUserModelById(userId);
		if(model!=null)
		{
			if(model.getPassword().equalsIgnoreCase(StringUtil.getMd5String(pwd, 32)))
				out.print("OK");
			else
				out.print("NO");
		}
		return;
	}
	
	String oldPwd = StringUtil.getString(request.getParameter("old_pwd"), "");
	String pwd = StringUtil.getString(request.getParameter("pwd"), "");
	String nickName = StringUtil.getString(request.getParameter("nick_name"), "");
	String name = StringUtil.getString(request.getParameter("login_name"), "");
	String mail = StringUtil.getString(request.getParameter("mail"), "");
	String qq = StringUtil.getString(request.getParameter("qq"), "");
	String phone = StringUtil.getString(request.getParameter("phone"), "");
	UserModel model = new UserModel();
	
	model.setId(userId);
	model.setPassword(pwd);
	model.setMail(mail);
	model.setQq(qq);
	model.setNickName(nickName);
	model.setName(name);
	model.setPhone(phone);
	
	//更新用户信息 2包括修改密码 3不改密码
	if(type==2 || type==3)
	{
		model.setStatus(1);
		
		if(type==2)
		{
			if(new UserServer().updateUserWithPwd(model))
			{
				out.print("OK");
			}
			else
			{
				out.print("NO");
			}
		}
		else if(type==3)
		{
			if(new UserServer().updateUserWithoutPwd(model))
			{
				out.print("OK");
			}
			else
			{
				out.print("NO");
			}
		}
		
		RightServer.updateUserInfo(userId, model, type==2 ? true : false);
	}
	
	if(type==4)
	{
		int curUserId = ((UserModel)session.getAttribute("user")).getId();
		
		model.setCreateUserId(curUserId);
		
		new UserServer().addUser(model);
		response.sendRedirect("user.jsp?" + Base64UTF.decode(query));
		return;
	}
	
	if(type==5)
	{
		if(oldPwd.equalsIgnoreCase(StringUtil.getMd5String(pwd,32)))
		{
			model.setPassword(oldPwd);
		}
		else
			model.setPassword(pwd);
		
		model.setStatus(StringUtil.getInteger(request.getParameter("status"), 1));
		
		new UserServer().updateUser(model);
		
		response.sendRedirect("user.jsp?" + Base64UTF.decode(query));
		
		return;
	}
	
	
%>
