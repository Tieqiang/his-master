/**
 * Created by wangbinbin on 2015/10/10.
 */
/***
 * 产品调价情况统计
 */
function ww3(date){
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    var d = date.getDate();
    var h = date.getHours();
    var min = date.getMinutes();
    var sec = date.getSeconds();
    var str = y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
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
        return new Date(y,m-1,d);
    } else {
        return new Date();
    }
}
$(function () {
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
            formatter : ww2
        },{
            title: '停止日期',
            field: 'stopDate',
            width: "11%",
            formatter : ww2
        },{
            title: '备注',
            field: 'memos',
            width: "20%"
        }
        ]]
    });
    function ww2(val, row){
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var d = date.getDate();
            var h = date.getHours();
            var min = date.getMinutes();
            var sec = date.getSeconds();
            var str = y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
            return str;
        }

    }
    //设置时间
    var curr_time = new Date();
    $("#startDate").datebox("setValue", ww3(curr_time));
    $("#stopDate").datebox("setValue", ww3(curr_time));
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

