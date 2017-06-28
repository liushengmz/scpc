<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="imsi2area.aspx.cs" Inherits="imsi2area" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
    <script type="text/javascript" src="http://bbs.gkong.com/scripts/ajax.js"></script>

    <script type="text/javascript">
        var provices = [];
        var citys = [];
        function frm_onsubmit(sender, e) {
            var imsi = sender["imsi"].value;
            if (imsi.length != 15)
                return false;

            var li = document.getElementById("i" + imsi);
            if (li != null) {

                return;
            }

            var ajax = new ajax2();
            var xml = ajax.POST(null, sender, true);
            var rlt = simpleXMlParse(xml);
            if (rlt.state != "ok") {
                alert(rlt.message);
                return;
            }
            var as = rlt.message.split(",");
            li = document.createElement("li");
            var ul = document.getElementById("rlt");
            li.innerHTML = imsi + ":" + as[0] + "xxxx - " + provices[parseInt(as[1])] + " " + citys[parseInt(as[2])];
            li.id = "i" + imsi;
            ul.insertBefore(li, ul.firstChild);
        }

    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <form method="post" action="ajax/imsi2area.ashx" onsubmit="frm_onsubmit(this,{});return false;">
        IMSI:<input type="text" name="imsi" />
        <input type="submit" value="查询" />
    </form>
    <ol id="rlt">
    </ol>
</asp:Content>

