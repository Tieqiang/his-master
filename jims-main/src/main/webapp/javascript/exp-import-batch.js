/**
 * 消耗品批量入库功能
 * Created by heren on 2015/10/19.
 */
$(function () {

    var depts=[] ;
    var rows=[] ;//选中记录
    var deptPromse = $.get("/api/dept-dict/list?hospitalId="+parent.config.hospitalId,function(data){
        for(var i = 0 ;i<data.length ;i++){
            var dept={} ;
            dept.supplierName = data[i].deptName ;
            dept.supplierCode = data[i].deptCode ;
            dept.inputCode = data[i].inputCode ;
            depts.push(dept) ;
        }
    }) ;
    deptPromse.done(function(){
        $("#supplier").combogrid('grid').datagrid('loadData',depts) ;
        console.log(depts) ;
    }) ;


    //提取按钮
    $("#fetchBtn").on('click',function(){
        $("#expExportDialog").dialog('open') ;
    }) ;

    /**
     * 出库记录弹出框
     */
    $("#expExportDialog").dialog({
        title: '出库记录',
        width: 900,
        height: 300,
        closed: false,
        catch: false,
        modal: true,
        closed: true,
        left:30,
        top:30,

        onBeforeOpen: function () {
            var storageCode ;
            var supplierName = $("#supplier").combogrid('getValue') ;
            if(!supplierName){
                $.messager.alert("系统提示","请选择供货单位",'error');
                return  false;
            }else{
                for(var i = 0 ;i<depts.length;i++){
                    if(depts[i].supplierName==supplierName){
                        storageCode = depts[i].supplierCode ;
                    }
                }
            }

            if(!storageCode){
                $.messager.alert("系统提示","获取供货单位代码失败！","error");
                return false;
            }
            $("#expExportDatagrid").datagrid('load', {storageCode:storageCode,stockName:parent.config.storage,expClass:parent.config.exportClass,hospitalId:parent.config.hospitalId});
            $("#expExportDatagrid").datagrid('selectRow', 0)
        }
    });

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
    $("#expExportDatagrid").datagrid({
        fit: true,
        fitColumns: true,
        singleSelect: false,
        showFooter:true,
        url:'/api/exp-export/exp-export-document-detail',
        mode:'remote',
        method:'GET',
        footer:'#expExportDiaglogFt',
        ctrlSelect:true,
        columns: [[{
            title: '项目代码',
            field: 'expCode'
        }, {
            title: '品名',
            field: 'expName',
            width: '10%'
        }, {
            title: '规格',
            field: 'packageSpec'
        }, {
            title: '单位',
            field: 'packageUnits'
        }, {
            title: "数量",
            field: 'quantity'
        }, {
            title: '批号',
            field: 'batchNo'
        }, {
            title: '进价',
            field: 'purchasePrice'
        }, {
            title: '金额',
            field: 'amount'
        }, {
            title: '产品类型',
            field: 'expForm'
        }, {
            title: '有效日期',
            field: 'expireDate'
        }, {
            title: '厂家',
            field: 'firmId'
        }, {
            title: '注册证号',
            field: 'expImportDetailRegistNo'
        }, {
            title: '许可证号',
            field: 'expImportDetailLicenceno'
        }, {
            title: '发票号',
            field: 'invoiceNo'
        }, {
            title: '发票日期',
            field: 'invoiceDate'
        }, {
            title: '备注',
            field: 'memo'
        }, {
            title: '生产日期',
            field: 'produceDate'

        }, {
            title: '消毒日期',
            field: 'disinfectDate'
        }, {
            title: '灭菌标志',
            field: 'killFlag'
        }, {
            title: '折扣',
            field: 'discount'
        }, {

            title: '招标文号',
            field: 'orderBatch'

        }, {
            title: '招标文号序号',
            field: 'tenderNo'
        }, {
            title: '现有数量',
            field: 'inventory'
        },{
            title:'零售价',
            field:'retailPrice',
            hidden:true
        },{
            title:'进货价',
            field:'tradePrice',
            hidden:true
        },{
            title:'最小规格',
            field:'expSpec',
            hidden:true
        },{
            title:'最小单位',
            field:'units',
            hidden:true
        }]]
    }) ;

    /***
     * 全选按钮
     */
    $("#selectAll").on('click',function(){
        var selectText = $("#selectAll").linkbutton('options').text;
        if(selectText=='全选'){
            $("#expExportDatagrid").datagrid('selectAll') ;
            $("#selectAll").linkbutton('options').text="全不选" ;
        }else{
            $("#expExportDatagrid").datagrid('unselectAll') ;
            $("#selectAll").linkbutton('options').text="全选" ;
        }
    }) ;

    /**
     * 确定按钮
     */
    $("#sureBtn").on('click',function(){
        var datas = [] ;
        rows=[] ;
        rows = $("#expExportDatagrid").datagrid('getSelections') ;
        var totals =0;
        for(var i = 0 ;i<rows.length ;i++){
            rows[i].amount = rows[i].quantity * rows[i].purchasePrice ;
            totals += rows[i].amount ;
            datas.push(rows[i]) ;
        }
        if(totals){
            $("#accountReceivable").numberbox('setValue',totals);
        }
        $("#importDetail").datagrid('loadData',datas) ;
        $("#expExportDialog").dialog('close') ;
    })

    $("#exitBtn").on('click',function(){
        $("#expExportDialog").dialog('close') ;
    })


    $("#saveBatchBtn").on('click',function(){
        var expImpVo = getCommitData() ;
        expImpVo.expExportDetialVoBeanChangeVo={} ;
        expImpVo.expExportDetialVoBeanChangeVo.updated=rows ;

        console.log(expImpVo) ;

        $.postJSON("/api/exp-stock/imp-batch", expImpVo, function (data) {
            $.messager.alert('系统提示', '入库成功', 'success');
            newDocument() ;
        }, function (data) {
            console.log(data) ;
            $.messager.alert("系统提示", data.responseJSON.errorMessage, 'error');
        })

    })



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
        importMaster.memos = $('#memos').textbox('getValue');
        importMaster.operator = parent.config.loginId;
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

})