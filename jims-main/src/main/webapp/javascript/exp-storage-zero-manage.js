/**
 * Created by wangbinbin on 2015/10/20.
 */
/***
 * 零库存管理
 */
$(function () {
//    $("#expName").searchbox({
//        searcher: function (value, name) {
//            alert(value,+","+name);
//            var rows = $("#dg").datagrid("getRows");
//            for (var i = 0; i < rows.length; i++) {
//                if (rows[i].expName == value) {
//                    $("#dg").datagrid('selectRow', i);
//                }
//            }
//        }
//    });
    $("#dg").datagrid({
//        title: '零库存管理',
        fit: true,//让#dg数据创铺满父类容器
        toolbar:'#ft',
        footer: '#tb',
//        rownumbers: true,
        singleSelect: false,
        ctrlSelect:true,
        columns: [[{
            title: 'id',
            field: 'id',
            hidden: true
        },{
            title: '产品所在库房',
            field: 'subStorage',
            width: "8%"
        }, {
            title: '产品名称',
            field: 'expName',
            width: "11%"
        }, {
            title: '产品包装规格',
            field: 'expSpec',
            width: "8%"
        }, {
            title: '产品单位',
            field: 'units',
            width: "8%"
        }, {
            title: '生产厂家',
            field: 'firmId',
            width: "11%"
        },{
            title: '产品批号',
            field: 'batchNo',
            width: "11%"
        },{
            title: '产品有效期',
            field: 'expireDate',
            width: "11%",
            formatter:formatterDate

        },{
            title: '产品单价',
            field: 'purchasePrice',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        },{
            title: '产品库存数量',
            field: 'quantity',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        },{
            title: '产品复价',
            field: 'subPackage1',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        },{
            title: '产品折扣',
            field: 'discount',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        }
        ]]
    });
    //格式化日期函数
    function formatterDate(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
//            var h = date.getHours();
//            var mm = date.getMinutes();
//            var s = date.getSeconds();
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d);
//                + (h < 10 ? ("0" + h) : h)+":"+ (mm < 10 ? ("0" + mm) : mm)+":"+ (s < 10 ? ("0" + s) : s);
            return dateTime
        }
    }
    var loadDict = function () {
        var expName=$("#expName").val();
//        alert(expName+"expName");
        var dicts = {};
        var promise = $.get("/api/exp-storage-zero-manage/list?storageCode=" +parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&expName="+expName, function(data){
            dicts=data;
//            console.log(data);
            if(data.length<=0){
                $.messager.alert("系统提示", "数据库暂无数据","info");
                $("#dg").datagrid('loadData', []);
                return dicts;
            }

        });
        promise.done(function(){
            $("#dg").datagrid('loadData', dicts);
        })
    }
    //提取
    $("#searchBtn").on('click',function(){
        loadDict();
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
            $("#report").prop("src", parent.config.defaultReportPath + "exp-storage-zero-manage.cpt");
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

    //删除全部
    $("#deleteBtnAll").on('click',function(){
        var selectRows = $("#dg").datagrid('selectAll');
        var rows = $("#dg").datagrid('getSelections',selectRows) ;
        if(rows) {
            $.messager.confirm("系统提醒", "您确定要删除全部吗？", function (data) {
                if (data) {
                    for (var i = 0; i < rows.length; i++) {
                        var index = $("#dg").datagrid('getRowIndex', rows[i]);
                        $("#dg").datagrid('deleteRow', index);
                    }
                }
            });

        }
    })
    //删除本行
    $("#deleteBtn").on('click',function(){
        var row = $("#dg").datagrid('getSelected') ;
        if(!row){
            $.messager.alert("系统提醒","请选择要删除的行","error") ;
            return ;
        }else{
            var flag=window.confirm("确定要删除吗?");
            if(flag){
                var index = $("#dg").datagrid('getRowIndex',row) ;
                $("#dg").datagrid('deleteRow',index) ;
            }
        }
     })

    $("#saveBtn").on('click',function(){
        var deleteData = $("#dg").datagrid('getChanges','deleted') ;
        if(deleteData&&deleteData.length > 0 ){
            $.postJSON("/api/exp-storage-zero-manage/delete",deleteData,function(data){
                $.messager.alert('系统提示','删除成功',"info");
                loadDict() ;
            },function(data){
                $.messager.alert("提示", data.responseJSON.errorMessage, "error");
            }) ;
        }
    });
})

