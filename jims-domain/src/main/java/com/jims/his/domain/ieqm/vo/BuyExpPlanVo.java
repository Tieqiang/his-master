package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 代购计划生成：列出页面左侧所有待采购物品的信息
 * Created by wangjing on 2015/10/23.
 */
@XmlRootElement
public class BuyExpPlanVo implements Serializable {

    private Double purchase;
    private Double quantity;
    private Double retailPrice;
    private Double planNumber;

    private String buyId;
    private Short buyNo;
    private String expCode;
    private String expName;
    private String expSpec;
    private String units;
    private String expForm;
    private String toxiProperty;
    private Double dosePerUnit;
    private String doseUnits;
    private Boolean expIndicator;
    private String inputCode;
    private Double wantNumber;
    private String storer;
    private Double stockNumber;
    private String stockSupplier;
    private String buyer;
    private Double checkNumber;
    private String checkSupplier;
    private String checker;
    private Integer flag;
    private String packSpec;
    private String packUnit;
    private String firmId;
    private Double purchasePrice;
    private String storage;
    private Double stockquantityRef;
    private Double exportquantityRef;

    public BuyExpPlanVo() {
    }

    public BuyExpPlanVo(Double purchase, Double quantity, Double retailPrice, Double planNumber, String buyId, Short buyNo, String expCode, String expName, String expSpec, String units, String expForm, String toxiProperty, Double dosePerUnit, String doseUnits, Boolean expIndicator, String inputCode, Double wantNumber, String storer, Double stockNumber, String stockSupplier, String buyer, Double checkNumber, String checkSupplier, String checker, Integer flag, String packSpec, String packUnit, String firmId, Double purchasePrice, String storage, Double stockquantityRef, Double exportquantityRef) {
        this.purchase = purchase;
        this.quantity = quantity;
        this.retailPrice = retailPrice;
        this.planNumber = planNumber;
        this.buyId = buyId;
        this.buyNo = buyNo;
        this.expCode = expCode;
        this.expName = expName;
        this.expSpec = expSpec;
        this.units = units;
        this.expForm = expForm;
        this.toxiProperty = toxiProperty;
        this.dosePerUnit = dosePerUnit;
        this.doseUnits = doseUnits;
        this.expIndicator = expIndicator;
        this.inputCode = inputCode;
        this.wantNumber = wantNumber;
        this.storer = storer;
        this.stockNumber = stockNumber;
        this.stockSupplier = stockSupplier;
        this.buyer = buyer;
        this.checkNumber = checkNumber;
        this.checkSupplier = checkSupplier;
        this.checker = checker;
        this.flag = flag;
        this.packSpec = packSpec;
        this.packUnit = packUnit;
        this.firmId = firmId;
        this.purchasePrice = purchasePrice;
        this.storage = storage;
        this.stockquantityRef = stockquantityRef;
        this.exportquantityRef = exportquantityRef;
    }

    public Double getPurchase() {
        return purchase;
    }

    public void setPurchase(Double purchase) {
        this.purchase = purchase;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(Double planNumber) {
        this.planNumber = planNumber;
    }

    public String getBuyId() {
        return buyId;
    }

    public void setBuyId(String buyId) {
        this.buyId = buyId;
    }

    public Short getBuyNo() {
        return buyNo;
    }

    public void setBuyNo(Short buyNo) {
        this.buyNo = buyNo;
    }

    public String getExpCode() {
        return expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
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

    public String getExpForm() {
        return expForm;
    }

    public void setExpForm(String expForm) {
        this.expForm = expForm;
    }

    public String getToxiProperty() {
        return toxiProperty;
    }

    public void setToxiProperty(String toxiProperty) {
        this.toxiProperty = toxiProperty;
    }

    public Double getDosePerUnit() {
        return dosePerUnit;
    }

    public void setDosePerUnit(Double dosePerUnit) {
        this.dosePerUnit = dosePerUnit;
    }

    public String getDoseUnits() {
        return doseUnits;
    }

    public void setDoseUnits(String doseUnits) {
        this.doseUnits = doseUnits;
    }

    public Boolean getExpIndicator() {
        return expIndicator;
    }

    public void setExpIndicator(Boolean expIndicator) {
        this.expIndicator = expIndicator;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    public Double getWantNumber() {
        return wantNumber;
    }

    public void setWantNumber(Double wantNumber) {
        this.wantNumber = wantNumber;
    }

    public String getStorer() {
        return storer;
    }

    public void setStorer(String storer) {
        this.storer = storer;
    }

    public Double getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(Double stockNumber) {
        this.stockNumber = stockNumber;
    }

    public String getStockSupplier() {
        return stockSupplier;
    }

    public void setStockSupplier(String stockSupplier) {
        this.stockSupplier = stockSupplier;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Double getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(Double checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getCheckSupplier() {
        return checkSupplier;
    }

    public void setCheckSupplier(String checkSupplier) {
        this.checkSupplier = checkSupplier;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getPackSpec() {
        return packSpec;
    }

    public void setPackSpec(String packSpec) {
        this.packSpec = packSpec;
    }

    public String getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(String packUnit) {
        this.packUnit = packUnit;
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

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public Double getStockquantityRef() {
        return stockquantityRef;
    }

    public void setStockquantityRef(Double stockquantityRef) {
        this.stockquantityRef = stockquantityRef;
    }

    public Double getExportquantityRef() {
        return exportquantityRef;
    }

    public void setExportquantityRef(Double exportquantityRef) {
        this.exportquantityRef = exportquantityRef;
    }
}
