package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.*;
import com.jims.his.domain.ieqm.vo.ExpPrepareVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/2.
 */
public class ExpPrepareMasterFacade extends BaseFacade {
    private EntityManager entityManager;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH;mm:ss");
    private ExpSupplierCatalogFacade expSupplierCatalogFacade;
    private ExpDictFacade  expDictFacade;
    private ExpPriceListFacade expPriceListFacade;
    private ExpSubStorageDictFacade expSubStorageDictFacade;
    private ExpPrepareDetailFacade  expPrepareDetailFacade;


    @Inject
    public ExpPrepareMasterFacade (EntityManager entityManager,ExpSupplierCatalogFacade expSupplierCatalogFacade,ExpDictFacade  expDictFacade,ExpPriceListFacade expPriceListFacade,ExpSubStorageDictFacade expSubStorageDictFacade,ExpPrepareDetailFacade  expPrepareDetailFacade){
        this.entityManager=entityManager;
        this.expSupplierCatalogFacade=expSupplierCatalogFacade;
        this.expPrepareDetailFacade=expPrepareDetailFacade;
        this.expDictFacade=expDictFacade;
        this.expPriceListFacade=expPriceListFacade;
        this.expSubStorageDictFacade=expSubStorageDictFacade;
    }

    /**
     *备货方法
     * @param expId   产品Id  expPriceListId
     * @param supplierId   供货商 Id
     * @param amount      备货数量
     * @return
     */
    @Transactional
    public List<ExpPrepareDetail> save(String expId, String supplierId, String amount,String staffName,double price,List<ExpPrepareDetail> list,String firmId,String subStorageId,String phone,String oper) {
        /**
         * 在 exp_prepare_master 表中写入数据
         */
        ExpPrepareMaster expPrepareMaster=new ExpPrepareMaster();
        expPrepareMaster.setExpId(expId);
        expPrepareMaster.setSupplierId(supplierId);
        expPrepareMaster.setPrepareCount(amount);
        expPrepareMaster.setPrepareDate(sdf.format(new Date()));
        expPrepareMaster.setOperator(staffName);
        expPrepareMaster.setPrice(price);
        expPrepareMaster.setPhone(phone);
        expPrepareMaster.setPreparePersonName(oper);
        expPrepareMaster.setSubStorageId(subStorageId);
        /**
         *
         */
        String id=this.expSupplierCatalogFacade.findBySuppierId(firmId);
        expPrepareMaster.setFirmId(id);
        expPrepareMaster=entityManager.merge(expPrepareMaster);
        /**
         * 在 exp_prepare_detail 表中写入数据
         */
        Integer amountInt=Integer.parseInt(amount);
        for(int i=0;i<amountInt;i++){
            String barCode=new Date().getTime()+i+"";
            ExpPrepareDetail expPrepareDetail=new ExpPrepareDetail();
            expPrepareDetail.setOperator(staffName);
            expPrepareDetail.setMasterId(expPrepareMaster.getId());
            expPrepareDetail.setExpBarCode(barCode);
            expPrepareDetail.setUseFlag("0");//0 未使用
            expPrepareDetail.setPrintFlag("0");//0 未打印
//            expPrepareDetail.setImpDocnoFirst(document1);//入库单据号
//            expPrepareDetail.setExpDocnoFirst(document2);//出库单据号
            expPrepareDetail=entityManager.merge(expPrepareDetail);
            if(list!=null&&!list.isEmpty()&&!list.contains(expPrepareDetail)){
                list.add(expPrepareDetail);
            }
            if(list.isEmpty()){
                list.add(expPrepareDetail);
            }
        }
        return list;
    }

    /**
     * chenxy
     * @param masterId
     * @return
     */
    public ExpPrepareMaster findById(String masterId) {
        return (ExpPrepareMaster)entityManager.createQuery("from ExpPrepareMaster where id='"+masterId+"'").getSingleResult();
    }

    /**
     * chenxy
     * @Description 入库 出库 反写数据
     * @param expPrepareMaster
     * @param documentNo    入库单据号
     * @param documentNo2   出库单据号
     * @param operator   操作人
     * @param patientId   病人Id
     * @return
     */
    @Transactional
    public ExpPrepareVo prepareFee(ExpPrepareMaster expPrepareMaster, String documentNo, String documentNo2,  String operator, String patientId,String hospitalId,String barCode,String useDeptCode) {
        ExpPrepareVo expPrepareVo=new ExpPrepareVo();
        try {
            /**
             * 入库主表 expImportMaster
             */
            ExpSubStorageDict expSubStorageDict=this.expSubStorageDictFacade.findById(expPrepareMaster.getSubStorageId());
            ExpImportMaster expImportMaster =new ExpImportMaster();
//            expImportMaster.setOperator(operator);
            expImportMaster.setDocumentNo(documentNo);
            expImportMaster.setStorage(expSubStorageDict.getStorageCode());
            expImportMaster.setImportDate(new Date());
            expImportMaster.setAcctdate(new Date());
            expImportMaster.setOperator("0000");
            expImportMaster.setBuyer("0000");
            expImportMaster.setCheckman("0000");
            expImportMaster.setPrincipal("0000");
            expImportMaster.setDocStatus("0");
            expImportMaster.setStorekeeper("0000");
            expImportMaster.setSupplier(this.expSupplierCatalogFacade.findNameById(expPrepareMaster.getSupplierId()));
            expImportMaster.setHospitalId(hospitalId);
            expImportMaster.setAccountReceivable(expPrepareMaster.getPrice());//应付金额==备货价格
            expImportMaster.setAccountPayed(0.0);
            expImportMaster.setAdditionalFee(0.0);
            expImportMaster.setImportClass("正常入库");
//            ExpSubStorageDict expSubStorageDict=this.expSubStorageDictFacade.findById(expPrepareMaster.getSubStorageId());
            if(expSubStorageDict!=null){
                expImportMaster.setSubStorage(expSubStorageDict.getSubStorage());//子库房
            }
            expImportMaster.setAccountIndicator(1);//默认已记账
            expImportMaster.setAcctoperator("0000");
            expImportMaster=super.merge(expImportMaster);
            /**
             * 入库详情表  exp_import_detail
             */
            ExpPriceList expPriceList=this.expPriceListFacade.findById(expPrepareMaster.getExpId());
            ExpImportDetail expImportDetail=new ExpImportDetail();
            expImportDetail.setHospitalId(hospitalId);
            expImportDetail.setDocumentNo(documentNo);
            expImportDetail.setExpCode(expPriceList.getExpCode());
            ExpDict expDict=this.expDictFacade.findByCode(expPriceList.getExpCode());
            if(expDict!=null){
                expImportDetail.setExpForm(expDict.getExpForm());
            }
            ExpSupplierCatalog e=this.expSupplierCatalogFacade.findById(expPrepareMaster.getFirmId());
            expImportDetail.setFirmId(e.getSupplierId());//生产厂家
            expImportDetail.setPackageSpec(expPriceList.getExpSpec());
            expImportDetail.setPurchasePrice(expPriceList.getTradePrice());//进价
            expImportDetail.setTradePrice(expPriceList.getTradePrice());//林售价
            expImportDetail.setRetailPrice(expPriceList.getRetailPrice());
            expImportDetail.setUnits(expPriceList.getMinUnits());//最小规格
            expImportDetail.setPackageUnits(expPriceList.getUnits());//包装规格
            expImportDetail.setExpSpec(expPriceList.getMinSpec());//最小规格
            expImportDetail.setItemNo(Short.parseShort("0"));
            expImportDetail.setInventory(1.0);//现有数量
            expImportDetail.setQuantity(1.0);//chuku
            expImportDetail.setBatchNo("x");
            expImportDetail=super.merge(expImportDetail);
            /**
             * 出库主表  exp_export_master
             */
            ExpExportMaster expExportMaster=new ExpExportMaster();
            expExportMaster.setDocumentNo(documentNo2);
            expExportMaster.setStorage(expSubStorageDict.getStorageCode());
            expExportMaster.setExportDate(new Date());
            expExportMaster.setReceiver(useDeptCode);//
            expExportMaster.setAccountReceivable(expPriceList.getRetailPrice());//零售价
            expExportMaster.setAccountPayed(0.0);
            expExportMaster.setAdditionalFee(0.0);
            expExportMaster.setExportClass("正常出库");
            expExportMaster.setSubStorage(expSubStorageDict.getSubStorage());//子库房
            expExportMaster.setAccountIndicator(true);
//            expExportMaster.setOperator(operator);
            expExportMaster.setHospitalId(hospitalId);
            expExportMaster.setStorekeeper("0000");
            expExportMaster.setBuyer("0000");
            expExportMaster.setOperator("0000");
            expExportMaster.setAcctoperator("0000");
            expExportMaster.setPrincipal("0000");
            expExportMaster.setDocStatus("0");
            expExportMaster.setAcctdate(new Date());
            expExportMaster=super.merge(expExportMaster);
            /**
             * 出库详情表 exp_export_detail
             */
            /**
             * exp_export_detail 的item_no  ,   import_document_no  ,rec_flag（接收标识），rec_operator(写入0000)  ，rec_date记录扫描时间，big_code  ,big_spec  ,big_firm_id ,killflag(默认灭菌，即写1)
             exp_export_master和exp_import_master要改的差不多
             */
            ExpExportDetail expExportDetail=new ExpExportDetail();
            expExportDetail.setItemNo(0);
            expExportDetail.setImportDocumentNo(documentNo);//入库单据号
            expExportDetail.setRecFlag(1);
            expExportDetail.setRecOperator("0000");
            expExportDetail.setRecDate(new Date());
            expExportDetail.setKillflag(1);//默认灭菌
            expExportDetail.setBigCode(expPriceList.getExpCode());
            expExportDetail.setBigSpec(expPriceList.getExpSpec());
            expExportDetail.setHospitalId(hospitalId);
            expExportDetail.setDocumentNo(documentNo2);
            expExportDetail.setExpSpec(expPriceList.getMinSpec());
            expExportDetail.setExpCode(expPriceList.getExpCode());
            expExportDetail.setUnits(expPriceList.getMinUnits());
            expExportDetail.setFirmId(e.getSupplierId());
            expExportDetail.setExpForm(expDict.getExpForm());
            expExportDetail.setPurchasePrice(expPriceList.getTradePrice());//
            expExportDetail.setRetailPrice(expPriceList.getRetailPrice());
            expExportDetail.setTradePrice(expPriceList.getTradePrice());
            expExportDetail.setBatchNo("x");
            expExportDetail.setPackageSpec(expPriceList.getExpSpec());
            expExportDetail.setQuantity(1.0);//出库数量
            expExportDetail.setPackageUnits(expPriceList.getUnits());
            expExportDetail.setInventory(0.0);//现有数量为0
            expExportDetail.setAssignCode("");//分摊方式
            expExportDetail.setBigFirmId(e.getSupplierId());
            expExportDetail= super.merge(expExportDetail);
            /**
             * 库存表 exp_stock
             */
            ExpStock expStock=null;
            String sql="from ExpStock where expCode='"+expPriceList.getExpCode()+"' and expSpec='"+expPriceList.getMinSpec()+"' and packageSpec='"+expPriceList.getExpSpec()+"' and firmId='"+e.getSupplierId()+"' and storage='"+expSubStorageDict.getStorageCode()+"'";
            List<ExpStock> list=entityManager.createQuery(sql).getResultList();
            if(list!=null&&!list.isEmpty()){
                expStock=list.get(0);
                expStock.setDocumentNo(documentNo);
            }else{
                expStock=new ExpStock();
                expStock.setDocumentNo(documentNo);
                expStock.setStorage(expSubStorageDict.getStorageCode());//入库单据号
                expStock.setExpCode(expPriceList.getExpCode());
                expStock.setExpSpec(expPriceList.getMinSpec());
                expStock.setPackageSpec(expPriceList.getExpSpec());
                expStock.setUnits(expPriceList.getMinUnits());
                expStock.setPackageUnits(expPriceList.getUnits());
                expStock.setFirmId(e.getSupplierId());
                expStock.setQuantity(0.0);
                expStock.setPurchasePrice(expPriceList.getTradePrice());
                expStock.setDiscount(100.0);
                expStock.setSubStorage(this.expSubStorageDictFacade.findById(expPrepareMaster.getSubStorageId()).getSubStorage());
                expStock.setHospitalId(hospitalId);
                expStock.setSupplyIndicator(1);//可供标志
                expStock.setBatchNo("x");
            }
            if(expStock!=null){
                expStock=super.merge(expStock);
            }
            /**
             * 回写exp_prepare_detail 表
             */
            ExpPrepareDetail expPrepareDetail=this.expPrepareDetailFacade.findByCode(barCode);
            expPrepareDetail.setUseFlag("1");//已经使用
            expPrepareDetail.setUseDate(sdf.format(new Date()));
            expPrepareDetail.setImpDocnoFirst(documentNo);//入库单据号
            expPrepareDetail.setExpDocnoFirst(documentNo2);//出库单据号
            expPrepareDetail.setUseDept(useDeptCode);
            expPrepareDetail.setUsePatientId(patientId);
            expPrepareDetail=super.merge(expPrepareDetail);
            expPrepareVo.setExpCode(expPriceList.getExpCode());
            expPrepareVo.setExpBarCode(barCode);
            expPrepareVo.setExpFrom(expDict.getExpForm());
            expPrepareVo.setExpSpec(expPriceList.getMinSpec());
            expPrepareVo.setUsePrice(expPriceList.getRetailPrice().toString());
            expPrepareVo.setSupplier(this.expSupplierCatalogFacade.findNameById(expPrepareMaster.getSupplierId()));//供货商
            expPrepareVo.setPackageUnits(expPriceList.getUnits());
            expPrepareVo.setPackageSpec(expPriceList.getExpSpec());
            expPrepareVo.setExpName(expDict.getExpName());
            expPrepareVo.setFirm(this.expSupplierCatalogFacade.findNameById(expPrepareMaster.getFirmId()));
            expPrepareVo.setUserDept(expPrepareDetail.getUseDept());
            expPrepareVo.setUsePatientId(patientId);
            expPrepareVo.setUseFlag("已使用");
            expPrepareVo.setOperator(operator);
            expPrepareVo.setUseDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            expPrepareVo.setInDocumentNo(documentNo);
            expPrepareVo.setOutDocumentNo(documentNo2);
            ExpSubStorageDict expSubStorageDict1=this.expSubStorageDictFacade.findById(expPrepareMaster.getSubStorageId());
            expSubStorageDict1.setImportNoAva(expSubStorageDict1.getImportNoAva()+1);
            expSubStorageDict1.setExportNoAva(expSubStorageDict1.getExportNoAva()+1);
            expSubStorageDict1=expSubStorageDictFacade.save(expSubStorageDict1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expPrepareVo;
    }

    /**
     * 回滚操作
     * @param barCode
     * @return
     */
    @Transactional
    public Map<String, Object> rollBack(String barCode) {
        Map<String, Object> map=new HashMap<String,Object>();
        /**
         *exp_prepare_detail
         */
        ExpPrepareDetail expPrepareDetail=(ExpPrepareDetail)entityManager.createQuery("from ExpPrepareDetail where expBarCode='"+barCode+"'").getSingleResult();
        expPrepareDetail.setUseDept("");
        expPrepareDetail.setUseFlag("0");
        expPrepareDetail.setUseDate("");
        expPrepareDetail.setUsePatientId("");
        expPrepareDetail.setImpDocnoFirst("");
        expPrepareDetail.setExpDocnoFirst("");
        expPrepareDetail=merge(expPrepareDetail);

        /**
         * import export
         */
        int rows1=entityManager.createQuery("delete from ExpImportMaster where documentNo='"+expPrepareDetail.getImpDocnoFirst()+"'").executeUpdate();
        int rows2=entityManager.createQuery("delete from ExpImportDetail where documentNo='"+expPrepareDetail.getImpDocnoFirst()+"'").executeUpdate();
        int rows3=entityManager.createQuery("delete from ExpExportMaster where documentNo='"+expPrepareDetail.getExpDocnoFirst()+"'").executeUpdate();
        int rows4=entityManager.createQuery("delete from ExpExportDetail where documentNo='"+expPrepareDetail.getExpDocnoFirst()+"'").executeUpdate();
         /**
         *exp_stock
         */
        int rows5=entityManager.createQuery("delete from ExpStock where documentNo='"+expPrepareDetail.getExpDocnoFirst()+"'").executeUpdate();
        if(rows1!=0&&rows2!=0&&rows3!=0&&rows4!=0){
            map.put("info","操作成功！");
        }else{
            map.put("info","参数不正确！");
        }
        return map;
    }
}
