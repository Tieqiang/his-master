package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.facade.ExpStorageDeptFacade;
import org.jboss.logging.Param;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by heren on 2015/9/18.
 */

@Path("exp-storage-dept")
@Produces("application/json")
public class ExpStorageDeptService {

    private ExpStorageDeptFacade expStorageDeptFacade;

    @Inject
    public ExpStorageDeptService(ExpStorageDeptFacade expStorageDeptFacade) {
        this.expStorageDeptFacade = expStorageDeptFacade;
    }

    @GET
    @Path("list")
    public List<ExpStorageDept> expStorageDeptList(@QueryParam("hospitalId")String hospitalId, @QueryParam("q") String q, @QueryParam("name") String name) {
        return expStorageDeptFacade.getByHospitalId(hospitalId ,q,name) ;
    }

    /**
     * 通过库房代码、医院Id查找前后缀
     * @param hospitalId
     * @param storageCode
     * @return
     */
    @GET
    @Path("list-storage")
    public List<ExpStorageDept> expStorageDept(@QueryParam("hospitalId")String hospitalId,@QueryParam("storageCode")String storageCode) {
        return expStorageDeptFacade.getByStorageCodeHospitalId(hospitalId,storageCode) ;
    }

    @POST
    @Path("merge")
    public Response mergerExpStorageDept(List<ExpStorageDept> expStorageDepts){
        try{

            expStorageDeptFacade.mergeExpStorageDept(expStorageDepts) ;
            return Response.status(Response.Status.OK).entity(expStorageDepts).build();
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

    @POST
    @Path("delete")
    public Response deleteExpStorageDept(List<ExpStorageDept> expStorageDepts){
        try{
            expStorageDeptFacade.deleteExpStorageDept(expStorageDepts) ;
            return Response.status(Response.Status.OK).entity(expStorageDepts).build();
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

}
