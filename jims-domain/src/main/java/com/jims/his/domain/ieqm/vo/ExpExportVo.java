package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 产品去向关于各种统计的展示对象
 * Created by wangjing on 2015/11/5.
 */
@XmlRootElement
public class ExpExportVo implements Serializable {
    private String receiver;
    private Integer importNo;
    private Integer importCode;
    private Double importAmount;
    private String expForm;
    private String subStorage;
    private String expCode;
    private String expName;
    private String packageSpec;
    private String packageUnits;
    private String firmId;
    private Double quantity;
    private Double amount;
    public ExpExportVo() {
    }

    public ExpExportVo(String receiver, Integer importNo, Integer importCode, Double importAmount, String expForm, String subStorage, String expCode, String expName, String packageSpec, String packageUnits, String firmId, Double quantity, Double amount) {
        this.receiver = receiver;
        this.importNo = importNo;
        this.importCode = importCode;
        this.importAmount = importAmount;
        this.expForm = expForm;
        this.subStorage = subStorage;
        this.expCode = expCode;
        this.expName = expName;
        this.packageSpec = packageSpec;
        this.packageUnits = packageUnits;
        this.firmId = firmId;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Integer getImportNo() {
        return importNo;
    }

    public void setImportNo(Integer importNo) {
        this.importNo = importNo;
    }

    public Integer getImportCode() {
        return importCode;
    }

    public void setImportCode(Integer importCode) {
        this.importCode = importCode;
    }

    public Double getImportAmount() {
        return importAmount;
    }

    public void setImportAmount(Double importAmount) {
        this.importAmount = importAmount;
    }

    public String getExpForm() {
        return expForm;
    }

    public void setExpForm(String expForm) {
        this.expForm = expForm;
    }

    public String getSubStorage() {
        return subStorage;
    }

    public void setSubStorage(String subStorage) {
        this.subStorage = subStorage;
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

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
