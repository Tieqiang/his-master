package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangjing on 2015/10/13.
 * 产品价格维护的时候，结果集展示涉及到EXP_PRICE_LIST和EXP_DICT两张表的内容，定义此VO用来展示联合信息
 */
@XmlRootElement
public class ExpPriceListVo implements Serializable {

    private String id ;
    private String expCode;
    private String expName;
    private Integer amountPerPackage;
    private Integer supplyIndicator;
    private String expSpec;
    private String units;
    private String firmId;
    private String materialCode;
    private String stopPrice;
    private Double tradePrice;
    private String priceRatio;
    private Double retailPrice;
    private Double maxRetailPrice;
    private String registerNo;
    private Date registerDate;
    private String minSpec;
    private String minUnits;
    private String classOnInpRcpt;
    private String classOnOutpRcpt;
    private String classOnReckoning;
    private String subjCode;
    private String classOnMr;
    private String permitNo;
    private Date permitDate;
    private String fdaOrCeNo;
    private Date fdaOrCeDate;
    private String otherNo;
    private Date otherDate;
    private Date startDate;
    private Date stopDate;
    private String memos;
    private String columnProtect;
    private String operator;
    private String inputCode;
    private String hospitalId;
    private String packageUnits ;
    private String expForm;

    public ExpPriceListVo() {
    }

    public ExpPriceListVo(String id, Integer supplyIndicator, String inputCode, String expName, String expCode, String expSpec, String firmId,
                          String units, Double tradePrice, Double retailPrice, Integer amountPerPackage,
                          String minSpec, String minUnits, String classOnInpRcpt, String classOnOutpRcpt,
                          String classOnReckoning, String subjCode, String classOnMr, Date startDate,
                          Date stopDate, String memos, Double maxRetailPrice, String materialCode,
                          String operator, String permitNo, Date permitDate, String registerNo,
                          Date registerDate, String fdaOrCeNo, Date fdaOrCeDate, String otherNo,
                          Date otherDate, String priceRatio, String stopPrice, String columnProtect, String hospitalId, String packageUnits) {
        this.id = id;
        this.supplyIndicator = supplyIndicator;
        this.inputCode = inputCode;
        this.expName = expName;
        this.expCode = expCode;
        this.expSpec = expSpec;
        this.firmId = firmId;
        this.units = units;
        this.tradePrice = tradePrice;
        this.retailPrice = retailPrice;
        this.amountPerPackage = amountPerPackage;
        this.minSpec = minSpec;
        this.minUnits = minUnits;
        this.classOnInpRcpt = classOnInpRcpt;
        this.classOnOutpRcpt = classOnOutpRcpt;
        this.classOnReckoning = classOnReckoning;
        this.subjCode = subjCode;
        this.classOnMr = classOnMr;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.memos = memos;
        this.maxRetailPrice = maxRetailPrice;
        this.materialCode = materialCode;
        this.operator = operator;
        this.permitNo = permitNo;
        this.permitDate = permitDate;
        this.registerNo = registerNo;
        this.registerDate = registerDate;
        this.fdaOrCeNo = fdaOrCeNo;
        this.fdaOrCeDate = fdaOrCeDate;
        this.otherNo = otherNo;
        this.otherDate = otherDate;
        this.priceRatio = priceRatio;
        this.stopPrice = stopPrice;
        this.columnProtect = columnProtect;
        this.hospitalId = hospitalId;
        this.packageUnits = packageUnits;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
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

    public String getClassOnInpRcpt() {
        return classOnInpRcpt;
    }

    public void setClassOnInpRcpt(String classOnInpRcpt) {
        this.classOnInpRcpt = classOnInpRcpt;
    }

    public String getClassOnOutpRcpt() {
        return classOnOutpRcpt;
    }

    public void setClassOnOutpRcpt(String classOnOutpRcpt) {
        this.classOnOutpRcpt = classOnOutpRcpt;
    }

    public String getClassOnReckoning() {
        return classOnReckoning;
    }

    public void setClassOnReckoning(String classOnReckoning) {
        this.classOnReckoning = classOnReckoning;
    }

    public String getSubjCode() {
        return subjCode;
    }

    public void setSubjCode(String subjCode) {
        this.subjCode = subjCode;
    }

    public String getClassOnMr() {
        return classOnMr;
    }

    public void setClassOnMr(String classOnMr) {
        this.classOnMr = classOnMr;
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

    public Double getMaxRetailPrice() {
        return maxRetailPrice;
    }

    public void setMaxRetailPrice(Double maxRetailPrice) {
        this.maxRetailPrice = maxRetailPrice;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPermitNo() {
        return permitNo;
    }

    public void setPermitNo(String permitNo) {
        this.permitNo = permitNo;
    }

    public Date getPermitDate() {
        return permitDate;
    }

    public void setPermitDate(Date permitDate) {
        this.permitDate = permitDate;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getFdaOrCeNo() {
        return fdaOrCeNo;
    }

    public void setFdaOrCeNo(String fdaOrCeNo) {
        this.fdaOrCeNo = fdaOrCeNo;
    }

    public Date getFdaOrCeDate() {
        return fdaOrCeDate;
    }

    public void setFdaOrCeDate(Date fdaOrCeDate) {
        this.fdaOrCeDate = fdaOrCeDate;
    }

    public String getOtherNo() {
        return otherNo;
    }

    public void setOtherNo(String otherNo) {
        this.otherNo = otherNo;
    }

    public Date getOtherDate() {
        return otherDate;
    }

    public void setOtherDate(Date otherDate) {
        this.otherDate = otherDate;
    }

    public String getPriceRatio() {
        return priceRatio;
    }

    public void setPriceRatio(String priceRatio) {
        this.priceRatio = priceRatio;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getColumnProtect() {
        return columnProtect;
    }

    public void setColumnProtect(String columnProtect) {
        this.columnProtect = columnProtect;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Integer getSupplyIndicator() {
        return supplyIndicator;
    }

    public void setSupplyIndicator(Integer supplyIndicator) {
        this.supplyIndicator = supplyIndicator;
    }

    public String getPackageUnits() {
        return packageUnits;
    }

    public void setPackageUnits(String packageUnits) {
        this.packageUnits = packageUnits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpForm() {
        return expForm;
    }

    public void setExpForm(String expForm) {
        this.expForm = expForm;
    }
}
