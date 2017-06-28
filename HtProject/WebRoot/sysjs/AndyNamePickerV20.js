/**
 * 用于存储全局所有数据
 */
var dataMap = new Map();

/**
 * 判断一个对象是否为空
 * @param strVal
 * @returns
 */
function isNullOrEmpty(strVal) 
{
	if (strVal == null || strVal == '' ||  strVal == undefined) 
	{
		return true;
	} 
	else 
	{
		return false;
	}
}

/**
 * 取得控件的绝对左位置
 * @param obj
 * @returns int
 */
function getAbsoluteObjectLeft(obj)
{
	var oLeft = obj.offsetLeft;
	while (obj.offsetParent != null)
	{
		oParent = obj.offsetParent;
		oLeft += oParent.offsetLeft;
		obj = oParent;
	}
	return oLeft;
}

/**
 * 获取控件上绝对上位置
 * @param obj
 * @returns int
 */
function getAbsoluteObjectTop(obj)
{
	var oTop = obj.offsetTop + obj.offsetHeight + 2;//加多一个2我也不知道为什么
	
	while (obj.offsetParent != null)
	{
		oParent = obj.offsetParent;
		oTop += oParent.offsetTop; // Add parent top position
		obj = oParent;
	}
	return oTop;
}

/**
 * 比较拼音字母的排序
 * @param obj1
 * @param obj2
 * @returns
 */
function compareJoSelOptionFunc(obj1,obj2)
{
	var result = 0;
	
	if(obj1.pyText > obj2.pyText)
		result = 1;
	else if(obj1.pyText < obj2.pyText)	
		result = -1;
	else 
		result = 0;
		
	return result;
}

/**
 *  Select 控件的 数据存储结构
 */
function joSelOption(id,value,text)
{
	var obj = {};
	obj.id = id;
	obj.value = value;
	obj.text = text;
	obj.pyText = pinyin.getCamelChars(text);
	return obj;
}

/**
 * 创建基础数据链
 * @param list
 * @returns
 */
function createBaseDataList(list)
{
	var baseList = new Array();
	
	list.sort(compareJoSelOptionFunc);
	
	for(var i=0; i<27; i++)
	{
		var arr = new Array();
		baseList.push(arr);
	}
	
	for(var i=0; i<list.length; i++)
	{
		if(list[i].pyText.charAt(0)=='A')
		{
			baseList[1].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='B')
		{
			baseList[2].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='C')
		{
			baseList[3].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='D')
		{
			baseList[4].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='E')
		{
			baseList[5].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='F')
		{
			baseList[6].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='G')
		{
			baseList[7].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='H')
		{
			baseList[8].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='I')
		{
			baseList[9].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='J')
		{
			baseList[10].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='K')
		{
			baseList[11].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='L')
		{
			baseList[12].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='M')
		{
			baseList[13].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='N')
		{
			baseList[14].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='O')
		{
			baseList[15].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='P')
		{
			baseList[16].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='Q')
		{
			baseList[17].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='R')
		{
			baseList[18].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='S')
		{
			baseList[19].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='T')
		{
			baseList[20].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='U')
		{
			baseList[21].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='V')
		{
			baseList[22].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='W')
		{
			baseList[23].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='X')
		{
			baseList[24].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='Y')
		{
			baseList[25].push(list[i]);
		}
		else if(list[i].pyText.charAt(0)=='Z')
		{
			baseList[26].push(list[i]);
		}
		else
		{
			baseList[0].push(list[i]);
		}
	}
	return baseList;
}


/**
 * 主方法，第一个是按钮控件，第二个是数据列表，第三个是动作结果
 */
function namePicker(parentObj,joOptionList,onDataSelect)
{
	if(parentObj==null || joOptionList==null)
	{
		alert("父对象和数据为空");
		return;
	}
		
	var objId = parentObj.id;
	
	if(objId==null || objId == "" || joOptionList.length<=0)
	{
		alert('父对像没有注明ID');
		return;
	}
	
	var baseDataList = dataMap.get("basedata" + objId);
	
	if(baseDataList==null)
	{
		joOptionList.push(new joSelOption(-1,"-1","#未选择"));
		baseDataList = createBaseDataList(joOptionList);
		dataMap.put("basedata" + objId,baseDataList);
		dataMap.put("oncallback" + objId,onDataSelect);
	}
	
	var showDiv = document.getElementById("name_picker_source_div_" + objId);
	
	if(showDiv==null)
	{
		showDiv = createShowDiv(parentObj,baseDataList);
		
		var idList = dataMap.get("data_id_list");
		
		if(idList==null)
		{
			idList = new Array();
			dataMap.put("data_id_list", idList);
		}
		
		idList.push(objId);
	}
	
	showDiv.style.display = "block";
	
	dataMap.put("isBlurFinish" + objId, false);
}

/**
 * 设置不知道什么鬼东西
 * @param objId
 * @param value
 * @returns
 */
function setIsOnDiv(objId,value)
{
	dataMap.put("isondiv" + objId, value);
	
	if(value==0)
	{
		var isBlurFinish = dataMap.get("isBlurFinish" + objId);
		
		if(isBlurFinish)
		{
			document.getElementById("name_picker_source_div_" + objId).style.display = "none";
		}
	}
}

/**
 * 选定数据的点击
 * @param objId 父ID
 * @param idi 
 * @param idj
 * @returns
 */
function onDataAClick(objId,idi,idj)
{
	var onCallBack = dataMap.get("oncallback" + objId);
	
	if(onCallBack!=null)
	{
		var baseData = dataMap.get("basedata" + objId);
		if(baseData!=null)
		{
			onCallBack(baseData[idi][idj]);
		}
	}
	
	document.getElementById("name_picker_source_div_" + objId).style.display = "none"; 
}


/**
 * 创建要设置的DIV
 * @param parentObj
 * @returns
 */
function createShowDiv(parentObj,dataList)
{
	var mydiv = document.createElement("div"); 
	mydiv.setAttribute("id","name_picker_source_div_" + parentObj.id); 
	mydiv.setAttribute("class","center");
	
	document.body.appendChild(mydiv);
	
	//显示位置，以后根据屏幕的情况进行动态分析，当前先写死右边显示
	//下面显示
	//mydiv.style.left =  getAbsoluteObjectLeft(parentObj) + "px";
	//mydiv.style.top = getAbsoluteObjectTop(parentObj) +  "px";
	
	//右边显示
	mydiv.style.left =  (getAbsoluteObjectLeft(parentObj) + parentObj.offsetWidth + 1) + "px";
	mydiv.style.top = (getAbsoluteObjectTop(parentObj) - parentObj.offsetHeight - 2) +  "px";
	
	var div2 = document.createElement("div");
	div2.setAttribute("id","no_name_div_2_" + parentObj.id); 
	div2.setAttribute("style","height:60px;border-bottom:1px #B7B7B7 solid;");
	mydiv.appendChild(div2);
	
	var div3 = document.createElement("div");
	div3.setAttribute("id","no_name_div_3_" + parentObj.id); 
	div3.setAttribute("style","height:30px;");
	div3.setAttribute("class","guideData");
	div2.appendChild(div3);
	
	var headHtml = "";
	var guildeCharts = ['#','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
	
	for(var i=0; i<guildeCharts.length; i++)
	{
		headHtml += "<a href='#' style='font-size: 14pt;' id='no_name_a_" + parentObj.id + "_" + guildeCharts[i] + "' onclick=locateData('" + parentObj.id + "','" +guildeCharts[i]  + "')>" +guildeCharts[i]  + "</a>\n";
	}
	
	div3.innerHTML = headHtml;
	
	var div4 = document.createElement("div");
	div4.setAttribute("id","no_name_div_4_" + parentObj.id); 
	div4.setAttribute("style","width: 200px;float: right;");
	
	//var div4Html = "<form onsubmit=\"searchData('" + parentObj.id + "'); return false;\">";
	//div4Html += "<input type=\"button\" id=\"button_search_id_" + parentObj.id + "\" value=\"查找\" style=\"float: right;font-size: 12pt; margin-top: -4px;cursor: pointer;border: 1px solid #000000;margin-right: 2px;\" onclick=\"searchData('" + parentObj.id + "')\" />";
	var div4Html = "<input type=\"text\" onkeyup=\"searchData('" + parentObj.id + "')\" id=\"input_search_id_" + parentObj.id + "\" value=\"\" style=\"float: right;margin-top: -1px;width:100px;border: 1px solid #000000;font-size: 11pt;margin-right: 25px;\"  />";
	
	div4.innerHTML = div4Html;
	
	div2.appendChild(div4);
	
	var div5 = document.createElement("div");
	div5.setAttribute("id","no_name_div_5_" + parentObj.id); 
	div5.setAttribute("class","dataDiv");
	mydiv.appendChild(div5);
	
	var div6 = document.createElement("div");
	div6.setAttribute("id","data_content_main_" + parentObj.id);
	div6.setAttribute("class","dataDivMain");
	div5.appendChild(div6);
	
	for(var i=0; i<dataList.length; i++)
	{
		if(dataList[i].length<=0)
			continue;
		
		var div = document.createElement("div");
		div.setAttribute("id","data_content_" + parentObj.id + "_" + guildeCharts[i]);
		div6.appendChild(div);
		
		var div2 = document.createElement("div");
		div2.setAttribute("id","no_name_guilde_div2_" + parentObj.id + "_" + guildeCharts[i]);
		div2.setAttribute("class","dataHeadDiv");
		div2.setAttribute("style","font-size: 12pt;");
		div2.innerHTML = guildeCharts[i];
		div.appendChild(div2);
		
		var div3 = document.createElement("div");
		div3.setAttribute("id","no_name_guilde_div3_" + parentObj.id + "_" + guildeCharts[i]);
		div3.setAttribute("class","dataParentdiv");
		
		//这里增加显示的内容
		var contentHtml = "";
		
		for(var j=0; j<dataList[i].length; j++)
		{
			contentHtml += "<div style='font-size: 12pt;' onclick=onDataAClick('"+ parentObj.id + "'," + i + "," + j + ")>" + dataList[i][j].text  + "</div>\n";
		}
		
		div3.innerHTML = contentHtml;
		
		div.appendChild(div3);
		
		var div4 = document.createElement("div");
		div4.setAttribute("id","no_name_guilde_div4_" + parentObj.id + "_" + guildeCharts[i]);
		div4.setAttribute("style","clear: both;");
		div.appendChild(div4);
	}
	
	var divSearch = document.createElement("div");
	divSearch.setAttribute("id","search_div_" + parentObj.id);
	divSearch.setAttribute("class","dataParentdiv");
	divSearch.setAttribute("style","display: none");
	
	div5.appendChild(divSearch);
	
	return mydiv;
}

function locateData(sId,locateId)
{
	var searchResultDiv = document.getElementById("search_div_" + sId);
	var dataContentDiv = document.getElementById("data_content_main_" + sId);
	
	searchResultDiv.style.display = "none";	
	dataContentDiv.style.display = "block";
	
    window.location.hash = "#data_content_" + sId + "_" + locateId;    
} 

function searchData(sId)
{
	var searchInputData = document.getElementById("input_search_id_" + sId).value;
	var searchResultDiv = document.getElementById("search_div_" + sId);
	var dataContentDiv = document.getElementById("data_content_main_" + sId);
	
	if(searchInputData==null || ""==searchInputData)
	{
		searchResultDiv.style.display = "none";	
		dataContentDiv.style.display = "block";
		return;
	}
	else
	{
		searchResultDiv.style.display = "block";	
		dataContentDiv.style.display = "none";
	}
	
	//清除查找DIV里的对像
	searchResultDiv.innerHTML = "";
	
	var dataList = dataMap.get("basedata" + sId);
	
	var contentHtml = "";
	
	for(var i=0; i<dataList.length; i++)
	{
		for(var j=0; j<dataList[i].length; j++)
		{
			if(dataList[i][j].text.indexOf(searchInputData)>=0 || dataList[i][j].pyText.indexOf(searchInputData.toUpperCase())>=0)
			{
				contentHtml += "<div style='font-size:12pt;border: 1px solid #CD661D;' onclick=onDataAClick('"+ sId + "'," + i + "," + j + ")>" + dataList[i][j].text  + "</div>\n";
			}
		}
	}
	
	searchResultDiv.innerHTML = contentHtml;
}

//外面点击隐藏DIV
$(document).bind('click',function(event)
{
	var eventId = $(event.target).attr("id");
	
	var dataIdList = dataMap.get("data_id_list");
	
	if(isNullOrEmpty(dataIdList))
		return;

	for(var i=0; i<dataIdList.length; i++)
	{
		if(eventId==dataIdList[i] 
			|| eventId==("name_picker_source_div_" + dataIdList[i]))
		{
			return;
		}
	}
	
	var eventNode = document.getElementById(eventId);
	
	var isHideAll = true;
	
	if(!isNullOrEmpty(eventNode))
	{
		var pNode = eventNode.parentNode;
		
		while(!isNullOrEmpty(pNode))
		{
			var pId = pNode.id;
			
			if(!isNullOrEmpty(pId))
			{
				if(pId.indexOf("name_picker_source_div")>=0)
				{
					return;
				}
			}
			
			pNode = pNode.parentNode;
		}
	}
	
	if(isHideAll)
	{
		for(var i=0; i<dataIdList.length; i++)
		{
			document.getElementById("name_picker_source_div_" + dataIdList[i]).style.display = "none";
		}
	}
	
});





































