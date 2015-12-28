/**
 * Created by heren on 2015/10/27.
 */
/**
 * 同步核算科室
 * 按道理每月同步一次
 */
$(function () {

    var rowValue = undefined;

    $("#acctDeptStaffWindow").window({
        width: '500',
        height: '450',
        title: '核算组分配',
        modal: true,
        iconCls: 'icon-save',
        closed: true,
        footer: '#acctDeptVsDeptDictFt'
    });

    $("#acctDeptStaffWindow").window('center');

    $("#acctDeptFormWindow").window({
        width: '330',
        height: '400',
        title: '核算单元维护',
        modal: true,
        closed: true,
        footer: '#winFt'
    });

    $("#acctDeptFormWindow").window('center');


    $.get("/api/acct-dept-dict/check-syn?hospitalId=" + parent.config.hospitalId, function (data) {
        if (data) {
            $("#synBtn").linkbutton('disable')
        }
    });


    //添加按钮单击事件
    $("#addBtn").on('click', function () {
        $("#acctDeptFormWindow").window('open');
    });

    /**
     * 加载医院信息表
     */
    var loadAcctDept = function () {
        var depts = [];
        var treeDepts = [];
        var loadPromise = $.get("/api/acct-dept-dict/acct-list?hospitalId=" + parent.config.hospitalId, function (data) {
            $.each(data, function (index, item) {
                var obj = {};
                obj.deptCode = item.deptCode;
                obj.id = item.id;
                obj.deptName = item.deptName;
                obj.deptAlis = item.deptAlis;
                obj.deptAttr = item.deptAttr;
                obj.deptOutpInp = item.deptOutpInp;
                obj.inputCode = item.inputCode;
                obj.deptDevideAttr = item.deptDevideAttr;
                obj.deptLocation = item.deptLocation;
                obj.costAppInd = item.costAppInd ;
                obj.costAppLevel = item.costAppLevel ;
                obj.deptStopFlag = item.deptStopFlag;
                obj.hospitalId = item.hospitalId;
                obj.parentId = item.parentId;
                obj.deptType = item.deptType;
                obj.deptClass = item.deptClass;
                obj.endDept = item.endDept;
                obj.buildArea = item.buildArea ;
                obj.staffNum = item.staffNum ;
                obj.children = [];
                depts.push(obj);
            });

        });


        loadPromise.done(function () {
            for (var i = 0; i < depts.length; i++) {
                for (var j = 0; j < depts.length; j++) {
                    if (depts[i].id == depts[j].parentId) {
                        depts[i].children.push(depts[j]);
                    }
                }
                if (depts[i].children.length > 0 && !depts[i].parentId) {
                    treeDepts.push(depts[i]);
                }

                if (!depts[i].parentId && depts[i].children <= 0) {
                    treeDepts.push(depts[i])
                }
            }
            $("#acctDeptDataGrid").treegrid('loadData', treeDepts);
        })
    }

    loadAcctDept();
    /**
     * 同步表格
     */
    $("#acctDeptDataGrid").treegrid({
        fitColumns: true,
        fit: true,
        singleSelect: true,
        idField: 'deptCode',
        treeField: 'deptName',
        toolbar: '#ft',
        columns: [[{
            title: '编号',
            field: 'id',
            hidden: true
        }, {
            title: '科室编码',
            field: 'deptCode',
            width: '10%'
        }, {
            title: '科室名称',
            field: 'deptName',
            width: '30%'
        }, {
            title:"使用面积",
            field:'buildArea'
        },{
            title:'在编人数',
            field:'staffNum'
        },{
            title: '门诊住院',
            field: 'deptOutpInp',
            width: '10%',
            formatter:function(value,rowIdex,row){
                if(value=='0'){
                    return '门诊';
                }
                if(value==1){
                    return '住院'
                }
                return value ;
            }
        }, {
            title: '科室类型',
            field: 'deptType',
            width: '10%',
            formatter:function(value,rowIndex,row){
                if(value=='0'){
                    return '直接医疗类科室'
                }
                if(value=='1'){
                    return '医疗技术类科室'
                }
                if(value=='2'){
                    return '医疗辅助类科室'
                }
                if(value=='3'){
                    return '管理类科室'
                }
                if(value=='4'){
                    return '未纳入科室'
                }
            }
        }, {
            title: '科室类别',
            field: 'deptClass',
            width: '10%'
        }, {
            title: '是否参与分摊',
            field: 'costAppInd',
            width: '10%',
            formatter:function(value,rowIdex,row){
                if(value=='0'){
                    return '否';
                }
                if(value==1){
                    return '是'
                }
                return value ;
            }
        }, {
            title: '分摊级别',
            field: 'costAppLevel',
            width: '10%',
            formatter:function(value,rowIndex,row){
                if(value=='5'){
                    return '直接医疗类科室'
                }
                if(value=='4'){
                    return '医疗技术类科室'
                }
                if(value=='3'){
                    return '医疗辅助类科室'
                }
                if(value=='2'){
                    return '管理类科室'
                }
                if(value=='1'){
                    return '未纳入科室'
                }
            }
        }, {
            title: '输入码',
            field: 'inputCode',
            width: '10%'
        }]],
        onDblClickRow: function (row) {
            $("#deptDictTable").datagrid('reload') ;
            $("#acctDeptStaffWindow").window('open');
            rowValue = row;
        },
        onClickRow:function(row){
            rowValue = row;
            var options = $("#acctVsDatagrid").datagrid('options') ;
            options.url="/api/dept-dict/list-width-recked-by-acct?hospitalId=" + parent.config.hospitalId+"&acctDeptId="+row.id ;
            $("#acctVsDatagrid").datagrid('reload') ;
        },
        onContextMenu:function(e,row){
            e.preventDefault() ;
            $("#rightMenu").menu('show',{
                left: e.pageX ,
                top: e.pageY,
                onClick:function(item){
                    if(item.id=='modifyDeptMenu'){
                        console.log(row) ;
                        $("#deptName").textbox('setValue',row.deptName);
                        $("#deptCode").textbox('setValue',row.deptCode);
                        $("#inpOrOutp").combobox('setValue',row.deptOutpInp);
                        $("#deptType").combobox('setValue',row.deptType);
                        $("#costAppInd").combobox('setValue',row.costAppInd);
                        $("#costAppLevel").combobox('setValue',row.costAppLevel);
                        $("#parentId").textbox('setValue',row.parentId);
                        $("#id").textbox('setValue',row.id) ;
                        $("#buildArea").textbox('setValue',row.buildArea) ;
                        $("#staffNum").textbox('setValue',row.staffNum);
                        $("#acctDeptFormWindow").window('open') ;
                    }
                    if(item.id=='modifyDeptVs'){
                        $("#deptDictTable").datagrid('reload') ;
                        $("#acctDeptStaffWindow").window('open');
                        rowValue = row;
                    }
                }
            }) ;
        }
    });

    $("#synBtn").on('click', function () {
        $.get("/api/acct-dept-dict/check-syn?hospitalId=" + parent.config.hospitalId, function (data) {
            if (data) {
                return;
            } else {
                loadAcctDept();
            }
        });
    });

    /**
     * 保存数据
     */
    $("#saveBtn").on('click', function (e) {
        var acctDeptDict = [];
        var rows = $("#acctDeptDataGrid").datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            var acc = {};
            acc.deptCode = rows[i].deptCode
            acc.deptName = rows[i].deptName
            acc.deptAttr = rows[i].deptAttr
            acc.deptOutpInp = rows[i].deptOutpInp
            acc.inputCode = rows[i].inputCode
            acc.deptDevideAttr = rows[i].deptDevideAttr
            acc.deptLocation = rows[i].deptLocation
            acc.deptOther = rows[i].deptOther
            acc.deptStopFlag = rows[i].deptStopFlag
            acc.hospitalId = rows[i].hospitalId
            acc.costAppInd = rows[i].costAppInd
            acc.costAppLevel = rows[i].costAppLevel
            acc.deptType = rows[i].deptType
            acc.synDate = rows[i].synDate
            acc.deptClass = rows[i].deptClass//科室类别一般分为经营科室和其他
            acc.endDept = rows[i].endDept;
            acc.costAppInd = rows[i].costAppInd;
            acc.costAppLevel = rows[i].costAppLevel;
            acc.buildArea = rows[i].buildArea ;
            acc.staffNum = rows[i].staffNum ;
            acc.endDept = rows[i].endDept
            acc.delFlag = '1' ;
            acctDeptDict.push(acc);
        }
        console.log(acctDeptDict);

        $.postJSON("/api/acct-dept-dict/save-syn", acctDeptDict, function (data) {
            loadAcctDept();
            $.messager.alert("系统提示", "同步成功", "info");
        }, function (errorData) {
            loadAcctDept();
            $.messager.alert("系统提示", errorData.responseText);
        });


    })


    $("#removeBtn").on('click', function (e) {
        var row = $("#acctDeptDataGrid").datagrid('getSelected');
        if (row) {
            $.post("/api/acct-dept-dict/del/" + row.id, function (data) {
                loadAcctDept();
                $.messager.alert("系统提示", "删除成功", 'info');
            })
        } else {
            $.messager.alert("系统提示", "请选择要删除的记录", 'info');
        }
    });


    //添加核算单元
    $("#addAcctBtn").on('click', function () {
        var acctDeptDict = {};

        acctDeptDict.deptName = $("#deptName").textbox('getValue');
        acctDeptDict.deptCode = $("#deptCode").textbox('getValue');
        acctDeptDict.deptOutpInp = $("#inpOrOutp").combobox('getValue');
        acctDeptDict.deptType = $("#deptType").combobox('getValue');
        acctDeptDict.costAppInd = $("#costAppInd").combobox('getValue');
        acctDeptDict.costAppLevel = $("#costAppLevel").combobox('getValue');
        acctDeptDict.hospitalId = parent.config.hospitalId;
        acctDeptDict.parentId = $("#parentId").textbox('getValue');
        acctDeptDict.id =$("#id").textbox('getValue') ;
        acctDeptDict.buildArea = $("#buildArea").textbox('getValue');
        acctDeptDict.staffNum= $("#staffNum").textbox('getValue');
        acctDeptDict.endDept = $("#endDept").combobox('getValue') ;
        acctDeptDict.delFlag = '1';

        if (!acctDeptDict.deptName || !acctDeptDict.deptCode) {
            $.messager.alert("系统提示", "请完善核算单元信息，核算单元名称与编码不允许为空！");
            return;
        } else {
            $.postJSON("/api/acct-dept-dict/save-acct", acctDeptDict, function (data) {
                $.messager.alert('体统提示', "添加核算单元完成", 'info');
                $("#acctDeptFormWindow").window('close');
                $("#parentId").textbox('clear');
                loadAcctDept();
            }, function (data) {
                loadAcctDept();
                console.log(data);
            })
        }
    });


    $("#deptDictTable").datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        singleSelect: true,
        url: "/api/dept-dict/list-with-recking?hospitalId=" + parent.config.hospitalId,
        method: 'GET',
        singleSelect: false,
        loadMsg: '正在加载数据，请稍等......',
        columns: [[{
            text: 'id',
            field: 'id',
            checkbox: 'true'
        }, {
            text: '科室代码',
            field: 'deptCode',
            width: '40%'
        }, {
            text: '科室名称',
            field: 'deptName',
            width: '50%'
        }]]
    });



    $("#accDeptVsAddBtn").on('click', function () {
        if (!rowValue) {
            $.messager.alert('系统提示', '选择了无效的核算组', 'error');
            return;
        } else {
            var selectedRows = $("#deptDictTable").datagrid('getSelections');
            if (!selectedRows) {
                $.messager.confirm('系统提示', '没有选择任何对照科室，是否关闭？', function (data) {
                    if (data == 1) {
                        $("#acctDeptStaffWindow").window('close');
                    }
                });
            } else {
                var acctVsDepts = [];

                for (var i = 0; i < selectedRows.length; i++) {
                    var acct = {};
                    acct.acctDeptId = rowValue.id;
                    acct.deptDictId = selectedRows[i].id;
                    acctVsDepts.push(acct);
                }

                $.postJSON("/api/acct-dept-dict/save-acct-vs-dept", acctVsDepts, function (data) {
                    $.messager.alert('系统提示', '核算科室与科室对照成功！', 'info');
                    $("#acctDeptStaffWindow").window('close');
                    var options = $("#acctVsDatagrid").datagrid('options') ;
                    options.url="/api/dept-dict/list-width-recked-by-acct?hospitalId=" + parent.config.hospitalId+"&acctDeptId="+rowValue.id ;
                    $("#acctVsDatagrid").datagrid('reload') ;
                }, function (data) {
                    $.messager.alert('系统提示', '核算科室与科室对照保存失败', 'error');
                    var options = $("#acctVsDatagrid").datagrid('options') ;
                    options.url="/api/dept-dict/list-width-recked-by-acct?hospitalId=" + parent.config.hospitalId+"&acctDeptId="+rowValue.id ;
                    $("#acctVsDatagrid").datagrid('reload') ;
                })
            }
        }

    });

    //添加下级核算科室
    $("#addNextBtn").on('click', function () {
        var row = $("#acctDeptDataGrid").treegrid('getSelected');
        if (!row) {
            $.messager.alert("系统提示", "请选择要添加下级核算科室的父级核算科室");
            return;
        } else {
            $("#parentId").textbox('setValue', row.id);
            $("#acctDeptFormWindow").window('open');
        }
    })


    //已经对照的科室
    $("#acctVsDatagrid").datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        singleSelect: true,
        method: 'GET',
        singleSelect: false,
        toolbar:'#footBar',
        columns: [[{
            text: 'id',
            field: 'id',
            checkbox: 'true'
        }, {
            text: '科室代码',
            field: 'deptCode',
            width: '40%'
        }, {
            text: '科室名称',
            field: 'deptName',
            width: '50%'
        }]]
    }) ;

    //取消对照
    $("#cancleVsBtn").on('click',function(){
        var rows = $("#acctVsDatagrid").datagrid('getSelections') ;
        var acctVsDept=[] ;
        for(var i = 0 ;i<rows.length ;i++){
            var acctvs = {} ;
            acctvs.deptDictId = rows[i].id ;
            acctvs.acctDeptId = rowValue.id ;
            acctVsDept.push(acctvs) ;
        }
        $.postJSON("/api/acct-dept-dict/del-vs",acctVsDept,function(data){
            $.messager.alert('系统提示','已经取消对照','info');
            var options = $("#acctVsDatagrid").datagrid('options') ;
            options.url="/api/dept-dict/list-width-recked-by-acct?hospitalId=" + parent.config.hospitalId+"&acctDeptId="+rowValue.id ;
            $("#acctVsDatagrid").datagrid('reload') ;
        })

    })
})