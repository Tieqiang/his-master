package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.StaffDict;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.*;
import com.jims.his.domain.ieqm.vo.ExpExportDetialVo;
import com.jims.his.domain.ieqm.vo.ExpExportManageVo;
import com.jims.his.domain.ieqm.vo.ExpImportVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangjing on 2015/11/3.
 */
public class ExpInventoryCheckFacade extends BaseFacade {
    private EntityManager entityManager;
    private ExpStockFacade expStockFacade;
    private ExpSubStorageDictFacade expSubStorageDictFacade;
    private ExpStorageDeptFacade expStorageDeptFacade;
    @Inject
    public ExpInventoryCheckFacade(EntityManager entityManager, ExpStockFacade expStockFacade, ExpSubStorageDictFacade
            expSubStorageDictFacade,ExpStorageDeptFacade expStorageDeptFacade) {
        this.entityManager = entityManager;
        this.expStockFacade = expStockFacade;
        this.expSubStorageDictFacade = expSubStorageDictFacade;
        this.expStorageDeptFacade = expStorageDeptFacade;
    }

    /**
     * 生成盘点记录前首先查询盘点记录表看是否有这段时间的盘点记录
     * @param storageCode
     * @param subStorage
     * @param checkMonth
     * @return
     */
    public int getInventoryNum(String hospitalId, String storageCode, String subStorage, String checkMonth){
        String sql = "select nvl(count (*),0) from exp_INVENTORY_CHECK where storage ='"+storageCode+"' and hospital_id='"+hospitalId+"' and to_char(check_year_month,'YYYY-MM') =to_char(to_date('" + checkMonth + "','YYYY-MM-DD HH24:MI:SS'),'YYYY-MM') and ( sub_storage ='"+subStorage+"' or '"+subStorage+"' ='全部' ) \n";
        List result = super.createNativeQuery(sql).getResultList();
        return ((BigDecimal)result.get(0)).intValue();
    }

    /**
     * 生成待盘点的记录
     * @param type(GET---生成按钮,SEARCH---检索按钮)
     * @param storageCode
     * @param hospitalId
     * @param subStorage
     * @param checkMonth
     * @param hiddenFlag  隐藏账面数为0的标志
     * @return
     */
    public List<ExpInventoryCheck> getInventory(String type, String storageCode, String hospitalId,
                                                String subStorage, String checkMonth,String hiddenFlag) {
        String sql = "";
        if(type.equals("get")){
            sql = "SELECT EXP_STOCK.STORAGE STORAGE,   \n" +
                    "         EXP_DICT.EXP_NAME EXP_NAME,   \n" +
                    "         EXP_DICT.EXP_FORM EXP_FORM,   \n" +
                    "         EXP_STOCK.EXP_CODE EXP_CODE,   \n" +
                    "         EXP_STOCK.PACKAGE_SPEC MIN_SPEC,   \n" +
                    "         EXP_STOCK.PACKAGE_UNITS MIN_UNITS,   \n" +
                    "         EXP_STOCK.FIRM_ID FIRM_ID,   \n" +
                    "         EXP_STOCK.BATCH_NO BATCH_NO,   \n" +
                    "         EXP_STOCK.EXP_SPEC EXP_SPEC,   \n" +
                    "         EXP_STOCK.UNITS UNITS,   \n" +
                    "         EXP_STOCK.SUB_STORAGE SUB_STORAGE,   \n" +
                    "         nvl(EXP_STOCK.QUANTITY,0) ACCOUNT_QUANTITY,   \n" +
                    "         0.00 ACTUAL_QUANTITY,   \n" +
                    "         nvl(EXP_PRICE_LIST.TRADE_PRICE,0) TRADE_PRICE,   \n" +
                    "         nvl(EXP_PRICE_LIST.RETAIL_PRICE,0) RETAIL_PRICE,   \n" +
                    "         nvl(EXP_STOCK.PURCHASE_PRICE,0) PURCHASE_PRICE,\n" +
                    "         EXP_STOCK.HOSPITAL_ID HOSPITAL_ID,\n" +
                    "         to_date('" + checkMonth + "','YYYY-MM-DD HH24:MI:SS') CHECK_YEAR_MONTH,   \n" +
                    "         0 REC_STATUS,\n" +
                    "         '          ' LOCATION\n" +
                    "    FROM EXP_DICT,\n" +
                    "         EXP_PRICE_LIST,   \n" +
                    "         EXP_STOCK  \n" +
                    "   WHERE EXP_STOCK.STORAGE = '" + storageCode + "' and    \n" +
                    "         EXP_STOCK.HOSPITAL_ID = '" + hospitalId + "' and    \n" +
                    "\t\t\tEXP_dict.EXP_code(+) = EXP_STOCK.EXP_code and  \n" +
                    "         EXP_dict.EXP_spec (+) = EXP_STOCK.EXP_spec and  \n" +
                    "\t\t\tEXP_price_list.EXP_code(+) = EXP_STOCK.EXP_code and  \n" +
                    "\t\t\tEXP_price_list.EXP_spec(+) = EXP_STOCK.package_spec and\n" +
                    "\t\t\tEXP_price_list.firm_id(+) = EXP_STOCK.firm_id and \n" +
                    "\t\t\tEXP_price_list.stop_date is null and\n" +
                    "\t\t\t(EXP_STOCK.SUB_STORAGE = '" + subStorage + "' OR '" + subStorage + "' = '全部') and\n" +
                    "\t\t\t(EXP_STOCK.EXP_CODE = '全部' OR '全部' = '全部') and\n" +
                    "\t\t\t(EXP_DICT.EXP_FORM = '全部' OR '全部' = '全部')";
            if(hiddenFlag.trim().equals("1")){
                sql += " and exp_stock.quantity > 0";
            }
        }
        if(type.equals("search")){
            sql = "SELECT EXP_INVENTORY_CHECK.*,   \n" +
                    "         EXP_DICT.EXP_FORM EXP_FORM,   \n" +
                    "         '          ' LOCATION\n" +
                    "    FROM EXP_DICT,   \n" +
                    "         EXP_INVENTORY_CHECK  \n" +
                    "   WHERE EXP_INVENTORY_CHECK.STORAGE = '" + storageCode + "' and    \n" +
                    "         EXP_INVENTORY_CHECK.HOSPITAL_ID = '" + hospitalId + "' and    \n" +
                    "         ( EXP_dict.EXP_code (+) = EXP_inventory_check.EXP_code) and  \n" +
                    "         ( EXP_dict.EXP_spec (+) = EXP_inventory_check.min_spec) and  \n" +
                    "         ( EXP_INVENTORY_CHECK.CHECK_YEAR_MONTH = to_date('" + checkMonth + "','YYYY-MM-DD HH24:MI:SS')  AND  \n" +
                    "         ( \"EXP_INVENTORY_CHECK\".\"SUB_STORAGE\" = '" + subStorage + "' OR '" + subStorage + "' = '全部') )";
        }

        List<ExpInventoryCheck> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpInventoryCheck.class);

        return result;
    }

    /**
     * 检索存在的盘点表
     * @param storageCode
     * @param checkMonth
     * @return
     */
    public List<ExpInventoryCheck> inventoryListByTime(String hospitalId,String storageCode, String checkMonth){
        String sql = "select distinct CHECK_YEAR_MONTH CHECK_YEAR_MONTH\n" +
                "from EXP_INVENTORY_CHECK\n" +
                "where STORAGE = '"+ storageCode+"' and hospital_id='" +hospitalId+
                "' and to_char(CHECK_YEAR_MONTH,'yyyy-mm-dd hh24:mi:ss') like '"+ checkMonth+"'||'%'";
        List<ExpInventoryCheck> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpInventoryCheck.class);

        return result;
    }

    //根据盘点记录生成入库数据
    public ExpImportVo generateImport(List<ExpInventoryCheck> rows){
        ExpImportVo importVo = new ExpImportVo();
        if (rows.size() > 0) {
            BeanChangeVo<ExpImportMaster> expImportMasterBeanChangeVo = new BeanChangeVo<ExpImportMaster>();//入库单据主记录
            BeanChangeVo<ExpImportDetail> expImportDetailBeanChangeVo = new BeanChangeVo<ExpImportDetail>();//入库单据明细记录

            List<ExpImportMaster> importMasterInserted = new ArrayList<ExpImportMaster>();
            List<ExpImportDetail> importDetailInserted = new ArrayList<ExpImportDetail>();

            ExpImportMaster importMaster;
            ExpImportDetail importDetail;
            boolean masterImportFlag = false;
            //生成入库单号
            String documentNo="";
            short i = 0;
            Double count = 0.00;    //计算总金额
            for (ExpInventoryCheck dict : rows) {
                i++;
                //生成一次入库master
                if (!masterImportFlag) {
                    ExpSubStorageDict subStorage= expSubStorageDictFacade.getSubStorage(dict.getStorage(), dict.getSubStorage(), dict.getHospitalId());
                    if (subStorage.getImportNoPrefix().length() <= 4) {
                        documentNo = subStorage.getImportNoPrefix() + "000000".substring((subStorage.getImportNoAva() + "").length()) + subStorage.getImportNoAva();
                    } else if (subStorage.getImportNoPrefix().length() == 5) {
                        documentNo = subStorage.getImportNoPrefix() + "00000".substring((subStorage.getImportNoAva() + "").length()) + subStorage.getImportNoAva();
                    } else if (subStorage.getImportNoPrefix().length() == 6) {
                        documentNo = subStorage.getImportNoPrefix() + "0000".substring((subStorage.getImportNoAva() + "").length()) + subStorage.getImportNoAva();
                    }
                    importMaster = new ExpImportMaster();
                    importMaster.setDocumentNo(documentNo);
                    importMaster.setStorage(dict.getStorage());
                    importMaster.setImportDate(dict.getCheckYearMonth());
                    //importMaster.setSupplier();
                    //importMaster.setAccountReceivable(dict.getQuantity()*dict.getPurchasePrice());
                    importMaster.setAccountPayed(0.0);
                    importMaster.setAdditionalFee(0.0);
                    importMaster.setImportClass("盘盈入库");
                    importMaster.setSubStorage(dict.getSubStorage());
                    importMaster.setAccountIndicator(1);
                    //importMaster.setMemos("");
                    //importMaster.setOperator();
                    importMaster.setDocStatus("0");
                    //importMaster.setTenderNo("");
                    //importMaster.setTenderType("");
                    importMaster.setHospitalId(dict.getHospitalId());

                    masterImportFlag = true;
                    importMasterInserted.add(importMaster);
                    expImportMasterBeanChangeVo.setInserted(importMasterInserted);
                }

                //生成多条入库记录
                importDetail = new ExpImportDetail();
                importDetail.setDocumentNo(documentNo);
                importDetail.setItemNo(i);
                importDetail.setExpCode(dict.getExpCode());
                importDetail.setExpSpec(dict.getExpSpec());
                importDetail.setUnits(dict.getUnits());
                importDetail.setBatchNo(dict.getBatchNo());
                importDetail.setExpireDate(dict.getCheckYearMonth());
                importDetail.setFirmId(dict.getFirmId());
                importDetail.setExpForm(dict.getExpForm());
                importDetail.setPurchasePrice(dict.getPurchasePrice());
                importDetail.setTradePrice(dict.getTradePrice());
                importDetail.setRetailPrice(dict.getRetailPrice());
                //importDetail.setDiscount();
                importDetail.setPackageSpec(dict.getMinSpec());
                importDetail.setQuantity(dict.getQuantity());
                importDetail.setPackageUnits(dict.getMinUnits());
                //importDetail.setSubPackage1();
                //importDetail.setSubPackageUnits1();
                //importDetail.setSubPackageSpec1();
                //importDetail.setSubPackage2();
                //importDetail.setSubPackageUnits2();
                //importDetail.setSubPackageUnits2();
                //importDetail.setInvoiceNo();
                //importDetail.setInvoiceDate();
                //importDetail.setDisburseCount();
                //importDetail.setDisburseRecNo();
                importDetail.setInventory(dict.getActualQuantity());
                //importDetail.setMemo();
                //importDetail.setRegistno();
                //importDetail.setLicenceno();
                //importDetail.setProducedate();
                //importDetail.setDisinfectdate();
                //importDetail.setKillflag();
                //importDetail.setOrderBatch();
                //importDetail.setTenderNo();
                importDetail.setHospitalId(dict.getHospitalId());
                importDetailInserted.add(importDetail);
            }
            expImportDetailBeanChangeVo.setInserted(importDetailInserted);

            importVo.setExpImportDetailBeanChangeVo(expImportDetailBeanChangeVo);
            importVo.setExpImportMasterBeanChangeVo(expImportMasterBeanChangeVo);

            Integer level = expStorageDeptFacade.findLevelByStorageCode(rows.get(0).getStorage(), rows.get(0).getHospitalId());//当前库房级别
            for (ExpInventoryCheck row : rows) {
                if(level == 1){
                    count += row.getQuantity() * row.getPurchasePrice(); //一级库房，总金额  进价 * 数量
                }else{
                    count += row.getQuantity() * row.getRetailPrice();  //不是一级库房，总金额  售价 * 数量
                }
            }
            importVo.getExpImportMasterBeanChangeVo().getInserted().get(0).setAccountReceivable(count);     //设置主表总金额
        }

        return importVo;
    }
    //根据盘点记录生成出库数据
    public ExpExportManageVo generateExport(List<ExpInventoryCheck> rows){
        ExpExportManageVo exportVo = new ExpExportManageVo();
        if (rows.size() > 0) {
            BeanChangeVo<ExpExportMaster> expExportMasterBeanChangeVo = new BeanChangeVo<ExpExportMaster>();
            BeanChangeVo<ExpExportDetail> expExportDetailBeanChangeVo = new BeanChangeVo<ExpExportDetail>();
            List<ExpExportMaster> expExportMasterInserted = new ArrayList<ExpExportMaster>();
            List<ExpExportDetail> expImportDetailInserted = new ArrayList<ExpExportDetail>();
            ExpExportMaster exportMaster;
            ExpExportDetail exportDetail;
            boolean masterExportFlag = false;
            String documentNo = "";
            short i = 0;
            Double count = 0.00;    //计算总金额
            for (ExpInventoryCheck dict : rows) {
                i++;
                //生成一次出库master
                if (!masterExportFlag) {
                    ExpSubStorageDict subStorage = expSubStorageDictFacade.getSubStorage(dict.getStorage(), dict.getSubStorage(), dict.getHospitalId());
                    if (subStorage.getExportNoPrefix().length() <= 4) {
                        documentNo = subStorage.getExportNoPrefix() + "000000".substring((subStorage.getExportNoAva() + "").length()) + subStorage.getExportNoAva();
                    } else if (subStorage.getExportNoPrefix().length() == 5) {
                        documentNo = subStorage.getExportNoPrefix() + "00000".substring((subStorage.getExportNoAva() + "").length()) + subStorage.getExportNoAva();
                    } else if (subStorage.getExportNoPrefix().length() == 6) {
                        documentNo = subStorage.getExportNoPrefix() + "0000".substring((subStorage.getExportNoAva() + "").length()) + subStorage.getExportNoAva();
                    }
                    exportMaster = new ExpExportMaster();
                    exportMaster.setDocumentNo(documentNo);
                    exportMaster.setStorage(dict.getStorage());
                    exportMaster.setExportClass("盘亏出库");
                    exportMaster.setExportDate(dict.getCheckYearMonth());
                    exportMaster.setReceiver(dict.getStorage());
                    //exportMaster.setAccountReceivable(-dict.getQuantity()*dict.getPurchasePrice());
                    exportMaster.setAccountPayed(0.0);
                    exportMaster.setAdditionalFee(0.0);
                    exportMaster.setSubStorage(dict.getSubStorage());
                    exportMaster.setAccountIndicator(true);
                    //exportMaster.setMemos();
                    //exportMaster.setFundItem();
                    //exportMaster.setOperator();
                    exportMaster.setDocStatus("0");
                    exportMaster.setHospitalId(dict.getHospitalId());
                    masterExportFlag = true;

                    expExportMasterInserted.add(exportMaster);
                    expExportMasterBeanChangeVo.setInserted(expExportMasterInserted);
                    exportVo.setExpExportMasterBeanChangeVo(expExportMasterBeanChangeVo);
                }

                //生成多条出库detail
                exportDetail = new ExpExportDetail();
                exportDetail.setDocumentNo(documentNo);
                //exportDetail.setItemNo();
                exportDetail.setExpCode(dict.getExpCode());
                exportDetail.setExpSpec(dict.getExpSpec());
                exportDetail.setUnits(dict.getUnits());
                exportDetail.setBatchNo(dict.getBatchNo());
                exportDetail.setExpireDate(dict.getCheckYearMonth());
                exportDetail.setFirmId(dict.getFirmId());
                exportDetail.setExpForm(dict.getExpForm());
                //exportDetail.setImportDocumentNo();
                exportDetail.setPurchasePrice(dict.getPurchasePrice());
                exportDetail.setTradePrice(dict.getTradePrice());
                exportDetail.setRetailPrice(dict.getRetailPrice());
                exportDetail.setPackageSpec(dict.getMinSpec());
                exportDetail.setQuantity(-dict.getQuantity());
                exportDetail.setPackageUnits(dict.getMinUnits());
                ////exportDetail.setSubPackage1();
                ////exportDetail.setSubPackageSpec1();
                ////exportDetail.setSubPackageUnits1();
                ////exportDetail.setSubPackage2();
                ////exportDetail.setSubPackageSpec2();
                ////exportDetail.setSubPackageUnits2();
                exportDetail.setInventory(dict.getActualQuantity());
                //exportDetail.setProducedate();
                //exportDetail.setDisinfectdate();
                //exportDetail.setKillflag();
                //exportDetail.setRecFlag();
                //exportDetail.setAssignCode();
                //exportDetail.setBigCode();
                //exportDetail.setBigFirmId();
                //exportDetail.setBigSpec();
                //exportDetail.setMemo();
                exportDetail.setExpSgtp("1");
                exportDetail.setHospitalId(dict.getHospitalId());

                expImportDetailInserted.add(exportDetail);
            }
            Integer level = expStorageDeptFacade.findLevelByStorageCode(rows.get(0).getStorage(), rows.get(0).getHospitalId());//当前库房级别
            for (ExpInventoryCheck row : rows) {
                if (level == 1) {
                    count += -row.getQuantity() * row.getPurchasePrice(); //一级库房，总金额  进价 * 数量
                } else {
                    count += -row.getQuantity() * row.getRetailPrice();  //不是一级库房，总金额  售价 * 数量
                }
            }
            exportVo.getExpExportMasterBeanChangeVo().getInserted().get(0).setAccountReceivable(count);     //设置盘亏出库主表总金额

            expExportDetailBeanChangeVo.setInserted(expImportDetailInserted);
            exportVo.setExpExportDetailBeanChangeVo(expExportDetailBeanChangeVo);
        }
        return exportVo;
    }
    @Transactional
    public List<ExpInventoryCheck> save(List<ExpInventoryCheck> rows) throws Exception{
        List<ExpInventoryCheck> newUpdateDict = new ArrayList<>();
        List<ExpInventoryCheck> exportCheckDict = new ArrayList<>();
        List<ExpInventoryCheck> importCheckDict = new ArrayList<>();
        List<ExpInventoryCheck> saveCheckDict = new ArrayList<>();

        if (rows.size() > 0) {
            for (ExpInventoryCheck dict : rows) {
                //最后一行统计信息不进行数据库操作
                if(null != dict.getExpCode() && !dict.getExpCode().trim().equals("")){
                    java.sql.Timestamp d = new java.sql.Timestamp(dict.getCheckYearMonth().getTime());
                    dict.setCheckYearMonth(d);
                    //状态为保存的盘点数据
                    if (dict.getRecStatus().intValue() == 1) {

                        if (dict.getAccountQuantity() > dict.getActualQuantity()) {
                            //账面数>实盘数，出库
                            exportCheckDict.add(dict);
                        }else if (dict.getAccountQuantity() < dict.getActualQuantity()) {
                            //账面数<实盘数，入库
                            importCheckDict.add(dict);
                        }
                            //账面数=实盘数，只保存盘点数据
                            saveCheckDict.add(dict);

                    }else{
                        newUpdateDict.add(merge(dict));
                    }
                }
            }
            if (importCheckDict.size() > 0) {
                //入库
                ExpImportVo importVo = generateImport(importCheckDict);
                expStockFacade.expImport(importVo);
                //for (ExpInventoryCheck dict : importCheckDict) {
                //    //设置账面数 = 实盘数
                //    dict.setAccountQuantity(dict.getActualQuantity());
                //    newUpdateDict.add(merge(dict));
                //}
            }
            if (exportCheckDict.size() > 0) {
                //出库
                ExpExportManageVo exportVo = generateExport(exportCheckDict);
                expStockFacade.expExportManage(exportVo);
                //逐条保存盘点信息
                //for (ExpInventoryCheck dict : exportCheckDict) {
                //    //设置账面数 = 实盘数
                //    dict.setAccountQuantity(dict.getActualQuantity());
                //    newUpdateDict.add(merge(dict));
                //}
            }
            if (saveCheckDict.size() > 0) {
                for (ExpInventoryCheck dict : saveCheckDict) {
                    newUpdateDict.add(merge(dict));
                }
            }
        }

        return newUpdateDict;
    }
}
