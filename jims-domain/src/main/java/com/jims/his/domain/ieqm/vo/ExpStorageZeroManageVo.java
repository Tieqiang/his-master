package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wbb on 2015/10/20.
 * 零库存管理，基于表：EXP_STOCK,EXP_DICT
 */
@XmlRootElement
public class ExpStorageZeroManageVo implements Serializable {
    private String id;
    private String storage;
    private String batchNo;
    private Date expireDate;
    private String firmId;
    private Double purchasePrice;
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
    private String subStorage;
    private String documentNo;
    private Integer supplyIndicator;
    private Date producedate;
    private Date disinfectdate;
    private Boolean killflag;
    private String expCode;
    private String expName;
    private String expSpec;
    private String units;
    private String expForm;
    private String toxiProperty;
    private Double dosePerUnit;
    private String doseUnits;
    private String inputCode;
    private Boolean expIndicator;
    private String storageCode;
    private String singleGroupIndicator;

    public ExpStorageZeroManageVo() {
    }

    public ExpStorageZeroManageVo(String id, String storage, String batchNo, Date expireDate, String firmId, Double purchasePrice, Double discount, String packageSpec, Double quantity, String packageUnits, Double subPackage1, String subPackageUnits1, String subPackageSpec1, Double subPackage2, String subPackageUnits2, String subPackageSpec2, String subStorage, String documentNo, Integer supplyIndicator, Date producedate, Date disinfectdate, Boolean killflag, String expCode, String expName, String expSpec, String units, String expForm, String toxiProperty, Double dosePerUnit, String doseUnits, String inputCode, Boolean expIndicator, String storageCode, String singleGroupIndicator) {
        this.id = id;
        this.storage = storage;
        this.batchNo = batchNo;
        this.expireDate = expireDate;
        this.firmId = firmId;
        this.purchasePrice = purchasePrice;
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
        this.subStorage = subStorage;
        this.documentNo = documentNo;
        this.supplyIndicator = supplyIndicator;
        this.producedate = producedate;
        this.disinfectdate = disinfectdate;
        this.killflag = killflag;
        this.expCode = expCode;
        this.expName = expName;
        this.expSpec = expSpec;
        this.units = units;
        this.expForm = expForm;
        this.toxiProperty = toxiProperty;
        this.dosePerUnit = dosePerUnit;
        this.doseUnits = doseUnits;
        this.inputCode = inputCode;
        this.expIndicator = expIndicator;
        this.storageCode = storageCode;
        this.singleGroupIndicator = singleGroupIndicator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
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

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
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

    public String getSubStorage() {
        return subStorage;
    }

    public void setSubStorage(String subStorage) {
        this.subStorage = subStorage;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public Integer getSupplyIndicator() {
        return supplyIndicator;
    }

    public void setSupplyIndicator(Integer supplyIndicator) {
        this.supplyIndicator = supplyIndicator;
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

    public Boolean getKillflag() {
        return killflag;
    }

    public void setKillflag(Boolean killflag) {
        this.killflag = killflag;
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

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    public Boolean getExpIndicator() {
        return expIndicator;
    }

    public void setExpIndicator(Boolean expIndicator) {
        this.expIndicator = expIndicator;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getSingleGroupIndicator() {
        return singleGroupIndicator;
    }

    public void setSingleGroupIndicator(String singleGroupIndicator) {
        this.singleGroupIndicator = singleGroupIndicator;
    }
}
