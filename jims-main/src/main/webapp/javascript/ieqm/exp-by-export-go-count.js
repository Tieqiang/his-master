/**
 *按出库去向出库统计
 * Created by wangbinbin on 2015/11、02.
 */
$(function(){
    $("#report").prop("src",parent.config.defaultReportPath + "/exp/exp_go/exp-by-export-go-count.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId);
});