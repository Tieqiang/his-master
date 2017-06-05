/**
 * Created by heren on 2015/11/25.
 */

$(function(){
    var editRow = undefined ;

    $("#costAttrDatagrid").datagrid({
        fit:true,
        fitColumns:true,
        striped:true,
        method:'GET',
        url:"/api/cost-attr/list-all",
        rownumbers:true,
        footer:"#ft",
        loadMsg:'数据正在加载中.....',
        columns:[[{
            title:'id',
            field:'id',
            checkbox:true
        },{
            title:'成本属性名称',
            field:'attrName',
            width:'30%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输入成本属性名称'
            }}
        },{
            title:'成本属性代码',
            field:'attrCode',
            width:'40%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输入成本属性代码'
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
            $("#costAttrDatagrid").datagrid('appendRow',{}) ;
            var rows = $("#costAttrDatagrid").datagrid('getRows') ;
            var index = $("#costAttrDatagrid").datagrid('getRowIndex',rows[rows.length -1]) ;
            editRow = index ;
            beginEdit() ;
        }
    }) ;

    //删除
    $("#delBtn").on('click',function(){
        var rows = $("#costAttrDatagrid").datagrid('getSelections') ;
        if(!rows){
            $.messager.alert('系统提示','请选择要删除的属性','error') ;
            return ;
        }

        $.messager.confirm('系统提示','确定要进行删除操作吗',function(r){
            if(r){
                for(var i = 0 ;i<rows.length ;i++){
                    var index = $("#costAttrDatagrid").datagrid('getRowIndex',rows[i]) ;
                    $("#costAttrDatagrid").datagrid('deleteRow',index) ;
                    if(index==editRow){
                        editRow = undefined ;
                    }
                }
            }
        });


    }) ;

    //保存
    $("#saveBtn").on('click',function(){
        if(stopEdit()){
            var inserted = $("#costAttrDatagrid").datagrid('getChanges','inserted') ;
            var updated = $("#costAttrDatagrid").datagrid('getChanges','updated') ;
            var deleted = $("#costAttrDatagrid").datagrid('getChanges','deleted') ;

            var beanChangeVo = {} ;
            beanChangeVo.inserted=inserted;
            beanChangeVo.deleted=deleted;
            beanChangeVo.updated= updated ;

            console.log(beanChangeVo) ;
            $.postJSON("/api/cost-attr/save-update",beanChangeVo,function(data){
                $.messager.alert('系统提示',"更新成功") ;
                $("#costAttrDatagrid").datagrid('acceptChanges') ;
                $("#costAttrDatagrid").datagrid('reload') ;
            },function(data){
                $.messager.alert('系统提示','更新失败') ;
            })
        }
    }) ;
    //刷新
    $("#searchBtn").on('click',function(){
        $("#costAttrDatagrid").datagrid('reload') ;
    })


    //停止编辑行
    var stopEdit = function(){
        if(editRow || editRow==0){
            var ed = $("#costAttrDatagrid").datagrid('getEditor', {index: editRow, field: "attrName"});
            if(ed.target){
                var flag = $(ed.target).validatebox('isValid');
                if (flag) {

                } else {
                    $.messager.alert("系统提示", "成本属性名称必须填写", 'error');
                    return flag;
                }
            }
            var ed1 = $("#costAttrDatagrid").datagrid('getEditor', {index: editRow, field: "attrCode"});
            if(ed1.target){
                var flag = $(ed1.target).validatebox('isValid');
                if (flag) {
                } else {
                    $.messager.alert("系统提示", "成本属性名称必须填写", 'error');
                    return flag;
                }
            }
            $("#costAttrDatagrid").datagrid('endEdit', editRow);
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
            $("#costAttrDatagrid").datagrid('beginEdit',editRow) ;
        }
    }
}) ;