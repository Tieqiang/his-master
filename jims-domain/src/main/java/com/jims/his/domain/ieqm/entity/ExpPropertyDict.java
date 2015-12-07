package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpPropertyDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_PROPERTY_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "TOXI_CODE"))
public class ExpPropertyDict implements java.io.Serializable {

	// Fields

	private String id;
	private String toxiCode;
	private String toxiProperty;
	private String inputCode;
	private String inputCodeWb;
	private String serialNo;

	// Constructors

	/** default constructor */
	public ExpPropertyDict() {
	}

	/** full constructor */
	public ExpPropertyDict(String toxiCode, String toxiProperty,
			String inputCode, String inputCodeWb, String serialNo) {
		this.toxiCode = toxiCode;
		this.toxiProperty = toxiProperty;
		this.inputCode = inputCode;
		this.inputCodeWb = inputCodeWb;
		this.serialNo = serialNo;
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

	@Column(name = "TOXI_CODE", unique = true, length = 2)
	public String getToxiCode() {
		return this.toxiCode;
	}

	public void setToxiCode(String toxiCode) {
		this.toxiCode = toxiCode;
	}

	@Column(name = "TOXI_PROPERTY", length = 10)
	public String getToxiProperty() {
		return this.toxiProperty;
	}

	public void setToxiProperty(String toxiProperty) {
		this.toxiProperty = toxiProperty;
	}

	@Column(name = "INPUT_CODE", length = 8)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "INPUT_CODE_WB", length = 8)
	public String getInputCodeWb() {
		return this.inputCodeWb;
	}

	public void setInputCodeWb(String inputCodeWb) {
		this.inputCodeWb = inputCodeWb;
	}

	@Column(name = "SERIAL_NO", length = 8)
	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

}