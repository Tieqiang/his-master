package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2015/11/2.
 * 付款处理 用于付款信息查询
 */
@XmlRootElement
public class ExpDisburseRecVo implements Serializable {
    private String id;
    private String expCode;
    private String expSpec;
    private String units;
    private String batchNo;
    private Date expireDate;
    private String firmId;
    private String expForm;
    private String expName;
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
    private String disburseRecNo;
    private String documentNo;
    private Short itemNo;
    private Double disburseCount;
    private Double payAmount;
    private Double retailAmount;
    private Double tradeAmount;
    private String supplier;

    public ExpDisburseRecVo() {
    }

    public ExpDisburseRecVo(String id, String expCode, String expSpec, String units, String batchNo, Date expireDate, String firmId, String expForm, String expName, Double purchasePrice, Double tradePrice, Double retailPrice, Double discount, String packageSpec, Double quantity, String packageUnits, Double subPackage1, String subPackageUnits1, String subPackageSpec1, Double subPackage2, String subPackageUnits2, String subPackageSpec2, String invoiceNo, Date invoiceDate, Double inventory, String memo, String registno, String licenceno, Date producedate, Date disinfectdate, Integer killflag, Integer tallyFlag, Date tallyDate, String tallyOpertor, String orderBatch, Short tenderNo, String hospitalId, String disburseRecNo, String documentNo, Short itemNo, Double disburseCount, Double payAmount, Double retailAmount, Double tradeAmount, String supplier) {
        this.id = id;
        this.expCode = expCode;
        this.expSpec = expSpec;
        this.units = units;
        this.batchNo = batchNo;
        this.expireDate = expireDate;
        this.firmId = firmId;
        this.expForm = expForm;
        this.expName = expName;
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
        this.disburseRecNo = disburseRecNo;
        this.documentNo = documentNo;
        this.itemNo = itemNo;
        this.disburseCount = disburseCount;
        this.payAmount = payAmount;
        this.retailAmount = retailAmount;
        this.tradeAmount = tradeAmount;
        this.supplier = supplier;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
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

    public String getDisburseRecNo() {
        return disburseRecNo;
    }

    public void setDisburseRecNo(String disburseRecNo) {
        this.disburseRecNo = disburseRecNo;
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

    public Double getDisburseCount() {
        return disburseCount;
    }

    public void setDisburseCount(Double disburseCount) {
        this.disburseCount = disburseCount;
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

    public Double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

}
