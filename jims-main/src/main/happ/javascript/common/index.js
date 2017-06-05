/**
 * Created by heren on 2015/9/11.
 */
var config = new common() ;

var isPublicIp=function(ip){
    if(ip=="localhost"){
        return false ;
    }
    var ips=ip.split(".") ;
    if(ips.length<4){
        return false ;
    }else{
        if(ips[0]=="10" || (ips[0]=="172" && ips[1]>='16' && ips[1]<="31") ||(ips[0]=="192" && ips[1]=="168")){
            return false ;
        }else{
            return true ;
        }
    }
}


window.addTab = function (title, href) {
    //如果路径为空，则直接返回
    if(!href){
        return ;
    }

    var hostName = window.location.hostname ;
    var reportIp=undefined ;
    var reportPort =undefined ;
    if(config.reportDict){
        if(isPublicIp(hostName)){
            reportIp = config.reportDict.remoteIp ;
            reportPort = config.reportDict.remotePort ;
        }else{
            reportIp = config.reportDict.ip ;
            reportPort = config.reportDict.port ;
        }
    }

    var tabs = $("#mainContent").tabs('tabs');
    if(tabs.length>10){
        $.messager.alert("系统提示","打开的Tab页面太多，请观不需要的，重新在打开",'info') ;
        return ;
    }
    if ($("#mainContent").tabs('exists', title)) {
        $("#mainContent").tabs('select', title);
    } else {
        var content = undefined;
        if ($.startWith(href, 'http')) {
            if (href.indexOf("OA") >= 0) {
                var userName = config.loginName.replace(/\b(0+)/gi, "");
                userName = userName.toLocaleLowerCase();
                href = String.format(href, userName, config.password);

            }
            if(href.indexOf("?")>=0){
                href = href+"&date="+new Date() ;
            }else{
                href=href +"?date="+new Date();
            }
            content = '<iframe scrolling="auto" frameborder="0"  src="' + href + '" style="width:100%;height:100%;"></iframe>';
        } else {
            if (href.indexOf("reportlet") >= 0) {
                href = "http://"+reportIp+":"+reportPort+href + "&dept_id=" + config.acctDeptId + "&emp_no=" + config.loginId + "&user_name=" + config.loginName;
                content = '<iframe scrolling="auto" frameborder="0"  src="' + href + '" style="width:100%;height:100%;"></iframe>';
            }else{
                content = '<iframe scrolling="auto" frameborder="0"  src="views' + href + '.html" style="width:100%;height:100%;"></iframe>'
            }
        }
        if (href.indexOf("OA") >= 0) {
            window.open(href);
        } else if(href.indexOf("LOCAL")>0){
            $.post(href, {}, function (data,status) {
                
            })
        }else {
            $("#mainContent").tabs('add', {
                title: title,
                content: content,
                closable: true
            });
        }

    }
}

window.updateTab = function (title, href) {
    var content = undefined;
    if ($.startWith(href, 'http')) {
        if (href.indexOf("reportlet")) {
            href = href + "&dept_id=" + config.acctDeptId;
        }
        content = '<iframe scrolling="auto" frameborder="0"  src="' + href + '" style="width:100%;height:100%;"></iframe>';
    } else {
        content = '<iframe scrolling="auto" frameborder="0"  src="views' + href + '.html" style="width:100%;height:100%;"></iframe>'
    }
    if ($("#mainContent").tabs('exists', title)) {
        $("#mainContent").tabs('select', title);
        var tab = $("#mainContent").tabs('getSelected');
        $("#mainContent").tabs('update', {
            tab: tab,
            options: {
                title: title,
                content: content,
                closable: true
            }
        });
    }
};
$(function () {
    var localApps = [];
    //var editorRow = undefined;
    $("#closeDlgNew").on('click', function () {
        $('#dlgNew').dialog('close');
    })
    $("#closeDlg").on('click', function () {
        $('#dlg').dialog('close');
    })
    $("#reLogin").on('click', function () {
        $.get("/api/login/log-out", function (data, status) {
            if (data == "ok") {
                location.href = "/login1.html"
            } else {
                $.messager.alert("退出登录失败！");
            }
        })

    })

    function getUrlParameter(name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(window.parent.location.href);
        if (results == null)    return ""; else {
            return results[1];
        }
    }

    var staffId = getUrlParameter("staffId");
    $("#changeLogin").on('click', function () {
        location.href = "/views/his/common/module-select.html?sId=" + staffId;
    })
    $("#dlgNew").dialog({
        title: '修改密码',
        width: 300,
        height: 250,
        closed: true,
        buttons: '#dlg-buttons',
        modal: true
    })
    $("#dlg").dialog({
        title: '修改密码',
        width: 300,
        height: 200,
        closed: true,
        buttons: '#dlgNew-buttons',
        modal: true
    })
    $("#cc").layout();
    $("#mainContent").tabs({
        onContextMenu: function (e, title, index) {
            e.preventDefault();
            if (index > 0) {
                $('#mm').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                }).data("tabTitle", title);
            }
        }
    })

    $("#mm").menu({
        onClick: function (item) {
            closeTab(item.id);
        }
    });
    var onlyOpenTitle = "主页";

    function closeTab(action) {
        var alltabs = $('#mainContent').tabs('tabs');
        var currentTab = $('#mainContent').tabs('getSelected');
        var allTabtitle = [];
        $.each(alltabs, function (i, n) {
            allTabtitle.push($(n).panel('options').title);
        })


        switch (action) {

            case "close":
                var currtab_title = currentTab.panel('options').title;
                $('#mainContent').tabs('close', currtab_title);
                break;
            case "closeall":
                $.each(allTabtitle, function (i, n) {
                    if (n != onlyOpenTitle) {
                        $('#mainContent').tabs('close', n);
                    }
                });
                break;
            case "closeother":
                var currtab_title = currentTab.panel('options').title;
                $.each(allTabtitle, function (i, n) {
                    if (n != currtab_title && n != onlyOpenTitle) {
                        $('#mainContent').tabs('close', n);
                    }
                });
                break;
            case "closeright":
                var tabIndex = $('#mainContent').tabs('getTabIndex', currentTab);

                if (tabIndex == alltabs.length - 1) {
                    $.messager.alert("提示", "该页已经是最右页！", "info");
                    return false;
                }
                $.each(allTabtitle, function (i, n) {
                    if (i > tabIndex) {
                        if (n != onlyOpenTitle) {
                            $('#mainContent').tabs('close', n);
                        }
                    }
                });

                break;
            case "closeleft":
                var tabIndex = $('#mainContent').tabs('getTabIndex', currentTab);
                if (tabIndex == 1) {
                    $.messager.alert("提示", "该页已经是最左页！", "info");
                    return false;
                }
                $.each(allTabtitle, function (i, n) {
                    if (i < tabIndex) {
                        if (n != onlyOpenTitle) {
                            $('#mainContent').tabs('close', n);
                        }
                    }
                });

                break;
            case "exit":
                $('#mm').menu('hide');
                break;
        }
    }


    $("#lockWindow").on('click', function () {
        $("#logwindow").window("open");
    })

    var promise = $.get('/api/login/get-login-info?date=' + new Date(), function (data) {
        if(data.errorMessage){
            $.messager.alert("系统提示",data.errorMessage,'error');

        }
        config = data;
        if(config.limitDays<30){
            $.messager.alert("系统提示","系统将在"+config.limitDays+"天后过期",'info');
        }
    })

    promise.done(function () {
        var menus = [];//菜单列表
        var menuTreeData = [];//菜单树的列表
        var url = "api/menu/list-login-module?moduleId=" + config.moduleId + "&loginName=" + config.loginName;
        var lisAll = "api/menu/list";
        $(".site_title").append(config.hospitalName + "-" + config.moduleName);

        //var url = "api/menu/list-login-module?moduleId=402886f350a6bd4f0150a6c0c4830001&loginName=000WL2"

        var menuPromise = $.get(url, function (data) {
            $.each(data, function (index, item) {
                var menu = {};
                menu.attributes = {};
                menu.id = item.id;
                menu.text = item.menuName;
                menu.state = "open";
                menu.attributes.url = item.href;
                menu.attributes.parentId = item.parentId;
                menu.attributes.index = item.position;
                menu.children = [];
                menus.push(menu);
            })

            for (var i = 0; i < menus.length; i++) {
                for (var j = 0; j < menus.length; j++) {
                    if (menus[i].id == menus[j].attributes.parentId) {
                        menus[i].children.push(menus[j]);
                        menus[i].state = 'closed';
                    }
                }

                if (menus[i].children.length > 0 && !menus[i].attributes.parentId) {
                    menuTreeData.push(menus[i]);
                }

                if (!menus[i].attributes.parentId && menus[i].children.length <= 0) {
                    menuTreeData.push(menus[i]);
                }
            }
            if (menuTreeData.length) {
                menuTreeData[0].state = 'open';
            }

            menus.sort(function (a, b) {
                return a.attributes.index - b.attributes.index;
            });
        });
        //判断是否有本地应用
        var localProgramPromise = $.get("/api/login/list-app?loginId=" + config.loginId + "&date=" + new Date(), function (data) {
            var localProgram = {
                text: '我的应用',
                state: 'open',
                children: []
            };
            localProgram.attributes={} ;
            localProgram.attributes.index=110000
            var objs = [];
            for (var i = 0; i < data.length; i++) {
                var flag = false;
                for (var j = 0; j < objs.length; j++) {
                    if (objs[j].appClass == data[i].appClass) {
                        objs[j].apps.push(data[i]);
                        flag = true;
                    }
                }
                if (!flag) {
                    var tempObje = {};
                    tempObje.appClass = data[i].appClass;
                    tempObje.apps = [];
                    tempObje.apps.push(data[i]);
                    objs.push(tempObje)
                    flag = false;
                }
            }

            for (var i = 0; i < objs.length; i++) {
                var m = {text: objs[i].appClass, children: []};
                for (var j = 0; j < objs[i].apps.length; j++) {
                    var tempM = {} ;
                    tempM.attributes={} ;
                    tempM.text=objs[i].apps[j].appName ;
                    var path = 'http://127.0.0.1:8987/api/local-listener/start?n=LOCAL&appName=' +objs[i].apps[j].appName + "&appUrl=" + objs[i].apps[j].appLocalPath
                    path = encodeURI(path);
                    tempM.attributes.url=path

                    m.children.push(tempM)
                }
                localProgram.children.push(m);
            }

            if(localProgram.children.length){
                menuTreeData.push(localProgram);
            }
        });



        //判断是否有首页，如果则打开首页
        if (config.firstPage) {
            addTab("首页", config.firstPage);
        }

        $("#uName").html(config.staffName);
        if (config.storageName != null) {
            $("#storageName").html(config.storageName + "-");
        }
        //初始化菜单
        menuPromise.done(function () {
            localProgramPromise.done(function () {
                menuTreeData.sort(function (a, b) {
                    return a.attributes.index - b.attributes.index;
                });
                $("#menuTree").tree({
                    fit: true,
                    lines: true,
                    onClick: function (node) {
                        if (node.attributes.url) {
                            addTab(node.text, node.attributes.url);
                        }
                    }
                });

                $("#menuTree").tree('loadData', menuTreeData);
                //$("#menuTree").tree('collapseAll')//默认折叠所有的选项
                var load = {};
            })
        });


    })
    $("#changePassWord").on('click', function () {
        $("#password").textbox('clear');
        $("#dlg").dialog('open').dialog('setTitle', '修改密码');
    })
    $("#saveBtnNew").on('click', function () {
        var password = psdEdit($("#password").textbox('getValue'));
        if ($("#fm").form('validate')) {
            $.get("/api/staff-dict/edit-pwd?hospitalId=" + parent.config.hospitalId + "&loginId=" + parent.config.loginId, function (data) {
                $("#dlg").dialog('close');
                if (password == data.password) {
                    $("#dlgNew").dialog('open').dialog('setTitle', '修改密码');
                    $("#new_password").textbox('clear');
                    $("#confirm_password").textbox('clear');
                } else {
                    $.messager.alert("系统提示", "密码错误请重新输入", 'error');
                }
            })
        } else {
            $.messager.alert("系统提示", "密码不能为空", 'info');
        }
    })
    $("#saveBtn").on('click', function () {
        if ($("#fm1").form('validate')) {
            var password1 = psdEdit($("#new_password").textbox('getValue'));
            var password2 = psdEdit($("#confirm_password").textbox('getValue'));
            if (password1 == password2) {
                var staffDict = {}
                staffDict.id = parent.config.loginId;
                staffDict.hospitalId = parent.config.hospitalId;
                staffDict.password = password1;
                $.postJSON("/api/staff-dict/edit-pwd-save", staffDict, function (data) {
                    $.messager.alert("系统提示", "修改密码成功", 'success');
                    $("#dlgNew").dialog("close");
                }, function (data) {
                    $.messager.alert("系统提示", "修改密码失败", 'error');
                })
                //$("#dlgNew").dialog('close')
            } else {
                $.messager.alert("系统提示", "两次输入的密码不一致，请重新输入", 'error');
            }
        } else {
            $.messager.alert("系统提示", "密码不能为空", 'info');
        }
    })
    var psdEdit = function (data) {
        switch (data.length) {
            case 1:
                data = "00000" + data;
                break;
            case 2:
                data = "0000" + data;
                break;
            case 3:
                data = "000" + data;
                break;
            case 4:
                data = "00" + data;
                break;
            case 5:
                data = "0" + data;
                break;
        }
        return data;
    }


});


