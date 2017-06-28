/**
 * 该文件用于初始化编辑器
 * 如编辑器需要修改请直接在这里进行设置或者修改
 *
 * @Author Xinpow
 * @Copyright 2013
 *
 **/

// ------------ 插件 ------------
KindEditor.lang({
    replaceword: '查找/替换'
});
KindEditor.plugin('replaceword', function(K) {
    var self = this, name = 'replaceword';
    self.clickToolbar(name, function() {
        var frameHtml = '<div style="margin:10px;">';
        frameHtml += '<label>查找内容：';
        frameHtml += '<input type="text" name="rs_searchWord" id="rs_searchWord" style="width: 300px; border: none; border: 1px solid #AAA;" value="" /></label>';
        frameHtml += '<br /><br />';
        frameHtml += '<label>替换内容：';
        frameHtml += '<input type="text" name="rs_replaceWord" id="rs_replaceWord" style="width: 300px; border: none; border: 1px solid #AAA;" value="" /></label>';
        frameHtml += '<br />';
        frameHtml += '</div>';
        var dialog = K.dialog({
            width: 400,
            title: '查找 / 替换',
            body: frameHtml,
            closeBtn: {
                name: '关闭',
                click: function(e) {
                    dialog.remove();
                }
            },
            yesBtn: {
                name: '替换所有内容',
                click: function(e) {
                    var html = self.html();
                    var search = $('#rs_searchWord').val();
                    var replac = $('#rs_replaceWord').val();
                    if (search == '') {
                        alert('请输入 [查找内容]');
                        $('#rs_searchWord').focus();
                        return false;
                    }
                    search = search.replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/&/g, '&amp;');
                    search = search.replace(/\\/g, '\\\\').replace(/\(/g, '\\(').replace(/\)/g, '\\)').replace(/\[/g, '\\[').replace(/\]/g, '\\]').replace(/\{/g, '\\{').replace(/\}/g, '\\}');
                    var _ = new RegExp(search + '(?!([^<]*>))', "ig");
                    var newCont = html.replace(_, replac);
                    self.html(newCont);
                    alert('替换完成!');
                    dialog.remove();
                    return true;
                }
            },
            noBtn: {
                name: '取消',
                click: function(e) {
                    dialog.remove();
                }
            }
        });
    });
});
// ------------ 启动编辑器 ------------
var editor;
KindEditor.ready(function(K) {
    editor = K.create('.editorTextarea', {
        allowFileManager: true,
        afterBlur: function() {
            this.sync();
        },
        items: ['source', '|', 'replaceword', '|', 'undo', 'redo', '|', 'plainpaste', '|', 'selectall', 'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', '|', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript', 'superscript', 'formatblock', 'fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'removeformat', '|', 'image', 'multiimage', 'flash', 'media', 'insertfile', '|', 'table', 'hr', 'link', 'unlink', '|', 'lineheight', 'dearhtml', 'quickformat', 'template', 'fullscreen', 'preview'],
        filterMode: false,
        newlineTag: 'br'
    });
});