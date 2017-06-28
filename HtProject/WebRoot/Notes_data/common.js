$(function() {
    /**
     *表单验证绑定
     */
    formobj = $("form").Validform({
        tiptype: function(msg, o, cssctl) {
            if (!o.obj.is("form")) {//验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
                o.obj.next(".Validform_checktip").remove();
                o.obj.after('<span class="Validform_checktip"></span>');
                var objtip = o.obj.next(".Validform_checktip");
                cssctl(objtip, o.type);
                objtip.text(msg);
            }
        },
        btnSubmit: ".btn_sub",
        btnReset: ".cancel",
        datatype: {
            "url1": /^[A-Za-z0-9\-]+$/,
            "menu-url": /^[A-Za-z0-9\-]+\.html$/,
            "ex": /^[\w\W]+[^\u4e00-\u9fa5]\.xlsx$/i,
            "pic": /^[\w\W]+[^\u4e00-\u9fa5]\.(jpg|gif|png|jpeg)$/i,
            "files": /^[\w\W]+\.(xml|html|txt)$/i,
            "excel": /^[\w\W]+\.(xlsx|xls)$/i,
            "theme": /^#[a-z|0-9|A-Z]{6}/i
        },
        showAllError: true
    });
    $.Tipmsg.r = "";

    //ajax获取子类
    $("#cat select").change(function() {
        ajaxChange($(this));
    });

    /**
     *资源全选/反选
     */
    $(".resource input").click(function() {
        var className = ".resource_cate_id" + $(this).attr("data");
        
        var falg = !$(this).attr("checked");
        
        $(className).each(function() {
        	$(className).attr("checked", !falg);
        });
    });

    /**
     * 全选/反选按钮
     */
    $("#checkBox").click(function() {
        $.each($("input[type=checkbox]"), function() {
            if ($(this).attr("id") !== 'checkBox') {
                $(this).attr("checked", !$(this).attr("checked"));
            }
        });
    });

    /**
     * 每页显示条数切换
     */
    $(".selectchange").change(function() {
        var url = $(this).val();
        window.location.href = url;
    });


    //栏目 添加和栏目编辑 -- 删除栏目图片
    $("#del_category_thumbnail").click(function() {
        $(this).siblings("input").val("");
        $(this).siblings("img").attr("src", "/Public/images/add_pic.gif")
    })



    /**
     **ajax生成图表
     *                terminal：标识手机（值：mobile）
     *                terminal：标识pc（值：pc）
     *                ele:图表放置的容器（id）
     *                data:参数数据 （例子：xxxx=23&xx=rrrr）
     *                url:地址
     *                图表数据必须遵从以下规则（json格式）：
     *                                status.name=XXXX
     *                                status.data=[1,2,3,4,5,6]
     */
    jQuery.chart = function(settings) {
        var config = {
            ele: null, //div容器 必须为id,
            data: null, //post  查询数据
            url: null, //接口地址
            type: null, //显示类型 'column':圆柱 | 'line':线性
            title: null, //标题
            cate: null, // X坐标显示栏目
            max: null, //最大显示数
            per: null, //每行数间隔数
            y_text: null,
            series: null

        };
        var option = {
            exporting: {
                enabled: true //用来设置是否显示‘打印’,'导出'等功能按钮，不设置时默认为显示
            },
            chart: {
                // renderTo: 'pc_statics',
                type: 'column',
                plotBackgroundColor: '#FDFDFD',
                marginTop: '80'
            },
            title: {
                text: '2013年度数据统计',
                style: {
                    font: 'normal 20px 微软雅黑, sans-serif',
                    color: '#296DA6'
                }
            },
            xAxis: {
                categories: [
                    '1月',
                    '2月',
                    '3月',
                    '4月',
                    '5月',
                    '6月',
                    '7月',
                    '8月',
                    '9月',
                    '10月',
                    '11月',
                    '12月'
                ],
                labels: {//设置横轴坐标的显示样式
                    rotation: -45, //倾斜度
                    align: 'right',
                    style: {
                        font: 'normal 11px 微软雅黑, sans-serif',
                        color: '#296DA6'
                    }
                }
            },
            yAxis: {
                // tickInterval: 20,
                //max: 200,
                min: 0,
                title: {
                    text: '信息量'
                },
                gridLineColor: "#ECECEC", //纵向格线的颜色
                gridLineDashStyle: 'Solid',
                gridLineWidth: 1

            },
            legend: {
                // layout: 'vertical',
                backgroundColor: '#FFFFFF',
                align: 'center',
                verticalAlign: 'top',
                x: 0,
                y: 30,
                //itemWidth: 200,
                floating: true,
                shadow: true
            },
            credits: {//右下角的文本
                enabled: true,
                position: {//位置设置
                    align: 'right',
                    x: -10,
                    y: -10
                },
                href: "", //点击文本时的链接
                text: ""//显示的内容
            },
            tooltip: {
                formatter: function() {
                    return '' +
                            this.x + ' ' + this.series.name + '信息量为：' + this.y;
                }
            },
            plotOptions: {
                column: {
                    // pointWidth: 15,
                    //pointPadding: 0.2,
                    //borderWidth: 0,
                    dataLabels: {
                        enabled: true
                    }
                },
                line: {
                    dataLabels: {
                        enabled: true
                    },
                    enableMouseTracking: false
                }
            }
            //,
            // series: [{}, {},{}, {}]
        }
        if (settings) {
            $.extend(config, settings);
        }
        option.chart.renderTo = config.ele
        if (config.title) {
            option.title.text = config.title
        }
        if (config.y_text) {
            option.yAxis.title.text = config.y_text
        }
        if (config.cate) {
            option.xAxis.categories = config.cate
        }
        if (config.max && config.per) {
            option.yAxis.max = config.max
            option.yAxis.tickInterval = config.per
        }
        if (config.type) {
            option.chart.type = config.type
        }
        if (config.url != '') {
            $.jBox.tip('加载中.....', 'loading')
            $.post(config.url, config.data, function(json) {
                $.jBox.tip('加载成功', 'success')
                option.series = new Array();
                /**动态添加json*/
                for (key in json) {
                    option.series.push(new Object())
                }
                for (key in json) {
                    option.series[key].name = json[key].name;
                    option.series[key].data = json[key].date;
                }
                new Highcharts.Chart(option);
            }, 'json');
        } else {
            if (config.series) {
                json = config.series;
                option.series = new Array();
                /**动态添加json*/
                for (key in json) {
                    option.series.push(new Object())
                }
                for (key in json) {
                    option.series[key].name = json[key].name;
                    option.series[key].data = json[key].data;
                }
            }
            new Highcharts.Chart(option);
        }

    }

});

function updateActionSort(action){
    $('#form_submit').attr('action', action);
    $.jBox.confirm("确定执行排序操作吗？", "提示", function(v, h, f) {
        if (v == 'ok') {
            $.jBox.tip("正在更新...", 'loading');
            $('#form_submit').submit();
        }
        return true; //close
    });
}
function confirmUrl(url,message) {
    $.jBox.confirm(message, "提示", function(v, h, f) {
        if (v == 'ok') {
            location.href = url;
        }
        return true; //close
    });
}
function getActionUrl(action)
{
    $('#form_submit').attr('action', action);
    var state = false;
    $.each($("input[type=checkbox]"), function() {
        if ($(this).attr("checked")) {
            state = true;
        }
    });

    if (state == true) {
        $.jBox.confirm("确定删除吗？", "提示", function(v, h, f) {
            if (v == 'ok') {
                $.jBox.tip("正在删除...", 'loading');
                $('#form_submit').submit();
            }
            return true; //close
        });
    } else {
	$.jBox.tip("请选择要删除的信息", 'warning');
    }
}

function setActionUrl(action)
{
    $('#form_submit').attr('action', action);
    var state = false;
    $.each($("input[type=checkbox]"), function() {
        if ($(this).attr("checked")) {
            state = true;
        }
    });

    if (state == true) {
        $.jBox.confirm("确定还原选择的吗？", "提示", function(v, h, f) {
            if (v == 'ok') {
                $.jBox.tip("正在修改...", 'loading');
                $('#form_submit').submit();
            }
            return true; //close
        });
    } else {
        $.jBox.tip("请选择要还原的信息", 'warning');
    }
}

/**
 *
 **/
function getOpenUrl(action)
{
    $('#form_submit').attr('action', action);
    var state = false;
    $.each($("input[type=checkbox]"), function() {
        if ($(this).attr("checked")) {
            state = true;
        }
    });

    if (state == true) {
        $.jBox.confirm("确定吗？", "提示", function(v, h, f) {
            if (v == 'ok') {
                $('#form_submit').submit();
            }
            return true; //close
        });
    } else {
        $.jBox.tip("请选择要开启的信息", 'warning');
    }
}

/**
 * 提示信息显示框
 */

/**
 * 提示信息显示框
 */

function showTips(msg) {
    var url = arguments[1] ? arguments[1] : "";
    var type = arguments[2] ? arguments[2] : "1";
    var time = arguments[3] ? arguments[3] : "2";
    if (url) {
        $.jBox.tip(msg);
        window.setTimeout(function() {
            location.href = url;
        }, time * 1000);
    } else {
        $.jBox.tip(msg);
    }
}
/**
 * 信息浮动框
 */
function windowsdig(iframetitle, iframesrc, width, height) {
    $.jBox.open("iframe:" + iframesrc, iframetitle, width, height, {
        buttons: false
    });
}

//遍历多选框并用','串接其值为字符串
function trimcheckbox(elmentid) {
    var checked = false;
    var _value = "";
    var ids = document.getElementsByName(elmentid);
    for (var i = 0; i < ids.length; i++) {
        if (!ids[i].checked)
            continue;
        if (ids[i].checked) {
            if (i == 0 || i == ids.length) {
                _value += ids[i].value;
            }
            else
                _value += ',' + ids[i].value;
        }
    }
    if (_value.substr(0, 1) == ',') {
        _value = _value.substr(1, _value.length - 1);
    }
    return _value;
}
/*左侧菜单导航*/
function showLft(t){
    url = $(t).attr("ajaxurl");
    $(t).parents(".navigation").find("a").removeClass("click");
    $(t).addClass("click");
    $(".leftmenu").load(url);
}
function loadinitmenu(url){
    $(".leftmenu").load(url);
}