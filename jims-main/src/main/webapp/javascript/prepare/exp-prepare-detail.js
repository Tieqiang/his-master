/**
 * Created by wei on 2016/6/3.
 */
$(function () {
    var editIndex;
    var stopEdit = function () {
        if (editIndex || editIndex == 0) {
            $("#dg").datagrid('endEdit', editIndex);
            editIndex = undefined;
        }
    }
    $("#dg").datagrid({
        title: '备货完成明细记录',
        singleSelect: true,
        fit: true,
        toolbar: '#ft',
        footer: '#tb',
        fitColumns: true,
        columns: [
            [{
                title: 'id',
                field: 'id',
                hidden: true
            }, {
                title: '品名',
                field: 'expName',
                width: "20%"
            },{
                title: '规格',
                field: 'expSpec',
                width: "10%"
            },{
                title: '条形码',
                field: 'expBarCode',
                width: "20%"
            }, {
                title: '备货记录ID',
                field: 'masterId',
                hidden: true
            }, {
                title: '是否使用',
                field: 'useFlag',
                width: "10%",
                editor: {type: 'text', options: {
                    valueField:'value',
                    textField:'title',
                    data:[{
                        value:'0',
                        title:'未使用'
                    },{
                        title:'已使用',
                        value:'1'
                    }]
                }}
            }, {
                title: '使用日期',
                field: 'useDate',
                width: "20%"
            }, {
                title: '使用病人',
                field: 'usePatientId',
                width: "10%"
            }, {
                title: '使用科室',
                field: 'userDept',
                width: "5%"
            }, {
                title: '备货人员',
                field: 'operator',
                width: "5%"

            }
            ]
        ]
    });

    var suppliers = [];
    var promise = $.get("/api/exp-supplier-catalog/list-all", function (data) {
        suppliers = data;
        console.log(data);
    });
    promise.done(function () {
        $("#supplier").combogrid({
            idField: 'id',
            textField: 'supplierName',
            data: suppliers,
            panelWidth: 450,
            fitColumns: true,
            columns: [
                [
                    {
                        title: '供应商名称',
                        field: 'supplierName', width: 180, align: 'center'
                    },
                    {
                        title: '供应商代码',
                        field: 'supplierCode', width: 130, align: 'center'
                    },
                    {
                        title: '输入码',
                        field: 'inputCode', width: 50, align: 'center'
                    }
                ]
            ]
        })

    });



        $('#dept').combogrid({
            panelWidth: 500,
            idField: 'expCode',
            textField: 'expName',
            url: '/api/exp-prepare/find-input-code-detail',
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




    var list=[];
    $("#searchBtn").on('click', function () {
        var supplierId=$("#supplier").combogrid("getValue");
        var dept=$("#dept").combogrid("getValue");
        $.get("/api/exp-prepare/find-detail?supplierId=" + supplierId + "&dept=" + dept, function (data) {
            list=data;
              for(var i=0;i<list.length;i++){
                  if(list[i].useFlag==0){
                      list[i].useFlag='未使用'
                  }else{
                      list[i].useFlag='已使用'
                  }
              }

            console.log(list);
            $("#dg").datagrid('loadData', list);
        });

    })
});