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
    var editIndex;
    var stopEdit = function () {
        var index = editIndex;
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            var row = $('#dg').datagrid('getData').rows[index];
            var balance = (row.actualQuantity * 1- row.accountQuantity * 1)*1;
            row.quantity = balance;
            row.profitAmount = balance* row.retailPrice;
            $("#dg").datagrid('refreshRow', index);

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
    var count = $("#dg").datagrid("getRows").length;
    $("#count").textbox("setText", count);
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
        $('#subStorage').combobox("select","全部");
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
    //定义expName
    $("#expName").searchbox({
        searcher: function (value, name) {
            var rows = $("#dg").datagrid("getRows");
            for (var i = 0; i < rows.length; i++) {
                if (rows[i].expName == value) {
                    $("#dg").datagrid('selectRow', i);
                }
            }
        }
    });
    var cellStyler = function (value, row, index) {
        if (row.quantity < 0) {
            return 'background-color:#80FFFF;color:red;';
        }
        if (row.quantity > 0) {
            return 'background-color:#FFFF80;color:red;';
        }
        return 'color:red;';
    }


    $("#dg").datagrid({
        title: '产品盘点',
        fit: true,//让#dg数据创铺满父类容器
        toolbar: '#ft',
        footer: '#tb',
        singleSelect: true,
        columns: [[{
            title: '序号',
            field: 'no',
            width: "5%"
        }, {
            title: '产品编码',
            field: 'expCode',
            width: "6%"
        }, {
            title: '产品名称',
            field: 'expName',
            width: "6%"
        }, {
            title: '包装规格',
            field: 'expSpec',
            width: "6%"
        }, {
            title: '单位',
            field: 'units',
            width: "6%"
        }, {
            title: '厂家',
            field: 'firmId',
            width: "6%"
        }, {
            title: '类型',
            field: 'expForm',
            width: "6%"
        }, {
            title: '单价',
            field: 'retailPrice',
            width: "6%"
        }, {
            title: '账面数',
            field: 'accountQuantity',
            width: "6%"
        }, {
            title: '实盘数',
            field: 'actualQuantity',
            width: "6%",
            styler: cellStyler,
            editor: {
                type: 'numberbox',
                options: {
                    max: 99999.99,
                    size: 8,
                    maxlength: 8,
                    precision: 2
                }
            }
        }, {
            title: '盈亏数',
            field: 'quantity',
            width: "6%",
            styler: cellStyler
        }, {
            title: '账面额',
            field: 'paperAmount',
            width: "5%"
        }, {
            title: '实盘额',
            field: 'realAmount',
            width: "6%",
            styler: cellStyler
        }, {
            title: '盈亏额',
            field: 'profitAmount',
            width: "6%",
            styler: cellStyler
        }, {
            title: '批号',
            field: 'batchNo',
            width: "6%"
        }, {
            title: '分库房',
            field: 'subStorage',
            width: "6%"
        }, {
            title: '状态',
            field: 'recStatus',
            width: "5%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    valueField: 'code',
                    textField: 'name',
                    data: [{'code': 0, 'name': '暂存'}, {'code': 1, 'name': '保存'}, {'code': 2, 'name': '确认'}]
                }
            }
        }]],
        onClickRow: function (index, row) {
            stopEdit();

            $(this).datagrid('beginEdit', index);
            editIndex = index;
        }
    });

    $("#listDialog").dialog({
        title: '选择盘点时间',
        width: 200,
        height: 300,
        closed: true,
        catch: false,
        modal: true,
        closable: true
    });

    $('#list').datagrid({
        width: 200,
        panelHeight: 'auto',
        //url: '/api/exp-inventory-check/inventory-list-by-time?storageCode=' + storageCode + "&checkMonth=" + date,
        //method: 'GET',
        columns: [[{
            title: '盘点时间',
            field: 'checkYearMonth',
            width: "95%",
            formatter: formatterDate
        }]],
        onClickRow: function (index, row) {
            //$("#listDialog").dialog('close');
            var date = row.checkYearMonth;
            var subStorage = $("#subStorage").combobox("getText");
            var storageCode = parent.config.storageCode;
            var hospitalId = parent.config.hospitalId;
            $.get("/api/exp-inventory-check/get-inventory?type=search&storageCode=" + storageCode + "&hospitalId=" + hospitalId +"&checkMonth=" + formatterDate(date) + "&subStorage=" + subStorage, function (data) {
                //账面额=账面数*单价
                var sumAccountQuantity = 0.00;
                var sumActualQuantity = 0.00;
                var sumPaperAmount = 0.00;

                $.each(data, function (index, item) {
                    item.no = index + 1;
                    item.paperAmount = item.retailPrice * item.accountQuantity;
                    sumAccountQuantity += item.accountQuantity;
                    sumActualQuantity += item.actualQuantity;
                    sumPaperAmount += item.paperAmount;
                });
                $("#count").textbox("setText", data.length);
                $("#dg").datagrid('loadData', data);
                $('#dg').datagrid('appendRow', {
                    accountQuantity: sumAccountQuantity,
                    actualQuantity: sumActualQuantity,
                    paperAmount: sumPaperAmount
                });
                $("#dg").datagrid("autoMergeCells", ['expCode']);
                $("#listDialog").dialog('close');
            });
        }
    });
    //生成
    $("#get").on('click', function () {
        var date = $("#startDate").datetimebox('getText');
        var storageCode = parent.config.storageCode;
        var subStorage = $("#subStorage").combobox("getText");
        var hospitalId = parent.config.hospitalId;

        $.get("/api/exp-inventory-check/get-inventory-num?storageCode=" + storageCode + "&subStorage=" + subStorage + "&checkMonth=" + date + "&hospitalId=" + hospitalId, function (data) {
            if(data<=0){
                $.get("/api/exp-inventory-check/get-inventory?type=get&storageCode=" + storageCode + "&hospitalId=" + hospitalId + "&subStorage=" + subStorage + "&checkMonth=" + date, function (data) {
                    //账面额=账面数*单价
                    var sumAccountQuantity = 0.00;
                    var sumActualQuantity = 0.00;
                    var sumPaperAmount = 0.00;

                    $.each(data, function (index, item) {
                        item.no = index+1;
                        item.paperAmount = item.retailPrice*item.accountQuantity;
                        sumAccountQuantity += item.accountQuantity;
                        sumActualQuantity += item.actualQuantity;
                        sumPaperAmount += item.paperAmount;
                    });
                    $("#count").textbox("setText", data.length);
                    $("#dg").datagrid('loadData', data);
                    $('#dg').datagrid('appendRow', {
                        accountQuantity: sumAccountQuantity,
                        actualQuantity: sumActualQuantity,
                        paperAmount: sumPaperAmount
                    });
                    $("#dg").datagrid("autoMergeCells", ['expCode']);
                });
            }else{
                $.messager.alert("提示","全院子库"+date+"月份的盘点记录已存在，请按检索按钮调出！")
            }
        });

    });
    //实盘填充
    $("#fill").on('click', function () {
        if ($("#dg").datagrid("getRows").length > 0) {
            $("#dg").datagrid("deleteRow", $("#dg").datagrid("getRows").length - 1);
            var rows = $("#dg").datagrid("getRows");
            var sumAccountQuantity = 0.00;
            var sumActualQuantity = 0.00;
            var sumPaperAmount = 0.00;
            var sumRealAmount = 0.00;
            var sumProfitAmount = 0.00;
            $.each(rows, function (index, item) {
                item.actualQuantity = item.accountQuantity;
                item.quantity = 0.00;
                item.realAmount = item.paperAmount;
                item.profitAmount = 0.00;
                sumAccountQuantity += item.accountQuantity;
                sumActualQuantity += item.actualQuantity;
                sumPaperAmount += item.paperAmount;
                sumRealAmount += item.realAmount;
                sumProfitAmount += item.profitAmount;
            });
            $("#dg").datagrid('loadData', rows);
            $('#dg').datagrid('appendRow', {
                accountQuantity: sumAccountQuantity,
                actualQuantity: sumActualQuantity,
                paperAmount: sumPaperAmount,
                realAmount: sumRealAmount,
                profitAmount: sumProfitAmount
            });
            $("#dg").datagrid("autoMergeCells", ['expCode']);
        } else {
            $.messager.alert("系统提示", "数据为空，不允许操作", "error");
        }
    });
    //所有状态为【保存】的产品必须实盘数大于账面数，才允许盘盈入库
    var validImport = function(){
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
        }
        var rows = $("#dg").datagrid("getRows");
        if (rows.length == 0) {
            $.messager.alert("系统提示", "没有盘点数据，不能盘盈入库", "info");
            return false;

        }
        var over=-1;
        for (var i = 0; i < rows.length-1; i++) {
            if (rows[i].recStatus != 1) {
                $.messager.alert("系统提示", "盘点还没有完成，不能修改库存", "info");
                return false;
            }
            if (rows[i].actualQuantity > rows[i].accountQuantity) {
                over++;
            }
        }
        if(over==-1){
            $.messager.alert("系统提示", "没有需要做盘盈入库的数据", "info");
            return false;
        }
        return true;
    }
    //盘盈入库
    $("#import").on('click', function () {
        if(validImport()){
            $.messager.confirm("提示信息", "是否真想使用当前显示的盘点数据修改库存？", function (r) {
                if (r) {
                    parent.addTab('入库处理', '/his/ieqm/exp-import');
                }
            });
        }
    });
    //盘点的时候状态为【保存】的产品，账面数小于实盘数才能【盘亏出库】
    var validExport = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
        }
        var rows = $("#dg").datagrid("getRows");
        if (rows.length == 0) {
            $.messager.alert("系统提示", "没有盘点数据，不能盘亏出库", "info");
            return false;

        }
        var over=-1;
        for (var i = 0; i < rows.length-1; i++) {
            if (rows[i].recStatus != 1) {
                $.messager.alert("系统提示", "盘点还没有完成，不能修改库存", "info");
                return false;
            }
            if (rows[i].accountQuantity > rows[i].actualQuantity) {
                over++;
            }
        }
        if(over==-1){
            $.messager.alert("系统提示", "没有需要做盘亏出库的数据", "info");
            return false;
        }
        return true;
    }
    //盘亏出库
    $("#export").on('click', function () {
        if (validExport()) {
            $.messager.confirm("提示信息", "是否真想使用当前显示的盘点数据修改库存？", function (r) {
                if (r) {
                    parent.addTab('出库处理', '/his/ieqm/exp-export');
                }
            });
        }
    });
    //暂存
    $("#tempSave").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
        }
        if($("#dg").datagrid("getRows").length>0){
            //$("#dg").datagrid("deleteRow", $("#dg").datagrid("getRows").length - 1);
            var rows = $("#dg").datagrid("getRows");
            $.each(rows, function (index, item) {
                item.recStatus = 0;
                //item.checkYearMonth = new Date(item.checkYearMonth);
            });
            $.postJSON("/api/exp-inventory-check/save", rows, function (data) {
                $.messager.alert("系统提示", "暂存成功", "info");
            });
        }else{
            $.messager.alert("系统提示", "数据为空，不允许操作", "error");
        }
    });
    //查找入出库数据
    var findExport = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
        }
        var rows = $("#dg").datagrid("getRows");

        var over = 0;
        for (var i = 0; i < rows.length - 1; i++) {
            if (rows[i].accountQuantity != rows[i].actualQuantity) {
                over++;
            }
        }

        return over;
    }
    //保存
    $("#save").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
        }
        if ($("#dg").datagrid("getRows").length > 0) {
            //$("#dg").datagrid("deleteRow", $("#dg").datagrid("getRows").length - 1);
            var rows = $("#dg").datagrid("getRows");
            if(findExport()>0){
                $.messager.confirm("提示信息", "存在需要入出库数据，是否确认保存盘点数据并且自动入出库？", function (r) {
                    if (r) {
                        $.each(rows, function (index, item) {
                            item.recStatus = 1;
                            item.checkYearMonth = new Date(item.checkYearMonth);
                        });

                        $.postJSON("/api/exp-inventory-check/save", rows, function (data) {
                            $.messager.alert("系统提示", "保存成功", "info");
                        });
                    }else{
                        $.messager.alert("系统提示", "实盘数账面数不平，只能进行暂存操作", "info");
                    }
                });
            }else{
                $.each(rows, function (index, item) {
                    item.recStatus = 1;
                    item.checkYearMonth = new Date(item.checkYearMonth);
                });

                $.postJSON("/api/exp-inventory-check/save", rows, function (data) {
                    $.messager.alert("系统提示", "保存成功", "info");
                });
            }

        } else {
            $.messager.alert("系统提示", "数据为空，不允许操作", "error");
        }
    });
    //检索
    $("#search").on('click', function () {
        var date = $("#startDate").datetimebox('getText');
        var storageCode = parent.config.storageCode;
        var hospitalId = parent.config.hospitalId;
        $.get('/api/exp-inventory-check/inventory-list-by-time?storageCode=' + storageCode + "&checkMonth=" + date + "&hospitalId=" + hospitalId,function(data){
           if(data.length <=0){
               $.messager.alert("系统提示", "没有"+date+"月份的盘点记录", "info");
           }else{
               $("#list").datagrid("loadData",data);
               $("#listDialog").dialog('open');
           }
        });
    });
    //清屏
    $("#clear").on('click', function () {
        $("#dg").datagrid('loadData', {total: 0, rows: []});
    });
    //另存为
    $("#saveAs").on('click', function () {
        $.messager.alert("系统提示", "另存为", "info");
    });
    //打印
    $("#print").on('click', function () {
        $.messager.alert("系统提示", "打印", "info");
    });
});