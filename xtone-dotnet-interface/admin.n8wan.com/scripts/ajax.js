// JavaScript Document
// Shotgun Ajax Module
// last modeify date:2013-3-19

function loadXMLDoc(url, iXML, postdata, CallBack) {
    var req = null, msg;
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
    }
    var Asyn = CallBack == null ? false : true;


    if (postdata == null)
        req.open("GET", url, Asyn);
    else {
        req.open("POST", url, Asyn);
        req.setRequestHeader("CONTENT-TYPE", "application/x-www-form-urlencoded");
    }
    req.setRequestHeader("If-Modified-Since", "Thu, 01 Jan 1970 00:00:00 GMT");
    req.setRequestHeader("Programer", "Jawin");
    if (Asyn) {
        req.onreadystatechange = function () {
            if (req.readyState != 4)
                return;
            if (iXML)
                CallBack(req.responseXML, req.status);
            else
                CallBack(req.responseText, req.status)
        }
        req.send(postdata);
        return true;
    }
    req.send(postdata);
    if (iXML)
        msg = req.responseXML;
    else
        msg = req.responseText;


    return msg;
}

function GetHtml(url) {
    var dt = new Date;
    var htmlCode = loadXMLDoc(url, false, null, null);
    return htmlCode;;
}
function AsynGetHtml(url, funCallBack) {
    return loadXMLDoc(url, false, null, funCallBack);
}
function AsynGetXML(url, funCallBack) {
    var dt = new Date;
    var xml = loadXMLDoc(url, true, null, funCallBack);
    return xml;;
}
function GetXML(url) {
    var dt = new Date;
    var xml = loadXMLDoc(url, true, null, null);
    return xml;;
}


function GetXMLPost(url, postData) {
    var dt = new Date;
    var xml = loadXMLDoc(url, true, postData, null);
    return xml;;
}

function simpleXMlParse(xml) {

    var Result = new Object();
    Result.OwnerXML = xml;
    Result.state = null;
    Result.message = null;
    Result.ExtXml = null;
    Result.DataArray = new Array();

    var root = null;
    var el = null;
    if (xml == null || (root = xml.documentElement) == null) {
        Result.state = 'XMLError';
        Result.message = '\u670D\u52A1\u54CD\u5E94\u7684XML\u6587\u4EF6\u683C\u5F0F\u9519\u8BEF!\n' +
                         '\u9519\u8BEF\u539F\u56E0\u53EF\u80FD\u662F\u670D\u52A1\u5668\u5904\u7406\u7A0B\u5E8F\u51FA\u9519\u6216\u7E41\u5FD9!';
        return Result;
    }
    //alert(root);
    for (var i = 0; i < root.childNodes.length; i++) {
        el = root.childNodes[i];
        switch (el.nodeName) {
            case 'state':
                {
                    if (el.childNodes.length)
                        Result.state = el.childNodes[0].data;
                    break;
                }
            case 'message':
                {
                    if (el.childNodes.length)
                        Result.message = el.childNodes[0].data;
                    break;
                }
            case "ExtInfo":
                Result.ExtXml = el;
                break;
            case "DataArray":
                for (var x = 0; x < el.childNodes.length; x++) {
                    if (el.childNodes[x].nodeType != 1)
                        continue;
                    if(el.childNodes[x].childNodes.length>0)
                        Result.DataArray.push(el.childNodes[x].childNodes[0].data);
                    else
                        Result.DataArray.push(null);
                }
                break;
            default:
        }
    }
    el = null;
    root = null;
    return Result;
}

//选定操作项 0取消所以选定 1全部选定 -1反选
function CheckerSelect(name, flag) {
    var objs = document.getElementsByName(name);
    var obj;
    for (var i = 0; i < objs.length; i++) {
        obj = objs[i];
        if (obj.tagName != "INPUT" || obj.type != "checkbox")
            continue;
        if (flag == 0)
            obj.checked = false;
        else if (flag > 0)
            obj.checked = true;
        else
            obj.checked = !obj.checked;
    }
}

function cbool(v) {
    var i = parseInt(v);
    if (!isNaN(i)) {
        return i != 0;
    }
    v = v.toLowerCase();
    switch (v) {
        case 'true':
        case 't':
            return true;
        default:
            return false;
    }
    return false;
}

var DialogResult = { OK: 1, Cancel: 0, YES: 1, NO: 0 };


//页面alert()
function Alert() {

    this.frmDiv = null;
    this.width = 350;
    this.height = 100;
    this.hTimer = null;

    this.onscroll = function () {
        this.frmDiv.style.left = (document.documentElement.clientWidth - this.width) / 2;
        this.frmDiv.style.top = (document.documentElement.clientHeight - this.height) / 2;
    };

    this.show = function (title, text) {
        if (this.frmDiv == null) {//创建提示框主体
            this.frmDiv = document.createElement("DIV");
            document.body.appendChild(this.frmDiv);

            this.frmDiv.className = "alert"; //已经定义好的css样式:wait

            var obj = document.createElement("DIV");
            this.frmDiv.appendChild(obj);
            obj.className = "title";
            obj.innerHTML = title;

            obj = document.createElement("DIV");
            this.frmDiv.appendChild(obj);
            obj.className = "Message";
            obj.innerHTML = text;

            obj = document.createElement("CENTER");
            this.frmDiv.appendChild(obj);

            var bt = document.createElement("BUTTON");
            obj.appendChild(bt);
            bt.className = "OKButton";
            bt.innerHTML = "\u786E\u5B9A";

            var _self = this;

            bt.onclick = function () { _self.dispose(1) };

            this.hTimer = window.setInterval(function () { _self.onscroll(); }, 10);
        }
    };
    this.dispose = function (result) {
        if (this.hTimer != null)
            window.clearInterval(this.hTimer);
        this.hTimer = null;

        if (this.frmDiv != null)
            document.body.removeChild(this.frmDiv);
        this.frmDiv = null;

        if (this.pfUnloadCallBack != null) {

            try {
                this.pfUnloadCallBack(this, result);
            }
            catch (e) {
                alert("Unload\u4E8B\u4EF6\u56DE\u8C03\u4EE3\u51FD\u51FA\u9519\n" + e.message);
            }

        }
        this.pfUnloadCallBack = null;


    }
    this.pfUnloadCallBack = null;
}

//<div class="alert">
//	<div class="title"></div>
//	<div class="Message"></div>
//	<center><button class="OKButton">确认</button></center>
//</div>

// Example:
// writeCookie("myCookie", "my name", 24);
// Stores the string "my name" in the cookie "myCookie" which expires after 24 hours.
function writeCookie(name, value, hours) {
    var expire = "";
    if (hours != null) {
        expire = new Date((new Date()).getTime() + hours * 3600000);
        expire = "; expires=" + expire.toGMTString();
    }
    document.cookie = name + "=" + escape(value) + expire;
}

// Example:
// alert( readCookie("myCookie") );
function readCookie(name) {
    var cookieValue = "";
    var search = name + "=";
    if (document.cookie.length > 0) {
        offset = document.cookie.indexOf(search);
        if (offset != -1) {
            offset += search.length;
            end = document.cookie.indexOf(";", offset);
            if (end == -1) end = document.cookie.length;
            cookieValue = unescape(document.cookie.substring(offset, end))
        }
    }
    return cookieValue;
}


function InstallTextSelecter() {
    var objs = document.getElementsByTagName("INPUT");
    for (var i = 0; i < objs.length; i++) {
        if (objs[i].type == "text") {
            objs[i].onfocus = new Function("this.select();");
        }
    }
}

function FieldCollector(frm, withHidden) {
    var Els = frm;
    var PostData = "";
    if (withHidden == null)
        withHidden = false;
    for (var i = 0; i < Els.length; i++) {
        switch (Els[i].tagName) {
            case "INPUT":
            case "SELECT":
            case "TEXTAREA":
                break;
            default:
                continue;
        }
        switch (Els[i].type) {
            case "hidden":
                if (!withHidden)
                    continue;
                break;
            case "submit":
            case "button":
                continue;
            case "checkbox":
            case "radio":
                if (!Els[i].checked)
                    continue;
        }
        if (Els[i].name == "")
            continue;
        PostData += "&";
        PostData += encodeURIComponent(Els[i].name) + "=" + encodeURIComponent(Els[i].value);
    }
    return PostData.substring(1);

}
//替换系统原来的close代码
var WindowClose = window.close;
window.close = function () {

    if (!(arguments.length == 1 && arguments[0] == 'true'))
        window.open('', '_self', '');
    WindowClose();
}
function ajax2() {

}
ajax2.prototype.onload = null;
ajax2.prototype.cache = true;
ajax2.prototype._core = function (e) {
    var req = null, msg;
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
    }
    else
        throw "\u6D4F\u89C8\u5668\u4E0D\u652F\u6301ajax";

    var Asyn = (typeof (e.onload) == "undefined" || e.onload == null) ? false : true;
    var postdata = (typeof (e.data) == "undefined") ? null : e.data;

    if (postdata == null)
        req.open("GET", e.url, Asyn);
    else {
        req.open("POST", e.url, Asyn);
        req.setRequestHeader("CONTENT-TYPE", "application/x-www-form-urlencoded");
    }
    if (typeof (e.cache) == "undefined" || e.cache != true)
        req.setRequestHeader("If-Modified-Since", "Thu, 01 Jan 1970 00:00:00 GMT");

    req.setRequestHeader("Programer", "Jawin");
    if (Asyn) {
        req.onreadystatechange = function () {
            if (req.readyState != 4)
                return;
            if (typeof (e.xml) != "undefined" && e.xml)
                e.onload(req.responseXML, req.status);
            else
                e.onload(req.responseText, req.status)
        }
        req.send(postdata);
        return true;
    }
    req.send(postdata);
    if (typeof (e.xml) != "undefined" && e.xml)
        msg = req.responseXML;
    else
        msg = req.responseText;

    return (msg);
}


ajax2.prototype._getdata = function (da) {
    switch (typeof (da)) {
        case "string":
            return da;
        case "object":
            if (typeof (da.tagName) == "undefined" && da.tagName != "FORM")
                throw "dataObj\u7C7B\u578B\u4E0D\u80FD\u8BC6\u522B\uFF01";
            return FieldCollector(da, true);
        default:
            throw "dataObj\u7C7B\u578B\u4E0D\u80FD\u8BC6\u522B\uFF01";
    }
}
ajax2.prototype.POST = function (url, dataObj, iXML) {
    if (url == null) {
        if (typeof dataObj.tagName != "undefined" && dataObj.tagName == "FORM")
            url = dataObj.action;
        if (url == null || url == "")
            url = document.URL;
    }
    var postData;
    if (dataObj == null)
        postData == "";
    else
        postData = this._getdata(dataObj);

    var rx = /(^|&)ajax=/i;
    if (!rx.test(url) && !rx.test(postData)) {
        if (postData == "")
            postData = "ajax=1";
        else
            postData = "ajax=1&" + postData;
    }
    return this._core({ url: url, data: postData, onload: this.onload, xml: iXML, cache: this.cache });
}
ajax2.prototype.GET = function (url, iXML) {
    if (url == null)
        url = document.URL;
    if ((typeof url == "object") && url.tagName == "FORM") {
        var ptr = FieldCollector(url, true);
        url = url.action;
        var i = url.indexOf("?");
        if (i != -1)
            url = url.substring(0, i);
        url += "?" + ptr;
    }
    var rx = /(^|&)ajax=/i;
    if (!rx.test(url)) {
        if (url.indexOf("?") == -1)
            url += "?ajax=1";
        else
            url += "&ajax=1";
    }
    return this._core({ url: url, data: null, onload: this.onload, xml: iXML, cache: this.cache });
}
