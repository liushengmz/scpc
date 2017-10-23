/***
 * 平台数据页面js
 * ***/
//var platDataPlatId = $.cookie("platId");      	  	     //平台id
//var selectedPromoterId = $.cookie("selectedPromoterId"); //推广商id
//var prePageName = "";  //上一页名称

//平台数据参数
var platDataDetailsParam = { 
		appId: $.cookie("selectedAppId"),        	//应用ID
		appVersionId: $.cookie("appVersionId"),		//版本ID
		createTimeBegin: "",						//开始时间
		createTimeEnd: "",							//结束时间
		curPage: 1,									//当前页
		pageData: 10								//每页条数
}
prePageName = getParamValueByName("prePageName");   //上一页名称
if( !!prePageName && prePageName == "platData" ){    //如果是从 推广下商的平台列表进来则要传入推广
	$(".location a.location-able").attr("page_url","promoter/platformList");
}

$(function(){
	/***日期初始化*****/
	 $('input[name="daterange"]').daterangepicker({
		 			separator:"至"
//		 			startDate:"",
//		 			endDate:"",
//		 			minDate:"",
//		 			maxDate:""
		},function(start,end){
			start = ( new Date(start) ).Format( "yyyy-MM-dd" );
			end = ( new Date(end) ).Format( "yyyy-MM-dd" );
			queryDetailSummayData(start,end);   //查询、显示数据明细、概要信息
	 });
	
	 queryDetailSummayData();   //查询、显示数据明细、概要信息
	 
    /*****绑定事件start*****/
	$("body").on("click","#input_trigger_demo3",function(){   //点击日历按钮
		$("input[name='daterange']").click();
		return false;
	});
	 
    $("body").on("click",".location a.location-able",function(){      //上面导航栏
    	var page_url = $(this).attr("page_url");
    	$.cookie("curPage",page_url,{path:'/'});    //设置cookie
    	
		$(".side-nav li",window.parent.frames['sideNav'].document).removeClass("current");
		$(".side-nav li[page-url*='"+ (page_url.split("/"))[0] +"/']",window.parent.frames['sideNav'].document).addClass("current");
    	
    	if( page_url.indexOf(".html") != -1 ){
    		$("#sideBox iframe",window.parent.document).attr("src",page_url);
    	}else{
    		$("#sideBox iframe",window.parent.document).attr("src",page_url+".html");
    	}
    });
    
    $(".explain-btn-data").on("mouseover",function(){    //查看提示语
    	var positionObj = $(this).offset();         //该元素相对于窗口的偏移位置
    	var width = $(this).width();                //该元素的宽度
    	var scrollTop = $(document).scrollTop();    //滚动条里顶部的距离
    	
    	var top = parseFloat(positionObj.top)-parseFloat(scrollTop) - 4;   
    	var left = parseFloat(positionObj.left)+parseFloat(width) + 2;
    	
    	$(this).addClass("blue").removeClass("gray");
    	
    	$(this).next().css({"top":top,"left":left,"display":"block"});
    }).on("mouseleave",function(){
    	$(this).addClass("gray").removeClass("blue");
    	$(".explain-wrap-data").hide();
    });
    
    $("body").on("click",".details-mode-header .export-oper",function(){     //平台数据明细导出
    	createFormAndSubmit("POST","/export/exportAppDataDetailed.do",platDataDetailsParam,["appId","appVersionId","createTimeBegin","createTimeEnd"]);
    });
    
    $("body").on("click",".dataDetails .operater a",function(){    //跳转至订单详情页面
    	var defaultDate = $(this).parents("tr").find(".statisDate").html();
    	
		$.cookie("curPage","platform/orderData.html?defaultDate="+defaultDate,{path:'/'});    //设置cookie
		$("#sideBox iframe",window.parent.document).attr("src","platform/orderData.html?defaultDate="+defaultDate);
    });
    /*****绑定事件end*****/
});


/***
 * 填充概要信息
 * @param  data： 数据
 * @param parentElement: 父元素
 * **/
function appendSummaryData(data,parentElement){
	var ps = $(parentElement).find("td").find("p:last-child");
	$.each(ps,function(index,item){
		var className = $(item).attr("class")
		$(item).html(data[className]);
	})
}

/***
 * 功能: 查询并显示概要、明细数据
 * @param startTime : 开始时间
 * @param endTime : 结束时间
 ***/
function queryDetailSummayData(startTime,endTime){
	var tbody = $(".dataDetailsBody");
	if( !startTime || !endTime ){
		timeZone = $("#date_demo3").val();        							//时间区域
		startTime = $.trim( timeZone.split("至")[0] );                       //开始时间
		endTime = $.trim( timeZone.split("至")[1] );							//结束时间
	}
	
	platDataDetailsParam.createTimeBegin = startTime;
	platDataDetailsParam.createTimeEnd = endTime;
	
	fillInTableData(tbody,"/UserTaskExecution/getPlatformData.do",platDataDetailsParam,"dataDetailsTrModule",true,"","","",function(result){
		appendSummaryData(result.Data,$(".summary-info"));
	},"","",3)
}
