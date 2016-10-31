/**
 * Created by tangxinbo on 2015/10/14.
 */
/**
 * 科室申请录入
 */
$(document).ready(function () {
    var fieldNo = 0;    //从左往右，光标停留在第几个单元格
    var editRowIndex;
    var storageDicts = [];
    //var rowDatas ;
    var storageCode;
    var limitNumber = 0;//当前可申领最大值
    var appFlag;

    //库房加载
    var storageDictsPromise = $.get("/api/exp-storage-dept/list-level-by-1", function (data) {
        $.each(data, function (index, item) {
            var storage = {};
            storage.storageCode = item.storageCode;
            storage.storageName = item.storageName;
            storageDicts.push(storage);
        });
        $("#storage").combobox({
            data: storageDicts,
            editable: false,
            valueField: "storageCode",
            textField: "storageName",
            onSelect: function () {
                storageCode = $("#storage").combobox("getValue");
                //alert(storageCode);
            }
        });
    });

    $("#dg").datagrid({
        title: "科室申请录入",
        fit: true,
        toolbar: "#topBar",
        footer: "#tb",
        singleSelect: true,
        rownumbers: true,
        fitColumns: true,
        columns: [[{
            title: "applicationId",
            field: "applicationId",
            hidden: true
        }, {
            title: "applicantStorage",
            field: "applicantStorage",
            hidden: true
        }, {
            title: "申请单号",
            field: "applicantNo",
            align: 'center',
            width: "10%",
            editor: {
                type: "textbox",
                options: {
                    disabled: true
                }
            }
        }, {
            title: "代码",
            field: "expCode",
            align: 'center',
            width: "10%",
            editor: {
                type: "textbox",
                options: {
                    disabled: true
                }
            }
        }, {
            title: "名称",
            field: "expName",
            align: 'center',
            width: "30%",
            editor: {
                type: "combogrid", options: {
                    panelWidth: 700,
                    idField: "expName",
                    textField: "expName",
                    url: "/api/exp-price-list/find-exp-list",
                    mode: 'remote',
                    onBeforeLoad: function (para) {
                        para.storageCode = storageCode;
                    },
                    method: 'GET',
                    columns: [[
                        {field: 'expCode', title: '编码', width: 100, align: 'center'},
                        {field: 'expName', title: '名称', width: 200, align: 'center'},
                        {field: 'amountPerPackage', title: '可申请数量', width: 80, align: 'center'},
                        {field: 'expSpec', title: '包装规格', width: 60, align: 'center'},
                        {field: 'minSpec', title: '最小规格', width: 60, align: 'center'},
                        {field: 'units', title: '单位', width: 50, align: 'center'},
                        {field: 'firmId', title: '厂家', width: 100, align: 'center'},
                        {field: 'inputCode', title: '拼音码', width: 50, align: 'center'},
                    ]],
                    onClickRow: function (rowIndex, rowData) {
                        $("#dg").datagrid('endEdit', editRowIndex);
                        //if (rowData.amountPerPackage > 0 && rowData.supplyIndicator == 1) {
                            var rows = $('#dg').datagrid('getRows');
                            for(var i = 0; i < rows.length; i++){
                                 var rowIndex = $('#dg').datagrid('getRowIndex',rows[i]);
                                 if(rowIndex == editRowIndex){ continue;}
                                 if (rows[i].expCode == rowData.expCode && rows[i].expName == rowData.expName && rows[i].packageSpec == rowData.expSpec
                                 && rows[i].packageUnits == rowData.units && rows[i].expSpec == rowData.minSpec) {
                                     $.messager.alert('系统提示', '相同名称、规格、包装规格、包装单位的物品已经存在,请不要重复添加', 'info');
                                     $('#dg').datagrid('deleteRow', editRowIndex);
                                     var newRows = $('#dg').datagrid("getRows");
                                     var applicationStorage = $("#storage").combobox("getValue");
                                     $('#dg').datagrid('appendRow', {
                                         provideStorage: applicationStorage,
                                         applicationMan: parent.config.staffName,
                                         applicantStorage: parent.config.storageCode
                                     });
                                     var addRowIndex = $("#dg").datagrid('getRowIndex', newRows[newRows.length - 1]);
                                     editRowIndex = addRowIndex;
                                     $("#dg").datagrid('selectRow', editRowIndex);
                                     $("#dg").datagrid('beginEdit', editRowIndex);
                                     return;
                                 }
                            }

                            var rowDetail = $("#dg").datagrid('getData').rows[editRowIndex];
                            rowDetail.packageSpec = rowData.expSpec;
                            rowDetail.expCode = rowData.expCode;
                            rowDetail.packageUnits = rowData.units;
                            rowDetail.expSpec = rowData.minSpec;
                            rowDetail.quantity = rowData.quantity;
                            limitNumber = rowData.amountPerPackage;
                            $("#dg").datagrid('refreshRow', editRowIndex);
                            $("#dg").datagrid('beginEdit', editRowIndex);
                        //}else {
                        //    $.messager.alert('系统消息', '申请库房暂不供应此产品', 'info');
                        //    $("#dg").datagrid('beginEdit', editRowIndex);
                        //    return;
                        //}

                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            $(this).combogrid('hidePanel');
                            //if (row && row.amountPerPackage > 0 && row.supplyIndicator == 1) {
                                var rows = $('#dg').datagrid('getRows');
                                for (var i = 0; i < rows.length; i++) {
                                    var rowIndex = $('#dg').datagrid('getRowIndex', rows[i]);
                                    if (rowIndex == editRowIndex) {
                                        continue;
                                    }
                                    if (rows[i].expCode == row.expCode && rows[i].expName == row.expName && rows[i].packageSpec == row.expSpec
                                        && rows[i].packageUnits == row.units && rows[i].expSpec == row.minSpec) {
                                        $.messager.alert('系统提示', '相同名称、规格、包装规格、包装单位的物品已经存在,请不要重复添加', 'info');
                                        $('#dg').datagrid('deleteRow', editRowIndex);
                                        var newRows = $('#dg').datagrid("getRows");
                                        var applicationStorage = $("#storage").combobox("getValue");
                                        $('#dg').datagrid('appendRow', {
                                            provideStorage: applicationStorage,
                                            applicationMan: parent.config.staffName,
                                            applicantStorage: parent.config.storageCode
                                        });
                                        var addRowIndex = $("#dg").datagrid('getRowIndex', newRows[newRows.length - 1]);
                                        editRowIndex = addRowIndex;
                                        $("#dg").datagrid('selectRow', editRowIndex);
                                        $("#dg").datagrid('beginEdit', editRowIndex);
                                        return;
                                    }
                                }

                                $("#dg").datagrid('endEdit', editRowIndex);
                                var rowDetail = $("#dg").datagrid('getData').rows[editRowIndex];
                                rowDetail.packageSpec = row.expSpec;
                                rowDetail.expCode = row.expCode;
                                rowDetail.packageUnits = row.units;
                                rowDetail.expSpec = row.minSpec;
                                rowDetail.expName = row.expName;
                                rowDetail.quantity = row.quantity;
                                limitNumber = row.amountPerPackage;
                            //} else {
                            //    $.messager.alert('系统消息', '申请库房暂不供应此产品', 'info');
                            //    return;
                            //}
                            $("#dg").datagrid('beginEdit', editRowIndex);
                            //光标定位到数量单元格
                            var selector = "#datagrid-row-r2-2-" + editRowIndex + " > td:nth-child(" + fieldNo + ") > div > table > tbody > tr > td > span > input";
                            $(selector).focus();
                        }
                    })
                }
            }
        }, {
            title: "最小规格",
            field: "expSpec",
            align: 'center',
            width: "10%",
            editor: {
                type: "textbox",
                options: {
                    disabled: true
                }
            }
        }, {
            title: "包装规格",
            field: "packageSpec",
            align: 'center',
            width: "10%",
            editor: {
                type: "textbox",
                options: {
                    disabled: true
                }
            }
        }, {
            title: "单位",
            field: "packageUnits",
            align: 'center',
            width: "10%",
            editor: {
                type: "textbox",
                options: {
                    disabled: true
                }
            }
        }, {
            title: "数量",
            field: "quantity",
            align: 'center',
            width: "10%",
            value: 0,
            editor: {type: "numberbox"}/*,
            formatter: function (value, row, index) {
                if (value > limitNumber) {
                    $.messager.alert("提示", "第" + (parseInt(index + 1)) + "行申请数量超过申领库房的数量，请重新填写申请数量。", "info");
                    appFlag = true;
                } else {
                    appFlag = false;
                }
                return value;
            }*/
        }, {
            title: "申请人",
            field: "applicationMan",
            hidden: true,
            editor: {
                type: "textbox",
                options: {
                    disabled: true
                }
            }
        }, {
            title: "被申请科室",
            field: "provideName",
            hidden: true,
            editor: {
                type: "textbox",
                options: {
                    disabled: true
                }
            }
        }, {
            title: "申请科室",
            field: "applicantName",
            hidden: true,
            editor: {
                type: "textbox",
                options: {
                    disabled: true
                }
            }
        }]],
        onClickRow: function (rowIndex, rowData) {
            if (editRowIndex || editRowIndex == 0) {
                $(this).datagrid("endEdit", editRowIndex);
            }
            $(this).datagrid("beginEdit", rowIndex);
            editRowIndex = rowIndex;
        }
    });
    //查询
    $("#searchBtn").on("click", function () {
        var provideStorage = $("#storage").combobox("getValue");
        var applicantNo = $("#applicantNo").textbox("getValue");
        if ($.trim(applicantNo) != "") {
            applicantNo = "'" + applicantNo + "'";
        }
        $.get("/api/exp-provide-application/find-dict-application?applyStorage=" + parent.config.storageCode + "&applyNo=" + applicantNo + "&storageCode=" + provideStorage, function (data) {
                if (data.length == 0) {
                    $.messager.alert("提示", "未查询到数据", "info");
                    $("#dg").datagrid("loadData", {rows: []});
                    return;
                } else {
                    $("#dg").datagrid("loadData", data);
                }
            }
        );
    });
    //新增
    $("#addBtn").on("click", function () {
        fieldNo = 5;
        if ($("#storage").textbox("getText") == "") {
            $.messager.alert("提示", "请选择库房", "info");
            return;
        }
        var applicationStorage = $("#storage").combobox("getValue");
        $("#dg").datagrid("appendRow", {
            provideStorage: applicationStorage,
            applicationMan: parent.config.staffName,
            applicantStorage: parent.config.storageCode
        });
        var rows = $('#dg').datagrid('getRows');
        var addRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);
        editRowIndex = addRowIndex;
        for (var i = 0; i < rows.length; i++) {
            $('#dg').datagrid('endEdit', i);
        }
        $('#dg').datagrid('selectRow', editRowIndex);
        $('#dg').datagrid('beginEdit', editRowIndex);
        //光标定位到名称单元格
        var selector = "#datagrid-row-r2-2-" + addRowIndex + " > td:nth-child(" + fieldNo + ") > div > table > tbody > tr > td > span > input";
        $(selector).focus();
        fieldNo = 9;
    });
    //删除
    $("#delBtn").on("click", function () {
        var row = $("#dg").datagrid("getSelected");
        var index = $("#dg").datagrid("getRowIndex", row);

        if (index == -1) {
            if (editRowIndex == undefined) {
                $.messager.alert("提示", "请选择删除的行", "info");
                return;
            } else {
                $("#dg").datagrid("deleteRow", editRowIndex);
                editRowIndex = undefined;
            }
        } else {
            $("#dg").datagrid("endEdit", editRowIndex);
            $("#dg").datagrid("deleteRow", index);
            editRowIndex = undefined;

        }
    });

    // 保存
    $("#saveBtn").on("click", function () {
        if (editRowIndex != undefined) {
            $("#dg").datagrid("endEdit", editRowIndex);
            editRowIndex = undefined;
        }
        var rows = $("#dg").datagrid("getRows");
        $.each(rows, function (index, item) {
            if ($.trim(item.expCode) == '' && $.trim(item.expSpec) == '' && $.trim(item.packageSpec) == '' &&
                $.trim(item.packageUnits) == '' && $.trim(item.quantity) == '') {
                $('#dg').datagrid('deleteRow', index);
            }
        });
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].quantity == 0 || rows[i].quantity == "") {
                $.messager.alert('系统提示', '第' + (parseInt(i + 1)) + '行申请数量不能为空或零，请重新填写！', 'error');
                return;
            }
            if ($.trim(rows[i].expName) == "") {
                $.messager.alert('系统提示', '第' + (parseInt(i + 1)) + '行申请数据不能为空，请重新填写！', 'error');
                return;
            }
        }
        /*if (appFlag) {
            return;
        }*/
        var insertData = $("#dg").datagrid("getChanges", "inserted");
        var updateData = $("#dg").datagrid("getChanges", "updated");
        var deleteData = $("#dg").datagrid("getChanges", "deleted");

        if (insertData.length > 0 || updateData.length > 0 || deleteData.length > 0) {
            var changeVo = {};
            changeVo.inserted = insertData;
            changeVo.updated = updateData;
            changeVo.deleted = deleteData;
            if (changeVo) {
                $.postJSON("/api/exp-provide-application/save", changeVo, function (data) {
                    $.messager.alert("系统提示", "保存成功", "info");
                    $("#newBtn").click();
                }, function (data) {
                    $.messager.alert('提示', "保存失败", "error");
                })
            }
        } else {
            $.messager.alert('系统消息', '没有要保存的数据，请规范操作系统！', 'error');
        }
    });


    //新单
    $("#newBtn").on("click", function () {
        $("#dg").datagrid("loadData", {rows: []});
        editRowIndex = undefined;
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
            var applicationStorage = $("#storage").combobox("getValue");
            var applicantNo = $("#applicantNo").textbox("getValue");
            var https = "http://" + parent.config.reportDict.ip + ":" + parent.config.reportDict.port + "/report/ReportServer?reportlet=exp/exp-list/exp-provide-application.cpt" + "&hospitalId=" + parent.config.hospitalId + "&storage=" + parent.config.storageCode + "&appNo=" + applicantNo + "&applyStorage=" + applicationStorage;
            $("#report").prop("src", cjkEncode(https));
        }
    })
    $("#printBtn").on('click', function () {
        var printData = $("#dg").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    })

    document.onkeydown = function (event) {
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if (e.keyCode == 13) {
            if (fieldNo == 9) {
                fieldNo = 5;
                if ($("#storage").textbox("getText") == "") {
                    $.messager.alert("提示", "请选择库房", "info");
                    return;
                }
                var applicationStorage = $("#storage").combobox("getValue");
                $("#dg").datagrid("appendRow", {
                    provideStorage: applicationStorage,
                    applicationMan: parent.config.staffName,
                    applicantStorage: parent.config.storageCode
                });
                var rows = $('#dg').datagrid('getRows');
                var addRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);
                editRowIndex = addRowIndex;
                for (var i = 0; i < rows.length; i++) {
                    $('#dg').datagrid('endEdit', i);
                }
                $('#dg').datagrid('selectRow', editRowIndex);
                $('#dg').datagrid('beginEdit', editRowIndex);
                //光标定位到名称单元格
                var selector = "#datagrid-row-r2-2-" + addRowIndex + " > td:nth-child(" + fieldNo + ") > div > table > tbody > tr > td > span > input";
                $(selector).focus();
                fieldNo = 9;
            }
        }
    }
});