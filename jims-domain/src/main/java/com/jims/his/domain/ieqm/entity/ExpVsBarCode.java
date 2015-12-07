package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpVsBarCode entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_VS_BAR_CODE", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "BAR_CODE"))
public class ExpVsBarCode implements java.io.Serializable {

	// Fields

	private String id;
	private String barCode;
	private String expCode;
	private Integer itemNo;
	private String expSpec;
	private String units;
	private String expName;

	// Constructors

	/** default constructor */
	public ExpVsBarCode() {
	}

	/** full constructor */
	public ExpVsBarCode(String barCode, String expCode, Integer itemNo,
			String expSpec, String units, String expName) {
		this.barCode = barCode;
		this.expCode = expCode;
		this.itemNo = itemNo;
		this.expSpec = expSpec;
		this.units = units;
		this.expName = expName;
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

	@Column(name = "BAR_CODE", unique = true, length = 40)
	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
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

	@Column(name = "EXP_NAME", length = 40)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

}