/**
 *供应商付款情况统计
 * Created by wangbinbin on 2015/11、02.
 */
$(function(){
    $("#report").prop("src",parent.config.defaultReportPath + "exp-supplier-pay-count.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId);
});