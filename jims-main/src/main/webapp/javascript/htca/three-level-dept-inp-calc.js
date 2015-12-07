/**
 * Created by heren on 2015/11/16.
 */
/**
 * Created by heren on 2015/10/29.
 */
function cjkEncode(text) {
    if (text == null) {
        return "";
    }
    var newText = "";
    for (var i = 0; i < text.length; i++) {
        var code = text.charCodeAt (i);
        if (code >= 128 || code == 91 || code == 93) {  //91 is "[", 93 is "]".
            newText += "[" + code.toString(16) + "]";
        } else {
            newText += text.charAt(i);
        }
    }
    return newText;
}

$(function(){
    var p = $('#synDate').datebox('panel');//日期选择对象
    var tds = false; //日期选择对象中月份
    var span = p.find('span.calendar-text'); //显示月份层的触发控件
    $("#synDate").datebox({
        onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
            span.trigger('click'); //触发click事件弹出月份层
            if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                tds = p.find('div.calendar-menu-month-inner td');
                tds.click(function (e) {
                    $(this).addClass("calendar-selected")
                    e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
                    var year = /\d{4}/.exec(span.html())[0]//得到年份
                    var month = parseInt($(this).attr('abbr'), 10) + 1; //月份
                    $("#synDate").datebox('hidePanel').datebox('setValue', year + "-" + month)
                });
            }, 0)
        },
        parser: function (s) {//配置parser，返回选择的日期
            if (!s) return new Date();
            var arr = s.split('-');
            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
        },
        formatter: function (d) {
            var year  = d.getFullYear() ;
            var month = d.getMonth() ;
            if(month ==0){
                month=12 ;
                year = year -1 ;
            }
            return year + '-' +month;

        }//配置formatter，只返回年月
    });


    $("#getDataBtn").on('click',function(){
        var yearMonth = $("#synDate").datebox('getValue') ;
        if(yearMonth){
            var reportSrc = "http://localhost:8075/WebReport/ReportServer?reportlet=inp_income_personal.cpt&__bypagesize__=false&yearMonth="+yearMonth+"&hospitalId="+config.hospitalId ;
            reportSrc = cjkEncode(reportSrc) ;
            document.getElementById("reportFrame").src=reportSrc;
        }else{
            $.messager.alert("系统提示","请选择核算日期",'error') ;
        }
    });

    $("#reCalcBtn").on('click',function(){
        var options =$("#hospitalIncomeRecordDatagrid").datagrid("options") ;
        options.url="/api/income-record/get-pre-income"
        var yearMonth = $("#synDate").datebox('getValue') ;
        if(yearMonth){
            $("#hospitalIncomeRecordDatagrid").datagrid('load',{hospitalId:config.hospitalId,yearMonth:yearMonth});
        }else{
            $.messager.alert("系统提示","请选择核算日期",'error') ;
        }
    })
})