<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="UnmatchCPTrone.aspx.cs" Inherits="report_UnmatchCPTrone" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
    <script type="text/javascript" src="http://bbs.gkong.com/scripts/ajax.js"></script>

    <script type="text/javascript">
        ajax2.prototype.cache = false;
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

        var batchIdx = 0;
        var batchLnks = [];
        function batch_remath() {
            batchLnks = document.getElementsByName("rm_lnk");
            NextClick();
        }
        function NextClick() {
            if (batchIdx >= batchLnks.length) {
                window.location.reload();
                return;
            }
            var ajax = new ajax2();
            ajax.onload = ajax_callback;
            ajax.GET(batchLnks[batchIdx].href, true);
        }

        function ajax_callback(xml, code) {
            var lnk = batchLnks[batchIdx++];
            setTimeout(NextClick, 10);
            var rlt = simpleXMlParse(xml);
            if (rlt.state == "ok") {
                lnk.style.color = "#ccc";
            }
            else
                alert(rlt.message);
        }


    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <form method="get">
        <select name="urlId">
            <option value="">*全部*</option>
            <asp:Repeater runat="server" ID="rpSel">
                <ItemTemplate>
                    <option value="<%#Eval("id") %>"><%#Eval("name") %></option>
                </ItemTemplate>
            </asp:Repeater>
        </select>
        <input type="submit" value="确定" />
        <a href="javascript:;" onclick="batch_remath();this.style.display='none';">批量重匹对</a>
    </form>

    <table class="datagrid">
        <tr>
            <th>序号</th>
            <th>SP</th>
            <th>同步URL</th>
            <th>通道名称</th>
            <th>LinkId</th>
            <th>端口</th>
            <th>指令</th>
            <th>手机</th>
            <th>CPID</th>
            <th>透参</th>
            <th>source_ip</th>
            <th>时间</th>
            <th>操作</th>
        </tr>
        <asp:Repeater runat="server" ID="rpList">
            <ItemTemplate>
                <tr style="text-align: center">
                    <td>
                        <input type="checkbox" name="id" value="<%#Eval("id") %>" /><%#Eval("id") %></td>
                    <td><%#GetTB_SP((int) Eval("sp_id") ) %></td>
                    <td><a href="/sp/tbl_troneList.aspx?urlId=<%#Eval("sp_api_url_id") %>"><%#GetTB((int) Eval("sp_api_url_id") ) %></a></td>
                    <td><%#GetTB_TD((int) Eval("trone_id") ) %></td>
                    <td><%#Server.HtmlEncode((string)Eval("linkid")) %></td>
                    <td><%#Server.HtmlEncode((string)Eval("ori_trone")) %></td>
                    <td><%#Server.HtmlEncode((string)Eval("ori_order")) %></td>
                    <td><%#Server.HtmlEncode((string)Eval("mobile")) %></td>
                    <td><%#Eval("cp_id") %></td>
                    <td><%#Eval("cp_param") %></td>
                    <td><%#Eval("ip")%></td>
                    <td><%#Eval("create_date") %></td>
                    <td><a href="../cp/tbl_trone_orderEditor.aspx?trone_id=<%#Eval(LightDataModel.tbl_mrItem.Fields.trone_id) %>">分配到CP</a>
                        <a href="../ajax/ReMatch.ashx?id=<%#Eval("id") %>" name="rm_lnk" onclick="rematch_link(this);return false;">重新匹对</a>
                    </td>
                </tr>
            </ItemTemplate>
        </asp:Repeater>

    </table>
    <div class="page">
        <%#base.PS.GetPageSpliterHtml(true) %>
    </div>


</asp:Content>

