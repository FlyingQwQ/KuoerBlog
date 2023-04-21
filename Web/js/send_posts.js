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


    editBtn.click(function() {
        addPosts(title.val(), editor.getValue(), label.val());
    });

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

})();