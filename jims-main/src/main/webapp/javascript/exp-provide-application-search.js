/**
 * Created by tangxinbo on 2015/10/15.
 */
/**
 * 查阅本库房所提的申请
 */
$(document).ready(function () {
    var editRowIndex;
    var dates;
    var storageDict = [];//库房字典

    //库房加载
    var storageDictPromise = $.get("/api/exp-storage-dept/list?hospitalId="+parent.config.hospitalId, function (data) {
        $.each(data, function (index, item) {
            var storage = {};
            storage.storageCode = item.storageCode;
            storage.storageName = item.storageName;
            storageDict.push(storage);
        })
    });
    //格式化日期(方法)
    var formatterDate = function (date) {
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d);
    };
    var formatterDateBeforeThreeDay = function (date) {
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate() - 3;
        return y + "-" + (m < 10 ? ("0" + m) : m) + "-" + (d < 10 ? ("0" + d) : d);
    };

    //设置默认值
    $("#startTime").datebox("setValue", formatterDateBeforeThreeDay(new Date()));//提前3天
    $("#endTime").datebox("setValue", formatterDate(new Date()));


    $("#dg").datagrid({
        title: "查阅本库房所提的申请",
        fit: true,
        toolbar: "#topBar",
        footer: "#tb",
        singleSelect: true,
        rownumbers: true,
        columns: [[{
            title: "申请单号",
            field: "applicantNo",
            width: "10%"
        }, {
            title: "向哪个库房申请",
            field: "provideName",
            width: "10%"
        }, {
            title: "代码",
            field: "expCode",
            width: "10%"
        }, {
            title: "序号",
            field: "itemNo",
            width: "10%"
        }, {
            title: "产名",
            field: "expName",
            width: "10%"
        }, {
            title: "规格",
            field: "packageSpec",
            width: "10%"
        }, {
            title: "单位",
            field: "packageUnits",
            width: "10%"
        }, {
            title: "数量",
            field: "quantity",
            width: "10%"
        }, {
            title: "申请人",
            field: "applicationMan",
            width: "10%"
        }]]
    });
    //查询
    $("#searchBtn").on("click", function () {
        var startTimeStr = $("#startTime").datebox("getValue").split("-");
        var startTime = startTimeStr[0]+"/"+startTimeStr[1]+"/"+startTimeStr[2];
        //alert(startTime);
        var endTimeStr = $("#endTime").datebox("getValue").split("-");
        var endTime = endTimeStr[0]+"/"+endTimeStr[1]+"/"+endTimeStr[2];

        var isSure = $("#topBar input[name='radioOne']:checked").val();
        var applicationStorage = parent.config.storageCode;
        //alert("startTime:"+startTime+"  endTime:"+endTime+"  isSure:"+isSure);

        var expProvideApplicationObjs = [];


        $.get("/api/exp-provide-application/find-cur-storage-application" +
            "?startTime=" + startTime + "&endTime=" + endTime + "&isSure=" + isSure + "&applicationStorage=" + applicationStorage, function (data) {
                if (data.length ==  0){
                    $.messager.alert("提示","该申请内无数据","info");
                    $("#dg").datagrid("loadData", {rows: []});
                    return ;
                } else {
                    $("#dg").datagrid("loadData", data);
                }
            }
        );
    });
    //清屏
    $("#clearBtn").on("click", function () {
        $("#dg").datagrid("loadData", {rows: []});
        editRowIndex = undefined;
    })
});