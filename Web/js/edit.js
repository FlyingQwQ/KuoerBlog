(function(){
    var title = $('.edit-header .titleFrame #title');
    var label = $('.labelFrame #label');
    var preview = $('.edit-content #preview');
    var edit = $('.edit-content #edit');
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


    var type = localStorage.getItem('editType');
    if(type == 'add') {
        // 发布新帖子
        editBtn.text('发布');
        editBtn.css({'display':'inline-block'});
        editBtn.click(function() {
            addPosts(title.val(), editor.getValue(), label.val());
        });
    } else if(type == 'modify') {
        // 修改现有的帖子
        let id = localStorage.getItem('modifyId');
        let removeBtn = $('.edit-header .buttonFrame .removeBtn');

        editBtn.text('确认修改');
        editBtn.css({'display':'inline-block'});
        removeBtn.css({'display':'inline-block'});
        // 加载帖子数据
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
            }
        });
        editBtn.click(function() {
            modifyPosts(id, title.val(), editor.getValue(), label.val());
        });
        removeBtn.click(function() {
            removePosts(id);
        });
    }

    editor.getSession().on('change', function(e) {
        preview.html(marked.parse(editor.getValue()));
        hljs.initHighlightingOnLoad();
    });



    function addPosts(title, content, labelName) {
        $.ajax({
            url: api + 'home/addPosts',
            type: 'post',
            data: {
                title,
                content,
                labelName,
                token
            },
            success: function(target) {
                if(target.code == 1) {
                    loadItem('posts_manage');
                    alert('发布成功!');
                } else {
                    alert('发布失败!');
                }
            }
        });
    }

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