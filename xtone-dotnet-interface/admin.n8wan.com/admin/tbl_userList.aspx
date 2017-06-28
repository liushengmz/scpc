<%@ Page Language="C#" AutoEventWireup="true" CodeFile="tbl_userList.aspx.cs" Inherits="admin_tbl_userList" MasterPageFile="~/MasterPage.master" %>

<asp:Content ID="Content1" ContentPlaceHolderID="body" runat="server">
    <asp:Repeater runat="server" ID="rpLst">

        <ItemTemplate>
            <%#Eval("id") %>
            <%#Eval("name") %>

            <a href="tbl_userEditor.aspx?id=<%#Eval("id") %>">编辑</a><br />
        </ItemTemplate>

    </asp:Repeater>
</asp:Content>