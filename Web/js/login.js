// 验证登录状态
verification().then(function(data) {
    location.href = './manage.html';
});

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
        url: api + 'user/login',
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