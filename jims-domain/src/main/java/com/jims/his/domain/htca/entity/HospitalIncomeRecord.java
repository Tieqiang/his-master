package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * HospitalIncomeRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HOSPITAL_INCOME_RECORD", schema = "HTCA")
public class HospitalIncomeRecord implements java.io.Serializable {

	// Fields

	private String id;
	private AcctDeptDict acctDeptDict;
	private String incomeYearMonth;
	private String incomeItemCode;
	private Double incomeAmount;
    private String incomeState ;
    private String incomeItemName ;
    private String hospitalId ;

	// Constructors

	/** default constructor */
	public HospitalIncomeRecord() {
	}

	/** full constructor */
	public HospitalIncomeRecord(AcctDeptDict acctDeptDict,
                                String incomeYearMonth, String incomeItemCode, Double incomeAmount, String incomeState, String incomeItemName, String hospitalId) {
		this.acctDeptDict = acctDeptDict;
		this.incomeYearMonth = incomeYearMonth;
		this.incomeItemCode = incomeItemCode;
		this.incomeAmount = incomeAmount;
        this.incomeState = incomeState;
        this.incomeItemName = incomeItemName;
        this.hospitalId = hospitalId;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPT_ID")
	public AcctDeptDict getAcctDeptDict() {
		return this.acctDeptDict;
	}

	public void setAcctDeptDict(AcctDeptDict acctDeptDict) {
		this.acctDeptDict = acctDeptDict;
	}

	@Column(name = "INCOME_YEAR_MONTH", length = 10)
	public String getIncomeYearMonth() {
		return this.incomeYearMonth;
	}

	public void setIncomeYearMonth(String incomeYearMonth) {
		this.incomeYearMonth = incomeYearMonth;
	}

	@Column(name = "INCOME_ITEM_CODE", length = 50)
	public String getIncomeItemCode() {
		return this.incomeItemCode;
	}

	public void setIncomeItemCode(String incomeItemCode) {
		this.incomeItemCode = incomeItemCode;
	}

	@Column(name = "INCOME_AMOUNT", precision = 22, scale = 0)
	public Double getIncomeAmount() {
		return this.incomeAmount;
	}

	public void setIncomeAmount(Double incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

    @Column(name="income_state")
    public String getIncomeState() {
        return incomeState;
    }

    public void setIncomeState(String incomeState) {
        this.incomeState = incomeState;
    }

    @Column(name="income_item_name")
    public String getIncomeItemName() {
        return incomeItemName;
    }

    public void setIncomeItemName(String incomeItemName) {
        this.incomeItemName = incomeItemName;
    }

    @Column(name="hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}