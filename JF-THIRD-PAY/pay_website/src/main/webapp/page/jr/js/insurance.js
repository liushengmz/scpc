/**
 * 
 * **/
var pageIndex;

$(function() {
	pageIndex = getParam("insuranceId");
	var urls = "http://10.0.3.11/jinfu_website/ProProductInsurance/getInsuranceInfo.do?insuranceId=" + pageIndex;

//	$.getJSON('dataJson/insurance.json?callback=?', function(data){
//		//初始化页面数据
//		var arr = !!data.Data ? data.Data : {};
//		initInsurancePageData(arr);
//	});
	$.ajax({
		type: "get",
		url: urls,
		async: true,
		success: function(data) {
			data = JSON.parse(data);
			console.log(data);
			if (data.resultCode == 200) {			
				var arr = !!data.Data ? data.Data : {};
				initInsurancePageData(arr);
			}else{
				console.log("请求失败，请重试")
			}
		}
	});
});

/**
 * 初始化保险页面数据
 ***/
function initInsurancePageData(dataObj) {
	/********保险logo**********/
	$(".left_img img").attr({"src":dataObj.insuIcon})
	/********截图示例**********/
	$(".eg").on("click",function(){
				console.log("click")
				window.location.href="exampleImg.html?"+dataObj.insuBuySuccPicExample;
			})
	/********免责声明**********/
	$(".egt").on("click", function() {
		window.location.href = "mianze.html"
	})
	if(dataObj.insuBuySuccPicExample===""||dataObj.insuBuySuccPicExample===undefined||dataObj.insuBuySuccPicExample===null){
		$(".eg").css({"display":'none'});
		$(".egt").css({"margin-top":'20px'});
	}

	
	
	/********一句话介绍**********/
	if(dataObj.insuProductIntroduce===""||dataObj.insuProductIntroduce===undefined||dataObj.insuProductIntroduce===null){
		$(".describe").css({"display":"none"});
	}else{
		$(".describe").text(dataObj.insuProductIntroduce);
	}
	/********自定义标签**********/
	function ifEmpty(obj) {
		for(var key in obj) {
			return false;
		}
		return true;
	}
	if(dataObj.insuCustomTags) {
		var arrs = dataObj.insuCustomTags;
		arrs = JSON.parse(arrs);
		var arr1 = arrs.autoTags || [],
			arr2 = arrs.customTags || [],
			arr3 = arrs.publicTags || [],
			arrays = [],
			str = "",
			price = 0;
		price = dataObj.insuOfficialAmount * (1 - dataObj.insuSubsidyValue * 0.01) < 1 ? (dataObj.insuOfficialAmount * (1 - dataObj.insuSubsidyValue * 0.01)).toFixed(2) : (dataObj.insuOfficialAmount * (1 - dataObj.insuSubsidyValue * 0.01)).toFixed(0);
		for(var i = 0; i < arr1.length; i++) {
			if(ifEmpty(arr1[i])) {

			} else {
				arrays.push(arr1[i]);
			}

		}
		for(var i = 0; i < arr2.length; i++) {
			if(ifEmpty(arr2[i])) {

			} else {
				arrays.push(arr2[i]);
			}

		}
		for(var i = 0; i < arr3.length; i++) {
			if(ifEmpty(arr3[i])) {

			} else {
				arrays.push(arr3[i]);
			}

		}
		for(var i = 0; i < arrays.length; i++) {
			str += '<div class="cu"><div class="cu_cu fl" style="background:' + arrays[i].tag_color + ';">' + arrays[i].tag_title + '</div><div class="cu_word fl">' + arrays[i].tag_first+'<span style="color: #FF5152;">'+arrays[i].tag_middle+'</span>'+arrays[i].tag_last + '</div></div>'
		}
		$(".atten").before(str);
	}
	/********注释标签*******/
	if(dataObj.insuTips){
		$(".atten_word").html(dataObj.insuTips);
	}else{
		$(".atten").css({"display":"none"})
	}
	
	if((dataObj.insuTips===""||dataObj.insuTips===undefined||dataObj.insuTips===null)&&(dataObj.insuCustomTags===""||dataObj.insuCustomTags===undefined||dataObj.insuCustomTags===null)){
		$(".info").css({"display":"none"});
	}
	/********保障内容**********/
	$(".content").text(dataObj.insuSafeguardContent);
	/********产品名称**********/
	$(".productName").text(dataObj.insuName);
	/********官方保费**********/
	$(".amount_1").text(dataObj.insuOfficialAmount);
	$(".priceold span").text(dataObj.insuOfficialAmount)
	/********保费补贴**********/
	if(dataObj.insuSubsidyType === 1) {
		$(".subsidies").text(dataObj.insuSubsidyValue);
		$(".subsidies2").text("%");
		$(".pricenew span").text(price);
	}
	if(dataObj.insuSubsidyType === 2) {
		$(".subsidies").text(dataObj.insuSubsidyValue);
		$(".subsidies2").text("元");
		$(".pricenew span").text(dataObj.insuOfficialAmount - dataObj.insuSubsidyValue);
	}
	
	if(dataObj.insuSubsidyValue===''||dataObj.insuSubsidyValue===undefined||dataObj.insuSubsidyValue===null||dataObj.insuSubsidyValue===0){
		$(".pricenew.o").css({"display":"none"});
		$(".priceold.t").css({"display":"none"});
		$(".pricenew.th").css({"display":"block"});
	}
	/********保险期限**********/
	$(".timelimit").text(dataObj.insuTerm);
	/********缴费频次**********/
	$(".payTimes").text(dataObj.insuFrequency);
	/********投保人条件**********/
	if(dataObj.insuQualification) {
		var stri = dataObj.insuQualification,
			str1 = '';
		if(stri === '' || stri === undefined || stri === null) {} else {
			stri = stri.split("\n");
			for(var i = 0; i < stri.length; i++) {
				str1 += '<p class="np1 conditions">'+(i+1)+"." + stri[i] + '</p>';
			}
			$(".neir").html(str1);
		}
	}

	//$(".conditions").text("1."+dataObj.insuQualification)
	/********所属机构**********/
	$(".agency").text(dataObj.companyName);
		/********保险数量**********/
	$(".insuranceAmount").text(dataObj.insuTotalCount);
	/********购买期限**********/
	$(".buyDate").text(dataObj.insuEndTime);
	/********保险名称**********/
	$(".title_name").text(dataObj.insuName);

}
/***
 * 获取请求参数
 * @param paramName : 参数名称
 * */
function getParam(paramName) {
	var paramValue = "";
	var isFound = false;
	if(this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
		arrSource = unescape(this.location.search).substring(1, this.location.search.length).split("&");
		i = 0;
		while(i < arrSource.length && !isFound) {
			if(arrSource[i].indexOf("=") > 0) {
				if(arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase()) {
					paramValue = arrSource[i].split("=")[1];
					isFound = true;
				}
			}
			i++;
		}
	}
	return paramValue;
}