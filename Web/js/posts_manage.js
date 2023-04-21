loadPosts();


// 获取帖子列表
function loadPosts() {
    $.ajax({
        url: api + 'home/findPostsAll',
        type: 'get',
        success: function(target) {

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
                            <p class="date col-xs-3">` + (time.getMonth() + 1) + "月" + time.getDate() + "日" + `</p>
                            <a href="javascript:edit(` + posts.id + `)" class="title col-xs-9">` + posts.title + `</a>
                        </div>
                    `)
                    region.append(item);
                });
                $('.posts').append(region);
            });
            
        }
    });
}

function edit(id) {
    localStorage.setItem('editType', 'modify');
    localStorage.setItem('modifyId', id);
    loadItem('edit_posts');
}

function sortDown(a,b){
    return b-a;
}