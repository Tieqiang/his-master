/**
 * 产品上账
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
        $("#startDate").datetimebox("clear");
        $("#stopDate").datetimebox("clear");
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
    var stopEdit = function () {
        if (editRowIndex != undefined) {
            $("#importMaster").datagrid('endEdit', editRowIndex);
            $('#importMaster').datagrid('refreshRow',editRowIndex);
            editRowIndex = undefined;
        }
    }
    var editRowIndex ;
    var masterDataVo = {};//主表vo
    var masters = [];//信息


    /**
     * 定义主表信息表格
     */
    $("#importMaster").datagrid({
        title: "产品上账",
        footer: '#ft',
        toolbar:'#tb',
        fit: true,
        fitColumns: true,
        singleSelect: false,
        selectOnCheck:true,
        checkOnSelect:true,
        showFooter:true,
        rownumbers:true,
        columns: [[{
            title: '入库单号',
            field: 'documentNo',
            width: '7%'
        },{
            title: 'id',
            field: 'id',
            width: '7%',
            hidden :true
        },{
            title: 'detailId',
            field: 'detailId',
            width: '7%',
            hidden :true
        },{
            title: '库房',
            field: 'storage',
            width: '7%'
        }, {
            title: '入库日期',
            field: 'importDate',
            width: '7%',
            formatter: myFormatter2
        },  {
            title: '供货商',
            field: 'supplier',
            width: '7%'
        }, {
            title: '入库类别',
            width: '7%',
            field: 'importClass'

        }, {
            title: "顺序号",
            width: '5%',
            field: 'itemNo'
        }, {
            title: '消耗品',
            field: 'expName',
            width: '7%'
        }, {
            title: '规格',
            field: 'expSpec',
            width: '5%'
        }, {
            title: '单位',
            width: '5%',
            field: 'units'
        }, {
            title: '单价',
            width: '7%',
            field: 'purchasePrice'
        }, {
            title: '数量',
            width: '5%',
            field: 'quantity'
        }, {
            title: '总价',
            width: '6%',
            field: 'payAmount'
        }, {
            title: '发票号',
            width: '6%',
            field: 'invoiceNo',
            editor: {type: 'textbox'}
        }, {
            title: '发票日期',
            width: '9%',
            field: 'invoiceDate',

            editor: {
                type: 'datetimebox',
                options: {
                    value: 'dateTime',
                    showSeconds: true,
                    formatter: myFormatter2,
                    parser: w3,
                    onSelect: function (date) {
                        var dateEd = $("#importMaster").datagrid('getEditor', {
                            index: editRowIndex,
                            field: 'invoiceDate'
                        });
                        var y = date.getFullYear();
                        var m = date.getMonth() + 1;
                        var d = date.getDate();
                        var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
                        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
                        $(dateEd.target).datetimebox('setValue', dateTime);
                        $(this).datetimebox('hidePanel');
                    }
                }
            }
        }, {
            title: '上账：',
            width: '5%',
            field: 'accountIndicator'
        }, {
            width: '4%',
            field: 'key',
            checkbox:true
        }]],
        onLoadSuccess:function(data){
            if(data){
                $.each(data.rows, function(index, item){
                    if(item.accountIndicator=='已记账'){
                        $('#importMaster').datagrid('checkRow', index);
                    }
                });
            }
        },onClickRow:function(rowIndex,rowData){
            if(rowData.accountIndicator=='未记账'){
                stopEdit();
                $(this).datagrid("beginEdit",rowIndex);
                editRowIndex = rowIndex;
            }
        },onUncheck:function(rowIndex,rowData){
            rowData.accountIndicator='未记账'
            $(this).datagrid("refreshRow",rowIndex);
        },onCheck:function(rowIndex,rowData){
            rowData.accountIndicator='已记账'
            $(this).datagrid("refreshRow",rowIndex);
        }
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
        loadDict();
    })
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var imClass = $("#importClass").combobox("getText");
            var startBill = $("#startBill").textbox("getText");
            var stopBill = $("#stopBill").textbox("getText");
            var billRadio = $("#detailForm input[name='radioTwo']:checked").val();
            if($("#dateTime:checked").val()){
                var startDate = $("#startDate").datetimebox("getText");
                var stopDate = $("#stopDate").datetimebox("getText");
            }else{
                $("#startDate").datetimebox("clear");
                $("#stopDate").datetimebox("clear");
                 startDate = $("#startDate").datetimebox("getText");
                 stopDate = $("#stopDate").datetimebox("getText");
            }
            var supplier = $("#supplier").combogrid("getText");
            var expCode = $("#searchInput").combogrid("getValue");
            var hospitalId = parent.config.hospitalId;
            var storage = parent.config.storageCode;
            $("#report").prop("src",parent.config.defaultReportPath + "/exp/exp_print/exp-do-account.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&imClass="+imClass+"&startBill="+startBill+"&stopBill="+stopBill+"&startDate="+startDate+"&stopDate="+stopDate+"&supplier="+supplier+"&billRadio="+billRadio+"&expCode="+expCode+"&loginId="+parent.config.loginId);
        }
    })
    $("#printBtn").on('click',function(){
        var printData = $("#importMaster").datagrid('getRows');
        if(printData.length<=0){
            $.messager.alert('系统提示','请先查询数据','info');
            return;
        }
        $("#printDiv").dialog('open');

    })
    var loadDict = function(){
        masterDataVo.imClass = $("#importClass").combobox("getText");
        masterDataVo.startBill = $("#startBill").textbox("getText");
        masterDataVo.stopBill = $("#stopBill").textbox("getText");
        masterDataVo.billRadio = $("#detailForm input[name='radioTwo']:checked").val();
        if($("#dateTime:checked").val()){
            masterDataVo.startDate = new Date($("#startDate").datetimebox("getText"));
            masterDataVo.stopDate = new Date($("#stopDate").datetimebox("getText"));
        }else{
            $("#startDate").datetimebox("clear");
            $("#stopDate").datetimebox("clear");
            masterDataVo.startDate = $("#startDate").datetimebox("getText");
            masterDataVo.stopDate = $("#stopDate").datetimebox("getText");
        }
        masterDataVo.supplier = $("#supplier").combogrid("getText");
        masterDataVo.expCode = $("#searchInput").combogrid("getValue");
        masterDataVo.hospitalId = parent.config.hospitalId;
        masterDataVo.storage = parent.config.storageCode;
        if(masterDataVo.expCode.length==0&&masterDataVo.imClass.length==0&&masterDataVo.startBill.length==0&&masterDataVo.startDate.length==0&&masterDataVo.supplier.length==0){
            $.messager.alert('系统提示','请选择查询参数','info');
            return;
        }
        console.log(masterDataVo);
        var promise =$.get("/api/exp-import/exp-do-account",masterDataVo,function(data){
            for(var i = 0 ;i<data.length;i++){
                if(data[i].accountIndicator=='1'){
                    data[i].accountIndicator='已记账';
                }
                if(data[i].accountIndicator=='0'|| data[i].accountIndicator==null ){
                    data[i].accountIndicator='未记账';
                }
                data[i].invoiceDate=myFormatter2(data[i].invoiceDate);
            }
            masters =data ;
        },'json');
        promise.done(function(){
            if(masters.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info')
                $("#importMaster").datagrid('loadData',[]);
                return;
            }
            $("#importMaster").datagrid('loadData',masters);
        })
        masters.splice(0,masters.length);
        return promise;

    }

    $("#saveBtn").on('click',function(){
        if(editRowIndex!=undefined){
            $("#importMaster").datagrid('endEdit',editRowIndex);
            $('#importMaster').datagrid('refreshRow',editRowIndex);
            editRowIndex = undefined;
        }
        var checkedItems = $('#importMaster').datagrid('getRows');
        //console.log(checkedItems);
        var importVo = {};
        var expImportMasterBeanChangeVo = {};
        expImportMasterBeanChangeVo.updated = [];
        var expImportDetailBeanChangeVo = {};
        expImportDetailBeanChangeVo.updated = [];
        importVo.expImportMasterBeanChangeVo = expImportMasterBeanChangeVo;
        importVo.expImportDetailBeanChangeVo = expImportDetailBeanChangeVo;
        for(var i =0; i<checkedItems.length;i++){
            for(var j =0; j<checkedItems.length;j++){
                if(checkedItems[i].documentNo==checkedItems[j].documentNo&&checkedItems[i].accountIndicator!=checkedItems[j].accountIndicator){
                    $.messager.alert('系统提示','同一账单的上账状态必须一致','info');
                    return;
                }
            }
            var saveVo = {};
            var detailVo={};
            //detailVo.storage = parent.config.storageCode;
            detailVo.hospitalId = parent.config.hospitalId;
            saveVo.storage = parent.config.storageCode;
            saveVo.hospitalId = parent.config.hospitalId;
            detailVo.invoiceNo = checkedItems[i].invoiceNo;
            detailVo.invoiceDate=w3(checkedItems[i].invoiceDate);
            detailVo.itemNo = checkedItems[i].itemNo;
            detailVo.id = checkedItems[i].detailId;
            saveVo.id = checkedItems[i].id;
            if(checkedItems[i].accountIndicator=='已记账'){
                saveVo.accountIndicator=1;
            }
            if(checkedItems[i].accountIndicator=='未记账'){
                saveVo.accountIndicator=0;
            }
            expImportMasterBeanChangeVo.updated.push(saveVo);
            expImportDetailBeanChangeVo.updated.push(detailVo);
        }
        //return;
        if(importVo.length<=0){
            $.messager.alert('系统提示','没有上账数据','info');
            loadDict();
            return;
        }
        $.postJSON("/api/exp-import/exp-do-account-save",importVo,function(data){
            $.messager.alert('系统提示','保存成功','info');
        },function(){
            $.messager.alert("系统提示", '保存失败', 'error');
            $("#importMaster").datagrid('loadData',[]);
        })
    })
})