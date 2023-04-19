// var api = 'http://localhost:8081/';
var api = 'https://blogapi.kuoer.top:2333/';

setTimeout(function() {
    let loading = $('.loading');
    if(loading.length > 0) {
        loading.show();
    }
}, 500);

// 启用Bootstrap的tooltip功能
$("[data-toggle='tooltip']").tooltip();


function addZero(num) {
    if(num < 10) {
        num = "0" + num;
    }
    return num;
}