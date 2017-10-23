/**
 * 收益详情页面js
 * */
var orderDataParam = {   //订单数据参数
		"companyId": "",		  		//机构ID
		"createTimeBegin": "",	 		//查询开始时间
		"createTimeEnd": "",	  		//查询结束时间
		"proType": "",			  		//1理财,2基金,3证券,4借贷,5保险（开户不传或为‘’）默认为：开户
		"spUserId": "",			  		//推广商id
		"userMobile": "",				//手机号
		"curPage": 1,			  		//当前页面
		"pageData": 10           		//页面数据量
}

$(function(){
	//获取
	var selectedSpUserId = getParamValueByName("id");                       //推广商id
	var createTimeBegin = getParamValueByName("createTimeBegin");			//开始时间
	var createTimeEnd = getParamValueByName("createTimeEnd");				//结束时间
	
	
	$("#createTimeBegin").val(createTimeBegin);
	$("#createTimeEnd").val(createTimeEnd);
	
	orderDataParam.spUserId = selectedSpUserId; 
	orderDataParam.createTimeBegin = createTimeBegin;
	orderDataParam.createTimeEnd = createTimeEnd;
	//查询填充订单数据
	queryAppendOrderData(true);
	
	/*绑定事件start*/
	$("body").on("click","#exclusiveExportData",function(){   //导出数据
		createFormAndSubmit("POST","/spUser/exportOrder.do",orderDataParam,[]);
	});
	
	$("body").on("click", "a#input_trigger_demo3,input#company", function(){	
		var state = $(this).siblings(".condition-combobox");
		if(state.is(":visible") == false) {
			state.show();
		} else {
			state.hide();
		}
	});
	// 下拉框内容选择
	$("body").on("click", ".condition-combobox .combobox-item", function(){
		$(this).parents(".list-count-time").find(".context-input").val($(this).text()).attr("companyId",$(this).attr("companyId"));
		$(this).parents(".condition-combobox").hide();
		
		queryAppendOrderData();   //查询填充订单数据
	});
	//tab切换
	$("body").on("click",".mode-header .list-count-data-trend",function(){
		$(this).parent().find(".list-count-data-trend").removeClass("active");
		$(this).addClass("active");
		queryAppendOrderData();   //查询填充订单数据
	});
	
//	$(".location_details1,.location_details2").on("click",function(){
//    	var page_url = $(this).attr("page_url");
//    	$.cookie("curPage",page_url,{path:'/'});    //设置cookie
//    	
//		$(".side-nav li",window.parent.frames['sideNav'].document).removeClass("current");
//		$(".side-nav li[page-url*='"+ (page_url.split("/"))[0] +"/']",window.parent.frames['sideNav'].document).addClass("current");
//    	
//    	if( page_url.indexOf(".html") != -1 ){
//    		$("#sideBox iframe",window.parent.document).attr("src",page_url);
//    	}else{
//    		$("#sideBox iframe",window.parent.document).attr("src",page_url+".html");
//    	}
//	});
	
	$("body").on("keydown",function(event){   //回车搜索
		if(  event.keyCode == 13){ 
			$("#payDetailsSearch").click();
		}
	});
	
	$("body").on("click","#payDetailsSearch",function(){  //点击搜索
		queryAppendOrderData();   //查询填充订单数据
	});
	/*绑定事件end*/
});
/**
 * 金额统计
 * */
function showPayDetailsHead(param){
	ajaxFunction("/game/getSpPayinfoCount.do",param,"JSON",true,function(result){
		if( !!result && result.resultCode == 200 ){
			var data = result.Data;
			$(".totalMoney").text(data.totalMoney);
		}else{
			alert(result.message);
		}
	});
}
/*****
 * 查询填充订单数据 
 * @param flag  是否初始化 
 * ***/
function queryAppendOrderData(flag){
	var sectionType = $(".list-count-data-trend.active").attr("section");
	var sectionObjInfo = getTabSectionRelatedArea(sectionType);
	
	if( flag == true ){
		var theadModuleObj = $("#"+sectionObjInfo.theadModuleIdName).cloneObj();   
		$("."+sectionType).find("thead").html("").append(theadModuleObj);
		
		fillInTableData(sectionObjInfo.tbody,"/spUser/orderList.do",orderDataParam,sectionObjInfo.trModuleIdName,true,"","","",function(result){
			$("."+sectionType).data("backuParam",copyJsonToOther( orderDataParam ) );
			//初始化所属机构下拉框
			initAffiliation(result);
			$("."+sectionType).find(".list-count .totalNum").text(result.Data.totalData);
			$("."+sectionType).find(".list-count .totalMoney").text(result.moneySum);
		},"",sectionObjInfo.pagination,"")
	}else{
		orderDataParam.companyId = $("#company").attr("companyId"); 						  //机构ID
		orderDataParam.createTimeBegin = $("#createTimeBegin").val();
		orderDataParam.createTimeEnd = $("#createTimeEnd").val();                             //结束时间
		orderDataParam.proType = $(".list-count-data-trend.active").attr("proType");        //1理财,2基金,3证券,4借贷,5保险;
//		orderDataParam.taskType =  $(".list-count-data-trend.active").attr("taskType")      //1 开户 2 购买 查询时候传参
		orderDataParam.userMobile = $("#keyWord").val();                                          //用户id
		
		sectionObjInfo = getTabSectionRelatedArea(sectionType);

		if( $(sectionObjInfo.tbody).html() !=""  && sectionObjInfo.flag == false ){  //已有数据，不需要重新请求
			
		}else{  //请求数据
			var theadModuleObj = $("#"+sectionObjInfo.theadModuleIdName).cloneObj();   
			$("."+sectionType).find("thead").html("").append(theadModuleObj);
			
			fillInTableData(sectionObjInfo.tbody,"/spUser/orderList.do",orderDataParam,sectionObjInfo.trModuleIdName,true,"","","",function(result){
				$("."+sectionType).data("backuParam",copyJsonToOther(orderDataParam) );
				$("."+sectionType).find(".list-count .totalNum").text(result.Data.totalData);
				$("."+sectionType).find(".list-count .totalMoney").text(result.moneySum);
			},"",sectionObjInfo.pagination,"")
		}
	}
	/***显示对应的区域***/
	$(".mode-paydetails").hide();
	$("."+sectionType).show();
}
/***
 *返回选中tab对应区域的相关信息
 *@param sectionType : openAccountSection:开户;financialSection:理财; fundSection:基金;insuranceSection:保险;securitySection:证券;borrowingSection:借贷;
 ****/
function getTabSectionRelatedArea(sectionType){
	var sectionObj = {};
	var trModuleIdName,theadModuleIdName;
	var flag = false;    //用户的参数是否变化
	var paramArr = ["companyId","createTimeBegin","createTimeEnd","spUserId","proType","userMobile"];
	var backuParam = $("." + sectionType).data("backuParam");
	
	if( !!backuParam ){
		$.each(paramArr,function(index,param){
			if( backuParam[param] != orderDataParam[param] ){
				flag = true;
			}
		})
	}else{
		flag = true;
	}
	
	if( !sectionType ){
		sectionType = $(".list-count-data-trend.active").attr("section");
	}
	 
	if( sectionType == "openAccountSection" ){   //开户
		trModuleIdName = "openAccountTrModule";
		theadModuleIdName = "openAccoutTheadMode1";
	}else{  //理财,基金,证券,借贷,保险;
		trModuleIdName = "otherTrModule";
		theadModuleIdName = "productTheadMode2";
	}
	
	sectionObj.flag = flag;
	sectionObj.theadModuleIdName = theadModuleIdName;
	sectionObj.trModuleIdName = trModuleIdName;
	sectionObj.tbody = $("." + sectionType).find("tbody");
	sectionObj.pagination = $("." + sectionType).find("div.pagination");
	
	return sectionObj;
}
/**
 *初始化所属机构下拉框
 *@result : 接口返回的结果集合
 * **/
function initAffiliation(result){
	var companyList = result.companyList;    //机构集合
	$.each(companyList,function(index,company){
		var html = "<li class='combobox-item' companyId='"+ company.id +"'>"+ company.companyName +"</li>";
		$(".condition-state").children("ul").append(html);
	});
}



