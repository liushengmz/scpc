/*header 事件start*/
var cdata = {};
ldata = {};
cdata.channelId = "";
cdata.companyId = "";
cdata.appId = "";
cdata.payWay = "";
cdata.dateType = 1;
cdata.endTime = "";
cdata.startTime = ""
max1=0,
max2=0;
	/*线上线下切换*/
$(".line").on("click mouseenter", function() {
	$(".changeline").show();
})
$(".line").on("click mouseleave", function() {
		$(".changeline").hide();
})
//	/*header 事件end*/
//	/*nav 事件start*/
//	/*nav 事件end*/
//var isSwitch = localStorage.isSwitch,
//	companyName = localStorage.companyname;
//$(".user span").text(companyName);
//if(isSwitch == 1) {
//	$(".changeline").remove();
//}
/*折线图switch切换start*/
$(".tabChange li").on("click", function() {
	$(".data1,.data2,.time1,.time2").val("");
	$(this).addClass("actives").siblings().removeClass("actives");
	cdata.dateType = Number($(this).attr("day"));
	
	cdata.channelId = $("#channelId").val();
	cdata.companyId = $("#companyId").val();	
	cdata.appId = $("#appId").val();
	cdata.payWay = $("#payWay").val();
	curxdata();
	
	charts();
})
$(".surebtn2").on("click", function() {
	if($(".data1").val() != "" && $(".data2").val() != "") {
		$(".tabChange li").removeClass("actives");
		cdata.dateType = 5;
		
		cdata.startTime = $(".data1").val();
		cdata.endTime = $(".data2").val();
	}
	cdata.channelId = $("#channelId").val();
	cdata.companyId = $("#companyId").val();	
	cdata.appId = $("#appId").val();
	cdata.payWay = $("#payWay").val();
	curxdata();	
	charts();
});
$(".surebtn2,.dld").on("mousedown",function(){
	$(this).css("background-color","#429842")
})
$(".surebtn2,.dld").on("mouseup",function(){
	$(this).css("background-color","#5eb95e")
})

$.datetimepicker.setLocale('ch');
$(".data1 , .data2").datetimepicker({
	format:"Y-m-d H:i",
	step:60,
	defaultTime:'00:00',
	timepickerScrollbar:false
})
$("body").on("focus", ".data1", function() {
	if($(".data2").val()) {
		$(".data1").datetimepicker({
			"minDate":getLastYearYestdy(new Date()),
			"maxDate": $(".data2").val(),
			startDate : new Date()
			
		});
	} else {
		$(".data1").datetimepicker({
			"minDate": getLastYearYestdy(new Date()),
			"maxDate": new Date(),
			startDate : new Date()
		});
	}
})
$("body").on("focus", ".data2", function() {
	if($(".data1").val()) {
		$(".data2").datetimepicker({
			"minDate": $(".data1").val(),
			"maxDate": new Date()
		});
	} else {
		$(".data2").datetimepicker({
			"maxDate": new Date()
		});
	}
})
curxdata();
function getLastYearYestdy(date){
    var strYear = date.getFullYear() - 1;     
    var strDay = date.getDate();     
    var strMonth = date.getMonth()+1;   
    if(strMonth<10)     
    {     
       strMonth="0"+strMonth;     
    }   
    if(strDay<10)     
    {     
       strDay="0"+strDay;     
    }   
    datastr = strYear+"-"+strMonth+"-"+strDay;
    return datastr;   
 }  
/*关键数据指标*/
function curxdata() {
	//console.log("curx")
	dateParam();
	$.ajax({
		type: "get",
		url: "curxData.do",
		async: true,
		data: cdata,
		success: function(data) {
			//console.log(data)
			if(!!data && data.resultCode == 200) {
				data = data.Data;
				$(".index-1 .allmoney").text(data.payAmount);
				$(".index-1 .allcount").text(data.payCount);
				$(".index-2 .allmoney").text(data.noPayAmount);
				$(".index-2 .allcount").text(data.noPayCount);
				$(".index-3 .allmoney").text(data.refundAmount);
				$(".index-3 .allcount").text(data.refundCount);
				var successNum = parseInt(data.payCount);				
				var totalNum = parseInt(data.payCount)+parseInt(data.noPayCount);
				if(totalNum == 0){
					$(".index-4 .index-1-data").text("0%");
				}else{
					$(".index-4 .index-1-data").text((successNum/totalNum).toFixed(4)*100+"%");
				}				
				$(".index-5 .index-1-data").text(data.payNet);
				$(".index-6 .index-1-data").text(data.rateAmount);
				console.log(successNum +"-----" +totalNum);
			} else {
				if(!!data.message) {
					layer.msg(data.message, {icon:2});
				} else {
					layer.msg("登录已失效，请重新登录", {icon:3});
					$("body").html(data);
				}
			}
		}
	});
}


charts();

function  DateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
     var  aDate,  oDate1,  oDate2,  iDays  
     aDate  =  sDate1.split("-")  
     oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0])    //转换为12-18-2006格式  
     aDate  =  sDate2.split("-")  
     oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0])  
     iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24)    //把相差的毫秒数转换为天数  
     return  iDays  
}
function arrMax(arr){
	var index = 0;  
    for(var x = 0; x < arr.length; x++){ 
    	if(arr[index] < arr[x]){  
            index = x;  
        }     
    } 
    return arr[index];
}
function charts() {
	/*折线图*/
	var myChart = echarts.init(document.getElementById('main')),
		xdata = [],
		ydata1 = [],
		ydata2 = [];
	option = {
		title: {},
		tooltip: {
			trigger: 'axis'
		},
		legend: {
			data: ['成功交易金额', '成功交易笔数']
		},
		grid: {
			left: '3%',
			right: '4%',
			bottom: '15%',
			containLabel: true
		},
		toolbox: {
			feature: {
				saveAsImage: {}
			}
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			axisLabel: {
				interval: 0,
				rotate: 60,
			},
			splitLine:{show: false},
			axisLabel:{interval: 2},
			data: ['00:00', '02:00', '04:00', '06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00', '24:00']
		},
		yAxis:  [{
			type: 'value',name:"金额",min:0
		},{type: 'value',name:"笔数",splitLine:{show: false},min:0}],
		series: [{
			name: '成功交易金额',
			type: 'line',
			itemStyle: {
				normal: {
					color: '#5d9cec'
				}
			},
			data: []
		}, {
			name: '成功交易笔数',
			type: 'line',
			itemStyle: {
				normal: {
					color: '#f15755'
				}
			},
			yAxisIndex: 1,
			data: []
		}]
	};
	linedata();
	function linedata() {
		ldata.channelId = cdata.channelId;
		ldata.companyId = cdata.companyId;
		ldata.appId = cdata.appId;
		ldata.payWay = cdata.payWay;
		ldata.dateType = cdata.dateType;
		ldata.endTime = cdata.endTime;
		ldata.startTime = cdata.startTime;
		datediff= DateDiff(ldata.endTime,ldata.startTime)
//		ldata.payWayId = $(".chooseWay select").find("option:selected").attr("paywayid");
		//console.log(datediff)
		$.ajax({
			type: "get",
			url: "timeTrend.do",
			async: false,
			data: ldata,
			success: function(data) {
				if(!!data && data.resultCode == 200) {
					var _data = data;
					data = data.Data.resultData;
					if(_data.Data.resultType == 1) {
						for(var i = 0; i < 24; i++) {
//							var k = 0,
//								m = -1;
//							for(var j = 0; j < data.length; j++) {
//								if(data[j].timeType == i) {
//									k = 1;
//									m = j;
//								}
//							}
//							if(k == 1) {
//								ydata1.push(data[m].successOrderMoney);
//								ydata2.push(data[m].successOrderCount);
//							} else {
//								ydata1.push("0");
//								ydata2.push("0");
//							}
							ydata1.push(data[i].payAmount);
							ydata2.push(data[i].payCount);
						}
						ydata1.push("0");
						ydata2.push("0");
						option.series[0].data = ydata1;
						option.series[1].data = ydata2;
						//console.log(ydata1);
//						option.yAxis[0].max=arrMax(ydata1);
//						option.yAxis[1].max=arrMax(ydata2);
						option.xAxis.data = ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00', '24:00']
					} else if(_data.Data.resultType == 2) {
						if($(".last7").hasClass("actives")){
							option.xAxis.axisLabel.interval=0;
						}
						if($(".last30").hasClass("actives")){
							option.xAxis.axisLabel.interval=2;
						}
						if(!$(".last7").hasClass("actives")&&!$(".last30").hasClass("actives")){
							if(datediff>16){
								option.xAxis.axisLabel.interval=parseInt(datediff/12);								
							}else{
								option.xAxis.axisLabel.interval=0
							}
						}
						for(var i = 0; i < data.length; i++) {
							var recordTime = new Date(data[i].recordTime);
							var y = recordTime.getFullYear(); 
						    var m = recordTime.getMonth()+1;
						    var d = recordTime.getDate();
						    if(d<10){
						        d = "0"+d;
						    }
							recordTime = y+"-"+m+"-"+d;
							xdata.push(recordTime);
							ydata1.push(data[i].payAmount);
							ydata2.push(data[i].payCount);
							option.xAxis.data = xdata;
							option.series[0].data = ydata1;
							option.series[1].data = ydata2;
						}
//						option.yAxis[0].max=arrMax(ydata1);
//						option.yAxis[1].max=arrMax(ydata2);
						//console.log(xdata)
						//option.xAxis.data=xdata;
					}
					max1 = arrMax(ydata1)
					max2 = arrMax(ydata2)
				} else {
					if(!!data.message) {
						alert(data.message);
					} else {
						//alert("登录已失效，请重新登录");
						$("body").html(data);
					}
				}
			}
		});
	}
	//console.log(option)
	
	/*折线图end*/
//	console.log(option.xAxis.axisLabel.interval)
	myChart.setOption(option);
}
//对时间搜索条件处理，根据对应的dateType，修改对应的开始结束时间
function dateParam(){		
	if(cdata.dateType == 1){			
		var dd = new Date(); 
	    var y = dd.getFullYear(); 
	    var m = dd.getMonth()+1;
	    var h = dd.getHours();
	    if(m<10){
	    	m = "0"+m;
	    }     
	    //获取当前月份的日期
	    var d = dd.getDate();
	    if(d<10){
	        d = "0"+d;
	    }
	    cdata.endTime = y+"-"+m+"-"+d + h;
	    cdata.startTime = y+"-"+m+"-"+d;
	}else if(cdata.dateType == 2){
		var dd = new Date(); 
		dd.setDate(dd.getDate()-1);
		var y = dd.getFullYear(); 
		var m = dd.getMonth()+1;
		if(m<10){
			m = "0"+m;
		}			
		//获取当前月份的日期			
		var d = dd.getDate();
		if(d<10){
			d = "0"+d;
		}					
		cdata.endTime = y+"-"+m+"-"+d + "23";
		cdata.startTime = y+"-"+m+"-"+d;
	}else if(cdata.dateType == 3){
		var dd = new Date(); 
		dd.setDate(dd.getDate()-7);
		var y = dd.getFullYear(); 
		var m = dd.getMonth()+1;
		if(m<10){
			m = "0"+m;
		}			
		//获取当前月份的日期			
		var d = dd.getDate();
		if(d<10){
			d = "0"+d;
		}
		
		cdata.startTime = y+"-"+m+"-"+d;
		
		var cc = new Date(); 
		cc.setDate(cc.getDate()-1);
		var y2 = cc.getFullYear(); 
		var m2 = cc.getMonth()+1;
		if(m2<10){
			m2 = "0"+m2;
		}			
		//获取当前月份的日期			
		var d2 = cc.getDate();
		if(d2<10){
			d2 = "0"+d2;
		}
		cdata.endTime = y2+"-"+m2+"-"+d2+ "23";
		
	}else if(cdata.dateType == 4){
		var dd = new Date(); 
		dd.setDate(dd.getDate()-30);
		var y = dd.getFullYear(); 
		var m = dd.getMonth()+1;
		if(m<10){
			m = "0"+m;
		}			
		//获取当前月份的日期			
		var d = dd.getDate();
		if(d<10){
			d = "0"+d;
		}
		
		cdata.startTime = y+"-"+m+"-"+d;
		
		var cc = new Date(); 
		cc.setDate(cc.getDate()-1);
		var y2 = cc.getFullYear(); 
		var m2 = cc.getMonth()+1;
		if(m2<10){
			m2 = "0"+m2;
		}			
		//获取当前月份的日期			
		var d2 = cc.getDate();
		if(d2<10){
			d2 = "0"+d2;
		}
		cdata.endTime = y2+"-"+m2+"-"+d2+ "23";
		
	}
}