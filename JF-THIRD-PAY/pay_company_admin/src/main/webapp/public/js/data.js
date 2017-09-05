/*header 事件start*/
var cdata = {};
ldata = {};
cdata.appId = "";
cdata.dateType = 1;
cdata.endTime = "";
cdata.startTime = ""
max1=0,
max2=0;
/*退出登录*/
$(".user i").on("click", function() {
	$(".changeuser").show();
})
$(".user").on("mouseleave", function() {
	$(".changeuser").hide();
})
$(".user i").on("mouseenter", function() {
	$(this).css("background-image","url(../public/img/down2.png)");
})
$(".user i").on("mouseleave", function() {
	$(this).css("background-image","url(../public/img/down1.png)");
})
	/*线上线下切换*/
$(".line").on("click mouseenter", function() {
	$(".changeline").show();
})
$(".line").on("click mouseleave", function() {
		$(".changeline").hide();
	})
	/*header 事件end*/
	/*nav 事件start*/
	/*nav 事件end*/
var isSwitch = localStorage.isSwitch,
	companyName = localStorage.companyname;
$(".user span").text(companyName);
if(isSwitch == 1) {
	$(".changeline").remove();
}
/*折线图switch切换start*/
$(".tabChange li").on("click", function() {
	$(".data1,.data2").val("");
	$(this).addClass("actives").siblings().removeClass("actives");
	cdata.dateType = Number($(this).attr("day"));
	curxdata();
	goods();
	passageway();
	charts();
})
$(".surebtn2").on("click", function() {
	if($(".data1").val() != "" && $(".data2").val() != "") {
		$(".tabChange li").removeClass("actives");
		cdata.dateType = 5;
		cdata.startTime = $(".data1").val();
		cdata.endTime = $(".data2").val();
	}
	cdata.appId = $(".choose option:selected").attr("appid");
	curxdata();
	goods();
	passageway();
	charts();
});
$(".surebtn2,.dld").on("mousedown",function(){
	$(this).css("background-color","#2ea1f9")
})
$(".surebtn2,.dld").on("mouseup",function(){
	$(this).css("background-color","#2581f4")
})
$("body").on("focus", ".data1", function() {
	if($(".data2").val()) {
		WdatePicker({
			"minDate":getLastYearYestdy(new Date()),
			"maxDate": $(".data2").val()
		});
	} else {
		WdatePicker({
			"minDate":getLastYearYestdy(new Date()),
			"maxDate": new Date()
		});
	}
})
$("body").on("focus", ".data2", function() {
	if($(".data1").val()) {
		WdatePicker({
			"minDate": $(".data1").val(),
			"maxDate": new Date()
		});
	} else {
		WdatePicker({
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
	$.ajax({
		type: "get",
		url: "/offline/bussCompanyData/curxData.do",
		async: true,
		data: cdata,
		success: function(data) {
			//console.log(data)
			if(!!data && data.resultCode == 200) {
				data = data.Data;
				$(".index-1 .index-1-data").text(data.allSuccessMoney);
				$(".index-2 .index-1-data").text(data.allSuccessCount);
				$(".index-3 .index-1-data").text(data.allFailMoney);
				$(".index-4 .index-1-data").text(data.allFailCount);
				$(".index-5 .index-1-data").text(data.allProcessingMoney);
				$(".index-6 .index-1-data").text(data.allProcessingCount);
			} else {
				if(!!data.message) {
					alert(data.message);
				} else {
					alert("登录已失效，请重新登录");
					$("body").html(data);
				}
			}
		}
	});
}
/*获取交易渠道*/
jyWays()

function jyWays() {
	console.log("list.do")
	$.ajax({
		type: "get",
		url: "/offline/bussCompanyOrder/getPayWayList.do",
		async: false,
		success: function(data) {
			//console.log(data);
			if(!!data && data.resultCode == 200) {
				data = data.Data;
				var str = "<option paywayid= >全部</option>";
				for(var i = 0; i < data.length; i++) {
					str += "<option paywayid=" + data[i].payWayId + ">" + data[i].payWayUserName + "</option>";
				}
				$(".chooseWay select").html(str);
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
/*交易分布饼状图数据*/
var optioncdata = [],
	colorArr = ['#2581f4','#25c281','#0068b7', '#52cdd5','#fde3a7']
valueData3 = [],
	data3 = [],
	optionddata = [],
	valueData4 = [],
	data4 = [],
	optionbdata = [],
	valueData2 = [],
	data2 = [],
	optionadata = [],
	valueData1 = [],
	data1 = [];

function goods() {
	//console.log("goods")
	$.ajax({
		type: "get",
		url: "/offline/bussCompanyData/tradeDistributedGoods.do",
		async: false,
		data: cdata,
		success: function(data) {
				//console.log(data);
			if(!!data && data.resultCode == 200) {
				data = data.Data;
				optioncdata.splice(0, optioncdata.length);;
				valueData3.splice(0, valueData3.length);;
				optionddata.splice(0, optionddata.length);;
				valueData4.splice(0, valueData4.length);;
				for(var i = 0; i < data.goodsMoneyMap.length; i++) {
					optioncdata.push(data.goodsMoneyMap[i].goods_name);
					valueData3.push(data.goodsMoneyMap[i].allMoney);
				}
				for(var i = 0; i < data.goodsCountMap.length; i++) {
					optionddata.push(data.goodsCountMap[i].goods_name);
					valueData4.push(data.goodsCountMap[i].allCount);
				}
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
goods();
passageway();

function passageway() {
	//console.log("passageway")
	$.ajax({
		type: "get",
		url: "/offline/bussCompanyData/tradeDistributedPayWay.do",
		async: false,
		data: cdata,
		success: function(data) {
			//console.log(data);
			if(!!data && data.resultCode == 200) {
				data = data.Data;
				optionbdata.splice(0, optionbdata.length);
				valueData2.splice(0, valueData2.length);
				optionadata.splice(0, optionadata.length);
				valueData1.splice(0, valueData1.length);
				for(var i = 0; i < data.topCount.length; i++) {
					optionbdata.push(data.topCount[i].payWayName);
					valueData2.push(data.topCount[i].allCount);
				}
				for(var i = 0; i < data.topMoney.length; i++) {
					optionadata.push(data.topMoney[i].payWayName);
					valueData1.push(data.topMoney[i].allMoney);
				}
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
/*交易分布环状图数据end*/
charts();
$("body").on("change",".chooseWay select",function(){
	charts();
})
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
		ldata.appId = cdata.appId;
		ldata.dateType = cdata.dateType;
		ldata.endTime = cdata.endTime;
		ldata.startTime = cdata.startTime;
		datediff= DateDiff(ldata.endTime,ldata.startTime)
		ldata.payWayId = $(".chooseWay select").find("option:selected").attr("paywayid");
		//console.log(datediff)
		$.ajax({
			type: "get",
			url: "/offline/bussCompanyData/timeTrend.do",
			async: false,
			data: ldata,
			success: function(data) {
				if(!!data && data.resultCode == 200) {
					var _data = data;
					data = data.Data.resultData;
					if(_data.Data.resultType == 1) {
						for(var i = 0; i < 24; i++) {
							var k = 0,
								m = -1;
							for(var j = 0; j < data.length; j++) {
								if(data[j].timeType == i) {
									k = 1;
									m = j;
								}
							}
							if(k == 1) {
								ydata1.push(data[m].successOrderMoney);
								ydata2.push(data[m].successOrderCount);
							} else {
								ydata1.push("0");
								ydata2.push("0");
							}
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
							xdata.push(data[i].statisticsStringTime);
							ydata1.push(data[i].successOrderMoney);
							ydata2.push(data[i].successOrderCount);
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
	//console.log(option.xAxis.axisLabel.interval)
	myChart.setOption(option);
	/*环状图1*/
	var myCharta = echarts.init(document.getElementById('main1'));
	optiona = {
		tooltip: {
			trigger: 'item',
			formatter: "{a} <br/>{b}: {c}元 ({d}%)"
		},
		legend: {
			orient: 'vertical',
			x: 'left',
			data: ['支付宝', '微信']
		},
		series: [{
			name: '交易金额通道分布',
			type: 'pie',
			radius : '55%',
            center: ['50%', '60%'],
			avoidLabelOverlap: false,
			 itemStyle: {
	                emphasis: {
	                    shadowBlur: 10,
	                    shadowOffsetX: 0,
	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	                }
	            },
			
			data: [{
				value: 235,
				itemStyle: {
					normal: {
						color: '#2581f4'
					}
				},
				name: '支付宝'
			}, {
				value: 310,
				itemStyle: {
					normal: {
						color: '#52cdd5'
					}
				},
				name: '微信'
			}]
		}]
	};
	data1.splice(0, data1.length)
	for(var i = 0; i < optionadata.length; i++) {
		var a = {
			value: valueData1[i],
			itemStyle: {
				normal: {
					color: colorArr[i]
				}
			},
			name: optionadata[i]
		}
		data1.push(a);
	}
	if(data1.length){
		optiona.legend.data.splice(0, optiona.legend.data.length);
		optiona.series[0].data.splice(0, optiona.series[0].data.length)
		optiona.legend.data = optionadata;
		optiona.series[0].data = data1;
		myCharta.setOption(optiona);
	}else{
		$("#main1").html("<div  align='center' style='width:100%;height:220px;'><img src='../public/img/nodata.png' style='margin-top:100px;'/></div><div style='text-align:center;'>暂无数据</div>")
	}
	
	/*环状图2*/
	var myChartb = echarts.init(document.getElementById('main2'));
	optionb = {
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b}: {c}笔 ({d}%)"
			},
			legend: {
				orient: 'vertical',
				x: 'left',
				data: ['支付宝', '微信']
			},
			series: [{
				name: '交易笔数通道分布',
				type: 'pie',
				radius : '55%',
	            center: ['50%', '60%'],
				avoidLabelOverlap: false,
				 itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
				
				data: [{
					value: 235,
					itemStyle: {
						normal: {
							color: '#2581f4'
						}
					},
					name: '支付宝'
				}, {
					value: 310,
					itemStyle: {
						normal: {
							color: '#52cdd5'
						}
					},
					name: '微信'
				}]
			}]
	};
	data2.splice(0, data2.length)
	for(var i = 0; i < optionbdata.length; i++) {
		var a = {
			value: valueData2[i],
			itemStyle: {
				normal: {
					color: colorArr[i]
				}
			},
			name: optionbdata[i]
		}
		data2.push(a);
	}
	if(data2.length){
		optionb.legend.data.splice(0, optionb.legend.data.length);
		optionb.series[0].data.splice(0, optionb.series[0].data.length);
		optionb.legend.data = optionbdata;
		optionb.series[0].data = data2;
		myChartb.setOption(optionb);
	}else{		
		$("#main2").html("<div  align='center' style='width:100%;height:220px;'><img src='../public/img/nodata.png' style='margin-top:100px;'/></div><div style='text-align:center;'>暂无数据</div>")
	}
	
	/*环状图3*/
	var myChartc = echarts.init(document.getElementById('main3'));
	optionc = {
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b}: {c}元 ({d}%)"
			},
			legend: {
				orient: 'vertical',
				x: 'left',
				data: ['支付宝', '微信']
			},
			series: [{
				name: '交易金额TOP5收入占比',
				type: 'pie',
				radius : '55%',
	            center: ['50%', '60%'],
				avoidLabelOverlap: false,
				 itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
				
				data: [{
					value: 235,
					itemStyle: {
						normal: {
							color: '#2581f4'
						}
					},
					name: '支付宝'
				}, {
					value: 310,
					itemStyle: {
						normal: {
							color: '#52cdd5'
						}
					},
					name: '微信'
				}]
			}]
	};
	data3.splice(0, data3.length)
	for(var i = 0; i < optioncdata.length; i++) {
		var a = {
			value: valueData3[i],
			itemStyle: {
				normal: {
					color: colorArr[i]
				}
			},
			name: optioncdata[i]
		}
		data3.push(a);
	}
	if(data3.length){
		optionc.legend.data.splice(0, optionc.legend.data.length);
		optionc.series[0].data.splice(0, optionc.series[0].data.length);
		optionc.legend.data = optioncdata;
		optionc.series[0].data = data3;
		//console.log(optioncdata)
		console.log(optionc);
		myChartc.setOption(optionc);
	}else{
		$("#main3").html("<div  align='center' style='width:100%;height:220px;'><img src='../public/img/nodata.png' style='margin-top:100px;'/></div><div style='text-align:center;'>暂无数据</div>")
	}
	
	/*环状图4*/
	var myChartd = echarts.init(document.getElementById('main4'));
	optiond = {
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b}: {c}笔 ({d}%)"
			},
			legend: {
				orient: 'vertical',
				x: 'left',
				data: ['支付宝', '微信']
			},
			series: [{
				name: '交易笔数TOP5收入占比',
				type: 'pie',
				radius : '55%',
	            center: ['50%', '60%'],
				avoidLabelOverlap: false,
				 itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
				
				data: [{
					value: 235,
					itemStyle: {
						normal: {
							color: '#2581f4'
						}
					},
					name: '支付宝'
				}, {
					value: 310,
					itemStyle: {
						normal: {
							color: '#52cdd5'
						}
					},
					name: '微信'
				}]
			}]
	};
	data4.splice(0, data4.length)
	for(var i = 0; i < optionddata.length; i++) {
		var a = {
			value: valueData4[i],
			itemStyle: {
				normal: {
					color: colorArr[i]
				}
			},
			name: optionddata[i]
		}
		data4.push(a);
	}
	if(data4.length){
		optiond.legend.data.splice(0, optiond.legend.data.length);
		optiond.series[0].data.splice(0, optiond.series[0].data.length);
		optiond.legend.data = optionddata;
		//console.log(optiond.series[0].data);
		optiond.series[0].data = data4;
		//console.log(optiond)
		//console.log(optiond.series[0].data);
		myChartd.setOption(optiond);
	}else{
		$("#main4").html("<div  align='center' style='width:100%;height:220px;'><img src='../public/img/nodata.png' style='margin-top:100px;'/></div><div style='text-align:center;'>暂无数据</div>")
	}
	
}