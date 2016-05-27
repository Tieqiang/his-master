package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangbinbin on 2015/10/22.
 * 消耗品入库单据查询：在查询入库明细的时候在exp_import_detail表的基础上咱家expName、supplier字段联合查询Vo
 * 2015/10/26 添加出库单据查询中的 zeroAccount receiver exportClass exportDate 等字段
 * 2015/11/03 添加 storage  importDate importClass accountIndicator 字段 用于 产品上账查询使用
 */
@XmlRootElement
public class ExpImportDetailVo implements Serializable {
    private String id;
    private String detailId;
    private String storage;
    private String storageName;
    private Integer accountIndicator;
    private String documentNo;
    private Short itemNo;
    private String expCode;
    private String expSpec;
    private String units;
    private String batchNo;
    private Date expireDate;
    private String firmId;
    private String expForm;
    private Double purchasePrice;
    private Double tradePrice;
    private Double retailPrice;
    private Double discount;
    private String packageSpec;
    private Double quantity;
    private String packageUnits;
    private Double subPackage1;
    private String subPackageUnits1;
    private String subPackageSpec1;
    private Double subPackage2;
    private String subPackageUnits2;
    private String subPackageSpec2;
    private String invoiceNo;
    private Date invoiceDate;
    private String disburseRecNo;
    private Double disburseCount;
    private Double inventory;
    private String memo;
    private String registno;
    private String licenceno;
    private Date producedate;
    private Date disinfectdate;
    private Integer killflag;
    private Integer tallyFlag;
    private Date tallyDate;
    private String tallyOpertor;
    private String orderBatch;
    private Short tenderNo;
    private String hospitalId ;
    private String expName;
    private String supplier;
    private Integer accountReceivable;
    private Integer zeroAccount;
    private String receiver;
    private String exportClass;
    private Date exportDate;
    private Date importDate;
    private String importClass;
    private Double payAmount;
    private Double sum;
    private String subStorage;
    private Double purchaseAmount;
    //添加下面8个字段 用于单品总账   如出库联合查询
    private String way; //方向
    private String ioClass;    //如出库类型
    private String ourName;    //科室
    private Double importNum ; //入库数量
    private Double importPrice; //入库金额
    private Double exportNum;   //出库数量
    private Double exportPrice; //出库金额
    private Date actionDate;    //如出库日期


    public ExpImportDetailVo() {
    }

    public ExpImportDetailVo(String id, String detailId, String storage,String storageName, Integer accountIndicator, String documentNo, Short itemNo, String expCode, String expSpec, String units, String batchNo, Date expireDate, String firmId, String expForm, Double purchasePrice, Double tradePrice, Double retailPrice, Double discount, String packageSpec, Double quantity, String packageUnits, Double subPackage1, String subPackageUnits1, String subPackageSpec1, Double subPackage2, String subPackageUnits2, String subPackageSpec2, String invoiceNo, Date invoiceDate, String disburseRecNo, Double disburseCount, Double inventory, String memo, String registno, String licenceno, Date producedate, Date disinfectdate, Integer killflag, Integer tallyFlag, Date tallyDate, String tallyOpertor, String orderBatch, Short tenderNo, String hospitalId, String expName, String supplier, Integer accountReceivable, Integer zeroAccount, String receiver, String exportClass, Date exportDate, Date importDate, String importClass, Double payAmount, Double sum, String subStorage, Double purchaseAmount, String way, String ioClass, String ourName, Double importNum, Double importPrice, Double exportNum, Double exportPrice, Date actionDate) {
        this.id = id;
        this.detailId = detailId;
        this.storage = storage;
        this.storageName=storageName;
        this.accountIndicator = accountIndicator;
        this.documentNo = documentNo;
        this.itemNo = itemNo;
        this.expCode = expCode;
        this.expSpec = expSpec;
        this.units = units;
        this.batchNo = batchNo;
        this.expireDate = expireDate;
        this.firmId = firmId;
        this.expForm = expForm;
        this.purchasePrice = purchasePrice;
        this.tradePrice = tradePrice;
        this.retailPrice = retailPrice;
        this.discount = discount;
        this.packageSpec = packageSpec;
        this.quantity = quantity;
        this.packageUnits = packageUnits;
        this.subPackage1 = subPackage1;
        this.subPackageUnits1 = subPackageUnits1;
        this.subPackageSpec1 = subPackageSpec1;
        this.subPackage2 = subPackage2;
        this.subPackageUnits2 = subPackageUnits2;
        this.subPackageSpec2 = subPackageSpec2;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.disburseRecNo = disburseRecNo;
        this.disburseCount = disburseCount;
        this.inventory = inventory;
        this.memo = memo;
        this.registno = registno;
        this.licenceno = licenceno;
        this.producedate = producedate;
        this.disinfectdate = disinfectdate;
        this.killflag = killflag;
        this.tallyFlag = tallyFlag;
        this.tallyDate = tallyDate;
        this.tallyOpertor = tallyOpertor;
        this.orderBatch = orderBatch;
        this.tenderNo = tenderNo;
        this.hospitalId = hospitalId;
        this.expName = expName;
        this.supplier = supplier;
        this.accountReceivable = accountReceivable;
        this.zeroAccount = zeroAccount;
        this.receiver = receiver;
        this.exportClass = exportClass;
        this.exportDate = exportDate;
        this.importDate = importDate;
        this.importClass = importClass;
        this.payAmount = payAmount;
        this.sum = sum;
        this.subStorage = subStorage;
        this.purchaseAmount = purchaseAmount;
        this.way = way;
        this.ioClass = ioClass;
        this.ourName = ourName;
        this.importNum = importNum;
        this.importPrice = importPrice;
        this.exportNum = exportNum;
        this.exportPrice = exportPrice;
        this.actionDate = actionDate;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getIoClass() {
        return ioClass;
    }

    public void setIoClass(String ioClass) {
        this.ioClass = ioClass;
    }

    public String getOurName() {
        return ourName;
    }

    public void setOurName(String ourName) {
        this.ourName = ourName;
    }

    public Double getImportNum() {
        return importNum;
    }

    public void setImportNum(Double importNum) {
        this.importNum = importNum;
    }

    public Double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(Double importPrice) {
        this.importPrice = importPrice;
    }

    public Double getExportNum() {
        return exportNum;
    }

    public void setExportNum(Double exportNum) {
        this.exportNum = exportNum;
    }

    public Double getExportPrice() {
        return exportPrice;
    }

    public void setExportPrice(Double exportPrice) {
        this.exportPrice = exportPrice;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public Integer getAccountIndicator() {
        return accountIndicator;
    }

    public void setAccountIndicator(Integer accountIndicator) {
        this.accountIndicator = accountIndicator;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Date getImportDate() {
        return importDate;
    }

    public String getImportClass() {
        return importClass;
    }

    public void setImportClass(String importClass) {
        this.importClass = importClass;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public Integer getZeroAccount() {
        return zeroAccount;
    }

    public void setZeroAccount(Integer zeroAccount) {
        this.zeroAccount = zeroAccount;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getExportClass() {
        return exportClass;
    }

    public void setExportClass(String exportClass) {
        this.exportClass = exportClass;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public Integer getAccountReceivable() {
        return accountReceivable;
    }

    public void setAccountReceivable(Integer accountReceivable) {
        this.accountReceivable = accountReceivable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public Short getItemNo() {
        return itemNo;
    }

    public void setItemNo(Short itemNo) {
        this.itemNo = itemNo;
    }

    public String getExpCode() {
        return expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    public String getExpSpec() {
        return expSpec;
    }

    public void setExpSpec(String expSpec) {
        this.expSpec = expSpec;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public String getExpForm() {
        return expForm;
    }

    public void setExpForm(String expForm) {
        this.expForm = expForm;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(Double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getPackageSpec() {
        return packageSpec;
    }

    public void setPackageSpec(String packageSpec) {
        this.packageSpec = packageSpec;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getPackageUnits() {
        return packageUnits;
    }

    public void setPackageUnits(String packageUnits) {
        this.packageUnits = packageUnits;
    }

    public Double getSubPackage1() {
        return subPackage1;
    }

    public void setSubPackage1(Double subPackage1) {
        this.subPackage1 = subPackage1;
    }

    public String getSubPackageUnits1() {
        return subPackageUnits1;
    }

    public void setSubPackageUnits1(String subPackageUnits1) {
        this.subPackageUnits1 = subPackageUnits1;
    }

    public String getSubPackageSpec1() {
        return subPackageSpec1;
    }

    public void setSubPackageSpec1(String subPackageSpec1) {
        this.subPackageSpec1 = subPackageSpec1;
    }

    public Double getSubPackage2() {
        return subPackage2;
    }

    public void setSubPackage2(Double subPackage2) {
        this.subPackage2 = subPackage2;
    }

    public String getSubPackageUnits2() {
        return subPackageUnits2;
    }

    public void setSubPackageUnits2(String subPackageUnits2) {
        this.subPackageUnits2 = subPackageUnits2;
    }

    public String getSubPackageSpec2() {
        return subPackageSpec2;
    }

    public void setSubPackageSpec2(String subPackageSpec2) {
        this.subPackageSpec2 = subPackageSpec2;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDisburseRecNo() {
        return disburseRecNo;
    }

    public void setDisburseRecNo(String disburseRecNo) {
        this.disburseRecNo = disburseRecNo;
    }

    public Double getDisburseCount() {
        return disburseCount;
    }

    public void setDisburseCount(Double disburseCount) {
        this.disburseCount = disburseCount;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRegistno() {
        return registno;
    }

    public void setRegistno(String registno) {
        this.registno = registno;
    }

    public String getLicenceno() {
        return licenceno;
    }

    public void setLicenceno(String licenceno) {
        this.licenceno = licenceno;
    }

    public Date getProducedate() {
        return producedate;
    }

    public void setProducedate(Date producedate) {
        this.producedate = producedate;
    }

    public Date getDisinfectdate() {
        return disinfectdate;
    }

    public void setDisinfectdate(Date disinfectdate) {
        this.disinfectdate = disinfectdate;
    }

    public Integer getKillflag() {
        return killflag;
    }

    public void setKillflag(Integer killflag) {
        this.killflag = killflag;
    }

    public Integer getTallyFlag() {
        return tallyFlag;
    }

    public void setTallyFlag(Integer tallyFlag) {
        this.tallyFlag = tallyFlag;
    }

    public Date getTallyDate() {
        return tallyDate;
    }

    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }

    public String getTallyOpertor() {
        return tallyOpertor;
    }

    public void setTallyOpertor(String tallyOpertor) {
        this.tallyOpertor = tallyOpertor;
    }

    public String getOrderBatch() {
        return orderBatch;
    }

    public void setOrderBatch(String orderBatch) {
        this.orderBatch = orderBatch;
    }

    public Short getTenderNo() {
        return tenderNo;
    }

    public void setTenderNo(Short tenderNo) {
        this.tenderNo = tenderNo;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getSubStorage() {
        return subStorage;
    }

    public void setSubStorage(String subStorage) {
        this.subStorage = subStorage;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }
}
