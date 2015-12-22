package com.jims.his.domain.htca.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * AcctSingleRewardRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCT_SINGLE_REWARD_RECORD", schema = "HTCA")
public class AcctSingleRewardRecord implements java.io.Serializable {

	// Fields

	private String id;
	private String yearMonth;
	private String hospitalId;
	private String acctDeptId;
	private Double rewardNum;
	private String operator;
	private Date operatorDate;
	private String rewardDictId;

	// Constructors

	/** default constructor */
	public AcctSingleRewardRecord() {
	}

	/** full constructor */
	public AcctSingleRewardRecord(String yearMonth, String hospitalId,
			String acctDeptId, Double rewardNum, String operator,
			Date operatorDate, String rewardDictId) {
		this.yearMonth = yearMonth;
		this.hospitalId = hospitalId;
		this.acctDeptId = acctDeptId;
		this.rewardNum = rewardNum;
		this.operator = operator;
		this.operatorDate = operatorDate;
		this.rewardDictId = rewardDictId;
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

	@Column(name = "ACCT_DEPT_ID", length = 64)
	public String getAcctDeptId() {
		return this.acctDeptId;
	}

	public void setAcctDeptId(String acctDeptId) {
		this.acctDeptId = acctDeptId;
	}

	@Column(name = "REWARD_NUM", precision = 22, scale = 0)
	public Double getRewardNum() {
		return this.rewardNum;
	}

	public void setRewardNum(Double rewardNum) {
		this.rewardNum = rewardNum;
	}

	@Column(name = "OPERATOR", length = 100)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OPERATOR_DATE", length = 7)
	public Date getOperatorDate() {
		return this.operatorDate;
	}

	public void setOperatorDate(Date operatorDate) {
		this.operatorDate = operatorDate;
	}

	@Column(name = "REWARD_DICT_ID", length = 64)
	public String getRewardDictId() {
		return this.rewardDictId;
	}

	public void setRewardDictId(String rewardDictId) {
		this.rewardDictId = rewardDictId;
	}

}