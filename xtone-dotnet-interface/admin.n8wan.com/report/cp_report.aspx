<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="cp_report.aspx.cs" Inherits="report_cp_report" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <table class="datagrid">
        <tr>
            <th>CP</th>
            <th>业务名</th>
            <th>同步数</th>
            <th>金额</th>
            <th>推送数</th>
            <th>推送金额</th>
            <th>失败条数</th>
            <th>失败金额</th>
            <th>失败率</th>

            <th>操作</th>
        </tr>
        <asp:Repeater runat="server" ID="rpList">
            <ItemTemplate>
                <tr>
                    <td>
                        <%#Eval("short_name") %></td>
                    <td><%#Eval("trone_name") %>(<%#Eval("price") %>)</td>
                    <td class="tr"><%#Eval("c","{0:#,###}") %></td>
                    <td class="tr"><%#Eval("amount","{0:#,###.00}") %></td>
                    <td><%#Eval("push") %></td>
                    <td>推送金额</td>
                    <td><%#Eval("hold") %></td>
                    <td>失败金额</td>
                    <td><%#Eval("rx") %></td>
                    <td>-</td>
                </tr>
            </ItemTemplate>
            <FooterTemplate>
                <tr>
                    <th>-</th>
                    <th>-</th>
                    <th class="tr"><%# totalCount.ToString("#,###") %></th>
                    <th class="tr"><%# totalPrice.ToString("#,###.00") %></th>
                    <th>-</th>
                </tr>
            </FooterTemplate>
        </asp:Repeater>

    </table>

</asp:Content>

