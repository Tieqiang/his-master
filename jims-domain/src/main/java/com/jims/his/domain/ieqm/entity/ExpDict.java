package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"EXP_CODE", "EXP_SPEC", "EXP_INDICATOR" }))
public class ExpDict implements java.io.Serializable {

	// Fields

	private String id;
	private String expCode;
	private String expName;
	private String expSpec;
	private String units;
	private String expForm;
	private String toxiProperty;
	private Double dosePerUnit;
	private String doseUnits;
	private String inputCode;
	private Integer expIndicator;
	private String storageCode;
	private String singleGroupIndicator;

	// Constructors

	/** default constructor */
	public ExpDict() {
	}

	/** full constructor */
	public ExpDict(String expCode, String expName, String expSpec,
			String units, String expForm, String toxiProperty,
			Double dosePerUnit, String doseUnits, String inputCode,
			Integer expIndicator, String storageCode,
			String singleGroupIndicator) {
		this.expCode = expCode;
		this.expName = expName;
		this.expSpec = expSpec;
		this.units = units;
		this.expForm = expForm;
		this.toxiProperty = toxiProperty;
		this.dosePerUnit = dosePerUnit;
		this.doseUnits = doseUnits;
		this.inputCode = inputCode;
		this.expIndicator = expIndicator;
		this.storageCode = storageCode;
		this.singleGroupIndicator = singleGroupIndicator;
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

	@Column(name = "EXP_SPEC", length = 20)
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

	@Column(name = "EXP_FORM", length = 30)
	public String getExpForm() {
		return this.expForm;
	}

	public void setExpForm(String expForm) {
		this.expForm = expForm;
	}

	@Column(name = "TOXI_PROPERTY", length = 10)
	public String getToxiProperty() {
		return this.toxiProperty;
	}

	public void setToxiProperty(String toxiProperty) {
		this.toxiProperty = toxiProperty;
	}

	@Column(name = "DOSE_PER_UNIT", precision = 8, scale = 3)
	public Double getDosePerUnit() {
		return this.dosePerUnit;
	}

	public void setDosePerUnit(Double dosePerUnit) {
		this.dosePerUnit = dosePerUnit;
	}

	@Column(name = "DOSE_UNITS", length = 8)
	public String getDoseUnits() {
		return this.doseUnits;
	}

	public void setDoseUnits(String doseUnits) {
		this.doseUnits = doseUnits;
	}

	@Column(name = "INPUT_CODE", length = 8)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "EXP_INDICATOR", precision = 1, scale = 0)
	public Integer getExpIndicator() {
		return this.expIndicator;
	}

	public void setExpIndicator(Integer expIndicator) {
		this.expIndicator = expIndicator;
	}

	@Column(name = "STORAGE_CODE", length = 10)
	public String getStorageCode() {
		return this.storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	@Column(name = "SINGLE_GROUP_INDICATOR", length = 1)
	public String getSingleGroupIndicator() {
		return this.singleGroupIndicator;
	}

	public void setSingleGroupIndicator(String singleGroupIndicator) {
		this.singleGroupIndicator = singleGroupIndicator;
	}

}