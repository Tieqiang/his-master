/**
 * Created by wangbinbin on 2015/10/21.
 */
/***
 * 产品供应维护
 */
function checkRadio(){
    if($("#expName:checked").val()){
        $("#searchInput").textbox("enable");
    }else{
        $("#searchInput").textbox({disabled:true});
        $("#searchInput").textbox("clear");
    }
}
$(function () {
    var editRowIndex;
    $("#dg").datagrid({
        title: '产品供应维护',
        fit: true,//让#dg数据创铺满父类容器
        toolbar: '#ft',
        footer: '#tb',
        rownumbers: true,
        singleSelect: true,
        columns: [[{
            title: '代码',
            field: 'expCode',
            align: 'center',
            width: "15%"
        }, {
            title: '产品名称',
            field: 'expName',
            align: 'center',
            width: "17%"
        }, {
            title: '包装规格',
            field: 'packageSpec',
            align: 'center',
            width: "10%"
        }, {
            title: '单位',
            field: 'packageUnits',
            align: 'center',
            width: "10%"
        }, {
            title: '厂家',
            field: 'firmId',
            align: 'center',
            width: "16%"
        }, {
            title: '批号',
            field: 'batchNo',
            align: 'center',
            width: "10%"
        }, {
            title: '数量',
            field: 'quantity',
            align: 'center',
            width: "10%"
        }, {
            title: '供应标志',
            field: 'supplyIndicator',
            align: 'center',
            width: "10%",
            editor: {
                type: 'combobox',
                options: {
                    panelHeight: 'auto',
                    valueField: 'code',
                    textField: 'name',
                    editable: false,
                    data: [{'code': '1', 'name': '可供应'}, {'code': '0', 'name': '不可供'}]
                }
            },
            formatter:function(value,row,index){
                if(value=="1"){
                    return value="可供应";
                }else if(value=="0"){
                    return value="不可供";
                }else{
                    return value;
                }
            }
        }
        ]],onClickRow: function (rowIndex,rowData) {
            if (editRowIndex || editRowIndex == 0){
                $(this).datagrid("endEdit",editRowIndex);
            }
            $(this).datagrid("beginEdit",rowIndex);
            var upperLevel = $("#dg").datagrid("getEditor",{index:rowIndex,field:"supplyIndicator"});
            editRowIndex = rowIndex;
        }
    });
    $('#searchInput').combogrid({
        panelWidth: 500,
        idField: 'expCode',
        textField: 'expName',
        url: '/api/exp-name-dict/list-exp-name-by-input',
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
    var stocks=[];
    var endStocks=[];
    //查询
    $("#searchBtn").on('click', function () {
         loadDict();
        endStocks.splice(0,endStocks.length);
    })
    //载入数据
    var loadDict = function () {
        $.get("/api/exp-supply-manage/manage?storageCode=" + parent.config.storageCode+"&hospitalId="+parent.config.hospitalId , function (data) {
            //+ "&expCode=" + expCode + "&supplyIndicator=" + radio
            stocks=data;
            var expCode =  $("#searchInput").combogrid('getValue');
            var flag0= 0;
            var flag1= 1;
            var flag2= 2;
            var radio = $("#ft input[name='radioOne']:checked").val();

            if(radio!=1||radio!=2){
                supplyIndicators='';
            }else{
                supplyIndicators=radio;
            }
            expStocks=expCode;

            if(expCode!=null){
                for(var i = 0 ;i<stocks.length;i++){
                    if(expCode==stocks[i].expCode){
                        console.log(expCode==stocks[i].expCode);
                        if(radio == flag2){
                            endStocks.push(stocks[i]);
                        }
                        else if(stocks[i].supplyIndicator == flag1 && radio == flag1 ){
                            endStocks.push(stocks[i]);
                        }
                        else if((stocks[i].supplyIndicator == flag0 && radio == flag0)||  (stocks[i].supplyIndicator==null && radio == flag0) ){
                            endStocks.push(stocks[i]);
                        }
                    }
                }
            }
            if(expCode==''){
                for(var i = 0 ;i<stocks.length;i++){
                    if(radio == flag2){
                        endStocks.push(stocks[i]);
                    }
                    else if(stocks[i].supplyIndicator == flag1 && radio == flag1 ){
                        endStocks.push(stocks[i]);
                    }
                    else if(stocks[i].supplyIndicator == flag0 && radio == flag0 || (stocks[i].supplyIndicator == null && radio == flag0)){
                        endStocks.push(stocks[i]);
                    }
                }
            }

            if(endStocks.length<=0){
                $.messager.alert('系统提示', '暂无数据', "info");
                $("#dg").datagrid('loadData', []);
                return;
            }
            $("#dg").datagrid('loadData', endStocks);
        })

    }
    //loadDict();
    //保存
    $("#saveBtn").on('click', function () {
        if (editRowIndex!=undefined) {
            $("#dg").datagrid('endEdit', editRowIndex);
            editRowIndex = undefined;
        }
        var updateData = $("#dg").datagrid('getChanges', 'updated');
        console.log(updateData);
        if (updateData && updateData.length > 0) {
            $.postJSON("/api/exp-supply-manage/save", updateData, function (data) {
                $.messager.alert('系统提示', '保存成功', "success",function(){
                    loadDict();
                });
                endStocks.splice(0,endStocks.length);
            }, function (data) {
                $.messager.alert("系统提示", '保存失败', "error");
            });
        }
    });

    //为报表准备字段
    var supplyIndicators='';
    var expStocks='';

    //打印
    $("#printDiv").dialog({
        title: '打印预览',
        width: 1000,
        height: 520,
        catch: false,
        modal: true,
        closed: true,
        onOpen: function () {
            var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/"+"exp-supply-manage.cpt"+"&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&expStock=" + expStocks + "&supplyIndicator=" + supplyIndicators;
            $("#report").prop("src",cjkEncode(https));
        }
    });
    $("#printBtn").on('click', function () {
        var printData = $("#dg").datagrid('getRows');
        if (printData.length <= 0) {
            $.messager.alert('系统提示', '请先查询数据', 'info');
            return;
        }
        $("#printDiv").dialog('open');

    });
})

