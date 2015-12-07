package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangbinbin on 2015/10/8.
 * 产品价格查询
 * 基于exp_price_list,exp_name_dict进行查询
 * 主要针对exp_price_list进行操作
 */
@XmlRootElement
public class ExpPriceSearchVo implements Serializable {
    private String expCode;
    private String expName;
    private String expSpec;
    private String firmId;
    private String materialCode;
    private Double retailPrice;
    private Double tradePrice;
    private String units;
    private Double amountPerPackage;
    private String minSpec;
    private String minUnits;
    private Date startDate;
    private Date stopDate;
    private String memos;
    public ExpPriceSearchVo() {
    }

    public ExpPriceSearchVo(String expCode, String expName, String expSpec, String firmId, String materialCode, Double retailPrice, Double tradePrice, String units, String expFrom, Double amountPerPackage, String minSpec, String minUnits, Date startDate, Date stopDate, String memos, String sign, String oldOrNew) {
        this.expCode = expCode;
        this.expName = expName;
        this.expSpec = expSpec;
        this.firmId = firmId;
        this.materialCode = materialCode;
        this.retailPrice = retailPrice;
        this.tradePrice = tradePrice;
        this.units = units;
        this.amountPerPackage = amountPerPackage;
        this.minSpec = minSpec;
        this.minUnits = minUnits;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.memos = memos;

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

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(Double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Double getAmountPerPackage() {
        return amountPerPackage;
    }

    public void setAmountPerPackage(Double amountPerPackage) {
        this.amountPerPackage = amountPerPackage;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

}