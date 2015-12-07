package com.jims.his.service.ieqm;

import com.jims.his.domain.ieqm.entity.ExpExportMaster;
import com.jims.his.domain.ieqm.entity.ExpImportDetail;
import com.jims.his.domain.ieqm.entity.ExpImportMaster;
import com.jims.his.domain.ieqm.facade.ExpExportFacade;
import com.jims.his.domain.ieqm.vo.ExpExportDetialVo;
import com.jims.his.domain.ieqm.vo.ExpExportVo;
import com.jims.his.domain.ieqm.vo.ExpImportDetailVo;
import com.jims.his.domain.ieqm.vo.ExpProvideApplicationVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * 消耗品出库相关
 * Created by heren on 2015/10/23.
 */

@Path("exp-export")
@Produces("application/json")
public class ExpExportService {


    private ExpExportFacade expExportFacade ;

    @Inject
    public ExpExportService(ExpExportFacade expExportFacade) {
        this.expExportFacade = expExportFacade;
    }


    @GET
    @Path("exp-export-document-detail")
    public List<ExpExportDetialVo> getExpDocumentDetail(@QueryParam("stockName")String stockName ,@QueryParam("storageCode")String storageCode,@QueryParam("expClass")String expClass,@QueryParam("hospitalId")String hospitalId){
        return expExportFacade.getExpExportDetailVo(expClass,stockName,storageCode,hospitalId) ;
    }

    /**
     * 出库单据查询
     * @param imClass    出库类型
     * @param startBill  开始单号
     * @param stopBill   停止单号
     * @param classRadio 类型标志
     * @param billRadio  单号标志
     * @param startDate  开始时间
     * @param stopDate   结束时间
     * @param receiver   接收方
     * @param searchInput 消耗品代码
     * @param hospitalId  医院Id
     * @param storage     库房代码
     * @return
     */
    @Path("exp-export-document-search")
    @GET
    @Consumes("application/json")
    public List<ExpExportMaster> searchMasterDataDict(@QueryParam("imClass") String imClass,@QueryParam("startBill") String startBill,@QueryParam("stopBill") String stopBill,@QueryParam("classRadio") String classRadio,@QueryParam("billRadio") Integer billRadio,@QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("receiver") String receiver,@QueryParam("searchInput") String searchInput,@QueryParam("hospitalId") String hospitalId,@QueryParam("storage") String storage){
        List<ExpExportMaster> dicts = expExportFacade.searchExportMasterDict(imClass, startBill, stopBill, searchInput, startDate, stopDate, storage, receiver, classRadio, billRadio, hospitalId) ;
        return dicts ;
    }

    /**
     * 消耗品出库单据明细查询 单号 医院Id
     * @param documentNo
     * @param hospitalId
     * @return
     */
    @Path("exp-export-document-detail-search")
    @GET
    @Consumes("application/json")
    public List<ExpImportDetailVo> searchExportDetailDataDict(@QueryParam("documentNo") String documentNo,@QueryParam("hospitalId") String hospitalId){
        if(documentNo == null && hospitalId == null){
            return new ArrayList<ExpImportDetailVo>();
        }else{
            List<ExpImportDetailVo> dicts = expExportFacade.searchExportDetailDict(documentNo, hospitalId) ;
            return dicts ;
        }
    }

    /**
     *出库记录查询
     * @param formClass  产品类型
     * @param deptAttr   部门属性
     * @param startDate  开始日期
     * @param stopDate   结束日期
     * @param expCode    消耗品代码
     * @param storage    部门代码
     * @param hospitalId  医院id
     * @return
     */
    @Path("exp-export-record-search")
    @GET
    @Consumes("application/json")
    public List<ExpExportDetialVo> searchExportRecordDataDict(@QueryParam("formClass") String formClass,@QueryParam("deptAttr") String deptAttr,@QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("expCode") String expCode,@QueryParam("storage") String storage,@QueryParam("hospitalId") String hospitalId){
            List<ExpExportDetialVo> dicts = expExportFacade.searchExportRecordDict(formClass, hospitalId, deptAttr, startDate, stopDate, expCode, storage) ;
            return dicts ;

    }

    /**
     *按子库房出库统计 产品类别 子库房 开始时间 结束时间 库房代码 医院id
     * @param formClass
     * @param subStorage
     * @param startDate
     * @param stopDate
     * @param storage
     * @param hospitalId
     * @return
     */
    @Path("exp-by-subStorage-export-count")
    @GET
    @Consumes("application/json")
    public List<ExpExportDetialVo> searchSubExportCountDataDict(@QueryParam("formClass") String formClass,@QueryParam("subStorage") String subStorage,@QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("storage") String storage,@QueryParam("hospitalId") String hospitalId){
            List<ExpExportDetialVo> dicts = expExportFacade.searchSubExportCountDict(formClass, hospitalId, subStorage, startDate, stopDate, storage) ;
            return dicts ;

    }
/**
     *库房产品去向汇总 产品类别 子库房 开始时间 结束时间 库房代码 医院id
     * @param formClass
     * @param subStorage
     * @param startDate
     * @param stopDate
     * @param storage
     * @param hospitalId
     * @return
     */
    @Path("storage-exp-go-count")
    @GET
    @Consumes("application/json")
    public List<ExpExportDetialVo> searchStorageGoCountDataDict(@QueryParam("formClass") String formClass,@QueryParam("subStorage") String subStorage,@QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("storage") String storage,@QueryParam("hospitalId") String hospitalId){
            List<ExpExportDetialVo> dicts = expExportFacade.searchStorageGoCountDict(formClass, hospitalId, subStorage, startDate, stopDate, storage) ;
            return dicts ;

    }

    /**
     * 出库分摊情况表
     * @param startDate
     * @param stopDate
     * @param storage
     * @param hospitalId
     * @return
     */
    @Path("exp-export-assign-search")
    @GET
    @Consumes("application/json")
    public List<ExpExportDetialVo> searchExportAssignDict(@QueryParam("startDate") Date startDate,@QueryParam("stopDate") Date stopDate,@QueryParam("storage") String storage,@QueryParam("hospitalId") String hospitalId){
            List<ExpExportDetialVo> dicts = expExportFacade.exportAssignDict( hospitalId, startDate, stopDate, storage) ;
            return dicts ;

    }

    /**
     * 产品去向/出库统计
     * @param type 统计类型
     * @param storage
     * @param hospitalId
     * @param value  类型对应的值
     * @param startDate
     * @param endDate
     * @return
     */
    @GET
    @Path("export-detail-by-exp-class")
    public List<ExpExportVo> getExportDetailByType(@QueryParam("type") String type,@QueryParam("storage") String storage, @QueryParam("hospitalId") String hospitalId,@QueryParam("value") String value, @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate){
        if(null != type && type.trim().equals("expClass")){
            return expExportFacade.getExportDetailByExportClass(storage, hospitalId, value, startDate, endDate);
        }
        if(null != type && type.trim().equals("storage")){
            return expExportFacade.getExportDetailByStorage(storage, hospitalId, value, startDate, endDate);
        }
        if (null != type && type.trim().equals("receiver")) {
            return expExportFacade.getExportDetailByTo(storage, hospitalId, value, startDate, endDate);
        }
        if (null != type && type.trim().equals("expCode")) {
            return expExportFacade.getExportDetailByExpCode(storage, hospitalId, value, startDate, endDate);
        }
        return null;
    }

    /**
     * 申请出库页面左上部分根据起止时间和库房查询出库申请记录
     * @param startDate
     * @param endDate
     * @param applyStorage
     * @param storage
     * @return
     */
    @GET
    @Path("export-apply")
    public List<ExpProvideApplicationVo> findExpProvideApplication(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate, @QueryParam("applyStorage") String applyStorage, @QueryParam("storage") String storage, @QueryParam("hospitalId") String hospitalId) {
        return expExportFacade.findExpProvideApplication(startDate,endDate,applyStorage,storage, hospitalId);
    }
}
