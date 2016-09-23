$(function () {
    var expCode = '';
    var prices = [];
    var simplePrice = [];
    var editIndex;
    var newBuyId = '';
    var maxBuyNo = 1;
    var data = [];

    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#right").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
    //生成采购单号
    var getBuyId = $.get("/api/buy-exp-plan/get-buy-id", function (data) {
        newBuyId = data;
    });

    var getBuyNo = $.get("/api/buy-exp-plan/get-buy-no?buyId=" + newBuyId, function (data) {
        maxBuyNo = data;
    });
    //左侧列表初始化
    $("#left").datagrid({
        title: '采购单确认列表',
        singleSelect: true,
        fit: true,
        nowrap: false,
        url: '/api/buy-exp-plan/list-execute?storageCode=' + parent.config.storageCode + "&loginName=" + parent.config.loginName,
        method: 'GET',
        columns: [[{
            title: '采购单号',
            field: 'buyId',
            width: "55%"
        }, {
            title: '审核人',
            field: 'checker',
            width: "45%"
        }]],
        onClickRow: function (index, row) {
            $.get("/api/buy-exp-plan/get-detail?buyId=" + row.buyId, function (data) {
                buyIds=row.buyId;
                $("#right").datagrid("loadData", data);
            });
        }
    });

    var buyIds='';
    //右侧列表初始化
    $("#right").datagrid({
        title: '采购单详情',
        singleSelect: true,
        fit: true,
        fitColumns: true,
        nowrap: false,
        columns: [[{
            title: '序号',
            field: 'buyNo',
            width: "3%"
        }, {
            title: '代码',
            field: 'expCode',
            width: "10%"
        }, {
            title: '物品名称',
            field: 'expName',
            width: "9%"
        }, {
            title: '厂家',
            field: 'firmId',
            width: "7%"
        }, {
            title: '包装规格',
            field: 'expSpec',
            width: "7%"
        }, {
            title: '包装单位',
            field: 'units',
            width: "7%"
        }, {
            title: '计划数量',
            field: 'wantNumber',
            width: "7%"
        }, {
            title: '采购数量',
            field: 'stockNumber',
            width: "7%"
        }, {
            title: '进货价',
            field: 'purchasePrice',
            width: "7%",
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2
                }
            }
        }, {
            title: '采购金额',
            field: 'planNumber',
            width: "7%",
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2
                }
            }
        }, {
            title: '采购供应商',
            field: 'stockSupplier',
            width: "7%"
        }, {
            title: '采购员',
            field: 'buyer',
            width: "7%"
        }, {
            title: '审核数量',
            field: 'checkNumber',
            width: "7%"
        }, {
            title: '审核供应商',
            field: 'checkSupplier',
            width: "7%"
        }, {
            title: '审核人',
            field: 'checker',
            width: "7%"
        }, {
            title: '类型',
            field: 'expForm',
            width: "7%"
        }, {
            title: '库存参考数',
            field: 'stockquantityRef',
            width: "10%",
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2
                }
            }
        }, {
            title: '消耗量',
            field: 'exportquantityRef',
            width: "7%",
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2
                }
            }
        }, {
            title: '仓管员',
            field: 'storer',
            width: "5%"
        }, {
            title: '采购单号',
            field: 'buyId',
            hidden: true
        }, {
            title: '标记',
            field: 'flag',
            hidden: true
        }]],
        onClickRow: function (index, row) {
            stopEdit();

            $(this).datagrid('beginEdit', index);
            editIndex = index;
        }
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

    var refresh = function(){
        $.get('/api/buy-exp-plan/list-execute?storageCode='+parent.config.storageCode + "&loginName=" + parent.config.loginName, function (data) {
            $("#left").datagrid("loadData", data);
        });
    }
    //刷新按钮功能
    $("#refresh").on('click', function () {
        refresh();
    });
    var checkValidate = function (rows) {
        for (var i = 0; i < rows.length; i++) {
            //删除空行
            if (rows[i].expCode == undefined || rows[i].expName == undefined || rows[i].firmId == undefined || rows[i].units == undefined) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行为空请删除", 'error');
                $("#right").datagrid('selectRow', i);
                return false;
            }
            if (rows[i].purchasePrice == undefined || rows[i].purchasePrice <= 0) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行:计进价不能小于0 请重新填写", 'error');
                $("#right").datagrid('selectRow', i);
                //$("#right").datagrid('beginEdit', i);
                return false;
            }
            if (rows[i].planNumber == undefined || rows[i].planNumber <= 0) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行:采购金额不能小于0 请重新填写", 'error');
                $("#right").datagrid('selectRow', i);
                //$("#right").datagrid('beginEdit', i);
                return false;
            }
            if (rows[i].stockquantityRef == undefined || rows[i].stockquantityRef <0) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行:库存参考量不能小于0 请重新填写", 'error');
                $("#right").datagrid('selectRow', i);
                //$("#right").datagrid('beginEdit', i);
                return false;
            }
//            if (rows[i].exportquantityRef == undefined || rows[i].exportquantityRef <= 0) {
//                $.messager.alert("系统提示", "第" + (i + 1) + "行:消耗量不能小于0 请重新填写", 'error');
//                $("#right").datagrid('selectRow', i);
//                //$("#right").datagrid('beginEdit', i);
//                return false;
//            }

            //if (rows[i].flag != 5&& rows[i].flag != 3) {
            //    $.messager.alert("系统提示", "第" + (i + 1) + "行:只有采购员保存确认或主任审批的单据才能执行!", 'error');
            //    $("#right").datagrid('selectRow', i);
            //    //$("#right").datagrid('beginEdit', i);
            //    return false;
            //}
        }
        return true;
    }
    //执行按钮操作
    $("#execute").on('click', function () {
        stopEdit();
        var rows = $("#right").datagrid('getRows');
        if (rows.length == 0) {
            $.messager.alert("系统提示", "采购计划为空，不允许保存", 'error');
            return false;
        }
        if (checkValidate(rows)) {
            for (var i = 0; i < rows.length; i++) {
                if (rows[i].flag == 3) {
                    rows[i].flag = 8;
                } else if (rows[i].flag == 5) {
                    rows[i].flag = 9;
                } else {
                    rows[i].flag = 9;
                }
                rows[i].buyer = parent.config.loginName;
            }

            var updated = [];
            var deleted = $("#right").datagrid('getChanges', "deleted");
            var expChange = {};
            expChange.inserted = rows;
            expChange.updated = updated;
            expChange.deleted = deleted;

            $.postJSON("/api/buy-exp-plan/save", expChange, function (data) {
                $.messager.alert("系统提示", "执行成功", "info");
                $("#right").datagrid('loadData', {total: 0, rows: []});
                refresh();
            }, function (data) {
                $.messager.alert('失败提示', data.responseJSON.errorMessage, "error");
            });
        }
    });

    //另存为操作按钮
    $("#saveAs").on('click', function () {
        alert("saveAs");
    });

    //打印操作按钮
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/buy-exp-plan-execute.cpt"+"&buyId=" + buyIds + "&hospitalId=" + parent.config.hospitalId;
            $("#report").prop("src",cjkEncode(https));
            //$("#report").prop("src",parent.config.defaultReportPath + "buy-exp-plan-execute.cpt");
        }
    })
    $("#print").on('click',function(){
        var printData = $("#right").datagrid('getRows');
        if(printData.length<=0){
            $.messager.alert('系统提示','请先查询数据','info');
            return;
        }
        $("#printDiv").dialog('open');

    })
});