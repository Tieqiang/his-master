/**
 * 按子库房出库统计
 * Created by wangbinbin on 2015/11/06.
 */
$.extend($.fn.datagrid.methods, {
    autoMergeCells: function (jq, fields) {
        return jq.each(function () {
            var target = $(this);
            if (!fields) {
                fields = target.datagrid("getColumnFields");
            }
            var rows = target.datagrid("getRows");
            var i = 0,
                j = 0,
                temp = {};
            for (i; i < rows.length; i++) {
                var row = rows[i];
                j = 0;
                for (j; j < fields.length; j++) {
                    var field = fields[j];
                    var tf = temp[field];
                    if (!tf) {
                        tf = temp[field] = {};
                        tf[row[field]] = [i];
                    } else {
                        var tfv = tf[row[field]];
                        if (tfv) {
                            tfv.push(i);
                        } else {
                            tfv = tf[row[field]] = [i];
                        }
                    }
                }
            }

            $.each(temp, function (field, colunm) {
                $.each(colunm, function () {
                    var group = this;
                    if (group.length > 1) {
                        var before,
                            after,
                            megerIndex = group[0];
                        for (var i = 0; i < group.length; i++) {
                            before = group[i];
                            after = group[i + 1];
                            if (after && (after - before) == 1) {
                                continue;
                            }
                            var rowspan = before - megerIndex + 1;
                            if (rowspan > 1) {
                                target.datagrid('mergeCells', {
                                    index: megerIndex,
                                    field: field,
                                    rowspan: rowspan
                                });
                            }
                            if (after && (after - before) != 1) {
                                megerIndex = after;
                            }
                        }
                    }
                });
            });
        });
    }
});
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

    var editRowIndex;
    var masterDataVo = {};//主表vo
    var masters = [];//信息

    /**
     * 定义主表信息表格
     */
    $("#importMaster").datagrid({
        title: "按子库房出库统计",
        fit: true,
        footer: '#ft',
        toolbar:'#tb',
        fitColumns: true,
        singleSelect: true,
        selectOnCheck:true,
        checkOnSelect:true,
        showFooter:true,
        rownumbers:true,
        columns: [[{
            title: '代码',
            field: 'expCode',
            width: '10%'
        },{
            title: '产品名称',
            field: 'expName',
            width: '10%'
        }, {
            title: '规格',
            field: 'packageSpec',
            width: '7%'
        },  {
            title: '单位',
            field: 'packageUnits',
            width: '7%'
        }, {
            title: '厂家',
            width: '10%',
            field: 'firmId'

        }, {
            title: "数量",
            width: '7%',
            field: 'quantity'
        }, {
            title: '进价金额',
            field: 'payAmount',
            width: '7%'
        }, {
            title: '零售金额',
            width: '7%',
            field: 'retailAmount'
        }, {
            title: '产品类别',
            width: '7%',
            field: 'expForm'
        }, {
            title: '出库单号',
            width: '10%',
            field: 'documentNo',
            editor: {type: 'textbox'}
        }, {
            title: '有效期',
            width: '11%',
            field: 'expireDate',
            formatter: myFormatter2
        }, {
            title: '批号',
            width: '4%',
            field: 'batchNo'
        }]]
    });
    //定义子库房
    var subStorages = [];
    var subStoragePromise = $.get('/api/exp-sub-storage-dict/list-by-storage?storageCode=' + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId, function (data) {
        $.each(data, function (index, item) {
            var ec = {};
            ec.storageCode = item.storageCode;
            ec.subStorage = item.subStorage;
            subStorages.push(ec);
        });
        var all = {};
        all.storageCode = '全部';
        all.subStorage = '全部';
        subStorages.unshift(all);

        $('#subStorage').combobox({
            panelHeight: 'auto',
            data: subStorages,
            valueField: 'subStorage',
            textField: 'subStorage'
        });
        $('#subStorage').combobox("select", "全部");
    });
    //类型字典
    var forms = [];
    var expFormDictPromise = $.get("/api/exp-form-dict/list", function (data) {
        $.each(data, function (index, item) {
            var ec = {};
            ec.formCode = item.formCode;
            ec.formName = item.formName;
            forms.push(ec);
        });
        var all = {};
        all.formCode = '';
        all.formName = '全部';
        forms.unshift(all);

        $('#formClass').combobox({
            panelHeight: 'auto',
            data: forms,
            valueField: 'formCode',
            textField: 'formName'
        });
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
    $('#expName').combobox({
        panelWidth: 200,
        valueField: 'expCode',
        textField: 'expName',
        url: '/api/exp-name-dict/list-exp-name-by-input',
        method: 'GET',
        mode: 'remote',
        columns: [[
            //{field: 'expCode', title: '消耗品代码', width: 100},
            {field: 'expName', title: '消耗品名称', width: 100}
        ]],
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        pageSize: 50,
        pageNumber: 1,
        onSelect:function(data){
            var expName = data.expCode;
            var rowData = $("#importMaster").datagrid('getRows');
            var index;
            for(var i=0;i<rowData.length;i++){
                if(rowData[i].expCode==expName){
                    index = $("#importMaster").datagrid('getRowIndex',rowData[i]) ;
                    var selectRow = $("#importMaster").datagrid('selectRow',index)
                    $("#importMaster").datagrid('scrollTo',index) ;
                }
            }
        }
    });
    $("#searchBtn").on('click', function () {
        loadDict();
    });
    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            $("#report").prop("src", parent.config.defaultReportPath + "exp-by-subStorage-export-count.cpt");
        }
    });
    $("#printBtn").on('click', function () {
        var printData = $("#importMaster").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    });
    var loadDict = function(){
        masterDataVo.formClass = $("#formClass").combobox("getText");
        var sub = $("#subStorage").textbox("getText");
        if(sub=="全部"){
            masterDataVo.subStorage ='';
        }else{
            masterDataVo.subStorage =sub;
        }
        masterDataVo.startDate = new Date($("#startDate").datebox("getText"));
        masterDataVo.stopDate = new Date($("#stopDate").datebox("getText"));
        masterDataVo.hospitalId = parent.config.hospitalId;
        masterDataVo.storage = parent.config.storageCode;
        var payAmount = 0.00;
        var retailAmount = 0.00;
        var promise =$.get("/api/exp-export/exp-by-subStorage-export-count",masterDataVo,function(data){
            console.log(data);
            masters =data ;
            for(var i = 0 ;i<data.length;i++){
                retailAmount+=data[i].retailAmount;
                payAmount+=data[i].payAmount;
            }
        },'json');
        promise.done(function(){
            if(masters.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                $("#importMaster").datagrid('loadData',[]);
                return;
            }
            $("#importMaster").datagrid('loadData',masters);
            $('#importMaster').datagrid('appendRow', {
                expName: "合计：",
                payAmount: payAmount,
                retailAmount: retailAmount
            });
            $("#importMaster").datagrid("autoMergeCells", ['expCode']);


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
        var checkedItems = $('#importMaster').datagrid('getChecked');
        //console.log(checkedItems);
        var importVo = {};
        var expImportMasterBeanChangeVo = {};
        expImportMasterBeanChangeVo.updated = [];
        var expImportDetailBeanChangeVo = {};
        expImportDetailBeanChangeVo.updated = [];
        importVo.expImportMasterBeanChangeVo = expImportMasterBeanChangeVo;
        importVo.expImportDetailBeanChangeVo = expImportDetailBeanChangeVo;
        for(var i =0; i<checkedItems.length;i++){
            if(checkedItems[i].accountIndicator=='未记账'){
                var saveVo = {};
                var detailVo={};
                //detailVo.storage = parent.config.storageCode;
                detailVo.hospitalId = parent.config.hospitalId;
                saveVo.storage = parent.config.storageCode;
                saveVo.hospitalId = parent.config.hospitalId;
                detailVo.invoiceNo = checkedItems[i].invoiceNo;
                console.log(checkedItems[i].invoiceNo);
                detailVo.invoiceDate=w3(checkedItems[i].invoiceDate);
                detailVo.itemNo = checkedItems[i].itemNo;
                detailVo.id = checkedItems[i].detailId;
                saveVo.id = checkedItems[i].id;
                expImportMasterBeanChangeVo.updated.push(saveVo);
                expImportDetailBeanChangeVo.updated.push(detailVo);
            }
        }
        //return;
        if(importVo.length<=0){
            $.messager.alert('系统提示','没有上账数据','info');
            loadDict();
            return;
        }
        console.log(importVo);
        $.postJSON("/api/exp-import/exp-do-account-save",importVo,function(data){
            loadDict();
            $.messager.alert('系统提示','保存成功','info');

        },function(){
            loadDict();
            $.messager.alert("系统提示", '保存失败', 'error');

        })
    })
})