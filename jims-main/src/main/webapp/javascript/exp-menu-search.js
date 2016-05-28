/**
 * Created by wangbinbin on 2015/9/30.
 */
/***
 * 产品目录查询
 */
$(function () {
    $("#dg").datagrid({
        title: '产品目录查询',
        fit: true,//让#dg数据创铺满父类容器
        footer: '#tb',
        singleSelect: true,
        columns: [[{
            title: '产品编号',
            field: 'expCode',
            width: "10%"
        }, {
            title: '产品名称',
            field: 'expName',
            width: "10%"
        }, {
            title: '包装规格',
            field: 'expSpec',
            width: "10%"
        },  {
            title: '厂家',
            field: 'firmId',
            width: "10%"
        }, {
            title: '单位',
            field: 'units',
            width: "10%"
        }, {
            title: '类型',
            field: 'expForm',
            width: "10%"
        }, {
            title: '批发价',
            field: 'tradePrice',
            width: "10%",
            editor: 'numberbox'
        }, {
            title: '零售价',
            field: 'retailPrice',
            width: "10%",
            editor: 'numberbox'
        }/*{
            title: '数量',
            field: 'quantity',
            width: "10%",
            editor: 'numberbox'
        }*/,{
            title: '产品范围',
            field: 'expIndicator',
            width: "10%"
        }]]
    });

    //提取
    $("#searchBtn").on('click', function () {
        loadDict();
    });

    //清屏
    $("#clearBtn").on('click', function () {
        $("#dg").datagrid("loadData", {rows: []});
    });
    //另存为
    $("#saveAsBtn").on('click', function () {
        alert("saveAs");
    });
    $("#printDiv").dialog({
        title: '打印',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        buttons: '#printft',
        closed: true,
        onOpen: function () {
            $("#report").prop("src", parent.config.defaultReportPath + "exp-menu-search.cpt&storageCode=" + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId);
        }
    })
    //打印
    $("#printBtn").on('click', function () {
        var printData = $("#dg").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先检索数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');
    });
    $("#printClose").on('click', function () {
        $("#printDiv").dialog('close');
        $("#dg").datagrid("loadData", {total:0,rows: []});
    })
    var loadDict = function () {
        $.get("/api/exp-menu-search/list?storageCode=" +parent.config.storageCode+"&hospitalId="+parent.config.hospitalId, function (data) {
            if (data.length == 0) {
                $.messager.alert("提示", "数据库暂无数据", "info");
                $("#dg").datagrid("loadData", {rows: []});
            } else {
                $.each(data, function (index, item) {
                    if(item.expIndicator==1){
                        item.expIndicator ="全院产品";
                    }else{
                        item.expIndicator = "普通产品";
                    }
                });
                $("#dg").datagrid("loadData", data);
            }
        });
    }



})