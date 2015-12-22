/**
 * 库存量查询
 * Created by wangbinbin on 2015/10/28.
 */
function checkRadio(){
    if($("#stockClass:checked").val()){
        $("#subStorageClass").combogrid("enable");
    }else{
        $("#subStorageClass").combogrid("clear");
        $("#subStorageClass").combogrid({disabled:true});
    }
    if($("#supply:checked").val()){
        $("#supplier").combogrid("enable");
    }else{
        $("#supplier").combogrid("clear");
        $("#supplier").combogrid({disabled:true});
    }
    if($("#formClassName:checked").val()){
        $("#formClass").combobox("enable");
    }else{
        $("#formClass").combobox("clear");
        $("#formClass").combobox({disabled:true});
    }
    if($("#expName:checked").val()){
        $("#searchInput").combogrid("enable");
    }else{
        $("#searchInput").combogrid("clear");
        $("#searchInput").combogrid({disabled:true});
    }
}
$(function () {
    var stockDataVo = {};//传递vo
    var stocksData = [];//信息


    /**
     * 定义库存表格
     */
    $("#expStockCol").datagrid({
        title: "库存量查询",
        footer: '#ft',
        toolbar:'#tb',
        fit: true,
        fitColumns: true,
        singleSelect: true,
        showFooter:true,
        rownumbers:true,
        columns: [[{
            title: '子库房',
            field: 'subStorage',
            width: '8%',
            editor: {type: 'textbox'}
        }, {
            title: '类别',
            field: 'expForm',
            width: '8%'

        }, {
            title: '代码',
            field: 'expCode',
            width: '8%',
            editor: {type: 'textbox'}
        }, {
            title: '产品名称',
            field: 'expName',
            width: '8%',
            editor: {type: 'textbox'}
        }, {
            title: "规格",
            width: '8%',
            field: 'packageSpec'
        }, {
            title: '厂家',
            field: 'firmId',
            width: '8%',
            editor: {type: 'textbox'}
        }, {
            title: '单位',
            field: 'packageUnits',
            width: '8%'
        }, {
            title: '数量',
            width: '8%',
            field: 'quantity',
            editor: {type: 'numberbox'}

        }, {
            title: '进货单价',
            width: '8%',
            field: 'purchasePrice',
            editor: {type: 'numberbox'}
        }, {
            title: '零售单价',
            width: '8%',
            field: 'retailPrice',
            editor: {type: 'numberbox'}
        }, {
            title: '进货金额',
            width: '8%',
            field: 'tradeAllPrice',
            editor: {type: 'numberbox'}
        }, {
            title: '零售金额',
            width: '8%',
            field: 'retailAllPrice',
            editor: {type: 'numberbox'}
        }]]
    });
    //子库房字典
    var promiseName = $.get('/api/exp-sub-storage-dict/list-by-storage?hospitalId='+parent.config.hospitalId+"&storageCode="+parent.config.storageCode,function(data){
        sName=data;
        return sName;
    });
    promiseName.done(function(){
        $('#subStorageClass').combogrid({
            panelWidth:160,
            idField:'storageCode',
            textField:'subStorage',
            data:sName,
            columns:[[
                {field:'storageCode',title:'库房代码',width:60},
                {field:'subStorage',title:'库房单元',width:100}
            ]]
        });
    })
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
            width: 200,
            data: forms,
            valueField: 'formCode',
            textField: 'formName'
        });
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
        var promiseStock = loadDict();
        promiseStock.done(function(){
            if(stocksData.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                $("#expStockCol").datagrid('loadData',[]);
                return;
            }
            $("#expStockCol").datagrid('loadData',stocksData);
        })
        stocksData.splice(0,stocksData.length);
    })
    var loadDict = function(){
        stockDataVo.formClass = $("#formClass").combobox("getText");
        stockDataVo.subStorageClass = $("#subStorageClass").combobox("getText");
        stockDataVo.supplier = $("#supplier").combogrid("getText");
        stockDataVo.searchInput = $("#searchInput").combogrid("getValue");
        stockDataVo.hospitalId = parent.config.hospitalId;
        stockDataVo.storage = parent.config.storageCode;
        var promise =$.get("/api/exp-stock/exp-stock-number-search",stockDataVo,function(data){
            if($("#numberForm input[name='radioOne']:checked").val()=='0'){
            stocksData=data;
            }else{
                for(var i = 0 ;i<data.length;i++){
                    if(data[i].quantity!='0'){
                        var stock = data[i];
                        stocksData.push(stock);
                        console.log(stocksData);
                    }
                }
            }
        },'json');
        return promise;
    }
})