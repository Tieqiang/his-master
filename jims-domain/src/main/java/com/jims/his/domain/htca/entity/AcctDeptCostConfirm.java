package com.jims.his.domain.htca.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * AcctDeptCostConfirm entity. @author ztq
 */
@Entity
@Table(name = "ACCT_DEPT_COST_CONFIRM", schema = "HTCA")
public class AcctDeptCostConfirm implements java.io.Serializable {

	// Fields

	private String id;
	private String acctDeptId;
	private String costItemId;
	private String memo;
    private String yearMonth ;
    private String hospitalId ;
    private String operator ;
    private Date   operatorDate ;


	// Constructors

	/** default constructor */
	public AcctDeptCostConfirm() {
	}

	/** full constructor */
	public AcctDeptCostConfirm(String acctDeptId, String costItemId, String memo, String yearMonth, String hospitalId, String operator, Date operatorDate) {
		this.acctDeptId = acctDeptId;
		this.costItemId = costItemId;
		this.memo = memo;
        this.yearMonth = yearMonth;
        this.hospitalId = hospitalId;
        this.operator = operator;
        this.operatorDate = operatorDate;

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

	@Column(name = "COST_ITEM_ID", length = 64)
	public String getCostItemId() {
		return this.costItemId;
	}

	public void setCostItemId(String costItemId) {
		this.costItemId = costItemId;
	}

	@Column(name = "MEMO", length = 1000)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

    @Column(name = "year_month")
    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    @Column(name="hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
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


}