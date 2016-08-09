package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.facade.ExpStorageDeptFacade;
import com.jims.his.domain.ieqm.facade.ExpSupplierCatalogFacade;
import com.jims.his.domain.ieqm.vo.ExpSupplierVo;
import org.jboss.logging.Param;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/18.
 */

@Path("exp-storage-dept")
@Produces("application/json")
public class ExpStorageDeptService {

    private ExpStorageDeptFacade expStorageDeptFacade;
    private ExpSupplierCatalogFacade expSupplierCatalogFacade;

    @Inject
    public ExpStorageDeptService(ExpStorageDeptFacade expStorageDeptFacade, ExpSupplierCatalogFacade expSupplierCatalogFacade) {
        this.expStorageDeptFacade = expStorageDeptFacade;
        this.expSupplierCatalogFacade = expSupplierCatalogFacade;
    }

    @GET
    @Path("list")
    public List<ExpStorageDept> expStorageDeptList(@QueryParam("hospitalId") String hospitalId, @QueryParam("q") String q, @QueryParam("name") String name) {
        return expStorageDeptFacade.getByHospitalId(hospitalId, q, name);
    }

    /**
     * 根据库房级别查询下级库房
     *
     * @param hospitalId
     * @param storageCode
     * @return
     */
    @GET
    @Path("listLevelDown")
    public List<ExpStorageDept> expStorageDeptLevelList(@QueryParam("hospitalId") String hospitalId, @QueryParam("storageCode") String storageCode) {
        return expStorageDeptFacade.getDeptByHospitalId(hospitalId, storageCode);
    }


    /**
     * 根据库房级别查询下级库房
     *
     * @param hospitalId
     * @param storageCode
     * @return
     */
    @GET
    @Path("listLevelUp")
    public List<ExpStorageDept> expStorageDeptLevelUpList(@QueryParam("hospitalId") String hospitalId, @QueryParam("storageCode") String storageCode) {
        return expStorageDeptFacade.getDeptUpByHospitalId(hospitalId, storageCode);
    }

    /**
     * 通过库房代码、医院Id查找前后缀
     *
     * @param hospitalId
     * @param storageCode
     * @return
     */
    @GET
    @Path("list-storage")
    public List<ExpStorageDept> expStorageDept(@QueryParam("hospitalId") String hospitalId, @QueryParam("storageCode") String storageCode) {
        return expStorageDeptFacade.getByStorageCodeHospitalId(hospitalId, storageCode);
    }

    @POST
    @Path("merge")
    public Response mergerExpStorageDept(BeanChangeVo<ExpStorageDept> beanChangeVo) {
        try {
            List<ExpStorageDept> newUpdateDict = new ArrayList<>();
            newUpdateDict = expStorageDeptFacade.mergeExpStorageDept(beanChangeVo);
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
    @Path("listLevelByThis")
    public List<ExpSupplierVo> listLevelByThis(@QueryParam("hospitalId") String hospitalId, @QueryParam("storageCode") String storageCode, @QueryParam("exportClass") String exportClass) {
        return expSupplierCatalogFacade.listLevelByThis(hospitalId, storageCode, exportClass);
    }

    @Path("list-level-by-this")
    @GET
    public List<ExpSupplierVo> listLevelByThis(@QueryParam("hospitalId") String hospitalId, @QueryParam("storageCode") String storageCode,
                                               @QueryParam("exportClass") String exportClass, @QueryParam("q") String q) {
        return expSupplierCatalogFacade.listLevelByThis(hospitalId, storageCode, exportClass, q);
    }

}
