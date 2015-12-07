package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpPatternDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_PATTERN_DETAIL", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"EXP_CODE", "EXP_SPEC", "FIRM_ID", "SERIAL_NO" }))
public class ExpPatternDetail implements java.io.Serializable {

	// Fields

	private String id;
	private String expCode;
	private String expSpec;
	private String expUnits;
	private String firmId;
	private Byte serialNo;
	private String singleCode;
	private String singleSpec;
	private String singleUnits;
	private String singleFirmId;
	private Double amount;
	private String docStatus;

	// Constructors

	/** default constructor */
	public ExpPatternDetail() {
	}

	/** full constructor */
	public ExpPatternDetail(String expCode, String expSpec, String expUnits,
			String firmId, Byte serialNo, String singleCode, String singleSpec,
			String singleUnits, String singleFirmId, Double amount,
			String docStatus) {
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.expUnits = expUnits;
		this.firmId = firmId;
		this.serialNo = serialNo;
		this.singleCode = singleCode;
		this.singleSpec = singleSpec;
		this.singleUnits = singleUnits;
		this.singleFirmId = singleFirmId;
		this.amount = amount;
		this.docStatus = docStatus;
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

	@Column(name = "EXP_UNITS", length = 8)
	public String getExpUnits() {
		return this.expUnits;
	}

	public void setExpUnits(String expUnits) {
		this.expUnits = expUnits;
	}

	@Column(name = "FIRM_ID", length = 10)
	public String getFirmId() {
		return this.firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	@Column(name = "SERIAL_NO", precision = 2, scale = 0)
	public Byte getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(Byte serialNo) {
		this.serialNo = serialNo;
	}

	@Column(name = "SINGLE_CODE", length = 20)
	public String getSingleCode() {
		return this.singleCode;
	}

	public void setSingleCode(String singleCode) {
		this.singleCode = singleCode;
	}

	@Column(name = "SINGLE_SPEC", length = 20)
	public String getSingleSpec() {
		return this.singleSpec;
	}

	public void setSingleSpec(String singleSpec) {
		this.singleSpec = singleSpec;
	}

	@Column(name = "SINGLE_UNITS", length = 8)
	public String getSingleUnits() {
		return this.singleUnits;
	}

	public void setSingleUnits(String singleUnits) {
		this.singleUnits = singleUnits;
	}

	@Column(name = "SINGLE_FIRM_ID", length = 10)
	public String getSingleFirmId() {
		return this.singleFirmId;
	}

	public void setSingleFirmId(String singleFirmId) {
		this.singleFirmId = singleFirmId;
	}

	@Column(name = "AMOUNT", precision = 6)
	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(name = "DOC_STATUS", length = 1)
	public String getDocStatus() {
		return this.docStatus;
	}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}

}