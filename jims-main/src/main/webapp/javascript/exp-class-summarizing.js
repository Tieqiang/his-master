/**
 * Created by txb on 2015/11/4.
 */
/***
 * 按开支类别汇总
 */
$(function(){
    $("#report").prop("src",parent.config.defaultReportPath + "exp_class_summarizing.cpt&stockCode="+parent.config.storageCode);
});