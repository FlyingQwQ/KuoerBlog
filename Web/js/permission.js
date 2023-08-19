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

    let roleid;
    $('#roleEditModal').on('show.bs.modal', function (event) {
        let tds = $(event.relatedTarget).parent().parent().find('td');
        roleid = tds[0].innerText;
        let roleName = tds[1].innerText;
        let roleDescription = tds[2].innerText;
        let permissionGroupDom = $(this).find('.permissionGroup');

        $('.roleName').val(roleName);
        $('.roleDescription').val(roleDescription);

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

    $('.roleEditModalConfirm').click(function() {
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

    loadRoles();
    loadPermissions();

    return {
        removeRole,
        removePermission
    }
})();