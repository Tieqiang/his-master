package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpSupplierCatalog;
import com.jims.his.domain.ieqm.facade.ExpSupplierCatalogFacade;
import com.jims.his.domain.ieqm.vo.ExpNameCaVo;
import com.jims.his.domain.ieqm.vo.ExpSupplierVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
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

    //@GET
    //@Path("list")
    //public List<ExpSupplierCatalog> getExpPriceList() {
    //    return expSupplierCatalogFacade.listExpSupplierCatalog();
    //}

    @GET
    @Path("find-supplier")
    public List<ExpSupplierCatalog> findSupplier(@QueryParam("supplierName")String supplierName,@QueryParam("supplier")String inputCode){
        List<ExpSupplierCatalog> expSupplierCatalogList = null;
        if (supplierName != null && supplierName.trim().length() > 0){
            expSupplierCatalogList = expSupplierCatalogFacade.findSupplierBySupplierClass(supplierName,null);
        }
        if(inputCode != null && inputCode.trim().length() > 0) {
            expSupplierCatalogList =expSupplierCatalogFacade.findSupplierByInputCode(inputCode,null);
        }
        return expSupplierCatalogList;
    }
    @GET
    @Path("find-supplier-by-q")
    public List<ExpSupplierCatalog> listExpNameCa(@QueryParam("q") String q, @QueryParam("type") String type) {
        List<ExpSupplierCatalog> expSupplierCatalogList = expSupplierCatalogFacade.listByInputCodeQ(q, type);
        return expSupplierCatalogList;
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     * @return
     */
    @POST
    @Path("merge")
    public Response save(BeanChangeVo<ExpSupplierCatalog> beanChangeVo) {
        try {
            List<ExpSupplierCatalog> newUpdateDict = new ArrayList<>();
            newUpdateDict = expSupplierCatalogFacade.save(beanChangeVo);
            return Response.status(Response.Status.OK).entity(newUpdateDict).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if (errorException.getErrorMessage().toString().indexOf("最大值") != -1) {
                errorException.setErrorMessage("输入数据超过长度！");
            } else if (errorException.getErrorMessage().toString().indexOf("唯一") != -1) {
                errorException.setErrorMessage("数据已存在，保存失败！");
            } else {
                errorException.setErrorMessage("保存失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }

    }

    @GET
    @Path("list-with-dept")
    public List<ExpSupplierVo> listSupplierVoByHospitalId(@QueryParam("hospitalId")String hospitalId,@QueryParam("q") String q){

        return expSupplierCatalogFacade.listExpSupplierWithDept(hospitalId,q) ;
    }

   /***
     * 查询所有供应商
     * @return
     */
    @GET
    @Path("list-all")
    public List<ExpSupplierVo> listAll(){
        return expSupplierCatalogFacade.findAll(new ArrayList<ExpSupplierVo>()) ;
    }


    @GET
    @Path("find-by-supplier-id")
    public ExpSupplierCatalog findBySupplierId(@QueryParam("supplierId") String supplierId){
        String id= expSupplierCatalogFacade.findBySuppierId(supplierId);
        if(id!=null)
        return expSupplierCatalogFacade.findById(id);
        return null;
    }
}
