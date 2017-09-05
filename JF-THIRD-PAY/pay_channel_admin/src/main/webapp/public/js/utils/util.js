/**
 * 默认的ajax数据处理
 * @param data
 */
(function($){
	function handelObjByMenu(obj){
		obj.find("[menu]").each(function(){
			var pageId = $(this).attr("menu");
			if($("a[page-id='"+pageId+"']").size()==0){
				$(this).remove();
			}
		});
		return obj;
	}
	$.fn.extend({
		cloneObj:function(){
			return handelObjByMenu($(this.clone().html()));
		},
		appendByRole:function(e,pageId){	//添加按钮做模块依赖校验
			if(pageId){
				if($("a[page-id='"+pageId+"']").size()>0){
					return this.append(e);
				}else{
					return this;
				}
			}else{
				return this.append(handelObjByMenu($(e)));
			}
		}
	});
	
	$.getHtmlContent = function(url,callBackFunc){
		$.ajax({
			url : url,
			async :false,
			type : "POST",
			dataType : "HTML",
			success: callBackFunc,
			error:function(){
			
			}
		});
	}
})($);

function defaultAjaxHandler(data) {
//	if(data.ErrorCode==2000){
//		window.location.href="/user/loginPage?from="+window.location;
//		return;
//	}
//	result=data;
}

function defalutAjaxErrorhandler(data){
	
}
/**
 * ajax的工具方法，同步请求，请求完之后会把请求结果返回
 * @author yangwenguang
 * @date 2015-6-25
 * @param url 请求的连接，必须的参数
 * @param para 发送的数据，必选的参数
 * @param dataType 返回结果类型，选填参数，默认为json
 * @param async 请求等待方式，选填参数，默认为同步
 * @returns
 */
function ajaxFunction(url,para,dataType,async,successHandler,errorHandler){
	if(dataType==null){
		dataType="json";
	}
	if(async==null){
		async=false;
	}
	if(successHandler==null){
		successHandler=defaultAjaxHandler;
	}
	if(errorHandler==null){
		errorHandler=defalutAjaxErrorhandler;
	}
	var result;
	$.ajax({
		type: "POST",
		dataType: dataType,
		url: url,
		async: async,
		data: para,
		success: function(response){
			if(response!=401){
				successHandler(response);
			}
		},
		complete:function(response){
			if(response.responseText==401){
				window.top.location.href="/page/login.html";
			}
		},
		error:errorHandler
	});
	return result;
}
/**
 * 获取表单里面的数据，返回一个json形式的对象  
 * 参数 form 为表单对象，filler 为字符串  
 * 对于表单中空值的元素，默认忽略，也可以设置填充值：filler 来填充空值  
 * */
function getFormData (form) {  
 var data = {};  
	var d = {};
	var t = form.serializeArray();
	$.each(t, function() {
	  d[this.name] = this.value;
	});
 return data;  
}

/**
 *对Date的扩展，将 Date 转化为指定格式的String
 *月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
 *年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
 *例子： 
 *		(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
 *		(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
***/
Date.prototype.Format = function (fmt) { 
	var o = {
	 "M+": this.getMonth() + 1, 			//月份 
	 "d+": this.getDate(), 					//日 
	 "h+": this.getHours(), 				//小时 
	 "m+": this.getMinutes(), 				//分 
	 "s+": this.getSeconds(), 				//秒 
	 "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	 "S": this.getMilliseconds() 			//毫秒 
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
	if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};

function getTimeStr(time,fmt){
	fmt = fmt?fmt:"yyyy-MM-dd hh:mm:ss";
	if(time){
		return new Date(time).Format(fmt);
	}else{
		return "";
	}
}

/**
*  获取交易审核项、开户审核项入参字符串
* @param 容器div
*/
function forEachGetStr(elment){
	var pArr = $(elment).children("p");
	var resultStr = "";
	$.each(pArr,function(index,p){
		var str = "";
		var chineseName = $(p).children("i").text();
		var engName = $(p).children("input").val();
		
		str = "name:"+engName+",value:"+chineseName;
		
		if( resultStr.length > 0 ){
			resultStr += ";" + str;
		}else{
			resultStr += str;
		}
	});
	
	return resultStr;
}

/**
 * 填充table数据
 * @param table tbody对象
 * @param url 请求地址
 * @param param 请求参数
 * @param trModuleIdName 行模板idName
 * @param flag 是否重新“填充”分页
 * @param operateParam : 操作名数组[ ]
 * @param statusParam : 状态规则 ,格式：{typeValue: typeName}
 * @param updateTd :指定更新的td的Class数组 []
 * @param backFun : 传入的函数
 * @param backFun1 : 回调函数1
 * @param pagination : 分页元素
 * @param rule : 从结果集合获取数据的规则  默认为：1
 * **/
function fillInTableData(tbody,url,param,trModuleIdName,flag,operateParam,statusParam,updateTd,backFun,backFun1,pagination,rule){
	ajaxFunction(url,param,"JSON",true,function(result){
		if( !!result && result.resultCode == 200 ){
			var data = obtainPageListData(result,rule);
			var dataList = data.dataList;
			var pageData = data.pageData;    //一页多少数目
			var curPage  = data.curPage;     //当前页面索引

			if( !!backFun ){   //如果传入的函数则调用
				backFun(result);
			}
			/*循环数据集合*/
			$(tbody).html("");
			$.each(dataList,function(index,data){
				var indexNum = ( parseInt(curPage) - 1)*parseInt(pageData) + (index +1);   //索引值
				var trModule = $("#"+trModuleIdName).cloneObj();
				var tr  = fillTrModuleData(trModule,data,statusParam,operateParam,indexNum,updateTd);
				if( !!backFun1 ){
					tr = backFun1(tr,data,curPage);
				}
				
				$(tbody).append(tr);
			});
			/* 填充分页 */
			if( flag == true ){
				if( !pagination ){
					pagination = $(".pagination");
				}
				$(pagination).createPage({  //分页
					pageCount: data.totalPage,
					current:data.curPage,
					backFn: function(index){
						param.curPage = index;
						fillInTableData(tbody,url,param,trModuleIdName,false,operateParam,statusParam,updateTd,backFun,backFun1,pagination,rule);
					}
				});
			}
		}else{
			alert(result.message);
		}
	});
}
/**
 *	根据一定的规则获取页面数据
 * @param result: 接口返回的结果集合
 * @param rule: 取数据列表的规则
 * **/
function obtainPageListData(result,rule){
	var data;
	if( !rule ){
		rule = 1;
	}
	
	if( rule == 1 ){
		data = result.Data;
	}else if( rule == 2 ){
		data = result.Data.pageList
	}else if( rule == 3 ){
		data = result.Data.platformDataList;
	}
	return data;
}
/**
 * 填充表格
 * @param trElement :行对象元素
 * @param dataList : 行数据
 * @param statusParam : 状态规则 ,格式：{typeValue: typeName}
 * @param operateParam : 操作名数组[ ]
 * @param indexNum 序号{type:[operateArr]}
 * @param updateTd :指定更新的td的Class数组 []
 * */
function fillTrModuleData(trElement,dataList,statusParam,operateParam,indexNum,updateTd){
	var tdArr = trElement.find("td");    //td数组
	if( !updateTd ){
		updateTd = [];
	}
	if( !!tdArr && tdArr.length > 0 ){
		$.each(tdArr,function(index,td){
			var filterClass = ["operater","indexNum","status"];    //过滤的class名数组
			var className =  $(td).attr("class");            //td类名
			var value = dataList[className];    
			var textTd = $(td).text();     //td中的值
			
			if( $.inArray(className,filterClass) == -1){   
				var defaultType = $(td).attr("defaultType");
				
				if( defaultType == "str" ){         //string格式用空时默认为：空
					value = !!value ? value : ""; 
				}else if( defaultType == "%"){      //百分号格式：空时默认为：0%
					value  = (!!value ? value : 0)*100+"%";   //留存
				}else if( defaultType == "date" ){  //日期格式
					value = (new Date(value)).Format("yyyy-MM-dd");
					value = !!value ? value : 0;
				}else if( defaultType == "%%" ){
					value  = (!!value ? value : 0)+"%";  
				}else{
					value = !!value ? value : 0;
				}
				if( $.inArray( className,updateTd ) != -1 || updateTd.length <= 0){ //指定更新
					$(td).text( value );   //设定值
				}
			}
			
			if( className == "status" && !!statusParam){  //状态
				if( $.inArray( className,updateTd ) != -1 || updateTd.length <= 0){
					$(td).find(".choose-state").text( statusParam[value] );
				}
			}
			
			if( ( (className == "operater") || className.indexOf("operater") != -1 ) && !!operateParam ){  //操作
				if( $.inArray( className,updateTd ) != -1 || updateTd.length <= 0){  //指定更新
					var status;
					if( dataList['status'] != 0 ){
						status = !!dataList['status'] ? dataList['status'] : "undefine";
					}else{
						status = 0;
					}
					var operateArr = operateParam[status];
					$(td).text("");
					$.each(!!operateArr ? operateArr : [],function( index,operate ){
						var obj = $("#" + operate).cloneObj();
						$(td).append(obj);
					});
				}
			}
			
			if( className == "indexNum" ){   //序号
				if( $.inArray( className,updateTd ) != -1 || updateTd.length <= 0){  //指定更新
					$(td).text( indexNum );   
				}
			}
			
		});
	}
	return trElement;
}
/*******显示加载效果函数Start********/
/**
 * 功能: 显示加载效果
 * @param title:提示标题
 **/
function popupShowLoading(title){
	title = title || "请求加载中，请等待...";
	var loadContent = function(){
		if(title){
			$("#popup_loading_title").html(title);
		}
		$("#popup_loading_bg,#popup_loading_content").show();
	};
	if($("#ng_popup_loading").length < 1){
		var callBackFunc = function(data){
			$("body").append(data);
			loadContent();
		};
		$.getHtmlContent("/public/js/utils/popup/page/loading.html",callBackFunc);
	}
	else{
		loadContent();
	}
}
/***
 * 弹出框提示
 * **/
function popupConfirm(title,contentInfo,confirmTips,cancelTips,callBack){
	title = title || "提示";
	var loadContent = function(callBack){
		if(title){
			$("#popupConfirmHeader h3").html(title);
		}
	
		if( contentInfo ){
			$("#popupConfirmConent .contentInfo").html(contentInfo);
		}
		
		if( confirmTips ){
			$("#popupConfirmConent .popup-oper-modify").val(confirmTips);
		}
		
		if( cancelTips ){
			$("#popupConfirmConent .popup-oper-fail-cancel").html(cancelTips);
		}
		$("body").off("click","#popupConfirmConent .popup-oper-modify");
		$("body").on("click","#popupConfirmConent .popup-oper-modify",function(){
			if( !!callBack ){
				callBack(true);
			}
			$("#popupConfirmSection").hide();
		});
		
		$("body").off("click","#popupConfirmConent .popup-oper-fail-cancel");
		$("body").on("click","#popupConfirmConent .popup-oper-fail-cancel",function(){
			if( !!callBack ){
				callBack(false);
			}
			$("#popupConfirmSection").hide();
		});
		
		$("#popupConfirmSection,#popupConfirmMainConent").show();
	};
	if($("#popupConfirmSection").length < 1){
		var callBackFunc = function(data){
			$("body").append(data);
			loadContent(callBack);
		};
		$.getHtmlContent("/public/js/utils/popup/page/popupConfirm.html",callBackFunc);
	}else{
		loadContent(callBack);
	}
}
/**
 * 关闭加载效果
 * */
function popupCloseLoading(){
	if( $("#ng_popup_loading").length > 0 ){
		$("#popup_loading_bg,#popup_loading_content").hide();
	}
}

/**
 * 根据参数创建form对象并提交
 * @param method : 请求方式 默认为：post
 * @param url : 请求地址
 * @param param : 请求参数对象 (json 对象)
 * @param paramNameArr : 指定需要请求的参数名
 * **/
function createFormAndSubmit(method,url,param,paramNameArr){
	var form = $("<form></form>");
	
	method = !method ? "post" : method;
	paramNameArr  = !paramNameArr ? [] : paramNameArr;
	
	$(form).attr("method",method);
	$(form).attr("action",url);
	
	
	/**追加请求参数*/
	if( paramNameArr.length > 0 ){
		$.each(paramNameArr,function(index,name){
			var formInputModule =  $("#formInputModule").cloneObj();
			$(formInputModule).attr("name",name);
			$(formInputModule).attr("value",param[name]);
			$(form).append(formInputModule);
		});
	}else{
		for( var key in  param){
			var formInputModule =  $("#formInputModule").cloneObj();
			$(formInputModule).attr("name",key);
			$(formInputModule).attr("value",param[key]);
			$(form).append(formInputModule);
		}
	}
	$(form).submit();   //form
}

/***
 * 获取请求参数
 * @param paramName : 参数名称
 * */
function getParamValueByName(paramName) {
    var paramValue = "";
    var isFound = false;
    if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
        arrSource = unescape(this.location.search).substring(1, this.location.search.length).split("&");
        i = 0;
        while (i < arrSource.length && !isFound) {
            if (arrSource[i].indexOf("=") > 0) {
                if (arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase()) {
                    paramValue = arrSource[i].split("=")[1];
                    isFound = true;
                }
            }
            i++;
        }
    }
    return paramValue;
}

/***
 * 获取json对象的复制值
 * 	@param: jsonObj json对象
 * **/
function copyJsonToOther(jsonObj){
	var otherJson = {};
	for(var key in jsonObj){
		otherJson[key] = jsonObj[key];
	}
	return otherJson;
}
