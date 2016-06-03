/**
 * Created by wei on 2016/6/2.
 */
$(function () {
    var expCode = '';
    var editIndex;
    var newBuyId = '';
    var maxBuyNo = 1;
    var currentExpCode = "";
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#right").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    };


    $("#expName").combobox({



    });



    //左侧列表初始化
    $("#left").datagrid({
        title: '备货记录',
        singleSelect: false,
        fit: true,
        nowrap: false,
        //url: '/api/buy-exp-plan/list-low?storageCode=' + parent.config.storageCode,
        method: 'GET',
        columns: [
            [
                {
                    title: '消耗品',
                    field: 'expId',
                    width: "25%"
                },
                {
                    title: '供应商',
                    field: 'supplierId',
                    width: "25%"
                },
                {
                    title: '备货时间',
                    field: 'prepareDate',
                    width: "25%"
                },
                {
                    title: '备货数量',
                    field: 'prepareCount',
                    width: "25%"
                },
                {
                    title: '操作人',
                    field: 'operator',
                    hidden: true
                },
                {
                    title: '备货人',
                    field: 'preparePersonName',
                    hidden: true
                },
                {
                    title: '备货人电话',
                    field: 'phone',
                    hidden: true
                },
                {
                    title: '备货价格',
                    field: 'price',
                    hidden: true
                }
            ]
        ],
        onDblClickRow: function (index, row) {
            var allRows = $('#right').datagrid('getRows');
            var buyNo = 0;
            if (allRows.length > 0) {
                buyNo = $('#right').datagrid('getData').rows[allRows.length - 1].buyNo;
            }
            buyNo = maxBuyNo >= buyNo ? maxBuyNo : buyNo;
            $('#right').datagrid('appendRow', {
               /* buyNo: buyNo + 1,
                expCode: row.expCode,
                expName: row.expName,
                firmId: row.firmId,
                expSpec: row.expSpec,
                units: row.units,
                purchasePrice: row.purchase,
                expForm: row.expForm,
                storer: parent.config.staffName,
                stockquantityRef: row.quantity,
                retailPrice: row.retailPrice,
                storage: row.storage,
                packSpec: row.packSpec,
                packUnit: row.packUnit,
                buyId: newBuyId*/
            });
        }
    });

    //右侧列表初始化
    $("#right").datagrid({
        title: '备货明细记录',
        singleSelect: true,
        fit: true,
        fitColumns: true,
        columns: [
            [
                {
                    title: '条形码',
                    field: 'expBarCode',
                    width: "10%"
                },
                {
                    title: '备货记录ID',
                    field: 'masterId',
                    width: "10%"
                },
                {
                    title: '是否使用',
                    field: 'useFlag',
                    width: "5%"
                },
                {
                    title: '使用日期',
                    field: 'useDate',
                    width: "10%"
                },
                {
                    title: '使用病人',
                    field: 'usePatientId',
                    width: "5%"
                },
                {
                    title: '使用科室',
                    field: 'userDept',
                    width: "10%"
                },
                {
                    title: '一级库入库单号',
                    field: 'impDocNoFirst',
                    width: "10%"

                },
                {
                    title: '一级库出库单号',
                    field: 'expDocNoFirst',
                    width: "10%"
                },
                {
                    title: '二级库入库单号',
                    field: 'impDocNoSecond',
                    width: "10%"
                },
                {
                    title: '二级库出库单号',
                    field: 'expDocNoSecond',
                    width: "10%"
                },
                {
                    title: '扫码人员',
                    field: 'operator',
                    width: "5%"

                },
                {
                    title: '是否打印',
                    field: 'printFlag',
                    width: "5%"
                }
            ]
        ],
        onClickRow: function (index, row) {
            stopEdit();

            $(this).datagrid('beginEdit', index);
            editIndex = index;
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

    $('#buyId').combobox({



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
        $('#right').datagrid('appendRow', {buyNo: buyNo + 1, buyId: newBuyId});

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
            $('#right').datagrid('deleteRow', index);
            editIndex = undefined;
        }
    });
    //加入按钮功能
    $("#join").on('click', function () {

        var leftRows = $("#left").datagrid('getSelections');
        if (leftRows.length == 0) {
            $.messager.alert("系统提示", "请先选择消耗品待选数据", 'error');
            return false;
        } else {
            var rows = $('#right').datagrid('getRows');
            var buyNo = 0;
            if (rows.length > 0) {
                buyNo = $('#right').datagrid('getData').rows[rows.length - 1].buyNo;
            }
            buyNo = maxBuyNo >= buyNo ? maxBuyNo : buyNo;
            $.each(leftRows, function (index, row) {

                buyNo = buyNo + 1;
                $('#right').datagrid('appendRow', {
                    buyNo: buyNo,
                    expCode: row.expCode,
                    expName: row.expName,
                    firmId: row.firmId,
                    packSpec: row.packSpec,
                    packUnit: row.packUnit,
                    purchasePrice: row.purchase,
                    expForm: row.expForm,
                    storer: parent.config.staffName,
                    stockquantityRef: row.quantity,
                    retailPrice: row.retailPrice,
                    storage: row.storage,
                    buyId: newBuyId
                });

            });

        }

    });

    //新建按钮功能
    $("#new").on('click', function () {

        $.get("/api/buy-exp-plan/get-buy-id", function (buyId) {
            newBuyId = buyId;
            console.log("getBuyId" + newBuyId);
            $("#right").datagrid('loadData', {total: 0, rows: []});
        });

    });


    var checkValidate = function (rows) {
        for (var i = 0; i < rows.length; i++) {
            //删除空行
            if (rows[i].expCode == undefined || rows[i].expName == undefined || rows[i].firmId == undefined || rows[i].units == undefined) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行为空请删除", 'error');
                $("#right").datagrid('selectRow', i);
                $("#right").datagrid("loadData", data);
                return false;

            }
            if (rows[i].wantNumber == undefined || rows[i].wantNumber <= 0) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行:计划数量不能小于0 请重新填写", 'error');
                $("#right").datagrid('selectRow', i);

                return false;
            }

        }
        return true;
    };

    //新增按钮
    $("#addBtn").on('click', function () {
        $('#dlg').dialog('open').dialog('center').dialog('setTitle', '新建调查问卷');
    });
    //保存按钮操作
    $("#save").on('click', function () {
        stopEdit();

        var rows = $("#right").datagrid('getRows');
        if (rows.length == 0) {
            $.messager.alert("系统提示", "备货列表为空，不允许保存", 'error');
            return false;
        }
        if (checkValidate(rows)) {
            for (var i = 0; i < rows.length; i++) {
                rows[i].buyId = newBuyId;
                rows[i].flag = 1;
            }

            var updated = [];
            var deleted = $("#right").datagrid('getChanges', "deleted");
            var expChangeVo = {};
            expChangeVo.inserted = rows;
            expChangeVo.updated = updated;
            expChangeVo.deleted = deleted;

            $.postJSON("/api/buy-exp-plan/save", expChangeVo, function (data) {
                $.messager.alert("系统提示", "保存成功", "info");
                $("#right").datagrid('loadData', {total: 0, rows: []});
                $.get("/api/buy-exp-plan/get-buy-id", function (data) {
                    newBuyId = data;
                });

            }, function (data) {
                $.messager.alert('失败提示', data.responseJSON.errorMessage, "error");
            })
        }

    });
    //打印按钮操作
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {


            $("#report").prop("src", parent.config.defaultReportPath + "buy-exp-plan.cpt");
        }
    });
    $("#print").on('click', function () {
        var printData = $("#right").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    })


});
