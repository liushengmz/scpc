<%@ Page Language="C#" AutoEventWireup="true" CodeFile="Details.aspx.cs" Inherits="report_Details" MasterPageFile="~/MasterPage.master" %>

<asp:Content ContentPlaceHolderID="head" runat="Server">
    <script type="text/javascript" src="/scripts/ajax.js"></script>
    <script type="text/javascript" src="/scripts/ToolsForm.js"></script>
    <script type="text/javascript">
        function rematch_link(sender, e) {
            var ajax = new ajax2();
            var xml = ajax.GET(sender.href, true);
            var rlt = simpleXMlParse(xml);
            if (rlt.state == "ok") {
                sender.style.color = "#ccc";
            }
            else
                alert(rlt.message);
        }

        var toolFrm = new ToolsFrm();
        toolFrm.Init();

        toolFrm.OnFrameCreate = function (e) {
            e.cnt.innerHTML = "Loading...";
            var ajax = new ajax2();
            if (e.id == "spId") {
                ajax.onload = function (html, code) {
                    e.cnt.innerHTML = ajaxdataProc(html, "spName_onclick");
                }
                ajax.GET("/ajax/GetNames.ashx?method=spname");
            } else if (e.id == "cpId") {
                ajax.onload = function (html, code) {
                    e.cnt.innerHTML = ajaxdataProc(html, "cpName_onclick");
                }
                ajax.GET("/ajax/GetNames.ashx?method=cpname");
            } else if (e.id == "spTrone") {
            }
        }

        function ajaxdataProc(data, funName) {
            var func = new Function("return " + data);
            var items = func();
            var html = "<dl>";
            for (var key in items) {
                var item = items[key];
                html += "<dt>" + key + "</dt>";
                html += "<dd><ul>";
                for (var i = 0; i < item.length; i++) {
                    html += "<li onclick='" + funName + "(this,{id:" + item[i].id + "})'>" + item[i].name + "</li>";
                }
                html += "</ul></dd>";
            }
            return html;
        }

        function spName_onclick(sender, e) {
            document.getElementById("spId").value = e.id + "," + sender.innerHTML;
            document.getElementById("spTrone").value = "";
            document.getElementById("spTrone").options.length = 1;
            var ajax = new ajax2();

        }


        function cpName_onclick(sender, e) {
            document.getElementById("cpId").value = e.id + "," + sender.innerHTML;
            var ajax = new ajax2();
            ajax.onload = function (html, e) {
            }
            document.getElementById("spOrder").options.length = 1;

            //ajax.GET("/ajax/GetNames.ashx?method=sptrone");
        }
    </script>

    <style type="text/css">
        .frm { position: absolute; width: 530px; min-height: 100px; top: 1px; left: 1px; background-color: #FFFFCC; border: 1px #FF9900 solid; overflow-y: auto; display: none; z-index: 1; }
            .frm dl { margin: 0; padding: 0; }
            .frm dt { position: absolute; width: 16px; margin: 0px; margin-left: 2px; padding: 0; font-weight: bold; color: #ff2323; }
            .frm dd { margin: 2px 0; margin-left: 12px; }
                .frm dd ul { margin: 0; padding: 0; }
                .frm dd li { display: inline-block; margin-left: 8px; cursor: pointer; }
        .sTxtbox { display: inline-block; width: 100px; border: 1px solid #ccc; background-color: #f6f6f6; padding-left: 2px; }
    </style>
</asp:Content>
<asp:Content ContentPlaceHolderID="body" runat="Server">
    <form method="get">
        SP:<input name="spId" id="spId" onclick="toolFrm.ShowHide({ event: this, id: this.id });" />
        业务：<select id="spTrone" name="spTrone"><option value="">*全部*</option>
        </select>
        CP:<input name="cpId" id="cpId" onclick="toolFrm.ShowHide({ event: this, id: this.id });" />
        CP指令：<select id="spOrder" name="spOrder"><option value="">*全部*</option>
        </select>
        <input type="submit" value="确定" />
    </form>

    <table class="datagrid">
        <tr>
            <th>ID</th>
            <th>SP</th>
            <th>同步URL</th>
            <th>端口</th>
            <th>指令</th>
            <th>时间</th>
            <th>操作</th>
        </tr>
        <asp:Repeater runat="server" ID="rpList">
            <ItemTemplate>
                <tr>
                    <td>
                        <input type="checkbox" name="id" value="<%#Eval("id") %>" /><%#Eval("id") %></td>
                    <td><%#Eval("sp_id") %></td>
                    <td><a href="/sp/tbl_troneList.aspx?urlId=<%#Eval("sp_api_url_id") %>"><%#Eval("sp_api_url_id") %></a></td>
                    <td><%#Eval("ori_trone") %></td>
                    <td><%#Eval("ori_order") %></td>
                    <td><%#Eval("create_date") %></td>
                    <td><a href="../sp/tbl_troneEditor.aspx?urlId=<%#Eval("sp_api_url_id") %>">配置指令</a>
                        <a href="../ajax/ReMatch.ashx?id=<%#Eval("id") %>" onclick="rematch_link(this);return false;">重新匹对</a>
                    </td>
                </tr>
            </ItemTemplate>
        </asp:Repeater>

    </table>
    <%--    <div class="page">
        <%#base.PS.GetPageSpliterHtml(true) %>
    </div>--%>
</asp:Content>
