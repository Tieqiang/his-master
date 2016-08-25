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
        $("#startDate").datetimebox("enable");
        $("#stopDate").datetimebox("enable");
    }else{
        $("#startDate").datetimebox("setValue","");
        $("#stopDate").datetimebox("setValue","");
        $("#startDate").datetimebox({disabled:true});
        $("#stopDate").datetimebox({disabled:true});
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

$(function () {
    //库房字典
    var storageDept = [];
    $.get("/api/exp-storage-dept/list?hospitalId=" + parent.config.hospitalId, function (data) {
        storageDept = data;
    });
    //格式化日期函数
    function formatterDate(val, row) {
//        if(val.index("1970")>0){
//            return null;
//        }
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
//            var h = date.getHours();
//            var mm = date.getMinutes();
//            var s = date.getSeconds();
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) ;
//                + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
            return dateTime
        }
    }
    function formatterDate2(val, row) {
       if (val != null) {
        var date = new Date(val);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var h = 00;
        var mm = 00;
        var s = 00;
        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' '
            + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
        return dateTime
    }
    }
    function formatterDate3(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var h =23;
            var mm =59;
            var s =59;
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

    /**
     * 供货方
     *
     */
    var masterDataVo = {};//主表vo
    var masters = [];//信息
    var currentDocumentNo;//当前账单号
    var flag = 0 ;
    //供应商
    var suppliers = {};
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
        return suppliers;
    });

    /**
     * 定义主表信息表格
     */
    $("#importMaster").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        //showFooter:true,
        //rownumbers:true,
        title: "本库房入库单据查询",
        footer: '#ft',
        toolbar:'#expImportDetail',
        columns: [[{
            title: '库房',
            field: 'storage',
            hidden:true
        }, {
            title: '入库单号',
            field: 'documentNo',
            width: '10%'
        }, {
            title: '入库日期',
            field: 'importDate',
            width: '10%',
            formatter: formatterDate
        }, {
            title: '供货商',
            field: 'supplier',
            width: '10%',
            formatter: function (value, row, index) {
                for (var i = 0; i < suppliers.length; i++) {
                    if (value == suppliers[i].supplierCode) {
                        return suppliers[i].supplierName;
                    }
                }
                return value;
            }
        }, {
            title: "应付金额",
            width: '10%',
            field: 'accountReceivable'
        }, {
            title: '已付金额',
            field: 'accountPayed',
            width: '10%'
        }, {
            title: '附加费用',
            field: 'additionalFee',
            width: '10%'
        }, {
            title: '入库类别',
            width: '10%',
            field: 'importClass'
        }, {
            title: '记账',
            width: '10%',
            field: 'accountIndicator'
        }, {
            title: '操作员',
            width: '10%',
            field: 'operator'
        }, {
            title: '作废',
            width: '10%',
            field: 'docStatus'
        }]],
        onClickRow: function(index,row){
            var printData = $("#importMaster").datagrid('getSelections');
            if (printData.length != 1) {
                $.messager.alert('系统提示', '只能先选择一条数据!', 'error');
                return;
            }
            $("#printDiv").dialog('open');
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
    //查看明细
    $('#lookDetail').on('click',function(){
        var row = $("#importMaster").datagrid('getSelected');
        currentDocumentNo = row.documentNo;
        $("#retailDialog").dialog('open');
    });

    //设置时间
    $('#startDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate2,
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
        value: 'dateTime',
        required: true,
        showSeconds: true,
        formatter: formatterDate3,
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
    //入库分类字典
    $("#importClass").combobox({
        url: '/api/exp-import-class-dict/list',
        valueField: 'importClass',
        textField: 'importClass',
        method: 'GET'
    });
    $('#searchInput').combogrid({
        disabled: true,
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

    promise.done(function () {
        $("#supplier").combogrid({
            idField: 'supplierCode',
            textField: 'supplierName',
            data: suppliers,
            panelWidth: 500,
            fitColumns: true,
            columns: [[{
                title: '供应商名称',
                field: 'supplierName', width: 200, align: 'center'
            }, {
                title: '供应商代码',
                field: 'supplierCode', width: 150, align: 'center'
            }, {
                title: '输入码',
                field: 'inputCode', width: 50, align: 'center'
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
            masterDataVo.startDate = $("#startDate").datetimebox("getText");
            masterDataVo.stopDate = $("#stopDate").datetimebox("getText");
        }else{
            $("#startDate").datetimebox("clear");
            $("#stopDate").datetimebox("clear");
            $("#startDate").datetimebox({disabled: true});
            $("#stopDate").datetimebox({disabled: true});
            masterDataVo.startDate ="";
            masterDataVo.stopDate="";
        }
        masterDataVo.supplier = $("#supplier").combogrid("getValue");
        masterDataVo.searchInput = $("#searchInput").combogrid("getValue");
        masterDataVo.hospitalId = parent.config.hospitalId;
        masterDataVo.storage = parent.config.storageCode;
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
        left: 100,
        top: 100,
        resizable: true,
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
        columns: [[ {
            title: '产品名称',
            field: 'expName'
        }, {
            title: '数量',
            field: 'quantity'
        }, {
            title: '应付款',
            field: 'accountReceivable'
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
            field: 'purchasePrice'
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

//打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        buttons: '#printft',
        closed: true,
        onOpen: function () {
            var printData = $("#importMaster").datagrid('getSelections');
            var  printDocumentNo=printData[0].documentNo;
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-import.cpt&documentNo=" + printDocumentNo;
            $("#report").prop("src",cjkEncode(https));
        }

    });
    $("#printBtn").on('click', function () {
        var printData = $("#importMaster").datagrid('getSelections');
        if (printData.length !=1) {
            $.messager.alert('系统提示', '只能先选择一条数据!', 'error');
            return;
        }
        $("#printDiv").dialog('open');

    })
    /**
     * 打印
     */
    $("#printBtn").on('click', function () {
        if(saveFlag){
            $("#printDiv").dialog('open');
        }else{
            var printData = $("#importDetail").datagrid('getRows');
            if (printData.length <= 0) {
                $.messager.alert('系统提示', '请先查询数据', 'info');
                return;
            }
            $("#printDiv").dialog('open');
        }

    })
    /**
     * 关闭
     */
    $("#printClose").on("click",function(){
        $("#printDiv").dialog('close');

    })
})