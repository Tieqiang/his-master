/**
 * Created by heren on 2015/9/11.
 */
var config = new common();


window.addTab = function (title, href) {
    //如果路径为空，则直接返回
    if (!href) {
        return;
    }
    var tabs = $("#mainContent").tabs('tabs');
    if (tabs.length > 10) {
        $.messager.alert("系统提示", "打开的Tab页面太多，请观不需要的，重新在打开", 'info');
        return;
    }
    if ($("#mainContent").tabs('exists', title)) {
        $("#mainContent").tabs('select', title);
    } else {
        var content = undefined;
        if ($.startWith(href, 'http')) {
            if (href.indexOf("reportlet") >= 0) {
                href = href + "&dept_id=" + config.acctDeptId + "&emp_no=" + config.loginId + "&user_name=" + config.loginName;
            }

            if (href.indexOf("OA") >= 0) {
                var userName = config.loginName.replace(/\b(0+)/gi, "");
                userName = userName.toLocaleLowerCase();
                href = String.format(href, userName, config.password);
            }
            console.log(href)
            content = '<iframe scrolling="auto" frameborder="0"  src="' + href + '" style="width:100%;height:100%;"></iframe>';
        } else {
            content = '<iframe scrolling="auto" frameborder="0"  src="views' + href + '.html" style="width:100%;height:100%;"></iframe>'
        }
        if (href.indexOf("OA") >= 0) {
            window.open(href);
        } else {
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
    var editorRow = undefined;
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
    $("#changeLogin").on('click', function () {
        location.href = "/views/his/common/module-select.html"
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
        config = data;
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

        menuTreeData.sort(function (a, b) {
            return a[0].attributes.index - b[0].attributes.index;
        });
        //$("#menuTree").accordion({
        //    fit:true
        //}) ;
        $("#uName").html(config.staffName);
        if (config.moduleName == "消耗品管理系统") {
            $("#storageName").html(config.storageName + "-");
        }
        //初始化菜单
        menuPromise.done(function () {

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
            if (menuTreeData) {
                var promiseLoad = $.get("/api/module-dict/list-tabs", function (data) {
                    load = data;
                    return load;
                });
                promiseLoad.done(function () {
                    if (load.length > 0) {
                        parent.addTab(load[0].moduleName, load[0].moduleLoad);
                    }

                });
            }
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


    //安装插件
    function IsLoad(_url, fun) {
        $.ajax({
            url: _url,
            type: "get",
            success: function () {
                //说明请求的url存在，并且可以访问
                if ($.isFunction(fun)) {
                    fun(true);
                }
            },
            error: function () {
                //说明请求的url不存在
                if ($.isFunction(fun)) {
                    fun(false);
                }
            }

        });
    }




    //更多应用程序设置

    $("#moreApp").on('click', function () {
        IsLoad("http://localhost:8987/api/hello",function(data){
            if(data){
                console.log("已经运行")
            }else{
                if(deployJava.getBrowser()=="MSIE"){
                    deployJava.launchWebStartApplication("jnlp/jimsplugin.jnlp");
                }else{
                    $.messager.alert("系统提示","非IE系列浏览器，不能够加载本地插件，无法使用部分功能","info");
                }
            }
        })
        $("#moreAppWin").window('open');

    })

    $("#moreAppWin").window({
        title: '更多应用程序',
        width: 900,
        height: 600,
        modal: true,
        closed: true,
        onOpen: function () {
            $(this).window('center');
        }
    })

    var startLocalApp = function(app){
        console.log(app) ;
        var program ={} ;
        program.appName = app.appName ;
        program.appUrl = app.appLocalPath+" "+config.loginName+";"+config.password ;
        var path = 'http://127.0.0.1:8987/api/local-listener/start?appName='+program.appName+"&appUrl="+program.appUrl
        path  =encodeURI(path) ;
        $.post(path,{},function(data){
            console.log(data) ;
        })
    }
    /***
     * 初始化函数界面
     * @param obj
     */
    var initPro = function (obj) {
        var row;
        var row1;
        var html;
        $('#appTable').empty();
        var rowAdd = "<tr><td><div class=\"tiles blue tile-group six-wide one-tall\"><div class=\"live-tile green\" id=\"Add\" data-speed=\"750\" data-dalay=\"3000\" data-mode=\"none\">\n    <span class=\"tile-title\">添加或修改</span>\n    <div><img class=\"full\" src=\"assert/images/add.png\" alt=\"1\"/></div>\n</div></div></td></tr>"
        for (var i = 0; i < obj.length; i++) {
            row = "<tr><td class=\"appClass\">" + obj[i].appClass + "</td></tr>";
            row1 = "<tr><td><div class=\"tiles blue tile-group six-wide one-tall\">";
            for (var j = 0; j < obj[i].apps.length; j++) {
                html = "<div class=\"live-tile " + obj[i].apps[j].color + "\" id=\"" + obj[i].apps[j].id + "\" data-speed=\"750\" data-dalay=\"3000\" data-mode=\"none\">\n    <span class=\"tile-title\">" + obj[i].apps[j].appName + "</span>\n    <div><img class=\"full\" src=\"" + obj[i].apps[j].picUrl + "\" alt=\"1\"/></div>\n</div>";
                row1 = row1 + html;
            }
            //var tempAdd = "<div class=\"live-tile green\" id=\"" + obj[i].appClass + "Add\" data-speed=\"750\" data-dalay=\"3000\" data-mode=\"none\">\n    <span class=\"tile-title\">添加或修改</span>\n    <div><img class=\"full\" src=\"assert/images/add.png\" alt=\"1\"/></div>\n</div>";
            //row1 = row1 + tempAdd;
            row1 = row1 + "</div></td></tr>";
            $("#appTable").append(row);
            $("#appTable").append(row1)

        }
        $("#appTable").append(rowAdd)

        $(".live-tile, .flip-list").not(".exclude").liveTile({
            click: function ($tile, tileData) {
                console.log($tile.eq(0).attr('id'));
                var id = $tile.eq(0).attr('id');
                if (id.indexOf("Add") >= 0) {
                    $("#addAppWin").window('open');
                } else {
                    for (var i = 0; i < localApps.length; i++) {
                        if (id == localApps[i].id) {
                            startLocalApp(localApps[i]) ;
                        }
                    }
                }
            }
        });

    }
    var initLocalApp = function () {

        var promise = $.get("/api/login/list-app?loginId=" + config.loginId+"&date="+new Date(), function (data) {
            localApps = data;
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
            initPro(objs);
        });


    };
    promise.done(function () {
        initLocalApp();
    })

    $("#addAppWin").window({
        title: '应用程序管理',
        width: 800,
        height: 400,
        modal: true,
        closed: true,
        onOpen: function () {
            $(this).window('center');
            $("#addAppTab").datagrid('loadData', localApps);
        }
    });

    $("#addAppTab").datagrid({
        fit: true,
        singleSelect: true,
        footer: '#tb',
        title: "本地应用程序维护",
        columns: [[{
            title: "id",
            field: 'id',
            hidden: true
        }, {
            title: '程序名称',
            field: 'appName',
            editor: {type: 'textbox', options: {}},
            width: '10%'
        }, {
            title: '路径',
            field: 'appLocalPath',
            editor: {type: 'textbox', options: {}},
            width: '30%'
        }, {
            title: '类别',
            field: 'appClass',
            editor: {type: 'textbox', options: {}},
            width: '10%'
        }, {
            title: '背景色',
            field: 'color',
            width: '10%',
            editor: {
                type: 'combobox', options: {
                    valueField: 'value',
                    textField: 'text',
                    data: [{
                        value: 'amber',
                        text: 'amber'
                    }, {
                        value: 'brown', text: 'brown'
                    }, {
                        value: 'crimson', text: 'crimson'
                    }, {
                        value: 'magenta', text: 'magenta'
                    }, {
                        value: 'indigo', text: 'indigo'
                    }, {
                        value: 'emerald', text: 'emerald'
                    }, {
                        value: 'mauve', text: 'mauve'
                    }, {
                        value: 'orange', text: 'orange'
                    }, {
                        value: 'red', text: 'red'
                    }, {
                        value: 'steel', text: 'steel'
                    }, {
                        value: 'violet', text: 'violet'
                    }, {
                        value: 'blue', text: 'blue'
                    }, {
                        value: 'cobait', text: 'cobait'
                    }, {
                        value: 'cyan', text: 'cyan'
                    }, {
                        value: 'lime', text: 'lime'
                    }, {
                        value: 'green', text: 'green'
                    }, {
                        value: 'mango', text: 'mango'
                    }, {
                        value: 'olive', text: 'olive'
                    }, {
                        value: 'pink', text: 'pink'
                    }, {
                        value: 'sienna', text: 'sienna'
                    }, {
                        value: 'teal', text: 'teal'
                    }, {
                        value: 'yellow', text: 'yellow'
                    }],
                    formatter: function (row) {
                        return "<div class=\" accentColor " + row.value + "\">" + row.text + "</div>"
                    }
                }
            }
        }, {
            title: '图片',
            field: 'picUrl',
            width: '40%',
            editor: {
                type: 'combobox', options: {
                    valueField: 'value',
                    textField: 'text',
                    data: [{
                        value: 'assert/images/BILLENTR.png',
                        text: 'BILLENTR.png'
                    }, {
                        value: 'assert/images/BILLMON.png',
                        text: 'BILLMON.png'
                    }, {
                        value: 'assert/images/ASEPSIS.png',
                        text: 'ASEPSIS.png'
                    }, {
                        value: 'assert/images/BLOOD.png',
                        text: 'BLOOD.png'
                    }, {
                        value: 'assert/images/DOCTWS.png',
                        text: 'DOCTWS.png'
                    }, {
                        value: 'assert/images/EQUIP.png',
                        text: 'EQUIP.png'
                    }, {
                        value: 'assert/images/EXAM.png',
                        text: 'EXAM.png'
                    }, {
                        value: 'assert/images/EXPSTOCK.png',
                        text: 'EXPSTOCK.png'
                    }, {
                        value: 'assert/images/IBILLING.png',
                        text: 'IBILLING.png'
                    }, {
                        value: 'assert/images/EXAM.png',
                        text: 'EXAM.png'
                    }, {
                        value: 'assert/images/INVOICE.png',
                        text: 'INVOICE.png'
                    }, {
                        value: 'assert/images/MEDCARD.png',
                        text: 'MEDCARD.png'
                    }, {
                        value: 'assert/images/NURSWS.png',
                        text: 'NURSWS.png'
                    }, {
                        value: 'assert/images/SYSTEMMGR.png',
                        text: 'SYSTEMMGR.png'
                    }, {
                        value: 'assert/images/OBILLING.png',
                        text: 'OBILLING.png'
                    }, {
                        value: 'assert/images/STOCKMGR.png',
                        text: 'STOCKMGR.png'
                    }, {
                        value: 'assert/images/PRESDISP.png',
                        text: 'PRESDISP.png'
                    }, {
                        value: 'assert/images/PREPOVMG.png',
                        text: 'PREPOVMG.png'
                    }, {
                        value: 'assert/images/PRCMGR.png',
                        text: 'PRCMGR.png'
                    }, {
                        value: 'assert/images/PHSTOCK.png',
                        text: 'PHSTOCK.png'
                    }, {
                        value: 'assert/images/OUTPDOCT.png',
                        text: 'OUTPDOCT.png'
                    }, {
                        value: 'assert/images/ORDDISP.png',
                        text: 'ORDDISP.png'
                    }, {
                        value: 'assert/images/OPERBILL.png',
                        text: 'OPERBILL.png'
                    }],
                    formatter: function (row) {
                        return "<img src=\"" + row.value + "\"/>"
                    }
                }
            }
        }]]
    })

    //添加
    $("#addApp").on('click', function () {
        if (editorRow == 0 || editorRow == undefined) {
        } else {
            $("#addAppTab").datagrid('endEdit', editorRow);
        }
        var row = $("#addAppTab").datagrid('appendRow', {});
        var rows = $("#addAppTab").datagrid('getRows');

        var rowIndex = $('#addAppTab').datagrid('getRowIndex', rows[rows.length - 1]);
        if (rowIndex >= 0) {
            $("#addAppTab").datagrid('beginEdit', rowIndex);
            editorRow = rowIndex
        }
    });

    //删除
    $("#modifyApp").on('click', function () {
        var row = $("#addAppTab").datagrid('getSelected');
        if (row) {
            var rowIndex = $("#addAppTab").datagrid('getRowIndex', row);
            if (rowIndex == editorRow) {

            } else {
                $('#addAppTab').datagrid('endEdit', editorRow);
                $('#addAppTab').datagrid('beginEdit', rowIndex);
                editorRow = rowIndex;
            }
        } else {
            $.messager.alert('系统提示', '请选择要修改的行', 'info');
        }
    });

    //删除
    $("#delApp").on('click', function () {
        if (editorRow >= 0) {
            $("#addAppTab").datagrid('endEdit', editorRow);
            editorRow = undefined;

        }

        var row = $("#addAppTab").datagrid('getSelected');
        if (row) {
            var rowIndex = $("#addAppTab").datagrid('getRowIndex', row);
            $("#addAppTab").datagrid('deleteRow', rowIndex);

        } else {
            $.messager.alert('系统提示', '请选择要删除的行', 'info');
        }
    });

    //保存
    $("#saveApp").on('click', function () {

        if (editorRow >= 0) {
            $("#addAppTab").datagrid('endEdit', editorRow);
        }

        var bean = {};
        bean.inserted = [];
        bean.inserted = $("#addAppTab").datagrid('getChanges', 'inserted');
        bean.deleted = $("#addAppTab").datagrid('getChanges', 'deleted');
        bean.updated = $("#addAppTab").datagrid('getChanges', 'updated');
        for (var i = 0; i < bean.inserted.length; i++) {
            bean.inserted[i].loginUser = config.loginId;
        }
        for (var i = 0; i < bean.updated.length; i++) {
            bean.updated[i].loginUser = config.loginId;
        }

        console.log(bean)
        $.postJSON("/api/login/save-app", bean, function (data) {
            $.messager.alert('系统提示', '保存成功', 'info');
            initLocalApp();
            initPro
        }, function (data) {
            $.messager.alert('系统提示', '保存失败', 'info');
        })
    });
    $("#shutWindow").on('click', function () {
        $("#addAppWin").window('close');
    })


});


