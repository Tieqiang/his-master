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
    //定义出库类型
    var expClasses = [];
    var expClassPromise = $.get('/api/exp-export-class-dict/list', function (data) {
        $.each(data, function (index, item) {
            var ec = {};
            ec.exportClass = item.exportClass;
            expClasses.push(ec);
        });
        var all = {};
        all.exportClass = '全部';
        expClasses.unshift(all);

        $('#expClass').combobox({
            panelHeight: 'auto',
            data: expClasses,
            valueField: 'exportClass',
            textField: 'exportClass'
        });
        $('#expClass').combobox("select", "全部");
    });


    $("#dg").datagrid({
        title: '按出库类型出库统计',
        fit: true,//让#dg数据创铺满父类容器
        toolbar: '#ft',
        footer: '#tb',
        singleSelect: true,
        nowrap: false,
        columns: [[{
            title: '去向库房',
            field: 'receiver',
            width: "30%"
        }, {
            title: '品次',
            field: 'importNo',
            width: "25%"
        }, {
            title: '品种',
            field: 'importCode',
            width: "25%"
        }, {
            title: '金额',
            field: 'importAmount',
            width: "20%"
        }]]
    });

    $("#search").on('click', function () {
        var startDate = $("#startDate").datetimebox('getText');
        var endDate = $("#endDate").datetimebox('getText');
        var expClass = $("#expClass").combobox("getText");
        var storageCode = parent.config.storageCode;
        var hospitalId = parent.config.hospitalId;
        //
        $.get("/api/exp-export/export-detail-by-exp-class?type=expClass&storage=" + storageCode + "&hospitalId=" + hospitalId + "&startDate=" + startDate + "&endDate=" + endDate + "&value=" + expClass, function (data) {
            if (data.length > 0) {
                var sum = 0.00;

                $.each(data, function (index, item) {
                    sum += item.importAmount;
                });
                $("#dg").datagrid('loadData', data);
                $('#dg').datagrid('appendRow', {
                    receiver: "合计：",
                    importAmount: sum
                });
            } else {
                $.messager.alert("提示", "起始时间段内无数据！")
            }
        });
    });
    $("#clear").on('click', function () {
        $("#dg").datagrid('loadData', {total: 0, rows: []});
    });
    $("#saveAs").on('click', function () {
        $.messager.alert("系统提示", "另存为", "info");
    });
    $("#print").on('click', function () {
        $.messager.alert("系统提示", "打印", "info");
    });
});