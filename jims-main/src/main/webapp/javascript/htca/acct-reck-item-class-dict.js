/**
 * Created by heren on 2015/11/18.
 * 核算科目维护
 */

$(function () {

    var incomeTypeDicts=[] ;

    var costItemDict =[]  ;

    $.get("/api/cost-item/list-item?hospitalId=" + parent.config.hospitalId,function(data){
        costItemDict = data ;
    })
    $.ajax("/api/income-type/list?hospitalId="+parent.config.hospitalId,{
        async:true,
        method:'GET',
        success:function(data){
            incomeTypeDicts = data ;
            console.log("innert:"+incomeTypeDicts) ;
        }
    }) ;
    var editRow = undefined;

    console.log("outer:"+incomeTypeDicts) ;

    $("#reckClassDatagrid").datagrid({
        fit: true,
        fitColumns: true,
        striped: true,
        singleSelect: true,
        url: '/api/acct-reck/list?hospitalId=' + parent.config.hospitalId,
        toolbar: '#ft',
        method: 'GET',
        pagination: true,
        pageSize: 30,
        rownumbers: true,
        loadMsg:'数据正在加载，请稍后......',
        columns: [[{
            title: 'id',
            field: 'id',
            hidden: true
        }, {
            title: '核算科目名称',
            field: 'reckItemName'
        }, {
            title: '核算科目代码',
            field: 'reckItemCode'
        }, {
            title: "收入项目拼音码",
            field: 'inputCode',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '门诊开单比率',
            field: 'outpOrderedBy',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '门诊执行比率',
            field: 'outpPerformedBy',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '门诊护士比率',
            field: 'outpWardCode',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '住院开单比率',
            field: 'inpOrderedBy',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '住院执行比率',
            field: 'inpPerformedBy',
            editor: {type: 'textbox', options: {}}
        }, {
            title: '住院护士比率',
            field: 'inpWardCode',
            editor: {type: 'textbox', options: {}}
        }, {
            title: "收入类别",
            field: 'incomeType',
            editor: {
                type: 'combobox', options: {
                    valueField: 'incomeTypeCode',
                    textField: 'incomeTypeName',
                    method: 'GET',
                    url: '/api/income-type/list?hospitalId=' + parent.config.hospitalId
                }
            },
            formatter:function(value,row,index){
                for(var i =0 ;i<incomeTypeDicts.length ;i++){
                    if(value==incomeTypeDicts[i].incomeTypeCode){
                        return incomeTypeDicts[i].incomeTypeName ;
                    }
                }
            }
        }, {
            title: "核算类型",
            field: 'reckType',
            editor: {
                type: 'combobox', options: {
                    valueField: 'id',
                    textField: 'name',
                    data: [{
                        id: "1",
                        name: '参与核算'
                    }, {
                        id: "0",
                        name: '不参与核算'
                    }]
                }
            },
            formatter: function (value, row, index) {
                if (value == '1') {
                    return '参与核算'
                } else if (value == '0') {
                    return '不参与核算'
                } else {
                    return value;
                }
            }
        }, {
            title: '固定折算',
            field: 'fixConvert',
            editor: {
                type: 'validatebox', options: {
                    required: true,
                    missingMessage: "请输入对应的固定折算值",
                    invalidMessage: "无效的输入，请重新输入"
                }
            }
        }, {
            title: '成本对照',
            field: 'costId',
            formatter:function(value,row,index){
                console.log(costItemDict) ;
                for(var i = 0 ;i<costItemDict.length ;i++){
                    if(value ==costItemDict[i].id){
                        return costItemDict[i].costItemName ;
                    }
                }
                return value ;
            }
        }]],
        onDblClickRow: function (rowIndex, rowData) {
            if (editRow||editRow==0 ) {
                var ed = $("#reckClassDatagrid").datagrid('getEditor', {index: editRow, field: "fixConvert"});
                if(ed.target){
                    var flag = $(ed.target).validatebox('isValid');
                    if (flag) {
                        $("#reckClassDatagrid").datagrid('endEdit', editRow);
                    } else {
                        $.messager.alert("系统提示", "固定折算必须填写", 'error');
                        return;
                    }
                }
            }
            editRow = rowIndex;
            $("#reckClassDatagrid").datagrid('beginEdit', editRow);
        }
    });


    //提取HIS中的新增的核算科目
    $("#getHisNewItemBtn").on('click', function () {
        var options = $("#reckClassDatagrid").datagrid('options');
        options.url = "/api/acct-reck/list-his?hospitalId=" + parent.config.hospitalId;
        $("#reckClassDatagrid").datagrid('reload');
    });

    //提取核算项目
    $("#fetchItemBtn").on('click', function () {
        var options = $("#reckClassDatagrid").datagrid('options');
        options.url = '/api/acct-reck/list?hospitalId=' + parent.config.hospitalId,
            $("#reckClassDatagrid").datagrid('reload');
    })
    //保存核算项目修改
    $("#saveReckItemClassBtn").on('click', function () {
        if (editRow || editRow == 0) {
            $("#reckClassDatagrid").datagrid('endEdit', editRow);
        }
        var rows = $("#reckClassDatagrid").datagrid('getRows');
        $.postJSON("/api/acct-reck/save-reck", rows, function (data) {
            $.messager.alert('系统提示', '保存成功', 'info');
            $("#fetchItemBtn").trigger('click');
            editRow=undefined ;
        }, function (data) {
            $.messager.alert('系统提示', '保存失败', 'error');
            editRow=undefined ;
        })

    });

    //删除核算项目
    $("#removeReckItemClassBtn").on('click', function () {
        var row = $("#reckClassDatagrid").datagrid('getSelected');
        if (!row) {
            $.messager.alert('系统提示', '请选择要删除的行', 'error');
            return;
        }

        var rowIndex = $("#reckClassDatagrid").datagrid('getRowIndex', row);
        if (!row.id) {
            $.messager.confirm('系统提示','确定要进行删除操作吗',function(r){
                if(r){
                    $("#reckClassDatagrid").datagrid('deleteRow', rowIndex);
                }
            });

        } else {
            $.post("/api/acct-reck/del/" + row.id, function (data) {
                        $.messager.alert('系统提示', '删除成功', 'info');
                        $("#fetchItemBtn").trigger('click');
                    })
        }
    })
});
