$(document).ready(function() {
    let content = $('.content');
    loadPosts(content)
});


// 获取帖子列表
function loadPosts(element) {
    $.ajax({
        url: api + 'home/findPostsAll',
        type: 'get',
        success: function(target) {
            element.html('');
            Object.keys(target.data).sort(sortDown).forEach(data => {
                let region = $('<div class="region"></div>');
                let year = $('<p class="year">' + data + '</p>')
                region.append(year);

                target.data[data].sort(function(a, b) {
                    return b.date - a.date;
                }).forEach(posts => {
                    let time = new Date(posts.date); 
                    let item = $(`
                        <div class="row item">
                            <div class="date col-xs-3 col-sm-2">
                                <p>${addZero(time.getMonth() + 1)}月${addZero(time.getDate())}日</p>
                            </div>
                            <div class="col-xs-9 col-sm-8">
                                <a href="./pages/post.html?id=${posts.id}" class="title">${posts.title}</a>
                            </div>
                            <div class="hidden-xs col-sm-2">
                                <div class="label">
                                    <a href="./pages/label.html?id=${posts.label}">${posts.labelName}</a>
                                </div>
                            </div>
                        </div>
                    `)
                    region.append(item);
                });
                element.append(region);
            });
            
        }
    });
}

function sortUp(a,b){
    return a-b;
}
function sortDown(a,b){
    return b-a;
}