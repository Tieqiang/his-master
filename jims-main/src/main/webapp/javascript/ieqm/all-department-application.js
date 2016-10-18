/**
 * Created by fyg on 2016/10/18.
 */
$(function () {
    var https = "http://" + parent.config.reportDict.ip + ":" + parent.config.reportDict.port + "/report/ReportServer?reportlet=exp/exp-list/all-department-application.cpt&storage=" + parent.config.storageCode + "&hospitalId=" + parent.config.hospitalId;
    $("#report").prop("src", cjkEncode(https));
});
