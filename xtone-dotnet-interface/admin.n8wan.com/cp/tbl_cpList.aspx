<%@ Page Title="渠道用户" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="tbl_cpList.aspx.cs" Inherits="tbl_cpList" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <a href="tbl_cpEditor.aspx" class="blue">添加CP</a>
    <asp:Repeater runat="server" ID="rpLst">
        <HeaderTemplate>
            <table class="datagrid">
                <tr>
                    <th>ID</th>
                    <th>用户名</th>
                    <th>昵称</th>
                    <th>操作</th>
                </tr>
        </HeaderTemplate>
        <ItemTemplate>
            <tr>
                <td><%#Eval("id","{0:000}") %></td>
                <td><%#Eval("name") %></td>
                <td><%#Eval("nick_name") %></td>
                <td>
                    <a href="tbl_cpEditor.aspx?id=<%#Eval("id") %>">编辑</a>
                    <a href="tbl_cp_push_urlList.aspx?id=<%#this.GetCPIDByUserId((int) Eval("id")) %>">同步地址</a>
                </td>
            </tr>
        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>
    </asp:Repeater>
</asp:Content>

