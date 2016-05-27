/**
 *登陆页面
 */
$(function () {
    var localApps = [];
    var editorRow = undefined;
    var startLocalApp = function (app) {
        var program = {};
        program.appName = app.appName;
        program.appUrl = app.appLocalPath + " " + parent.config.loginName + ";" + parent.config.password;
        var path = 'http://127.0.0.1:8987/api/local-listener/start?appName=' + program.appName + "&appUrl=" + program.appUrl
        path = encodeURI(path);
        $.post(path, {}, function (data,status) {
            console.log(data);
            console.log(status)
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
        var rowAdd = "<tr><td><div class=\"tiles blue tile-group seven-wide\"><div class=\"live-tile green\" id=\"Add\" data-speed=\"750\" data-dalay=\"3000\" data-mode=\"none\">\n    <span class=\"tile-title\">添加或修改</span>\n    <div><img class=\"full\" src=\"/assert/images/add.png\" alt=\"1\"/></div>\n</div></div></td></tr>"
        for (var i = 0; i < obj.length; i++) {
            row = "<tr><td class=\"appClass\">" + obj[i].appClass + "</td></tr>";
            row1 = "<tr><td><div class=\"tiles blue tile-group seven-wide\">";
            for (var j = 0; j < obj[i].apps.length; j++) {
                html = "<div class=\"live-tile " + obj[i].apps[j].color + "\" id=\"" + obj[i].apps[j].id + "\" data-speed=\"750\" data-dalay=\"3000\" data-mode=\"none\">\n    <span class=\"tile-title\">" + obj[i].apps[j].appName + "</span>\n    <div><img class=\"full\" src=\"" + obj[i].apps[j].picUrl + "\" alt=\"1\"/></div>\n</div>";
                row1 = row1 + html;
            }
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
                            startLocalApp(localApps[i]);
                        }
                    }
                }
            }
        });

    }
    var initLocalApp = function () {

        console.log(parent.config.loginId)
        var promise = $.get("/api/login/list-app?loginId=" + parent.config.loginId + "&date=" + new Date(), function (data) {
            localApps = data;
            console.log(localApps);
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
    initLocalApp();


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
                        value: '/assert/images/BILLENTR.png',
                        text: 'BILLENTR.png'
                    }, {
                        value: '/assert/images/BILLMON.png',
                        text: 'BILLMON.png'
                    }, {
                        value: '/assert/images/ASEPSIS.png',
                        text: 'ASEPSIS.png'
                    }, {
                        value: '/assert/images/BLOOD.png',
                        text: 'BLOOD.png'
                    }, {
                        value: '/assert/images/DOCTWS.png',
                        text: 'DOCTWS.png'
                    }, {
                        value: '/assert/images/EQUIP.png',
                        text: 'EQUIP.png'
                    }, {
                        value: '/assert/images/EXAM.png',
                        text: 'EXAM.png'
                    }, {
                        value: '/assert/images/EXPSTOCK.png',
                        text: 'EXPSTOCK.png'
                    }, {
                        value: '/assert/images/IBILLING.png',
                        text: 'IBILLING.png'
                    }, {
                        value: '/assert/images/EXAM.png',
                        text: 'EXAM.png'
                    }, {
                        value: '/assert/images/INVOICE.png',
                        text: 'INVOICE.png'
                    }, {
                        value: '/assert/images/MEDCARD.png',
                        text: 'MEDCARD.png'
                    }, {
                        value: '/assert/images/NURSWS.png',
                        text: 'NURSWS.png'
                    }, {
                        value: '/assert/images/SYSTEMMGR.png',
                        text: 'SYSTEMMGR.png'
                    }, {
                        value: '/assert/images/OBILLING.png',
                        text: 'OBILLING.png'
                    }, {
                        value: '/assert/images/STOCKMGR.png',
                        text: 'STOCKMGR.png'
                    }, {
                        value: '/assert/images/PRESDISP.png',
                        text: 'PRESDISP.png'
                    }, {
                        value: '/assert/images/PREPOVMG.png',
                        text: 'PREPOVMG.png'
                    }, {
                        value: '/assert/images/PRCMGR.png',
                        text: 'PRCMGR.png'
                    }, {
                        value: '/assert/images/PHSTOCK.png',
                        text: 'PHSTOCK.png'
                    }, {
                        value: '/assert/images/OUTPDOCT.png',
                        text: 'OUTPDOCT.png'
                    }, {
                        value: '/assert/images/ORDDISP.png',
                        text: 'ORDDISP.png'
                    }, {
                        value: '/assert/images/OPERBILL.png',
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
        if (editorRow == undefined) {
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
            bean.inserted[i].loginUser = parent.config.loginId;
        }
        for (var i = 0; i < bean.updated.length; i++) {
            bean.updated[i].loginUser = parent.config.loginId;
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
    IsLoad("http://localhost:8987/api/hello",function(data){
        if(data){
            console.log("已经运行")
        }else{
            if(deployJava.getBrowser()=="MSIE"){
                deployJava.launchWebStartApplication("/jnlp/jimsplugin.jnlp");
            }else{
                $.messager.alert("系统提示","非IE系列浏览器，不能够加载本地插件，无法使用部分功能","info");
            }
        }
    })


});

