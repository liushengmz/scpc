/*
 * 平台列表
 **/
var exclusivePageData = 12;
var exclusivePlatformParam = {   
		"url":"/app/getSpAppPage.do",     		//请求的接口
		"flag": true,							//是否是第一次访问
		"curPage": 1,							//当前页
		"pageData": exclusivePageData,			//每页条数
		"createTimeBegin": "",					//开始时间
		"createTimeEnd": "",      				//结束时间
		"platformName":"", 						//平台名称
		"appName":""                            //APP名字
}
$(function(){
	/**初始化**/
//	var DateObj =  new Date();
//	var currentDate = (DateObj.getFullYear() +  "-" + ( ( DateObj.getMonth() + 1 ) < 10 ? "0"+ ( DateObj.getMonth() + 1 ) : ( DateObj.getMonth() + 1 ) ) + "-" + ( DateObj.getDate() < 10 ? "0"+ DateObj.getDate() : DateObj.getDate() ))
//	exclusivePlatformParam.createTimeBegin = currentDate;
//	exclusivePlatformParam.createTimeEnd = currentDate;
//	$("#createTimeBegin,#createTimeEnd").val(currentDate);
	
	popupShowLoading("数据请求中,请稍后...");
	showDataList(exclusivePlatformParam,function(dataList){  //查询显示平台列表
		appendeExclusiveHtml(dataList);
		showExclusiveNum();
	},function(index){
		exclusivePlatformParam.curPage = index;
		exclusivePlatformParam.flag = false;
		showDataList(exclusivePlatformParam,function(dataList){
			appendeExclusiveHtml(dataList);
		});
	});
	
	/**关闭/开启操作**/
	$("body").on("click","a.open,a.close",function(){
		var _this = $(this);
		var tips = "";
		var spAppUserId = _this.parents("tr").data("spAppUserId");
		var flag = _this.attr("flag");
		
		if(  flag == "1" ){
			tips = "开启";
		}else if(  flag == "0" ){
			tips = "关闭";
		}
		layer("","是否确定要"+ tips +"桌面图标快捷方式","","",function(isRcon){
			if( isRcon ){ //确定提交
				ajaxFunction("/app/updateGenerate.do",{"spAppUserId":spAppUserId,"generateStatus":flag},"JSON",true,function(result){   
					if( !!result && result.resultCode == 200 ){
						/**修改游戏中心状态**/
						_this.parents("tr").find(".isGenerate").text(tips);
						
						_this.hide();
						if(  flag == "1" ){
							_this.parents("tr").find(".close").show();    //关闭按钮显示
						}else if(  flag == "0" ){
							_this.parents("tr").find(".open").show();     //开启按钮显示
						}
					}else{
						alert(result.message);
					}
				});
			}
		});
	});
	/* 绑定事件start */
	$("body").on("click","a.platData",function(){   //跳到平台数据
		var appId = $(this).parents("tr").data("id");   			    //应用id
		var appVersionId = $(this).parents("tr").data("appVersionId");   //app版本id
		$.cookie("curPage","platform/platData",{path:'/'});    		    //设置cookie
		$.cookie("selectedAppId",appId,{path:'/'});
		$.cookie("appVersionId",appVersionId,{path:'/'});   
		$("#sideBox iframe",window.parent.document).attr("src","platform/platData.html");
	});
	
	$("body").on("click","#exclusiveExportData",function(){  //导出数据
		createFormAndSubmit("POST","/export/exportAppData.do",exclusivePlatformParam,["appName","createTimeBegin","createTimeEnd"]);
	});
	
	$("body").on("keydown",function(event){   //回车搜索
		if(  event.keyCode == 13){ 
			$("#exclusiveSearch").click();
		}
	});
	// 点击显示提示
	$(".explain-btn").on("click", function(){
		$(this).siblings().show();
	});
	
	// 离开提示区域 自身隐藏
	$(".explain-wrap").on("mouseleave", function(){
		$(this).hide();
	});
	
	// 点击显示隐藏管理列表
	$("#exclusiveTable").on("click",".data-manage",function(){
		$(".manage-list").hide();
		var list = $(this).siblings(".manage-list");
		if(list.is(":hidden")) {
			list.show();
		} else {
			list.hide();
		}
	});
	// 点击/右键空白区域 隐藏管理列表
	$(document).on("contextmenu", function(){
		$(".manage-list").hide();
	}).on("click", function(e){
		var target = $(e.target);
		if(target.closest(".data-manage").length == 0) {
			$(".manage-list").hide();
		}
	});
	
	$("#exclusiveSearch").on("click",function(){  //点击搜索
		var keyWord = $("#keyWord").val();
		var createTimeBegin = $("#createTimeBegin").val();
		var createTimeEnd = $("#createTimeEnd").val();
		
		
		exclusivePlatformParam.flag = true;
		exclusivePlatformParam.curPage = 1;
		exclusivePlatformParam.createTimeBegin = createTimeBegin;
		exclusivePlatformParam.createTimeEnd = createTimeEnd;
		exclusivePlatformParam.appName = keyWord;
		exclusivePlatformParam.platformName = keyWord;
		
		popupShowLoading("数据请求中,请稍后...");
		showDataList(exclusivePlatformParam,function(dataList){  //查询显示平台列表
			appendeExclusiveHtml(dataList);
			showExclusiveNum();
		},function(index){
			exclusivePlatformParam.curPage = index;
			exclusivePlatformParam.flag = false;
			
			showDataList(exclusivePlatformParam,function(){
				showDataList(exclusivePlatformParam,function(dataList){
					appendeExclusiveHtml(dataList);
				});
			});
		});
		
	});
	
	$("#exclusiveTable").on("click", "a.btn-down-bag",function(){  //下载包
		var fileMD5 = $(this).parents("tr").data("fileMD5");
		
		ajaxFunction("/app/getDownPath.do",{fileMD5:fileMD5},"JSON",true,function(result){
			if( !!result && result.resultCode == 200 ){
				window.location.href = result.Data.downPath;
			}else{
				alert(result.message);
			}
		});
	});
	
	
	// 生成包操作
	$("#exclusiveTable").on("click","a.btn-create-bag",function(){   //生成包弹出窗口
		//初始化点击生成包 的  推广商弹出框
		initPromoterWindow($(this).parents("tr"));
		$(".bg-popup, .popup-create-package").show();
	});
	
	$(".popup-create-package .popup-oper-cancel,.popup-create-package .close-btn").on("click",function(){ //关闭生成包窗口
		$(".bg-popup, .popup-create-package").hide();
	});
	
	$("input#oneKeyCreatePacke").on("click",function(){  //一键生成包
		var appVersionId = $(this).data("appVersionId");
		var spUserId = "";
		var elements = $(".popup-create-package .choose-list").find("i.active").parents("li.choose-row"); 
		
		var length = !!elements.length ? elements.length : 0;
		for( var i = 0; i < length; i++  ){
			var item = $(elements[i]).children("span");
			if( !!spUserId ){
				spUserId = spUserId +"," + item.attr("id");
			}else{
				spUserId = item.attr("id");
			}
		}
		
		if( !spUserId ){
			alert("请选择平台");
			return;
		}
		
		ajaxFunction("/app/makePackage.do",{appVersionId:appVersionId,spUserId:spUserId},"JSON",true,function(result){
			if( !!result && result.resultCode == 200  ){
				alert("生成包成功");
				$(".bg-popup, .popup-create-package").hide();
			}else{
				alert(result.message);
			}
		});
		
	});
	
	// 生成包窗口选择操作 start
	$(".popup-create-package").on("click", ".choose-all", function(){
		var _this = $(this);
		var chooseList = _this.parent().siblings("ul");
		
		if(!_this.hasClass("active")) {
			_this.addClass("active");
			chooseList.find(".choose-input[unbind!='unbind']").addClass("active");
		} else {
			_this.removeClass("active");
			chooseList.find(".choose-input[unbind!='unbind']").removeClass("active");
		}
	}).on("click", ".choose-list .choose-input[unbind!='unbind']", function(){
		var _this = $(this);
		var chooseAll = $(".choose-all");
		_this.toggleClass("active");
		chooseAll.removeClass("active");
	});
	// 生成包窗口选择操作 end 
	$("#exclusiveTable").on("click","li.delete",function(){   //删除
		var param = {
				appId: $(this).parents("tr").data("id"),
				spUserId: ""
		};
		var _this = $(this);
		ajaxFunction("/app/delApp.do",param,"JSON",true,function(result){
			if( !!result && result.resultCode == 200  ){
				_this.parents("tr").remove();
			}else{
				alert(result.message);
			}
		});
	});
	
	$("#exclusiveTable").on("click","li.out",function(){   //下架或恢复
		var type = $(this).attr("operatType");
		var trParent = $(this).parents("tr");
		var param = {
				appId: trParent.data("id"),
				type: type,
				spUserId: ""
		};
		ajaxFunction("/app/offTheShelves.do",param,"JSON",true,function(result){
			if( !!result && result.resultCode == 200  ){    
				if( type == "0" ){
					trParent.find("td.status").children("span").attr("Class","").addClass("choose-state state-over").text("下架");
					trParent.find("li.packages,li.downLoad").hide();
					trParent.find("li.out").attr("operattype","1").find("a").text("恢复");
				}else if( type == "1" ){
					trParent.find("td.status").children("span").attr("Class","").addClass("choose-state state-generated").text("推广中");
					trParent.find("li.packages,li.downLoad").show();
					trParent.find("li.out").attr("operattype","0").find("a").text("下架");
				}
			}else{
				alert(result.message);
			}
		});
	});
	
	$("#exclusiveTable").on("click","a.orderData",function(){   //调至订单数据
		var id = $(this).parents("tr").data("id");   //应用id  
//		var platformName = $(this).parents("tr").data("platformName");
//		//把应用id存入session
		$.cookie("selectedAppId",id,{path:'/'});    //设置cookie
		$.cookie("curPage","platform/orderData",{path:'/'});    //设置cookie
		$("#sideBox iframe",window.parent.document).attr("src","platform/orderData.html?appId="+id);
	});
	/* 绑定事件end */
});

/**
 * 更新table里面的内容
 * @param dataList:  平台列表数据
 * */
function appendeExclusiveHtml(dataList){
	var flag = false;   //更新标识
	$("#exclusiveTable tbody").html("");
	if( !!dataList && dataList.length > 0 ){
		$.each(dataList,function(index,data){
			var tr = $("#exclusiveTrMoudle").cloneObj();
			var stausStr = "";
			var num = ( parseInt(exclusivePlatformParam.curPage) - 1)*parseInt(exclusivePageData) + (index +1);
			var payRate = ( (!!data.payRate ? data.payRate : 0) * 100 ).toFixed(2);     //付费渗透率
			var beExtant = ( (!!data.beExtant ? data.beExtant : 0) * 100 ).toFixed(2);  //留存
			
			$(tr).data({"id":data.id,"fileMD5":data.fileMD5,"platformName":data.platformName,"appVersionId":data.newAppVersionId,"spAppUserId":data.spAppUserId});
			/*初始化操作*/
			var tr = initOperate($(tr),data.status,data.isGenerate);
			
			tr.find(".indexNum").text( num );           						//序号
			tr.find(".id").text(data.id);               						//中心ID 
			tr.find(".appName").text(data.appName);     						//app名字
			tr.find(".platformName").text(data.platformName); 					//平台名称
			
			if( data.updateFlag == 1){  //更新标识
				tr.addClass("update-app");
				flag = true;
			}else if( data.updateFlag == 0){
				tr.removeClass("update-app");
			}
			
			/**屏蔽掉删除、下架/恢复 */
			tr.find("li.out").remove();
			tr.find("li.delete").remove();
			
			$("#exclusiveTable tbody").append(tr);
		});
	}
	if( flag ){
		$(".updateFlagTips").show();
	}else{
		$(".updateFlagTips").hide();
	}
}
/**
 * 平台统计显示
 * */
function showExclusiveNum(createTimeBegin,createTimeEnd,keyword){
	var param = {
			createTimeBegin: exclusivePlatformParam.createTimeBegin,     //开始时间
			createTimeEnd: exclusivePlatformParam.createTimeEnd,		 //结束时间
			appName: exclusivePlatformParam.appName,					 //app名字
			platformName: exclusivePlatformParam.platformName			 //平台名字
	}
	ajaxFunction("/app/getSpAppCount.do",param,"JSON",true,function(result){
		if( !!result && result.resultCode == 200 ){
			var data = result.Data;
			$("span.centerTotal").text(data.appCount);
			$("span.recording").text(data.upCount);
			$("span.reupload").text(data.lowerCount);
		}else{
			alert(result.message);
		}
	});
}

/**
 * 初始化点击生成包 的  推广商弹出框
 * */
function initPromoterWindow(_this){
	var platformName = _this.data("platformName");
	var appVersionId = _this.data("appVersionId");
	
	$(".popup-create-package").find("h3").text("为"+platformName+"生成推广商平台包");
	$("#oneKeyCreatePacke").data("appVersionId",_this.data("appVersionId"));
	ajaxFunction("/spUser/childAllList.do",{appVersionId:appVersionId},"JSON",true,function(result){
		if( !!result && result.resultCode == 200 ){
			var dataList = result.Data.dataList;
			if( !!dataList && dataList.length > 0 ){
				$(".popup-create-package .choose-list").html("");
				$.each(dataList,function(index,data){
					var exclusivePromoters = $("#exclusivePromoters").cloneObj();
					var status = data.status;
					exclusivePromoters.find("span.choose-name").text(data.company_name).attr("id",data.id).attr("title",data.company_name);
					
					if( status == -1 ){
						exclusivePromoters.find(".choose-state").addClass('state-generated').text("未授权");
					}else if( status == 0 || status == 2 ){
						exclusivePromoters.find(".choose-state").addClass('state-over').text("已终止");
					}else if( status == 1 ){
						exclusivePromoters.find(".choose-state").addClass('state-over').text("已授权");
					}else if( status == 3 ){
						exclusivePromoters.find(".choose-state").addClass('state-over').text("生成中");
					}else if( status == 4 ){
						exclusivePromoters.find(".choose-state").addClass('state-over').text("未审核");
					}
					
					if( status == 0 || status == 1 || status == 2 || status == 3 ){
						exclusivePromoters.find(".choose-input").attr("unbind","unbind");
					}
					$(".popup-create-package .choose-list").append(exclusivePromoters);
				});
			}
		}else{
			alert(result.message);
		}
	});
}
/**
 * 初始化"操作"框
 * @param tr:操作的行对象
 * @param status: 状态  推广状态 0-下架，1-推广中 2-官方下架
 * @param isGenerate : 是否生成图标 1:是     0:否
 * */
function initOperate(tr,status,isGenerate){
	var isGenerate_tr = "";
	if( isGenerate == "1" ){
		isGenerate_tr = "开启";
	}else if( isGenerate == "0" ){
		isGenerate_tr = "关闭";
	}
	
	switch(status){
		case 0:   //下架
			tr.find(".choose-state").addClass("state-over").text("下架");
			tr.find("a.btn-create-bag").hide();
			tr.find("a.btn-down-bag").hide();
			break;   
		case 1:   //推广中
			tr.find(".choose-state").addClass("state-generated").text("推广中");
			if( isGenerate == "1" ){
				tr.find("a.close").show();
				tr.find("a.open").hide();
			}else if( isGenerate == "0" ){
				tr.find("a.close").hide();
				tr.find("a.open").show();
			}
			break; 
		case 2:   //官方下架
			tr.find(".choose-state").addClass("state-red").text("官方下架");
			tr.find("a.btn-create-bag").hide();
			tr.find("a.btn-down-bag").hide();
			break; 
		case 4:  //未审核
		tr.find(".choose-state").addClass("state-red").text("未审核");
		break; 
	}
	$(tr).find(".isGenerate").text(isGenerate_tr);    //游戏中心
	return tr;
}
