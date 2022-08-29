// 验证登录状态
verification(localStorage.getItem('token'));

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
            if(target.id > 0) {
                location.href = './manage.html';
            } 
        }
    });
}