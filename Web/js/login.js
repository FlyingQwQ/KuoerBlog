// 验证登录状态
verification(localStorage.getItem('token'));

$(document).ready(function() {
    var token = getQueryVariable('token');
    if(!token) {
        
    } else if(token != 'null') {
        localStorage.setItem('token', token);
        location.href = './manage.html';
    } else {
        alert('登录失败，这个QQ号没有绑定账号');
        location.href = '.';
    }
});

$('input').keydown(function(evt) {
    if(evt.keyCode == 13) {
        $('.loginBtn').click();
    }
});


$('.loginBtn').click(function() {
    let username = $('#username').val();
    let password = $('#password').val();
    login(username, password);
});

function login(username, password) {
    $.ajax({
        url: api + 'admin/login',
        type: 'get',
        data: {
            username,
            password
        },
        success: function(target) {
            if(target.id > 0) {
                localStorage.setItem('token', target.token);
                location.href = './manage.html';
            } else {
                alert('登录失败！');
            }
        }
    });
}

function verification(token) {
    $.ajax({
        url: api + 'admin/verification',
        type: 'get',
        data: {token},
        success: function(target) {
            if(target.code == 1) {
                location.href = './manage.html';
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

function qqLogin() {
    let callBack = api + 'admin/qqlogin';
    location.href = 'https://api.uomg.com/api/login.qq?method=login&callback=' + callBack;
}