package com.jims.his.domain.htca.entity;

import java.lang.Double;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * AcctDeptCost entity. @author ztq
 */
@Entity
@Table(name = "ACCT_DEPT_COST", schema = "HTCA")
public class AcctDeptCost implements java.io.Serializable {

	// Fields

	private String id;
	private String acctDeptId;
	private String costItemId;
	private Double cost;
	private Double minusCost;
	private String memo;
    private String yearMonth ;
    private String hospitalId ;
    private String fetchWay ;//获取方式
    private String operator ;
    private Date   operatorDate ;

	// Constructors

	/** default constructor */
	public AcctDeptCost() {
	}

	/** full constructor */
	public AcctDeptCost(String acctDeptId, String costItemId, Double cost,
                        Double minusCost, String memo, String yearMonth, String hospitalId, String fetchWay, String operator, Date operatorDate) {
		this.acctDeptId = acctDeptId;
		this.costItemId = costItemId;
		this.cost = cost;
		this.minusCost = minusCost;
		this.memo = memo;
        this.yearMonth = yearMonth;
        this.hospitalId = hospitalId;
        this.fetchWay = fetchWay;
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

	@Column(name = "COST", precision = 22, scale = 0)
	public Double getCost() {
		return this.cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Column(name = "MINUS_COST", precision = 22, scale = 0)
	public Double getMinusCost() {
		return this.minusCost;
	}

	public void setMinusCost(Double minusCost) {
		this.minusCost = minusCost;
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

    @Column(name="fetch_way")
    public String getFetchWay() {

        return fetchWay;
    }

    public void setFetchWay(String fetchWay) {
        this.fetchWay = fetchWay;
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