package com.jims.his.domain.htca.vo;

import java.io.Serializable;

/**
 * Created by heren on 2015/10/29.
 */
public class HospitalIncomeRecordVo implements Serializable {

    private String hospitalId ;
    private String deptName ;
    private String deptCode ;
    private String incomeItemCode;
    private String incomeItemName;
    private String incomeYearMonth ;
    private String incomeState ;
    private String deptId ;
    private String id ;
    private Double incomeAmount ;



    public HospitalIncomeRecordVo(String hospitalId, String deptName, String deptCode, String incomeItemCode, String incomeItemName, String incomeYearMonth, String incomeState, String deptId, String id, Double incomeAmount) {
        this.hospitalId = hospitalId;
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.incomeItemCode = incomeItemCode;
        this.incomeItemName = incomeItemName;
        this.incomeYearMonth = incomeYearMonth;
        this.incomeState = incomeState;
        this.deptId = deptId;
        this.id = id;
        this.incomeAmount = incomeAmount;
    }

    public HospitalIncomeRecordVo() {
    }


    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getIncomeItemCode() {
        return incomeItemCode;
    }

    public void setIncomeItemCode(String incomeItemCode) {
        this.incomeItemCode = incomeItemCode;
    }

    public String getIncomeItemName() {
        return incomeItemName;
    }

    public void setIncomeItemName(String incomeItemName) {
        this.incomeItemName = incomeItemName;
    }

    public String getIncomeYearMonth() {
        return incomeYearMonth;
    }

    public void setIncomeYearMonth(String incomeYearMonth) {
        this.incomeYearMonth = incomeYearMonth;
    }

    public String getIncomeState() {
        return incomeState;
    }

    public void setIncomeState(String incomeState) {
        this.incomeState = incomeState;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }
}
