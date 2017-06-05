/**
 * 会计应付款
 * Created by wangbinbin on 2015/10/04.
 */
$(function(){
    var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-accountor-pay-search.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&loginName="+parent.config.loginName;
    $("#report").prop("src",cjkEncode(https));
    //$("#report").prop("src",parent.config.defaultReportPath + "exp-accountor-pay-search.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&loginName="+parent.config.loginName);
});