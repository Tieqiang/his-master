/**
 * Created by fyg on 2016/5/28.
 */
//供应商类别 拼音二选一
function checkRadio() {
    if ($("#supplierType:checked").val() != null) {
        $("#searchInputCode").textbox("disable");
        $("#searchInputCode").combogrid("clear");
        $("#supplierSelect").textbox("enable");
    } else {
        $("#searchInputCode").combobox("enable");
        $("#supplierSelect").combobox("clear");
        $("#supplierSelect").combobox("disable");
    }
}
$(function () {
    var editIndex;
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
    //格式化日期函数
    function formatterDate(val) {   //把number类型的毫秒数转换成字符串   1464417772000 ---> 2016-05-28 14:42:52
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
            return dateTime;
        }
    }

    $("#dg").datagrid({
        title: "产品供应商目录维护",
        fit: true,
        singleSelect: true,
        rownumbers: true,
        toolbar: "#topbar",
        footer: "#tb",
        columns: [[{
            title: "简称",
            field: "supplierId",
            width: "10%",
            align: 'center',
            editor: 'text'
        }, {
            title: "名称",
            field: "supplier",
            width: "10%",
            align: 'center',
            editor: 'text'
        }, {
            title: "类别",
            field: "supplierClass",
            align: 'center',
            width: "8%",
            editor: {
                type: "combobox", options: {
                    valueField: "value",
                    textField: "text",
                    data: [
                        {
                            value: "供应商",
                            text: "供应商"
                        }, {
                            value: "生产商",
                            text: "生产商"
                        }
                    ]
                }
            }
        }, {
            title: "产品范围",
            field: "suppbound",
            width: "8%",
            align: 'center',
            editor: {
                type: "combobox", options: {
                    editable: false,
                    valueField: "key",
                    textField: "value",
                    data: [{
                        key: "1",
                        value: "全院产品"
                    }, {
                        key: "2",
                        value: "普通产品"
                    }]
                }
            }, formatter: function (value, row, index) {
                if (row.suppbound == '1') {
                    return '全院产品';
                } else if (row.suppbound == '2') {
                    return '普通产品';
                }
                return value;
            }
        }, {
            title: "输入码",
            field: "inputCode",
            width: "8%",
            align: 'center',
            editor: {
                type: "textbox",
                options: {
                    editable: false,
                    disabled: true
                }
            }
        }, {
            title: "备注",
            field: "memo",
            width: "6%",
            align: 'center',
            editor: 'text'
        }, {
            title: "厂商地址",
            field: "supplierAddres",
            width: "9%",
            align: 'center',
            editor: 'text'
        }, {
            title: "厂商邮编",
            field: "supplierPostalcode",
            width: "6%",
            align: 'center',
            editor: 'text'
        }, {
            title: "法人姓名",
            field: "artificialPerson",
            width: "6%",
            align: 'center',
            editor: 'text'
        }, {
            title: "联系电话",
            field: "linkphone",
            width: "7%",
            align: 'center',
            editor: 'text'
        }, {
            title: "营业执照号",
            field: "licenceNo",
            width: "8%",
            align: 'center',
            editor: 'text'
        }, {
            title: "营业执照期限",
            field: "licenceDate",
            width: "11%",
            align: 'center',
            formatter: formatDateBoxFull,
            editor: 'datetimebox'
        }, {
            title: "生产经营许可证号",
            field: "permitNo",
            width: "10%",
            align: 'center',
            editor: 'text'
        }, {
            title: "许可证期限",
            field: "permitDate",
            width: "11%",
            align: 'center',
            formatter: formatDateBoxFull,
            editor: 'datetimebox'
        }, {
            title: "医疗器械注册证号",
            field: "registerNo",
            width: "10%",
            align: 'center',
            editor: 'text'
        }, {
            title: "美国或欧洲号",
            field: "fdaOrCeNo",
            width: "9%",
            align: 'center',
            editor: 'text'
        }, {
            title: "截止日期",
            field: "fdaOrCeDate",
            width: "11%",
            align: 'center',
            formatter: formatDateBoxFull,
            editor: 'datetimebox'
        }, {
            title: "其它证号",
            field: "otherNo",
            width: "8%",
            align: 'center',
            editor: 'text'
        }, {
            title: "其它日期",
            field: "otherDate",
            width: "11%",
            align: 'center',
            formatter: formatDateBoxFull,
            editor: 'datetimebox'
        }]],
        onClickRow: function (index, row) {
            stopEdit();
            $(this).datagrid('beginEdit', index);
            editIndex = index;

        }
    });
    //供应商拼音字头检索
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
    //供应商查询按钮
    $("#searchBtn").on("click", function () {
        var selectedValue = $("#supplierSelect").combobox("getValue");
        var supplier = $("#searchInputCode").combogrid("getValue");
        if (!selectedValue && !supplier) {
            $.messager.alert("提示", "请选择供应商", "info");
            return;
        }
        $.get("/api/exp-supplier-catalog/find-supplier?supplierName=" + selectedValue + "&supplier=" + supplier, function (data) {
            for (var i = 0; i < data.length; i++) {
            }
            if (data.length <= 0) {
                $.messager.alert('系统消息', '数据库暂无数据', 'info')
            }
            $("#dg").datagrid("loadData", data);
        });
    });
    //添加按钮
    $("#addBtn").on("click", function () {
        stopEdit();
        $("#dg").datagrid('appendRow', {});
        var rows = $("#dg").datagrid('getRows');
        var addRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);
        editIndex = addRowIndex;
        $("#dg").datagrid('selectRow', editIndex);
        $("#dg").datagrid('beginEdit', editIndex);
    });
    //删除
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
    //清屏按钮
    $("#clearBtn").on("click", function () {
        $("#dg").datagrid("loadData", {rows: []});
        editIndex = undefined;
    });
    //保存
    $("#saveBtn").on("click", function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid("endEdit", editIndex);
        }
        var insertData = $("#dg").datagrid("getChanges", "inserted");
        var updateDate = $("#dg").datagrid("getChanges", "updated");
        var deleteDate = $("#dg").datagrid("getChanges", "deleted");
        if (insertData.length > 0) {
            for (var i = 0; i < insertData.length; i++) {
                console.log("名称:" + insertData[i].supplierClass);
                console.log("length:" + insertData[i].supplierClass.length);
                if (insertData[i].supplier.length == 0) {
                    $.messager.alert('提示', '名称不能为空!', 'error');
                    return;
                }
                if (insertData[i].supplierClass.length == 0) {
                    $.messager.alert('提示', '请选择类别!', 'error');
                    return;
                }
                console.log("insertData[i].licenceDate:" + insertData[i].licenceDate);
                insertData[i].licenceDate = new Date(insertData[i].licenceDate);
                insertData[i].permitDate = new Date(insertData[i].permitDate);
                insertData[i].fdaOrCeDate = new Date(insertData[i].fdaOrCeDate);
                insertData[i].otherDate = new Date(insertData[i].otherDate);
            }
        }
        if (updateDate.length > 0) {
            for (var i = 0; i < updateDate.length; i++) {
                updateDate[i].licenceDate = new Date(updateDate[i].licenceDate);
                updateDate[i].permitDate = new Date(updateDate[i].permitDate);
                updateDate[i].fdaOrCeDate = new Date(updateDate[i].fdaOrCeDate);
                updateDate[i].otherDate = new Date(updateDate[i].otherDate);
            }
        }
        if (deleteDate.length > 0) {
            for (var i = 0; i < insertData.length; i++) {
                deleteDate[i].licenceDate = new Date(deleteDate[i].licenceDate);
                deleteDate[i].permitDate = new Date(deleteDate[i].permitDate);
                deleteDate[i].fdaOrCeDate = new Date(deleteDate[i].fdaOrCeDate);
                deleteDate[i].otherDate = new Date(deleteDate[i].otherDate);
            }
        }
        if (insertData.length > 0 || updateDate.length > 0 || deleteDate.length > 0) {
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
        } else {
            $.messager.alert('系统消息', '没有要保存的信息，请规范操作系统！', 'info')
        }
    });
});


/*该方法使日期列的显示符合阅读习惯*/
//datagrid中用法：{ field:'StartDate',title:'开始日期', formatter: formatDatebox, width:80}
function formatDatebox(value) {
    if (value == null || value == '') {
        return '';
    }
    var dt = parseToDate(value);
    return dt.format("yyyy-MM-dd");
}

/*转换日期字符串为带时间的格式*/
function formatDateBoxFull(value) {
    if (value == null || value == '') {
        return '';
    }
    var dt = parseToDate(value);
    return dt.format("yyyy-MM-dd hh:mm:ss");
}

//辅助方法(转换日期)
function parseToDate(value) {
    if (value == null || value == '') {
        return undefined;
    }

    var dt;
    if (value instanceof Date) {
        dt = value;
    }
    else {
        if (!isNaN(value)) {
            dt = new Date(value);
        }
        else if (value.indexOf('/Date') > -1) {
            value = value.replace(/\/Date\((-?\d+)\)\//, '$1');
            dt = new Date();
            dt.setTime(value);
        } else if (value.indexOf('/') > -1) {
            dt = new Date(Date.parse(value.replace(/-/g, '/')));
        } else {
            dt = new Date(value);
        }
    }
    return dt;
}

//为Date类型拓展一个format方法，用于格式化日期
Date.prototype.format = function (format) //author: meizz
{
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(),    //day
        "h+": this.getHours(),   //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds() //millisecond
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1,
                RegExp.$1.length == 1 ? o[k] :
                    ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};

//以下拓展是为了datagrid的日期列在编辑状态下显示正确日期
$.extend(
    $.fn.datagrid.defaults.editors, {
        datebox: {
            init: function (container, options) {
                var input = $('<input type="text">').appendTo(container);
                input.datebox(options);
                return input;
            },
            destroy: function (target) {
                $(target).datebox('destroy');
            },
            getValue: function (target) {
                return $(target).datebox('getValue');
            },
            setValue: function (target, value) {
                $(target).datebox('setValue', formatDatebox(value));
            },
            resize: function (target, width) {
                $(target).datebox('resize', width);
            }
        },
        datetimebox: {
            init: function (container, options) {
                var input = $('<input type="text">').appendTo(container);
                input.datetimebox(options);
                return input;
            },
            destroy: function (target) {
                $(target).datetimebox('destroy');
            },
            getValue: function (target) {
                return $(target).datetimebox('getValue');
            },
            setValue: function (target, value) {
                $(target).datetimebox('setValue', formatDateBoxFull(value));
            },
            resize: function (target, width) {
                $(target).datetimebox('resize', width);
            }
        }
    });

