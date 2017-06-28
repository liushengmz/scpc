<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="UnmatchTrone.aspx.cs" Inherits="report_UnmatchTrone" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
    <script type="text/javascript" src="http://bbs.gkong.com/scripts/ajax.js"></script>

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
    </form>

    <table class="datagrid">
        <tr>
            <th>序号</th>
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
    <div class="page">
        <%#base.PS.GetPageSpliterHtml(true) %>
    </div>


</asp:Content>

