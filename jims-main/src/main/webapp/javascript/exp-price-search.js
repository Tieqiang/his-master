/**
 * Created by wangbinbin on 2015/10/8.
 */
/***
 * 产品价格查询
 */
$(function () {
    $("#dg").datagrid({
        title: '产品价格查询',
        fit: true,//让#dg数据创铺满父类容器
        toolbar:'#ft',
        footer: '#tb',
        singleSelect: true,
        columns: [[{
            title: '代码',
            field: 'expCode',
            align: 'center',
            width: "7%"
        }, {
            title: '产品名称',
            field: 'expName',
            align: 'center',
            width: "8%"
        },  {
            title: '产品规格',
            field: 'expSpec',
            align: 'center',
            width: "8%"
        }, {
            title: '单位',
            field: 'units',
            align: 'center',
            width: "3%"
        }, {
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: "8%"
        }, {
            title: '市场价',
            field: 'tradePrice',
            align: 'center',
            width: "6%"
        }, {
            title: '零售价',
            field: 'retailPrice',
            align: 'center',
            width: "6%"
        },{
            title: '包装量',
            field: 'amountPerPackage',
            align: 'center',
            width: "5%"
        },{
            title: '最小规格',
            field: 'minSpec',
            align: 'center',
            width: "6%"
        },{
            title: '最小单位',
            field: 'minUnits',
            align: 'center',
            width: "6%"
        },{
            title: '起始日期',
            field: 'startDate',
            align: 'center',
            width: "12%",
            formatter : formatterDate
        },{
            title: '停止日期',
            field: 'stopDate',
            align: 'center',
            width: "12%",
            formatter : formatterDate
        }, {
            title: '物价编码',
            field: 'materialCode',
            align: 'center',
            width: "7%"
        },{
            title: '备注',
            field: 'memos',
            align: 'center',
            width: "7%"
        }
        ]]
    });
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
                + (h < 10 ? ("0" + h) : h)+":"+ (mm < 10 ? ("0" + mm) : mm)+":"+ (s < 10 ? ("0" + s) : s);
            return dateTime
        }
    }
    //定义expName
    $('#search').combogrid({
        panelWidth: 500,
        idField: 'expCode',
        textField: 'expName',
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'expCode', title: '编码', width: 150, align: 'center'},
            {field: 'expName', title: '名称', width: 200, align: 'center'},
            {field: 'inputCode', title: '拼音', width: 50, align: 'center'}
        ]],
        loadMsg: 'loading',
        fitColumns: true,
        url: "/api/exp-name-dict/list-exp-name-by-input"
    });
    var prices = [];
    var nowPriceData = []; //应加载现行价格的数据
    var oldPriceData = []; //应加载历史价格的数据
    var nowTime = new Date().getTime();
    //提取
    $("#searchBtn").on('click', function () {
        var expName =$('#search').combo('getValue');
        //if (!expName) {
        //    $.messager.alert("系统提醒", "请选择产品名称", "error");
        //    return;
        //}
        var promise = loadDict() ;
        promise.done(function(){
            if(prices.length<=0){
                $.messager.alert("系统提示", "数据库暂无数据");
                printFlag=false;
                $("#dg").datagrid('loadData', []);
                return;
            }
            $("#dg").datagrid('loadData', prices);
            times=0;
            printFlag=true;
        })

    });
    //现行价格
    $("#nowPriceBtn").on('click',function(){
        var expName =$('#search').combo('getValue');
        //if (!expName) {
        //    $.messager.alert("系统提醒", "请选择产品名称", "error");
        //    return;
        //}
        var promise = loadDict();
        promise.done(function(){
            for(var i = 0 ;i<prices.length;i++){
                if(((prices[i].stopDate-nowTime)>0 || (prices[i].stopDate == null))  && prices[i].startDate-nowTime<0){
                    nowPriceData.push(prices[i]);
                }
            }
            $("#dg").datagrid('loadData', nowPriceData);
            if(nowPriceData.length<=0){
                $.messager.alert("系统提示", "数据库暂无数据");
                printFlag=false;
                $("#dg").datagrid('loadData', []);
                return;
            }
            nowPriceData.splice(0,nowPriceData.length);
            times=1;
            printFlag=true;
        });

    })


    //历史价格
    $("#oldPriceBtn").on('click',function(){
        var expName =$('#search').combo('getValue');
        //if (!expName) {
        //    $.messager.alert("系统提醒", "请选择产品名称", "error");
        //    return;
        //}
        var promise =loadDict();
        promise.done(function(){
            for(var i = 0 ;i<prices.length;i++){
                console.log(prices[i].stopDate-nowTime);
                if(prices[i].stopDate != null && prices[i].stopDate-nowTime<=0){
                    oldPriceData.push(prices[i]);
                }
            }
            if(oldPriceData.length<=0){
                $.messager.alert("系统提示", "数据库暂无数据");
                $("#dg").datagrid('loadData', []);
                printFlag=false;
                return;
            }
            $("#dg").datagrid('loadData', oldPriceData);
            printFlag=true;
            times=2;
            oldPriceData.splice(0,oldPriceData.length);
        });
    })

    var times='';
    var printFlag=false;
    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var expCode = $("#search").combogrid('getValue')
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-price-search.cpt&expCode=" + expCode+"&hospitalId="+parent.config.hospitalId+"&times="+times;
            $("#report").prop("src",cjkEncode(https));
        }
    })
    $("#printBtn").on('click', function () {
        var rows = $("#dg").datagrid("getRows");
        console.log(rows);
        if (printFlag){

        }else {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    })
    //加载相应编码的数据
    var loadDict = function () {
        var expCode = $("#search").combogrid('getValue');
        prices.splice(0,prices.length);
        var pricePromise =   $.get("/api/exp-price-list/list?expCode=" + expCode+"&hospitalId="+parent.config.hospitalId+"&flag=1", function(data){
            $.each(data,function(index,item){
                var price ={};
                price.expCode = item.expCode;
                price.materialCode = item.materialCode;
                price.expName = item.expName;
                price.expSpec = item.expSpec;
                price.units = item.units;
                price.firmId = item.firmId;
                price.tradePrice = item.tradePrice;
                price.retailPrice = item.retailPrice;
                price.amountPerPackage = item.amountPerPackage;
                price.minSpec = item.minSpec;
                price.minUnits = item.minUnits;
                price.startDate = item.startDate;
                price.stopDate = item.stopDate;
                price.memos = item.memos;
                prices.push(price);
            });
        });
        return pricePromise ;
    }
})