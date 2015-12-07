/**
 * Created by heren on 2015/9/17.
 */
/***
 * 消耗品产品属性字典维护
 */
$(function(){
    var editRowIndex ;
    $("#dg").datagrid({
        title:'消耗品产品属性字典维护',
        fit:true,
        footer:'#tb',
        singleSelect:true,
        rownumbers:true,
        columns:[[{
            title:"属性代码",
            field:"toxiCode",
            width:"20%",
            editor:{type:"validatebox",options:{
                required:true,validType:'length[0,2]',missingMessage:'请输入两个字符',invalidMessage:'输入值不在范围'}
            }

        },{
            title:"属性名称",
            field:"toxiProperty",
            width:"20%",
            editor:{type:'validatebox',options:{required:true,validType:'length[0,10]',missingMessage:'请输入五个以内的汉字',invalidMessage:'输入值不在范围'}}
        },{
            title:"拼音码",
            field:"inputCode",
            width:"20%",
            editor:{type:'validatebox',options:{required:true,validType:'length[0,8]',missingMessage:'请输入‘属性名称’对应大写首字母',invalidMessage:'输入值不在范围'}}
        }]]
    })  ;

    $("#searchBtn").on("click", function () {
        var name = $("#name").textbox("getValue");

        $.get("/api/exp-property-dict/list?name=" + name, function (data) {
            $("#dg").datagrid('loadData', data);
        });
    });

    $("#addBtn").on("click", function () {
        $("#dg").datagrid("appendRow",{});
        var rows = $("#dg").datagrid("getRows");
        var row = rows[rows.length-1];
        var index = $("#dg").datagrid("getRowIndex",row);
        $("#dg").datagrid("selectRow",index);
        if (editRowIndex == undefined){
            $("#dg").datagrid("beginEdit",index);
            editRowIndex = index;
        }
        if (editRowIndex == index-1){
            $("#dg").datagrid("endEdit",editRowIndex);
            editRowIndex = index;
            $("#dg").datagrid("beginEdit",index);
        }
    });

    $("#editBtn").on("click", function () {
        var row = $("#dg").datagrid("getSelected");
        var index = $("#dg").datagrid("getRowIndex",row);
        //console.log(row);
        //console.log(index);
        if ( index == -1 ) {
            $.messager.alert("提示","请选择要修改的行！","info");
            return ;
        }

        if (editRowIndex == undefined){
            $("#dg").datagrid("beginEdit",index);
            editRowIndex = index;
        }else {
            $("#dg").datagrid("endEdit",editRowIndex);
            $("#dg").datagrid("beginEdit",index);
            editRowIndex = index;
        }
    });

    $("#delBtn").on("click", function () {
        var row  = $("#dg").datagrid("getSelected");
        var index = $("#dg").datagrid("getRowIndex",row);
        if (index == -1){
            $.messager.alert("提示","请选择删除的行","info");
        }else {
            $("#dg").datagrid("deleteRow",index);
            editRowIndex = undefined;
        }
    });

    $("#saveBtn").on("click", function () {
        if (editRowIndex != undefined){
            $("#dg").datagrid("endEdit",editRowIndex);
            editRowIndex = undefined;
        }

        var insertData = $("#dg").datagrid("getChanges","inserted");
        var updateDate = $("#dg").datagrid("getChanges","updated");
        var deleteDate = $("#dg").datagrid("getChanges","deleted");

        //alert("insertData"+insertData+"updateDate"+updateDate+"deleteDate"+deleteDate);
        //alert("insertData"+insertData.length+"updateDate"+updateDate.length+"deleteDate"+deleteDate.length);
        if (insertData && insertData.length > 0){
            $.postJSON("/api/exp-property-dict/add",insertData, function (data) {
                $.messager.alert('提示',"保存成功","info");
                loadDict();
            },function(data){
                $.messager.alert('提示',data.responseJSON.errorMessage,"error");
                loadDict();
            })
        }
        if (updateDate && updateDate.length > 0){
            $.postJSON("/api/exp-property-dict/update",insertData, function (data) {
                $.messager.alert('提示',"修改成功","info");
                loadDict();
            },function(data){
                $.messager.alert('提示',data.responseJSON.errorMessage,"error");
                loadDict();
            })
        }
        if (deleteDate && deleteDate.length > 0){
            $.postJSON("/api/exp-property-dict/delete",insertData, function (data) {
                $.messager.alert('提示',"删除成功","info");
                loadDict();
            },function(data){
                $.messager.alert('提示',data.responseJSON.errorMessage,"error");
                loadDict();
            })
        }
    });

    var loadDict = function(){
        $.get("/api/exp-property-dict/list",function(data){
            $("#dg").datagrid('loadData',data) ;
        }) ;
    }
    loadDict() ;

});