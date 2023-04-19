$(document).ready(function() {
    let content = $('.content');
    loadPosts(content)
});


// 获取帖子列表
function loadPosts(element) {
    $.ajax({
        url: api + 'home/findPostsByLabel',
        type: 'get',
        data: {
            id: getQueryVariable('id')
        },
        success: function(result) {
            let target = result.data;

            if(Object.keys(target).length < 1) {
                return;
            }
            element.html('');
            
            let labelName = target[Object.keys(target)[0]][0].labelName;
            document.title = labelName + ' - ' + document.title;
            element.append($(`<p class="title">${labelName}</p>`));

            Object.keys(target).sort(sortDown).forEach(data => {
                let region = $('<div class="region"></div>');
                let year = $('<p class="year">' + data + '</p>')
                region.append(year);

                target[data].sort(function(a, b) {
                    return b.date - a.date;
                }).forEach(posts => {
                    let time = new Date(posts.date); 
                    let item = $(`
                        <div class="row item">
                            <div class="date col-xs-3 col-sm-2">
                                <p>` + (time.getMonth() + 1) + "月" + time.getDate() + "日" + `</p>
                            </div>
                            <div class="col-xs-9 col-sm-10">
                                <a href="./post.html?id=${posts.id}" class="title">${posts.title}</a>
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


function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
            var pair = vars[i].split("=");
            if(pair[0] == variable){return pair[1];}
    }
    return false;
}