let content = $('.content');

$(document).ready(function() {
    // 默认加载页面
    let uriItemName = getQueryVariable('item');
    if(!uriItemName) {
        loadItem('user_info');
    } else {
        loadItem(uriItemName);
    }

    $('.title .userName').text(currUserInfo.name);

    verification(token);
});

$('.main .header .homeBtn').click(function() {
    location.href = '../../index.html'
});
$('.main .header .item').click(function() {
    loadItem($(this).attr('item'));
});


function loadItem(pageName) {
    let items = $('.main .header .item');
    let next = true;

    // 检查是否拥有指定权限
    items.each((index, element) => {
        let item = $(element);
        if(item.attr('item') == pageName) {
            let permission = item.attr('permission')
            if(permission != undefined) {
                if(!checkLoadItemPermission(permission)) {
                    loadItem('user_info');
                    next = false;
                }
            }
        }
    });
    if(!next) {
        return;
    }

    $.ajax({
        url: './' + pageName + '.html',
        type: 'get',
        success: function(target) {
            content.html(target);
            history.pushState("", "", window.location.pathname + "?item=" + pageName);
            loadPlugin(location.origin + "/manage_menu?item=" + pageName);
        }
    });

    items.each((index, element) => {
        let item = $(element);
        if(item.attr('item') == pageName) {
            item.addClass('active').siblings().removeClass('active');
        }
    });
}


function verification(token) {
    $.ajax({
        url: api + 'user/verification',
        type: 'get',
        data: {token},
        success: function(target) {
            if(target.code == 5) {
                location.href = './index.html';
            }
        }
    });
}

function cancel() {
    localStorage.setItem('token', '');
    location.href = './index.html';
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

function checkLoadItemPermission(permissionName) {
    let target = false;
    if(currUserInfo.permissions != undefined) {
        currUserInfo.permissions.forEach((permission) => {
            if(permission.name == permissionName) {
                target = true;
            }
        });
    }
    return target;
}