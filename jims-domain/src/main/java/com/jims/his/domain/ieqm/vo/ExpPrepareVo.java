package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by zhuq on 2016/6/2.
 * 高值耗材集合类
 */
@XmlRootElement
public class ExpPrepareVo {
    private String expBarCode ;         //条形码
    private String masterId ;           //备货记录ID
    private String useFlag ;            //是否使用
    private String useDate ;            //使用日期
    private String usePatientId ;       //使用病人
    private String useDept ;            //使用科室
    private String impDocnoFirst   ;    //入库单号
    private String expDocnoFirst   ;    //出库单号
    private String impDocnoSecond  ;    //入库单号
    private String expDocnoSecond  ;    //出库单号
    private String barCodeOperator ;     //扫码人员
    private String printFlag ;          //是否打印
    private String expId ;               //消耗品
    private String supplierId ;          //供应商
    private String prepareDate ;         //备货时间
    private String prepareCount ;        //备货数量
    private String operator ;            //操作人
    private String preparePersonName ;   //备货人
    private String phone ;               //备货人电话
    private Double price ;               //备货价格
    private String expCode;               //耗材编码
    private String expName;               //耗材名称
    private String expSpec;               //规格
    private String units;                //单位
    private String expFrom;                //高值耗材
    private String usePrice;               //出库价格
    private String supplier;

    public ExpPrepareVo(){

    }

    public ExpPrepareVo(String supplier,String expBarCode, String masterId, String useFlag, String useDate, String usePatientId, String useDept, String impDocnoFirst, String expDocnoFirst, String impDocnoSecond, String expDocnoSecond, String barCodeOperator, String printFlag, String expId, String supplierId, String prepareDate, String prepareCount, String operator, String preparePersonName, String phone, Double price, String expCode, String expName, String expSpec, String units, String expFrom, String usePrice) {
        this.expBarCode = expBarCode;
        this.masterId = masterId;
        this.useFlag = useFlag;
        this.useDate = useDate;
        this.usePatientId = usePatientId;
        this.useDept = useDept;
        this.impDocnoFirst = impDocnoFirst;
        this.expDocnoFirst = expDocnoFirst;
        this.impDocnoSecond = impDocnoSecond;
        this.expDocnoSecond = expDocnoSecond;
        this.barCodeOperator = barCodeOperator;
        this.printFlag = printFlag;
        this.expId = expId;
        this.supplierId = supplierId;
        this.prepareDate = prepareDate;
        this.prepareCount = prepareCount;
        this.operator = operator;
        this.preparePersonName = preparePersonName;
        this.phone = phone;
        this.price = price;
        this.expCode = expCode;
        this.expName = expName;
        this.expSpec = expSpec;
        this.units = units;
        this.expFrom = expFrom;
        this.usePrice = usePrice;
        this.supplier=supplier;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getExpBarCode() {
        return expBarCode;
    }

    public void setExpBarCode(String expBarCode) {
        this.expBarCode = expBarCode;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;
    }

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    public String getUsePatientId() {
        return usePatientId;
    }

    public void setUsePatientId(String usePatientId) {
        this.usePatientId = usePatientId;
    }

    public String getUseDept() {
        return useDept;
    }

    public void setUseDept(String useDept) {
        this.useDept = useDept;
    }

    public String getImpDocnoFirst() {
        return impDocnoFirst;
    }

    public void setImpDocnoFirst(String impDocnoFirst) {
        this.impDocnoFirst = impDocnoFirst;
    }

    public String getExpDocnoFirst() {
        return expDocnoFirst;
    }

    public void setExpDocnoFirst(String expDocnoFirst) {
        this.expDocnoFirst = expDocnoFirst;
    }

    public String getImpDocnoSecond() {
        return impDocnoSecond;
    }

    public void setImpDocnoSecond(String impDocnoSecond) {
        this.impDocnoSecond = impDocnoSecond;
    }

    public String getExpDocnoSecond() {
        return expDocnoSecond;
    }

    public void setExpDocnoSecond(String expDocnoSecond) {
        this.expDocnoSecond = expDocnoSecond;
    }

    public String getBarCodeOperator() {
        return barCodeOperator;
    }

    public void setBarCodeOperator(String barCodeOperator) {
        this.barCodeOperator = barCodeOperator;
    }

    public String getPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(String printFlag) {
        this.printFlag = printFlag;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getPrepareDate() {
        return prepareDate;
    }

    public void setPrepareDate(String prepareDate) {
        this.prepareDate = prepareDate;
    }

    public String getPrepareCount() {
        return prepareCount;
    }

    public void setPrepareCount(String prepareCount) {
        this.prepareCount = prepareCount;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPreparePersonName() {
        return preparePersonName;
    }

    public void setPreparePersonName(String preparePersonName) {
        this.preparePersonName = preparePersonName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getExpFrom() {
        return expFrom;
    }

    public void setExpFrom(String expFrom) {
        this.expFrom = expFrom;
    }

    public String getUsePrice() {
        return usePrice;
    }

    public void setUsePrice(String usePrice) {
        this.usePrice = usePrice;
    }
}
