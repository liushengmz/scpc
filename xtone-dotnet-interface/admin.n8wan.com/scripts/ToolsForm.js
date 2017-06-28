
function ToolsFrm() { }
ToolsFrm.prototype._lastTick = null;
ToolsFrm.prototype.frameCSSName = "frm";//外框样式
ToolsFrm.prototype._frms = {};
ToolsFrm.prototype.onShowHide = null; //当显示隐藏工具页时
ToolsFrm.prototype.button = null;
ToolsFrm.prototype._onShowHide = function (frm, isHide) {
    if (this.onShowHide != null) {
        try { this.onShowHide(this, { frm: frm, isHide: isHide }); } catch (e) { alert(e.message); }
    }
    if (isHide)
        this.button = null;

}

ToolsFrm.prototype._onblur = function (iFocus) {
    if (!iFocus) {
        if (document.activeElement == this.button)
            return;
        if (this._lastTick != null) {
            var ts = (new Date()) - this._lastTick;
            if (ts < 100)
                return;
        }
    }
    var frm = this._frms["#"];
    if (frm == null)
        return;

    if (!iFocus && document.activeElement == this._frms["#"])
        return;
    frm.style.top = (-10 - frm.clientHeight) + "px";
    this._frms["#"] = null;
    this._onShowHide(frm, true);
}
ToolsFrm.prototype.OnFrameCreate = function (e) {
    e.innerHTML = "需要实现方法OnFrameCreate(e)";
}
ToolsFrm.prototype._CreateFrame = function (e) {
    var frm = this._frms[e] = document.createElement("div");
    frm.className = this.frameCSSName;
    document.body.appendChild(frm);
    this.OnFrameCreate({ cnt: frm, id: e });
    return frm;
}
ToolsFrm.prototype.Init = function () {//初始化
    var me = this;
    var fun = function () { me._onblur(false); }
    function OnEvent(d) {
        if (typeof d.attachEvent != "undefined")
            d.attachEvent("onclick", fun);
        else
            d.addEventListener("click", fun);
    }
    var win = window;

    while (win != null) {
        try {
            OnEvent(win.document);
        } catch (e) {
            break;
        }
        if (win.parent == win)
            break;
        win = win.parent;
    }
}
ToolsFrm.prototype.Hide = function (e) { this._onblur(false); }
ToolsFrm.prototype.ShowHide = function (e) {//显示隐藏
    this._lastTick = null;
    if (e == null || typeof e == "undefined") {
        this._onblur(true);
        return;
    }

    if (this._frms["#"] != null) {
        if (this._frms["#"] == this._frms[e.id]) {
            this._onblur(true);
            return;
        } else {
            this._onblur(true);
        }
    }

    this._lastTick = new Date();
    var frm = this._frms[e.id];
    if (frm == null)
        frm = this._CreateFrame(e.id);

    frm.style.display = "block";
    this._frms["#"] = frm;
    this.button = e.event;


    if (typeof e.event.getBoundingClientRect()) {
        var rct = e.event.getBoundingClientRect();
        var x, y;
        y = document.documentElement.offsetTop + rct.bottom;
        x = document.documentElement.offsetLeft + rct.left;// - (rct.right - rct.left) / 2 - frm.clientWidth / 2;
        frm.style.top = y + "px";
        frm.style.left = x + "px";
    }
    this._onShowHide(frm, false);




}
function ShowHideObject(win, isHide) {
    var doc = null;
    try {
        doc = win.document;
        var objs = doc.getElementsByTagName("OBJECT");
        for (var i = 0; i < objs.length; i++) {
            objs[i].style.visibility = isHide ? "hidden" : "visible";
        }
    } catch (e) { return; }
    var frms = doc.getElementsByTagName("iframe");
    for (var i = 0; i < frms.length; i++) {
        ShowHideObject(frms[i].contentWindow, isHide);
    }
}