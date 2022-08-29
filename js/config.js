// var api = 'http://localhost:8081/';
var api = 'https://106.13.232.55:2333/';

setTimeout(function() {
    let loading = $('.loading');
    if(loading.length > 0) {
        loading.show();
    }
}, 500);

// 启用Bootstrap的tooltip功能
$("[data-toggle='tooltip']").tooltip();