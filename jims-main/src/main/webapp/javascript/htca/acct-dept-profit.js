/**
 * 科室单项成本录入功能
 * Created by heren on 2015/11/30.
 */
$(function () {
    var acctDeptDict = [];
    $.get("/api/acct-dept-dict/acct-list?hospitalId=" + parent.config.hospitalId, function (data) {
        acctDeptDict = data;
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
        rownumbers:true,
        pagination: true,
        loadMsg: '数据正在加载，请稍后......',
        columns: [[{
            title: 'id',
            field: 'id',
            //checkbox: true
            hidden:true
        }, {
            title: '核算单元',
            field: 'acctDeptId',
            width: '20%',
            formatter: function (value, row, index) {
                for (var i = 0; i < acctDeptDict.length; i++) {
                    if (value == acctDeptDict[i].id) {
                        return acctDeptDict[i].deptName;
                    }
                }
                return value;
            }
        }, {
            title: '科室收入',
            field: 'deptIncome',
            width: '10%'
        }, {
            title: '科室成本',
            field: 'deptCost',
            width: '10%'
        }, {
            title:'绩效提成比例',
            field:'convertRate',
            editor: {
                type: 'validatebox', options: {
                    validType: 'number'
                }
            },
            width:'10%'
        },{
            title: '满意率',
            field: 'pleasedNum',
            width: '20%',
            editor: {
                type: 'validatebox', options: {
                    validType: 'number'
                }
            }
        }, {
            title: '单项考评奖',
            field: 'specialIncome',
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
        onAfterEdit:function(index,rowData,changes){
            console.log(rowData) ;
            rowData.deptLastIncome = rowData.specialIncome*1 + ((rowData.deptIncome-rowData.deptCost)*rowData.pleasedNum/100*rowData.convertRate/100 );
            rowData.deptLastIncome = rowData.deptLastIncome.toFixed(2) ;
            $(this).datagrid('updateRow',{
                index:index,
                row:rowData
            });
        }
    });


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
            //var ed = $("#acctDeptProfitDg").datagrid('getEditor', {index: editRow, field: "cost"});
            //if (ed.target) {
            //    var flag = $(ed.target).validatebox('isValid');
            //    if (flag) {
            //
            //    } else {
            //        $.messager.alert("系统提示", "金额必须填写", 'error');
            //        return flag;
            //    }
            //}
            //var ed1 = $("#acctDeptProfitDg").datagrid('getEditor', {index: editRow, field: "minusCost"});
            //if (ed1.target) {
            //    var flag = $(ed1.target).validatebox('isValid');
            //    if (flag) {
            //    } else {
            //        $.messager.alert("系统提示", "名称必须填写", 'error');
            //        return flag;
            //    }
            //}
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
            $.postJSON("/api/dept-profit/save-update", rows, function (data) {
                $("#searchBtn").trigger('click');
                $.messager.alert("系统提示", "保存成功", "info");
            }, function (data) {
                $.messager.alert("系统提示", "保存失败", "info");
            })
        }

    });


    //删除某一个成本项目
    $("#removeBtn").on('click', function () {
        var rows = $("#acctDeptProfitDg").datagrid('getSelections');
        console.log(rows);
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
            $.postJSON("/api/dept-profit/delete-dept-cost", deleteIds, function (data) {
                $.messager.alert('系统提示', '删除成功', 'info');
                $("#searchBtn").trigger('click');
            }, function (data) {

            })
        } else {
            $.messager.alert("系统提示", "本页数据未存在在数据库中，不能删除", "info");
        }
    })


    //效益计算
    $("#calcBtn").on('click',function(){
        //核算单元效益参数
        var paramId='4028804151a8cc690151a97338840000' ;
        var yearMonth =$("#fetchDate").datebox('getValue') ;
        if(!yearMonth){
            $.messager.alert("系统提示","请填写效益的月份",'info') ;
            return ;
        }
        $.get("/api/dept-profit/calc-profit?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth+"&paramId="+paramId,function(data){
            $("#acctDeptProfitDg").datagrid('loadData',data) ;
        })
    }) ;
});