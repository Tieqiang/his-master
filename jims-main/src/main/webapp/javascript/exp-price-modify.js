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
    var editIndex;

    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
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
                + (h < 10 ? ("0" + h) : h)+":"+ (mm < 10 ? ("0" + mm) : mm)+":"+ (s < 10 ? ("0" + s) : s);
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
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d)  + ' ' + time;
            $('#startDate').datetimebox('setText', dateTime);
            $('#startDate').datetimebox('hidePanel');
        }
    });
    $('#stopDate').datetimebox({
        value: 'dateTime',
        required: true,
        showSeconds: true,
        formatter: formatterDate,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#stopDate').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
                //(m < 10 ? ("0" + m) : m) + "/" + (d < 10 ? ("0" + d) : d) + "/" + y + " " + time;
            $('#stopDate').datetimebox('setText', dateTime);
            $('#stopDate').datetimebox('hidePanel');
        }
    });

    var setDefaultDate = function () {
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var h = date.getHours();
        var mm = date.getMinutes();
        var s = date.getSeconds();
        var time = (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
        return m + '/' + d + '/' + y + " " + time;
    }


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
                expCode: expCode,
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
            title: '物价编码',
            field: 'materialCode'
        }, {
            title: '注册证号',
            field: 'registerNo'
        }, {
            title: '许可证号',
            field: 'permitNo'
        }]],
        onClickRow: function (index, row) {
            var expCodeEdit = $("#dg").datagrid('getEditor', {index: editIndex, field: 'expCode'});
            $(expCodeEdit.target).textbox('setValue', row.expCode);

            //var expNameEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'expName'});
            //$(expNameEd.target).textbox('setValue', row.expName);
            var materialCodeEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'materialCode'});
            $(materialCodeEd.target).textbox('setValue', row.materialCode);

            var SpecEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'expSpec'});
            $(SpecEd.target).textbox('setValue', row.expSpec);

            var unitsEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'units'});
            $(unitsEd.target).textbox('setValue', row.units);

            var minSpecEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'minSpec'});
            $(minSpecEd.target).textbox('setValue', row.minSpec);

            var minUnitsEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'minUnits'});
            $(minUnitsEd.target).textbox('setValue', row.minUnits);

            var firmIdEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'firmId'});
            $(firmIdEd.target).textbox('setValue', row.firmId);

            var originalTradePriceEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'originalTradePrice'});
            $(originalTradePriceEd.target).numberbox('setValue',row.tradePrice);

            var currentTradePriceEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'currentTradePrice'});
            $(currentTradePriceEd.target).numberbox('setValue', '0.00');

            var originalRetailPriceEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'originalRetailPrice'});
            $(originalRetailPriceEd.target).numberbox('setValue', row.retailPrice);

            var currentRetailPriceEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'currentRetailPrice'});
            $(currentRetailPriceEd.target).numberbox('setValue', '0.00');

            var noticeEfficientDateEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'noticeEfficientDate'});
            $(noticeEfficientDateEd.target).textbox('setValue', setDefaultDate());

            var modifyCredentialEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'modifyCredential'});
            $(modifyCredentialEd.target).textbox('setValue', 'x');

            var modifyManEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'modifyMan'});
            $(modifyManEd.target).textbox('setValue', parent.config.staffName);

            $("#stockRecordDialog").dialog('close');
        }
    });

    $("#hisDialog").dialog({
        title: '产品价格信息',
        width: 1000,
        height: 300,
        left: 100,
        top:100,
        closed: false,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            $("#priceHis").datagrid('load', {
                expCode: expCode
            });
        }
    });
    $("#priceHis").datagrid({
        url: '/api/exp-price-modify/list-history/',
        method: 'GET',
        singleSelect: true,
        fit:true,
        columns: [[{
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
            width: "7%"
        }, {
            title: '产品规格',
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
            title: '市场价',
            field: 'tradePrice',
            width: "5%"
        }, {
            title: '零售价',
            field: 'retailPrice',
            width: "5%"
        }, {
            title: '包装量',
            field: 'amountPerPackage',
            width: "7%"
        }, {
            title: '最小规格',
            field: 'minSpec',
            width: "7%"
        }, {
            title: '最小单位',
            field: 'minUnits',
            width: "5%"
        }, {
            title: '起用日期',
            field: 'startDate',
            width: "8%",
            formatter: formatterDate
        }, {
            title: '停止日期',
            field: 'stopDate',
            width: "8%",
            formatter: formatterDate
        }, {
            title: '备注',
            field: 'memos',
            width: "7%"
        }
        ]]
    });

    $("#dg").datagrid({
        title: '调价记录维护',
        loadMsg: '数据正在加载',
        toolbar: '#ft',
        footer: '#tb',
        singleSelect: true,
        fit:true,
        columns: [[{
            title: '代码',
            field: 'expCode',
            width: "7%",
            editor: {type: 'textbox', options: {editable: false}}
        }, {
            title: '产品名称',
            field: 'expName',
            width: "10%",
            editor: {
                type: 'combogrid',
                options: {
                    panelWidth: 500,
                    idField: 'expName',
                    textField: 'expName',
                    loadMsg: '数据正在加载',
                    url: "/api/exp-name-dict/list-exp-name-by-input",
                    mode: 'remote',
                    method: 'GET',
                    columns: [[
                        {field: 'expCode', title: '编码', width: 150, align: 'center'},
                        {field: 'expName', title: '名称', width: 200, align: 'center'}
                    ]],
                    onClickRow: function (index, row) {
                        var ed = $("#dg").datagrid('getEditor', {index: editIndex, field: 'expCode'});
                        $(ed.target).val(row.expCode);
                        expCode = row.expCode;
                        $("#stockRecordDialog").dialog('open');
                        //var edu = $("#dg").datagrid('getEditor', {index: editIndex, field: 'units'});
                        //$(edu.target).combobox('setValue', row.units);
                    },
                    pagination: false,
                    fitColumns: true,
                    rowNumber: true,
                    autoRowHeight: false,
                    pageSize: 50,
                    pageNumber: 1
                }
            }
        }, {
            title: '物价编码',
            field: 'materialCode',
            width: "7%",
            editor: {type: 'textbox', options: {editable:false}}
        }, {
            title: '包装规格',
            field: 'expSpec',
            width: "10%",
            editor: {type: 'textbox', options: {editable: false}}
        }, {
            title: '包装单位',
            field: 'units',
            width: "5%",
            editor: {type: 'textbox', options: {editable: false}}
        }, {
            title: '最小规格',
            field: 'minSpec',
            width: "7%",
            editor: {type: 'textbox', options: {editable: false}}
        }, {
            title: '最小单位',
            field: 'minUnits',
            width: "7%",
            editor: {type: 'textbox', options: {editable: false}}
        }, {
            title: '厂家',
            field: 'firmId',
            width: "7%",
            editor: {type: 'textbox', options: {editable: false}}
        }, {
            title: '原批发价',
            field: 'originalTradePrice',
            width: "5%",
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2,
                    editable: false
                }
            }
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
            width: "5%",
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2,
                    editable: false
                }
            }
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
            title: '通知生效日期',
            field: 'noticeEfficientDate',
            width: "15%",
            formatter: formatterDate,
            editor: {
                type: 'datetimebox',
                options: {
                    value: 'dateTime',
                    showSeconds: true,
                    formatter: formatterDate,
                    parser: w3,
                    onSelect: function (date) {
                        var dateEd = $("#dg").datagrid('getEditor', {
                            index: editIndex,
                            field: 'noticeEfficientDate'
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
            title: '调价依据',
            field: 'modifyCredential',
            width: "7%",
            editor: {type: 'textbox', options: {}}
        }, {
            title: '调价人',
            field: 'modifyMan',
            width: "7%",
            editor: {type: 'textbox', options: {}}
        }
        ]],
        onClickRow: function (index, row) {
            stopEdit();
            if (!row.id > 0) {
                $(this).datagrid('beginEdit', index);
                editIndex = index;
            }else{
                expCode = row.expCode;
                $("#hisDialog").dialog('open');
            }
        }
    });

    //新增按钮功能
    $("#add").on('click', function () {
        stopEdit();
        var rows = $('#dg').datagrid("getRows");

        $('#dg').datagrid('appendRow', {});

        var addRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);
        editIndex = addRowIndex;
        $("#dg").datagrid('selectRow', editIndex);
        $("#dg").datagrid('beginEdit', editIndex);
    });

    //删除按钮功能
    $("#delete").on('click', function () {
        var row = $('#dg').datagrid('getSelected');
        var index = $('#dg').datagrid('getRowIndex', row);
        if (index == -1) {
            $.messager.alert("提示", "请选择删除的行", "info");
        } else {
            if (row.id>0) {
                $.messager.alert("提示", "不能删除该记录！", "info");
            } else {
                $('#dg').datagrid('deleteRow', index);
                editIndex = undefined;
            }
        }
    });

    //查询按钮操作
    $("#search").on('click', function () {
        $("#dg").datagrid('loadData', []);
        $("#priceHis").datagrid('loadData', []);
        var startDate = $('#startDate').datetimebox('getText');
        var stopDate = $('#stopDate').datetimebox('getText');

        if ($.trim(startDate) != ''&& $.trim(stopDate) != '') {
            loadDict();
        } else {
            $.messager.alert("提示", "请先选择起止日期！", "info");
        }
    });

    //保存按钮操作
    $("#save").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
        }
        var flag=true;
        var inserted = $("#dg").datagrid("getChanges", "inserted");
        var deleted = $("#dg").datagrid('getChanges', 'deleted');
        var updated = $("#dg").datagrid('getChanges', 'updated');

        $.each(inserted, function (index, item) {
            if(item.currentTradePrice<=0 || item.currentRetailPrice<=0){
                $.messager.alert("系统提示", "第" + parseInt(index+1) + "行新批发价或新零售价为0 请重新填写", 'error');
                return flag=false;

            }
            //格式化日期
            item.noticeEfficientDate = new Date(item.noticeEfficientDate);
            item.hospitalId = parent.config.hospitalId;
        });
        $.each(updated, function (index, item) {
            if(item.currentTradePrice<=0 || item.currentRetailPrice<=0){
                $.messager.alert("系统提示", "第" + parseInt(index+1) + "行新批发价或新零售价为0 请重新填写", 'error');
                return flag=false;

            }
            //格式化日期
            item.noticeEfficientDate = new Date(item.noticeEfficientDate);
            item.hospitalId = parent.config.hospitalId;
        });
        $.each(deleted, function (index, item) {
            //格式化日期
            item.noticeEfficientDate = new Date(item.noticeEfficientDate);
            item.hospitalId = parent.config.hospitalId;
        });
        var expPriceModifyChangeVo = {};
        expPriceModifyChangeVo.inserted = inserted;
        expPriceModifyChangeVo.deleted = deleted;
        expPriceModifyChangeVo.updated = updated;
        if (expPriceModifyChangeVo && flag) {
            $.postJSON("/api/exp-price-modify/save", expPriceModifyChangeVo, function (data) {
                $.messager.alert("系统提示", "保存成功", "info");
                parent.updateTab('调价记录维护', '/his/ieqm/exp-price-modify');
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
            if (data.length>0) {
                $("#dg").datagrid('loadData', data);
            }else{
                $.messager.alert("系统提示", "数据库暂无数据", "info");
                return;
            }

        });
    }

});



