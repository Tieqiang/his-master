package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by wangbinbin on 2015/10/8.
 */
@XmlRootElement
public class ExpMenuSearchVo implements Serializable {
    private String expCode;
    private String expName;
    private String expSpec;
    private String firmId;
    private Double retailPrice;
    private String units;
    private String expForm;
    private Double tradePrice;
    private Double quantity;
    private Integer expIndicator;

    public ExpMenuSearchVo() {
    }

    public ExpMenuSearchVo(String expCode, String expName, String expSpec, String firmId, Double retailPrice, String units, String expForm, Double tradePrice, Double quantity, Integer expIndicator) {
        this.expCode = expCode;
        this.expName = expName;
        this.expSpec = expSpec;
        this.firmId = firmId;
        this.retailPrice = retailPrice;
        this.units = units;
        this.expForm = expForm;
        this.tradePrice = tradePrice;
        this.quantity = quantity;
        this.expIndicator = expIndicator;
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

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
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

    public Double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(Double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getExpIndicator() {
        return expIndicator;
    }

    public void setExpIndicator(Integer expIndicator) {
        this.expIndicator = expIndicator;
    }
}