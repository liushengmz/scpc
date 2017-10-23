$(function(){
	// $.ajax({
	// 	type:"post",
	//     url: 'http://10.0.4.71:8080/jinfu_website/ProProductFinancial/getProProductFinancialById.do?proId=1',
	//     beforeSend:"http://www.baidu.com",
	//     dataType: "json",
	//     success: function (data) {
	//     	console.log(data);
	//     }
	// });
	
	/*添加基金--基金类型的选择*/
	$("body").on("change",".fundTypeSelect",function(){
		value = $(this).val();
		if( value == "1" ){    //1:混合型、债券型、股票型、指数型、QDII、其他 ; 2: 货币型、理财型
			$(".fundType1").show();
			$(".fundType2").hide();
		}else if( value == "2" ){
			$(".fundType1").hide();
			$(".fundType2").show();
		}
	});
	
	//选择网页和apk对应输入列表
	$(".changeSelect").change(function(event) {
		var value = $(this).val();
		if(value == "1"){
			if ($(this).parent("dd").find('.changeCont').length != 0) {
				$(this).parent("dd").find('.changeCont').eq(0).show().siblings('.changeCont').hide();
			}
		}else if (value == "2"){
			if ($(this).parent("dd").find('.changeCont').eq(1).length != 0) {
				$(this).parent("dd").find('.changeCont').eq(1).show().siblings('.changeCont').hide();
			}else{
				$(this).parent("dd").find('.changeCont').hide();
			}
		}
	});

	$(".changeSelect2").change(function(event) {
		if($(this).val() == 1){
			$("dl.cpaDl,dl.cpsDl").show();
		}else if ($(this).val() == 2 ){
			$("dl.cpaDl").hide();
			$("dl.cpsDl").show();
		}else if ($(this).val() == 3 ){
			$("dl.cpaDl,dl.cpsDl").hide();
		}
	});
	$(".changeSelect3").change(function(event) {
		if($(this).val() == "3"){
			$("dl.dlCompany,h3.h3Company").show();
		}else if ($(this).val() == "4" ){
			$("dl.dlCompany,h3.h3Company").hide();
		}
	});

	// 选中当前多选框，对应显示下方的输入列表
	$(".productTypeList>input").click(function(event) {
		if ($(this)[0].checked == true ) {
			if ($(this).parent(".productTypeList").next(".producTypeCon").length != 0) {
			 	$(this).parent(".productTypeList").next(".producTypeCon").show();
			}
		}else{
			if ($(this).parent(".productTypeList").next(".producTypeCon").length != 0) {
			 	$(this).parent(".productTypeList").next(".producTypeCon").hide();
			}
		}
	});

	//自定义ui标签
	$(".addColorLable").click(function(event) {
		var allparent = $(this).parent("dd").parent("dl").parent(".rowLabel"),
			colorLable = allparent.find(".colorLable").val(),
			colorCon = allparent.find(".colorCon").val();
		var str = allparent.attr("tags");
		if (colorLable != "" && colorCon != "") {
			var colorName = allparent.find(".colorRadio:checked").next("label").attr("class");
			var colorVal = allparent.find(".colorRadio:checked").val();
			allparent.find(".customLabel").append('<p><input type="hidden" name="'+str+'CustomColor" value="'+colorVal+'"/><input type="hidden" name="'+str+'CustomName" value="'+colorLable+'"/><input type="hidden" name="'+str+'CustomContent" value="'+colorCon+'"/><i class="'+colorName+'">'+colorLable+'</i><span>'+colorCon+'</span><a href="javascript:;" class="deleteLabel">删除</a></p>')
			$(".colorLable").val("");
			$(".colorCon").val("");
		}else{
			alert("您输入的参数有误，请检查后重新输入");
		}

	});

	// p标签中的删除按钮
	$("body").on('click', '.deleteLabel', function(event) {
		event.preventDefault();
		$(this).parent("p").remove();
	});

	//添加产品注意事项
	$(".addRemindIndex").click(function(event) {
		var openIndex = $(this).prev("input").val();
		if (openIndex != "") {
			$(this).parent("dd").parent("dl").next("dl").find('.remindList').append('<p><i>'+openIndex+'</i><input type="hidden" name="tips" value="'+openIndex+'"/><a href="javascript:;" class="deleteLabel">删除</a></p>')
			$(this).prev("input").val("");
		}else{
			alert("您还没有输入内容");
		}
	});

	//label标签点击选中之前的input
	$("body").on('click', 'label', function(event) {
//		event.preventDefault();
		if ($(this).prev("input").length!= 0) {
			$(this).prev("input").click();
		}
	});

	//添加开户指标
	$(".bankId").click(function(event) {
		if ($(this)[0].checked == true) {
			$(".bankIconUpdata").show();
		}else{
			$(".bankIconUpdata").hide();
		}
	});
	
	//选择添加的开户指标
	$("body").on("change",".auditItemsList>div>input",function(){
		var englistName = $(this).attr("name");   //英文名
		var list = $(this).parents("dl").next().find(".auditItems");
		if( $(this).is(':checked') ){   //选中
			var chinaName = $(this).next().text();
			var htmlStr = "<p><i>"+chinaName+"</i><input type='hidden' name='"+ englistName +"' value='"+ englistName +"' /><a href='javascript:;' class='auditDeleteBtn'>删除</a></p>";
			$(list).append(htmlStr);
		}else{   //取消选中
			$(list).find("input[name='"+ englistName +"']").next().click(); //触发删除按钮点击事件
 		}
	});
	
	// p标签中的删除按钮
	$("body").on('click', '.auditDeleteBtn', function(event) {
		event.preventDefault();
		var prev = $(this).parents("dl").prev().find(".auditItemsList");
		var name = $(this).prev().attr("name");
		$(prev).find("input[name='"+ name +"']").attr("checked",false);
		$(this).parent("p").remove();
	});
	
	
	//删除上传的截图
	$("body").on("click",".upLoadShots .imageOper",function(){
		$(this).parent(".shotList").remove();
	});
	
	// 省市二级联动
    $("#chooseCity").citySelect({
		nodata:"none",
		required:false
	});

});

/***
 * 功能： 初始化交易审核项、开户审核项的必填项
 * @param parentsElement: 被选择的审核项的父元素
 * @param required: 必填项的中英文名的对象数组 格式：[{chinaName:xx,englistName:xx}..........]   
 * */
function initRequiredAudit(parentsElement,required){
	required = !!required ? required : [];
	
	var list = parentsElement.parents("dl").next().find(".auditItems");
	$.each(required,function(index,item){
		var input = parentsElement.find("input[name='"+ item.englistName +"']");
		var htmlStr = "<p><i>"+item.chinaName+"</i><input type='hidden' name='"+ item.englistName +"' value='"+ item.englistName +"' /></p>";
		
		$(input).attr("disabled","disabled").attr("checked",true);
		$(list).append(htmlStr);
	});
}

//delog插件
function　dialog_span(dialog_spanobj){
	dialog_spanobj.click(function(event) {
		$("body").append('<div class="blocker"><div class="dialog"><div class="msg"><p>'+$(this).attr("data-remind")+'</p></div><div class="form-group toolbar"><button class="btn btn-primary cancel" type="submit">OK</button></div></div></div>');
		$(".blocker").show();
	});
	$("body").on('click',".blocker .cancel",function(event) {
		$(".blocker").remove();
	});
}