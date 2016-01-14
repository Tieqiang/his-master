/**
 * 单品总账
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

$(function () {

    /**
     * 定义明细表格
     */
    $("#importDetail").datagrid({
        title: "单品总账",
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
            width: '6%'
        }, {
            title: '代码',
            field: 'expCode',
            width: '10%'
        }, {
            title: '类型',
            field: 'ioClass',
            width: '7%'
        }, {
            title: "科室",
            width: '7%',
            field: 'ourName'
        }, {
            title: '单位',
            field: 'packageUnits',
            width: '7%'
        }, {
            title: '厂家',
            field: 'firmId',
            width: '10%'
        }, {
            title: '批号',
            width: '5%',
            field: 'batchNo'
        } , {
            title: '入(出)库日期',
            width: '11%',
            field: 'actionDate',
            formatter: myFormatter2
        }, {
            title: '失效日期',
            width: '11%',
            field: 'expireDate',
            formatter: myFormatter2
        }, {
            title: "结存",
            width: '7%',
            field: 'inventory'
        }, {
            title: '单价',
            width: '7%',
            field: 'purchasePrice',
            type:'numberbox'
        }, {
            title: '入库数量',
            width: '7%',
            field: 'importNum',
            type:'numberbox'
        }, {
            title: '入库金额',
            width: '7%',
            field: 'importPrice',
            type:'numberbox'
        }, {
            title: '出库数量',
            width: '7%',
            field: 'exportNum',
            type:'numberbox'
        }, {
            title: '出库金额',
            width: '7%',
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
        pageNumber: 1,
        onSelect: function(rowIndex, rowData){
           var url =  '/api/exp-dict/exp-dict-list-by-expCode?expCode='+rowData.expCode;
           $('#singleSpec').combobox('reload',url);
        }
    });
    $('#singleSpec').combobox({
            panelWidth: 200,
            valueField: 'id',
            textField: 'expSpec',
            method: 'GET'
    });
    $("#searchBtn").on('click', function () {
        var promiseDetail = loadDict();
    })
    var importDetailDataVO = {};//传递vo
    var detailsData = [];//信息
    var loadDict = function(){
        importDetailDataVO.stopDate = new Date($("#stopDate").datetimebox("getText"));
        importDetailDataVO.startDate = new Date($("#startDate").datetimebox("getText"));
        var imS =importDetailDataVO.packageSpec = $("#singleSpec").combobox("getText");
        var imE =importDetailDataVO.expCode = $("#searchInput").combogrid("getValue");
        importDetailDataVO.hospitalId = parent.config.hospitalId;
        importDetailDataVO.storage = parent.config.storageCode;
        if((imS==null || imS.trim()=="")|| (imE==null)||imE.trim()==""){
            $.messager.alert('系统提示','产品名称和规格不能为空','error');
            return;
        }
        var importNum = 0.00;
        var importPrice = 0.00;
        var exportNum = 0.00;
        var exportPrice = 0.00;
        var promise =$.get("/api/exp-import/exp-single-account",importDetailDataVO,function(data){
            if(data.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                $("#importDetail").datagrid('loadData',[]);
                $("#singleSpec").combobox("clear");
                $("#searchInput").combogrid("clear");
                return;
            }
            detailsData=data;
            for(var i = 0 ;i<data.length;i++){
                importNum+=data[i].importNum;
                importPrice+=data[i].importPrice;
                exportNum+=data[i].exportNum;
                exportPrice+=data[i].exportPrice;
            }
        },'json');
        promise.done(function(){

            $("#importDetail").datagrid('loadData',detailsData);
            $('#importDetail').datagrid('appendRow', {
                ioClass: "合计：",
                importNum: importNum,
                importPrice: importPrice,
                exportNum: exportNum,
                exportPrice: exportPrice
            });
            $("#importDetail").datagrid("autoMergeCells", ['way', 'expCode']);
            $("#singleSpec").combobox("clear");
            $("#searchInput").combogrid("clear");
        })
        detailsData.splice(0,detailsData.length);

    }
})