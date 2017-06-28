var matchSubmit = (function(){
    function matchSubmit(){
        var ok = true;
        $(".add_text").each(function(){
            if($(this).val() === ''){
                var title = $(this).attr("title");
                $(this).css("border","1px solid red");
                $(this).parents("dd").find("span").eq(0).html(title+"不能为空");
                ok = false;

            }else{
                $(this).css("border","1px solid green");
            }

        });
        return ok;
    }

    function matchKeyup(){
        $(".add_text").keyup(function(){
            if($(this).val() !== ''){
                $(this).css("border","1px solid green");
            }
        });
    }

    function matchAll(){
        var btn = $("#btn_match");
        btn.click(function(){

            if(matchSubmit()){

            }else{
                return false;
            }
        });


    }



    return{
        matchSubmit:matchSubmit,
        matchKeyup:matchKeyup,
        matchAll:matchAll
    }
})();
$(document).ready(function(){
    matchSubmit.matchAll();
    matchSubmit.matchKeyup();
});