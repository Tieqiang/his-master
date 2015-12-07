package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * FeeTypeDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "FEE_TYPE_DICT", schema = "HTCA")
public class FeeTypeDict implements java.io.Serializable {

	// Fields

	private String id;
	private String feeClassCode;
	private String feeClassName;
	private String incomeItemName;
	private String incomeTypeName;
	private String inputCode;
	private String hospitalId;

	// Constructors

	/** default constructor */
	public FeeTypeDict() {
	}

	/** full constructor */
	public FeeTypeDict(String feeClassCode, String feeClassName,
			String incomeItemName, String incomeTypeName, String inputCode,
			String hospitalId) {
		this.feeClassCode = feeClassCode;
		this.feeClassName = feeClassName;
		this.incomeItemName = incomeItemName;
		this.incomeTypeName = incomeTypeName;
		this.inputCode = inputCode;
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

	@Column(name = "FEE_CLASS_CODE", length = 10)
	public String getFeeClassCode() {
		return this.feeClassCode;
	}

	public void setFeeClassCode(String feeClassCode) {
		this.feeClassCode = feeClassCode;
	}

	@Column(name = "FEE_CLASS_NAME", length = 20)
	public String getFeeClassName() {
		return this.feeClassName;
	}

	public void setFeeClassName(String feeClassName) {
		this.feeClassName = feeClassName;
	}

	@Column(name = "INCOME_ITEM_NAME", length = 50)
	public String getIncomeItemName() {
		return this.incomeItemName;
	}

	public void setIncomeItemName(String incomeItemName) {
		this.incomeItemName = incomeItemName;
	}

	@Column(name = "INCOME_TYPE_NAME", length = 50)
	public String getIncomeTypeName() {
		return this.incomeTypeName;
	}

	public void setIncomeTypeName(String incomeTypeName) {
		this.incomeTypeName = incomeTypeName;
	}

	@Column(name = "INPUT_CODE", length = 5)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "HOSPITAL_ID", length = 64)
	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

}