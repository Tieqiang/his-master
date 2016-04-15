/**
 *单品总账月结
 * Created by wangbinbin on 2015/11/25.
 */
$(function(){
    $("#report").prop("src",parent.config.defaultReportPath + "exp-single-month-count.cpt&storageCode="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId);
});