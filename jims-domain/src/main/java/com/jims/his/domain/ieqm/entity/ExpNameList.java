package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpNameList entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_NAME_LIST", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"EXP_CODE", "EXP_SPEC", "STORAGE" }))
public class ExpNameList implements java.io.Serializable {

	// Fields

	private String id;
	private Integer expNo;
	private String expCode;
	private String expName;
	private String expSpec;
	private String units;
	private String storage;

	// Constructors

	/** default constructor */
	public ExpNameList() {
	}

	/** full constructor */
	public ExpNameList(Integer expNo, String expCode, String expName,
			String expSpec, String units, String storage) {
		this.expNo = expNo;
		this.expCode = expCode;
		this.expName = expName;
		this.expSpec = expSpec;
		this.units = units;
		this.storage = storage;
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

	@Column(name = "EXP_NO", precision = 6, scale = 0)
	public Integer getExpNo() {
		return this.expNo;
	}

	public void setExpNo(Integer expNo) {
		this.expNo = expNo;
	}

	@Column(name = "EXP_CODE", length = 20)
	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	@Column(name = "EXP_NAME", length = 100)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	@Column(name = "EXP_SPEC", length = 50)
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

	@Column(name = "STORAGE", length = 10)
	public String getStorage() {
		return this.storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

}