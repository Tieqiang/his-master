package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpExportDetail;
import com.jims.his.domain.ieqm.entity.ExpStock;
import com.jims.his.domain.ieqm.facade.ExpAssignDictFacade;
import com.jims.his.domain.ieqm.facade.ExpStockFacade;
import com.jims.his.domain.ieqm.vo.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private ExpAssignDictFacade expAssignDictFacade;

    @Inject
    public ExpStockService(ExpStockFacade expStockFacade,ExpAssignDictFacade expAssignDictFacade) {
        this.expStockFacade = expStockFacade;
        this.expAssignDictFacade=expAssignDictFacade;
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
                                                     @QueryParam("hospitalId")String hospitalId,@QueryParam("subStorage")String subStorage){
        return expStockFacade.expExportStockRe(storageCode,hospitalId,expCode, subStorage);
    }
    /**
     * 查询对消入出库记录根据库房代码，消耗品代码，医院Id进行查询
     * @param storageCode
     * @param expCode
     * @param hospitalId
     * @return
     */
    @GET
    @Path("stock-export-import-balance")
    public List<ExpStockRecord> expExportImportStockRecord(@QueryParam("storageCode")String storageCode,@QueryParam("expCode")String expCode,
                                                     @QueryParam("hospitalId")String hospitalId){
        return expStockFacade.expExportImportStockRe(storageCode,hospitalId,expCode);
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
        String sql = "SELECT b.EXP_NAME,b.EXP_FORM,\n" +
                "       c.EXP_CODE,\n" +
                "       c.EXP_SPEC,\n" +
                "       c.units,\n" +
                "       c.min_spec,\n" +
                "       c.min_UNITS,\n" +
                "       c.FIRM_ID,\n" +
                "       c.TRADE_PRICE,\n" +
                "       c.TRADE_PRICE purchase_Price,\n" +
                "       c.retail_price,\n" +
                "       c.material_code,\n" +
                "       c.Register_no,\n" +
                "       0 quantity," +
                "       c.Permit_no,   c.amount_per_package \n" +
                "  FROM exp_dict b, exp_price_list c\n" +
                " WHERE b.EXP_CODE = c.EXP_CODE\n" +
                "   AND b.exp_spec = c.min_spec\n" +
                "   AND c.start_date <= sysdate\n" +
                "   AND (c.stop_date IS NULL OR c.stop_date > sysdate)\n" +
                "   AND b.EXP_CODE = '"+expCode+"'" +
                "   and c.hospital_id = '"+hospitalId+"'" ;

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
                errorException.setErrorMessage("数据已存在，保存失败！");
            }
            //else {
            //    errorException.setErrorMessage("保存失败！");
            //}
            return Response.status(Response.Status.OK).entity(errorException).build() ;
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
            List<ExpExportDetail> list=exportVo.getExpExportDetailBeanChangeVo().getInserted();
            if(list!=null&&!list.isEmpty()){
                for(ExpExportDetail e:list){
                    e.setAssignCode(expAssignDictFacade.findByCode(e.getAssignCode())==null?null:expAssignDictFacade.findByCode(e.getAssignCode()).getAssignCode());
                }
             }
            expStockFacade.expExportManage(exportVo) ;
            return Response.status(Response.Status.OK).entity(exportVo).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，保存失败！");
            }
            return Response.status(Response.Status.OK).entity(errorException).build() ;
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
                errorException.setErrorMessage("数据已存在，保存失败！");
            }
            //else {
            //    errorException.setErrorMessage("保存失败！");
            //}
            return Response.status(Response.Status.OK).entity(errorException).build() ;
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
    public List<ExpStorageProfileVo> searchWarningExpStock(@QueryParam("storage")String storage, @QueryParam("hospitalId")String hospitalId,@QueryParam("expName") String expName){
        List<ExpStorageProfileVo> dicts =  expStockFacade.searchWarningStock(storage,hospitalId,expName) ;
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
                errorException.setErrorMessage("数据已存在，保存失败！");
            }
            //else {
            //    errorException.setErrorMessage("保存失败！");
            //}
            return Response.status(Response.Status.OK).entity(errorException).build();
        }

    }

    /**
     * 根据月结开始时间获得月结结束时间
     * @param startDate
     * @return
     */
    public String getStopDate(String startDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(startDate));
            cal.add(Calendar.MONTH, 1);
            String stopDate = sdf.format(cal.getTime()) + " 00:00:00";
            return stopDate;
        } catch (Exception e) {
            e.printStackTrace();
            return sdf.format(new Date());
        }
    }
    /**
     * 产品月结，根据月结月份查询月结记录数目
     * @param storageCode
     * @param checkMonth
     * @return
     */
    @POST
    @Path("count-balance")
    @Produces("text/html")
    public Response countStockBalance(@QueryParam("storageCode") String storageCode, @QueryParam("checkMonth") String checkMonth) {
        String over;
        try {

            String startDate = checkMonth + "-01 00:00:00";
            String stopDate = getStopDate(startDate);
            Integer num = expStockFacade.countStockBalance(storageCode, startDate, stopDate);
            if(num==0){
               over = "ok";
            }else{
                over = "no";
            }
            return Response.status(Response.Status.OK).entity(over).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }
    /**
     * 产品月结，根据月结月份查询月结记录数目
     * @param storageCode
     * @param checkMonth
     * @return
     */
    @POST
    @Path("exp-current-balance")
    @Produces("text/html")
    public Response expCurrentStockBalance(@QueryParam("storageCode") String storageCode, @QueryParam("checkMonth") String checkMonth) {
        try {
            String startDate = checkMonth + "-01 00:00:00";
            String stopDate = getStopDate(startDate);
            expStockFacade.expCurrentStockBalance(storageCode, startDate, stopDate);
            return Response.status(Response.Status.OK).entity("ok").build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    /**
     * 本月内存在月结记录，（是）查看 （月结最大时间，最小时间）
     * @param storageCode
     * @param checkMonth
     * @return
     */
    @GET
    @Path("get-all-balance")
    public List<ExpStockBalanceVo> getAllBalance(@QueryParam("storageCode") String storageCode, @QueryParam("checkMonth") String checkMonth){
        String startDate = checkMonth + "-01 00:00:00";
        String stopDate = getStopDate(startDate);
        return expStockFacade.getAllBalance(storageCode, startDate, stopDate);
    }

    /**
     * 本月内存在月结记录，（否）查看 （月结最大时间，最小时间）
     * @param storageCode
     * @param checkMonth
     * @return
     */
    @GET
    @Path("get-last-balance")
    public List<ExpStockBalanceVo> getLastBalance(@QueryParam("storageCode") String storageCode, @QueryParam("checkMonth") String checkMonth) {
        String startDate = checkMonth + "-01 00:00:00";
        String stopDate = getStopDate(startDate);
        return expStockFacade.getLastBalance(storageCode, startDate, stopDate);
    }

    /**
     * 本月份内不存在月结记录，则使用本月份的开始时间，本月份的结束时间
     * @param storageCode
     * @param checkMonth
     * @return
     */
    @GET
    @Path("get-current-balance")
    public List<ExpStockBalanceVo> getCurrentBalance(@QueryParam("storageCode") String storageCode, @QueryParam("checkMonth") String checkMonth) {
        String startDate = checkMonth + "-01 00:00:00";
        String stopDate = getStopDate(startDate);
        return expStockFacade.getCurrentBalance(storageCode, startDate, stopDate);
    }

    /**
     * 根据expCode,expSpec,firmId获取全院库存
     * @param expCode
     * @param expSpec
     * @param firmId
     * @return
     */
    @GET
    @Path("get-quantity")
    public int getQuantity(@QueryParam("expCode") String expCode, @QueryParam("expSpec") String expSpec, @QueryParam("firmId") String firmId) {
        return expStockFacade.getQuantity(expCode, expSpec, firmId);
    }

}
