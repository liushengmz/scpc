/**
 * 默认的ajax数据处理
 * @author yangwenguang
 * @date 2015-11-24
 * @param data
 */
function defaultAjaxHandler(data) {
//	if(data.ErrorCode==2000){
//		window.location.href="/user/loginPage?from="+window.location;
//		return;
//	}
//	result=data;
}

function defalutAjaxErrorhandler(data){
	popupCloseLoading();   //关闭加载效果
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
			popupCloseLoading();   //关闭加载效果
			if(response.responseText==401){
				window.top.location.href="/page/login.html";
			}
		},
		error:errorHandler
	});
	return result;
}

//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { //author: meizz 
 var o = {
     "M+": this.getMonth() + 1, //月份 
     "d+": this.getDate(), //日 
     "h+": this.getHours(), //小时 
     "m+": this.getMinutes(), //分 
     "s+": this.getSeconds(), //秒 
     "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
     "S": this.getMilliseconds() //毫秒 
 };
 if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
 for (var k in o)
 if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
 return fmt;
};
//判断对象是否为空
function notNullBean(bean){
	if(bean !=undefined && bean!=null){
		return true;
	}
	return false;
}
//判断字符串是否为空
function notNullStr(str){
	if(str!=undefined && str!=null  && $.trim(str)!=""){
		return true;
	}
	return false;
}
//判断数组是否为空
function notNullArray(array){
	if(array !=undefined && array!=null && array.length>0){
		return true;
	}
	return false;
}

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
})($);

/**
 * 字符串时间转时间 【针对ie11不支持 new Date(str)】
 * @param {} str  2016-01-05 20:28  或  2016-01-05 20:28:05
 * @return {} 时间类型 
 */
function handleStringToDate(str){
	if(str){
		var dateArray=str.split(" ");
		var dateArrayYear=dateArray[0].split("-");
		var dateArrayHH=dateArray[1].split(":");
		return new Date(dateArrayYear[0],parseInt(dateArrayYear[1])-1,dateArrayYear[2],
						dateArrayHH[0],dateArrayHH[1],dateArrayHH.lenght>2?dateArrayHH[2]:0); 
	}else{
		return new Date();
	}
}
/*******前端格式校验  end*****/
/**
 * 验证用户名
 * @param: userName 用户名
 */
function regUserName(userName,tips){
	tips = !!tips ? tips : "用户名";
	if( !!userName ) {
		if( $.trim( userName ).length < 2 ){
			alert(tips+"长度必须两位以上");
			return false;
		}else{
			
		}
	}else{
		alert(tips+"不能为空");
		return false;
	}
	
	return true;
}
/**
 * 验证手机号码
 * @param: mobile 手机格式校验
 */
function regMobile(mobile){
	var reg = /^1[3|4|5|7|8]\d{9}$/;
	if( !!mobile ){
		if( !(reg.test(mobile)) ){
			alert("手机号码格式不对！");
			return false;
		}else{
		}
	}else{
		alert("手机号不能为空");
		return false;
	}
	return true;
}
/**
 * 验证验证码
 * @param: valicode 验证码格式校验
 */
function regValicode(valicode){
	if( !!valicode ){
		
	}else{
		alert("验证码不能为空");
		return false;
	}
	return true;
}

/**
 * 验证登录密码
 * @param: pwd 登录密码格式校验
 */
function regLoginPwd(pwd,tips){
	var reg = /^[0-9a-zA-Z]{6,18}$/;
	tips = !!tips ? tips : "密码";
	if( !!pwd ){
		if( !(reg.test(pwd)) ){
			alert(tips+"长度必须在6~18之间，且不能包括特殊字符");
			return false;
		}
	}else{
		alert(tips+"不能为空");
		return false;
	}
	return true;
}
/*******提示start*****/


/**
 * 填充表格
 * @param trElement :行对象元素
 * @param dataList : 行数据
 * @param statusParam : 状态规则 ,格式：[{typeValue: typeName,"className":xxxx}.......]
 * @param operateParam : 操作名数组{"relatveClassName":xxx,"1":value}
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
			
			$.each(statusParam,function(index,item){    //值可变的列  如果：status
				if( className == item["className"] && !!item){ 
					if( $.inArray( className,updateTd ) != -1 || updateTd.length <= 0){
//						$(td).find(".choose-state").text( item[value] );
						$(td).text( item[value] );
					}
				}
			});
			
			if( $(td).hasClass("operater") && !!operateParam ){  //操作
				if( $.inArray( className,updateTd ) != -1 || updateTd.length <= 0){  //指定更新
					var status;
					var relativeClassName = operateParam["relativeClassName"];
					if( dataList[relativeClassName] != 0 ){
						status = !!dataList[relativeClassName] ? dataList[relativeClassName] : "undefine";
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

/**
 * 查询数据并填充表格
 * */
function showDataList(param,func1,func2){
	if( !param.flag ){
		param.flag = false;
	}
	ajaxFunction(param.url,param,"JSON",true,function(result){
		if( !!result && result.resultCode == 200 ){
			var data = result.Data;
			var dataList = data.dataList;
			
			if( param.flag == true ){
				$(".pagination").createPage({  //分页
					pageCount: data.totalPage,
					current:data.curPage,
					backFn: function(index){
						func2(index);
					}
				});
			}
			
			if( !!func1 ){
				func1(dataList,data);
			}
		}else{
			alert(result.message);
		}
	});
}

/**
 * 填充table数据
 * @param table tbody对象
 * @param url 请求地址
 * @param param 请求参数
 * @param trModuleIdName 行模板idName
 * @param flag 是否重新“填充”分页
 * @param operateParam : 操作名数组{"relatveClassName":xxx,"1":value}
 * @param statusParam : 状态规则 ,格式：[{typeValue: typeName,"className":xxxx}.......]
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
					tr = backFun1(tr,data);
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
	}else if( rule == 4 ){
		data = result.list;
	}
	return data;
}

/**
 * 信息提示框
 * @param title:提示框标题信息 默认：“提示”
 * @param content:提示信息内容 
 * @param cancelTips 取消按钮信息 默认：“取消”
 * @param okTips 确定按钮信息  默认：“确定”
 * @param backFun:回调函数
 * */
function layer(title,content,okTips,cancelTips,backFun){
	title = !!title ? title : "提示";
	okTips = !!okTips ? okTips : "确定";
	cancelTips = !!cancelTips ? cancelTips : "取消";
	
	$(".popup-info-tip").find("h3").text(title);
	$(".popup-info-tip").find(".contentInfo").text(content);
	
	$(".popup-info-tip").find(".popup-oper-fail-cancel").val(cancelTips);
	$(".popup-info-tip").find(".popup-oper-modify").val(okTips);
	
	$(".popup-info-tip").find(".popup-oper-modify").unbind("click").on("click",function(){   //确定
		if( !!backFun ){
			$(".popup-info-tip").hide();
			var length = $(".bg-popup .pb-popup:visible").length;
			length < 1 && $(".bg-popup").hide();
			backFun(true);
		}
	});
	
	$(".popup-info-tip").find(".popup-oper-fail-cancel").unbind("click").on("click",function(){   //取消
		if( !!backFun ){
			$(".popup-info-tip").hide();
			var length = $(".bg-popup .pb-popup:visible").length;
			length < 1 && $(".bg-popup").hide();
			backFun(false);
		}
	});
	
	$(".popup-info-tip").find(".close-btn").unbind("click").on("click",function(){   //关闭
		$(".popup-info-tip").hide();
		var length = $(".bg-popup .pb-popup:visible").length;
		length < 1 && $(".bg-popup").hide();
	});
	$(".bg-popup").show();
	$(".popup-info-tip").show().css({"z-index":9999});
}


function showErrorTips(title,tips,sureFun,cancelFun){
	var popup_fail_tip =  $(".popup-fail-tip").cloneObj();
	
	if( !title ){
		title = "提示";
	}
	$(".popup-fail-tip").find("h3").text(title);
	$(".popup-fail-tip").find(".popup-p").text(tips);
	
	$(".popup-fail-tip").find(".popup-oper-fail-cancel").on("click",function(){   //取消
		if( !!cancelFun ){
			$(".popup-fail-tip").hide();
			cancelFun();
		}
	});
	
	$(".popup-fail-tip").find(".popup-oper-modify").on("click",function(){   //继续修改
		if( !!sureFun ){
			$(".popup-fail-tip").hide();
			sureFun();
		}
	});
	$(".popup-fail-tip").show();
}

function showCheckTips(title,tips,sureFun,cancelFun){
	if( !title ){
		title = "提示";
	}
	$(".popup-check-tip").find("h3").text(title);
	$(".popup-check-tip").find(".popup-p").text(tips);
	
	$(".popup-check-tip").find(".popup-oper-check-cancel").on("click",function(){   //取消
		if( !!cancelFun ){
			$(".popup-check-tip").hide();
			cancelFun();
		}
	});
	
	$(".popup-check-tip").find(".popup-oper-replace").on("click",function(){   //替换版本
		if( !!sureFun ){
			$(".popup-check-tip").hide();
			sureFun();
		}
	});
	$(".popup-check-tip").show();
}
/*******提示end*****/
/*******显示加载效果函数Start********/
(function(){
	$.getHtmlContent = function(url,callBackFunc){
		$.ajax({
			url : url,
			async: false,
			type : "POST",
			dataType : "HTML",
			success: callBackFunc,
			error:function(){
			
			}
		});
	}
})($);

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
/**
 * 功能：调整页面元素的高度
 * */
function adjustPageHeight(){
	var clientHeight = document.documentElement.clientHeight|| document.body.clientHeight;   //浏览器文档的高度
	var contentHieght = clientHeight - 105;
	
	$("#content_box,#sideNav,#sideBox").css({height:contentHieght});
	$("#sideBox").css({position:"relative"});
	$("#sideBox iframe").css({position:"absolute",width:"100%"});
	
	$(".nav-list",window.frames['sideNav'].document).css({"min-height":contentHieght});
} 

/***功能：多文件上传
 * @param :actionUrl :请求地址
 * @param elementId:input file元素
 * @param :callback: 回调函数
 * **/
function mutilfileFun(actionUrl,element,callback){
	if($(element).val()==""){
		alert("请选择上传文件！");
		return;
	}
	$.ajaxFileUpload({
		url : actionUrl,
		secureuri : false,
		fileElement : $(element),
		dataType : "text",//返回数据的类型
		success : function(data,status) {
			var repObj = $.parseJSON(data);
			if (status == "success") {
				if (callback) {
					callback(repObj);
				}
			} else {
				alert("上传失败！");
			}
			
		}
	});
}
/**
 * 复制值的设置   
 */
function setClickCopyValue(id,value,call,appendElem,stylesToAdd){
    var clip = new ZeroClipboard.Client(); // 新建一个对象
    clip.setHandCursor( true );
    clip.setText(value); // 设置要复制的文本。
    clip.addEventListener("mouseUp", function(client) {
        alert("复制成功！");
        client.hide();
        if(call){
        	call();
        }
    });
    // 注册一个 button，参数为 id。点击这个 button 就会复制。
    //这个 button 不一定要求是一个 input 按钮，也可以是其他 DOM 元素。
    clip.glue(id,appendElem,stylesToAdd); // 和上一句位置不可调换
}

/**
 * 关闭加载效果
 * */
function popupCloseLoading(){
	if( $("#ng_popup_loading").length > 0 ){
		$("#popup_loading_bg,#popup_loading_content").hide();
	}
}
/********显示加载效果函数end********/

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



/**
 * javascript 两数相加的方法 
 ***/
function accAdd(arg1,arg2){
	  var r1,r2,m;
	  try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
	  try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
	  m=Math.pow(10,Math.max(r1,r2))
	  return (arg1*m+arg2*m)/m
}

function pageData(){
		this.list=new Array(),
		this.pageNumber=1,
		this.pageSize=10,
		this.totalPage=1,
		this.totalRow=0
};//页面对象