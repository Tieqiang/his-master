package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * ServiceDeptIncome entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SERVICE_DEPT_INCOME", schema = "HTCA")
public class ServiceDeptIncome implements java.io.Serializable {

    // Fields
    private String id;
    private String acctDeptId;
    private String incomeTypeId;
    private Double totalIncome;
    private String hospitalId;
    private String yearMonth ;
    private String serviceForDeptId ;
    private String getWay ;
    private String outFlag ;
    private String operator ;
    private Date operatorDate ;
    private String confirmStatus ;
    private String confirmOperator ;
    private Date confirmDate ;
    private String detailIncomeId ;
    // Constructors
    /**
     * default constructor
     */
    public ServiceDeptIncome() {
    }

    /**
     * full constructor
     */
    public ServiceDeptIncome(String acctDeptId, String incomeTypeId,
                             Double totalIncome, String hospitalId, String yearMonth, String serviceForDeptId, String getWay, String outFlag, String operator, Date operatorDate, String confirmStatus, String confirmOperator, Date confirmDate, String detailIncomeId) {
        this.acctDeptId = acctDeptId;
        this.incomeTypeId = incomeTypeId;
        this.totalIncome = totalIncome;
        this.hospitalId = hospitalId;
        this.yearMonth = yearMonth;
        this.serviceForDeptId = serviceForDeptId;
        this.getWay = getWay;
        this.outFlag = outFlag;
        this.operator = operator;
        this.operatorDate = operatorDate;
        this.confirmStatus = confirmStatus;
        this.confirmOperator = confirmOperator;
        this.confirmDate = confirmDate;
        this.detailIncomeId = detailIncomeId;
    }

    // Property accessors
    @GenericGenerator(name = "generator", strategy = "uuid.hex")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 64)
    public String getId() {
        return this.id;
    }


    public void setId(String id) {
        this.id = id;
    }



    @Column(name = "ACCT_DEPT_ID", length = 64)
    public String getAcctDeptId() {
        return this.acctDeptId;
    }

    public void setAcctDeptId(String acctDeptId) {
        this.acctDeptId = acctDeptId;
    }

    @Column(name = "INCOME_TYPE_ID", length = 64)
    public String getIncomeTypeId() {
        return this.incomeTypeId;
    }

    public void setIncomeTypeId(String incomeTypeId) {
        this.incomeTypeId = incomeTypeId;
    }

    @Column(name = "TOTAL_INCOME", precision = 22, scale = 0)
    public Double getTotalIncome() {
        return this.totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    @Column(name = "HOSPITAL_ID", length = 64)
    public String getHospitalId() {
        return this.hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Column(name="year_month")
    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    @Column(name="service_for_dept_id")
    public String getServiceForDeptId() {
        return serviceForDeptId;
    }

    public void setServiceForDeptId(String serviceForDeptId) {
        this.serviceForDeptId = serviceForDeptId;
    }

    @Column(name="get_way")
    public String getGetWay() {
        return getWay;
    }

    public void setGetWay(String getWay) {
        this.getWay = getWay;
    }

    @Column(name="out_flag")
    public String getOutFlag() {
        return outFlag;
    }

    public void setOutFlag(String outFlag) {
        this.outFlag = outFlag;
    }

    @Column(name="operator")
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name="operator_date")
    public Date getOperatorDate() {
        return operatorDate;
    }

    public void setOperatorDate(Date operatorDate) {
        this.operatorDate = operatorDate;
    }

    @Column(name="confirm_status")
    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    @Column(name="confirm_operator")
    public String getConfirmOperator() {
        return confirmOperator;
    }

    public void setConfirmOperator(String confirmOperator) {
        this.confirmOperator = confirmOperator;
    }

    @Column(name="confirm_date")
    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }


    @Column(name="detail_income_id")
    public String getDetailIncomeId() {
        return detailIncomeId;
    }

    public void setDetailIncomeId(String detailIncomeId) {
        this.detailIncomeId = detailIncomeId;
    }
}