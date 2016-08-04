$(function () {
    var deptId = '';
    var editIndex;
    var applyNo;
    var subStorageDicts = [];
    var documentNo;
    var currentExpCode;
    var saveFlag;

    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#right").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
    var setDefaultDate = function () {
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return m + '/' + d + '/' + y
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

    //入库日期
    $('#importDate').datebox({
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
            $('#importDate').datetimebox('setText', dateTime);
            $('#importDate').datetimebox('hidePanel');
        }
    });
    //出库日期
    $('#exportDate').datebox({
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
    var promise = $.get('/api/exp-sub-storage-dict/list-by-storage?storageCode=' + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId, function (data) {
        subStorageDicts = data;
    });

    promise.done(function () {
        //入库子库房数据加载
        $('#subStorageIn').combobox({
            panelHeight: 'auto',
            data: subStorageDicts,
            valueField: 'subStorage',
            textField: 'subStorage',
            onLoadSuccess: function () {
                var data = $(this).combobox('getData');
                if (data.length > 0) {
                    $(this).combobox('select', data[0].subStorage);
                }
            },
            onChange: function (newValue, oldValue) {
                var no = createNewDocument(newValue, "in");
                $("#documentNoIn").textbox('setValue', no);
                var no = createNewDocument(newValue, "out");
                $("#documentNo").textbox('setValue', no);
            }
        });

        //出库子库房数据加载
        $('#subStorage').combobox({
            panelHeight: 'auto',
            data: subStorageDicts,
            valueField: 'subStorage',
            textField: 'subStorage',
            onLoadSuccess: function () {
                var data = $(this).combobox('getData');
                if (data.length > 0) {
                    $(this).combobox('select', data[0].subStorage);
                }
            },
            onChange: function (newValue, oldValue) {
                var no = createNewDocument(newValue, "out");
                $("#documentNo").textbox('setValue', no);
            }
        });
    });
    //生成单据号
    var createNewDocument = function (subStorageCode, type) {
        var storage;
        $.each(subStorageDicts, function (index, item) {
            if (item.subStorage == subStorageCode) {
                storage = item;
            }
        });
        if (type == "in") {
            if (storage) {
                if (storage.importNoPrefix.length <= 4) {
                    documentNo = storage.importNoPrefix + '000000'.substring((storage.importNoAva + "").length) + storage.importNoAva;
                } else if (storage.importNoPrefix.length = 5) {
                    documentNo = storage.importNoPrefix + '00000'.substring((storage.importNoAva + "").length) + storage.importNoAva;
                } else if (storage.importNoPrefix.length = 6) {
                    documentNo = storage.importNoPrefix + '0000'.substring((storage.importNoAva + "").length) + storage.importNoAva;
                }
            }
        } else {
            if (storage) {
                if (storage.exportNoPrefix.length <= 4) {
                    documentNo = storage.exportNoPrefix + '000000'.substring((storage.exportNoAva + "").length) + storage.exportNoAva;
                } else if (storage.exportNoPrefix.length = 5) {
                    documentNo = storage.exportNoPrefix + '00000'.substring((storage.exportNoAva + "").length) + storage.exportNoAva;
                } else if (storage.exportNoPrefix.length = 6) {
                    documentNo = storage.exportNoPrefix + '0000'.substring((storage.exportNoAva + "").length) + storage.exportNoAva;
                }
            }
        }

        //单据号
        return documentNo;
    }
    $("#documentNoIn").textbox({disabled: true});
    $("#documentNo").textbox({disabled: true});

    //入库分类字典
    $("#importClass").combobox({
        url: '/api/exp-import-class-dict/list',
        valueField: 'importClass',
        textField: 'importClass',
        method: 'GET',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            for (var i = 0; i < data.length; i++) {
                if (data[i].importClass == "采购入库") {
                    $(this).combobox('select', data[i].importClass);
                }
            }
        }
    });

    //招标方式数据加载
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
    });

//    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
//        suppliers = data;
//    });
    //供货方数据加载
//    promise.done(function () {
    $("#supplier").combogrid({
        mode:'remote',
        url:'/api/exp-supplier-catalog/list-with-dept?hospitalId=' + parent.config.hospitalId,
        idField: 'supplierCode',
        textField: 'supplierName',
        method:'GET',
//            data: suppliers,
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
//    });
    /**
     * 加载出库类别数据
     * exp_export_class_dict
     */
    function loadExportClass(){
        $.ajax({
            url:"/api/exp-export-class-dict/list",
            type:"GET",
            dataType:"JSON",
            cache:false,
            success:function(data){
                console.info(data);
                for(var i=0;i<data.length;i++){
                    $("#exportClass").append("<option value="+data[i].direction+">"+data[i].exportClass+"</option>");
                }
            }
        });
    }
    loadExportClass();
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
                $(this).combobox('select', data[0].fundItem);
            }
        }
    });

    //出库类别数据加载
//    $('#exportClass').combobox({
//        panelHeight: 'auto',
//        url: '/api/exp-export-class-dict/list',
//        method: 'GET',
//        valueField: 'exportClass',
//        textField: 'exportClass',
//        onLoadSuccess: function () {
//            var data = $(this).combobox('getData');
//            for (var i = 0; i < data.length; i++) {
//                if (data[i].exportClass == "发放出库") {
//                    $(this).combobox('select', data[i].exportClass);
//                }
//            }
//        }
//    });

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



    $("#exportClass").change(function() {
        var checkValue = $("#exportClass").val();
        alert(checkValue);
        var depts = [];
        var promise = $.get("/api/exp-storage-dept/listLevelByThis?hospitalId=" + parent.config.hospitalId + "&storageCode=" + parent.config.storageCode + "&exportClass=" + checkValue, function (data) {
            console.info(data);
            depts = data;
            return depts;
        });
        promise.done(function () {
            $("#receiver").combogrid({
                idField: 'supplierCode',
                textField: 'supplierName',
                data: depts,
                panelWidth: 300,
                columns: [
                    [
                        {
                            title: '科室名称',
                            field: 'supplierName',
                            width: 200
                        },
                        {
                            title: '科室代码',
                            field: 'supplierCode',
                            width: 50
                        },
                        {
                            title: '输入码',
                            field: 'inputCode',
                            width: 50
                        }
                    ]
                ]
            })
        })
    });
    //发往库房数据加载
//    $('#receiver').combogrid({
//        panelWidth: 500,
//        idField: 'storageCode',
//        textField: 'storageName',
//        loadMsg: '数据正在加载',
//        url: '/api/exp-storage-dept/list?hospitalId=' + parent.config.hospitalId,
//        mode: 'remote',
//        method: 'GET',
//        columns: [[
//            {field: 'storageCode', title: '编码', width: 150, align: 'center'},
//            {field: 'storageName', title: '名称', width: 150, align: 'center'},
//            {field: 'disburseNoPrefix', title: '拼音', width: 100, align: 'center'}
//        ]],
//        pagination: false,
//        fitColumns: true,
//        rowNumber: true,
//        autoRowHeight: false,
//        pageSize: 50,
//        pageNumber: 1
//    });

    //出库负责人数据加载
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

    //出库保管员数据加载
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

    //出库领取人数据加载
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

    //入库负责人数据加载
    $('#principalIn').combogrid({
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

    //入库保管员数据加载
    $('#storekeeperIn').combogrid({
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
    //入库采购员数据加载
    $('#buyerIn').combogrid({
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
    //入库验收人数据加载
    $('#checkMan').combogrid({
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
            $("#expDetailDatagrid").datagrid('load', {
                storageCode: parent.config.storageCode,
                expCode: currentExpCode,
                hospitalId: parent.config.hospitalId
            });
            $("#expDetailDatagrid").datagrid('selectRow', 0);
        }
    });

    $("#expDetailDatagrid").datagrid({
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
            title: '包装规格',
            field: 'expSpec'
        },  {
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
            editor: {
                type: 'datebox'
            }
        }, {
            title: '入库单号',
            field: 'documentNo'
        }, {
            title: '子包装1',
            field: 'subPackage1',
            hidden:true
        }, {
            title: '子单位1',
            field: 'subPackageUnits1',
            hidden:true
        }, {
            title: '子规格1',
            field: 'subPackageSpec1',
            hidden:true
        }, {
            title: '子包装2',
            field: 'subPackage2',
            hidden:true
        }, {
            title: '子单位2',
            field: 'subPackageUnits2',
            hidden:true
        }, {
            title: '子规格2',
            field: 'subPackageSpec2',
            hidden:true
        }, {
            title: '生产日期',
            field: 'producedate',
            hidden:true,
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
                            field: 'producedate'
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
            hidden:true,
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
            title: '灭菌标识',
            field: 'killflag',
            hidden:true
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
            title: '注册证号',
            field: 'registerNo',
            hidden:true
        }, {
            title: '许可证号',
            field: 'permitNo',
            hidden:true
        }]],
        onDblClickRow: function (index, row) {
            $("#dg").datagrid('endEdit', editIndex);
            var rows = $("#dg").datagrid('getRows') ;
            for(var i = 0;i<rows.length;i++){
                if(rows[i].expCode == row.expCode && rows[i].packageSpec==row.expSpec && rows[i].firmId ==row.firmId){
                    $.messager.alert("系统提示","同批次、同厂商、同规格的【"+row.expName+"】记录已经存在，请不要重复添加",'info');
                    return ;
                }
            }
            var rowDetail = $("#dg").datagrid('getData').rows[editIndex];

            rowDetail.expName = row.expName;
            rowDetail.expForm = row.expForm;
            rowDetail.expCode = row.expCode;
            rowDetail.packageSpec = row.expSpec;
            rowDetail.packageUnits = row.units;
            rowDetail.disNum = row.quantity;
            rowDetail.purchasePrice = row.purchasePrice;
            rowDetail.amount = 0;
            rowDetail.firmId = row.firmId;
            rowDetail.batchNo = "X";
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


            rowDetail.expSpec = row.minSpec;
            rowDetail.units = row.minUnits;
            rowDetail.memos = row.memos;
            rowDetail.expImportDetailRegistNo = row.expImportDetailRegistNo;
            rowDetail.expImportDetailLicenceno = row.expImportDetailLicenceno;
            rowDetail.invoiceDate = setDefaultDate();
            rowDetail.invoiceNo = row.invoiceNo;




            $("#dg").datagrid('refreshRow', editIndex);
            $("#expDetailDialog").dialog('close');
            $("#dg").datagrid('beginEdit', editIndex);


        }

    });
    //列表初始化
    $("#dg").datagrid({
        title:'对消入出库',
        fit: true,//让#dg数据创铺满父类容器
        toolbar: '#ft',
        footer: '#tb',
        singleSelect: true,
        nowrap: false,
        columns: [[{
            title: "expCode",
            field: "expCode",
            hidden: 'true'
        }, {
            title: '品名',
            field: 'expName',
            width:'20%',
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
                        $("#expDetailDialog").dialog('open');
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            if (row) {
                                currentExpCode = row.expCode;
                                $("#expDetailDialog").dialog('open');
                            }
                            $(this).combogrid('hidePanel');
                        }
                    })
                }
            }
        }, {
            title: '规格',
            field: 'packageSpec',
            width: '7%'
        }, {
            title: '单位',
            field: 'packageUnits',
            width: '7%'
        }, {
            title: '产品类型',
            field: 'expForm',
            width: '7%'
        }, {
            title: '数量',
            field: 'quantity',
            width: '7%',
            editor: {
                type: 'numberbox',
                options: {
                    onChange: function (newValue, oldValue) {
                        var row = $("#dg").datagrid('getData').rows[editIndex];
                        console.log(row);
                        var amountEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'planNumber'});
                        $(amountEd.target).numberbox('setValue',newValue * row.purchasePrice)
                        var rows = $("#dg").datagrid('getRows');
                        var totalAmount = 0;
                        var exportAmount = 0;
                        for (var i = 0; i < rows.length; i++) {
                            var rowIndex = $("#dg").datagrid('getRowIndex', rows[i]);
                            if (rowIndex == editIndex) {
                                continue;
                            }
                            totalAmount += Number(rows[i].planNumber);
                            exportAmount+= Number(Number(rows[i].quantity)*Number(rows[i].retailPrice));
                        }

                        totalAmount += newValue * row.purchasePrice;
                        exportAmount += newValue * row.retailPrice;
                        $("#accountReceivable").numberbox('setValue', exportAmount);
                        $("#accountReceivableIn").numberbox('setValue', totalAmount);
                    },
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2
                }
            }
        }, {
            title: '进价',
            field: 'purchasePrice',
            width: '7%',
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2,
                    editable: false,
                    disabled: true
                }
            }
        }, {
            title: '金额',
            field: 'planNumber',
            width: '7%',
            editor: {
                type: 'numberbox',
                options: {
                    precision: '2',
                    editable: false,
                    disabled: true
                }
            },
            formatter:function(value,row,index){
                if(row.quantity){
                    return row.quantity * row.purchasePrice;
                }
            }
        }, {
            title: '零售价',
            field: 'retailPrice',
            type:'numberbox'
            //,
            //hidden: 'true'
        }, {
            title: '最小规格',
            field: 'expSpec',
            hidden: 'true'
        }, {
            title: '最小单位',
            field: 'units',
            hidden: 'true'
        }, {
            title: '结存量',
            field: 'disNum',
            hidden:true,
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2,
                    editable: false,
                    disabled: true
                }
            },
            //hidden: 'true',
            formatter:function(value,row,index){
                if($.trim(value)==""){
                    return value = 0;
                }
                return value;
            }
        },  {
            title: '厂家',
            field: 'firmId',
            width: '7%'
        }, {
            title: '分摊方式',
            field: 'assignCode',
            hidden:true,
            //width: '7%',
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
            title: '注册证号',
            field: 'expImportDetailRegistNo',
            //width: '7%',
            hidden:true,
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '许可证号',
            field: 'expImportDetailLicenceno',
            hidden:true,
            //width: '7%',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '备注',
            field: 'memos',
            hidden:true,
            //width: '7%',
            editor: {type: 'text'}
        }, {
            title: '批号',
            field: 'batchNo',
            width: '10%',
            editor:{type:'textbox'}
        }, {
            title: '有效期',
            field: 'expireDate',
            width: '10%',
            editor: {
                type: 'datebox',
                options: {
                    onSelect: function (date) {
//                        flag=0;
                        $("#dg").datagrid('appendRow', {documNo:documentNo});
                        var rows = $("#dg").datagrid('getRows');
                        var appendRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);

                        if (editIndex || editIndex == 0) {
                            $("#dg").datagrid('endEdit', editIndex);
                        }
                        editIndex = appendRowIndex;
                        $("#dg").datagrid('beginEdit', editIndex);
                    },
                    keyHandler: $.extend({}, $.fn.combo.defaults.keyHandler, {
                        enter: function (e) {
                            var dateEd = $("#dg").datagrid('getEditor', {
                                index: editIndex,
                                field: 'expireDate'
                            });

                            var date=$(dateEd.target).textbox('getText');
                            if(date==null&& date==""){
                                if(date.indexOf("-")==-1 || date.length!=10){
                                    $.messager.alert("系统提示","请输入正确格式的日期","error");
                                    return;
                                }
                            }
                            $(dateEd.target).textbox('setValue',date);
                            $("#dg").datagrid('appendRow', {documNo:documentNo});
                            var rows = $("#dg").datagrid('getRows');

                            var appendRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);

                            if (editIndex || editIndex == 0) {
                                $("#dg").datagrid('endEdit', editIndex);
                            }
                            editIndex = appendRowIndex;
                            $("#dg").datagrid('beginEdit', editIndex);
//                            #datagrid-row-r22-2-1 > td:nth-child(2) > div > table > tbody > tr > td > span > input
//                            #datagrid-row-r22-2-0 > td:nth-child(2) > div > table > tbody > tr > td > span > input.textbox-text.validatebox-text
//                            #datagrid-row-r22-2-1 > td:nth-child(2) > div > table > tbody > tr > td > span > input
                            var selector = "#datagrid-row-r22-2-"+appendRowIndex+" > td:nth-child(2) > div > table > tbody > tr > td > span > input";
                            $(selector).focus();

                        }
                    })

                }
            }
        }, {
            title: '发票号',
            field: 'invoiceNo',
            hidden:true,
            //width: '7%',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '发票日期',
            field: 'invoiceDate',
            //width: '10%',
            hidden:true,
            editor: {
                type: 'datebox'
                //options: {
                //    value: 'dateTime',
                //    showSeconds: true,
                //    formatter: formatterDate,
                //    parser: w3,
                //    onSelect: function (date) {
                //        var dateEd = $("#dg").datagrid('getEditor', {
                //            index: editIndex,
                //            field: 'invoiceDate'
                //        });
                //        var y = date.getFullYear();
                //        var m = date.getMonth() + 1;
                //        var d = date.getDate();
                //        //var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
                //        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) ;
                //
                //        $(dateEd.target).textbox('setValue', dateTime);
                //        $(this).datetimebox('hidePanel');
                //    }
                //}
            }
        }, {
            title: '生产日期',
            field: 'producedate',
            //width: '10%',
            hidden:true,
            editor: {
                type: 'datebox'
            }
        }, {
            title: '消毒日期',
            field: 'disinfectdate',
            //width: '10%',
            hidden:true,
            editor: {
                type: 'datebox'
            }
        }, {
            title: '灭菌标识',
            field: 'killflag',
            //width: '7%',
            hidden:true,
            align: 'center',
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
            title: '批发价',
            field: 'tradePrice',
            hidden: 'true'
        }, {
            title: '折扣',
            field: 'discount',
            hidden: 'true'
        }, {

            title: '招标文号',
            field: 'orderBatch',
            hidden: 'true'

        }, {
            title: '招标文号序号',
            field: 'tenderNo',
            hidden: 'true'
        }, {
            title: '现有数量',
            field: 'inventory',
            hidden: 'true'
        }]],
        onClickRow: function (index, row) {
            if (index != editIndex) {
                $(this).datagrid('endEdit', editIndex);
                editIndex = index;
            }
            $(this).datagrid('beginEdit', editIndex);
        }
    });

    //记账功能
    $("#account").on('click', function () {
        $("#save").click();
    });

    //计价按钮功能
    $("#price").on('click', function () {
        $.messager.alert("提示", "计价", "info");
    });

    //新增按钮功能
    $("#add").on('click', function () {
        $("#dg").datagrid('appendRow', {});
        var rows = $("#dg").datagrid('getRows');
        var appendRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);

        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
        }
        editIndex = appendRowIndex;
        $("#dg").datagrid('beginEdit', editIndex);
    });

    /**
     * 删除按钮
     */
    $("#del").on('click', function () {
        var row = $("#dg").datagrid('getSelected');
        if (row) {
            var index = $("#dg").datagrid('getRowIndex', row);
            $("#dg").datagrid('deleteRow', index);
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

        var rows = $("#dg").datagrid('getRows');
        console.log(rows);
        if (rows.length == 0) {
            $.messager.alert("系统提示", "明细记录为空，不允许保存", 'error');
            return false;
        }
        //判断供货方是否为空
        var supplier = $("#supplier").combogrid('getValue');
        if (!supplier) {
            $.messager.alert("系统提示", "产品入库，供货方不能为空", 'error');
            return false;
        }
        var importDate = $("#importDate").datebox('calendar').calendar('options').current;
        if (!importDate) {
            $.messager.alert("系统提示", "产品入库，入库时间不能为空", 'error');
            return false;
        }
        //判断发往是否为空
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
                $.messager.alert("系统提示", "第" + i + "行数量为0 请重新填写", 'error');
                return false;
            }
        }

        return true;
    }
    //封装出库数据
    var getExportData = function () {
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
        exportMaster.exportClass = getSelectedText("exportClass");
        exportMaster.subStorage = $("#subStorageIn").combobox('getValue');
        exportMaster.accountIndicator = 1;
        exportMaster.memos = $('#memos').textbox('getValue');
        exportMaster.fundItem = $('#fundItem').combogrid('getValue');
        exportMaster.operator = parent.config.staffName;
        exportMaster.acctoperator = parent.config.staffName;
        exportMaster.acctdate = new Date();
        exportMaster.principal = $("#principal").combogrid('getText');
        exportMaster.storekeeper = $("#storekeeper").combogrid('getText');
        exportMaster.buyer = $("#buyer").combogrid('getText');
        exportMaster.docStatus = 0;
        exportMaster.hospitalId = parent.config.hospitalId;

        expExportMasterBeanChangeVo.inserted.push(exportMaster);

        //明细记录
        var expExportDetailBeanChangeVo = {};
        expExportDetailBeanChangeVo.inserted = [];

        var rows = $("#dg").datagrid('getRows');
        console.info(rows);
        for (var i = 0; i < rows.length; i++) {
            var rowIndex = $("#dg").datagrid('getRowIndex', rows[i]);

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
            detail.importDocumentNo = $("#documentNoIn").textbox('getValue');
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
            detail.inventory = rows[i].disNum;
            detail.producedate = new Date(rows[i].producedate);
            detail.disinfectdate = new Date(rows[i].disinfectdate);
            detail.killflag = rows[i].killflag;
            detail.recFlag=0;
            //detail.recOperator="";
            //detail.recDate="";
            detail.assignCode = rows[i].assignCode;
            detail.bigCode = rows[i].expCode;
            detail.bigSpec = rows[i].packageSpec;
            detail.bigFirmId = rows[i].firmId;
            detail.expSgtp = "1";
            detail.memo = rows[i].memos;
            detail.hospitalId = parent.config.hospitalId;
            expExportDetailBeanChangeVo.inserted.push(detail);
        }

        var exportVo = {};
        exportVo.expExportMasterBeanChangeVo = expExportMasterBeanChangeVo;
        exportVo.expExportDetailBeanChangeVo = expExportDetailBeanChangeVo;
        return exportVo;
    }
    //封装入库记录
    var getImportData = function () {
        var expImportMasterBeanChangeVo = {};
        expImportMasterBeanChangeVo.inserted = [];
        var importMaster = {};

        importMaster.importDate = new Date($("#importDate").datebox('getValue'));
        importMaster.storage = parent.config.storageCode;
        importMaster.documentNo = $("#documentNoIn").textbox('getValue');
        importMaster.supplier = $("#supplier").combogrid('getValue');
        importMaster.accountReceivable = $("#accountReceivableIn").numberbox('getValue');
        importMaster.accountPayed = $("#accountPayedIn").numberbox('getValue');
        importMaster.additionalFee = $("#additionalFeeIn").numberbox('getValue');
        importMaster.importClass = $("#importClass").combobox('getValue');
        importMaster.subStorage = $("#subStorageIn").combobox('getValue');
        importMaster.accountIndicator = 1;
        importMaster.memos = $('#memosIn').textbox('getValue');
        importMaster.operator = parent.config.staffName;
        importMaster.acctoperator = parent.config.staffName;////////// /////////////////////
        importMaster.acctdate = new Date();
        importMaster.principal = $("#principalIn").combogrid('getValue');
        importMaster.storekeeper = $("#storekeeperIn").combogrid('getValue');
        importMaster.buyer = $("#buyerIn").combogrid('getValue');
        importMaster.checkman = $("#checkMan").combogrid('getValue');
        importMaster.tenderNo = $("#tenderNo").textbox('getValue');
        importMaster.tenderType = $("#tenderType").combobox('getValue');
        importMaster.hospitalId = parent.config.hospitalId;
        importMaster.docStatus = 0;
        expImportMasterBeanChangeVo.inserted.push(importMaster);

        //明细记录
        var expImportDetailBeanChangeVo = {};
        expImportDetailBeanChangeVo.inserted = [];

        var rows = $("#dg").datagrid('getRows');

        for (var i = 0; i < rows.length; i++) {
            var detail = {};
            detail.documentNo = importMaster.documentNo;
            detail.itemNo = i;
            var rowIndex = $("#dg").datagrid('getRowIndex', rows[i]);

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
            detail.subPackage1 = rows[i].subPackage1;
            detail.subPackageUnits1 = rows[i].subPackageUnits1;
            detail.subPackageSpec1 = rows[i].subPackageSpec1;
            detail.subPackage2 = rows[i].subPackage2;
            detail.subPackageSpec2 = rows[i].subPackageSpec2;
            detail.subPackageUnits2 = rows[i].subPackageUnits2;
            detail.memo = rows[i].memos;
            detail.inventory = parseFloat(parseFloat(rows[i].quantity) + parseFloat(rows[i].disNum));
            detail.registerNo = rows[i].registerNo;
            detail.permitNo = rows[i].permitNo;
            //detail.recFlag = 0;
            //detail.expSgtp = "1";
            detail.tallyFlag = 0;
            expImportDetailBeanChangeVo.inserted.push(detail);
        }

        var importVo = {};
        importVo.expImportMasterBeanChangeVo = expImportMasterBeanChangeVo;
        importVo.expImportDetailBeanChangeVo = expImportDetailBeanChangeVo;
        return importVo;
    }
    //保存按钮操作
    $("#save").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
        }
        $.get("/api/app-configer-parameter/list?hospitalId="+parent.config.hospitalId+"&name=DO_ACCT",function(data){
            if(data!=null&&data!=""){
                if(data[0].parameterValue=='0'){
                    $.messager.alert('系统消息','此消耗品不能对消入出库！','error');
                    return;
                }
            }
        })
        if (dataValid()) {

            $.messager.confirm("提示信息", "确定要进行对消入出库操作？", function (r) {
                if (r) {
                    var exportVo = getExportData();
                    var importVo = getImportData();
                    var exportImportVo = {};
                    exportImportVo.exportVo= exportVo;
                    exportImportVo.importVo= importVo;
                    console.log(exportImportVo);
                    $.postJSON("/api/exp-stock/exp-export-import", exportImportVo, function (data) {
                        if (data.errorMessage) {
                            $.messager.alert("系统提示", data.errorMessage, 'error');
                            return;
                        }
                        $.messager.alert("系统提示", "数据保存成功", "info", function () {
                            saveFlag = true;
                            $("#print").trigger('click');
                            //parent.updateTab('对消入出库', '/his/ieqm/exp-export-import-balance');
                        });
                    }, function (data) {
                        $.messager.alert('提示', data.responseJSON.errorMessage, "error");
                    });
                }

            })
        }
    });
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
            var importDocumentNo = $("#documentNoIn").textbox('getValue');
            var exportDocumentNo = $("#documentNo").textbox('getValue');
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-export-import-balance.cpt&importDocumentNo=" + importDocumentNo + "&documentNo=" + exportDocumentNo;
            $("#report").prop("src",cjkEncode(https));
            //$("#report").prop("src", "http://localhost:8075/WebReport/ReportServer?reportlet=exp%2Fexp%2Fexp-export-import-balance.cpt&__bypagesize__=false&importDocumentNo="+importDocumentNo+"&exportDocumentNo="+exportDocumentNo);
        }
    })
    $("#printClose").on('click', function () {
        parent.updateTab('对消入出库', '/his/ieqm/exp-export-import-balance');
    })
    $("#print").on('click', function () {
        if (saveFlag) {
            $("#printDiv").dialog('open');
        } else {
            var printData = $("#dg").datagrid('getRows');
            if (printData.length <= 0) {
                $.messager.alert('系统提示', '请先查询数据', 'info');
                return;
            }
            $("#printDiv").dialog('open');
        }
    })
    //清屏按钮操作
    $("#clear").on('click', function () {
        parent.updateTab('对消入出库', '/his/ieqm/exp-export-import-balance');
    });
    function getSelectedText(name){
        var obj=document.getElementById(name);
        for(i=0;i<obj.length;i++){
            if(obj[i].selected==true){
                return obj[i].innerText;//关键是通过option对象的innerText属性获取到选项文本
            }
        }
    }
});
