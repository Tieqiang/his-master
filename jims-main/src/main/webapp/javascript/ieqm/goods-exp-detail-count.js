/**
 *物资消耗品明细汇总
 * Created by wangbinbin on 2015/11、02.
 */
$(function(){
    $("#report").prop("src","http://"+parent.config.reportDict.ip+":"+parent.config.reportDict.port+"/report/ReportServer?reportlet=exp/exp-list/"+ "goods-exp-detail-count.cpt&storage="+parent.config.storageCode+"&hospitalId="+parent.config.hospitalId);
});