// 验证登录状态
var token = localStorage.getItem('token');
// verification(token);

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
    
});

// 默认加载push页面
// loadItem('web_manage');
loadItem('personnel');
// loadItem('plugin_manage');

$('.main .header .homeBtn').click(function() {
    location.href = '../../index.html'
});
$('.main .header .personnelBtn').click(function() {
    loadItem('personnel');
    $('.main .header .personnelBtn').addClass('active').siblings().removeClass('active');
});
$('.main .header .postsBtn').click(function() {
    loadItem('posts_manage');
    $('.main .header .postsBtn').addClass('active').siblings().removeClass('active');
});
$('.main .header .pushBtn').click(function() {
    loadItem('send_posts');
    $('.main .header .pushBtn').addClass('active').siblings().removeClass('active');
});
$('.main .header .webManageBtn').click(function() {
    loadItem('web_manage');
    $('.main .header .webManageBtn').addClass('active').siblings().removeClass('active');
});
$('.main .header .pluginManageBtn').click(function() {
    loadItem('plugin_manage');
    $('.main .header .pluginManageBtn').addClass('active').siblings().removeClass('active');
});



function loadItem(pageName) {
    verification(token)
    $.ajax({
        url: './' + pageName + '.html',
        type: 'get',
        success: function(target) {
            content.html(target);
        }
    });
}

function verification(token) {
    $.ajax({
        url: api + 'admin/verification',
        type: 'get',
        data: {token},
        success: function(target) {
            if(target == '') {
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