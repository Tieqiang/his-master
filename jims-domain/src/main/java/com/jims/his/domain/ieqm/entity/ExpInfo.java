package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_INFO", schema = "JIMS")
public class ExpInfo implements java.io.Serializable {

	// Fields

	private String id;
	private String expCode;
	private String expName;
	private String attr;
	private String specs;
	private String specsAbbr;
	private String units;
	private String country;
	private String factory;
	private Double tradePric;
	private Double retailPric;
	private String operator;
	private String storeId;

	// Constructors

	/** default constructor */
	public ExpInfo() {
	}

	/** full constructor */
	public ExpInfo(String expCode, String expName, String attr, String specs,
			String specsAbbr, String units, String country, String factory,
			Double tradePric, Double retailPric, String operator, String storeId) {
		this.expCode = expCode;
		this.expName = expName;
		this.attr = attr;
		this.specs = specs;
		this.specsAbbr = specsAbbr;
		this.units = units;
		this.country = country;
		this.factory = factory;
		this.tradePric = tradePric;
		this.retailPric = retailPric;
		this.operator = operator;
		this.storeId = storeId;
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

	@Column(name = "EXP_NAME", length = 100)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	@Column(name = "ATTR", length = 1)
	public String getAttr() {
		return this.attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	@Column(name = "SPECS", length = 48)
	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	@Column(name = "SPECS_ABBR", length = 20)
	public String getSpecsAbbr() {
		return this.specsAbbr;
	}

	public void setSpecsAbbr(String specsAbbr) {
		this.specsAbbr = specsAbbr;
	}

	@Column(name = "UNITS", length = 4)
	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Column(name = "COUNTRY", length = 2)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "FACTORY", length = 4)
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Column(name = "TRADE_PRIC", precision = 12, scale = 3)
	public Double getTradePric() {
		return this.tradePric;
	}

	public void setTradePric(Double tradePric) {
		this.tradePric = tradePric;
	}

	@Column(name = "RETAIL_PRIC", precision = 12, scale = 3)
	public Double getRetailPric() {
		return this.retailPric;
	}

	public void setRetailPric(Double retailPric) {
		this.retailPric = retailPric;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "STORE_ID", length = 10)
	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

}