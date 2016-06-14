/**
 * 付款情况查询
 * Created by wangbinbin on 2015/11、02.
 */
//格式化日期函数
function myformatter2(val, row) {
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
        title: "付款情况查询",
        footer: '#ft',
        toolbar:'#tb',
        fit: true,
        fitColumns: true,
        singleSelect: false,
        showFooter:true,
        footer: '#ft',
        rownumbers:true,
        columns: [[{
            title: '供货商',
            field: 'supplier',
            width: '5%'
        }, {
            title: '代码',
            field: 'expCode',
            width: '6%'
        }, {
            title: '产品名称',
            field: 'expName',
            width: '5%'
        }, {
            title: "规格",
            width: '5%',
            field: 'packageSpec'
        }, {
            title: '单位',
            field: 'packageUnits',
            width: '5%'
        }, {
            title: '厂家',
            field: 'firmId',
            width: '5%'
        }, {
            title: '数量',
            width: '5%',
            field: 'quantity'

        }, {
            title: '已结数',
            width: '5%',
            field: 'disburseCount'

        }, {
            title: '批发价',
            width: '5%',
            field: 'purchasePrice',
            type:'numberbox'
        }, {
            title: '扣率',
            width: '5%',
            field: 'discount',
            editor: {type: 'numberbox'}
        }, {
            title: '批发金额',
            width: '5%',
            field: 'purchaseAllPrice',
            type:'numberbox'
        }, {
            title: '取货价',
            width: '5%',
            field: 'tradePrice',
            type:'numberbox'
        }, {
            title: '取货金额',
            width: '5%',
            field: 'tradeAllPrice',
            type:'numberbox'
        }, {
            title: '零售价',
            width: '5%',
            field: 'retailPrice'
        }, {
            title: '零售金额',
            width: '5%',
            field: 'retailAllPrice'
        }, {
            title: '发票号',
            width: '5%',
            field: 'invoiceNo',
            hidden:true
        }, {
            title: '有效日期',
            width: '8%',
            field: 'expireDate',
            formatter: myformatter2
        }, {
            title: '批号',
            width: '5%',
            field: 'batchNo'
        }, {
            title: "入库单号",
            width: '7%',
            field: 'documentNo'
        }]]
    });

    //设置时间
    var curr_time = new Date();
    $("#startDate").datetimebox("setValue", myformatter2(curr_time));
    $("#stopDate").datetimebox("setValue", myformatter2(curr_time));
    $('#startDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: myformatter2,
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
        formatter: myformatter2,
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
    //供应商
    var suppliers = {};
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
        return suppliers;
    });
    promise.done(function () {
        $("#supplier").combogrid({
            idField: 'supplierCode',
            textField: 'supplierName',
            data: suppliers,
            panelWidth: 500,
            fitColumns: true,
            columns: [[{
                title: '供应商名称',field: 'supplierName', width: 200, align: 'center'
            }, {
                title: '供应商代码',field: 'supplierCode', width: 150, align: 'center'
            }, {
                title: '输入码',field: 'inputCode', width: 50, align: 'center'
            }]],
            filter: function (q, row) {
                return $.startWith(row.inputCode.toUpperCase(), q.toUpperCase());
            }
        })
    });
    $('#searchInput').combogrid({
            panelWidth: 500,
            idField: 'expCode',
            textField: 'expName',
            url: '/api/exp-name-dict/list-exp-name-by-input',
            method: 'GET',
            mode: 'remote',
            columns: [[
                {field: 'expCode', title: '编码', width: 150, align: 'center'},
                {field: 'expName', title: '名称', width: 200, align: 'center'},
                {field: 'inputCode', title: '拼音', width: 50, align: 'center'}
            ]],
            pagination: false,
            fitColumns: true,
            rowNumber: true,
            autoRowHeight: false,
            pageSize: 50,
            pageNumber: 1
    });
    $("#searchBtn").on('click', function () {
        var promiseDetail = loadDict();
    })
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var stopDate = $("#stopDate").datetimebox("getText");
            var startDate =$("#startDate").datetimebox("getText");
            var supplier = $("#supplier").combogrid("getText");
            var documentNo = $("#documentNo").textbox("getValue");
            var searchInput = $("#searchInput").textbox("getValue");
            var radio = $("#detailForm input[name='radioOne']:checked").val();
            var hospitalId = parent.config.hospitalId;
            var storage = parent.config.storageCode;
            $("#report").prop("src",parent.config.defaultReportPath + "exp-pay-search.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&documentNo="+documentNo+"&startDate="+startDate+"&stopDate="+stopDate+"&supplier="+supplier+"&radio="+radio+"&searchInput="+searchInput+"&loginId="+parent.config.loginId);
        }
    })
    $("#printBtn").on('click',function(){
        var printData = $("#importDetail").datagrid('getRows');
        if(printData.length<=0){
            $.messager.alert('系统提示','请先查询数据','info');
            return;
        }
        $("#printDiv").dialog('open');

    })
    var importDetailDataVO = {};//传递vo
    var detailsData = [];//信息
    var loadDict = function(){
        importDetailDataVO.stopDate = new Date($("#stopDate").datetimebox("getText"));
        importDetailDataVO.startDate =new Date($("#startDate").datetimebox("getText"));
        importDetailDataVO.supplier = $("#supplier").combogrid("getText");
        importDetailDataVO.documentNo = $("#documentNo").textbox("getValue");
        importDetailDataVO.searchInput = $("#searchInput").textbox("getValue");
        importDetailDataVO.radio = $("#detailForm input[name='radioOne']:checked").val();
        importDetailDataVO.hospitalId = parent.config.hospitalId;
        importDetailDataVO.storage = parent.config.storageCode;
        var promise =$.get("/api/exp-import/exp-pay-search",importDetailDataVO,function(data){
            for(var i = 0 ;i<data.length;i++){
                var impo = [];
                impo.supplier = data[i].supplier;
                impo.expCode = data[i].expCode;
                impo.expName = data[i].expName;
                impo.documentNo = data[i].documentNo;
                impo.itemNo = data[i].itemNo;
                impo.id = data[i].id;
                impo.packageSpec = data[i].packageSpec;
                impo.packageUnits =data[i].packageUnits;
                impo.firmId = data[i].firmId;
                impo.batchNo = data[i].batchNo;
                impo.expireDate = data[i].expireDate;
                impo.purchasePrice = data[i].purchasePrice;
                impo.discount = data[i].discount;
                impo.tradePrice = data[i].tradePrice;
                impo.retailPrice = data[i].retailPrice;
                impo.quantity = data[i].quantity;
                impo.invoiceNo = data[i].invoiceNo;
                impo.invoiceDate= data[i].invoiceDate;
                impo.disburswRecNo = data[i].disburswRecNo;
                impo.discount = data[i].discount;
                impo.disburseCount = data[i].disburseCount;
                impo.needDisburse =data[i].quantity-data[i].disburseCount;
                impo.needPrice = impo.needDisburse*data[i].purchasePrice;
                impo.disbursePrice = data[i].disburseCount*data[i].purchasePrice;
                impo.purchaseAllPrice =data[i].purchasePrice*data[i].quantity;
                impo.tradeAllPrice=data[i].tradePrice*data[i].quantity;
                impo.retailAllPrice=data[i].retailPrice*data[i].quantity;
                detailsData.push(impo);
            }
        },'json');
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