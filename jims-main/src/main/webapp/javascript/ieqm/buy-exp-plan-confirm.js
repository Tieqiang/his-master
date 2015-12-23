$(function () {
    var expCode = '';
    var editIndex;
    var newBuyId = '';
    var maxBuyNo = 1;

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
        url: '/api/buy-exp-plan/list-confirm?storageCode=' + parent.config.storageCode + "&loginName=" + parent.config.staffName,
        method: 'GET',
        columns: [[{
            title: '采购单号',
            field: 'buyId',
            width: "55%"
        }, {
            title: '仓管员',
            field: 'storer',
            width: "45%"
        }]],
        onClickRow: function (index, row) {
            $.get("/api/buy-exp-plan/get-detail?buyId="+row.buyId, function (data) {
                $("#right").datagrid("loadData",data);
            });
        }
    });

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
            hidden:true,
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
            width: "7%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    valueField: 'supplierId',
                    textField: 'supplier',
                    method: 'get',
                    url: "/api/exp-supplier-catalog/find-supplier?supplierName=" + '供应商'
                }
            }
        }, {
            title: '采购员',
            field: 'buyer',
            width: "7%"
        }, {
            title: '类型',
            field: 'expForm',
            width: "7%"
        }, {
            title: '库存参考数',
            field: 'stockquantityRef',
            width: "10%"
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
        }, {
            title: 'ID',
            field: 'id',
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
    var refresh=function(){
        $.get('/api/buy-exp-plan/list-confirm?storageCode=' + parent.config.storageCode + "&loginName=" + parent.config.staffName, function (data) {
            $("#left").datagrid("loadData", data);
        });
    };

    //刷新按钮功能
    $("#refresh").on('click', function () {
        refresh();
    });
    //新增按钮功能
    $("#add").on('click', function () {
        stopEdit();

        var rows = $('#right').datagrid("getRows");


        var buyNo = 0;
        if (rows.length > 0) {
            buyNo = $('#right').datagrid('getData').rows[rows.length - 1].buyNo;
        }
        buyNo = maxBuyNo >= buyNo ? maxBuyNo : buyNo;
        $('#right').datagrid('appendRow', {buyNo: buyNo + 1, buyId: newBuyId,buyer:parent.config.staffName});

        var addRowIndex = $("#right").datagrid('getRowIndex', rows[rows.length - 1]);
        editIndex = addRowIndex;
        $("#right").datagrid('selectRow', editIndex);
        $("#right").datagrid('beginEdit', editIndex);

    });
    //删除按钮功能
    $("#delete").on('click', function () {
        var row = $('#right').datagrid('getSelected');
        var index = $('#right').datagrid('getRowIndex', row);
        if (index == -1) {
            $.messager.alert("提示", "请选择删除的行", "info");
        } else {
            if(row.expCode== undefined){
                $('#right').datagrid('deleteRow', index);
                editIndex = undefined;
            }
        }
    });

    var checkValidate=function(rows){
        for (var i = 0; i < rows.length; i++) {
            //删除空行
            if (rows[i].expCode == undefined || rows[i].expName == undefined || rows[i].firmId == undefined || rows[i].units == undefined) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行为空请删除", 'error');
                $("#right").datagrid('selectRow', i);
                return false;
            }
            if (rows[i].stockNumber == undefined || rows[i].stockNumber <= 0) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行:采购数量不能小于0 请重新填写", 'error');
                $("#right").datagrid('selectRow', i);
                //$("#right").datagrid('beginEdit', i);
                return false;
            }
            if (rows[i].purchasePrice == undefined || rows[i].purchasePrice <= 0) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行:进货价不能小于0 请重新填写", 'error');
                $("#right").datagrid('selectRow', i);
                //$("#right").datagrid('beginEdit', i);
                return false;
            }
            //if (rows[i].planNumber == undefined || rows[i].planNumber <= 0) {
            //    $.messager.alert("系统提示", "第" + (i + 1) + "行:采购金额不能小于0 请重新填写", 'error');
            //    $("#right").datagrid('selectRow', i);
            //    //$("#right").datagrid('beginEdit', i);
            //    return false;
            //}
            if (!rows[i].stockSupplier) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行:采购供应商不能为空 请重新填写", 'error');
                $("#right").datagrid('selectRow', i);
                //$("#right").datagrid('beginEdit', i);
                return false;
            }
            if (rows[i].exportquantityRef == undefined || rows[i].exportquantityRef <= 0) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行:消耗量不能小于0 请重新填写", 'error');
                $("#right").datagrid('selectRow', i);
                //$("#right").datagrid('beginEdit', i);
                return false;
            }
        }
        return true;
    }
    //暂存按钮操作
    $("#ts").on('click', function () {
        stopEdit();

        var rows = $("#right").datagrid('getRows');
        if (rows.length == 0) {
            $.messager.alert("系统提示", "采购计划为空，不允许暂存", 'error');
            return false;
        }
        if(checkValidate(rows)){
            for (var i = 0; i < rows.length; i++) {
                rows[i].flag = 2;
                rows[i].buyer = parent.config.staffName;
            }


            var updated = [];
            var deleted = [];//$("#right").datagrid('getChanges', "deleted");
            var expChange = {};
            expChange.inserted = rows;
            expChange.updated = updated;
            expChange.deleted = deleted;
            $.postJSON("/api/buy-exp-plan/save", expChange, function (data) {
                $.messager.alert("系统提示", "暂存成功", "info");
                $("#right").datagrid('loadData', {total: 0, rows: []});
                refresh();

            }, function (data) {
                $.messager.alert('失败提示', data.responseJSON.errorMessage, "error");
            })
        }

    });

    //保存按钮操作
    $("#save").on('click', function () {
        stopEdit();
        var rows = $("#right").datagrid('getRows');
        if (rows.length == 0) {
            $.messager.alert("系统提示", "采购计划为空，不允许保存", 'error');
            return false;
        }
        if (checkValidate(rows)) {
            for (var i = 0; i < rows.length; i++) {
                rows[i].flag = 3;
                rows[i].buyer = parent.config.staffName;
            }

            var updated = [];
            var deleted = [];//$("#right").datagrid('getChanges', "deleted");
            var expChange = {};
            expChange.inserted = rows;
            expChange.updated = updated;
            expChange.deleted = deleted;

            $.postJSON("/api/buy-exp-plan/save", expChange, function (data) {
                $.messager.alert("系统提示", "保存成功", "info");
                $("#right").datagrid('loadData', {total: 0, rows: []});
                refresh();
            }, function (data) {
                $.messager.alert('失败提示', data.responseJSON.errorMessage, "error");
            })
        }

    });
    //另存为操作按钮
    $("#saveAs").on('click', function(){
        alert("saveAs");
    });
});