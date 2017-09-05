
	/*loading图片个数*/
	var max_line_num;
	
	function uploadImg(actionUrl,element,hiddenField) {
		if($(element).val()==""){
			alert("请选择上传文件！");
			return;
		}
		//如果是loading图，多文件上传。上传之前清空url列表和图片
		commonTextUp(actionUrl, $(element), function(data) {
	         //在回调函数中把返回的url赋值到文本框，以及img的src目录下去请求显示图片
	         //{message=成功, Data={appName=testad, iconPath=http://7xnj8k.com1.z0.glb.clouddn.com/icon/8a8080a0507003a901507009b7100001.png, path=http://7xnj8k.com1.z0.glb.clouddn.com/apk/8a8080a0507003a901507009b7100001testad.apk}, resultCode=200}
			 $(hiddenField).val(data.url);
			 if($(element).attr("id")=="multipleImg"){
					$("#content").html("");
					addLine(data.url,"multipleImg");
					max_line_num = $('#content').children('div').length;
			 }
			 if($(element).attr("id")=="Imgs"){
					$("#imgscontent").html("");
					addLine(data.url,"Imgs");
					max_line_num = $('#imgscontent').children('div').length;
			 }
		});

	}
	
	// 无刷新上传文件
	function commonTextUp(url, e, callback) {
		$.ajaxFileUpload({
			url : url,
			secureuri : false,
			fileElement : e,
			dataType : "text",//返回数据的类型
			success : function(data,status) {
				var repObj = $.parseJSON(data);
				if (status == "success") {
					if (callback) {
						callback(repObj);
					}
				} else {
					alert("上传失败！");
				}
				
			}
		});
	}
	
	



//添加一行loading图片
	function addLine(urls,flag){
		var result=urls.split(",");
				var html= "<div id=\"line#id\" class='myLine'> <span style='display:none;' class=\"btn-upload form-group\">\n" +
		"	        <input style='display:none;' class=\"input-text upload-url\" type=\"text\" name=\"proLoadingImg\" id=\"proLoadingImg\" value='#url' readonly  datatype=\"*\" nullmsg=\"请添加附件！\" style=\"width:200px\">\n" +
		"	        </span>\n" +
		"	        <span class=\"btn-upload imgSpan form-group\">\n" +
		"	        <img style='width:160px;height:100%;' src=\"#src\" >\n" +
		"	        </span></div>"; 
		
		for(var i=0;i<result.length;i++){
		   	var url = result[i];
			var src = url;
			var needHtml = html.replace("#src",src);
			var needHtml = needHtml.replace("#id",i+1);
			var needHtml = needHtml.replace(/\#url/g,url);
			if( flag == "multipleImg" ){
				$("#content").append($(needHtml)).css("width","100%");
				$("#content").find(".operateInput:last").val(i+1);
			}else if( flag == "Imgs" ){
				$("#imgscontent").append($(needHtml)).css("width","100%");
				$("#imgscontent").find(".operateInput:last").val(i+1);
			}
		}
	}
	
	function getImageIndexData(elementId){
		var imgStr = "";
		var myLines = $("#" + elementId).children("div.myLine");
		
		myLines = !!myLines ? myLines : [];
		$.each(myLines,function(index,myLine){
			var address = $(myLine).children("span.imgSpan").children("img").attr("src");
			if( imgStr.length > 0  ){
				imgStr += ","+ address;
			}else{
				imgStr += address;
			}
		});
		return imgStr;
	}
	