package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by wangbinbin on 2015/10/27.
 * 1、根据消耗量定库存量，基于exp_export_master ,EXP_PRICE_LIST,exp_export_detail,exp_dict，EXP_STORAGE_PROFILE查询
 * 主要是对EXP_STORAGE_PROFILE表的操作
 * 2、新增tradePrice、 retailPrice、purchasePrice 字段实现库存限量报警功能
 * 3、新增 firmId、quantity、batchNo 字段实现过期产品统计功能
 * 4、新增expForm 消耗品类别,packageSpec 包装规格,retailAllPrice 零售金额,tradeAllPrice 进货金额
 */
@XmlRootElement
public class ExpStorageProfileVo {
    private String id;
    private String storage;
    private String expName;
    private String storageCode;
    private String expCode;
    private String expSpec;
    private String units;
    private Integer amountPerPackage;
    private String packageUnits;
    private Integer upperLevel;
    private Integer lowLevel;
    private String location;
    private String supplier;
    private Integer stockQuantity;
    private Date expireDate ;
    private Double tradePrice;
    private Double retailPrice;
    private Double purchasePrice;
    private String batchNo;
    private String firmId;
    private Double quantity;
    private String expForm;
    private String packageSpec;
    private Double retailAllPrice;
    private Double tradeAllPrice;
    private String subStorage;


    public ExpStorageProfileVo() {
    }

    public ExpStorageProfileVo(String id, String storage, String expName, String storageCode, String expCode, String expSpec, String units, Integer amountPerPackage, String packageUnits, Integer upperLevel, Integer lowLevel, String location, String supplier, Integer stockQuantity, Date expireDate, Double tradePrice, Double retailPrice, Double purchasePrice, String batchNo, String firmId, Double quantity, String expForm, String packageSpec, Double retailAllPrice, Double tradeAllPrice, String subStorage) {
        this.id = id;
        this.storage = storage;
        this.expName = expName;
        this.storageCode = storageCode;
        this.expCode = expCode;
        this.expSpec = expSpec;
        this.units = units;
        this.amountPerPackage = amountPerPackage;
        this.packageUnits = packageUnits;
        this.upperLevel = upperLevel;
        this.lowLevel = lowLevel;
        this.location = location;
        this.supplier = supplier;
        this.stockQuantity = stockQuantity;
        this.expireDate = expireDate;
        this.tradePrice = tradePrice;
        this.retailPrice = retailPrice;
        this.purchasePrice = purchasePrice;
        this.batchNo = batchNo;
        this.firmId = firmId;
        this.quantity = quantity;
        this.expForm = expForm;
        this.packageSpec = packageSpec;
        this.retailAllPrice = retailAllPrice;
        this.tradeAllPrice = tradeAllPrice;
        this.subStorage = subStorage;
    }

    public String getSubStorage() {
        return subStorage;
    }

    public void setSubStorage(String subStorage) {
        this.subStorage = subStorage;
    }

    public String getExpForm() {
        return expForm;
    }

    public void setExpForm(String expForm) {
        this.expForm = expForm;
    }

    public String getPackageSpec() {
        return packageSpec;
    }

    public void setPackageSpec(String packageSpec) {
        this.packageSpec = packageSpec;
    }

    public Double getRetailAllPrice() {
        return retailAllPrice;
    }

    public void setRetailAllPrice(Double retailAllPrice) {
        this.retailAllPrice = retailAllPrice;
    }

    public Double getTradeAllPrice() {
        return tradeAllPrice;
    }

    public void setTradeAllPrice(Double tradeAllPrice) {
        this.tradeAllPrice = tradeAllPrice;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
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

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
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

    public Integer getAmountPerPackage() {
        return amountPerPackage;
    }

    public void setAmountPerPackage(Integer amountPerPackage) {
        this.amountPerPackage = amountPerPackage;
    }

    public String getPackageUnits() {
        return packageUnits;
    }

    public void setPackageUnits(String packageUnits) {
        this.packageUnits = packageUnits;
    }

    public Integer getUpperLevel() {
        return upperLevel;
    }

    public void setUpperLevel(Integer upperLevel) {
        this.upperLevel = upperLevel;
    }

    public Integer getLowLevel() {
        return lowLevel;
    }

    public void setLowLevel(Integer lowLevel) {
        this.lowLevel = lowLevel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
