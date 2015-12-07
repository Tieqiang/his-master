package com.jims.his.domain.htca.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by heren on 2015/11/17.
 */
@Entity
@Table(name = "acct_reck_item_class_dict",schema = "HTCA")
public class AcctReckItemClassDict implements Serializable {
    private String id ;
    private String reckItemName ;
    private String reckItemCode ;
    private String inputCode ;
    private String outpOrderedBy ;
    private String outpPerformedBy ;
    private String outpWardCode ;
    private String inpOrderedBy ;
    private String inpPerformedBy ;
    private String inpWardCode ;
    private String hospitalId ;
    private String incomeType ;//收入类别
    private String reckType ;//核算类型
    private Double fixConvert;//固定折算 100-固定折算=成本
    private String costId ;//对应成本

    public AcctReckItemClassDict(String id, String reckItemName, String reckItemCode, String inputCode, String outpOrderedBy, String outpPerformedBy, String outpWardCode, String inpOrderedBy, String inpPerformedBy, String inpWardCode, String hospitalId, String incomeType, String reckType, Double fixConvert, String costId) {
        this.id = id;
        this.reckItemName = reckItemName;
        this.reckItemCode = reckItemCode;
        this.inputCode = inputCode;
        this.outpOrderedBy = outpOrderedBy;
        this.outpPerformedBy = outpPerformedBy;
        this.outpWardCode = outpWardCode;
        this.inpOrderedBy = inpOrderedBy;
        this.inpPerformedBy = inpPerformedBy;
        this.inpWardCode = inpWardCode;
        this.hospitalId = hospitalId;
        this.incomeType = incomeType;
        this.reckType = reckType;
        this.fixConvert = fixConvert;
        this.costId = costId;
    }

    public AcctReckItemClassDict() {

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

    @Column(name = "reck_item_name")
    public String getReckItemName() {
        return reckItemName;
    }

    public void setReckItemName(String reckItemName) {
        this.reckItemName = reckItemName;
    }

    @Column(name="reck_item_code")
    public String getReckItemCode() {
        return reckItemCode;
    }

    public void setReckItemCode(String reckItemCode) {
        this.reckItemCode = reckItemCode;
    }
    @Column(name="input_code")
    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    @Column(name="outp_ordered_by")
    public String getOutpOrderedBy() {
        return outpOrderedBy;
    }

    public void setOutpOrderedBy(String outpOrderedBy) {
        this.outpOrderedBy = outpOrderedBy;
    }

    @Column(name="outp_performed_by")
    public String getOutpPerformedBy() {
        return outpPerformedBy;
    }


    public void setOutpPerformedBy(String outpPerformedBy) {
        this.outpPerformedBy = outpPerformedBy;
    }

    @Column(name="outp_ward_code")
    public String getOutpWardCode() {
        return outpWardCode;
    }

    public void setOutpWardCode(String outpWardCode) {
        this.outpWardCode = outpWardCode;
    }

    @Column(name="inp_ordered_by")
    public String getInpOrderedBy() {
        return inpOrderedBy;
    }

    public void setInpOrderedBy(String inpOrderedBy) {
        this.inpOrderedBy = inpOrderedBy;
    }

    @Column(name="inp_performed_by")
    public String getInpPerformedBy() {
        return inpPerformedBy;
    }

    public void setInpPerformedBy(String inpPerformedBy) {
        this.inpPerformedBy = inpPerformedBy;
    }

    @Column(name="inp_ward_code")
    public String getInpWardCode() {
        return inpWardCode;
    }

    public void setInpWardCode(String inpWardCode) {
        this.inpWardCode = inpWardCode;
    }

    @Column(name="hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Column(name="income_type")
    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    @Column(name = "reck_type")
    public String getReckType() {
        return reckType;
    }

    public void setReckType(String reckType) {
        this.reckType = reckType;
    }

    @Column(name="fix_convert")
    public Double getFixConvert() {
        return fixConvert;
    }

    public void setFixConvert(Double fixConvert) {
        this.fixConvert = fixConvert;
    }

    @Column(name="cost_id")
    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }
}
