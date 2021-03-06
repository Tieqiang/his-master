/**
 * Created by heren on 2015/10/28.
 */
/**
 * 收益调整项目字典
 */
$(function(){

    var editRow = undefined ;//当前处于编辑状态的行

    $("#incomeTypeGrid").datagrid({
        fit:true,
        fitColumns:true,
        singleSelect:true,
        url:'/api/acct-profit-change-dict/list?hospitalId='+parent.config.hospitalId,
        footer:'#ft',
        method:'GET',
        columns:[[{
            title:'id',
            field:'id',
            hidden:true
        },{
            title:'调整项名称',
            field:'changeItemName',
            editor:{type:'textbox',options:{}},
            width:'30%'
        },{
            title:'收益分类输入码',
            field:'inputCode',
            editor:{type:'textbox',options:{}},
            width:'30%'
        }]],
        onClickRow:function(index,row){
            if(editRow==0||editRow){
                $(this).datagrid('endEdit',editRow) ;
            }
            editRow = index ;

            $(this).datagrid('beginEdit',editRow) ;

        }
    }) ;


    $("#addBtn").on('click',function(){
        if(editRow||editRow==0){
            $("#incomeTypeGrid").datagrid('endEdit',editRow) ;
        }

        $("#incomeTypeGrid").datagrid("appendRow",{}) ;

        var rows = $("#incomeTypeGrid").datagrid('getRows') ;
        if(rows.length>0){
            var rowIndex = $("#incomeTypeGrid").datagrid('getRowIndex',rows[rows.length - 1]);
            if(rowIndex||rowIndex==0){
                editRow = rowIndex ;
                $("#incomeTypeGrid").datagrid("beginEdit",editRow) ;
            }
        }
    }) ;

    $("#removeBtn").on('click',function(){
        var row = $("#incomeTypeGrid").datagrid('getSelected') ;
        if(row){
            var rowIndex = $("#incomeTypeGrid").datagrid("getRowIndex",row) ;
            if(rowIndex == editRow){
                editRow = undefined ;
            }
            $.messager.confirm('系统提示','确定要进行删除操作吗',function(r){
                if(r){
                    $("#incomeTypeGrid").datagrid('deleteRow',rowIndex) ;
                }
            });
        }
    }) ;

    $("#saveBtn").on('click',function(){
        if(editRow||editRow==0){
            $("#incomeTypeGrid").datagrid('endEdit',editRow) ;
        }
        var acctProfitChangeDicttBeanChangeVo ={} ;

        acctProfitChangeDicttBeanChangeVo.inserted = [] ;
        acctProfitChangeDicttBeanChangeVo.updated = [] ;
        acctProfitChangeDicttBeanChangeVo.deleted = [] ;

        var inserted = $("#incomeTypeGrid").datagrid('getChanges','inserted') ;
        var updated = $("#incomeTypeGrid").datagrid('getChanges','updated') ;
        var deleted = $("#incomeTypeGrid").datagrid('getChanges','deleted') ;
        for(var i = 0 ;i<inserted.length;i++){
            inserted[i].hospitalId = parent.config.hospitalId ;
            acctProfitChangeDicttBeanChangeVo.inserted.push(inserted[i])
        }
        for(var i = 0 ;i<updated.length;i++){
            updated[i].hospitalId = parent.config.hospitalId ;
            acctProfitChangeDicttBeanChangeVo.updated.push(updated[i])
        }
        for(var i = 0 ;i<deleted.length;i++){
            deleted[i].hospitalId = parent.config.hospitalId ;
            acctProfitChangeDicttBeanChangeVo.deleted.push(deleted[i])
        }

        $.postJSON("/api/acct-profit-change-dict/save-update",acctProfitChangeDicttBeanChangeVo,function(data){

            $("#incomeTypeGrid").datagrid('reload') ;
            $.messager.alert('系统提示',"保存成功",'info') ;
        },function(error){
            $.messager.alert("系统提示","保存失败","error") ;
            console.log(error)
        })

        console.log(acctProfitChangeDicttBeanChangeVo);

    }) ;
})