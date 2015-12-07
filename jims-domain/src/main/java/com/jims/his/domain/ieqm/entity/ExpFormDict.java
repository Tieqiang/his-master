package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpFormDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_FORM_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"FORM_NAME", "STORAGE_CODE" }))
public class ExpFormDict implements java.io.Serializable {

	// Fields

	private String id;
	private Byte serialNo;
	private String expClass;
	private String formCode;
	private String formName;
	private String inputCode;
	private String storageCode;

	// Constructors

	/** default constructor */
	public ExpFormDict() {
	}

	/** full constructor */
	public ExpFormDict(Byte serialNo, String expClass, String formCode,
			String formName, String inputCode, String storageCode) {
		this.serialNo = serialNo;
		this.expClass = expClass;
		this.formCode = formCode;
		this.formName = formName;
		this.inputCode = inputCode;
		this.storageCode = storageCode;
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

	@Column(name = "SERIAL_NO", precision = 2, scale = 0)
	public Byte getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(Byte serialNo) {
		this.serialNo = serialNo;
	}

	@Column(name = "EXP_CLASS", length = 10)
	public String getExpClass() {
		return this.expClass;
	}

	public void setExpClass(String expClass) {
		this.expClass = expClass;
	}

	@Column(name = "FORM_CODE", length = 10)
	public String getFormCode() {
		return this.formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	@Column(name = "FORM_NAME", length = 30)
	public String getFormName() {
		return this.formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	@Column(name = "INPUT_CODE", length = 8)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "STORAGE_CODE", length = 10)
	public String getStorageCode() {
		return this.storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

}