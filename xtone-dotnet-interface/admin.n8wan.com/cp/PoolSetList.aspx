<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="false" %>

<script runat="server">
    protected override void OnInit(EventArgs e)
    {
        var m = Request["mode"];
        if (string.IsNullOrEmpty(m))
        {
            if ("full".Equals(m, StringComparison.OrdinalIgnoreCase))
                Response.Cookies["viewmodel"].Expires = new DateTime(1999, 9, 9);
            else
                Response.Cookies["viewmodel"].Value = m;
        }
        base.OnInit(e);
    }
</script>

<asp:Content ContentPlaceHolderID="head" runat="Server">
    <script src="../scripts/jquery-3.1.1.js"></script>
    <script src="../scripts/ToolsForm.js"></script>
    <script src="../scripts/provices.js"></script>
    <style type="text/css">
        .del td { color: #666; }
        .tooltip { position: absolute; max-width: 600px; max-height: 600px; overflow-y: auto; border: 1px dotted #ffd800; background: #fffef7; }
            .tooltip p { margin: 5px; padding: 0; font-size: 1em; border-bottom: 1px dotted #666; min-height: 3em; margin-bottom: 2px; padding-bottom: 2px; }
                .tooltip p::first-letter { font-size: 2.8em; line-height: 1em; margin: 5px 5px 0 0; color: #ff6a00; float: left; display: block; }
                .tooltip p:last-child { border-bottom-style: none; margin-bottom: 0; }
                .tooltip p label { margin-right: 0.5em; white-space: nowrap; }
                    .tooltip p label:hover { color: #f00; }
                    .tooltip p label:last-child { clear: both; }

        .td_province { position: relative; width: 200px; }
            .td_province .pool_province { max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
                .td_province .pool_province:hover { position: absolute; top: 0; left: 0; max-width: inherit; border: 1px solid #ff6a00; background-color: #fff7e4; padding: 5px; margin: -1px 0 0 -1px; }
    </style>


    <script type="text/javascript">
        var data = [];
        var org_pooldata = [];
        var pooldata = [];
        var provindes = [];
        var curPage = 1;
        var pageSize = 20;
        $(document).ready(function () {
            $.getJSON("../ajax/getCpPinYin.ashx", null, function (e) {
                data = e;
                $.getJSON("../ajax/pool/getCpPool.ashx", { "ticks": new Date().getTime() }, function (e) {
                    org_pooldata = pooldata = e;
                    InitList();
                    var ids = "";
                    for (var i = 0; i < pooldata.length; i++)
                        ids += "," + pooldata[i].id;

                    $.getJSON("../ajax/pool/getPoolProvices.ashx?ids=" + ids, null, function (e) { provindes = e; InitProvinces() });
                });
            });
        });

        var toolFrm = new ToolsFrm();
        toolFrm.frameCSSName = "tooltip";
        toolFrm.Init();

        toolFrm.onShowHide = function (sender, e) { }

        toolFrm.OnFrameCreate = function (e) {
            e.cnt.innerHTML = "Loading...";
        }


        function searchAndShow(cnt, keyword) {
            var group = [];
            var items = [];

            for (var i = 0; i < data.length; i++) {
                var item = data[i];
                if (keyword != null && keyword != "" && item.key.indexOf(keyword.toUpperCase()) == -1 && item.name.indexOf(keyword) == -1)
                    continue;
                var k = item.key.substring(0, 1);
                items = group[k];
                if (items == null) {
                    items = [];
                    group[k] = items;
                }
                items.push(item);
            }
            var html = "";
            for (var k in group) {
                html += "<p>" + k;
                var items = group[k];
                for (var i = 0; i < items.length; i++) {
                    var item = items[i];
                    html += "<label onclick='item_onclick(this,{id:" + item.id + ",name:\"" + item.name + "\"});' >" + item.name + "</label> ";
                }
                html += "</p>";
            }
            if (html == "")
                html = "<p>无匹配项</p>";
            cnt.innerHTML = html;

        }

        function txt_onfocus(sender, e) {
            var hTime = setTimeout(function () {
                clearTimeout(hTime);
                hTime = null;
                if (toolFrm.button != null)
                    return;
                sender.select();
                toolFrm.ShowHide({ event: sender });
                txt_onkeyup(null, "");
            }, 100);
        }

        function txt_onblur(sender, e) {
            //var hTime = setTimeout(function () { clearTimeout(hTime); toolFrm.ShowHide(null); }, 100);

            var label = document.getElementsByClassName("tooltip")[0].getElementsByTagName("label");
            if (label.length == 1)
                label[0].click();

            var id = parseInt(document.getElementById(sender.getAttribute("data-name")).value);
            if (isNaN(id))
                return;
            for (var i = 0; i < data.length; i++) {
                if (data[i].id == id) {
                    tooltip_save(sender, data[i]);
                    return;
                }
            }
            sender.value = "";
        }

        function txt_onkeyup(sender, e) {
            searchAndShow(document.getElementsByClassName("tooltip")[0], e == null ? sender.value : e);
        }
        function item_onclick(sender, e) {
            tooltip_save(toolFrm.button, e);
            setTimeout(function () { toolFrm.ShowHide(null); }, 100);
        }
        function tooltip_save(cnt, e) {
            cnt.value = e.name;
            document.getElementById(cnt.getAttribute("data-name")).value = e.id;
        }

        function appendFilter() {
            var cpid = parseInt(document.getElementById("cpid").value);
            if (isNaN(cpid))
                cpid = 0;
            pooldata = [];
            for (var i = 0; i < org_pooldata.length  ; i++) {
                if (cpid != 0 && cpid != org_pooldata[i].cp_id)
                    continue;
                pooldata.push(org_pooldata[i]);
            }
        }


        function InitList() {

            var lst = document.getElementById("lst");
            var tab = document.createElement("table");
            tab.className = "datagrid";

            tab.appendChild(tr = document.createElement("tr"));
            tr.appendChild(document.createElement("th")).textContent = "PoolId";
            tr.appendChild(document.createElement("th")).textContent = "名称";
            tr.appendChild(document.createElement("th")).textContent = "CP";
            tr.appendChild(document.createElement("th")).textContent = "创建日期";
            tr.appendChild(document.createElement("th")).textContent = "价格";
            tr.appendChild(document.createElement("th")).textContent = "省份";
            tr.appendChild(document.createElement("th")).textContent = "操作";
            var pSize = pageSize;

            for (var i = (curPage - 1) * pSize; i < pooldata.length && pSize > 0; i++) {
                pSize--;
                var item = pooldata[i];
                var cpInfo = null;
                tab.appendChild(row = document.createElement("tr"));

                row.appendChild(document.createElement("td")).textContent = "P" + (100000 + item.id).toString().substring(1);
                row.appendChild(document.createElement("td")).textContent = item.name;

                row.appendChild(cell = document.createElement("td")).textContent = (2000 + item.cp_id);
                for (var x = 0; x < data.length; x++) {
                    if (data[x].id == item.cp_id) {
                        cpInfo = data[x];
                        cell.textContent = data[x].name;
                        break;
                    }
                }

                row.appendChild(document.createElement("td")).textContent = item.create_date;
                row.appendChild(document.createElement("td")).textContent = item.fee / 100;
                row.appendChild(cell = document.createElement("td")).innerHTML = "<div class='pool_province' id='p_" + item.id + "' data-poolId='" + item.id + "' >Loaing...</div>";
                cell.className = "td_province";

                row.appendChild(cell = document.createElement("td"));

                cell.appendChild(lnk = document.createElement("a"));
                lnk.onclick = new Function("switch_onclick(this,{'id':" + item.id + "});");
                lnk.href = "javascript:;";
                if (item.status) {
                    lnk.text = "停用";
                    lnk.title = "当前状已启用";
                    lnk.style.color = "red";
                }
                else {
                    lnk.text = "启用";
                    lnk.title = "当前状已停用";
                    lnk.style.color = "blue";
                    row.className = "del";
                }


                cell.appendChild(document.createTextNode(" "));

                cell.appendChild(lnk = document.createElement("a"));
                lnk.href = "PoolSetEditor.aspx?id=" + item.id;
                lnk.text = "增加通道";

                cell.appendChild(document.createTextNode(" "));

                cell.appendChild(lnk = document.createElement("a"));
                lnk.href = "PoolSetPriorityEditor.aspx?id=" + item.id;
                lnk.text = "优先级";

                cell.appendChild(document.createTextNode(" "));
                cell.appendChild(lnk = document.createElement("a"));
                lnk.href = "CpSyncTest.ashx?id=" + item.id;
                lnk.text = "模似同步";
                lnk.target = "_blank";

                cell.appendChild(document.createTextNode(" "));
                cell.appendChild(lnk = document.createElement("a"));
                lnk.href = "exportPoolInfo.ashx/" + cpInfo.name + "_" + item.name + ".txt?id=" + item.id;
                lnk.text = "导出";
                lnk.target = "_blank";

            }
            tab.appendChild(tr = document.createElement("tr"));
            tr.appendChild(cell = document.createElement("td"));
            cell.colSpan = 7;

            cell.appendChild(lnk = document.createElement("a"));
            lnk.onclick = new Function("pager_onclick(this,{\"step\":-1});");
            lnk.innerHTML = "上一页";
            lnk.href = "javascript:;";
            cell.appendChild(document.createTextNode(" "));

            cell.appendChild(lnk = document.createElement("a"));
            lnk.onclick = new Function("pager_onclick(this,{\"step\":+1});");
            lnk.innerHTML = "下一页";
            lnk.href = "javascript:;";


            while (lst.firstChild != null)
                lst.removeChild(lst.firstChild);
            lst.appendChild(tab);
            if (provindes.length != 0)
                InitProvinces();

        }

        function InitProvinces() {
            $(".pool_province").each(function (idx, e) {
                var key = e.getAttribute("data-poolId");
                var val = provindes[key];
                if (typeof val == "undefined")
                    e.innerHTML = "N/A";
                else
                    e.innerHTML = provindes[key].length.toString() + ":" + new Provinces().getNamesByIds(provindes[key]);
            })
        }

        function switch_onclick(sender, e) {
            var item = null;
            pooldata.forEach(function (t) {
                if (t.id == e.id) {
                    item = t;
                    return false;
                }
            });
            if (item == null) {
                alert("信息丢失！");
                return;
            }
            $.getJSON("../ajax/pool/setPoolStatus.ashx?id=" + e.id + "&status=" + (item.status ? "0" : "1"), null, function (r) {
                item.status = r.status;
                InitList();
            })
        }

        function pager_onclick(sender, e) {
            var max = parseInt(pooldata.length / pageSize) + (pooldata.length % pageSize != 0 ? 1 : 0);
            if (e.step == 0) {
                curPage = 1;
                appendFilter();
            }
            else
                curPage += e.step;
            if (curPage > max) {
                alert("已是最后一页");
                curPage = max;
                return;
            } else if (curPage < 1) {
                alert("已是第一页");
                curPage = 1;
                return;
            }
            InitList();

        }
    </script>
</asp:Content>
<asp:Content ContentPlaceHolderID="body" runat="Server">
    <form action="../ajax/pool/getCpPool.ashx" onsubmit="pager_onclick(this,{'step':0});return false;">
        <input name="cpid" id="cpid" type="hidden" />
        <input autocomplete="off" style="ime-mode: disabled;" size="50" data-name="cpid" required="required" onfocus="txt_onfocus(this, null)"
            onclick="txt_onfocus(this, null)" onblur="txt_onblur(this,null)" onkeyup="txt_onkeyup(this, null)" />
        <input type="submit" value="查找" />
        <a href="CreatePoolSet.aspx" onclick="window.open(this.href+'#cpid='+$('#cpid').val(),'_self');return false;">创建新池</a>
    </form>
    <div id="lst">
        Loading...
    </div>

</asp:Content>

