/**
 * Created by heren on 2015/11/26.
 */

$(function(){
    var editRow = undefined ;

    $("#costRateDatagrid").datagrid({
        fit:true,
        fitColumns:true,
        striped:true,
        method:'GET',
        url:"/api/cost-rate/list-all",
        rownumbers:true,
        footer:"#ft",
        loadMsg:'数据正在加载中.....',
        columns:[[{
            title:'id',
            field:'id',
            checkbox:true
        },{
            title:'比例名称',
            field:'rateName',
            width:'30%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输比例名称'
            }}
        },{
            title:'比例代码',
            field:'rateCode',
            width:'40%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输入比例代码'
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
            $("#costRateDatagrid").datagrid('appendRow',{}) ;
            var rows = $("#costRateDatagrid").datagrid('getRows') ;
            var index = $("#costRateDatagrid").datagrid('getRowIndex',rows[rows.length -1]) ;
            editRow = index ;
            beginEdit() ;
        }
    }) ;

    //删除
    $("#delBtn").on('click',function(){
        var rows = $("#costRateDatagrid").datagrid('getSelections') ;
        if(!rows){
            $.messager.alert('系统提示','请选择要删除的属性','error') ;
            return ;
        }

        for(var i = 0 ;i<rows.length ;i++){
            var index = $("#costRateDatagrid").datagrid('getRowIndex',rows[i]) ;
            $("#costRateDatagrid").datagrid('deleteRow',index) ;
            if(index==editRow){
                editRow = undefined ;
            }
        }

    }) ;

    //保存
    $("#saveBtn").on('click',function(){
        if(stopEdit()){
            var inserted = $("#costRateDatagrid").datagrid('getChanges','inserted') ;
            var updated = $("#costRateDatagrid").datagrid('getChanges','updated') ;
            var deleted = $("#costRateDatagrid").datagrid('getChanges','deleted') ;

            var beanChangeVo = {} ;
            beanChangeVo.inserted=inserted;
            beanChangeVo.deleted=deleted;
            beanChangeVo.updated= updated ;

            console.log(beanChangeVo) ;
            $.postJSON("/api/cost-rate/save-update",beanChangeVo,function(data){
                $.messager.alert('系统提示',"更新成功") ;
                $("#costRateDatagrid").datagrid('acceptChanges') ;
                $("#costRateDatagrid").datagrid('reload') ;
            },function(data){
                $.messager.alert('系统提示','更新失败') ;
            })
        }
    }) ;
    //刷新
    $("#searchBtn").on('click',function(){
        $("#costRateDatagrid").datagrid('reload') ;
    })


    //停止编辑行
    var stopEdit = function(){
        if(editRow || editRow==0){
            var ed = $("#costRateDatagrid").datagrid('getEditor', {index: editRow, field: "rateName"});
            if(ed.target){
                var flag = $(ed.target).validatebox('isValid');
                if (flag) {

                } else {
                    $.messager.alert("系统提示", "成本属性名称必须填写", 'error');
                    return flag;
                }
            }
            var ed1 = $("#costRateDatagrid").datagrid('getEditor', {index: editRow, field: "rateCode"});
            if(ed1.target){
                var flag = $(ed1.target).validatebox('isValid');
                if (flag) {
                } else {
                    $.messager.alert("系统提示", "成本属性名称必须填写", 'error');
                    return flag;
                }
            }
            $("#costRateDatagrid").datagrid('endEdit', editRow);
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
            $("#costRateDatagrid").datagrid('beginEdit',editRow) ;
        }
    }
}) ;