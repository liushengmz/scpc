/**
 * 初始化理财页面数据
 ***/

var pageIndex;
$(function() {
	pageIndex = getParam("proID");
	var urls = "http://api.aijinfu.cn/jinfu_website/ProProductFinancial/getProProductFinancialById.do?proId=" + pageIndex;
	$.ajax({
		type: "get",
		url: urls,
		async: true,
		success: function(data) {
			data = JSON.parse(data);
			console.log(data)
			if(data.resultCode == 200) {
				var dataObj = !!data.Data ? data.Data : {};
				initLiCaiPageData(dataObj);
			} else {
				console.log("请求失败，请重试")
			}

		}
	});

//	$.getJSON('dataJson/test2.json?callback=?', function(data){
//		//初始化页面数据
//		var dataObj = !!data.Data ? data.Data : {};
//		console.log(dataObj)
//		initLiCaiPageData(dataObj);
//	});
});

/**
 * 初始化理财页面数据
 ***/
function initLiCaiPageData(arr) {
	console.log(arr.finaType)
	console.log(arr.finaFirstSubsidyValue)
	var bannerData, rateTotal;
	var mainRate, butieRate, futouRate;
	var firstBoolean = arr.finaFirstSubsidyValue === '' || arr.finaFirstSubsidyValue === 0 || arr.finaFirstSubsidyValue === undefined || arr.finaFirstSubsidyValue === null;
	var repeatBoolean = arr.finaRepeatSubsidyValue === "" || arr.finaRepeatSubsidyValue === undefined || arr.finaRepeatSubsidyValue === null || arr.finaRepeatSubsidyValue === 0;
	

	/*********示例截图***********/
	$(".eg").on("click", function() {	
			window.location.href = "exampleImg.html?" + arr.finaOpenAccountPicExample;
		})
	/*********示例截图***********/
	if(arr.finaOpenAccountPicExample === "" || arr.finaOpenAccountPicExample === undefined || arr.finaOpenAccountPicExample === null) {
		$(".eg").css({
			"display": 'none'
		});
		$(".egt").css({
			"margin-top": '20px'
		});
	}
	/*********免责声明***********/
	$(".egt").on("click", function() {
			window.location.href = "mianze.html"
		})
	
	/*********没有首投补贴***********/
	if(firstBoolean){
		
		$(".banner").css({
			"display": "none"
		});
		$(".pro").css({
			"display": "block"
		});
		$(".intro .t2").html("<span class='earning'>" + ((Number(arr.finaIncomeValue) * 100) / 365).toFixed(3) + "</span>")
	}
	/*********复投补贴***********/
	if(repeatBoolean) {
		mainRate = ((Number(arr.finaIncomeValue) * 100) / 365).toFixed(3);
		butieRate = ((Number(arr.finaFirstSubsidyValue) * 100) / 365).toFixed(3);
		rateTotal = (Number(mainRate) + Number(butieRate)).toFixed(3);
		bannerData = (Number(arr.finaIncomeValue) + Number(arr.finaFirstSubsidyValue)).toFixed(2);

		$(".inleft .main").html(mainRate);
		$(".inleft .butie").html(butieRate);
		$(".inleft .earning").html(rateTotal);

		$(".futoufre").css({
			"display": "none"
		});
		$(".futousub").css({
			"display": "none"
		});
		$(".futoumax").css({
			"display": "none"
		});
	} else {
		mainRate = ((Number(arr.finaIncomeValue) * 100) / 365).toFixed(3);
		butieRate = ((Number(arr.finaFirstSubsidyValue) * 100) / 365).toFixed(3);
		futouRate = ((Number(arr.finaRepeatSubsidyValue) * 100) / 365).toFixed(3);
		rateTotal = (Number(mainRate) + Number(butieRate)).toFixed(3);
		bannerData = (Number(arr.finaIncomeValue) + Number(arr.finaFirstSubsidyValue)).toFixed(2);
		$(".inleft .main").html(mainRate);
		$(".inleft .butie").html(butieRate);
		$(".inleft .earning").html(rateTotal);
		if(arr.finaRepeatSubsidyType === 1) {
			$(".futousub .right2").html('<span style="color:#007aff">+</span>' + '<span style="color:#007aff;font-weight:bolder">' + arr.finaRepeatSubsidyValue + '</span><span style="color:#007aff">%</span>年化收益率');
		}
		if(arr.finaRepeatSubsidyType === 2) {
			$(".futousub .right2").html('<span style="color:#007aff">+</span>' + '<span style="color:#007aff;font-weight:bolder">' + arr.finaRepeatSubsidyValue + '</span><span style="color:#007aff"></span>元');
		}
		$(".futoufre .right2").html(arr.finaUserRepeatSubsidyFrequency + '/' + arr.finaRepeatSubsidyFrequency + '次');
	}
	/******自定义标签******/
	function ifEmpty(obj) {
		for(var key in obj) {
			return false;
		}
		return true;
	}

	if(arr.finaCustomTags) {
		var arrs = arr.finaCustomTags;
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

	//注
	if(arr.finaSubsidyDescribe) {
		$(".atten_word").html(arr.finaSubsidyDescribe);
	} else {
		$(".atten").css({
			"display": "none"
		});
	}

	if((arr.finaTips === "" || arr.finaTips === undefined || arr.finaTips === null) && (arr.finaCustomTags === "" || arr.finaCustomTags === undefined || arr.finaCustomTags === null)) {
		$(".info").css({
			"display": "none"
		});
	}

	//官方收益
	$(".banner_title span").html(bannerData);
	$(".num").html(bannerData);
	//官方收益名称
	$(".prosev").html(arr.finaIncomeName)
	//官方年化
	$(".left h1").html(arr.finaIncomeName);
	$(".data1 h1").html(arr.finaIncomeName);
	$(".left span").html(arr.finaIncomeValue);
	$(".data1 span").html(arr.finaIncomeValue);
	//补贴年化
	$(".right span").html(arr.finaFirstSubsidyValue);
	$(".data2 span").html(arr.finaFirstSubsidyValue);
	//复投年华
	$(".data3 span").html(arr.finaRepeatSubsidyValue);
	//起投
	$(".mount").html(arr.finaStartingAmount);
	//产品名称
	if(arr.finaName) {
		$(".name .right2").html(arr.finaName);
	} else {
		$(".name").css({
			"display": "none"
		});
	}
	//产品期限
	if(arr.finaTermMin) {
		$(".time .right2").html(arr.finaTermMin+'天');
	} else {
		$(".time").css({
			"display": "none"
		});
	}
	if(arr.finaTermMin===0){
		$(".time .right2").html("活期");
	}
	//产品原利率
	if(arr.finaIncomeValue) {
		$(".lilv .right2 span").html(arr.finaIncomeValue);
	} else {
		$(".lilv").css({
			"display": "none"
		});
	}
	//首投补贴
	if(arr.finaFirstSubsidyValue) {
		$(".first .right2 .n").html(arr.finaFirstSubsidyValue);
	} else {
		$(".first").css({
			"display": "none"
		});
		$(".max").css({
			"display": "none"
		});
	}
	//首投补贴上限
	$(".max .right2 span").html(arr.finaFirstSubsidyBuyMax);
	//复投补贴上限 
	$(".futoumax .right2 span").html(arr.finaRepeatSubsidyBuyMax);
	//托管机构
	$(".tuoguan .right2").html(arr.companyName);
	//产品数量
	if(arr.finaTotalAmount===""||arr.finaTotalAmount===undefined||arr.finaTotalAmount===null){		
	}else{
		if(arr.finaTotalAmount===0){
			$(".amount .right2").html("充沛");
		}else{
			$(".amount .right2").html(arr.finaTotalAmount);
		}
		
	}
	//担保机构
	if(arr.finaGuaranteeCompany) {
		$(".danbao .right2").html(arr.finaGuaranteeCompany);
	} else {
		$(".danbao").css({
			"display": "none"
		});
	}
	//购买有效期
	$(".qixian .right2").html(arr.finaEndTime);
	//收益方式	
	if(arr.finaIncomeDescribe===""||arr.finaIncomeDescribe===undefined||arr.finaIncomeDescribe===null){
		$(".way .right2").css({"height":"0.88rem"})
	}else{
		$(".way .right2").html(arr.finaIncomeDescribe);

		$(".neirongcontainer span").html(arr.companyName);
		var h = $(".way .right2").height() + document.documentElement.clientWidth / 7.2 * 0.4;
		$(".way").css({
			"height": h
		});
		if(length = $(".way .right2").text().length <= 17) {
			length = $(".way .right2").css({
				"text-align": "right"
			})
		}
	}
		//类型为机构
	if(arr.finaType===2){
		if(firstBoolean){
		
			$(".banner").css({
				"display": "none"
			});
			$(".pro").css({
				"display": "block"
			});
			$(".intro .t2").html("<span class='earning'>" + ((Number(arr.finaIncomeValue) * 100) / 365).toFixed(3) + "</span>")
		}
		
		$(".banner").css({
			"display": "none"
		});
		$(".pro").css({
			"display": "none"
		});
		$(".intro").css({
			"display": "none"
		});
		$(".banner2").css({
			"display": "block"
		});
		$(".banner2 .img img").attr({"src":arr.finaIcon});
		$(".banner2 .jigou").html(arr.companyName);
		if(arr.finaFirstSubsidyValue===""||arr.finaFirstSubsidyValue===undefined||arr.finaFirstSubsidyValue===null||arr.finaFirstSubsidyValue===0){
			$(".shouyi").css({
				"display": "none"
			});
			$(".btl .btlw").html("官方平均年化收益率");
			$(".btl .minx").html(arr.finaIncomeValue);
			$(".btl .maxx").html(arr.finaIncomeValueMax);
			$(".btr .btlw").html("官方每万元收益(元)");
			$(".officialRate .btlp").html((arr.finaIncomeValue*100/365).toFixed(3)+"~"+(arr.finaIncomeValueMax*100/365).toFixed(3));			
		}else{						
			$(".shouyi").css({
				"display": "block"
			});
			$(".btl .minx").html(arr.finaIncomeValue+arr.finaFirstSubsidyValue);
			$(".btl .maxx").html(arr.finaIncomeValueMax+arr.finaFirstSubsidyValue);
			$(".btr .minx").html(arr.finaIncomeValue);
			$(".btr .maxx").html(arr.finaIncomeValueMax);
			//官方每万份收益
			$(".perWanGuan .mi").html((arr.finaIncomeValue*100/365).toFixed(3));
			$(".perWanGuan .ma").html((arr.finaIncomeValueMax*100/365).toFixed(3));
			//补贴后每万份收益
			$(".perWanAfter .mi").html(((arr.finaIncomeValue+arr.finaFirstSubsidyValue)*100/365).toFixed(3));
			$(".perWanAfter .ma").html(((arr.finaIncomeValueMax+arr.finaFirstSubsidyValue)*100/365).toFixed(3))
		}
	}else{
		if(firstBoolean){				
			$(".banner").css({
				"display": "none"
			});
			$(".pro").css({
				"display": "block"
			});
			$(".intro .t2").html("<span class='earning'>" + ((Number(arr.finaIncomeValue) * 100) / 365).toFixed(3) + "</span>")
		}else{
			$(".banner").css({
			"display": "block"
			});
			$(".pro").css({
				"display": "none"
			});
		}
				
		$(".intro").css({
			"display": "block"
		});
		$(".shouyi").css({
				"display": "none"
		});
		$(".banner2").css({
			"display": "none"
		});
	}
	
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