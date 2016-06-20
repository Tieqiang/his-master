/**
 * Created by txb on 2015/11/4.
 */
/***
 * 按开支类别科室统计
 */
$(function(){
    var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp_dept_summarizing.cpt&stockCode="+parent.config.storageCode;
    $("#report").prop("src",cjkEncode(https));
});
