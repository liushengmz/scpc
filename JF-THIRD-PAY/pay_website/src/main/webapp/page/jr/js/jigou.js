/**
 * 机构页面js
 **/
var pageIndex;   //页面请求参数---id

$(function(){
	/**获取页面参数**/
	pageIndex = getParam("pageIndex");
//	$.ajax({
//		type:"get",
//		url:"dataJson/jigou.json",
//		async:false,
//		success:function(Data){
////			初始化页面数据
//			var dataObj = !!Data.data[pageIndex] ? Data.data[pageIndex] : {};
//			initJgPageData(dataObj);
//		}
//	});
	
	$.getJSON('dataJson/jigou.json?callback=?', function(Data){
		//初始化页面数据
		var dataObj = !!Data.data[pageIndex] ? Data.data[pageIndex] : {};
		initJgPageData(dataObj);
	});
});


/**
 *  初始化页面数据
 *  @param dataObj: 数据对象
 * */
function initJgPageData(dataObj){
	var productRate = !!dataObj["productRate"] ? dataObj["productRate"].split(",") : [];
	var annual = !!dataObj["annual"] ? dataObj["annual"] : 0;
	
	$(".backName").text(dataObj.backName);
	
	/**官方平均年化率**/
	$(".officialRate .minx").text(productRate[0]);
	$(".officialRate .maxx").text(productRate[1]);
	
	/**补贴后年化率**/
	var miniAnnual = ( Number(annual) + Number( productRate[0] ) ).toFixed(2);
	var maxAnnual = (Number(annual) + Number( productRate[1] )).toFixed(2);
	$(".afterRate .minx").text(miniAnnual);
	$(".afterRate .maxx").text(maxAnnual);
	
	/**补贴后每万元收益(元)**/
	var minPerWanAfter = ( (miniAnnual*100)/365 ).toFixed(3);
	var maxPerWanAfter = ( (maxAnnual*100)/365 ).toFixed(3);
	
	$(".perWanAfter .mi").text(minPerWanAfter);
	$(".perWanAfter .ma").text(maxPerWanAfter);
	
	/**官方每万元收益(元)**/
	var minPerWanGuan =( ( Number( productRate[0] )*100)/365 ).toFixed(3);
	var maxPerWanGuan =( ( Number( productRate[1] )*100)/365 ).toFixed(3);
	
	$(".perWanGuan .mi").text(minPerWanGuan);
	$(".perWanGuan .ma").text(maxPerWanGuan);
	
	/**起投金额**/
	var investAmount = !!dataObj['investAmount'] ? dataObj['investAmount'] : 0;   //起投金额
	$(".investAmount").text(investAmount + "元起");
	
	/**锁定期**/
	$(".deadline").text("活期"+dataObj['deadline']);
	
	/**产品原利率**/
	var productRateStr = productRate[0]+"%~"+productRate[1];
	$(".productRate").text(productRateStr);
	
	/***首投补贴**/
	$(".annual").text(dataObj['annual']);
	
	/***复投加送**/
	$(".redoBt").text(dataObj['redo']);
	
	/**补贴上限**/
	$(".subsidy").text(dataObj['subsidy']);
	
	/**担保机构**/
	$(".guaranteeAgency").text(dataObj['guaranteeAgency']);
	
	/**托管机构**/
	$(".trusteeAgency").text(dataObj['trusteeAgency']);
}
/***
 * 获取请求参数
 * @param paramName : 参数名称
 * */
function getParam(paramName) {
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

(function hov(){	
	$(".tab1").bind("touchstart",function(){
		$(".change1").css({"display":"block"});
		$(".change2").css({"display":"none"});
		$(this).css({
			"color":"#007aff",
			"borderBottom":"2px solid #007aff"
		});
		$(".tab2").css({
			"color":"#000",
			"borderBottom":"1px solid #fff"
		});
	});
	$(".tab2").bind("touchstart",function(){
		$(".change2").css({"display":"block"});
		$(".change1").css({"display":"none"});
		$(this).css({
			"color":"#007aff",
			"borderBottom":"2px solid #007aff"
		});
		$(".tab1").css({
			"color":"#000",
			"borderBottom":"1px solid #fff"
		});
	});
})()






