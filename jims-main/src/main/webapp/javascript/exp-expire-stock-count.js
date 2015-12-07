/**
 * Created by wangbinbin on 2015/10/8.
 */
/***
 * 产品过期查询
 */
function myformatter2(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}
function w3(s) {
    if (!s) return new Date();
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
    $("#dg").datagrid({
        title: '过期产品统计',
        fit: true,//让#dg数据创铺满父类容器
        toolbar:'#ft',
        footer: '#tb',
        rownumbers: true,
        singleSelect: true,
        columns: [[{
            title: '代码',
            field: 'expCode',
            width: "11%"
        }, {
            title: '产品名称',
            field: 'expName',
            width: "11%"
        },  {
            title: '产品规格',
            field: 'expSpec',
            width: "11%"
        }, {
            title: '单位',
            field: 'units',
            width: "11%"
        }, {
            title: '厂家',
            field: 'firmId',
            width: "11%"
        }, {
            title: '批号',
            field: 'batchNo',
            width: "11%"
        }, {
            title: '有效期',
            field: 'expireDate',
            width: "9%",
            formatter : formatterDate
        },{
            title: '库存量',
            field: 'quantity',
            width: "11%",
            editor: 'numberbox'
        },{
            title: '金额',
            field: 'stockQuantity',
            width: "15%",
            editor: 'numberbox'
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
    var curr_time = new Date();
    $("#overDate").datebox("setValue", myformatter2(curr_time));
    //提取
    var expires = [];
    $("#searchBtn").on('click', function () {
        var overDate =$('#overDate').combo('getValue');
        if (!overDate) {
            $.messager.alert("系统提醒", "请选择截至日期", "error");
            return;
        }
        var promise = loadDict() ;
        promise.done(function(){
            if(expires.length<=0){
                $.messager.alert("系统提示", "数据库暂无数据");
                $("#dg").datagrid('loadData', []);
                return;
            }
            $("#dg").datagrid('loadData', expires);
        })

    });

    //加载相应编码的数据
    var loadDict = function () {
        var overDate = $("#overDate").combogrid('getText');
        expires.slice(0,expires.length);
        var expirePromise =   $.get("/api/exp-stock/expire-count?storage=" + parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&overDate="+overDate, function(data){
           expires = data;
        });
        return expirePromise ;
    }
})