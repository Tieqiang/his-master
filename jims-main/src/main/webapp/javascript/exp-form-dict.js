/**
 * Created by heren on 2015/9/16.
 */
/***
 * 消耗品产品经费支出字典
 */
$(function(){
    var editRowIndex ;
    $("#dg").datagrid({
        fit:true,//让#dg数据创铺满父类容器
        footer:'#tb',
        singleSelect:true,
        title: parent.config.storage + "--产品类别字典维护",
        columns:[[{
            title:'编号',
            field:'id',
            hidden:'true'
        },{
            title:'序号',
            field:'serialNo',
            width:"16%",
            editor:{type:'numberbox',options:{required:true,validType:'length[0,2]',missingMessage:'请输入0-99的数字',invalidMessage:'输入值不在范围'}}
        },{
            title:'类别',
            field:'expClass',
            width:"16%",
            editor:{type:'textbox',options:{required:true,validType:'length[0,5]',missingMessage:'请输入5个以内的汉字',invalidMessage:'输入值不在范围'}}
        },{
            title:'类型代码',
            field:'formCode',
            width:"16%",
            editor:{type:'textbox',options:{required:true,validType:'length[0,10]',missingMessage:'请输入10个以内的字符',invalidMessage:'输入值不在范围'}}
        },{
            title:'类型名称',
            field:'formName',
            width:"16%",
            editor:{type:'textbox',options:{required:true,validType:'length[0,15]',missingMessage:'请输入15个以内的汉字',invalidMessage:'输入值不在范围'}}
        },{
            title:'输入码',
            field:'inputCode',
            width:"16%",
            hidden:true,
            editor:{type:'textbox',options:{required:true,validType:'length[0,8]',missingMessage:'请输入8个之内的字符',invalidMessage:'输入值不在范围'}}
        },{
            title:'库存管理单位',
            field:'storageCode',
            width:"20%",
            editor:{type:'combogrid',
                     options:{
                         idField:'storageCode',
                         textValue:'storageName',
                         method: 'GET',
                         url:'/api/exp-storage-dept/list?hospitalId='+parent.config.hospitalId ,
                         singleSelect:true,
                         fit:true,
                         columns:[[{
                             field:'storageCode',title:'库房代码',width:102
                         },{
                             field:'storageName',title:'库房名称',width:102
                         }]],
                         onClickRow:function(index,row){
                             var ed = $("#dg").datagrid('getEditor',{index:editRowIndex,field:'storageCode'}) ;
                             $(ed.target).combogrid('setValue',row.storageCode) ;
                         },
                         keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler,{
                             enter:function(e){

                                 var row  = $(this).combogrid('grid').datagrid('getSelected') ;
                                 if(row){
                                     var ed = $("#dg").datagrid('getEditor',{index:editIndex,field:'storageCode'}) ;
                                     $(ed.target).combogrid('setValue',row.storageCode) ;
                                 }
                                 $(this).combogrid('hidePanel') ;
                             }
                         })
                     }
            }
        }]]
    })  ;

    $("#searchBtn").on("click", function () {
        var name = $("#name").textbox("getValue");

        $.get("/api/exp-form-dict/list?name=" + name, function (data) {
            $("#dg").datagrid('loadData', data);
        });
    });

    $("#addBtn").on('click',function(){
        $("#dg").datagrid('appendRow',{}) ;
        var rows = $("#dg").datagrid('getRows') ;
        var row = rows[rows.length - 1 ] ;
        var index = $("#dg").datagrid('getRowIndex',row) ;

        $("#dg").datagrid('selectRow',index) ;
        if(editRowIndex ==index){
            $("#dg").datagrid('beginEdit',editRowIndex);
            $("#dg").datagrid('getEditor',{index:editRowIndex,field:'storageCode'}) ;
        }
        if(editRowIndex==undefined){
            $("#dg").datagrid('beginEdit',index);
            $("#dg").datagrid('getEditor',{index:index,field:'storageCode'}) ;
            editRowIndex = index ;
        }else{
            $("#dg").datagrid('endEdit',editRowIndex) ;
            $("#dg").datagrid('beginEdit',index) ;
            $("#dg").datagrid('getEditor',{index:index,field:'storageCode'}) ;
            editRowIndex = index ;
        }
    }) ;

    $("#delBtn").on('click',function(){
        var row = $("#dg").datagrid('getSelected') ;
        if(!row){
            $.messager.alert("系统提醒","请选择要删除的行","error") ;
            return ;
        }
        var index = $("#dg").datagrid('getRowIndex',row) ;
        if(index==editRowIndex){
            editRowIndex =undefined ;
        }
        $("#dg").datagrid('deleteRow',index) ;

    }) ;

    $("#editBtn").on('click',function(){
        var row = $("#dg").datagrid('getSelected') ;
        if(!row){
            $.messager.alert("系统提醒","请选择要编辑的行","error") ;
            return ;
        }
        var index = $("#dg").datagrid('getRowIndex',row) ;
        if(editRowIndex ==undefined){
            $("#dg").datagrid("beginEdit",index) ;
            $("#dg").datagrid('getEditor',{index:index,field:'storageCode'}) ;
            $()
            editRowIndex = index ;
        }else{
            $("#dg").datagrid('endEdit',editRowIndex) ;
            $("#dg").datagrid('getEditor',{index:editRowIndex,field:'storageCode'}) ;
            $("#dg").datagrid('beginEdit',index) ;
            editRowIndex = index ;
        }
    }) ;

    var loadDict = function(){
        var name = $("#name").textbox("getValue");
        $.get("/api/exp-form-dict/list?name=" + name, function (data) {
            $("#dg").datagrid('loadData',data) ;
        }) ;
    }
    loadDict() ;


    /**
     * 保存修改的内容
     * 基础字典的改变，势必会影响其他的统计查询
     * 基础字典的维护只能在基础数据维护的时候使用。
     */
    $("#saveBtn").on('click',function(){
        if(editRowIndex){
            $("#dg").datagrid('endEdit',editRowIndex) ;
            editRowIndex = undefined ;
        }
        var insertData = $("#dg").datagrid('getChanges','inserted') ;
        var updateData = $("#dg").datagrid('getChanges','updated') ;
        var deleteData = $("#dg").datagrid('getChanges','deleted') ;
        if(insertData&&insertData.length > 0 ){
                $.postJSON("/api/exp-form-dict/add",insertData,function(data){
                    $.messager.alert('系统提示','保存成功',"info");
                    loadDict() ;
                },function(data){
                    $.messager.alert("系统提示",data.responseJSON.errorMessage,"error");
                    loadDict() ;
                }) ;
        }

        if(updateData&&updateData.length > 0 ){
            $.postJSON("/api/exp-form-dict/add",updateData,function(data){
                $.messager.alert('系统提示','修改成功',"info");
                loadDict() ;
            },function(data){
                $.messager.alert("系统提示",data.responseJSON.errorMessage,"error");
                loadDict() ;
            }) ;
        }
        if(deleteData&&deleteData.length > 0 ){
            $.postJSON("/api/exp-form-dict/delete",deleteData,function(data){
                $.messager.alert('系统提示','删除成功',"info");
                loadDict() ;
            },function(data){
                $.messager.alert("系统提示",data.responseJSON.errorMessage,"error");
                loadDict() ;
            }) ;
        }
    }) ;
})