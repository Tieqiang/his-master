package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * CostAttrDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "COST_ATTR_DICT", schema = "HTCA")
public class CostAttrDict implements java.io.Serializable {

	// Fields

	private String id;
	private String attrName;
	private String attrCode;
	private String inputCode;

	// Constructors

	/** default constructor */
	public CostAttrDict() {
	}

	/** full constructor */
	public CostAttrDict(String attrName, String attrCode, String inputCode) {
		this.attrName = attrName;
		this.attrCode = attrCode;
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

	@Column(name = "ATTR_NAME", length = 100)
	public String getAttrName() {
		return this.attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	@Column(name = "ATTR_CODE", length = 50)
	public String getAttrCode() {
		return this.attrCode;
	}

	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
	}

	@Column(name = "INPUT_CODE", length = 50)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

}