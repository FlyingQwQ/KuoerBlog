var api = 'http://localhost:8081/';
// var api = 'https://blogapi.kuoer.top:2333/';
var createTime = '2021 - ' + new Date().getFullYear();
var filingNumber = '粤ICP备2022013504号';

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

function loadPlugin(uri) {
    $.ajax({
        url: api + 'plugin/getplugindata',
        data: {
            pageURL: uri ? uri : location.href
        },
        type: 'get',
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

loadPlugin();

// 启用Bootstrap的tooltip功能
$("[data-toggle='tooltip']").tooltip();

$('#createTime').text(createTime);
$('#filing').text(filingNumber);