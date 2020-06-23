'use strict';

$.ajaxSetup({
    cache : false,
    headers : {
        "token" : localStorage.getItem("token")
    },
    error : function(xhr, textStatus, errorThrown) {
        var msg = xhr.responseText;
        var response = JSON.parse(msg);
        var code = response.code;
        var message = response.message;
        if (code == 400) {
            console.log(message)
            layer.msg(message);
        } else if (code == 401) {
            localStorage.removeItem("token");
            location.href = '/login';
        } else if (code == 403) {
            console.log("未授权:" + message);
            layer.msg('未授权');
        } else if (code == 500) {
            layer.msg('系统错误：' + message);
        }
    }
});

initMenu();
function initMenu(){
     $.ajax({  
         url:"/permissions/current",  
         type:"get",  
         async:false,
         success:function(data){
             if(!$.isArray(data)){
                 location.href='/login';
                 return;
             }
             var menu = $("#leftmenu");
             $.each(data, function(i,item){
                 var a = $("<a href='javascript:;'></a>");
                 var css = item.css;
                 if(css!=null && css!=""){
                     a.append("<i aria-hidden='true' class='fa " + css +"'></i>&emsp; ");
                 }
                 a.append("<span class='layui-nav-title'>"+item.name+"</span>");
                 //a.attr("lay-id", item.id);
                 var href = item.href;
                 if(href == null || href == ""){
                     a.attr("lay-url", "#");
                 } else if(href != null && href != ""){
                    a.attr("lay-url", href);
                 }
                 var li = $("<li class='layui-nav-item'></li>");
                 li.append(a);
                 menu.append(li);
                 //多级菜单
                 setChild(li, item.child)
            });
         }
     });
}

function setChild(parentElement, child){
    if(child != null && child.length > 0){
        $.each(child, function(j,item2){
            var ca = $("<a href='javascript:;'></a>");
            ca.attr("lay-url", item2.href);
            var css2 = item2.css;
            if(css2!=null && css2!=""){
                ca.append("<i aria-hidden='true' class='fa " + css2 +"'></i>");
            }
            ca.append("<span class='layui-nav-title'>"+item2.name+"</span>");
            var dd = $("<dd></dd>");
            dd.append(ca);
            var dl = $("<dl class='layui-nav-child'></dl>");
            dl.append(dd);
            parentElement.append(dl);
            // 递归
            setChild(dd, item2.child);
        });
    }
}

showLoginInfo();
function showLoginInfo(){
    $.ajax({
        type : 'get',
        url : '/user/current',
        async : false,
        success : function(data) {
            $(".timo-nav-user span").text(data.nickname);
            var pro = window.location.protocol;
            var host = window.location.host;
            var domain = pro + "//" + host;
            
            var sex = data.sex;
            var url = data.headImgUrl;
            if(url == null || url == ""){
                if(sex == 1){
                    url = "/static/images/avatars/sunny.png";
                } else {
                    url = "/static/images/avatars/1.png";
                }
                
                url = domain + url;
            } else {
                url = domain + "/static" + url;
            }
            var img = $(".timo-nav-user img");
            img.attr("src", url);
        }
    });
}

layui.use(['element', 'form', 'layer', 'upload'], function () {
    var $ = layui.jquery;
    var element = layui.element; //加载element模块
    var layer = layui.layer; //加载layer模块
    var upload = layui.upload;  //加载upload模块

    /* 侧边栏开关 */
    $(".side-toggle").on("click", function (e) {
        e.preventDefault();
        var to = $(".layui-layout-admin");
        to.toggleClass("layui-side-shrink");
        to.attr("toggle") === 'on' ? to.attr("toggle", "off") : to.attr("toggle", "on");
    });
    $(".layui-side").on("click", function () {
        var to = $(".layui-layout-admin");
        if (to.attr("toggle") === 'on') {
            to.attr("toggle", "off");
            to.removeClass("layui-side-shrink");
        }
    });
    
    /* 渲染 tab 右键菜单 */
    function createStyle(config) {
        return ".rightmenu{position:absolute;width:" + config.width + "px;z-index:9999;display:none;background-color:#fff;padding:2px;color:#333;border:1px solid #eee;border-radius:2px;cursor:pointer}.rightmenu li{text-align:center;display:block;height:30px;line-height:30px;margin:8px 0 8px 0}.rightmenu li:hover{background-color:#f2f2f2;color:#000}";
    }

    function createRightMenu(config) {
        // 使用"filter"属性作为css样式名称
        $("<style></style>").text(createStyle(config)).appendTo($("head"));
        var li = "";
        $.each(config.navArr, function (index, conf) {
            if (conf.eventName === "line") {
                li += "<hr/>";
            } else {
                li += "<li data-type='" + conf.eventName + "'><i class='layui-icon " + conf.icon + "'></i>" + conf.title + "</li>";
            }
        });
        var ul = "<ul class='rightmenu'>" + li + "</ul>";
        $(ul).appendTo("body");
        customRightClick(config);
    }
    function customRightClick(config) {
        //桌面点击右击 todo  document 范围变小.
        $(document).on("contextmenu", "li", function (e) {
            var popupmenu = $(".rightmenu");
            popupmenu.show();
            currentActiveTabID = $(e.target).attr("lay-id");
            var l = ($(document).width() - e.clientX) < popupmenu.width() ? (e.clientX - popupmenu.width()) : e.clientX;
            var t = ($(document).height() - e.clientY) < popupmenu.height() ? (e.clientY - popupmenu.height()) : e.clientY;
            popupmenu.css({left: l,top: t}).show();
            return false;
        });
        // 点击空白处隐藏右键菜单.
        $(document).click(function () {
            $(".rightmenu").hide();
        });
        /**
         * 是否允许关闭.
         */
        function isAllowClose(id) {
            return !(tabRightMenu.pintabIDs && tabRightMenu.pintabIDs.indexOf(id) !== -1 || id === undefined);
        }
        // 点击右键菜单的功能时.
        $(".rightmenu li").click(function () {
            var event = $(this).attr("data-type");
            var tabs = $(".layui-tab[lay-filter='" + config.filter + "'] li");
            switch (event) {
                case "closeThis":
                    if (isAllowClose(currentActiveTabID)) {
                        element.tabDelete(config.filter, currentActiveTabID);
                    } else {
                        layer.msg("此页不允许关闭");
                    }
                    break;
                case "closeAll":
                    $.each(tabs, function (i) {
                        var id = $(this).attr("lay-id");
                        if (isAllowClose(id)) {
                            element.tabDelete(config.filter, id);
                        }
                    });
                    break;
                case "closeOther":
                    $.each(tabs, function (i) {
                        var id = $(this).attr("lay-id");
                        if (isAllowClose(id) && id !== currentActiveTabID) {
                            element.tabDelete(config.filter, id);
                        }
                    });
                    break;
                case "closeLeft":
                    $.each(tabs, function (i) {
                        var id = $(this).attr("lay-id");
                        if (isAllowClose(id) && id !== currentActiveTabID) {
                            element.tabDelete(config.filter, id);
                        }
                    });
                    break;
                case "closeRight":
                    var flag = false;
                    $.each(tabs, function (i) {
                        var id = $(this).attr("lay-id");
                        if (id === currentActiveTabID) {
                            flag = true;
                            return true;
                        }
                        if (flag && isAllowClose(id)) {
                            element.tabDelete(config.filter, id);
                        }
                    });
                    break;
            }
            $(".rightmenu").hide();
        });
    }
    var currentActiveTabID;
    var TabRightMenu = function() {
        this.v = "0.1";
    }
    TabRightMenu.prototype.render = function(config) {
        if (!config.filter) {
            console.error("[ERROR]使用 tabRightMenu 组件需要指定 'filter' 属性！");
            return;
        }
        this.filter = config.filter;
        this.width = config.width ? config.width : 110;// 右键弹出框的宽度，一般100~110即可
        // 固定的 ID.
        this.pintabIDs = config.pintabIDs;
        var defaultNavArr = [
            {eventName: "closeThis", title: "关闭此页"},
            {eventName: "closeAll", title: "关闭所有"},
            {eventName: "closeOther", title: "关闭其它"},
            {eventName: "closeLeft", title: "关闭左侧"},
            {eventName: "closeRight", title: "关闭右侧"}
        ];
        config.navArr = config.navArr || defaultNavArr;
        createRightMenu(config);
    };
    var tabRightMenu = new TabRightMenu();
    tabRightMenu.render({
        filter: "iframe-tabs",
        pintabIDs: ["home"],
        width: 110,
    });
    
    /* 新建或切换标签栏 */
    var tabs = function (url) {
        var item = $('[lay-url="' + url + '"]');
        if (url !== undefined && url !== '#' && item.length > 0) {
            var bootLay = $('[lay-id="' + url + '"]');
            if (bootLay.length === 0) {
                var title = item.attr("lay-icon") === 'true' ? item.html()
                    : item.children(".layui-nav-title").text();
                var iframeUrl = url.replace('//','/');
                element.tabAdd('iframe-tabs', {
                    title: title
                    , content: '<iframe src="' + iframeUrl + '" frameborder="0" class="layui-layout-iframe"></iframe>'
                    , id: url
                });
            }
            element.tabChange('iframe-tabs', url);
        }
    };

    /* 监听导航栏事件，实现标签页的切换 */
    element.on("nav(layui-nav-side)", function ($this) {
        var url = $this.attr('lay-url');
        tabs(url);
    });

    /* 监听标签栏事件，实现导航栏高亮显示 */
    element.on("tab(iframe-tabs)", function () {
        var layId = $(this).attr("lay-id");
        $(".layui-side .layui-this").removeClass("layui-this");
        $('[lay-url="' + layId + '"]').parent().addClass("layui-this");
        // 改变地址hash值
        location.hash = this.getAttribute('lay-id');
        $(".layui-tab ul").children('li').first().children('.layui-tab-close').css("display",'none');
    });
    
    /* 监听hash来切换选项卡*/
    window.onhashchange = function (e) {
        var url = location.hash.replace(/^#/, '');
        var index = $(".layui-layout-admin .layui-side .layui-nav-item")[0];
        $(index).children("a").attr("lay-icon", "true");
        if (url === "" || url === undefined) {
            url = $(index).children("[lay-url]").attr("lay-url");
        }
        tabs(url);
    };
    window.onhashchange();

    /* 最大化窗口 */
    $(".timo-screen-full").on("click", function (e) {
        e.preventDefault();
        if (!$(this).hasClass("full-on")) {
            var docElm = document.documentElement;
            var full = docElm.requestFullScreen || docElm.webkitRequestFullScreen ||
                docElm.mozRequestFullScreen || docElm.msRequestFullscreen;
            "undefined" !== typeof full && full && full.call(docElm);
        } else {
            document.exitFullscreen ? document.exitFullscreen()
                : document.mozCancelFullScreen ? document.mozCancelFullScreen()
                : document.webkitCancelFullScreen ? document.webkitCancelFullScreen()
                    : document.msExitFullscreen && document.msExitFullscreen()
        }
        $(this).toggleClass("full-on");
    });

    /* 初始化时展开子菜单 */
    $("dd.layui-this").parents(".layui-nav-child").parent()
        .addClass("layui-nav-itemed");

    /* 刷新iframe页面 */
    $(".refresh-btn").click(function () {
        location.reload();
    });

    /* AJAX请求默认选项，处理连接超时问题 */
    $.ajaxSetup({
        beforeSend: function () {
            layer.load(0);
        },
        complete: function (xhr) {
            layer.closeAll('loading');
            if (xhr.status === 401) {
                layer.confirm('session连接超时，是否重新登录？', {
                    btn: ['是', '否']
                }, function () {
                    if (window.parent.window !== window) {
                        window.top.location = window.location.pathname + '/login';
                    }
                });
            }
        }
    });

    /*  漂浮消息 */
    $.fn.Messager = function (result) {
        if (result.code === 200) {
            layer.msg(result.msg, {offset: '15px', time: 3000, icon: 1});
            setTimeout(function () {
                if (result.data === 'submit[refresh]') {
                    parent.location.reload();
                    return;
                }
                if (result.data == null) {
                    window.location.reload();
                } else {
                    window.location.href = result.data
                }
            }, 2000);
        } else {
            layer.msg(result.msg, {offset: '15px', time: 3000, icon: 2});
        }
    };

    /* 提交表单数据 */
    $(document).on("click", ".ajax-submit", function (e) {
        e.preventDefault();
        var form = $(this).parents("form");
        var url = form.attr("action");
        var serializeArray = form.serializeArray();
        $.post(url, serializeArray, function (result) {
            if (result.data == null) {
                result.data = 'submit[refresh]';
            }
            $.fn.Messager(result);
        });
    });

    /* get方式异步 */
    $(document).on("click", ".ajax-get", function (e) {
        e.preventDefault();
        var msg = $(this).data("msg");
        if (msg !== undefined) {
            layer.confirm(msg + '？', {
                title: '提示',
                btn: ['确认', '取消']
            }, function () {
                $.get(e.target.href, function (result) {
                    $.fn.Messager(result);
                });
            });
        } else {
            $.get(e.target.href, function (result) {
                $.fn.Messager(result);
            });
        }
    });

    // post方式异步-操作状态
    $(".ajax-status").on("click", function (e) {
        e.preventDefault();
        var checked = [];
        var tdcheckbox = $(".timo-table td .timo-checkbox :checkbox:checked");
        if (tdcheckbox.length > 0) {
            tdcheckbox.each(function (key, val) {
                checked.push("ids=" + $(val).attr("value"));
            });
            $.post(e.target.href, checked.join("&"), function (result) {
                $.fn.Messager(result);
            });
        } else {
            layer.msg('请选择一条记录');
        }
    });

    /* 添加/修改弹出层 */
    $(document).on("click", ".open-popup, .open-popup-param", function () {
        var title = $(this).data("title");
        var url = $(this).attr("data-url");
        if ($(this).hasClass("open-popup-param")) {
            var tdcheckbox = $(".timo-table td .timo-checkbox :checkbox:checked");
            var param = '';
            if (tdcheckbox.length === 0) {
                layer.msg('请选择一条记录');
                return;
            }
            if (tdcheckbox.length > 1 && $(this).data("type") === 'radio') {
                layer.msg('只允许选中一个');
                return;
            }
            tdcheckbox.each(function (key, val) {
                param += "ids=" + $(val).attr("value") + "&";
            });
            param = param.substr(0, param.length - 1);
            url += "?" + param;
        }
        var size = $(this).attr("data-size"), layerArea;
        if (size === undefined || size === "auto" || size === "max") {
            layerArea = ['50%', '80%'];
        }else if (size.indexOf(',') !== -1) {
            var split = size.split(",");
            layerArea = [split[0] + 'px', split[1] + 'px'];
        }
        window.layerIndex = layer.open({
            type: 2,
            title: title,
            shadeClose: true,
            maxmin: true,
            area: layerArea,
            content: [url, 'on']
        });
        if (size === "max") {
            layer.full(layerIndex);
        }
    });

    /* 关闭弹出层 */
    $(".close-popup").click(function (e) {
        e.preventDefault();
        parent.layer.close(window.parent.layerIndex);
    });

    /* 关闭弹出层 */
    $(".close-frame-popup").click(function (e) {
        e.preventDefault();
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });

    /* 显示错误信息 */
    function showError(msg) {
        layer.msg(msg, {icon: 2});
    }

    /**
     * 全局 AJAX error 处理事件.
     */
    $(document).ajaxError(function(event, response){
        console.log("错误响应状态码: ",response.status);
        console.log("错误响应结果: ",response.responseJSON);
        showError(response.responseJSON.message);
    });

    /**
     * 禁用 ajax 缓存
     */
    $.ajaxSetup({
        cache: false
    });

});

/**
 * 处理 ajax 请求结果
 */
function handlerResult(result, fn) {
    // 成功执行操作，失败提示原因
    if (result.code === 200) {
        fn(result.data);
    } else {
        showError(result.message);
    }
}

function showError(msg) {
    layer.msg(msg, {icon: 2});
}

function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the
                                                        // clock_seq_hi_and_reserved
                                                        // to 01
    s[8] = s[13] = s[18] = s[23] = "-";
  
    var uuid = s.join("");
    return uuid;
}

function ajaxJsonRequest(method, url, jsonData, handleData) {
    $.ajax({
        type : method,
        url : url,
        data : jsonData,
        dataType : "json",
        contentType : "application/json; charset=utf-8",
        success : handleData,
        error : function(xhr, textStatus, errorThrown){
            var msg = xhr.responseText;
            console.log(msg);
            var response = JSON.parse(msg);
            var code = response.code;
            var message = response.message;
            if (code == 400) {
                layer.msg(message);
            } else if (code == 401) {
                localStorage.removeItem("token");
                layer.msg("token过期，请先登录", {shift: -1, time: 1000}, function(){
                    location.href = '/login.html';
                });
            } else if (code == 403) {
                console.log("未授权:" + message);
                layer.msg('未授权');
            } else if (code == 500) {
                layer.msg('系统错误：' + message);
            }
        }
    });
    return false;
}

function ajaxDownloadFile(urlToSend, filename) {
    var req = new XMLHttpRequest();
    req.open("GET", urlToSend, true);
    req.responseType = "blob";
    req.setRequestHeader('token', localStorage.getItem("token"));
    req.onload = function (event) {
        if (this.status === 200) {
            var blob = this.response;
            if(window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveBlob(blob, fileName);
            }
            else{
                var downloadLink = window.document.createElement('a');
                var contentTypeHeader = req.getResponseHeader("Content-Type");
                downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
                downloadLink.download = filename;
                document.body.appendChild(downloadLink);
                downloadLink.click();
                document.body.removeChild(downloadLink);
            }
        }
    };
    req.send();
}

function ajaxLayuiRequest(method, url, handleData) {
    $.ajax({
        type : method,
        url : url,
        beforeSend: function () {
            $(".page-loading").show()
        },
        async: true,
        success : handleData,
        error : function(xhr, textStatus, errorThrown){
            var msg = xhr.responseText;
            console.log(msg);
            var response = JSON.parse(msg);
            var code = response.code;
            var message = response.message;
            if (code == 400) {
                layer.msg(message);
            } else if (code == 401) {
                localStorage.removeItem("token");
                layer.msg("token过期，请先登录", {shift: -1, time: 1000}, function(){
                    location.href = '/login.html';
                });
            } else if (code == 403) {
                console.log("未授权:" + message);
                layer.msg('未授权');
            } else if (code == 500) {
                layer.msg('系统错误：' + message);
            }
        },
        complete: function(){
            $(".page-loading").hide();
        }
    });
    return false;
}

function ajaxGetRequest(url, id, handleData) {
    $.ajax({
        type : "GET",
        url : url + "?id=" + id,
        dataType : "json",
        contentType : "application/json; charset=utf-8",
        success : handleData,
        error : function(xhr, textStatus, errorThrown){
            var msg = xhr.responseText;
            console.log(msg);
            var response = JSON.parse(msg);
            var code = response.code;
            var message = response.message;
            if (code == 400) {
                layer.msg(message);
            } else if (code == 401) {
                localStorage.removeItem("token");
                layer.msg("token过期，请先登录", {shift: -1, time: 1000}, function(){
                    location.href = '/login.html';
                });
            } else if (code == 403) {
                console.log("未授权:" + message);
                layer.msg('未授权');
            } else if (code == 500) {
                layer.msg('系统错误：' + message);
            }
        }
    });
    return false;
}

function ajaxFormRequest(method, url, formData, handleData) {
    $.ajax({
        type : method,
        url : url,
        data : formData,
        contentType : false,
        processData : false,
        cache : false,
        success : handleData,
        error : function(xhr, textStatus, errorThrown){
            var msg = xhr.responseText;
            console.log(msg);
            var response = JSON.parse(msg);
            var code = response.code;
            var message = response.message;
            if (code == 400) {
                layer.msg(message);
            } else if (code == 401) {
                localStorage.removeItem("token");
                layer.msg("token过期，请先登录", {shift: -1, time: 1000}, function(){
                    location.href = '/login.html';
                });
            } else if (code == 403) {
                console.log("未授权:" + message);
                layer.msg('未授权');
            } else if (code == 500) {
                layer.msg('系统错误：' + message);
            }
        }
    });
    return false;
}

function getWeekList() {
    var today = new Date();
    var data = [];
    for (var i = 0; i < 7; i++) {
        var temp = new Date(today.getTime() - 1000 * 60 * 60 * 24 * i);
        data[6 - i] = dateFormat("yyyy-MM-dd", temp);
    }
    return data;
}

function dateFormat(fmt, date) {
    var o = {
        "M+": date.getMonth() + 1,                      // 月份
        "d+": date.getDate(),                           // 日
        "h+": date.getHours(),                          // 小时
        "m+": date.getMinutes(),                        // 分
        "s+": date.getSeconds(),                        // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3),    // 季度
        "S": date.getMilliseconds()                     // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function getUrlIdParam(name) {
    var reg = new RegExp(name + "/([0-9]*)$");
    var r = window.location.href.match(reg);
    if (r != null) return unescape(r[1]);
    return null;
}

Date.prototype.format = function(fmt) {
    var o = { 
            "M+" : this.getMonth()+1,
            "d+" : this.getDate(),
            "h+" : this.getHours(),
            "m+" : this.getMinutes(),
            "s+" : this.getSeconds(),
            "q+" : Math.floor((this.getMonth()+3)/3),
            "S"  : this.getMilliseconds()
    }; 
    if(/(y+)/.test(fmt)) fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    for(var k in o) 
        if(new RegExp("("+ k +")").test(fmt)) 
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
    return fmt; 
}