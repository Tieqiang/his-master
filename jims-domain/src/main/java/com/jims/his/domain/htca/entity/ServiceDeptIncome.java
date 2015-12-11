package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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
                             Double totalIncome, String hospitalId, String yearMonth, String serviceForDeptId) {
        this.acctDeptId = acctDeptId;
        this.incomeTypeId = incomeTypeId;
        this.totalIncome = totalIncome;
        this.hospitalId = hospitalId;
        this.yearMonth = yearMonth;
        this.serviceForDeptId = serviceForDeptId;
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
}