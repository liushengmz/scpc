<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="PoolSetEditor.aspx.cs" Inherits="cp_PoolSetEditor" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
    <style type="text/css">
        .del td { color: #ccc; }
    </style>
    <script src="../scripts/provices.js"></script>
        <script type="text/javascript">
            function writeProNames(e) {
                var names = new Provinces().getNamesByIds(e);
                document.write(names.join(","));
            }

    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <h1>池通道编辑</h1>
    <form method="post">
        <asp:Repeater runat="server" ID="rplst">
            <HeaderTemplate>
                <table class="datagrid">
                    <tr>
                        <th></th>
                        <th>PayCode</th>
                        <th>SP ID</th>
                        <th>业务名</th>
                        <th>通道</th>
                        <th>价格</th>
                        <th>省份</th>
                    </tr>
            </HeaderTemplate>
            <ItemTemplate>
                <tr>
                    <td <%#Eval("status").Equals(0) ?"class='del'":"" %>>
                        <input type="checkbox" name="troneOrderId" value="<%#Eval("paycode") %>"
                            <%#!DBNull.Value.Equals(Eval("cp_pool_id"))?"checked='checked'":"" %>
                            <%#!DBNull.Value.Equals(Eval("cp_pool_id"))||Eval("status").Equals(0) ?"disabled='disabled'":"" %> /></td>
                    <td>1<%#Eval("paycode","{0:00000}") %></td>
                    <td><%#Eval("sp_id", "1{0:000}") %></td>
                    <td><%#Eval("Name") %></td>
                    <td><%#Eval("trone_name") %></td>
                    <td><%#Eval("price","{0:c}") %></td>
                    <td>
                        <script type="text/javascript">writeProNames([<%#Eval("provinces") %>])</script>
                    </td>
                </tr>
            </ItemTemplate>
            <FooterTemplate>
                <footer>
                    <tr>
                        <td colspan="7">
                            <input type="submit" value="添加" />
                        </td>
                    </tr>
                </footer>
                </table>
            </FooterTemplate>
        </asp:Repeater>
    </form>

</asp:Content>

