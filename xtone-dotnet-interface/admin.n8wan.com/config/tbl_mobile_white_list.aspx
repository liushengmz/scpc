<%@ Page Language="C#" AutoEventWireup="true" CodeFile="tbl_mobile_white_list.aspx.cs" Inherits="tbl_mobile_white_list" ValidateRequest="false" MasterPageFile="~/MasterPage.master" %>

<asp:Content runat="server" ContentPlaceHolderID="head">
    <script type="text/ecmascript">
        function add_onclick(sender, e) {

        }
    </script>

    <style type="text/css">
        .list { margin: 0; padding: 0; list-style-type: none; }
            .list li { padding: 0; margin-left: 1em; display: inline-block; width: 150px; }
    </style>

</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <form id="form1" runat="server">
        手机号或IMSI:<asp:TextBox ID="txtmobile" runat="server" />

        <input type="submit" value="添加" />
        <%--<a href="javascript:;" onclick="add_onclick(this,null);return false;">添加</a>--%>
    </form>

    <ul class="list">
        <asp:Repeater runat="server" ID="rpList">
            <ItemTemplate>
                <li><%#Eval("mobile") %></li>
            </ItemTemplate>
        </asp:Repeater>
    </ul>
</asp:Content>
