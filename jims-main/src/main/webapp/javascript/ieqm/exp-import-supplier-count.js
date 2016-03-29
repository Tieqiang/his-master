/**
 * 供货商供货情况统计
 * Created by wangbinbin on 2015/11、05.
 */
$(function(){
    $("#report").prop("src",parent.config.defaultReportPath + "exp-import-supplier-count.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId);
});