$(function () {
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

    //供应商
    var suppliers = {};
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
        return suppliers;
    });
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
    var subStorages = [];
    var subStoragePromise = $.get('/api/exp-sub-storage-dict/list-by-storage?storageCode=' + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId, function (data) {
        $.each(data, function (index, item) {
            var ec = {};
            ec.id = item.id;
            ec.subStorage = item.subStorage;
            subStorages.push(ec);
        });
        var all = {};
        all.id = '全部';
        all.subStorage = '全部';
        subStorages.unshift(all);

        $('#subStorage').combobox({
            panelHeight: 'auto',
            data: subStorages,
            valueField: 'id',
            textField: 'subStorage'
        });
        $('#subStorage').combobox("select", "全部");
    });


    $("#dg").datagrid({
        title: '按产品类别出库统计',
        fit: true,//让#dg数据创铺满父类容器
        toolbar: '#ft',
        footer: '#tb',
        rownumbers: true,
        singleSelect: true,
        nowrap: false,
        columns: [[{
            title: '去向库房',
            field: 'receiver',
            align: 'center',
            width: "20%",
            formatter: function (value, row, index) {
                for (var i = 0; i < suppliers.length; i++) {
                    if (value == suppliers[i].supplierCode) {
                        return suppliers[i].supplierName;
                    }
                }
                return value;
            }
        }, {
            title: '产品类别',
            field: 'expForm',
            align: 'center',
            width: "20%"
        }, {
            title: '全部（金额）',
            field: 'importAmount',
            align: 'right',
            width: "20%"
        }, {
            title: '小计（金额）',
            field: 'importAmount',
            align: 'right',
            width: "20%"
        }]]
    });

    //为报表准备字段
    var startDates='';
    var stopDates='';
    var subStor='';

    $("#search").on('click', function () {
        var startDate = $("#startDate").datetimebox('getText');
        var endDate = $("#endDate").datetimebox('getText');
        var subStorage = $("#subStorage").combobox("getText");
        var storageCode = parent.config.storageCode;
        var hospitalId = parent.config.hospitalId;
        //为报表字段准备数据
        startDates=startDate;
        stopDates=endDate;
        if(subStorage=='全部'){
            subStor='';
        }else{
            subStor=subStorage;
        }

        $.get("/api/exp-export/export-detail-by-exp-class?type=storage&storage=" + storageCode + "&hospitalId=" + hospitalId + "&startDate=" + startDate + "&endDate=" + endDate + "&value=" + subStorage, function (data) {
            if (data.length > 0) {
                var sum = 0.00;
                sum = parseFloat(sum);
                $.each(data, function (index, item) {
                    if(item.importAmount == null || item.importAmount == '' || typeof(item.importAmount) == 'undefined'){
                        item.importAmount = 0.00;
                    }
                    item.importAmount = parseFloat(item.importAmount);
                    sum += item.importAmount;
                    item.importAmount = fmoney(item.importAmount,2);
                });
                sum = fmoney(sum,2);
                $("#dg").datagrid('loadData', data);
                $('#dg').datagrid('appendRow', {
                    receiver: '',
                    expForm: "合计：",
                    importAmount: sum
                });
                $("#dg").datagrid('loadData', data);
            } else {
                $.messager.alert("提示", "起始时间段内无数据！");
                var nullData = [];
                $("#dg").datagrid('loadData', nullData);
            }
        });
    });

    $("#setPrint").on('click', function () {
        $.messager.alert("系统提示", "打印设置", "info");
    });
    $("#saveAs").on('click', function () {
        $.messager.alert("系统提示", "另存为", "info");
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
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/"+"export-detail-by-storage.cpt"+"&hospitalId="+parent.config.hospitalId+"&storage="+parent.config.storageCode+ "&startDate=" + startDates + "&stopDate=" + stopDates + "&subStorage="+subStor;
            console.info(cjkEncode(https));
            $("#report").prop("src",cjkEncode(https));
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
});