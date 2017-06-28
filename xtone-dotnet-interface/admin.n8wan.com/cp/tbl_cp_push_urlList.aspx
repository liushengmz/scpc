<%@ Page Title="渠道同步地址" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="tbl_cp_push_urlList.aspx.cs" Inherits="tbl_cp_push_urlList" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <a href="tbl_cp_push_urlEditor.aspx?uid=<%# id %>" class="blue">添加同步地址</a>
    <asp:Repeater runat="server" ID="rpLst">
        <HeaderTemplate>
            <table class="datagrid">
                <tr>
                    <th>ID</th>
                    <th>CP</th>
                    <th>备注名称</th>
                    <th>指令数</th>
                    <th>同步URL</th>
                    <th>操作</th>
                </tr>
        </HeaderTemplate>
        <ItemTemplate>
            <tr>
                <td><%#Eval("id","{0:000}") %></td>
                <td><%#GetCP((int) Eval("cp_id") ) %></td>
                <td><%#Eval("name") %></td>
                <td><%#Eval("ref_count") %></td>
                <td><%#Eval("url") %></td>
                <td>
                    <a href="tbl_cp_push_urlEditor.aspx?id=<%#Eval("id") %>">编辑</a>
                </td>
            </tr>
        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>
    </asp:Repeater>
</asp:Content>

