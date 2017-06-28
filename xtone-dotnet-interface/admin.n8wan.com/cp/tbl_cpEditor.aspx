<%@ Page Title="渠道用户信息编辑"  Language="C#" AutoEventWireup="true" CodeFile="tbl_cpEditor.aspx.cs" Inherits="tbl_cp_Editor" ValidateRequest="false" MasterPageFile="~/MasterPage.master" %>

<asp:Content ContentPlaceHolderID="body" runat="server">
    <form id="form1" runat="server">
        <table class="datagrid">

            <tr>
                <th>用户名:</th>
                <td>
                    <asp:TextBox ID="txtname" runat="server" /></td>
                <td>用户登录名</td>
            </tr>

            <tr>
                <th>密码:</th>
                <td>
                    <asp:TextBox ID="txtpwd" runat="server" /></td>
                <td>password用MD5加密</td>
            </tr>

            <tr>
                <th>昵称:</th>
                <td>
                    <asp:TextBox ID="txtnick_name" runat="server" /></td>
                <td>显示在系统的昵称</td>
            </tr>

            <tr>
                <th>公司全称:</th>
                <td>
                    <asp:TextBox ID="txtfull_name" runat="server" /></td>
                <td>CP全称</td>
            </tr>
            <tr>
                <th>公司简称:</th>
                <td>
                    <asp:TextBox ID="txtshort_name" runat="server" /></td>
                <td>CP简称</td>
            </tr>
            <tr>
                <th>联系人:</th>
                <td>
                    <asp:TextBox ID="txtcontact_person" runat="server" /></td>
                <td>联系人</td>
            </tr>
            <tr>
                <th>商务对接人:</th>
                <td>
                    <asp:RadioButtonList runat="server" ID="rblcommerce_user_id" RepeatDirection="Horizontal" RepeatLayout="Flow" />
                </td>
                <td>商务在用户表的ID，其实也就是tbl_user表里面的ID</td>
            </tr>
            <tr>
                <th>默认扣量:</th>
                <td>
                    <asp:TextBox ID="txtdefault_hold_percent" runat="server" /></td>
                <td>默认的扣量比， 0~99，0表示不扣</td>
            </tr>
            <tr>
                <th>地址:</th>
                <td>
                    <asp:TextBox ID="txtaddress" runat="server" /></td>
                <td>address</td>
            </tr>
<%--            <tr>
                <th>合同起始日:</th>
                <td>
                    <asp:TextBox ID="txtcontract_start_date" runat="server" /></td>
                <td>合同起始日</td>
            </tr>
            <tr>
                <th>合同结束日:</th>
                <td>
                    <asp:TextBox ID="txtcontract_end_date" runat="server" /></td>
                <td>合同结束日</td>
            </tr>--%>
            <tr>
                <th>status:</th>
                <td>
                    <asp:CheckBox ID="chkstatus" runat="server" Text="锁定" />
                </td>
                <td>状态，1为正常，0为锁定</td>
            </tr>


        </table>
        <input type="submit" value="确定" />
    </form>
</asp:Content>
