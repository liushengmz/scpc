$(".cl").click(function(){
	$(this).css({
		"border":"1px solid #007aff",
	});
	$(this).find(".bgtit").css({"color":"#007aff"});
	$(this).find(".bgb").css({"color":"#007aff"});
	$(this).children().find("img").attr({"src":"img/g.png"});
	$(this).siblings().css({
		"border":"1px solid #dedede",
	});
	$(this).siblings().children().find(".bgtit").css({"color":"#333"});
	$(this).siblings().find(".bgb").css({"color":"#808080"});
	$(this).siblings().children().find("img").attr({"src":"img/gn.png"});
});

 window.onload=function(){
 		var a=$(".lhb4 .bgtit").width()+18;
 		var b=$(".lhb4 .bgtwt").width();
 		var c=(b-a)/2;
 		$(".lhb4 .bgtit").css({"marginLeft":c});
 		
 		var a=$(".lhb3 .bgtit").width()+18;
 		var b=$(".lhb3 .bgtwt").width();
 		var c=(b-a)/2;
 		$(".lhb3 .bgtit").css({"marginLeft":c});
 		
 		var a=$(".lhb2 .bgtit").width()+18;
 		var b=$(".lhb2 .bgtwt").width();
 		var c=(b-a)/2;
 		$(".lhb2 .bgtit").css({"marginLeft":c});
 		
 		var a=$(".lhb1 .bgtit").width()+18;
 		var b=$(".lhb1 .bgtwt").width();
 		var c=(b-a)/2;
 		$(".lhb1 .bgtit").css({"marginLeft":c});

 }



