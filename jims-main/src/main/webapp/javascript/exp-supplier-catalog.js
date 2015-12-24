/**
 * Created by tangxinbo on 2015/10/10.
 */
/**
 * 产品供应商目录维护
 */

//供应商类别 拼音二选一
function checkRadio(){
    if ($("#supplierType:checked").val() != null){
        $("#searchInputCode").textbox("disable");
        $("#searchInputCode").combogrid("clear");
        $("#supplierSelect").textbox("enable");
    }else {
        $("#searchInputCode").combobox("enable");
        $("#supplierSelect").combobox("clear");
        $("#supplierSelect").combobox("disable");
    }
}
$(document).ready(function () {
    var editRowIndex;

    //格式化日期函数
    function formatterDate(val, row) {
        if (val != null) {
            var date = new Date(val);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var h = date.getHours();
            var mm = date.getMinutes();
            var s = date.getSeconds();
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

    $("#dg").datagrid({
        title:"产品供应商目录维护",
        fit:true,
        fitColumns:false,
        toolbar:"#topbar",
        footer:"#tb",
        singleSelect:true,
        rownumbers:true,
        columns:[[{
                title:"简称",
                field:"supplierId",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"名称",
                field:"supplier",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"类别",
                field:"supplierClass",
                width:"10%",
                editor:{type:"combobox",options:{
                    valueField:"value",
                    textField:"text",
                    data:[
                        {
                            value:"供应商",
                            text:"供应商"
                        },{
                            value:"生产商",
                            text:"生产商"
                        }
                    ]
                }}
            }, {
                title: "产品范围",
                field: "suppbound",
                width:"10%",
                editor:{type:"combobox",options:{
                    valueField:"key",
                    textField:"value",
                    data:[{
                        key:"1",
                        value:"全院产品"
                     },{
                        key:"2",
                        value:"普通产品"
                    }]
                    }
                },formatter: function(value,row,index){
                if (row.suppbound='1'){
                    return '全院产品';
                }else{
                    return '普通产品';
                }
            }
            },{
                title:"输入码",
                field:"inputCode",
                width:"10%",
                editor:{type:"textbox",
                    options:{
                        editable: false,
                        disabled: true}
                }
            },{
                title:"备注",
                field:"memo",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"厂商地址",
                field:"supplierAddres",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"厂商邮编",
                field:"supplierPostalcode",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"法人姓名",
                field:"artificialPerson",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"联系电话",
                field:"linkphone",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"营业执照号",
                field:"licenceNo",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"营业执照期限",
                field:"licenceDate",
                width:"11%",
                formatter: formatterDate,
                editor: {
                    type: 'datetimebox',
                    options: {
                        value: 'dateTime',
                        showSeconds: true,
                        formatter: formatterDate,
                        parser: w3,
                        onSelect: function (date) {
                            var dateEd = $("#dg").datagrid('getEditor', {
                                index: editRowIndex,
                                field: 'licenceDate'
                            });
                            var y = date.getFullYear();
                            var m = date.getMonth() + 1;
                            var d = date.getDate();
                            var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
                            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;

                            $(dateEd.target).textbox('setValue', dateTime);
                            $(this).datetimebox('hidePanel');
                        }
                    }
                }
            },{
                title:"生产经营许可证号",
                field:"permitNo",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"许可证期限",
                field:"permitDate",
                width:"11%",
                formatter: formatterDate,
                editor: {
                    type: 'datetimebox',
                    options: {
                        value: 'dateTime',
                        showSeconds: true,
                        formatter: formatterDate,
                        parser: w3,
                        onSelect: function (date) {
                            var dateEd = $("#dg").datagrid('getEditor', {
                                index: editRowIndex,
                                field: 'permitDate'
                            });
                            var y = date.getFullYear();
                            var m = date.getMonth() + 1;
                            var d = date.getDate();
                            var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
                            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;

                            $(dateEd.target).textbox('setValue', dateTime);
                            $(this).datetimebox('hidePanel');
                        }
                    }
                }
            },{
                title:"医疗器械注册证号",
                field:"registerNo",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"美国或欧洲号",
                field:"fdaOrCeNo",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"截止日期",
                field:"fdaOrCeDate",
                width:"11%",
                formatter: formatterDate,
                editor: {
                    type: 'datetimebox',
                    options: {
                        value: 'dateTime',
                        showSeconds: true,
                        formatter: formatterDate,
                        parser: w3,
                        onSelect: function (date) {
                            var dateEd = $("#dg").datagrid('getEditor', {
                                index: editRowIndex,
                                field: 'fdaOrCeDate'
                            });
                            var y = date.getFullYear();
                            var m = date.getMonth() + 1;
                            var d = date.getDate();
                            var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
                            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;

                            $(dateEd.target).textbox('setValue', dateTime);
                            $(this).datetimebox('hidePanel');
                        }
                    }
                }
            },{
                title:"其它证号",
                field:"otherNo",
                width:"10%",
                editor:{type:"validatebox"}
            },{
                title:"其它日期",
                field:"otherDate",
                width:"11%",
                formatter: formatterDate,
                editor: {
                    type: 'datetimebox',
                    options: {
                        value: 'dateTime',
                        showSeconds: true,
                        formatter: formatterDate,
                        parser: w3,
                        onSelect: function (date) {
                            var dateEd = $("#dg").datagrid('getEditor', {
                                index: editRowIndex,
                                field: 'otherDate'
                            });
                            var y = date.getFullYear();
                            var m = date.getMonth() + 1;
                            var d = date.getDate();
                            var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
                            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;

                            $(dateEd.target).textbox('setValue', dateTime);
                            $(this).datetimebox('hidePanel');
                        }
                    }
                }
            }
        ]],
        onClickRow: function (rowIndex,rowData) {
            if (editRowIndex || editRowIndex == 0){
                $(this).datagrid("endEdit",editRowIndex);
            }
            $(this).datagrid("beginEdit",rowIndex);
            editRowIndex = rowIndex;
        }
    });
    //供应商检索
    //$("#searchInputCode").textbox({
    //    onChange: function (newValue,oldValue) {
    //        //alert(newValue);
    //        //alert(oldValue);
    //        if($("#supplierSearch:checked").val()){
    //            $.get("/api/exp-supplier-catalog/find-supplier?inputCode="+newValue, function (data) {
    //                $("#dg").datagrid("loadData",data);
    //            });
    //        }
    //    }
    //});
    $('#searchInputCode').combogrid({
        panelWidth: 500,
        idField: 'supplier',
        textField: 'supplier',
        loadMsg: '数据正在加载',
        url: "/api/exp-supplier-catalog/find-supplier-by-q",
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'supplierClass', title: '类别', width: 150, align: 'center'},
            {field: 'supplier', title: '名称', width: 200, align: 'center'},
            {field: 'inputCode', title: '拼音', width: 50, align: 'center'}
        ]],
        pagination: false,
        fitColumns: true,
        rowNumber: true,
        autoRowHeight: false,
        pageSize: 50,
        pageNumber: 1

    });
    //供应商查询
    $("#searchBtn").on("click", function () {
        //if ($("#supplierType:checked").val()){
        //    var selectedValue = $("#supplierSelect").combobox("getValue");
        //    var supplier = $("#searchInputCode").combogrid("getValue");
        //    if (!selectedValue || !supplier ){
        //        $.messager.alert("提示","请选择供应商","info");
        //        return ;
        //    }

            var selectedValue = $("#supplierSelect").combobox("getValue");
            var supplier = $("#searchInputCode").combogrid("getValue");
            if (!selectedValue && !supplier ){
                $.messager.alert("提示","请选择供应商","info");
                return ;
            }
            $.get("/api/exp-supplier-catalog/find-supplier?supplierName="+selectedValue+"&supplier="+supplier, function (data) {
                if(data.length<=0){
                    $.messager.alert('系统消息','数据库暂无数据','info')
                }
                console.log(data);
                $("#dg").datagrid("loadData",data);
            });

        //else {
        //    var searchValue = $("#searchInputCode").val();
        //    //alert(searchValue);
        //    $.get("/api/exp-supplier-catalog/find-supplier?inputCode="+searchValue, function (data) {
        //        $("#dg").datagrid("loadData",data);
        //    });
        //}
    });
    //添加按钮
    $("#addBtn").on("click", function () {
        $("#dg").datagrid("appendRow",{});
        var rows = $("#dg").datagrid("getRows"); //得到所有行
        var row = rows[rows.length - 1];//获得最后一行
        var row_index = $("#dg").datagrid("getRowIndex",row);//得到最后一行索引号

        $("#dg").datagrid("selectRow",row_index);//选择当前行
        //alert('editRowIndex'+editRowIndex+"row_index"+row_index);
        if (editRowIndex == undefined){
            $("#dg").datagrid("beginEdit",row_index);
            editRowIndex = row_index;
        }
        if(editRowIndex == row_index - 1 || editRowIndex != undefined){ //取消上一行的编辑 并且选中后取消编辑
            $("#dg").datagrid("endEdit",editRowIndex);
            editRowIndex =row_index;
            $("#dg").datagrid("beginEdit",editRowIndex);
        }
    });
    //修改
    $("#editBtn").on("click", function () {
        $("#dg").datagrid("endEdit",editRowIndex);
        var row = $("#dg").datagrid("getSelected");
        var row_index = $("#dg").datagrid("getRowIndex",row);

        $("#dg").datagrid("beginEdit",row_index);
        editRowIndex = row_index;

    });
    //删除
    $("#delBtn").on("click", function () {
        $("#dg").datagrid("endEdit",editRowIndex);
        var row = $("#dg").datagrid("getSelected");
        var row_index = $("#dg").datagrid("getRowIndex",row);
        if(row_index == -1){
            if  (editRowIndex == undefined){
                $.messager.alert("提示","请选择要删除的行","info");
                return ;
            }else {
                $("#dg").datagrid("deleteRow",editRowIndex);
                editRowIndex = undefined;
            }
        }else {
            $("#dg").datagrid("endEdit",editRowIndex);
            $("#dg").datagrid("deleteRow",row_index);
            editRowIndex = undefined
        }
    });

    //保存
    $("#saveBtn").on("click", function () {
        if (editRowIndex || editRowIndex == 0) {
            $("#dg").datagrid("endEdit", editRowIndex);
        }

        var insertData = $("#dg").datagrid("getChanges", "inserted");
        var updateDate = $("#dg").datagrid("getChanges", "updated");
        var deleteDate = $("#dg").datagrid("getChanges", "deleted");

        var beanChangeVo = {};
        beanChangeVo.inserted = insertData;
        beanChangeVo.deleted = deleteDate;
        beanChangeVo.updated = updateDate;

        if (beanChangeVo) {
            $.postJSON("/api/exp-supplier-catalog/merge", beanChangeVo, function (data, status) {
                $.messager.alert("系统提示", "保存成功", "info");
                $("#searchBtn").click();
            }, function (data) {
                $.messager.alert('提示', data.responseJSON.errorMessage, "error");
            })
        }

    });
    
    $("#clearBtn").on("click", function () {
        $("#dg").datagrid("loadData",{rows:[]});
        editRowIndex = undefined ;
    })

});