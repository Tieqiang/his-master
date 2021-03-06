/**
 * Created by wangbinbin on 2015/10/20.
 */
/***
 * 零库存管理
 */
$(function () {
    //产品名称
    $("#expName").combogrid({
        idField: 'expName',
        textField: 'expName',
        method: 'get',
        url: '/api/exp-dict/exp-dict-list-by-input',
        mode: 'remote',
        columns: [[{
            title: 'ID',
            field: 'id',
            hidden: true
        },{
            title: '名称',
            field: 'expName',
            align: 'center',
            width: '50%'
        },{
            title: '输入码',
            field: 'inputCode',
            align: 'center',
            width: '30%'
        }]],
        onClickRow: function(index,row){
            loadDict();
        },
        keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
            enter: function (e) {
                var expName = $('#expName').combogrid('getValue');
                $(this).combogrid('hidePanel');
                loadDict();
            }
        })
    });
    $("#dg").datagrid({
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
            align: 'center',
            width: "8%"
        }, {
            title: '产品名称',
            field: 'expName',
            align: 'center',
            width: "11%"
        }, {
            title: '产品包装规格',
            field: 'packageSpec',
            align: 'center',
            width: "8%"
        }, {
            title: '产品单位',
            field: 'packageUnits',
            align: 'center',
            width: "8%"
        }, {
            title: '生产厂家',
            field: 'firmId',
            align: 'center',
            width: "11%"
        },{
            title: '产品批号',
            field: 'batchNo',
            align: 'center',
            width: "11%"
        },{
            title: '产品有效期',
            field: 'expireDate',
            align: 'center',
            width: "11%",
            formatter:formatterDate

        },{
            title: '产品单价',
            field: 'purchasePrice',
            align: 'right',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        },{
            title: '产品库存数量',
            field: 'quantity',
            align: 'center',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        },{
            title: '产品复价',
            field: 'subPackage1',
            align: 'right',
            width: "8%",
            editor: {
                type: 'validatebox', options: {
                    precision: 2
                }
            }
        },{
            title: '产品折扣',
            field: 'discount',
            align: 'center',
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
        var expName=$("#expName").combogrid('getValue');
        //为报表准备字段
        expNames=expName;

//        alert(expName+"expName");
        var dicts = {};
        var promise = $.get("/api/exp-storage-zero-manage/list?storageCode=" +parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&expName="+expName, function(data){
            $.each(data,function(index,item){
                if(item.purchasePrice == null || item.purchasePrice == '' || typeof(item.purchasePrice) == 'undefined'){
                    item.purchasePrice = 0.00;
                }
                if (item.subPackage1 == null || item.subPackage1 == '' || typeof(item.subPackage1) == 'undefined') {
                    item.subPackage1 = 0.00;
                }
                item.purchasePrice = fmoney(item.purchasePrice,2);
                item.subPackage1 = fmoney(item.subPackage1,2);
            });
            dicts=data;
//            ?
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

    //为报表准备字段
    var expNames='';
    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-storage-zero-manage.cpt"+"&hospitalId="+parent.config.hospitalId+"&storageCode="+parent.config.storageCode+"&expName="+expNames;
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

