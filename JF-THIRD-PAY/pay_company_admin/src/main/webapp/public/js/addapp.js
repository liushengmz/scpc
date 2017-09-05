/*header 事件start*/	
	$(".app-icon").hide();
	$(".introwords").css("display","block");
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
				

var param = {},
	iossi = 0,
	ios1i = 0,
	ios2i = 0,
	and1 = 0,
	str = "";
var regUrl = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/gi;
$(".ioss").hide();
$(".iph-dl").hide();
$(".iph").hide();
$(".ipd-dl").hide();
$(".ipd").hide();

/*获取行业*/
$.ajax({
	type: "get",
	url: "/online/payv2BussCompanyApp/getAppTypeList.do",
	async: false,
	success: function(data) {
		//console.log(data);
		if(!!data && data.resultCode == 200) {
			str = "<select>";
			//console.log(data.Data.appTypeList);
			for(var i = 0; i < data.Data.appTypeList.length; i++) {
				str += "<option value='" + data.Data.appTypeList[i].id + "'>" + data.Data.appTypeList[i].typeName + "</option>";
			}
			str += "</select>";
			$(".appKey dd").html(str);
		} else {
			if(!!data.message){
				alert(data.message);
			}else{
				alert("登录已失效，请重新登录");
				$("body").html(data);
			}
		}
	}
});

$("body").on("click", ".bigimg i", function() {
	$(this).parent().remove();
})
$("body").on("mouseenter", ".bigimg i", function() {
	$(this).css("background-image","url(../public/img/close3.png)");
})
$("body").on("mouseleave", ".bigimg i", function() {
	$(this).css("background-image","url(../public/img/close2.png)");
})
$(".timeLi li").on("click", function() {
	str = "";
	if($(this).hasClass("activess")) {
		$(this).removeClass("activess");
		$(this).attr({
			"state": "2"
		});
	} else {
		$(this).addClass("activess");
		$(this).attr({
			"state": "1"
		});
	}
	
	if($(".and").hasClass("activess")) {
		$(".platform").show();
		$(".android").show();
		$(".ad-dl").show();
	} else {
		$(".android").hide();
		$(".ad-dl").hide();
	}
	
	if($(".isios").hasClass("activess")) {
		$(".platform").show();
		$(".ioss").show();
		$(".iph").show();
		$(".ipd").show();
	} else {
		$(".ioss").hide();
		$(".iph").hide();
		$(".ipd").hide();
	}
	if($(".and").hasClass("activess")||$(".isios").hasClass("activess")){
		
	}else{
		$(".platform").hide();
	}
});
$("body").on("click", ".ioss", function() {
	iossi++;
	if(iossi % 2 != 0) {
		$(this).find("i").css("background", "url(../public/img/nc.png)");
		$(".iph,.iph-dl,.ipd,.ipd-dl").hide();

	} else {
		$(this).find("i").css("background", "url(../public/img/cs.png)");
		$(".iph,.iph-dl,.ipd,.ipd-dl").show();
	}
});
$("body").on("click", ".iph", function() {
	ios1i++;
	if(ios1i % 2 != 0) {
		$(this).attr("iphoneState","2")
		$(this).find("i").css("background", "url(../public/img/cs.png)");
		$(".iph-dl").show();
	} else {
		$(this).attr("iphoneState","1")
		$(this).find("i").css("background", "url(../public/img/nc.png)");
		$(".iph-dl").hide();
		
	}
});
$("body").on("click", ".ipd", function() {
	ios2i++;
	if(ios2i % 2 != 0) {
		$(this).attr("ipadState","2")
		$(this).find("i").css("background", "url(../public/img/cs.png)");
		$(".ipd-dl").show();

	} else {
		$(this).attr("ipadState","1")
		$(this).find("i").css("background", "url(../public/img/nc.png)");
		$(".ipd-dl").hide();
		
	}
});
$(".add-shop textarea").on("change",function(){
	if($(this).val().length>120){
		$(this).val().substring(0,120);
	}
})
$(".account-footer").on("click", function() {
	var arr = [];
	if($(".androidApkPackage span").html()){
		param.androidApkPackage = $(".androidApkPackage span").html()
	}
	param.appName = $(".appName input").val();
	param.appDesc = $(".appDesc textarea").val();
	param.iosIphoneId = $(".iosIphoneId input").val();
	param.iosIphoneTestId = $(".iosIphoneTestId input").val();
	param.iosIpaMd5 = $(".iosIpaMd5 input").val();
	param.iosIpaTestId = $(".iosIpaTestId input").val();
	param.androidAppMd5 = $(".androidAppMd5 input").val();
	param.androidAppPackage = $(".androidAppPackage input").val();
	param.appTypeId = $(".appKey option:selected").val();
	
	if($(".webUrl input").val().match(regUrl)) {
		param.webUrl = $(".webUrl input").val();
	} else {
		param.webUrl = "";
		$(".detail-app .webUrl input").attr("nullMsg","网址请加上http://或者https://前缀")
		$(".detail-app .webUrl input").val("");
	};

	if($(".iosIphoneUrl input").val().match(regUrl)) {
		param.iosIphoneUrl = $(".iosIphoneUrl input").val();
	} else {
		param.iosIphoneUrl = "";
		$(".iosIphoneUrl input").val("");
	};

	if($(".iosIpaUrl input").val().match(regUrl)) {
		param.iosIpaUrl = $(".iosIpaUrl input").val();
	} else {
		param.iosIpaUrl = "";
		$(".iosIpaUrl input").val("");
	};

	if($(".androidAppUrl input").val().match(regUrl)) {
		param.androidAppUrl = $(".androidAppUrl input").val();
	} else {
		param.androidAppUrl = "";
		$(".androidAppUrl input").val("");
	};
	for(var i = 0; i < $(".appImg .bigimg img").length; i++) {
		arr.push($(".appImg .bigimg img").eq(i).attr("src"))
	}
	param.appImg = arr.join(",");
	arr = [];
	for(var i = 0; i < $(".appDescFile .bigimg img").length; i++) {
		arr.push($(".appDescFile .bigimg img").eq(i).attr("src"))
	}
	param.appDescFile = arr.join(",");
	arr = [];
	for(var i = 0; i < $(".appCopyright .bigimg img").length; i++) {
		arr.push($(".appCopyright .bigimg img").eq(i).attr("src"))
	}
	param.androidApkUrl = $(".androidApkPackage span").attr("urls");
	param.appCopyright = arr.join(",");
	param.isAndroid = $(".and").attr("state");
	param.isIos = $(".isios").attr("state");
	param.isWeb = $(".web").attr("state");
	console.log(param);
	if(param.isAndroid==2&&param.isIos==2&&param.isWeb==2){
		alert("至少选择一个应用类型")
	}else if(param.isIos==1){
		if($(".iph").attr("iphoneState")==1&&$(".ipd").attr("ipadState")==1){
			alert("至少选择一个设备类型")
		}else{
			if($(".imgs4 .oldimg img").attr("src")) {
				param.appIcon = $(".imgs4 .oldimg img").attr("src");
				var formtest = $(".formtest").Validform({ //ios推广位增加
					tiptype: 3,
					showAllError: true, //表单提交时显示所有提示信息
					ignoreHidden: true, //隐藏的表单元素不校验
					ajaxPost: true,
					callback: function(result) {
						if(!!result && result.resultCode == 200) {
							alert("添加成功");
							location.href="appManager.html"
						} else {
							if(!!result.message){
								alert(result.message);
							}else{
								alert("登录已失效，请重新登录");
								$("body").html(result);
							}
						}
					}
				});
				formtest.config({
					url: "/online/payv2BussCompanyApp/addPayv2BussCompanyApp.do",
					ajaxpost: {
						data: param,
						type: "POST",
						async: true
					}
				});
				formtest.submitForm(false); //提交表单
				$("#Validform_msg").hide();
			} else {
				alert("请上传应用图标");
			}
		}
	}else{
		if($(".imgs4 .oldimg img").attr("src")) {
			param.appIcon = $(".imgs4 .oldimg img").attr("src");
			var formtest = $(".formtest").Validform({ //ios推广位增加
				tiptype: 3,
				showAllError: true, //表单提交时显示所有提示信息
				ignoreHidden: true, //隐藏的表单元素不校验
				ajaxPost: true,
				callback: function(result) {
					if(!!result && result.resultCode == 200) {
						alert("添加成功");
						location.href="appManager.html"
					} else {
						if(!!result.message){
							alert(result.message);
						}else{
							alert("登录已失效，请重新登录");
							$("body").html(result);
						}
					}
				}
			});
			formtest.config({
				url: "/online/payv2BussCompanyApp/addPayv2BussCompanyApp.do",
				ajaxpost: {
					data: param,
					type: "POST",
					async: true
				}
			});
			formtest.submitForm(false); //提交表单
			$("#Validform_msg").hide();
		} else {
			alert("请上传应用图标");
		}
	}
	
})
$("#Validform_msg").hide();
/*上传图片*/
function upFiles(e){
	$.ajaxFileUpload({
		url: '/upload/addFile.do',
		secureuri: false,
		fileElement: e,
		dataType: "JSON", //返回数据的类型
		success: function(data, status) {
			if(data.resultCode == 200) {
				$(".androidApkPackage span").attr("urls",data.Data.url);
				$(".androidApkPackage span").html(e.files[0].name);
			} else {
				alert("上传失败！");
				//alert(data.Data);
			}
		}
	});
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
	'fileTypeExts': 'rar,zip',
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
			$(".androidApkPackage span").html(result.Data.fileName);
		} else {
			alert(result.message);
		}
	},
	//上传失败
	'onUploadError': function(file) {
		alert("上传失败!");
	}
});