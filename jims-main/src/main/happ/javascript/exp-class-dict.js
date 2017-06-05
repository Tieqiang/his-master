
/***
 * 消耗品类别字典维护
 */
$(function(){
    var editIndex;
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
    $("#dg").datagrid({
        title:'消耗品类别字典维护',
        fit:true,//让#dg数据创铺满父类容器
        footer:'#tb',
        singleSelect:true,
        sortName:'classCode',
        sortOrder:'asc',
        columns:[[{
            title:'编号',
            field:'id',
            hidden:'true'
        },{
            title:'类别代码',
            field:'classCode',
            width:"20%",
            editor: 'textbox',
            sortable:true,
            sorter:function(a,b){
                console.log(a.substring(0,2));
                if(a.substring(0,2)> b.substring(0,2)){
                    return 1
                }else{
                    return -1 ;
                }
            }
        },{
            title:'类别名称',
            field:'className',
            width:"30%",
            editor:{type:'textbox',options:{
                required:true,validType:'length[0,10]',missingMessage:'请输入五个以内的汉字'}
            }}]],
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
        //var name = $("#name").textbox("getValue");

        $.get("/api/exp-class-dict/list", function (data) {
            data.sort(function(a,b){
                if(a.classCode.length== b.classCode.length){
                    return a.classCode- b.classCode ;
                }
                if(a.classCode.length> b.classCode.length){
                    if(a.classCode.substring(0,2)== b.classCode.substring(0,2)){
                        return -1;
                    }else{
                        return a.classCode.substr(0,2) - b.classCode.substr(0,2);
                    }
                }else{
                    if(a.classCode.substring(0,2)== b.classCode.substring(0,2)){
                        return -1;
                    }else{
                        return a.classCode.substr(0,2) - b.classCode.substr(0,2);
                    }
                }

            })
            $("#dg").datagrid('loadData', data);
        });
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
            $.postJSON("/api/exp-class-dict/merge", beanChangeVo, function (data, status) {
                $.messager.alert("系统提示", "保存成功", "info");
                loadDict();
            }, function (data) {
                $.messager.alert('提示', data.responseJSON.errorMessage, "error");
            })
        }
    }) ;
})