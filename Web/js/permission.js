(function() {
    let roles_table_dom = $('.permission .roles-table');
    let permission_table_dom = $('.permission .permission-table');

    function loadRoles() {
        $.ajax({
            url: api + 'user/getallrole',
            type: 'get',
            data: {
                token
            },
            success: function(target) {
                if(target.code == 1) {
                    let tbody = roles_table_dom.find('tbody');
                    target.data.forEach((role) => {
                        var tr = $(`
                            <tr>
                                <td>${role.id}</td>
                                <td style="min-width: 60px">${role.name}</td>
                                <td>${role.description}</td>
                                <td>
                                    <button type="button" class="btn btn-primary" onclick="">编辑</button>
                                    <button type="button" class="btn btn-danger" onclick="">删除</button>
                                </td>
                            </tr>
                        `);
                        tbody.append(tr);
                    });
                }
            }
        });
    }

    function loadPermissions() {
        $.ajax({
            url: api + 'user/getallpermission',
            type: 'get',
            data: {
                token
            },
            success: function(target) {
                if(target.code == 1) {
                    let tbody = permission_table_dom.find('tbody');
                    target.data.forEach((permission) => {
                        var tr = $(`
                            <tr>
                                <td>${permission.id}</td>
                                <td style="min-width: 60px">${permission.name}</td>
                                <td>${permission.description}</td>
                                <td>
                                    <button type="button" class="btn btn-primary" onclick="">编辑</button>
                                    <button type="button" class="btn btn-danger" onclick="">删除</button>
                                </td>
                            </tr>
                        `);
                        tbody.append(tr);
                    });
                }
            }
        });
    }


    loadRoles();
    loadPermissions();
})();