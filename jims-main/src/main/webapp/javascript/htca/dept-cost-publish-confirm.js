/**
 * 科室成本确认
 * Created by heren on 2015/11/30.
 */
$(function () {
    var acctDeptDict = [];
    $.get("/api/acct-dept-dict/acct-list?hospitalId=" + parent.config.hospitalId, function (data) {
        acctDeptDict = data;
    });
    var costItems = [];
    $.get("/api/cost-item/list-item?hospitalId=" + parent.config.hospitalId, function (data) {
        costItems = data;
    })
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
    //var promiseCostDIct = $.get('/api/cost-item/list-item?hospitalId=' + parent.config.hospitalId, function (data) {
    //    costTypeDict = data;
    //    var itemAll = {};
    //    itemAll.id = "0";
    //    itemAll.costItemName = "全部";
    //    costTypeDict.push(itemAll);
    //    return costTypeDict;
    //})
    //promiseCostDIct.done(function () {
    //    $('#costType').combogrid({
    //        panelWidth: 160,
    //        value: '全部',
    //        idField: 'id',
    //        textField: 'costItemName',
    //        data: costTypeDict,
    //        columns: [[
    //            {field: 'costItemName', title: '成本项目', width: 155}
    //        ]]
    //    });
    //})

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

    $("#acctCostTable").datagrid({
        fit: true,
        fitColumns: true,
        method: 'GET',
        checkOnSelect: true,
        striped: true,
        singleSelect: false,
        toolbar: '#ft',
        rownubers: true,
        pagination: true,
        loadMsg: '数据正在加载，请稍后......',
        columns: [[{
            title: 'id',
            field: 'id',
            checkbox: true
        }, {
            title: '核算单元',
            field: 'acctDeptId',
            width: '12%',
            formatter: function (value, row, index) {
                for (var i = 0; i < acctDeptDict.length; i++) {
                    if (value == acctDeptDict[i].id) {
                        return acctDeptDict[i].deptName;
                    }
                }
                return value;
            }
        }, {
            title: '成本金额',
            field: 'cost',
            width: '15%'
        }, {
            title: '减免成本',
            field: 'minusCost',
            width: '7%'
        }, {
            title: '成本类型',
            field: 'costItemId',
            width: '10%',
            formatter: function (value, row, index) {
                for (var i = 0; i < costItems.length; i++) {
                    if (value == costItems[i].id) {
                        return costItems[i].costItemName;
                    }
                }
                return value;
            }
        }, {
            title: '计入方式',
            field: 'fetchWay',
            width: '7%'
        }
        //    , {
        //    title: '操作人',
        //    field: 'operator',
        //    width: '7%',
        //    formatter: function (value, row, index) {
        //        return value = parent.config.staffName;
        //    }
        //}
            , {
            title: '操作时间',
            field: 'operatorDate',
            formatter: setDefaultDate,
            width: '13%'

        }, {
            title: '发布时间',
            field: 'publishDate',
            formatter: setDefaultDate,
            width: '13%'

        }, {
            title: '确认状态',
            field: 'confirmPublish',
            width: '10%',
            formatter: function (value, row, index) {
                if (value=="1") {
                    return value = "已确认";
                } else {
                    return value = "未确认";
                }
            }
        }, {
            title: '备注信息',
            field: 'memo',
            width: '40%',
            editor: {type: 'textbox', options: {}}
        }]],
        onDblClickRow: function (rowIndex, rowData) {
            if (editRow == 0 || editRow) {
                stopEdit();
            }
            editRow = rowIndex;
            $(this).datagrid('checkRow', rowIndex);
            beginEdit();
        }
    });


    //查询按钮
    $("#searchBtn").on('click', function () {
        var yearMonth = $("#fetchDate").datebox('getValue');
        if (!yearMonth) {
            $.messager.alert("系统提示", "查询时间不能为空", 'info');
            return;
        }
        //var costItemId = $("#costType").combobox('getValue');

        //if (!costItemId) {
        //    $.messager.alert("系统提示", "请选择对应程成本项目", 'info');
        //    return;
        //}
        //if (costItemId == "0" || costItemId == "全部") {
        //    costItemId = "";
        //}
        var options = $("#acctCostTable").datagrid('options');
        options.url = "/api/acct-dept-cost/publish-confirm?hospitalId=" + parent.config.hospitalId  + "&yearMonth=" + yearMonth+"&acctDeptId="+parent.config.acctDeptId;
        $("#acctCostTable").datagrid('reload');

    })


    //停止编辑行
    var stopEdit = function () {
        $("#acctCostTable").datagrid('endEdit', editRow);
        editRow = undefined;
        return true;

    }

    //开始编辑行
    var beginEdit = function () {
        if (editRow || editRow == 0) {
            $("#acctCostTable").datagrid('beginEdit', editRow);
        }
    }


    //保存按钮
    $("#saveBtn").on('click', function () {
        stopEdit();
        var rows = $("#acctCostTable").datagrid('getSelections');
        console.log(rows);
        if (!rows.length) {
            $.messager.alert("系统提示", "请选择要确认的记录", "info");
            return;
        }
        var yearMonth = $("#fetchDate").datebox('getValue');
        //var costItemId = $("#costType").combobox('getValue');

        for (var i = 0; i < rows.length; i++) {
            rows[i].yearMonth = yearMonth;
            rows[i].hospitalId = parent.config.hospitalId;
            rows[i].operator = parent.config.loginId;
            rows[i].operatorDate = new Date();
        }


        $.postJSON("/api/acct-dept-cost/save-publish-confirm/1", rows, function (data) {
            $("#searchBtn").trigger('click');
            $.messager.alert("系统提示", "保存成功", "info");
        }, function (data) {
                $.messager.alert("系统提示", "保存失败", "info");
        })
    });


    //删除某一个成本项目
    $("#backBtn").on('click', function () {
        stopEdit();
        var rows = $("#acctCostTable").datagrid('getSelections');
        console.log(rows);
        if (!rows.length) {
            $.messager.alert("系统提示", "请选择要删除的项目", "info");
            return;
        }

        if (!rows.length) {
            $.messager.alert("系统提示", "请选择要确认的记录", "info");
            return;
        }
        var yearMonth = $("#fetchDate").datebox('getValue');
        //var costItemId = $("#costType").combobox('getValue');

        for (var i = 0; i < rows.length; i++) {
            rows[i].yearMonth = yearMonth;
            rows[i].hospitalId = parent.config.hospitalId;
            rows[i].operator = parent.config.loginId;
            rows[i].operatorDate = new Date();
        }
        $.postJSON("/api/acct-dept-cost/save-publish-confirm/0", rows, function (data) {
            $.messager.alert('系统提示', '保存成功', 'info');
            $("#searchBtn").trigger('click');
        }, function (data) {
            $.messager.alert('系统提示', '保存失败', 'info');
        })

    })
});