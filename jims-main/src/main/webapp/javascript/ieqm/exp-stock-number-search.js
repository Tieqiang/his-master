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
//        title: "库存量查询",
        footer: '#ft',
//        toolbar:'#tb',
        fit: true,
        fitColumns: true,
        singleSelect: true,
        showFooter:true,
//        rownumbers:true,
        columns: [[{
            title: '库房名称',
            field: 'subStorage',
            align: 'center',
            width: '7%',
            editor: {type: 'textbox'}
        }, {
            title: '产品类别',
            field: 'expForm',
            align: 'center',
            width: '7%'

        }, {
            title: '产品代码',
            field: 'expCode',
            align: 'center',
            width: '10%',
            editor: {type: 'textbox'}
        }, {
            title: '产品名称',
            field: 'expName',
            align: 'center',
            width: '18%',
            editor: {type: 'textbox'}
        }, {
            title: "产品规格",
            width: '10%',
            field: 'packageSpec',
            align: 'center'
        }, {
            title: '生产厂家',
            field: 'firmId',
            align: 'center',
            width: '11%',
            editor: {type: 'textbox'}
        }, {
            title: '产品单位',
            field: 'units',
            align: 'center',
            width: '7%'
        }, {
            title: '库存数量',
            width: '7%',
            align: 'center',
            field: 'quantity',
            editor: {type: 'numberbox'}

        }, {
            title: '批号',
            width: '7%',
            field: 'batchNo',
            align: 'center',
            editor: {type: 'numberbox'}

        }, {
            title: '进货单价',
            width: '7%',
            align: 'right',
            field: 'purchasePrice',
            editor: {type: 'numberbox'}
        }, {
            title: '零售单价',
            width: '7%',
            align: 'right',
            field: 'retailPrice',
            editor: {type: 'numberbox'}
        }, {
            title: '进货金额',
            width: '7%',
            align: 'right',
            field: 'tradeAllPrice',
            editor: {type: 'numberbox'}
        }, {
            title: '零售金额',
            width: '7%',
            align: 'right',
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
            panelWidth:400,
            idField:'subStorage',
            textField:'subStorage',
            data:sName,
            fitColumns: true,
            columns:[[
                {field:'storageCode',title:'库房代码',width:150, align: 'center'},
                {field:'subStorage',title:'库房单元',width:200, align: 'center'}
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
    //供应商
    var suppliers = {};
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
        return suppliers;
    });
    promise.done(function () {
        $("#supplier").combogrid({
            idField: 'supplierCode',
            textField: 'supplierCode',
            data: suppliers,
            panelWidth: 500,
            fitColumns: true,
            columns: [[{
                title: '供应商名称', field: 'supplierName', width: 200, align: 'center'
            }, {
                title: '厂商', field: 'supplierCode', width: 150, align: 'center'
            }, {
                title: '输入码', field: 'inputCode', width: 50, align: 'center'
            }]],
            filter: function (q, row) {
                var opts = $(this).combogrid('options');
                if(row[opts.textField].indexOf(q) == 0){
                    return true;
                }else{
                    return $.startWith(row.inputCode.toUpperCase(), q.toUpperCase());
                }
            }
        })
    });
    $("#searchBtn").on('click', function () {
        var promiseStock = loadDict();
        promiseStock.done(function(){
            if(stocksData.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                $("#expStockCol").datagrid('loadData',{total:0,rows:[]});
                return;
            }
            $("#expStockCol").datagrid('loadData',stocksData);
        })
        stocksData.splice(0,stocksData.length);
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
            var printExpName = $("#searchInput").combogrid('getValue');
            var printFormClass = $("#formClass").combobox('getText');
            if(printFormClass=="全部"){
                printFormClass="";
            }
            var printSubStorageClass = $("#subStorageClass").combogrid('getText');
            var printSupplier = $("#supplier").combogrid('getText');
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-stock-number-search.cpt"+"&hospitalId="+parent.config.hospitalId+"&storage="+parent.config.storageCode +"&expCode="+printExpName+"&subStorage="+printSubStorageClass+"&expForm="+printFormClass+"&firmId="+printSupplier+"&quantity="+ stocksDatas;
            $("#report").prop("src",cjkEncode(https));
        }
    });
    $("#printClose").on('click', function () {
        $("#printDiv").dialog('close');
        $("#expStockCol").datagrid('loadData', {total: 0, rows: []});
    })
    $("#printBtn").on('click', function () {
        var printData = $("#expStockCol").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');
    });
    var stocksDatas=0;
    var loadDict = function(){
        stockDataVo.formClass = $("#formClass").combobox("getText");
        stockDataVo.subStorageClass = $("#subStorageClass").combobox("getText");
        stockDataVo.supplier = $("#supplier").combogrid("getText");
        stockDataVo.searchInput = $("#searchInput").combogrid("getValue");
        stockDataVo.hospitalId = parent.config.hospitalId;
        stockDataVo.storage = parent.config.storageCode;
        var promise =$.get("/api/exp-stock/exp-stock-number-search",stockDataVo,function(data){
            $.each(data,function(index,item){
                item.purchasePrice = fmoney(item.purchasePrice,2);
                item.retailPrice = fmoney(item.retailPrice,2);
                item.tradeAllPrice = fmoney(item.tradeAllPrice,2);
                item.retailAllPrice = fmoney(item.retailAllPrice,2);
            });
            var radios=$("#numberForm input[name='radioOne']:checked").val();
            if(radios=='0'){
                stocksData=data;
                stocksDatas=0;
            }else{
                for(var i = 0 ;i<data.length;i++){
                    if(data[i].quantity!='0'){
                        var stock = data[i];
                        stocksData.push(stock);
                    }
                }
                stocksDatas=1;
            }
        },'json');
        return promise;
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