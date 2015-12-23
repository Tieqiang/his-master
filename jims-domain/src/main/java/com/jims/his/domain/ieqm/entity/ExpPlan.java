package com.jims.his.domain.ieqm.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpPlan entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_PLAN", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORE_ID", "PLAN_ID", "PLAN_ITEM" }))
public class ExpPlan implements java.io.Serializable {

	// Fields

	private String id;
	private String storeId;
	private String planId;
	private String planItem;
	private String sign;
	private String expName;
	private String specs;
	private String specsAbbr;
	private String factory;
	private String fcountry;
	private String country;
	private String billId;
	private Short bookId;
	private Double planQuan;
	private Double planSum;
	private Double inQuan;
	private Double inSum;
	private Date planDate;
	private Date overDate;

	// Constructors

	/** default constructor */
	public ExpPlan() {
	}

	/** full constructor */
	public ExpPlan(String storeId, String planId, String planItem, String sign,
			String expName, String specs, String specsAbbr, String factory,
			String fcountry, String country, String billId, Short bookId,
			Double planQuan, Double planSum, Double inQuan, Double inSum,
			Date planDate, Date overDate) {
		this.storeId = storeId;
		this.planId = planId;
		this.planItem = planItem;
		this.sign = sign;
		this.expName = expName;
		this.specs = specs;
		this.specsAbbr = specsAbbr;
		this.factory = factory;
		this.fcountry = fcountry;
		this.country = country;
		this.billId = billId;
		this.bookId = bookId;
		this.planQuan = planQuan;
		this.planSum = planSum;
		this.inQuan = inQuan;
		this.inSum = inSum;
		this.planDate = planDate;
		this.overDate = overDate;
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

	@Column(name = "STORE_ID", length = 10)
	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Column(name = "PLAN_ID", length = 12)
	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	@Column(name = "PLAN_ITEM", length = 2)
	public String getPlanItem() {
		return this.planItem;
	}

	public void setPlanItem(String planItem) {
		this.planItem = planItem;
	}

	@Column(name = "SIGN", length = 1)
	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Column(name = "EXP_NAME", length = 100)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	@Column(name = "SPECS", length = 48)
	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	@Column(name = "SPECS_ABBR", length = 20)
	public String getSpecsAbbr() {
		return this.specsAbbr;
	}

	public void setSpecsAbbr(String specsAbbr) {
		this.specsAbbr = specsAbbr;
	}

	@Column(name = "FACTORY", length = 40)
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Column(name = "FCOUNTRY", length = 2)
	public String getFcountry() {
		return this.fcountry;
	}

	public void setFcountry(String fcountry) {
		this.fcountry = fcountry;
	}

	@Column(name = "COUNTRY", length = 2)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "BILL_ID", length = 12)
	public String getBillId() {
		return this.billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	@Column(name = "BOOK_ID", precision = 4, scale = 0)
	public Short getBookId() {
		return this.bookId;
	}

	public void setBookId(Short bookId) {
		this.bookId = bookId;
	}

	@Column(name = "PLAN_QUAN", precision = 10)
	public Double getPlanQuan() {
		return this.planQuan;
	}

	public void setPlanQuan(Double planQuan) {
		this.planQuan = planQuan;
	}

	@Column(name = "PLAN_SUM", precision = 12, scale = 3)
	public Double getPlanSum() {
		return this.planSum;
	}

	public void setPlanSum(Double planSum) {
		this.planSum = planSum;
	}

	@Column(name = "IN_QUAN", precision = 10)
	public Double getInQuan() {
		return this.inQuan;
	}

	public void setInQuan(Double inQuan) {
		this.inQuan = inQuan;
	}

	@Column(name = "IN_SUM", precision = 12, scale = 3)
	public Double getInSum() {
		return this.inSum;
	}

	public void setInSum(Double inSum) {
		this.inSum = inSum;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_DATE", length = 7)
	public Date getPlanDate() {
		return this.planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OVER_DATE", length = 7)
	public Date getOverDate() {
		return this.overDate;
	}

	public void setOverDate(Date overDate) {
		this.overDate = overDate;
	}

}