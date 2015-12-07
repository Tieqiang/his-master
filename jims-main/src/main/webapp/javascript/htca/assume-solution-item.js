/**
 * Created by heren on 2015/11/26.
 */

$(function () {
    var editRow = undefined;

    var solutions = [] ;
    $.ajax({
        url: "/api/assume-solution/list-all",
        success:function(data){
            solutions = data ;
        }
    })

    $("#costRateGrid").datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        method: 'GET',
        url: "/api/cost-rate/list-all",
        rownumbers: true,
        loadMsg: '数据正在加载中.....',
        columns:[[{
            title:'id',
            field:'id',
            checkbox:true
        },{
            title:'比例名称',
            field:'rateName',
            width:'50%'
        },{
            title:"比例值",
            field:'rateValue',
            width:'50%',
            editor:{type:'validatebox',options:{}},
            formatter:function(value,row,index){
                if(!value){
                    return 0 ;
                }else{
                    return value ;
                }
            }
        }]],
        onSelect:function(rowIndex,rowData){
            $(this).datagrid('beginEdit',rowIndex) ;
        },
        onUnselect:function(rowIndex,rowData){
            $(this).datagrid('endEdit',rowIndex) ;
        }
    }) ;

    $("#diag").window({
        height:310,
        width:450,
        title:'设置成本比例',
        modal:true,
        closed:true,
        footer:'#classWinFt',
        onOpen:function(){
            $("#costRateGrid").datagrid('reload') ;
            $(this).window('center') ;
        }
    })

    $("#assumeSolutionItemDatagrid").datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        singleSelect:true,
        method: 'GET',
        url: "/api/assume-solution-item/list-all",
        rownumbers: true,
        footer: "#ft",
        loadMsg: '数据正在加载中.....',
        columns: [[{
            title: 'id',
            field: 'id',
            hidden: true
        }, {
            title: '分摊方法名称',
            field: 'solutionItemName',
            width: '30%',
            editor: {
                type: 'validatebox', options: {
                    required: true,
                    missingMessage: '请输入分摊方法名称'
                }
            }
        }, {
            title: '分摊方法代码',
            field: 'solutionItemCode',
            width: '20%',
            editor: {
                type: 'validatebox', options: {
                    required: true,
                    missingMessage: '请输入分摊方法代码'
                }
            }
        }, {
            title: '分摊方法比例',
            field: 'solutionRate',
            width: '30%'
        }, {
            title: "分摊方案类别",
            field: 'solutionId',
            width: '10%',
            editor: {
                type: "combobox", options: {
                    valueField: "id",
                    textField: "assumeSolutionName",
                    url: "/api/assume-solution/list-all",
                    method: 'GET'
                }
            },
            formatter:function(value,row,rowIndex){
                for(var i = 0 ;i<solutions.length ;i++){
                    if(value==solutions[i].id){
                        return solutions[i].assumeSolutionName ;
                    }
                }
            }
        }]],
        onDblClickRow: function (rowIndex, rowData) {
            if (stopEdit()) {
                editRow = rowIndex;
                beginEdit();
            }
        }
    });


    //添加
    $("#addBtn").on('click', function () {
        if (stopEdit()) {
            $("#assumeSolutionItemDatagrid").datagrid('appendRow', {});
            var rows = $("#assumeSolutionItemDatagrid").datagrid('getRows');
            var index = $("#assumeSolutionItemDatagrid").datagrid('getRowIndex', rows[rows.length - 1]);
            editRow = index;
            beginEdit();
        }
    });

    //删除
    $("#delBtn").on('click', function () {
        var rows = $("#assumeSolutionItemDatagrid").datagrid('getSelections');
        if (!rows) {
            $.messager.alert('系统提示', '请选择要删除的属性', 'error');
            return;
        }

        for (var i = 0; i < rows.length; i++) {
            var index = $("#assumeSolutionItemDatagrid").datagrid('getRowIndex', rows[i]);
            $("#assumeSolutionItemDatagrid").datagrid('deleteRow', index);
            if (index == editRow) {
                editRow = undefined;
            }
        }

    });

    //保存
    $("#saveBtn").on('click', function () {
        if (stopEdit()) {
            var inserted = $("#assumeSolutionItemDatagrid").datagrid('getChanges', 'inserted');
            var updated = $("#assumeSolutionItemDatagrid").datagrid('getChanges', 'updated');
            var deleted = $("#assumeSolutionItemDatagrid").datagrid('getChanges', 'deleted');

            var beanChangeVo = {};
            beanChangeVo.inserted = inserted;
            beanChangeVo.deleted = deleted;
            beanChangeVo.updated = updated;


            var rows = $("#assumeSolutionItemDatagrid").datagrid('getRows') ;
            for(var i = 0 ;i<rows.length ;i++){
                if(rows[i].id !=null&&rows[i].id !=undefined && rows[i].id !=""){
                    beanChangeVo.updated.push(rows[i]) ;
                }
            }

            console.log(beanChangeVo);
            $.postJSON("/api/assume-solution-item/save-update", beanChangeVo, function (data) {
                $.messager.alert('系统提示', "更新成功");
                $("#assumeSolutionItemDatagrid").datagrid('acceptChanges');
                $("#assumeSolutionItemDatagrid").datagrid('reload');
            }, function (data) {
                $.messager.alert('系统提示', '更新失败');
            })
        }
    });

    //刷新
    $("#searchBtn").on('click', function () {
        $("#assumeSolutionItemDatagrid").datagrid('reload');
    })

    $("#setRateBtn").on('click',function(){
        var rows = $("#assumeSolutionItemDatagrid").datagrid('getSelections') ;
        if(rows.length>0){
            $("#diag").window('open') ;
        }else{
            $.messager.alert('系统提示','请选择要设置的项目','info') ;
        }
    }) ;

    $("#cancel").on('click',function(){
        $("#diag").window('close') ;
    })

    $("#saveRateBtn").on('click',function(){
        var rows = $("#costRateGrid").datagrid('getSelections') ;

        for(var i = 0 ;i<rows.length;i++){
            var index = $("#costRateGrid").datagrid('getRowIndex',rows[i]) ;
            $("#costRateGrid").datagrid('endEdit',index) ;
        }

        console.log(rows)
        if(rows.length<=0){
            $("#diag").window('close') ;
        }else{
            var totals =0;
            var text = "" ;
            for(var i = 0 ;i<rows.length ;i++){
                rateValue = rows[i].rateValue ;
                if(!rateValue){
                    continue ;
                }
                totals +=rateValue ;

                if(i != rows.length -1){
                    text+=rows[i].rateName +"*" +rateValue + " +";
                }else{
                    text+=rows[i].rateName +"*" +rateValue ;
                }

                if($.endWith(text,'+')&&i==rows.length-1){
                    text = text.substr(0,text.length -1) ;
                }

            }

            if(totals>1){
                $.messager.alert("系统提示",'总比例不能大于1') ;
            }else{
                var wRow = $("#assumeSolutionItemDatagrid").datagrid('getSelections') ;

                for(var i = 0 ;i<wRow.length;i++){
                    wRow[i].solutionRate = text ;
                    var rowIndex = $("#assumeSolutionItemDatagrid").datagrid('getRowIndex',wRow[i]) ;
                    $("#assumeSolutionItemDatagrid").datagrid('endEdit',rowIndex) ;
                    $("#assumeSolutionItemDatagrid").datagrid('updateRow',{index:rowIndex,row:wRow[i]}) ;
                }
                $('#diag').window('close') ;
            }
        }
    })

    //停止编辑行
    var stopEdit = function () {
        if (editRow || editRow == 0) {
            var ed = $("#assumeSolutionItemDatagrid").datagrid('getEditor', {
                index: editRow,
                field: "solutionItemName"
            });
            if (ed) {
                var flag = $(ed.target).validatebox('isValid');
                if (flag) {

                } else {
                    $.messager.alert("系统提示", "成本属性名称必须填写", 'error');
                    return flag;
                }
            }
            var ed1 = $("#assumeSolutionItemDatagrid").datagrid('getEditor', {
                index: editRow,
                field: "solutionItemCode"
            });
            if (ed1) {
                var flag = $(ed1.target).validatebox('isValid');
                if (flag) {
                } else {
                    $.messager.alert("系统提示", "成本属性名称必须填写", 'error');
                    return flag;
                }
            }
            $("#assumeSolutionItemDatagrid").datagrid('endEdit', editRow);
            editRow = undefined;
            return true;
        } else {
            editRow = undefined;
            return true;
        }
    }

    //开始编辑行
    var beginEdit = function () {
        if (editRow || editRow == 0) {
            $("#assumeSolutionItemDatagrid").datagrid('beginEdit', editRow);
        }
    }
});