/**
 * Created by heren on 2015/11/18.
 * 提取收入
 */

$(function () {

    var acctDepts = [];

    var currentPage = 0;

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
            if(month<10){
                return year + '-0' + month;
            }
            return year + '-' + month;

        }//配置formatter，只返回年月
    });

    //收入表格
    $("#incomeDatagrid").datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        singleSelect: true,
        toolbar: '#ft',
        method: 'GET',
        pagination: true,
        pageSize: 50,
        loadMsg: '正在分割收入中，请不要关闭浏览器.....',
        columns: [[{
            title: 'id',
            field: 'id',
            hidden: true
        }, {
            title: '收入项目名称',
            field: 'incomeItemName'
        }, {
            title: '收入项目名称',
            field: 'incomeItemCode'
        }, {
            title: "核算科目分类",
            field: 'classOnRecking'
        }, {
            title: '开单核算单元',
            field: 'orderedBy',
            formatter: function (value, row, index) {
                for (var i = 0; i < acctDepts.length; i++) {
                    if (acctDepts[i].id == value) {
                        return acctDepts[i].deptName;
                    }
                }
                return value;
            }
        }, {
            title: '执行核算单元',
            field: 'performedBy',
            formatter: function (value, row, index) {
                for (var i = 0; i < acctDepts.length; i++) {
                    if (acctDepts[i].id == value) {
                        return acctDepts[i].deptName;
                    }
                }
                return value;
            }
        }, {
            title: '护理核算单元',
            field: 'wardCode',
            formatter: function (value, row, index) {
                for (var i = 0; i < acctDepts.length; i++) {
                    if (acctDepts[i].id == value) {
                        return acctDepts[i].deptName;
                    }
                }
                return value;
            }
        }, {
            title: '开单医生',
            field: 'orderDoctor'
        }, {
            title: '执行医生',
            field: 'performedDoctor'
        }, {
            title: '门诊或住院',
            field: 'inpOrOutp',
            formatter: function (value, row, index) {
                if (row.inpOrOutp == '0') {
                    return '门诊'
                }

                if (row.inpOrOutp == '1') {
                    return '住院'
                }

                return value;
            }
        }, {
            title: "费用",
            field: 'totalCost'
        }, {
            title: '开单核算单位收入',
            field: 'orderIncome'
        }, {
            title: '执行核算单位收入',
            field: 'performIncome'
        }, {
            title: '护理核算单元收入',
            field: "wardIncome"
        }]],
        onLoadSuccess: function (data) {
            //var options = $(p1).pagination('options');
            //var totals = options.total;
            //var pages = Math.ceil(totals / options.pageSize);
            //if (currentPage <= pages+1) {
            //    //var rows = $("#incomeDatagrid").datagrid('getRows');
            //    var rows=data.rows
            //    if (!rows.length) {
            //        $.messager.alert("系统提示", "请首先提取数据", 'info');
            //        return;
            //    } else {
            //        for (var i = 0; i <rows.length; i++) {
            //            var priceItemCode = rows[i].incomeItemCode;
            //            for (var j = 0; j < incomeItems.length; j++) {
            //                if (incomeItems[j].priceItemCode == priceItemCode) {
            //                    var rowIndex = $("#incomeDatagrid").datagrid('getRowIndex', rows[i]);
            //                    $("#incomeDatagrid").datagrid('selectRow', rowIndex);
            //                    $("#incomeDatagrid").datagrid('scrollTo', rowIndex);
            //
            //                    //门诊
            //                    if (rows[i].inpOrOutp == '0') {
            //                        rows[i].orderIncome = rows[i].totalCost * incomeItems[j].outpOrderedBy / 100;
            //                        rows[i].performIncome = rows[i].totalCost * incomeItems[j].outpPerformedBy / 100;
            //                        rows[i].wardIncome = rows[i].totalCost * incomeItems[j].outpWardCode / 100;
            //                    }
            //                    //住院
            //                    if (rows[i].inpOrOutp == '1') {
            //                        rows[i].orderIncome = rows[i].totalCost * incomeItems[j].inpOrderedBy / 100;
            //                        rows[i].performIncome = rows[i].totalCost * incomeItems[j].inpPerformedBy / 100;
            //                        rows[i].wardIncome = rows[i].totalCost * incomeItems[j].inpWardCode / 100;
            //                    }
            //                    //$("#incomeDatagrid").datagrid('updateRow', {index: rowIndex, row: rows[i]});
            //                    break;
            //                }
            //            }
            //        }
            //    }
            //    $.postJSON("/api/fetch-data/save-calc", rows, function (data) {
            //        console.log('保存成功')
            //    }, function (data) {
            //    })
            //    if(currentPage>pages){
            //        devideIncome(pages);
            //    }else{
            //        devideIncome(currentPage);
            //    }
            //    currentPage++;
            //}

        }
    });

    //设置分页
    var p1 = $('#incomeDatagrid').datagrid('getPager');
    $(p1).pagination({
        pageSize: 100,//每页显示的记录条数，默认为10
        pageList: [100, 200, 300, 500],//可以设置每页记录条数的列表
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });

    //提取HIS数据
    $("#fetchIncomeBtn").on('click', function () {
        $.messager.confirm('系统提示','重新提取HIS数据，将删除已经提取的本月份数据，是否继续？',function(data){
            if(data==1){
                $.messager.progress({
                    title:'正在提取数据',
                    text:'数据高效提取中，请稍后....'
                }) ;
                var options = $("#incomeDatagrid").datagrid('options');
                var yearMonth = $("#fetchDate").datebox('getValue');
                if (!yearMonth) {
                    $.messager.alert('系统提示', '请选择提取的月份', 'error');
                    $.messager.progress('close') ;
                    return;
                }
                var fetchTypeId=$("#fetchType").combobox('getValue') ;
                if(!fetchTypeId){
                    $.messager.alert('系统提示','获取提取数据方式出错，请刷新页面','info') ;
                    $.messager.progress('close') ;
                    return ;
                }
                $.post("/api/fetch-data/fetch-from-his?hospitalId=" + parent.config.hospitalId + "&yearMonth=" + yearMonth+"&fetchTypeId="+fetchTypeId, function (data) {
                    $.messager.progress('close') ;
                    $("#fetchItemBtn").click();
                })
            }
        });
    });

    //提取已经存在的数据
    $("#fetchItemBtn").on('click', function () {
        var options = $("#incomeDatagrid").datagrid('options');
        var yearMonth = $("#fetchDate").datebox('getValue');
        if (!yearMonth) {
            $.messager.alert('系统提示', '请选择提取的月份', 'error');
            return;
        }
        options.url = "/api/fetch-data/fetch-by-page?hospitalId=" + parent.config.hospitalId + "&yearMonth=" + yearMonth;
        $("#incomeDatagrid").datagrid('reload');
    });


    var loadAcctDepts = function () {
        $.get("/api/acct-dept-dict/acct-list?hospitalId=" + parent.config.hospitalId, function (data) {
            acctDepts = data;
        })
    };
    loadAcctDepts();

    var objtemp = {};

    var incomeItems = [];
    var loadAcctIncomeItem = function () {
        $.get("/api/income-item/list-all?hospitalId=" + parent.config.hospitalId, function (data) {
            incomeItems = data;
        })
    }
    loadAcctDepts();
    loadAcctIncomeItem();


    //分割收入
    $("#devideIncomeBtn").on('click', function () {
        $.messager.confirm('系统提示','重新分割将重写以前分割数据，是否继续？',function(data){
            if(data==1){
                $.messager.progress({
                    title:'收入分割',
                    text:'正在进行数据分割，请稍后.....'
                }) ;
                var yearMonth = $("#fetchDate").datebox('getValue');
                if (!yearMonth) {
                    $.messager.alert('系统提示', '请选择提取的月份', 'error');
                    return;
                }
                $.post("/api/fetch-data/devide-income?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth,function(data){
                    $.messager.progress('close')
                    $("#fetchItemBtn").click();
                })
            }
        });

    });

    var devideIncome = function (i) {
        $(p1).pagination('select', i);
    }


    $("#saveIncomeBtn").on('click', function () {
        //var rows = $("#incomeDatagrid").datagrid('getRows');
        //$.postJSON("/api/fetch-data/save-calc", rows, function (data) {
        //    $.messager.alert("系统提示", "保存成功");
        //}, function (data) {
        //    $.messager.alert("系统提示", "保存失败");
        //})
        //devideIncome(currentPage) ;
    })

    $("#fetchType").combobox({
        textField:'paramName',
        valueField:'id',
        method:'GET',
        url:'/api/acct-param/list-by-type?hospitalId='+parent.config.hospitalId+"&fetchType=income_fetch_type",
        onLoadSuccess:function(){
            var data = $(this).combobox('getData') ;
            for(var i = 0 ;i<data.length;i++){
                if(data[i].paramName=='实时发生制'){
                    $(this).combobox('setValue',data[i].id) ;
                    break ;
                }
            }
        }
    })  ;
});
