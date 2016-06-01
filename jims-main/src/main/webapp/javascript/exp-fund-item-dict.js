
/***
 * 消耗品产品经费支出字典
 */
$(function(){
    var editIndex;
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
    $("#dg").datagrid({
        title:'产品经费支出字典',
        fit:true,//让#dg数据创铺满父类容器
        footer:'#tb',
        singleSelect:true,
        columns:[[{
            title:'编号',
            field:'id',
            hidden:'true'
        },{
            title:'序号',
            field:'serialNo',
            width:"20%",
            editor: 'numberbox'
        },{
            title:'支出经费名称',
            field:'fundItem',
            width:"30%",
            editor:{type:'text',options:{
                required:true,validType:'length[0,10]',missingMessage:'请输入五个以内的汉字'}
            }}]],
        onClickRow: function (index, row) {
            stopEdit();
            $(this).datagrid('beginEdit', index);
            editIndex = index;
        }
    })  ;

    $("#searchBtn").on("click", function () {
        loadDict();
    });

    $("#addBtn").on('click',function(){
        stopEdit();
        $("#dg").datagrid('appendRow', {});
        var rows = $("#dg").datagrid('getRows');
        var addRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);
        editIndex = addRowIndex;
        $("#dg").datagrid('selectRow', editIndex);
        $("#dg").datagrid('beginEdit', editIndex);
    }) ;

    $("#delBtn").on('click',function(){
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
    }) ;

    $("#editBtn").on('click',function(){
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
    }) ;

    var loadDict = function(){
        $.get("/api/exp-fund-item-dict/list", function (data) {
            $("#dg").datagrid('loadData', data);
        });
    }
    loadDict() ;

    /**
     * 保存修改的内容
     * 基础字典的改变，势必会影响其他的统计查询
     * 基础字典的维护只能在基础数据维护的时候使用。
     */
    $("#saveBtn").on('click',function(){
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
                var fundItem = beanChangeVo.inserted[i].fundItem;
                var serialNo = beanChangeVo.inserted[i].serialNo;
                if(serialNo.length > 2){
                    $.messager.alert('提示','序号最多2位数字!','error');
                    return ;
                }
                if (fundItem.length == 0) {
                    $.messager.alert('提示', '名称不能为空!!', 'error');
                    return;
                } else if (fundItem.length > 10) {
                    $.messager.alert('提示', '添加失败:名称超过长度,请输入五个以内的汉字!', 'error');
                    return;
                }
            }
        }
        if (beanChangeVo.updated.length > 0) {
            for (var i = 0; i < beanChangeVo.updated.length; i++) {
                var fundItem = beanChangeVo.updated[i].fundItem;
                var serialNo = beanChangeVo.updated[i].serialNo;
                if (serialNo.length > 2) {
                    $.messager.alert('提示', '序号最多2位数字!', 'error');
                    loadDict();
                    return;
                }
                if (fundItem.length == 0) {
                    $.messager.alert('提示', '名称不能为空!!', 'error');
                    return;
                } else if (fundItem.length > 10) {
                    $.messager.alert('提示', '修改失败:名称超过长度,请输入五个以内的汉字!', 'error');
                    return;
                }
            }
        }

        if (beanChangeVo) {
            $.postJSON("/api/exp-fund-item-dict/merge", beanChangeVo, function (data, status) {
                $.messager.alert("系统提示", "保存成功", "info");
                loadDict();
            }, function (data) {
                $.messager.alert('提示', data.responseJSON.errorMessage, "error");
            })
        }
    }) ;
})