/**
 * 会计应付款
 * Created by wangbinbin on 2015/10/04.
 */
$(function(){
    $("#report").prop("src",parent.config.defaultReportPath + "exp-accountor-pay-search.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId+"&loginName="+parent.config.loginName);
});