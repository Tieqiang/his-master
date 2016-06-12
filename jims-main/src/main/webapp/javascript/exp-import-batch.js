/**
 * 消耗品批量入库功能
 * Created by heren on 2015/10/19.
 */
$(function () {
    var editIndex;
    var depts=[] ;
    var flag;
    var staffs = [];//员工信息
    var rows=[] ;//选中记录

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
            var h = date.getHours();
            var mm = date.getMinutes();
            var s = date.getSeconds();
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' '
                + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
            return dateTime;
        }
    }
    //格式化日期函数
    function formatterYMD(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d);
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
    var deptPromse = $.get("/api/exp-storage-dept/list?hospitalId="+parent.config.hospitalId,function(data){
        for(var i = 0 ;i<data.length ;i++){
            var dept={} ;
            dept.supplierName = data[i].storageName ;
            dept.supplierCode = data[i].storageCode ;
            dept.inputCode = data[i].disburseNoPrefix ;
            depts.push(dept) ;
        }
    }) ;
    deptPromse.done(function(){
        $("#supplier").combogrid({
            idField: 'supplierName',
            textField: 'supplierName',
            data: depts,
            panelWidth: 500,
            fitColumns: true,
            columns: [[{
                title: '供应商名称',
                field: 'supplierName', width: 200, align: 'center'
            }, {
                title: '供应商代码',
                field: 'supplierCode', width: 150, align: 'center'
            }, {
                title: '输入码',
                field: 'inputCode', width: 50, align: 'center'
            }]],
            filter: function (q, row) {
                return $.startWith(row.inputCode.toUpperCase(), q.toUpperCase());
            }
        })
    }) ;

//入库分类字典
    $("#importClass").combobox({
        url: '/api/exp-import-class-dict/list',
        valueField: 'importClass',
        textField: 'importClass',
        method: 'GET',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            for (var i = 0; i < data.length; i++) {
                if (data[i].importClass == "正常入库") {
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


    $("#tenderType").combobox({
        url: '/api/exp-tender-type-dict/list',
        valueField: 'tenderTypeCode',
        textField: 'tenderTypeName',
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
    //提取按钮
    $("#fetchBtn").on('click',function(){
        $("#expExportDialog").dialog('open') ;
    }) ;
    /**
     * 定义明细信息表格
     */
    $("#importDetail").datagrid({
        fit: true,
        fitColumns: true,
        rownumbers: true,
        singleSelect: true,
        showFooter: true,
        title: "消耗品入库单",
        footer: '#ft',
        toolbar: '#expImportMaster',
        columns: [[{
            title: '项目代码',
            field: 'expCode',
            align: 'center',
            width: '5%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: false
            }}
        }, {
            title: '品名',
            field: 'expName',
            align: 'center',
            width: '5%',
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
                disabled: false}}
        }, {
            title: '单位',
            field: 'packageUnits',
            align: 'center',
            width: '5%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: false}}
        }, {
            title: "数量",
            field: 'quantity',
            align: 'center',
            width:'5%',
            editor: {
                type: 'numberbox', options: {
                    onChange: function (newValue, oldValue) {
                        var retailPriceEd = $("#importDetail").datagrid('getEditor', {
                            index: editIndex,
                            field: 'retailPrice'
                        });
                        var retailPrice = $(retailPriceEd.target).textbox('getValue');

                        var amountEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'amount'});
                        $(amountEd.target).numberbox('setValue', newValue * retailPrice);

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
                            totalAmount += newValue * retailPrice;
                        } else {
                            totalAmount = newValue * retailPrice;
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
                disabled: false}}
        }, {
            title: '进价',
            field: 'retailPrice',
            align: 'center',
            width: '5%',
            editor: {type: 'numberbox', options: {
                editable: false,
                disabled: false,
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
                    disabled: false
                }
            }
        }, {
            title: '产品类型',
            field: 'expForm',
            align: 'center',
            width: '6%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: false}}
        }, {
            title: '有效日期',
            field: 'expireDate',
            align: 'center',
            formatter: formatterDate,
            width:'7%',
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
                            field: 'expireDate'
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
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: '5%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: false}}
        }, {
            title: '最小规格',
            field: 'expSpec',
            align: 'center',
            width: '6%',
            editor: {
                type: 'textbox', options: {
                    editable: false,
                    disabled: false
                }
            }
        }, {
            title: '最小单位',
            field: 'units',
            align: 'center',
            width: '6%',
            editor: {
                type: 'textbox', options: {
                    editable: false,
                    disabled: false
                }
            }
        }, {
            title: '注册证号',
            field: 'expImportDetailRegistNo',
            align: 'center',
            width: '6%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: false}}
        }, {
            title: '许可证号',
            field: 'expImportDetailLicenceno',
            align: 'center',
            width: '6%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: false}}
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
            formatter: formatterDate,
            width:'7%',
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
                            field: 'invoiceDate'
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
            title: '备注',
            field: 'memo',
            align: 'center',
            width: '4%',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '生产日期',
            field: 'producedate',
            align: 'center',
            formatter: formatterYMD,
            width:'7%',
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
                            field: 'produceDate'
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
            title: '消毒日期',
            field: 'disinfectdate',
            align: 'center',
            formatter: formatterYMD,
            width:'7%',
            editor: {
                type: 'datetimebox',
                options: {
                    value: 'dateTime',
                    showSeconds: true,
                    formatter: formatterYMD,
                    parser: w3,
                    onSelect: function (date) {
                        var dateEd = $("#importDetail").datagrid('getEditor', {
                            index: editIndex,
                            field: 'disinfectDate'
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
            field: 'killflag',
            align: 'center',
            width: '6%',
            editor: {type: 'checkbox', options: {}}
        }, {
            title: '折扣',
            field: 'discount',
            align: 'center',
            width: '4%',
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
            editor: {type: 'textbox', options: {}}
        }, {
            title: '现有数量',
            field: 'inventory',
            align: 'center',
            width: '6%',
            editor: {type: 'numberbox', options: {
                editable: false,
                disabled: false,precision: '2'}}
        }, {
            title: '零售价',
            field: 'retailPrice',
            hidden: true
        }, {
            title: '进货价',
            field: 'tradePrice',
            hidden: true
        }
        ]],
        onClickCell: function (index, field, row) {
            //if (index != editIndex) {
            //    $(this).datagrid('endEdit', editIndex);
            //    editIndex = index;
            //}
            //
            //$(this).datagrid('beginEdit', editIndex);

            //var ed = $(this).datagrid('getEditor', {index: index, field: field});
            //$(ed.target).focus();
        }
    });
    /**
     * 出库记录弹出框
     */
    $("#expExportDialog").dialog({
        title: '消耗品接收',
        width: 900,
        height: 300,
        catch: false,
        modal: true,
        closed: true,
        left:30,
        top:30,

        onBeforeOpen: function () {
            var storageCode ;
            var supplierName = $("#supplier").combogrid('getValue') ;
            if(!supplierName){
                $.messager.alert("系统提示","请选择供货单位",'error');
                return  false;
            }else{
                for(var i = 0 ;i<depts.length;i++){
                    if(depts[i].supplierName==supplierName){
                        storageCode = depts[i].supplierCode ;
                    }
                }
            }

            if(!storageCode){
                $.messager.alert("系统提示","获取供货单位代码失败！","error");
                return false;
            }
            $("#expExportDatagrid").datagrid('load', {storageCode:storageCode,stockName:parent.config.storageCode,expClass:parent.config.exportClass,hospitalId:parent.config.hospitalId});
            $("#expExportDatagrid").datagrid('selectRow', 0)
        }
    });

//    ???
    $("#expExportDatagrid").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: false,
        showFooter:true,
        url:'/api/exp-export/exp-export-document-detail',
        mode:'remote',
        method:'GET',
        footer:'#expExportDiaglogFt',
        ctrlSelect:true,
        columns: [[{
            title: '项目代码',
            field: 'expCode'
        }, {
            title: '品名',
            field: 'expName',
            width: '10%'
        }, {
            title: '包装规格',
            field: 'packageSpec'
        }, {
            title: '单位',
            field: 'packageUnits'
        }, {
            title: "数量",
            field: 'quantity',
            hidden:true
        }, {
            title: '批号',
            field: 'batchNo'
        }, {
            title: '进价',
            field: 'retailPrice',
            hidden:true
         }, {
            title: '金额',
            field: 'amount'
        }, {
            title: '产品类型',
            field: 'expForm'
        }, {
            title: '有效日期',
            field: 'expireDate',
            formatter: formatterYMD
        }, {
            title: '厂家',
            field: 'firmId'
        }

            , {
            title: '备注',
            field: 'memo'
        }, {
            title: '生产日期',
            field: 'producedate',
            formatter: formatterYMD

        }, {
            title: '消毒日期',
            field: 'disinfectdate',
            formatter: formatterYMD
        }, {
            title: '灭菌标志',
            field: 'killflag'
        }
        ,{
            title:'零售价',
            field:'retailPrice',
             hidden:true
         },{
            title:'进货价',
            field:'tradePrice',
            hidden:true
        },{
            title:'最小规格',
            field:'expSpec',
            hidden:true
        },{
            title:'最小单位',
            field:'units',
            hidden:true
        }]]
    }) ;
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
            field: 'expCode'
        }, {
            title: '名称',
            field: 'expName'
        }, {
            title: '数量',
            field: 'quantity'
        }, {
            title: '规格',
            field: 'expSpec'
        }, {
            title: '最小规格',
            field: 'minSpec'
        }, {
            title: '单位',
            field: 'units'
        }, {
            title: '最小单位',
            field: 'minUnits'
        }, {
            title: '厂家',
            field: 'firmId'
        }, {
            title: '批发价',
            field: 'tradePrice'
        }, {
            title: '零售价',
            field: 'retailPrice'
        }, {
            title: '注册证号',
            field: 'registerNo'
        }, {
            title: '许可证号',
            field: 'permitNo'
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
            $(expFormEd.target).textbox('setValue', '消耗材料');

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
            var produceDateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'produceDate'});
            $(produceDateEd.target).textbox('setValue', setDefaultDate());
            var disinfectDateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'disinfectDate'});
            $(disinfectDateEd.target).textbox('setValue', setDefaultDate());

            var discountEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'discount'});
            $(discountEd.target).textbox('setValue', '100');

            var orderBatchEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'orderBatch'});
            $(orderBatchEd.target).textbox('setValue', 'x');

            var retailedEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'retailPrice'});
            $(retailedEd.target).numberbox('setValue', row.retailPrice);

            var tradePriceEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'tradePrice'});
            $(tradePriceEd.target).numberbox('setValue',row.tradePrice);

            $("#stockRecordDialog").dialog('close');
        }

    });

    /***
     * 全选按钮
     */
    $("#selectAll").on('click',function(){
        var selectText = $("#selectAll").linkbutton('options').text;
        if(selectText=='全选'){
            $("#expExportDatagrid").datagrid('selectAll') ;
            $("#selectAll").linkbutton('options').text="全不选" ;
        }else{
            $("#expExportDatagrid").datagrid('unselectAll') ;
            $("#selectAll").linkbutton('options').text="全选" ;
        }
    }) ;

    /**
     * 确定按钮
     */
    $("#sureBtn").on('click',function(){
        var datas = [] ;
        var rows = $("#expExportDatagrid").datagrid('getSelections') ;
        var totals =0;
        for(var i = 0 ;i<rows.length ;i++){
            rows[i].amount = rows[i].quantity * rows[i].retailPrice ;
            totals += rows[i].amount ;
            datas.push(rows[i]) ;
//            console.log(rows[i].documentNo);
        }
        if(totals){
            $("#accountReceivable").numberbox('setValue',totals);
        }
        $("#importDetail").datagrid('loadData',datas) ;
        $("#expExportDialog").dialog('close') ;
    })

    $("#exitBtn").on('click',function(){
        $("#expExportDialog").dialog('close') ;
    })

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

        var importDate = $("#importDate").datebox('calendar').calendar('options').current;
        if (!importDate) {
            $.messager.alert("系统提示", "产品入库，入库时间不能为空", 'error');
            return false;
        }
        return true;
    }
    var saveFlag ;
    $("#saveBatchBtn").on('click',function(){
        if (editIndex || editIndex == 0) {
            $("#importDetail").datagrid('endEdit',editIndex);
        }
        if (dataValid()) {
            var rows = $("#importDetail").datagrid("getRows");
            var expImpVo = getCommitData() ;
            expImpVo.expExportDetialVoBeanChangeVo={} ;
            for(var i =0;i<rows.length;i++){
//                rows[i].expireDate=new Date(rows[i].expireDate);
                rows[i].invoiceDate=new Date(rows[i].invoiceDate);
                rows[i].disinfectDate= new Date(rows[i].disinfectDate);
                rows[i].produceDate= new Date(rows[i].produceDate);

            }
            expImpVo.expExportDetialVoBeanChangeVo.updated=rows ;
            $.postJSON("/api/exp-stock/imp-batch", expImpVo, function (data) {
                if (data.errorMessage) {
                    $.messager.alert("系统提示", data.errorMessage, 'error');
                    return;
                }
                $.messager.alert('系统提示', '入库成功', 'success',function(){
                    saveFlag = true;
                    $("#printBtn").trigger('click');
                    //parent.updateTab('批量入库', '/his/ieqm/exp-import-batch');
                });
            }, function (data) {
                $.messager.alert("系统提示", data.responseJSON.errorMessage, 'error');
            })
        }

    })



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
        importMaster.importClass = $("#importClass").combobox('getValue');
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
            detail.documentNo = importMaster.documentNo;
            detail.itemNo = i;
            var rowIndex = $("#importDetail").datagrid('getRowIndex', rows[i]);
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
            detail.tradePrice = rows[i].purchasePrice;
            detail.tallyFlag = 0;
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
    /**
     * 新单
     */
    $("#newBtn").on('click',function(){
        newDocument() ;
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

    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var printDocumentNo = $("#documentNo").textbox('getValue');
            //$("#report").prop("src", "http://localhost:8075/WebReport/ReportServer?reportlet=exp%2Fexp%2Fexp-import.cpt&__bypagesize__=false&documentNo=" + printDocumentNo);
            $("#report").prop("src", parent.config.defaultReportPath + "exp-import.cpt&documentNo=" + printDocumentNo);
        }
    })
    $("#printClose").on('click', function () {
        parent.updateTab('批量入库', '/his/ieqm/exp-import-batch');
    })
    $("#printBtn").on('click', function () {
        if (saveFlag) {
            $("#printDiv").dialog('open');
        } else {
            var printData = $("#importDetail").datagrid('getRows');
            if (printData.length <= 0) {
                $.messager.alert('系统提示', '请先查询数据', 'info');
                return;
            }
            $("#printDiv").dialog('open');
        }
    })
})