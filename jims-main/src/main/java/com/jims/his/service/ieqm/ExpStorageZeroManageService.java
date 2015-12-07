package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpExportClassDict;
import com.jims.his.domain.ieqm.entity.ExpStock;
import com.jims.his.domain.ieqm.facade.ExpStockFacade;
import com.jims.his.domain.ieqm.facade.ExpStorageUpperLowerMarketFacade;
import com.jims.his.domain.ieqm.vo.ExpStorageProfileVo;
import com.jims.his.domain.ieqm.vo.ExpStorageZeroManageVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by wangbinbin on 2015/10/20
 */
@Path("exp-storage-zero-manage")
@Produces("application/json")
public class ExpStorageZeroManageService {

    private ExpStockFacade expStockFacade ;

    @Inject
    public ExpStorageZeroManageService(ExpStockFacade expStockFacade) {
        this.expStockFacade = expStockFacade;
    }

    /**
     * 零库存管理
     * @param storageCode
     * @param hospitalId
     * @return
     */
    @GET
    @Path("list")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpStorageZeroManageVo> listExpSearch(@QueryParam("storageCode") String storageCode,@QueryParam("hospitalId") String hospitalId){
        List<ExpStorageZeroManageVo> resultList = expStockFacade.findStockZeroAll(storageCode,hospitalId);
        return resultList;
    }

    /**
     * 删除 exp_storage
     * @param expStocks
     * @return
     */
    @Path("delete")
    @POST
    public Response delExpStock(List<ExpStorageZeroManageVo> expStocks){
        try{
            expStockFacade.saveStorage(expStocks);
            return Response.status(Response.Status.OK).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }

    }

}
