var api = 'http://localhost:8081/';
// var api = 'https://blogapi.kuoer.top:2333/';
var createTime = '2021 - ' + new Date().getFullYear();
var filingNumber = '粤ICP备2022013504号';
var token = localStorage.getItem('token');
if(token == 'undefined' || localStorage.getItem('token') == null) {
    localStorage.setItem('token', '');
    location.reload();
}
var currUserInfo = {};

setTimeout(function() {
    let loading = $('.loading');
    if(loading.length > 0) {
        loading.show();
    }
}, 500);

function addZero(num) {
    if(num < 10) {
        num = "0" + num;
    }
    return num;
}

// 加载插件数据
function loadPlugin(uri) {
    $.ajax({
        url: api + 'plugin/getplugindata',
        data: {
            pageURL: uri ? uri : location.href,
            token
        },
        type: 'post',
        async: false,
        success: function(target) {
            if(target.code != 1) {
                return;
            }
            target.data.forEach(data => {
                $('body').append(data.template);
            });
        }
    });
}

// 获取当前页面的所有带权限的DOM，如果没有权限将不显示
function permission_display_dom() {
    let permissionDoms = $('[permission]');
    $.ajax({
        url: api + 'user/getuserinfo',
        data: {token},
        type: 'get',
        async: false,
        success: function(target) {
            if(target.code == 1) {
                currUserInfo = target.data;
                target.data.permissions.forEach((permission) => {
                    permissionDoms.each((index, dom) => {
                        let testDom = $(dom)
                        if(testDom.attr('permission') == permission.name) {
                            testDom.show();
                        }
                    });
                });
            }
        }
    });
}

async function verification() {
    const target = await new Promise(function(resolve, reject) {
        $.ajax({
            url: api + 'user/verification',
            type: 'get',
            data: {token},
            async: false,
            success: function(target) {
                if(target.code == 1) {
                    resolve(target);
                } else {
                    reject(target);
                }
            },
            error: function(error) {
                reject(error);
            }
        });
    });
    return target;
}

loadPlugin();
permission_display_dom();

// 启用Bootstrap的tooltip功能
let tooltip = $("[data-toggle='tooltip']");
if(tooltip.length > 0) {
    tooltip.tooltip();
}

$('#createTime').text(createTime);
$('#filing').text(filingNumber);


