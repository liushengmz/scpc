var ec=0,
	param={},
	iossi=0,
	ios1i=0,
	ios2i=0
	and1=0; 
var thisid=location.search.slice(7,location.search.length);
/*header 事件start*/
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
			
$(".line").on("click mouseenter", function() {
	$(".changeline").show();
})
$(".line").on("click mouseleave", function() {
	$(".changeline").hide();
})
$(".imgheight div").on("mouseenter",function(){
	$(this).css("border","1px solid #2581f4")
	$(this).find("img").attr("src","../public/img/add1.png");
})
$(".imgheight div").on("mouseleave",function(){
	$(this).css("border","1px solid #d9d9d9")
	$(this).find("img").attr("src","../public/img/add.png");
})
	/*header 事件start*/
	/*nav 事件start*/

/*nav 事件end*/
//$("body").on("click",".imgsCon img",function(){
//	$(".bg").css("height",$(".main").height()+61+"px")
//	$(".bg").show();
//})
/*编辑*/
$(".edit").on("click",function(){
	
	ec++;
	$(".nodata").remove();
	$(".edit").hide();
	$("span.introwords").css("display","block");
	var str=""
	if(ec==1){
		$.ajax({
			type:"get",
			url:"/online/payv2BussCompanyApp/getAppTypeList.do",
			async:false,
			success:function(data){
				//console.log(data);
				if(!!data&&data.resultCode==200){
					str="<select>";
					//console.log(data.Data.appTypeList);
					for(var i=0;i<data.Data.appTypeList.length;i++){
						str+="<option value='"+data.Data.appTypeList[i].id+"'>"+data.Data.appTypeList[i].typeName+"</option>";
					}
					str+="</select>";
					
				}else{
					if(!!data.message){
						alert(data.message);
					}else{
						alert("登录已失效，请重新登录");
						$("body").html(data);
					}
				}
			}
		});
		//console.log(str)
		param.appName=$(".appName dd").html();
		param.appIcon=$(".appIcon dd img").eq(0).attr("src");
		param.appDesc=$(".appDesc dd").html();
		param.iosIphoneUrl=$(".iosIphoneUrl .dd-up").html();
		param.iosIphoneId=$(".iosIphoneId .dd-up").html();
		param.iosIphoneTestId=$(".iosIphoneTestId .dd-up").html();
		param.iosIpaUrl=$(".iosIpaUrl .dd-up").html();
		param.iosIpaMd5=$(".iosIpaMd5 .dd-up").html();
		param.iosIpaTestId=$(".iosIpaTestId .dd-up").html();
		param.androidAppUrl=$(".androidAppUrl .dd-up").html();
		param.androidAppMd5=$(".androidAppMd5 .dd-up").html();
		param.androidAppPackage=$(".androidAppPackage .dd-up").html();
		$(".account-footer").show();
		$(".imgsCon i").show();
		$(".addImg").show();
		$(".addimg").show();
		$(".appName dd").html("<input type='text' value='' placeholder='"+param.appName+"' />");
		$(".typeName dd").html(str);
		$(".appDesc dd").html("<textarea value='' placeholder='"+param.appDesc+"' />");
		$(".iosIphoneUrl .dd-up").html("<input type='text' value='' placeholder='"+param.iosIphoneUrl+"' />");
		$(".iosIphoneId .dd-up").html("<input type='text' value='' placeholder='"+param.iosIphoneId+"' />");
		$(".iosIphoneTestId .dd-up").html("<input type='text' value='' placeholder='"+param.iosIphoneTestId+"' />");
		$(".iosIpaUrl .dd-up").html("<input type='text' value='' placeholder='"+param.iosIpaUrl+"' />");
		$(".iosIpaMd5 .dd-up").html("<input type='text' value='' placeholder='"+param.iosIpaMd5+"' />");
		$(".iosIpaTestId .dd-up").html("<input type='text' value='' placeholder='"+param.iosIpaTestId+"' />");
		$(".androidAppUrl .dd-up").html("<input type='text' value='' placeholder='"+param.androidAppUrl+"' />");
		$(".androidAppMd5 .dd-up").html("<input type='text' value='' placeholder='"+param.androidAppMd5+"' />");
		$(".androidAppPackage .dd-up").html("<input type='text' value='' placeholder='"+param.androidAppPackage+"' />");
	}		
});
$("body").on("click",".cancel",function(){
	$(".account-footer").hide();
	$(".imgsCon i").hide();
	$(".addImg").hide();
	history.go(0);
});

$("body").on("click",".save",function(){
	var arr=[];
	if($(".appDesc textarea").val().length==0||($(".appDesc textarea").val().length>20&&$(".appDesc textarea").val().length<120)){
		param.appName=$(".appName input").val()||param.appName;
		param.appTypeId=$(".typeName option:selected").val();
		param.appIcon=$(".imgs4 .oldimg img").attr("src");
		param.appDesc=$(".appDesc textarea").val()||param.appDesc;
		param.iosIphoneUrl=$(".iosIphoneUrl input").val()||param.iosIphoneUrl;
		param.iosIphoneId=$(".iosIphoneId input").val()||param.iosIphoneId;
		param.iosIphoneTestId=$(".iosIphoneTestId input").val()||param.iosIphoneTestId;
		param.iosIpaUrl=$(".iosIpaUrl input").val()||param.iosIpaUrl;
		param.iosIpaMd5=$(".iosIpaMd5 input").val()||param.iosIpaMd5;
		param.iosIpaTestId=$(".iosIpaTestId input").val()||param.iosIpaTestId;
		param.androidAppUrl=$(".androidAppUrl input").val()||param.androidAppUrl;
		param.androidAppMd5=$(".androidAppMd5 input").val()||param.androidAppMd5;
		param.androidAppPackage=$(".androidAppPackage input").val()||param.androidAppPackage;
		for(var i=0;i<$(".appImg .bigimg img").length;i++){
			arr.push($(".appImg .bigimg img").eq(i).attr("src"))
		}
		param.appImg=arr.join(",");
		arr=[];
		for(var i=0;i<$(".appDescFile .bigimg img").length;i++){
			arr.push($(".appDescFile .bigimg img").eq(i).attr("src"))
		}
		param.appDescFile=arr.join(",");
		arr=[];
		for(var i=0;i<$(".appCopyright .bigimg img").length;i++){
			arr.push($(".appCopyright .bigimg img").eq(i).attr("src"))
		}
		param.appCopyright=arr.join(",");
		param.id=thisid;
		console.log(param);
		$.ajax({
			type:"post",
			url:" /online/payv2BussCompanyApp/updatePayv2BussCompanyApp.do",
			data:param,
			async:false,
			success:function(data){
				console.log(data);
				if(!!data&&data.resultCode==200){
					alert("修改成功")
					history.go(0);
				}else{
					if(!!data.message){
						alert(data.message);
					}else{
						alert("登录已失效，请重新登录");
						$("body").html(data);
					}
				}
			}
		})
	}else{
		alert("应用说明长度应在20-120个字符之间")
	}
	
	//console.log(param.typeName);
})
$("body").on("click",".bigimg i",function(){
	$(this).parent().remove();
})
//console.log(thisid)
getdata();
function getdata(){
	$.ajax({
		typr:"get",
		url:"../online/payv2BussCompanyApp/toEditPayv2BussCompanyApp.do",
		data:{"id":thisid},
		async:false,
		success:function(data){
			//console.log(data);
			if(!!data&&data.resultCode==200){
				data=data.Data;
				inidata(data);
			}else{
				if(!!data.message){
					alert(data.message);
				}else{
					alert("登录已失效，请重新登录");
					$("body").html(data);
				}
			}
		}
	})
}
/*数据展示*/
function inidata(data){
	console.log(data)
	var str1="",
		str2="",
		str3="",
		str4="",
		model1="",
		model2="",
		model3="";
	$(".appName dd").html(data.appName);
	//$(".id dd").html(data.id);
	if(data.appKey){
		$(".appKey dd").html(data.appKey);
	}else{
		$(".appKey dd").html("审核中");
	}
	
	$(".appIcon .imgs4 .oldimg img").attr("src",data.appIcon);
	$(".typeName dd").html(data.typeName);
	$(".appDesc dd").html(data.appDesc);
	$(".androidApkPackage span").html(data.androidApkPackage);
	$(".androidApkPackage span").css("margin-top","0")
	$(".iosIphoneUrl .dd-up").html(data.iosIphoneUrl);
	$(".iosIphoneId .dd-up").html(data.iosIphoneId);
	$(".iosIphoneTestId .dd-up").html(data.iosIphoneTestId);
	$(".iosIpaUrl .dd-up").html(data.iosIpaUrl);
	$(".iosIpaMd5 .dd-up").html(data.iosIpaMd5);
	$(".iosIpaTestId .dd-up").html(data.iosIpaTestId);
	$(".androidAppUrl .dd-up").html(data.androidAppUrl);
	$(".androidAppMd5 .dd-up").html(data.androidAppMd5);
	$(".androidAppPackage .dd-up").html(data.androidAppPackage);
	if(data.isAndroid==2){
		$(".android").hide();
		$(".ad-dl").hide();
	}
	if(data.isIos==2){
		$(".ioss").hide();
		$(".iph").hide();
		$(".iph-dl").hide();
		$(".ipd").hide();
		$(".ipd-dl").hide();
	}
	if(data.appImg){
		var str1=data.appImg;
		str1=str1.split(",");
		//console.log(str1)
		for(var i=0;i<str1.length;i++){
			model1+=$("#imgMode").html();
		}
		$(".appImg dd").prepend(model1);
		for(var i=0;i<str1.length;i++){
			$(".appImg dd div").eq(i).find("img").attr("src",str1[i]);
		}
	}else{
		$(".appImg dd").prepend("<div class='nodata' style='border:0;color:#808080'>未添加</div>")
	}
	if(data.appDescFile){
		var str2=data.appDescFile;
		str2=str2.split(",");
		//console.log(str2)
		for(var i=0;i<str2.length;i++){
			model2+=$("#imgMode").html();
		}
		$(".appDescFile dd").prepend(model2);
		for(var i=0;i<str2.length;i++){
			$(".appDescFile dd div").eq(i).find("img").attr("src",str2[i]);
		}
	}else{
		$(".appDescFile dd").prepend("<div class='nodata' style='border:0;color:#808080;'>未添加</div>")
	}
	if(data.appCopyright){
		var str3=data.appCopyright;
		str3=str3.split(",");
		//console.log(str3)
		for(var i=0;i<str3.length;i++){
			model3+=$("#imgMode").html();
		}
		//console.log(model3)
		$(".appCopyright dd").prepend(model3);
		for(var i=0;i<str3.length;i++){
			$(".appCopyright dd div").eq(i).find("img").attr("src",str3[i]);
		}
	}else{
		$(".appCopyright dd").prepend("<div class='nodata' style='border:0;color:#808080;'>未添加</div>")
	}
	
	for(var i=0;i<data.supportList.length;i++){
		str4+='<div class="fl ali"><i></i><span style="padding:0 10px;text-indent:1px;">支付宝</span></div>';
	}
	$(".supportList dd").html(str4);
	for(var i=0;i<data.supportList.length;i++){
		$(".supportList dd i").eq(i).css("background-image","url("+data.supportList[i].wayIcon+")");
		$(".supportList dd span").eq(i).html(data.supportList[i].wayName);
	}
}
/*上传图片*/
function upImgFiles(e){
	if(e.files[0].size/1024/1024<2&&(e.files[0].type.indexOf("png")!=-1||e.files[0].type.indexOf("jpeg")!=-1)){
		if($(e).hasClass("img1")){
			if($(".imgs1 .bigimg").length<5){
				uploadimg(e)
			}else{
				alert("最多只能上传5张图哟！")
			}
		};
		if($(e).hasClass("img2")){
			if($(".imgs2 .bigimg").length<5){
				uploadimg(e)
			}else{
				alert("最多只能上传5张图哟！")
			}
		};
		if($(e).hasClass("img3")){
			if($(".imgs3 .bigimg").length<5){
				uploadimg(e)
			}else{
				alert("最多只能上传5张图哟！")
			}
		};
		if($(e).hasClass("img4")){
			
			$.ajaxFileUpload({
				url : '/upload/addFile.do',
				secureuri : false,
				fileElement : e,
				dataType : "JSON",//返回数据的类型
				success : function(data, status) {
					//console.log(data);
					if (data.resultCode == 200) {
						//console.log(data.url)
						if($(e).hasClass("img4")){
							//console.log(data.Data.url)
							$(".imgs4 img").eq(0).attr("src",data.Data.url);
						}
					} else {
						alert("上传失败！");
						//alert(data.Data);
					}
				}
			});
		};
	}else{
		alert("图片格式或大小不正确")
	}
}
$("input.img1").h5upload({
	//上传的格式
	'fileTypeExts': 'jpg,jpge,png,gif',
	'fileObjName': 'file',
	//上传处理程序
	'url': '/upload/addFiles.do',
	//上传文件的大小限制
	'fileSizeLimit': 50000 * 1024 * 1024,

	//上传进度更新
	//上传到服务器，服务器返回相应信息到data里
	'onUploadSuccess': function(file, data) {
		var result = JSON.parse(data);
		if(!!result && result.resultCode == 200) {
			var url = result.Data.url;
			if($(".imgs1 .bigimg").length>=5){
				alert("最多只能上传五张图片")
			}else{
				$(".imgs1").prepend($("#imgMode").html());
				$(".imgs1 img").eq(0).attr("src", url);
				$(".imgs1 i").show();
			}
			
		} else {
			alert(result.message);
		}
	},
	//上传失败
	'onUploadError': function(file) {
		alert("上传失败!");
	}
});

$("input.img2").h5upload({
	//上传的格式
	'fileTypeExts': 'jpg,jpge,png,gif',
	'fileObjName': 'file',
	//上传处理程序
	'url': '/upload/addFiles.do',
	//上传文件的大小限制
	'fileSizeLimit': 50000 * 1024 * 1024,

	//上传进度更新
	//上传到服务器，服务器返回相应信息到data里
	'onUploadSuccess': function(file, data) {
		var result = JSON.parse(data);
		if(!!result && result.resultCode == 200) {
			var url = result.Data.url;
			if($(".imgs2 .bigimg").length>=5){
				alert("最多只能上传五张图片")
			}else{
				$(".imgs2").prepend($("#imgMode").html());
				$(".imgs2 img").eq(0).attr("src", url);
				$(".imgs2 i").show();
			}
			
		} else {
			alert(result.message);
		}
	},
	//上传失败
	'onUploadError': function(file) {
		alert("上传失败!");
	}
});

$("input.img3").h5upload({
	//上传的格式
	'fileTypeExts': 'jpg,jpge,png,gif',
	'fileObjName': 'file',
	//上传处理程序
	'url': '/upload/addFiles.do',
	//上传文件的大小限制
	'fileSizeLimit': 50000 * 1024 * 1024,

	//上传进度更新
	//上传到服务器，服务器返回相应信息到data里
	'onUploadSuccess': function(file, data) {
		var result = JSON.parse(data);
		if(!!result && result.resultCode == 200) {
			var url = result.Data.url;
			if($(".imgs3 .bigimg").length>=5){
				alert("最多只能上传五张图片")
			}else{
				$(".imgs3").prepend($("#imgMode").html());
				$(".imgs3 img").eq(0).attr("src", url);
				$(".imgs3 i").show();
			}
			
		} else {
			alert(result.message);
		}
	},
	//上传失败
	'onUploadError': function(file) {
		alert("上传失败!");
	}
});
$("input.img4").h5upload({
	//上传的格式
	'fileTypeExts': 'jpg,jpge,png,gif',
	'fileObjName': 'file',
	//上传处理程序
	'url': '/upload/addFiles.do',
	//上传文件的大小限制
	'fileSizeLimit': 50000 * 1024 * 1024,

	//上传进度更新
	//上传到服务器，服务器返回相应信息到data里
	'onUploadSuccess': function(file, data) {
		var result = JSON.parse(data);
		if(!!result && result.resultCode == 200) {
			var url = result.Data.url;
			$(".app-icon").show();
			$(".app-icon").attr("src",url);
			
		} else {
			alert(result.message);
		}
	},
	//上传失败
	'onUploadError': function(file) {
		alert("上传失败!");
	}
});

$("input.uploadfileInput").h5upload({
	//上传的格式
	'fileTypeExts': '',
	'fileObjName': 'file',
	//上传处理程序
	'url': '/upload/addFiles.do',
	//上传文件的大小限制
	'fileSizeLimit': 1000000 * 1024 * 1024,

	//上传进度更新
	//上传到服务器，服务器返回相应信息到data里
	'onUploadSuccess': function(file, data) {
		var result = JSON.parse(data);
		if(!!result && result.resultCode == 200) {
			console.log(result)
			
		} else {
			alert(result.message);
		}
	},
	//上传失败
	'onUploadError': function(file) {
		alert("上传失败!");
	}
});