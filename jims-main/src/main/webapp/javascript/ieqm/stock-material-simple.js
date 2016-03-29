/**
 * Created by txb on 2015/11/4.
 */
/***
 * 按开支类别出库查询
 */
$(function(){
    $("#report").prop("src",parent.config.defaultReportPath + "stock_material_simple.cpt&storage="+parent.config.storageCode+"&loginName="+parent.config.loginName);
});