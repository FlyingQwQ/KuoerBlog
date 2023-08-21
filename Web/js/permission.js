window.permission = (function() {
    let roles_table_dom = $('.permission .roles-table');
    let permission_table_dom = $('.permission .permission-table');

    let roles = {};
    let permissions = {};

    function loadRoles() {
        $.ajax({
            url: api + 'auth/getallrole',
            type: 'get',
            data: {token},
            success: function(target) {
                if(target.code == 1) {
                    let tbody = roles_table_dom.find('tbody');
                    roles = target.data;
                    target.data.forEach((role) => {
                        var tr = $(`
                            <tr>
                                <td>${role.id}</td>
                                <td>${role.name}</td>
                                <td>${role.description}</td>
                                <td>
                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#roleEditModal">编辑</button>
                                    <button type="button" class="btn btn-danger" onclick="permission.removeRole(${role.id})">删除</button>
                                </td>
                            </tr>
                        `);
                        tbody.append(tr);
                    });
                }
            }
        });
    }

    function removeRole(roleId) {
        if(!confirm("你确定要删除这个角色？删除后无法恢复.")) {
            return;
        }
        $.ajax({
            url: api + 'auth/removerole',
            type: 'post',
            data: {token, roleId},
            success: function(target) {
                if(target.code == 1) {
                    loadItem('permission');
                }
            }
        });
    }

    function loadPermissions() {
        $.ajax({
            url: api + 'auth/getallpermission',
            type: 'get',
            data: {token},
            success: function(target) {
                if(target.code == 1) {
                    let tbody = permission_table_dom.find('tbody');
                    permissions = target.data;
                    target.data.forEach((permission) => {
                        var tr = $(`
                            <tr>
                                <td>${permission.id}</td>
                                <td>${permission.name}</td>
                                <td>${permission.description}</td>
                                <td>
                                    <button type="button" class="btn btn-danger" onclick="permission.removePermission(${permission.id})">删除</button>
                                </td>
                            </tr>
                        `);
                        tbody.append(tr);
                    });
                }
            }
        });
    }

    function removePermission(permissionId) {
        if(!confirm("你确定要删除这个权限？删除后无法恢复.")) {
            return;
        }
        $.ajax({
            url: api + 'auth/removeperission',
            type: 'post',
            data: {token, permissionId},
            success: function(target) {
                if(target.code == 1) {
                    loadItem('permission');
                }
            }
        });
    }

    // 编辑角色模态框
    let roleid;
    $('#roleEditModal').on('show.bs.modal', function (event) {
        let tds = $(event.relatedTarget).parent().parent().find('td');
        roleid = tds[0].innerText;
        let roleName = tds[1].innerText;
        let roleDescription = tds[2].innerText;
        let permissionGroupDom = $(this).find('.permissionGroup');

        $(this).find('.roleName').val(roleName);
        $(this).find('.roleDescription').val(roleDescription);

        permissionGroupDom.html('');
        permissions.forEach((permission) => {
            let newCheckBoxDom = $(`
                <div class="checkbox">
                    <label><input type="checkbox" name="permission" value="${permission.id}">${permission.description}</label>
                </div>
            `);
            permissionGroupDom.append(newCheckBoxDom);
        });
        $.ajax({
            url: api + 'auth/getrolepermission',
            data: {token, roleid},
            success: function(target) {
                if(target.code == 1) {
                    target.data.forEach((permission) => {
                        $('#roleEditModal .checkbox input').filter(function() {
                            return $(this).val() == permission.id;
                        }).prop('checked', true);
                    });
                }   
            }
        });
    })
    $('#roleEditModal .confirm').click(function() {
        let checkedPermissionIds = [];
        $('#roleEditModal .checkbox input:checked').each(function() {
            checkedPermissionIds.push($(this).val());
        });
        $.ajax({
            url: api + 'auth/modifyrolepermission',
            type: 'post',
            data: {
                token, 
                roleid,
                roleName: $('.roleName').val(), 
                roleDescription: $('.roleDescription').val(),
                permissionids: checkedPermissionIds.join(',')
            },
            success: function(target) {
                if(target.code == 1) {
                    location.reload();
                }   
            },
            error: function() {
                alert('修改失败，请检查是否有 "auth:modifyrolepermission" 权限');
            }
        });
    });


    // 添加新角色模态框
    $('#newRoleModal .confirm').click(function() {
        let roleName = $('#newRoleModal .roleName').val();
        let roleDescription = $('#newRoleModal .roleDescription').val();
        if(roleName != '' && roleDescription != '') {
            $.ajax({
                url: api + 'auth/addrole',
                type: 'post',
                data: {
                    token, roleid,
                    name: roleName,
                    description: roleDescription
                },
                success: function(target) {
                    if(target.code == 1) {
                        location.reload();
                    }   
                }
            });
        } else {
            alert('不能留空!');
        }
    });

    // 添加新权限模态框
    $('#newPerissionModal .confirm').click(function() {
        let perissionName = $('#newPerissionModal .perissionName').val();
        let perissionDescription = $('#newPerissionModal .perissionDescription').val();
        if(perissionName != '' && perissionDescription != '') {
            $.ajax({
                url: api + 'auth/addperission',
                type: 'post',
                data: {
                    token, roleid,
                    name: perissionName,
                    description: perissionDescription
                },
                success: function(target) {
                    if(target.code == 1) {
                        location.reload();
                    }   
                }
            });
        } else {
            alert('不能留空!');
        }
    });



    loadRoles();
    loadPermissions();

    return {
        removeRole,
        removePermission
    }
})();