/**
 * Created by tangxinbo on 2015/10/13.
 */
/**
 * 产品库存定义
 */
function checkRadio(){
    if($("#productName:checked").val()){
        $("#productTypeSelect").combobox("disable");
        $("#productTypeSelect").combobox("clear");
        $("#inputCode").textbox("enable");
    }else{
        $("#productTypeSelect").combobox("enable");
        $("#inputCode").combogrid("disable");
        $("#inputCode").combogrid("clear");
    }
}
$(document).ready(function () {
    var currentExpCode;
    var editIndex;
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
    var easyFormDicts = [];//产品类别
    var packageUnits = [];//包装单位

    //产品类别查询
    var expFromDictPromise = $.get("/api/exp-form-dict/list", function (data) {
        $.each(data, function (index,item) {
            var easyFormDict ={};
            easyFormDict.formCode = item.formCode;
            easyFormDict.formName = item.formName ;
            //+ "(" + item.formCode + ")";
            easyFormDicts.push(easyFormDict);
        })
        var all = {};
        all.formCode = '';
        all.formName = '全部';
        easyFormDicts.unshift(all);
        $("#productTypeSelect").combobox({
            data:easyFormDicts,
            valueField:"formCode",
            textField:"formName"
        });
    });
    //产品包装单位查询
    var expPackageUnitPromise = $.get("/api/measures-dict/list", function (data) {
        $.each(data, function (index,item) {
            var expPackageUnit ={};
            expPackageUnit.measuresCode = item.measuresName;
            expPackageUnit.measuresName = item.measuresName;
            packageUnits.push(expPackageUnit);
        })
    });
    $('#inputCode').combogrid({
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
    $("#dg").datagrid({
        title:"产品库存定义",
        fit:true,
        toolbar:"#topBar",
        footer:"#tb",
        singleSelect:true,
        rownumbers:true,
        columns:[[{
            title: 'id',
            field: 'id',
            hidden: true
        }, {
            title: "库房代码",
            field: "storage",
            hidden: true
        }, {
            title: "消耗品代码",
            field: "expCode",
            hidden: true
        },{
            title:"品名",
            field:"expName",
            width:"20%",
            editor: {
                type: 'combogrid', options: {
                    mode: 'remote',
                    url: '/api/exp-dict/list-exp-name-define-by-input',
                    singleSelect: true,
                    method: 'GET',
                    panelWidth: 400,
                    idField: 'expName',
                    textField: 'expName',
                    columns: [[{
                        title: '代码',
                        field: 'expCode',
                        width: "20%"
                    }, {
                        title: '品名',
                        field: 'expName',
                        width: "20%",
                        editor: {type: 'text', options: {required: true}}
                    }, {
                        title: '规格',
                        field: 'expSpec',
                        width: "20%"
                    }, {
                        title: '单位',
                        field: 'units',
                        width: "30%"
                    }]],
                    onClickRow: function (index, row) {
                        $('#dg').datagrid('endEdit',editIndex);
                        $('#dg').datagrid('updateRow',{
                            index: editIndex,
                            row: {
                                expSpec: row.expSpec,
                                expName: row.expName,
                                expForm: row.expForm,
                                upperLevel: 0,
                                lowLevel: 0,
                                amountPerPackage: 0,
                                storage: parent.config.storageCode,
                                expCode: row.expCode,
                                location: row.expCode,
                                units: row.units
                            }
                        });

                        $('#dg').datagrid('beginEdit',editIndex);
                        //$(this).combogrid('hidePanel');
                        //currentExpCode = row.expCode;
                        //$("#stockRecordDialog").dialog('open');
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            $(this).combogrid('hidePanel');
                            $('#dg').datagrid('endEdit',editIndex);
                            if (row) {
                                $('#dg').datagrid('updateRow',{
                                    index: editIndex,
                                    row: {
                                        expSpec: row.expSpec,
                                        expName: row.expName,
                                        expForm: row.expForm,
                                        upperLevel: 0,
                                        lowLevel: 0,
                                        amountPerPackage: 0,
                                        storage: parent.config.storageCode,
                                        expCode: row.expCode,
                                        location: row.expCode,
                                        units: row.units
                                    }
                                });
                                //currentExpCode = row.expCode;
                                //$("#stockRecordDialog").dialog('open');
                            }
                            $('#dg').datagrid('beginEdit',editIndex);

                        }
                    })
                }
            },
            width:"7%"
        }, {
            title: "类型",
            field: "expForm",
            width: "10%"
        },{
            title:"规格",
            field:"expSpec",
            width:"10%"
        },{
            title:"单位",
            field:"units",
            width:"10%"
        },{
            title:"常规包装数量",
            field:"amountPerPackage",
            width:"10%",
            editor:{type:"validatebox"}
        },{
            title:"常规包装单位",
            field:"packageUnits",
            width:"10%",
            editor:{type:"combobox",options:{
                data:packageUnits,
                valueField:"measuresCode",
                textField:"measuresName"
            }}
        },{
            title:"高位水平",
            field:"upperLevel",
            width:"10%",
            editor:{type:"validatebox"}
        },{
            title:"低位水平",
            field:"lowLevel",
            width:"10%",
            editor:{type:"validatebox"}
        },{
            title:"位置",
            field:"location",
            hidden:true
        }]],
        onClickRow: function (index, row) {
            stopEdit();
            $(this).datagrid('beginEdit', index);
            editIndex = index;
        }
    });
    var define = {};
    //查询
    $("#searchBtn").on("click", function () {
        if (!$("#productTypeSelect").combobox("getText") && $("#productType:checked").val() ){
            $.messager.alert("提示","请选择查询内容","info");
            return ;
        }
        var expCode = $("#inputCode").combogrid('getValue');
        var expForm = $("#productTypeSelect").combogrid('getText');
       var promise =  $.get("/api/exp-stock-define/find-stock-define?expCode="+expCode+"&expForm="+expForm+"&storage="+parent.config.storageCode, function (data) {
            define = data;
            if(define.length==0){
                $.messager.alert('系统提示','数据库暂无数据','info');
                $("#dg").datagrid('loadData',[]);
                return;
            }
           return define;
        })
        promise.done(function(){
            $("#dg").datagrid('loadData',define);
        })

    });



    //添加
    $("#addBtn").on("click", function () {
        stopEdit();
        $("#dg").datagrid('appendRow', {storage:parent.config.storageCode});
        var rows = $("#dg").datagrid('getRows');
        var addRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);
        editIndex = addRowIndex;
        $("#dg").datagrid('selectRow', editIndex);
        $("#dg").datagrid('beginEdit', editIndex);
    });
    // 删除
    $("#delBtn").on("click", function () {
        var row = $("#dg").datagrid('getSelected');
        if (row) {
            var rowIndex = $("#dg").datagrid('getRowIndex', row);
            $("#dg").datagrid('deleteRow', rowIndex);
            if (editIndex == rowIndex) {
                editIndex = undefined;
            }
        } else {
            $.messager.alert('系统提示', "请选择要删除的行", 'info');
        }
    });

    //保存
    $("#saveBtn").on("click", function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid("endEdit", editIndex);
        }
        var rows = $("#dg").datagrid("getRows");
        for(var i = 0;i<rows.length;i++){
            if($.trim(rows[i].expName)==""){
                $.messager.alert('系统提示','第'+(parseInt(i+1))+'行数据为空，请规范操作本系统！','error');
                return;
            }
        }
        var insertData = $("#dg").datagrid("getChanges","inserted");
        var updateData = $("#dg").datagrid("getChanges","updated");
        var deleteData = $("#dg").datagrid("getChanges","deleted");
        if(insertData.length>0||updateData.length>0||deleteData.length>0){
            var beanChangeVo = {};
            beanChangeVo.inserted = insertData;
            beanChangeVo.deleted = deleteData;
            beanChangeVo.updated = updateData;

            if (beanChangeVo) {
                $.postJSON("/api/exp-stock-define/save", beanChangeVo, function (data) {
                    $.messager.alert("提示", "保存成功", "info");
                    $("#searchBtn").click();
                }, function (data) {
                    $.messager.alert("提示", data.responseJSON.errorMessage, "error");
                })
            }
        }else{
            $.messager.alert('系统消息','没有要保存的数据，请规范操作本系统！','error');
            return;
        }

    });
});