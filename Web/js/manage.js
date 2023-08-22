let content = $('.content');

$(document).ready(function() {
    var result = getQueryVariable('result');
    if(result != '' || result != false) {
        if(result == 'qqsuccess') {
            alert('QQ绑定成功');
        } else if(result == 'qqerror') {
            alert('QQ绑定失败');
        }
        location.href = '.';
    }

    // 默认加载页面
    let uriItemName = getQueryVariable('item');
    if(!uriItemName) {
        loadItem('user_info');
    } else {
        loadItem(uriItemName);
    }

    $('.title .userName').text(currUserInfo.name );
});

$('.main .header .homeBtn').click(function() {
    location.href = '../../index.html'
});
$('.main .header .userInfoBtn').click(function() {
    loadItem('user_info');
});
$('.main .header .personnelBtn').click(function() {
    loadItem('personnel');
});
$('.main .header .permissionBtn').click(function() {
    loadItem('permission');
});
$('.main .header .postsBtn').click(function() {
    loadItem('posts_manage');
});
$('.main .header .pushBtn').click(function() {
    loadItem('send_posts');
});
$('.main .header .webManageBtn').click(function() {
    loadItem('web_manage');
});
$('.main .header .pluginManageBtn').click(function() {
    loadItem('plugin_manage');
});



function loadItem(pageName) {
    verification(token);

    $.ajax({
        url: './' + pageName + '.html',
        type: 'get',
        success: function(target) {
            content.html(target);
            history.pushState("", "", window.location.pathname + "?item=" + pageName);
            loadPlugin(location.origin + "/manage_menu?item=" + pageName);
        }
    });
    
    let items = $('.main .header .item');
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