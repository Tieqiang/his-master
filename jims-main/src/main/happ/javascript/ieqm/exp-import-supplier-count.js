/**
 * 供货商供货情况统计
 * Created by wangbinbin on 2015/11、05.
 */
$(function(){
    var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-import-supplier-count.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId;
    $("#report").prop("src",cjkEncode(https));
});