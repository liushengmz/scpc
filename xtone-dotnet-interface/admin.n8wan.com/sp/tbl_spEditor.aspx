<%@ Page Title="SP信息编辑" Language="C#" AutoEventWireup="true" CodeFile="tbl_spEditor.aspx.cs" Inherits="tbl_sp_Editor" ValidateRequest="false" MasterPageFile="~/MasterPage.master" %>

<asp:Content ContentPlaceHolderID="body" runat="server">
    <form id="form1" runat="server">
        <table class="datagrid">

            <tr>
                <th>full_name:</th>
                <td>
                    <asp:TextBox ID="txtfull_name" runat="server" /></td>
                <td>全称</td>
            </tr>

            <tr>
                <th>short_name:</th>
                <td>
                    <asp:TextBox ID="txtshort_name" runat="server" /></td>
                <td>简称</td>
            </tr>

            <tr>
                <th>contract_person:</th>
                <td>
                    <asp:TextBox ID="txtcontract_person" runat="server" /></td>
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
                <th>address:</th>
                <td>
                    <asp:TextBox ID="txtaddress" runat="server" /></td>
                <td>address</td>
            </tr>

            <tr>
                <th>contract_start_date:</th>
                <td>
                    <asp:TextBox ID="txtcontract_start_date" runat="server" /></td>
                <td>合同起始时间</td>
            </tr>

            <tr>
                <th>contract_end_date:</th>
                <td>
                    <asp:TextBox ID="txtcontract_end_date" runat="server" /></td>
                <td>合同结束时间</td>
            </tr>

            <tr>
                <th>status:</th>
                <td>
                    <asp:CheckBox runat="server" ID="chkstatus" Text="锁定" />
                </td>
                <td>状态，1为正常，0为锁定</td>
            </tr>


        </table>
        <input type="submit" value="确定" />
    </form>
</asp:Content>
