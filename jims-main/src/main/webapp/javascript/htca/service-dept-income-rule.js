/**
 * Created by heren on 2015/12/2.
 */
$(function () {
    $("#ruleParamTable").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        url: '/api/service-dept-income-rule/list?hospitalId=' + parent.config.hospitalId,
        toolbar: '#ft',
        method: 'GET',
        loadMsg: '数据正在加载中，请稍后.....',
        columns: [[{
            title: 'id',
            field: 'id',
            hidden: true
        }, {
            title: '规则名称',
            field: 'ruleName',
            width: '20%'
        }, {
            title: 'SQL',
            field: 'ruleSql',
            width: '80%'
        }, {
            title: '使用核算单元',
            field: 'depts'
        }]]
    });

    $("#paramWin").window({
        closed: true,
        modal: true,
        onOpen: function () {
            $(this).window("center");
        },
        onClose: function () {
            $('#itemForm').form('reset');
        }
    });


    $("#addBtn").on('click', function () {
        $("#paramWin").window("open");
    })

    //确定保存参数
    $("#okBtn").on('click', function () {
        var obj = {};
        obj.id = $("#id").textbox('getValue');
        obj.ruleName = $("#ruleName").textbox('getValue');
        obj.ruleSql = $("#ruleSql").val();
        obj.hospitalId = parent.config.hospitalId;
        $.postJSON("/api/service-dept-income-rule/save", obj, function (data) {
            $.messager.alert("系统提示", '保存成功', 'info');
            $("#ruleParamTable").datagrid('reload');
            $("#paramWin").window('close');
        }, function (data) {
            $.messager.alert('系统提示', '保存失败', 'error');
        })
    })

    $("#noBtn").on('click', function () {
        $("#paramWin").window('close');
    });

    //编辑
    $("#editBtn").on('click', function () {
        var row = $("#ruleParamTable").datagrid('getSelected');
        if (!row) {
            $.messager.alert('系统提示', '请选择要编辑的行', 'error');
            return;
        }

        $("#id").textbox('setValue', row.id);
        $("#ruleName").textbox('setValue', row.ruleName);
        $("#ruleSql").val(row.ruleSql);
        $("#paramWin").window('open');

    })
    //删除一个参数
    $("#removeBtn").on('click', function () {
        var row = $("#ruleParamTable").datagrid('getSelected');
        if (!row) {
            $.messager.alert('系统提示', '请选择要编辑的行', 'error');
            return;
        }

        if (!row.id) {
            $.messager.alert('系统提示', '获取关键数据失败', 'error');
            return;
        }

        $.post("/api/service-dept-income-rule/del?id=" + row.id, function () {
            $.messager.alert('系统提示', '删除成功', 'info');
            $("#ruleParamTable").datagrid('reload');
        })
    })


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
                obj.costAppInd = item.costAppInd;
                obj.costAppLevel = item.costAppLevel;
                obj.deptStopFlag = item.deptStopFlag;
                obj.hospitalId = item.hospitalId;
                obj.parentId = item.parentId;
                obj.deptType = item.deptType;
                obj.deptClass = item.deptClass;
                obj.endDept = item.endDept;
                obj.buildArea = item.buildArea;
                obj.staffNum = item.staffNum;
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
        singleSelect: false,
        idField: 'id',
        treeField: 'deptName',
        footer: '#deptFt',
        columns: [[{
            title: '编号',
            field: 'id',
            checkbox: true
        }, {
            title: '科室编码',
            field: 'deptCode',
            width: '10%'
        }, {
            title: '科室名称',
            field: 'deptName',
            width: '30%'
        }, {
            title: '门诊住院',
            field: 'deptOutpInp',
            width: '10%',
            formatter: function (value, rowIdex, row) {
                if (value == '0') {
                    return '门诊';
                }
                if (value == 1) {
                    return '住院'
                }
                return value;
            }
        }, {
            title: '输入码',
            field: 'inputCode',
            width: '10%'
        }, {
            title: '科室类型',
            field: 'deptType',
            width: '10%',
            formatter: function (value, rowIndex, row) {
                if (value == '0') {
                    return '直接医疗类科室'
                }
                if (value == '1') {
                    return '医疗技术类科室'
                }
                if (value == '2') {
                    return '医疗辅助类科室'
                }
                if (value == '3') {
                    return '管理类科室'
                }
                if (value == '4') {
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
            formatter: function (value, rowIdex, row) {
                if (value == '0') {
                    return '否';
                }
                if (value == 1) {
                    return '是'
                }
                return value;
            }
        }, {
            title: '分摊级别',
            field: 'costAppLevel',
            width: '10%',
            formatter: function (value, rowIndex, row) {
                if (value == '5') {
                    return '直接医疗类科室'
                }
                if (value == '4') {
                    return '医疗技术类科室'
                }
                if (value == '3') {
                    return '医疗辅助类科室'
                }
                if (value == '2') {
                    return '管理类科室'
                }
                if (value == '1') {
                    return '未纳入科室'
                }
            }
        }, {
            title: "使用面积",
            field: 'buildArea'
        }, {
            title: '在编人数',
            field: 'staffNum'
        }]]
    });

    $("#acctDeptWin").window({
        title: '设置核算单元',
        width: 700,
        height: 400,
        modal: true,
        closed: true,
        onOpen: function () {
            $(this).window('center');
            var row = $("#ruleParamTable").datagrid('getSelected');
            console.log(row) ;
            if (row.depts&&row.depts.length > 0) {
                var depts = row.depts.split(";");
                for(var i=0;i<depts.length;i++){
                    $("#acctDeptDataGrid").treegrid('select',depts[i]) ;
                }
            }
        },
        onClose:function(){
            $("#acctDeptDataGrid").treegrid('unselectAll') ;
        }
    })

    $("#setDeptBtn").on('click', function () {
        var row = $("#ruleParamTable").datagrid('getSelected');
        if (!row) {
            $.messager.alert("系统提示", '请选择要设置的项目', 'error');
            return;
        }
        $("#acctDeptWin").window('open');
    })

    $("#checkBtn").on('click', function () {
        var rows = $("#acctDeptDataGrid").treegrid('getSelections');
        if (rows.length <= 0) {
            $.messager.alert("系统提示", "请选择要选择的项目", "error");
            return;
        }
        var row = $("#ruleParamTable").datagrid('getSelected');
        row.depts = '';
        for (var i = 0; i < rows.length; i++) {
            row.depts += rows[i].id + ";"
        }

        if (row.depts.length > 0) {
            row.depts = row.depts.substr(0, row.depts.length - 1)
        }

        $.postJSON("/api/service-dept-income-rule/save", row, function (data) {
            $.messager.alert("系统提示", '保存成功', 'info');
            $("#ruleParamTable").datagrid('reload');
            $("#acctDeptWin").window('close');
        }, function (data) {
            $.messager.alert('系统提示', '保存失败', 'error');
        })


    })

    $("#unCheckBtn").on('click',function(){
        $("#acctDeptWin").window('close') ;
    }) ;
})