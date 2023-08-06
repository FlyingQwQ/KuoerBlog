(function(){
    var title = $('.edit-header .titleFrame #title');
    var label = $('.labelFrame #label');
    var editBtn = $('.edit-header .buttonFrame .editBtn');
    let id = localStorage.getItem('modifyId');
    let removeBtn = $('.edit-header .buttonFrame .removeBtn');

    // 初始化Vditor编辑器
    let vditor = new Vditor('vditor', {
        "height": 450,
        cache: {
            enable: false
        },
        after () {
            loadPostsInfo();
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
        modifyPosts(id, title.val(), vditor.getValue(), label.val());
    });
    removeBtn.click(function() {
        if(!confirm("你确定要删除这个帖子？删除后无法恢复.")) {
            return;
        }
        removePosts(id);
    });

    function loadPostsInfo() {
        $.ajax({
            url: api + 'home/findPostsById?id=' + id,
            type: 'get',
            success: function(result) {
                let target = result.data;
    
                title.val(target.title);
                label.val(target.labelName);
    
                vditor.setValue(target.content, true);
                hljs.initHighlightingOnLoad();
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