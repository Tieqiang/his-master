/**
 * 库房产品去向汇总
 * Created by wangbinbin on 2015/11/06.
 */
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
function myFormatter2(val,row) {
    if(val != null){
        var date = new Date(val);
        var y = date.getFullYear();
        var m = date.getMonth()+1;
        var d = date.getDate();
        var h = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        var str = y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(min<10?('0'+min):min)+':'+(sec<10?('0'+sec):sec);
        return str;
    }

}

function formatterDate2(val, row) {
    if (val != null) {
        var date = new Date(val);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var h = 00;
        var mm = 00;
        var s = 00;
        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' '
            + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
        return dateTime
    }
}
function formatterDate3(val, row) {
    if (val != null) {
        var date = new Date(val);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var h = 23;
        var mm = 59;
        var s = 59;
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
$(function () {
    var masterDataVo = {};//主表vo
    var masters = [];//信息
    //供应商
    var suppliers = {};
    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
        return suppliers;
    });
    /**
     * 定义主表信息表格
     */
    $("#importMaster").datagrid({
        title: "库房产品去向汇总",
        fit: true,
        toolbar: '#tb',
        footer: '#ft',
        fitColumns: true,
        singleSelect: true,
        selectOnCheck:true,
        checkOnSelect:true,
        showFooter:true,
        rownumbers:true,
        columns: [[{
            title: '去向库房',
            field: 'receiver',
            align: 'center',
            width: '10%',
            formatter: function (value, row, index) {
                for (var i = 0; i < suppliers.length; i++) {
                    if (value == suppliers[i].supplierCode) {
                        return suppliers[i].supplierName;
                    }
                }
                return value;
            }
        },{
            title: '代码',
            field: 'expCode',
            align: 'center',
            width: '9%'
        },{
            title: '产品名称',
            field: 'expName',
            align: 'center',
            width: '10%'
        }, {
            title: '产品类别',
            field: 'expForm',
            align: 'center',
            width: '10%'
        }, {
            title: '规格',
            field: 'packageSpec',
            align: 'center',
            width: '9%'
        },  {
            title: '单位',
            field: 'packageUnits',
            align: 'center',
            width: '10%'
        }, {
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: '10%'
        }, {
            title: "数量",
            field: 'quantity',
            align: 'center',
            width: '10%'
        },{
            title: '零售金额',
            field: 'retailAmount',
            align: 'right',
            width: '10%'
        },{
            title: '批发总价',
            field: 'payAmount',
            align: 'right',
            width: '10%'
        }]]
    });
    //定义子库房
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
        $('#subStorage').combobox("select", "全部");
    });
    //类型字典
    var forms = [];
    var expFormDictPromise = $.get("/api/exp-form-dict/list", function (data) {
        $.each(data, function (index, item) {
            var ec = {};
            ec.formCode = item.formCode;
            ec.formName = item.formName;
            forms.push(ec);
        });
        var all = {};
        all.formCode = '';
        all.formName = '全部';
        forms.unshift(all);

        $('#formClass').combobox({
            panelHeight: 'auto',
            data: forms,
            valueField: 'formCode',
            textField: 'formName'
        });
    });

    //设置时间
    var curr_time = new Date();
    $("#startDate").datetimebox("setValue", myFormatter2(curr_time));
    $("#stopDate").datetimebox("setValue", myFormatter2(curr_time));
    $('#startDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate2,
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

    $('#stopDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate3,
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
    $("#searchBtn").on('click', function () {
        loadDict();
    });

    //为报表准备字段
    var startDates='';
    var stopDates='';
    var subStor='';
    var expForms='';

    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            if(subStor=='全部'){
                subStor='';
            }
            if(expForms=='全部'){
                expForms='';
            }
            startDates= $("#startDate").datebox("getText");
            stopDates= $("#stopDate").datebox("getText");
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/storage-exp-go-count.cpt"+"&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&startDate=" + startDates + "&stopDate=" + stopDates+"&subStorage="+subStor+"&expForm="+expForms;

            $("#report").prop("src",cjkEncode(https));
        }
    });
    $("#printBtn").on('click', function () {
        var printData = $("#importMaster").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    });
    var loadDict = function(){
        masterDataVo.formClass = $("#formClass").combobox("getText");
        var sub = $("#subStorage").textbox("getText");
        if(sub=="全部"){
            masterDataVo.subStorage ='';
        }else{
            masterDataVo.subStorage =sub;
        }
        masterDataVo.startDate =new Date($("#startDate").datebox("getText"));
        masterDataVo.stopDate = new Date($("#stopDate").datebox("getText"));
        masterDataVo.hospitalId = parent.config.hospitalId;
        masterDataVo.storage = parent.config.storageCode;
        var retailAmount = 0.00;
        var payAmount = 0.00;
        payAmount = parseFloat(payAmount);
        retailAmount = parseFloat(retailAmount);
        var promise =$.get("/api/exp-export/storage-exp-go-count",masterDataVo,function(data){
            for(var i = 0 ;i<data.length;i++){
                retailAmount += parseFloat(data[i].retailAmount);
                payAmount+=parseFloat(data[i].payAmount);

                data[i].retailAmount = fmoney(data[i].retailAmount, 2);
                data[i].payAmount = fmoney(data[i].payAmount, 2);
            }
            masters = data;
        },'json');
        promise.done(function(){
            if(masters.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                $("#importMaster").datagrid('loadData',[]);
                return;
            }

            startDates=masterDataVo.startDate;
            stopDates=masterDataVo.stopDate;
            subStor=masterDataVo.subStorage;
            expForms=masterDataVo.formClass;

            $("#importMaster").datagrid('loadData',masters);
            $('#importMaster').datagrid('appendRow', {
                receiver: '',
                expName: "合计：",
                retailAmount: fmoney(retailAmount,2),
                payAmount: fmoney(payAmount,2)
            });
            $("#importMaster").datagrid("autoMergeCells", ['expCode']);
        })
        masters.splice(0,masters.length);
        return promise;

    }

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