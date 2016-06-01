/**
 * 消耗品入库功能
 * Created by heren on 2015/10/19.
 */

$(function () {

    /**
     * 供货方
     *
     */
    var receiver = [];//供应商
    var staffs = [];//员工信息
    var documentNo;//入库单号
    var editIndex;
    var currentExpCode;
    var flag;
    var saveFlag;

    var setDefaultDate = function () {
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var h = date.getHours();
        var mm = date.getMinutes();
        var s = date.getSeconds();
        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' '
            + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
        return dateTime;
    }
    //格式化日期函数
    function formatterDate(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
//            var h = date.getHours();
//            var mm = date.getMinutes();
//            var s = date.getSeconds();
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d);
//                + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
            return dateTime;
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

    /**
     * 定义明细信息表格
     */
    $("#importDetail").datagrid({
        fit: true,
        fitColumns: true,
        showFooter: true,
        title: "消耗品入库单",
        footer: '#ft',
        toolbar: '#expImportMaster',
        columns: [[{
            title: '项目代码',
            field: 'expCode',
            align: 'center',
            width: '6%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '品名',
            field: 'expName',
            align: 'center',
            width: '9%',
            editor: {
                type: 'combogrid', options: {
                    mode: 'remote',
                    url: '/api/exp-name-dict/list-exp-name-by-input',
                    singleSelect: true,
                    method: 'GET',
                    panelWidth: 300,
                    idField: 'expName',
                    textField: 'expName',
                    columns: [[{
                        title: '代码',
                        field: 'expCode',
                        align: 'center',
                        width: 110
                    }, {
                        title: '品名',
                        field: 'expName',
                        align: 'center',
                        width: 120
                    }, {
                        title: '拼音码',
                        field: 'inputCode',
                        align: 'center',
                        width: 50
                    }]],
                    onClickRow: function (index, row) {
                        var ed = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expCode'});
                        $(ed.target).textbox('setValue', row.expCode);
                        currentExpCode = row.expCode;
                        $("#stockRecordDialog").dialog('open');
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            if (row) {
                                var ed = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expCode'});
                                $(ed.target).textbox('setValue', row.expCode);
                                currentExpCode = row.expCode;
                                $("#stockRecordDialog").dialog('open');
                            }
                            $(this).combogrid('hidePanel');
                        }
                    })
                }
            }
        }, {
            title: '规格',
            field: 'packageSpec',
            align: 'center',
            width: '5%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '单位',
            field: 'packageUnits',
            align: 'center',
            width: '5%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: "数量",
            field: 'quantity',
            align: 'center',
            width:'5%',
            editor: {
                type: 'numberbox', options: {
                    onChange: function (newValue, oldValue) {
                        var selectRows = $("#importDetail").datagrid('getData').rows;
                        console.log(selectRows[editIndex]);
                        //var purchasePriceEd = $("#importDetail").datagrid('getEditor', {
                        //    index: editIndex,
                        //    field: 'purchasePrice'
                        //});
                        var purchasePrice =selectRows[editIndex].purchasePrice;

                        var amountEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'amount'});
                        $(amountEd.target).numberbox('setValue', newValue * purchasePrice);

                        var rows = $("#importDetail").datagrid('getRows');
                        var totalAmount = 0;
                        for (var i = 0; i < rows.length; i++) {
                            var rowIndex = $("#importDetail").datagrid('getRowIndex', rows[i]);
                            if (rowIndex == editIndex) {
                                continue;
                            }
                            totalAmount += Number(rows[i].amount);
                        }
                        if (totalAmount) {
                            totalAmount += newValue * purchasePrice;
                        } else {
                            totalAmount = newValue * purchasePrice;
                        }
                        $("#accountReceivable").numberbox('setValue', totalAmount);
                    }
                }
            }
        }, {
            title: '批号',
            field: 'batchNo',
            align: 'center',
            width: '5%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '进价',
            field: 'purchasePrice',
            align: 'center',
            width: '6%',
            editor: {type: 'numberbox', options: {
                editable: false,
                disabled: true,
                precision: '2', min: "0.01"}}
        }, {
            title: '金额',
            field: 'amount',
            align: 'center',
            width: '5%',
            editor: {
                type: 'numberbox', options: {
                    precision: '2',
                    editable: false,
                    disabled: true
                }
            }
        }, {
            title: '产品类型',
            field: 'expForm',
            align: 'center',
            width: '6%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '有效日期',
            field: 'expireDate',
            align: 'center',
            width:'9%',
            editor: {
                type: 'datetimebox',
                options: {
                    value: 'dateTime',
                    showSeconds: true,
                    formatter: formatterDate
//                    parser: w3,
//                    onSelect: function (date) {
//                        var dateEd = $("#importDetail").datagrid('getEditor', {
//                            index: editIndex,
//                            field: 'expireDate'
//                        });
//                        var y = date.getFullYear()+1;
//                        var m = date.getMonth() + 1;
//                        var d = date.getDate();
//                        var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
//                        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
//
//                        $(dateEd.target).textbox('setValue', dateTime);
//                        $(this).datetimebox('hidePanel');
//                    }
                }
            }
        }, {
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: '6%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '注册证号',
            field: 'expImportDetailRegistNo',
            align: 'center',
            width: '6%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}
            }
        }, {
            title: '许可证号',
            field: 'expImportDetailLicenceno',
            align: 'center',
            width: '6%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '发票号',
            field: 'invoiceNo',
            align: 'center',
            width: '5%',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '发票日期',
            field: 'invoiceDate',
            align: 'center',
            width:'9%',
            editor: {
                type: 'datetimebox',
                options: {
                    value: 'dateTime',
                    showSeconds: true,
                    formatter: formatterDate
//                    parser: w3,
//                    onSelect: function (date) {
//                        var dateEd = $("#importDetail").datagrid('getEditor', {
//                            index: editIndex,
//                            field: 'invoiceDate'
//                        });
//                        var y = date.getFullYear();
//                        var m = date.getMonth() + 1;
//                        var d = date.getDate();
//                        var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
//                        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
//
//                        $(dateEd.target).textbox('setValue', dateTime);
//                        $(this).datetimebox('hidePanel');
//                    }
                }
            }
        }, {
            title: '备注',
            field: 'memo',
            align: 'center',
            width: '5%',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '生产日期',
            field: 'producedate',
            align: 'center',
            width:'9%',
            editor: {
                type: 'datetimebox',
                options: {
                    value: 'dateTime',
                    showSeconds: true,
                    formatter: formatterDate
//                    parser: w3,
//                    onSelect: function (date) {
//                        var dateEd = $("#importDetail").datagrid('getEditor', {
//                            index: editIndex,
//                            field: 'producedate'
//                        });
//                        var y = date.getFullYear();
//                        var m = date.getMonth() + 1;
//                        var d = date.getDate();
//                        var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
//                        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
//
//                        $(dateEd.target).textbox('setValue', dateTime);
//                        $(this).datetimebox('hidePanel');
//                    }
                }
            }
        }, {
            title: '消毒日期',
            field: 'disinfectdate',
            align: 'center',
            width:'9%',
            editor: {
                type: 'datetimebox',
                options: {
                    value: 'dateTime',
                    showSeconds: true,
                    formatter: formatterDate,
                    parser: w3,
                    onSelect: function (date) {
                        var dateEd = $("#importDetail").datagrid('getEditor', {
                            index: editIndex,
                            field: 'disinfectdate'
                        });
                        var y = date.getFullYear();
                        var m = date.getMonth() + 1;
                        var d = date.getDate();
                        var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
                        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;

                        $(dateEd.target).textbox('setValue', dateTime);
                        $(this).datetimebox('hidePanel');
                    }
                }
            }
        }, {
            title: '灭菌标志',
            field: 'killFlag',
            align: 'center',
            width: '6%',
            editor: {type: 'combobox', options: {
                valueField:'value',
                textField:'title',
                editable: false,
                data:[{
                    value:'1',
                    title:'已灭菌'
                },{
                    title:'未灭菌',
                    value:'0'
                }]
            }}
        }, {
            title: '折扣',
            field: 'discount',
            align: 'center',
            width: '5%',
            editor: {type: 'textbox', options: {}}
        }, {

            title: '招标文号',
            field: 'orderBatch',
            align: 'center',
            width: '6%',
            editor: {type: 'textbox', options: {}}

        }, {
            title: '招标文号序号',
            field: 'tenderNo',
            align: 'center',
            width: '8%',
            editor: {type: 'numberbox', options: {}}
        }, {
            title: '现有数量',
            field: 'inventory',
            align: 'center',
            width: '6%',
            editor: {type: 'numberbox', options: {
                editable: false,
                disabled: true,precision: '2'}}
        }, {
            title: '零售价',
            field: 'retailPrice',
            align: 'center',
            width: '6%',
            editor: {type: 'numberbox', options: {
                editable: false,
                disabled: true,precision: '2'}}
        }, {
            title: '进货价',
            field: 'tradePrice',
            align: 'center',
            width: '6%',
            editor: {type: 'numberbox', options: {
                editable: false,
                disabled: true,precision: '2'}}
        }, {
            title: '最小规格',
            field: 'expSpec',
            hidden: true,
            editor: {type: 'textbox'}
        }, {
            title: '最小单位',
            field: 'units',
            hidden: true,
            editor: {type: 'textbox'}
        }
        ]],
        onClickCell: function (index, field, row) {
            if (index != editIndex) {
                $(this).datagrid('endEdit', editIndex);
                editIndex = index;
            }
            $(this).datagrid('beginEdit', editIndex);
            var ed = $(this).datagrid('getEditor', {index: index, field: field});
            $(ed.target).focus();
        }
    });

    //入库类别
    $("#importClass").combobox({
        url: '/api/exp-import-class-dict/list',
        valueField: 'importClass',
        textField: 'importClass',
        editable: false,
        method: 'GET',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            for(var i = 0 ;i<data.length;i++){
                if(data[i].importClass=="正常入库"){
                    $(this).combobox('select', data[i].importClass);
                }
            }
        }
    });

    $("#documentNo").textbox({
        disabled: true
    });//设置入库单号不能进行编辑

    /**
     * 创建新的入库单号
     * @param subStorageCode
     * @returns {*}
     */
    var createNewDocument = function (subStorageCode) {
        var storage;
        var promise = $.get('/api/exp-sub-storage-dict/list-by-storage?storageCode=' + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId, function (data) {
            $.each(data, function (index, item) {
                if (item.subStorage == subStorageCode) {
                    storage = item;
                }
            });

            if (storage) {
                if (storage.importNoPrefix.length <= 4) {
                    documentNo = storage.importNoPrefix + '000000'.substring((storage.importNoAva + "").length) + storage.importNoAva;
                } else if (storage.importNoPrefix.length = 5) {
                    documentNo = storage.importNoPrefix + '00000'.substring((storage.importNoAva + "").length) + storage.importNoAva;
                } else if (storage.importNoPrefix.length = 6) {
                    documentNo = storage.importNoPrefix + '0000'.substring((storage.importNoAva + "").length) + storage.importNoAva;
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
        editable: false,
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

    //招标方式
    $("#tenderType").combobox({
        url: '/api/exp-tender-type-dict/list',
        valueField: 'tenderTypeCode',
        textField: 'tenderTypeName',
        editable: false,
        method: 'GET',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('select', data[0].tenderTypeCode);
            }
        }
    })

    $('#importDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#importDate').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
            $('#importDate').datetimebox('setText', dateTime);
            $('#importDate').datetimebox('hidePanel');
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
    //保管人数据加载
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
    //采购人数据加载
    $('#buyer').combogrid({
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
    //验收人数据加载
    $('#checkMan').combogrid({
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
    var suppliers = [];
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
    });

    promise.done(function () {
        $("#supplier").combogrid({
            idField: 'supplierName',
            textField: 'supplierName',
            data: suppliers,
            panelWidth: 450,
            fitColumns: true,
            columns: [[{
                title: '供应商名称',
                field: 'supplierName', width: 180, align: 'center'
            }, {
                title: '供应商代码',
                field: 'supplierCode', width: 130, align: 'center'
            }, {
                title: '输入码',
                field: 'inputCode', width: 50, align: 'center'
            }]]
        })
    });

    //追加

    $("#addRow").on('click', function () {
        flag=0;
        $("#importDetail").datagrid('appendRow', {});
        var rows = $("#importDetail").datagrid('getRows');
        var appendRowIndex = $("#importDetail").datagrid('getRowIndex', rows[rows.length - 1]);

        if (editIndex || editIndex == 0) {
            $("#importDetail").datagrid('endEdit', editIndex);
        }
        editIndex = appendRowIndex;
        $("#importDetail").datagrid('beginEdit', editIndex);

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
        columns: [[{
            title: '代码',
            field: 'expCode',
            align: 'center',
            width: '8%'
        }, {
            title: '名称',
            field: 'expName',
            align: 'center',
            width: '8%'
        },  {
            title: '规格',
            field: 'expSpec',
            align: 'center',
            width: '6%'
        }, {
            title: '最小规格',
            field: 'minSpec',
            align: 'center',
            width: '8%'
        }, {
            title: '单位',
            field: 'units',
            align: 'center',
            width: '6%'
        }, {
            title: '最小单位',
            field: 'minUnits',
            align: 'center',
            width: '8%'
        }, {
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: '8%'
        }, {
            title: '批发价',
            field: 'tradePrice',
            align: 'center',
            width: '8%'
        }, {
            title: '零售价',
            field: 'retailPrice',
            align: 'center',
            width: '8%'
        }, {
            title: '注册证号',
            field: 'registerNo',
            align: 'center',
            width: '8%'
        }, {
            title: '许可证号',
            field: 'permitNo',
            align: 'center',
            width: '8%'
        }]],
        onLoadSuccess:function(data){
            flag = flag+1;
            if(flag==1){
                if(data.total==0 && editIndex!=undefined){
                    //$("#exportDetail").datagrid('endEdit', editIndex);
                    $.messager.alert('系统提示','无法获取产品的价格信息！','info');
                    $("#stockRecordDialog").dialog('close');
                    //$("#exportDetail").datagrid('beginEdit', editIndex);
                }
                flag=0;
            }
        },
        onClickRow: function (index, row) {
            console.log(row);
            var expCodeEdit = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expCode'});
            $(expCodeEdit.target).textbox('setValue', row.expCode);

            var expNameEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expName'});
            $(expNameEd.target).textbox('setValue', row.expName);

            var packageSpecEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'packageSpec'});
            $(packageSpecEd.target).textbox('setValue', row.expSpec);

            var packageUnitsEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'packageUnits'});
            $(packageUnitsEd.target).textbox('setValue', row.units);

            var SpecEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expSpec'});
            $(SpecEd.target).textbox('setValue', row.minSpec);

            var unitsEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'units'});
            $(unitsEd.target).textbox('setValue', row.minUnits);


            var quantityEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'quantity'});
            $(quantityEd.target).textbox('setValue', 0);
            $(quantityEd.target).focus();

            var batchNoEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'batchNo'});
            $(batchNoEd.target).textbox('setValue', 'X');

            var purchasePriceEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'purchasePrice'});
            $(purchasePriceEd.target).textbox('setValue', row.tradePrice);

            var amountEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'amount'});
            $(amountEd.target).textbox('setValue', 0);

            var expFormEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expForm'});
            $(expFormEd.target).textbox('setValue', row.expForm);

            var firmIdEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'firmId'});
            $(firmIdEd.target).textbox('setValue', row.firmId);
            var inventoryEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'inventory'});
            $(inventoryEd.target).textbox('setValue', row.quantity);

            var expImportDetailRegistNoEd = $("#importDetail").datagrid('getEditor', {
                index: editIndex,
                field: 'expImportDetailRegistNo'
            });
            $(expImportDetailRegistNoEd.target).textbox('setValue', row.registerNo);

            var expImportDetailLicencenoEd = $("#importDetail").datagrid('getEditor', {
                index: editIndex,
                field: 'expImportDetailLicenceno'
            });
            $(expImportDetailLicencenoEd.target).textbox('setValue', row.permitNo);

            var invoiceDateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'invoiceDate'});
            $(invoiceDateEd.target).textbox('setValue', setDefaultDate());
            var producedateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'producedate'});
            $(producedateEd.target).textbox('setValue', setDefaultDate());
            var disinfectdateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'disinfectdate'});
            $(disinfectdateEd.target).textbox('setValue', setDefaultDate());

            var discountEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'discount'});
            $(discountEd.target).textbox('setValue', '100');

            var orderBatchEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'orderBatch'});
            $(orderBatchEd.target).textbox('setValue', 'x');

            var retailedEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'retailPrice'});
            $(retailedEd.target).numberbox('setValue', row.retailPrice);

            var tradePriceEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'tradePrice'});
            $(tradePriceEd.target).numberbox('setValue', row.tradePrice);
            $("#importDetail").datagrid('endEdit',editIndex);
            $("#importDetail").datagrid('beginEdit',editIndex);

            $("#stockRecordDialog").dialog('close');
        }

    });

    //stockRecord

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


    /**
     * 查询
     */
    $("#searchBtn").on('click',function(){
        parent.addTab('入库单据查询', '/his/ieqm/exp-import-document-search');
    })
    /**
     * 定义新供应商
     */
    $("#newSupplier").on('click',function(){
        parent.addTab('产品供应商目录维护', '/his/ieqm/exp-supplier-catalog');
    })
    /**
     * 删除按钮
     */
    $("#delRow").on('click', function () {
        var row = $("#importDetail").datagrid('getSelected');
        if (row) {
            var index = $("#importDetail").datagrid('getRowIndex', row);
            $("#importDetail").datagrid('deleteRow', index);
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
        var rows = $("#importDetail").datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].quantity == 0) {
                $.messager.alert("系统提示", "第" + i + "行入库数量为0 请重新填写", 'error');
                return false;
            }
        }

        if (rows.length == 0) {
            $.messager.alert("系统提示", "明细记录为空，不允许保存", 'error');
            return false;
        }

        //判断供货商是否为空
        var supplier = $("#supplier").combogrid('getValue');
        if (!supplier) {
            $.messager.alert("系统提示", "产品入库，供货商不能为空", 'error');
            return false;
        }

        var importDate = $("#importDate").datetimebox('getValue');
        if (!importDate) {
            $.messager.alert("系统提示", "产品入库，入库时间不能为空", 'error');
            return false;
        }
        return true;
    }

    var getCommitData = function(){
        var expImportMasterBeanChangeVo = {};
        expImportMasterBeanChangeVo.inserted = [];
        var importMaster = {};
        importMaster.importClass = $("#importClass").combobox('getValue');
        importMaster.importDate = new Date($("#importDate").datetimebox('getValue'));
        importMaster.storage = parent.config.storageCode;
        importMaster.documentNo = $("#documentNo").textbox('getValue');
        importMaster.supplier = $("#supplier").combogrid('getValue');
        importMaster.accountReceivable = $("#accountReceivable").numberbox('getValue');
        importMaster.accountPayed = $("#accountPayed").numberbox('getValue');
        importMaster.additionalFee = $("#additionalFee").numberbox('getValue');
        importMaster.subStorage = $("#subStorage").combobox('getValue');
        importMaster.accountIndicator = 1;
        importMaster.docStatus = 0;
        importMaster.memos = $('#memos').textbox('getValue');
        importMaster.operator = parent.config.staffName;
        importMaster.principal = $("#principal").combogrid('getText');
        importMaster.storekeeper = $("#storekeeper").combogrid('getText');
        importMaster.buyer = $("#buyer").combogrid('getText');
        importMaster.checkman = $("#checkMan").combogrid('getText');
        importMaster.tenderNo = $("#tenderNo").textbox('getValue');
        importMaster.tenderType = $("#tenderType").combobox('getValue');
        importMaster.hospitalId = parent.config.hospitalId;
        expImportMasterBeanChangeVo.inserted.push(importMaster);

        //明细记录
        var expImportDetailBeanChangeVo = {};
        expImportDetailBeanChangeVo.inserted = [];

        var rows = $("#importDetail").datagrid('getRows');

        for (var i = 0; i < rows.length; i++) {
            var detail = {};
            var rowIndex = $("#importDetail").datagrid('getRowIndex', rows[i]);
            detail.documentNo = importMaster.documentNo;
            detail.itemNo = i;
            detail.expCode = rows[i].expCode;
            detail.expSpec = rows[i].expSpec;
            detail.units = rows[i].units;
            detail.packageSpec = rows[i].packageSpec;
            detail.packageUnits = rows[i].packageUnits;
            detail.quantity = rows[i].quantity;
            detail.batchNo = rows[i].batchNo;
            detail.purchasePrice = rows[i].purchasePrice;
            detail.expireDate = new Date(rows[i].expireDate);
            detail.expForm = rows[i].expForm;
            detail.firmId = rows[i].firmId;
            detail.retailPrice = rows[i].retailPrice;
            detail.tallyFlag = 0;
            detail.tradePrice = rows[i].tradePrice;
            detail.killflag = rows[i].killflag;
            detail.discount = rows[i].discount;
            detail.orderBatch = rows[i].orderBatch;
            detail.tenderNo = rows[i].tenderNo;
            detail.invoiceDate = new Date(rows[i].invoiceDate);
            detail.producedate = new Date(rows[i].producedate);
            detail.disinfectdate = new Date(rows[i].disinfectdate);
            detail.invoiceNo = rows[i].invoiceNo;
            detail.hospitalId = parent.config.hospitalId;

            expImportDetailBeanChangeVo.inserted.push(detail);
        }

        var importVo = {};
        importVo.expImportMasterBeanChangeVo = expImportMasterBeanChangeVo;
        importVo.expImportDetailBeanChangeVo = expImportDetailBeanChangeVo;
        return importVo ;
    }

    /**
     * 保存功能
     */
    $("#saveBtn").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#importDetail").datagrid('endEdit', editIndex);
        }
        if (dataValid()) {
            var importVo = getCommitData() ;
            $.postJSON("/api/exp-stock/imp", importVo, function (data) {
                console.log(data.errorMessage)
                //console.log(data.responseJSON.errorMessage)
                console.log(data)
                if(data.errorMessage){
                    $.messager.alert("系统提示", data.errorMessage, 'error');
                    return;
                }
                $.messager.alert('系统提示', '入库成功', 'success',function(){
                    saveFlag = true;
                    $("#printBtn").trigger('click');
                });
            }, function (data) {
                $.messager.alert("系统提示", data.responseJSON.errorMessage, 'error');
            })
        }
    });

    var newDocument = function () {
        //点击按钮调用的方法
        var subStorage = $("#subStorage").textbox('getValue');
        if(subStorage){
            var promise = createNewDocument(subStorage) ;
            promise.done(function(){
                $("#documentNo").textbox('setValue',documentNo);
            })
        }
        $("#importDetail").datagrid('loadData',[]) ;

    }

    /**
     * 新单
     */
    $("#newBtn").on('click',function(){
        parent.updateTab('入库处理', '/his/ieqm/exp-import');
    })

    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        buttons: '#printft',
        closed: true,
        onOpen: function () {
            var printDocumentNo = $("#documentNo").textbox('getValue');
            //console.log(printDocumentNo);
            //$("#report").prop("src",  "http://localhost:8075/WebReport/ReportServer?reportlet=exp%2Fexp%2Fexp-import.cpt&__bypagesize__=false&documentNo="+printDocumentNo);
            $("#report").prop("src", parent.config.defaultReportPath + "exp-import.cpt&documentNo=" + printDocumentNo);
        }
    })
    $("#printClose").on('click',function(){
        parent.updateTab('入库处理', '/his/ieqm/exp-import');
    })
    $("#printBtn").on('click', function () {
        if(saveFlag){
            $("#printDiv").dialog('open');
        }else{
            var printData = $("#importDetail").datagrid('getRows');
            if (printData.length <= 0) {
                $.messager.alert('系统提示', '请先查询数据', 'info');
                return;
            }
            $("#printDiv").dialog('open');
        }

    })
})