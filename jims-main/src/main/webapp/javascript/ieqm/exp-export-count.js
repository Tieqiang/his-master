/**
 * 产品去向汇总
 * Created by fengyuguang on 2016-08-18.
 */
$(function(){
    var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-by-export-go-count.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId;
    $("#report").prop("src",cjkEncode(https));
});