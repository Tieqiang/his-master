/**
 * Created by txb on 2015/11/4.
 */
/***
 * 按开支类别出库查询
 */
$(function(){
    var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/"+"export_export_detail.cpt"+"&stockCode="+parent.config.storageCode;
    $("#report").prop("src",cjkEncode(https));});