// 验证登录状态
var token = localStorage.getItem('token');
// verification(token);

let content = $('.content');

// 默认加载push页面
// loadItem('web_manage');
loadItem('personnel');

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
    localStorage.setItem('editType', 'add');
    loadItem('edit');
    $('.main .header .pushBtn').addClass('active').siblings().removeClass('active');
});
$('.main .header .webManageBtn').click(function() {
    loadItem('web_manage');
    $('.main .header .webManageBtn').addClass('active').siblings().removeClass('active');
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
            if(target == '' || target.id < 0) {
                location.href = './index.html';
            }
        }
    });
}

function cancel() {
    localStorage.setItem('token', '');
    location.href = './index.html';
}