/**
 * Created by txb on 2015/11/4.
 */
/***
 * 按开支类别出库查询
 */
$(function(){
    $("#report").prop("src",parent.config.defaultReportPath + "exp_export_detail.cpt&stockCode="+parent.config.storageCode);
});
