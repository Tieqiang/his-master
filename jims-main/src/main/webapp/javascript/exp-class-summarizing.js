/**
 * Created by txb on 2015/11/4.
 */
/***
 * 按开支类别汇总
 */
$(function(){
    var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp_class_summarizing.cpt&stockCode="+parent.config.storageCode;
    $("#report").prop("src",cjkEncode(https));
});