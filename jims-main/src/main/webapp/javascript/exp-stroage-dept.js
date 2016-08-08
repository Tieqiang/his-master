/**
 * Created by heren on 2015/9/17.
 */

/**
 * 库房字典维护
 */
$(function () {
    var editIndex;
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
    //初始化表单
    var data = [];

    var promise = $.get('/api/dept-dict/list?hospitalId=' + parent.config.hospitalId, function (data1) {
        data = data1;
    });

    promise.done(function () {
        $("#dg").datagrid({
            title: '库房字典维护',
            rownumbers: true,
            singleSelect: true,
            fit: true,
            url: '/api/exp-storage-dept/list?hospitalId=' + parent.config.hospitalId,
            method: 'GET',
            footer: '#tb',
            columns: [[{
                title: 'id',
                field: 'id',
                hidden: true
            }, {
                title: '库房代码',
                field: 'storageCode',
                align: 'center',
                width: "20%",
                editor: {type: 'textbox', options: {editable: false}}
            }, {
                title: '库房名称',
                field: 'storageName',
                align: 'center',
                width: "20%",
                editor: {
                    type: "combogrid", options: {
                        //editable: false,
                        idField: 'deptCode',
                        textValue: 'deptName',
                        panelHeight: 300,
                        //data:data,
                        singleSelect: true,
                        url: '/api/dept-dict/list-by-input?hospitalId=' + parent.config.hospitalId,
                        mode: 'remote',
                        method: 'GET',
                        columns: [[{
                            field: 'deptCode', title: '科室代码', width: 60, align: 'center'
                        }, {
                            field: 'deptName', title: '科室名称', width: 80, align: 'center'
                        }, {
                            field: 'inputCode', title: '输入码', width: 60, align: 'center'
                        }]],
                        onClickRow: function (index, row) {
                            var ed = $("#dg").datagrid('getEditor', {index: editIndex, field: 'storageCode'});
                            $(ed.target).textbox('setValue', row.deptCode);

                            var comboEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'storageName'});
                            $(comboEd.target).combogrid('setValue', row.deptName);
                        },
                        keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                            enter: function (e) {

                                var row = $(this).combogrid('grid').datagrid('getSelected');
                                if (row) {
                                    var ed = $("#dg").datagrid('getEditor', {index: editIndex, field: 'storageCode'});
                                    $(ed.target).textbox('setValue', row.deptCode);
                                    var comboEd = $("#dg").datagrid('getEditor', {index: editIndex, field: 'storageName'});
                                    $(comboEd.target).combogrid('setValue', row.deptName);
                                }

                                $(this).combogrid('hidePanel');
                            }
                        }),
                        filter: function (q, row) {

                            if ($.startWith(row.inputCode, q)) {
                                var dg = $(this).combogrid('grid');

                                var index = dg.datagrid('getRowIndex', row);
                                dg.datagrid('selectRow', index);
                                dg.datagrid('scrollTo', index);
                                return true;
                            }
                            return false;

                        }
                    }
                }
            }, {
                title: '库房级别',
                field: 'storageLevel',
                align: 'center',
                width: "20%",
                editor: {
                    type: 'combobox', options: {
                        editable: false,
                        align: 'center',
                        valueField: 'value',
                        textField: 'text',
                        data: [{
                            value: '1',
                            text: '一级库房',
                            selected: true
                        }, {
                            value: '2',
                            text: '二级库房'
                        }, {
                            value: '3',
                            text: '三级库房'
                        }]
                    }
                },
                formatter: function (value, row, index) {
                    if (value == "1") {
                        return "一级库房";
                    }
                    if (value == "2") {
                        return "二级库房";
                    }
                    if (value == "3") {
                        return "三级库房";
                    }
                }
            }, {
                title: '付款单前缀',
                field: 'disburseNoPrefix',
                align: 'center',
                width: "20%",
                editor: {
                    type: 'text',
                    options: {required: true, validType: 'length[0,6]', missingMessage: '请输入六字符以内的相应的前缀', invalidMessage: '输入值不在范围'}
                }
            }, {
                title: '当前付款单号',
                field: 'disburseNoAva',
                align: 'center',
                width: "20%",
                editor: 'numberbox'
            }, {
                title: "医院编号",
                field: 'hospitalId',
                hidden: true
            }]],
            onClickRow: function (index, row) {
                stopEdit();
                $(this).datagrid('beginEdit', index);
                editIndex = index;
            }
        });
    })

    var loadDict = function () {
        $.get('/api/exp-storage-dept/list?hospitalId=' + parent.config.hospitalId, function (data) {
            $("#dg").datagrid('loadData', data);
        });
    }
    loadDict();

    $("#searchBtn").on("click", function () {
        var name = $("#name").textbox("getValue");

        $.get('/api/exp-storage-dept/list?hospitalId=' + parent.config.hospitalId + '&name=' + name, function (data) {
            $("#dg").datagrid('loadData', data);
        });
    });

    $("#addBtn").on('click', function () {
        $("#dg").datagrid('appendRow', {hospitalId: parent.config.hospitalId});
        var rows = $("#dg").datagrid('getRows');
        var editRow = rows[rows.length - 1];
        var rowIndex = $("#dg").datagrid("getRowIndex", editRow);

        if (editIndex == undefined || editIndex != rowIndex) {
            $("#dg").datagrid("endEdit", editIndex);
            $("#dg").datagrid("beginEdit", rowIndex);
            editIndex = rowIndex;
        }

        $("#dg").datagrid('selectRow', editIndex);
        var comboEdit = $("#dg").datagrid('getEditor', {index: rowIndex, field: 'storageName'});
        $(comboEdit.target).focus();
        $(comboEdit.target).combogrid('showPanel');
    });

    /**
     * 进行保存操作
     */
    $("#saveBtn").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid("endEdit", editIndex);
        }

        var insertData = $("#dg").datagrid("getChanges", "inserted");
        var updateDate = $("#dg").datagrid("getChanges", "updated");
        var deleteDate = $("#dg").datagrid("getChanges", "deleted");

        var beanChangeVo = {};
        beanChangeVo.inserted = insertData;
        beanChangeVo.deleted = deleteDate;
        beanChangeVo.updated = updateDate;

        if (beanChangeVo.inserted.length > 0) {
            for (var i = 0; i < beanChangeVo.inserted.length; i++) {
                var disburseNoPrefix = beanChangeVo.inserted[i].disburseNoPrefix;
                if (disburseNoPrefix.length == 0) {
                    $.messager.alert('提示', '付款单前缀不能为空', 'error');
                    return;
                }
                if (disburseNoPrefix.length > 6) {
                    $.messager.alert('提示', '付款单前缀最多6位!', 'error');
                    return;
                }
            }
        }
        if (beanChangeVo.updated.length > 0) {
            for (var i = 0; i < beanChangeVo.updated.length; i++) {
                var disburseNoPrefix = beanChangeVo.updated[i].disburseNoPrefix;
                if (disburseNoPrefix.length == 0) {
                    $.messager.alert('提示', '付款单前缀不能为空', 'error');
                    return;
                }
                if (disburseNoPrefix.length > 6) {
                    $.messager.alert('提示', '付款单前缀最多6位!', 'error');
                    return;
                }
            }
        }

        if (beanChangeVo) {
            $.postJSON("/api/exp-storage-dept/merge", beanChangeVo, function (data, status) {
                $.messager.alert("系统提示", "保存成功", "info");

                var name = $("#name").textbox("getValue");
                $.get('/api/exp-storage-dept/list?hospitalId=' + parent.config.hospitalId + '&name=' + name, function (data) {
                    $("#dg").datagrid('loadData', data);
                });
            }, function (data) {
                $.messager.alert('提示', data.responseJSON.errorMessage, "error");
            })
        }
    });


    $("#delBtn").on('click', function () {
        var row = $("#dg").datagrid('getSelected');
        if (row) {
            var rowIndex = $("#dg").datagrid('getRowIndex', row);
            $("#dg").datagrid('deleteRow', rowIndex);
            if (editIndex == rowIndex) {
                editIndex = undefined;
            }
        } else {
            $.messager.alert('系统提示', "请选择要删除的行", 'info');
        }
    });


    $("#editBtn").on('click', function () {
        var row = $("#dg").datagrid("getSelected");
        var index = $("#dg").datagrid("getRowIndex", row);

        if (index == -1) {
            $.messager.alert("提示", "请选择要修改的行！", "info");
            return;
        }

        if (editIndex == undefined) {
            $("#dg").datagrid("beginEdit", index);
            editIndex = index;
        } else {
            $("#dg").datagrid("endEdit", editIndex);
            $("#dg").datagrid("beginEdit", index);
            editIndex = index;
        }
    })
})