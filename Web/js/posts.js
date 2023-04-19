$(document).ready(function() {
    loadContent();
});

// 加载帖子内容
function loadContent() {
    let title = $('.main .posts_header .title');
    let postDate = $('.main .posts_header .date');
    let content = $('.main .content');
    

    $.ajax({
        url: api + 'home/findPostsById?id=' + getQueryVariable('id'),
        type: 'get',
        success: function(target) {
            content.html('');
            if (target.code == 1) {
                let result = target.data
                
                document.title = result.title + ' - ' + document.title;
                title.html(result.title);
                content.html(marked.parse(result.content));
                let date = new Date(result.date);
                postDate.text(date.getFullYear() + "-" + addZero(date.getMonth() + 1) + "-" + addZero(date.getDate()));
                $('.commentFrame').css({'display':'block'});

                // 初始化代码高亮
                hljs.initHighlightingOnLoad();
            } else {
                location.href = '/';
            }
        }
    });
}

function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if(pair[0] == variable){
                return pair[1];
            }
    }
    return false;
}