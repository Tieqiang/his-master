package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpImportMaster;
import com.jims.his.domain.ieqm.facade.ExpImportDetailFacade;
import com.jims.his.domain.ieqm.facade.ExpImportMasterFacade;
import com.jims.his.domain.ieqm.facade.ExpSubStorageDictFacade;
import com.jims.his.domain.ieqm.vo.ExpDisburseRecVo;
import com.jims.his.domain.ieqm.vo.ExpDisburseVo;
import com.jims.his.domain.ieqm.vo.ExpImportDetailVo;
import com.jims.his.domain.ieqm.vo.ExpImportVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *消耗品入库
 */
@Path("exp-import")
@Produces("application/json")
public class ExpImportService {
    private ExpSubStorageDictFacade expSubStorageDictFacade ;//自库房Facade
    private ExpImportMasterFacade expImportMasterFacade ;
    private ExpImportDetailFacade expImportDetailFacade ;

    @Inject
    public ExpImportService(ExpSubStorageDictFacade expSubStorageDictFacade, ExpImportMasterFacade expImportMasterFacade, ExpImportDetailFacade expImportDetailFacade) {
        this.expSubStorageDictFacade = expSubStorageDictFacade;
        this.expImportMasterFacade = expImportMasterFacade;
        this.expImportDetailFacade = expImportDetailFacade;
    }

    /**
     *消耗品入库主记录查询
     * @param imClass    入库类型
     * @param startBill  开始账单
     * @param stopBill   结束账单
     * @param classRadio 类型标志
     * @param billRadio 账单标识
     * @param startDate 开始日期
     * @param stopDate  结束日期
     * @param supplier 供应商
     * @param searchInput 消耗品代码
     * @param hospitalId 医院id
     * @param storage    库房代码
     * @return
     */
    @Path("exp-import-document-search")
    @GET
    @Consumes("application/json")
    public List<ExpImportMaster> searchMasterDataDict(@QueryParam("imClass") String imClass,@QueryParam("startBill") String startBill,@QueryParam("stopBill") String stopBill,@QueryParam("classRadio") String classRadio,@QueryParam("billRadio") Integer billRadio,@QueryParam("startDate") String startDate,@QueryParam("stopDate") String stopDate,@QueryParam("supplier") String supplier,@QueryParam("searchInput") String searchInput,@QueryParam("hospitalId") String hospitalId,@QueryParam("storage") String storage){
        List<ExpImportMaster> dicts = expImportMasterFacade.searchImportMasterDict(imClass,startBill,stopBill,searchInput,startDate,stopDate,storage,supplier,classRadio,billRadio,hospitalId) ;
        return dicts ;
    }

    /**
     *消耗品入库明细查询
     * @param documentNo  单号
     * @param hospitalId  医院Id
     * @return
     */
    @Path("exp-import-document-detail-search")
    @GET
    @Consumes("application/json")
    public List<ExpImportDetailVo> searchImportDetailDataDict(@QueryParam("documentNo") String documentNo,@QueryParam("hospitalId") String hospitalId){

       if(documentNo == null && hospitalId == null){
           return new ArrayList<ExpImportDetailVo>();
       }else{
        List<ExpImportDetailVo> dicts = expImportDetailFacade.searchImportDetailDict(documentNo, hospitalId) ;
        return dicts ;
       }
    }

    /**
     *付款处理
     * @param documentNo  入库单号
     * @param hospitalId  医院id
     * @param startDate   开始日期
     * @param stopDate    结束日期
     * @param supplier    供应商
     * @param storage     库房代码
     * @return
     */
    @Path("exp-pay-manage")
    @GET
    @Consumes("application/json")
    public List<ExpImportDetailVo> managePayImportDetailDict(@QueryParam("documentNo") String documentNo,@QueryParam("hospitalId") String hospitalId,@QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("supplier") String supplier,@QueryParam("storage") String storage){

        List<ExpImportDetailVo> dicts = expImportDetailFacade.managePayImportDetail(documentNo, hospitalId,startDate,stopDate,storage,supplier);
        return dicts ;

    }

    /**
     *付款情况查询
     * @param documentNo   入库账单
     * @param payRadio     付款标志
     * @param startDate    开始日期
     * @param stopDate     结束日期
     * @param supplier     供应商
     * @param searchInput  expCode
     * @param hospitalId   医院Id
     * @param storage      库房代码
     * @return
     */
    @Path("exp-pay-search")
    @GET
    @Consumes("application/json")
    public List<ExpImportDetailVo> searchPayDataDict(@QueryParam("documentNo") String documentNo,@QueryParam("radio") String payRadio,@QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("supplier") String supplier,@QueryParam("searchInput") String searchInput,@QueryParam("hospitalId") String hospitalId,@QueryParam("storage") String storage){
        List<ExpImportDetailVo> dicts = expImportMasterFacade.searchImportPayMasterDict(documentNo,searchInput,startDate,stopDate,storage,supplier,hospitalId,payRadio) ;
        return dicts ;
    }

    /**
     * 消耗品供应商查询
     * @param startDate  开始日期
     * @param stopDate   结束日期
     * @param hospitalId 医院id
     * @param storage    库房代码
     * @return
     */
    @Path("exp-supplier-search")
    @GET
    public List<ExpDisburseRecVo> searchSupplierPayDataDict(@QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("hospitalId") String hospitalId,@QueryParam("storage") String storage){
        List<ExpDisburseRecVo> dicts = expImportDetailFacade.searchSupplierPayDict(startDate, stopDate, storage, hospitalId) ;
        return dicts ;
    }

    /**
     * 会计应付款
     * @param startDate
     * @param stopDate
     * @param hospitalId
     * @param storage
     * @return
     */
    @Path("exp-account-search")
    @GET
    public List<ExpImportDetailVo> searchAccountPayDataDict(@QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("hospitalId") String hospitalId,@QueryParam("storage") String storage){
        List<ExpImportDetailVo> dicts = expImportDetailFacade.searchAccountorPayDict(startDate, stopDate, storage, hospitalId) ;
        return dicts ;
    }

    /**
     * 产品上账查询
     * @param startDate   开始日期
     * @param stopDate    结束日期
     * @param hospitalId  医院id
     * @param imClass     入库类型
     * @param startBill   开始账单
     * @param stopBill    结束账单
     * @param billRadio   记账标志
     * @param storage     库房代码
     * @param supplier    供应商
     * @param expCode     消耗品代码
     * @return
     */
    @Path("exp-do-account")
    @GET
    public List<ExpImportDetailVo> searchDoAccountDict(@QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("hospitalId") String hospitalId,@QueryParam("imClass") String imClass,@QueryParam("startBill") String startBill,@QueryParam("stopBill") String stopBill,@QueryParam("billRadio") String billRadio,@QueryParam("storage") String storage,@QueryParam("supplier") String supplier,@QueryParam("expCode") String expCode){
        List<ExpImportDetailVo> dicts = expImportDetailFacade.searchDoAccountDict(startDate, stopDate, storage, hospitalId,imClass,startBill,stopBill,billRadio,supplier,expCode);
        return dicts ;
    }

    /**
     * 产品上账保存
     * @param importVo
     * @return
     */
    @Path("exp-do-account-save")
    @POST
    public Response saveDoAccountDict(ExpImportVo importVo){
        try {
            expImportDetailFacade.saveDoAccountDict(importVo) ;
            return Response.status(Response.Status.OK).entity(importVo).build();
        } catch (Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，保存失败！");
            } else {
                errorException.setErrorMessage("保存失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

    /**
     * 产品来源/入库记录查询
     *
     * @param hospitalId
     * @param startDateString
     * @param stopDateString
     * @param storage
     * @return
     */
    @GET
    @Path("exp-import-detail")
    @Consumes("application/json")
    public List<ExpImportDetailVo> getImportDetailDict(@QueryParam("hospitalId") String hospitalId, @QueryParam("startDate") String startDateString, @QueryParam("stopDate") String stopDateString, @QueryParam("storage") String storage) {
        Date startDate = new Date();
        Date stopDate = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            startDate = sdf.parse(startDateString);
            stopDate = sdf.parse(stopDateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ExpImportDetailVo> dicts = expImportDetailFacade.managePayImportDetail(null, hospitalId, startDate, stopDate, storage, null);
        return dicts;

    }

    /**
     *产品来源/按子库房入库统计
     * @param hospitalId
     * @param startDate
     * @param stopDate
     * @param storage
     * @param subStorage
     * @param expForm
     * @return
     */
    @GET
    @Path("exp-sub-import-detail")
    public List<ExpImportDetailVo> getSubImportDetails(@QueryParam("hospitalId") String hospitalId, @QueryParam("startDate") String startDate, @QueryParam("stopDate") String stopDate, @QueryParam("storage") String storage, @QueryParam("subStorage") String subStorage, @QueryParam("expForm") String expForm) {
        List<ExpImportDetailVo> dicts = expImportDetailFacade.getSubImportDetails(hospitalId, startDate, stopDate, storage, subStorage, expForm);
        return dicts;

    }

    /**
     * 单品总账
     * @param hospitalId  医院id
     * @param startDate   开始时间
     * @param stopDate    结束时间
     * @param storage     库房代码
     * @param expCode     消耗品代码
     * @param packageSpec  消耗品规格
     * @return
     */
    @GET
    @Path("exp-single-account")
    public List<ExpImportDetailVo> getExpSingleAccount(@QueryParam("hospitalId") String hospitalId, @QueryParam("startDate") Date startDate, @QueryParam("stopDate") Date stopDate, @QueryParam("storage") String storage, @QueryParam("expCode") String expCode, @QueryParam("packageSpec") String packageSpec) {
        List<ExpImportDetailVo> dicts = expImportDetailFacade.getSingleAccount(hospitalId, startDate, stopDate, storage, expCode, packageSpec);
        return dicts;

    }

    /**
     * 按入库类型入库统计  医院id 开始时间 结束时间 库房代码 入库类型
     * @param hospitalId
     * @param startDate
     * @param stopDate
     * @param storage
     * @param importClass
     * @return
     */
    @GET
    @Path("exp-import-class-account")
    public List<ExpImportDetailVo> getExpImportClassAccount(@QueryParam("hospitalId") String hospitalId, @QueryParam("startDate") Date startDate, @QueryParam("stopDate") Date stopDate, @QueryParam("storage") String storage, @QueryParam("importClass") String importClass) {
        List<ExpImportDetailVo> dicts = expImportDetailFacade.getImportClassAccount(hospitalId, startDate, stopDate, storage, importClass);
        return dicts;

    }

    /**
     * 供货商供货情况查询  医院id 开始时间 结束时间 库房代码 供应商
     * @param hospitalId
     * @param startDate
     * @param stopDate
     * @param storage
     * @param supplier
     * @return
     */
    @GET
    @Path("exp-import-supplier-search")
    public List<ExpImportDetailVo> getExpImportSupplierSearch(@QueryParam("hospitalId") String hospitalId, @QueryParam("startDate") Date startDate, @QueryParam("stopDate") Date stopDate, @QueryParam("storage") String storage, @QueryParam("supplier") String supplier) {
        List<ExpImportDetailVo> dicts = expImportDetailFacade.getImportSupplierSearch(hospitalId, startDate, stopDate, storage, supplier);
        return dicts;

    }

    /**
     * 供货商供货情况统计 医院id 开始时间 结束时间 库房代码 子库房
     * @param hospitalId
     * @param startDate
     * @param stopDate
     * @param storage
     * @param subStorage
     * @return
     */
    @GET
    @Path("exp-import-supplier-count")
    public List<ExpImportDetailVo> getExpImportSupplierCount(@QueryParam("hospitalId") String hospitalId, @QueryParam("startDate") Date startDate, @QueryParam("stopDate") Date stopDate, @QueryParam("storage") String storage, @QueryParam("subStorage") String subStorage) {
        List<ExpImportDetailVo> dicts = expImportDetailFacade.getImportSupplierCount(hospitalId, startDate, stopDate, storage, subStorage);
        return dicts;

    }
}
