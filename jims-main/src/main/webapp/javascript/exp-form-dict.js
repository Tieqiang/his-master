/**
 * Created by heren on 2015/9/16.
 */
/***
 * 消耗品产品经费支出字典
 */
$(function(){
    var expStorageDept = [];
    $.get("/api/exp-storage-dept/list?hospitalId="+parent.config.hospitalId , function (data) {
        expStorageDept = data;
    });
    var editIndex;
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
    $("#dg").datagrid({
        fit:true,//让#dg数据创铺满父类容器
        footer:'#tb',
        singleSelect:true,
        title:  "产品类型字典维护",
        columns:[[{
            title:'编号',
            field:'id',
            hidden:'true'
        },{
            title:'序号',
            field:'serialNo',
            width:"16%",
            editor: 'numberbox'
        },{
            title:'类别',
            field:'expClass',
            width:"16%",
            editor:{type:'text',options:{required:true,validType:'length[0,5]',missingMessage:'请输入5个以内的汉字',invalidMessage:'输入值不在范围'}}
        },{
            title:'类型代码',
            field:'formCode',
            width:"16%",
            editor:{type:'text',options:{required:true,validType:'length[0,10]',missingMessage:'请输入10个以内的字符',invalidMessage:'输入值不在范围'}}
        },{
            title:'类型名称',
            field:'formName',
            width:"16%",
            editor:{type:'text',options:{required:true,validType:'length[0,15]',missingMessage:'请输入15个以内的汉字',invalidMessage:'输入值不在范围'}}
        },{
            title:'输入码',
            field:'inputCode',
            width:"16%",
            hidden:true,
            editor:{type:'text',options:{required:true,validType:'length[0,8]',missingMessage:'请输入8个之内的字符',invalidMessage:'输入值不在范围'}}
        },{
            title:'库存管理单位',
            field:'storageCode',
            width:"20%",
            editor:{type:'combobox',
                     options:{
                         valueField:'storageCode',
                         textField:'storageName',
                         method: 'GET',
                         mode: 'remote',
                         url:'/api/exp-storage-dept/list?hospitalId='+parent.config.hospitalId ,
                         singleSelect:true,
                         fit:true,
                         columns:[[{
                             field:'storageCode',title:'库房代码',width:102
                         },{
                             field:'storageName',title:'库房名称',width:102
                         },{
                             field:'inputCode',title:'输入码',width:102
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
                         }),
                         filter: function (q, row) {
                             if ($.startWith(row.inputCode.toUpperCase(), q.toUpperCase())) {
                                 return $.startWith(row.inputCode.toUpperCase(), q.toUpperCase());
                             }
                             var opts = $(this).combobox('options');
                             return row[opts.textField].indexOf(q) == 0;
                         }
                     }
            },
            formatter:function(value,row,index){
                for(var i = 0;i<expStorageDept.length;i++){
                    if (value == expStorageDept[i].storageCode) {
                        return  expStorageDept[i].storageName;
                    }
                }
                return value;
            }
        }]],
        onClickRow: function (index, row) {
            stopEdit();
            $(this).datagrid('beginEdit', index);
            editIndex = index;
        }
    })  ;

    $("#searchBtn").on("click", function () {
        loadDict();
    });

    $("#addBtn").on('click',function(){
        stopEdit();
        $("#dg").datagrid('appendRow', {});
        var rows = $("#dg").datagrid('getRows');
        var addRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);
        editIndex = addRowIndex;
        $("#dg").datagrid('selectRow', editIndex);
        $("#dg").datagrid('beginEdit', editIndex);
    }) ;

    $("#delBtn").on('click',function(){
        var row = $("#dg").datagrid('getSelected');
        if (row) {
            var rowIndex = $("#dg").datagrid('getRowIndex', row);
            $("#dg").datagrid('deleteRow', rowIndex);
            if (editIndex == rowIndex) {
                editIndex = undefined;
            }
        } else {
            $.messager.alert('系统提示', "请选择要删除的行", 'info');
        }
    }) ;

    $("#editBtn").on('click',function(){
        var row = $("#dg").datagrid("getSelected");
        var index = $("#dg").datagrid("getRowIndex", row);

        if (index == -1) {
            $.messager.alert("提示", "请选择要修改的行！", "info");
            return;
        }

        if (editIndex == undefined) {
            $("#dg").datagrid("beginEdit", index);
            editIndex = index;
        } else {
            $("#dg").datagrid("endEdit", editIndex);
            $("#dg").datagrid("beginEdit", index);
            editIndex = index;
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
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid("endEdit", editIndex);
        }

        var insertData = $("#dg").datagrid("getChanges", "inserted");
        var updateDate = $("#dg").datagrid("getChanges", "updated");
        var deleteDate = $("#dg").datagrid("getChanges", "deleted");

        var beanChangeVo = {};
        beanChangeVo.inserted = insertData;
        beanChangeVo.deleted = deleteDate;
        beanChangeVo.updated = updateDate;


        if (beanChangeVo) {
            $.postJSON("/api/exp-form-dict/merge", beanChangeVo, function (data, status) {
                $.messager.alert("系统提示", "保存成功", "info");
                loadDict();
            }, function (data) {
                $.messager.alert('提示', data.responseJSON.errorMessage, "error");
            })
        }

    }) ;
})