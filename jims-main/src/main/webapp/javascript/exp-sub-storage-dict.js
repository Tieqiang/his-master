$(function () {
    var storages = [];
    var editIndex;

    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }

    $("#dg").datagrid({
        title: '子库房维护',
        fit: true,//让#dg数据创铺满父类容器
        footer: '#tb',
        url: '/api/exp-sub-storage-dict/list',
        method: 'GET',
        singleSelect: true,
        columns: [[{
            title: '库房代码',
            field: 'storageCode',
            width: "15%",
            editor:{type:'combogrid',
                options:{
                    idField:'storageCode',
                    textValue:'storageName',
                    method: 'GET',
                    url:'/api/exp-storage-dept/list?hospitalId='+parent.config.hospitalId ,
                    singleSelect:true,
                    mode: 'remote',
                    fit:true,
                    columns:[[{
                        field:'storageCode',title:'库房代码',width:88
                    },{
                        field:'storageName',title:'库房名称',width:88
                    }]],
                    onClickRow:function(index,row){
                        var ed = $("#dg").datagrid('getEditor',{index:editIndex,field:'storageCode'}) ;
                        $(ed.target).combogrid('setValue',row.storageCode) ;
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler,{
                        enter:function(e){

                            var row  = $(this).combogrid('grid').datagrid('getSelected') ;
                            if(row){
                                var ed = $("#dg").datagrid('getEditor',{index:editIndex,field:'storageCode'}) ;
                                $(ed.target).combogrid('setValue',row.storageCode) ;
                            }
                            $(this).combogrid('hidePanel') ;
                        }
                    })
                }
            }
        }, {
            title: '子库房名称',
            field: 'subStorage',
            width: "10%",
            editor:{type:'text',options:{required:true,validType:'length[0,10]',missingMessage:'请输入10个以内的汉字',invalidMessage:'输入值不在范围'}}
        }, {
            title: '入库单号前缀',
            field: 'importNoPrefix',
            width: "10%",
            editor:{type:'text',options:{required:true,validType:'length[0,6]',missingMessage:'请输入6字符以内的入库单前缀',invalidMessage:'输入值不在范围'}}
        }, {
            title: '当前入库号',
            field: 'importNoAva',
            width: "7%",
            editor:'numberbox'
        }, {
            title: '出库单号前缀',
            field: 'exportNoPrefix',
            width: "10%",
            editor:{type:'text',options:{required:true,validType:'length[0,6]',missingMessage:'请输入6字符以内的出库单号前缀',invalidMessage:'输入值不在范围'}}
        }, {
            title: '当前出库号',
            field: 'exportNoAva',
            width: "7%",
            editor:'numberbox'
        }, {
            title: '子库房类型',
            field: 'storageType',
            width: "10%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    valueField: 'name',
                    textField: 'name',
                    data: [{'code': '1', 'name': '全院产品'}, {'code': '2', 'name': '普通产品'}]
                }
            }
        }, {
            title: "医院编号",
            field: 'hospitalId',
            hidden: true
        }
        ]],
        onClickRow: function (index, row) {
            stopEdit();
            $(this).datagrid('beginEdit', index);
            editIndex = index;
        }
    });

    var loadDict = function () {
        storages.splice(0, storages.length);
        var promise = $.get("/api/exp-sub-storage-dict/list", function (data) {
            $("#dg").datagrid('loadData', data);
        });
        return promise;
    };


    /**
     * 添加
     */
    $("#addBtn").on('click', function () {

        stopEdit();
        $("#dg").datagrid('appendRow', {hospitalId: parent.config.hospitalId});
        var rows = $("#dg").datagrid('getRows');
        var addRowIndex = $("#dg").datagrid('getRowIndex', rows[rows.length - 1]);
        editIndex = addRowIndex;
        $("#dg").datagrid('selectRow', editIndex);
        $("#dg").datagrid('beginEdit', editIndex);
    });

    $("#editBtn").on('click', function () {
        stopEdit();
        var row = $("#dg").datagrid('getSelected');
        if (!row) {
            $.messager.alert("系统提示", "请选择要删除的行", "info");
            return;
        }
        var rowIndex = $("#dg").datagrid('getRowIndex', row);

        if (editIndex) {
            $("#dg").datagrid("endEdit", editIndex);
            editIndex = rowIndex;
            $("#dg").datagrid('beginEdit', rowIndex);
        } else {
            $("#dg").datagrid('beginEdit', rowIndex);
            editIndex = rowIndex;
        }
    })
    /**
     * 删除
     */
    $("#delBtn").on('click', function () {

        var row = $("#dg").datagrid('getSelected');
        if (row) {
            var rowIndex = $("#dg").datagrid('getRowIndex', row);

            $.get("/api/exp-stock/list?subStorage=" + row.subStorage, function (data) {
                if (data.length > 0 && row.id) {
                    $.messager.alert("提示", "该库房信息正在被使用，不允许删除！", "info");
                } else {
                    $("#dg").datagrid('deleteRow', rowIndex);
                    if (editIndex == rowIndex) {
                        editIndex = undefined;
                    }
                }
            })
        } else {
            $.messager.alert('系统提示', "请选择要删除的行", 'info');
        }


    });

    /**
     * 查询操作
     */
    $("#searchBtn").on('click', function () {
        var subStorage = $("#subStorage").textbox('getValue');

        $("#dg").datagrid({
            fit: true,//让#dg数据创铺满父类容器
            footer: '#tb',
            singleSelect: true,
            url: '/api/exp-sub-storage-dict/list?subStorage='+ subStorage,
            method: 'get'
        });
    });

    /**
     * 保存修改内容
     */
    $("#saveBtn").on('click', function () {

        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);

        }
        var inserted = $("#dg").datagrid('getChanges', 'inserted');
        var deleted = $("#dg").datagrid('getChanges', 'deleted');
        var updated = $("#dg").datagrid('getChanges', 'updated');

        var expSubStorageDictChangeVo = {};
        expSubStorageDictChangeVo.inserted = inserted;
        expSubStorageDictChangeVo.deleted = deleted;
        expSubStorageDictChangeVo.updated = updated;

        $.postJSON("/api/exp-sub-storage-dict/merge", expSubStorageDictChangeVo, function (data, status) {
            $.messager.alert("系统提示", "保存成功", "info");
            loadDict();
        }, function (data) {
            $.messager.alert("系统提示",data.responseJSON.errorMessage , "error");
        })
    });
});