<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="PoolSetPriorityEditor.aspx.cs" Inherits="cp_PoolSetPriority" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
    <script src="../scripts/provices.js"></script>
    <script type="text/javascript">
        function writeProNames(e) {
            var names = new Provinces().getNamesByIds(e);
            document.write(names.join(","));
        }

    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <h1>池通道优先级调整</h1>
    <form method="post">


        <asp:Repeater runat="server" ID="rplst">
            <HeaderTemplate>
                <table class="datagrid">
                    <tr>
                        <th colspan="2">池名称</th>
                        <td colspan="6">
                            <input name="poolName" value="<%# Server.HtmlEncode( _poolModel.name) %>" /></td>
                    </tr>
                    <tr>
                        <th>启用</th>
                        <th>优先级</th>
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
                    <td>
                        <input type="hidden" value="<%#Eval("cp_pool_set_id")%>" name="troneOrderId" />
                        <label>
                            <input type="checkbox" value="1" name="e_<%#Eval("cp_pool_set_id")%>" <%#Eval("status").Equals(1)?"checked='checked'":"" %> /></label></td>
                    <td>
                        <input value="<%#Eval("Priority") %>" required="required" type="number" name="p_<%#Eval("cp_pool_set_id")%>" />
                    </td>
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
                        <td colspan="8">
                            <input type="submit" value="确定" />
                        </td>
                    </tr>
                </footer>
                </table>
            </FooterTemplate>
        </asp:Repeater>
    </form>


</asp:Content>

