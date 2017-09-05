$(function(){
	$(".exitlogin").on("click",function(){
		$.ajax({
			type:"post",
			url:"/sys/loginOut.do",
			async:false,
			success:function(data){
				if(!!data&&data.resultCode==200){
					localStorage.clear();
					location.href="/";
				}else{
					alert(data.message);
				}
			}
		})
		
	})
})
/*修改密码*/
$("body").on("click",".changepwd",function(){
	$(".bg").show();
	$(".window3").show();
	$(".window").hide();
	$(".bottom3").show();
	$("body,html").css({"height":"100%","width":"100%","overflow":"hidden"});
});
$("body").on("click",".top2 div",function(){
	$(".bg").hide();
	$(".window3").hide();
	$(".window").hide();
	$(".oldpwd span").hide();
	$(".newpwd span").hide();
	$(".newpwda span").hide();
	$(".oldpwd input").val("");
	$(".newpwd input").val("");
	$(".newpwda input").val("");
	$("body,html").css({"height":"","width":"","overflow":""});
})
$("body").on("mouseenter",".top2 div",function(){
	$(this).css("background-color","#fb6054")
	$(this).find("i").css("background0image","url(../img/closeh.png)")
})
$("body").on("mouseleave",".top2 div",function(){
	$(this).css("background-color","rgba(0,0,0,0)")
	$(this).find("i").css("background0image","url(../img/close.png)")
})
$("body").on("click",".window3 .surebtn",function(){
	var pwdparam={};
	pwdparam.nowPassword = $(".newpwd  input").val();
	pwdparam.nowPasswordCry = md5(md5($(".newpwd  input").val()));
	pwdparam.oldPassword =  md5(md5($(".oldpwd  input").val()));
	$(".oldpwd span").hide();
	$(".newpwd span").hide();
	$(".newpwda span").hide();
	if(!$(".oldpwd  input").val()){
		$(".oldpwd span").show();
	}else if(!$(".newpwd  input").val()){
		$(".newpwd span").show();
	}else if(!$(".newpwda  input").val()){
		$(".newpwda span").show();
	}else if($(".newpwd  input").val().length<6||$(".newpwd  input").val().length>16){
		$(".newpwd span").html("请输入6-16位密码");
		$(".newpwd span").show();
	}else if(!$(".newpwd  input").val().match(/^[A-Za-z0-9]{6,16}$/)){
		alert("密码格式不正确")
	}else if($(".newpwd  input").val()!=$(".newpwda  input").val()){
		$(".newpwda span").html("两次密码输入不一致");
		$(".newpwda span").show();
	}else if($(".oldpwd  input").val()==$(".newpwd  input").val()){
		$(".newpwda span").html("新密码与老密码不能一致!");
		$(".newpwda span").show();
		return false;
	}else{
		//console.log("111")
		$.ajax({
			type:"get",
			url:"/offline/bussCompanyAccount/editBussCompanyPassword.do",
			data:pwdparam,
			async:false,
			success:function(data){
				//console.log(data);
				if(!!data&&data.resultCode==200){
					alert("修改成功")
					$(".oldpwd input").val("");
					$(".newpwd input").val("");
					$(".newpwda input").val("");
					$(".bg").hide();
					$(".window3").hide();
					$(".window").hide();
				}else{
					alert(data.message)
				}
			}
		})
	}
});