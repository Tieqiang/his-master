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
 * ExpDrawProximo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_DRAW_PROXIMO", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"PROVIDE_STORAGE", "ITEM_NO", "APPLICANT_NO" }))
public class ExpDrawProximo implements java.io.Serializable {

	// Fields

	private String id;
	private String applicantStorage;
	private String provideStorage;
	private Short itemNo;
	private String expCode;
	private String expSpec;
	private String packageSpec;
	private Double quantity;
	private String packageUnits;
	private Date enterDateTime;
	private String applicantNo;
	private String applicationMan;
	private String auditingOperator;
	private Double auditingQuantity;

	// Constructors

	/** default constructor */
	public ExpDrawProximo() {
	}

	/** full constructor */
	public ExpDrawProximo(String applicantStorage, String provideStorage,
			Short itemNo, String expCode, String expSpec, String packageSpec,
			Double quantity, String packageUnits, Date enterDateTime,
			String applicantNo, String applicationMan, String auditingOperator,
			Double auditingQuantity) {
		this.applicantStorage = applicantStorage;
		this.provideStorage = provideStorage;
		this.itemNo = itemNo;
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.packageSpec = packageSpec;
		this.quantity = quantity;
		this.packageUnits = packageUnits;
		this.enterDateTime = enterDateTime;
		this.applicantNo = applicantNo;
		this.applicationMan = applicationMan;
		this.auditingOperator = auditingOperator;
		this.auditingQuantity = auditingQuantity;
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

	@Column(name = "APPLICANT_STORAGE", length = 8)
	public String getApplicantStorage() {
		return this.applicantStorage;
	}

	public void setApplicantStorage(String applicantStorage) {
		this.applicantStorage = applicantStorage;
	}

	@Column(name = "PROVIDE_STORAGE", length = 8)
	public String getProvideStorage() {
		return this.provideStorage;
	}

	public void setProvideStorage(String provideStorage) {
		this.provideStorage = provideStorage;
	}

	@Column(name = "ITEM_NO", precision = 4, scale = 0)
	public Short getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(Short itemNo) {
		this.itemNo = itemNo;
	}

	@Column(name = "EXP_CODE", length = 20)
	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	@Column(name = "EXP_SPEC", length = 20)
	public String getExpSpec() {
		return this.expSpec;
	}

	public void setExpSpec(String expSpec) {
		this.expSpec = expSpec;
	}

	@Column(name = "PACKAGE_SPEC", length = 20)
	public String getPackageSpec() {
		return this.packageSpec;
	}

	public void setPackageSpec(String packageSpec) {
		this.packageSpec = packageSpec;
	}

	@Column(name = "QUANTITY", precision = 12)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "PACKAGE_UNITS", length = 8)
	public String getPackageUnits() {
		return this.packageUnits;
	}

	public void setPackageUnits(String packageUnits) {
		this.packageUnits = packageUnits;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTER_DATE_TIME", length = 7)
	public Date getEnterDateTime() {
		return this.enterDateTime;
	}

	public void setEnterDateTime(Date enterDateTime) {
		this.enterDateTime = enterDateTime;
	}

	@Column(name = "APPLICANT_NO", length = 10)
	public String getApplicantNo() {
		return this.applicantNo;
	}

	public void setApplicantNo(String applicantNo) {
		this.applicantNo = applicantNo;
	}

	@Column(name = "APPLICATION_MAN", length = 20)
	public String getApplicationMan() {
		return this.applicationMan;
	}

	public void setApplicationMan(String applicationMan) {
		this.applicationMan = applicationMan;
	}

	@Column(name = "AUDITING_OPERATOR", length = 20)
	public String getAuditingOperator() {
		return this.auditingOperator;
	}

	public void setAuditingOperator(String auditingOperator) {
		this.auditingOperator = auditingOperator;
	}

	@Column(name = "AUDITING_QUANTITY", precision = 12)
	public Double getAuditingQuantity() {
		return this.auditingQuantity;
	}

	public void setAuditingQuantity(Double auditingQuantity) {
		this.auditingQuantity = auditingQuantity;
	}

}