/**
 * 出库分摊情况表
 * Created by wangbinbin on 2015/11/10.
 */
function myFormatter2(val,row) {
    if(val!=null){
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
function w3(s) {
    if (!s) return new Date();
    var y = s.substring(0, 4);
    var m = s.substring(5, 7);
    var d = s.substring(8, 10);
    var h = s.substring(11, 14);
    var min = s.substring(15, 17);
    var sec = s.substring(18, 20);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d) && !isNaN(h) && !isNaN(min) && !isNaN(sec)) {
        return new Date(y, m - 1, d, h, min, sec);
    } else {
        return new Date();
    }
}
$(function () {

    /**
     * 定义明细表格
     */
    $("#importDetail").datagrid({
        title: "出库分摊情况表",
        fit: true,
        toolbar: '#tb',
        footer: '#ft',
        fitColumns: true,
        singleSelect: true,
        showFooter:true,
        rownumbers:true,
        columns: [[{
            title: '产品类型',
            field: 'expForm',
            width: '9%'
        }, {
            title: '资金类型',
            field: 'fundItem',
            width: '9%'
        }, {
            title: '分摊方式名称',
            field: 'assignName',
            width: '9%'
        }, {
            title: '出库金额',
            field: 'payAmount',
            width: '7%'
        }]]
    });
    //设置时间
    var curr_time = new Date();
    $("#startDate").datetimebox("setValue", myFormatter2(curr_time));
    $("#stopDate").datetimebox("setValue", myFormatter2(curr_time));
    $('#startDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: myFormatter2,
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
        formatter: myFormatter2,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#stopDate').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
            $('#stopDate').datetimebox('setText', dateTime);
            $('#stopDate').datetimebox('hidePanel');
        }
    });
    $("#searchBtn").on('click', function () {
        var promiseDetail = loadDict();
    })
    var importDetailDataVO = {};//传递vo
    var detailsData = [];//信息
    var specCodeAll =[];
    var loadDict = function(){
        var importPrice = 0.00;
        importDetailDataVO.stopDate = new Date($("#stopDate").datetimebox("getText"));
        importDetailDataVO.startDate = new Date($("#startDate").datetimebox("getText"));
        importDetailDataVO.hospitalId = parent.config.hospitalId;
        importDetailDataVO.storage = parent.config.storageCode;
        var promise =$.get("/api/exp-export/exp-export-assign-search",importDetailDataVO,function(data){
            detailsData=data;
            for(var l = 0 ;l<data.length;l++){
                    importPrice+=data[l].payAmount;
        }
        },'json');
        promise.done(function(){
            if(detailsData.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                $("#importDetail").datagrid('loadData',[]);
                return;
            }
            $("#importDetail").datagrid('loadData',detailsData);
            $("#importDetail").datagrid('appendRow',{
                fundItem:"总计：",
                payAmount:importPrice
            });

        })
        detailsData.splice(0,detailsData.length);
    }
})