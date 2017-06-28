// JavaScript Document
$(document).ready(function(e) {
    //login
    var focustextbox;
    $(".login_form li .user_txt_bg :input").focusin(function() {
        $(this).parent().addClass("hover");
        focustextbox = this;
    }).focusout(function() {
        $(this).parent().removeClass("hover");
    }).hover(function() {
        $(this).parent().addClass("hover");
    }, function() {
        if (this != focustextbox) {
            $(this).parent().removeClass("hover")
        }
    })

    $(".login_form li .login_btn .login_submit").hover(function() {
        $(this).removeClass("login_submit");
        $(this).addClass("login_submit_hover");
    }, function() {
        $(this).removeClass("login_submit_hover");
        $(this).addClass("login_submit");
    })
    $(".login_form li .login_btn .login_reset").hover(function() {
        $(this).removeClass("login_reset");
        $(this).addClass("login_reset_hover");
    }, function() {
        $(this).removeClass("login_reset_hover");
        $(this).addClass("login_reset");
    })

    if (!window.XMLHttpRequest) {
        $(window).resize(function() {
            ieresize();
        })
        ieresize();
        if ($(".main").height() < 600) {
            $(".main").height(600);
        }
    }
    function ieresize() {
        if ($(window).width() < 1000) {
            $(".header, .company").css("width", 1000);
            $(".wrapper").css("width", 970);
        }
        else {
            $(".header, .company, .wrapper").css("width", "auto");
        }
    }

    //企业名网址提示
    $(".company_lk h2").hover(function() {
        $(this).next(".website").show();
    }, function() {
        $(this).next(".website").hide();
    })

    //语言切换
    $(".language .toggle_lg").click(function() {
        $(this).next("ul").toggle();
        $(this).find("i").toggleClass("click");
        return false;
    })
    $(".language ul li a").click(function() {
        $(this).parents("ul").prev(".toggle_lg").find("span").html($(this).html());
        $(this).parents("ul").hide();
        $(this).parents("ul").prev(".toggle_lg").find("i").removeClass("click");
    })
    $(document).click(function() {
        $(".language .toggle_lg").next("ul").hide();
        $(".language .toggle_lg").find("i").removeClass("click");
    })

    //左侧收缩
    $(".toggleleft").toggle(function() {
        $(this).animate({
            left: 0
        }, 300);
        $(".leftmenu").animate({
            //width:0
            left: (-1 * $(this).width())
        }, 300);
        $(".rightmain").animate({
            marginLeft: 0,
            paddingLeft: 0
        }, 300);
        $(".white_line").hide();
        $(this).removeClass("toggleleft_hide");
        $(this).addClass("toggleleft_show");

    }, function() {
        $(this).animate({
            left: 174
        }, 300);
        $(".leftmenu").animate({
            // width:175
            left: 0
        }, 300);
        $(".rightmain").animate({
            marginLeft: 170,
            paddingLeft: 5
        }, 300, function() {
            $(".white_line").show();
        });
        $(this).removeClass("toggleleft_show");
        $(this).addClass("toggleleft_hide");
    })

    //leftmenu菜单提示框
	function tip_span(){
		$(".tip_span").each(function(){
			var s_width = $(this).width();
			if(s_width>116){
				$(this).css("width",116);
				$(this).parent().hover(function(){
					var tip_text = $(this).text();
					$(".leftmenu").append("<div class='tip_text'><b class='triangle'></b><span>" + tip_text + "</span></div>");
					var _top = $(this).position().top + 32;
					$(".tip_text").css({
						top:_top,
						display: "block",
						opacity: 0
					});
					
					$(".tip_text:not(:animated)").animate({
						opacity: 1,
						top: _top
					},300);
				},function(){
					$(".tip_text").fadeOut("10",function(){$(this).remove();});
				});
			}
		});
	}
	tip_span();
	
	
	//全局高度
	function auto_height(){
		if(+$(".leftmenu").height()>500){
			$('.rightmain .main').animate({
				'min-height': $(".leftmenu").height()
			}, 300)
			//IE6
			if($.browser.msie&&($.browser.version == "6.0")&&!$.support.style){
				if($(".leftmenu").height() > $('.rightmain .main').height()){
					var move_height = $(".leftmenu").height();
					$('.rightmain .main').animate({
						'height': move_height
					}, 300)
				}
			}
		}
	}
	auto_height();
	
    //leftmenu
	var global_height=0;
        $(".leftmenu .menu li .menu_title").live("click",function() {
            $(this).toggleClass("menu_title_click");
            if (!$(this).next("ul").is(":animated")) {
                $(this).next("ul").slideToggle(300, function() {
                    var parent = $(this).parents('.leftmenu')
                    if (global_height == 0)
                        global_height = $('.rightmain .main').height();
                    if (parent.height() > global_height)
                        var move_height = parent.height()
                    else
                        var move_height = global_height
                    $('.rightmain .main').animate({
                        'height': move_height
                    }, 300)
                });
            }
    
    	tip_span();
            $(".white_line").hide();
        });
    function loadWhiteLine() {
        $(".white_line").show();
        var menu_offset = $(".leftmenu .menu ul li a.click").offset();
        if (menu_offset == null) {
            return false;
        }
        $(".white_line").css({
            top: menu_offset.top - 148,
            left: 170
        });
        if (!window.XMLHttpRequest) {
            $(".white_line").css({
                top: menu_offset.top - 147,
                left: 170
            });
        }
    }
    loadWhiteLine();
    $(".leftmenu .menu ul").each(function() {
        $(this).find("li:last").addClass("last");
    })

    //list_table
    $(".list_table").each(function() {
        $(this).find(".select_all :checkbox").click(function() {
            if ($(this).attr("checked") == "checked") {
                $(this).parents("tr.list_title").siblings("tr").find(".select_sg :checkbox").attr("checked", true);
            }
            else {
                $(this).parents("tr.list_title").siblings("tr").find(".select_sg :checkbox").attr("checked", false);
            }
        })
        $(this).find("tr:eq(1) td").css("border-top-color", "#A6C8E0");
    })
    $(".act_prize_table tr:gt(0)").hover(function() {
        $(this).css("background-color", "#EAF4C9");
    }, function() {
        $(this).css("background-color", "inherit");
    })
     //user search
    $(".search li .title_txt").focusin(function() {
        if ($(this).val() == "请输入关键字") {
            $(this).val("");
        }
    })
    $(".search li .title_txt").focusout(function() {
        if ($(this).val() == "") {
            $(this).val("请输入关键字");
        }
    })
    $(".search li .search_btn").click(function(){
	if ($(".search li .title_txt").val() == "请输入关键字") {
	    return false;
        }
    });

    //pc_index
    $(".rr_table tr:gt(0)").hover(function() {
        $(this).addClass("hover");
    }, function() {
        $(this).removeClass("hover");
    })

    //file
    $(".file_box :input[type='file']").change(function() {
        var filesrc = $(this).val();
        if (filesrc != "") {
            $(this).next(":input[type='text']").val(filesrc);
        }
    })
    $(".bn_file :input[type='file']").change(function() {
        var filesrc = $(this).val();
        if (filesrc != "") {
            $(this).siblings(":input[type='text']").val(filesrc);
        }
    })

    //add_grouping
    $(".group_table tr th :checkbox").click(function() {
        if ($(this).attr("checked") == "checked") {
            $(this).parents("tr").next("tr").find("td .group_list li :checkbox").attr("checked", true);
        }
        else {
            $(this).parents("tr").next("tr").find("td .group_list li :checkbox").attr("checked", false);
        }
    })
    $(".rt_select").click(function() {
        $(".group_table :checkbox").attr("checked", false)
    })

    //visits_table
    $(".visit_show").toggle(function() {
        $(this).removeClass("visit_show");
        $(this).addClass("visit_hide");
        $(this).parents(".visits_table").find("tr:gt(1)").removeClass("visit_other");
    }, function() {
        $(this).removeClass("visit_hide");
        $(this).addClass("visit_show");
        $(this).parents(".visits_table").find("tr:gt(1)").addClass("visit_other");
    })

    //add_lottery
    $(".addlottery_table").each(function() {
        $(this).find(".select_all :checkbox").click(function() {
            if ($(this).attr("checked") == "checked") {
                $(this).parents(".addlottery_title").next(".addlottery_con").find(".addlottery_table .select_sg :checkbox").attr("checked", true);
            }
            else {
                $(this).parents(".addlottery_title").next(".addlottery_con").find(".addlottery_table .select_sg :checkbox").attr("checked", false);
            }
        })
    });

    //关闭弹出框
    $(".addlottery_button :input").click(function() {
        $(".popup").hide();
        $(".popup_bg").remove();
    })
    //IE6(当滚动条滚动时)
    $(window).scroll(function() {
        if (!window.XMLHttpRequest) {
            $(".popup").css({
                marginLeft: -$(".popup").width() / 2 + $(window).scrollLeft(),
                marginTop: -$(".popup").height() / 2 + $(window).scrollTop()
            })
            $(".popup_bg").css({
                width: $(window).width() + $(window).scrollLeft(),
                height: $(document).height()
            })
        }
    })
    //IE6(当窗口大小改变时)
    $(window).resize(function() {
        if (!window.XMLHttpRequest) {
            if ($(document).width() > $(window).width()) {
                $(".popup_bg").css({
                    width: $(document).width(),
                    height: $(document).height()
                })
            } else {
                $(".popup_bg").css({
                    width: "100%",
                    height: $(document).height()
                })
            }
        }
    })
    /**所有上传提示设置**/
    $("span[tips]").click(function() {
        var me = $(this)
        var tips = $(this).attr('tips')
        if (tips === '')
            return false;
        var html = "<div  style='padding:10px;'>请输入提示语：<input type='text' id='" + tips + "'  name='" + tips + "' /></div>";
        var submit = function(v, h, f) {
            $.jBox.tip('加载中...', 'loading')
            var tipcon = h.find('#' + tips + '').val()
            if ($.trim(tipcon) == '') {
                $.jBox.tip("请输入提示语。", 'error', {focusId: tips}); // 关闭设置 yourname 为焦点
                return false;
            }
            if (typeof(ARG) === 'undefined')
                ARG = ''
            $.post('/admin/tips/setips', '' + tips + '=' + tipcon + '&tem=' + TEM + '&model_id=' + MODELID + '&arguments=' + ARG, function(json) {
                $.jBox.tip(json.message)
                me.html('* ' + tipcon)
            }, 'json'
                    )

        };
        $.jBox(html, {title: "提示语设置", submit: submit});

    })

    /**获取提示语*/
    $("span[tips]").css({'padding': '0 0 0 10px', 'color': 'red'})
    if ($("span[tips]").size() > 0) {
        if (typeof(ARG) === 'undefined')
            posttips()
        else
            posttips(ARG)

    }
    /**广告列表提示语*/
    $('#advert_type_id').change(function() {
        var val = $(this).val();
        if (val) {
            $("span[tips]").attr('tips', 'advert_' + val);
        } else {
            $("span[tips]").attr('tips', '');
        }
        posttips('advert')

    })
    function posttips() {
        var arg = arguments[0] ? arguments[0] : ''
        $.post('/admin/tips/getips', 'tem=' + TEM + '&model_id=' + MODELID + '&arguments=' + arg, function(json) {
            if (json.isadmin != 1)
                $("span[tips]").unbind('click')
            else
                $("span[tips]").css('cursor', 'pointer')
            $("span[tips]").html('* 没有设置提示语')
            $.each(json.data, function(k, v) {
                $("span[tips='" + k + "']").html('* ' + v)
            })
        }, 'json')
    }

    /** 意见反馈 **/
    $('a#FeedbackGMS').click(function() {
        var html = '<form action="javascript:void(0)" method="post" enctype="application/x-www-form-urlencoded" id="memberRegister">'
        html += '<table width="100%" cellpadding="0" cellspacing="0" class="registerTable">';
        html += '<tr>';
        html += '<td class="right">反馈内容：</td>';
        html += '<td><textarea name="contents" class="w100" style="width: 240px; height: 80px;"></textarea></td>';
        html += '</tr>';
        html += '<tr>';
        html += '<td style="text-align: center;" colspan="2"></td>';
        html += '</tr>';
        html += '</table>';
        html += '</form>';
        var submit = function(v, h, f) {
            if (v == true) {
                h.find(".w100").css('border', '1px solid #BBB');
                if (f.contents == '' || h.find(":input[name='contents']").val().length > 300) {
                    $.jBox.tip('请填入你要反馈的意见内容（限制300字）', 'error');
                    h.find(":input[name='contents']").css('border', '1px solid red');
                    return false;
                }
                var tData = h.find('#memberRegister').serialize();
                $.jBox.tip('正在发送反馈……', 'loading');
                $.post('/admin/feedback/send', tData, function(data) {
                    if (data.error == '0') {
                        $.jBox.tip('反馈信息已成功发送，感谢您提出的宝贵意见，我们会及时处理', 'success', {timeout: 2000});
                        $.jBox.tip(data.message, 'success', {timeout: 2000});
                    } else {
                        $.jBox.tip(data.message, 'error', {timeout: 2000});
                        return false;
                    }
                }, 'json');
            }
            return true;
        }
        $.jBox(html, {title: "意见反馈", buttons: {'提交反馈': true, '取消反馈': false}, submit: submit});
    })

})


/*添加奖品弹出层*/
function addlotterypop() {
    $(".addlottery_pop").show();
    $(".popup").css({
        marginLeft: -$(".addlottery_pop").width() / 2,
        marginTop: -$(".addlottery_pop").height() / 2
    });
    $(".addlottery_pop").before("<div class='popup_bg'></div>");
}

// 修改当前URL的Params
function setNewUrl(paramName, replaceWith, oUrl) {
    if (oUrl == undefined || oUrl == '') {
        oUrl = this.location.href.toString();
    }
    oUrl = oUrl.replace(/\/$/g, '');
    eval('var regex = /' + paramName + '[\/|=][a-zA-Z0-9]{1,}/i');
    var r = oUrl.search(regex);
    if (r > 0) {
        var nUrl = oUrl.replace(regex, paramName + '/' + replaceWith);
    } else {
        var nUrl = oUrl + '/' + paramName + '/' + replaceWith;
    }
    return nUrl;
}

function goUrl(paramName, paramValue) {
    if (paramValue < 1)
        return false;
    var oUrl = location.href.toString();
    oUrl = oUrl.replace(/(\/$)/g, '');
    var r = oUrl.search(/\/index/i);
    if (r < 0) {
        window.location.href = setNewUrl(paramName, paramValue, oUrl + '/index');
    } else {
        window.location.href = setNewUrl(paramName, paramValue);
    }
}

function onlyNum(x) {
    _o = $(x).val();
    if (_o < 0) {
        $.jBox.tip('库存不能为负数', 'error', {
            timeout: 500,
            closed: function() {
                $(x).val('');
            }
        });
        return false;
    }
    if (isNaN(_o)) {
        $.jBox.tip('只能输入数字', 'error', {
            timeout: 500,
            closed: function() {
                $(x).val('');
            }
        });
        return false;
    }
    if (_o != parseInt(_o)) {
        $.jBox.tip('只能输入整数，不能输入小数', 'error', {
            timeout: 500,
            closed: function() {
                $(x).val('');
            }
        });
        return false;
    }
}