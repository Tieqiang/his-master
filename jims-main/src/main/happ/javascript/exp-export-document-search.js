///**
// * 消耗品出库单据查询
// * Created by wangbinbin on 2015/10/26.
// */
function checkRadio() {
    if ($("#billing:checked").val()) {
        $("#startBill").textbox("enable");
        $("#stopBill").textbox("enable");
    } else {
        $("#startBill").textbox("clear");
        $("#stopBill").textbox("clear");
        $("#startBill").textbox({disabled: true});
        $("#stopBill").textbox({disabled: true});
    }
    if ($("#dateTime:checked").val()) {
        $("#startDate").datetimebox("enable");
        $("#stopDate").datetimebox("enable");
    } else {
        $("#startDate").datebox("clear");
        $("#stopDate").datebox("clear");
        $("#startDate").datetimebox({disabled: true});
        $("#stopDate").datetimebox({disabled: true});
    }
    if ($("#expName:checked").val()) {
        $("#searchInput").combogrid("enable");
    } else {
        $("#searchInput").combogrid("clear");
        $("#searchInput").combogrid({disabled: true});
    }
    if ($("#dept:checked").val()) {
        $("#depts").combogrid("enable");
    } else {
        $("#depts").combogrid("clear");
        $("#depts").combogrid({disabled: true});
    }
    if ($("#exportClassName:checked").val()) {
        $("#exportClass").combobox("enable");
    } else {
        $("#exportClass").combobox("clear");
        $("#exportClass").combobox({disabled: true});
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
    var flag = 0;
    //库房字典
    var storageDept = [];
    $.get("/api/exp-storage-dept/listLevelDown?hospitalId=" + parent.config.hospitalId + "&storageCode=" + parent.config.storageCode, function (data) {
        storageDept = data;
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
                + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
            return dateTime
        }
    }
    function formatterDate2(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var h =00;
            var mm =00;
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
    //供应商
    var suppliers = {};
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
        return suppliers;
    });
    /**
    * 定义主表信息表格
    */
    $("#exportMaster").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        showFooter: true,
        rownumbers: true,
        title: "本库房出库单据查询",
        footer: '#ft',
        toolbar:'#expExportDetail',
        columns: [[{
            title: '库房',
            field: 'storage',
            hidden:true,
            width: '9%',
            editor: {type: 'textbox'}
        }, {
            title: '出库单号',
            field: 'documentNo',
            align: 'center',
            width: '9%',
            editor: {type: 'textbox'}
        }, {
            title: '出库日期',
            field: 'exportDate',
            align: 'center',
            width: '11%',
            formatter: formatterDate
        }, {
            title: '收货方',
            field: 'receiver',
            align: 'center',
            width: '15%',
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
            width: '9%',
            align: 'right',
            field: 'accountReceivable'
        }, {
            title: '已付金额',
            field: 'accountPayed',
            align: 'right',
            width: '9%',
            editor: {type: 'textbox'}
        }, {
            title: '附加费用',
            field: 'additionalFee',
            align: 'right',
            width: '9%',
            editor: {type: 'numberbox'}
        }, {
            title: '出库类别',
            width: '9%',
            align: 'center',
            field: 'exportClass'

        }, {
            title: '记账',
            width: '9%',
            align: 'center',
            field: 'accountIndicator'
        }, {
            title: '操作员',
            width: '9%',
            align: 'center',
            field: 'operator'
        }, {
            title: '作废',
            width: '9%',
            align: 'center',
            field: 'docStatus'
        }]],
        onClickRow: function (index, row) {
            var printData = $("#exportMaster").datagrid('getSelections');
            if (printData.length != 1) {
                $.messager.alert('系统提示', '只能选择一条数据!', 'error');
                return;
            }
            $("#printDiv").dialog('open');
        },
        keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
            enter: function (e) {
                var row = $("#exportMaster").datagrid('getSelected');
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
        var row = $("#exportMaster").datagrid('getSelected');
        if(null == row || row == {} || typeof(row) == 'undefined'){
            $.messager.alert('系统提示','请选择一条记录查看明细!','info');
            return ;
        }
        currentDocumentNo = row.documentNo;
        $("#retailDialog").dialog('open');
    });

    //出库分类字典
    $("#exportClass").combobox({
        url: '/api/exp-export-class-dict/list',
        valueField: 'exportClass',
        textField: 'exportClass',
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
        //pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false
        //pageSize: 50,
        //pageNumber: 1
    });
    //科室字典
    var depts = {};
    var promise = $.get("/api/dept-dict/list?hospitalId=" + parent.config.hospitalId, function (data) {
        depts = data;
        return depts;
    });
    promise.done(function () {
        $("#depts").combogrid({
            idField: 'supplierCode',
            textField: 'supplierName',
            data: suppliers,
            panelWidth: 500,
            fitColumns: true,
            columns: [[{
                title: '科室名称',
                field: 'supplierName', width: 200, align: 'center'
            }, {
                title: '科室代码',
                field: 'supplierCode', width: 150, align: 'center'
            }, {
                title: '输入码',
                field: 'inputCode', width: 50, align: 'center'
            }]],
            filter: function (q, row) {
                if($.startWith(row.inputCode.toUpperCase(), q.toUpperCase())){
                    return $.startWith(row.inputCode.toUpperCase(), q.toUpperCase());
                }
                var opts = $(this).combogrid('options');
                return row[opts.textField].indexOf(q) == 0;

            }
        })
    });

    $("#searchBtn").on('click', function () {
        var promiseMaster = loadDict();
        promiseMaster.done(function () {
            if(masters.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                return;
            }
            $("#exportMaster").datagrid('loadData', masters);

        })
    })
    var loadDict = function () {
        masterDataVo.imClass = $("#exportClass").combobox("getText");
        masterDataVo.startBill = $("#startBill").textbox("getText");
        masterDataVo.stopBill = $("#stopBill").textbox("getText");
        masterDataVo.classRadio = $("#detailForm input[name='radioOne']:checked").val();
        masterDataVo.billRadio = $("#detailForm input[name='radioTwo']:checked").val();
        if ($("#dateTime:checked").val()) {
            masterDataVo.startDate = $("#startDate").datetimebox('getText');
            masterDataVo.stopDate = $("#stopDate").datetimebox('getText');
        } else {
            $("#startDate").datebox("clear");
            $("#stopDate").datebox("clear");
            $("#startDate").datetimebox({disabled: true});
            $("#stopDate").datetimebox({disabled: true});
            masterDataVo.startDate ="";
            masterDataVo.stopDate = "";
        }
        masterDataVo.receiver = $("#depts").combogrid("getValue");
        masterDataVo.searchInput = $("#searchInput").combogrid("getValue");
        masterDataVo.hospitalId = parent.config.hospitalId;
        masterDataVo.storage = parent.config.storageCode;
        var promise = $.get("/api/exp-export/exp-export-document-search", masterDataVo, function (data) {
            $.each(data,function(index,item){
                item.accountReceivable = fmoney(item.accountReceivable,2);  //应付金额
                item.accountPayed = fmoney(item.accountPayed,2);        //已付金额
                item.additionalFee = fmoney(item.additionalFee,2);      //附加费用
            });
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
            masters = data;
        }, 'json');
        return promise;

    }
    $("#retailDialog").dialog({
        title: '消耗品出库明细',
        width: "80%",
        height: 300,
        left:100,
        top:100,
        resizable: true,
        closed: false,
        inline: true,
        catch: false,
        modal: true,
        closed: true,
        onBeforeOpen: function () {
            $("#exportRetailData").datagrid('load', {documentNo: currentDocumentNo, hospitalId: parent.config.hospitalId});
            $("#exportRetailData").datagrid('selectRow', 0)
        }
    });
    $("#exportRetailData").datagrid({
        singleSelect: true,
        fit: true,
        fitColumns: true,
        url: '/api/exp-export/exp-export-document-detail-search/',
        method: 'GET',
        columns: [[{
            title: '产品名称',
            field: 'expName',
            align: 'center',
            width: '11%'
        }, {
            title: '数量',
            field: 'quantity',
            align: 'center',
            width: '7%'
        }, {
            title: '规格',
            field: 'expSpec',
            align: 'center',
            width: '11%'
        }, {
            title: '批号',
            field: 'batchNo',
            align: 'center',
            width: '11%'
        }, {
            title: '单位',
            field: 'units',
            align: 'center',
            width: '7%'
        }, {
            title: '有效期',
            field: 'expireDate',
            align: 'center',
            width:'11%',
            formatter: formatterDate
        }, {
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: '11%'
        }, {
            title: '批发价',
            field: 'tradePrice',
            align: 'center',
            width: '7%'
        }, {
            title: '零售价',
            field: 'retailPrice',
            align: 'center',
            width: '7%'
        }, {
            title: '进价',
            field: 'purchasePrice',
            align: 'center',
            width: '7%'
        }, {
            title: '结存',
            field: 'inventory',
            align: 'center',
            width: '7%'
        }, {
            title: '零价合计',
            field: 'zeroAccount',
            align: 'center',
            width: '7%'
        }]],
        onLoadSuccess:function(data){
            flag = flag+1;
            if(flag==2){
                if(data.total==0 ){
                    $.messager.alert('系统提示','库房暂无该出库单据明细','info');
                    $("#retailDialog").dialog('close');
                }
                flag=0;
            }
        }
    });


    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var printData = $("#exportMaster").datagrid('getSelections');
            var  printDocumentNo=printData[0].documentNo;
            var row = $('#exportMaster').datagrid('getSelected');
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-export.cpt&documentNo=" + printDocumentNo + '&hospitalId=' + parent.config.hospitalId + '&storageCode=' + parent.config.storageCode + '&exportClass=' + row.exportClass;
            $("#report").prop("src",cjkEncode(https));
        }
    })
    $("#printBtn").on('click', function () {
        var printData = $("#exportMaster").datagrid('getSelections');
//        alert(printData.length);
        if (printData.length!=1) {
            $.messager.alert('系统提示', '只能选择一条数据!', 'error');
            return;
        }
        $("#printDiv").dialog('open');
    })

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