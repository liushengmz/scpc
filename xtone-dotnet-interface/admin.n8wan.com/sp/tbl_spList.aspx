<%@ Page Title="SP列表" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="tbl_spList.aspx.cs" Inherits="sp_tbl_spList" %>

<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <a href="tbl_spEditor.aspx" class="blue" >添加SP</a>
    <asp:Repeater runat="server" ID="rpLst">
        <HeaderTemplate>
            <table class="datagrid">
                <tr>
                    <th>ID</th>
                    <th>公司名</th>
                    <th>联系人</th>
                    <th>操作</th>
                </tr>
        </HeaderTemplate>
        <ItemTemplate>
            <tr>
                <td><%#Eval("id","{0:000}") %></td>
                <td><%#Eval("short_name") %></td>
                <td><%#Eval("contract_person") %></td>
                <td>
                    <a href="tbl_spEditor.aspx?id=<%#Eval("id") %>">编辑</a>
                    <a href="tbl_troneList.aspx?spid=<%#Eval("id") %>">指令列表</a>
                    <a href="tbl_sp_api_urlList.aspx?spid=<%#Eval("id") %>">同步地址</a>
                </td>
            </tr>
        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>
    </asp:Repeater>

</asp:Content>

