/**
 * Created by heren on 2015/11/26.
 */

$(function(){
    var editRowType = undefined ;
    var editRowDetail = undefined ;
    var incomeTypes = [] ;

    $.get('/api/cost-item/list-by-class?hospitalId='+parent.config.hospitalId+"&classId=4028803e519f790001519fac9c760009",function(data){
        incomeTypes = data ;
    })
    $("#serviceIncomeTypeDatagrid").datagrid({
        fit:true,
        fitColumns:true,
        striped:true,
        method:'GET',
        url:'/api/cost-item/list-by-class?hospitalId='+parent.config.hospitalId+"&classId=4028803e519f790001519fac9c760009",
        rownumbers:true,
        singleSelect:true,
        toolbar:"#ft",
        loadMsg:'数据正在加载中.....',
        columns:[[{
            title:'id',
            field:'id',
            hidden:true
        },{
            title:'服务类别名称',
            field:'costItemName',
            width:'55%'
        },{
            title:'加成率',
            field:'addRate',
            width:'15%',
            editor:{type:'textbox',options:{}}
        },{
            title:'编码',
            field:'costItemCode',
            width:'15%'
        }]],
        onDblClickRow:function(rowIndex,rowData){
            if(stopEdit()){
                editRowType = rowIndex ;
                beginEdit() ;
            }
        },
        onClickRow:function(rowIndex,rowData){
            var options = $("#incomeDetailDatagrid").datagrid('options') ;
            options.url = "/api/service-income-type/list-detail?hospitalId="+parent.config.hospitalId+"&typeId="+rowData.id ;
            $("#incomeDetailDatagrid").datagrid('reload') ;
        }
    }) ;

    $("#incomeDetailDatagrid").datagrid({
        fit:true,
        fitColumns:true,
        striped:true,
        method:'GET',
        url:"/api/service-income-type/list-detail?hospitalId="+parent.config.hospitalId,
        rownumbers:true,
        singleSelect:true,
        toolbar:"#tbDetail",
        loadMsg:'数据正在加载中.....',
        columns:[[{
            title:'id',
            field:'id',
            hidden:true
        },{
            title:'收入分类',
            field:'incomeTypeId',
            width:'10%',
            formatter:function(value,row,index){
                for(var i = 0 ;i<incomeTypes.length;i++){
                    if(value == incomeTypes[i].id){
                        return incomeTypes[i].costItemName ;
                    }else{
                        continue ;
                    }
                }
                return value ;
            }
        },{
            title:'明细项目名称',
            field:'incomeDetailName',
            width:'10%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输入名项目名称'
            }}
        },{
            title:'加成率',
            field:'addRate',
            width:'15%',
            editor:{type:'textbox',options:{}}

        },{
            title:'拼音码',
            field:'inputCode',
            width:'10%',
            editor:{type:'validatebox',options:{
                required:true,
                missingMessage:'请输入拼音码'
            }}
        },{
            title:'单位',
            field:'unit',
            width:'10%',
            editor:{type:'textbox',options:{}}
        },{
            title:'单价',
            field:'price',
            width:'10%',
            editor:{type:'textbox',options:{}}
        }]],
        onDblClickRow:function(rowIndex,rowData){
            if(stopEditDetail()){
                editRowDetail = rowIndex ;
                beginEditDetail() ;
            }
        }
    }) ;

    //添加
    $("#addBtn").on('click',function(){
        if(stopEdit()){
            $("#serviceIncomeTypeDatagrid").datagrid('appendRow',{}) ;
            var rows = $("#serviceIncomeTypeDatagrid").datagrid('getRows') ;
            var index = $("#serviceIncomeTypeDatagrid").datagrid('getRowIndex',rows[rows.length -1]) ;
            editRowType = index ;
            beginEdit() ;
        }
    }) ;

    //删除
    $("#delBtn").on('click',function(){
        var rows = $("#serviceIncomeTypeDatagrid").datagrid('getSelections') ;
        if(!rows){
            $.messager.alert('系统提示','请选择要删除的属性','error') ;
            return ;
        }

        for(var i = 0 ;i<rows.length ;i++){
            var index = $("#serviceIncomeTypeDatagrid").datagrid('getRowIndex',rows[i]) ;
            $("#serviceIncomeTypeDatagrid").datagrid('deleteRow',index) ;
            if(index==editRowType){
                editRowType = undefined ;
            }
        }

    }) ;


    //添加
    $("#addDBtn").on('click',function(){
        var row = $("#serviceIncomeTypeDatagrid").datagrid('getSelected') ;
        if(!row){
            $.messager.alert("系统提示","请选择要添加明细项目的类别") ;
            return ;
        }
        if(stopEditDetail()){
            $("#incomeDetailDatagrid").datagrid('appendRow',{incomeTypeId:row.id,addRate:row.addRate}) ;
            var rows = $("#incomeDetailDatagrid").datagrid('getRows') ;
            var index = $("#incomeDetailDatagrid").datagrid('getRowIndex',rows[rows.length -1]) ;
            editRowDetail = index ;
            beginEditDetail() ;
        }
    }) ;

    //删除
    $("#delDBtn").on('click',function(){
        var rows = $("#incomeDetailDatagrid").datagrid('getSelections') ;
        if(!rows){
            $.messager.alert('系统提示','请选择要删除的属性','error') ;
            return ;
        }

        for(var i = 0 ;i<rows.length ;i++){
            var index = $("#incomeDetailDatagrid").datagrid('getRowIndex',rows[i]) ;
            $("#incomeDetailDatagrid").datagrid('deleteRow',index) ;
            if(index==editRowDetail){
                editRowDetail = undefined ;
            }
        }

    }) ;

    //保存
    $("#saveBtn").on('click',function(){
        if(stopEdit()){
            var inserted = $("#serviceIncomeTypeDatagrid").datagrid('getChanges','inserted') ;
            var updated = $("#serviceIncomeTypeDatagrid").datagrid('getChanges','updated') ;
            var deleted = $("#serviceIncomeTypeDatagrid").datagrid('getChanges','deleted') ;
            for(var i = 0 ;i<inserted.length ;i++){
                inserted[i].hospitalId = parent.config.hospitalId ;
            }
            for(var i = 0 ;i<deleted.length ;i++){
                deleted[i].hospitalId = parent.config.hospitalId ;
            }
            for(var i = 0 ;i<updated.length ;i++){
                updated[i].hospitalId = parent.config.hospitalId ;
            }

            var beanChangeVo = {} ;
            beanChangeVo.inserted=inserted;
            beanChangeVo.deleted=deleted;
            beanChangeVo.updated= updated ;
            $.postJSON("/api/cost-item/save-update",beanChangeVo,function(data){
                $.messager.alert('系统提示',"更新成功") ;
                $("#serviceIncomeTypeDatagrid").datagrid('reload') ;
            },function(data){
                $.messager.alert('系统提示','更新失败') ;
            })
        }
    }) ;



    //保存
    $("#saveDBtn").on('click',function(){
        if(stopEditDetail()){
            var inserted = $("#incomeDetailDatagrid").datagrid('getChanges','inserted') ;
            var updated = $("#incomeDetailDatagrid").datagrid('getChanges','updated') ;
            var deleted = $("#incomeDetailDatagrid").datagrid('getChanges','deleted') ;
            for(var i = 0 ;i<inserted.length ;i++){
                inserted[i].hospitalId = parent.config.hospitalId ;
            }
            for(var i = 0 ;i<deleted.length ;i++){
                deleted[i].hospitalId = parent.config.hospitalId ;
            }
            for(var i = 0 ;i<updated.length ;i++){
                updated[i].hospitalId = parent.config.hospitalId ;
            }

            var beanChangeVo = {} ;
            beanChangeVo.inserted=inserted;
            beanChangeVo.deleted=deleted;
            beanChangeVo.updated= updated ;
            for(var i = 0;i<beanChangeVo.inserted.length ;i++){
                if(isNaN(beanChangeVo.inserted[i].price)){
                    $.messager.alert("系统提示","名称为：["+beanChangeVo.inserted[i].incomeDetailName+"]的项目输入的价格不合理",'error') ;
                    return ;
                }
            }
            for(var i = 0;i<beanChangeVo.updated.length ;i++){
                if(isNaN(beanChangeVo.updated[i].price)){
                    $.messager.alert("系统提示","名称为：["+beanChangeVo.updated[i].incomeDetailName+"]的项目输入的价格不合理",'error') ;
                    return ;
                }
            }
            $.postJSON("/api/service-income-type/save-update-detail",beanChangeVo,function(data){
                $.messager.alert('系统提示',"更新成功") ;
                $("#incomeDetailDatagrid").datagrid('reload') ;
            },function(data){
                $.messager.alert('系统提示','更新失败') ;
            })
        }
    }) ;

    //刷新
    $("#searchBtn").on('click',function(){
        $("#serviceIncomeTypeDatagrid").datagrid('reload') ;
    })


    //停止编辑行
    var stopEdit = function(){
        if(editRowType || editRowType==0){
            console.log(editRowType) ;
            $("#serviceIncomeTypeDatagrid").datagrid('endEdit',editRowType);
            editRowType = undefined ;
            return true ;
        }else{
            editRowType=undefined ;
            return true ;
        }
    }

    //开始编辑行
    var beginEdit = function(){
        if(editRowType||editRowType==0){
            $("#serviceIncomeTypeDatagrid").datagrid('beginEdit',editRowType) ;
        }
    }



    //停止编辑行
    var stopEditDetail = function(){
        if(editRowDetail || editRowDetail==0){
            var ed = $("#incomeDetailDatagrid").datagrid('getEditor', {index: editRowDetail, field: "incomeDetailName"});
            if(ed.target){
                var flag = $(ed.target).validatebox('isValid');
                if (flag) {

                } else {
                    $.messager.alert("系统提示", "必须填写明细项目的名称", 'error');
                    return flag;
                }
            }
            var ed1 = $("#incomeDetailDatagrid").datagrid('getEditor', {index: editRowDetail, field: "inputCode"});
            if(ed1.target){
                var flag = $(ed1.target).validatebox('isValid');
                if (flag) {
                } else {
                    $.messager.alert("系统提示", "拼音码必须填写", 'error');
                    return flag;
                }
            }
            $("#incomeDetailDatagrid").datagrid('endEdit', editRowDetail);
            editRowDetail = undefined ;
            return true ;
        }else{
            editRowDetail=undefined ;
            return true ;
        }
    }

    //开始编辑行
    var beginEditDetail = function(){
        if(editRowDetail||editRowDetail==0){
            $("#incomeDetailDatagrid").datagrid('beginEdit',editRowDetail) ;
        }
    }


    //设置分摊项目的对照科室
    $("#acctDeptWin").window({
        title:'分摊项目设置',
        width:'500',
        height:'500',
        modal:true,
        closed:true,
        onOpen:function(){
            $(this).window('center');
            var row = $('#serviceIncomeTypeDatagrid').datagrid('getSelected');
            $.get("/api/cost-item/cost-devide?costId="+row.id,function(data){
                for(var i = 0 ;i<data.length ;i++){
                    var deptId = data[i].deptId ;
                    var rows =$("#acctDeptTable").datagrid('getRows') ;
                    for(var j=0 ;j<rows.length;j++){
                        if(rows[j].id==deptId){
                            var rowIndex = $("#acctDeptTable").datagrid('getRowIndex',rows[j]) ;
                            $("#acctDeptTable").datagrid('checkRow',rowIndex) ;
                        }
                    }
                }
            })
        },
        onClose:function(){
            $("#acctDeptTable").datagrid('unselectAll') ;
        }
    }) ;

    $("#acctDeptTable").datagrid({
        method:'GET',
        fit:true,
        fitColumns:true,
        url:'/api/acct-dept-dict/list-end-dept?hospitalId='+parent.config.hospitalId,
        columns:[[{
            title:"编号",
            field:'id',
            checkbox:true
        },{
            title:'科室名称',
            field:'deptName',
            width:'50%'
        },{
            title:'科室代码',
            field:'deptCode',
            width:'50%'
        }]]
    }) ;



    $("#cancelAcctDeptBtn").on('click',function(e){
        $("#acctDeptWin").window('close') ;
    }) ;

    $("#setDevideBtn").on('click',function(){
        var row = $('#serviceIncomeTypeDatagrid').datagrid('getSelected')
        if(!row){//如果没有选择或者不是获取方式不是分摊的话
            $.messager.alert('系统提示','没有选择对应的成本项目','error')
            return ;
        }
        $("#acctDeptWin").window('open') ;
    }) ;

    $("#saveAcctDeptBtn").on('click',function(){
        var row = $('#serviceIncomeTypeDatagrid').datagrid('getSelected')
        var rows = $("#acctDeptTable").datagrid('getSelections') ;
        var costDevide=[] ;

        for(var i=0 ;i<rows.length;i++){
            var cs = {};
            cs.costItemId = row.id ;
            cs.deptId=rows[i].id ;
            costDevide.push(cs) ;
        }

        $.postJSON("/api/cost-item/save-devide/"+row.id,costDevide,function(data){
            $.messager.alert('系统提示','保存成功','info') ;
            $("#acctDeptWin").window('close') ;
        },function(data){})
    })
}) ;