/**
 *供应商付款情况统计
 * Created by wangbinbin on 2015/11、02.
 */
$(function(){
    var https="http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/exp-supplier-pay-count.cpt"+"&hospitalId="+parent.config.hospitalId+"&storage="+parent.config.storageCode;
    $("#report").prop("src",cjkEncode(https));
});