/**
 * Created by heren on 2015/12/2.
 */
//一线科室成本提取
$(function(){
    var acctDeptDict=[] ;
    $.get("/api/acct-dept-dict/acct-list?hospitalId="+parent.config.hospitalId,function(data){
        acctDeptDict = data ;
    }) ;

    var editRow = undefined ;
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
            return year + '-' + month;

        }//配置formatter，只返回年月
    });




    $("#deptCostTable").datagrid({
        fit:true,
        fitColumns:true,
        method:'GET',
        checkOnSelect:true,
        striped: true,
        singleSelect: false,
        toolbar: '#ft',
        method: 'GET',
        pagination: true,
        loadMsg:'数据正在加载，请稍后......',
        columns:[[{
            title:'id',
            field:'id',
            checkbox:true
        },{
            title:'核算单元',
            field:'acctDeptId',
            width:'20%',
            formatter:function(value,row,index){
                for(var i = 0 ;i<acctDeptDict.length ;i++){
                    if(value==acctDeptDict[i].id){
                        return acctDeptDict[i].deptName ;
                    }
                }
                return value ;
            }
        },{
            title:'成本类型',
            field:'costItemId',
            width:'10%'
        },{
            title:'成本金额',
            field:'cost',
            width:'10%',
            editor:{type:'validatebox',options:{
                validType:'number'
            }}
        },{
            title:'减免成本',
            field:'minusCost',
            width:'10%',
            editor:{type:'validatebox',options:{
                validType:'number'
            }}
        },{
            title:'备注信息',
            field:'memo',
            width:'40%',
            editor:{type:'textbox',options:{}}
        }]]
    }) ;
    //设置分页
    var p1 = $('#deptCostTable').datagrid('getPager');
    $(p1).pagination({
        pageSize: 100,//每页显示的记录条数，默认为10
        pageList: [100, 200, 300, 500],//可以设置每页记录条数的列表
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });


    //成本提取
    $("#searchBtn").on('click',function(){
        var yearMonth = $("#fetchDate").datebox('getValue') ;
        if(!yearMonth){
            $.messager.alert("系统提示","请选择计算成本月份","info") ;
            return ;
        }

        $.messager.progress({
            title:'正在提取成本',
            text:'数据高效提取中，请稍后....'
        }) ;
        $.get("/api/acct-dept-cost/fetch-cost?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth,function(data){
            var options = $("#deptCostTable").datagrid('options') ;
            options.url = "/api/acct-dept-cost/list-all?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth ;
            $("#deptCostTable").datagrid('reload')  ;
            $.messager.progress('close') ;
        })
    }) ;

});

