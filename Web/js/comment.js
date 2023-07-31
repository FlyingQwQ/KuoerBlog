class Comment {
	constructor(elm, label){
        this.elm = $(elm);
        this.label = label;

        this.replyCommentEle = undefined;
        this.replyID = -1;

        this.init();
    }

    // 初始化评论
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
                <button class="btn push">发表评论</button>
                <button class="btn cancelReply" style="display: none;">取消回复</button>
            </div>
            <div class="comments">
                
            </div>
        `);
        this.elm.append(this.commentFrame);
        this.event();
    }

    // 加载评论
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

                target.data.forEach(data => {
                    let date = new Date(data.date);
                    let newDate = date.getFullYear() + "年" + addZero(date.getMonth() + 1) + "月" + addZero(date.getDate()) + "日";
                    let item = $(`
                        <div class="item">
                            <div class="comment">
                                <img class="icon" src="${this.gen_text_img([50, 50], data.name)}" width="50" height="50" draggable="false">
                                <div>
                                    <p class="name">${data.name}<span class="date">${newDate}</span> <a class="reply" cid="${data.id}">回复</a></p>
                                    <p class="value"></p>
                                </div>
                            </div>
                            <div class="replyComment">

                            </div>
                        </div>
                    `);

                    let replyEle = replyCommentsfun(data.replyComments, data.id);

                    item.find('.value').text(data.value);

                    let replyComment = item.find('.replyComment');
                    replyComment.append(replyEle);

                    comments.append(item);

                    item.find('.reply').click((target) => {
                        this.reply(target);
                    });
                });
            }
        });

        function replyCommentsfun(replyCommentList, mainCommentId) {
            let reply = '';
            replyCommentList.forEach((comment) => {
                let replyEle = replyCommentsfun(comment.replyComments, mainCommentId);

                let date = new Date(comment.date);
                let newDate = date.getFullYear() + "年" + addZero(date.getMonth() + 1) + "月" + addZero(date.getDate()) + "日";
                
                let recipientEle = '';
                if(mainCommentId != comment.replyid) {
                    recipientEle = `
                        <span class="recipient">回复 @${comment.recipient}：</span>
                    `;
                }
                
                let replyCommentItemEle = `
                    <div class="replyItem">
                        <p class="name">
                            ${comment.name} <span class="date">${newDate}</span> <a class="reply" cid="${comment.id}">回复</a>
                        </p>
                        <p class="value">
                            ${recipientEle}
                            ${comment.value}
                        </p>
                    </div>
                    ${replyEle}
                `;
                reply = reply + replyCommentItemEle;

                return replyEle;
            });

            return reply;
        }
    }

    event() {
        var comment = this.elm.find('#comment');
        var name = this.elm.find('.name');
        let push = this.elm.find('.push');
        let cancelReplyElm = this.elm.find('.cancelReply');

        push.click(() => {

            if(name.val() == '' || comment.val() == '') {
                alert('不能留空！');
                return;
            }

            localStorage.setItem('commentName', name.val());

            if(this.replyID > -1) {
                $.ajax({
                    url: api + 'comment/addReplyComment',
                    data: {
                        name: name.val(),
                        value: comment.val(),
                        label: this.label,
                        replyid: this.replyID
                    },
                    type: 'get',
                    success: (target) => {
                        if(target.code == 1) {
                            this.loadComment();
                            comment.val('');
                            this.cancelReply();
                        } else {
                            alert('发表失败！');
                        }
                        
                    }
                });
            } else {
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
            }
            
        });

        cancelReplyElm.click(() => {
            this.cancelReply();
        });
    }

    // 生成头像
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
        ctx.font = size[0] * 0.4 + "px HarmonyOS";
        ctx.textBaseline = "middle";
        ctx.textAlign = "center";
        ctx.fillText(s.substr(0,1), size[0]/2,size[1]/2);
        return cvs.toDataURL('image/jpeg', 1);
    }

    // 设置需要回复的评论
    reply(target) {
        let commentId = $(target.currentTarget).attr('cid');
        this.replyID = commentId;
        this.elm.find('.cancelReply').show();
        
        if(this.replyCommentEle) {
            this.replyCommentEle.css({
                'backgroundColor': 'transparent'
            });
        }
        this.replyCommentEle = $(target.currentTarget).parent().parent();
        this.replyCommentEle.css({
            'backgroundColor': 'rgba(61, 61, 61, 0.1)'
        });
    }
    // 取消回复
    cancelReply() {
        this.replyID = -1;
        this.elm.find('.cancelReply').hide();

        this.replyCommentEle.css({
            'backgroundColor': 'transparent'
        });
    }

    // 将html代码转成文本
    repalceHtmlToText(str) {
        str = str.replace(/<\/?.+?>/g, "");
        str = str.replace(/&nbsp;/g, "");
        return str;
    }
}