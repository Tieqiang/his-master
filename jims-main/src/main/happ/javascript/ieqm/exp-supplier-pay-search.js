/**
 * 供应商付款情况查询
 * Created by wangbinbin on 2015/10/28.
 */
function myformatter2(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return y + '/' + (m < 10 ? ('0' + m) : m) + '/' + (d < 10 ? ('0' + d) : d);
}
function w3(s) {
    if (!s){
        return new Date();
    }
    var y = s.substring(0, 4);
    var m = s.substring(5, 7);
    var d = s.substring(8, 10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
        return new Date(y, m - 1, d);
    } else {
        return new Date();
    }
}
$(function () {

    /**
     * 定义明细表格
     */
    $("#importDetail").datagrid({
        title: "供应商付款情况查询",
        footer: '#ft',
        toolbar: '#tb',
        fit: true,
        fitColumns: true,
        singleSelect: false,
        showFooter:true,
        rownumbers:true,
        columns: [[{
            title: '供货商',
            field: 'supplier',
            width: '6%'
        }, {
            title: '已付金额',
            width: '15%',
            field: 'ok',
            editor: {
                singleSelect: true,
                columns: [[{
                    title: '进价金额',
                    width: '5%',
                    field: 'payAmount',
                    type:'numberbox'
                }, {
                    title: '零售金额',
                    width: '5%',
                    field: 'retailAmount'
                }, {
                    title: '进零价差额',
                    width: '5%',
                    field: 'balanceOkDisPrice'
                }]]}
        }, {
            title: '未付金额',
            width: '15%',
            field: 'no',
            editor: {
                singleSelect: true,
                columns: [[{
                    title: '进价金额',
                    width: '5%',
                    field: 'purchasePrice',
                    type:'numberbox'
                }, {
                    title: '零售金额',
                    width: '5%',
                    field: 'retailPrice'
                }, {
                    title: '进零价差额',
                    width: '5%',
                    field: 'balanceNoDisPrice'
                }]]}
        },{
            title: '统计区间',
            field: 'supplier',
            width: '14%',
            editor: {
                fit: true,
                fitColumns: true,
                singleSelect: true,
                columns: [[{
                    title: '开始时间',
                    width: '7%',
                    field: 'startTime',
                    formatter:formatterDate
                }, {
                    title: '结束时间',
                    width: '7%',
                    field: 'stopTime',
                    formatter:formatterDate
                }]]}
        },{
            title: '记账单位',
            field: 'storage' ,
            width: '6%'
        }]]
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
    //设置时间
    var curr_time = new Date();
    $("#startDate").datebox("setValue", myformatter2(curr_time));
    $("#stopDate").datebox("setValue", myformatter2(curr_time));
    //供应商
    $("#searchBtn").on('click', function () {
        var promiseDetail = loadDict();
    })
    var importDetailDataVO = {};//传递vo
    var detailsData = [];//信息
    var loadDict = function(){
        importDetailDataVO.stopDate = $("#stopDate").datebox("getText");
        importDetailDataVO.startDate = $("#startDate").datebox("getText");
        importDetailDataVO.hospitalId = parent.config.hospitalId;
        importDetailDataVO.storage = parent.config.storageCode;
        var promise =$.get("/api/exp-import/exp-supplier-search",importDetailDataVO,function(result){
            var detail = [];
            for(var i = 0 ;i<result.length;i++){
                for(var j = 0;j<result.length;j++){
                    if(result[i].supplier == result[j].supplier){
                        detail.supplier = result[i].supplier;
                        detail.payAmount = result[i].payAmount+result[j].payAmount;
                        detail.retailAmount = result[i].retailAmount+result[j].retailAmount;
                        detail.balanceOkDisPrice = detail.payAmount-detail.retailAmount;
                        detail.purchasePrice = result[i].purchasePrice+result[j].purchasePrice;
                        detail.retailPrice = result[i].retailPrice+result[j].retailPrice;
                        detail.balanceNoDisPrice = detail.retailPrice-detail.purchasePrice;
                        detail.startTime = $("#startDate").datebox('getText');
                        detail.stopTime = $("#stopDate").datebox('getText');
                        detail.storage = parent.config.storage;
                        detailsData.push(detail);
                    }
                }
            }
        });
        promise.done(function(){
            if(detailsData.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                $("#importDetail").datagrid('loadData',[]);
                return;
            }
            $("#importDetail").datagrid('loadData',detailsData);
        })
        detailsData.splice(0,detailsData.length);
    }
})