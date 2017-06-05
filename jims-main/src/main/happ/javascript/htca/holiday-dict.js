/**
 * Created by heren on 2015/12/9.
 * 二线科室收入统计
 */

$(function () {
    var p = $('#synDate').datebox('panel');//日期选择对象
    var editorRow = undefined;
    var tds = false; //日期选择对象中月份
    var span = p.find('span.calendar-text'); //显示月份层的触发控件
    $("#synDate").datebox({
        onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
            span.trigger('click'); //触发click事件弹出月份层
            if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                tds = p.find('div.calendar-menu-month-inner td');
                tds.click(function (e) {
                    $(this).addClass("calendar-selected")
                    e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
                    var year = /\d{4}/.exec(span.html())[0]//得到年份
                    var month = parseInt($(this).attr('abbr'), 10) + 1; //月份
                    $("#synDate").datebox('hidePanel').datebox('setValue', year + "-" + month)
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

    $("#holidayDictTable").datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        method: 'GET',
        toolbar: "#ft",
        url: '',
        showFooter: true,
        rownumbers: true,
        pageSize: 30,
        singleSelect: true,
        loadMsg: '数据正在加载中，请稍等',
        columns: [[{
            title: 'id',
            field: 'id',
            hidden: true
        }, {
            title: '假期名称',
            field: 'holidayName',
            width: '10%',
            editor:{type:'textbox',options:{}}
        }, {
            title: '是否全天',
            field: 'fullDay',
            width: '20%',
            formatter: function (value, row, index) {
                if (value == "1") {
                    return "全天";
                }
                if (value == "0") {
                    return "半天";
                }
                return value;
            },
            editor: {
                type: 'combobox', options: {
                    valueField: 'value',
                    textField: 'text',
                    data: [{
                        value: '1',
                        text: '全天'
                    }, {
                        value: '0',
                        text: '半天'
                    }]
                }
            }
        }, {
            title: '假期日期',
            field: 'holiday',
            width: '30%',
            editor: {
                type: 'datebox', options: {
                    parser: function (s) {//配置parser，返回选择的日期
                        if (!s) return new Date();
                        var arr = s.split('-');
                        return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) -1, parseInt(arr[2], 10));
                    },
                    formatter: function (d) {
                        var year = d.getFullYear();
                        var month = d.getMonth();
                        var day = d.getDate() ;
                        var monthStr = undefined ;
                        var dayStr = undefined ;
                        var yearStr = undefined ;
                        month = month +1 ;
                        yearStr = year+"" ;
                        if(day<10){
                            dayStr = "0"+day ;
                        }else{
                            dayStr = day+"" ;
                        }
                        if(month <10){
                            monthStr = "0"+month ;
                        }else{
                            monthStr = month +"" ;
                        }


                        return yearStr +"-"+monthStr +"-"+ dayStr ;
                    }
                }
            }
        }]],
        onDblClickRow: function (rowIndex, rowData) {
            if (editorRow >= 0) {
                $("#holidayDictTable").datagrid('endEdit', editorRow);
                editorRow = rowIndex;
            } else {
                editorRow = rowIndex;
            }
            $("#holidayDictTable").datagrid('beginEdit', editorRow);
        }
    });


    //保存
    $("#saveBtn").on('click', function () {
        var rows = $("#holidayDictTable").datagrid('getRows');
        if (editorRow >= 0) {
            $("#holidayDictTable").datagrid('endEdit', editorRow);
        }
        for (var i = 0; i < rows.length; i++) {
            rows[i].operator = parent.config.loginId
            rows[i].operatorDate = new Date();
        }

        if (rows.length > 0) {
            $.postJSON("/api/holiday-dict/merge", rows, function (data) {
                $.messager.alert("系统提示", "保存成功", "info");
                $("#queryBtn").trigger('click');
            }, function (data) {
            })
        }
    });

    //查询发布记录
    $("#queryBtn").on('click', function () {
        var yearMonth = $("#synDate").datebox('getValue');
        var options = $("#holidayDictTable").datagrid('options');
        options.url = "/api/holiday-dict/list?yearMonth=" + yearMonth;
        $("#holidayDictTable").datagrid('reload');
    });

    //新增项目
    $("#addBtn").on('click', function () {
        var rows = $("#holidayDictTable").datagrid('getRows');


        $("#holidayDictTable").datagrid('appendRow', {})
        var rows = $("#holidayDictTable").datagrid('getRows');
        if (rows.length > 0) {
            if (editorRow == 0 || editorRow > 0) {
                $("#holidayDictTable").datagrid('endEdit', editorRow);
                editorRow = $("#holidayDictTable").datagrid('getRowIndex', rows[rows.length - 1]);
            } else {
                editorRow = $("#holidayDictTable").datagrid('getRowIndex', rows[rows.length - 1]);
            }
            $("#holidayDictTable").datagrid('beginEdit', editorRow);
        }
    });


    //删除项目
    $("#delBtn").on('click', function () {
        var rows = $("#holidayDictTable").datagrid('getSelected')
        if (!rows) {
            $.messager.alert("系统提示", "请选择要删除的记录", "error");
            return;
        }


        if (!rows.id) {
            var index = $("#holidayDictTable").datagrid('getRowIndex', rows);

            $.messager.confirm('系统提示', '确定要进行删除操作吗', function (r) {
                if (r) {
                    $("#holidayDictTable").datagrid('deleteRow', index);
                }
            });

        } else {
            $.messager.confirm('系统提示', '确定要进行删除操作吗', function (r) {
                if (r) {
                    $.post("/api/holiday-dict/del?id="+rows.id, function (data) {
                        $.messager.alert('系统提示', '删除成功', 'info');
                        $("#queryBtn").trigger('click');
                    })
                }
            });

        }
    });
})