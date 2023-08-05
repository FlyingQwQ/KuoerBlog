(function(){
    var title = $('.edit-header .titleFrame #title');
    var label = $('.labelFrame #label');
    var preview = $('.edit-content #preview');
    var editBtn = $('.edit-header .buttonFrame .editBtn');


    // 初始化ACE编辑器
    ace.require("ace/ext/language_tools");
    var editor = ace.edit("editor");
    editor.session.setMode("ace/mode/html");
    editor.setTheme("ace/theme/tomorrow");
    editor.getSession().setUseWrapMode(true);
    editor.setOptions({
        enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: true
    });


    let id = localStorage.getItem('modifyId');
    let removeBtn = $('.edit-header .buttonFrame .removeBtn');
    $.ajax({
        url: api + 'home/findPostsById?id=' + id,
        type: 'get',
        success: function(result) {
            let target = result.data;

            title.val(target.title);
            label.val(target.labelName);
            editor.setValue(target.content);
            preview.html(marked.parse(target.content));
            hljs.initHighlightingOnLoad();

            editor.clearSelection()
            $(editor).resize();

            // 加载评论
            loadComment();
        }
    });
    editBtn.click(function() {
        modifyPosts(id, title.val(), editor.getValue(), label.val());
    });
    removeBtn.click(function() {
        if(!confirm("你确定要删除这个帖子？删除后无法恢复.")) {
            return;
        }
        removePosts(id);
    });


    editor.getSession().on('change', function(e) {
        preview.html(marked.parse(editor.getValue()));
        hljs.initHighlightingOnLoad();
    });


    function modifyPosts(id, title, content, labelName) {
        $.ajax({
            url: api + 'home/modifyPosts',
            type: 'post',
            data: {
                id,
                title,
                content,
                labelName,
                token
            },
            success: function(target) {
                if(target.code == 1) {
                    loadItem('posts_manage');
                    alert('修改成功!');
                } else {
                    alert('修改失败!');
                }
            }
        });
    }

    function removePosts(id) {
        $.ajax({
            url: api + 'home/removePosts',
            type: 'get',
            data: {
                id,
                token
            },
            success: function(target) {
                if(target.code == 1) {
                    loadItem('posts_manage');
                    alert('删除成功!');
                } else {
                    alert('删除失败!');
                }
            }
        });
    }
})();