/**
 * Created by heren on 2015/12/9.
 * 二线科室收入统计
 */

$(function () {

    var acctDetps = [];
    $.get('/api/acct-dept-dict/acct-list?hospitalId=' + parent.config.hospitalId, function (data) {
        acctDetps = data;
    })

    var editRow = undefined ;
    var incomeTypes = [];
    $.get('/api/service-income-type/list-all?hospitalId=' + parent.config.hospitalId, function (data) {
        incomeTypes = data;
    })

    var p = $('#synDate').datebox('panel');//日期选择对象
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


    //二级科室选择
    $("#secondLevelDept").combobox({
        method: 'GET',
        url: '/api/acct-dept-dict/list-end-dept?hospitalId=' + parent.config.hospitalId,
        textField: 'deptName',
        valueField: 'id',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length) {
                $(this).combobox('setValue', data[0].id);
            }
        }
    });
    $("#serviceDeptIncomeTable").datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        method: 'GET',
        toolbar: "#ft",
        url: '',
        showFooter: true,
        rownumbers: true,
        pageSize: 30,
        loadMsg: '数据正在加载中.....',
        columns: [[{
            title: 'id',
            field: 'id',
            hidden: true
        }, {
            title: '核算单元名称',
            field: 'acctDeptId',
            width: '10%',
            formatter: function (value, row, index) {
                for (var i = 0; i < acctDetps.length; i++) {
                    if (acctDetps[i].id == value) {
                        return acctDetps[i].deptName;
                    }
                }
                return value;
            }
        }, {
            title: '收入项目',
            field: 'incomeTypeId',
            width: '20%',
            formatter: function (value, row, index) {
                for (var i = 0; i < incomeTypes.length; i++) {
                    if (value == incomeTypes[i].id) {
                        return incomeTypes[i].serviceTypeName;
                    }
                }
                return value;
            }
        }, {
            title: '收入金额',
            field: 'totalIncome',
            width: '10%'
        }, {
            title: '统计月份',
            field: 'yearMonth',
            width: '10%'
        }, {
            title: '接受服务核算单元',
            field: 'serviceForDeptId',
            width: '20%',
            formatter: function (value, row, index) {
                for (var i = 0; i < acctDetps.length; i++) {
                    if (acctDetps[i].id == value) {
                        return acctDetps[i].deptName;
                    }
                }
                return value;
            }
        }, {
            title: '是否本院',
            field: 'outFlag',
            width: '10%',
            formatter: function (value, row, index) {
                if (value == '0') {
                    return "院外"
                }
                if (value == '1') {
                    return "院内"
                }
                return value;
            }
        },{
            title:'审核状态',
            field:'confirmStatus',
            width:'20%',
            formatter:function(value,row,index){
                if(value=='0'){
                    return '暂存'
                }
                if(value=='1'){
                    return '录入科室提交'
                }
                if(value=='2'){
                    return '通过审核'
                }

                if(value=='-1'){
                    return '审核未通过'
                }
            },
            editor:{type:'combobox',options:{
                textField:'name',
                valueField:'value',
                data:[{
                    name:'暂存',
                    value:'0'
                },{
                    name:'录入科室提交',
                    value:'1'
                },{
                    name:'通过审核',
                    value:'2'
                },{
                    name:'未通过审核',
                    value:'-1'
                }],
                onShowPanel:function(){
                }
            }}
        },{
            title:'录入者',
            field:'operator',
            hidden:true
        },{
            title:'录入时间',
            field:'operatorDate',
            hidden:true
        }]],
        onLoadSuccess: function (data) {
            var totalIncomes = 0;
            for (var i = 0; i < data.rows.length; i++) {
                totalIncomes += data.rows[i].totalIncome;
            }
            $(this).datagrid('insertRow', {
                index: 0,
                row: {
                    acctDeptId: '合计',
                    incomeTypeId: '卖服务收入',
                    totalIncome: totalIncomes
                }
            })
        },
        onClickRow:function(rowIndex,rowData){
            if(rowIndex>0){
                if(editRow){
                    $(this).datagrid('endEdit',editRow) ;
                    $(this).datagrid('beginEdit',rowIndex) ;
                    editRow =rowIndex;
                }else{
                    editRow = rowIndex ;
                    $(this).datagrid('beginEdit',rowIndex) ;
                }
            }
        }

    });


    //保存
    $("#saveBtn").on('click', function () {

    });

    //查询某一个二线核算单元的收入
    $("#queryBtn").on('click', function () {
        var yearMonth = $("#synDate").datebox('getValue');
        if (!yearMonth) {
            $.messager.alert('系统提示', '请选择要提取的月份', 'info');
            return;
        }

        var deptId = $("#secondLevelDept").combobox('getValue')
        if (!deptId) {
            $.messager.alert("系统提示", '请选择提取科室', 'info');
            return;
        }
        var options = $("#serviceDeptIncomeTable").datagrid('options');
        options.url = "/api/service-dept-income/list-by-confirm-status?hospitalId=" + parent.config.hospitalId + "&yearMonth=" + yearMonth + "&deptId=" + deptId+"&confirmStatus=1"
        $("#serviceDeptIncomeTable").datagrid('reload');
    });

    //添加单项收入开始
    $("#acctDeptId").combobox({
        readonly: true,
        textField: 'deptName',
        valueField: 'id',
        method: 'GET',
        url: '/api/acct-dept-dict/acct-list?hospitalId=' + parent.config.hospitalId
    });

    //服务科室
    $("#serviceForDeptId").combobox({
        textField: 'deptName',
        valueField: 'id',
        method: 'GET',
        url: '/api/acct-dept-dict/acct-list?hospitalId=' + parent.config.hospitalId,
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('setValue', data[0].id);
            }
        }
    });



    //保存审核的项目
    $("#saveSingleBtn").on('click',function(){
        if(editRow){
            $("#serviceDeptIncomeTable").datagrid('endEdit',editRow) ;
        }
        var rows = $("#serviceDeptIncomeTable").datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            rows[i].hospitalId = parent.config.hospitalId;
            rows[i].confirmOperator = parent.config.loginId
            rows[i].confirmDate = new Date();
        }
        rows.shift() ;
        if (rows.length > 0) {
            $.postJSON("/api/service-dept-income/merge", rows, function (data) {
                $.messager.alert("系统提示", "保存成功", "info");
                $("#queryBtn").trigger('click');
            }, function (data) {
            })
        }
    })


    $("#allConfirmBtn").on('click',function(){
        var rows = $("#serviceDeptIncomeTable").datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            rows[i].hospitalId = parent.config.hospitalId;
            rows[i].confirmOperator = parent.config.loginId
            rows[i].confirmDate = new Date();
            rows[i].confirmStatus = '2' ;
        }
        rows.shift() ;
        if (rows.length > 0) {
            $.postJSON("/api/service-dept-income/merge", rows, function (data) {
                $.messager.alert("系统提示", "审核成功！", "info");
                $("#queryBtn").trigger('click');
            }, function (data) {
            })
        }
    })

    $("#allNotConfirmBtn").on('click',function(){
        var rows = $("#serviceDeptIncomeTable").datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            rows[i].hospitalId = parent.config.hospitalId;
            rows[i].confirmOperator = parent.config.loginId
            rows[i].confirmDate = new Date();
            rows[i].confirmStatus = '-1' ;
        }
        rows.shift() ;
        if (rows.length > 0) {
            $.postJSON("/api/service-dept-income/merge", rows, function (data) {
                $.messager.alert("系统提示", "审核成功！", "info");
                $("#queryBtn").trigger('click');
            }, function (data) {

            })
        }
    }) ;

    $("#queryGoodBtn").on('click',function(){
        var yearMonth = $("#synDate").datebox('getValue');
        if (!yearMonth) {
            $.messager.alert('系统提示', '请选择要提取的月份', 'info');
            return;
        }

        var deptId = $("#secondLevelDept").combobox('getValue')
        if (!deptId) {
            $.messager.alert("系统提示", '请选择提取科室', 'info');
            return;
        }
        var options = $("#serviceDeptIncomeTable").datagrid('options');
        options.url = "/api/service-dept-income/list-by-confirm-status?hospitalId=" + parent.config.hospitalId + "&yearMonth=" + yearMonth + "&deptId=" + deptId+"&confirmStatus=2"
        $("#serviceDeptIncomeTable").datagrid('reload')
    }) ;
    $("#queryFailBtn").on('click',function(){
        var yearMonth = $("#synDate").datebox('getValue');
        if (!yearMonth) {
            $.messager.alert('系统提示', '请选择要提取的月份', 'info');
            return;
        }

        var deptId = $("#secondLevelDept").combobox('getValue')
        if (!deptId) {
            $.messager.alert("系统提示", '请选择提取科室', 'info');
            return;
        }
        var options = $("#serviceDeptIncomeTable").datagrid('options');
        options.url = "/api/service-dept-income/list-by-confirm-status?hospitalId=" + parent.config.hospitalId + "&yearMonth=" + yearMonth + "&deptId="+ deptId+"&confirmStatus=-1"
        $("#serviceDeptIncomeTable").datagrid('reload')
    });
})