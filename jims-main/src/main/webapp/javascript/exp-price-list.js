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
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        pageSize: 50,
        pageNumber: 1,
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
            width: "7%"
        }, {
            title: '品名',
            field: 'expName',
            width: "7%"
        }, {
            title: '包装数量',
            field: 'amountPerPackage',
            width: "5%",
            editor:'numberbox'
        }, {
            title: '包装规格',
            field: 'expSpec',
            width: "10%",
            editor: {
                type: 'combogrid',
                options: {
                    panelHeight: 'auto',
                    idField: 'expSpec',
                    textValue: 'expSpec',
                    singleSelect: true,
                    method: 'GET',
                    url: '/api/exp-dict/list-query',
                    mode: 'remote',
                    onBeforeLoad: function (para) {
                        para.expCode = expCode;
                    },
                    columns: [[{
                        field: 'expSpec', title: '包装规格', width: "50%"
                    }, {
                        field: 'units', title: '包装单位', width: "50%"
                    }]],
                    onClickRow: function (index, row) {
                        var ed = $("#dg").datagrid('getEditor', {index: editIndex, field: 'expSpec'});
                        $(ed.target).combogrid('setValue', row.expSpec);
                        var edu = $("#dg").datagrid('getEditor', {index: editIndex, field: 'units'});
                        $(edu.target).textbox('setValue', row.units);
                        var minSpec = $("#dg").datagrid('getEditor', {index: editIndex, field: 'minSpec'});
                        $(minSpec.target).textbox('setValue', row.expSpec);
                        var minUnits = $("#dg").datagrid('getEditor', {index: editIndex, field: 'minUnits'});
                        $(minUnits.target).textbox('setValue', row.units);
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {

                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            if (row) {
                                var ed = $("#dg").datagrid('getEditor', {index: editIndex, field: 'expSpec'});
                                $(ed.target).combogrid('setValue', row.expSpec);
                                var edu = $("#dg").datagrid('getEditor', {index: editIndex, field: 'units'});
                                $(edu.target).textbox('setValue', row.units);
                                var minSpec = $("#dg").datagrid('getEditor', {index: editIndex, field: 'minSpec'});
                                $(minSpec.target).textbox('setValue', row.expSpec);
                                var minUnits = $("#dg").datagrid('getEditor', {index: editIndex, field: 'minUnits'});
                                $(minUnits.target).textbox('setValue', row.units);
                            }
                            $(this).combogrid('hidePanel');
                        }
                    })
                }
            }
        }, {
            title: '包装单位',
            field: 'units',
            width: "5%",
            editor: {type:'textbox',
                options:{
                    editable:false
                }
            }
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
            title: '厂家',
            field: 'firmId',
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
            title: '物价编码',
            field: 'materialCode',
            width: "7%",
            editor: 'text'
        },{
            title: '批发价格',
            field: 'tradePrice',
            width: "5%",
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
            width: "5%",
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
            width: "5%",
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
            width: "7%",
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
            width: "5%",
            editor: 'numberbox'
        }, {
            title: '最小规格',
            field: 'minSpec',
            width: "10%",
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
            width: "5%",
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
            width: "9%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=INP_RCPT_FEE_DICT'
                }
            }
        }, {
            title: '门诊收据费用分类',
            field: 'classOnOutpRcpt',
            width: "9%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=OUTP_RCPT_FEE_DICT'
                }
            }
        }, {
            title: '核算项目分类',
            field: 'classOnReckoning',
            width: "7%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=RECK_ITEM_CLASS_DICT'
                }
            }
        }, {
            title: '会计科目',
            field: 'subjCode',
            width: "7%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=TALLY_SUBJECT_DICT&length=3'
                }
            }
        }, {
            title: '病案首页费用项目分类',
            field: 'classOnMr',
            width: "9%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    valueField: 'baseCode',
                    textField: 'baseName',
                    method: 'get',
                    url: '/api/base-dict/list-by-type?baseType=MR_FEE_CLASS_DICT'
                }
            }
        }, {
            title: '许可证号',
            field: 'permitNo',
            width: "7%",
            editor: 'text'
        }, {
            title: '美国或欧洲号',
            field: 'fdaOrCeNo',
            width: "7%",
            editor: 'text'
        }, {
            title: '备注',
            field: 'memos',
            width: "7%",
            editor: 'text'
        }, {
            title: '医院id',
            field: 'hospitalId',
            width: "7%",
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
        $('#expName').combogrid('setValue', getCookie("exp_code"));
        $("#filter").click();
    }

    //新增按钮功能
    $("#add").on('click', function(){
        stopEdit();

        var rows = $('#dg').datagrid("getRows");
        var code = "";
        var name = "";

        if (rows.length > 0) {
            code = $('#dg').datagrid('getData').rows[rows.length - 1].expCode;
            name = $('#dg').datagrid('getData').rows[rows.length - 1].expName;
        } else {
            code = $('#expName').combogrid('getValue');
        }
        $('#dg').datagrid('appendRow', {
            expCode: code,
            expName: name,
            hospitalId:parent.config.hospitalId
        });

        var addRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);
        editIndex = addRowIndex;
        $("#dg").datagrid('selectRow', editIndex);
        $("#dg").datagrid('beginEdit', editIndex);
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
                            var promise = loadDict();//有价格信息

                            promise.done(function () {
                                if (prices.length > 0) {
                                    $("#dg").datagrid('loadData', prices);
                                    return;
                                }
                            });
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
        expDictChangeVo.inserted = insertData;
        expDictChangeVo.updated = updateData;
        expDictChangeVo.deleted = deleteData;


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

    $("#print").on('click', function () {
        $.messager.alert('提示', "打印", "info");
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
                price.minUnits = item.units;
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
            $.each(data, function (index, item) {
                var price = {};
                price.id=item.id;
                price.expCode = item.expCode;
                price.expName = item.expName;
                price.amountPerPackage = item.amountPerPackage;
                price.expSpec = item.expSpec;
                price.units = item.units;
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


