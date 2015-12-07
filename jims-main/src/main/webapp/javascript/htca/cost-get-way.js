/**
 * Created by heren on 2015/11/26.
 */

$(function(){
    var editRow = undefined ;

    $("#costGetWatDatagrid").datagrid({
        fit:true,
        fitColumns:true,
        striped:true,
        method:'GET',
        url:"/api/cost-get-way/list-all",
        rownumbers:true,
        footer:"#ft",
        loadMsg:'数据正在加载中.....',
        columns:[[{
            title:'id',
            field:'id',
            checkbox:true
        },{
            title:'获取方式名称',
            field:'getWayName',
            width:'30%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输获取方式名称'
            }}
        },{
            title:'获取方式代码',
            field:'getWayCode',
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
            $("#costGetWatDatagrid").datagrid('appendRow',{}) ;
            var rows = $("#costGetWatDatagrid").datagrid('getRows') ;
            var index = $("#costGetWatDatagrid").datagrid('getRowIndex',rows[rows.length -1]) ;
            editRow = index ;
            beginEdit() ;
        }
    }) ;

    //删除
    $("#delBtn").on('click',function(){
        var rows = $("#costGetWatDatagrid").datagrid('getSelections') ;
        if(!rows){
            $.messager.alert('系统提示','请选择要删除的属性','error') ;
            return ;
        }

        for(var i = 0 ;i<rows.length ;i++){
            var index = $("#costGetWatDatagrid").datagrid('getRowIndex',rows[i]) ;
            $("#costGetWatDatagrid").datagrid('deleteRow',index) ;
            if(index==editRow){
                editRow = undefined ;
            }
        }

    }) ;

    //保存
    $("#saveBtn").on('click',function(){
        if(stopEdit()){
            var inserted = $("#costGetWatDatagrid").datagrid('getChanges','inserted') ;
            var updated = $("#costGetWatDatagrid").datagrid('getChanges','updated') ;
            var deleted = $("#costGetWatDatagrid").datagrid('getChanges','deleted') ;

            var beanChangeVo = {} ;
            beanChangeVo.inserted=inserted;
            beanChangeVo.deleted=deleted;
            beanChangeVo.updated= updated ;

            console.log(beanChangeVo) ;
            $.postJSON("/api/cost-get-way/save-update",beanChangeVo,function(data){
                $.messager.alert('系统提示',"更新成功") ;
                $("#costGetWatDatagrid").datagrid('acceptChanges') ;
                $("#costGetWatDatagrid").datagrid('reload') ;
            },function(data){
                $.messager.alert('系统提示','更新失败') ;
            })
        }
    }) ;
    //刷新
    $("#searchBtn").on('click',function(){
        $("#costGetWatDatagrid").datagrid('reload') ;
    })


    //停止编辑行
    var stopEdit = function(){
        if(editRow || editRow==0){
            var ed = $("#costGetWatDatagrid").datagrid('getEditor', {index: editRow, field: "getWayName"});
            if(ed.target){
                var flag = $(ed.target).validatebox('isValid');
                if (flag) {

                } else {
                    $.messager.alert("系统提示", "成本属性名称必须填写", 'error');
                    return flag;
                }
            }
            var ed1 = $("#costGetWatDatagrid").datagrid('getEditor', {index: editRow, field: "getWayCode"});
            if(ed1.target){
                var flag = $(ed1.target).validatebox('isValid');
                if (flag) {
                } else {
                    $.messager.alert("系统提示", "成本属性名称必须填写", 'error');
                    return flag;
                }
            }
            $("#costGetWatDatagrid").datagrid('endEdit', editRow);
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
            $("#costGetWatDatagrid").datagrid('beginEdit',editRow) ;
        }
    }
}) ;