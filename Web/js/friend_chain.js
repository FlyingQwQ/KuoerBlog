loadChain();

function loadChain() {
    let items = $('.main .content .items');

    $.ajax({
        url: api + 'friendchain/findFriendChainAll',
        type: 'get',
        success: function(target) {
            items.html('');
            target.data.forEach(element => {
                let item = $(`
                    <div class="col-sm-4 col-xs-12 item">
                        <img class="webIcon" src="${element.icon}">
                        <div class="info">
                            <a class="title" target="_blank" href="${element.url}">${element.title}</a>
                            <p class="subTitle">${element.subtitle}</p>
                        </div>
                    </div>
                `);
                items.append(item);
            });
        }
    });
}