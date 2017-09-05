/**
 * 
 * **/
var pageIndex;

$(function() {
	pageIndex = getParam("loanId");
	var urls = "http://10.0.3.11/jinfu_website/ProProductLoan/getLoanInfo.do?loanId=" + pageIndex;

//	$.getJSON('dataJson/test3.json?callback=?', function(data) {
//		//初始化页面数据			
//		var arr = !!data.Data ? data.Data : {};
//		loanPageData(arr);
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
				loanPageData(arr);
			}else{
				console.log("请求失败，请重试")
			}
		}
	});
});
/**
 * 初始化保险页面数据
 ***/
function loanPageData(dataObj) {
	$(".eg").on("click", function() {
		console.log("click")
		window.location.href = "exampleImg.html?" + dataObj.loanBuySuccPicExample;
	})
	$(".egt").on("click", function() {
		window.location.href = "mianze.html"
	})
	if(dataObj.loanBuySuccPicExample === "" || dataObj.loanBuySuccPicExample === undefined || dataObj.loanBuySuccPicExample === null) {
		$(".eg").css({
			"display": 'none'
		});
		$(".egt").css({
			"margin-top": '20px'
		});
	}
//	/********没有补贴**********/
//	if(dataObj.termList[0]. loanSubsidyRate===""||dataObj.termList[0]. loanSubsidyRate===0||dataObj.termList[0]. loanSubsidyRate===undefined||dataObj.termList[0]. loanSubsidyRate===null){
//		$(".header").css({"display":"none"});
//		$(".header2").css({"display":"block"});
//		$(".name").css({"display":"none"});
//		$(".time").css({"display":"none"});
//		$(".first").css({"display":"none"});
//	}
	/********自定义标签**********/
	function ifEmpty(obj) {
		for(var key in obj) {
			return false;
		}
		return true;
	}

	if(dataObj.finaCustomTags) {
		var arrs = dataObj.finaCustomTags;
		arrs = JSON.parse(arrs);
		var arr1 = arrs.autoTags || [],
			arr2 = arrs.customTags || [],
			arr3 = arrs.publicTags || [],
			arrays = [];
		str = "";
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
			str += '<div class="new"><div class="new_cu fl" style="background:' + arrays[i].tag_color + ';">' + arrays[i].tag_title + '</div><div class="new_word fl">' + arrays[i].tag_first + '<span style="color: #FF5152;">' + arrays[i].tag_middle + '</span>' + arrays[i].tag_last + '</div></div>'
		}
		$(".atten").before(str);
	}
	/*********注意标签************/
	if(dataObj.loanTips) {
		$(".atten_word").html(dataObj.loanTips);
	}

	if((dataObj.loanTips === "" || dataObj.loanTips === undefined || dataObj.loanTips === null) && (dataObj.loanCustomTags === "" || dataObj.loanCustomTags === undefined || dataObj.loanCustomTags === null)) {
		$(".info").css({
			"display": "none"
		});
	}
	/********额度**********/
	var limitmax = dataObj.loanBorrowMoneyMax,
		limitmin = dataObj.loanBorrowMoneyMin;

	/********申请条件**********/
	if(dataObj.loanApplyCondition) {
		var factors = dataObj.loanApplyCondition,
			factorsstr = "";
		factors = factors.split("\n");
		for(var i = 0; i < factors.length; i++) {
			if(i === 0) {
				factorsstr += '<p class="firstp">' + factors[i] + '</p>';
			} else if(i > 0 && i < factors.length-1) {
				factorsstr += '<p>' + factors[i] + '</p>';			
			} else {
				factorsstr += '<p class="lastp">' + factors[i] + '</p>';
			}
		}
		if(factors.length === 1) {
			factorsstr = '<p class="firstp lastp">' + factors[0] + '</p>';
		}
		$(".af_container1").append(factorsstr);
	}else{
		$(".applyforcondition").css({"display":"none"});
		$(".af_container1").css({"display":"none"});
	}
	if(dataObj.loanApplyCondition==="无"){
		$(".applyforcondition").css({"display":"none"});
		$(".af_container1").css({"display":"none"});
	}

	/********申请所需材料**********/
	if(dataObj.loanApplyNeedStuff) {
		var factors = dataObj.loanApplyNeedStuff,
			factorsstr = "";
		factors = factors.split("\n");
		for(var i = 0; i < factors.length; i++) {
			if(i === 0) {
				factorsstr += '<p class="firstp">' + factors[i] + '</p>';
			} else if(i > 0 && i < factors.length-1) {
				factorsstr += '<p>' + factors[i] + '</p>';
				
			} else {
				factorsstr += '<p class="lastp">' + factors[i] + '</p>';
			}
		}
		if(factors.length === 1) {
			factorsstr = '<p class="firstp lastp">' + factors[0] + '</p>';
		}
		$(".af_container2").append(factorsstr);
	}else{
		$(".material").css({"display":"none"});
		$(".af_container2").css({"display":"none"});
	}
	if(dataObj.loanApplyNeedStuff==="无"){
		$(".material").css({"display":"none"});
		$(".af_container2").css({"display":"none"});
	}

	/********产品特点**********/
	if(dataObj.loanTrait) {
		var factors = dataObj.loanTrait,
			factorsstr = "";
		factors = factors.split("\n");
		for(var i = 0; i < factors.length; i++) {
			if(i === 0) {
				factorsstr += '<p class="firstp">' + factors[i] + '</p>';
			} else if(i > 0 && i < factors.length-1) {
				factorsstr += '<p>' + factors[i] + '</p>';			
			} else {
				factorsstr += '<p class="lastp">' + factors[i] + '</p>';
			}
		}
		if(factors.length === 1) {
			factorsstr = '<p class="firstp lastp">' + factors[0] + '</p>';
		}
		$(".af_container3").append(factorsstr);
	}else{
		$(".characteristic").css({"display":"none"});
		$(".af_container3").css({"display":"none"});
	}
	if(dataObj.loanTrait==="无"){
		$(".characteristic").css({"display":"none"});
		$(".af_container3").css({"display":"none"});
	}

	/********产品名称**********/
	$(".loanName").text(dataObj.loanName);
	/********产品ICON**********/
	$(".hd_left img").attr({
		"src": dataObj.loanIcon
	});
	/********贷款总金额*********/
	$(".total").html("<span>" + dataObj.loanLoanedAmount + "万</span>/" + dataObj.loanTotalAmount + "万");
	/********托管机构**********/
	$(".agency").text(dataObj.loanGuaranteeCompany);
	/********贷款截止时间**********/
	$(".buyDate").text(dataObj.loanEndTime);
	/********贷款补贴次数*********/
	$(".loanSubsidyFrequency").text(dataObj.userSubsidyFrequency + "/" + dataObj.loanSubsidyFrequency + "次");
	/********贷款补贴上限*********/
	$(".loanSubsidyMax").text(dataObj.loanSubsidyMax);
	/********产品期数**********/
	if(dataObj.termList) {
		var timerarr = dataObj.termList,
			timer = [];
		opstr = "";
		for(var i = 0; i < timerarr.length; i++) {
			opstr += '<option>' + timerarr[i].loanTerm + '个月</option>';
			timer.push(timerarr[i].loanTerm);
		}
		$(".aselect").append(opstr);
	}	
	/********数据处理**********/
	function datas(i, v) {
		/********贷款补贴**********/
		$(".loanSubsidyRate").text(dataObj.termList[i].loanSubsidyRate);
		
		/********官方月利率**********/
		$(".loanRate").text(dataObj.termList[i].loanRate);
		/********官方总利息**********/
		$(".loanRates").text(((dataObj.termList[i].loanRate / 100) * v).toFixed(0));
		/********官方月还款**********/
		$(".loanRatesper").text((((dataObj.termList[i].loanRate / 100) * v + v) / timer[i]).toFixed(0));
		/********平台月利率**********/
		$(".loanSubsidyRatesper").text((dataObj.termList[i].loanRate - dataObj.termList[i].loanSubsidyRate).toFixed(2));
		/********平台总利息**********/
		$(".loanSubsidyRates").text((((dataObj.termList[i].loanRate - dataObj.termList[i].loanSubsidyRate) / 100) * v).toFixed(0));
		/********平台月还款**********/
		$(".loanSubsidyRatespers").text(((((dataObj.termList[i].loanRate - dataObj.termList[i].loanSubsidyRate) / 100) * v + v) / timer[i]).toFixed(0));
		
		/********平台没有补贴**********/
		if(dataObj.termList[i]. loanSubsidyRate===""||dataObj.termList[i]. loanSubsidyRate===0||dataObj.termList[i]. loanSubsidyRate===undefined||dataObj.termList[i]. loanSubsidyRate===null){
			console.log("qqq")
			$(".header").css({"display":"none"});
			$(".header2").css({"display":"block"});
			$(".name").css({"display":"none"});
			$(".time").css({"display":"none"});
			$(".first").css({"display":"none"});
		}else{
			console.log("aaa")
			$(".header2").css({"display":"none"});
			$(".header").css({"display":"block"});
			$(".name").css({"display":"block"});
			$(".time").css({"display":"block"});
			$(".first").css({"display":"block"});
		}
	}
	/********初始化**********/
	datas(0,10000);
	$('.limits').on('change', function() {
		//input
		var v = parseInt($(".limit").val());
		if(v > limitmax) {
			$(".limit").val(limitmax);
		}
		if(v < limitmin) {
			$(".limit").val(limitmin);
		}
		v = parseInt($(".limit").val());
		//select
		var value = $(".aselect").prop('selectedIndex');
		datas(value,v);
		
	});
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