/**
 * Created by txb on 2015/11/4.
 */
/***
 * 按开支类别科室统计
 */
$(function(){
    $("#report").prop("src",parent.config.defaultReportPath + "exp_dept_summarizing.cpt&stockCode="+parent.config.storageCode);
});