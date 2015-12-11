/**
 * Created by heren on 2015/12/9.
 * 二线科室收入统计
 */

$(function () {

    var acctDetps = [] ;
    $.get('/api/acct-dept-dict/acct-list?hospitalId='+parent.config.hospitalId,function(data){
        acctDetps = data ;
    })

    var incomeTypes=[] ;
    $.get('/api/acct-reck/list?hospitalId='+parent.config.hospitalId,function(data){
        incomeTypes = data ;
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
            return year + '-' + month;

        }//配置formatter，只返回年月
    });

    //获取方式
    $("#fetchWay").combobox({
        method: 'GET',
        url: '/api/acct-param/list-by-type?hospitalId=' + parent.config.hospitalId + "&fetchType=second_level_dept_income",
        textField: 'paramName',
        valueField: 'id',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length) {
                $(this).combobox('setValue', data[0].id)
            }
        }
    });
    //二级科室选择
    $("#secondLevelDept").combobox({
        method: 'GET',
        url: '/api/acct-dept-dict/list-second-level-dept?hospitalId=' + parent.config.hospitalId,
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
        url:'',
        pagination:true,
        showFooter:true,
        pageSize:30,
        loadMsg: '数据正在加载中.....',
        columns: [[{
            title: 'id',
            field: 'id',
            hidden: true
        }, {
            title: '核算单元名称',
            field: 'acctDeptId',
            width:'20%',
            formatter:function(value,row,index){
                for(var i = 0 ;i<acctDetps.length ;i++){
                    if(acctDetps[i].id==value){
                        return acctDetps[i].deptName ;
                    }
                }
                return value ;
            }
        }, {
            title: '收入项目',
            field: 'incomeTypeId',
            width:'20%',
            formatter:function(value,row,index){
                for(var i = 0 ;i<incomeTypes.length ;i++){
                    if(value==incomeTypes[i].id){
                        return incomeTypes[i].reckItemName;
                    }
                }
                return value ;
            }
        }, {
            title: '收入金额',
            field:'totalIncome',
            width:'10%'
        },{
            title:'统计月份',
            field:'yearMonth',
            width:'10%'
        },{
            title:'接受服务核算单元',
            field:'serviceForDeptId',
            width:'20%',
            formatter:function(value,row,index){
                for(var i = 0 ;i<acctDetps.length ;i++){
                    if(acctDetps[i].id==value){
                        return acctDetps[i].deptName ;
                    }
                }
                return value ;
            }
        }]]
    });


    //收入提取
    $("#searchBtn").on('click',function(){
        var yearMonth = $("#synDate").datebox('getValue') ;
        if(!yearMonth){
            $.messager.alert('系统提示','请选择要提取的月份','info') ;
            return ;
        }
        var paramId = $("#fetchWay").combobox('getValue') ;
        if(!paramId){
            $.messager.alert("系统提示",'请选择提取标准','info') ;
            return ;
        }
        var deptId = $("#secondLevelDept").combobox('getValue')
        if(!deptId){
            $.messager.alert("系统提示",'请选择提取科室','info') ;
            return ;
        }

        $.get("/api/service-dept-income/list-by-dept?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth+"&deptId="+deptId,function(data){
            for(var i = 0 ;i<data.length ;i++){
                if(data[i].incomeTypeId=="按比例提取"){
                    $.messager.confirm("系统提示","本月的分本科室的收入已经计算，重新计算将删除以往的计算记录，是否继续？",function(r){
                        if(r){
                            var options = $("#serviceDeptIncomeTable").datagrid('options') ;
                            options.url = "/api/service-dept-income/fetch-service-income?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth+"&paramId="+paramId+"&deptId="+deptId
                            $("#serviceDeptIncomeTable").datagrid('reload') ;
                        }else{
                            $("#queryBtn").trigger('click') ;
                        }
                    }) ;
                    return ;
                }
            }
            var options = $("#serviceDeptIncomeTable").datagrid('options') ;
            options.url = "/api/service-dept-income/fetch-service-income?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth+"&paramId="+paramId+"&deptId="+deptId
            $("#serviceDeptIncomeTable").datagrid('reload') ;
        }) ;


    })

    //保存
    $("#saveBtn").on('click',function(){

        var rows = $("#serviceDeptIncomeTable").datagrid('getRows') ;
        for(var i = 0 ;i<rows.length ;i++){
            rows[i].hospitalId = parent.config.hospitalId ;
        }

        if(rows.length>0){
            $.postJSON("/api/service-dept-income/merge",rows,function(data){
                $.messager.alert("系统提示","保存成功","info") ;
                $("#queryBtn").trigger('click') ;
            },function(data){})
        }

    }) ;

    //查询某一个二线核算单元的收入
    $("#queryBtn").on('click',function(){
        var yearMonth = $("#synDate").datebox('getValue') ;
        if(!yearMonth){
            $.messager.alert('系统提示','请选择要提取的月份','info') ;
            return ;
        }

        var deptId = $("#secondLevelDept").combobox('getValue')
        if(!deptId){
            $.messager.alert("系统提示",'请选择提取科室','info') ;
            return ;
        }
        var options = $("#serviceDeptIncomeTable").datagrid('options') ;
        options.url = "/api/service-dept-income/list-by-dept?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth+"&deptId="+deptId
        $("#serviceDeptIncomeTable").datagrid('reload') ;
    }) ;

    //添加单项收入开始
    $("#acctDeptId").combobox({
        readonly:true,
        textField:'deptName',
        valueField:'id',
        method:'GET',
        url:'/api/acct-dept-dict/acct-list?hospitalId='+parent.config.hospitalId
    }) ;

    //服务科室
    $("#serviceForDeptId").combobox({
        textField:'deptName',
        valueField:'id',
        method:'GET',
        url:'/api/acct-dept-dict/acct-list?hospitalId='+parent.config.hospitalId,
        onLoadSuccess:function(){
            var data = $(this).combobox('getData') ;
            if(data.length>0){
                $(this).combobox('setValue',data[0].id) ;
            }
        }
    }) ;

    $("#incomeTypeId").combobox({
        textField:'reckItemName',
        valueField:'id',
        method:'GET',
        url:'/api/acct-reck/list?hospitalId='+parent.config.hospitalId,
        onLoadSuccess:function(){
            var data = $(this).combobox('getData') ;
            if(data.length>0){
                $(this).combobox('setValue',data[0].id) ;
            }
        }
    }) ;

    $("#itemWin").window({
        closed:true,
        onClose:function(){
            $("#itemForm").form('reset') ;
        },
        onBeforeOpen:function(){
            var deptId = $("#secondLevelDept").combobox('getValue')
            if(!deptId){
                $.messager.alert("系统提示",'获取当前核算单元出错','info') ;
                return  false ;
            }
            var yearMonth = $("#synDate").datebox('getValue') ;
            if(!yearMonth){
                $.messager.alert('系统提示','录入的金额无效','info') ;
                return  false ;
            }

            $("#acctDeptId").combobox('setValue',deptId) ;

            var data = $("#incomeTypeId").combobox('getData') ;
            if(data.length>0){
                $("#incomeTypeId").combobox('setValue',data[0].id) ;
            }
            var data = $("#serviceForDeptId").combobox('getData') ;
            if(data.length>0){
                $("#serviceForDeptId").combobox('setValue',data[0].id) ;
            }
        }
    }) ;


    $("#addBtn").on('click',function(){
        $("#itemWin").window('open') ;
    }) ;

    //保存项目
    $("#saveIncomeBtn").on('click',function(){
        var totalIncome = $("#totalIncome").textbox('getValue') ;
        var flag  = isNaN(totalIncome) ;
        if(!flag){
            var obj = {} ;
            obj.acctDeptId = $("#acctDeptId").combobox("getValue") ;
            obj.yearMonth =  $("#synDate").datebox('getValue') ;
            obj.totalIncome = $("#totalIncome").val() ;
            obj.serviceForDeptId = $("#serviceForDeptId").combobox('getValue')
            obj.incomeTypeId = $("#incomeTypeId").combobox('getValue') ;

            $("#serviceDeptIncomeTable").datagrid('appendRow',obj) ;
            $("#itemWin").window('close');
        }else{
            $.messager.alert('系统提示','请输入收入项目','info') ;
        }
    })

    $("#cancelIncomeBtn").on('click',function(){
        $("#itemWin").window('close');
    })

    $("#delBtn").on('click',function(){
        var rows = $("#serviceDeptIncomeTable").datagrid('getSelected')
        if(!rows){
            $.messager.alert("系统提示","请选择要删除的记录","error") ;
        }

        if(!rows.id){
            var index = $("#serviceDeptIncomeTable").datagrid('getRowIndex',rows) ;
            $("#serviceDeptIncomeTable").datagrid('deleteRow',index) ;
        }else{
            $.postJSON("/api/service-dept-income/del-service-income",rows,function(data){
                $.messager.alert('系统提示','删除成功','info') ;
                $("#queryBtn").trigger('click');
            },function(data){})
        }
    })
})