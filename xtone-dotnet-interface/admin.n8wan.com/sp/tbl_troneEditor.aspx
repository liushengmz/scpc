<%@ Page Title="同步指令" Language="C#" AutoEventWireup="true" CodeFile="tbl_troneEditor.aspx.cs" Inherits="tbl_trone_Editor" ValidateRequest="false" MasterPageFile="~/MasterPage.master" %>

<asp:Content runat="server" ContentPlaceHolderID="head">
    <script type="text/javascript">
        var spurls = [];

        function url_onchange(sender, e) {
            if (sender == null)
                sender = document.getElementById("ddlUrlId");
            var id = parseInt(sender.value);
            var p = "000000" + id.toString();
            p = p.substr(p.length - 5);
            p = "3" + p;
            document.getElementById("spVPort").innerHTML = p;
            document.getElementById("spVMsg").innerHTML = "ht_" + p + "_xxx";
        }

        function page_onload() {
            url_onchange(null, null);
        }
    </script>
</asp:Content>
<asp:Content ContentPlaceHolderID="body" runat="server">
    <form id="form1" runat="server">
        <table class="datagrid">
            <tr>
                <th>SP用户:</th>
                <td>
                    <asp:DropDownList runat="server" ID="ddlSpId" DataTextField="full_name" DataValueField="id" AutoPostBack="true" OnSelectedIndexChanged="ddlSpId_SelectedIndexChanged">
                        <asp:ListItem Value="0" Text="*请选择*" />
                    </asp:DropDownList>
                </td>
                <td>tbl_sp表主键</td>
            </tr>

            <tr>
                <th>同步URL:</th>
                <td>
                    <asp:DropDownList runat="server" ID="ddlUrlId" DataTextField="name" DataValueField="id" onchange="url_onchange(this,this.value)">
                        <asp:ListItem Value="0" Text="*请选择*" />
                    </asp:DropDownList>

                </td>
                <td><a href="tbl_sp_api_urlEditor.aspx" class="blue" onclick="return !window.open(this.href+'?spid='+document.getElementById('ddlSpId').value,'_self')">添加新回调API</a>
                </td>
            </tr>

            <tr>
                <th>业务名称:</th>
                <td>
                    <asp:TextBox ID="txttrone_name" runat="server" /></td>
                <td></td>
            </tr>
            <tr>
                <th>通道号:</th>
                <td>
                    <asp:TextBox ID="txttrone_num" runat="server" /></td>
                <td>内部虚似通道号通常为：<span id="spVPort">3000XX</span>,虚似指令通常为：<span id="spVMsg">ht_3000xx_xxx</span></td>
            </tr>
            <tr>
                <th>指令:</th>
                <td>
                    <asp:TextBox ID="txtorders" runat="server" />
                    <asp:CheckBox runat="server" ID="chkdymaic" Text="模糊" />

                </td>
                <td>指令集模糊部分用(*或?)代替,*表示无限位，?表示一位</td>
            </tr>
            <tr>
                <th>特殊指令:</th>
                <td>
                    <asp:CheckBox ID="chkmatch_price" runat="server" Text="无规则指令" /></td>
                <td>无规则指令，将直接配对URL传入价格，不配对指令</td>
            </tr>
            <tr>
                <th>价格:</th>
                <td>
                    <asp:TextBox ID="txtPrice" runat="server" /></td>
                <td class="red">单价：元</td>
            </tr>

            <tr>
                <th>状态:</th>
                <td>
                    <asp:CheckBox runat="server" ID="chkstatus" Text="停用" />
                </td>
                <td>状态，1为正常，0为停用</td>
            </tr>

        </table>
        <input type="submit" value="确定" name="action" />
    </form>
</asp:Content>
