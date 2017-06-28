<%@page import="com.system.model.JsTypeModel"%>
<%@page import="com.system.server.JsTypeServer"%>
<%@page import="com.system.model.UserModel"%>
<%@page import="com.system.vmodel.SpFinanceShowModel"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.system.model.SettleAccountModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.system.server.SettleAccountServer"%>
<%@page import="com.system.server.SpServer"%>
<%@page import="com.system.model.SpModel"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int rightType=-1;//是否进行权限控制。-1不进行权限控制
	UserModel user=(UserModel)session.getAttribute("user");
	int userId=user.getId();
	String startDate = StringUtil.getString(request.getParameter("startdate"), StringUtil.getMonthHeadDate());
	String endDate = StringUtil.getString(request.getParameter("enddate"), StringUtil.getMonthEndDate());
	int spId = StringUtil.getInteger(request.getParameter("sp_id"), -1);
	int dateType = StringUtil.getInteger(request.getParameter("datetype"), -1);
	int export=StringUtil.getInteger(request.getParameter("load"), -1);
//	boolean isNotFirstLoad = StringUtil.getInteger(request.getParameter("load"), -1) == -1 ? false : true;
	List<SpModel> spList = new SpServer().loadSp();
	List<JsTypeModel> jsTypeList = new JsTypeServer().loadJsType();
	String display = "";
	Map<String, List<SpFinanceShowModel>> map = null;
//	if (spId > 0 && isNotFirstLoad) {
	if (spId > 0 && export==1) {
		SettleAccountServer accountServer = new SettleAccountServer();
		List<SettleAccountModel> list = accountServer.loadSpSettleAccountList(spId, startDate, endDate,dateType);
		if (list != null && list.size() > 0) {
			String spName = "";
			for (SpModel sp : spList) {
				if (sp.getId() == spId) {
					spName = sp.getShortName();
					break;
				}
			}
			response.setContentType("application/octet-stream;charset=utf-8");

			String fileName = spName + "(" + startDate + "_" + endDate + ").xls";

			String filePath = com.system.util.ConfigManager.getConfigData("EXCEL_DEMO")
					+ (1 == 1 ? "SpDemo.xls" : "CpDemo.xls");

			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			}

			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			accountServer.exportSettleAccount(1, dateType, spName, startDate, endDate, list,
					response.getOutputStream());

			out.clear();

			out = pageContext.pushBody();
		} else {
			display = "alert('没有相应的数据');";
		}
//	} else if (spId < 0 && isNotFirstLoad) {
	} else{	
		map = new SettleAccountServer().loadSpSettleAccountDataAll(startDate,endDate,spId,dateType,userId,rightType);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运营管理平台</title>
<link href="../wel_data/right.css" rel="stylesheet" type="text/css">
<link href="../wel_data/gray.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../sysjs/jquery-1.7.js"></script>
<script type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../sysjs/MapUtil.js"></script>
<script type="text/javascript" src="../sysjs/base.js"></script>
<script type="text/javascript" src="../sysjs/pinyin.js"></script>
<script type="text/javascript" src="../sysjs/AndyNamePickerV20.js"></script><link href="../css/namepicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
var spList = new Array();
<%
for(SpModel spModel : spList)
{
	%>
	spList.push(new joSelOption(<%= spModel.getId() %>,1,'<%= spModel.getShortName() %>'));
	<%
}
%>


	$(function()
	{
		$("#sel_date_type").val("<%= dateType %>");
		$("#sel_sp").val("<%= spId %>");

	});

	function subForm() 
	{

		if ($("#sel_date_type").val() == "-1") {
			alert("请选择日期结算类型");
			$("#selsel_date_type_sp").focus();
			return;
		}

		document.getElementById("exportform").submit();
	}
	
	function onSpDataSelect(joData)
	{
		$("#sel_sp").val(joData.id);
	}
</script>
<body style="padding-top: 40px">
	<div class="main_content">
			<div class="content" style="position: fixed; left: 0px; right: 0px">
			<form action="spexport_all.jsp" method="post" id="exportform">
				<dl>
				 
					<input type="hidden" value="2" name="load" />
					
					<dd class="dd01_me">开始日期</dd>
					<dd class="dd03_me">
						<input name="startdate" type="text" value="<%=startDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
					<dd class="dd01_me">结束日期</dd>
					<dd class="dd03_me">
						<input name="enddate" type="text" value="<%=endDate%>"
							onclick="WdatePicker({isShowClear:false,readOnly:true})">
					</dd>
						<dd class="dd01_me">SP</dd>
					<dd class="dd04_me">
						<select name="sp_id" id="sel_sp"  style="width: 120px" onclick="namePicker(this,spList,onSpDataSelect)">
							<option value="-1">全部</option>
							<%
								for (SpModel sp : spList)
								{
							%>
							<option value="<%=sp.getId()%>"><%=sp.getShortName()%></option>
							<%
								}
							%>
						</select>
					</dd>
					<dd class="dd01_me">结算类型</dd>
					<dd class="dd04_me">
						<select name="datetype" id="sel_date_type" title="选择结算类型" style="width:100px">
							<option value="-1">请选择</option>
							<%
								for(JsTypeModel jsTypeModel : jsTypeList)
								{
									%>
							<option value="<%= jsTypeModel.getJsType() %>"><%= jsTypeModel.getJsName() %></option>		
									<%
								}
							%>
						</select>
					</dd>

					<dd class="dd00_me"></dd>
					<dd class="dd00_me"></dd>
					<dd class="ddbtn" style="margin-left: 10px;" />
					<input type="button" value="查  询" onclick="subForm()">
					</dd>
				</dl>
			</form>
			</div>
			<br /><br />
			<table cellpadding="0" cellspacing="0">
				<thead>
				<br/>
					<tr>
						<td>SP名称</td>
						<td>业务线</td>
						<td>SP业务名称</td>
						<td>金额</td>
						<td>结算率</td>
						<td>合作方分成</td>
						<td>操作</td>
					</tr>
				</thead>
				<tbody>
					<%
						if (map != null)
						{
							SpFinanceShowModel sfsModel = null;
							List<SpFinanceShowModel> tmpList = null;
							for (String key : map.keySet())
							{
								tmpList = map.get(key);
								float totalAmount = 0;
								for (int i = 0; i < tmpList.size(); i++)
								{
									sfsModel = tmpList.get(i);
									totalAmount += (sfsModel.getAmount()
											* sfsModel.getJiesuanlv());
									if (i == 0)
									{
										out.println("<tr><td rowspan='" + tmpList.size()
												+ "'>" + key + "</td><td>"
												+ sfsModel.getOperatorName() + "</td><td>"
												+ sfsModel.getSpTroneName() + "</td><td>"
												+ sfsModel.getAmount() + "</td><td>"
												+ sfsModel.getJiesuanlv() + "</td><td>"
												+ StringUtil.getDecimalFormat(sfsModel.getAmount()
														* sfsModel.getJiesuanlv())
												+ "</td><td rowspan='" + tmpList.size()
												+ "'><a href='spexport_all.jsp?startdate="
												+ startDate + "&enddate=" + endDate
												+ "&sp_id=" + sfsModel.getSpId()
												+ "&load=1&datetype=" + dateType
												+ "'>导出</a></td></tr>");
									}
									else
									{
										out.println("<tr><td>" + sfsModel.getOperatorName()
												+ "</td><td>" + sfsModel.getSpTroneName()
												+ "</td><td>" + sfsModel.getAmount()
												+ "</td><td>" + sfsModel.getJiesuanlv()
												+ "</td><td>"
												+ StringUtil.getDecimalFormat(sfsModel.getAmount()
														* sfsModel.getJiesuanlv())
												+ "</td></tr>");
									}
								}
								out.println(
										"<tr style='background-color: #E0EEEE;'><td colspan='5' >合计</td><td>"
												+ StringUtil.getDecimalFormat(totalAmount) + "</td><td></td></tr>");
							}
						}
					%>
				</tbody>
			</table>
		</div>

	</div>
</body>
</html>