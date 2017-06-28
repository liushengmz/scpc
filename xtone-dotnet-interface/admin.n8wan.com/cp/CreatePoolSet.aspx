<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="CreatePoolSet.aspx.cs" Inherits="cp_CreatePoolSet" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
    <script src="../scripts/jquery-3.1.1.js"></script>
    <script src="../scripts/ToolsForm.js"></script>

    <style type="text/css">
        .tooltip { position: absolute; max-width: 600px; max-height: 600px; overflow: auto; border: 1px dotted #ffd800; background: #fffef7; }
            .tooltip p { margin: 5px; padding: 0; font-size: 1em; border-bottom: 1px dotted #666; min-height: 3em; margin-bottom: 2px; padding-bottom: 2px; }
                .tooltip p::first-letter { font-size: 2.8em; line-height: 1em; margin: 5px 5px 0 0; color: #ff6a00; float: left; display: block; }
                .tooltip p:last-child { border-bottom-style: none; margin-bottom: 0; }
                .tooltip p label { margin-right: 0.5em; white-space: nowrap; }
                    .tooltip p label:hover { color: #f00; }
                    .tooltip p label:last-child { clear: both; }
    </style>
    <script type="text/javascript">
        var data = [{ "key": "abc", "name": "abc", "id": "1" }, { "key": "asc", "name": "dafadf", "id": "2" }, { "key": "zoom", "name": "放大", "id": "3" }];

        $(document).ready(function () {
            $.getJSON("../ajax/getCpPinYin.ashx", null, function (e) {
                data = e;
                var mc = /cpid=(\d+)/.exec(location.hash);
                if (mc == null)
                    return;

                var id = parseInt(mc[1]);
                if (isNaN(id))
                    return;

                $("#cpid").val(id);
                for (var i = 0; i < data.length; i++) {
                    if (data[i].id == id) {
                        tooltip_save($("#cpid_cnt")[0], data[i]);
                        return;
                    }
                }

            });

        });

        var toolFrm = new ToolsFrm();
        toolFrm.frameCSSName = "tooltip";
        toolFrm.Init();

        toolFrm.onShowHide = function (sender, e) { }

        toolFrm.OnFrameCreate = function (e) {
            e.cnt.innerHTML = "Loading...";
        }


        function searchAndShow(cnt, keyword) {
            var group = [];
            var items = [];

            for (var i = 0; i < data.length; i++) {
                var item = data[i];
                if (keyword != null && keyword != "" && item.key.indexOf(keyword.toUpperCase()) == -1 && item.name.indexOf(keyword) == -1)
                    continue;
                var k = item.key.substring(0, 1);
                items = group[k];
                if (items == null) {
                    items = [];
                    group[k] = items;
                }
                items.push(item);
            }
            var html = "";
            for (var k in group) {
                html += "<p>" + k;
                var items = group[k];
                for (var i = 0; i < items.length; i++) {
                    var item = items[i];
                    html += "<label onclick='item_onclick(this,{id:" + item.id + ",name:\"" + item.name + "\"});' >" + item.name + "</label> ";
                }
                html += "</p>";
            }
            if (html == "")
                html = "<p>无匹配项</p>";
            cnt.innerHTML = html;

        }

        function txt_onfocus(sender, e) {

            var hTime = setTimeout(function () {
                clearTimeout(hTime);
                hTime = null;
                if (toolFrm.button == sender)
                    return;
                sender.select();
                toolFrm.ShowHide({ event: sender });
                txt_onkeyup(null, "");
            }, 100);
        }

        function txt_onblur(sender, e) {

            var hTime = setTimeout(function () { clearTimeout(hTime); toolFrm.Hide(false); }, 100);

            var label = document.getElementsByClassName("tooltip")[0].getElementsByTagName("label");
            if (label.length == 1)
                label[0].click();

            var field = document.getElementById(sender.getAttribute("data-name"));
            var id = parseInt(field.value);
            if (sender.value == "") {
                field.value = "";
                return;
            }

            if (isNaN(id))
                return;

            for (var i = 0; i < data.length; i++) {
                if (data[i].id == id) {
                    tooltip_save(sender, data[i]);
                    return;
                }
            }
        }

        function txt_onkeyup(sender, e) {
            searchAndShow(document.getElementsByClassName("tooltip")[0], e == null ? sender.value : e);
        }
        function item_onclick(sender, e) {
            tooltip_save(toolFrm.button, e);
            setTimeout(function () { toolFrm.ShowHide(null); }, 100);
        }
        function tooltip_save(cnt, e) {
            cnt.value = e.name;
            document.getElementById(cnt.getAttribute("data-name")).value = e.id;
        }


    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <form method="post">
        <table class="datagrid">
            <tr>
                <th>名称：</th>
                <td>
                    <input name="name" size="50" required="required" /></td>
                <td>助记名称</td>
            </tr>
            <tr>
                <th>渠道：</th>
                <td>
                    <input type="hidden" name="cpid" id="cpid" />
                    <input id="cpid_cnt" autocomplete="off" size="50" required="required" onfocus="txt_onfocus(this, null)" data-name="cpid"
                        onclick="txt_onfocus(this, null)" onblur="txt_onblur(this,null)" onkeyup="txt_onkeyup(this, null)" /></td>
                <td></td>
            </tr>
            <tr>
                <th>价格：</th>
                <td>
                    <input type="number" name="price" required="required" /></td>
                <td>单位：元</td>
            </tr>

            <tr>
                <td colspan="3">
                    <input type="submit" value="创建" /></td>
            </tr>

        </table>
    </form>

</asp:Content>

