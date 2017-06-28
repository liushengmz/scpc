//此文件说明
// 1、dataMap 是用于存储所有数据全局数据
var dataMap = new Map();

function joSelOption(id,value,text)
{
	var obj = {};
	obj.id = id;
	obj.value = value;
	obj.text = text;
	obj.pyText = pinyin.getCamelChars(text);
	return obj;
}

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
		baseDataList = createBaseDataList(joOptionList);
		dataMap.put("basedata" + objId,baseDataList);
		dataMap.put("oncallback" + objId,onDataSelect);
	}
	
	var showDiv = document.getElementById("name_picker_source_div_" + objId);
	
	if(showDiv==null)
	{
		showDiv = createShowDiv(parentObj,objId,baseDataList);
		
		var idList = dataMap.get("data_id_list");
		
		if(idList==null)
		{
			idList = new Array();
			dataMap.put("data_id_list", idList);
		}
		
		idList.push(objId);
	}
	
	showDiv.style.display = "block";
	
	dataMap.put("isBlurFinish" + parentObj.id, false);
}

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

function createShowDiv(parentObj,objId,jolist)
{
	mydiv = document.createElement("div"); 
	
	mydiv.setAttribute("id","name_picker_source_div_" + objId); 
	mydiv.style.position = "absolute";
	//mydiv.style.width="320px"; 
	//mydiv.style.height="250px";
	mydiv.style.backgroundColor="#F6F5F3";
	mydiv.style.border = "solid 1px #D1CDC5";
	mydiv.style.padding = "5px";
	mydiv.style.lineHeight = "1.5em";
	mydiv.style.fontSize = "14px";
	document.body.appendChild(mydiv);
	//下面显示
	//mydiv.style.left =  getAbsoluteObjectLeft(parentObj) + "px";
	//mydiv.style.top = getAbsoluteObjectTop(parentObj) +  "px";
	//右边显示
	mydiv.style.left =  (getAbsoluteObjectLeft(parentObj) + parentObj.offsetWidth ) + "px";
	mydiv.style.top = (getAbsoluteObjectTop(parentObj) - parentObj.offsetHeight) +  "px";
	
	
	//每个字母一行
	var characterList = ['0','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
	
	var html = "";
	
	for(var i=0; i<jolist.length; i++)
	{
		if(jolist[i].length>0)
		{
			html += "<span style='color: #FE984F;font-weight: bold;' >"+ characterList[i] +":&nbsp;&nbsp;&nbsp;</span>";
			for(var j=0; j<jolist[i].length; j++)
			{
				html += "<span style='cursor: pointer' onclick='onDataAClick(\""+ objId +"\","+ i +","+ j +")'>"+ jolist[i][j].text +"</span> ";
				
				if((j+1)%7==0)
					html += "<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				
			}
			html += "<br />";
		}
	}
	
	/*
	var html = "<span style='color: #FE984F;font-weight: bold;' >ABCD:&nbsp;&nbsp;&nbsp;</span>";
	
	for(var i=0; i<5; i++)
	{
		for(var j=0; j<jolist[i].length; j++)
		{
			html += "<span style='cursor: pointer' onclick='onDataAClick(\""+ objId +"\","+ i +","+ j +")'>"+ jolist[i][j].text +"</span> ";
			if((j+1)%7==0)
			{
				html += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			}
		}
	}
	
	html += "<br />";
	html += "<span style='color: #FE984F;font-weight: bold;' >EFGHI:&nbsp;&nbsp;&nbsp;&nbsp;</span>";
	
	for(var i=5; i<10; i++)
	{
		for(var j=0; j<jolist[i].length; j++)
		{
			html += "<span style='cursor: pointer' onclick='onDataAClick(\""+ objId +"\","+ i +","+ j +")'>"+ jolist[i][j].text +"</span> ";
			if((j+1)%7==0)
			{
				html += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			}
		}
	}
	
	html += "<br />";
	html += "<span style='color: #FE984F;font-weight: bold;' >JKLMN:&nbsp;&nbsp;</span>";
	
	for(var i=10; i<15; i++)
	{
		for(var j=0; j<jolist[i].length; j++)
		{
			html += "<span style='cursor: pointer' onclick='onDataAClick(\""+ objId +"\","+ i +","+ j +")'>"+ jolist[i][j].text +"</span> ";
			if((j+1)%7==0)
			{
				html += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			}
		}
	}
	
	html += "<br />";
	html += "<span style='color: #FE984F;font-weight: bold;' >OPQRS:&nbsp;&nbsp;&nbsp;&nbsp;</span>";
	
	for(var i=15; i<20; i++)
	{
		for(var j=0; j<jolist[i].length; j++)
		{
			html += "<span style='cursor: pointer' onclick='onDataAClick(\""+ objId +"\","+ i +","+ j +")'>"+ jolist[i][j].text +"</span> ";
			if((j+1)%7==0)
			{
				html += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			}
		}
	}
	
	html += "<br />";
	html += "<span style='color: #FE984F;font-weight: bold;' >TUVXYZ:&nbsp;&nbsp;&nbsp;</span>";
	
	for(var i=20; i<jolist.length; i++)
	{
		for(var j=0; j<jolist[i].length; j++)
		{
			html += "<span style='cursor: pointer' onclick='onDataAClick(\""+ objId +"\","+ i +","+ j +")'>"+ jolist[i][j].text +"</span> ";
			if((j+1)%7==0)
			{
				html += "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			}
		}
	}
	*/
	
	mydiv.innerHTML = html;
	
	//mydiv.onmouseover = function(){setIsOnDiv(objId,1);};
	//mydiv.onmouseout = function(){setIsOnDiv(objId,0);};
	
	return mydiv;
}

//取得控件的绝对左位置
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

//获取控件上绝对上位置
function getAbsoluteObjectTop(obj)
{
	var oTop = obj.offsetTop + obj.offsetHeight + 2;
	
	while (obj.offsetParent != null)
	{
		oParent = obj.offsetParent;
		oTop += oParent.offsetTop; // Add parent top position
		obj = oParent;
	}
	return oTop;
}

//外面点击隐藏DIV
$(document).bind('click',function(event){
	
	var dataIdList = dataMap.get("data_id_list");
	
	var eventId = $(event.target).attr("id");
	
	if(dataIdList==null)
		return;
	
	var hideAll = true;
	
	for(var i=0; i<dataIdList.length; i++)
	{
		if(eventId==dataIdList[i] || eventId==("name_picker_source_div_" + dataIdList[i]))
		{
			hideAll = false;
		}
	}
	
	if(hideAll)
	{
		for(var i=0; i<dataIdList.length; i++)
		{
			document.getElementById("name_picker_source_div_" + dataIdList[i]).style.display = "none";
		}
	}
	
});



