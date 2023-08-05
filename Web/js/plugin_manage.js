function openPluginConfigPage(pluginName) {
    window.open('plugin_config.html?name=' + pluginName, pluginName + '插件配置', "width=800,height=400,menubar=0,scrollbars=1,resizable=1,status=1,titlebar=0,toolbar=0,location=0");
}

function loadPluginList() {
    let tbody = $('.plugins .panel .panel-body .table tbody');

    $.ajax({
        url: api + 'plugin/findallplugininfo',
        type: 'get',
        success: function(target) {
            target.data.forEach(data => {
                var tr = $(`
                    <tr>
                        <td>${data.name}</td>
                        <td style="min-width: 60px">${data.version}</td>
                        <td>${data.introduce}</td>
                        <td>
                            <button type="button" class="btn btn-primary" onclick="openPluginConfigPage('${data.name}')">管理</button>
                        </td>
                    </tr>
                `);
                tbody.append(tr);
            });
        }
    });
}

loadPluginList();