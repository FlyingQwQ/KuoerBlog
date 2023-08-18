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
                            <button type="button" class="btn btn-primary" onclick="editUser(${element.id}, this)">编辑</button>
                            <button type="button" class="btn btn-danger" onclick="removeAdmin(${element.id})">删除</button>
                        </td>
                    </tr>
                `);
                tbody.append(tr);
            });
        }
    });
}

function modifyAdmin(name, pass) {
    $.ajax({
        url: api + 'admin/modifyadmin',
        type: 'get',
        data: {
            username: name,
            password: pass,
            token
        },
        success: function(target) {
            if(target.code == 1) {
                loadItem('personnel');
                alert('修改成功！');
            } else {
                alert('修改失败！\n请检查用户名是否有误。');
            }
        }
    });
}

function removeAdmin(id) {
    if(!confirm("你确定要删除这个账号？")) {
        return;
    }
    $.ajax({
        url: api + 'admin/removeadmin',
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

function editUser(id, target) {
    let tds = $(target).parent().parent().find('td');
    $('.modifyName').val(tds[1].innerText);
    $('.modifyPass').val('');
    $(".role-select > option").filter(function() {
        return $(this).text() === tds[2].innerText;
    }).prop("selected", true);
}


function getAllRole() {
    $.ajax({
        url: api + 'user/getallrole',
        type: 'get',
        data: {
            token
        },
        success: function(target) {
            if(target.code == 1) {
                let roleSelectDom = $('.role-select');
                target.data.forEach((role) => {
                    roleSelectDom.append(`<option value="${role.name}">${role.description}</option>`);
                });
            }
        }
    });
}
getAllRole();