/**
 * 科室成本确认
 * Created by heren on 2015/11/30.
 */
$(function(){
    var acctDeptDict=[] ;
    $.get("/api/acct-dept-dict/acct-list?hospitalId="+parent.config.hospitalId,function(data){
        acctDeptDict = data ;
    }) ;
    var costItems = [];
    $.get("/api/cost-item/list-item?hospitalId=" + parent.config.hospitalId, function (data) {
        costItems = data;
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
        fit:true,
        fitColumns:true,
        method:'GET',
        checkOnSelect:true,
        striped: true,
        singleSelect: false,
        toolbar: '#ft',
        method: 'GET',
        rownubers:true,
        pagination: true,
        loadMsg:'数据正在加载，请稍后......',
        columns:[[{
            title:'id',
            field:'id',
            checkbox:true
        },{
            title:'核算单元',
            field:'acctDeptId',
            width:'12%',
            formatter:function(value,row,index){
                for(var i = 0 ;i<acctDeptDict.length ;i++){
                    if(value==acctDeptDict[i].id){
                        return acctDeptDict[i].deptName ;
                    }
                }
                return value ;
            }
        },{
            title:'成本金额',
            field:'cost',
            width:'15%'
        },{
            title:'减免成本',
            field:'minusCost',
            width:'7%'
        },{
            title:'成本类型',
            field:'costItemId',
            width:'10%',
            formatter: function (value, row, index) {
                for (var i = 0; i < costItems.length; i++) {
                    if (value == costItems[i].id) {
                        return costItems[i].costItemName;
                    }
                }
                return value;
            }
        },{
            title:'计入方式',
            field:'fetchWay',
            width:'7%'
        },{
            title:'操作人',
            field:'operator',
            width:'7%',
            formatter: function (value, row, index) {
                return value = parent.config.staffName;
            }
        },{
            title:'操作时间',
            field:'operatorDate',
            formatter: setDefaultDate,
            width:'13%'

        },{
            title:'发布时间',
            field:'publishDate',
            formatter: setDefaultDate,
            width:'13%'

        },{
            title:'发布状态',
            field:'publish',
            width:'10%',
            formatter:function (value, row, index) {
                if ($.trim(row.publishDate)){
                    return value="已发布";
                }else{
                    return value="未发布";
                }
            }
        }]]
    }) ;
    var costTypeDict = [];
    var promiseCostDIct = $.get('/api/cost-item/list-item?hospitalId=' + parent.config.hospitalId ,function(data){
        costTypeDict = data;
        var itemAll = {};
        itemAll.id = "0";
        itemAll.costItemName = "全部";
        costTypeDict.push(itemAll);
        return costTypeDict;
    })
    promiseCostDIct.done(function () {
        $('#costType').combogrid({
            panelWidth: 160,
            value: '全部',
            idField: 'id',
            textField: 'costItemName',
            data: costTypeDict,
            columns: [[
                {field: 'costItemName', title: '成本项目', width: 155}
            ]]
        });
    })
    //查询成本按钮
    $("#searchBtn").on('click',function(){
        var yearMonth = $("#fetchDate").datebox('getValue') ;
        if(!yearMonth){
            $.messager.alert("系统提示","查询时间不能为空",'info') ;
            return ;
        }
        var costItemId = $("#costType").combobox('getValue') ;

        if(!costItemId){
            $.messager.alert("系统提示","请选择对应程成本项目",'info') ;
            return ;
        }
        if(costItemId=="0"||costItemId=="全部"){
            costItemId = "";
        }
        console.log(costItemId);
        var options = $("#acctCostTable").datagrid('options') ;
        options.url = "/api/acct-dept-cost/list-publish?hospitalId="+parent.config.hospitalId+"&costItemId="+costItemId+"&yearMonth="+yearMonth+"&operator="+parent.config.loginId ;
        $("#acctCostTable").datagrid('reload') ;

    })
        //发布成本
        $("#saveBtn").on('click',function(){
            var rows = $("#acctCostTable").datagrid('getSelections') ;
            console.log(rows) ;
            if(!rows.length){
                $.messager.alert("系统提示","请选择要发布的记录","info") ;
                return  ;
            }
            for(var i = 0 ;i<rows.length ;i++){
                rows[i].publishDate = new Date();
            }


            $.postJSON("/api/acct-dept-cost/save-publish",rows,function(data){
                $("#searchBtn").trigger('click') ;
                $.messager.alert("系统提示","保存成功","info") ;
            },function(data){
                    $.messager.alert("系统提示","保存失败","info") ;
            })
        })  ;

}) ;