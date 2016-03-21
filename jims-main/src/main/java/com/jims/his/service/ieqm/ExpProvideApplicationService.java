package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpDict;
import com.jims.his.domain.ieqm.entity.ExpProvideApplication;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;
import com.jims.his.domain.ieqm.entity.ExpSupplierCatalog;
import com.jims.his.domain.ieqm.facade.ExpDictFacade;
import com.jims.his.domain.ieqm.facade.ExpProvideApplicationFacade;
import com.jims.his.domain.ieqm.facade.ExpStorageDeptFacade;
import com.jims.his.domain.ieqm.facade.ExpSupplierCatalogFacade;
import com.jims.his.domain.ieqm.vo.ExpProvideApplicationVo;
import com.jims.his.domain.ieqm.vo.ExpStockDefineVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangxinbo on 2015/10/14.
 */
@Path("exp-provide-application")
@Produces("application/json")
public class ExpProvideApplicationService {
    private ExpProvideApplicationFacade expProvideApplicationFacade;

    @Inject
    public ExpProvideApplicationService(ExpProvideApplicationFacade expProvideApplicationFacade) {
        this.expProvideApplicationFacade = expProvideApplicationFacade;
    }

    @POST
    @Path("save")
    public Response saveExpProvideApplication(BeanChangeVo<ExpProvideApplicationVo> changeVo) {
        try {
            List<ExpProvideApplication> newUpdateDict = new ArrayList<>();
            if (changeVo != null) {
                newUpdateDict = expProvideApplicationFacade.saveExpProvideApplication(changeVo);
            }

            return Response.status(Response.Status.OK).entity(newUpdateDict).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，保存失败！");
            }else{
                errorException.setErrorMessage("保存失败！");
            } 
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    //@POST
    //@Path("update")
    //public Response updateExpProvideApplication(List<ExpProvideApplicationVo> updateData, @QueryParam("applicationStorage") String applicationStorage) {
    //    try {
    //        List<ExpProvideApplicationVo> expProvideApplicationVos = expProvideApplicationFacade.updateExpProvideApplication(updateData, applicationStorage);
    //        return Response.status(Response.Status.OK).entity(expProvideApplicationVos).build();
    //    } catch (Exception e) {
    //        ErrorException errorException = new ErrorException();
    //        errorException.setMessage(e);
    //        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
    //    }
    //}

    /**
     * 作废
     * @param updateData
     * @return
     */
    @POST
    @Path("abandon")
    public Response abandonExpProvideApplication(List<ExpProvideApplicationVo> updateData) {
        try {
            List<ExpProvideApplicationVo> expProvideApplicationVos = expProvideApplicationFacade.abandonExpProvideApplication(updateData);
            return Response.status(Response.Status.OK).entity(expProvideApplicationVos).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，保存失败！");
            }else{
                errorException.setErrorMessage("保存失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    @POST
    @Path("delete")
    public Response deleteExpProvideApplication(List<ExpProvideApplicationVo> deleteData) {
        try {
            List<ExpProvideApplicationVo> expProvideApplicationVos = expProvideApplicationFacade.deleteExpProvideApplication(deleteData);
            return Response.status(Response.Status.OK).entity(expProvideApplicationVos).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    @GET
    @Path("find-cur-storage-application")
    public Response findCurStorageApplication(@QueryParam("startTime") String startTime, @QueryParam("endTime") String endTime, @QueryParam("isSure") String isSure, @QueryParam("applicationStorage") String applicationStorage) {
        try {
            List<ExpProvideApplicationVo> expProvideApplicationVos = expProvideApplicationFacade.findCurStorageApplication(startTime,endTime,isSure,applicationStorage);
            return Response.status(Response.Status.OK).entity(expProvideApplicationVos).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，保存失败！");
            }else{
                errorException.setErrorMessage("保存失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }

    /**
     * 出库申请：查询出库申请物品信息列表
     * @param storageCode
     * @param applyStorage
     * @param applyNo
     * @return
     */
    @GET
    @Path("find-dict-application")
    public List<ExpProvideApplicationVo> findExportApplyDict(@QueryParam("storageCode") String storageCode, @QueryParam("hospitalId") String hospitalId, @QueryParam("applyStorage") String applyStorage, @QueryParam("applyNo") String applyNo){
        return expProvideApplicationFacade.findExportApplyDict(storageCode,hospitalId,applyStorage,applyNo);
    }

}
