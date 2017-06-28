<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.server.GroupServer"%>
<%@page import="com.system.model.GroupModel"%>
<%@page import="com.system.util.StringUtil"%>
<%@page import="com.system.server.Menu2Server"%>
<%@page import="com.system.model.Menu2Model"%>
<%@page import="com.system.model.Menu1Model"%>
<%@page import="com.system.server.Menu1Server"%>
<%@page import="com.system.server.MenuHeadServer"%>
<%@page import="com.system.model.MenuHeadModel"%>
<%@page import="java.util.List"%>
<%@page import="com.system.server.UserServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.model.ProvinceModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int pageIndex = StringUtil.getInteger(request.getParameter("pageindex"), 1);
	String name = StringUtil.getString(request.getParameter("encodeStr"), "");
	int type = StringUtil.getInteger(request.getParameter("type"), 1);
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	GroupServer groupServer = new GroupServer();
	GroupModel group = groupServer.loadGroupById(id);
	if(group==null)
	{
		response.sendRedirect("group.jsp");
		return;
	}
	List<MenuHeadModel> headList = new MenuHeadServer().loadMenuHeadList();
	List<Menu1Model> menu1List = new Menu1Server().loadMenu1List();
	List<Menu2Model> menu2List = new Menu2Server().loadMenu2List();
	String msg = "";
	String encodeStr = URLEncoder.encode(name,"GBK"); 
	if(StringUtil.getInteger(request.getParameter("msg"), -1)==1){
		msg = "alert('修改成功');";
		msg += "window.location.href = 'group.jsp?pageindex="+pageIndex+"&name="+encodeStr+"&type="+type+";'";
		}
	List<Integer> rightList = groupServer.loadRightByGroupId(id);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

	var rightArr = new Array();
	
	<%for(int right : rightList){%>rightArr.push(<%= right %>);<%}%>

	function subForm() 
	{
		document.getElementById("addform").submit();
	}
	
	$(function() 
	{
		resetForm();
		<%= msg %>
		
	});
	
	function resetForm()
	{
		$("[name='menu2id']").removeAttr("checked");
		
		for(i=0; i<rightArr.length; i++)
		{
			document.getElementById("menuid_" + rightArr[i]).checked = true;	
		}
	}
	
	function goToMain()
	{
		window.location.href = "group.jsp?pageindex=<%=pageIndex%>&name=<%=encodeStr%>&type=<%=type%>";
	}
	
</script>
<body>
	<div class="main_content">
		<div class="content" style="margin-top: 10px">
			<dl>
				<dd class="ddbtn" style="width:200px">
					<label>组别授权(<%= group.getName() %>)</label>
				</dd>
			</dl>
			<br /> <br />
			<dl>
				<form action="groupaction.jsp" method="post" id="addform">
					<input type="hidden" value="<%= group.getId() %>" name="id">
					<input type="hidden" value="1" name="type">
					<%
					for (MenuHeadModel headModel : headList)
					{
					%>
					<dd class="dd01_me" ><%=headModel.getName()%></dd>
					<%
						for(Menu1Model menu1Model : menu1List)
						{
							if(menu1Model.getMenuHeadId()==headModel.getId())
							{
					%>
					<dd class="dd00"></dd>
					<dd class="ddtxt03"></dd>
					<dd class="dd01_me"><%=menu1Model.getName()%></dd>
					<dd class="ddtxt02">			
					<%	
								int rowCount = 0;
								for(Menu2Model menu2Model : menu2List)
								{
									
									if(menu2Model.getMenu1Id() == menu1Model.getId())
									{
										rowCount++;
					%>
										<span style="display:inline-block;width: 150px;text-align: left;height: 30px;">
											<label title="<%= menu2Model.getRemark() %>" >
												<input type="checkbox" name="menu2id" id="menuid_<%= menu2Model.getId() %>" value="<%= menu2Model.getId() %>"  ></input>
												<%= menu2Model.getName()%>
											</label>
										</span>
					<%
										if(rowCount%7==0)
											out.print("</dd><dd class=\"dd00\"></dd><dd class=\"ddtxt03\"></dd><dd style=\"background: transparent;\" class=\"dd01_me\">&nbsp;&nbsp;</dd><dd class=\"ddtxt02\">");
									}
								}
					%>
					</dd>
					<%
							}
						}
					%>
					<div style="clear: both;"><br /></div>
					<%
					}
					%>
					
					<dd class="dd00"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 100px; margin-top: 10px">
						<input type="button" value="提 交" onclick="subForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="重 置" onclick="resetForm()">
					</dd>
					<dd class="ddbtn" style="margin-left: 32px; margin-top: 10px">
						<input type="button" value="返 回" onclick="goToMain()">
					</dd>

				</form>
			</dl>
		</div>

	</div>
</body>
</html>