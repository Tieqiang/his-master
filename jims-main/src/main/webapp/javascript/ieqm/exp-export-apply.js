$(function () {
    var deptId = '';
    var editIndex;
    var applyNo;
    var subStorageDicts = [];
    var documentNo;
    var currentExpCode;
    var exportFlag;

    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#right").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }

    //格式化日期(方法)
    function formatterDate(val, row) {
//        2016/5/26 9:29:19
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
    //申请开始日期
    $('#startDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#startDate').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
            $('#startDate').datetimebox('setText', dateTime);
            $('#startDate').datetimebox('hidePanel');
        }
    });
    //申请结束日期
    $('#endDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#endDate').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
            $('#endDate').datetimebox('setText', dateTime);
            $('#endDate').datetimebox('hidePanel');
        }
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

    //获取子库房数据
    var promise=$.get('/api/exp-sub-storage-dict/list-by-storage?storageCode=' + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId, function (data) {
        subStorageDicts=data;
    });

    promise.done(function(){
        //子库房数据加载
        $('#subStorage').combobox({
            panelHeight: 'auto',
            data:subStorageDicts,
            valueField: 'subStorage',
            textField: 'subStorage',
            onLoadSuccess: function () {
                var data = $(this).combobox('getData');
                if (data.length > 0) {
                    $(this).combobox('select', data[0].subStorage);
                }
            },
            onChange: function (newValue, oldValue) {
                createNewDocument(newValue);
            }
        });
    });
    //生成单据号
    var createNewDocument = function (subStorageCode) {
        var storage;
        $.each(subStorageDicts, function (index, item) {
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
        //单据号赋值
        $("#documentNo").textbox('setValue', documentNo);
    }
    $("#documentNo").textbox({disabled: true});

    //开支类别数据加载
    $('#fundItem').combobox({
        panelHeight: 'auto',
        url: '/api/exp-fund-item-dict/list',
        method: 'GET',
        valueField: 'serialNo',
        textField: 'fundItem',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('select', data[0].serialNo);
            }
        }
    });

    //出库类别数据加载
    $('#exportClass').combobox({
        panelHeight: 'auto',
        url: '/api/exp-export-class-dict/list',
        method: 'GET',
        valueField: 'exportClass',
        textField: 'exportClass',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            for (var i = 0; i < data.length; i++) {
                if (data[i].exportClass == "发放出库") {
                    $(this).combobox('select', data[i].exportClass);
                }
            }
        }
    });

    //申请库房数据加载
    $('#storage').combogrid({
        panelWidth: 500,
        idField: 'storageCode',
        textField: 'storageName',
        loadMsg: '数据正在加载',
        url: '/api/exp-storage-dept/list?hospitalId=' + parent.config.hospitalId,
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'storageCode', title: '编码', width: 150, align: 'center'},
            {field: 'storageName', title: '名称', width: 150, align: 'center'},
            {field: 'disburseNoPrefix', title: '拼音', width: 100, align: 'center'}
        ]],
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        pageSize: 50,
        pageNumber: 1
    });


    //发往库房数据加载
    $('#receiver').combogrid({
        panelWidth: 500,
        idField: 'storageCode',
        textField: 'storageName',
        loadMsg: '数据正在加载',
        url: '/api/exp-storage-dept/list?hospitalId=' + parent.config.hospitalId,
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'storageCode', title: '编码', width: 150, align: 'center'},
            {field: 'storageName', title: '名称', width: 150, align: 'center'},
            {field: 'disburseNoPrefix', title: '拼音', width: 100, align: 'center'}
        ]],
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        pageSize: 50,
        pageNumber: 1
    });

    //负责人数据加载
    $('#principal').combogrid({
        panelWidth: 500,
        idField: 'name',
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
        idField: 'name',
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
    $('#buyer').combogrid({
        panelWidth: 500,
        idField: 'name',
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

    //左侧列表初始化
    $("#left").datagrid({
        title: '出库申请单',
        singleSelect: true,
        fit: true,
        nowrap: false,
        url: '',
        method: 'GET',
        columns: [[{
            title: '申请编号',
            field: 'applicantNo',
            width: "25%"
        }, {
            title: '申请单位',
            field: 'deptName',
            width: "25%"
        }, {
            title: '单位代码',
            field: 'applicantStorage',
            width: "25%"
        }, {
            title: '申请时间',
            field: 'enterDate',
            width: "25%",
            formatter: formatterDate
        }]],
        onSelect:function(index, row){
            var rows = $("#left").datagrid("getSelections");
            if(rows.length==1){
                deptId="";
                $('#receiver').combogrid("setValue","");
                $('#receiver').combogrid("setText", "");
            }
            if(deptId==""){
                deptId=row.deptId;
                $('#receiver').combogrid("setValue", deptId);
                $('#receiver').combogrid("setText", row.deptName);
            }else{
                if(deptId!=row.deptId){
                    $.messager.alert("提示","您本次所选的申请单的申请科室和前面所选的申请单的申请科室不一致，不允许选中本申请单！","error");
                    $("#left").datagrid("unselectRow",index);
                }
            }



        }
    });

    //右侧列表初始化
    $("#right").datagrid({
        title: '申请出库消耗品',
        singleSelect: true,
        fit: true,
        fitColumns: true,
        columns: [[{
            title: 'id',
            field: 'applicationId',
            width: "10%",
            hidden:true
        },{
            title: '产品代码',
            field: 'expCode',
            width: "10%"
        }, {
            title: '产品类型',
            field: 'expForm',
            width: "7%"
        }, {
            title: '品名',
            field: 'expName',
            width: "9%",
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
                        //var ed = $("#right").datagrid('getEditor', {index: editIndex, field: 'expCode'});
                        //$(ed.target).textbox('setValue', row.expCode);
                        currentExpCode = row.expCode;
                        $("#expDetailDialog").dialog('open');
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            if (row) {
                                //var ed = $("#right").datagrid('getEditor', {index: editIndex, field: 'expCode'});
                                //$(ed.target).textbox('setValue', row.expCode);
                                currentExpCode = row.expCode;

                                $("#expDetailDialog").dialog('open');
                            }
                            $(this).combogrid('hidePanel');
                        }
                    })
                }
            }
        }, {
            title: '包装规格',
            field: 'packageSpec',
            width: "7%"
        }, {
            title: '包装单位',
            field: 'packageUnits',
            width: "7%"
        }, {
            title: '数量',
            field: 'quantity',
            width: "7%",
            editor: {
                type: 'numberbox',
                options: {
                    onChange: function (newValue, oldValue) {
                        var selectRows = $("#right").datagrid('getData').rows;
                        var retailPrice =selectRows[editIndex].retailPrice;

                        //var amountEd = $("#right").datagrid('getEditor', {index: editIndex, field: 'amount'});
                        //$(amountEd.target).numberbox('setValue', newValue * retailPrice);
                        var rowDetail = $("#right").datagrid('getData').rows[editIndex];
                        rowDetail.amount=newValue * retailPrice;


                        var rows = $("#right").datagrid('getRows');
                        var totalAmount = 0;
                        for (var i = 0; i < rows.length; i++) {
                            var rowIndex = $("#right").datagrid('getRowIndex', rows[i]);
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
//                        var selectRows = $("#right").datagrid('getData').rows;
////                        console.log(selectRows[editIndex]);
//
//                        var retailPrice =selectRows[editIndex].retailPrice;
//                        var rowDetail = $("#right").datagrid('getData').rows[editIndex];
//                        rowDetail.amount=newValue * retailPrice;
//                        var rows = $("#right").datagrid('getRows');
//                        var totalAmount = 0;
//                        for (var i = 0; i < rows.length; i++) {
//                            var rowIndex = $("#right").datagrid('getRowIndex', rows[i]);
//                            if (rowIndex == editIndex) {
//                                continue;
//                            }
//                            totalAmount += Number(rows[i].planNumber);
//                        }
//                        if (totalAmount) {
//                            totalAmount += newValue * retailPrice;
//                        } else {
//                            totalAmount = newValue * retailPrice;
//                        }
//                        $("#accountReceivable").numberbox('setValue', totalAmount);
                    },
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2,
                    editable: false
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
            title: '单价',
            field: 'retailPrice',
            width: "7%"
        }
// , {
//            title: '零售价',
//            field: 'retailPrice',
//            width: "7%",
//            editor: {
//                type: 'numberbox',
//                options: {
//                    editable: false,
//                    disabled: true}
//            },
//            hidden:true
//        }
            /*, {
             {title: '进价',
             field: 'tradePrice',
             width: "7%",
             editor: {
             type: 'numberbox',
             options: {}
             },
             hidden: true
             }*/, {
                title: '金额',
                field: 'planNumber',
                width: "7%",
                editor: {
                    type: 'numberbox',
                    options: {
                        precision: '2',
                        editable: false,
                        disabled: true
                    }
                },
                formatter:function(value,row,index){
                    if($.trim(row.retailPrice)!=""){
                        return row.retailPrice * row.quantity;
                    }
                }
            }, {
                title: '厂家',
                field: 'firmId',
                width: "7%"
            }, {
                title: '分摊方式',
                field: 'assignCode',
                width: "7%",
                editor: {
                    type: 'combobox',
                    options: {
                        panelHeight: 'auto',
                        url: '/api/exp-assign-dict/list',
                        valueField: 'assignCode',
                        textField: 'assignName',
                        method: 'GET',
                        onLoadSuccess: function () {
//                        var data = $(this).combobox('getData');
                            $(this).combobox('select',"请选择");
                        }
                    }
                }
            }, {
                title: '备注',
                field: 'memos',
                width: "7%",
                editor: {type: 'text'}
            }, {
                title: '批号',
                field: 'batchNo',
                width: "7%"
            }, {
                title: '有效期',
                field: 'expireDate',
                formatter: formatterDate,
                width: "7%"
            }, {
                title: '生产日期',
                field: 'producedate',
                formatter: formatterDate,
                width: "7%"
            }, {
                title: '消毒日期',
                field: 'disinfectdate',
                formatter: formatterDate,
                width: "7%"
            }, {
                title: '灭菌标识',
                field: 'killflag',
                width: '7%',
                formatter: function (value, row, index) {
                    if (value == '1') {
                        return '<input type="checkbox" name="DataGridCheckbox" checked="true" />';
                    }
                    if (value == '0') {
                        return '<input type="checkbox" name="DataGridCheckbox" />';
                    }
                    if (value == undefined) {
                        return value = '<input type="checkbox" name="DataGridCheckbox" />';
                    }
                }
            }, {
                title: '单位',
                field: 'units',
                width: "7%"
            }, {
                title: '结存量',
                field: 'disNum',
                width: "7%"
            },{
                title: '入库单据号',
                field: 'importDocumentNo',
                hidden:true
            }]],
        onDblClickRow: function (index, row) {
            console.info(row);
            if (index != editIndex) {
                $(this).datagrid('endEdit', editIndex);
                editIndex = index;
            }
            $(this).datagrid('beginEdit', editIndex);
            currentExpCode = row.expCode;
            $("#expDetailDialog").dialog('open');
        },
        onClickRow:function(rowIndex, rowData){
            $(this).datagrid('endEdit', rowIndex);
            $(this).datagrid('endEdit', editIndex);
            editIndex = rowIndex;
        }
    });

    $("#top").datagrid({
        toolbar: '#ft',
        border: true
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

    $("#applyDialog").dialog({
        title: '选择申请出库的消耗品',
        width: 700,
        height: 300,
        closed: false,
        catch: false,
        modal: true,
        closed: true,
        footer: '#footer',
        onOpen: function () {
            $("#applyDatagrid").datagrid('load', {
                storageCode: parent.config.storageCode,
                applyStorage: deptId,
                applyNo:applyNo,
                hospitalId: parent.config.hospitalId
            });
            $("#applyDatagrid").datagrid('selectRow', 0);
        }
    });

    $("#applyDatagrid").datagrid({
        singleSelect: false,
        fit: true,
        fitColumns: true,
        url: '/api/exp-provide-application/find-dict-application/',
        method: 'GET',
        columns: [[{
            title: '申请单号',
            field: 'applicantNo',
            width: "10%"
        }, {
            title: '序号',
            field: 'itemNo',
            width: "7%"
        }, {
            title: 'id',
            field: 'applicationId',
            width: "7%",
            hidden:true
        }, {
            title: '消耗品',
            field: 'expName',
            width: "10%"
        },{
            title: '规格',
            field: 'packageSpec',
            width: "10%"
        }, {
            title: '数量',
            field: 'quantity',
            width: "7%"
        }, {
            title: '单位',
            field: 'packageUnits',
            width: "7%"
        }, {
            title: '申请时间',
            field: 'enterDateTime',
            formatter: formatterDate,
            width: "10%"
        }, {
            title: '产品类型',
            field: 'expForm',
            width: "10%"
        }, {
            title: '审核人',
            field: 'name',
            width: "10%",
            editor: {
                type: 'combogrid',
                options: {
                    panelWidth: 500,
                    idField: 'name',
                    textField: 'name',
                    loadMsg: '数据正在加载',
                    url: '/api/staff-dict/list-by-hospital?hospitalId=' + parent.config.hospitalId,
                    mode: 'remote',
                    method: 'GET',
                    columns: [[
                        //{field: 'empNo', title: '员工编号', width: 150, align: 'center'},
                        {field: 'name', title: '姓名', width: 150, align: 'center'},
                        {field: 'loginName', title: '登录名', width: 150, align: 'center'},
                        {field: 'inputCode', title: '拼音码', width: 150, align: 'center'}
                    ]],
                    pagination: false,
                    fitColumns: true,
                    rowNumber: true,
                    autoRowHeight: false,
                    pageSize: 50,
                    pageNumber: 1
                }
            }
        }, {
            title: '审核数量',
            field: 'auditingQuantity',
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
        },{
            field:"importDocumentNo",
            hidden:true
        }]],
        onLoadSuccess:function(data){
            if(data.total==0){
                $.messager.alert("系统提示","未查询到数据！","info");
                $("#applyDialog").dialog('close');
            }
        },
        onClickCell: function (index, field, row) {
            if (index != editIndex) {
                $(this).datagrid('endEdit', editIndex);
                editIndex = index;
            }
            $(this).datagrid('beginEdit', editIndex);
        }
    });

    //全选功能
    $("#selectAll").on('click', function () {
        $("#applyDatagrid").datagrid('selectAll');
    });
    //不选功能
    $("#selectNon").on('click', function () {
        $("#applyDatagrid").datagrid('unselectAll');
    });
    //作废功能
    $("#abandon").on('click', function () {
        $("#applyDatagrid").datagrid('endEdit', editIndex);
        $("#applyDatagrid").datagrid("acceptChanges");
        var rows = $("#applyDatagrid").datagrid("getSelections");
        if(rows.length<=0){
            $.messager.alert("系统提示", "请选择要作废的数据", "info");
        }else{
            $.messager.confirm("提示信息","确定要作废这些项目吗？",function(r){
                if(r){
                    $.postJSON("/api/exp-provide-application/abandon", rows, function (data) {
                        $.messager.alert("系统提示", "作废成功", "info");
                        //刷新左侧列表
                        $("#applyDialog").dialog('close');
                        $("#search").click();
                    }, function (data) {
                        $.messager.alert('提示', data.responseJSON.errorMessage, "error");
                    });
                }

            })
        }
    });
    //确定功能
    $("#confirm").on('click', function () {
        $("#applyDatagrid").datagrid('endEdit', editIndex);
        var rows = $("#applyDatagrid").datagrid("getSelections");
        console.info("************"+rows);
        for(var i =0 ;i<rows.length;i++){
            if(rows[i].auditingQuantity==0){
                $.messager.alert('系统消息','审核数量不能为0！','info');
                return;
            }
            //console.log(rows[i].auditingOperator)
            if(rows[i].name==undefined){
                $.messager.alert('系统消息', '审核人不能为空！', 'info');
                return;
            }
        }
        if (rows.length <= 0) {
            $.messager.alert("系统提示", "请选择要出库的数据", "info");
        } else {
            for(var i =0;i<rows.length;i++){
                rows[i].quantity = rows[i].auditingQuantity;
            }
            $("#right").datagrid("loadData", rows);

            $("#applyDialog").dialog('close');
            //$("#expDetailDialog").dialog('open');
            //$("#expDetailDatagrid").datagrid('selectRow', 0);
            $.messager.alert('系统提示','请双击数据选择对应的规格！','info');
        }
    });
    //取消功能
    $("#cancel").on('click', function () {
        $("#applyDialog").dialog('close');
    });

    //选择规格
    $("#expDetailDialog").dialog({
        title: '选择规格',
        width: 700,
        height: 300,
        closed: false,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var currentSubStorage = $("#subStorage").combobox("getValue");
            $("#expDetailDatagrid").datagrid('load', {
                storageCode: parent.config.storageCode,
                expCode: currentExpCode,
                hospitalId: parent.config.hospitalId,
                subStorage: currentSubStorage
            });
            $("#expDetailDatagrid").datagrid('selectRow', 0);
        }
    });

    $("#expDetailDatagrid").datagrid({
        singleSelect: true,
        fit: true,
        fitColumns: true,
        ////
        //
//        ？？
        url: '/api/exp-stock/stock-export-record/',
        method: 'GET',
        columns: [[{
            title: '代码',
            field: 'expCode'
        }, {
            title: '名称',
            field: 'expName'
        },{
            title: '数量',
            field: 'quantity'
        }, {
            title: '包装规格',
            field: 'packageSpec'
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
        onDblClickRow: function (index, row) {
            $("#right").datagrid('endEdit', editIndex);
            var rowDetail = $("#right").datagrid('getData').rows[editIndex];
            rowDetail.expName = row.expName;
            rowDetail.expForm = row.expForm;
            rowDetail.packageSpec = row.packageSpec;
            rowDetail.expCode = row.expCode;
            rowDetail.units = row.minUnits;
            rowDetail.packageUnits = row.units;
            rowDetail.disNum = row.quantity;
            rowDetail.purchasePrice = row.purchasePrice;
            rowDetail.retailPrice = row.retailPrice;
            rowDetail.tradePrice = row.tradePrice;
            rowDetail.memos = row.memos;
            rowDetail.firmId = row.firmId;
            rowDetail.batchNo = row.batchNo;
            rowDetail.expireDate = row.expireDate;
            rowDetail.producedate = row.producedate;
            rowDetail.disinfectdate = row.disinfectdate;
            rowDetail.killflag = row.killflag;
//            rowDetail.importDocumentNo=row.importDocumentNo;
            $("#right").datagrid('refreshRow', editIndex);
            $("#expDetailDialog").dialog('close');
            $("#right").datagrid('beginEdit', editIndex);
        },
        onLoadSuccess: function (data) {
            if (data.total == 0 && (editIndex!=undefined)) {
                $("#right").datagrid('endEdit', editIndex);
                $.messager.alert('系统提示', '当前子库房暂无该产品,请重置子库房后再进行查询！', 'info');
                $("#expDetailDialog").dialog('close');
                //$("#exportDetail").datagrid('beginEdit', editIndex);
            }
        }
    });


    //查询功能
    $("#search").on('click', function () {
        var startDate = $("#startDate").datetimebox('getText');
        var endDate = $("#endDate").datetimebox('getText');
        var applyStorage = $("#storage").combobox("getValue");
        var provideStorage = parent.config.storageCode;
        var hospitalId = parent.config.hospitalId;
        $.get('/api/exp-export/export-apply?storage=' + provideStorage + "&applyStorage=" + applyStorage +"&hospitalId="+ hospitalId+ "&startDate=" + startDate + "&endDate=" + endDate, function (data) {
            if (data.length <= 0) {
                $("#left").datagrid("loadData", []);
                $.messager.alert('系统提示', '数据库暂无数据', 'info');
            } else {
                $("#left").datagrid("loadData", data);
            }
        });
    });

    //追加功能
    $("#addRow").on('click', function () {
        $('#right').datagrid('appendRow', {});
        var rows = $("#right").datagrid('getRows');
        var appendRowIndex = $("#right").datagrid('getRowIndex', rows[rows.length - 1]);

        if (editIndex || editIndex == 0) {
            $("#right").datagrid('endEdit', editIndex);
        }
        editIndex = appendRowIndex;
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

    //申请数据按钮功能
    $("#apply").on('click', function () {
        var rows = $("#left").datagrid("getSelections");
        var nos = "";
        if(rows.length<=0){
            $.messager.alert("提示", "请选择出库申请单", "info");
        }else{
            $.each(rows, function (index, item) {
                nos+="'"+item.applicantNo+"',";
            })
            nos = nos.substr(0,nos.length-1);
            applyNo = nos;
            $("#applyDialog").dialog('open');
        }

    });

    /**
     * 进行数据校验
     */
    var dataValid = function () {
        var rows = $("#right").datagrid('getRows');
        if (rows.length == 0) {
            $.messager.alert("系统提示", "明细记录为空，不允许保存", 'error');
            return false;
        }
        //判断供货商是否为空
        var receiver = $("#receiver").combogrid('getValue');
        if (!receiver) {
            $.messager.alert("系统提示", "产品出库，发往不能为空", 'error');
            return false;
        }
        var exportDate = $("#exportDate").datebox('calendar').calendar('options').current;
        if (!exportDate) {
            $.messager.alert("系统提示", "产品出库，出库时间不能为空", 'error');
            return false;
        }
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].quantity == 0) {
                $.messager.alert("系统提示", "第" + (parseInt(i+1)) + "行出库数量为0 请重新填写", 'error');
                return false;
            }
            if(($.trim(rows[i].purchasePrice)==""|| rows[i].purchasePrice==0)|| ($.trim(rows[i].disNum)=="")|| rows[i].disNum==0){
                $.messager.alert("系统提示", "第" + (parseInt(i + 1)) + "行出库信息不完善，请双击该行完善出库信息。", 'error');
                return false;
            }
        }

        return true;
    }
    //封装出库数据
    var getCommitData = function () {
        var expExportMasterBeanChangeVo = {};
        expExportMasterBeanChangeVo.inserted = [];
        var exportMaster = {};
        exportMaster.documentNo = $("#documentNo").textbox('getValue');
        exportMaster.storage = parent.config.storageCode;
        exportMaster.exportDate = new Date($("#exportDate").datebox('getValue'));
        exportMaster.receiver = $("#receiver").combogrid('getValue');
        exportMaster.accountReceivable = $("#accountReceivable").numberbox('getValue');
        exportMaster.accountPayed = $("#accountPayed").numberbox('getValue');
        exportMaster.additionalFee = $("#additionalFee").numberbox('getValue');
        exportMaster.exportClass = $("#exportClass").combobox('getValue');
        exportMaster.subStorage = $("#subStorage").combobox('getValue');
        exportMaster.accountIndicator = 1;
        exportMaster.memos = $('#memos').textbox('getValue');
        exportMaster.fundItem = $('#fundItem').combogrid('getValue');
        exportMaster.operator = parent.config.staffName;
        exportMaster.acctoperator = parent.config.staffName;
        //exportMaster.acctdate = "";
        exportMaster.principal = $("#principal").combogrid('getValue');
//        alert($("#principal").combogrid('getValue'));
        exportMaster.storekeeper = $("#storekeeper").combogrid('getValue');
//        ？
        exportMaster.buyer = $("#buyer").combogrid('getValue');
        exportMaster.docStatus = 0;
        exportMaster.hospitalId = parent.config.hospitalId;

        expExportMasterBeanChangeVo.inserted.push(exportMaster);

        //明细记录
        var expExportDetailBeanChangeVo = {};
        expExportDetailBeanChangeVo.inserted = [];

        var rows = $("#right").datagrid('getRows');

        for (var i = 0; i < rows.length; i++) {
            var rowIndex = $("#right").datagrid('getRowIndex', rows[i]);

            var detail = {};
            detail.documentNo = exportMaster.documentNo;
            detail.itemNo = i;
            detail.expCode = rows[i].expCode;
            detail.expSpec = rows[i].expSpec;
            detail.units = rows[i].units;
            detail.batchNo = rows[i].batchNo;
            detail.expireDate = new Date(rows[i].expireDate);
            detail.firmId = rows[i].firmId;
            detail.expForm = rows[i].expForm;
            detail.importDocumentNo = rows[i].importDocumentNo;
            detail.purchasePrice = rows[i].purchasePrice;
            detail.tradePrice = rows[i].tradePrice;
            detail.retailPrice = rows[i].retailPrice;
            detail.packageSpec = rows[i].packageSpec;
            detail.quantity = rows[i].quantity;
            detail.packageUnits = rows[i].packageUnits;
            detail.subPackage1 = rows[i].subPackage1;
            detail.subPackageUnits1 = rows[i].subPackageUnits1;
            detail.subPackageSpec1 = rows[i].subPackageSpec1;
            detail.subPackage2 = rows[i].subPackage2;
            detail.subPackageUnits2 = rows[i].subPackageUnits2;
            detail.subPackageSpec2 = rows[i].subPackageSpec2;
            detail.inventory = rows[i].disNum-rows[i].quantity;
            detail.producedate = new Date(rows[i].producedate);
            detail.disinfectdate = new Date(rows[i].disinfectdate);
            detail.killflag = rows[i].killflag;
            detail.id = rows[i].applicationId;
            detail.recFlag=0;
            //detail.recOperator="";
            //detail.recDate="";
            detail.assignCode = rows[i].assignCode;
            detail.bigCode = rows[i].expCode;
            detail.bigSpec = rows[i].packageSpec;
            detail.bigFirmId = rows[i].firmId;
            detail.expSgtp = "1";
            detail.memo = rows[i].memo;
            detail.hospitalId = parent.config.hospitalId;
            expExportDetailBeanChangeVo.inserted.push(detail);
        }

        var exportVo = {};
        exportVo.expExportMasterBeanChangeVo = expExportMasterBeanChangeVo;
        exportVo.expExportDetailBeanChangeVo = expExportDetailBeanChangeVo;
        console.log(exportVo);
        return exportVo;
    }
    var saveFlag;
    //保存按钮操作
    $("#save").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#right").datagrid('endEdit', editIndex);
        }
        if (!exportFlag) {
            return;
        }
        if(dataValid()){
            $.messager.confirm("提示信息", "确定要进行出库操作？", function (r) {
                if (r) {
                    var exportVo = getCommitData();
                    $.postJSON("/api/exp-stock/exp-export-manage", exportVo, function (data) {
                        if (data.errorMessage) {
                            $.messager.alert("系统提示", data.errorMessage, 'error');
                            return;
                        }
                        $.messager.alert('系统提示', '出库成功', 'success',function(){
                            saveFlag = true;
                            $("#print").trigger('click');
//                            $("#print").trigger('click');
                            //parent.updateTab('申请出库', '/his/ieqm/exp-export-apply');
                            //刷新左侧列表
//                            $("#left").datagrid("reload");
                            var startDate = $("#startDate").datetimebox('getText');
                            var endDate = $("#endDate").datetimebox('getText');
                            var applyStorage = $("#storage").combobox("getValue");
                            var provideStorage = parent.config.storageCode;
                            var hospitalId = parent.config.hospitalId;
                            $.get('/api/exp-export/export-apply?storage=' + provideStorage + "&applyStorage=" + applyStorage +"&hospitalId="+ hospitalId+ "&startDate=" + startDate + "&endDate=" + endDate, function (data) {
                                $("#left").datagrid("loadData", data);
                            });
                            $("#right").datagrid("loadData",[]);
                        }, function (data) {
                            $.messager.alert('提示', data.responseJSON.errorMessage, "error");
                        });
                    }, function (data) {
                        $.messager.alert('提示', data.responseJSON.errorMessage, "error");
                    });
                }

            })
        }
    });
    ///打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var printDocumentNo = $("#documentNo").textbox('getValue');
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-export-apply.cpt&documentNo=" + printDocumentNo;
            $("#report").prop("src",cjkEncode(https));
            //$("#report").prop("src", "http://localhost:8075/WebReport/ReportServer?reportlet=exp%2Fexp%2Fexp-export.cpt&__bypagesize__=false&documentNo=" + printDocumentNo);
        }
    })
    $("#print").on('click', function () {
        if (saveFlag) {
            $("#printDiv").dialog('open');
        } else {
            var printData = $("#right").datagrid('getRows');
            if (printData.length <= 0) {
                $.messager.alert('系统提示', '请先查询数据', 'info');
                return;
            }
            $("#printDiv").dialog('open');
        }
    })
    $("#printClose").on('click', function () {
        parent.updateTab('申请出库', '/his/ieqm/exp-export-apply');
    })
    //清屏按钮操作
    $("#clear").on('click', function () {
        $("#right").datagrid('loadData', {total: 0, rows: []});
    });
});
