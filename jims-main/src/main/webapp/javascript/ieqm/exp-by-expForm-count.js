/**
 *按出库类型汇总表
 * Created by wangbinbin on 2015/11、02.
 */
$(function(){
    $("#report").prop("src",parent.config.defaultReportPath + "exp-by-expForm-count.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId);
});