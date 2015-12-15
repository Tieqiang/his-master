/**
 * Created by tangxinbo on 2015/10/14.
 */
/**
 * 科室申请录入
 */
$(document).ready(function () {
    var editRowIndex;
    var storageDicts = [];
    //var rowDatas ;
    var storageCode;

    //库房加载
    var storageDictsPromise = $.get("/api/exp-storage-dept/list?hospitalId="+parent.config.hospitalId, function (data) {
        $.each(data, function (index, item) {
            var storage = {};
            storage.storageCode = item.storageCode;
            storage.storageName = item.storageName;
            storageDicts.push(storage);
        });
        $("#storage").combobox({
            data:storageDicts,
            valueField:"storageCode",
            textField:"storageName",
            onSelect: function () {
                storageCode = $("#storage").combobox("getValue");
                //alert(storageCode);
            }
        });
    });

    $("#dg").datagrid({
        title:"科室申请录入",
        fit:true,
        toolbar:"#topBar",
        footer:"#tb",
        singleSelect:true,
        rownumbers:true,
        columns:[[{
            title:"applicationId",
            field:"applicationId",
            width:"10%",
            hidden:true
        }, {
            title: "applicantStorage",
            field: "applicantStorage",
            width: "10%",
            hidden: true
        }, {
            title: "expSpec",
            field: "expSpec",
            width: "10%",
            hidden: true
        }, {
            title: "申请单号",
            field: "applicantNo",
            width: "10%",
            editor: {
                type: "textbox",
                options: {
                    disabled: true
                }
            }
        },{
            title:"代码",
            field:"expCode",
            width:"10%",
            editor:{
                type:"textbox",
                options: {
                    disabled: true
                }}
        },{
            title:"名称",
            field:"expName",
            width:"10%",
            editor:{type:"combogrid",options:{
                panelWidth:580,
                idField:"expName",
                textField:"expName",
                url: "/api/exp-price-list/find-exp-list",
                mode: 'remote',
                onBeforeLoad: function (para) {
                    para.storageCode = storageCode;
                },
                method: 'GET',
                columns: [[
                    {field: 'expCode', title: '编码', width: 200, align: 'center'},
                    {field: 'expName', title: '名称', width: 150, align: 'center'},
                    {field: 'expSpec', title: '规格', width: 50, align: 'center'},
                    {field: 'units', title: '单位', width: 50, align: 'center'},
                    {field: 'firmId', title: '厂家', width: 100, align: 'center'},
                    {field: 'inputCode', title: '拼音码', width: 50, align: 'center'},
                ]],
                onClickRow: function (rowIndex,rowData) {
                    $("#dg").datagrid('endEdit', editRowIndex);
                    var rowDetail = $("#dg").datagrid('getData').rows[editRowIndex];

                    rowDetail.packageSpec = rowData.expSpec;
                    rowDetail.expCode = rowData.expCode;
                    rowDetail.packageUnits = rowData.units;
                    rowDetail.expSpec = rowData.expSpec;
                    $("#dg").datagrid('refreshRow', editRowIndex);
                    $("#dg").datagrid('beginEdit', editRowIndex);
                }
            }}
        },{
            title:"规格",
            field:"packageSpec",
            width:"10%",
            editor:{
                type:"textbox",
                options: {
                    disabled: true
                }}
        },{
            title:"单位",
            field:"packageUnits",
            width:"10%",
            editor:{
                type:"textbox",
                options: {
                    disabled: true
                }}
        },{
            title:"数量",
            field:"quantity",
            width:"10%",
            editor:{type:"numberbox"}
        },{
            title:"申请人",
            field:"applicationMan",
            width:"10%",
            hidden:true,
            editor:{
                type:"textbox",
                options: {
                    disabled: true
                }}
        },{
            title:"被申请科室",
            field:"provideName",
            width:"10%",
            editor:{
                type:"textbox",
                options: {
                    disabled: true
                }}
        }, {
            title: "申请科室",
            field: "applicantName",
            width: "10%",
            editor: {
                type: "textbox",
                options: {
                    disabled: true
                }
            }
        }]],
        onClickRow: function (rowIndex,rowData) {
            if (editRowIndex || editRowIndex == 0){
                $(this).datagrid("endEdit",editRowIndex);
            }
            $(this).datagrid("beginEdit",rowIndex);
            editRowIndex = rowIndex;
        }
    });
    //查询
    $("#searchBtn").on("click", function () {
        var provideStorage = $("#storage").combobox("getValue");
        var applicantNo = $("#applicantNo").textbox("getValue");
        $.get("/api/exp-provide-application/find-dict-application?applyStorage=" + parent.config.storageCode+"&applyNo="+applicantNo+"&storageCode="+ provideStorage, function (data) {
                if (data.length == 0) {
                    $.messager.alert("提示", "未查询到数据", "info");
                    $("#dg").datagrid("loadData", {rows: []});
                    return;
                } else {
                    $("#dg").datagrid("loadData", data);
                }
            }
        );
    });
    //新增
    $("#addBtn").on("click", function () {
        if ($("#storage").textbox("getText") == ""){
            $.messager.alert("提示","请选择库房","info");
            return ;
        }
        var applicationStorage = $("#storage").combobox("getValue");
        $("#dg").datagrid("appendRow",{provideStorage: applicationStorage,applicationMan:parent.config.staffName, applicantStorage:parent.config.storageCode});
    });
    //删除
    $("#delBtn").on("click", function () {
        var row = $("#dg").datagrid("getSelected");
        var index = $("#dg").datagrid("getRowIndex",row);

        if (index == -1){
            if (editRowIndex == undefined){
                $.messager.alert("提示","请选择删除的行","info");
                return ;
            }else {
                $("#dg").datagrid("deleteRow",editRowIndex);
                editRowIndex = undefined;
            }
        }else {
            $("#dg").datagrid("endEdit",editRowIndex);
            $("#dg").datagrid("deleteRow",index);
            editRowIndex = undefined;

        }
    });

    // 保存
    $("#saveBtn").on("click", function () {
        if (editRowIndex != undefined){
            $("#dg").datagrid("endEdit",editRowIndex);
            editRowIndex = undefined;
        }


        var insertData = $("#dg").datagrid("getChanges","inserted");
        var updateData = $("#dg").datagrid("getChanges","updated");
        var deleteData = $("#dg").datagrid("getChanges","deleted");


        var changeVo = {};
        changeVo.inserted = insertData;
        changeVo.updated = updateData;
        changeVo.deleted = deleteData;

        if (changeVo) {
            $.postJSON("/api/exp-provide-application/save", changeVo, function (data) {
                $.messager.alert("系统提示", "保存成功", "info");
            }, function (data) {
                $.messager.alert('提示', "保存失败", "error");
            })
        }

    });


    //新单
    $("#newBtn").on("click", function () {
        $("#dg").datagrid("loadData",{rows:[]});
        editRowIndex = undefined;
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
            var applicationStorage = $("#storage").combobox("getValue");
            var applicantNo = $("#applicantNo").textbox("getValue");
            $("#report").prop("src",parent.config.defaultReportPath + "/exp/exp_print/exp-provide-application.cpt&storage="+parent.config.storageCode+"&applicantNo="+applicantNo+"&applicationStorage="+applicationStorage);
        }
    })
    $("#printBtn").on('click',function(){
        var printData = $("#dg").datagrid('getRows');
        if(printData.length<=0){
            $.messager.alert('系统提示','请先查询数据','info');
            return;
        }
        $("#printDiv").dialog('open');

    })


});