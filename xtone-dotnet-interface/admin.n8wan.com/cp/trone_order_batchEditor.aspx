<%@ Page Language="C#" AutoEventWireup="true" CodeFile="trone_order_batchEditor.aspx.cs" Inherits="trone_order_batchEditor" MasterPageFile="~/MasterPage.master" %>

<asp:Content runat="server" ContentPlaceHolderID="head">
    <script type="text/javascript">
        function page_onload() {
            var cpidcnt = document.getElementById("rblCpId");
            var inputs = cpidcnt.getElementsByTagName("input");
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].onchange = new Function("cpId_onclick(this,this.vale);");
            }
        }

        function cpId_onclick(sender, e) {
            var urlsCnt = document.getElementById("urlsCnt");
            urlsCnt.innerHTML = "loading...";
            var url = "/ajax/GetCpPushURL.ashx?callback=push_url_callback&id=" + sender.value;
            var s = document.createElement("script");
            s.src = url;
            document.body.appendChild(s);
        }

        function push_url_callback(e) {
            var urlsCnt = document.getElementById("urlsCnt");
            var html = "";
            for (var i = 0; i < e.length; i++) {
                html += "<label><input type='radio' name='url_id' value='"
                    + e[i][0] + "'/>" + e[i][1] + " - " + e[i][2] + "</label>";
            }
            urlsCnt.innerHTML = html;

        }

        function frm_onsubmit(sender, e) {
            var urlId = document.getElementsByName("url_id");
            if (urlId.length == 0) {
                alert("请选择同步地址");
            }
        }

    </script>
    <style type="text/css">
        #rblCpId { margin: 0; padding: 0; list-style: none; }
            #rblCpId li { display: inline-block; min-width: 100px; }
        #urlsCnt label { display: block; }
        th { white-space: nowrap; }
    </style>
</asp:Content>
<asp:Content runat="server" ContentPlaceHolderID="body">
    <form runat="server" onsubmit="frm_onsubmit(this,{});">
        <table class="datagrid">

            <tr>
                <th>渠道：</th>
                <td>
                    <asp:RadioButtonList runat="server" ID="rblCpId" RepeatLayout="OrderedList" />
                </td>
            </tr>

            <tr>
                <th>同步地址：</th>
                <td id="urlsCnt"></td>
            </tr>
            <asp:Repeater runat="server" ID="rpTrone">
                <ItemTemplate>
                    <tr>
                        <th>SP指令：</th>
                        <th style="text-align: left;"><%#Eval(LightDataModel.tbl_troneItem.Fields.trone_name) %>(<%#Eval(LightDataModel.tbl_troneItem.Fields.orders) %>) - <%#Eval(LightDataModel.tbl_troneItem.Fields.price) %> </th>
                    </tr>
                    <tr>
                        <th>CP指令名称:</th>
                        <td>
                            <input name="CPTroneName_<%#Eval( LightDataModel.tbl_troneItem.Fields.id) %>"
                                value="<%#Eval(LightDataModel.tbl_troneItem.Fields.trone_name) %> - <%#Eval(LightDataModel.tbl_troneItem.Fields.price) %>" />
                        </td>
                    </tr>
                    <tr>
                        <th>CP匹配指令:</th>
                        <td>
                            <input name="CPTroneOrder_<%#Eval( LightDataModel.tbl_troneItem.Fields.id) %>" value="<%#Eval(LightDataModel.tbl_troneItem.Fields.orders) %>" />
                            <label>
                                <input type="checkbox" value="1" name="dynamic_<%#Eval( LightDataModel.tbl_troneItem.Fields.id) %>"
                                    <%#(bool)Eval(LightDataModel.tbl_troneItem.Fields.is_dynamic)?"":"disabled='disabled'" %> />允许模糊</label>
                        </td>
                    </tr>
                </ItemTemplate>
            </asp:Repeater>
        </table>
        <input type="submit" value="确定" />
    </form>
</asp:Content>
