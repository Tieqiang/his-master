/**
 * 科室单项成本录入功能
 * Created by heren on 2015/11/30.
 */
$(function () {
    var acctDeptDict = [];

    var acctProfitVo = {} ;//保存项目VO
    acctProfitVo.acctProfitChangeRecordBeanChangeVo={} ;
    acctProfitVo.acctProfitChangeRecordBeanChangeVo.inserted=[] ;
    acctProfitVo.acctProfitChangeRecordBeanChangeVo.updated = [] ;
    acctProfitVo.acctProfitChangeRecordBeanChangeVo.deleted=[] ;


    $.get("/api/acct-dept-dict/acct-list?hospitalId=" + parent.config.hospitalId, function (data) {
        acctDeptDict = data;
    });
    var loadAcctProfitChangeDict = [];
    $.get("/api/acct-profit-change-dict/list?hospitalId=" + parent.config.hospitalId, function (data) {
        loadAcctProfitChangeDict = data;
    });
    //入库分类字典
    $("#changeItemId").combobox({
        valueField: 'id',
        textField: 'changeItemName'
    });
    var editRow = undefined;
    var p = $('#fetchDate').datebox('panel');//日期选择对象
    var tds = false; //日期选择对象中月份
    var span = p.find('span.calendar-text'); //显示月份层的触发控件
    $("#fetchDate").datebox({
        onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
            span.trigger('click'); //触发click事件弹出月份层
            if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                tds = p.find('div.calendar-menu-month-inner td');
                tds.click(function (e) {
                    $(this).addClass("calendar-selected")
                    e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
                    var year = /\d{4}/.exec(span.html())[0]//得到年份
                    var month = parseInt($(this).attr('abbr'), 10) + 1; //月份
                    $("#fetchDate").datebox('hidePanel').datebox('setValue', year + "-" + month)
                });
            }, 0)
        },
        parser: function (s) {//配置parser，返回选择的日期
            if (!s) return new Date();
            var arr = s.split('-');
            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
        },
        formatter: function (d) {
            var year = d.getFullYear();
            var month = d.getMonth();
            if (month == 0) {
                month = 12;
                year = year - 1;
            }
            if (month < 10) {
                return year + '-0' + month;
            }
            return year + '-' + month;

        }//配置formatter，只返回年月
    });


    $("#acctDeptProfitDg").datagrid({
        fit: true,
        fitColumns: true,
        method: 'GET',
        checkOnSelect: true,
        striped: true,
        singleSelect: false,
        toolbar: '#ft',
        method: 'GET',
        rownumbers: true,
        loadMsg: '数据正在加载，请稍后......',
        columns: [[{
            title: 'id',
            field: 'id',
            //checkbox: true
            hidden: true
        }, {
            title: '核算单元',
            field: 'acctDeptId',
            width: '10%',
            formatter: function (value, row, index) {
                for (var i = 0; i < acctDeptDict.length; i++) {
                    if (value == acctDeptDict[i].id) {
                        return acctDeptDict[i].deptName;
                    }
                }
                return value;
            }
        }, {
            title: '收入',
            field: 'deptIncome',
            width: '5%'
        }, {
            title: '成本',
            field: 'deptCost',
            width: '5%'
        }, {
            title: '收入调整',
            field: 'incomeChangeItem',
            width: '5%'
        }, {
            title: '成本调整',
            field: 'costChangeItem',
            width: '5%'
        }, {
            title: '分摊前利润',
            field: 'acctBalance',
            width: '10%'
        }, {
            title: '人员分摊',
            field: 'managerStaffCost',
            width: '10%'
        }, {
            title: '效益分摊',
            field: 'managerProfitCost',
            width: '10%'
        }, {
            title: '绩效提成比例',
            field: 'convertRate',
            editor: {
                type: 'validatebox', options: {
                    validType: 'number'
                }
            },
            width: '6%'
        }, {
            title: '质量达标率',
            field: 'pleasedNum',
            width: '5%',
            editor: {
                type: 'validatebox', options: {
                    validType: 'number'
                }
            }
        }, {
            title: '专项奖',
            field: 'specialIncome',
            width: '10%',
            editor: {
                type: 'validatebox',
                options: {
                    validType: 'number'
                }
            }
        }, {
            title: '综合业绩奖',
            field: 'allRoundIncome',
            width: '10%',
            editor: {
                type: 'validatebox',
                options: {
                    validType: 'number'
                }
            }
        }, {
            title: '奖金额度',
            field: 'deptLastIncome',
            width: '20%'
        }]],
        onDblClickRow: function (rowIndex, rowData) {
            if (editRow == 0 || editRow) {
                stopEdit();
            }
            editRow = rowIndex;
            $(this).datagrid('checkRow', rowIndex);
            beginEdit();
        },
        onAfterEdit: function (index, rowData, changes) {
            rowData.deptLastIncome = rowData.specialIncome * 1 + ((rowData.deptIncome - rowData.deptCost) * rowData.pleasedNum / 100 * rowData.convertRate / 100 );
            rowData.deptLastIncome = rowData.deptLastIncome.toFixed(2);
            $(this).datagrid('updateRow', {
                index: index,
                row: rowData
            });
        }, onRowContextMenu: onRowContextMenu
    });
    $("#acctDeptFormWindow").window({
        width: '330',
        height: '350',
        title: '调整收益',
        modal: true,
        closed: true,
        footer: '#winFt'
    });
    $("#acctDeptFormWindow").window('center');
    var profitChangeRecord = {};
    var profitChangeDict = {};
    var changeIndex;
    var yearMonthAll;
    var incomeOrCostAll;
    var acctDeptIdAll;

//添加右击菜单内容
    function onRowContextMenu(e, rowIndex, rowData) {
        e.preventDefault();
        var selected = $("#acctDeptProfitDg").datagrid('getRows'); //获取所有行集合对象
        var row = selected[rowIndex]; //index为当前右键行的索引，指向当前行对象
        $('#rightMenu').menu('show', {
            left: e.pageX,
            top: e.pageY,
            onClick: function (item) {
                changeIndex = rowIndex;
                $("#changeAmount").numberbox('clear');
                $("#changeReason").textbox('clear');
                $("#changeItemId").combobox('loadData', loadAcctProfitChangeDict);
                //$("#changeItemId").textbox('setValue', row.id);  //调整项目编码
                $("#yearMonth").textbox('setValue', $("#fetchDate").datebox('getValue'));   //调整月份
                $("#operator").textbox('setValue', parent.config.loginId);//操作者
                $("#operatorDate").textbox('setValue', setDefaultDate());//调整日期
                $("#acctDeptId").textbox('setValue', row.acctDeptId);//调整核算单元编码
                acctDeptIdAll = row.acctDeptId;
                //$("#id").textbox('setValue', rowIndex);//调整核算单元编码
                if (item.id == 'modifyDeptMenu' || item.id == 'modifyIncomeChange') {  //收入调整
                    $("#incomeOrCost").combobox('setValue', "1");// 调整项目
                    incomeOrCostAll = 1;
                }
                if (item.id == 'modifyDeptVs' || item.id == 'modifyCostChange') {    //成本调整
                    $("#incomeOrCost").combobox('setValue', "0");// 调整项目

                }
                if (item.id == 'modifyDeptMenu' || item.id == 'modifyDeptVs') {
                    $("#acctDeptFormWindow").window('open');
                }
                if (item.id == 'modifyIncomeChange' || item.id == 'modifyCostChange') {
                    flag = 0
                    $("#modifyChangeWindow").dialog('open');
                }
            }
        });
    }

    var changeProfit = new Array;
    $("#clearBtn").on('click', function () {
        var change = {};
        change.changeAmount = $("#changeAmount").numberbox('getValue');
        change.changeReason = $("#changeReason").textbox('getValue');
        change.changeItemId = $("#changeItemId").textbox('getValue');
        change.yearMonth = $("#yearMonth").textbox('getValue');
        change.operator = $("#operator").textbox('getValue');
        change.operatorDate = new Date($("#operatorDate").textbox('getValue'));
        change.acctDeptId = $("#acctDeptId").textbox('getValue');
        change.incomeOrCost = $("#incomeOrCost").combobox('getValue');
        var row = $('#acctDeptProfitDg').datagrid('getData').rows[changeIndex];

        if(!row.incomeChangeItem){
            row.incomeChangeItem = 0 ;
        }
        if(!row.costChangeItem){
            row.costChangeItem = 0 ;
        }
        var number = parseFloat(row.incomeChangeItem) + parseFloat(change.changeAmount);
        var number1 = parseFloat(row.costChangeItem) + parseFloat(change.changeAmount);
        if (change.incomeOrCost == "1") {
            $('#acctDeptProfitDg').datagrid('updateRow', {
                index: changeIndex,
                row: {
                    incomeChangeItem: number
                }
            });
        }
        if (change.incomeOrCost == "0") {
            console.log(number1) ;
            $('#acctDeptProfitDg').datagrid('updateRow', {
                index: changeIndex,
                row: {
                    costChangeItem: number1
                }
            });
        }
        if ($.trim(change.changeAmount) != "" && $.trim(change.changeReason) != "" && $.trim(change.changeItemId)) {
            //changeProfit.push(change);
            acctProfitVo.acctProfitChangeRecordBeanChangeVo.inserted.push(change);
        } else {
            $.messager.alert('系统提示', '“调整金额、调整原因、调整项目编码”其中最少有一项为空，请重新填写', 'error')
            return;
        }
        $("#acctDeptFormWindow").window('close');
    })
    $("#addAcctBtn").on('click', function () {
        $("#acctDeptFormWindow").window('close');
    })


    var setDefaultDate = function (val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var h = date.getHours();
            var mm = date.getMinutes();
            var s = date.getSeconds();
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' '
                + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
            return dateTime;
        } else {
            var date = new Date();
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var h = date.getHours();
            var mm = date.getMinutes();
            var s = date.getSeconds();
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' '
                + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
            return dateTime;
        }

    }


    $("#modifyChangeWindow").dialog({
        title: '修改调整',
        width: 700,
        height: 300,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var yearMonth = $("#yearMonth").textbox('getValue');
            var incomeOrCost = $("#incomeOrCost").textbox('getValue');
            var acctDeptId = $("#acctDeptId").textbox('getValue');
            $("#modifyChangeDataGrid").datagrid('load', {
                yearMonth: yearMonth,
                incomeOrCost: incomeOrCost,
                acctDeptId: acctDeptId
            });
            $("#modifyChangeDataGrid").datagrid('selectRow', 0)
        }
    });

    var flag;
    $("#modifyChangeDataGrid").datagrid({
        singleSelect: true,
        fit: true,
        fitColumns: true,
        url: '/api/acct-proft-chage-record/change-record-search/',
        method: 'GET',
        footer: '#changeModifyButton',
        columns: [[{
            title: '调整项目',
            field: 'incomeOrCost',
            formatter: function (value, row, index) {
                if (value == "1") {
                    return value = "收入调整";
                } else if (value == "0") {
                    return value = "成本调整";
                } else {
                    return value;
                }
            }

        }, {
            title: '调整核算单元编码',
            field: 'acctDeptId',
            formatter: function (value, row, index) {
                for (var i = 0; i < acctDeptDict.length; i++) {
                    if (value == acctDeptDict[i].id) {
                        return acctDeptDict[i].deptName;
                    }
                }
                return value;
            }
        }, {
            title: '操作者编码',
            field: 'operator',
            formatter: function (value, row, index) {
                if (value == parent.config.loginId) {
                    return value = parent.config.loginName
                } else {
                    return value;
                }
            }
        }, {
            title: '调整项目编码',
            field: 'changeItemId'
        }, {
            title: '调整金额',
            field: 'changeAmount'
        }, {
            title: '调整原因',
            field: 'changeReason'
        }, {
            title: 'id',
            field: 'id',
            hidden: false
        }, {
            title: '调整日期',
            field: 'operatorDate',
            formatter: setDefaultDate
        }]],
        onLoadSuccess: function (data) {
            flag = flag + 1;
            if (flag == 1) {
                if (data.total == 0) {
                    $.messager.alert('系统提示', '该核算单元暂未调整，无法进行修改操作', 'info');
                    $("#modifyChangeWindow").dialog('close');
                }
                flag = 0;
            }
        }
    });

    //删除调整项目
    $("#timeChangeButton").on('click', function () {
        var row = $("#modifyChangeDataGrid").datagrid('getSelected');
        if (row == null) {
            $.messager.alert("系统提示", "请选择要删除的项目", "info");
            return;
        }
        $.messager.confirm('系统提示', '确定要进行删除操作吗', function (r) {
            if (r) {
                var number = $("#modifyChangeDataGrid").datagrid('getRowIndex', row);
                var rowProfit = $('#acctDeptProfitDg').datagrid('getData').rows[changeIndex];
                var number1 = parseFloat(rowProfit.incomeChangeItem) - parseFloat(row.changeAmount);
                var number2 = parseFloat(rowProfit.costChangeItem) - parseFloat(row.changeAmount);
                if (row.incomeOrCost == "1") {
                    $('#acctDeptProfitDg').datagrid('updateRow', {
                        index: changeIndex,
                        row: {
                            incomeChangeItem: number1
                        }
                    });
                }
                if (row.incomeOrCost == "0") {
                    $('#acctDeptProfitDg').datagrid('updateRow', {
                        index: changeIndex,
                        row: {
                            costChangeItem: number2
                        }
                    });
                }
                $("#modifyChangeDataGrid").datagrid('deleteRow', number);
                var acctProftChageRecordBeanChangeVo = {};
                acctProftChageRecordBeanChangeVo.deleted = [];
                var deleted = $("#modifyChangeDataGrid").datagrid('getChanges', 'deleted');
                acctProfitVo.acctProfitChangeRecordBeanChangeVo.deleted.push(row) ;

            }
        });
    })

    $("#closeChangeButton").on('click', function () {
        $("#modifyChangeWindow").dialog('close');
    })
    //查询按钮
    $("#searchBtn").on('click', function () {
        var yearMonth = $("#fetchDate").datebox('getValue');
        if (!yearMonth) {
            $.messager.alert("系统提示", "查询时间不能为空", 'info');
            return;
        }
        var options = $("#acctDeptProfitDg").datagrid('options');
        options.url = "/api/dept-profit/list?hospitalId=" + parent.config.hospitalId + "&yearMonth=" + yearMonth;
        $("#acctDeptProfitDg").datagrid('reload');
    })
    //停止编辑行
    var stopEdit = function () {
        if (editRow || editRow == 0) {
            $("#acctDeptProfitDg").datagrid('endEdit', editRow);
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
            $("#acctDeptProfitDg").datagrid('beginEdit', editRow);
        }
    }


    //保存按钮
    $("#saveBtn").on('click', function () {
        var rows = $("#acctDeptProfitDg").datagrid('getRows');
        if (!rows.length) {
            $.messager.alert("系统提示", "没有要保存的内容", "info");
            return;
        }
        var yearMonth = $("#fetchDate").datebox('getValue');

        for (var i = 0; i < rows.length; i++) {
            rows[i].yearMonth = yearMonth;
            rows[i].hospitalId = parent.config.hospitalId;
        }

        if (stopEdit()) {
            $.messager.confirm('系统提示', "如果调整了相关数据，需要重新进行分摊管理成本，在进行保存，是否继续？", function (data) {
                if (data == 1) {
                    acctProfitVo.acctDeptProfits = rows ;
                    $.postJSON("/api/dept-profit/save-update", acctProfitVo, function (data) {
                        $.messager.alert("系统提示","保存成功","info") ;
                        acctProfitVo.acctProfitChangeRecordBeanChangeVo.inserted.splice(0,acctProfitVo.acctProfitChangeRecordBeanChangeVo.inserted.length) ;
                        acctProfitVo.acctProfitChangeRecordBeanChangeVo.updated.splice(0,acctProfitVo.acctProfitChangeRecordBeanChangeVo.updated.length) ;
                        acctProfitVo.acctProfitChangeRecordBeanChangeVo.deleted.splice(0,acctProfitVo.acctProfitChangeRecordBeanChangeVo.deleted.length) ;
                    }, function (data) {
                        $.messager.alert("系统提示", "保存失败", "info");
                    })
                }
            })
        }

    });


    //删除某一个成本项目
    $("#removeBtn").on('click', function () {
        var rows = $("#acctDeptProfitDg").datagrid('getSelections');
        if (!rows.length) {
            $.messager.alert("系统提示", "请选择要删除的项目", "info");
            return;
        }

        var deleteIds = [];
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].id) {
                deleteIds.push(rows[i].id);
            }
        }
        //如果需要删除的
        if (deleteIds.length) {
            $.messager.confirm('系统提示', '确定要进行删除操作吗', function (r) {
                if (r) {
                    $.postJSON("/api/dept-profit/delete-dept-cost", deleteIds, function (data) {
                        $.messager.alert('系统提示', '删除成功', 'info');
                        $("#searchBtn").trigger('click');
                    }, function (data) {

                    })
                }
            });

        } else {
            $.messager.alert("系统提示", "本页数据未存在在数据库中，不能删除", "info");
        }
    })


    //效益计算
    $("#calcBtn").on('click', function () {
        //核算单元效益参数
        var paramId = '4028804151a8cc690151a97338840000';
        var yearMonth = $("#fetchDate").datebox('getValue');
        if (!yearMonth) {
            $.messager.alert("系统提示", "请填写效益的月份", 'info');
            return;
        }
        $.get("/api/dept-profit/calc-profit?hospitalId=" + parent.config.hospitalId + "&yearMonth=" + yearMonth + "&paramId=" + paramId, function (data) {
            $("#acctDeptProfitDg").datagrid('loadData', data);
        })
    });

    $("#devideBtn").on('click', function () {
        var rows = $("#acctDeptProfitDg").datagrid('getRows');
        var yearMonth = $("#fetchDate").datebox('getValue');
        if (stopEdit()) {
            $.postJSON("/api/dept-profit/re-devide-manager/" + parent.config.hospitalId + "/" + yearMonth, rows, function (data) {
                $("#acctDeptProfitDg").datagrid('loadData', data);
            }, function (data) {

            })
        }
    });
});