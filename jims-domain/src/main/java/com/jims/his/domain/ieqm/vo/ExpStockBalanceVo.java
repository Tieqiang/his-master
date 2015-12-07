package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by wangjing on 2015/11/11
 */
@XmlRootElement
public class ExpStockBalanceVo implements java.io.Serializable {

	private String storage;
    private String hospitalId;
    private String expName;
	private String expCode;
    private String expSpec;
    private String packageSpec;
	private String packageUnits;
	private String expForm;
    private String firmId;
    private Double initialQuantity;
    private Double initialMoney;
    private Double importQuantity;
    private Double importMoney;
    private Double exportQuantity;
    private Double exportMoney;
    private Double inventory;
    private Double inventoryMoney;
    private Double profit;
    private Double realInitialMoney;
    private Double realImportMoney;
    private Double realExportMoney;
    private Double realInventoryMoney;
    private Double realProfit;


    public ExpStockBalanceVo() {
    }

    public ExpStockBalanceVo(String storage, String hospitalId, String expName, String expCode, String expSpec, String packageSpec, String packageUnits, String expForm, String firmId, Double initialQuantity, Double initialMoney, Double importQuantity, Double importMoney, Double exportQuantity, Double exportMoney, Double inventory, Double inventoryMoney, Double profit, Double realInitialMoney, Double realImportMoney, Double realExportMoney, Double realInventoryMoney, Double realProfit) {
        this.storage = storage;
        this.hospitalId = hospitalId;
        this.expName = expName;
        this.expCode = expCode;
        this.expSpec = expSpec;
        this.packageSpec = packageSpec;
        this.packageUnits = packageUnits;
        this.expForm = expForm;
        this.firmId = firmId;
        this.initialQuantity = initialQuantity;
        this.initialMoney = initialMoney;
        this.importQuantity = importQuantity;
        this.importMoney = importMoney;
        this.exportQuantity = exportQuantity;
        this.exportMoney = exportMoney;
        this.inventory = inventory;
        this.inventoryMoney = inventoryMoney;
        this.profit = profit;
        this.realInitialMoney = realInitialMoney;
        this.realImportMoney = realImportMoney;
        this.realExportMoney = realExportMoney;
        this.realInventoryMoney = realInventoryMoney;
        this.realProfit = realProfit;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
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

    public String getPackageSpec() {
        return packageSpec;
    }

    public void setPackageSpec(String packageSpec) {
        this.packageSpec = packageSpec;
    }

    public String getPackageUnits() {
        return packageUnits;
    }

    public void setPackageUnits(String packageUnits) {
        this.packageUnits = packageUnits;
    }

    public String getExpForm() {
        return expForm;
    }

    public void setExpForm(String expForm) {
        this.expForm = expForm;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public Double getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Double initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public Double getInitialMoney() {
        return initialMoney;
    }

    public void setInitialMoney(Double initialMoney) {
        this.initialMoney = initialMoney;
    }

    public Double getImportQuantity() {
        return importQuantity;
    }

    public void setImportQuantity(Double importQuantity) {
        this.importQuantity = importQuantity;
    }

    public Double getImportMoney() {
        return importMoney;
    }

    public void setImportMoney(Double importMoney) {
        this.importMoney = importMoney;
    }

    public Double getExportQuantity() {
        return exportQuantity;
    }

    public void setExportQuantity(Double exportQuantity) {
        this.exportQuantity = exportQuantity;
    }

    public Double getExportMoney() {
        return exportMoney;
    }

    public void setExportMoney(Double exportMoney) {
        this.exportMoney = exportMoney;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public Double getInventoryMoney() {
        return inventoryMoney;
    }

    public void setInventoryMoney(Double inventoryMoney) {
        this.inventoryMoney = inventoryMoney;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getRealInitialMoney() {
        return realInitialMoney;
    }

    public void setRealInitialMoney(Double realInitialMoney) {
        this.realInitialMoney = realInitialMoney;
    }

    public Double getRealImportMoney() {
        return realImportMoney;
    }

    public void setRealImportMoney(Double realImportMoney) {
        this.realImportMoney = realImportMoney;
    }

    public Double getRealExportMoney() {
        return realExportMoney;
    }

    public void setRealExportMoney(Double realExportMoney) {
        this.realExportMoney = realExportMoney;
    }

    public Double getRealInventoryMoney() {
        return realInventoryMoney;
    }

    public void setRealInventoryMoney(Double realInventoryMoney) {
        this.realInventoryMoney = realInventoryMoney;
    }

    public Double getRealProfit() {
        return realProfit;
    }

    public void setRealProfit(Double realProfit) {
        this.realProfit = realProfit;
    }
}