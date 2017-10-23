(function($){
	var optionsFun = {};
	var ms = {
		init:function(obj,args){
			return (function(){
				ms.fillHtml(obj,args);
			})();
		},
		//填充html
		fillHtml:function(obj,args){
			return (function(){
				obj.empty();
				obj.append( "<span class='page-lb'>跳转到：</span><input class='page-btn to-page' type='text' value='"+ args.current +"'>" );
				//上一页
				if(args.current > 1){
					obj.append('<a href="javascript:;" class="prevPage page-btn page-btn-gray">上一页</a>');
				}else{
					obj.remove('.prevPage');
					obj.append('<a href="javascript:;" class="page-btn page-btn-gray">上一页</a>');
				}
				//中间页码
				if(args.current != 1 && args.current >= 4 && args.pageCount != 4){
					obj.append('<a href="javascript:;" class="tcdNumber page-btn">'+1+'</a>');
				}
				if(args.current-2 > 2 && args.current <= args.pageCount && args.pageCount > 5){
					obj.append('<span class="preSpan page-btn">...</span>');
				}
				var start = args.current -2,end = args.current+2;
				if((start > 1 && args.current < 4)||args.current == 1){
					end++;
				}
				if(args.current > args.pageCount-4 && args.current >= args.pageCount){
					start--;
				}
				for (;start <= end; start++) {
					if(start <= args.pageCount && start >= 1){
						if(start != args.current){
							obj.append('<a href="javascript:;" class="tcdNumber page-btn">'+ start +'</a>');
						}else{
							obj.append('<a href="javascript:;" class="tcdNumber page-btn current-index">'+ start +'</a>');
						}
					}
				}
				if(args.current + 2 < args.pageCount - 1 && args.current >= 1 && args.pageCount > 5){
					obj.append('<span class="nextSpan page-btn">...</span>');
				}
				if(args.current != args.pageCount && args.current < args.pageCount -2  && args.pageCount != 4){
					obj.append('<a href="javascript:;" class="tcdNumber page-btn">'+args.pageCount+'</a>');
				}
				//下一页
				if(args.current < args.pageCount){
					obj.append('<a href="javascript:;" class="nextPage page-btn">下一页</a>');
				}else{
					obj.remove('.nextPage');
					obj.append('<a href="javascript:;" class="page-btn">下一页</a>');
				}
				ms.bindEvent(obj,args);
			})();
		},
		//绑定事件
		bindEvent:function(obj,args){
			return (function(){
				obj.find("a.tcdNumber").unbind("click").click(function(){
					var current = parseInt($(this).text());
					ms.fillHtml(obj,{"current":current,"pageCount":args.pageCount,"backFn": optionsFun.backFn});
					if(typeof(args.backFn)=="function"){
						args.backFn(current);
					}
				});
//				obj.on("click","a.tcdNumber",function(){
//					var current = parseInt($(this).text());
//					ms.fillHtml(obj,{"current":current,"pageCount":args.pageCount});
//					if(typeof(args.backFn)=="function"){
//						args.backFn(current);
//					}
//				});
				//指定跳转到
				obj.find("input.to-page").unbind("change").on("change",function(){
					var current = parseInt(obj.children("input.to-page").val());
					if( !!current ){
						if( current > args.pageCount ){
							obj.children("input.to-page").val(args.current);
							alert("超出最大页数了");
							return false;
						}
						ms.fillHtml(obj,{"current":current,"pageCount":args.pageCount,"backFn": optionsFun.backFn});
						if(typeof(args.backFn)=="function"){
							args.backFn(current);
						}
					}
				});
//				obj.on("change","input.to-page",function(){
//					var current = parseInt(obj.children("input.to-page").val());
//					if( !!current ){
//						if( current > args.pageCount ){
//							obj.children("input.to-page").val(args.current);
//							alert("超出最大页数了");
//							return false;
//						}
//						ms.fillHtml(obj,{"current":current,"pageCount":args.pageCount,"backFn": optionsFun.backFn});
//						if(typeof(args.backFn)=="function"){
//							args.backFn(current);
//						}
//					}
//				});
				
				//上一页
				obj.find("a.prevPage").unbind("click").click(function(){
					var current = parseInt(obj.children("a.current-index").text());
					ms.fillHtml(obj,{"current":current-1,"pageCount":args.pageCount,"backFn": optionsFun.backFn});
					if(typeof(args.backFn)=="function"){
						args.backFn(current-1);
					}
				});
//				obj.on("click","a.prevPage",function(){
//					var current = parseInt(obj.children("a.current-index").text());
//					ms.fillHtml(obj,{"current":current-1,"pageCount":args.pageCount});
//					if(typeof(args.backFn)=="function"){
//						args.backFn(current-1);
//					}
//				});
				//下一页
				obj.find("a.nextPage").unbind("click").click(function(){
					var current = parseInt(obj.children("a.current-index").text());
					ms.fillHtml(obj,{"current":current+1,"pageCount":args.pageCount,"backFn": optionsFun.backFn});
					if(typeof(args.backFn)=="function"){
						args.backFn(current+1);
					}
				});
				
//				obj.on("click","a.nextPage",function(){
//					var current = parseInt(obj.children("a.current-index").text());
//					ms.fillHtml(obj,{"current":current+1,"pageCount":args.pageCount});
//					if(typeof(args.backFn)=="function"){
//						args.backFn(current+1);
//					}
//				});
				
			})();
		}
	}
	$.fn.createPage = function(options){
		optionsFun = options;
		var args = $.extend({
			pageCount : 10,
			current : 1,
			backFn : function(){}
		},options);
		ms.init(this,args);
	}
})(jQuery);