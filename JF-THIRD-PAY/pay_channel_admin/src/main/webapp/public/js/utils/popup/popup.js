//(function(){
//	$.getHtmlContent = function(url,callBackFunc){
//		$.ajax({
//			url : url,
//			type : "POST",
//			dataType : "HTML",
//			success: callBackFunc,
//			error:function(){
//			
//			}
//		});
//	}
//})($);
//
///**
// * 功能: 显示加载效果
// * @param title:提示标题
// **/
//function popupShowLoading(title){
//	title = title || "请求加载中，请等待...";
//	var loadContent = function(){
//		if(title){
//			$("#popup_loading_title").html(title);
//		}
//		$("#popup_loading_bg,#popup_loading_content").show();
//	};
//	if($("#ng_popup_loading").length < 1){
//		var callBackFunc = function(data){
//			$("body").append(data);
//			loadContent();
//		};
//		$.getHtmlContent("/public/js/utils/popup/page/loading.html",callBackFunc);
//	}
//	else{
//		loadContent();
//	}
//}
//
///**
// * 关闭加载效果
// * */
//function popupCloseLoading(){
//	if( $("#ng_popup_loading").length > 0 ){
//		$("#popup_loading_bg,#popup_loading_content").hide();
//	}
//}