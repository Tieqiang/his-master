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
 * AcctSaveRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCT_SAVE_RECORD", schema = "HTCA")
public class AcctSaveRecord implements java.io.Serializable {

	// Fields

	private String id;
	private String hospitalId;
	private String yearMonth;
	private String operator;
	private Date operateDate;
	private Double saveStatus;

	// Constructors

	/** default constructor */
	public AcctSaveRecord() {
	}

	/** full constructor */
	public AcctSaveRecord(String hospitalId, String yearMonth, String operator,
			Date operateDate, Double saveStatus) {
		this.hospitalId = hospitalId;
		this.yearMonth = yearMonth;
		this.operator = operator;
		this.operateDate = operateDate;
		this.saveStatus = saveStatus;
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

	@Column(name = "HOSPITAL_ID", length = 64)
	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	@Column(name = "YEAR_MONTH", length = 100)
	public String getYearMonth() {
		return this.yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Column(name = "OPERATOR", length = 64)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OPERATE_DATE", length = 7)
	public Date getOperateDate() {
		return this.operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	@Column(name = "SAVE_STATUS", precision = 22, scale = 0)
	public Double getSaveStatus() {
		return this.saveStatus;
	}

	public void setSaveStatus(Double saveStatus) {
		this.saveStatus = saveStatus;
	}

}