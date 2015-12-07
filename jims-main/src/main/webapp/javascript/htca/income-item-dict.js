/**
 * Created by heren on 2015/10/28.
 */
/**
 * 收入分类维护
 */
$(function(){

    var editRow = undefined ;//当前处于编辑状态的行

    //下拉框
    $("#reckCombo").combobox({
        url:'/api/acct-reck/list?hospitalId='+parent.config.hospitalId,
        valueField:'reckItemCode',
        textField:'reckItemName',
        method:'GET'
    }) ;
    //定义输入项目表格
    $("#incomeItemGrid").datagrid({
        fit:true,
        fitColumns:true,
        striped:true,
        singleSelect:true,
        url:'/api/income-item/list?hospitalId='+parent.config.hospitalId,
        toolbar:'#ft',
        method:'GET',
        loadMsg:'正在加载数据，请稍等......',
        pagination:true,
        pageSize:30,
        columns:[[{
            title:'id',
            field:'id',
            hidden:true
        },{
            title:'收入项目名称',
            field:'priceItemName'
        },{
            title:'收入项目代码',
            field:'priceItemCode'
        },{
            title:"收入项目拼音码",
            field:'inputCode',
            editor:{type:'textbox',options:{}}
        },{
            title:'核算科目名称',
            field:'reckItemName'
        },{
            title:'核算科目代码',
            field:'reckItemCode'
        },{
            title:'门诊开单比率',
            field:'outpOrderedBy',
            editor:{type:'textbox',options:{}}
        },{
            title:'门诊执行比率',
            field:'outpPerformedBy',
            editor:{type:'textbox',options:{}}
        },{
            title:'门诊护士比率',
            field:'outpWardCode',
            editor:{type:'textbox',options:{}}
        },{
            title:'住院开单比率',
            field:'inpOrderedBy',
            editor:{type:'textbox',options:{}}
        },{
            title:'住院执行比率',
            field:'inpPerformedBy',
            editor:{type:'textbox',options:{}}
        },{
            title:'住院护士比率',
            field:'inpWardCode',
            editor:{type:'textbox',options:{}}
        }]],
        onClickRow:function(index,row){
            if(editRow==0||editRow){
                $(this).datagrid('endEdit',editRow) ;
            }
            editRow = index ;

            $(this).datagrid('beginEdit',editRow) ;
        },
        onRowContextMenu:function(e,rowIndex,rowData){
            e.preventDefault() ;
            $("#rightMenu").menu('show',{
                left: e.pageX ,
                top: e.pageY,
                onClick:function(item){

                    if(item.id=='makeAllBtn'){
                        var rows = $("#incomeItemGrid").datagrid('getRows') ;
                    }
                }
            }) ;
        }
    }) ;

    //设置分页
    var p = $('#incomeItemGrid').datagrid('getPager');
    $(p).pagination({
        pageSize: 30,//每页显示的记录条数，默认为10
        pageList: [10,20,30,50],//可以设置每页记录条数的列表
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });

    $("#addBtn").on('click',function(){
        var options = $("#incomeItemGrid").datagrid('options') ;

        var reckCode = $("#reckCombo").combobox('getValue') ;
        options.url = '/api/income-item/list-price-item?hospitalId='+parent.config.hospitalId+"&reckCode="+reckCode;
        $("#incomeItemGrid").datagrid('reload') ;
    }) ;

    //删除项目
    $("#removeBtn").on('click',function(){
        var row = $("#incomeItemGrid").datagrid('getSelected') ;
        if(row){
            var rowIndex = $("#incomeItemGrid").datagrid("getRowIndex",row) ;
            if(rowIndex == editRow){
                editRow = undefined ;
            }

            $("#incomeItemGrid").datagrid('removeRow',rowIndex) ;
        }
    }) ;

    //保存项目
    $("#saveBtn").on('click',function(){
        if(editRow||editRow==0){
            $("#incomeItemGrid").datagrid('endEdit',editRow) ;
        }
        var incomeItemDictBeanChangeVo ={} ;

        incomeItemDictBeanChangeVo.inserted = [] ;
        incomeItemDictBeanChangeVo.updated = [] ;
        incomeItemDictBeanChangeVo.deleted = [] ;

        //var inserted = $("#incomeItemGrid").datagrid('getChanges','inserted') ;
        var updated = $("#incomeItemGrid").datagrid('getChanges','updated') ;
        var deleted = $("#incomeItemGrid").datagrid('getChanges','deleted') ;
        //保存所有
        var inserted = $("#incomeItemGrid").datagrid('getRows') ;
        for(var i = 0 ;i<inserted.length;i++){
            inserted[i].hospitalId = config.hospitalId ;
            incomeItemDictBeanChangeVo.inserted.push(inserted[i])
        }
        for(var i = 0 ;i<updated.length;i++){
            updated[i].hospitalId = config.hospitalId ;
            incomeItemDictBeanChangeVo.updated.push(updated[i])
        }
        for(var i = 0 ;i<deleted.length;i++){
            deleted[i].hospitalId = config.hospitalId ;
            incomeItemDictBeanChangeVo.deleted.push(deleted[i])
        }

        $.postJSON("/api/income-item/save-update",incomeItemDictBeanChangeVo,function(data){

            $("#incomeItemGrid").datagrid('reload') ;
            $.messager.alert('系统提示',"保存成功",'info') ;
        },function(error){
            $.messager.alert("系统提示","保存失败","error") ;
            console.log(error)
        })


    }) ;

    //跟新输入法
    $("#makeInputCodeBtn").on('click',function(){
        $.post("/api/income-item/update-input?hospitalId="+config.hospitalId,function(data){
            $.messager.alert("系统提示","更新输入法成功！","info") ;
            $("#incomeItemGrid").datagrid('reload') ;
            $.messager.alert("系统提示","更新输入法成功！","info") ;
        },function(error){
            $.messager.alert("系统提示","更新输入法失败",'error') ;
        })
    }) ;

    $("#searchBtn").on('click',function(){
        var reckCode = $("#reckCombo").combobox('getValue') ;
        if(!reckCode){
            $.messager.alert('系统提示','请选择相应的核算类别','error') ;
            return ;
        }
        var options = $("#incomeItemGrid").datagrid('options') ;
        options.url = '/api/income-item/list-reck?hospitalId='+parent.config.hospitalId+"&reckCode="+reckCode;
        $("#incomeItemGrid").datagrid('reload') ;
    })

    $("#makeAllBtn").on('click',function(){
        var reckCode = $("#reckCombo").combobox('getValue') ;
        if(!reckCode){
            $.messager.alert('系统提示','请选择相应的核算类别','error') ;
            return ;
        }
        $.get("/api/acct-reck/get-by-reck?reckCode="+reckCode+"&hospitalId="+parent.config.hospitalId,function(data){
            var rows = $("#incomeItemGrid").datagrid('getRows') ;
            for(var i = 0 ;i<rows.length ;i++){
                rows[i].outpOrderedBy = data.outpOrderedBy ;
                rows[i].outpPerformedBy = data.outpPerformedBy ;
                rows[i].outpWardCode = data.outpWardCode ;
                rows[i].inpOrderedBy = data.inpOrderedBy ;
                rows[i].inpPerformedBy = data.inpPerformedBy ;
                rows[i].inpWardCode = data.inpWardCode ;

                var index = $("#incomeItemGrid").datagrid('getRowIndex',rows[i]) ;
                $("#incomeItemGrid").datagrid('updateRow',{index:index,row:rows[i]}) ;
            }
        }) ;

    })
})