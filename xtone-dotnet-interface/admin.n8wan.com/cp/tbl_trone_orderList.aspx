<%@ Page Language="C#" AutoEventWireup="true" CodeFile="tbl_trone_orderList.aspx.cs" Inherits="cp_tbl_trone_orderList" MasterPageFile="~/MasterPage.master" %>

<asp:Content runat="server" ContentPlaceHolderID="head">
    <style type="text/css">
        .del { text-decoration: line-through; }
    </style>
</asp:Content>

<asp:Content runat="server" ContentPlaceHolderID="body">
    <form method="get">
        <select name="cpid" id="cpid">
            <option value="">*全部 CP*</option>
            <asp:Repeater runat="server" ID="rpCPLst">
                <ItemTemplate>
                    <option value="<%#Eval("id") %>"><%#Eval("short_name") %></option>
                </ItemTemplate>
            </asp:Repeater>
        </select>
        <input type="submit" value="确定" />
    </form>
    <script type="text/javascript">
        document.getElementById("cpid").value = "<%#Request["cpid"]%>";
    </script>

    <asp:Repeater runat="server" ID="rpLst">
        <HeaderTemplate>
            <table class="datagrid">
                <tr>
                    <th>ID</th>
                    <th>同步业务名</th>
                    <th>指令</th>
                    <th>渠道</th>
                    <th>扣量比</th>
                    <th>最大同步</th>
                    <th>同步地址</th>
                    <th>操作</th>
                </tr>
        </HeaderTemplate>
        <ItemTemplate>
            <tr <%#(bool)Eval("disable")?"class='del'":"" %>>
                <td><%#Eval("id") %></td>
                <td><%#Eval("order_trone_name") %></td>
                <td><%#Eval("order_num") %></td>
                <td><%#GetCPName((int)Eval("cp_id")) %></td>
                <td><%#GetHoldInfo(Container.DataItem) %></td>
                <td><%#GetCPName((int)Eval("cp_id")) %></td>
                <td><%#GetPushURL((int)Eval("push_url_id")) %></td>
                <td>
                    <a href="tbl_trone_orderEditor.aspx?id=<%#Eval("id") %>">编辑</a>
                </td>
            </tr>
        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>
    </asp:Repeater>

</asp:Content>
