/**
 * Created by wangbinbin on 2015/10/10.
 */
/***
 * 产品调价情况统计
 */
$(function () {
    //格式化日期函数
    function formatterDate(val, row) {
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
            return dateTime
        }
    }
    $('#startDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#startDate').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
            $('#startDate').datetimebox('setText', dateTime);
            $('#startDate').datetimebox('hidePanel');
        }
    });
    $('#stopDate').datetimebox({
        value: 'dateTime',
        required: true,
        showSeconds: true,
        formatter: formatterDate,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#stopDate').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
            //(m < 10 ? ("0" + m) : m) + "/" + (d < 10 ? ("0" + d) : d) + "/" + y + " " + time;
            $('#stopDate').datetimebox('setText', dateTime);
            $('#stopDate').datetimebox('hidePanel');
        }
    });

    $("#dg").datagrid({
        title: '产品调价情况统计',
        fit: true,//让#dg数据创铺满父类容器
        toolbar:'#ft',
        footer: '#tb',
        rownumbers: true,
        singleSelect: true,
        columns: [[{
            title: '产品名称',
            field: 'expName',
            width: "9%"
        },  {
            title: '产品规格',
            field: 'expSpec',
            width: "9%"
        }, {
            title: '单位',
            field: 'units',
            width: "5%"
        }, {
            title: '厂家',
            field: 'firmId',
            width: "9%"
        }, {
            title: '批发价格',
            field: 'tradePrice',
            width: "11%",
            editor: 'numberbox'
        }, {
            title: '零售价',
            field: 'retailPrice',
            width: "11%",
            editor: 'numberbox'
        },{
            title: '起用日期',
            field: 'startDate',
            width: "11%",
            formatter : formatterDate
        },{
            title: '停止日期',
            field: 'stopDate',
            width: "11%",
            formatter : formatterDate
        },{
            title: '备注',
            field: 'memos',
            width: "20%"
        }
        ]]
    });

    var prices = [];
    $("#searchBtn").on('click',function(){
        var get = loadDict();
        get.done(function(){
            if(prices.length<=0){
                $.messager.alert("系统提示", "数据库暂无数据");
                $("#dg").datagrid('loadData', []);
            }else
                $("#dg").datagrid('loadData', prices);
        });

    })
    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            $("#report").prop("src", parent.config.defaultReportPath + "exp-change-price-count.cpt");
        }
    })
    $("#printBtn").on('click', function () {
        var printData = $("#dg").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    })
    var loadDict = function () {
        var startDate = $('#startDate').datebox('getText');
        var stopDate = $('#stopDate').datebox('getText');
        if(!startDate || !stopDate){
            $.messager.alert("系统提醒","请选择开始和结束日期","error") ;
            return ;
        }
        var get =$.get("/api/exp-price-search/countList?longStartTime=" + startDate +"&longStopTime="+stopDate+"&hospitalId="+parent.config.hospitalId, function(data){
            prices=data;
        });
        return get;
    }
})

