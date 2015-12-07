/**
 * Created by wangbinbin on 2015/10/20.
 */
/***
 * 零库存管理
 */
$(function () {
    $("#dg").datagrid({
        title: '零库存管理',
        fit: true,//让#dg数据创铺满父类容器
        toolbar:'#ft',
        footer: '#tb',
        rownumbers: true,
        singleSelect: false,
        ctrlSelect:true,
        columns: [[{
            title: '库房',
            field: 'subStorage',
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
            title: '厂家',
            field: 'firmId',
            width: "8%"
        },{
            title: '批号',
            field: 'batchNo',
            width: "8%"
        },{
            title: '有效期',
            field: 'expireDate',
            width: "8%",
            formatter:formatterDate

        },{
            title: '单价',
            field: 'purchasePrice',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        },{
            title: '数量',
            field: 'quantity',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        },{
            title: '复价',
            field: 'subPackage1',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        },{
            title: '折扣',
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
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d);
            return dateTime
        }
    }
    var loadDict = function () {
        var dicts = {};
        var promise = $.get("/api/exp-storage-zero-manage/list?storageCode=" +parent.config.storageCode+"&hospitalId="+parent.config.hospitalId, function(data){
            dicts=data;
            console.log(data);
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
    })
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

    //删除全部
    $("#deleteBtnAll").on('click',function(){
        var selectRows = $("#dg").datagrid('selectAll');
        var rows = $("#dg").datagrid('getSelections',selectRows) ;
        if(rows) {
            $.messager.alert("系统提醒", "您确定要删除全部吗？", "info");
            for(var i= 0;i<rows.length;i++){
                var index = $("#dg").datagrid('getRowIndex',rows[i]) ;
                $("#dg").datagrid('deleteRow',index) ;
            }
        }
    })
    //删除本行
    $("#deleteBtn").on('click',function(){
        var row = $("#dg").datagrid('getSelected') ;
        if(!row){
            $.messager.alert("系统提醒","请选择要删除的行","error") ;
            return ;
        }
        var index = $("#dg").datagrid('getRowIndex',row) ;
        $("#dg").datagrid('deleteRow',index) ;
    })
    $("#locationBtn").on('click',function(){
        var rowData = $("#dg").datagrid('getRows');
        var expName = $("#expName").combogrid("getText");
        for(var i=0;i<rowData.length;i++){
            if(rowData[i].expName==expName){
                var index = $("#dg").datagrid('getRowIndex',rowData[i]) ;
                var selectRow = $("#dg").datagrid('selectRow',index)
                $("#dg").datagrid('scrollTo',index) ;
            }
        }
    });
    $("#saveBtn").on('click',function(){
        var deleteData = $("#dg").datagrid('getChanges','deleted') ;
        if(deleteData&&deleteData.length > 0 ){
            $.postJSON("/api/exp-storage-zero-manage/delete",deleteData,function(data){
                $.messager.alert('系统提示','删除成功',"info");
                loadDict() ;
            },function(data){
                console.log(data) ;
                $.messager.alert("系统提示",'删除失败',"error");
            }) ;
        }
    });
})

