/**
 *按出库去向出库统计
 * Created by wangbinbin on 2015/11、02.
 */
$(function(){
    var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-by-export-go-count.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId;
    $("#report").prop("src",cjkEncode(https));
    //$("#report").prop("src",parent.config.defaultReportPath + "exp-by-export-go-count.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId);
});