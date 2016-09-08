/**
 * 消耗品：人员对应库房维护
 * Created by fengyuguang on 2016-08-22.
 */
$(function(){

    $('#dg').layout('panel', 'north').panel('resize', {height: 'auto'});
    $("#dg").layout({
        fit: true
    });
    $("#top").datagrid({
        toolbar: '#tb',
        border: false
    });

    var loadLeftDict = function(){
        var leftList = [];
        $.get('/api/staff-vs-storage/list-staff?hospitalId=' + parent.config.hospitalId, function(data){
            for(var i = 0; i < data.length; i++){
                leftList[i] = data[i];
                leftList[i].editFlag = false;
            }
            $('#staff').datagrid('loadData', leftList);
        });
    }
    loadLeftDict();

    $('#staff').datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        loadMsg: '数据正在加载，请稍后......',
        singleSelect: true,
        ctrlSelect: true,
        remoteSort: true,
        columns: [[{
            title: '编辑标志',
            field: 'editFlag',
            hidden: true
        },{
            title: '人员ID',
            field: 'staffId',
            align: 'center',
            width: '50%'
        },{
            title: '人员姓名',
            field: 'staffName',
            align: 'center',
            width: '30%',
            editor: {
                type: 'combogrid',
                options: {
                    idField: 'name',
                    textField: 'name',
                    mode: 'remote',
                    url: '/api/staff-vs-storage/list-staff-remove-exist-in-vs?hospitalId=' + parent.config.hospitalId,
                    singleSelect: true,
                    method: 'GET',
                    panelWidth: 200,
                    columns: [[{
                        title: 'ID',
                        field: 'id',
                        hidden: true
                    }, {
                        title: '姓名',
                        field: 'name',
                        align: 'center',
                        width: "40%"
                    }, {
                        title: '拼音码',
                        field: 'inputCode',
                        align: 'center',
                        width: "30%"
                    }]],
                    onClickRow: function(index,row){
                        var theRow = $('#staff').datagrid('getSelected');
                        var theRowIndex = $('#staff').datagrid('getRowIndex',theRow);
                        theRow.staffId = row.id;
                        $('#staff').datagrid('endEdit', theRowIndex);
                        $('#storage').datagrid('appendRow', {editFlag: true});
                        var rightRows = $("#storage").datagrid('getRows');
                        var addRightRowIndex = $("#storage").datagrid('getRowIndex', rightRows[rightRows.length - 1]);
                        $('#storage').datagrid('selectRow', addRightRowIndex);
                        $('#storage').datagrid('beginEdit', addRightRowIndex);
                        //光标定位库房名称单元格
                        $('#datagrid-row-r6-2-' + addRightRowIndex + ' .textbox-text').focus();
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            if(row && e.keyCode == 13){
                                var theRow = $('#staff').datagrid('getSelected');
                                var theRowIndex = $('#staff').datagrid('getRowIndex',theRow);
                                theRow.staffId = row.id;
                                $('#staff').datagrid('endEdit', theRowIndex);
                                $('#storage').datagrid('appendRow', {editFlag: true});
                                var rightRows = $("#storage").datagrid('getRows');
                                var addRightRowIndex = $("#storage").datagrid('getRowIndex', rightRows[rightRows.length - 1]);
                                $('#storage').datagrid('clearSelections');
                                $('#storage').datagrid('selectRow', addRightRowIndex);
                                $('#storage').datagrid('beginEdit', addRightRowIndex);
                                //光标定位库房名称单元格
                                $('#datagrid-row-r6-2-' + addRightRowIndex + ' .textbox-text').focus();
                            }
                        }
                    })
                }
            }
        }]],
        onLoadSuccess: function(data){
            $(this).datagrid('selectRow',0);
            var row = $('#staff').datagrid('getSelected');
            if(row != null && row != '' && typeof(row) != 'undefined'){
                var rightList = [];
                $.get('/api/staff-vs-storage/list?name=' + row.staffName, function (data) {
                    if(data && data.length > 0){
                        for(var i = 0; i < data.length; i++){
                            rightList[i] = data[i];
                            rightList[i].editFlag = false;
                        }
                    }
                    $('#storage').datagrid('loadData', rightList);
                });
            }
        },
        onClickRow: function(index,row){
            $(this).datagrid('selectRow', index);
            var rightRows = $('#storage').datagrid('getRows');
            if(rightRows && rightRows.length > 0 && rightRows != []){
                $.each(rightRows,function(index,item){
                    var itemIndex = $('#storage').datagrid('getRowIndex',item);
                    $('#storage').datagrid('endEdit',itemIndex);
                    $('#storage').datagrid('unselectAll');
                    if(item.storageId == null || item.storageId == '' || typeof(item.storageId) == 'undefined'){
                        var nullItemIndex = $('#storage').datagrid('getRowIndex',item);
                        $('#storage').datagrid('deleteRow',nullItemIndex);
                    }
                });
            }
            if(row.editFlag){
                $(this).datagrid('beginEdit',index);
            }else{
                var changesList = $('#storage').datagrid('getChanges');
                if(changesList && changesList.length > 0){
                    $.messager.confirm('确认消息','您要保存刚才的操作吗?',function(y){
                        if(y){
                            save();
                        }else{
                            loadLeftDict();
                        }
                    });
                }else{
                    $('#staff').datagrid('selectRow', index);
                    var rightList = [];
                    $.get('/api/staff-vs-storage/list?name=' + row.staffName, function (data) {
                        if (data && data.length > 0) {
                            for (var i = 0; i < data.length; i++) {
                                rightList[i] = data[i];
                                rightList[i].editFlag = false;
                            }
                        }
                        $('#storage').datagrid('loadData', rightList);
                    });
                }
            }
        }
    });

    $('#storage').datagrid({
        fit: true,
        striped: true,
        fitColumns: true,
        remoteSort: true,
        method: 'get',
        columns: [[{
            title: '编辑标志',
            field: 'editFlag',
            hidden: true
        },{
            title: 'ID',
            field: 'id',
            hidden: true
        },{
            title: '库房ID',
            field: 'storageId',
            align: 'center',
            width: '50%'
        }, {
            title: '库房名称',
            field: 'storageName',
            align: 'center',
            width: '30%',
            editor: {
                type: 'combogrid',
                options: {
                    idField: 'storageName',
                    textField: 'storageName',
                    mode: 'remote',
                    url: '/api/exp-storage-dept/list?hospitalId=' + parent.config.hospitalId,
                    singleSelect: true,
                    method: 'GET',
                    panelWidth: 270,
                    columns: [[{
                        title: '代码',
                        field: 'storageCode',
                        align: 'center',
                        width: '30%'
                    }, {
                        title: '名称',
                        field: 'storageName',
                        align: 'center',
                        width: '40%'
                    }, {
                        title: '拼音码',
                        field: 'disburseNoPrefix',
                        align: 'center',
                        width: '30%'
                    }]],
                    onClickRow: function (index, row) {
                        var rightRows = $('#storage').datagrid('getRows');
                        for(var i = 0; i < rightRows.length - 1;i++){
                            if(row.id == rightRows[i].storageId){
                                $.messager.alert('系统提示', '该用户已经存在此库房,请重新选择!', 'info');
                                var thenRightRow = $('#storage').datagrid('getSelected');
                                var thenRightRowIndex = $('#storage').datagrid('getRowIndex', thenRightRow);
                                $('#storage').datagrid('deleteRow', thenRightRowIndex);
                                $('#storage').datagrid('appendRow', {editFlag: true});
                                var newRightRows = $('#storage').datagrid('getRows');
                                var appendRowIndex = $('#storage').datagrid('getRowIndex', newRightRows[newRightRows.length - 1]);
                                $('#storage').datagrid('selectRow', appendRowIndex);
                                $('#storage').datagrid('beginEdit', appendRowIndex);
                                //光标定位到追加行的库房单元格
                                $('#datagrid-row-r6-2-' + appendRowIndex + ' .textbox-text').focus();
                                return;
                            }else{
                                var theRowIndex = $('#storage').datagrid('getRowIndex', rightRows[rightRows.length - 1]);
                                $('#storage').datagrid('unselectAll');
                                $('#storage').datagrid('selectRow', theRowIndex);
                                var theRow = $('#storage').datagrid('getSelected');
                                theRow.storageId = row.id;
                                $('#storage').datagrid('endEdit', theRowIndex);
                            }
                        }
                    },
                    keyHandler: $.extend({}, $.fn.combogrid.defaults.keyHandler, {
                        enter: function (e) {
                            var row = $(this).combogrid('grid').datagrid('getSelected');
                            if (row && e.keyCode == 13) {
                                var rightRows = $('#storage').datagrid('getRows');
                                for(var i = 0; i < rightRows.length - 1; i++){
                                    if(row.id == rightRows[i].storageId){
                                        $.messager.alert('系统提示', '该用户已经存在此库房,请重新选择!', 'info');
                                        var thenRightRow = $('#storage').datagrid('getSelected');
                                        var thenRightRowIndex = $('#storage').datagrid('getRowIndex', thenRightRow);
                                        $('#storage').datagrid('deleteRow', thenRightRowIndex);
                                        $('#storage').datagrid('appendRow', {editFlag: true});
                                        var newRightRows = $('#storage').datagrid('getRows');
                                        var appendRowIndex = $('#storage').datagrid('getRowIndex', newRightRows[newRightRows.length - 1]);
                                        $('#storage').datagrid('selectRow', appendRowIndex);
                                        $('#storage').datagrid('beginEdit', appendRowIndex);
                                        //光标定位到追加行的库房单元格
                                        $('#datagrid-row-r6-2-' + appendRowIndex + ' .textbox-text').focus();
                                        return;
                                    }
                                }

                                var addRightRowIndex = $('#storage').datagrid('getRowIndex', rightRows[rightRows.length - 1]);
                                $('#storage').datagrid('unselectAll');
                                $('#storage').datagrid('selectRow', addRightRowIndex);
                                var theRow = $('#storage').datagrid('getSelected');
                                theRow.storageId = row.id;
                                var theRowIndex = $('#storage').datagrid('getRowIndex', theRow);
                                $('#storage').datagrid('endEdit', theRowIndex);

                                $('#storage').datagrid('appendRow', {editFlag: true});
                                $('#storage').datagrid('unselectAll');
                                var newRightRows = $('#storage').datagrid('getRows');
                                $('#storage').datagrid('selectRow', newRightRows.length-1);
                                $('#storage').datagrid('beginEdit', newRightRows.length-1);
                                //光标定位库房名称单元格
                                $('#datagrid-row-r6-2-' + (newRightRows.length-1) + ' .textbox-text').focus();
                            }
                        }
                    })
                }
            }
        }]],
        onClickRow: function(index,row){
            var leftRow = $('#staff').datagrid('getSelected');
            var leftRowIndex = $('#staff').datagrid('getRowIndex',leftRow);
            $('#staff').datagrid('endEdit',leftRowIndex);
            var rightRows = $('#storage').datagrid('getRows');
            if(rightRows && rightRows.length > 0){
                for(var i = 0; i < rightRows.length; i++){
                    var rightRowIndex = $('#storage').datagrid('getRowIndex', rightRows[i]);
                    if(!rightRows[i].storageId || rightRows[i].storageId == null || rightRows[i].storageId == '' || typeof(rightRows[i].storageId) == 'undefined'){
                        $('#storage').datagrid('deleteRow',rightRowIndex);
                    }else{
                        $('#storage').datagrid('endEdit', rightRowIndex);
                    }
                }
            }
            if(row.editFlag){
                $('#storage').datagrid('unselectAll');
                $('#storage').datagrid('selectRow',index);
                $(this).datagrid('beginEdit',index);
            }
        }
    });

    $('#addBtn').on('click',function(){
        var changesList = $('#storage').datagrid('getChanges');
        if (changesList && changesList.length > 0) {
            $.each(changesList, function (index, item) {
                var itemIndex = $('#storage').datagrid('getRowIndex', item);
                $('#storage').datagrid('endEdit', itemIndex);
                $('#storage').datagrid('unselectAll');
                if (item.storageId == null || item.storageId == '' || typeof(item.storageId) == 'undefined') {
                    var nullItemIndex = $('#storage').datagrid('getRowIndex', item);
                    $('#storage').datagrid('deleteRow', nullItemIndex);
                }
            });
            $.messager.confirm('确认消息', '您要保存刚才的操作吗?', function (y) {
                if (y) {
                    save();
                    add();
                }
            });
        }else{
            add();
        }
    });
    //添加
    function add(){
        var theRow = $('#staff').datagrid('getSelected');
        if (theRow && theRow != null && theRow != {} && typeof(theRow) != 'undefined') {
            var theRowIndex = $('#staff').datagrid('getRowIndex');
            $('#staff').datagrid('endEdit', theRowIndex);
        }
        $("#staff").datagrid('appendRow', {editFlag: true});
        $('#storage').datagrid('loadData', []);
        var leftRows = $("#staff").datagrid('getRows');
        var addLeftRowIndex = $("#staff").datagrid('getRowIndex', leftRows[leftRows.length - 1]);
        $("#staff").datagrid('selectRow', addLeftRowIndex);
        $("#staff").datagrid('beginEdit', addLeftRowIndex);
        //光标定位到人员姓名单元格
        $('#datagrid-row-r5-2-' + addLeftRowIndex + ' .textbox-text').focus();
    }

    $("#delBtn").on("click", function () {
        var rightRows = $("#storage").datagrid('getSelections');
        if (rightRows && rightRows.length > 0) {

            $.messager.confirm('确认消息', '您确认要删除吗?', function (y) {
                if (y) {
                    $.each(rightRows, function (index, item) {
                        var delRowIndex = $('#storage').datagrid('getRowIndex',item);
                        $('#storage').datagrid('deleteRow',delRowIndex);
                    });
                }
            });
        } else {
            $.messager.alert('系统提示', "请选择右侧列表要删除的行!", 'info');
            return ;
        }
    });

    $("#saveBtn").on("click", function(){
        save();
    });
    //追加
    $('#addStorage').on('click',function(){
        var rightRows = $('#storage').datagrid('getRows');
        if(rightRows != null && rightRows != [] && rightRows.length > 0){
            $.each(rightRows,function(index,item){
                var rightRowsIndex = $('#storage').datagrid('getRowIndex',item);
                $('#storage').datagrid('endEdit',rightRowsIndex);
                $('#storage').datagrid('unselectAll');
            });
        }
        $('#storage').datagrid('appendRow',{editFlag: true});
        var newRightRows = $('#storage').datagrid('getRows');
        var appendRowIndex =  $('#storage').datagrid('getRowIndex',newRightRows[newRightRows.length-1]);
        $('#storage').datagrid('selectRow',appendRowIndex);
        $('#storage').datagrid('beginEdit',appendRowIndex);
        //光标定位到追加行的库房单元格
        $('#datagrid-row-r6-2-' + appendRowIndex + ' .textbox-text').focus();
    });

    function save(){
        var leftRow = $('#staff').datagrid('getSelected');
        var leftRowIndex = $('#staff').datagrid('getRowIndex',leftRow);
        $('#staff').datagrid('endEdit',leftRowIndex);
        var rightRow = $('#storage').datagrid('getSelected');
        var rightRowIndex = $('#storage').datagrid('getRowIndex', rightRow);
        $('#storage').datagrid('endEdit', rightRowIndex);

        var insertedData = $('#storage').datagrid('getChanges','inserted');
        var deletedData = $('#storage').datagrid('getChanges','deleted');
        var beanChangeVo = {};
        var insertList = [];
        var deleteList = [];
        if(insertedData && insertedData.length > 0){
            $.each(insertedData, function(index,item){
                if (item.storageId == null || item.storageId == '' || typeof(item.storageId) == 'undefined') {
                    var nullItemIndex = $('#storage').datagrid('getRowIndex', item);
                    $('#storage').datagrid('deleteRow', nullItemIndex);
                }else{
                    var staffVsStorage = {};
                    staffVsStorage.staffId = leftRow.staffId;
                    staffVsStorage.storageId = item.storageId;
                    insertList.push(staffVsStorage);
                }
            });
            if(insertList != null && insertList != [] && insertList.length == insertedData.length){
                beanChangeVo.inserted = insertList;
            }
        }
        if(deletedData && deletedData.length > 0){
            $.each(deletedData, function (index, item) {
                var staffVsStorage = {};
                staffVsStorage.id = item.id;
                staffVsStorage.staffId = item.staffId;
                staffVsStorage.storageId = item.storageId;
                deleteList.push(staffVsStorage);
            });
            if(deleteList != null && deleteList != [] && deleteList.length == deletedData.length){
                beanChangeVo.deleted = deleteList;
            }
        }
        if(beanChangeVo && beanChangeVo != null && beanChangeVo != {} && typeof(beanChangeVo) != 'undefined'){
            //保存
            $.postJSON('/api/staff-vs-storage/merge',beanChangeVo,function(data,status){
                $.messager.alert('系统提示','保存成功','info');
                loadLeftDict();
            },function(data){
                $.messager.alert('提示', data.responseJSON.errorMessage, "error");
            });
        }
    }

    $('#searchBtn').on('click',function(){
        var name = $('#staffName').textbox('getValue');
        $.get("/api/staff-vs-storage/list-staff?name=" + name + '&hospitalId=' + parent.config.hospitalId, function (data) {
            if(data != null && data.length > 0 && data != []){
                $('#staff').datagrid('loadData',data);
                $('#staff').datagrid('selectRow', 0);
                var row = $('#staff').datagrid('getSelected');
                if (row != null && row != '' && typeof(row) != 'undefined') {
                    $('#storage').datagrid('load', '/api/staff-vs-storage/list?name=' + row.staffName);
                    $('#storage').datagrid('selectRow', 0);
                }
            }else{
                $.messager.alert('系统提示','没有找到相应的数据,请确认输入是否正确!','info');
                $('#staff').datagrid('loadData',[]);
                $.get('/api/staff-vs-storage/list-staff?hospitalId=' + parent.config.hospitalId,function(data){
                    $('#staff').datagrid('loadData',data);
                    $('#staff').datagrid('selectRow', 0);
                    var row = $('#staff').datagrid('getSelected');
                    if (row != null && row != '' && typeof(row) != 'undefined') {
                        $('#storage').datagrid('load', '/api/staff-vs-storage/list?name=' + row.staffName);
                        $('#storage').datagrid('selectRow', 0);
                    }
                });
            }
        });
    });
});
