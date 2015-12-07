/**
 * Created by heren on 2015/9/16.
 */
/***
 * 消耗品产品编码描述字典维护
 */
$(function () {
    var editRowIndex;
    $("#dg").datagrid({
        title: '产品编码描述字典维护',
        fit: true,//让#dg数据创铺满父类容器
        footer: '#tb',
        singleSelect: true,
        columns: [[{
            title: '编号',
            field: 'id',
            hidden: 'true'
        }, {
            title: '编码级别',
            field: 'codeLevel',
            width: "30%",
            editor: {type:'numberbox',options:{required: true,min:0,
                max:9,validType: 'length[0,1]', missingMessage:'请输入0-9的数字'}
            }
        }, {
            title: '编码级长度',
            field: 'levelWidth',
            width: "30%",
            editor: {type:'numberbox', options: {required: true,validType: 'length[0,2]',min:0, max:19, missingMessage:'请输入0-99的数字'}
            }
        }, {
            title: '编码级名称',
            field: 'className',
            width: "40%",
            editor: {
                type: 'validatebox', options: {
                    required: true, validType: 'length[0,8]', missingMessage:'请输入四个以内的汉字'}
            }
        }]]
    });

    $("#searchBtn").on("click", function () {
        var name = $("#name").textbox("getValue");

        $.get("/api/exp-coding-rule/list?name=" + name, function (data) {
            $("#dg").datagrid('loadData', data);
        });
    });

    $("#addBtn").on('click', function () {

        $("#dg").datagrid('appendRow', {});
        var rows = $("#dg").datagrid('getRows');
        var row = rows[rows.length - 1];
        var index = $("#dg").datagrid('getRowIndex', row);

        $("#dg").datagrid('selectRow', index);
        if (editRowIndex == index) {
            $("#dg").datagrid('beginEdit', editRowIndex);
        }
        if (editRowIndex == undefined) {
            $("#dg").datagrid('beginEdit', index);
            editRowIndex = index;
        } else {
            $("#dg").datagrid('endEdit', editRowIndex);
            $("#dg").datagrid('beginEdit', index);
            editRowIndex = index;
        }
    });

    $("#delBtn").on('click', function () {
        var row = $("#dg").datagrid('getSelected');
        if (!row) {
            $.messager.alert("系统提醒", "请选择要删除的行", "error");
            return;
        }

        var index = $("#dg").datagrid('getRowIndex', row);

        if (index == editRowIndex) {
            editRowIndex = undefined;
        }
        $("#dg").datagrid('deleteRow', index);

    });

    $("#editBtn").on('click', function () {
        var row = $("#dg").datagrid('getSelected');
        if (!row) {
            $.messager.alert("系统提醒", "请选择要编辑的行", "error");
            return;
        }

        var index = $("#dg").datagrid('getRowIndex', row);

        if (editRowIndex == undefined) {

            $("#dg").datagrid("beginEdit", index);
            editRowIndex = index;
        } else {
            $("#dg").datagrid('endEdit', editRowIndex);
            $("#dg").datagrid('beginEdit', index);
            editRowIndex = index;
        }
    });

    var loadDict = function () {
        var name = $("#name").textbox("getValue");

        $.get("/api/exp-coding-rule/list?name=" + name, function (data) {
            $("#dg").datagrid('loadData', data);
        });
    }

    loadDict();


    /**
     * 保存修改的内容
     * 基础字典的改变，势必会影响其他的统计查询
     * 基础字典的维护只能在基础数据维护的时候使用。
     */
    $("#saveBtn").on('click', function () {
        if (editRowIndex) {
            $("#dg").datagrid('endEdit', editRowIndex);
            editRowIndex = undefined;
        }
        var insertData = $("#dg").datagrid('getChanges', 'inserted');
        var updateData = $("#dg").datagrid('getChanges', 'updated');
        var deleteData = $("#dg").datagrid('getChanges', 'deleted');


        if (insertData && insertData.length > 0) {
            $.postJSON("/api/exp-coding-rule/add", insertData, function (data) {
                $.messager.alert('系统提示', '保存成功', "info");
                loadDict();
            }, function (data) {
                console.log(data) ;
                $.messager.alert("系统提示",data.responseJSON.errorMessage,"error");
                loadDict();
            });
        }

        if (updateData && updateData.length > 0) {
            $.postJSON("/api/exp-coding-rule/add", updateData, function (data) {
                $.messager.alert('系统提示', '修改成功', "info");
                loadDict();
            }, function (data) {
                console.log(data) ;
                $.messager.alert("系统提示",data.responseJSON.errorMessage,"error");
                loadDict();
            });
        }


        if (deleteData && deleteData.length > 0) {
            $.postJSON("/api/exp-coding-rule/delete", deleteData, function (data) {
                $.messager.alert('系统提示', '删除成功', "info");
                loadDict();
            }, function (data) {
                console.log(data) ;
                $.messager.alert("系统提示",data.responseJSON.errorMessage,"error");
                loadDict();
            });
        }
    });
})