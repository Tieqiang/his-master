$.extend($.fn.datagrid.methods, {
    autoMergeCells: function (jq, fields) {
        return jq.each(function () {
            var target = $(this);
            if (!fields) {
                fields = target.datagrid("getColumnFields");
            }
            var rows = target.datagrid("getRows");
            var i = 0,
                j = 0,
                temp = {};
            for (i; i < rows.length; i++) {
                var row = rows[i];
                j = 0;
                for (j; j < fields.length; j++) {
                    var field = fields[j];
                    var tf = temp[field];
                    if (!tf) {
                        tf = temp[field] = {};
                        tf[row[field]] = [i];
                    } else {
                        var tfv = tf[row[field]];
                        if (tfv) {
                            tfv.push(i);
                        } else {
                            tfv = tf[row[field]] = [i];
                        }
                    }
                }
            }

            $.each(temp, function (field, colunm) {
                $.each(colunm, function () {
                    var group = this;
                    if (group.length > 1) {
                        var before,
                            after,
                            megerIndex = group[0];
                        for (var i = 0; i < group.length; i++) {
                            before = group[i];
                            after = group[i + 1];
                            if (after && (after - before) == 1) {
                                continue;
                            }
                            var rowspan = before - megerIndex + 1;
                            if (rowspan > 1) {
                                target.datagrid('mergeCells', {
                                    index: megerIndex,
                                    field: field,
                                    rowspan: rowspan
                                });
                            }
                            if (after && (after - before) != 1) {
                                megerIndex = after;
                            }
                        }
                    }
                });
            });
        });
    }
});

$(function () {
    var expCode = '';
    var prices = [];
    var storage = [];
    var editIndex;

    var rowStyler = function (index, row) {
        if (row.expName == '小计') {
            return 'background-color:#EAF2FF;color:black;font-weight:bolder;';
        }
        if (row.expName == '合计') {
            return 'background-color:#EAF2FF;color:black;font-weight:bolder;';
        }
    }
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#tab1").datagrid('endEdit', editIndex);
            editIndex = undefined;
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
        return null;
    }

    //格式化日期函数：2016-09-09 00:00:00
    function formatterDate2(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var h = 00;
            var mm = 00;
            var s = 00;
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' '
                + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
            return dateTime
        }
    }

    //格式化日期函数：2016-09-09 23:59:59
    function formatterDate3(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var h = 23;
            var mm = 59;
            var s = 59;
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' '
                + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
            return dateTime
        }
    }

    $('#startDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate2,
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
    $('#stopDate').datetimebox({
        value: 'dateTime',
        required: true,
        showSeconds: true,
        formatter: formatterDate3,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#stopDate').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
            $('#stopDate').datetimebox('setText', dateTime);
            $('#stopDate').datetimebox('hidePanel');
        }
    });

    $("#dg").panel({
        title: '调价记录确认',
        toolbar: '#ft',
        footer: '#tb'
    });
    $("#tt").tabs('add', {
        title: '调价记录',
        content: $("#tab1")
    });
    $("#tt").tabs('add', {
        title: '调价盈亏',
        content: $("#tab2")
    });
    $("#tt").tabs('select', '调价记录');
    $("#tab1").datagrid({
        loadMsg: '数据正在加载',
        height: 500,
        singleSelect: true,
        columns: [[{
            title: 'id',
            field: 'id',
            hidden: true
        }, {
            title: '代码',
            field: 'expCode',
            width: "7%"
        }, {
            title: '物价编码',
            field: 'materialCode',
            width: "7%"
        }, {
            title: '产品名称',
            field: 'expName',
            width: "10%"
        }, {
            title: '包装规格',
            field: 'expSpec',
            width: "7%"
        }, {
            title: '单位',
            field: 'units',
            width: "7%"
        }, {
            title: '厂家',
            field: 'firmId',
            width: "7%"
        }, {
            title: '原批发价',
            field: 'originalTradePrice',
            width: "5%"
        }, {
            title: '新批发价',
            field: 'currentTradePrice',
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
            title: '原零售价',
            field: 'originalRetailPrice',
            width: "5%"
        }, {
            title: '新零售价',
            field: 'currentRetailPrice',
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
            title: '实际生效日期',
            field: 'actualEfficientDate',
            width: "15%",
            editor: {
                type: 'datetimebox'
            }
        }, {
            title: '通知生效日期',
            field: 'noticeEfficientDate',
            formatter: formatterDate,
            width: "15%"
        }, {
            title: '调价依据',
            field: 'modifyCredential',
            width: "7%"
        }, {
            title: '医院编码',
            field: 'hospitalId',
            width: "7%",
            hidden: true
        }
        ]],
        onClickRow: function (index, row) {
            stopEdit();
            $(this).datagrid('beginEdit', index);
            editIndex = index;
        },
        onLoadSuccess: function () {
            if ($(this).datagrid('getRows').length > 0) {
                $("#delete").linkbutton('enable');
                $("#add").linkbutton('enable');
            } else {
                $("#delete").linkbutton('disable');
                $("#add").linkbutton('disable');
            }

        }
    });

    $("#tab2").datagrid({
        loadMsg: '数据正在加载',
        height: 500,
        singleSelect: true,
        rowStyler: rowStyler,
        columns: [[{
            title: '库存单位',
            field: 'storageName',
            width: "7%"
        }, {
            title: '代码',
            field: 'expCode',
            width: "7%"
        }, {
            title: '产品名称',
            field: 'expName',
            width: "10%"
        }, {
            title: '包装规格',
            field: 'expSpec',
            width: "7%"
        }, {
            title: '单位',
            field: 'units',
            width: "5%"
        }, {
            title: '厂家',
            field: 'firmId',
            width: "7%"
        }, {
            title: '数量',
            field: 'quantity',
            width: "5%"
        }, {
            title: '原批发价',
            field: 'originalTradePrice',
            width: "5%"
        }, {
            title: '新批发价',
            field: 'currentTradePrice',
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
            title: '批价盈亏',
            field: 'tradePriceProfit',
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
        }, {
            title: '原零售价',
            field: 'originalRetailPrice',
            width: "5%"
        }, {
            title: '新零售价',
            field: 'currentRetailPrice',
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
            title: '零价盈亏',
            field: 'retailPriceProfit',
            width: "10%",
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2
                }
            },
            formatter: function (value, row, index) {
                if (row.retailPriceProfit) {
                    return row.retailPriceProfit.toFixed(2);
                } else {
                    return value;
                }
            }

        }, {
            title: '执行时间',
            field: 'actualEfficientDate',
            width: "15%",
            formatter: formatterDate
        }
        ]],
        onLoadSuccess: function () {
            if ($(this).datagrid('getRows').length > 0) {
                $("#save").linkbutton('enable');
            } else {
                $("#save").linkbutton('disable');
            }
        }
    });

    //计算按钮功能
    $("#add").on('click', function () {
        stopEdit();
        var rows = $("#tab1").datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            var line = i + 1;

            if (rows[i].noticeEfficientDate == '') {
                $.messager.alert("系统提示", "第" + line + "行实际生效时间为空 请重新填写", 'error');
                return false;
            }
        }

        if (rows.length == 0) {
            $.messager.alert("系统提示", "调价记录为空，不允许计算", 'error');
            return false;
        }
        $("#tt").tabs('select', '调价盈亏');
        loadProfit();
    });

    //删除按钮功能
    $("#delete").on('click', function () {
        $("#tt").tabs('select', '调价记录');
        var row = $('#tab1').datagrid('getSelected');
        var index = $('#tab1').datagrid('getRowIndex', row);
        if (index == -1) {
            $.messager.alert("提示", "请选择删除的行", "info");
        } else {
            $('#tab1').datagrid('deleteRow', index);
            editIndex = undefined;
            var clear = [];
            $('#tab2').datagrid('loadData', clear);
            $('#tab2').datagrid('loadData', {total: 0, rows: []});
        }
    });

    //查询按钮操作
    $("#search").on('click', function () {
        $("#tt").tabs('select', '调价记录');
        var startDate = $('#startDate').datetimebox('getText');
        var stopDate = $('#stopDate').datetimebox('getText');

        if ($.trim(startDate) != '' && $.trim(stopDate) != '') {
            loadDict();
        } else {
            $.messager.alert("提示", "请先选择起止日期！", "info");
        }
    });

    //保存按钮操作
    $("#save").on('click', function () {
        stopEdit();

        var inserted = $("#tab2").datagrid("getRows");
        var deleted = $("#tab1").datagrid('getChanges', 'deleted');
        var updated = $("#tab1").datagrid("getRows");

        var expPriceModifyChange = {};
        expPriceModifyChange.deleted = deleted;
        expPriceModifyChange.updated = updated;

        var expPriceModifyProfitChange = {};
        expPriceModifyProfitChange.inserted = inserted;

        var expPriceModifyProfitVo = {};
        expPriceModifyProfitVo.expPriceModifyChange = expPriceModifyChange;
        expPriceModifyProfitVo.expPriceModifyProfitChange = expPriceModifyProfitChange;

        if (expPriceModifyProfitVo) {
            for (var i = 0; i < expPriceModifyProfitVo.expPriceModifyChange.updated.length; i++) {
                if (expPriceModifyProfitVo.expPriceModifyChange.updated[i].actualEfficientDate == null || expPriceModifyProfitVo.expPriceModifyChange.updated[i].actualEfficientDate == '') {
                    $.messager.alert('系统提示', '请设置实际生效日期,并保证实际生效日期不小于通知生效日期', 'info');
                    return;
                } else if ((new Date(expPriceModifyProfitVo.expPriceModifyChange.updated[i].actualEfficientDate)).getTime() < expPriceModifyProfitVo.expPriceModifyChange.updated[i].noticeEfficientDate) {
                    $.messager.alert('系统提示', '实际生效日期不能小于通知生效日期,请重新设置', 'info');
                    $('#tab1').datagrid('selectRow', i);
                    return;
                } else {
                    if (expPriceModifyProfitVo.expPriceModifyProfitChange.inserted.length == 0) {
                        $.messager.alert('系统信息', '请点击计算按钮计算了调价盈亏后再保存', 'info');
                        return;
                    }
                }
            }
            console.log(expPriceModifyProfitVo);
            $.postJSON("/api/exp-price-modify/save-modify-confirm", expPriceModifyProfitVo, function (data) {
                $.messager.alert("系统提示", "保存成功", "info");
                //loadDict();
                $("#tab1").datagrid('loadData', {total: 0, rows: []})
                $("#tab2").datagrid('loadData', {total: 0, rows: []})
            }, function (data) {
                $.messager.alert('提示', "保存失败", "error");
            })
        }
    });
    //加载页面
    var loadDict = function () {
        var startDate = $('#startDate').datetimebox('getText');
        var stopDate = $('#stopDate').datetimebox('getText');
        $.get("/api/exp-price-modify/list?startDate=" + startDate + "&stopDate=" + stopDate, function (data) {
            if (data.length > 0) {
                $("#tab1").datagrid('loadData', data);
            } else {
                $.messager.alert("系统提示", "数据库暂无数据", "info");
                return;
            }

        });
    }

    //加载调价盈亏页面
    var loadProfit = function (expCode) {
        var rows = $("#tab1").datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            var line = i + 1;
            if (rows[i].actualEfficientDate == null || $.trim(rows[i].actualEfficientDate) == '') {
                $.messager.alert('系统提示', '第' + line + '行实际生效日期为空,请设置实际生效日期,并确保实际生效日期不小于通知生效日期', 'info');
                return;
            } else if ((new Date(rows[i].actualEfficientDate)).getTime() < rows[i].noticeEfficientDate) {
                $.messager.alert('系统提示', '实际生效日期不能小于通知生效日期,请重新设置', 'info');
                $('#tab1').datagrid('selectRow', i);
                return;
            } else {
                //格式化日期
                rows[i].noticeEfficientDate = new Date(rows[i].noticeEfficientDate);
                rows[i].actualEfficientDate = new Date(rows[i].actualEfficientDate);
            }
        }
        $.postJSON("/api/exp-price-modify-profit/calc-profit", rows, function (data) {
            if (data) {
                $("#tab2").datagrid('loadData', data);
            }
        });
    }
});