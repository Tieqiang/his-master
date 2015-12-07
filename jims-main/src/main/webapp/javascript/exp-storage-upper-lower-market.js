/**
 * Created by wangbinbin on 2015/10/13.
 */
/***
 * 根据消耗量定义库存量
 */
function myformatter(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return y + '/' + (m < 10 ? ('0' + m) : m) + '/01';
}
function myformatter2(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return y + '/' + (m < 10 ? ('0' + m) : m) + '/' + (d < 10 ? ('0' + d) : d);
}
function w3(s) {
    if (!s) return new Date();
    var y = s.substring(0, 4);
    var m = s.substring(5, 7);
    var d = s.substring(8, 10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
        return new Date(y, m - 1, d);
    } else {
        return new Date();
    }
}
$(function () {
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
            field: 'storage',
            width: "8%"
        }, {
            title: '产品名称',
            field: 'expName',
            width: "8%"
        }, {
            title: '包装规格',
            field: 'expSpec',
            width: "8%"
        }, {
            title: '单位',
            field: 'units',
            width: "5%"
        }, {
            title: '每包装含量',
            field: 'amountPerPackage',
            width: "8%"
        }, {
            title: '包装单位',
            field: 'packageUnits',
            width: "8%"
        }, {
            title: '高位水平',
            field: 'upperLevel',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        }, {
            title: '低位水平',
            field: 'lowLevel',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        }, {
            title: '统计区间消耗',
            field: 'stockQuantity',
            width: "8%"
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
    //设置时间
    var curr_time = new Date();
    $("#startDate").datebox("setValue", myformatter(curr_time));
    $("#stopDate").datebox("setValue", myformatter2(curr_time));
    $('#expName').combogrid({
        panelWidth: 200,
        idField: 'expCode',
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
        pageNumber: 1
    });
    var prices = [];
    $("#searchBtn").on('click', function () {
         loadDict();
    })
    var loadDict = function () {
        var startDate = $('#startDate').datetimebox('getText');
        var stopDate = $('#stopDate').datetimebox('getText');
        if (!startDate || !stopDate) {
            $.messager.alert("系统提醒", "请选择统计区间", "error");
            return;
        }
        var pricePromise = $.get("/api/exp-storage-profile-market/upper-lower?storageCode=" + parent.config.storageCode+"&startTime="+startDate+"&stopTime="+stopDate+"&hospitalId="+parent.config.hospitalId, function (data) {
            $.each(data, function (index, item) {
                    var price = {};
                    price.id = item.id;
                    price.storage = item.storage;
                    price.expName = item.expName;
                    price.expSpec = item.expSpec;
                    price.units = item.units;
                    price.expCode = item.expCode;
                    price.amountPerPackage = item.amountPerPackage;
                    price.packageUnits = item.packageUnits;
                    if (item.stockQuantity != null) {
                        price.upperLevel = item.stockQuantity*1.6;
                        price.lowLevel = item.stockQuantity*0.6;
                    }else{
                        price.upperLevel = item.upperLevel;
                        price.lowLevel = item.lowLevel;
                    }
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
    $("#locationBtn").on('click',function(){
        var rowData = $("#dg").datagrid('getRows');
        var expName = $("#expName").combogrid("getText");
        var index;
        for(var i=0;i<rowData.length;i++){
            if(rowData[i].expName==expName){
                index = $("#dg").datagrid('getRowIndex',rowData[i]) ;
                var selectRow = $("#dg").datagrid('selectRow',index)
                $("#dg").datagrid('scrollTo',index) ;
            }
        }
        if(editRowIndex ==undefined){
            $("#dg").datagrid("beginEdit",index) ;
            editRowIndex = index ;
        }else{
            $("#dg").datagrid('endEdit',editRowIndex) ;
            $("#dg").datagrid('beginEdit',index) ;
            editRowIndex = index ;
        }
    });
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

