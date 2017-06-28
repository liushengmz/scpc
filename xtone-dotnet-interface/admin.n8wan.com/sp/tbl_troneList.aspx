<%@ Page Title="通道指令列表" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="tbl_troneList.aspx.cs" Inherits="sp_tbl_troneList" %>


<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <form method="get" action="../cp/trone_order_batchEditor.aspx">
        <a href="tbl_troneEditor.aspx?spid=<%#Request["spid"] %>&urlId=<%#Request["urlId"] %>">添加指令</a>
        <asp:Repeater runat="server" ID="rpLst">
            <HeaderTemplate>
                <table class="datagrid">
                    <tr>
                        <th>ID</th>
                        <th>通道名</th>
                        <th>端口</th>
                        <th>指令</th>
                        <th>类型</th>
                        <th>价格</th>
                        <th>操作</th>
                    </tr>
            </HeaderTemplate>
            <ItemTemplate>
                <tr class="<%#(int)Eval("status")==1?"":"del"%>">
                    <td>
                        <label>
                            <input type="checkbox" name="troneId" value="<%#Eval("id") %>" />
                            <%#Eval("id") %></label></td>
                    <td><%#Eval("trone_name") %></td>
                    <td><%#Eval("trone_num") %></td>
                    <td><%#Eval("orders") %></td>
                    <td><%#(bool)Eval("is_dynamic")?"模糊":"精确" %></td>
                    <td><%#Eval("price") %></td>
                    <td>
                        <a href="tbl_troneEditor.aspx?id=<%#Eval("id") %>">编辑</a>
                        <a href="../cp/tbl_trone_orderEditor.aspx?trone_id=<%#Eval("id") %>">分配渠道</a>
                    </td>
                </tr>

                <asp:Repeater ID="rpUsing" runat="server">
                    <HeaderTemplate>
                        <tr>
                            <td colspan="7">
                                <ol class="auto_hidden">
                    </HeaderTemplate>
                    <ItemTemplate>
                        <li <%#(bool)Eval("disable")?"class='del'":"" %>><%# Eval("order_num") %>(<%#(bool)Eval("is_dynamic")?"模糊":"精确" %>)  - <%# GetCpName((int) Eval("cp_id")) %> - <a href="../cp/tbl_trone_orderEditor.aspx?id=<%# Eval("id") %>">编辑</a> </li>
                    </ItemTemplate>
                    <FooterTemplate>
                        </ol>
                </td>
            </tr>
                    </FooterTemplate>
                </asp:Repeater>

            </ItemTemplate>
            <FooterTemplate>
                </table>
            </FooterTemplate>
        </asp:Repeater>
        <input type="submit" value="批量分配" />
    </form>
</asp:Content>

