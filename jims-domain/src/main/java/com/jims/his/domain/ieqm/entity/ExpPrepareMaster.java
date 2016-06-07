package com.jims.his.domain.ieqm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/6/2.
 */
@Entity
@Table(name = "EXP_PREPARE_MASTER", schema = "JIMS")
public class ExpPrepareMaster implements java.io.Serializable{
    private String id ;
    private String expId ;               //消耗品
    private String supplierId ;          //供应商
    private String prepareDate ;         //备货时间
    private String prepareCount ;        //备货数量
    private String operator ;            //操作人
    private String preparePersonName ;   //备货人
    private String phone ;               //备货人电话
    private Double price ;               //备货价格
    private String firmId;              //生产厂商

    public ExpPrepareMaster (){

    }

    public ExpPrepareMaster(String expId, String supplierId, String prepareDate, String prepareCount, String operator, String preparePersonName, String phone, Double price) {
        this.expId = expId;
        this.supplierId = supplierId;
        this.prepareDate = prepareDate;
        this.prepareCount = prepareCount;
        this.operator = operator;
        this.preparePersonName = preparePersonName;
        this.phone = phone;
        this.price = price;
    }

    @GenericGenerator(name = "generator", strategy = "uuid.hex")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "EXP_ID", length = 64)
    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    @Column(name = "SUPPLIER_ID", length = 64)
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Column(name = "PREPARE_DATE", length = 32)
    public String getPrepareDate() {
        return prepareDate;
    }

    public void setPrepareDate(String prepareDate) {
        this.prepareDate = prepareDate;
    }

    @Column(name = "PREPARE_COUNT", length = 10)
    public String getPrepareCount() {
        return prepareCount;
    }

    public void setPrepareCount(String prepareCount) {
        this.prepareCount = prepareCount;
    }

    @Column(name = "OPERATOR", length = 16)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "PREPARE_PERSON_NAME", length = 16)
    public String getPreparePersonName() {
        return preparePersonName;
    }

    public void setPreparePersonName(String preparePersonName) {
        this.preparePersonName = preparePersonName;
    }

    @Column(name = "PHONE", length = 16)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "PRICE")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(name="firm_id")
    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }
}
