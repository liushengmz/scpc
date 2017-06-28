<%@ Page Title="SP同步地址列表" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="tbl_sp_api_urlList.aspx.cs" Inherits="sp_tbl_sp_api_urlList" %>

<script runat="server">
    string urlPfx;
    protected override void OnLoad(EventArgs e)
    {
        urlPfx = n8wan.Public.Library.syncUrlPerfix();
        base.OnLoad(e);
    }
        
</script>
<asp:Content runat="server" ContentPlaceHolderID="head">
    <style type="text/css">
        .del { text-decoration: line-through; }
    </style>

</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="Server">
    <form method="get">
        <select name="spId">
            <option value="" selected="selected">*全部*</option>
            <asp:Repeater runat="server" ID="rpSp">
                <ItemTemplate>
                    <option value="<%#Eval("id") %>"><%#Eval("short_name") %></option>
                </ItemTemplate>
            </asp:Repeater>
        </select>
        <input type="submit" value="确定" />
        <a href="tbl_sp_api_urlEditor.aspx?spid=<%#Request["id"] %>" class="blue">添加同步</a>
    </form>


    <asp:Repeater runat="server" ID="rpLst">
        <HeaderTemplate>
            <table class="datagrid">
                <tr>
                    <th>ID</th>
                    <th>SP</th>
                    <th>备注名</th>
                    <th>URL</th>
                    <th>操作</th>
                </tr>
        </HeaderTemplate>
        <ItemTemplate>
            <tr <%#(bool)Eval("Disable")?"class='del'":"" %>>
                <td><%#Eval("id","{0}") %></td>
                <td><%#GetSpName((int)Eval("sp_id")) %></td>
                <td><%#Eval("name") %></td>
                <td><%#urlPfx %><%#Eval("urlPath") %></td>
                <td>
                    <a href="tbl_sp_api_urlEditor.aspx?id=<%#Eval("id") %>">编辑</a>
                    <a href="tbl_troneList.aspx?urlId=<%#Eval("id") %>">指令列表</a>
                    <%-- <a href="del_sp_api_url.aspx?spid=<%#Eval("id") %>">删除</a>--%>
                </td>
            </tr>
        </ItemTemplate>
        <FooterTemplate>
            </table>
        </FooterTemplate>
    </asp:Repeater>
</asp:Content>

