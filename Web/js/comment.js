class Comment {
	constructor(elm, label){
        this.elm = $(elm);
        this.label = label;

        this.init();
    }

    init() {
        let commentName = '';
        commentName = (commentName = localStorage.getItem('commentName')) ? commentName : '';
        this.elm.html('');
        this.commentFrame = $(`
            <p class="title">评论</p>
            <textarea id="comment"></textarea>
            <div class="operation row">
                <div class="author col-xs-6 col-sm-4">
                    名称
                    <input class="name" type="text" value="${commentName}">
                </div>
                <button class="push">发表评论</button>
            </div>
            <div class="comments">
                
            </div>
        `);
        this.elm.append(this.commentFrame);
        this.event();
    }

    loadComment() {
        let comments = this.elm.find('.comments');
        $.ajax({
            url: api + 'comment/findCommentByLabel',
            data: {
                label: this.label
            },
            type: 'get',
            success: (target) => {
                comments.html('');
                target.data.sort(function(a, b) {
                    return b.id - a.id;
                }).forEach(element => {
                    let date = new Date(element.date);
                    let newDate = date.getFullYear() + "年" + addZero(date.getMonth() + 1) + "月" + addZero(date.getDate()) + "日";
                    let item = $(`
                        <div class="item">
                            <img class="icon" src="${this.gen_text_img([50, 50], element.name)}" width="50" height="50">
                            <div>
                                <p class="name">&lt;${element.name}&gt;<span class="date">${newDate}</span></p>
                                <p class="value"></p>
                            </div>
                        </div>
                    `);
                    item.find('.value').text(" - " + element.value);
                    comments.append(item);
                });
            }
        });
    }

    event() {
        var comment = this.elm.find('#comment');
        var name = this.elm.find('.name');
        let push = this.elm.find('.push');
        

        push.click(() => {

            if(name.val() == '' || comment.val() == '') {
                alert('不能留空！');
                return;
            }

            localStorage.setItem('commentName', name.val());

            var loadComment = this.loadComment;
            $.ajax({
                url: api + 'comment/addComment',
                data: {
                    name: name.val(),
                    value: comment.val(),
                    label: this.label
                },
                type: 'get',
                success: (target) => {
                    if(target.code == 1) {
                        this.loadComment();
                        comment.val('');
                    } else {
                        alert('发表失败！');
                    }
                    
                }
            });
        });
    }

    gen_text_img(size, s) {
        let letter = s.substr(0,1).toLowerCase();
        let letterNumber = (letter.charCodeAt() - 96);

        let colors = [
            '#FF1493', '#FF00FF', '#8A2BE2', '#4169E1', 
            '#008B8B', '#D2691E', '#FFEFD5', '#ADFF2F', 
            '#40E0D0', '#1E90FF', '#BA55D3', '#DC143C',
            '#008080', '#FF7F50', '#FFB6C1', '#8B008B', 
            '#7B68EE', '#ADD8E6', '#00FF7F', '#FFA500', 
            '#FF4500', '#FF6347', '#48D1CC', '#00008B',
            '#4B0082', '#FF69B4', '#9932CC'
        ];
        let cvs = document.createElement("canvas");
        cvs.setAttribute('width', size[0]);
        cvs.setAttribute('height', size[1]);
        let ctx = cvs.getContext("2d");
        ctx.fillStyle = colors[letterNumber > 26 ? 26 : letterNumber];
        ctx.fillRect(0, 0, size[0], size[1]);
        ctx.fillStyle = 'rgb(255,255,255)';
        ctx.font = size[0] * 0.5 + "px HarmonyOS";
        ctx.textBaseline = "middle";
        ctx.textAlign = "center";
        ctx.fillText(s.substr(0,1), size[0]/2,size[1]/2);
        return cvs.toDataURL('image/jpeg', 1);
    }

}