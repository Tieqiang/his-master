/**
 * 出库记录查询
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
    if(val!=null){
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
    ////格式化日期函数
    //function formatterDate(val, row) {
    //    if (val != null) {
    //        var date = new Date(val);
    //        var y = date.getFullYear();
    //        var m = date.getMonth() + 1;
    //        var d = date.getDate();
    //        var h = date.getHours();
    //        var mm = date.getMinutes();
    //        var s = date.getSeconds();
    //        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' '
    //            + (h < 10 ? ("0" + h) : h) + ":" + (mm < 10 ? ("0" + mm) : mm) + ":" + (s < 10 ? ("0" + s) : s);
    //        return dateTime
    //    }
    //}
    var masterDataVo = {};//主表vo
    var masters = [];//信息
    $('#startDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: myFormatter2,
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
        formatter: myFormatter2,
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
    /**
     * 定义主表信息表格
     */
    $("#importMaster").datagrid({
        title: "出库记录查询",
        footer: '#ft',
        toolbar:'#tb',
        fit: true,
        fitColumns: true,
        singleSelect: true,
        selectOnCheck:true,
        checkOnSelect:true,
        showFooter:true,
        rownumbers:true,
        columns: [[{
            title: '子库房',
            field: 'subStorage',
            width: '5%'
        },{
            title: '代码',
            field: 'expCode',
            width: '6%'
        },{
            title: '产品名称',
            field: 'expName',
            width: '7%'
        },{
            title: '产品类别',
            field: 'expForm',
            width: '5%'
        }, {
            title: '规格',
            field: 'packageSpec',
            width: '4%'
        },  {
            title: '单位',
            field: 'packageUnits',
            width: '4%'
        }, {
            title: '厂家',
            width: '8%',
            field: 'firmId'

        }, {
            title: "数量",
            width: '5%',
            field: 'quantity'
        }, {
            title: '进价',
            field: 'purchasePrice',
            width: '5%'
        }, {
            title: '进价金额',
            field: 'payAmount',
            width: '5%'
        }, {
            title: '零售价',
            width: '5%',
            field: 'retailPrice'
        }, {
            title: '零售金额',
            width: '5%',
            field: 'retailAmount'
        }, {
            title: '请领部门',
            width: '8%',
            field: 'receiver'
        }, {
            title: '部门属性',
            width: '6%',
            field: 'deptAttr'
        }, {
            title: '出库单号',
            width: '6%',
            field: 'documentNo',
            editor: {type: 'textbox'}
        }, {
            title: '有效期',
            width: '11%',
            field: 'expireDate',
            formatter: myFormatter2
        }, {
            title: '批号',
            width: '4%',
            field: 'batchNo'
        }]]
    });
    //类型字典
    var forms = [];
    //产品类别数据加载
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
    //科室属性
    $("#exportDeptAttr").combobox({
        url: '/api/base-dict/list-by-type?baseType=DEPT_CLINIC_ATTR_DICT&length=0',
        valueField: 'baseCode',
        textField: 'baseName',
        method: 'GET'
    });
    //设置时间
    var curr_time = new Date();
    $("#startDate").datetimebox("setValue", myFormatter2(curr_time));
    $("#stopDate").datetimebox("setValue", myFormatter2(curr_time));

    $('#searchInput').combogrid({
        panelWidth: 500,
        idField: 'expCode',
        textField: 'expName',
        url: '/api/exp-name-dict/list-exp-name-by-input',
        method: 'GET',
        mode: 'remote',
        columns: [[
            {field: 'expCode', title: '编码', width: 150, align: 'center'},
            {field: 'expName', title: '名称', width: 200, align: 'center'},
            {field: 'inputCode', title: '拼音', width: 50, align: 'center'}
        ]],
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        pageSize: 50,
        pageNumber: 1
    });
    $("#searchBtn").on('click', function () {
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
            $("#report").prop("src", parent.config.defaultReportPath + "exp-export-record-search.cpt");
        }
    })
    $("#printBtn").on('click', function () {
        var printData = $("#importMaster").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    })
    var loadDict = function(){
        masterDataVo.formClass = $("#formClass").combobox("getText");
        masterDataVo.deptAttr = $("#exportDeptAttr").textbox("getValue");
        masterDataVo.startDate = new Date($("#startDate").datebox("getText"));
        masterDataVo.stopDate = new Date($("#stopDate").datebox("getText"));
        masterDataVo.expCode = $("#searchInput").combogrid("getValue");
        masterDataVo.hospitalId = parent.config.hospitalId;
        masterDataVo.storage = parent.config.storageCode;
        var payAmount = 0.00;
        var retailAmount = 0.00;
        var promise =$.get("/api/exp-export/exp-export-record-search",masterDataVo,function(data){
            masters =data ;
            for(var i = 0 ;i<data.length;i++){
                retailAmount+=data[i].retailAmount;
                payAmount+=data[i].payAmount;
            }
        },'json');
        promise.done(function(){
            if(masters.length<=0){
                $.messager.alert('系统提示','数据库暂无数据','info')
                $("#importMaster").datagrid('loadData',[]);
                return;
            }
            $("#importMaster").datagrid('loadData',masters);
            $('#importMaster').datagrid('appendRow', {
                expName: "合计：",
                payAmount: payAmount,
                retailAmount: retailAmount
            });
            $("#importMaster").datagrid("autoMergeCells", ['subStorage']);


        })
        masters.splice(0,masters.length);
        return promise;
    }
})