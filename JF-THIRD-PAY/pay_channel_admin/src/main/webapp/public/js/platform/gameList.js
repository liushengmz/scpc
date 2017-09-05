var gameCurpage = 1;   //当前页面
var gamePageData = 12;  //一页多少条数
var selectedAppId = getCookie("selectedAppId");
var gameListKeyParam = {};
/**
 * 游戏列表页面 
 */
var platGameParam = {
		"url":"/game/getGsGamePage.do",      			//请求的接口
		"flag": true,									//是否是第一次访问
		"appId": selectedAppId,							//应用id
		"curPage": 1,						            //当前页
		"pageData": gamePageData,					    //每页条数
		"createTimeBegin": "",                          //开始时间
		"createTimeEnd": "",                            //结束时间
		"keyword": "",
		"childSpUserId": ""                             //推广商id
}
$(function(){
	/*初始化页面上数据start*/
	/**初始化**/
	var DateObj =  new Date();
	var currentDate = (DateObj.getFullYear() +  "-" + ( ( DateObj.getMonth() + 1 ) < 10 ? "0"+ ( DateObj.getMonth() + 1 ) : ( DateObj.getMonth() + 1 ) ) + "-" + ( DateObj.getDate() < 10 ? "0"+ DateObj.getDate() : DateObj.getDate() ))
	platGameParam.createTimeBegin = currentDate;
	platGameParam.createTimeEnd = currentDate;
	$("#createTimeBegin,#createTimeEnd").val(currentDate);
	
	$("input[type='hidden'][name='appId']").val(selectedAppId);    //初始化表单提交数据
	var platformName = getCookie("platformName");
	$(".location_plat2").text(platformName+"平台游戏列表");
	
	popupShowLoading("数据请求中,请稍后...");
	showDataList(platGameParam,function(dataList,data){  //查询显示平台列表
		appendeGameHtml(dataList,data);
		var param1 = {
				"appId": selectedAppId,
				"createTimeBegin": currentDate,
				"createTimeEnd": currentDate,
				"keyword": "",
				"childSpUserId": ""
		};
		showGameListHeadDetail(param1);
			
	},function(index){
		platGameParam.curPage = index;
		platGameParam.flag = false;
		gameCurpage = index;
		showDataList(platGameParam,function(dataList){
			appendeGameHtml(dataList);
		});
	});
	/*初始化页面上数据end*/
	
	/*绑定事件start*/
	$("body").on("click","#exclusiveExportData",function(){  //导出数据
		var form = $(this).parent("form");
		var param = ["appId","createTimeBegin","createTimeEnd","keyword","childSpUserId"];
		
		$.each(param,function(index,name){   //向form表单插入数据
			var formInputModule =  $("#formInputModule").cloneObj();
			formInputModule.attr("name",name);
			formInputModule.val(platGameParam[name]);
			form.prepend(formInputModule);
		});
		
		form.submit();
	});
	
	$("body").on("keydown",function(event){   //回车搜索
		if(  event.keyCode == 13){ 
			$("#gameSearch").click();
		}
	});
	
	$(".location_plat1").on("click",function(){
		var url = $(this).attr("url_path");
		$.cookie("curPage","platform/exclusivePlatform",{ path:'/'});    //设置cookie
		$("#sideBox iframe",window.parent.document).attr("src","platform/exclusivePlatform.html");
	});
	
	$("#gameSearch").on("click",function(){     //点击搜索
		var keyWord = $("#keyWord").val();
		var createTimeBegin = $("#createTimeBegin").val();
		var createTimeEnd = $("#createTimeEnd").val();
		gameCurpage = 1;
		
		platGameParam.flag = true;
		platGameParam.curPage = 1;
		platGameParam.keyword = keyWord;
		platGameParam.createTimeBegin = createTimeBegin;
		platGameParam.createTimeEnd = createTimeEnd;

		popupShowLoading("数据请求中,请稍后...");
		showDataList(platGameParam,function(dataList,data){  //查询显示平台列表
			appendeGameHtml(dataList,data);
			var param1 = {}; 
			param1.appId = selectedAppId
			param1.createTimeBegin = createTimeBegin;
			param1.createTimeEnd = createTimeEnd;
			param1.keyword = keyWord;
			param1.childSpUserId = ""; 
			showGameListHeadDetail(param1);
			
		},function(index){
			platGameParam.curPage = index;
			gameCurpage = index;
			platGameParam.flag = false;
			showDataList(platGameParam,function(dataList){
				appendeGameHtml(dataList);
			});
		});
	});
	
	$("#gameTable").on("click",".gameToDetails",function(){  //点击跳转至消费详情
		var gameId = $(this).parents("tr").data("gameId");
		SetCookie("selectedGameId",gameId);
		$.cookie("curPage","platform/payDetails",{path:'/'});    //设置cookie
		$("#sideBox iframe",window.parent.document).attr("src","platform/payDetails.html");
	});
	/*绑定事件end*/
});

/**
 *  显示统计游戏信息
 * */
function showGameListHeadDetail(param){
	ajaxFunction("/game/getGsGameCount.do",param,"JSON",true,function(result){
		if( !!result && result.resultCode == 200 ){
			var data = result.Data;
			$(".gameIncome").text(data.totalMoney);
		}else{
			alert(result.message);
		}
	});
}

/**
 * 更新table里面的内容
 * @param dataList:  游戏列表数据
 * */
function appendeGameHtml(dataList,data){
	$("#gameTable tbody").html("");
	if( !!data ){
		$(".gameListNum").text(data.totalData);
	}
	if( !!dataList && dataList.length > 0 ){
		$.each(dataList,function(index,data){
			var tr = $("#gameTrModule").cloneObj();
			var payRate  = ( (!!data.payRate ? data.payRate : 0)*100 ).toFixed(2);      //付费渗透率
			var beExtant  = ( (!!data.beExtant ? data.beExtant : 0)*100 ).toFixed(2);   //留存
			
			tr.data("gameId",data.id);
			var stausStr = "";
			
			if( data.status == "1" ){   //上线
				tr.find(".choose-state").addClass("state-generated").text("已上线");
			}else{ //下线
				tr.find(".choose-state").addClass("state-over").text("已下线");
			}
			
			tr.find(".indexNum").text( parseInt(gameCurpage-1)*parseInt(gamePageData) + index + 1);     //序号
			tr.find(".id").text(data.id);                  						//中心ID 
			tr.find(".game_name").text(data.gameName);    						//游戏名称
			tr.find(".startUpNum").text(data.startUpNum);     					//平台启动次数
			tr.find(".newInsertUseNum").text(!!data.newInsertUseNum ? data.newInsertUseNum : 0 );     	//新增用户
			tr.find(".activeUserNum").text(!!data.activeUserNum ? data.activeUserNum : 0);     			//活跃用户
			tr.find(".loginUserNum").text(!!data.loginUserNum ? data.loginUserNum : 0);     			//登录用户
			tr.find(".payUserNum").text(!!data.payUserNum ? data.payUserNum : 0);     					//活跃付费用户
			tr.find(".payRate").text(payRate+"%");     													//付费渗透率
			tr.find(".payNum").text(!!data.payNum ? data.payNum : 0);     								//付费次数
			tr.find(".payTotal").text((!!data.payTotal) ? data.payTotal : 0);     						//付费金额
			tr.find(".beExtant").text(beExtant+"%");     												//留存
			
			$("#gameTable tbody").append(tr);
		});
	}
}
