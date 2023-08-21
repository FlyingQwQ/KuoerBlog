window.personnel = (function() {
    loadAdmin();

    $('.modifyBtn').click(function() {
        var name = $('.modifyName').val();
        var pass = $('.modifyPass').val();
        if(name != '' && pass != '') {
            modifyAdmin(name, pass);
        }
    });

    $('.addBtn').click(function() {
        var name = $('.addName').val();
        var pass = $('.addPass').val();
        if(name != '' && pass != '') {
            addAdmin(name, pass);
        }
    });


    function loadAdmin() {
        let tbody = $('.personnel .panel .panel-body .table tbody');

        $.ajax({
            url: api + 'user/userlist',
            type: 'get',
            data: {token},
            success: function(target) {
                target.data.forEach(element => {
                    let roleNames = [];
                    element.roles.forEach((role) => {
                        roleNames.push(role.description);
                    });

                    var tr = $(`
                        <tr>
                            <td>${element.id}</td>
                            <td style="min-width: 60px">${element.name}</td>
                            <td>${roleNames.join('，')}</td>
                            <td>
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#userEditModal">编辑</button>
                                <button type="button" class="btn btn-danger" onclick="personnel.removeAdmin(${element.id})">删除</button>
                            </td>
                        </tr>
                    `);
                    tbody.append(tr);
                });
            }
        });
    }

    function removeAdmin(id) {
        if(!confirm("你确定要删除这个账号？")) {
            return;
        }
        $.ajax({
            url: api + 'user/remove',
            type: 'get',
            data: {
                id,
                token
            },
            success: function(target) {
                if(target.code == 1) {
                    loadItem('personnel');
                    alert('删除成功！');
                } else {
                    alert('删除失败！');
                }
            }
        });
    }

    function getAllRole() {
        $.ajax({
            url: api + 'auth/getallrole',
            type: 'get',
            data: {
                token
            },
            success: function(target) {
                if(target.code == 1) {
                    let roleSelectDom = $('.role-select');
                    target.data.forEach((role) => {
                        roleSelectDom.append(`<option value="${role.id}">${role.description}</option>`);
                    });
                }
            }
        });
    }
    getAllRole();

    // 编辑用户
    let editUserId;
    $('#userEditModal').on('show.bs.modal', function (event) {
        let tds = $(event.relatedTarget).parent().parent().find('td');
        editUserId = tds[0].innerText;
        $(this).find('.name').val(tds[1].innerText);
        $(this).find('.password').val('');
        $(this).find(".role-select > option").filter(function() {
            return $(this).text() === tds[2].innerText;
        }).prop("selected", true);
    });
    $('#userEditModal .confirm').click(function() {
        if(editUserId == '') {
            return;
        }
        $.ajax({
            url: api + 'user/modify',
            type: 'get',
            data: {
                userid: editUserId,
                password: $('#userEditModal .password').val(),
                roleid: $('#userEditModal .role-select option:selected').val(),
                token
            },
            success: function(target) {
                if(target.code == 1) {
                    location.reload();
                } else {
                    alert('修改失败！\n请检查用户名是否有误。');
                }
            }
        });
    });

    return {
        removeAdmin
    }
})();