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

    function loadComment() {
        let comments = $('.comments');
        comments.html('');

        $.ajax({
            url: api + 'comment/findCommentByLabel',
            data: {
                label: id
            },
            type: 'get',
            success: (target) => {
                target.data.forEach(data => {
                    let date = new Date(data.date);
                    let newDate = date.getFullYear() + "年" + addZero(date.getMonth() + 1) + "月" + addZero(date.getDate()) + "日";
                    let item = $(`
                        <div class="item">
                            <div class="comment">
                                <p class="name">${data.name}<span class="date">${newDate}</span> <a class="delComment" pid="${data.id}">删除</a></p>
                                <p class="value">${data.value}</p>
                            </div>
                            <div class="replyComment">

                            </div>
                        </div>
                    `);

                    let replyEle = replyCommentsfun(data.replyComments, 0);

                    let replyComment = item.find('.replyComment');
                    replyComment.append(replyEle);

                    comments.append(item);
                });

                $('.delComment').click((event) => {
                    let id = $(event.currentTarget).attr('pid');
                    delComment(id);
                });
            }
        });

    }

    function delComment(id) {
        $.ajax({
            url: api + 'comment/delComment',
            data: {
                token,
                id
            },
            type: 'get',
            success: (target) => {
                loadComment();
            }
        });
    }

    function replyCommentsfun(replyCommentList, index) {
        let replyCommentEle = $('<div class="replyComment"></div>');
        replyCommentList.forEach((comment) => {
            let replyEle = replyCommentsfun(comment.replyComments, index + 1);

            let date = new Date(comment.date);
            let newDate = date.getFullYear() + "年" + addZero(date.getMonth() + 1) + "月" + addZero(date.getDate()) + "日";
            let replyCommentItem = $(`
                <div class="replyItem">
                    <p class="name">${comment.name}<span class="date">${newDate}</span> <a class="delComment" pid="${comment.id}">删除</a></p>
                    <p class="value">${comment.value}</p>
                </div>
            `);
            replyCommentItem.append(replyEle);
            replyCommentEle.append(replyCommentItem);

            return replyEle;
        });

        if(index > 0) {
            return replyCommentEle;
        } else {
            return $(replyCommentEle.html());
        }
    }

})();