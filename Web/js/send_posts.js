(function(){
    var title = $('.edit-header .titleFrame #title');
    var label = $('.labelFrame #label');
    var editBtn = $('.edit-header .buttonFrame .editBtn');


    // 初始化Vditor编辑器
    let vditor = new Vditor('vditor', {
        "height": 450,
        cache: {
            enable: false
        },
        toolbar: [
            "emoji",
            "headings",
            "bold",
            "italic",
            "strike",
            "link",
            "table",
            "|",
            "list",
            "ordered-list",
            "check",
            "outdent",
            "indent",
            "|",
            "quote",
            "line",
            "code",
            "inline-code",
            "insert-before",
            "insert-after",
            "|",
            "undo",
            "redo",
            "|",
            "fullscreen",
            "edit-mode"
        ]
    });

    editBtn.click(function() {
        addPosts(title.val(), vditor.getValue(), label.val());
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