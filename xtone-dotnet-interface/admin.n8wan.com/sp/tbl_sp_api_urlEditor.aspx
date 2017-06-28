<%@ Page Title="同步URL配置" Language="C#" AutoEventWireup="true" CodeFile="tbl_sp_api_urlEditor.aspx.cs" Inherits="tbl_sp_api_url_Editor" ValidateRequest="false" MasterPageFile="~/MasterPage.master" %>

<asp:Content runat="server" ContentPlaceHolderID="head">
    <style type="text/css">
        input[type="text"] { width: 350px; }
        .sfields > td { margin: 0; padding: 0; }
        .sfields:hover { background-color: #fff; }
        .sfields table { border-style: none; border-right: 1px #ccc solid; }
        .sfields input[type="text"] { width: 100px; }
    </style>
    <script type="text/javascript">
        function frm_onsubmit(sender, e) {
            var obj = document.getElementById("ddlSp_id");
            if (obj.value == "" || obj.value == "0") {
                alert("请选择SP");
                return false;
            }
            obj = document.getElementById("txtMrStatus");
            if (obj.value == "") {
                obj = document.getElementById("txtMrFieldMap_status");
                if (obj.value == "") {
                    if (!confirm("当前同步没有配置MR同步状态，请确认SP是否只同步成功数据"))
                        return false;
                } else {
                    alert("配置了MR的同步，但未配置状态值");
                    return false;
                }
            }
            else {
                obj = document.getElementById("txtMrFieldMap_status");
                if (obj.value == "") {
                    alert("配置了状态值，但未配置状态值同步参数");
                    return false;
                }
            }

            return true;
        }

        function motomr_onclick(sender, e) {
            if (!sender.checked)
                return;
            var id = sender.id.replace(/^chkCpy_/, "");
            var mrId, moId;
            if (id == "price") {
                mrId = "txtMr_" + id;
                moId = "txtMr_" + id;
            }
            else {
                mrId = "txtMrFieldMap_" + id;
                moId = "txtMoFieldMap_" + id;
            }

            var mr = document.getElementById(mrId);
            if (mr.value != "") {
                alert("配置冲突!\nMR的" + sender.innerHTML + "同步已配置");
                return;
            }
            var mo = document.getElementById(moId);

            if (mo.value == "") {
                alert("配置错误!\nMO的" + sender.innerHTML + "同步未配置");
                return;
            }
        }


    </script>
</asp:Content>
<asp:Content runat="server" ContentPlaceHolderID="body">
    <form id="form1" runat="server" onsubmit="return frm_onsubmit(this,null);">
        <table class="datagrid">
            <tr>
                <th style="width: 110px;">SP:</th>
                <td colspan="2">
                    <asp:DropDownList runat="server" ID="ddlSp_id" DataTextField="short_name" DataValueField="id">
                        <asp:ListItem Value="0" Text="*请选择*" />
                    </asp:DropDownList>
                </td>
            </tr>

            <tr>
                <th class="red">同步名:</th>
                <td>
                    <asp:TextBox ID="txtName" runat="server" /></td>
                <td>同步地址备注信息</td>
            </tr>

            <tr>
                <th class="red">文件名:</th>
                <td>
                    <asp:TextBox ID="txtvirtual_page" runat="server" /></td>
                <td>文件名(不需要后缀)，将追加到同步URL上</td>
            </tr>

            <tr>
                <th>MO标记:</th>
                <td>
                    <asp:TextBox ID="txtMoCheck" runat="server" /></td>
                <td>检查是否为MO同步,格式:UrlField:Regex</td>
            </tr>

            <tr>
                <th>MoLink:</th>
                <td>
                    <asp:TextBox ID="txtMoLink" runat="server" /></td>
                <td>唯一订单号</td>
            </tr>

            <tr>
                <th class="red">MrLink:</th>
                <td>
                    <asp:TextBox ID="txtMrLink" runat="server" /></td>
                <td>唯一订单号。如果有MO同步，需要能MO的link对应。</td>
            </tr>



            <tr class="sfields">
                <td colspan="3">
                    <table>
                        <tr>
                            <th style="width: 109px;"></th>
                            <th>Mo UrlField</th>
                            <th>Mr UrlField</th>
                            <th></th>
                        </tr>

                        <tr>
                            <th>MMC字段：</th>
                            <td>
                                <asp:TextBox ID="txtMoFieldMap_mmc" runat="server" /></td>
                            <td>
                                <asp:TextBox ID="txtMrFieldMap_mmc" runat="server" /></td>
                            <td></td>
                        </tr>
                        <tr>
                            <th>IMSI字段：</th>
                            <td>
                                <asp:TextBox ID="txtMoFieldMap_imsi" runat="server" /></td>
                            <td>
                                <asp:TextBox ID="txtMrFieldMap_imsi" runat="server" /></td>
                            <td></td>
                        </tr>
                        <tr>
                            <th>IMEI字段：</th>
                            <td>
                                <asp:TextBox ID="txtMoFieldMap_imei" runat="server" /></td>
                            <td>
                                <asp:TextBox ID="txtMrFieldMap_imei" runat="server" /></td>
                            <td></td>
                        </tr>
                        <tr>
                            <th>手机字段：</th>
                            <td>
                                <asp:TextBox ID="txtMoFieldMap_mobile" runat="server" /></td>
                            <td>
                                <asp:TextBox ID="txtMrFieldMap_mobile" runat="server" /></td>
                            <td></td>
                        </tr>

                        <tr>
                            <th>端口字段：</th>
                            <td>
                                <asp:TextBox ID="txtMoFieldMap_ori_trone" runat="server" /></td>
                            <td>
                                <asp:TextBox ID="txtMrFieldMap_ori_trone" runat="server" /></td>
                            <td>填入“VirtualPort”可产生虚拟号码“3xxxxx”</td>
                        </tr>
                        <tr>
                            <th>指令字段：</th>
                            <td>
                                <asp:TextBox ID="txtMoFieldMap_ori_order" runat="server" /></td>
                            <td>
                                <asp:TextBox ID="txtMrFieldMap_ori_order" runat="server" /></td>
                            <td>填入“VirtualMsg”可产生虚拟指令“HT_3xxxxx_YYY”</td>
                        </tr>
                        <tr>
                            <th>透传字段：</th>
                            <td>
                                <asp:TextBox ID="txtMoFieldMap_cp_param" runat="server" /></td>
                            <td>
                                <asp:TextBox ID="txtMrFieldMap_cp_param" runat="server" /></td>
                            <td></td>
                        </tr>
                        <tr>
                            <th>业务号：</th>
                            <td>
                                <asp:TextBox ID="txtMoFieldMap_service_code" runat="server" /></td>
                            <td>
                                <asp:TextBox ID="txtMrFieldMap_service_code" runat="server" /></td>
                            <td></td>
                        </tr>
                        <tr>
                            <th>状态字段：</th>
                            <td>
                                <asp:TextBox ID="txtMoFieldMap_status" runat="server" Enabled="false" /></td>
                            <td>
                                <asp:TextBox ID="txtMrFieldMap_status" runat="server" /></td>
                            <td></td>
                        </tr>
                        <tr>
                            <th>IVR时长字段：</th>
                            <td>
                                <asp:TextBox ID="txtMoFieldMap_ivr_time" runat="server" /></td>
                            <td>
                                <asp:TextBox ID="txtMrFieldMap_ivr_time" runat="server" /></td>
                            <td></td>
                        </tr>

                    </table>
                </td>

            </tr>
            <tr>
                <th>MoToMr:</th>
                <td>
                    <asp:CheckBox ID="chkCpy_price" Text="价格" runat="server" onclick="motomr_onclick(this,{})" />
                    <asp:CheckBox ID="chkCpy_mobile" Text="手机号" runat="server" onclick="motomr_onclick(this,{})" />
                    <asp:CheckBox ID="chkCpy_ori_order" Text="指令" runat="server" onclick="motomr_onclick(this,{})" />
                    <asp:CheckBox ID="chkCpy_ori_trone" Text="端口" runat="server" onclick="motomr_onclick(this,{})" />
                </td>
                <td>收到MR时，需要从MO记录中复制的内容</td>
            </tr>

            <tr>
                <th>MR成功状态值:</th>
                <td>
                    <asp:TextBox ID="txtMrStatus" runat="server" /></td>
                <td>Mr同步时,可接收的状态值.格式,正则表达式</td>
            </tr>
            <tr>
                <th>传入MO价格：</th>
                <td>
                    <asp:TextBox ID="txtMo_price" runat="server" /></td>
                <td>SP同步回传价格，主要用于生成虚似指令。格式：UrlField,0（0:分/1:元/3:角/2:配对模式[传入值:分,传入值:分...])</td>
            </tr>

            <tr>
                <th>传入MR价格：</th>
                <td>
                    <asp:TextBox ID="txtMr_price" runat="server" /></td>
                <td>同上</td>
            </tr>
            <tr>
                <th>回应SP:</th>
                <td>
                    <asp:TextBox ID="txtMsgOutput" runat="server" /></td>
                <td>同步结果输出:格式:ok/error/existed</td>
            </tr>

            <tr>
                <th>SP服务器IP:</th>
                <td>
                    <asp:TextBox ID="txtsp_server_ips" runat="server" /></td>
                <td>SP服务器IP，用于IP鉴权。多个IP，用英文逗号分割</td>
            </tr>

            <tr>
                <th>其它:</th>
                <td>
                    <asp:CheckBox runat="server" ID="chkDisable" Text="停用" />
                    <asp:CheckBox runat="server" ID="chkphy_file" Text="真实文件" />

                </td>
                <td>&nbsp;</td>
            </tr>


        </table>
        <input type="submit" value="确定" />
    </form>
</asp:Content>
