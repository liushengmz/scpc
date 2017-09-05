/**
 * 收益详情页面js
 * */
var orderDataParam = {   //订单数据参数
		"companyId": "",		  		//机构ID
		"createTimeBegin": "",	 		//查询开始时间
		"createTimeEnd": "",	  		//查询结束时间
		"proType": "",			  		//1理财,2基金,3证券,4借贷,5保险（开户不传或为‘’）默认为：开户
		"spUserId": "",			  		//推广商id
		"userAppId":"",					//应用ID
		"userMobile": "",				//手机号
		"curPage": 1,			  		//当前页面
		"pageData": 10           		//页面数据量
}

$(function(){
	
	orderDataParam.spUserId = $(".spuserid").val();
	orderDataParam.userAppId = $(".userappid").val();
	pulldownList()//所属机构下拉框填充
	queryAppendOrderData(true);
	/*绑定事件start*/
	$("body").on("click","#exclusiveExportData",function(){   //导出数据
		createFormAndSubmit("POST","/spUser/exportOrder.do",orderDataParam,[]);
	});
	//tab切换
	$("body").on("click",".mode-header .list-count-data-trend",function(){
		$(this).parent().find(".list-count-data-trend").removeClass("active");
		$(this).addClass("active");
		queryAppendOrderData();   //查询填充订单数据
	});
	//下拉框更改
	$("body").on("change",".condition-list select",function(){
		orderDataParam.companyId = $(this).val();
		queryAppendOrderData();   //查询填充订单数据
	})
	
	/*绑定事件end*/
});

//所属机构下拉框填充 &&总金额
function pulldownList(){
	$.ajax({
		type:"get",
		url:"/spUser/orderList.do",
		data:orderDataParam,
		async:true,
		success:function(data){
			if(!!data&&data.resultCode==200){
				var list = data.companyList,
					str="";
				for(var i=0;i<list.length;i++){
					str+='<option value="'+list.id+'">'+list.companyName+'</option>'
				}
				$(".condition-list select").append(str);
			}else{
				alert(data.message);
			}
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
			pulldownList();
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
		$.ajax({
			type:"get",
			url:"/spUser/orderList.do",
			data:orderDataParam,
			async:true,
			success:function(data){
				if(!!data&&data.resultCode==200){
					$(".totalMoney").text(data.moneySum);
				}else{
					alert(data.message);
				}
			}
		});
	}
	/***显示对应的区域***/
	$(".mode-paydetails").hide();
	$("."+sectionType).show();
}
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

