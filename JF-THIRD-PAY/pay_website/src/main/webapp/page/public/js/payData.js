$(function(){
	var payWayId='';
	//事件start
	$("body").on("touchend", ".choose", function() {
		$(".purseChoose img").attr("src","../public/img/c1.png");
		$(".choose").addClass("c1").removeClass("cs").parent().parent().attr("choose","");
		$(this).addClass("cs").removeClass("c1").parent().parent().attr("choose","choose");
		$(".btn").html("确认支付"+$(".odMoney").html()+"元");
	})
	$("body").on("touchend", ".purseChoose", function() {
		$(".purseChoose img").attr("src","../public/img/c.png");
		$(".choose").addClass("c1").removeClass("cs");
		payWayId=$(this).attr("payway");
		$(".btn").html("确认支付"+(Number($(".odMoney").html())-Number($(".reduceMoney span").html()))+"元");
	})
	$("body").on("touchend",".otherPurse",function(){
		$("body").css({"height":"100%","width":"100%","overflow":"hidden"})
		$(".bg").show();
		$(".otsPurse").show();
	})
	$("body").on("touchend",".otsImg",function(){
		$("body").css({"height":"","width":"","overflow":""})
		$(".bg").hide();
		$(".otsPurse").hide();
	})
	$("body").on("touchend",".choose",function(){
		payWayId=$(this).attr("payway");
	});
	$("body").on("touchend",".bg",function(){
		$(".bg").hide();
		$(".otsPurse").hide();
	});
	$("body").on("touchend",".ots",function(){
		payWayId=$(this).attr("payway");
		$(".purseChoose img").attr("src","../public/img/c.png");
		$(".choose").addClass("c1").removeClass("cs");
		$(".purseChoose").attr("payway",$(this).attr("payway"));
		$(".psPays").attr("urls",$(this).attr("urls"));
		$(".purseIcon img").attr("src",$(this).children().children("img").attr("src")) ;
		$(".purseName").html($(this).children().children(".bankUp").html());
		$(".purseOnsale").html($(this).children().children(".bankDown").html());
		$(".reduceMoney").html($(this).children(".otSale").html());
		$(this).children(".checked").css({"backgroundImage":"url(../public/img/checked.png)","backgroundSize":"100% 100%"});
		$(this).siblings().children(".checked").css("backgroundImage","url()");
		$(".bg").hide();
		$(".otsPurse").hide();
		$(".btn").html("确认支付"+(Number($(".odMoney").html())-Number($(".reduceMoney span").html()))+"元");
	})
	$("body").on("touchend",".downloadPurse",function(){
		window.location.href=$(".psPays").attr("urls");
	})
	$("body").on("touchend",".btn",function(){
		var param={};
		param.payWayId=payWayId;
		param.orderNum=orderNum;
		var browser = {
			versions: function() {
				var u = navigator.userAgent,
					app = navigator.appVersion;
				return { //移动终端浏览器版本信息
					trident: u.indexOf('Trident') > -1, //IE内核
					presto: u.indexOf('Presto') > -1, //opera内核
					webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
					gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
					mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
					ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
					android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
					iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
					iPad: u.indexOf('iPad') > -1, //是否iPad
					webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
				};
			}(),
			language: (navigator.browserLanguage || navigator.language).toLowerCase()
		}
		if(browser.versions.mobile) {
			if(browser.versions.ios) {
				param.appType="2";
			}
			if(browser.versions.android) {
				param.appType="1";
				
			}
		}
		console.log(param);
		$.ajax({
			type:"get",
			url:"/payDetail/payH5Alipay.do",
			async:true,
			data:param,
			success:function(data){
				data = JSON.parse(data)
				//window.location="http://192.168.1.118:8080/jinfu_transfer/page/pay/aliPayH5.html?data="+data;
				console.log(data);
				//$("body").html(data);
				if(data.resultCode==200){
					if(data.Data.webStr){
						window.location.href=data.Data.webStr
					}else if(data.Data.alipayStr){
						$("body").html(data.Data.alipayStr);
					}
				}else{
					alert(data.message);	
				}
			}
		});
	})
	//事件end
	//页面传参处理
	if(location.search.indexOf("?paramStr=")!=-1){
		var newstr=location.search.slice(10,location.search.length);
		//newstr=encodeURIComponent(newstr);
		newstr = decodeURIComponent(newstr);
		$.ajax({
			type: "get",
			url: "/pay/payOrderH5Detail.do",
			data: {"paramStr":newstr},
			async: true,
			success: function(data) {
				data=JSON.parse(data);
				console.log(data);
				if(!!data&&data.resultCode==200){
					data=data.Data;
					if(data.otherList.length!=0){
						payWayId=data.otherList[0].id;
					}
					orderNum=data.orderNum;
					initializeData(data);
				}else{
					alert(data.message);
					window.history.go(-2);
				}
				
			}
		
		});
	}else{
		var obj=transmit();
		$.ajax({
			type: "get",
			url: "/payDetail/payOrderH5Detail.do",
			data: obj,
			async: true,
			success: function(data) {
				data=JSON.parse(data);
				console.log(data);
				if(!!data&&data.resultCode==200){
					data=data.Data;
//					if(data.walletList.length!=0){			
//						payWayId=data.walletList[0].id;
//					}else if(data.otherList.length!=0){
//						payWayId=data.otherList[0].id;
//					}
					if(data.otherList.length!=0){
						payWayId=data.otherList[0].id;
					}
					orderNum=data.orderNum;
					initializeData(data);
				}else{
					alert(data.message);
					window.history.go(-2);
				}
				
			}
		
		});
	}
	
})
function transmit(){
	var str = window.location.search;
	str=str.slice(1,str.length);
	str=str.split("&");
	var obj={},obj2={};
	for(var i=0;i<str.length;i++){
		strs=str[i];
		strs=strs.split("=");
		obj[strs[0]]=strs[1];			
	}
	
	for(var key in obj){
		obj2[key]=decodeURIComponent(obj[key]);
	}
	return obj2;
}

function initializeData(result) {
	//判断平台
	//var urls = platform(result);
	$(".odInforWords").html(result.orderName);
	$(".odMoney").html(result.payMoney);
	//初始化钱包支付方式
	if(result.otherList.length==0){
		$(".othersPay").hide();
		$(".otherPay").hide();
	}
	
	
	$(".maxSale").html("最高优惠" + result.maxDiscountMoney + "元");
	//$(".psPays").attr("urls",urls[0]);
	
	//其它钱包支付方式
//	var length = result.walletList.length;
//	var purseStr = "";
//	var str = '<div class="ots"><div class="bankIcon fl"><img src="img/tf.png" /></div><div class="bankIntro fl"><div class="bankUp">天府钱包</div><div class="bankDown">每日限量单先到先得罄</div></div><div class="fr checked"></div><div class="fr otSale">-￥<span>3.80</span></div></div>'
//	for(var i = 0; i < length; i++) {
//		purseStr += str;
//	}
//	$('.otsCon').html(purseStr);
//	for(var i = 0; i < length; i++) {
//		$(".otsCon .ots").eq(i).attr("urls", urls[i])
//		$(".otsCon .ots").eq(i).attr("payway", result.walletList[i].id)
//		$(".otsCon .ots").eq(i).children().find(".bankIcon img").attr("src", result.walletList[i].wayIcon);
//		$(".otsCon .ots").eq(i).children().find(".bankUp").html(result.walletList[i].wayName);
//		$(".otsCon .ots").eq(0).children(".checked").css({"backgroundImage":"url(../public/img/checked.png)","backgroundSize":"100% 100%"});
//		$(".otsCon .ots").eq(i).children().find(".bankDown").html(result.walletList[i].waySlogan);
//		$(".otsCon .ots").eq(i).children(".otSale").children().html(result.walletList[i].discountMoney);
//	}
	//其他支付方式
	var otherStr = "";
	var length = result.otherList.length;
	var otherstr = '<div class="cardPay" choose=""><div class="cpCon"><div class="logoIcon fl"><img src="" /></div><div class="words fl"><div class="wsUp">银行卡支付</div><div class="wsDown">安全快速无需开通网银</div></div><div class="choose fr c1"></div></div></div>'
	for(var i = 0; i < length; i++) {
		otherStr += otherstr;
	}
	$('.othersPay').html(otherStr);
	for(var i = 0; i < length; i++) {
		$(".othersPay .cardPay").eq(i).children().find(".choose").attr("payway",result.otherList[i].id)
		$(".othersPay .cardPay").eq(i).children().find(".logoIcon img").attr("src", result.otherList[i].wayIcon);
		$(".othersPay .cardPay").eq(i).children().children().find(".wsUp").html(result.otherList[i].wayName);
		$(".othersPay .cardPay").eq(i).children().children().find(".wsDown").html(result.otherList[i].waySlogan);
		$(".othersPay .cardPay").eq(i).children(".otSale").html("-￥" + result.otherList[i].discountMoney);
	}
//	if(result.walletList.length!=0){
//		$(".purseChoose").attr("payway",result.walletList[0].id)
//		$(".purseIcon img").attr("src", result.walletList[0].wayIcon);
//		$(".purseName").html(result.walletList[0].wayName);
//		$(".purseOnsale").html(result.walletList[0].waySlogan);
//		$(".reduceMoney span").html(result.walletList[0].discountMoney);
//		$(".btn").html("确认支付"+(Number($(".odMoney").html())-Number($(".reduceMoney span").html()))+"元");
//	}else{
		$(".pursePay").hide();
		$(".purse").hide();
		$(".cardPay").eq(0).children().children(".choose").addClass("cs").removeClass("c1");
		$(".btn").html("确认支付"+$(".odMoney").html()+"元");
	//}
}

//判断用什么客户端打开
function platform(result) {
	var urls=[];
	var browser = {
		versions: function() {
			var u = navigator.userAgent,
				app = navigator.appVersion;
			return { //移动终端浏览器版本信息
				trident: u.indexOf('Trident') > -1, //IE内核
				presto: u.indexOf('Presto') > -1, //opera内核
				webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
				gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
				mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
				ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
				android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
				iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
				iPad: u.indexOf('iPad') > -1, //是否iPad
				webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
			};
		}(),
		language: (navigator.browserLanguage || navigator.language).toLowerCase()
	}
	if(browser.versions.mobile) {
		if(browser.versions.ios) {
			var length = result.walletList.length;
			for(var i = 0; i < length; i++) {
				urls.push(result.walletList[i].wayIosUrl);
			}
		}
		if(browser.versions.android) {
			var length = result.walletList.length;
			for(var i = 0; i < length; i++) {
				urls.push(result.walletList[i].wayApkUrl)
			}
		}
	}
	return urls;
}