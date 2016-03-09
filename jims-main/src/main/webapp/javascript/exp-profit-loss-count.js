/**
 * Created by wangbinbin on 2015/10/13.
 */
/***
 * 产品盈亏统计
 */

function ww3(date){
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    var d = date.getDate();
    var h = date.getHours();
    var min = date.getMinutes();
    var sec = date.getSeconds();
    var str = y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(min<10?('0'+min):min)+':'+(sec<10?('0'+sec):sec);
    return str;
}
function w3(s){
    if (!s) return new Date();
    var y = s.substring(0,4);
    var m =s.substring(5,7);
    var d = s.substring(8,10);
    var h = s.substring(11,14);
    var min = s.substring(15,17);
    var sec = s.substring(18,20);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d) && !isNaN(h) && !isNaN(min) && !isNaN(sec)){
        return new Date(y,m-1,d,h,min,sec);
    } else {
        return new Date();
    }
}
$(function () {
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
            $('#stopDate').datetimebox('setText', dateTime);
            $('#stopDate').datetimebox('hidePanel');
        }
    });
    $("#dg").datagrid({
        title: '产品调价盈亏统计',
        fit: true,//让#dg数据创铺满父类容器
        toolbar:'#ft',
        footer: '#tb',
        rownumbers: true,
        singleSelect: true,
        columns: [[{
            title: '库存单位',
            field: 'storageName',
            width: "10%"
        },  {
            title: '代码',
            field: 'storage',
            width: "7%"
        }, {
            title: '产品名称',
            field: 'expName',
            width: "7%"
        }, {
            title: '包装规格',
            field: 'expSpec',
            width: "7%"
        }, {
            title: '单位',
            field: 'units',
            width: "7%"
        }, {
            title: '厂家',
            field: 'firmId',
            width: "7%"
        },{
            title: '数量',
            field: 'quantity',
            width: "7%"
        },{
            title: '原批发价',
            field: 'originalTradePrice',
            width: "7%"
        },{
            title: '新批发价',
            field: 'currentTradePrice',
            width: "7%"
        },{
            title: '批价盈亏',
            field: 'tradePriceProfit',
            width: "7%"
        },{
            title: '原零售价',
            field: 'originalRetailPrice',
            width: "7%"
        },{
            title: '新零售价',
            field: 'currentRetailPrice',
            width: "7%"
        },{
            title: '零价亏盈',
            field: 'retailPriceProfit',
            width: "7%"
        },{
            title: '执行时间',
            field: 'actualEfficientDate',
            width: "15%",
           formatter: formatterDate
        }
        ]]
    });

    //格式化日期函数
    function formatterDate(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var d = date.getDate();
            var h = date.getHours();
            var min = date.getMinutes();
            var sec = date.getSeconds();
            var str = y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(min<10?('0'+min):min)+':'+(sec<10?('0'+sec):sec);
            return str;
        }
    }
    var sName = {};
    var promiseName = $.get('/api/exp-storage-dept/list?hospitalId='+parent.config.hospitalId,function(data){
        var name={};
        name.storageCode='';
        name.storageName='全部库存单元';
        data.push(name);
        sName=data;
        return sName;
        });
    promiseName.done(function(){
        $('#storageName').combogrid({
            panelWidth:160,
            value:'全部库存单位',
            idField:'storageCode',
            textField:'storageName',
            //method:'GET',
            data:sName,
            columns:[[
                {field:'storageCode',title:'库房代码',width:60},
                {field:'storageName',title:'库房单元',width:100}
            ]]
        });
    })

    var prices = [];
    $("#searchBtn").on('click',function(){
        var promise = loadDict();
        promise.done(function(){
            $("#dg").datagrid('loadData', prices);
        })

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
            $("#report").prop("src", parent.config.defaultReportPath + "/exp/exp_print/exp-profit-loss-count.cpt");
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
        var startDate = $('#startDate').datetimebox('getText');
        var stopDate = $('#stopDate').datetimebox('getText');
        var storageCode = $("#storageName").combogrid("getValue");
        var flag = "全部库存单位";
        if(!startDate || !stopDate){
            $.messager.alert("系统提醒","请选择开始和结束日期","error") ;
            return ;
        }
        prices.splice(0,prices.length);
        var pricePromise =$.get("/api/exp-price-modify-profit/countList?longStartTime=" + startDate +"&longStopTime="+stopDate+"&hospitalId="+parent.config.hospitalId, function(data){
            console.log(data);
                $.each(data,function(index,item){
                    var price ={};
                    price.quantity = item.quantity;
                    price.storageName = item.storageName;
                    price.expName = item.expName;
                    price.expSpec = item.expSpec;
                    price.units = item.units;
                    price.firmId = item.firmId;
                    price.storage = item.storage;
                    price.originalTradePrice = item.originalTradePrice;
                    price.currentTradePrice = item.currentTradePrice;
                    price.tradePriceProfit = item.tradePriceProfit;
                    price.originalRetailPrice = item.originalRetailPrice;
                    price.currentRetailPrice = item.currentRetailPrice;
                    price.retailPriceProfit = item.retailPriceProfit;
                    price.actualEfficientDate = item.actualEfficientDate;
                    if( price.storage==storageCode || storageCode==flag){
                        prices.push(price);
                    }
            });
            if(prices.length<=0){
                $.messager.alert("系统提示", "数据库暂无数据");
                $("#dg").datagrid('loadData', []);
                return;
            }
        });
        return pricePromise ;
    }
})

