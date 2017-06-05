/**
 * Created by heren on 2015/10/9.
 */
$(function(){

    var editIndex ;
    var stopEdit = function(){
        if(editIndex||editIndex==0){
            $("#baseDictDg").datagrid('endEdit',editIndex) ;
            editIndex = undefined ;
        }
    }
    $("#baseDictDg").datagrid({
        title:'基础字典维护',
        fit:true,
        rownumbers:true,
        footer:"#tb",
        url:'/api/base-dict/list-by-type',
        method:'GET',
        rownumbers: true,
        singleSelect:true,
        columns:[[{
            title:'id',
            field:'id',
            hidden:true
        },{
            title:'键值',
            field:'baseCode',
            align: 'center',
            width:"10%",
            editor:'text'
        },{
            title:'键名',
            field:'baseName',
            align: 'center',
            width:'30%',
            editor:'text'
        },{
            title:'字典名称',
            field:'baseType',
            align: 'center',
            width:'20%',
            editor:'text'
        },{
            title:'输入码',
            field:'inputCode',
            align: 'center',
            width:'20%'
        }]],
        onClickRow:function(index,row){
            stopEdit() ;
            $(this).datagrid('beginEdit',index) ;
            editIndex = index ;
        }

    });

    var loadDict = function () {
        //var name = $("#name").textbox("getValue");
        $.get('/api/base-dict/list-by-type', function (data) {
            $("#baseDictDg").datagrid('loadData', data);
        });
    }
    loadDict();

    /**
     * 添加字典
     */
    $("#addBaseDictBtn").on('click',function(){

        stopEdit() ;

        $("#baseDictDg").datagrid('appendRow',{}) ;
        var rows = $("#baseDictDg").datagrid('getRows') ;
        var addRowIndex = $("#baseDictDg").datagrid('getRowIndex',rows[rows.length -1]) ;
        editIndex =addRowIndex ;
        $("#baseDictDg").datagrid('selectRow',editIndex) ;
        $("#baseDictDg").datagrid('beginEdit',editIndex) ;
    }) ;


    /**
     * 追加键值
     */
    $("#appendBaseDictBtn").on('click',function(){
        //选中某行，在某行后面追加键值
        if(typeof (editIndex) != "undefined"){
            $("#baseDictDg").datagrid('endEdit', editIndex);
            var thisRow = $("#baseDictDg").datagrid('getSelected');
            editIndex = editIndex + 1 ;
            $("#baseDictDg").datagrid('insertRow',{
                index: editIndex ,
                row: {
                    id: '',
                    baseCode: '',
                    baseName: '',
                    baseType: thisRow.baseType
                }
            });
            $("#baseDictDg").datagrid('selectRow', editIndex);
            $("#baseDictDg").datagrid('beginEdit',editIndex);
            var dateEd = $("#baseDictDg").datagrid('getEditor', {index: editIndex, field: 'baseType'});
            //$(dateEd.target).attr('disabled',true); //设置字典名称不可编辑
            var col = dateEd.target;
            col.prop('readonly',true);   //设置字典名称只读
        }else{      //未选中任意一行，直接在最后一行追加键值
            stopEdit();
            var rows = $("#baseDictDg").datagrid('getRows');
            var lastRow;
            if (rows.length > 0) {
                lastRow = rows[rows.length - 1];
            }

            if (lastRow) {
                $("#baseDictDg").datagrid('appendRow', {id: '', baseCode: '', baseName: '', baseType: lastRow.baseType});
                var newRows = $("#baseDictDg").datagrid('getRows');
                var newRowIndex = $("#baseDictDg").datagrid('getRowIndex', newRows[newRows.length - 1]);

                editIndex = newRowIndex;
                $("#baseDictDg").datagrid('selectRow', editIndex);
                $("#baseDictDg").datagrid('beginEdit', editIndex);
                var dateEd = $("#baseDictDg").datagrid('getEditor', {index: editIndex, field: 'baseType'});
                //$(dateEd.target).attr('disabled',true); //设置字典名称不可编辑
                var col = dateEd.target;
                col.prop('readonly', true);   //设置字典名称只读
            } else {
                $("#baseDictDg").datagrid('appendRow', {});
                var newRows = $("#baseDictDg").datagrid('getRows');
                var newRowIndex = $("#baseDictDg").datagrid('getRowIndex', newRows[newRows.length - 1]);

                editIndex = newRowIndex;
                $("#baseDictDg").datagrid('selectRow', editIndex);
                $("#baseDictDg").datagrid('beginEdit', editIndex);
                var dateEd = $("#baseDictDg").datagrid('getEditor', {index: editIndex, field: 'baseType'});
                //$(dateEd.target).attr('disabled',true); //设置字典名称不可编辑
                var col = dateEd.target;
                col.prop('readonly', true);   //设置字典名称只读
            }
        }
    }) ;

    /**
     * 删除字典操作
     */
    $("#delBaseDictBtn").on('click',function(){
        var row = $("#baseDictDg").datagrid('getSelected') ;
        if(row){
            var rowIndex = $("#baseDictDg").datagrid('getRowIndex',row) ;
            $("#baseDictDg").datagrid('deleteRow',rowIndex) ;
            if(editIndex ==rowIndex){
                editIndex = undefined ;
            }
        }else{
            $.messager.alert('系统提示',"请选择要删除的行",'info') ;
        }
    }) ;

    /**
     * 查询操作
     */
    $("#searchBtn").on('click',function(){
        var dictType = $("#baseName").textbox('getValue');

        $("#baseDictDg").datagrid(
            'load',{
                baseType:dictType
            }
        ) ;
    }) ;

    /**
     * 保存修改内容
     */
    $("#saveBaseDictBtn").on('click',function(){
        if(editIndex||editIndex==0){
            $("#baseDictDg").datagrid('endEdit',editIndex) ;

        }
        var inserted  = $("#baseDictDg").datagrid('getChanges','inserted') ;
        var deleted = $("#baseDictDg").datagrid('getChanges','deleted') ;
        var updated = $("#baseDictDg").datagrid('getChanges','updated') ;

        var baseDictBeanChangeVo = {} ;
        baseDictBeanChangeVo.inserted = inserted ;
        baseDictBeanChangeVo.deleted = deleted ;
        baseDictBeanChangeVo.updated = updated ;

        if(baseDictBeanChangeVo.inserted.length > 0){
            for(var i = 0 ; i < baseDictBeanChangeVo.inserted.length ; i++){
                var baseCode = baseDictBeanChangeVo.inserted[i].baseCode;
                var baseName = baseDictBeanChangeVo.inserted[i].baseName;
                var baseType = baseDictBeanChangeVo.inserted[i].baseType;
                if(baseCode.length == 0){
                    $.messager.alert('提示','键值不能为空!','error');
                    return ;
                }
                if (baseName.length == 0) {
                    $.messager.alert('提示', '键名不能为空!', 'error');
                    return;
                }
                if (baseType.length == 0) {
                    $.messager.alert('提示', '字典名称不能为空!', 'error');
                    return;
                }
            }
        }
        if (baseDictBeanChangeVo.updated.length > 0) {
            for (var i = 0; i < baseDictBeanChangeVo.updated.length; i++) {
                var baseCode = baseDictBeanChangeVo.updated[i].baseCode;
                var baseName = baseDictBeanChangeVo.updated[i].baseName;
                var baseType = baseDictBeanChangeVo.updated[i].baseType;
                if (baseCode.length == 0) {
                    $.messager.alert('提示', '键值不能为空!', 'error');
                    return;
                }
                if (baseName.length == 0) {
                    $.messager.alert('提示', '键名不能为空!', 'error');
                    return;
                }
                if (baseType.length == 0) {
                    $.messager.alert('提示', '字典名称不能为空!', 'error');
                    return;
                }
            }
        }

        $.postJSON("/api/base-dict/merge",baseDictBeanChangeVo,function(data,status){
            $.messager.alert("系统提示","保存成功","info");
            loadDict();
        },function(error){
            $.messager.alert("系统提示","保存失败","error");
            loadDict();
        })
    }) ;

})