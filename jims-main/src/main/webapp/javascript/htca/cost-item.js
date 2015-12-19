/**
 * Created by heren on 2015/11/24.
 * 成本项目维护
 *
 */

$(function () {

    var getWays = [];
    var assumeSolutions = [];
    var costAttrs = [];
    $.get('/api/cost-get-way/list-all', function (data) {
        getWays = data;
    });

    $.get("/api/assume-solution-item/list-all", function (data) {
        assumeSolutions = data;
    })

    $.get('/api/cost-attr/list-all', function (data) {
        costAttrs = data;
    })

    $("#costItemClassGrid").datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        singleSelect: true,
        toolbar: '#classft',
        url: "/api/cost-item/list-item-class?hospitalId=" + parent.config.hospitalId,
        method: 'GET',
        loadMsg: '数据正在加载中，请稍后.....',
        columns: [[{
            title: 'id',
            field: "id",
            hidden: true
        }, {
            title: '成本类别编码',
            field: 'costItemClassCode',
            width: '50%'
        }, {
            title: '成本类别名称',
            field: 'costItemClassName',
            width: '50%'
        }]],
        onClickRow: function (rowIndex, rowData) {
            var options = $("#costItemDictGrid").datagrid('options');
            options.url = "/api/cost-item/list-by-class?hospitalId=" + parent.config.hospitalId + "&classId=" + rowData.id;
            $("#costItemDictGrid").datagrid('reload');
        }
    });

    $("#costItemDictGrid").datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        singleSelect: true,
        toolbar: '#ft',
        method: 'GET',
        url: "/api/cost-item/list-item?hospitalId=" + parent.config.hospitalId,
        loadMsg: '数据正在加载中，请稍后.....',
        columns: [[{
            title: "id",
            field: "id",
            hidden: true
        }, {
            title: '成本项目名称',
            field: 'costItemName'
        }, {
            title: '成本项目编码',
            field: 'costItemCode'
        }, {
            title: '成本属性',
            field: 'costAttr',
            formatter: function (value, row, index) {
                for (var i = 0; i < costAttrs.length; i++) {
                    if (costAttrs[i].attrCode == value) {
                        return costAttrs[i].attrName;
                    }
                }
                return value;
            }

        }, {
            title: '军地分摊方案',
            field: 'armyLocayCalcWay',
            formatter: function (value, row, index) {
                for (var i = 0; i < assumeSolutions.length; i++) {
                    if (assumeSolutions[i].solutionItemCode == value) {
                        return assumeSolutions[i].solutionItemName;
                    }
                }
                return value;
            }
        }, {
            title: '次级分摊方案',
            field: 'secondCalcWay',
            formatter: function (value, row, index) {
                for (var i = 0; i < assumeSolutions.length; i++) {
                    if (assumeSolutions[i].solutionItemCode == value) {
                        return assumeSolutions[i].solutionItemName;
                    }
                }
                return value;
            }
        }, {
            title: '人员分摊方案',
            field: 'personCalcWay',
            formatter: function (value, row, index) {
                for (var i = 0; i < assumeSolutions.length; i++) {
                    if (assumeSolutions[i].solutionItemCode == value) {
                        return assumeSolutions[i].solutionItemName;
                    }
                }
                return value;
            }
        }, {
            title: '获取方式',
            field: 'getWay',
            formatter: function (value, row, index) {
                for (var i = 0; i < getWays.length; i++) {
                    if (getWays[i].getWayCode == value) {
                        return getWays[i].getWayName;
                    }
                }
            }
        }, {
            title: '核算类型',
            field: "calcType",
            formatter: function (value, row, index) {
                if (value == '1') {
                    return '参与核算';
                }
                if (value == '2') {
                    return '不参与核算'
                }
                return value;
            }
        }, {
            title: "计入方式",
            field: 'costType',
            formatter: function (value, row, index) {
                if (value == '1') {
                    return '直接成本';
                }
                if (value == '2') {
                    return '间接成本'
                }
                return value;
            }
        }, {
            title: '计入百分比',
            field: "calcPercent"
        }, {
            title: "住院开单比例",
            field: 'inpOrderRate',
            editor: {type: 'validate'}
        }, {
            title: "住院执行比例",
            field: 'inpPerformRate'
        }, {
            title: '住院护理比例',
            field: 'inpWardRate'
        }, {
            title: '门诊开单比例',
            field: "outpOrderRate"
        }, {
            title: '门诊执行比例',
            field: 'outpPerformRate'
        }, {
            title: '门诊护理比例',
            field: 'outpWardRate'
        }]]
    });

    //项目类别
    $("#costItemClassId").combobox({
        valueField: 'id',
        textField: 'costItemClassName',
        method: 'GET',
        url: '/api/cost-item/list-item-class?hospitalId=' + parent.config.hospitalId,
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('setValue', data[0].id);
            }
        }
    });

    //成本属性
    $("#costItemAttr").combobox({
        valueField: 'attrCode',
        textField: 'attrName',
        method: 'GET',
        url: '/api/cost-attr/list-all',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('setValue', data[0].attrCode);
            }
        }
    });

    //获取方式
    $("#getWay").combobox({
        valueField: 'getWayCode',
        textField: 'getWayName',
        method: 'GET',
        url: '/api/cost-get-way/list-all',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('setValue', data[0].getWayCode);
            }
        }
    });

    //军地分摊方案
    $("#armyLocayCalcWay").combobox({
        valueField: 'solutionItemCode',
        textField: 'solutionItemName',
        method: 'GET',
        url: '/api/assume-solution-item/list-by-type?solutionId=402880415141920601514196bd52002f',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('setValue', data[0].solutionItemCode);
            }
        }
    });

    //刺激分摊方案
    $("#secondCalcWay").combobox({
        valueField: 'solutionItemCode',
        textField: 'solutionItemName',
        method: 'GET',
        url: '/api/assume-solution-item/list-by-type?solutionId=402880415141920601514196bd520030',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('setValue', data[0].solutionItemCode);
            }
        }
    });
    //核算类型
    $("#calcType").combobox({
        valueField: 'value',
        textField: 'text',
        method: 'GET',
        data: [{
            value: '1',
            text: '参与核算',
            selected: true
        }, {
            value: '2',
            text: '不参与核算'
        }]
    });

    //核算类型
    $("#costType").combobox({
        valueField: 'value',
        textField: 'text',
        data: [{
            value: '1',
            text: '直接成本',
            selected: true
        }, {
            value: '2',
            text: '间接成本'
        }]
    });
    //军地分摊方案
    $("#personCalcWay").combobox({
        valueField: 'solutionItemCode',
        textField: 'solutionItemName',
        method: 'GET',
        url: '/api/assume-solution-item/list-by-type?solutionId=402880415141920601514196bd520031',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('setValue', data[0].solutionItemCode);
            }
        }
    });

    //成本分类模态框
    $("#classWin").window({
        title: '成本分类维护',
        closed: true,
        modal: true
    });

    //成本项目维护模态框

    $("#itemWin").window({
        title: '成本项目维护',
        closed: true,
        modal: true,
        width: 650,
        height: 400,
        onClose: function () {
            $("#itemForm").form('reset');
        }
    });


    //添加分类按钮
    $("#addClassBtn").on('click', function () {
        $("#classWin").window('open');
    });

    //分类项目保存
    $("#saveClassBtn").on('click', function () {
        if (!$("#costItemClassCode").validatebox('isValid')) {
            $.messager.alert("系统提示", '请输入分类编码', 'info');
            return;
        }
        if (!$("#costItemClassName").validatebox('isValid')) {
            $.messager.alert("系统提示", '请输入分类编码', 'info');
            return;
        }

        var saveObj = {};
        saveObj.costItemClassName = $("#costItemClassName").val();
        saveObj.costItemClassCode = $("#costItemClassCode").val();
        console.log(parent.config)
        saveObj.hospitalId = parent.config.hospitalId;

        $.postJSON("/api/cost-item/save-item-class", saveObj, function (data) {
            $.messager.alert('系统提示', '成本分类添加成功');
            $("#costItemClassGrid").datagrid('reload');
            $("#classForm").form('reset');
        }, function (data) {
            $.messager.alert('系统提示', '保存失败', 'info');
        });
    });

    //删除分类项目
    $("#removeClassBtn").on('click', function () {
        var row = $("#costItemClassGrid").datagrid('getSelected');
        if (!row) {
            $.messager.alert("系统提示", '请选择要删除的项目', 'error');
            return;
        }
        //首先判断有没有子项目
        var promise = $.get("/api/cost-item/list-by-class?hospitalId=" + parent.config.hospitalId + "&classId=" + row.id, function (data) {
            if (data.length > 0) {
                $.messager.alert("系统提示", '存在该类别的成本项目，不允许删除！', 'error');
                return;
            }
            $.post("/api/cost-item/del-class?id=" + row.id, function (data) {
                $.messager.alert('系统提示', '删除成功', 'info');
                $("#costItemClassGrid").datagrid('reload');
            });
        })

    });

    //取消按钮
    $("#cancelClassBtn").on('click', function () {
        $("#classForm").form('reset');
        $("#classWin").window('close');
    })

    //加载数据
    $("#loadBtn").on('click', function () {
        var row = $("#costItemClassGrid").datagrid('getSelected');
        if (!row) {
            $("#costItemDictGrid").datagrid('reload');
        } else {
            var options = $("#costItemDictGrid").datagrid('options');
            options.url = "/api/cost-item/list-by-class?hospitalId=" + parent.config.hospitalId + "&classId=" + row.id;
            $("#costItemDictGrid").datagrid('reload');
        }
    });

    //添加项目
    $("#addBtn").on('click', function () {
        $("#itemWin").window('open');
    })

    //保存项目
    $("#saveItemBtn").on('click', function () {
        var obj = {};
        obj.costItemClassDict = {};
        obj.costItemClassDict.id = $('#costItemClassId').combobox('getValue');
        obj.costAttr = $("#costItemAttr").combobox('getValue');
        obj.calcType = $("#calcType").combobox('getValue');
        obj.costItemName = $("#costItemName").val();
        obj.costItemCode = $("#costItemCode").val();
        obj.armyLocayCalcWay = $("#armyLocayCalcWay").combobox('getValue');
        obj.secondCalcWay = $("#secondCalcWay").combobox('getValue');
        obj.personCalcWay = $("#personCalcWay").combobox('getValue');
        obj.getWay = $("#getWay").combobox('getValue');
        obj.costType = $("#costType").combobox('getValue');
        obj.calcPercent = $("#calcPercent").val();
        obj.inpOrderRate = $("#inpOrderRate").textbox('getValue');
        obj.inpPerformRate = $("#inpPerformRate").textbox('getValue');
        obj.inpWardRate = $("#inpWardRate").textbox('getValue');
        obj.outpOrderRate = $("#outpOrderRate").textbox('getValue');
        obj.outpPerformRate = $("#outpPerformRate").textbox('getValue');
        obj.outpWardRate = $("#outpWardRate").textbox('getValue');
        obj.hospitalId = parent.config.hospitalId;
        obj.id = $("#id").textbox('getValue');

        $.postJSON("/api/cost-item/save-item", obj, function (data) {
            $.messager.alert('系统提示', '保存成功', 'info');
            $("#itemWin").window('close');
            var options = $("#costItemDictGrid").datagrid('options');
            options.url = "/api/cost-item/list-by-class?hospitalId=" + parent.config.hospitalId + "&classId=" + obj.costItemClassDict.id;
            $("#costItemDictGrid").datagrid('reload');
        }, function (data) {
            $.messager.alert('系统提示', '保存失败', 'error');
        })
        console.log(obj);
    });


    $("#cancelItemBtn").on('click', function () {
        $("#itemWin").window('close');
    })

    $("#removeBtn").on('click', function () {

        var row = $("#costItemDictGrid").datagrid('getSelected');
        if (!row) {
            $.messager.alert('系统提示', '请选择要删除的项目', 'info');
            return;
        }

        $.post("/api/cost-item/del-cost-item?id=" + row.id, function (data) {
            $.messager.alert("系统提示", '删除成功', 'info');
            $("#costItemDictGrid").datagrid('reload');
        })
    });

    /**
     *编辑按钮
     *
     */
    $("#editBtn").on('click', function () {
        var row = $("#costItemDictGrid").datagrid('getSelected');
        if (!row) {
            $.messager.alert("系统提示", "请选择要编辑的记录", "info");
            return;
        }
        $('#costItemClassId').combobox('setValue', row.costItemClassDict.id);
        $("#costItemAttr").combobox('setValue', row.costAttr);
        $("#calcType").combobox('setValue', row.calcType);
        $("#costItemName").val(row.costItemName);
        $("#costItemCode").val(row.costItemCode);
        $("#armyLocayCalcWay").combobox('setValue', row.armyLocayCalcWay);
        $("#secondCalcWay").combobox('setValue', row.secondCalcWay);
        $("#personCalcWay").combobox('setValue', row.personCalcWay);
        $("#getWay").combobox('setValue', row.getWay);
        $("#costType").combobox('setValue', row.costType);
        $("#calcPercent").val(row.calcPercent);
        $("#inpOrderRate").textbox('setValue', row.inpOrderRate);
        $("#inpPerformRate").textbox('setValue', row.inpPerformRate);
        $("#inpWardRate").textbox('setValue', row.inpWardRate);
        $("#outpOrderRate").textbox('setValue', row.outpOrderRate);
        $("#outpPerformRate").textbox('setValue', row.outpPerformRate);
        $("#outpWardRate").textbox('setValue', row.outpWardRate);
        $("#id").textbox('setValue', row.id);


        console.log($("#id").textbox('getValue'))

        $("#itemWin").window('open');
    });


    $("#incomeTypeWindow").window({
        height: 500,
        width: 900,
        modal: true,
        title:'设置收入与成本对照',
        iconCls:'icon-man',
        closed: true,
        onOpen:function(){
            $(this).window('center');
        }
    });

    $("#addVsIncomeBtn").on('click',function(){

        var row = $("#costItemDictGrid").datagrid('getSelected');

        if(!row||row.getWay !='HQFS03'){
            $.messager.alert('系统提示','请选择获取方式为折算的项目','error') ;
            return ;
        }
        var options = $("#incomeTypeSelectdDg").datagrid('options') ;
        options.url ='/api/acct-reck/list-ok-vs-cost?hospitalId=' + parent.config.hospitalId+"&costId="+row.id ;
        $("#incomeTypeSelectdDg").datagrid('reload') ;
        $("#incomeTypeWaitDg").datagrid('reload') ;
        $("#incomeTypeWindow").window('open') ;
    });


    $("#incomeTypeWaitDg").datagrid({
        fit:true,
        fitColumns: true,
        striped: true,
        singleSelect: false,
        url: '/api/acct-reck/list-no-vs-cost?hospitalId=' + parent.config.hospitalId,
        method: 'GET',
        rownumbers: true,
        loadMsg: '数据正在加载，请稍后......',
        columns: [[{
            title: 'id',
            field: 'id',
            checkbox: true
        }, {
            title: '核算科目名称',
            field: 'reckItemName'
        }, {
            title: '核算科目代码',
            field: 'reckItemCode'
        }, {
            title: "收入项目拼音码",
            field: 'inputCode',
            editor: {type: 'textbox', options: {}}
        },  {
            title: '固定折算',
            field: 'fixConvert'
        }]]
    })

    $("#incomeTypeSelectdDg").datagrid({
        fit:true,
        fitColumns: true,
        region:'east',
        striped: true,
        singleSelect: false,

        method: 'GET',
        rownumbers: true,
        loadMsg: '数据正在加载，请稍后......',
        columns: [[{
            title: 'id',
            field: 'id',
            checkbox: true
        }, {
            title: '核算科目名称',
            field: 'reckItemName'
        }, {
            title: '核算科目代码',
            field: 'reckItemCode'
        }, {
            title: "收入项目拼音码",
            field: 'inputCode',
            editor: {type: 'textbox', options: {}}
        },  {
            title: '固定折算',
            field: 'fixConvert'
        }]]
    }) ;


    var updateAcctReckClassDict = function(acctReckClassDict){

        $.postJSON("/api/acct-reck/update-costId",acctReckClassDict,function(data){
            $.messager.alert('系统提示','添加成功',"info") ;
            var row = $("#costItemDictGrid").datagrid('getSelected');

            if(!row){
                $.messager.alert('系统提示','请选择要设置的成本项目') ;
                return ;
            }
            var options = $("#incomeTypeSelectdDg").datagrid('options') ;
            options.url ='/api/acct-reck/list-ok-vs-cost?hospitalId=' + parent.config.hospitalId+"&costId="+row.id ;
            $("#incomeTypeSelectdDg").datagrid('reload') ;
            $("#incomeTypeWaitDg").datagrid('reload') ;
        },function(data){
            $.messager.alert("系统提示","添加失败",'error') ;
            var row = $("#costItemDictGrid").datagrid('getSelected');

            if(!row){
                $.messager.alert('系统提示','请选择要设置的成本项目') ;
                return ;
            }
            var options = $("#incomeTypeSelectdDg").datagrid('options') ;
            options.url ='/api/acct-reck/list-ok-vs-cost?hospitalId=' + parent.config.hospitalId+"&costId="+row.id ;
            $("#incomeTypeSelectdDg").datagrid('reload') ;
            $("#incomeTypeWaitDg").datagrid('reload') ;
        })
    }

    $("#rightAllBtn").on('click',function(){
        var row = $("#costItemDictGrid").datagrid('getSelected');
        if(!row){
            $.messager.alert("系统提示","获取成本项目失败",'error') ;
            return ;
        }
        var rows =$("#incomeTypeWaitDg").datagrid('getRows') ;
        if(rows.length>0){
            for(var i = 0 ;i<rows.length;i++){
                rows[i].costId =row.id ;
            }
            updateAcctReckClassDict(rows) ;
        }else{
            $.messager.alert('系统提示','没有保存的项目','info') ;
        }
    }) ;
    $("#rightBtn").on('click',function(){
        var row = $("#costItemDictGrid").datagrid('getSelected');
        if(!row){
            $.messager.alert("系统提示","获取成本项目失败",'error') ;
            return ;
        }
        var rows =$("#incomeTypeWaitDg").datagrid('getSelections') ;
        if(rows.length>0){
            for(var i = 0 ;i<rows.length;i++){
                rows[i].costId =row.id ;
            }
            updateAcctReckClassDict(rows) ;
        }else{
            $.messager.alert('系统提示','没有保存的项目','info') ;
        }
    }) ;
    $("#leftAllBtn").on('click',function(){

        var rows =$("#incomeTypeSelectdDg").datagrid('getRows') ;
        if(rows.length>0){
            for(var i = 0 ;i<rows.length;i++){
                rows[i].costId ="";
            }
            updateAcctReckClassDict(rows) ;
        }else{
            $.messager.alert('系统提示','没有保存的项目','info') ;
        }
    }) ;

    $("#leftBtn").on('click',function(){

        var rows =$("#incomeTypeSelectdDg").datagrid('getSelections') ;
        if(rows.length>0){
            for(var i = 0 ;i<rows.length;i++){
                rows[i].costId ="";
            }
            updateAcctReckClassDict(rows) ;
        }else{
            $.messager.alert('系统提示','没有保存的项目','info') ;
        }
    }) ;


    //设置分摊项目的对照科室
    $("#acctDeptWin").window({
        title:'分摊项目设置',
        width:'500',
        height:'500',
        modal:true,
        closed:true,
        onOpen:function(){
            $(this).window('center');
            var row = $('#costItemDictGrid').datagrid('getSelected');
            console.log(row) ;
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
        }
    }) ;

    $("#acctDeptTable").datagrid({
        method:'GET',
        fit:true,
        fitColumns:true,
        url:'/api/acct-dept-dict/acct-list?hospitalId='+parent.config.hospitalId,
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

    $("#setDevideBtn").on('click',function(){
        var row = $('#costItemDictGrid').datagrid('getSelected')
        if(!row||row.getWay!='HQFS04'){//如果没有选择或者不是获取方式不是分摊的话
            $.messager.alert('系统提示','没有选择项目，或者选择项目非分摊类型的数据','error')
            return ;
        }
        $("#acctDeptWin").window('open') ;
    }) ;

    $("#saveAcctDeptBtn").on('click',function(){
        var row = $('#costItemDictGrid').datagrid('getSelected')
        var rows = $("#acctDeptTable").datagrid('getSelections') ;
        var costDevide=[] ;

        for(var i=0 ;i<rows.length;i++){
            var cs = {};
            cs.costItemId = row.id ;
            cs.deptId=rows[i].id ;
            costDevide.push(cs) ;
        }

        $.postJSON("/api/cost-item/save-devide",costDevide,function(data){
            $.messager.alert('系统提示','保存成功','info') ;
            $("#acctDeptWin").window('close') ;
        },function(data){})
    })

    $("#cancelAcctDeptBtn").on('click',function(e){
        $("#acctDeptWin").window('close') ;
    }) ;
})