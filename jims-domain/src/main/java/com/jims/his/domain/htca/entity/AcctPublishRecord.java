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
 * AcctPublishRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCT_PUBLISH_RECORD", schema = "HTCA")
public class AcctPublishRecord implements java.io.Serializable {

	// Fields

	private String id;
	private String yearMonth;
	private String incomeFlag;
	private String operator;
	private Date publishDate;
	private Date openStartDate;
	private Date openEndDate;
	private String hospitalId;

	// Constructors

	/** default constructor */
	public AcctPublishRecord() {
	}

	/** full constructor */
	public AcctPublishRecord(String yearMonth, String incomeFlag,
			String operator, Date publishDate, Date openStartDate,
			Date openEndDate, String hospitalId) {
		this.yearMonth = yearMonth;
		this.incomeFlag = incomeFlag;
		this.operator = operator;
		this.publishDate = publishDate;
		this.openStartDate = openStartDate;
		this.openEndDate = openEndDate;
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

	@Column(name = "YEAR_MONTH", length = 20)
	public String getYearMonth() {
		return this.yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Column(name = "INCOME_FLAG", length = 10)
	public String getIncomeFlag() {
		return this.incomeFlag;
	}

	public void setIncomeFlag(String incomeFlag) {
		this.incomeFlag = incomeFlag;
	}

	@Column(name = "OPERATOR", length = 64)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PUBLISH_DATE", length = 7)
	public Date getPublishDate() {
		return this.publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OPEN_START_DATE", length = 7)
	public Date getOpenStartDate() {
		return this.openStartDate;
	}

	public void setOpenStartDate(Date openStartDate) {
		this.openStartDate = openStartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OPEN_END_DATE", length = 7)
	public Date getOpenEndDate() {
		return this.openEndDate;
	}

	public void setOpenEndDate(Date openEndDate) {
		this.openEndDate = openEndDate;
	}

	@Column(name = "HOSPITAL_ID", length = 64)
	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

}