package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpStock;
import com.jims.his.domain.ieqm.facade.ExpStockFacade;
import com.jims.his.domain.ieqm.vo.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjing on 2015/10/27.
 */
@Path("exp-stock")
@Produces("application/json")
public class ExpStockService {
    private ExpStockFacade expStockFacade;
    private String formClass;
    private String storage;
    private String hospitalId;
    private String subStorageClass;
    private String classRadio;
    private String supplier;
    private String expCode;

    @Inject
    public ExpStockService(ExpStockFacade expStockFacade) {
        this.expStockFacade = expStockFacade;
    }

    /**
     * 查询数据，可以使用子库房名字进行查询
     * @param subStorage
     * @return
     */
    @GET
    @Path("list")
    public List<ExpStock> listExpStock(@QueryParam("subStorage") String subStorage){
        return expStockFacade.listExpStock(subStorage);
    }

    /**
     * 查询出库库房记录根据库房代码，消耗品代码，医院Id进行查询
     * @param storageCode
     * @param expCode
     * @param hospitalId
     * @return
     */
    @GET
    @Path("stock-export-record")
    public List<ExpStockRecord> expExportStockRecord(@QueryParam("storageCode")String storageCode,@QueryParam("expCode")String expCode,
                                                     @QueryParam("hospitalId")String hospitalId){
        return expStockFacade.expExportStockRe(storageCode,hospitalId,expCode);
    }


    /**
     * 查询某一个消耗品在某一个库房中的记录
     * @param storageCode
     * @param expCode
     * @return
     */
    @GET
    @Path("stock-record")
    public List<ExpStockRecord> listExpStockRecord(@QueryParam("storageCode")String storageCode,@QueryParam("expCode")String expCode,
                                                   @QueryParam("hospitalId")String hospitalId){
        String sql = "SELECT B.EXP_NAME,B.EXP_FORM,\n" +
                "       c.EXP_CODE,\n" +
                "       c.EXP_SPEC,\n" +
                "       c.units,\n" +
                "       c.min_spec,\n" +
                "       c.min_UNITS,\n" +
                "       c.FIRM_ID,\n" +
                "       c.TRADE_PRICE,\n" +
                "       c.retail_price,\n" +
                "       nvl(d.quantity, 0)quantity,\n" +
                "       c.Register_no,\n" +
                "       c.Permit_no\n" +
                "  FROM exp_dict B, exp_price_list c, exp_stock d\n" +
                " WHERE b.EXP_CODE = C.EXP_CODE\n" +
                "   AND b.exp_spec = c.min_spec\n" +
                "   and c.EXP_CODE = d.exp_code(+)\n" +
                "   and c.min_SPEC = d.exp_spec(+)\n" +
                "   and c.firm_id = d.firm_id(+)\n" +
                "   AND c.start_date <= sysdate\n" +
                "   AND (c.stop_date IS NULL OR c.stop_date > sysdate)\n" +
                "   and d.storage(+) like '"+storageCode+"' || '%'\n" +
                "   AND b.EXP_CODE = '"+expCode+"'" +
                "   and d.hospital_id = '"+hospitalId+"'" ;

        return expStockFacade.createNativeQuery(sql,new ArrayList<Object>(),ExpStockRecord.class) ;

    }


    /**
     * 消耗品入库
     * @return
     */
    @POST
    @Path("imp")
    public Response expImport(ExpImportVo importVo){
        try {
            expStockFacade.expImport(importVo) ;
            return Response.status(Response.Status.OK).entity(importVo).build() ;
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
     * 消耗品出库
     * @return
     */
    @POST
    @Path("exp-export-manage")
    public Response expExportManage(ExpExportManageVo exportVo){
        try {
            expStockFacade.expExportManage(exportVo) ;
            return Response.status(Response.Status.OK).entity(exportVo).build() ;
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
     * 消耗品入库
     * @return
     */
    @POST
    @Path("imp-batch")
    public Response expImportBatch(ExpImportVo importVo){
        try {
            expStockFacade.expImportBatch(importVo) ;
            return Response.status(Response.Status.OK).entity(importVo).build() ;
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
     * 库存限量报警：库存量和上下限进行比较，对于超过和少于上下限 的库存量进行报警
     * @param storage      库房代码
     * @param hospitalId  医院Id
     * @return
     */
    @GET
    @Path("upper-low-warning")
    public List<ExpStorageProfileVo> searchWarningExpStock(@QueryParam("storage")String storage, @QueryParam("hospitalId")String hospitalId){
        List<ExpStorageProfileVo> dicts =  expStockFacade.searchWarningStock(storage,hospitalId) ;
        return  dicts;
    }

    /**
     * 过期产品统计   根据截至日期、库房代码、医院Id 查询已过期的消耗品
     * @param overDate
     * @param storage
     * @param hospitalId
     * @return
     */
    @GET
    @Path("expire-count")
    public List<ExpStorageProfileVo> searchExpireExpStock(@QueryParam("overDate") String overDate,@QueryParam("storage")String storage, @QueryParam("hospitalId")String hospitalId){
        List<ExpStorageProfileVo> dicts =  expStockFacade.searchExpireStock(overDate,storage, hospitalId) ;
        return  dicts;
    }

    /**
     * 产品库存量查询
     * @param formClass  库存类型
     * @param storage    库房代码
     * @param hospitalId 医院Id
     * @param subStorageClass 子库房
     * @param supplier         供应商
     * @param expCode          消耗品代码
     * @return
     */
    @GET
    @Path("exp-stock-number-search")
    public List<ExpStorageProfileVo> searchExpStockNumber(@QueryParam("formClass") String formClass,@QueryParam("storage")String storage, @QueryParam("hospitalId")String hospitalId, @QueryParam("subStorageClass")String subStorageClass, @QueryParam("supplier")String supplier, @QueryParam("searchInput")String expCode){

        List<ExpStorageProfileVo> dicts =  expStockFacade.searchExpireStockNumber(formClass,storage, hospitalId,subStorageClass,supplier,expCode) ;
        return  dicts;
    }

    /**
     * 产品入出存/入出存统计-月结
     * @param storageCode
     * @param hospitalId
     * @param startDate
     * @param endDate
     * @param expForm
     * @return
     */
    @GET
    @Path("get-stock-balance")
    public List<ExpStockBalanceVo> getStockBalance(@QueryParam("storageCode")String storageCode, @QueryParam("hospitalId") String hospitalId, @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate, @QueryParam("expForm") String expForm){
        return expStockFacade.getStockBalance(storageCode,hospitalId,startDate,endDate,expForm);
    }

    /**
     * 对消入出库保存
     * @param portVo （有两个属性，一个是如初数据，一个是出库数据）
     * @return
     */
    @POST
    @Path("exp-export-import")
    public Response expExportImport(ExpExportImportVo portVo) {
        try {
            expStockFacade.expExportImport(portVo);
            return Response.status(Response.Status.OK).entity(portVo).build();
        } catch (Exception e) {
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

}
