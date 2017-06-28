<%@ Page Language="C#" AutoEventWireup="true" CodeFile="tbl_userEditor.aspx.cs" Inherits="tbl_user_Editor" ValidateRequest="false" MasterPageFile="~/MasterPage.master" %>

<asp:Content ContentPlaceHolderID="body" runat="server">
    <form id="form1" runat="server">
        <table class="datagrid">
            <tr>
                <th>name:</th>
                <td>
                    <asp:TextBox ID="txtname" runat="server" /></td>
                <td>用户登录名</td>
            </tr>
            <tr>
                <th>pwd:</th>
                <td>
                    <asp:TextBox ID="txtpwd" runat="server" TextMode="Password" /></td>
                <td>password用MD5加密</td>
            </tr>
            <tr>
                <th>nick_name:</th>
                <td>
                    <asp:TextBox ID="txtnick_name" runat="server" /></td>
                <td>显示在系统的昵称</td>
            </tr>
            <tr>
                <th>mail:</th>
                <td>
                    <asp:TextBox ID="txtmail" runat="server" /></td>
                <td>mail</td>
            </tr>
            <tr>
                <th>qq:</th>
                <td>
                    <asp:TextBox ID="txtqq" runat="server" /></td>
                <td>qq</td>
            </tr>
            <tr>
                <th>phone:</th>
                <td>
                    <asp:TextBox ID="txtphone" runat="server" /></td>
                <td>phone</td>
            </tr>
            <tr>
                <th>group_list:</th>
                <td>
                    <asp:TextBox ID="txtgroup_list" runat="server" /></td>
                <td>所属组别，ID对应为tbl_group的主键，用英文逗号隔开，表示同一用户可以从属于多个组别</td>
            </tr>
            <tr>
                <th>status:</th>
                <td>
                    <asp:CheckBox runat="server" ID="chkStatus" Text="停用" />
                </td>
                <td>状态，1为正常，0为锁定，锁定状态下不能登录</td>
            </tr>
            <%--<tr>
                <th>create_date:</th>
                <td>
                    <asp:TextBox ID="txtcreate_date" runat="server" /></td>
                <td>创建时间</td>
            </tr>--%>
        </table>
        <input type="submit" value="确定" />
    </form>
</asp:Content>
