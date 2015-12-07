package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpDisburseRec;
import com.jims.his.domain.ieqm.entity.ExpStock;
import com.jims.his.domain.ieqm.facade.ExpDisburseFacade;
import com.jims.his.domain.ieqm.facade.ExpStockFacade;
import com.jims.his.domain.ieqm.vo.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangbinbin on 2015/10/27.
 */
@Path("exp-dis")
@Produces("application/json")
public class ExpDisburseRecService {
    private ExpDisburseFacade expDisburseFacade;

    @Inject
    public ExpDisburseRecService(ExpDisburseFacade expDisburseFacade) {
        this.expDisburseFacade = expDisburseFacade;
    }
    /**
     * 付款处理
     * @return
     */
    @POST
    @Path("dis")
    public Response expDisburse(ExpDisburseVo disburseVo){
        try {
            expDisburseFacade.expDisburseVo(disburseVo) ;
            return Response.status(Response.Status.OK).entity(disburseVo).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

    /**
     *付款单据打印
     * @param disburseRecNo
     * @param startDate
     * @param stopDate
     * @param supplier
     * @param hospitalId
     * @param storage
     * @return
     */
    @Path("exp-pay-document-print")
    @GET
    @Consumes("application/json")
    public List<ExpDisburseRec> searchPayPrintDataDict(@QueryParam("disburseRecNo")String disburseRecNo, @QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("supplier") String supplier,@QueryParam("hospitalId") String hospitalId,@QueryParam("storage") String storage){
        List<ExpDisburseRec> dicts = expDisburseFacade.searchPayPrintDict(disburseRecNo,startDate,stopDate,storage,supplier,hospitalId) ;
        return dicts ;
    }

    /**
     *付款单据打印明细
     * @param disburseRecNo
     * @param hospitalId
     * @param storage
     * @return
     */
    @Path("exp-pay-detail-print")
    @GET
    @Consumes("application/json")
    public List<ExpDisburseRecVo> searchPayPrintDetailDataDict(@QueryParam("disburseRecNo") String disburseRecNo,@QueryParam("hospitalId") String hospitalId,@QueryParam("storage") String storage){
        List<ExpDisburseRecVo> dicts = expDisburseFacade.searchPayDetailPrintDict(storage,disburseRecNo ,hospitalId);
        return dicts ;
    }
}
