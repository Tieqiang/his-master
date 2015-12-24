/**
 * 付款处理
 * Created by wangbinbin on 2015/10/28.
 */


function checkRadio(){
    if($("#dateTime:checked").val()){
        $("#startDate").datetimebox("enable");
        $("#stopDate").datetimebox("enable");
    }else{
        $("#startDate").datetimebox("clear");
        $("#stopDate").datetimebox("clear");
        $("#startDate").datetimebox({disabled:true});
        $("#stopDate").datetimebox({disabled:true});
    }
    if($("#supply:checked").val()){
        $("#supplier").combogrid("enable");
    }else{
        $("#supplier").combogrid("clear");
        $("#supplier").combogrid({disabled:true});
    }
    if($("#importDocument:checked").val()){
        $("#documentNo").textbox("enable");
    }else{
        $("#documentNo").textbox("clear");
        $("#documentNo").textbox({disabled:true});
    }
}
$(function () {
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
    var editRowIndex  ; //编辑行

    /**
     * 定义明细表格
     */
    $("#importDetail").datagrid({
        title: "付款处理",
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
            width: '6%'
        }, {
            title: '代码',
            field: 'expCode',
            width: '6%'
        }, {
            title: '产品名称',
            field: 'expName',
            width: '6%'
        }, {
            title: "入库单号",
            width: '6%',
            field: 'documentNo'
        }, {
            title: "规格",
            width: '5%',
            field: 'packageSpec'
        }, {
            title: '厂家',
            field: 'firmId',
            width: '6%'
        }, {
            title: '单位',
            field: 'packageUnits',
            width: '5%'
        }, {
            title: '数量',
            width: '5%',
            field: 'quantity'

        }, {
            title: '日期',
            width: '11%',
            field: 'expireDate',
            formatter: myformatter2
        }, {
            title: '批号',
            width: '5%',
            field: 'batchNo'
        }, {
            title: '待结算数',
            width: '5%',
            field: 'needDisburse',
            editor: {type: 'numberbox'}
        }, {
            title: '已结数量',
            width: '5%',
            field: 'disburseCount',
            type:'numberbox',
            formatter: function(value,row,index){
                if (row.disburseCount){
                    return row.disburseCount.toFixed(2);
                } else {
                    return value;
                }
            }
        }, {
            title: '入库价',
            width: '5%',
            field: 'purchasePrice',
            type:'numberbox',
            formatter: function(value,row,index){
                if (row.purchasePrice){
                    return row.purchasePrice.toFixed(2);
                } else {
                    return value;
                }
            }
        }, {
            title: '入库金额',
            width: '5%',
            field: 'purchaseAllPrice',
            type:'numberbox',
            formatter: function(value,row,index){
                if (row.purchaseAllPrice){
                    return row.purchaseAllPrice.toFixed(2);
                } else {
                    return value;
                }
            }
        }, {
            title: '待结金额',
            width: '5%',
            field: 'needPrice',
            type:'numberbox',
            formatter: function(value,row,index){
                if (row.needPrice){
                    return row.needPrice.toFixed(2);
                } else {
                    return value;
                }
            }
        }, {
            title: '已结金额',
            width: '6%',
            field: 'disbursePrice',
            type:'numberbox',
            formatter: function(value,row,index){
                if (row.disbursePrice){
                    return row.disbursePrice.toFixed(2);
                } else {
                    return value;
                }
            }
        }, {
            title: '批发价',
            width: '5%',
            field: 'tradePrice',
            type:'numberbox',
            formatter: function(value,row,index){
                if (row.tradePrice){
                    return row.tradePrice.toFixed(2);
                } else {
                    return value;
                }
            }
        }, {
            title: '零售价',
            width: '5%',
            field: 'retailPrice',
            type:'numberbox',
            formatter: function(value,row,index){
                if (row.retailPrice){
                    return row.retailPrice.toFixed(2);
                } else {
                    return value;
                }
            }
        }, {
            title: '顺序号',
            width: '5%',
            field: 'itemNo',
            hidden:true
        }, {
            title: 'id',
            width: '5%',
            field: 'id',
            hidden:true
        }]],onClickRow: function (rowIndex,rowData) {

            var needDis = rowData.needDisburse;
            if(editRowIndex==undefined){
                $(this).datagrid("beginEdit",rowIndex);
            }
            if (editRowIndex!=undefined && editRowIndex!=rowIndex){
                $(this).datagrid("endEdit",editRowIndex);
                $(this).datagrid("beginEdit",rowIndex);
            }
            if(editRowIndex==rowIndex){
                //rowData.needDisburse = needDis;
                //$(this).datagrid("reload",{needDisburse:needDis})
                $(this).datagrid("endEdit",editRowIndex);
                editRowIndex = undefined;
                return;
            }

            //var upperLevel = $("#dg").datagrid("getEditor",{index:rowIndex,field:"needDisburse"});
            editRowIndex = rowIndex;
        }
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

    var loadDocument = function(){
        var document = {};
        var promiseDocument = $.get('/api/exp-storage-dept/list-storage?hospitalId='+parent.config.hospitalId+"&storageCode="+parent.config.storageCode,function(data){
            for(var i =0;i<data.length;i++){
                var zero = data[i].disburseNoPrefix.length>2?"000":"0000";
                var documentNo = data[i].disburseNoPrefix+zero+data[i].disburseNoAva;
                $('#payDocument').textbox('setValue',documentNo);
            }
        }) ;
    }
    loadDocument();
    var importDetailDataVO = {};//传递vo
    var detailsData = [];//信息
    $("#searchBtn").on('click', function () {
        var promiseDetail = loadDict();
    })
    var loadDict = function(){
        if($("#dateTime:checked").val()){
            importDetailDataVO.startDate = new Date($("#startDate").datetimebox("getText"));
            importDetailDataVO.stopDate = new Date($("#stopDate").datetimebox("getText"));
        }else{
            $("#startDate").datetimebox("clear");
            $("#stopDate").datetimebox("clear");
            importDetailDataVO.startDate = $("#startDate").datetimebox("getText");
            importDetailDataVO.stopDate = $("#stopDate").datetimebox("getText");
        }
        importDetailDataVO.supplier = $("#supplier").combogrid("getText");
        importDetailDataVO.documentNo = $("#documentNo").textbox("getValue");
        importDetailDataVO.hospitalId = parent.config.hospitalId;
        importDetailDataVO.storage = parent.config.storageCode;
        if(importDetailDataVO.documentNo.length==0&&importDetailDataVO.startDate.length==0&&importDetailDataVO.supplier.length==0){
            $.messager.alert('系统提示','请选择查询参数','info');
            return;
        }
        var promise =$.get("/api/exp-import/exp-pay-manage",importDetailDataVO,function(data){
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
                if(data[i].disburseCount == null ||data[i].disburseCount==undefined){
                    data[i].disburseCount=0;
                }
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
    var expDisburseVo = {};
    var expDisburseRecDetailBeanChangeVo = {};
    var expDisburseRecBeanChangeVo ={};
    expDisburseVo.expDisburseRecBeanChangeVo = expDisburseRecBeanChangeVo;
    expDisburseVo.expDisburseRecDetailBeanChangeVo = expDisburseRecDetailBeanChangeVo;
    $("#editBtn").on('click',function(){
        if(editRowIndex!=undefined){
            $("#importDetail").datagrid('endEdit',editRowIndex);
            editRowIndex = undefined;
        }
        var rows = $("#importDetail").datagrid('getSelections');
        if(rows.length<=0){
            $.messager.alert('系统提示','请选择填付的行','info');
            return;
        }
        for(var i =0 ;i<rows.length;i++){
            var index =1+$("#importDetail").datagrid('getRowIndex',rows[i]);
            if(rows[i].needDisburse<=0){
                $.messager.alert('系统提示','第'+index+'行的“待结算数”不允许出现小于等于0的数','info');
                loadDict();
                clearData();
                return;
            }
            if(rows[i].needDisburse>rows[i].quantity){
                $.messager.alert('系统提示','第'+index+'行的“待结算数”不允许出现大于当前行“数量”的数','info');
                loadDict();
                clearData();
                return;
            }
            for(var j=0 ;j<rows.length;j++){
                var index2 = 1+$("#importDetail").datagrid("getRowIndex",rows[j]);
                if(rows[i].supplier != rows[j].supplier){
                    $.messager.alert('系统提示','第'+index+'行和第'+index2+'的“供应商不一致，不能出现在同一张付款单上”，要求供应商一致才能进行“填付”操作','info');
                    loadDict();
                    clearData();
                    return;
                }
            }
        }
        var payPrice = 0 ;
        var detailPrice = 0 ;
        var tradePrice = 0 ;

        for(var i = 0;i<rows.length;i++){
            rows[i].needPrice = rows[i].needDisburse*rows[i].purchasePrice;
            var index1 = $("#importDetail").datagrid("getRowIndex",rows[i]);
            $('#importDetail').datagrid('refreshRow',index1);
            $('#supplierUnits').textbox('setValue',rows[0].supplier);
            payPrice += rows[i].purchasePrice*rows[i].needDisburse;
            detailPrice +=rows[i].retailPrice*rows[i].needDisburse;
            tradePrice +=rows[i].tradePrice*rows[i].needDisburse;
        }
        $('#payPrice').textbox('setValue',payPrice);
        $('#detailPrice').textbox('setValue',detailPrice);
        $('#tradePrice').textbox('setValue',tradePrice);
        expDisburseRecDetailBeanChangeVo.inserted=[];
        for(var i = 0 ;i<rows.length;i++){
            var detail = {};
            //var rowIndex = $("#importDetail").datagrid('getRowIndex', rows[i]);
            detail.disburseRecNo = $("#payDocument").textbox("getValue");
            detail.documentNo = rows[i].documentNo;
            detail.itemNo = rows[i].itemNo;
            detail.disburseCount = parseFloat(rows[i].disburseCount) + parseFloat(rows[i].needDisburse);
            detail.payAmount = rows[i].purchasePrice;
            detail.retailAmount = rows[i].retailPrice;
            detail.tradeAmount = rows[i].tradePrice;
            detail.id = rows[i].id;
            detail.hospitalId = parent.config.hospitalId;
            expDisburseRecDetailBeanChangeVo.inserted.push(detail);
        }

    })
    $("#saveBtn").on("click",function(){
        expDisburseRecBeanChangeVo.inserted = [];
        var rec ={};
        rec.disburseRecNo = $("#payDocument").textbox('getValue');
        rec.storage = parent.config.storageCode;
        rec.receiver = $("#supplierUnits").textbox('getValue');
        rec.payAmount = $("#payPrice").textbox('getValue');
        rec.retailAmount = $("#detailPrice").textbox('getValue');
        rec.tradeAmount = $("#tradePrice").textbox('getValue');
        rec.bank = $("#bank").textbox('getValue');
        rec.disburseDate = new Date().getTime();
        rec.operator = parent.config.loginName;
        rec.hospitalId  = parent.config.hospitalId;
        expDisburseRecBeanChangeVo.inserted.push(rec);
        $.postJSON("/api/exp-dis/dis", expDisburseVo, function (data) {
            loadDocument();
            loadDict();
            $.messager.alert('系统提示', '保存成功', 'success');
        }, function (data) {
            //$.messager.alert("系统提示", data.errorMessage, 'error');
            $.messager.alert("系统提示", '保存失败', 'error');
            loadDocument();
            loadDict();
        })
        clearData();

    })
    var clearData = function(){
        $('#supplierUnits').textbox('clear');
        $('#payPrice').textbox('clear');
        $('#detailPrice').textbox('clear');
        $('#tradePrice').textbox('clear');
    }
})