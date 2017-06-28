<%@ Page Language="C#" AutoEventWireup="true" CodeFile="tbl_cp_push_urlEditor.aspx.cs" Inherits="tbl_cp_push_url_Editor" ValidateRequest="false" MasterPageFile="~/MasterPage.master" %>

<asp:Content runat="server" ContentPlaceHolderID="head">
    <style type="text/css">
        input[type="text"] { width:500px; }
        ol { margin:0; padding:0;  list-style-type:none; }
        ol li { display: inline-block; width:auto;}
    </style>
</asp:Content>

<asp:Content runat="server" ContentPlaceHolderID="body">

    <form id="form1" runat="server">
        <table class="datagrid">
            <tr>
                <th style="width:150px;" >渠道用户:</th>
                <td colspan="2">
                    <asp:RadioButtonList ID="rblcp_id" runat="server" DataValueField="id" DataTextField="short_name"  RepeatLayout="OrderedList" />
                </td>
            </tr>
            <tr>
                <th>name:</th>
                <td style="width:500px;">
                    <asp:TextBox ID="txtname" runat="server" /></td>
                <td></td>
            </tr>

            <tr>
                <th>url:</th>
                <td>
                    <asp:TextBox ID="txturl" runat="server" /></td>
                <td>CP接收指令的URL</td>
            </tr>

            <tr>
                <th>扣量比:</th>
                <td>
                    <asp:TextBox ID="txthold_percent" runat="server" Text="0" /></td>
                <td>扣量比量(0~99)百分比</td>
            </tr>
            <tr>
                <th>同步上限:</th>
                <td>
                    <asp:TextBox ID="txtmax_amonut" runat="server" Text="0" /></td>
                <td>当天最大同步金额，0:不限</td>
            </tr>

            <tr>
                <th>amount:</th>
                <td>
                    <asp:Label ID="lbamount" runat="server" /></td>
                <td>当天已经同步金额</td>
            </tr>
            <tr>
                <th>引用数:</th>
                <td>
                    <asp:Label runat="server" ID="lbref_count" /></td>
                <td>指令引用数</td>
            </tr>
            <%--            <tr>
                <th>每日起扣条:</th>
                <td>
                    <asp:TextBox ID="txthold_start" runat="server" /></td>
                <td>起扣条数</td>
            </tr>
            <tr>
                <th>hold_count:</th>
                <td>
                    <asp:TextBox ID="txthold_count" runat="server" /></td>
                <td>当天已经扣条数</td>
            </tr>--%>
        </table>
        <input type="submit" value="确定" />
    </form>

</asp:Content>
