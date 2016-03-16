package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * 用于批量入库的时候，出库单据VO
 * Created by heren on 2015/10/23.
 */
@XmlRootElement
public class ExpExportDetialVo {

    private String documentNo;
    private Short itemNo;
    private String orderBatch;
    private String tenderNo;
    private String expName ;
    private String expCode;
    private String expSpec;
    private String units;
    private String batchNo;
    private Date invoiceDate;
    private Date expireDate;
    private String firmId;
    private String expForm;
    private String importDocumentNo;
    private Double purchasePrice;
    private Double tradePrice;
    private Double retailPrice;
    private String packageSpec;
    private Double quantity;
    private String packageUnits;
    private Double subPackage1;
    private String subPackageUnits1;
    private String subPackageSpec1;
    private Double subPackage2;
    private String subPackageUnits2;
    private String subPackageSpec2;
    private Double inventory;
    private Double discount;
    private Date produceDate;
    private Date producedate;
    private Date disinfectdate;
    private Date disinfectDate;
    private Integer killflag;
    private Short recFlag;
    private String recOperator;
    private Date recDate;
    private String assignCode;
    private String bigCode;
    private String bigSpec;
    private String bigFirmId;
    private String expSgtp;
    private String memo;
    private String registNo ;
    private String licenceNo ;
    private String hospitalId ;
    private Integer expIndicator ;
    private Double amount ;
    private String deptAttr;
    private String receiver;
    private String subStorage;
    private Double payAmount;    //进价金额
    private Double retailAmount; //零售金额
    private String fundItem;
    private String assignName;
    private String expImportDetailRegistNo;
    private String expImportDetailLicenceno;
    private String invoiceNo;

    public ExpExportDetialVo(String documentNo, Short itemNo, String orderBatch, String tenderNo, String expName, String expCode, String expSpec, String units, String batchNo, Date invoiceDate, Date expireDate, String firmId, String expForm, String importDocumentNo, Double purchasePrice, Double tradePrice, Double retailPrice, String packageSpec, Double quantity, String packageUnits, Double subPackage1, String subPackageUnits1, String subPackageSpec1, Double subPackage2, String subPackageUnits2, String subPackageSpec2, Double inventory, Double discount, Date produceDate, Date producedate, Date disinfectdate, Date disinfectDate, Integer killflag, Short recFlag, String recOperator, Date recDate, String assignCode, String bigCode, String bigSpec, String bigFirmId, String expSgtp, String memo, String registNo, String licenceNo, String hospitalId, Integer expIndicator, Double amount, String deptAttr, String receiver, String subStorage, Double payAmount, Double retailAmount, String fundItem, String assignName, String expImportDetailRegistNo, String expImportDetailLicenceno, String invoiceNo) {
        this.documentNo = documentNo;
        this.itemNo = itemNo;
        this.orderBatch = orderBatch;
        this.tenderNo = tenderNo;
        this.expName = expName;
        this.expCode = expCode;
        this.expSpec = expSpec;
        this.units = units;
        this.batchNo = batchNo;
        this.invoiceDate = invoiceDate;
        this.expireDate = expireDate;
        this.firmId = firmId;
        this.expForm = expForm;
        this.importDocumentNo = importDocumentNo;
        this.purchasePrice = purchasePrice;
        this.tradePrice = tradePrice;
        this.retailPrice = retailPrice;
        this.packageSpec = packageSpec;
        this.quantity = quantity;
        this.packageUnits = packageUnits;
        this.subPackage1 = subPackage1;
        this.subPackageUnits1 = subPackageUnits1;
        this.subPackageSpec1 = subPackageSpec1;
        this.subPackage2 = subPackage2;
        this.subPackageUnits2 = subPackageUnits2;
        this.subPackageSpec2 = subPackageSpec2;
        this.inventory = inventory;
        this.discount = discount;
        this.produceDate = produceDate;
        this.producedate = producedate;
        this.disinfectdate = disinfectdate;
        this.disinfectDate = disinfectDate;
        this.killflag = killflag;
        this.recFlag = recFlag;
        this.recOperator = recOperator;
        this.recDate = recDate;
        this.assignCode = assignCode;
        this.bigCode = bigCode;
        this.bigSpec = bigSpec;
        this.bigFirmId = bigFirmId;
        this.expSgtp = expSgtp;
        this.memo = memo;
        this.registNo = registNo;
        this.licenceNo = licenceNo;
        this.hospitalId = hospitalId;
        this.expIndicator = expIndicator;
        this.amount = amount;
        this.deptAttr = deptAttr;
        this.receiver = receiver;
        this.subStorage = subStorage;
        this.payAmount = payAmount;
        this.retailAmount = retailAmount;
        this.fundItem = fundItem;
        this.assignName = assignName;
        this.expImportDetailRegistNo = expImportDetailRegistNo;
        this.expImportDetailLicenceno = expImportDetailLicenceno;
        this.invoiceNo = invoiceNo;
    }

    public ExpExportDetialVo() {
    }

    public String getFundItem() {
        return fundItem;
    }

    public void setFundItem(String fundItem) {
        this.fundItem = fundItem;
    }

    public String getAssignName() {
        return assignName;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Double getRetailAmount() {
        return retailAmount;
    }

    public void setRetailAmount(Double retailAmount) {
        this.retailAmount = retailAmount;
    }

    public String getSubStorage() {
        return subStorage;
    }

    public void setSubStorage(String subStorage) {
        this.subStorage = subStorage;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDeptAttr() {
        return deptAttr;
    }

    public void setDeptAttr(String deptAttr) {
        this.deptAttr = deptAttr;
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

    public String getImportDocumentNo() {
        return importDocumentNo;
    }

    public void setImportDocumentNo(String importDocumentNo) {
        this.importDocumentNo = importDocumentNo;
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

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
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

    public Short getRecFlag() {
        return recFlag;
    }

    public void setRecFlag(Short recFlag) {
        this.recFlag = recFlag;
    }

    public String getRecOperator() {
        return recOperator;
    }

    public void setRecOperator(String recOperator) {
        this.recOperator = recOperator;
    }

    public Date getRecDate() {
        return recDate;
    }

    public void setRecDate(Date recDate) {
        this.recDate = recDate;
    }

    public String getAssignCode() {
        return assignCode;
    }

    public void setAssignCode(String assignCode) {
        this.assignCode = assignCode;
    }

    public String getBigCode() {
        return bigCode;
    }

    public void setBigCode(String bigCode) {
        this.bigCode = bigCode;
    }

    public String getBigSpec() {
        return bigSpec;
    }

    public void setBigSpec(String bigSpec) {
        this.bigSpec = bigSpec;
    }

    public String getBigFirmId() {
        return bigFirmId;
    }

    public void setBigFirmId(String bigFirmId) {
        this.bigFirmId = bigFirmId;
    }

    public String getExpSgtp() {
        return expSgtp;
    }

    public void setExpSgtp(String expSgtp) {
        this.expSgtp = expSgtp;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRegistNo() {
        return registNo;
    }

    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public Integer getExpIndicator() {
        return expIndicator;
    }

    public void setExpIndicator(Integer expIndicator) {
        this.expIndicator = expIndicator;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getExpImportDetailRegistNo() {
        return expImportDetailRegistNo;
    }

    public void setExpImportDetailRegistNo(String expImportDetailRegistNo) {
        this.expImportDetailRegistNo = expImportDetailRegistNo;
    }

    public String getExpImportDetailLicenceno() {
        return expImportDetailLicenceno;
    }

    public void setExpImportDetailLicenceno(String expImportDetailLicenceno) {
        this.expImportDetailLicenceno = expImportDetailLicenceno;
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

    public Date getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(Date produceDate) {
        this.produceDate = produceDate;
    }

    public Date getProducedate() {
        return producedate;
    }

    public void setProducedate(Date producedate) {
        this.producedate = producedate;
    }

    public Date getDisinfectDate() {
        return disinfectDate;
    }

    public void setDisinfectDate(Date disinfectDate) {
        this.disinfectDate = disinfectDate;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getOrderBatch() {
        return orderBatch;
    }

    public void setOrderBatch(String orderBatch) {
        this.orderBatch = orderBatch;
    }

    public String getTenderNo() {
        return tenderNo;
    }

    public void setTenderNo(String tenderNo) {
        this.tenderNo = tenderNo;
    }
}
