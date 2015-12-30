/**
 * Created by heren on 2015/12/2.
 */
$(function(){
    $("#acctParamTable").datagrid({
        fit:true,
        fitColumns:true,
        singleSelect:true,
        url:'/api/acct-param/list?hospitalId='+parent.config.hospitalId,
        toolbar:'#ft',
        method:'GET',
        loadMsg:'数据正在加载中，请稍后.....',
        columns:[[{
            title:'id',
            field:'id',
            hidden:true
        },{
            title:'参数名称',
            field:'paramName',
            width:'20%'
        },{
            title:'参数类型',
            field:'paramType',
            width:'20%'
        },{
            title:'SQL',
            field:'paramSql',
            width:'60%'
        }]]
    }) ;

    $("#paramWin").window({
        closed:true,
        modal:true,
        onOpen:function(){
            $(this).window("center") ;
        },
        onClose:function(){
            $('#itemForm').form('reset') ;
        }
    }) ;


    $("#addBtn").on('click',function(){
        $("#paramWin").window("open") ;
    })

    //确定保存参数
    $("#okBtn").on('click',function(){
        var obj = {} ;
        obj.id = $("#id").textbox('getValue') ;
        obj.paramName =$("#paramName").textbox('getValue') ;
        obj.paramSql = $("#paramSql").val() ;
        obj.paramType = $("#paramType").textbox('getValue') ;
        obj.hospitalId = parent.config.hospitalId ;
        $.postJSON("/api/acct-param/save",obj,function(data){
            $.messager.alert("系统提示",'保存成功','info') ;
            $("#acctParamTable").datagrid('reload') ;
            $("#paramWin").window('close');
        },function(data){
            $.messager.alert('系统提示','保存失败','error') ;
        })
    })

    $("#noBtn").on('click',function(){
        $("#paramWin").window('close') ;
    }) ;

    //编辑
    $("#editBtn").on('click',function(){
        var row  = $("#acctParamTable").datagrid('getSelected') ;
        if(!row){
            $.messager.alert('系统提示','请选择要编辑的行','error') ;
            return ;
        }

        $("#id").textbox('setValue',row.id) ;
        $("#paramName").textbox('setValue',row.paramName) ;
        $("#paramType").textbox('setValue',row.paramType) ;
        $("#paramSql").val(row.paramSql) ;
        $("#paramWin").window('open');

    })
    //删除一个参数
    $("#removeBtn").on('click',function(){
        var row  = $("#acctParamTable").datagrid('getSelected') ;
        if(!row){
            $.messager.alert('系统提示','请选择要编辑的行','error') ;
            return ;
        }

        if(!row.id){
            $.messager.alert('系统提示','获取关键数据失败','error') ;
            return ;
        }
        $.messager.confirm('系统提示','确定要进行删除操作吗',function(r){
            if(r){
                $.post("/api/acct-param/del?id="+row.id,function(){
                    $.messager.alert('系统提示','删除成功','info') ;
                    $("#acctParamTable").datagrid('reload') ;
                })
            }
        });
    })
})