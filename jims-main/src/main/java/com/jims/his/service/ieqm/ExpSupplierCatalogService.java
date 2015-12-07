package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpSupplierCatalog;
import com.jims.his.domain.ieqm.facade.ExpSupplierCatalogFacade;
import com.jims.his.domain.ieqm.vo.ExpNameCaVo;
import com.jims.his.domain.ieqm.vo.ExpSupplierVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by tangxinbo on 2015/10/10.
 */
@Path("exp-supplier-catalog")
@Produces("application/json")
public class ExpSupplierCatalogService {
    private ExpSupplierCatalogFacade expSupplierCatalogFacade;

    @Inject
    public ExpSupplierCatalogService(ExpSupplierCatalogFacade expSupplierCatalogFacade) {
        this.expSupplierCatalogFacade = expSupplierCatalogFacade;
    }

    @GET
    @Path("list")
    public List<ExpSupplierCatalog> getExpPriceList() {
        return expSupplierCatalogFacade.listExpSupplierCatalog();
    }

    @GET
    @Path("find-supplier")
    public List<ExpSupplierCatalog> findSupplier(@QueryParam("supplierName")String supplierName,@QueryParam("supplier")String inputCode){
        List<ExpSupplierCatalog> expSupplierCatalogList = null;
        if (supplierName != null && supplierName.trim().length() > 0){
            expSupplierCatalogList = expSupplierCatalogFacade.findSupplierBySupplierClass(supplierName);
        }
        if(inputCode != null && inputCode.trim().length() > 0) {
            expSupplierCatalogList =expSupplierCatalogFacade.findSupplierByInputCode(inputCode);
        }
        return expSupplierCatalogList;
    }
    @GET
    @Path("find-supplier-by-q")
    public List<ExpSupplierCatalog> listExpNameCa(@QueryParam("q") String q) {
        List<ExpSupplierCatalog> expSupplierCatalogList = expSupplierCatalogFacade.listByInputCodeQ(q);
        return expSupplierCatalogList;
    }
    @POST
    @Path("add")
    public Response saveExpSupplierCatalog(List<ExpSupplierCatalog> insertDate){
        try {
            List<ExpSupplierCatalog> expSupplierCatalogList = expSupplierCatalogFacade.saveExpSupplierCatalog(insertDate);
            return Response.status(Response.Status.OK).entity(expSupplierCatalogList).build();
        }catch (Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }
    @POST
    @Path("update")
    public Response updateExpSupplierCatalog(List<ExpSupplierCatalog> updateDate){
        try {
            List<ExpSupplierCatalog> expSupplierCatalogList = expSupplierCatalogFacade.updateExpSupplierCatalog(updateDate);
            return Response.status(Response.Status.OK).entity(expSupplierCatalogList).build();
        } catch (Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }
    @POST
    @Path("delete")
    public Response deleteExpSupplierCatalog(List<ExpSupplierCatalog> deleteDate){
        try {
            List<ExpSupplierCatalog> expSupplierCatalogList = expSupplierCatalogFacade.deleteExpSupplierCatalog(deleteDate);
            return Response.status(Response.Status.OK).entity(expSupplierCatalogList).build();
        }catch (Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    @GET
    @Path("list-with-dept")
    public List<ExpSupplierVo> listSupplierVoByHospitalId(@QueryParam("hospitalId")String hospitalId){

        return expSupplierCatalogFacade.listExpSupplierWithDept(hospitalId) ;
    }
}
