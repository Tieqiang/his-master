package com.jims.his.domain.htca.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * AcctDeptProfit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCT_DEPT_PROFIT", schema = "HTCA")
public class AcctDeptProfit implements java.io.Serializable {

	// Fields

	private String id;
	private String yearMonth;
	private String hospitalId;
	private Double deptIncome;
	private Double deptCost;
	private Double pleasedNum;
	private Double specialIncome;
    private String acctDeptId ;
    private Double deptLastIncome ;
    private Double convertRate ;


	// Constructors

	/** default constructor */
	public AcctDeptProfit() {
	}

	/** full constructor */
	public AcctDeptProfit(String yearMonth, String hospitalId,
                          Double deptIncome, Double deptCost, Double pleasedNum,
                          Double specialIncome, String acctDeptId, Double deptLastIncome, Double convertRate) {
		this.yearMonth = yearMonth;
		this.hospitalId = hospitalId;
		this.deptIncome = deptIncome;
		this.deptCost = deptCost;
		this.pleasedNum = pleasedNum;
		this.specialIncome = specialIncome;
        this.acctDeptId = acctDeptId;
        this.deptLastIncome = deptLastIncome;
        this.convertRate = convertRate;
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

	@Column(name = "YEAR_MONTH", length = 20)
	public String getYearMonth() {
		return this.yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Column(name = "HOSPITAL_ID", length = 64)
	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	@Column(name = "DEPT_INCOME", precision = 22, scale = 0)
	public Double getDeptIncome() {
		return this.deptIncome;
	}

	public void setDeptIncome(Double deptIncome) {
		this.deptIncome = deptIncome;
	}

    @Column(name="dept_last_income")
    public Double getDeptLastIncome() {
        return deptLastIncome;
    }

    public void setDeptLastIncome(Double deptLastIncome) {
        this.deptLastIncome = deptLastIncome;
    }

    @Column(name = "DEPT_COST", precision = 22, scale = 0)
	public Double getDeptCost() {
		return this.deptCost;
	}

	public void setDeptCost(Double deptCost) {
		this.deptCost = deptCost;
	}

	@Column(name = "PLEASED_NUM", precision = 22, scale = 0)
	public Double getPleasedNum() {
		return this.pleasedNum;
	}

	public void setPleasedNum(Double pleasedNum) {
		this.pleasedNum = pleasedNum;
	}

	@Column(name = "SPECIAL_INCOME", precision = 22, scale = 0)
	public Double getSpecialIncome() {
		return this.specialIncome;
	}

	public void setSpecialIncome(Double specialIncome) {
		this.specialIncome = specialIncome;
	}


    @Column(name="acct_dept_id")
    public String getAcctDeptId() {
        return acctDeptId;
    }

    public void setAcctDeptId(String acctDeptId) {
        this.acctDeptId = acctDeptId;
    }

    @Column(name="convert_rate")
    public Double getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(Double convertRate) {
        this.convertRate = convertRate;
    }
}