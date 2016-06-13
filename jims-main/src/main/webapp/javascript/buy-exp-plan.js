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
    //生成采购单号
    var getBuyId = $.get("/api/buy-exp-plan/get-buy-id", function (data) {
        newBuyId = data;
    });

    var getBuyNo = $.get("/api/buy-exp-plan/get-buy-no?buyId=" + newBuyId, function (data) {
        maxBuyNo = data;
    });

    $("#expName").searchbox({
        searcher: function (value, name) {
            var rows = $("#left").datagrid("getRows");
            for (var i = 0; i < rows.length; i++) {
                if (rows[i].expName == value) {
                    $("#left").datagrid('selectRow', i);
                }
            }
        }
    });

    //采购单号数据加载
    $('#buyId').combobox({
        url: '/api/buy-exp-plan/list-buy-id?storageCode=' + parent.config.storageCode + "&expNo=" + parent.config.staffName,
        method: 'GET',
        valueField: 'classCode',
        textField: 'className',
        onChange: function (newValue, oldValue) {
            $.get("/api/buy-exp-plan/list-temp?buyId=" + newValue, function (data) {
                $("#right").datagrid('loadData', data);
                newBuyId = newValue;
                $.get("/api/buy-exp-plan/get-buy-no?buyId=" + newBuyId, function (data) {
                    maxBuyNo = data;
                });
            });
        }
    });

    //左侧查询过滤条件（低于下限，全部）
    $(".radios").change(function () {
        //低于下限
        if ($(this).val() == 0) {
            $.get('/api/buy-exp-plan/list-low?storageCode=' + parent.config.storageCode, function (data) {
                $("#left").datagrid("loadData", data);
            });
        }
        //全部
        if ($(this).val() == 1) {
            $.get('/api/buy-exp-plan/list-all?storageCode=' + parent.config.storageCode, function (data) {
                $("#left").datagrid("loadData", data);
            });
        }
    });

    //初始化查询条件（生成采购数量依据）
    $("#expScope").combobox({
        onSelect: function (item) {
            if (item.value == 3) {
                $("#day").textbox('enable');
            } else {
                $("#day").textbox('disable');
            }
        }
    });

    //左侧列表初始化
    $("#left").datagrid({
        title: '消耗品待选表',
        singleSelect: false,
        fit: true,
//        nowrap: false,
        url: '/api/buy-exp-plan/list-low?storageCode=' + parent.config.storageCode,
        method: 'GET',
        columns: [
            [
                {
                    title: '代码',
                    field: 'expCode',
                    width: "15%"
                },
                {
                    title: '名称',
                    field: 'expName',
                    width: "15%"
                },
                {
                    title: '规格',
                    field: 'packSpec',
                    width: "15%"
                },
                {
                    title: '单位',
                    field: 'packUnit',
                    width: "15%"
                },
                {
                    title: '厂家',
                    field: 'firmId',
                    width: "15%"
                },
                {
                    title: '进价',
                    field: 'purchase',
                    width: "15%"
                },
                {
                    title: '库存总量',
                    field: 'quantity',
                    width: "10%"
                },
                {
                    title: '类型',
                    field: 'expForm',
                    hidden: true
                },
                {
                    title: '库房',
                    field: 'storage',
                    hidden: true
                }/*, {
             title: '最小规格',
             field: 'packSpec',
             hidden: true
             }, {
             title: '最小单位',
             field: 'packUnit',
             hidden: true
             }*/
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
                buyNo: buyNo + 1,
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
                buyId: newBuyId
            });
        }
    });

    //右侧列表初始化
    $("#right").datagrid({
        title: '消耗品采购计划表',
        singleSelect: true,
        fit: true,
        fitColumns: true,
        columns: [
            [
                {
                    title: '序号',
                    field: 'buyNo',
                    width: "5%"
                },
                {
                    title: '代码',
                    field: 'expCode',
                    width: "15%",
                    editor: {
                        type: 'combogrid', options: {
                            mode: 'remote',
                            url: '/api/exp-name-dict/list-exp-name-by-input',
                            singleSelect: true,
                            method: 'GET',
                            panelWidth: 300,
                            idField: 'expCode',
                            textField: 'expCode',
                            columns: [[{
                                title: '代码',
                                field: 'expCode',
                                width: "30%"
                            }, {
                                title: '品名',
                                field: 'expName',
                                width: "40%",
                                editor: {type: 'text', options: {required: true}}
                            }, {
                                title: '拼音码',
                                field: 'inputCode',
                                width: "30%",
                                editor: 'text'
                            }]],
                            onClickRow: function (index, row) {
                                currentExpCode = row.expCode;
                                $("#stockRecordDialog").dialog('open');
                            },
                            keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                                enter: function (e) {
                                    var row = $(this).combogrid('grid').datagrid('getSelected');
                                    if (row) {
                                        currentExpCode = row.expCode;
                                        $("#stockRecordDialog").dialog('open');
                                    }
                                    $(this).combogrid('hidePanel');
                                }
                            })
                        }
                    }
                },
                {
                    title: '物品名称',
                    field: 'expName',
                    width: "15%"
                },
                {
                    title: '厂家',
                    field: 'firmId',
                    width: "9%"
                },
                {
                    title: '包装规格',
                    field: 'packSpec',
                    width: "7%"
                },
                {
                    title: '包装单位',
                    field: 'packUnit',
                    width: "7%"
                },
                {
                    title: '计划数量',
                    field: 'wantNumber',
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
                },
                {
                    title: '进货价',
                    field: 'purchasePrice',
                    width: "7%"
                },
                {
                    title: '计划金额',
                    field: 'planNumber',
                    width: "7%",
                    hidden: true,
                    editor: {
                        type: 'numberbox',
                        options: {
                            max: 99999.99,
                            size: 8,
                            maxlength: 8,
                            precision: 2
                        }
                    }
                },
                {
                    title: '类型',
                    field: 'expForm',
                    width: "9%"
                },
                {
                    title: '仓管员',
                    field: 'storer',
                    width: "5%",
                    value: parent.config.staffName
                },
                {
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
                },
                {
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
                },
                {
                    title: '零售价',
                    field: 'retailPrice',
                    width: "5%",
                    hidden: true,
                    editor: {
                        type: 'numberbox',
                        options: {
                            max: 99999.99,
                            size: 8,
                            maxlength: 8,
                            precision: 2
                        }
                    }
                },
                {
                    title: '采购单号',
                    field: 'buyId',
                    hidden: true
                },
                {
                    title: '标记',
                    field: 'flag',
                    hidden: true
                },
                {
                    title: '库房',
                    field: 'storage',
                    hidden: true
                }/*, {
             title: '最小规格',
             field: 'packSpec',
             hidden: true
             }, {
             title: '最小单位',
             field: 'packUnit',
             hidden: true
             }*/
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
//                    packSpec: row.packSpec,
//                    packUnit: row.packUnit,
                    buyId: newBuyId
                });

            });

        }


    });
    //全部加入按钮功能
    $("#joinAll").on('click', function () {

        var leftRows = $("#left").datagrid('getRows');
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
                buyId: newBuyId
            });
        });

    });
    //新建按钮功能
    $("#new").on('click', function () {

        $.get("/api/buy-exp-plan/get-buy-id", function (buyId) {
            newBuyId = buyId;
            console.log("getBuyId" + newBuyId);
            $("#right").datagrid('loadData', {total: 0, rows: []});
        });

    });

    //生成采购数按钮操作
    $("#getNum").on('click', function () {
        stopEdit();
        var rows = $("#right").datagrid('getRows');
        if (rows.length == 0) {
            $.messager.alert("系统提示", "采购计划为空，不允许计算", 'error');
            return false;
        }
        for (var i = 0; i < rows.length; i++) {

            if ($.trim(rows[i].storage) == '' || $.trim(rows[i].storage) == '' ) {
                rows[i].storage=parent.config.storageCode;
                return false;
            }
        }
        if ($("#expScope").combobox("getValue") == 1) {
            $.postJSON('/api/buy-exp-plan/generate-num-up?storageCode='+ parent.config.storageCode, rows, function (data) {

                $("#right").datagrid("loadData", data);
            });
        }
        if ($("#expScope").combobox("getValue") == 2) {
            $.postJSON('/api/buy-exp-plan/generate-num-low?storageCode='+ parent.config.storageCode, rows, function (data) {

                $("#right").datagrid("loadData", data);
            });
        }
        if ($("#expScope").combobox("getValue") == 3) {
//            var day = {
//                "storage": "DAY",
//                "planNumber": $("#day").textbox("getText")
//            };
            var day=$("#day").textbox("getText");
            for(var i=0;i<rows.length;i++){
                rows[i].planNumber=day;
            }
            $.postJSON('/api/buy-exp-plan/generate-num-sale', rows, function (data) {

                $("#right").datagrid("loadData", data);
            });
        }

    });

    var checkValidate = function (rows) {
        for (var i = 0; i < rows.length; i++) {
            //删除空行
//            if (rows[i].expCode == undefined || rows[i].expName == undefined || rows[i].firmId == undefined || rows[i].units == undefined) {
//                $.messager.alert("系统提示", "第" + (i + 1) + "行为空请删除", 'error');
//                $("#right").datagrid('selectRow', i);
//                $("#right").datagrid("loadData", data);
//                return false;
//
//            }
            if (rows[i].wantNumber == undefined || rows[i].wantNumber <= 0) {
                $.messager.alert("系统提示", "第" + (i + 1) + "行:计划数量不能小于0 请重新填写", 'error');
                $("#right").datagrid('selectRow', i);

                return false;
            }

        }
        return true;
    };
    //暂存按钮操作
    $("#ts").on('click', function () {
        stopEdit();
        var rows = $("#right").datagrid('getRows');
        if (rows.length == 0) {
            $.messager.alert("系统提示", "采购计划为空，不允许保存", 'error');
            return false;
        }
        if (checkValidate(rows)) {
            for (var i = 0; i < rows.length; i++) {
                rows[i].buyId = newBuyId;
                rows[i].flag = 0;
            }


            var updated = [];
            var deleted = $("#right").datagrid('getChanges', "deleted");
            var expChangeVo = {};
            expChangeVo.inserted = rows;
            expChangeVo.updated = updated;
            expChangeVo.deleted = deleted;
            $.postJSON("/api/buy-exp-plan/save", expChangeVo, function (data) {
                $.messager.alert("系统提示", "暂存成功", "info");
                $("#right").datagrid('loadData', {total: 0, rows: []});
                $.get("/api/buy-exp-plan/get-buy-id", function (data) {
                    newBuyId = data;
                });
                //采购单号数据加载
                $('#buyId').combobox({

                    url: '/api/buy-exp-plan/list-buy-id?storageCode=' + parent.config.storageCode + "&expNo=" + parent.config.staffName,
                    method: 'GET',
                    valueField: 'classCode',
                    textField: 'className',
                    onChange: function (newValue, oldValue) {
                        $.get("/api/buy-exp-plan/list-temp?buyId=" + newValue, function (data) {
                            $("#right").datagrid('loadData', data);
                            newBuyId = newValue;
                            $.get("/api/buy-exp-plan/get-buy-no?buyId=" + newBuyId, function (data) {
                                maxBuyNo = data;
                            });
                        });
                    }
                });

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
                //采购单号数据加载
                $('#buyId').combobox({

                    url: '/api/buy-exp-plan/list-buy-id?storageCode=' + parent.config.storageCode + "&expNo=" + parent.config.staffName,
                    method: 'GET',
                    valueField: 'classCode',
                    textField: 'className',
                    onChange: function (newValue, oldValue) {
                        $.get("/api/buy-exp-plan/list-temp?buyId=" + newValue, function (data) {
                            $("#right").datagrid('loadData', data);
                            newBuyId = newValue;
                            $.get("/api/buy-exp-plan/get-buy-no?buyId=" + newBuyId, function (data) {
                                maxBuyNo = data;
                            });
                        });
                    }
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
    $("#stockRecordDialog").dialog({
        title: '选择规格',
        width: 700,
        height: 300,
        closed: false,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            $("#stockRecordDatagrid").datagrid('load', {
                storageCode: parent.config.storageCode,
                expCode: currentExpCode,
                hospitalId: parent.config.hospitalId
            });
            $("#stockRecordDatagrid").datagrid('selectRow', 0)
        }
    });

    $("#stockRecordDatagrid").datagrid({
        singleSelect: true,
        fit: true,
        fitColumns: true,
        url: '/api/exp-stock/stock-record/',
        method: 'GET',
        columns: [
            [
                {
                    title: '代码',
                    field: 'expCode',
                    align: 'center',
                    width: '8%'
                },
                {
                    title: '名称',
                    field: 'expName',
                    align: 'center',
                    width: '8%'
                },
                {
                    title: '规格',
                    field: 'expSpec',
                    align: 'center',
                    width: '6%'
                },
                {
                    title: '最小规格',
                    field: 'minSpec',
                    align: 'center',
                    width: '8%'
                },
                {
                    title: '单位',
                    field: 'units',
                    align: 'center',
                    width: '6%'
                },
                {
                    title: '最小单位',
                    field: 'minUnits',
                    align: 'center',
                    width: '8%'
                },
                {
                    title: '厂家',
                    field: 'firmId',
                    align: 'center',
                    width: '8%'
                },
                {
                    title: '批发价',
                    field: 'tradePrice',
                    align: 'center',
                    width: '8%'
                },
                {
                    title: '零售价',
                    field: 'retailPrice',
                    align: 'center',
                    width: '8%'
                },
                {
                    title: '注册证号',
                    field: 'registerNo',
                    align: 'center',
                    width: '8%'
                },
                {
                    title: '许可证号',
                    field: 'permitNo',
                    align: 'center',
                    width: '8%'
                },
                {
                    title: '类型',
                    field: 'expForm',
                    align: 'center',
                    width: '8%'
                }, {
                    title: '库存量',
                    field: 'quantity',
                    align: 'center',
                    width: '8%'
                }
            ]
        ],
        onDblClickRow: function (index, row) {
            $("#right").datagrid('endEdit', editIndex);
            var rows = $("#right").datagrid('getRows') ;
            for(var i = 0;i<rows.length;i++){
                if(rows[i].expCode == row.expCode && rows[i].packageSpec==row.expSpec && rows[i].firmId ==row.firmId){
                    $.messager.alert("系统提示","同批次、同厂商、同规格的【"+row.expName+"】记录已经存在，请不要重复添加",'info');
                    return ;
                }
            }
            var rowDetail = $("#right").datagrid('getData').rows[editIndex];

            rowDetail.expName=row.expName;
            rowDetail.firmId=row.firmId;
            rowDetail.packSpec=row.expSpec;
            rowDetail.packUnit=row.units;
            rowDetail.purchasePrice=row.tradePrice;
            rowDetail.storer= parent.config.staffName;
            rowDetail.expForm=row.expForm;
            rowDetail.stockquantityRef=row.quantity;
            //rowDetail.invoiceDate = setDefaultDate();
            //rowDetail.invoiceNo = row.invoiceNo;

            $("#right").datagrid('refreshRow', editIndex);
            $("#stockRecordDialog").dialog('close');
            $("#right").datagrid('beginEdit', editIndex);


        }
        //onLoadSuccess: function (data) {
        //    if (data.total == 0 && editIndex != undefined) {
        //        $.messager.alert('系统提示', '无法获取产品的价格信息！', 'info');
        //        $("#stockRecordDialog").dialog('close');
        //    }
        //},



//        onClickRow: function (index, row) {
//            /**
//             *    expCode: row.expCode,
//             //                                    expName:row.expName,
//             firmId: row.firmId,
//             //                                    packSpec: row.packSpec,
//             //                                    packUnit:row.packUnit,
//             //                                    wantNumber: 0,
//             //                                    purchasePrice: row.purchasePrice,
//             //                                    planNumber: 0,
//             //                                    expForm: row.expForm,
//             //                                    storer: parent.config.staffName,
//             //                                    retailPrice: row.retailPrice
//             *
//             */
////            console.log(row);
//            var expCodeEdit = $("#right").datagrid('getEditor', {index: editIndex, field: 'expCode'});
////            $(expCodeEdit.target).textbox('setValue', row.expCode);
////            expName:row.expName
//            console.log(expCodeEdit.target+"(((((((((((((((999");
//            var expNameEd = $("#right").datagrid('getEditor', {index: editIndex, field: 'expName'});
//            $(expNameEd.target).textbox('setValue', row.expName);
//
//            var packageSpecEd = $("#right").datagrid('getEditor', {index: editIndex, field: 'packSpec'});
//            $(packageSpecEd.target).textbox('setValue', row.expSpec);
//
//            var packageUnitsEd = $("#right").datagrid('getEditor', {index: editIndex, field: 'packUnit'});
//            $(packageUnitsEd.target).textbox('setValue', row.units);
//
//            var batchNoEd = $("#right").datagrid('getEditor', {index: editIndex, field: 'planNumber'});
//            $(batchNoEd.target).textbox('setValue', 0);
//
//            var purchasePriceEd = $("#right").datagrid('getEditor', {index: editIndex, field: 'retailPrice'});
//            $(purchasePriceEd.target).textbox('setValue', row.retailPrice);
//
//            var amountEd = $("#right").datagrid('getEditor', {index: editIndex, field: 'storer'});
//            $(amountEd.target).textbox('setValue', parent.config.staffName);
//
//            var expFormEd = $("#right").datagrid('getEditor', {index: editIndex, field: 'expForm'});
//            $(expFormEd.target).textbox('setValue', row.expForm);
//
//            var firmIdEd = $("#right").datagrid('getEditor', {index: editIndex, field: 'firmId'});
//            $(firmIdEd.target).textbox('setValue', row.firmId);
//            var inventoryEd = $("#right").datagrid('getEditor', {index: editIndex, field: 'purchasePrice'});
//            $(inventoryEd.target).textbox('setValue', row.purchasePrice);
//
//            $("#right").datagrid('endEdit', editIndex);
//            $("#right").datagrid('beginEdit', editIndex);
//            $("#stockRecordDialog").dialog('close');
//        }

    });
});
