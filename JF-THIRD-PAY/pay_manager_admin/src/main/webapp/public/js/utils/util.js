/**
 * 默认的ajax数据处理
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

