package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpNameDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_NAME_DICT", schema = "JIMS", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "EXP_CODE", "EXP_NAME" }),
		@UniqueConstraint(columnNames = "EXP_NAME") })
public class ExpNameDict implements java.io.Serializable {

	// Fields

	private String id;
	private String expCode;
	private String expName;
	private Boolean stdIndicator;
	private String inputCode;
	private String inputCodeWb;

	// Constructors

	/** default constructor */
	public ExpNameDict() {
	}

	/** full constructor */
	public ExpNameDict(String expCode, String expName, Boolean stdIndicator,
			String inputCode, String inputCodeWb) {
		this.expCode = expCode;
		this.expName = expName;
		this.stdIndicator = stdIndicator;
		this.inputCode = inputCode;
		this.inputCodeWb = inputCodeWb;
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

	@Column(name = "EXP_NAME", unique = true, length = 100)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	@Column(name = "STD_INDICATOR", precision = 1, scale = 0)
	public Boolean getStdIndicator() {
		return this.stdIndicator;
	}

	public void setStdIndicator(Boolean stdIndicator) {
		this.stdIndicator = stdIndicator;
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

}