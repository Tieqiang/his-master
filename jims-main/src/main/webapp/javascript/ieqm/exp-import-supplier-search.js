/**
 * 供货商供货情况查询
 * Created by wangbinbin on 2015/11、05.
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

$(function () {
    function myFormatter2(val,row) {
        if(val != null){
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
            var h = 23;
            var mm = 59;
            var s = 59;
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
     * 定义明细表格
     */
    $("#importDetail").datagrid({
        fit: true,
        fitColumns: true,
        title: "供货商供货情况查询",
        toolbar: '#tb',
        footer: '#ft',
        rownumbers: true,
        singleSelect: false,
        showFooter:true,
        //footer: '#ft',
        rownumbers:true,
        columns: [[{
            title: '代码',
            field: 'expCode',
            align: 'center',
            width:'10%'
        }, {
            title: '产品名称',
            field: 'expName',
            align: 'center',
            width: '15%'
        }, {
            title: '规格',
            field: 'packageSpec',
            align: 'center',
            width: '10%'
        }, {
            title: "单位",
            field: 'packageUnits',
            align: 'center',
            width: '10%'
        }, {
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: '10%',
            type:'textbox'
        }, {
            title: '数量',
            field: 'quantity',
            align: 'center',
            width: '10%',
            type:'numberbox'
        }, {
            title: '金额',
            field: 'payAmount',
            align: 'right',
            width: '10%',
            type:'numberbox'
        }, {
            title: '实际金额',
            field: 'purchaseAmount',
            align: 'right',
            width: '10%',
            type:'numberbox'
        }]]
    });
    //设置时间
    var curr_time = new Date();
    $("#startDate").datetimebox("setValue", formatterDate2(curr_time));
    $("#stopDate").datetimebox("setValue", formatterDate3(curr_time));
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
        required: true,
        showSeconds: true,
        value: 'dateTime',
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
        var promiseDetail = loadDict();
    });

    //为报表准备字段
    var startDates='';
    var stopDates='';
    var suppliers='';

    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            startDates=myFormatter2(startDates);
            stopDates=myFormatter2(stopDates);
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-import-supplier-search.cpt"+"&hospitalId="+parent.config.hospitalId+"&startDate=" + startDates + "&stopDate=" + stopDates+"&supplier=" + suppliers+"&storage="+parent.config.storageCode;
            $("#report").prop("src",cjkEncode(https));
        }
    });
    $("#printBtn").on('click', function () {
        var printData = $("#importDetail").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    });
    var importDetailDataVO = {};//传递vo
    var detailsData = [];//信息


    var loadDict = function(){
        importDetailDataVO.stopDate = new Date($("#stopDate").datetimebox("getText"));
        importDetailDataVO.startDate = new Date($("#startDate").datetimebox("getText"));
        importDetailDataVO.supplier = $("#supplier").combogrid("getValue");
        importDetailDataVO.hospitalId = parent.config.hospitalId;
        importDetailDataVO.storage = parent.config.storageCode;
        var payAmount = 0.00;
        var purchaseAmount = 0.00;
        purchaseAmount = parseFloat(purchaseAmount);
        payAmount = parseFloat(payAmount);
        var promise =$.get("/api/exp-import/exp-import-supplier-search",importDetailDataVO,function(data){
            detailsData=data;

            //为报表准备字段
            startDates=importDetailDataVO.startDate  ;
            stopDates=importDetailDataVO.stopDate ;
            suppliers=importDetailDataVO.supplier;

            for (var i = 0; i < detailsData.length; i++) {
                detailsData[i].purchaseAmount = parseFloat(detailsData[i].purchaseAmount);
                detailsData[i].payAmount = parseFloat(detailsData[i].payAmount);
                purchaseAmount += detailsData[i].purchaseAmount;
                payAmount += detailsData[i].payAmount;
                detailsData[i].purchaseAmount = fmoney(detailsData[i].purchaseAmount,2);
                detailsData[i].payAmount = fmoney(detailsData[i].payAmount,2);
            }
            purchaseAmount = fmoney(purchaseAmount,2);
            payAmount = fmoney(payAmount,2);
        },'json');
        promise.done(function(){
            if(detailsData.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                $("#importDetail").datagrid('loadData',[]);
                return;
            }
            $("#importDetail").datagrid('loadData',detailsData);
            $('#importDetail').datagrid('appendRow', {
                expName: "合计：",
                payAmount: fmoney(payAmount,2),
                purchaseAmount: fmoney(purchaseAmount,2)
            });
            $("#importDetail").datagrid("autoMergeCells", ['expCode']);
        })
        detailsData.splice(0,detailsData.length);
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