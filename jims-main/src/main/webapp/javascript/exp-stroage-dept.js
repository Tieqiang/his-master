/**
 * Created by heren on 2015/9/17.
 */

/**
 * 库房字典维护
 */
$(function(){


    var editIndex ;
    //初始化表单
    var data =[] ;

    var promise =$.get('/api/dept-dict/list?hospitalId='+parent.config.hospitalId,function(data1){
        data = data1 ;
    }) ;

    promise.done(function(){
        $("#dg").datagrid({
            title:'库房字典维护',
            singleSelect:true,
            fit:true,
            url:'/api/exp-storage-dept/list?hospitalId='+parent.config.hospitalId,
            method:'GET',
            footer:'#tb',
            columns:[[{
                title:'库房代码',
                field:'storageCode',
                width:"20%",
                editor:{type:'textbox',options:{editable:false}}
            },{
                title:'库房名称',
                field:'storageName',
                width:"20%",
                editor:{type:"combogrid",options:{
                    idField:'deptCode',
                    textValue:'deptName',
                    panelHeight:300,
                    data:data,
                    singleSelect:true,
                    method:'GET',
                    mode:'local',
                    columns:[[{
                        field:'deptCode',title:'科室代码',width:60
                    },{
                        field:'deptName',title:'科室名称',width:60
                    },{
                        field:'inputCode',title:'输入码',width:60
                    }]],
                    onClickRow:function(index,row){
                        var ed = $("#dg").datagrid('getEditor',{index:editIndex,field:'storageCode'}) ;
                        $(ed.target).textbox('setValue',row.deptCode) ;

                        var comboEd = $("#dg").datagrid('getEditor',{index:editIndex,field:'storageName'}) ;
                        $(comboEd.target).combogrid('setValue',row.deptName) ;
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler,{
                        enter:function(e){

                            var row  = $(this).combogrid('grid').datagrid('getSelected') ;
                            if(row){
                                var ed = $("#dg").datagrid('getEditor',{index:editIndex,field:'storageCode'}) ;
                                $(ed.target).textbox('setValue',row.deptCode) ;
                                var comboEd = $("#dg").datagrid('getEditor',{index:editIndex,field:'storageName'}) ;
                                $(comboEd.target).combogrid('setValue',row.deptName) ;
                            }

                            $(this).combogrid('hidePanel') ;
                        }
                    }),
                    filter:function(q,row){

                        if($.startWith(row.inputCode,q)){
                            var dg = $(this).combogrid('grid') ;

                            var index = dg.datagrid('getRowIndex',row) ;
                            dg.datagrid('selectRow',index) ;
                            dg.datagrid('scrollTo',index);
                            return true ;
                        }
                        return false ;

                    }
                }}
            },{
                title:'库房级别',
                field:'storageLevel',
                width:"20%",
                editor:{type:'combobox',options:{
                    valueField:'value',
                    textField:'text',
                    data:[{
                        value:'1',
                        text:'一级库房',
                        selected:true
                    },{
                        value:'2',
                        text:'二级库房'
                    },{
                        value:'3',
                        text:'三级库房'
                    }]
                }}
            },{
                title:'付款单前缀',
                field:'disburseNoPrefix',
                width:"20%",
                editor:{type:'validatebox',options:{required:true,validType:'length[0,6]',missingMessage:'请输入六字符以内的相应的前缀',invalidMessage:'输入值不在范围'}}
            },{
                title:'当前付款单号',
                field:'disburseNoAva',
                width:"20%",
                editor:{type:'numberbox',options:{required:true,validType:'length[0,4]',missingMessage:'请输入0-9999之内的数字',invalidMessage:'输入值不在范围'}}
            },{
                title:"医院编号",
                field:'hospitalId',
                hidden:true
            }]]
        }) ;
    })

    $("#searchBtn").on("click", function () {
        var name = $("#name").textbox("getValue");

        $.get('/api/exp-storage-dept/list?hospitalId='+parent.config.hospitalId+'&name=' + name, function (data) {
            $("#dg").datagrid('loadData', data);
        });
    });

    $("#addBtn").on('click',function(){
        $("#dg").datagrid('appendRow',{hospitalId:parent.config.hospitalId}) ;
        var rows = $("#dg").datagrid('getRows') ;
        var editRow = rows[rows.length -1 ] ;
        var rowIndex = $("#dg").datagrid("getRowIndex",editRow) ;

        if(editIndex==undefined || editIndex !=rowIndex){
            $("#dg").datagrid("endEdit",editIndex) ;
            $("#dg").datagrid("beginEdit",rowIndex) ;
            editIndex = rowIndex ;
        }

        $("#dg").datagrid('selectRow',editIndex) ;
        var comboEdit = $("#dg").datagrid('getEditor',{index:rowIndex,field:'storageName'}) ;
        $(comboEdit.target).focus() ;
        $(comboEdit.target).combogrid('showPanel') ;
    }) ;

    /**
     * 进行保存操作
     */
    $("#saveBtn").on('click',function(){

        if(editIndex){
            $("#dg").datagrid('endEdit',editIndex) ;
            editIndex = undefined ;
        }

        var insertFlag=false ;
        var updateFlag =false;
        var deleteFlag =false;
        var insertData = $("#dg").datagrid("getChanges","inserted") ;
        var updateData = $("#dg").datagrid('getChanges',"updated") ;
        var deleteData = $("#dg").datagrid('getChanges','deleted') ;

        if(insertData.length >0){
            //插入数据进行保存
            $.postJSON("/api/exp-storage-dept/merge",insertData,function(data){
                insertFlag = true ;
                $.messager.alert("系统提示","插入数据成功","info") ;
            },function(data){
                console.log(data) ;
                $.messager.alert("系统提示",data.responseJSON.errorMessage,"error");
            })
        }

        if(updateData.length>0){
            //更新数据进行保存
            $.postJSON("/api/exp-storage-dept/merge",updateData,function(data){
                updateFlag = true ;
                $.messager.alert("系统提示","更新数据成功","info") ;
            },function(data){
                $.messager.alert("系统提示",data.responseJSON.errorMessage,"error");
            })
        }
        if(deleteData.length >0 ){
            //删除数据进行保存
            $.postJSON("/api/exp-storage-dept/delete",deleteData,function(data){
                deleteFlag = true ;
                $.messager.alert("系统提示","删除数据成功","info") ;
            },function(data){
                $.messager.alert("系统提示",data.responseJSON.errorMessage,"error");
            })
        }

        $("#dg").datagrid('acceptChanges') ;

        if(insertFlag || updateFlag || deleteFlag){
            $.messager.alert("系统提示","数据保存成功","info") ;
        }

    }) ;


    $("#delBtn").on('click',function(){
        if(editIndex){
            $("#dg").datagrid('endEdit',editIndex) ;
            editIndex = undefined ;
        }
        var row = $("#dg").datagrid('getSelected') ;
        if(!row){
            $.messager.alert("系统提示","请选择要删除的行","info") ;
            return ;
        }
        var rowIndex = $("#dg").datagrid('getRowIndex',row) ;
        $("#dg").datagrid('deleteRow',rowIndex) ;
    }) ;



    $("#editBtn").on('click',function(){
        var row = $("#dg").datagrid('getSelected') ;
        if(!row){
            $.messager.alert("系统提示","请选择要删除的行","info") ;
            return ;
        }
        var rowIndex = $("#dg").datagrid('getRowIndex',row) ;

        if(editIndex){
            $("#dg").datagrid("endEdit",editIndex) ;
            editIndex = rowIndex ;
            $("#dg").datagrid('beginEdit',rowIndex) ;
        }else{
            $("#dg").datagrid('beginEdit',rowIndex) ;
            editIndex = rowIndex ;
        }
    })
})