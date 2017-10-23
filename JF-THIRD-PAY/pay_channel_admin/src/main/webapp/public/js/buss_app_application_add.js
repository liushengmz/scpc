//搴旂敤鍒楄〃搴旂敤杩愯惀鏍囩璁剧疆
$(".tags input").on("change", function() {
	alert("change")
	if($('.zdybq').is(":checked")) {
		$(".owntags").show();
	} else {
		$(".owntags").hide();
	}
});
//搴旂敤鍒楄〃搴旂敤杩愯惀鏍囩璁剧疆
$(".ppTransitionTypes input").on("change", function() {
	if($(".types2").is(":checked")) {
		$(".zdyjump").show();
	} else {
		$(".zdyjump").hide();
	}
});
//璺宠浆杩囨浮椤佃缃�
$(".progress_style input").on("change", function() {
	if($(".progress_lines").is(":checked")) {
		$(".progress_line").show();
		$(".progress_img").hide();
	}
	if($(".progress_imgs").is(":checked")) {
		$(".progress_line").hide();
		$(".progress_img").show();
	}
});
//鏀粯閫氶亾鍙婇�閬撹垂璁剧疆
$(".moneyset select").on("change", function() {
	if($(this).val() == 1) {
		$(".dandu").show();
		$(".dabao").hide();

	} else if($(this).val() == 2) {
		$(".dandu").hide();
		$(".dabao").show();
	}
})
$(".dandu input[type=checkbox]").on("change", function() {
	if($(".ali").is(":checked")) {
		$(".alipay").show();
	}else if(!$(".ali").is(":checked")) {
		$(".alipay").hide();
	}
	if($(".we").is(":checked")) {
		$(".wechat").show();
	}else if(!$(".we").is(":checked")) {
		$(".wechat").hide();
	}
})
//浣ｉ噾鍙婃秷璐逛紭鎯犺缃�
$(".onsale input").on("change",function(){
	if($(".db").is(":checked")){
		$(".mlj").show();
		$(".dz").hide();
	}else if($(".dzz").is(":checked")){
		$(".mlj").hide();
		$(".dz").show();
	}else{
		$(".mlj").hide();
		$(".dz").hide();
	}
})
//閫夋嫨鏁伴噺澧炲姞鏂规鍜屽噺灏戞柟妗�
$(".reduce div,.useradd div").on("click",function(){
	if(!$(this).hasClass("non")){
		$(this).addClass("activechoise").siblings().removeClass("activechoise");
	}
	
})
