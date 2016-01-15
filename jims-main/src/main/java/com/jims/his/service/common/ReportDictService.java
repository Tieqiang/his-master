package com.jims.his.service.common;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.entity.ReportDict;
import com.jims.his.domain.common.facade.ReportDictFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjing on 2016/1/14.
 */
@Path("report-dict")
public class ReportDictService {

    private ReportDictFacade reportDictFacade;

    @Inject
    public ReportDictService(ReportDictFacade reportDictFacade){
        this.reportDictFacade = reportDictFacade;
    }

    @Path("list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReportDict> findAllReport(){
        List<ReportDict> reportDicts = reportDictFacade.findAll(ReportDict.class) ;

        return reportDicts ;
    }

    @POST
    @Path("merge")
    public Response mergerReportDept(BeanChangeVo<ReportDict> beanChangeVo) {
        try {
            List<ReportDict> newUpdateDict = new ArrayList<>();
            newUpdateDict = reportDictFacade.mergeReportDept(beanChangeVo);
            return Response.status(Response.Status.OK).entity(newUpdateDict).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if (errorException.getErrorMessage().toString().indexOf("最大值") != -1) {
                errorException.setErrorMessage("输入数据超过长度！");
            } else if (errorException.getErrorMessage().toString().indexOf("唯一") != -1) {
                errorException.setErrorMessage("数据已存在，提交失败！");
            } else {
                errorException.setErrorMessage("提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }
}
