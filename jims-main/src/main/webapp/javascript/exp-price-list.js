$(function () {
    var expCode = '';
    var prices = [];
    var simplePrice=[];
    var editIndex;

    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }

    //零售价格=批发价格*价格比例
    var getRetailPrice = function () {
        var ed = $('#dg').datagrid('getEditors', editIndex);

        for (var i = 0; i < ed.length; i++) {
            var e = ed[i];
            var priceRatio;
            var tradePrice;
            if (ed[i].field == 'tradePrice') {
                tradePrice = $(e.target).numberbox('getValue');
            }
            if (ed[i].field == 'priceRatio') {
                priceRatio = $(e.target).numberbox('getValue');
            }
            if(priceRatio&& tradePrice&& tradePrice * priceRatio){
                if (ed[i].field == 'retailPrice') {
                    $(e.target).numberbox('setValue', tradePrice * priceRatio);
                }
            }
        }
    };
    //比较零售价最高零售价
    var comparePrice = function(){
        var ed = $('#dg').datagrid('getEditors', editIndex);

        for (var i = 0; i < ed.length; i++) {
            var e = ed[i];
            var maxRetailPrice;
            var retailPrice;
            if (ed[i].field == 'retailPrice') {
                retailPrice = $(e.target).numberbox('getValue')*1;
            }
            if (ed[i].field == 'maxRetailPrice') {
                maxRetailPrice = $(e.target).numberbox('getValue')*1;
            }
            if (maxRetailPrice< retailPrice) {
                if (ed[i].field == 'maxRetailPrice') {
                    $(e.target).numberbox('setValue', retailPrice);
                    $.messager.alert("提示", "最高零售价不能低于零售价格！", "info");
                }
            }
        }
    }
    //定义expName
    $('#expName').combogrid({
        panelWidth: 500,
        idField: 'expCode',
        textField: 'expName',
        loadMsg: '数据正在加载',
        url: "/api/exp-name-dict/list-exp-name-by-input",
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'expCode', title: '编码', width: 150, align: 'center'},
            {field: 'expName', title: '名称', width: 200, align: 'center'},
            {field: 'inputCode', title: '拼音', width: 50, align: 'center'}
        ]],
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        onSelect: function () {
            expCode = $("#expName").combobox("getValue");
        }
    });

    var data = [];

    $("#dg").datagrid({
        title: '产品价格维护',
        fit: true,//让#dg数据创铺满父类容器
        toolbar: '#ft',
        footer: '#tb',
        singleSelect: true,
        columns: [[{
            title: 'id',
            field: 'id',
            hidden: true
        },{
            title: '代码',
            field: 'expCode',
            align: 'center',
            width: "6%"
        }, {
            title: '品名',
            field: 'expName',
            align: 'center',
            width: "7%"
        }, {
            title: '包装数量',
            field: 'amountPerPackage',
            align: 'center',
            width: "6%",
            editor: {
                type: 'numberbox'
            }
        }, {
            title: '包装规格',
            field: 'expSpec',
            align: 'center',
            width: "6%",
            formatter:function(value,row,index){
                if(/*$.trim(row.amountPerPackage)!=''&& */$.trim(row.minSpec)!=''){
                    row.expSpec = /*row.amountPerPackage+"*"+*/ row.minSpec;
                    return row.expSpec;
                }
                return value;
            }
        }, {
            title: '包装单位',
            field: 'units',
            align: 'center',
            width: "6%",
            editable:false
//            editor: {
//                type: 'combobox',
//                options: {
//                    panelHeight: 200,
//                    valueField: 'measuresName',
//                    textField: 'measuresName',
//                    method: 'get',
//                    url: '/api/measures-dict/list'
//                }
//            }

        }, {
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: "7%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    panelMaxHeight:'200',
                    panelWidth:'100',
                    valueField: 'supplierId',
                    textField: 'supplier',
                    method: 'get',
                    url: "/api/exp-supplier-catalog/find-supplier?supplierName=" + '生产商'
                }
            }
        }, {
            title: '物价编码',
            field: 'materialCode',
            align: 'center',
            width: "6%",
            editor: 'text'
        },{
            title: '批发价格',
            field: 'tradePrice',
            align: 'center',
            width: "6%",
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2,
                    onChange: getRetailPrice
                }
            }
        }, {
            title: '价格比例',
            field: 'priceRatio',
            align: 'center',
            width: "6%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=PRICE_RATIO',
                    onSelect: getRetailPrice
                }
            }
        }, {
            title: '零售价格',
            field: 'retailPrice',
            align: 'center',
            width: "6%",
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
            title: '最高零售价',
            field: 'maxRetailPrice',
            align: 'center',
            width: "7%",
            hidden:true,
            editor: {
                type:'numberbox',
                options: {
                    max: 99999.99,
                    size:8,
                    maxlength:8,
                    precision:2,
                    onChange:comparePrice
                }
            }
        }, {
            title: '注册证号',
            field: 'registerNo',
            align: 'center',
            width: "6%",
            editor: 'numberbox'
        }, {
            title: '最小规格',
            field: 'minSpec',
            align: 'center',
            hidden:true,
            width: "6%",
            editor: {
                type: 'textbox',
                options: {
                    editable: false
                }
            }
            //,
            //editor: {
            //    type: 'combogrid',
            //    options: {
            //        idField: 'minSpec',
            //        textValue: 'minSpec',
            //        singleSelect: true,
            //        method: 'GET',
            //        url: '/api/exp-dict/list-query',
            //        mode: 'remote',
            //        onBeforeLoad: function (para) {
            //            para.expCode = expCode;
            //        },
            //        columns: [[{
            //            field: 'expSpec', title: '包装规格', width: "50%"
            //        }, {
            //            field: 'units', title: '包装单位', width: "50%"
            //        }]],
            //        onClickRow: function (index, row) {
            //            var ed = $("#dg").datagrid('getEditor', {index: editIndex, field: 'minSpec'});
            //            $(ed.target).combogrid('setValue', row.expSpec);
            //            var edu = $("#dg").datagrid('getEditor', {index: editIndex, field: 'minUnits'});
            //            $(edu.target).combobox('setValue', row.units);
            //        },
            //        keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
            //            enter: function (e) {
            //
            //                var row = $(this).combogrid('grid').datagrid('getSelected');
            //                if (row) {
            //                    var ed = $("#dg").datagrid('getEditor', {index: editIndex, field: 'minSpec'});
            //                    $(ed.target).combogrid('setValue', row.expSpec);
            //                    var edu = $("#dg").datagrid('getEditor', {index: editIndex, field: 'minUnits'});
            //                    $(edu.target).combobox('setValue', row.units);
            //                }
            //                $(this).combogrid('hidePanel');
            //            }
            //        })
            //    }
            //}
        }, {
            title: '最小单位',
            field: 'minUnits',
            align: 'center',
            width: "6%",
            hidden:true,
            editor: {
                type: 'textbox',
                options: {
                    editable: false
                }
            }
            //,
            //editor: {
            //    type: 'combobox',
            //    options: {
            //        panelHeight: 'auto',
            //        valueField: 'measuresName',
            //        textField: 'measuresName',
            //        method: 'get',
            //        url: '/api/measures-dict/list'
            //    }
            //}
        }, {
            title: '住院收据费用分类',
            field: 'classOnInpRcpt',
            align: 'center',
            width: "11%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    panelMaxHeight: 200,
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=INP_RCPT_FEE_DICT',
                    filter: function (q, row) {
                        var opts = $(this).combobox('options');
                        return row[opts.textField].indexOf(q) == 0;
                    }
                }
            }
        }, {
            title: '门诊收据费用分类',
            field: 'classOnOutpRcpt',
            align: 'center',
            width: "11%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    panelMaxHeight: 200,
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=OUTP_RCPT_FEE_DICT',
                    filter: function (q, row) {
                        var opts = $(this).combobox('options');
                        return row[opts.textField].indexOf(q) == 0;
                    }
                }
            }
        }, {
            title: '核算项目分类',
            field: 'classOnReckoning',
            align: 'center',
            width: "9%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    panelMaxHeight: 200,
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=RECK_ITEM_CLASS_DICT',
                    filter: function (q, row) {
                        var opts = $(this).combobox('options');
                        return row[opts.textField].indexOf(q) == 0;
                    }
                }
            }
        }, {
            title: '会计科目',
            field: 'subjCode',
            align: 'center',
            width: "6%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    panelMaxHeight:200,
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=TALLY_SUBJECT_DICT&length=3',
                    filter: function (q, row) {
                        var opts = $(this).combobox('options');
                        return row[opts.textField].indexOf(q) == 0;
                    }
                }
            }
        }, {
            title: '病案首页费用项目分类',
            field: 'classOnMr',
            align: 'center',
            width: "12%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    panelMaxHeight: 200,
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=MR_FEE_CLASS_DICT',
                    filter: function (q, row) {
                        var opts = $(this).combobox('options');
                        return row[opts.textField].indexOf(q) == 0;
                    }
                }
            }
        }, {
            title: '许可证号',
            field: 'permitNo',
            align: 'center',
            width: "6%",
            editor: 'text'
        }, {
            title: '美国或欧洲号',
            field: 'fdaOrCeNo',
            align: 'center',
            width: "8%",
            editor: 'text'
        }, {
            title: '备注',
            field: 'memos',
            align: 'center',
            width: "5%",
            editor: 'text'
        }, {
            title: '医院id',
            field: 'hospitalId',
            hidden:true
        }
        ]],
        onClickRow: function (index, row) {
            stopEdit();
            if(row.columnProtect!=1){
                $(this).datagrid('beginEdit', index);
                editIndex = index;
            }
        }
    });
    if(getCookie("exp_code")){
        $('#expName').combo('setText', getCookie("exp_name"));
        $('#expName').combo('setValue', getCookie("exp_code"));
        $("#filter").click();
    }

    //新增按钮功能
    $("#add").on('click', function(){
        stopEdit();

        var rows = $('#dg').datagrid("getRows");
        console.log(rows);
        var newRows = [];

        if (rows.length > 0) {
            var tempRow = $('#dg').datagrid('getData').rows[rows.length - 1] ;
            var obj = {} ;
            //obj = tempRow ;
            $.extend(obj,tempRow)
            obj.packageUnits='' ;
            obj.expSpec='' ;
            obj.amountPerPackage = '' ;
            obj.columnProtect = '0';
            newRows.push(obj) ;
        } else {
            var code = $('#expName').combogrid('getValue');
            $.get("/api/exp-dict/list-query?expCode="+code,function(data){
                $.extend(newRows,data) ;
                for(var i = 0 ;i<newRows.length ;i++){
                    var obj = $("#dg").datagrid('appendRow',newRows[i]) ;
                    var rowsTemp = $("#dg").datagrid('getRows') ;
                    console.log(rowsTemp);
                    var addRowIndex = $("#dg").datagrid('getRowIndex',rowsTemp[rowsTemp.length -1 ]);
                    editIndex = addRowIndex;
                    $("#dg").datagrid('selectRow', editIndex);
                    $("#dg").datagrid('beginEdit', editIndex);
                }
            })
        }
        for(var i = 0 ;i<newRows.length ;i++){
            var obj = $("#dg").datagrid('appendRow',newRows[i]) ;
            var rowsTemp = $("#dg").datagrid('getRows') ;
            console.log(rowsTemp);
            var addRowIndex = $("#dg").datagrid('getRowIndex',rowsTemp[rowsTemp.length -1 ]);
            editIndex = addRowIndex;
            $("#dg").datagrid('selectRow', editIndex);
            $("#dg").datagrid('beginEdit', editIndex);
        }
    });

    //删除按钮功能
    $("#delete").on('click', function(){
        var row = $('#dg').datagrid('getSelected');
        var index = $('#dg').datagrid('getRowIndex', row);
        if (index == -1) {
            $.messager.alert("提示", "请选择删除的行", "info");
        } else {
            if (row.columnProtect == 1) {
                $.messager.alert("提示", "该产品非新定义价格，不能删除，只能停用！", "info");
            } else {
                $('#dg').datagrid('deleteRow', index);
                editIndex = undefined;
            }
        }
    });
    //停价按钮功能
    $("#stop").on('click', function () {
        var row = $('#dg').datagrid('getSelected');
        var index = $('#dg').datagrid('getRowIndex', row);
        if (index == -1) {
            $.messager.alert("提示", "请选择停价的行", "info");
        } else {
            if (row.columnProtect == 1) {
                $.get("/api/exp-stock/get-quantity?expCode="+row.expCode+"&expSpec="+row.expSpec+"&firmId="+row.firmId, function (data) {
                    if(data>0){
                        $.messager.alert("提示", "全院库存不为0，不能停价！", "error");
                    }else{
                        $.postJSON("/api/exp-price-list/stop-price",row, function (data) {
                            $.messager.alert("提示", "停价成功", "info");
                            $("#dg").datagrid('deleteRow', index);
                        }, function (data) {
                            $.messager.alert("提示", data.responseJSON.errorMessage, "error");
                        })
                    }
                })
            } else {
                $.messager.alert("提示", "请先保存价格价格信息！", "info");
            }
        }
    });
    //提取按钮操作
    $("#filter").on('click', function () {
        expCode = $('#expName').combogrid('getValue');

        if ($.trim(expCode) != '') {
            //expNameDict数据添加
            var promise = loadDict();//有价格信息
            var simple = loadSimple();//无价格信息，根据对expDict的查询增加一行价格信息，显示其中几个属性值

            promise.done(function () {
                if (prices.length > 0) {
                    $("#dg").datagrid('loadData', prices);
                    return;
                }else{
                    simple.done(function () {
                        $("#dg").datagrid('loadData', simplePrice);
                        return;
                    });
                }
            });
        } else {
            $.messager.alert("提示", "请先选择产品名称！", "info");
        }
    });
    $("#save").on('click', function(){
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
        }
        var insertData = $("#dg").datagrid("getChanges", "inserted");
        var updateData = $("#dg").datagrid("getChanges", "updated");
        var deleteData = $("#dg").datagrid("getChanges", "deleted");
        var expDictChangeVo = {};
        if(insertData.length>0){
            for(var i=0;i<insertData.length;i++){
                insertData[i].minSpec=insertData[i].expSpec;
                insertData[i].minUnits=insertData[i].units;
            }
        }
        expDictChangeVo.inserted = insertData;
        expDictChangeVo.updated = updateData;
        expDictChangeVo.deleted = deleteData;
        console.log(expDictChangeVo)

        if (expDictChangeVo) {
            $.postJSON("/api/exp-price-list/save", expDictChangeVo, function (data) {
                $.messager.alert("系统提示", "保存成功", "info");

                var promise = loadDict();//有价格信息

                promise.done(function () {
                    if (prices.length > 0) {
                        $("#dg").datagrid('loadData', prices);
                        return;
                    }
                });

            }, function (data) {
                $.messager.alert('提示', data.responseJSON.errorMessage, "error");
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
        closed: true,
        onOpen: function () {
            expCode = $('#expName').combogrid('getValue');
            $("#report").prop("src", parent.config.defaultReportPath + "exp-price-list.cpt&expCode=" + expCode);
        }
    })
    $("#print").on('click', function () {
        var printData = $("#dg").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');
    });

    var loadSimple = function(){
        var expCode = $('#expName').combogrid('getValue');
        simplePrice.splice(0, simplePrice.length);
        var pricePromise = $.get("/api/exp-dict/list-query?expCode=" + expCode, function (data) {
            $.each(data, function (index, item) {
                var price = {};
                price.expCode = item.expCode;
                price.expName = item.expName;
                price.amountPerPackage = item.dosePerUnit;
                price.minSpec = item.expSpec;
                price.minUnits = item.doseUnits;
                price.units = item.doseUnits;
                price.hospitalId = parent.config.hospitalId;
                simplePrice.push(price);
            });
        });
        return pricePromise;
    };

    var loadDict = function () {
        var expCode = $('#expName').combogrid('getValue');
        prices.splice(0, prices.length);
        var pricePromise = $.get("/api/exp-price-list/list?expCode=" + expCode + "&hospitalId=" + parent.config.hospitalId, function (data) {
           console.info(data);
            $.each(data, function (index, item) {
                var price = {};
                price.id=item.id;
                price.expCode = item.expCode;
                price.expName = item.expName;
                price.amountPerPackage = item.amountPerPackage;
                price.expSpec = item.expSpec;
                price.units = item.doseUnits;
                price.firmId = item.firmId;
                price.materialCode = item.materialCode;
                price.tradePrice = item.tradePrice;
                price.priceRatio = item.priceRatio;
                price.retailPrice = item.retailPrice;
                price.maxRetailPrice = item.maxRetailPrice;
                price.registerNo = item.registerNo;
                price.minSpec = item.minSpec;
                price.minUnits = item.minUnits;
                price.classOnInpRcpt = item.classOnInpRcpt;
                price.classOnOutpRcpt = item.classOnOutpRcpt;
                price.classOnReckoning = item.classOnReckoning;
                price.subjCode = item.subjCode;
                price.classOnMr = item.classOnMr;
                price.permitNo = item.permitNo;
                price.fdaOrCeNo = item.fdaOrCeNo;
                price.startDate = item.startDate;
                price.stopDate = item.stopDate;
                price.memos = item.memos;
                price.hospitalId = parent.config.hospitalId;
                price.columnProtect = '1';
                //price.stopPrice = item.stopPrice;
                //price.registerDate = item.registerDate;
                //price.permitDate = item.permitDate;
                //price.fdaOrCeDate = item.fdaOrCeDate;
                //price.otherNo = item.otherNo;
                //price.otherDate = item.otherDate;
                prices.push(price);
            });
        });
        return pricePromise;
    }
});


