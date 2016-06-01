/**
 * Created by heren on 2015/10/23.
 */
$(function () {

    var exportToFlag = undefined;//退库的时候，标志，用于区分是否退货给供应商。

    function formatterYMD(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d);
            return dateTime
        }
    }

    //格式化日期函数
    function formatterDate(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var h = date.getHours();
            var mm = date.getMinutes();
            var s = date.getSeconds();
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' '
                + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
            return dateTime
        }
    }

    function w3(s) {
        if (!s) return new Date();
        var y = s.substring(0, 4);
        var m = s.substring(5, 7);
        var d = s.substring(8, 10);
        var h = s.substring(11, 14);
        var min = s.substring(15, 17);
        var sec = s.substring(18, 20);
        if (!isNaN(y) && !isNaN(m) && !isNaN(d) && !isNaN(h) && !isNaN(min) && !isNaN(sec)) {
            return new Date(y, m - 1, d, h, min, sec);
        } else {
            return new Date();
        }
    }

    $.extend($.fn.datagrid.methods, {
        getChecked: function (jq) {
            var rr = [];
            var rows = jq.datagrid('getRows');
            jq.datagrid('getPanel').find('div.datagrid-cell input:checked').each(function () {
                var index = $(this).parents('tr:first').attr('datagrid-row-index');
                rr.push(rows[index]);
            });
            return rr;
        }
    });
    var documentNo;//入库单号
    var flag;
    var editIndex;
    var currentExpCode;
    var exportFlag;
    var upStorageFlag;
    var panelHeight = $(window).height - 300;
    //库房字典
    var depts = [];
    var deptsBack = [];
    var saveFlag;
    var promise = $.get("/api/exp-storage-dept/listLevelDown?hospitalId=" + parent.config.hospitalId + "&storageCode=" + parent.config.storageCode, function (data) {
        depts = data;
        deptsBack = data;
        return depts;
    });

    //出库日期
    $('#exportDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#startTime').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
            $('#exportDate').datetimebox('setText', dateTime);
            $('#exportDate').datetimebox('hidePanel');
        }
    });

    var suppliers = [];
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
    });
    /**
     * 设置明细信息
     */
    $("#exportDetail").datagrid({
        fit: true,
        fitColumns: true,
        footer: '#ft',
        toolbar: '#expExportMaster',
        title: '消耗品出库单',
        columns: [[{
            title: "产品代码",
            field: 'expCode',
            width: "7%"
        }, {
            title: '产品名称',
            field: 'expName',
            editor: {
                type: 'combogrid', options: {
                    mode: 'remote',
                    url: '/api/exp-name-dict/list-exp-name-by-input',
                    singleSelect: true,
                    method: 'GET',
                    panelWidth: 200,
                    idField: 'expName',
                    textField: 'expName',
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
                        $('#exportDetail').datagrid('updateRow', {
                            index: editIndex,
                            row: {
                                expCode: row.expCode,
                                expName: row.expName
                            }
                        });
                        currentExpCode = row.expCode;
                        $("#stockRecordDialog").dialog('open');
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            $(this).combogrid('hidePanel');
                            if (row) {
                                $('#exportDetail').datagrid('updateRow', {
                                    index: editIndex,
                                    row: {
                                        expCode: row.expCode,
                                        expName: row.expName
                                    }
                                });
                                currentExpCode = row.expCode;
                                $("#stockRecordDialog").dialog('open');
                            }

                        }
                    })
                }
            },
            width: "7%"
        }, {
            title: '包装规格',
            field: 'packageSpec'
        }, {
            title: '包装单位',
            field: 'packageUnits'
        }, {
            title: '产品类型',
            field: 'expForm'
        }, {
            title: '数量',
            field: 'quantity',
            value: 0,
            editor: {
                type: 'numberbox', options: {
                    onChange: function (newValue, oldValue) {
                        var row = $("#exportDetail").datagrid('getData').rows[editIndex];
                        var amountEd = $("#exportDetail").datagrid('getEditor', {index: editIndex, field: 'amount'});
                        var value = $('#receiver').combobox('getValue');
                        if (value != null && value != "") {
                            if (exportToFlag == "toSupplier") {//进价
                                $(amountEd.target).numberbox('setValue', newValue * row.purchasePrice);
                            } else {//
                                $(amountEd.target).numberbox('setValue', newValue * row.retailPrice);
                            }
                        } else {
                            $.messager.alert("系统提示", "请选择出库单位", "error");
                        }

                        var rows = $("#exportDetail").datagrid('getRows');
                        var totalAmount = 0;
                        var backAmount = 0;
                        for (var i = 0; i < rows.length; i++) {
                            var rowIndex = $("#exportDetail").datagrid('getRowIndex', rows[i]);
                            if (rowIndex == editIndex) {
                                continue;
                            }
                            totalAmount += Number(rows[i].retailPrice * rows[i].quantity);
                            backAmount += Number(rows[i].purchasePrice * rows[i].quantity);
                        }
                        if (totalAmount) {
                            totalAmount += newValue * row.retailPrice;
                        } else {
                            totalAmount = newValue * row.retailPrice;
                        }
                        if (backAmount) {
                            backAmount += newValue * row.purchasePrice;
                        } else {
                            backAmount = newValue * row.purchasePrice;
                        }
                        if (exportToFlag == "toSupplier") {
                            $("#accountReceivable").numberbox('setValue', backAmount);
                            upStorageFlag = false;
                        }else{
                            $("#accountReceivable").numberbox('setValue', totalAmount);
                            upStorageFlag = false;
                        }
                    }
                }
            }, formatter: function (value, row, index) {
                if (value > row.disNum) {
                    $.messager.alert('系统消息', '第' + (parseInt(index + 1)) + '行出库数量超过库存量，请重新填编辑。', 'info');
                    value = 0;
                    exportFlag = false;
                } else {
                    exportFlag = true;
                }
                return value;
            }
        }, {
            title: '进价',
            field: 'purchasePrice',
            width: "5%",
            precision: '2'
        }, {
            title: '售价',
            field: 'retailPrice',
            width: '5%',
            precision: '2'
        }, {
            title: '金额',
            field: 'amount',
            editor: {
                type: 'numberbox', options: {
                    precision: '2',
                    editable: false,
                    disabled: true
                }
            },
            width: "5%"
        }, {
            title: '厂家',
            field: 'firmId',
            width: "7%"
        }, {
            title: '分摊方式',
            field: 'assignCode',
            editor: {
                type: 'combobox', options: {
                    url: '/api/exp-assign-dict/list',
                    valueField: 'assignCode',
                    textField: 'assignName',
                    method: 'GET',
                    onLoadSuccess: function () {
                        var data = $(this).combobox('getData');
                        $(this).combobox('select', data[0].assignName);
                    }
                },
                width: "8%"
            }
        }, {
            title: '备注',
            field: 'memo',
            editor: {type: 'textbox', options: {}},
            width: "8%"
        }, {
            title: '批号',
            field: 'batchNo'
        }, {
            title: '有效期',
            field: 'expireDate',
            formatter: formatterDate,
            width: "12%"
        }, {
            title: '生产日期',
            field: 'producedate',
            formatter: formatterDate,
            width: "12%"
        }, {
            title: '消毒日期',
            field: 'disinfectdate',
            formatter: formatterDate,
            width: "12%"
        }, {
            title: '单位',
            field: 'units'
        }, {
            title: '结存量',
            field: 'disNum',
            editor: {
                type: 'numberbox', options: {
                    //precision: '2',
                    editable: false,
                    disabled: true
                }
            }
        }, {
            title: '灭菌标志',
            field: 'killflag',
            width: '8%',
            align: 'center',
            formatter: function (value, row, index) {
                if (value == '1') {
                    return '<input type="checkbox" name="DGC" checked="true" style="width: 15px" />';
                }
                if (value == '0') {
                    return '<input type="checkbox" name="DGC" style="width: 15px"/>';
                } else {//if(value==undefined){
                    return '<input type="checkbox" name="DGC" style="width: 15px"/>';
                }
            },
            editor: {type: 'combobox', options: {
                valueField:'value',
                textField:'title',
                data:[{
                    value:'1',
                    title:'已灭菌'
                },{
                    title:'未灭菌',
                    value:'0'
                }]
            }}

        }, {
            title: '子包装1',
            field: 'subPackage1',
            hidden: 'true'
        }, {
            title: '子单位1',
            field: 'subPackageUnits1',
            hidden: 'true'
        }, {
            title: '子规格1',
            field: 'subPackageSpec1',
            hidden: 'true'
        }, {
            title: '子包装2',
            field: 'subPackage2',
            hidden: 'true'
        }, {
            title: '子单位2',
            field: 'subPackageUnits2',
            hidden: 'true'
        }, {
            title: '子规格2',
            field: 'subPackageSpec2',
            hidden: 'true'
        }, {
            title: '入库单据号',
            field: 'importDocumentNo',
            hidden: 'true'
        }, {
            title: '零售价',
            field: 'retailPrice',
            hidden: 'true'
        }, {
            title: '批发价',
            field: 'tradePrice',
            hidden: 'true'
        }]],
        onClickCell: function (index, field, row) {
            if (editIndex == undefined) {
                editIndex = index;
            } else {
                $(this).datagrid('endEdit', editIndex);
                editIndex = index;
            }

            $(this).datagrid('beginEdit', editIndex);
            var ed = $(this).datagrid('getEditor', {index: index, field: field});
        }
    });
    //出库类别
    $("#exportClass").combobox({
        url: '/api/exp-export-class-dict/list',
        valueField: 'exportClass',
        textField: 'exportClass',
        method: 'GET',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            for (var i = 0; i < data.length; i++) {
                if (data[i].exportClass == "发放出库") {
                    $(this).combobox('select', data[i].exportClass);
                }
            }
        },
        onChange: function (newValue, oldValue) {
            if (newValue.indexOf("退") >= 0) {
                $('#receiver').combogrid('enable');
                $.messager.defaults.ok = "供货商";
                $.messager.defaults.cancel = "上级库房";
                $.messager.confirm('系统消息', '请选择退货对象', function (r) {
                    if (r) {
                        exportToFlag = "toSupplier";
                        depts = new Array;
                        upStorageFlag = true;
                        for (var i = 0; i < suppliers.length; i++) {
                            var dept = {};
                            dept.storageName = suppliers[i].supplierName;
                            dept.storageCode = suppliers[i].supplierCode;
                            dept.disburseNoPrefix = suppliers[i].inputCode;
                            depts.push(dept)
                        }
                        $('#receiver').combogrid('grid').datagrid('loadData', depts);
                    } else {
                        exportToFlag = "toHigherStorage"
                    }
                    $.messager.defaults.ok = "确定";
                    $.messager.defaults.cancel = "取消";
                });
            } else if (newValue == "盘亏出库") {
                $('#receiver').combogrid('disable');
            } else {
                $('#receiver').combogrid('enable');
                depts = deptsBack;
            }
            $('#receiver').combogrid('grid').datagrid('loadData', depts);
        }
    });

    $("#documentNo").textbox({
        disabled: true
    });
    //创建出库单号
    var createNewDocument = function (subStorageCode) {
        var storage;
        var promise = $.get('/api/exp-sub-storage-dict/list-by-storage?storageCode=' + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId, function (data) {
            $.each(data, function (index, item) {
                if (item.subStorage == subStorageCode) {
                    storage = item;
                }
            });

            if (storage) {
                if (storage.exportNoPrefix.length <= 4) {
                    documentNo = storage.exportNoPrefix + '000000'.substring((storage.exportNoAva + "").length) + storage.exportNoAva;
                } else if (storage.exportNoPrefix.length = 5) {
                    documentNo = storage.exportNoPrefix + '00000'.substring((storage.exportNoAva + "").length) + storage.exportNoAva;
                } else if (storage.exportNoPrefix.length = 6) {
                    documentNo = storage.exportNoPrefix + '0000'.substring((storage.exportNoAva + "").length) + storage.exportNoAva;
                }
            }
        });

        return promise;
    }
    //子库房
    $("#subStorage").combobox({
        url: '/api/exp-sub-storage-dict/list-by-storage?storageCode=' + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId,
        valueField: 'subStorage',
        textField: 'subStorage',
        method: 'GET',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('select', data[0].subStorage);
            }
        },
        onChange: function (newValue, oldValue) {
            var promise = createNewDocument(newValue);
            promise.done(function () {
                $("#documentNo").textbox('setValue', documentNo);
            })
        }
    });
    //开支类别
    $("#fundItem").combobox({
        url: '/api/exp-fund-item-dict/list',
        valueField: 'fundItem',
        textField: 'fundItem',
        method: 'GET',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('select', data[0].fundItem);
            }
        }
    });

    //负责人数据加载
    $('#principal').combogrid({
        panelWidth: 500,
        idField: 'id',
        textField: 'name',
        loadMsg: '数据正在加载',
        url: '/api/staff-dict/list-by-hospital?hospitalId=' + parent.config.hospitalId,
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'job', title: '工种', width: 150, align: 'center'},
            {field: 'name', title: '姓名', width: 150, align: 'center'},
            {field: 'loginName', title: '用户名', width: 150, align: 'center'},
            {field: 'inputCode', title: '拼音码', width: 150, align: 'center'}
        ]],
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        pageSize: 50,
        pageNumber: 1
    });

    //保管员数据加载
    $('#storekeeper').combogrid({
        panelWidth: 500,
        idField: 'id',
        textField: 'name',
        loadMsg: '数据正在加载',
        url: '/api/staff-dict/list-by-hospital?hospitalId=' + parent.config.hospitalId,
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'job', title: '工种', width: 150, align: 'center'},
            {field: 'name', title: '姓名', width: 150, align: 'center'},
            {field: 'loginName', title: '用户名', width: 150, align: 'center'},
            {field: 'inputCode', title: '拼音码', width: 150, align: 'center'}
        ]],
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        pageSize: 50,
        pageNumber: 1
    });

    //领取人数据加载
    $('#acctoperator').combogrid({
        panelWidth: 500,
        idField: 'id',
        textField: 'name',
        loadMsg: '数据正在加载',
        url: '/api/staff-dict/list-by-hospital?hospitalId=' + parent.config.hospitalId,
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'job', title: '工种', width: 150, align: 'center'},
            {field: 'name', title: '姓名', width: 150, align: 'center'},
            {field: 'loginName', title: '用户名', width: 150, align: 'center'},
            {field: 'inputCode', title: '拼音码', width: 150, align: 'center'}
        ]],
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        pageSize: 50,
        pageNumber: 1
    });

    promise.done(function () {
        $("#receiver").combogrid({
            idField: 'storageCode',
            textField: 'storageName',
            data: depts,
            panelWidth: 300,
            columns: [[{
                title: '科室名称',
                field: 'storageName',
                width: 200
            }, {
                title: '科室代码',
                field: 'storageCode',
                width: 50
            }, {
                title: '输入码',
                field: 'disburseNoPrefix',
                width: 50
            }]]
        })
    });
    $("#addRow").on('click', function () {
        flag = 0;
        $("#exportDetail").datagrid('appendRow', {});
        var rows = $("#exportDetail").datagrid('getRows');
        var appendRowIndex = $("#exportDetail").datagrid('getRowIndex', rows[rows.length - 1]);

        if (editIndex || editIndex == 0) {
            $("#exportDetail").datagrid('endEdit', editIndex);
        }
        editIndex = appendRowIndex;
        $("#exportDetail").datagrid('beginEdit', editIndex);


    });
    $("#stockRecordDatagrid").datagrid({
        singleSelect: true,
        fit: true,
        fitColumns: true,
        url: '/api/exp-stock/stock-export-record/',
        method: 'GET',
        columns: [[{
            title: '代码',
            field: 'expCode'
        }, {
            title: '名称',
            field: 'expName'
        }, {
            title: '包装规格',
            field: 'packageSpec'
        }, {
            title: '数量',
            field: 'quantity'
        }, {
            title: '包装单位',
            field: 'units'
        }, {
            title: '基本规格',
            field: 'minSpec'
        }, {
            title: '基本单位',
            field: 'minUnits'
        }, {
            title: '厂家',
            field: 'firmId'
        }, {
            title: '进货价',
            field: 'purchasePrice'
        }, {
            title: '批发价',
            field: 'tradePrice'
        }, {
            title: '零售价',
            field: 'retailPrice'
        }, {
            title: '批号',
            field: 'batchNo'
        }, {
            title: '有效期',
            field: 'expireDate',
            formatter: formatterDate
        }, {
            title: '入库单号',
            field: 'documentNo'
        }, {
            title: '生产日期',
            field: 'producedate',
            formatter: formatterDate
        }, {
            title: '消毒日期',
            field: 'disinfectdate',
            formatter: formatterDate
        }, {
            title: '产品类别',
            field: 'expForm'
        }, {
            title: '是否包装',
            field: 'singleGroupIndicator',
            formatter: function (value, row, index) {
                if (value == "1") {
                    value = "是";
                }
                else if (value == "2") {
                    value = "否";
                }
                else if (value == "S") {
                    value = "是";
                } else {
                    value = "是";
                }
                return value;
            }
        }, {
            title: '子包装1',
            field: 'subPackage1'
        }, {
            title: '子单位1',
            field: 'subPackageUnits1'
        }, {
            title: '子规格1',
            field: 'subPackageSpec1'
        }, {
            title: '子包装2',
            field: 'subPackage2'
        }, {
            title: '子单位2',
            field: 'subPackageUnits2'
        }, {
            title: '子规格2',
            field: 'subPackageSpec2'
        }, {
            title: '灭菌标识',
            field: 'killflag',
            editor: {type: 'combobox', options: {
                valueField:'value',
                textField:'title',
                data:[{
                    value:'1',
                    title:'已灭菌'
                },{
                    title:'未灭菌',
                    value:'0'
                }]
            }}
        }]],
        onLoadSuccess: function (data) {
            var dat = {};
            dat = $("#stockRecordDatagrid").datagrid('getData');
            console.log(dat);
            if (dat.total == 0 && editIndex != undefined) {
                $("#exportDetail").datagrid('endEdit', editIndex);
                $("#stockRecordDialog").dialog('close');
                $.messager.alert('系统提示', '该子库房暂无此产品,请重置产品名称或子库房！', 'info');
                $("#exportDetail").datagrid('beginEdit', editIndex);
            }
        },
        onClickRow: function (index, row) {
            var rows = $("#exportDetail").datagrid('getRows');
            for (var i = 0; i < rows.length; i++) {
                if (rows[i].expCode == row.expCode && rows[i].packageSpec == row.packageSpec && rows[i].batchNo == row.batchNo && rows[i].firmId == row.firmId) {
                    $.messager.alert("系统提示", "同批次、同厂商、同规格的【" + row.expName + "】记录已经存在，请不要重复添加", 'info');
                    return;
                }
            }

            $("#exportDetail").datagrid('endEdit', editIndex);
            var rowDetail = $("#exportDetail").datagrid('getData').rows[editIndex];
            rowDetail.expName = row.expName;
            rowDetail.expForm = row.expForm;
            rowDetail.expCode = row.expCode;
            rowDetail.packageSpec = row.packageSpec;
            rowDetail.expSpec = row.minSpec;
            rowDetail.units = row.minUnits;
            rowDetail.packageUnits = row.units;
            rowDetail.disNum = row.quantity;
            rowDetail.purchasePrice = row.purchasePrice;
            rowDetail.amount = 0;
            rowDetail.firmId = row.firmId;
            rowDetail.batchNo = row.batchNo;
            rowDetail.expireDate = row.expireDate;
            rowDetail.producedate = row.producedate;
            rowDetail.disinfectdate = row.disinfectdate;
            rowDetail.killflag = row.killflag;
            rowDetail.subPackage1 = row.subPackage1;
            rowDetail.subPackageUnits1 = row.subPackageUnits1;
            rowDetail.subPackageSpec1 = row.subPackageSpec1;
            rowDetail.subPackage2 = row.subPackage2;
            rowDetail.subPackageUnits2 = row.subPackageUnits2;
            rowDetail.subPackageSpec2 = row.subPackageSpec2;
            rowDetail.importDocumentNo = row.documentNo;
            rowDetail.retailPrice = row.retailPrice;
            rowDetail.tradePrice = row.tradePrice;
            $("#exportDetail").datagrid('refreshRow', editIndex);
            $("#stockRecordDialog").dialog('close');
            $("#exportDetail").datagrid('beginEdit', editIndex);
        }
    });
    $("#stockRecordDialog").dialog({
        title: '选择规格',
        //style="width:500px;height:300px;
        width: 1000,
        height: 300,
        closed: false,
        catch: false,
        modal: true,
        closed: true,
        onBeforeOpen: function () {
            var subStorage = $("#subStorage").combobox("getValue");
            $("#stockRecordDatagrid").datagrid('load', {
                storageCode: parent.config.storageCode,
                expCode: currentExpCode,
                hospitalId: parent.config.hospitalId,
                subStorage: subStorage
            });

            var s = $("#stockRecordDatagrid").datagrid('getRows');
            $("#stockRecordDatagrid").datagrid('selectRow', 0);
        },
        onOpen: function () {

        }
    });
    /**
     * 查询按钮
     */
    $("#searchBtn").on('click', function () {
        parent.addTab('出库单据查询', '/his/ieqm/exp-export-document-search');
    })
    /**
     * 删除按钮
     */
    $("#delRow").on('click', function () {
        var row = $("#exportDetail").datagrid('getSelected');
        if (row) {
            var index = $("#exportDetail").datagrid('getRowIndex', row);
            $("#exportDetail").datagrid('deleteRow', index);
            if (editIndex == index) {
                editIndex = undefined;
            }
        } else {
            $.messager.alert("系统提示", "请选择要删除的行", 'info');
        }
    });

    /**
     * 进行数据校验
     */
    var dataValid = function () {
        var rows = $("#exportDetail").datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].quantity == 0) {
                $.messager.alert("系统提示", "第" + parseInt(i + 1) + "行出库数量为0 请重新填写", 'error');
                $("#exportDetail").datagrid('beginEdit', i);
                return false;
            }
        }

        if (rows.length == 0) {
            $.messager.alert("系统提示", "明细记录为空，不允许保存", 'error');
            return false;
        }

        //判断供货商是否为空
        var receiver = $("#receiver").combogrid('getValue');
        var exportClass1 = $("#exportClass").combobox('getValue');
        if (!receiver && exportClass1 != '盘亏出库') {
            $.messager.alert("系统提示", "产品出库，发往不能为空", 'error');
            return false;
        }

        var exportDate = $("#exportDate").datetimebox('calendar').calendar('options').current;
        if (!exportDate) {
            $.messager.alert("系统提示", "产品出库，出库时间不能为空", 'error');
            return false;
        }
        var rows = $("#exportDetail").datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].disNum <= 0) {
                $.messager.alert("系统提示", "第" + parseInt(i + 1) + "行产品库房中不存在，不能出库", 'error');
                return false;
            }
        }
        return true;
    }
    var getCommitData = function () {
        var expExportMasterBeanChangeVo = {};
        expExportMasterBeanChangeVo.inserted = [];
        var exportMaster = {};
        exportMaster.documentNo = $("#documentNo").textbox('getValue');
        exportMaster.exportClass = $("#exportClass").combobox('getValue');
        exportMaster.exportDate = new Date($("#exportDate").datetimebox('getValue'));
        exportMaster.storage = parent.config.storageCode;
        exportMaster.receiver = $("#receiver").combogrid('getValue');
        exportMaster.accountReceivable = $("#accountReceivable").numberbox('getValue');
        exportMaster.accountPayed = $("#accountPayed").numberbox('getValue');
        exportMaster.additionalFee = $("#additionalFee").numberbox('getValue');
        exportMaster.subStorage = $("#subStorage").combobox('getValue');
        exportMaster.accountIndicator = 1;
        exportMaster.docStatus = 0;
        exportMaster.memos = $('#memos').textbox('getValue');
        exportMaster.fundItem = $('#fundItem').combobox('getValue');
        exportMaster.operator = parent.config.staffName;
        exportMaster.principal = $("#principal").combogrid('getText');
        exportMaster.storekeeper = $("#storekeeper").combogrid('getText');
        exportMaster.acctoperator = parent.config.staffName;
        exportMaster.acctdate = new Date();
        exportMaster.principal = $("#principal").combogrid('getText');
        //exportMaster.auditor = $("#auditor").combobox('getValue');
        //exportMaster.rcptNo = $("#rcptNo").combobox('getValue');
        exportMaster.buyer = $("#acctoperator").combobox('getText');
        exportMaster.hospitalId = parent.config.hospitalId;
        expExportMasterBeanChangeVo.inserted.push(exportMaster);

        //明细记录
        var expExportDetailBeanChangeVo = {};
        expExportDetailBeanChangeVo.inserted = [];

        var rows = $("#exportDetail").datagrid('getRows');

        for (var i = 0; i < rows.length; i++) {
            var detail = {};
            detail.documentNo = exportMaster.documentNo;
            detail.itemNo = i;
            var rowIndex = $("#exportDetail").datagrid('getRowIndex', rows[i]);
            detail.expCode = rows[i].expCode;
            detail.expSpec = rows[i].expSpec;
            detail.units = rows[i].units;
            detail.batchNo = rows[i].batchNo;
            detail.importDocumentNo = rows[i].importDocumentNo;
            detail.retailPrice = rows[i].retailPrice;
            detail.tradePrice = rows[i].tradePrice;
            detail.packageSpec = rows[i].packageSpec;
            detail.packageUnits = rows[i].packageUnits;
            detail.quantity = rows[i].quantity;
            detail.subPackage1 = rows[i].subPackage1;
            detail.subPackageUnits1 = rows[i].subPackageUnits1;
            detail.subPackageSpec1 = rows[i].subPackageSpec1;
            detail.subPackage2 = rows[i].subPackage2;
            detail.subPackageUnits2 = rows[i].subPackageUnits2;
            detail.subPackageSpec2 = rows[i].subPackageSpec2;
            detail.purchasePrice = rows[i].purchasePrice;
            detail.expireDate = new Date(rows[i].expireDate);
            detail.expForm = rows[i].expForm;
            detail.recFlag = 0;
            detail.firmId = rows[i].firmId;
            detail.inventory = rows[i].disNum - rows[i].quantity;
            detail.producedate = new Date(rows[i].producedate);
            detail.disinfectdate = new Date(rows[i].disinfectdate);
            if ($(rows[i].killflag).attr('type') == "checked") {
                detail.killflag = 1;

            } else {
                detail.killflag = 0;
            }
            detail.expSgtp = 1;
            //if($(input).is(":checked")){
            //    alert(5);
            //}
            //$("#order > thead > tr > td > input ").click(function(){
            //    if(this.checked){
            //        row.killflag=1;
            //        //做if条件判断，如果是被选中的，那么.....
            //    }
            //   // 或者
            //    if($(this).attr("type")=="checkbox"&&$(this).attr("type").prop("checked")){
            //    row.killflag=1;
            //    }
            //    //请问这里能用this关键字来查询么，因为当前对象已经是这个input了
            //})
            detail.assignCode = rows[i].assignCode;
            detail.bigCode = rows[i].expCode;
            detail.bigSpec = rows[i].packageSpec;
            detail.bigFirmId = rows[i].firmId;

            //detail.expSgtp = rows[i].expSgtp;
            detail.memo = rows[i].memo;
            detail.hospitalId = parent.config.hospitalId;
            expExportDetailBeanChangeVo.inserted.push(detail);
        }

        var importVo = {};
        importVo.expExportMasterBeanChangeVo = expExportMasterBeanChangeVo;
        importVo.expExportDetailBeanChangeVo = expExportDetailBeanChangeVo;
        return importVo;
    }
    /**
     * 保存功能
     */
    $("#saveBtn").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#exportDetail").datagrid('endEdit', editIndex);
        }
        if (!exportFlag) {
            return;
        }
        if (dataValid()) {
            var importVo = getCommitData();
            $.postJSON("/api/exp-stock/exp-export-manage", importVo, function (data) {
                if (data.errorMessage) {
                    $.messager.alert("系统提示", data.errorMessage, 'error');
                    return;
                }
                $.messager.alert('系统提示', '出库成功', 'success', function () {
                    saveFlag = true;
                    $("#printBtn").trigger('click');
                });
            }, function (data) {
                $.messager.alert("系统提示", data.errorMessage, 'error');
            })
        }
    });


    /**
     * 新单
     */
    $("#newBtn").on('click', function () {
        parent.updateTab('出库处理', '/his/ieqm/exp-export');
    })

    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        buttons: '#printft',
        onOpen: function () {
            var printDocumentNo = $("#documentNo").textbox('getValue')
            $("#report").prop("src", parent.config.defaultReportPath + "exp-export.cpt&documentNo=" + printDocumentNo);
            //$("#report").prop("src", "http://localhost:8075/WebReport/ReportServer?reportlet=exp%2Fexp%2Fexp-export.cpt&__bypagesize__=false&documentNo="+printDocumentNo);
        }
    })
    $("#printClose").on('click', function () {
        parent.updateTab('出库处理', '/his/ieqm/exp-export');
    })
    $("#printBtn").on('click', function () {
        if (saveFlag) {
            $("#printDiv").dialog('open');
        } else {
            var printData = $("#exportDetail").datagrid('getRows');
            if (printData.length <= 0) {
                $.messager.alert('系统提示', '请先查询数据', 'info');
                return;
            }
            $("#printDiv").dialog('open');
        }
    })
})