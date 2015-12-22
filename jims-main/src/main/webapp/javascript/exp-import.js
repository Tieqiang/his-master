/**
 * 消耗品入库功能
 * Created by heren on 2015/10/19.
 */

$(function () {

    /**
     * 供货方
     *
     */
    var suppliers = [];//供应商

    var staffs = [];//员工信息

    var documentNo;//入库单号

    var editIndex;
    var currentExpCode;
    var flag;


    /**
     * 定义明细信息表格
     */
    $("#importDetail").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: true,
        showFooter: true,
        title: "消耗品入库单",
        footer: '#ft',
        toolbar: '#expImportMaster',
        columns: [[{
            title: '项目代码',
            field: 'expCode',
            editor: {type: 'textbox'}
        }, {
            title: '品名',
            field: 'expName',
            width: '10%',
            editor: {
                type: 'combogrid', options: {
                    mode: 'remote',
                    url: '/api/exp-name-dict/list-exp-name-by-input',
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
                        title: '拼音码',
                        field: 'inputCode',
                        width: "30%",
                        editor: 'text'
                    }]],
                    onClickRow: function (index, row) {
                        var ed = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expCode'});
                        $(ed.target).textbox('setValue', row.expCode);
                        currentExpCode = row.expCode;
                        $("#stockRecordDialog").dialog('open');
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            if (row) {
                                var ed = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expCode'});
                                $(ed.target).textbox('setValue', row.expCode);
                                currentExpCode = row.expCode;
                                $("#stockRecordDialog").dialog('open');
                            }
                            $(this).combogrid('hidePanel');
                        }
                    })
                }
            }
        }, {
            title: '规格',
            field: 'packageSpec',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '单位',
            field: 'packageUnits',
            editor: {type: 'textbox', options: {}}
        }, {
            title: "数量",
            field: 'quantity',
            width:'5%',
            editor: {
                type: 'numberbox', options: {
                    onChange: function (newValue, oldValue) {
                        var purchasePriceEd = $("#importDetail").datagrid('getEditor', {
                            index: editIndex,
                            field: 'purchasePrice'
                        });
                        var purchasePrice = $(purchasePriceEd.target).textbox('getValue');

                        var amountEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'amount'});
                        $(amountEd.target).numberbox('setValue', newValue * purchasePrice);

                        var rows = $("#importDetail").datagrid('getRows');
                        var totalAmount = 0;
                        for (var i = 0; i < rows.length; i++) {
                            var rowIndex = $("#importDetail").datagrid('getRowIndex', rows[i]);
                            if (rowIndex == editIndex) {
                                continue;
                            }
                            totalAmount += Number(rows[i].amount);
                        }
                        if (totalAmount) {
                            totalAmount += newValue * purchasePrice;
                        } else {
                            totalAmount = newValue * purchasePrice;
                        }
                        $("#accountReceivable").numberbox('setValue', totalAmount);
                    }
                }
            }
        }, {
            title: '批号',
            field: 'batchNo',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '进价',
            field: 'purchasePrice',
            editor: {type: 'numberbox', options: {precision: '2', min: "0.01"}}
        }, {
            title: '金额',
            field: 'amount',
            editor: {
                type: 'numberbox', options: {
                    precision: '2',
                    editable: false,
                    disabled: true
                }
            }
        }, {
            title: '产品类型',
            field: 'expForm',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '有效日期',
            field: 'expireDate',
            width:'7%',
            editor: {
                type: 'datebox', options: {
                    formatter: function (date) {
                        if (date) {
                            var y = date.getFullYear();
                            var m = date.getMonth() + 1;
                            var d = date.getDate();
                            return y + "-" + m + "-" + d
                        }

                    },
                    parser: function (date) {
                        if (date) {
                            return new Date(Date.parse(date.replace(/-/g, "/")));
                        }
                        return null;
                    }
                }
            }
        }, {
            title: '厂家',
            field: 'firmId',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '注册证号',
            field: 'expImportDetailRegistNo',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '许可证号',
            field: 'expImportDetailLicenceno',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '发票号',
            field: 'invoiceNo',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '发票日期',
            field: 'invoiceDate',
            width:'7%',
            editor: {
                type: 'datebox', options: {
                    formatter: function (date) {
                        if (date) {
                            var y = date.getFullYear();
                            var m = date.getMonth() + 1;
                            var d = date.getDate();
                            return y + "-" + m + "-" + d
                        }

                    },
                    parser: function (date) {
                        if (date) {
                            return new Date(Date.parse(date.replace(/-/g, "/")));
                        }
                        return null;
                    }
                }
            }
        }, {
            title: '备注',
            field: 'memo',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '生产日期',
            field: 'produceDate',
            width:'7%',
            editor: {
                type: 'datebox', options: {
                    formatter: function (date) {
                        if (date) {
                            var y = date.getFullYear();
                            var m = date.getMonth() + 1;
                            var d = date.getDate();
                            return y + "-" + m + "-" + d
                        }

                    },
                    parser: function (date) {
                        if (date) {
                            return new Date(Date.parse(date.replace(/-/g, "/")));
                        }
                        return null;
                    }
                }
            }
        }, {
            title: '消毒日期',
            field: 'disinfectDate',
            width:'7%',
            editor: {
                type: 'datebox', options: {
                    formatter: function (date) {
                        if (date) {
                            var y = date.getFullYear();
                            var m = date.getMonth() + 1;
                            var d = date.getDate();
                            return y + "-" + m + "-" + d
                        }

                    },
                    parser: function (date) {
                        if (date) {
                            return new Date(Date.parse(date.replace(/-/g, "/")));
                        }
                        return null;
                    }
                }
            }
        }, {
            title: '灭菌标志',
            field: 'killFlag',
            editor: {type: 'checkbox', options: {}}
        }, {
            title: '折扣',
            field: 'discount',
            editor: {type: 'textbox', options: {}}
        }, {

            title: '招标文号',
            field: 'orderBatch',
            editor: {type: 'textbox', options: {}}

        }, {
            title: '招标文号序号',
            field: 'tenderNo',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '现有数量',
            field: 'inventory',
            editor: {type: 'numberbox', options: {precision: '2'}}
        }, {
            title: '零售价',
            field: 'retailPrice',
            hidden: true,
            editor: {type: 'numberbox', options: {precision: '2'}}
        }, {
            title: '进货价',
            field: 'tradePrice',
            hidden: true,
            editor: {type: 'numberbox', options: {precision: '2'}}
        }, {
            title: '最小规格',
            field: 'expSpec',
            hidden: true,
            editor: {type: 'textbox', options: {}}
        }, {
            title: '最小单位',
            field: 'units',
            hidden: true,
            editor: {type: 'textbox', options: {}}
        }
        ]],
        onClickCell: function (index, field, row) {
            if (index != editIndex) {
                $(this).datagrid('endEdit', editIndex);
                editIndex = index;
            }

            $(this).datagrid('beginEdit', editIndex);

            var ed = $(this).datagrid('getEditor', {index: index, field: field});
            $(ed.target).focus();
        }
    });

    //入库分类字典
    $("#importClass").combobox({
        url: '/api/exp-import-class-dict/list',
        valueField: 'importClass',
        textField: 'importClass',
        method: 'GET',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            $(this).combobox('select', data[0].importClass);
        }
    });

    $("#documentNo").textbox({
        disabled: true
    });//设置入库单号不能进行编辑

    /**
     * 创建新的入库单号
     * @param subStorageCode
     * @returns {*}
     */
    var createNewDocument = function (subStorageCode) {
        var storage;
        var promise = $.get('/api/exp-sub-storage-dict/list-by-storage?storageCode=' + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId, function (data) {
            $.each(data, function (index, item) {
                if (item.subStorage == subStorageCode) {
                    storage = item;
                }
            });

            if (storage) {
                if (storage.importNoPrefix.length <= 4) {
                    documentNo = storage.importNoPrefix + '000000'.substring((storage.importNoAva + "").length) + storage.importNoAva;
                } else if (storage.importNoPrefix.length = 5) {
                    documentNo = storage.importNoPrefix + '00000'.substring((storage.importNoAva + "").length) + storage.importNoAva;
                } else if (storage.importNoPrefix.length = 6) {
                    documentNo = storage.importNoPrefix + '0000'.substring((storage.importNoAva + "").length) + storage.importNoAva;
                }
            }
        });

        return promise;
    }

    $("#subStorage").combobox({
        url: '/api/exp-sub-storage-dict/list-by-storage?storageCode=' + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId,
        valueField: 'subStorage',
        textField: 'subStorage',
        method: 'GET',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('select', data[0].subStorage);
            }
        },
        onChange: function (newValue, oldValue) {
            var promise = createNewDocument(newValue);
            promise.done(function () {
                $("#documentNo").textbox('setValue', documentNo);
            })
        }
    });


    $("#tenderType").combobox({
        url: '/api/exp-tender-type-dict/list',
        valueField: 'tenderTypeCode',
        textField: 'tenderTypeName',
        method: 'GET',
        onLoadSuccess: function () {
            var data = $(this).combobox('getData');
            if (data.length > 0) {
                $(this).combobox('select', data[0].tenderTypeCode);
            }
        }
    })

    $("#importDate").datebox({
        currentText: '选择入库日期',
        okText: '确定',
        closeText: '关闭',
        formatter: function (date) {
            if (date) {
                var y = date.getFullYear();
                var m = date.getMonth() + 1;
                var d = date.getDate();
                return y + "-" + m + "-" + d
            }

        },
        parser: function (date) {
            if (date) {
                return new Date(Date.parse(date.replace(/-/g, "/")));
            }
            return null;
        }
    });

    var staffPromise = $.get('/api/staff-dict/list-by-hospital?hospitalId=' + parent.config.hospitalId, function (data) {
        staffs = data;
    });

    /**
     * 初始化人员行管
     */
    staffPromise.done(function () {
        var staffSetting = {
            idField: 'loginName',
            textField: 'name',
            model: 'remote',
            data: staffs,
            columns: [[{
                title: '登陆名',
                field: 'loginName'
            }, {
                title: '名字',
                field: 'name'
            }, {
                title: '职称',
                field: 'title'
            }, {
                title: '科室',
                field: 'deptDict',
                formatter: function (value, row, index) {
                    if (row.deptDict) {
                        return row.deptDict.deptName;
                    } else {
                        return value;
                    }
                }
            }]]
        };

        /**
         * 负责人下拉框
         */
        $("#principal").combogrid(staffSetting)

        /**
         * 保管人下拉框
         */
        $("#storekeeper").combogrid(staffSetting)

        /**
         * 采购人下拉框
         */
        $("#buyer").combogrid(staffSetting)

        /**
         * 验收人下拉框
         */
        $("#checkMan").combogrid(staffSetting)
    })


    var setDefaultDate = function () {
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return m + '/' + d + '/' + y
    }
    $("#importDate").datebox('setValue', setDefaultDate())


    var promise = $.get("/api/exp-supplier-catalog/list-with-dept?hospitalId=" + parent.config.hospitalId, function (data) {
        suppliers = data;
    });

    promise.done(function () {
        $("#supplier").combogrid({
            idField: 'supplierName',
            textField: 'supplierName',
            data: suppliers,
            panelWidth: 200,
            columns: [[{
                title: '供应商名称',
                field: 'supplierName'
            }, {
                title: '供应商代码',
                field: 'supplierCode'
            }, {
                title: '输入码',
                field: 'inputCode'
            }]],
            filter: function (q, row) {
                return $.startWith(row.inputCode.toUpperCase(), q.toUpperCase());
            }
        })
    });
    var setDefaultDate = function () {
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return m + '/' + d + '/' + y
    }
    $("#importDate").datebox('setValue', setDefaultDate())

    //追加

    $("#addRow").on('click', function () {
        flag=0;
        $("#importDetail").datagrid('appendRow', {});
        var rows = $("#importDetail").datagrid('getRows');
        var appendRowIndex = $("#importDetail").datagrid('getRowIndex', rows[rows.length - 1]);

        if (editIndex || editIndex == 0) {
            $("#importDetail").datagrid('endEdit', editIndex);
        }
        editIndex = appendRowIndex;
        $("#importDetail").datagrid('beginEdit', editIndex);

    })

    $("#stockRecordDialog").dialog({
        title: '选择规格',
        width: 700,
        height: 300,
        closed: false,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            $("#stockRecordDatagrid").datagrid('load', {
                storageCode: parent.config.storageCode,
                expCode: currentExpCode,
                hospitalId: parent.config.hospitalId
            });
            $("#stockRecordDatagrid").datagrid('selectRow', 0)
        }
    });

    $("#stockRecordDatagrid").datagrid({
        singleSelect: true,
        fit: true,
        fitColumns: true,
        url: '/api/exp-stock/stock-record/',
        method: 'GET',
        columns: [[{
            title: '代码',
            field: 'expCode'
        }, {
            title: '名称',
            field: 'expName'
        }, {
            title: '数量',
            field: 'quantity'
        }, {
            title: '规格',
            field: 'expSpec'
        }, {
            title: '最小规格',
            field: 'minSpec'
        }, {
            title: '单位',
            field: 'units'
        }, {
            title: '最小单位',
            field: 'minUnits'
        }, {
            title: '厂家',
            field: 'firmId'
        }, {
            title: '批发价',
            field: 'tradePrice'
        }, {
            title: '零售价',
            field: 'retailPrice'
        }, {
            title: '注册证号',
            field: 'registerNo'
        }, {
            title: '许可证号',
            field: 'permitNo'
        }]],
        onLoadSuccess:function(data){
            flag = flag+1;
            if(flag==1){
                if(data.total==0 && editIndex!=undefined){
                    //$("#exportDetail").datagrid('endEdit', editIndex);
                    $.messager.alert('系统提示','库房暂无该产品,请重置产品名称','info');
                    $("#stockRecordDialog").dialog('close');
                    //$("#exportDetail").datagrid('beginEdit', editIndex);
                }
                flag=0;
            }
        },
        onClickRow: function (index, row) {
            var expCodeEdit = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expCode'});
            $(expCodeEdit.target).textbox('setValue', row.expCode);

            var expNameEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expName'});
            $(expNameEd.target).textbox('setValue', row.expName);

            var packageSpecEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'packageSpec'});
            $(packageSpecEd.target).textbox('setValue', row.expSpec);

            var packageUnitsEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'packageUnits'});
            $(packageUnitsEd.target).textbox('setValue', row.units);

            var SpecEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expSpec'});
            $(SpecEd.target).textbox('setValue', row.minSpec);

            var unitsEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'units'});
            $(unitsEd.target).textbox('setValue', row.minUnits);


            var quantityEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'quantity'});
            $(quantityEd.target).textbox('setValue', 0);
            $(quantityEd.target).focus();

            var batchNoEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'batchNo'});
            $(batchNoEd.target).textbox('setValue', 'X');

            var purchasePriceEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'purchasePrice'});
            $(purchasePriceEd.target).textbox('setValue', row.tradePrice);

            var amountEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'amount'});
            $(amountEd.target).textbox('setValue', 0);

            var expFormEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expForm'});
            $(expFormEd.target).textbox('setValue', '消耗材料');

            var firmIdEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'firmId'});
            $(firmIdEd.target).textbox('setValue', row.firmId);
            var inventoryEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'inventory'});
            $(inventoryEd.target).textbox('setValue', row.quantity);

            var expImportDetailRegistNoEd = $("#importDetail").datagrid('getEditor', {
                index: editIndex,
                field: 'expImportDetailRegistNo'
            });
            $(expImportDetailRegistNoEd.target).textbox('setValue', row.registerNo);

            var expImportDetailLicencenoEd = $("#importDetail").datagrid('getEditor', {
                index: editIndex,
                field: 'expImportDetailLicenceno'
            });
            $(expImportDetailLicencenoEd.target).textbox('setValue', row.permitNo);

            var invoiceDateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'invoiceDate'});
            $(invoiceDateEd.target).textbox('setValue', setDefaultDate());
            var produceDateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'produceDate'});
            $(produceDateEd.target).textbox('setValue', setDefaultDate());
            var disinfectDateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'disinfectDate'});
            $(disinfectDateEd.target).textbox('setValue', setDefaultDate());

            var discountEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'discount'});
            $(discountEd.target).textbox('setValue', '100');

            var orderBatchEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'orderBatch'});
            $(orderBatchEd.target).textbox('setValue', 'x');

            var retailedEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'retailPrice'});
            $(retailedEd.target).numberbox('setValue', 'x');

            var tradePriceEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'tradePrice'});
            $(tradePriceEd.target).numberbox('setValue', 'x');

            $("#stockRecordDialog").dialog('close');
        }

    });

    //stockRecord

    $("#stockRecordDialog").dialog({
        title: '选择规格',
        width: 700,
        height: 300,
        closed: false,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            $("#stockRecordDatagrid").datagrid('load', {
                storageCode: parent.config.storageCode,
                expCode: currentExpCode,
                hospitalId: parent.config.hospitalId
            });
            $("#stockRecordDatagrid").datagrid('selectRow', 0)
        }
    });

    $("#stockRecordDatagrid").datagrid({
        singleSelect: true,
        fit: true,
        fitColumns: true,
        url: '/api/exp-stock/stock-record/',
        method: 'GET',
        columns: [[{
            title: '代码',
            field: 'expCode'
        }, {
            title: '名称',
            field: 'expName'
        }, {
            title: '数量',
            field: 'quantity'
        }, {
            title: '规格',
            field: 'expSpec'
        }, {
            title: '最小规格',
            field: 'minSpec'
        }, {
            title: '单位',
            field: 'units'
        }, {
            title: '最小单位',
            field: 'minUnits'
        }, {
            title: '厂家',
            field: 'firmId'
        }, {
            title: '批发价',
            field: 'tradePrice'
        }, {
            title: '零售价',
            field: 'retailPrice'
        }, {
            title: '注册证号',
            field: 'registerNo'
        }, {
            title: '许可证号',
            field: 'permitNo'
        }]],
        onLoadSuccess:function(data){
            flag = flag+1;
            if(flag==1){
                if(data.total==0 && editIndex!=undefined){
                    //$("#exportDetail").datagrid('endEdit', editIndex);
                    $.messager.alert('系统提示','库房暂无该产品,请重置产品名称','info');
                    $("#stockRecordDialog").dialog('close');
                    //$("#exportDetail").datagrid('beginEdit', editIndex);
                }
                flag=0;
            }
        },
        onClickRow: function (index, row) {
            var expCodeEdit = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expCode'});
            $(expCodeEdit.target).textbox('setValue', row.expCode);

            var expNameEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expName'});
            $(expNameEd.target).textbox('setValue', row.expName);

            var packageSpecEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'packageSpec'});
            $(packageSpecEd.target).textbox('setValue', row.expSpec);

            var packageUnitsEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'packageUnits'});
            $(packageUnitsEd.target).textbox('setValue', row.units);

            var SpecEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expSpec'});
            $(SpecEd.target).textbox('setValue', row.minSpec);

            var unitsEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'units'});
            $(unitsEd.target).textbox('setValue', row.minUnits);


            var quantityEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'quantity'});
            $(quantityEd.target).textbox('setValue', 0);
            $(quantityEd.target).focus();

            var batchNoEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'batchNo'});
            $(batchNoEd.target).textbox('setValue', 'X');

            var purchasePriceEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'purchasePrice'});
            $(purchasePriceEd.target).textbox('setValue', row.tradePrice);

            var amountEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'amount'});
            $(amountEd.target).textbox('setValue', 0);

            var expFormEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'expForm'});
            $(expFormEd.target).textbox('setValue', '消耗材料');

            var firmIdEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'firmId'});
            $(firmIdEd.target).textbox('setValue', row.firmId);
            var inventoryEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'inventory'});
            $(inventoryEd.target).textbox('setValue', row.quantity);

            var expImportDetailRegistNoEd = $("#importDetail").datagrid('getEditor', {
                index: editIndex,
                field: 'expImportDetailRegistNo'
            });
            $(expImportDetailRegistNoEd.target).textbox('setValue', row.registerNo);

            var expImportDetailLicencenoEd = $("#importDetail").datagrid('getEditor', {
                index: editIndex,
                field: 'expImportDetailLicenceno'
            });
            $(expImportDetailLicencenoEd.target).textbox('setValue', row.permitNo);

            var invoiceDateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'invoiceDate'});
            $(invoiceDateEd.target).textbox('setValue', setDefaultDate());
            var produceDateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'produceDate'});
            $(produceDateEd.target).textbox('setValue', setDefaultDate());
            var disinfectDateEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'disinfectDate'});
            $(disinfectDateEd.target).textbox('setValue', setDefaultDate());

            var discountEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'discount'});
            $(discountEd.target).textbox('setValue', '100');

            var orderBatchEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'orderBatch'});
            $(orderBatchEd.target).textbox('setValue', 'x');

            var retailedEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'retailPrice'});
            $(retailedEd.target).numberbox('setValue', 'x');

            var tradePriceEd = $("#importDetail").datagrid('getEditor', {index: editIndex, field: 'tradePrice'});
            $(tradePriceEd.target).numberbox('setValue', 'x');

            $("#stockRecordDialog").dialog('close');
        }

    });
    /**
     * 查询
     */
    $("#searchBtn").on('click',function(){
        parent.addTab('入库单据查询', '/his/ieqm/exp-import-document-search');
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
        var row = $("#importDetail").datagrid('getSelected');
        if (row) {
            var index = $("#importDetail").datagrid('getRowIndex', row);
            $("#importDetail").datagrid('deleteRow', index);
            if (editIndex == index) {
                editIndex = undefined;
            }
        } else {
            $.messager.alert("系统提示", "请选择要删除的行", 'info');
        }
    });

    /**
     * 进行数据校验
     */
    var dataValid = function () {
        var rows = $("#importDetail").datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].quantity == 0) {
                $.messager.alert("系统提示", "第" + i + "行入库数量为0 请重新填写", 'error');
                return false;
            }
        }

        if (rows.length == 0) {
            $.messager.alert("系统提示", "明细记录为空，不允许保存", 'error');
            return false;
        }

        //判断供货商是否为空
        var supplier = $("#supplier").combogrid('getValue');
        if (!supplier) {
            $.messager.alert("系统提示", "产品入库，供货商不能为空", 'error');
            return false;
        }

        var importDate = $("#importDate").datebox('calendar').calendar('options').current;
        if (!importDate) {
            $.messager.alert("系统提示", "产品入库，入库时间不能为空", 'error');
            return false;
        }
        console.log(importDate);
        return true;
    }

    var getCommitData = function(){
        var expImportMasterBeanChangeVo = {};
        expImportMasterBeanChangeVo.inserted = [];
        var importMaster = {};
        importMaster.importClass = $("#importClass").combobox('getValue');
        importMaster.importDate = $("#importDate").datebox('calendar').calendar('options').current;
        importMaster.storage = parent.config.storageCode;
        importMaster.documentNo = $("#documentNo").textbox('getValue');
        importMaster.supplier = $("#supplier").combogrid('getValue');
        importMaster.accountReceivable = $("#accountReceivable").numberbox('getValue');
        importMaster.accountPayed = $("#accountPayed").numberbox('getValue');
        importMaster.additionalFee = $("#additionalFee").numberbox('getValue');
        importMaster.importClass = $("#importClass").combobox('getValue');
        importMaster.subStorage = $("#subStorage").combobox('getValue');
        importMaster.accountIndicator = 0;
        importMaster.docStatus = 0;
        importMaster.memos = $('#memos').textbox('getValue');
        importMaster.operator = parent.config.staffName;
        importMaster.principal = $("#principal").combogrid('getText');
        importMaster.storekeeper = $("#storekeeper").combogrid('getText');
        importMaster.buyer = $("#buyer").combogrid('getText');
        importMaster.checkman = $("#checkMan").combogrid('getText');
        importMaster.tenderNo = $("#tenderNo").textbox('getValue');
        importMaster.tenderType = $("#tenderType").combobox('getValue');
        importMaster.hospitalId = parent.config.hospitalId;
        expImportMasterBeanChangeVo.inserted.push(importMaster);

        //明细记录
        var expImportDetailBeanChangeVo = {};
        expImportDetailBeanChangeVo.inserted = [];

        var rows = $("#importDetail").datagrid('getRows');

        for (var i = 0; i < rows.length; i++) {
            var detail = {};
            detail.documentNo = importMaster.documentNo;
            detail.itemNo = i;
            var rowIndex = $("#importDetail").datagrid('getRowIndex', rows[i]);

            detail.expCode = rows[i].expCode;
            detail.expSpec = rows[i].expSpec;
            detail.units = rows[i].units;
            detail.packageSpec = rows[i].packageSpec;
            detail.packageUnits = rows[i].packageUnits;
            detail.quantity = rows[i].quantity;
            detail.batchNo = rows[i].batchNo;
            detail.purchasePrice = rows[i].purchasePrice;

            detail.expireDate = new Date(rows[i].expireDate);
            detail.expForm = rows[i].expForm;
            detail.firmId = rows[i].firmId;
            detail.retailPrice = rows[i].retailPrice;
            detail.tradePrice = rows[i].retailPrice;

            detail.killflag = rows[i].killflag;
            detail.discount = rows[i].discount;
            detail.orderBatch = rows[i].orderBatch;
            detail.tenderNo = rows[i].tenderNo;
            detail.invoiceDate = new Date(rows[i].invoiceDate);
            detail.producedate = new Date(rows[i].producedate);
            detail.disinfectdate = new Date(rows[i].disinfectdate);
            detail.invoiceNo = rows[i].invoiceNo;
            detail.hospitalId = parent.config.hospitalId;

            expImportDetailBeanChangeVo.inserted.push(detail);
        }

        var importVo = {};
        importVo.expImportMasterBeanChangeVo = expImportMasterBeanChangeVo;
        importVo.expImportDetailBeanChangeVo = expImportDetailBeanChangeVo;
        return importVo ;
    }

    /**
     * 保存功能
     */
    $("#saveBtn").on('click', function () {
        if (editIndex || editIndex == 0) {
            $("#importDetail").datagrid('endEdit', editIndex);
        }
        if (dataValid()) {
            var importVo = getCommitData() ;
            $.postJSON("/api/exp-stock/imp", importVo, function (data) {
                $.messager.alert('系统提示', '入库成功', 'success');
                newDocument() ;
            }, function (data) {
                $.messager.alert("系统提示", data.errorMessage, 'error');
            })
        }
    });

    var newDocument = function () {
        //点击按钮调用的方法
        var subStorage = $("#subStorage").textbox('getValue');
        if(subStorage){
            var promise = createNewDocument(subStorage) ;
            promise.done(function(){
                $("#documentNo").textbox('setValue',documentNo);
            })
        }
        $("#importDetail").datagrid('loadData',[]) ;

    }

    /**
     * 新单
     */
    $("#newBtn").on('click',function(){
        newDocument() ;
    })
})