/**
 * 消耗品入库单据查询
 * Created by wangbinbin on 2015/10/22.
 */
function checkRadio(){
    if($("#billing:checked").val()){
        $("#startBill").textbox("enable");
        $("#stopBill").textbox("enable");
    }else{
        $("#startBill").textbox("clear");
        $("#stopBill").textbox("clear");
        $("#startBill").textbox({disabled:true});
        $("#stopBill").textbox({disabled:true});
    }
    if($("#dateTime:checked").val()){
        $("#startDate").datebox("enable");
        $("#stopDate").datebox("enable");
    }else{
        $("#startDate").datebox("clear");
        $("#stopDate").datebox("clear");
        $("#startDate").datebox({disabled:true});
        $("#stopDate").datebox({disabled:true});
    }
    if($("#expName:checked").val()){
        $("#searchInput").combogrid("enable");
    }else{
        $("#searchInput").combogrid("clear");
        $("#searchInput").combogrid({disabled:true});
    }
    if($("#supply:checked").val()){
        $("#supplier").combogrid("enable");
    }else{
        $("#supplier").combogrid("clear");
        $("#supplier").combogrid({disabled:true});
    }
    if($("#importClassName:checked").val()){
        $("#importClass").combobox("enable");
    }else{
        $("#importClass").combobox("clear");
        $("#importClass").combobox({disabled:true});
    }
}
function myFormatter2(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return y + '/' + (m < 10 ? ('0' + m) : m) + '/' + (d < 10 ? ('0' + d) : d);
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
    /**
     * 供货方
     *
     */
    var masterDataVo = {};//主表vo
    var masters = [];//信息
    var currentDocumentNo;//当前账单号
    var flag = 0 ;

    /**
     * 定义主表信息表格
     */
    $("#importMaster").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        showFooter:true,
        rownumbers:true,
        title: "消耗品入库单信息",
        footer: '#ft',
        toolbar:'#expImportDetail',
        columns: [[{
            title: '库房',
            field: 'storage',
            width: '9%',
            editor: {type: 'textbox'}
        }, {
            title: '入库日期',
            field: 'importDate',
            width: '9%'

        }, {
            title: '入库单号',
            field: 'documentNo',
            width: '9%',
            editor: {type: 'textbox'}
        }, {
            title: '供货商',
            field: 'supplier',
            width: '9%',
            editor: {type: 'textbox'}
        }, {
            title: "应付金额",
            width: '9%',
            field: 'accountReceivable'
        }, {
            title: '已付金额',
            field: 'accountPayed',
            width: '9%',
            editor: {type: 'textbox'}
        }, {
            title: '附加费用',
            field: 'additionalFee',
            width: '9%',
            editor: {type: 'numberbox'}
        }, {
            title: '入库类别',
            width: '9%',
            field: 'importClass'

        }, {
            title: '记账',
            width: '9%',
            field: 'accountIndicator'
        }, {
            title: '操作员',
            width: '9%',
            field: 'operator'
        }, {
            title: '作废',
            width: '9%',
            field: 'docStatus'
        }]],
        onClickRow: function (index, row) {
            var row = $("#importMaster").datagrid('getSelected') ;
            currentDocumentNo = row.documentNo;
            $("#retailDialog").dialog('open');
        },
        keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
            enter: function (e) {
                var row = $("#importMaster").datagrid('getSelected') ;
                if (row) {
                    currentDocumentNo = row.documentNo;
                    $("#retailDialog").dialog('open');
                }
                $(this).combogrid('hidePanel');
            }
        })
    });
    //设置时间
    var curr_time = new Date();
    $("#startDate").datebox("setValue", myFormatter2(curr_time));
    $("#stopDate").datebox("setValue", myFormatter2(curr_time));
    //入库分类字典
    $("#importClass").combobox({
        url: '/api/exp-import-class-dict/list',
        valueField: 'importClass',
        textField: 'importClass',
        method: 'GET'
    });
    $('#searchInput').combogrid({
        disabled: true,
        panelWidth: 200,
        idField: 'expCode',
        textField: 'expName',
        url: '/api/exp-name-dict/list-exp-name-by-input',
        method: 'GET',
        mode: 'remote',
        columns: [[
            {field: 'expCode', title: '消耗品代码', width: 100},
            {field: 'expName', title: '消耗品名称', width: 100}
        ]],
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        pageSize: 50,
        pageNumber: 1
    });
    //供应商
    var suppliers = {};
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
        return suppliers;
    });
    promise.done(function () {
        $("#supplier").combogrid({
            idField: 'supplierName',
            textField: 'supplierName',
            data: suppliers,
            panelWidth: 200,
            columns: [[{
                title: '供应商名称',
                field: 'supplierName'
            }, {
                title: '供应商代码',
                field: 'supplierCode'
            }, {
                title: '输入码',
                field: 'inputCode'
            }]],
            filter: function (q, row) {
                return $.startWith(row.inputCode.toUpperCase(), q.toUpperCase());
            }
        })
    });
    $("#searchBtn").on('click', function () {
        var promiseMaster = loadDict();
        promiseMaster.done(function(){
            if(masters.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                return;
            }
            $("#importMaster").datagrid('loadData',masters);
        })
    })
    var loadDict = function(){
        masterDataVo.imClass = $("#importClass").combobox("getText");
        masterDataVo.startBill = $("#startBill").textbox("getText");
        masterDataVo.stopBill = $("#stopBill").textbox("getText");
        masterDataVo.classRadio = $("#detailForm input[name='radioOne']:checked").val();
        masterDataVo.billRadio = $("#detailForm input[name='radioTwo']:checked").val();
        if($("#dateTime:checked").val()){
            masterDataVo.startDate = $("#startDate").datebox("getText");
            masterDataVo.stopDate = $("#stopDate").datebox("getText");
        }else{
            $("#startDate").datebox("clear");
            $("#stopDate").datebox("clear");
            masterDataVo.startDate = $("#startDate").datebox("getText");
            masterDataVo.stopDate = $("#stopDate").datebox("getText");
        }
        masterDataVo.supplier = $("#supplier").combogrid("getText");
        masterDataVo.searchInput = $("#searchInput").combogrid("getValue");
        masterDataVo.hospitalId = parent.config.hospitalId;
        masterDataVo.storage = parent.config.storageCode;
        console.log(masterDataVo);
        var promise =$.get("/api/exp-import/exp-import-document-search",masterDataVo,function(data){
            for(var i = 0 ;i<data.length;i++){
                if(data[i].accountIndicator=='1'){
                    data[i].accountIndicator='已记账';
                }
                if(data[i].accountIndicator=='0'|| data[i].accountIndicator==null ){
                    data[i].accountIndicator='未记账';
                }
                if(data[i].docStatus=='1' ||data[i].docStatus==null){
                    data[i].docStatus='作废';
                }
                if(data[i].docStatus=='0'){
                    data[i].docStatus='未作废';
                }
            }
            masters =data ;
        },'json');
        return promise;

    }
    $("#retailDialog").dialog({
        title: '消耗品入库明细',
        width: "80%",
        height: 300,
        closed: false,
        inline:true,
        catch: false,
        modal: true,
        closed: true,
        onBeforeOpen: function () {
            $("#importRetailData").datagrid('load', {documentNo: currentDocumentNo,hospitalId :parent.config.hospitalId});
            $("#importRetailData").datagrid('selectRow', 0)
        }
    });
    $("#importRetailData").datagrid({
        singleSelect: true,
        fit: true,
        fitColumns: true,
        url: '/api/exp-import/exp-import-document-detail-search/',
        method: 'GET',
        columns: [[{
            title: '付价',
            field: 'accountReceivable'
        }, {
            title: '产品名称',
            field: 'expName'
        }, {
            title: '数量',
            field: 'quantity'
        }, {
            title: '规格',
            field: 'expSpec'
        }, {
            title: '批号',
            field: 'batchNo'
        }, {
            title: '单位',
            field: 'units'
        }, {
            title: '有效期',
            field: 'expireDate',
            formatter:formatterDate
        }, {
            title: '厂家',
            field: 'firmId'
        }, {
            title: '批发价',
            field: 'tradePrice'
        }, {
            title: '零售价',
            field: 'retailPrice'
        }, {
            title: '进价',
            field: 'purchasePrice'
        }, {
            title: '折扣',
            field: 'discount'
        }, {
            title: '发票号',
            field: 'invoiceNo'
        }, {
            title: '发票日期',
            field: 'invoiceDate',
            formatter:formatterDate
        }, {
            title: '结存',
            field: 'inventory'
        }, {
            title: '许可证号',
            field: 'licenceno'
        }, {
            title: '注册证号',
            field: 'registno'
        }, {
            title: '备注',
            field: 'memo'
        }]],
        onLoadSuccess:function(data){
            flag = flag+1;
            if(flag==2){
                if(data.total==0 ){
                    $.messager.alert('系统提示','库房暂无该入库单据明细','info');
                    $("#retailDialog").dialog('close');
                }
                flag=0;
            }
        }
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
})