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
            if(month<10){
                return year + '-0' + month;
            }
            return year + '-' + month;

        }//配置formatter，只返回年月
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
        },{
            title:'是否本院',
            field:'outFlag',
            width:'20%',
            formatter:function(value,row,index){
                if(value=='0'){
                    return "院外"
                }

                if(value=='1'){
                    return "院内"
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

        $.get("/api/service-dept-income/list-calc?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth,function(data){
            for(var i = 0 ;i<data.length ;i++){
                if(data[i].incomeTypeId=="按比例提取"){
                    $.messager.confirm("系统提示","本月的分本科室的收入已经计算，重新计算将删除以往的计算记录，是否继续？",function(r){
                        if(r){
                            var options = $("#serviceDeptIncomeTable").datagrid('options') ;
                            options.url = "/api/service-dept-income/fetch-service-income-calc?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth
                            $("#serviceDeptIncomeTable").datagrid('reload') ;
                        }else{
                            $("#queryBtn").trigger('click') ;
                        }
                    }) ;
                    return ;
                }
            }
            var options = $("#serviceDeptIncomeTable").datagrid('options') ;
            options.url = "/api/service-dept-income/fetch-service-income-calc?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth
            $("#serviceDeptIncomeTable").datagrid('reload') ;
        }) ;


    })

    //保存
    $("#saveBtn").on('click',function(){

        var rows = $("#serviceDeptIncomeTable").datagrid('getRows') ;
        for(var i = 0 ;i<rows.length ;i++){
            rows[i].hospitalId = parent.config.hospitalId ;
            rows[i].operator = parent.config.loginId
            rows[i].operatorDate = new Date() ;
        }

        if(rows.length>0){
            $.postJSON("/api/service-dept-income/merge",rows,function(data){
                $.messager.alert("系统提示","保存成功","info") ;
                $("#queryBtn").trigger('click') ;
            },function(data){})
        }

    }) ;

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
    });

})