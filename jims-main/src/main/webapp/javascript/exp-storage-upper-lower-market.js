/**
 * Created by wangbinbin on 2015/10/13.
 */
/***
 * 根据消耗量定义库存量
 */
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
            + (h < 10 ? ("0" + h) : h)+":"+ (mm < 10 ? ("0" + mm) : mm)+":"+ (s < 10 ? ("0" + s) : s);
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
$(function () {
//    var suppliers = [];
//    var promise = $.get("/api/exp-supplier-catalog/list-all", function (data) {
//        suppliers = data;
//    });
//    promise.done(function () {
//        $("#supplier").combogrid({
//            idField: 'supplierCode',
//            textField: 'supplierName',
//            data: suppliers,
//            panelWidth: 450,
//            fitColumns: true,
//            columns: [
//                [
//                    {
//                        title: '供应商名称',
//                        field: 'supplierName', width: 180, align: 'center'
//                    },
//                    {
//                        title: '供应商代码',
//                        field: 'supplierCode', width: 130, align: 'center'
//                    },
//                    {
//                        title: '输入码',
//                        field: 'inputCode', width: 50, align: 'center'
//                    }
//                ]
//            ]
//        })
//    });
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
    //开始日期
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
    //结束日期
    $('#stopDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate,
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
    var editRowIndex;
    $("#dg").datagrid({
        title: '根据消耗量定义库存量',
        fit: true,//让#dg数据创铺满父类容器
        toolbar: '#ft',
        footer: '#tb',
        rownumbers: true,
        singleSelect: true,
        columns: [[{
            title: '代码',
            field: 'expCode',
            width: "10%"
        }, {
            title: '产品名称',
            field: 'expName',
            width: "20%"
        }, {
            title: '包装规格',
            field: 'expSpec',
            width: "5%"
        }, {
            title: '单位',
            field: 'units',
            width: "5%"
        }, {
            title: '生产商',
            field: 'supplier',
            width: "7%"
        },{
            title: '每包装含量',
            field: 'amountPerPackage',
            width: "7%"
        }, {
            title: '包装单位',
            field: 'packageUnits',
            width: "10%"
        }, {
            title: '高位水平',
            field: 'upperLevel',
            width: "10%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        }, {
            title: '低位水平',
            field: 'lowLevel',
            width: "10%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        }, {
            title: '统计区间消耗',
            field: 'stockQuantity',
            width: "10%"
        }
        ]],onClickRow: function (rowIndex,rowData) {
            if (editRowIndex || editRowIndex == 0){
                $(this).datagrid("endEdit",editRowIndex);
            }
            $(this).datagrid("beginEdit",rowIndex);
            var upperLevel = $("#dg").datagrid("getEditor",{index:rowIndex,field:"upperLevel"});
            var lowLevel = $("#dg").datagrid("getEditor",{index:rowIndex,field:"lowLevel"});
            editRowIndex = rowIndex;
        }
    });
    var prices = [];
    $("#searchBtn").on('click', function () {
         loadDict();
    });

    //为报表准备字段
    var startDates='';
    var stopDates='';
    var supplierIds='';


    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-storage-upper-lower-market.cpt"+"&hospitalId="+parent.config.hospitalId+"&storage="+parent.config.storageCode+"&startDate=" + startDates + "&stopDate=" + stopDates+"&supplierId="+supplierIds;
            $("#report").prop("src",cjkEncode(https));
        }
    });
    $("#printBtn").on('click', function () {
        var printData = $("#dg").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    });
    var loadDict = function () {
        var startDate = $('#startDate').datetimebox('getText');
        var stopDate = $('#stopDate').datetimebox('getText');
        if (!startDate || !stopDate) {
            $.messager.alert("系统提醒", "请选择统计区间", "error");
            return;
        }
        //为报表准备字段
        startDates=startDate;
        stopDates=stopDate;
//        var supplierId=$("#supplier").combogrid("getValue");
        var pricePromise = $.get("/api/exp-storage-profile-market/upper-lower?storageCode=" + parent.config.storageCode+"&startTime="+startDate+"&stopTime="+stopDate+"&hospitalId="+parent.config.hospitalId, function (data) {
            $.each(data, function (index, item) {
                    var price = {};
                    price.id = item.id;
                    price.storage = item.storage;
                    price.expName = item.expName;
                    price.expSpec = item.expSpec;
                    price.units = item.units;
                    price.expCode = item.expCode;
                    price.supplier=item.supplier;
                    price.amountPerPackage = item.amountPerPackage;
                    price.packageUnits = item.packageUnits;
//                    if (item.stockQuantity != null) {
//                        price.upperLevel = item.stockQuantity*1.6;
//                        price.lowLevel = item.stockQuantity*0.6;
//                    }else{
                        price.upperLevel = item.upperLevel;
                        price.lowLevel = item.lowLevel;
//                    }
                    price.location = item.expCode;
                    price.expireDate = item.expireDate;
                    price.supplier = item.supplier;
                    price.stockQuantity = item.stockQuantity;
                    prices.push(price);
                }
            );
            if (prices.length <= 0) {
                $.messager.alert("系统提示", "数据库暂无数据");
                $("#dg").datagrid('loadData', []);
                return;
            }
            return prices;
        });
        pricePromise.done(function(){
            $("#dg").datagrid('loadData', prices);
        });
        prices.splice(0,prices.length);

    }

    $("#saveBtn").on('click', function () {
        if (editRowIndex) {
            $("#dg").datagrid('endEdit', editRowIndex);
            editRowIndex = undefined;
        }
        var updateData = $("#dg").datagrid('getChanges', 'updated');
        //for(var i=0;i<updateData.length;i++){
        //    if(updateData[i].lowLevel<updateData[i].stockQuantity*0.6){
        //        $.messager.alert("系统提示", '下线不得低于'+updateData[i].stockQuantity*0.6, "info");
        //        break;
        //    }
        //}
        if (updateData && updateData.length > 0) {
            $.postJSON("/api/exp-storage-profile-market/save", updateData, function (data) {
                $.messager.alert('系统提示', '保存成功', "info");
                loadDict();
            }, function (data) {
                $.messager.alert("系统提示", '保存失败', "error");
                $("#dg").datagrid('loadData', []);
            });
        }
    });
})

