/**
 * Created by heren on 2015/9/17.
 */
/***
 * 消耗品产品属性字典维护
 */
$(function(){
    var editIndex ;
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
    $("#dg").datagrid({
        title:'消耗品产品属性字典维护',
        fit:true,
        footer:'#tb',
        singleSelect:true,
        rownumbers:true,
        columns:[[{
            title: "id",
            field: "id",
            hidden: true
        },{
            title:"属性代码",
            field:"toxiCode",
            align: 'center',
            width:"20%",
            editor:{type:"text",options:{
                required:true,validType:'length[0,2]',missingMessage:'请输入两个字符',invalidMessage:'输入值不在范围'}
            }
        },{
            title:"属性名称",
            field:"toxiProperty",
            align: 'center',
            width:"20%",
            editor:{type:'text',options:{required:true,validType:'length[0,10]',missingMessage:'请输入五个以内的汉字',invalidMessage:'输入值不在范围'}}
        },{
            title:"拼音码",
            field:"inputCode",
            align: 'center',
            width:"20%"
            //editor:{type:'text',options:{required:true,validType:'length[0,8]',missingMessage:'请输入‘属性名称’对应大写首字母',invalidMessage:'输入值不在范围'}}
        }]],
        onClickRow: function (index, row) {
            stopEdit();
            $(this).datagrid('beginEdit', index);
            editIndex = index;
        }
    })  ;

    $("#searchBtn").on("click", function () {
        loadDict();
    });

    $("#addBtn").on("click", function () {
        stopEdit();
        $("#dg").datagrid('appendRow', {});
        var rows = $("#dg").datagrid('getRows');
        var addRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);
        editIndex = addRowIndex;
        $("#dg").datagrid('selectRow', editIndex);
        $("#dg").datagrid('beginEdit', editIndex);
    });

    $("#editBtn").on("click", function () {
        var row = $("#dg").datagrid("getSelected");
        var index = $("#dg").datagrid("getRowIndex",row);

        if ( index == -1 ) {
            $.messager.alert("提示","请选择要修改的行！","info");
            return ;
        }

        if (editIndex == undefined){
            $("#dg").datagrid("beginEdit",index);
            editIndex = index;
        }else {
            $("#dg").datagrid("endEdit", editIndex);
            $("#dg").datagrid("beginEdit",index);
            editIndex = index;
        }
    });

    $("#delBtn").on("click", function () {
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

    $("#saveBtn").on("click", function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid("endEdit", editIndex);
        }

        var insertData = $("#dg").datagrid("getChanges","inserted");
        var updateDate = $("#dg").datagrid("getChanges","updated");
        var deleteDate = $("#dg").datagrid("getChanges","deleted");

        var beanChangeVo = {};
        beanChangeVo.inserted = insertData;
        beanChangeVo.deleted = deleteDate;
        beanChangeVo.updated = updateDate;

        if (beanChangeVo.inserted.length > 0) {
            for (var i = 0; i < beanChangeVo.inserted.length; i++) {
                var toxiCode = beanChangeVo.inserted[i].toxiCode;   //属性代码
                var toxiProperty = beanChangeVo.inserted[i].toxiProperty;   //属性名称
                if (toxiCode.length == 0) {
                    $.messager.alert('提示', '属性代码不能为空!!', 'error');
                    loadDict();
                    return;
                }else if(toxiCode.length > 2){
                    $.messager.alert('提示', '属性代码最多两个字符!!', 'error');
                    loadDict();
                    return;
                }
                if (toxiProperty.length == 0) {
                    $.messager.alert('提示', '属性名称不能为空!!', 'error');
                    loadDict();
                    return;
                }else if(toxiProperty.length > 10){
                    $.messager.alert('提示', '属性名称最多5个汉字!!', 'error');
                    loadDict();
                    return;
                }
            }
        }
        if (beanChangeVo.updated.length > 0) {
            for (var i = 0; i < beanChangeVo.updated.length; i++) {
                var toxiCode = beanChangeVo.updated[i].toxiCode;   //属性代码
                var toxiProperty = beanChangeVo.updated[i].toxiProperty;   //属性名称
                if (toxiCode.length == 0) {
                    $.messager.alert('提示', '属性代码不能为空!!', 'error');
                    loadDict();
                    return;
                } else if (toxiCode.length > 2) {
                    $.messager.alert('提示', '属性代码最多两个字符!!', 'error');
                    loadDict();
                    return;
                }
                if (toxiProperty.length == 0) {
                    $.messager.alert('提示', '属性名称不能为空!!', 'error');
                    loadDict();
                    return;
                } else if (toxiProperty.length > 10) {
                    $.messager.alert('提示', '属性名称最多5个汉字!!', 'error');
                    loadDict();
                    return;
                }
            }
        }


        if(beanChangeVo){
            $.postJSON("/api/exp-property-dict/merge", beanChangeVo, function (data, status) {
                $.messager.alert("系统提示", "保存成功", "info");
                loadDict();
            }, function (data) {
                $.messager.alert('提示', data.responseJSON.errorMessage, "error");
            })
        }
    });

    var loadDict = function(){
        //var name = $("#name").textbox("getValue");

        $.get("/api/exp-property-dict/list" , function (data) {
            $("#dg").datagrid('loadData', data);
        });
    }
    loadDict() ;

});