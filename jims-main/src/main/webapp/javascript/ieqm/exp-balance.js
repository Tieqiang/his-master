$(function () {
    var expCode = '';

    //格式化日期函数
    function formatterDate(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m);
            return dateTime
        }
    }

    function w3(s) {
        if (!s) return new Date();
        var y = s.substring(0, 4);
        var m = s.substring(5, 7);
        if (!isNaN(y) && !isNaN(m)) {
            return new Date(y, m - 1);
        } else {
            return new Date();
        }
    }

    $('#checkMonth').datebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m);
            $('#checkMonth').datetimebox('setText', dateTime);
            $('#checkMonth').datetimebox('hidePanel');
        }
    });
    //定义expName
    $("#expName").searchbox({
        searcher: function (value, name) {
            $("#left").datagrid("unselectAll");
            var rows = $("#dg").datagrid("getRows");
            for (var i = 0; i < rows.length; i++) {
                if (rows[i].expName == value) {
                    $("#left").datagrid('selectRow', i);
                }
            }
        }
    });

    //左侧列表初始化
    $("#left").datagrid({
        singleSelect: true,
        fit: true,
        fitColumns: true,
        columns: [[{
            title: '代码',
            field: 'expCode',
            width: "30%"
        }, {
            title: '品名',
            field: 'expName',
            width: "30%"
        }, {
            title: '规格',
            field: 'packageSpec',
            width: "15%"
        }, {
            title: '单位',
            field: 'packageUnits',
            width: "15%"
        }, {
            title: '厂家',
            field: 'firmId',
            width: "20%"
        }]],
        onClickRow: function (index, row) {
            var rows = $("#right").datagrid("getRows");
            $.each(rows,function(ind,item){
                if(item.expCode== row.expCode){
                    $("#right").datagrid("selectRow",ind);
                }
            })
        }
    });
    var totalStyle = function(index,row){
        if(row.expCode=="总计"){
            return "background-color:#EAF2FF"
        }
    }
    //右侧列表初始化
    $("#right").datagrid({
        singleSelect: true,
        fit: true,
        fitColumns: true,
        nowrap:false,
        rowStyler:totalStyle,
        columns: [[{
            title: '代码',
            field: 'expCode',
            width: "15%"
        }, {
            title: '初期数',
            field: 'initialQuantity',
            width: "10%"
        }, {
            title: '初期金额',
            field: 'initialMoney',
            width: "10%"
        }, {
            title: '入库数',
            field: 'importQuantity',
            width: "10%"
        }, {
            title: '入库金额',
            field: 'importMoney',
            width: "10%"
        }, {
            title: '出库数',
            field: 'exportQuantity',
            width: "10%"
        }, {
            title: '出库金额',
            field: 'exportMoney',
            width: "10%"
        }, {
            title: '结存数',
            field: 'inventory',
            width: "10%"
        }, {
            title: '结存金额',
            field: 'inventoryMoney',
            width: "10%"
        }, {
            title: '调价盈亏',
            field: 'profit',
            width: "10%"
        }
        , {
            title: '零价初期金额',
            field: 'realInitialMoney',
            width: "10%"
        }, {
            title: '零价入库金额',
            field: 'realImportMoney',
            width: "10%"
        }, {
            title: '零价出库金额',
            field: 'realExportMoney',
            width: "10%"
        }, {
            title: '零价结存金额',
            field: 'realInventoryMoney',
            width: "10%"
        }, {
            title: '零价调价盈亏',
            field: 'realProfit',
            width: "10%"
        }
        ]],
        onClickRow: function (index, row) {
            var rows = $("#left").datagrid("getRows");
            $.each(rows, function (ind, item) {
                if (item.expCode == row.expCode) {
                    $("#left").datagrid("selectRow", ind);
                }
            })
        }
    });
    $("#top").datagrid({
        toolbar: '#ft',
        border: false
    });

    $("#bottom").datagrid({
        footer: '#tb',
        border: false
    });

    $('#dg').layout('panel', 'north').panel('resize', {height: 'auto'});
    $('#dg').layout('panel', 'south').panel('resize', {height: 'auto'});

    $("#dg").layout({
        fit: true
    });

    var showData = function(data){
        $("#left").datagrid("loadData", data);
        var sumInitialMoney = 0.00;
        var sumImportMoney = 0.00;
        var sumExportMoney = 0.00;
        var sumInventoryMoney = 0.00;
        var sumProfit = 0.00;


        $.each(data, function (index, item) {
            sumInitialMoney += item.initialMoney;
            sumImportMoney += item.importMoney;
            sumExportMoney += item.exportMoney;
            sumInventoryMoney + item.inventoryMoney;
            sumProfit += item.profit;
        });
        $("#right").datagrid("loadData", data);
        $('#right').datagrid('appendRow', {
            expCode: '总计',
            initialMoney: sumInitialMoney,
            importMoney: sumImportMoney,
            exportMoney: sumExportMoney,
            inventoryMoney: sumInventoryMoney,
            profit: sumProfit
        });
    }

    //提取按钮
    $("#search").on('click', function () {
        var checkMonth = $("#checkMonth").datetimebox('getText');
        var storageCode = parent.config.storageCode;
        var hospitalId = parent.config.hospitalId;
        $.get("/api/exp-stock/count-balance?storageCode=" + storageCode + "&checkMonth=" + checkMonth, function (data) {
            if(data>0){
                $.messager.confirm("提示信息", "本月内存在月结记录，是否查看？", function (r) {
                    if (r) {
                        $.get("/api/exp-stock/get-all-balance?storageCode=" + storageCode + "&checkMonth=" + checkMonth, function (data) {
                            if (data.length > 0) {
                                showData(data);
                            } else {
                                $.messager.alert("提示", "未查询到数据！", "info");
                                $("#left").datagrid("loadData", []);
                                $("#right").datagrid("loadData", []);
                            }
                        });
                    }else{
                        $.get("/api/exp-stock/get-last-balance?storageCode=" + storageCode + "&checkMonth=" + checkMonth, function (data) {
                            if (data.length > 0) {
                                showData(data);
                            } else {
                                $.messager.alert("提示", "未查询到数据！", "info");
                                $("#left").datagrid("loadData", []);
                                $("#right").datagrid("loadData", []);
                            }
                        });
                    }
                });
            }else{
                $.get("/api/exp-stock/get-current-balance?storageCode=" + storageCode + "&checkMonth=" + checkMonth, function (data) {
                    if (data.length > 0) {
                        showData(data);
                    } else {
                        $.messager.alert("提示", "未查询到数据！", "info");
                        $("#left").datagrid("loadData", []);
                        $("#right").datagrid("loadData", []);
                    }
                });
            }

        });
    });

    //统计操作按钮
    $("#statistic").on('click', function () {
        var checkMonth = $("#checkMonth").datetimebox('getText');
        var storageCode = parent.config.storageCode;
        var hospitalId = parent.config.hospitalId;
        $.get("/api/exp-stock/count-balance?storageCode=" + storageCode + "&checkMonth=" + checkMonth, function (data) {

        });
    });
    //打印按钮
    $("#print").on('click', function () {
        alert("print");
    });

});