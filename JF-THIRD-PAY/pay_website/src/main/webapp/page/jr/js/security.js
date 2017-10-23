/**
 * 证券页面
 **/
var pageIndex; //页面请求参数---id

$(function() {
	/**获取页面参数**/
	pageIndex = getParam("bondId");

//	$.getJSON('dataJson/bond.json?callback=?', function(data){
//		//初始化页面数据
//		var dataObj = !!data.Data ? data.Data : {};
//		console.log(dataObj)
//		initSecurityPageData(dataObj);
//	});

	var urls = "http://10.0.3.11/jinfu_website/ProProductBond/getProProductBondById.do?bondId=" + pageIndex;
	$.ajax({
		type: "get",
		url: urls,
		async: true,
		success: function(data) {
			if (data.resultCode == 200) {
				data = JSON.parse(data);
				console.log(data)
				var dataObj = !!data.Data ? data.Data : {};
				initSecurityPageData(dataObj);
			}else{
				console.log("请求失败，请重试")
			}
			
		}
	});
});

/**
 *  初始化页面数据
 *  @param dataObj: 数据对象
 * */
function initSecurityPageData(dataObj) {
	$(".eg").on("click", function() {
		console.log("click")
		window.location.href = "exampleImg.html?" + dataObj.bondBuySuccPicExample;
	})
	$(".egt").on("click", function() {
		window.location.href = "mianze.html"
	})
	if(dataObj.bondBuySuccPicExample === "" || dataObj.bondBuySuccPicExample === undefined || dataObj.bondBuySuccPicExample === null) {
		$(".eg").css({
			"display": 'none'
		});
		$(".egt").css({
			"margin-top": '20px'
		});
	}
	/*******平台用户收益率*********/
	var bondPlatIncomeRates;
	if(dataObj.bondPlatIncomeRate) {
		bondPlatIncomeRates = dataObj.bondPlatIncomeRate.toFixed(2)
	}
	$(".platform").text(bondPlatIncomeRates);

	/*******官方用户收益率*********/
	var bondOfficialIncomeRates;
	if(dataObj.bondOfficialIncomeRate) {
		bondOfficialIncomeRates = dataObj.bondOfficialIncomeRate.toFixed(2)
	}
	$(".official").text(bondOfficialIncomeRates);

	/*******注意事项*********/
	if(dataObj.bondTips) {
		var att = dataObj.bondTips;
		att = att.split(",");
		var str2 = "";
		for(var i = 0; i < att.length; i++) {
			str2 += '<p>' + att[i] + '</p>';
		}
		$(".atten_word").append(str2);
	} else {
		$(".atten").css({
			"display": "none"
		});
	}
	

	/**自定义标签**/
	function ifEmpty(obj) {
		for(var key in obj) {
			return false;
		}
		return true;
	}
	if(dataObj.bondCustomTags) {
		var arrs = dataObj.bondCustomTags;
		arrs = JSON.parse(arrs);
		var arr1 = arrs.autoTags,
			arr2 = arrs.customTags,
			arr3 = arrs.publicTags,
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
			str += '<div class="cu"><div class="cu_cu fl" style="background:' + arrays[i].tag_color + ';">' + arrays[i].tag_title + '</div><div class="cu_word fl">' + arrays[i].tag_first + '<span style="color: #FF5152;">' + arrays[i].tag_middle + '</span>' + arrays[i].tag_last + '</div></div>'
		}
		$(".atten").before(str);
	}
	if((dataObj.bondTips === "" || dataObj.bondTips === undefined || dataObj.bondTips === null) && (str === "" || str === undefined || str === null)) {
		$(".info").css({
			"display": "none"
		});
	}
	/*******累计开户*********/
	$(".totalopen").text(dataObj.cumulativeCount);

	/*******本月开户*********/
	$(".instantopen").text(dataObj.monthCount);

	/*******公司简介*********/
	$(".company").text(dataObj.bondCompanyIntroduce);

	/*******金服补贴*********/
	if(dataObj.bondTransactionSubsidyValue === "" || dataObj.bondTransactionSubsidyValue === undefined || dataObj.bondTransactionSubsidyValue === null) {
		$(".first").css({
			"display": "none"
		});
	}
	if(dataObj.bondTransactionSubsidyType === 1) {
		$(".goldSubsidies").text(dataObj.bondTransactionSubsidyValue + "折");
	}
	if(dataObj.bondTransactionSubsidyType === 2) {
		$(".goldSubsidies").text(dataObj.bondTransactionSubsidyValue + "元");
	}

	/*******补贴上限*********/
	//   TODO
	if(dataObj.bondTransactionAmountMax) {
		$(".investAmount").text("按投资金额"+dataObj.bondTransactionAmountMax+"万元计算");
	}else{
		$(".max").css({
			"display":"none"
		});
	}

	/*******所属机构*********/
	$(".agency").text(dataObj.companyName);

	/*******开户数量*********/
	if(dataObj.bondTotalAmountType === 1) {
		$(".openamount").text("交易金额");
	}
	if(dataObj.bondTotalAmountType === 2) {
		$(".openamount").text("开户数量");
	}
	if(dataObj.bondTotalAmount === 0) {
		$(".accountNum").text("不限");
	} else {
		$(".accountNum").text(dataObj.bondTotalAmount);
	}

	/*******购买期限*********/
	$(".periodVali").text(dataObj.bondEndTime);

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