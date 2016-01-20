/**
 * Created by heren on 2015/10/28.
 */
/**
 * 收益调整记录表
 */
$(function(){

    var editRow = undefined ;//当前处于编辑状态的行

    $("#incomeTypeGrid").datagrid({
        fit:true,
        fitColumns:true,
        singleSelect:true,
        url:'/api/acct-proft-chage-record/list',
        footer:'#ft',
        method:'GET',
        columns:[[{
            title:'id',
            field:'id',
            hidden:true
        },{
            title:'调整项目编码',
            field:'changeItemId',
            editor:{type:'textbox',options:{}},
            width:'10%'
        },{
            title:'调整月份',
            field:'yearMonth',
            editor:{type:'textbox',options:{}},
            width:'10%'
        },{
            title:'调整金额',
            field:'changeAmount',
            editor:{type:'textbox',options:{}},
            width:'10%'
        },{
            title:'操作者',
            field:'operator',
            editor:{type:'textbox',options:{}},
            width:'10%'
        },{
            title:'调整日期',
            field:'operatorDate',
            editor:{type:'textbox',options:{}},
            width:'10%'
        },{
            title:'调整项目',
            field:'incomeOrCost',
            editor:{type:'textbox',options:{}},
            width:'10%'
        },{
            title:'调整核算单元编码',
            field:'acctDeptId',
            editor:{type:'textbox',options:{}},
            width:'10%'
        },{
            title:'调整原因',
            field:'changeReason',
            editor:{type:'textbox',options:{}},
            width:'40%'
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
        var acctProftChageRecordBeanChangeVo ={} ;

        acctProftChageRecordBeanChangeVo.inserted = [] ;
        acctProftChageRecordBeanChangeVo.updated = [] ;
        acctProftChageRecordBeanChangeVo.deleted = [] ;

        var inserted = $("#incomeTypeGrid").datagrid('getChanges','inserted') ;
        var updated = $("#incomeTypeGrid").datagrid('getChanges','updated') ;
        var deleted = $("#incomeTypeGrid").datagrid('getChanges','deleted') ;
        for(var i = 0 ;i<inserted.length;i++){
            acctProftChageRecordBeanChangeVo.inserted.push(inserted[i])
        }
        for(var i = 0 ;i<updated.length;i++){
            acctProftChageRecordBeanChangeVo.updated.push(updated[i])
        }
        for(var i = 0 ;i<deleted.length;i++){
            console.log(deleted[i]);
            acctProftChageRecordBeanChangeVo.deleted.push(deleted[i])
        }

        $.postJSON("/api/acct-proft-chage-record/save-update",acctProftChageRecordBeanChangeVo,function(data){

            $("#incomeTypeGrid").datagrid('reload') ;
            $.messager.alert('系统提示',"保存成功",'info') ;
        },function(error){
            $.messager.alert("系统提示","保存失败","error") ;
            console.log(error)
        })

        console.log(acctProftChageRecordBeanChangeVo);

    }) ;
})