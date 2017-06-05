/**
 * 消耗品备货功能
 * Created by heren on 2016/4/11.
 */

$(function () {

    /**
     * 供货方
     *
     */
    var receiver = [];//供应商
    var staffs = [];//员工信息
    var editIndex;
    var currentSupplierCode;
    var currentProduceCode;
    var currentExpCode;
    var setDefaultDate = function () {
        var date = new Date();
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
            return dateTime;
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

   //定义expName
    $('#expName').combogrid({
        panelWidth: 500,
        idField: 'expCode',
        textField: 'expName',
        mode: 'remote',
        method: 'GET',
        columns: [[
            {field: 'expCode', title: '编码', width: 150, align: 'center'},
            {field: 'expName', title: '名称', width: 200, align: 'center'},
            {field: 'inputCode', title: '拼音', width: 50, align: 'center'}
        ]],
        loadMsg: 'loading',
        fitColumns: true,
        url: "/api/exp-name-dict/list-exp-name-by-input"
    });
    /**
     * 定义明细信息表格
     */
    $("#backDetail").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        showFooter: true,
        title: "备货明细",
        footer: '#ft',
        toolbar: '#expBackFormDiv',
        columns: [[{
            title: 'id',
            field: 'id',
            hidden:true,
            editor: {
                type: 'textbox', options: {
                    editable: false,
                    disabled: true
                }
            }
        },{
            title: '项目代码',
            field: 'expCode',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '品名',
            field: 'expName',
            width: '10%',
            editor: {
                type: 'combogrid', options: {
                    mode: 'remote',
                    url: '/api/exp-dict/exp-dict-list-by-input',
                    singleSelect: true,
                    method: 'GET',
                    panelWidth: 200,
                    idField: 'expName',
                    textField: 'expName',
                    columns: [[{
                        title: '代码',
                        field: 'expCode',
                        width: "30%"
                    }, {
                        title: '品名',
                        field: 'expName',
                        width: "40%",
                        editor: {type: 'text', options: {required: true}}
                    }, {
                        title: '规格',
                        field: 'expSpec',
                        width: "40%",
                        editor: {type: 'text', options: {required: true}}
                    }, {
                        title: '单位',
                        field: 'units',
                        width: "40%",
                        editor: {type: 'text', options: {required: true}}
                    },{
                        title: '类型',
                        field: 'expForm',
                        width: "40%",
                        editor: {type: 'text', options: {required: true}}
                    }, {
                        title: '最小规格',
                        field: 'minSpec',
                        width: "40%",
                        editor: {type: 'text', options: {required: true}}
                    }, {
                        title: '最小单位',
                        field: 'minUnits',
                        width: "40%",
                        editor: {type: 'text', options: {required: true}}
                    }, {
                        title: '拼音码',
                        field: 'inputCode',
                        width: "30%",
                        editor: 'text'
                    }]],
                    onClickRow: function (index, row) {
                        if(row){
                            var edExpCode = $("#backDetail").datagrid('getEditor', {
                                index: editIndex,
                                field: 'expCode'
                            });
                            $(edExpCode.target).textbox('setValue', row.expCode);
                            var edExpSpec = $("#backDetail").datagrid('getEditor', {
                                index: editIndex,
                                field: 'expSpec'
                            });
                            $(edExpSpec.target).textbox('setValue', row.expSpec);
                            var edUnits = $("#backDetail").datagrid('getEditor', {index: editIndex, field: 'units'});
                            $(edUnits.target).textbox('setValue', row.units);
                            var edMinSpec = $("#backDetail").datagrid('getEditor', {
                                index: editIndex,
                                field: 'minSpec'
                            });
                            $(edMinSpec.target).textbox('setValue', row.minSpec);
                            var edExpForm = $("#backDetail").datagrid('getEditor', {
                                index: editIndex,
                                field: 'expForm'
                            });
                            $(edExpForm.target).textbox('setValue', row.expForm);
                            currentExpCode = row.expCode;
                            if (currentExpCode != "" && currentProduceCode != "" & currentSupplierCode != "") {
                                var edBackCode = $("#backDetail").datagrid('getEditor', {
                                    index: editIndex,
                                    field: 'backCode'
                                });
                                $(edBackCode.target).textbox('setValue', currentExpCode + currentProduceCode + currentSupplierCode);
                            }
                        }

                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            if (row) {
                                var edExpCode = $("#backDetail").datagrid('getEditor', {
                                    index: editIndex,
                                    field: 'expCode'
                                });
                                $(edExpCode.target).textbox('setValue', row.expCode);
                                var edExpSpec = $("#backDetail").datagrid('getEditor', {
                                    index: editIndex,
                                    field: 'expSpec'
                                });
                                $(edExpSpec.target).textbox('setValue', row.expSpec);
                                var edUnits = $("#backDetail").datagrid('getEditor', {
                                    index: editIndex,
                                    field: 'units'
                                });
                                $(edUnits.target).textbox('setValue', row.units);
                                var edMinSpec = $("#backDetail").datagrid('getEditor', {
                                    index: editIndex,
                                    field: 'minSpec'
                                });
                                $(edMinSpec.target).textbox('setValue', row.minSpec);
                                var edExpForm = $("#backDetail").datagrid('getEditor', {
                                    index: editIndex,
                                    field: 'expForm'
                                });
                                $(edExpForm.target).textbox('setValue', row.expForm);
                                currentExpCode = row.expCode;
                                if (currentExpCode != "" && currentProduceCode != "" & currentSupplierCode != "") {
                                    var edBackCode = $("#backDetail").datagrid('getEditor', {
                                        index: editIndex,
                                        field: 'backCode'
                                    });
                                    $(edBackCode.target).textbox('setValue', currentExpCode + currentProduceCode + currentSupplierCode);
                                }
                            }
                            $(this).combogrid('hidePanel');
                        }
                    })
                }
            }
        }, {
            title: '包装规格',
            field: 'expSpec',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '包装单位',
            field: 'units',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '产品类型',
            field: 'expForm',
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '供应商',
            field: 'supplierId',
            width: "10%",
            editor: {
                type: 'combogrid', options: {
                    //mode: 'remote',
                    url: '/api/exp-supplier-catalog/find-supplier?supplierName=供应商',
                    singleSelect: true,
                    method: 'GET',
                    panelWidth: 200,
                    idField: 'supplierId',
                    textField: 'supplierId',
                    columns: [[{
                        title: '供应商',
                        field: 'supplierId',
                        width: "30%"
                    }, {
                        title: '全称',
                        field: 'supplier',
                        width: "40%",
                        editor: {type: 'text', options: {required: true}}
                    }]],
                    onClickRow: function (index, row) {
                        if (row) {
                            var edSupplier = $("#backDetail").datagrid('getEditor', {
                                index: editIndex,
                                field: 'supplier'
                            });
                            $(edSupplier.target).textbox('setValue', row.supplier);

                            currentSupplierCode = row.supplierCode;
                            if (currentExpCode != "" && currentProduceCode != "" & currentSupplierCode != "") {
                                var edBackCode = $("#backDetail").datagrid('getEditor', {
                                    index: editIndex,
                                    field: 'backCode'
                                });
                                $(edBackCode.target).textbox('setValue', currentExpCode + currentProduceCode + currentSupplierCode);
                            }
                        }

                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            if (row) {
                                var edSupplier = $("#backDetail").datagrid('getEditor', {
                                    index: editIndex,
                                    field: 'supplier'
                                });
                                $(edSupplier.target).textbox('setValue', row.supplier);

                                currentSupplierCode = row.supplierCode;
                                if (currentExpCode != "" && currentProduceCode != "" & currentSupplierCode != "") {
                                    var edBackCode = $("#backDetail").datagrid('getEditor', {
                                        index: editIndex,
                                        field: 'backCode'
                                    });
                                    $(edBackCode.target).textbox('setValue', currentExpCode + currentProduceCode + currentSupplierCode);
                                }
                            }
                            $(this).combogrid('hidePanel');
                        }
                    })
                }
            }
        }, {
            title: '供应商全称',
            field: 'supplier',
            hidden:true,
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '生产商',
            field: 'produceId',
            width: "10%",
            editor: {
                type: 'combogrid', options: {
                    //mode: 'remote',
                    url: '/api/exp-supplier-catalog/find-supplier?supplierName=生产商',
                    singleSelect: true,
                    method: 'GET',
                    panelWidth: 200,
                    idField: 'supplierId',
                    textField: 'supplierId',
                    columns: [[{
                        title: '生产商',
                        field: 'supplierId',
                        width: "30%"
                    }, {
                        title: '全称',
                        field: 'supplier',
                        width: "40%",
                        editor: {type: 'text', options: {required: true}}
                    }]],
                    onClickRow: function (index, row) {
                        if (row) {
                            var edProduceId = $("#backDetail").datagrid('getEditor', {
                                index: editIndex,
                                field: 'produce'
                            });
                            $(edProduceId.target).textbox('setValue', row.supplier);

                            currentProduceCode = row.supplierCode;
                            if (currentExpCode != "" && currentProduceCode != "" & currentSupplierCode != "") {
                                var edBackCode = $("#backDetail").datagrid('getEditor', {
                                    index: editIndex,
                                    field: 'backCode'
                                });
                                $(edBackCode.target).textbox('setValue', currentExpCode + currentProduceCode + currentSupplierCode);
                            }
                        }

                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            if (row) {
                                var edProduceId = $("#backDetail").datagrid('getEditor', {
                                    index: editIndex,
                                    field: 'produce'
                                });
                                $(edProduceId.target).textbox('setValue', row.supplier);

                                currentProduceCode = row.supplierCode;
                                if(currentExpCode!=""&&currentProduceCode!=""&currentSupplierCode!=""){
                                    var edBackCode = $("#backDetail").datagrid('getEditor', {
                                        index: editIndex,
                                        field: 'backCode'
                                    });
                                    $(edBackCode.target).textbox('setValue', currentExpCode + currentProduceCode + currentSupplierCode);
                                }
                            }
                            $(this).combogrid('hidePanel');
                        }
                    })
                }
            }
        }, {
            title: '生产商全称',
            field: 'produce',
            hidden:true,
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '有效日期',
            field: 'expireDate',
            formatter: formatterDate,
            width:'13%',
            editor: {
                type: 'datetimebox',
                options: {
                    value: 'dateTime',
                    showSeconds: true,
                    formatter: formatterDate,
                    parser: w3,
                    onSelect: function (date) {
                        var dateEd = $("#backDetail").datagrid('getEditor', {
                            index: editIndex,
                            field: 'expireDate'
                        });
                        var y = date.getFullYear()+1;
                        var m = date.getMonth() + 1;
                        var d = date.getDate();
                        var time = $(dateEd.target).datetimebox('spinner').spinner('getValue');
                        var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;

                        $(dateEd.target).textbox('setValue', dateTime);
                        $(this).datetimebox('hidePanel');
                    }
                }
            }
        }, {
            title: '生产日期',
            field: 'produceDate',
            width:'13%',
            formatter: formatterDate,
            editor: {
                type: 'datetimebox',
                options: {
                    value: 'dateTime',
                    showSeconds: true,
                    formatter: formatterDate,
                    parser: w3,
                    onSelect: function (date) {
                        var dateEd = $("#backDetail").datagrid('getEditor', {
                            index: editIndex,
                            field: 'produceDate'
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
        }, {
            title: '进货价',
            field: 'tradePrice',
            width: '7%',
            //hidden: true,
            editor: {type: 'numberbox', options: {
                precision: '2',
            onChange:function(newValue, oldValue){
                var ed = $("#backDetail").datagrid('getEditor', {
                    index: editIndex,
                    field: 'retailPrice'
                });
                $(ed.target).textbox('setValue', newValue*1.05);
            }}}
        }, {
            title: '零售价',
            field: 'retailPrice',
            width: '7%',
            //hidden: true,
            editor: {type: 'numberbox', options: {
                editable: false,
                disabled: true,precision: '2'}}
        }, {
            title: '最小规格',
            field: 'minSpec',
            hidden: true,
            editor: {type: 'textbox', options: {
                editable: false,
                disabled: true}}
        }, {
            title: '最小单位',
            field: 'minUnits',
            hidden: true,
            editor: {type: 'textbox', options: {}}
        }, {
            title: '条码值',
            field: 'backCode',
            width: '12%',
            editor: {
                type: 'textbox', options: {
                    editable: false,
                    disabled: true
                }
            }
        }
        ]],
        onDblClickRow: function (index, field, row) {
            if (index != editIndex) {
                $(this).datagrid('endEdit', editIndex);
                editIndex = index;
            }
            $(this).datagrid('beginEdit', editIndex);
            var ed = $(this).datagrid('getEditor', {index: index, field: field});
            $(ed.target).focus();
        }
    });

    $('#importDate').datetimebox({
        required: true,
        showSeconds: true,
        value: 'dateTime',
        formatter: formatterDate,
        onSelect: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var time = $('#importDate').datetimebox('spinner').spinner('getValue');
            var dateTime = y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d) + ' ' + time;
            $('#importDate').datetimebox('setText', dateTime);
            $('#importDate').datetimebox('hidePanel');
        }
    });



    var suppliers = [];
    var promise = $.get("/api/exp-supplier-catalog/find-supplier?supplierName=供应商", function (data) {
        console.log(data)
        suppliers = data;
    });

    promise.done(function () {
        $("#supplier").combogrid({
            idField: 'supplier',
            textField: 'supplier',
            data: suppliers,
            panelWidth: 500,
            fitColumns: true,
            columns: [[{
                title: '供应商名称',
                field: 'supplier', width: 200, align: 'center'
            }, {
                title: '供应商代码',
                field: 'supplierCode', width: 150, align: 'center'
            }, {
                title: '输入码',
                field: 'inputCode', width: 50, align: 'center'
            }]]
        })
    });
    var produces = [];
    var promiseProduce = $.get("/api/exp-supplier-catalog/find-supplier?supplierName=生产商", function (data) {
        produces = data;
    });

    promiseProduce.done(function () {
        $("#produce").combogrid({
            idField: 'supplier',
            textField: 'supplier',
            data: produces,
            panelWidth: 500,
            fitColumns: true,
            columns: [[{
                title: '生产商名称',
                field: 'supplier', width: 200, align: 'center'
            }, {
                title: '生产商代码',
                field: 'supplierCode', width: 150, align: 'center'
            }, {
                title: '输入码',
                field: 'inputCode', width: 50, align: 'center'
            }]]
        })
    });

    //追加

    $("#addRow").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#backDetail").datagrid('endEdit', editIndex);
        }
        $("#backDetail").datagrid('appendRow', {});
        var rows = $("#backDetail").datagrid('getRows');
        var appendRowIndex = $("#backDetail").datagrid('getRowIndex', rows[rows.length - 1]);
        currentExpCode = "";
        currentProduceCode = "";
        currentSupplierCode = "";
        editIndex = appendRowIndex;
        $("#backDetail").datagrid('beginEdit', editIndex);


    })

    /**
     * 查询
     */
    $("#searchBtn").on('click',function(){
        parent.addTab('目录维护', '/his/ieqm/exp-name-dict');
    })
    /**
     * 定义新供应商
     */
    $("#newSupplier").on('click',function(){
        parent.addTab('产品供应商目录维护', '/his/ieqm/exp-supplier-catalog');
    })
    /**
     * 删除按钮
     */
    $("#delRow").on('click', function () {
        var row = $("#backDetail").datagrid('getSelected');
        if (row) {
            var index = $("#backDetail").datagrid('getRowIndex', row);
            $("#backDetail").datagrid('deleteRow', index);
            if (editIndex == index) {
                editIndex = undefined;
            }
        } else {
            $.messager.alert("系统提示", "请选择要删除的行", 'info');
        }
    });
    /**
     * 保存功能
     */
    $("#saveBtn").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#backDetail").datagrid('endEdit', editIndex);
        }
        var rows = $("#backDetail").datagrid('getRows');
        console.log(rows)
        var inserted = $("#backDetail").datagrid('getChanges', 'inserted');
        var deleted = $("#backDetail").datagrid('getChanges', 'deleted');
        var updated = $("#backDetail").datagrid('getChanges', 'updated');
        console.log(inserted)
        $.each(inserted, function (index, item) {
            //格式化日期
            item.expireDate = new Date(item.expireDate);
            item.produceDate = new Date(item.produceDate);
            item.operator = parent.config.staffName;
            item.hospitalId = parent.config.hospitalId;
        });
        $.each(updated, function (index, item) {
            //格式化日期
            item.expireDate = new Date(item.expireDate);
            item.operator = parent.config.staffName;
            item.hospitalId = parent.config.hospitalId;
            item.produceDate = new Date(item.produceDate);
        });
        $.each(deleted, function (index, item) {
            //格式化日期
            item.expireDate = new Date(item.expireDate);
            item.produceDate = new Date(item.produceDate);
        });
        console.log(inserted)
        var expStockBackChangeVo = {};
        expStockBackChangeVo.inserted = inserted;
        expStockBackChangeVo.deleted = deleted;
        expStockBackChangeVo.updated = updated;

        $.postJSON("/api/exp-stock-back/save", expStockBackChangeVo, function (data) {
            $.messager.alert('系统提示', '保存成功', 'success',function(){
                $("#backDetail").datagrid('loadData', {total: 0, rows: []});
            });
        }, function (data) {
            $.messager.alert("系统提示", data.responseJSON.errorMessage, 'error');
            $("#backDetail").datagrid('loadData', {total: 0, rows: []});
        })


    });



    /**
     * 查询
     */
    $("#search").on('click',function(){
        var searchVo = {};
        searchVo.expName = $("#expName").combobox('getValue');
        searchVo.supplier = $("#supplier").combogrid('getValue');
        searchVo.produce = $("#produce").combogrid('getValue');
        searchVo.expireDate  = $("#importDate").datetimebox('getText');
        $.get("/api/exp-stock-back/search",searchVo, function (data) {
            if(data.length==0){
                $.messager.alert('系统提示','查询范围内暂无数据，请重置查询条件！','info');
                $("#backDetail").datagrid('loadData',{total:0,rows:[]});
            }
            $("#backDetail").datagrid('loadData',data);
        });
    })

    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        buttons: '#printft',
        closed: true,
        onOpen: function () {
            var expName = $("#expName").combobox('getValue');
            var supplier = $("#supplier").combogrid('getValue');
            var produce = $("#produce").combogrid('getValue');
            var expireDate = $("#importDate").datetimebox('getText');
            //$("#report").prop("src",  "http://localhost:8075/WebReport/ReportServer?reportlet=exp%2Fexp%2Fexp-import.cpt&__bypagesize__=false&documentNo="+printDocumentNo);
            $("#report").prop("src", parent.config.defaultReportPath + "exp_stock_back.cpt&expName=" + expName+"&supplier="+supplier+"&produce="+produce+"&expireDate="+expireDate);
        }
    })
    $("#printClose").on('click',function(){
        $("#printDiv").dialog('close');
        parent.updateTab('备货功能', '/his/ieqm/exp-stock-back');
    })
    $("#printBtn").on('click', function () {
        $("#printDiv").dialog('open');
    })
})