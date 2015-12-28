/**
 * Created by heren on 2015/12/2.
 */
//一线科室成本提取
$(function(){
    var acctDeptDict=[] ;
    $.get("/api/acct-dept-dict/acct-list?hospitalId="+parent.config.hospitalId,function(data){
        acctDeptDict = data ;
    }) ;

    var costItems = [] ;
    $.get("/api/cost-item/list-item?hospitalId="+parent.config.hospitalId,function(data){
        costItems = data ;
    })
    $.get("/api/service-income-type/list-all?hospitalId="+parent.config.hospitalId,function(data){
        costItems.push(data) ;
    })

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
            if(month<10){
                return year + '-0' + month;
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
        pageSize:100,
        pageList: [50,100, 200, 300],
        pagination: true,
        loadMsg:'数据正在加载，请稍后......',
        columns:[[{
            title:'id',
            field:'id',
            hidden:true
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
            width:'10%',
            formatter:function(value,row,index){
                for(var i = 0 ;i<costItems.length;i++){
                    if(value ==costItems[i].id){
                        return costItems[i].costItemName ;
                    }
                }
                return value ;
            }
        },{
            title:'成本金额',
            field:'cost',
            width:'10%',
            editor:{type:'validatebox',options:{
                validType:'number'
            }},
            formatter:function(value,row,index){
                return parseFloat(value).toFixed(2) ;
            }
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
            width:'20%',
            editor:{type:'textbox',options:{}}
        },{
            title:"获取方式",
            field:'fetchWay',
            width:'20%'
        },{
            title:"提取月份",
            field:'yearMonth',
            hidden:true
        },{
            title:'所属医院',
            field:'hospitalId',
            hidden:true
        }]],
        onLoadSuccess:function(data){
            var totalCost =0.0 ;
            for(var i = 0 ;i<data.total;i++){
                totalCost += data.rows[i].cost ;
            }

            $(this).datagrid('insertRow',{
                index:0,
                row:{
                    acctDeptId:'成本合计',
                    cost:totalCost
                }
            }) ;
        }
    }) ;
    //设置分页
    var p1 = $('#deptCostTable').datagrid('getPager');
    $(p1).pagination({
        pageSize: 100,//每页显示的记录条数，默认为10
        pageList: [50,100, 200, 300],//可以设置每页记录条数的列表
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

        var fetchTypeId = $("#fetchType").combobox('getValue') ;
        $.messager.progress({
            title:'正在提取成本',
            text:'数据高效提取中，请稍后....'
        }) ;
        $.get("/api/acct-dept-cost/fetch-cost?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth+"&fetchTypeId="+fetchTypeId,function(data){
            //var options = $("#deptCostTable").datagrid('options') ;
            //options.url = "/api/acct-dept-cost/list-all?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth ;
            //$("#deptCostTable").datagrid('reload')  ;
            $("#deptCostTable").datagrid('loadData',data) ;
            $.messager.progress('close') ;
        })
    }) ;

    $("#fetchType").combobox({
        textField:'paramName',
        valueField:'id',
        method:'GET',
        url:'/api/acct-param/list-by-type?hospitalId='+parent.config.hospitalId+"&fetchType=cost_fetch_type",
        onLoadSuccess:function(){
            var data = $(this).combobox('getData') ;
            if(data.length>0){
                $(this).combobox('setValue',data[0].id)
            }
        }
    })  ;


    //查看汇总信息
    $("#queryCollectionBtn").on('click',function(){
        var yearMonth = $("#fetchDate").datebox('getValue') ;
        if(!yearMonth){
            $.messager.alert('系统提示','获取日期失败','info') ;
            return ;
        }
        var options = $("#deptCostTable").datagrid('options') ;
        options.url = "/api/acct-dept-cost/list-collection?hospitalId="+parent.config.hospitalId+"&yearMonth="+yearMonth ;
        $("#deptCostTable").datagrid('reload')  ;
    }) ;

    /**
     * 保存
     */
    $("#saveBtn").on('click',function(){
        var rows = $("#deptCostTable").datagrid('getRows') ;
        rows.shift() ;
        if(rows.length<=0){
            $.messager.alert("系统提示","没有要保存的数据，请县提取，然后在保存！",'error') ;
            return
        }
        $.postJSON("/api/acct-dept-cost/save-cost",rows,function(data){
            $.messager.alert("系统提示","保存成功，在成本报表中查看各个科室的成本") ;
        },function(data){})
    }) ;

});

