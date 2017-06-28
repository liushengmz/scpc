<%@ Page Language="C#" Title="指令分配" AutoEventWireup="true" CodeFile="tbl_trone_orderEditor.aspx.cs" Inherits="tbl_trone_order_Editor" ValidateRequest="false" MasterPageFile="~/MasterPage.master" %>

<asp:Content runat="server" ContentPlaceHolderID="body">
    <asp:MultiView runat="server" ID="mv" ActiveViewIndex="0">
        <asp:View runat="server">
            <form method="get">
                <asp:Repeater runat="server" ID="rpCPList">
                    <ItemTemplate>
                        <label>
                            <input type="radio" name="cpId" value="<%#Eval("id") %>" /><%#Eval("full_name") %></label><br />
                    </ItemTemplate>
                </asp:Repeater>
                <input type="hidden" name="trone_id" value="<%#Request["trone_id"] %>" />
                <input type="hidden" name="returnUrl" value="<%#Request["returnUrl"] %>" />
                <input type="submit" value="下一步" />
            </form>

        </asp:View>
        <asp:View runat="server">
            <form id="form1" runat="server">
                <table class="datagrid">
                    <tr>
                        <th>SP指令:</th>
                        <td colspan="2">
                            <asp:Label runat="server" ID="lbTrone_Name" />
                        </td>

                    </tr>
                    <tr>
                        <th>渠道用户:</th>
                        <td colspan="2">
                            <asp:Label runat="server" ID="lbCpName" /></td>

                    </tr>
                    <tr>
                        <th>CP匹配指令:</th>
                        <td colspan="2">
                            <asp:TextBox ID="txtorder_num" runat="server" /><asp:CheckBox runat="server" ID="chkdynamic" Text="允许模糊" /></td>

                    </tr>


                    <tr>
                        <th class="auto-style1">同步地址:</th>
                        <td colspan="2" class="auto-style1">
                            <asp:RadioButtonList runat="server" ID="rblPush_Url_Id" RepeatDirection="Vertical" RepeatLayout="Flow" />
                        </td>
                    </tr>
                    <tr>
                        <th>指令名称:</th>
                        <td>
                            <asp:TextBox ID="txtorder_trone_name" runat="server" /></td>
                        <td>指令通道名称（和CP业务挂钩）</td>
                    </tr>
                    <tr>
                        <th colspan="2" class="tl">
                            <asp:CheckBox runat="server" ID="chkIsCustom" Text="使用单独扣量设置" onclick="custom_onclick(this,{});" /></th>
                        <td>默认情况使用同步URL的扣量设置</td>
                    </tr>
                    <tr>
                        <th>扣量比:</th>
                        <td>
                            <asp:TextBox ID="txtHold_precent" runat="server" /></td>
                        <td>0-90表示扣量百分比，-1表示使用同步URL上的扣量比</td>
                    </tr>
                    <tr>
                        <th>单日最大同步金额:</th>
                        <td>
                            <asp:TextBox ID="txtHold_amount" runat="server" /></td>
                        <td>单日最大同步金额，达到设置后，将停止数据同步,0表示无限制，-1表示使用同步URL上的设置</td>
                    </tr>
                    <tr>
                        <th>状态:</th>
                        <td>
                            <asp:CheckBox runat="server" ID="chkDisable" Text="停用" />
                        </td>
                        <td>停用后将停止同步符合条的记录</td>
                    </tr>

                </table>
                <input type="submit" value="确定" />
            </form>
        </asp:View>
    </asp:MultiView>
    <ul>
        <asp:Repeater runat="server" ID="rpUsing">
            <ItemTemplate>
                <li></li>
            </ItemTemplate>
        </asp:Repeater>
    </ul>


</asp:Content>
<asp:Content ID="Content1" runat="server" ContentPlaceHolderID="head">
    <style type="text/css">
        .auto-style1 { height: 36px; }
    </style>
    <script type="text/javascript">
        function custom_onclick(sender, e) {
            if (sender == null)
                sender = document.getElementById("chkIsCustom");
            document.getElementById("txtHold_precent").disabled = !sender.checked;
            document.getElementById("txtHold_amount").disabled = !sender.checked;
        }
        function page_onload() {
            custom_onclick(null, null);
        }
    </script>
</asp:Content>

