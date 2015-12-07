package com.jims.his.domain.ieqm.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 消耗品出入库的时候选择不同的厂商信息等
 *
 * Created by heren on 2015/10/20.
 */
public class ExpStockRecord implements Serializable {

    private String expName ;
    private String expCode ;
    private String expSpec ;
    private String units ;
    private String minSpec ;
    private String minUnits ;
    private String firmId ;
    private Double purchasePrice ;
    private Double tradePrice ;
    private Double retailPrice ;
    private Double quantity  ;
    private String registerNo ;
    private String permitNo ;
    private String batchNo;
    private Date disinfectdate;
    private Date producedate;
    private Date expireDate;
    private String documentNo;
    private String singleGroupIndicator;
    private String expForm;
    private Integer killflag;
    private String subPackageUnits1;
    private Double subPackage1;
    private String subPackageSpec1;
    private String subPackageUnits2;
    private Double subPackage2;
    private String subPackageSpec2;
    private Double discount;
    private Long indicator;


    public ExpStockRecord(String expName, String expCode, String expSpec, String units, String minSpec, String minUnits, String firmId, Double purchasePrice, Double tradePrice, Double retailPrice, Double quantity, String registerNo, String permitNo, String batchNo, Date disinfectdate, Date producedate, Date expireDate, String documentNo, String singleGroupIndicator, String expForm, Integer killflag, String subPackageUnits1, Double subPackage1, String subPackageSpec1, String subPackageUnits2, Double subPackage2, String subPackageSpec2, Double discount, Long indicator) {
        this.expName = expName;
        this.expCode = expCode;
        this.expSpec = expSpec;
        this.units = units;
        this.minSpec = minSpec;
        this.minUnits = minUnits;
        this.firmId = firmId;
        this.purchasePrice = purchasePrice;
        this.tradePrice = tradePrice;
        this.retailPrice = retailPrice;
        this.quantity = quantity;
        this.registerNo = registerNo;
        this.permitNo = permitNo;
        this.batchNo = batchNo;
        this.disinfectdate = disinfectdate;
        this.producedate = producedate;
        this.expireDate = expireDate;
        this.documentNo = documentNo;
        this.singleGroupIndicator = singleGroupIndicator;
        this.expForm = expForm;
        this.killflag = killflag;
        this.subPackageUnits1 = subPackageUnits1;
        this.subPackage1 = subPackage1;
        this.subPackageSpec1 = subPackageSpec1;
        this.subPackageUnits2 = subPackageUnits2;
        this.subPackage2 = subPackage2;
        this.subPackageSpec2 = subPackageSpec2;
        this.discount = discount;
        this.indicator = indicator;
    }

    public ExpStockRecord() {

    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
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

    public String getMinSpec() {
        return minSpec;
    }

    public void setMinSpec(String minSpec) {
        this.minSpec = minSpec;
    }

    public String getMinUnits() {
        return minUnits;
    }

    public void setMinUnits(String minUnits) {
        this.minUnits = minUnits;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getPermitNo() {
        return permitNo;
    }

    public void setPermitNo(String permitNo) {
        this.permitNo = permitNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getDisinfectdate() {
        return disinfectdate;
    }

    public void setDisinfectdate(Date disinfectdate) {
        this.disinfectdate = disinfectdate;
    }

    public Date getProducedate() {
        return producedate;
    }

    public void setProducedate(Date producedate) {
        this.producedate = producedate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getSingleGroupIndicator() {
        return singleGroupIndicator;
    }

    public void setSingleGroupIndicator(String singleGroupIndicator) {
        this.singleGroupIndicator = singleGroupIndicator;
    }

    public String getExpForm() {
        return expForm;
    }

    public void setExpForm(String expForm) {
        this.expForm = expForm;
    }

    public Integer getKillflag() {
        return killflag;
    }

    public void setKillflag(Integer killflag) {
        this.killflag = killflag;
    }

    public String getSubPackageUnits1() {
        return subPackageUnits1;
    }

    public void setSubPackageUnits1(String subPackageUnits1) {
        this.subPackageUnits1 = subPackageUnits1;
    }

    public Double getSubPackage1() {
        return subPackage1;
    }

    public void setSubPackage1(Double subPackage1) {
        this.subPackage1 = subPackage1;
    }

    public String getSubPackageSpec1() {
        return subPackageSpec1;
    }

    public void setSubPackageSpec1(String subPackageSpec1) {
        this.subPackageSpec1 = subPackageSpec1;
    }

    public String getSubPackageUnits2() {
        return subPackageUnits2;
    }

    public void setSubPackageUnits2(String subPackageUnits2) {
        this.subPackageUnits2 = subPackageUnits2;
    }

    public Double getSubPackage2() {
        return subPackage2;
    }

    public void setSubPackage2(Double subPackage2) {
        this.subPackage2 = subPackage2;
    }

    public String getSubPackageSpec2() {
        return subPackageSpec2;
    }

    public void setSubPackageSpec2(String subPackageSpec2) {
        this.subPackageSpec2 = subPackageSpec2;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Long getIndicator() {
        return indicator;
    }

    public void setIndicator(Long indicator) {
        this.indicator = indicator;
    }
}

