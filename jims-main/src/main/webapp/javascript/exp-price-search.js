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
        rownumbers: true,
        singleSelect: true,
        columns: [[{
            title: '代码',
            field: 'expCode',
            width: "7%"
        }, {
            title: '物价编码',
            field: 'materialCode',
            width: "7%"
        }, {
            title: '产品名称',
            field: 'expName',
            width: "6%"
        },  {
            title: '产品规格',
            field: 'expSpec',
            width: "5%"
        }, {
            title: '单位',
            field: 'units',
            width: "4%"
        }, {
            title: '厂家',
            field: 'firmId',
            width: "5%"
        }, {
            title: '市场价',
            field: 'tradePrice',
            width: "9%",
            editor: 'numberbox'
        }, {
            title: '零售价',
            field: 'retailPrice',
            width: "9%",
            editor: 'numberbox'
        },{
            title: '包装量',
            field: 'amountPerPackage',
            width: "5%",
            editor: 'numberbox'
        },{
            title: '最小规格',
            field: 'minSpec',
            width: "5%"
        },{
            title: '最小单位',
            field: 'minUnits',
            width: "5%"
        },{
            title: '起始日期',
            field: 'startDate',
            width: "9%",
            formatter : formatterDate
        },{
            title: '停止日期',
            field: 'stopDate',
            width: "9%",
            formatter : formatterDate
        },{
            title: '备注',
            field: 'memos',
            width: "10%"
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
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d);
            return dateTime
        }
    }
    //定义expName
    $('#search').combogrid({
        panelWidth: 300,
        idField: 'expCode',
        textField: 'expName',
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'expCode', title: '编码', width: 100, align: 'center'},
            {field: 'expName', title: '名称', width: 200, align: 'center'}
        ]],
        loadMsg: 'loading',
        url: "/api/exp-name-dict/list-exp-name-by-input"
    });
    var prices = [];
    var nowPriceData = []; //应加载现行价格的数据
    var oldPriceData = []; //应加载历史价格的数据
    var nowTime = new Date().getTime();
    //提取
    $("#searchBtn").on('click', function () {
        var expName =$('#search').combo('getValue');
        if (!expName) {
            $.messager.alert("系统提醒", "请选择产品名称", "error");
            return;
        }
        var promise = loadDict() ;
        promise.done(function(){
            if(prices.length<=0){
                $.messager.alert("系统提示", "数据库暂无数据");
                $("#dg").datagrid('loadData', []);
                return;
            }
            $("#dg").datagrid('loadData', prices);
        })

    });
    //现行价格
    $("#nowPriceBtn").on('click',function(){
        var expName =$('#search').combo('getValue');
        if (!expName) {
            $.messager.alert("系统提醒", "请选择产品名称", "error");
            return;
        }
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
                $("#dg").datagrid('loadData', []);
                return;
            }
            nowPriceData.splice(0,nowPriceData.length);
        });

    })
    //历史价格
    $("#oldPriceBtn").on('click',function(){
        var expName =$('#search').combo('getValue');
        if (!expName) {
            $.messager.alert("系统提醒", "请选择产品名称", "error");
            return;
        }
        var promise =loadDict();
        promise.done(function(){
            for(var i = 0 ;i<prices.length;i++){
                if(prices[i].stopDate != null && prices[i].stopDate-nowTime<0){
                    oldPriceData.push(prices[i]);
                }
            }
            if(oldPriceData.length<=0){
                $.messager.alert("系统提示", "数据库暂无数据");
                $("#dg").datagrid('loadData', []);
                return;
            }
            $("#dg").datagrid('loadData', oldPriceData);
            oldPriceData.splice(0,oldPriceData.length);
        });
    })
    //加载相应编码的数据
    var loadDict = function () {
        var expCode = $("#search").combogrid('getValue')
        prices.splice(0,prices.length);
        var pricePromise =   $.get("/api/exp-price-search/list?expCode=" + expCode+"&hospitalId="+parent.config.hospitalId, function(data){
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