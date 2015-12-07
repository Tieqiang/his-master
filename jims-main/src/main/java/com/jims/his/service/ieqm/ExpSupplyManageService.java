package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.facade.ExpStockFacade;
import com.jims.his.domain.ieqm.vo.ExpStorageProfileVo;
import com.jims.his.domain.ieqm.vo.ExpStorageZeroManageVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * Created by wangbinbin on 2015/10/21
 */
@Path("exp-supply-manage")
@Produces("application/json")
public class ExpSupplyManageService {

    private ExpStockFacade expStockFacade ;

    @Inject
    public ExpSupplyManageService(ExpStockFacade expStockFacade) {
        this.expStockFacade = expStockFacade;
    }

    /**
     * 零库存管理
     * @param storageCode
     * @return
     */
    @GET
    @Path("manage")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpStorageZeroManageVo> listExpSearch(@QueryParam("storageCode") String storageCode,@QueryParam("hospitalId") String hospitalId){
//        ,@QueryParam("expCode") String expCode,@QueryParam("supplyIndicator") String supplyIndicator
        List<ExpStorageZeroManageVo> resultList = expStockFacade.findSupplyManage(storageCode,hospitalId);
        return resultList;
    }

    /**
     * 保存 exp_stock 中修改后的供应标志
     * @param expStorageZeroManageVos
     * @return
     */
    @Path("save")
    @POST
    public Response updateExpStock(List<ExpStorageZeroManageVo> expStorageZeroManageVos){
        try{
            List<ExpStorageZeroManageVo> dicts =  expStockFacade.saveStockSupplyManageUpdate(expStorageZeroManageVos);
            return Response.status(Response.Status.OK).entity(dicts).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }
}
