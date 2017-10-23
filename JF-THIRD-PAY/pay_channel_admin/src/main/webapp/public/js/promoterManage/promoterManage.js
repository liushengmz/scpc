/*****
 * 推广商管理页面js
 ***/
var currentSelectedId = "-1";   //当前选中的推广商id 
var PromoterDataParam = {
		appId: "",    			//应用id
		parentId: "-1",  		//推广商id
		statisDateBegin: "",    //开始时间
		statisDateEnd: "",      //结束时间
		curPage:"1",
		pageData:6
}
$(function(){
	//推广商数目统计
	ajaxFunction("/spUser/queryChildCount.do",{},"JSON",true,function(result){
		if( !!result && result.resultCode == 200 ){
			var data = result.Data;
			$(".totalCount").text(data.count);                    //1级推广商总数 
			$(".nextPromoterCount").text(data.count1);            //下线推广商总数
			//查询显示一级推广商
			queryAndShowChild(-1,"",$(".menu_body > .promoter_body > .next_section"),false);
			$(".promoter_list_tree > .promoter_body > label").click();      
		}else{
			alert(result.message);
		}
	});
	
	var validForm = $(".addPromoterForm").Validform({   //添加推广商
		tiptype:3,
		ajaxPost:true,
		showAllError: true,
		callback:function(result){
			if(result.resultCode==200){
//				$(".menu_body > .promoter_body > i").click();
				queryAndShowChild("-1","",$(".promoter_list_tree > .promoter_body > .next_section"));
				alert("添加成功!");
			}else{
				alert(result.message);
			}
			$(".bg-popup,.popup-add-user").hide();
			$("form.addPromoterForm").find(".Validform_checktip").removeClass("Validform_right").removeClass(" Validform_wrong").html("");
			$("form.addPromoterForm").find("input[type='text']").val("").removeClass("Validform_error");
			$("form.addPromoterForm").find("input[type='password']").val("").removeClass("Validform_error");
		}
	});
	
	/*******绑定事件start*******/
	$("body").on("click",".popup-oper-search",function(){  //搜索左边树形图
		var companyName = $(".id-name-search").val();    //	推广商名称
		var parentId = "";                             //父级id
		
		queryAndShowChild(parentId,companyName,$(".promoter_list_tree > .promoter_body > .next_section"));
	});
	
	$("body").on("click",".out_data",function(){   //导出推广商数据
		createFormAndSubmit("POST","/spUser/exportUserStatistics.do",PromoterDataParam,[]);
	});
	
	$("body").on("click","tr.promoterTrModule .operater > a",function(){   //跳转至订单详情
		 var id = $(this).parents("tr.promoterTrModule").data("id");
		 var createTimeBegin = $(".statisDateBegin").val();
		 var createTimeEnd =  $(".statisDateEnd").val();
		 
		 window.location.href = "/page/promoterManage/orderData.html?id="+id+"&createTimeBegin="+createTimeBegin+"&createTimeEnd="+createTimeEnd;
	});
	
	$("body").on("click",".promoter_body > i",function(){   //树形推广商图
		var parent = $(this).parent(".promoter_body");
		var id = $(parent).attr("id");     					//推广商id
		var sectionObj = $(parent).find(".next_section");   //显示子推广商区域
		if( $(parent).hasClass("active") ){         //收起
			$(parent).removeClass("active").addClass("unactive");
			$(sectionObj).html("");
		}else if( $(parent).hasClass("unactive") ){ //展开
			$(parent).removeClass("unactive").addClass("active");
			queryAndShowChild(id,"",sectionObj);
			$(parent).children(".next_section").show();
		}
	});
	
	$("body").on("click",".menu_footer span",function(){     //添加推广商弹出框
		$(".bg-popup,.popup-add-user").show();
	});
	$("body").on("click",".pb-popup .close-btn",function(){  //关闭弹出框
		$("form.addPromoterForm").find(".Validform_checktip").removeClass("Validform_right").removeClass(" Validform_wrong").html("");
		$("form.addPromoterForm").find("input[type='text']").val("").removeClass("Validform_error");
		$("form.addPromoterForm").find("input[type='password']").val("").removeClass("Validform_error");
		$(".bg-popup,.popup-add-user").hide();
	});
	
	$("body").on("click",".popup-oper-create",function(){    //添加推广商提交
		validForm.ajaxPost(false,false,"/spUser/addSpUser.do");
	});
	
	$("body").on("click",".promoter_body > label",function(){     //点击推广商名查看相关数据
		var promoterBody = $(this).parent();
		var id = $(promoterBody).attr("id");        //点击的推广商id
		var activeTab = $(".switch div.actives");   //选中的tab
		var sectionName = $(activeTab).attr("page_url");
		
		/***添加选中样式**/
		$(this).parents("div.promoter_list_tree").find(".promoter_body > label").removeClass("active");
		$(this).addClass("active");
		
		$(activeTab).attr("tabSelectedId",id);
		currentSelectedId = id;
		
		if( sectionName == "firstSection" ){    
			showAndAppendPromoterData({"parentId":id},true);    //查询并显示推广商数据 
		}else if( sectionName == "secondSection" ){
			queryAndAppendPromoterData(id);
		}
	});
	$("body").on("click",".search-promoter-data",function(){     //搜索推广商的相关数据
		var appId = $(".appSelector").attr("app_id");                //应用id
		var statisDateBegin = $(".statisDateBegin").val();			 //开始时间
		var statisDateEnd = $(".statisDateEnd").val();				 //结束时间
		
		//查询并显示推广商数据
		showAndAppendPromoterData({"appId":appId,"statisDateBegin":statisDateBegin,"statisDateEnd":statisDateEnd});
	});
	
	$("body").on("click",".tabIndex",function(){              //tab切换
		var tabSelectedId = $(this).attr("tabSelectedId");    //
		if( !$(this).hasClass("actives") ){
			var page_url = $(this).attr("page_url");
			$(".tabIndex").removeClass("actives");
			$(this).addClass("actives").attr("tabSelectedId",currentSelectedId);
			$(".dataSection").hide();
			$("."+page_url).show();
			
			if( page_url == "firstSection" ){
				$(".data-search-conditions").show();
				if( !(tabSelectedId == currentSelectedId) ){
					showAndAppendPromoterData({"parentId":currentSelectedId});
				}
			}else{
				$(".data-search-conditions").hide();
				if( !(tabSelectedId == currentSelectedId) ){
					queryAndAppendPromoterData(currentSelectedId);
				}
			}
		}
	});
	
	$("body").on("click",".promoterDataModule > .operater > a",function(){   //推广商信息操作: 删除账号 ,恢复,停用,初始化密码
		var className = $(this).attr("class");
		var id = $(this).parents("tr.promoterDataModule").data("id");
		if( className == "deleteOperModule" || className.indexOf( "deleteOperModule" ) != -1 ){  			//删除账号
			promoterOperFunc(1,id);
		}else if( className == "restoreOperModule" || className.indexOf( "restoreOperModule" ) != -1 ){     //恢复
			promoterOperFunc(2,id)
		}else if( className == "cancelOperModule" || className.indexOf( "cancelOperModule" ) != -1 ){  		 //停用
			promoterOperFunc(3,id)
		}else if( className == "reIuputPwdOperModule" || className.indexOf( "reIuputPwdOperModule" ) != -1 ){  //初始化密码
			promoterOperFunc(4,id)
		}else if(className == "listOperModule" || className.indexOf( "listOperModule" ) != -1 ){  //初始化密码
			promoterOperFunc(5,id)
		}
	});
	//平台列表点击跳转
	$("body").on("click",".listOperModule",function(){
		var id = $(this).parent().parent().find(".id").html();
		window.location.href="/spAppUser/platformList.do?spUserId="+id
	})
	/*******绑定事件end*******/
});

/**
 *  功能：根据父亲id 查询并显示子推广商信息(下拉树形图)
 *  @param parentId :  父id   查询顶级推广商父亲id传-1
 *  @param companyName : 推广商名称
 *  @param section :   追加自推广商区域
 *  @param async : 是否异步 默认为异步 
 **/
function queryAndShowChild(parentId,companyName,section,async){
	$(section).html("");
	if( async == null ){
		async = true;
	}
	$(section).html("");
	ajaxFunction("/spUser/queryChild.do",{parentId:parentId,companyName:companyName},"JSON",async,function(result){
		if( !!result && result.resultCode == 200 ){
			var dataList = result.Data;
			
			if( !dataList || dataList.length <= 0 ){   //没有子推广商
				$(section).parent().removeClass("active").removeClass("unactive").addClass("newadd");
			}else{
				$.each(dataList,function(index,data){
					var treePormoterModule = $("#treePormoterModule").cloneObj();
					var status = data.status;    //状态：	0-停用、1-使用中、9-删除
					
					treePormoterModule.find("label").attr("title",data.companyName).html(data.companyName);
					treePormoterModule.attr({"parentId":data.parentId,"id":data.id});
					
					if( status == 0 ){
						treePormoterModule.find("label").addClass("delete");
					}else if( status == 9 ){
						treePormoterModule.find("label").addClass("cancel");
					}
					
					$(section).append(treePormoterModule);
				});
			}
			
		}else{
			alert(result.message);
		}
	});
}
/****
 * 查询并显示推广商数据
 * paramObj{parentId,appId,statisDateBegin,statisDateEnd}
 * @param parentId: 推广商id
 * @param appId： 应用id
 * @param statisDateBegin： 开始时间
 * @param statisDateEnd： 结束时间
 * @param appAppflag : 是否追加推广应用  默认不追加
 ***/
function showAndAppendPromoterData(paramObj,appAppflag){
	var paramObj = !paramObj ? {} : paramObj;
	
	PromoterDataParam.statisDateBegin = $(".statisDateBegin").val();
	PromoterDataParam.statisDateEnd =  $(".statisDateEnd").val();
	PromoterDataParam.appId = $(".appSelector").attr("app_id");
	
	for( var key in paramObj ){
		PromoterDataParam[key] = paramObj[key];
	}
	PromoterDataParam.curPage = 1;
	
	fillInTableData($(".promoterAllChildren"),"/spUser/spStatistics.do",PromoterDataParam,"promoterTrModule",true,"","","",function(result){
		//追加推广应用
		if( appAppflag == true ){
			appendPromoterApp(result,$(".appSelector"));
		}
		//显示选择推广商的推广数据
		appendSinglePromoterData(result);
	},function(tr,data){
		$(tr).data("id",data.id);
		return $(tr);
	},$(".allChildrenPagination"),"");
}
/***
 * 功能：显示选择推广商的推广数据
 * @param result : 数据集合
 * **/
function appendSinglePromoterData(result){
	var spStatistics = result.spStatistics;
	for( var key in spStatistics ){
		$(".detail_data_table").find("."+key).html(spStatistics[key]);
	}
}
/***
 * 功能：追加推广应用数据
 * @param result : 数据集合
 * @param select : 下拉元素
 * **/
function appendPromoterApp(result,select){
	var appArr = result.app;
	$(select).html("").append("<option app_id=''>全部</option>");
	$.each(appArr,function(index,app){
		var htmlObj = "<option app_id='"+ app.app_id +"'>"+ app.app_name +"</option>";
		$(select).append(htmlObj);
	});
}
/***
 * 功能： 查询推广商信息并追加到页面 
 * @param  parentId: 推广商id
 * 
 * **/
function queryAndAppendPromoterData(parentId){
	var paramData = {
		"parentId": parentId,
		 curPage:  1,
		 pageData: 12
	};
	var statusParam = {	"0":"停用","1":"使用中","9":"删除"};
	var operateParam = {
			"0":["deleteOperModule","restoreOperModule","reIuputPwdOperModule","listOperModule"],
			"1":["deleteOperModule","cancelOperModule","reIuputPwdOperModule","listOperModule"],
			"9":[]
	}
	fillInTableData($(".promoterDataBody"),"/spUser/queryChildInfo.do",paramData,"promoterDataModule",true,operateParam,statusParam,"","",function(tr,data,curPage){
		if( parseInt( curPage ) > 1 ){
			var indexNum = $(tr).find(".indexNum");
			$(indexNum).text( parseInt( $(indexNum).text() ) + 1 );
		}
		$(tr).data("id",data.id);
		return tr;
	},$(".promoterDataPagination"),"");
}

/***
 * 推广信息数据操作
 * type： 操作类型;   1:删除账号  2:恢复   3:停用   4: 初始化密码 5：应用列表
 * @param id :推广商id
 * @param operElement : 操作元素 可不传
 * **/
function promoterOperFunc(type,id,operElement){
	if( type == 1 ){   //删除账号
		popupConfirm("","确定删除?","","",function(flag){
			if( flag == true ){
				ajaxFunction("/spUser/deleteUser.do",{id:id},"JSON",true,function(result){
					if( !!result && result.resultCode == 200 ){
						queryAndAppendPromoterData(currentSelectedId);
						alert("删除成功");
					}else{
						alert(result.message);
					}
				});
			}
		});
	}else if( type == 2 ){ //恢复
		popupConfirm("","确定恢复?","","",function(flag){
			if( flag == true ){
				ajaxFunction("/spUser/disableUser.do",{id:id,status:1},"JSON",true,function(result){
					if( !!result && result.resultCode == 200 ){
						queryAndAppendPromoterData(currentSelectedId);
						alert("恢复成功！");
					}else{
						alert(result.message);
					}
				});
			}
		});
	}else if( type == 3 ){ //停用
		popupConfirm("","确定停用?","","",function(flag){
			if( flag == true ){
				ajaxFunction("/spUser/disableUser.do",{id:id,status:0},"JSON",true,function(result){
					if( !!result && result.resultCode == 200 ){
						queryAndAppendPromoterData(currentSelectedId);
						alert("停用成功！");
					}else{
						alert(result.message);
					}
				});
			}
		});
	}else if( type == 4 ){ //初始化密码
		var html = "<p class='popup-p contentInfo'>确定删除?</p>";
		html+= "<p class='popup-p contentInfo'>初始化密码为：123456</p>";
		popupConfirm("",html,"","",function(flag){
			if( flag == true ){
				ajaxFunction("/spUser/initPassword.do",{id:id},"JSON",true,function(result){
					if( !!result && result.resultCode == 200 ){
						alert("初始化密码成功！");
					}else{
						alert(result.message);
					}
				});
			}
		});
	}else if(type ==5){//平台列表
		
	}
}













