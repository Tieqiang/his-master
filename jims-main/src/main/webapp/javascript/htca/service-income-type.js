/**
 * Created by heren on 2015/11/26.
 */

$(function(){
    var editRow = undefined ;

    $("#serviceIncomeTypeDatagrid").datagrid({
        fit:true,
        fitColumns:true,
        striped:true,
        method:'GET',
        url:"/api/service-income-type/list-all?hospitalId="+parent.config.hospitalId,
        rownumbers:true,
        footer:"#ft",
        loadMsg:'数据正在加载中.....',
        columns:[[{
            title:'id',
            field:'id',
            checkbox:true
        },{
            title:'服务类别名称',
            field:'serviceTypeName',
            width:'30%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输入服务类别名称'
            }}
        },{
            title:'拼音码',
            field:'inputCode',
            width:'40%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输入拼音码'
            }}
        }]],
        onDblClickRow:function(rowIndex,rowData){
            if(stopEdit()){
                editRow = rowIndex ;
                beginEdit() ;
            }
        }
    }) ;

    //添加
    $("#addBtn").on('click',function(){
        if(stopEdit()){
            $("#serviceIncomeTypeDatagrid").datagrid('appendRow',{}) ;
            var rows = $("#serviceIncomeTypeDatagrid").datagrid('getRows') ;
            var index = $("#serviceIncomeTypeDatagrid").datagrid('getRowIndex',rows[rows.length -1]) ;
            editRow = index ;
            beginEdit() ;
        }
    }) ;

    //删除
    $("#delBtn").on('click',function(){
        var rows = $("#serviceIncomeTypeDatagrid").datagrid('getSelections') ;
        if(!rows){
            $.messager.alert('系统提示','请选择要删除的属性','error') ;
            return ;
        }

        for(var i = 0 ;i<rows.length ;i++){
            var index = $("#serviceIncomeTypeDatagrid").datagrid('getRowIndex',rows[i]) ;
            $("#serviceIncomeTypeDatagrid").datagrid('deleteRow',index) ;
            if(index==editRow){
                editRow = undefined ;
            }
        }

    }) ;

    //保存
    $("#saveBtn").on('click',function(){
        if(stopEdit()){
            var inserted = $("#serviceIncomeTypeDatagrid").datagrid('getChanges','inserted') ;
            var updated = $("#serviceIncomeTypeDatagrid").datagrid('getChanges','updated') ;
            var deleted = $("#serviceIncomeTypeDatagrid").datagrid('getChanges','deleted') ;
            for(var i = 0 ;i<inserted.length ;i++){
                inserted[i].hospitalId = parent.config.hospitalId ;
            }
            for(var i = 0 ;i<deleted.length ;i++){
                deleted[i].hospitalId = parent.config.hospitalId ;
            }
            for(var i = 0 ;i<updated.length ;i++){
                updated[i].hospitalId = parent.config.hospitalId ;
            }

            var beanChangeVo = {} ;
            beanChangeVo.inserted=inserted;
            beanChangeVo.deleted=deleted;
            beanChangeVo.updated= updated ;
            $.postJSON("/api/service-income-type/save-update",beanChangeVo,function(data){
                $.messager.alert('系统提示',"更新成功") ;
                $("#serviceIncomeTypeDatagrid").datagrid('acceptChanges') ;
                $("#serviceIncomeTypeDatagrid").datagrid('reload') ;
            },function(data){
                $.messager.alert('系统提示','更新失败') ;
            })
        }
    }) ;
    //刷新
    $("#searchBtn").on('click',function(){
        $("#serviceIncomeTypeDatagrid").datagrid('reload') ;
    })


    //停止编辑行
    var stopEdit = function(){
        if(editRow || editRow==0){
            var ed = $("#serviceIncomeTypeDatagrid").datagrid('getEditor', {index: editRow, field: "serviceTypeName"});
            if(ed.target){
                var flag = $(ed.target).validatebox('isValid');
                if (flag) {

                } else {
                    $.messager.alert("系统提示", "服务类型名称必须填写", 'error');
                    return flag;
                }
            }
            var ed1 = $("#serviceIncomeTypeDatagrid").datagrid('getEditor', {index: editRow, field: "inputCode"});
            if(ed1.target){
                var flag = $(ed1.target).validatebox('isValid');
                if (flag) {
                } else {
                    $.messager.alert("系统提示", "服务类型名称必须填写", 'error');
                    return flag;
                }
            }
            $("#serviceIncomeTypeDatagrid").datagrid('endEdit', editRow);
            editRow = undefined ;
            return true ;
        }else{
            editRow=undefined ;
            return true ;
        }
    }

    //开始编辑行
    var beginEdit = function(){
        if(editRow||editRow==0){
            $("#serviceIncomeTypeDatagrid").datagrid('beginEdit',editRow) ;
        }
    }
}) ;