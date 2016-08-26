/**
 * Created by wangbinbin on 2015/10/27.
 */
/***
 * 库存限量报警
 */
$(function () {
    $("#dg").datagrid({
//        title: '库存限量报警',
        fit: true,//让#dg数据创铺满父类容器
        footer: '#tb',
//        rownumbers: true,
        singleSelect: true,
        columns: [[{
            title: '代码',
            field: 'expCode',
            align: 'center',
            width: "16%"
        }, {
            title: '产品名称',
            field: 'expName',
            align: 'center',
            width: "17%"
        },  {
            title: '产品规格',
            field: 'packageSpec',
            align: 'center',
            width: "17%"
        }, {
            title: '包装单位',
            field: 'packageUnits',
            align: 'center',
            width: "10%"
        }, {
            title: '参考批价',
            field: 'tradePrice',
            align: 'right',
            width: "8%",
            editor: 'numberbox'
        }, {
            title: '参考零价',
            field: 'retailPrice',
            align: 'right',
            width: "8%",
            editor: 'numberbox'
        }, {
            title: '库存上限',
            field: 'upperLevel',
            align: 'center',
            width: "8%"
        }, {
            title: '库存下限',
            field: 'lowLevel',
            align: 'center',
            width: "8%"
        },{
            title: '当前库存',
            field: 'stockQuantity',
            align: 'center',
            width: "10%",
            styler: function(value,row,index){
                if (row.upperLevel<row.stockQuantity){
                    return 'color:blue;';
                }
                if(row.lowLevel>=row.stockQuantity){
                    return 'color:red;';
                }
            }
        }
        ]]
    });
   var counts = [];
    $("#searchBtn").on('click', function () {
        var promise = loadDict() ;
        promise.done(function(){
            if(counts.length<=0){
                $.messager.alert("系统提示", "数据库暂无数据");
                $("#dg").datagrid('loadData', []);
                return;
            }
            $("#dg").datagrid('loadData', counts);
        })

    });

    //加载相应编码的数据
    var loadDict = function () {
        var expName=$("#expName").val();
        counts.splice(0,counts.length);

        //为报表准备字段
        expNames=expName;

        var countPromise =   $.get("/api/exp-stock/upper-low-warning?storage=" + parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&expName="+expName, function(data){
            $.each(data,function(index,item){
                item.tradePrice = fmoney(item.tradePrice,2);
                item.retailPrice = fmoney(item.retailPrice,2);
            });
           counts = data;
        });
        return countPromise ;
    }

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
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-stock-upper-low-warning.cpt"+"&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&expName="+expNames;
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
})