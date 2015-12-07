package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangjing on 2015/10/19.
 *调价记录维护功能页面下方的历史价格记录的展示涉及到exp_price_list和exp_name_dict两张表内容，定义此对象用来展示联合信息
 */
@XmlRootElement
public class ExpPriceHisVo implements Serializable {
    private String expCode;
    private String expName;
    private String expSpec;
    private String firmId;
    private String units;
    private Double tradePrice;
    private Double retailPrice;
    private Date startDate;
    private Date stopDate;
    private Integer amountPerPackage;
    private String minSpec;
    private String minUnits;
    private String memos;
    private String oldOrNew;
    private String materialCode;
    private String hospitalId;

    public ExpPriceHisVo() {
    }

    public ExpPriceHisVo(String expCode, String expName, String expSpec,
                         String firmId, String units, Double tradePrice,
                         Double retailPrice, Date startDate, Date stopDate,
                         Integer amountPerPackage, String minSpec, String minUnits,
                         String memos, String oldOrNew, String materialCode, String hospitalId) {
        this.expCode = expCode;
        this.expName = expName;
        this.expSpec = expSpec;
        this.firmId = firmId;
        this.units = units;
        this.tradePrice = tradePrice;
        this.retailPrice = retailPrice;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.amountPerPackage = amountPerPackage;
        this.minSpec = minSpec;
        this.minUnits = minUnits;
        this.memos = memos;
        this.oldOrNew = oldOrNew;
        this.materialCode = materialCode;
        this.hospitalId = hospitalId;
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

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
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

    public Integer getAmountPerPackage() {
        return amountPerPackage;
    }

    public void setAmountPerPackage(Integer amountPerPackage) {
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

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

    public String getOldOrNew() {
        return oldOrNew;
    }

    public void setOldOrNew(String oldOrNew) {
        this.oldOrNew = oldOrNew;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}
