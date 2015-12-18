package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpStorageProfile;
import com.jims.his.domain.ieqm.facade.ExpPriceSearchFacade;
import com.jims.his.domain.ieqm.facade.ExpStorageProfileFacade;
import com.jims.his.domain.ieqm.facade.ExpStorageUpperLowerMarketFacade;
import com.jims.his.domain.ieqm.vo.ExpPriceSearchVo;
import com.jims.his.domain.ieqm.vo.ExpStorageProfileVo;
import com.jims.his.domain.ieqm.vo.ExpStorageZeroManageVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;


/**
 * Created by wangbinbin on 2015/10/15
 */
@Path("exp-storage-profile-market")
@Produces("application/json")
public class ExpStorageUpperLowerMarketService {

    private ExpStorageUpperLowerMarketFacade expStorageUpperLowerMarketFacade ;
    private ExpStorageProfileFacade expStorageProfileFacade;

    @Inject
    public ExpStorageUpperLowerMarketService(ExpStorageUpperLowerMarketFacade expStorageUpperLowerMarketFacade,ExpStorageProfileFacade expStorageProfileFacade) {
        this.expStorageUpperLowerMarketFacade = expStorageUpperLowerMarketFacade;
        this.expStorageProfileFacade = expStorageProfileFacade;
    }

    /**
     * 根据消耗量定义库存量
     * @param storageCode
     * @param startTime
     * @param stopTime
     * @return
     */
    @GET
    @Path("upper-lower")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpStorageProfileVo> listExpMenuSearch(@QueryParam("storageCode") String storageCode,@QueryParam("startTime") String startTime,@QueryParam("stopTime") String stopTime,@QueryParam("hospitalId") String hospitalId){

        List<ExpStorageProfileVo> resultList = expStorageUpperLowerMarketFacade.findAll(storageCode,startTime,stopTime,hospitalId);
        return resultList;
    }

    /**
     *保存修改后的上下限
     * @param expStorageProfileVos
     * @return
     */
    @Path("save")
    @POST
    public Response updateExpStock(List<ExpStorageProfileVo> expStorageProfileVos){
        try{
            List<ExpStorageProfileVo> dicts =  expStorageProfileFacade.saveStorageUpdate(expStorageProfileVos);
            return Response.status(Response.Status.OK).entity(dicts).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            } else {
                errorException.setErrorMessage("提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }

    }

}
