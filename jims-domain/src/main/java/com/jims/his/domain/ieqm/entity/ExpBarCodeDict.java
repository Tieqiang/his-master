package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpBarCodeDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_BAR_CODE_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"EXP_CODE", "ITEM_NO", "EXP_SPEC", "UNITS" }))
public class ExpBarCodeDict implements java.io.Serializable {

	// Fields

	private String id;
	private String expCode;
	private Integer itemNo;
	private String expName;
	private String expSpec;
	private String units;
	private String flag;
	private String state;
	private String inputCode;

	// Constructors

	/** default constructor */
	public ExpBarCodeDict() {
	}

	/** full constructor */
	public ExpBarCodeDict(String expCode, Integer itemNo, String expName,
			String expSpec, String units, String flag, String state,
			String inputCode) {
		this.expCode = expCode;
		this.itemNo = itemNo;
		this.expName = expName;
		this.expSpec = expSpec;
		this.units = units;
		this.flag = flag;
		this.state = state;
		this.inputCode = inputCode;
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

	@Column(name = "ITEM_NO", precision = 5, scale = 0)
	public Integer getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
	}

	@Column(name = "EXP_NAME", length = 50)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	@Column(name = "EXP_SPEC", length = 40)
	public String getExpSpec() {
		return this.expSpec;
	}

	public void setExpSpec(String expSpec) {
		this.expSpec = expSpec;
	}

	@Column(name = "UNITS", length = 8)
	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Column(name = "FLAG", length = 1)
	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Column(name = "STATE", length = 6)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "INPUT_CODE", length = 20)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

}