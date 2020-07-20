function initTableConfig(layer, request_url, request_data, columns, order) {
    return {
        'searching' : false
        , 'processing' : true
        , 'serverSide' : true
        , 'autoWidth': false
        , 'language' : {
            'url' : '/static/lang/Chinese.lang'
        }
        , 'ajax' : {
            'url' : request_url,
            'type' : 'get',
            'data' : request_data,
            'error' : function(xhr, textStatus, errorThrown) {
                var msg = xhr.responseText;
                console.log(msg);
                var response = JSON.parse(msg);
                var code = response.code;
                var message = response.message;
                if (code == 400) {
                    layer.msg(message);
                } else if (code == 401) {
                    localStorage.removeItem('token');
                    layer.msg('token过期，请先登录', {
                        shift : -1,
                        time : 1000
                    }, function() {
                        location.href = '/login';
                    });
                } else if (code == 403) {
                    console.log('未授权:' + message);
                    layer.msg('未授权');
                } else if (code == 500) {
                    layer.msg('系统错误：' + message);
                }
            }
        },
        'lengthMenu': [ [10, 20, 50], ['10条/页', '20条/页', '50条/页'] ],
        'dom' : "<'dt-toolbar'r>t<'dt-toolbar-footer'<'col-sm-10 col-xs-12 hidden-xs'i l><'col-xs-12 col-sm-10' p v>>",
        'columns': columns,
        'order' : order
    };
}

function drawCallback(wrapper, table) {
    return function (settings) {
        //渲染完毕后的回调
        //清空全选状态
        $(":checkbox[name='cb-check-all']", wrapper).prop('checked', false);
        //默认选中第一行
        $("tbody tr", table).eq(0).click();
    }
}

function buttonEdit(data){
    var btn = $("<button class='layui-btn layui-btn-xs' title='编辑' onclick='editInfo(\"" + data +"\")'><i class='layui-icon'>&#xe642;</i></button>");
    return btn.prop("outerHTML");
}

function buttonDel(data){
    var btn = $("<button class='layui-btn layui-btn-xs btn-danger' title='删除' onclick='deleteInfo(\"" + data +"\")'><i class='layui-icon'>&#xe640;</i></button>");
    return btn.prop("outerHTML");
}

function buttonInfo(data){
    var btn = $("<button class='layui-btn layui-btn-xs btn-info' title='详情' onclick='showDetail(\"" + data +"\")'><i class='layui-icon'>&#xe65f;</i></button>");
    return btn.prop("outerHTML");
}

function buttonDownload(id, name){
    var btn = $("<button class='layui-btn layui-btn-xs btn-info' title='下载' onclick='download(\"" + id + "\",\"" + name +"\")'><i class='layui-icon'>&#xe601;</i></button>");
    return btn.prop("outerHTML");
}
