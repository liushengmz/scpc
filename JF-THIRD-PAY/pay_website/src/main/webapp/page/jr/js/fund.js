/**
 * 基金页面
 **/
var pageIndex; //页面请求参数---id

$(function() {
	/**获取页面参数**/
	pageIndex = getParam("fundId");
	var urls = "http://10.0.3.11/jinfu_website/ProProductFund/getProProductFundById.do?fundId=" + pageIndex;
//	$.getJSON('dataJson/test.json?callback=?', function(data){
//		//初始化页面数据
//		var dataObj = !!data.Data ? data.Data : {};
//		initFundPageData(dataObj);
//	});

	$.ajax({
		type: "get",
		url: urls,
		async: true,
		success: function(data) {
			data = JSON.parse(data);
			console.log(data)
			if (data.resultCode == 200) {				
				var dataObj = !!data.Data ? data.Data : {};
				initFundPageData(dataObj);
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
function initFundPageData(dataObj) {
	/*******示例截图******/
	$(".eg").on("click", function() {
		window.location.href = "exampleImg.html?" + dataObj.fundBuySuccPicExample;
	})
	/*******免责条例******/
	$(".egt").on("click", function() {
		window.location.href = "mianze.html"
	})
	if(dataObj.fundBuySuccPicExample === "" || dataObj.fundBuySuccPicExample === undefined || dataObj.fundBuySuccPicExample === null) {
		$(".eg").css({
			"display": 'none'
		});
		$(".egt").css({
			"margin-top": '20px'
		});
	}
	/*******产品类型******/
	var type = '';
	switch(dataObj.fundType) {
		case 1:
			type = '混合型';
			break;
		case 2:
			type = '货币型';
			break;
		case 3:
			type = '债券型';
			break;
		case 4:
			type = '指数型';
			break;
		case 5:
			type = '股票型';
			break;
		case 6:
			type = 'QDII';
			break;
		case 7:
			type = '理财型';
			break;
		case 8:
			type = '其它';
			break;
	}
	/**产品类型**/
	$(".productType").text(type);

	/**七日年化收益率**/
	var sevenDays = Number(dataObj.fundSevenDayIncomeRate);
	var annual = Number(dataObj.fundFirstSubsidyValue);

	$(".sevenDaysAndAnnual").text(Number(annual + sevenDays).toFixed(2));
	/**官方年化**/
	$(".sevenDays").text(dataObj.fundSevenDayIncomeRate);
	/**首投补贴**/
	if(dataObj.fundFirstSubsidyValue === "" || dataObj.fundFirstSubsidyValue === undefined || dataObj.fundFirstSubsidyValue === null) {
		$(".annual").text(0);
		$(".bottom").text(Number(((sevenDays * 100) / 365)).toFixed(3))
	} else {
		$(".annual").text(dataObj.fundFirstSubsidyValue);
	}

	/**每万份收益(元)**/
	var latestNet = Number(((sevenDays * 100) / 365)).toFixed(3);
	var annual_1 = Number(((annual * 100) / 365)).toFixed(3);

	$(".perEarningsl").text(latestNet);
	$(".perEarningsA").text(annual_1);
	$(".perEarningsTotal").text(((((annual + sevenDays) * 100) / 365)).toFixed(3));
	if(type === "货币型"||type === "理财型") {
		$(".banner_title .dis").html("官方万份收益");
		$(".banner_title .disd").html("官方七日年化");
		$(".banner_footer .dis").html("平台万份收益");
		$(".banner_footer .disd").html("平台七日年化");

		/**官方七日年化**/
		$(".banner_title .right .shuzi .num").text(dataObj.fundSevenDayIncomeRate);
		/**官方万份收益**/
		$(".banner_title .left .shuzi").text((dataObj.fundSevenDayIncomeRate * 10000 / 365).toFixed(2));
		/**平台七日年化**/
		$(".banner_footer .right .shuzi .num").text(dataObj.fundSevenDayIncomeRate + dataObj.fundFirstSubsidyValue);
		/**平台万份收益**/
		$(".banner_footer .left .shuzi").text(((dataObj.fundSevenDayIncomeRate + dataObj.fundFirstSubsidyValue) * 10000 / 365).toFixed(2));

		/**每万份收益(元)**/
		var latestNet = Number(((dataObj.fundSevenDayIncomeRate * 100) / 365)).toFixed(3);
		var annual_1 = Number(((dataObj.fundSevenDayIncomeRate * 100) / 365)).toFixed(3);

		$(".perEarningsl").text(latestNet);
		$(".perEarningsA").text(annual_1);
		$(".perEarningsTotal").text((Number(latestNet) + Number(annual_1)).toFixed(3));
		
		/********近1月涨跌幅**********/
		$(".up1 .right2").html(dataObj.fundOneMonthRange+"%");
		/********近3月涨跌幅**********/
		$(".up3 .right2").html(dataObj.fundThreeMonthRange+"%");
		/********近6月涨跌幅**********/
		$(".up6 .right2").html(dataObj.fundSixMonthRange+"%");
		/********近1年涨跌幅**********/
		$(".upy .right2").html(dataObj.fundTwelveMonthRange+"%");
	}else{
		$(".banner_title .dis").html("官方最新净值");
		$(".banner_title .disd").html("官方日增长率");
		$(".banner_footer .dis").html("平台最新净值");
		$(".banner_footer .disd").html("平台首投补贴");

		/**官方日增长率**/
		$(".banner_title .right .shuzi .num").text(dataObj.fundYesterdayRate);
		/**官方最新净值**/
		$(".banner_title .left .shuzi").text(dataObj.fundPureValue);
		/**平台首投补贴**/
		$(".banner_footer .right .shuzi .num").text(dataObj.fundFirstSubsidyValue);
		/**平台最新净值**/
		$(".banner_footer .left .shuzi").text(dataObj.fundPureValue + dataObj.fundFirstSubsidyValue);

		/**每万份收益(元)**/
		var latestNet = Number(((dataObj.fundSevenDayIncomeRate * 100) / 365)).toFixed(3);
		var annual_1 = Number(((dataObj.fundSevenDayIncomeRate * 100) / 365)).toFixed(3);

		$(".perEarningsl").text(latestNet);
		$(".perEarningsA").text(annual_1);
		$(".perEarningsTotal").text((Number(latestNet) + Number(annual_1)).toFixed(3));
		
		/********近1月增长率**********/
		$(".up1 .right2").html(dataObj.fundOneMonthRate+"%");
		/********近3月增长率**********/
		$(".up3 .right2").html(dataObj.fundThreeMonthRate+"%");
		/********近6月增长率**********/
		$(".up6 .right2").html(dataObj.fundSixMonthRate+"%");
		/********近1年增长率**********/
		$(".upy .right2").html(dataObj.fundTwelveMonthRate+"%");
	}

	/**自定义标签**/
	function ifEmpty(obj) {
		for(var key in obj) {
			return false;
		}
		return true;
	}
	if(dataObj.fundCustomTags) {
		var arrs = dataObj.fundCustomTags;
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

	/**	注释标签 **/
	if(dataObj.fundTips) {
		var att = dataObj.fundTips;
		att = att.split(",");
		var str2 = "";
		for(var i = 0; i < att.length; i++) {
			str2 += '<p>' + att[i] + '</p>';
		}
		$(".atten_word").append(str2);
	}else{
		$(".atten").css({
			"display": "none"
		});
	}
	if((dataObj.fundTips === "" || dataObj.fundTips === undefined || dataObj.fundTips === null) && (dataObj.fundCustomTags === "" || dataObj.fundCustomTags === undefined || dataObj.fundCustomTags === null)) {
		$(".info").css({
			"display": "none"
		});
	}
	/**产品名称**/
	$(".productName").text(dataObj.fundName);

	/**产品类型**/
	$(".productType").text(type);

	/**首投补贴**/
	if(dataObj.fundFirstSubsidyValue === "" || dataObj.fundFirstSubsidyValue === undefined || dataObj.fundFirstSubsidyValue === null||dataObj.fundFirstSubsidyValue === 0) {
		$(".first").css({
			"display": "none"
		});
		$(".max").css({
			"display": "none"
		});
		$(".banner").css({
			"display": "none"
		});
		$(".banner2").css({
			"display": "block"
		});
	} else {
		/**首投补贴**/
		$(".first .right2").html('<span style="color:#007aff">+</span>' + '<span style="color:#007aff;font-weight:bolder">' + dataObj.fundFirstSubsidyValue + '</span><span style="color:#007aff">%</span>年化收益率');
		/**首投补贴上限**/
		$(".max span").html(dataObj.fundFirstSubsidyBuyMax);
	}
	/********复投补贴**********/
	if(dataObj.fundRepeatSubsidyValue === "" || dataObj.fundRepeatSubsidyValue === undefined || dataObj.fundRepeatSubsidyValue === null||dataObj.fundRepeatSubsidyValue === 0) {
		$(".futoufre").css({
			"display": "none"
		});
		$(".futoumax").css({
			"display": "none"
		});
		$(".futousub").css({
			"display": "none"
		});
	} else {
		/********复投补贴**********/
		$(".futousub .right2").html('<span style="color:#007aff">+</span>' + '<span style="color:#007aff;font-weight:bolder">' + dataObj.fundRepeatSubsidyValue + '</span><span style="color:#007aff">%</span>年化收益率');
		/********复投补贴上限**********/
		$(".futoumax span").html(dataObj.fundRepeatSubsidyBuyMax);
		/********复投补贴次数**********/
		$(".futoufre .right2").html(dataObj.fundUserRepeatSubsidyFrequency + '/' + dataObj.fundRepeatSubsidyFrequency + '次');
	}
	/**来源机构**/
	$(".agency").text(dataObj.companyName);

	/**产品数量**/
	if(dataObj.fundTotalAmount) {
		$(".productNum").text(dataObj.fundTotalAmount);
	} else {
		$(".productNum").text("充沛");
	}

	/**购买期限**/
	$(".buyDate").text(dataObj.fundEndTime);
	
	/**风险等级**/
	var grade = '',
		lv = dataObj.fundRiskLv;
	switch(lv){
		case 1:
			grade = "低风险";
			break;
		case 2:
			grade = "中低风险";
			break;
		case 3:
			grade = "中风险";
			break;
		case 4:
			grade = "中高风险";
			break;
		case 5:
			grade = "高风险";
			break;
	}
	$(".fxcd .right").html(grade);
	/**交易费率**/
	$(".jyfl .right").html(dataObj.fundTransactionRate);
	/**基金经理**/
	$(".fxcd .right").html(dataObj.fundAmaldarName);
	
	
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