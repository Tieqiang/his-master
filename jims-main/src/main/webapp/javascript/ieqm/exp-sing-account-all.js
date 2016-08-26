/**
 * 单品总账汇总
 * Created by wangbinbin on 2015/11、02.
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
    //供应商
    var suppliers = {};
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
        return suppliers;
    });
    /**
     * 定义明细表格
     */
    $("#importDetail").datagrid({
        title: "单品总账-汇总",
        fit: true,
        fitColumns: true,
        singleSelect: true,
        showFooter:true,
        rownumbers:true,
        footer: '#ft',
        toolbar:'#tb',
        columns: [[{
            title: '方式',
            field: 'way',
            align: 'center',
            width: '6%'
        }, {
            title: '代码',
            field: 'expCode',
            align: 'center',
            width: '10%'
        }, {
            title: '产品名称',
            field: 'expName',
            align: 'center',
            width: '10%'
        }, {
            title: '类型',
            field: 'ioClass',
            align: 'center',
            width: '7%'
        }, {
            title: "科室",
            width: '7%',
            align: 'center',
            field: 'ourName',
            formatter: function (value, row, index) {
                for (var i = 0; i < suppliers.length; i++) {
                    if (value == suppliers[i].supplierCode) {
                        return suppliers[i].supplierName;
                    }
                }
                return value;
            }
        }, {
            title: '单位',
            field: 'packageUnits',
            align: 'center',
            width: '7%'
        }, {
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: '10%'
        }, {
            title: '批号',
            width: '5%',
            align: 'center',
            field: 'batchNo'
        } , {
            title: '入(出)库日期',
            width: '11%',
            align: 'center',
            field: 'actionDate',
            formatter: myFormatter2
        }, {
            title: '失效日期',
            width: '11%',
            align: 'center',
            field: 'expireDate',
            formatter: myFormatter2
        }, {
            title: "结存",
            width: '7%',
            align: 'center',
            field: 'inventory'
        }, {
            title: '单价',
            width: '7%',
            align: 'right',
            field: 'purchasePrice',
            type:'numberbox'
        }, {
            title: '入库数量',
            width: '7%',
            align: 'center',
            field: 'importNum',
            type:'numberbox'
        }, {
            title: '入库金额',
            width: '7%',
            align: 'right',
            field: 'importPrice',
            type:'numberbox'
        }, {
            title: '出库数量',
            width: '7%',
            align: 'center',
            field: 'exportNum',
            type:'numberbox'
        }, {
            title: '出库金额',
            width: '7%',
            align: 'right',
            field: 'exportPrice',
            type:'numberbox'
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
    });

    //为报表准备字段
    var startDates='';
    var stopDates='';

    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            startDates=myFormatter2(startDates);
            stopDates=myFormatter2(stopDates);
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-single-account-all.cpt"+"&hospitalId="+parent.config.hospitalId+"&storageCode="+parent.config.storageCode+"&startDate=" + startDates + "&stopDate=" + stopDates;
            $("#report").prop("src",cjkEncode(https));
        }
    });
    $("#printBtn").on('click', function () {
        var printData = $("#importDetail").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    });
    var importDetailDataVO = {};//传递vo
    var detailsData = [];//信息
    var specCodeAll =[];
    var importPrice = 0.00;
    var exportPrice = 0.00;
    importPrice = parseFloat(importPrice);
    exportPrice = parseFloat(exportPrice);
    var loadDict = function(){
        importDetailDataVO.stopDate = new Date($("#stopDate").datetimebox("getText"));
        importDetailDataVO.startDate = new Date($("#startDate").datetimebox("getText"));
        importDetailDataVO.hospitalId = parent.config.hospitalId;
        importDetailDataVO.storage = parent.config.storageCode;
        var promise =$.get("/api/exp-import/exp-single-account",importDetailDataVO,function(data) {
            startDates=importDetailDataVO.startDate;
            stopDates=importDetailDataVO.stopDate;
            $.each(data,function(index,item){
                if(item.purchasePrice == null || item.purchasePrice == '' || typeof(item.purchasePrice) == 'undefined'){
                    item.purchasePrice = 0.00;
                }
                if (item.importPrice == null || item.importPrice == '' || typeof(item.importPrice) == 'undefined') {
                    item.importPrice = 0.00;
                }
                if (item.exportPrice == null || item.exportPrice == '' || typeof(item.exportPrice) == 'undefined') {
                    item.exportPrice = 0.00;
                }
                item.purchasePrice = parseFloat(item.purchasePrice);
                item.importPrice = parseFloat(item.importPrice);
                item.exportPrice = parseFloat(item.exportPrice);

                importPrice += item.importPrice;
                exportPrice += item.exportPrice;

                item.purchasePrice = fmoney(item.purchasePrice,2);
                item.importPrice = fmoney(item.importPrice,2);
                item.exportPrice = fmoney(item.exportPrice,2);
            });
            if (data.length <= 0) {//map {"size",value}
                $.messager.alert('系统提示', '数据库暂无数据', 'info');
                $("#importDetail").datagrid('loadData', []);
                return;
            }else{
                detailsData=data;
                $("#importDetail").datagrid('loadData',data);
                $('#importDetail').datagrid('appendRow',{
                    ourName: '',
                    purchasePrice: '合计：',
                    importPrice: fmoney(importPrice,2),
                    exportPrice: fmoney(exportPrice,2)
                });
            }
           },'json');
        promise.done(function(){
             $("#importDetail").datagrid('loadData', detailsData);
        })
     }

    //格式化金额
    function fmoney(s, n) {
        n = n > 0 && n <= 20 ? n : 2;
        s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
        var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
        t = "";
        for (i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
        }
        return t.split("").reverse().join("") + "." + r;
    }
})