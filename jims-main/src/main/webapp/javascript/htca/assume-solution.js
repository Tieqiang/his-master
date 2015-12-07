/**
 * Created by heren on 2015/11/26.
 */

$(function(){
    var editRow = undefined ;

    $("#assumeSolutionDatagrid").datagrid({
        fit:true,
        fitColumns:true,
        striped:true,
        method:'GET',
        url:"/api/assume-solution/list-all",
        rownumbers:true,
        footer:"#ft",
        loadMsg:'数据正在加载中.....',
        columns:[[{
            title:'id',
            field:'id',
            checkbox:true
        },{
            title:'成本方案名称',
            field:'assumeSolutionName',
            width:'30%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输获取方式名称'
            }}
        },{
            title:'成本方案代码',
            field:'assumeSolutionCode',
            width:'40%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输入获取方式代码'
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
            $("#assumeSolutionDatagrid").datagrid('appendRow',{}) ;
            var rows = $("#assumeSolutionDatagrid").datagrid('getRows') ;
            var index = $("#assumeSolutionDatagrid").datagrid('getRowIndex',rows[rows.length -1]) ;
            editRow = index ;
            beginEdit() ;
        }
    }) ;

    //删除
    $("#delBtn").on('click',function(){
        var rows = $("#assumeSolutionDatagrid").datagrid('getSelections') ;
        if(!rows){
            $.messager.alert('系统提示','请选择要删除的属性','error') ;
            return ;
        }

        for(var i = 0 ;i<rows.length ;i++){
            var index = $("#assumeSolutionDatagrid").datagrid('getRowIndex',rows[i]) ;
            $("#assumeSolutionDatagrid").datagrid('deleteRow',index) ;
            if(index==editRow){
                editRow = undefined ;
            }
        }

    }) ;

    //保存
    $("#saveBtn").on('click',function(){
        if(stopEdit()){
            var inserted = $("#assumeSolutionDatagrid").datagrid('getChanges','inserted') ;
            var updated = $("#assumeSolutionDatagrid").datagrid('getChanges','updated') ;
            var deleted = $("#assumeSolutionDatagrid").datagrid('getChanges','deleted') ;

            var beanChangeVo = {} ;
            beanChangeVo.inserted=inserted;
            beanChangeVo.deleted=deleted;
            beanChangeVo.updated= updated ;

            console.log(beanChangeVo) ;
            $.postJSON("/api/assume-solution/save-update",beanChangeVo,function(data){
                $.messager.alert('系统提示',"更新成功") ;
                $("#assumeSolutionDatagrid").datagrid('acceptChanges') ;
                $("#assumeSolutionDatagrid").datagrid('reload') ;
            },function(data){
                $.messager.alert('系统提示','更新失败') ;
            })
        }
    }) ;

    //刷新
    $("#searchBtn").on('click',function(){
        $("#assumeSolutionDatagrid").datagrid('reload') ;
    })


    //停止编辑行
    var stopEdit = function(){
        if(editRow || editRow==0){
            var ed = $("#assumeSolutionDatagrid").datagrid('getEditor', {index: editRow, field: "assumeSolutionName"});
            if(ed.target){
                var flag = $(ed.target).validatebox('isValid');
                if (flag) {

                } else {
                    $.messager.alert("系统提示", "成本属性名称必须填写", 'error');
                    return flag;
                }
            }
            var ed1 = $("#assumeSolutionDatagrid").datagrid('getEditor', {index: editRow, field: "assumeSolutionCode"});
            if(ed1.target){
                var flag = $(ed1.target).validatebox('isValid');
                if (flag) {
                } else {
                    $.messager.alert("系统提示", "成本属性名称必须填写", 'error');
                    return flag;
                }
            }
            $("#assumeSolutionDatagrid").datagrid('endEdit', editRow);
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
            $("#assumeSolutionDatagrid").datagrid('beginEdit',editRow) ;
        }
    }
}) ;