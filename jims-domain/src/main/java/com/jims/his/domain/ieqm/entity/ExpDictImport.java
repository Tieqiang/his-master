package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpDictImport entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_DICT_IMPORT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORAGE_CODE", "EXP_CODE" }))
public class ExpDictImport implements java.io.Serializable {

	// Fields

	private String id;
	private String storageCode;
	private String stroageName;
	private String expCode;
	private String expName;
	private String expSpec;
	private String units;
	private String inputCode;
	private String inputCodeWb;
	private Double price;
	private Double quantity;

	// Constructors

	/** default constructor */
	public ExpDictImport() {
	}

	/** full constructor */
	public ExpDictImport(String storageCode, String stroageName,
			String expCode, String expName, String expSpec, String units,
			String inputCode, String inputCodeWb, Double price, Double quantity) {
		this.storageCode = storageCode;
		this.stroageName = stroageName;
		this.expCode = expCode;
		this.expName = expName;
		this.expSpec = expSpec;
		this.units = units;
		this.inputCode = inputCode;
		this.inputCodeWb = inputCodeWb;
		this.price = price;
		this.quantity = quantity;
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

	@Column(name = "STORAGE_CODE", length = 10)
	public String getStorageCode() {
		return this.storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	@Column(name = "STROAGE_NAME", length = 40)
	public String getStroageName() {
		return this.stroageName;
	}

	public void setStroageName(String stroageName) {
		this.stroageName = stroageName;
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

	@Column(name = "PRICE", precision = 12, scale = 4)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "QUANTITY", precision = 12, scale = 4)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

}