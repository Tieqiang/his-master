$(function () {
    var editIndex;
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
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
        formatter: formatterDate,
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

    $('#endDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#endDate').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
            $('#endDate').datetimebox('setText', dateTime);
            $('#endDate').datetimebox('hidePanel');
        }
    });
//供应商
    var suppliers = {};
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
        return suppliers;
    });
    //发往库房数据加载
    $('#receiver').combogrid({
        panelWidth: 500,
        idField: 'supplierCode',
        textField: 'supplierName',
        loadMsg: '数据正在加载',
        url: '/api/exp-supplier-catalog/list-with-dept?hospitalId=' + parent.config.hospitalId,
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'supplierCode', title: '编码', width: 100, align: 'center'},
            {field: 'supplierName', title: '名称', width: 200, align: 'center'},
            {field: 'inputCode', title: '拼音码', width: 100, align: 'center'}
        ]],
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false/*,
        pageSize: 50,
        pageNumber: 1,
        filter: function (q, row) {
        if ($.startWith(row.inputCode.toUpperCase(), q.toUpperCase())) {
            return $.startWith(row.inputCode.toUpperCase(), q.toUpperCase());
        }
        var opts = $(this).combogrid('options');
        return row[opts.textField].indexOf(q) == 0;

    }*/
    });

    $("#dg").datagrid({
        title: '按出库去向出库查询',
        fit: true,//让#dg数据创铺满父类容器
        toolbar: '#ft',
        footer: '#tb',
        rownumbers: true,
        singleSelect: true,
        columns: [[{
            title: '库房',
            field: 'subStorage',
            align: 'center',
            width: "10%"
        }, {
            title: '去向',
            field: 'receiver',
            align: 'center',
            width: "12%",
            formatter: function (value, row, index) {
                for (var i = 0; i < suppliers.length; i++) {
                    if (value == suppliers[i].supplierCode) {
                        return suppliers[i].supplierName;
                    }
                }
                return value;
            }
        }, {
            title: '产品编码',
            field: 'expCode',
            align: 'center',
            width: "10%"
        }, {
            title: '产品名称',
            field: 'expName',
            align: 'center',
            width: "15%"
        }, {
            title: '包装规格',
            field: 'packageSpec',
            align: 'center',
            width: "10%"
        }, {
            title: '单位',
            field: 'packageUnits',
            align: 'center',
            width: "9%"
        }, {
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: "10%"
        }, {
            title: '数量',
            field: 'quantity',
            align: 'center',
            width: "10%"
        }, {
            title: '金额',
            field: 'amount',
            align: 'center',
            width: "10%"
        }]]
    });

    $("#search").on('click', function () {
        var startDate = $("#startDate").datetimebox('getText');
        var endDate = $("#endDate").datetimebox('getText');
        var receiver = $("#receiver").combobox('getValue');
        var storageCode = parent.config.storageCode;
        var hospitalId = parent.config.hospitalId;
        $.get('/api/exp-export/export-detail-by-exp-class?type=receiver&storage=' + storageCode  + "&hospitalId=" + hospitalId + "&startDate=" + startDate + "&endDate=" + endDate+"&value="+receiver, function (data) {
            if (data.length > 0) {
                var sumQuantity = 0.00;
                var sumAmount = 0.00;

                //为报表准备字段
                startDates=startDate;
                stopDates=endDate;
                receivers=receiver;

                $.each(data, function (index, item) {
                    sumQuantity += item.quantity;
                    sumAmount += item.amount;
                });
                $("#dg").datagrid('loadData', data);
                $('#dg').datagrid('appendRow', {
                    receiver: '',
                    firmId: "合计：",
                    quantity: sumQuantity,
                    amount: sumAmount
                });
            } else {
                $.messager.alert("提示", "起始时间段内无数据！")
            }
        });
    });

    $("#saveAs").on('click', function () {
        $.messager.alert("系统提示", "另存为", "info");
    });

    //为报表准备字段
    var startDates='';
    var stopDates='';
    var receivers='';

    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var stopDate = getNowFormatDate();
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/"+"export-by-receiver.cpt"+"&hospitalId="+parent.config.hospitalId+"&storage="+parent.config.storageCode+"&startDate=" + startDates + "&receiver=" + receivers + "&stopDate=" + stopDate;
            $("#report").prop("src", cjkEncode(https));
        }
    });
    $("#print").on('click', function () {
        var printData = $("#dg").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    });

    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
        return currentdate;
    }
});