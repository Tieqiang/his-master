package com.jims.his.domain.ieqm.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * ExpStorageProfile entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_STORAGE_PROFILE", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORAGE", "EXP_CODE", "EXP_SPEC" }))
public class ExpStorageProfile implements java.io.Serializable {

	// Fields

	private String id;
	private String storage;
	private String expCode;
	private String expSpec;
	private String units;
	private Integer amountPerPackage;
	private String packageUnits;
	private Integer upperLevel;
	private Integer lowLevel;
	private String location;
	private String supplier;
    @Transient
    private String expName;
    @Transient
    private String expForm;
	// Constructors

	/** default constructor */
	public ExpStorageProfile() {
	}

	/** full constructor */
	public ExpStorageProfile(String storage, String expCode, String expSpec,
			String units, Integer amountPerPackage, String packageUnits,
			Integer upperLevel, Integer lowLevel, String location,
			String supplier) {
		this.storage = storage;
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.units = units;
		this.amountPerPackage = amountPerPackage;
		this.packageUnits = packageUnits;
		this.upperLevel = upperLevel;
		this.lowLevel = lowLevel;
		this.location = location;
		this.supplier = supplier;
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

	@Column(name = "STORAGE", length = 8)
	public String getStorage() {
		return this.storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	@Column(name = "EXP_CODE", length = 20)
	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
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

	@Column(name = "AMOUNT_PER_PACKAGE", precision = 5, scale = 0)
	public Integer getAmountPerPackage() {
		return this.amountPerPackage;
	}

	public void setAmountPerPackage(Integer amountPerPackage) {
		this.amountPerPackage = amountPerPackage;
	}

	@Column(name = "PACKAGE_UNITS", length = 8)
	public String getPackageUnits() {
		return this.packageUnits;
	}

	public void setPackageUnits(String packageUnits) {
		this.packageUnits = packageUnits;
	}

	@Column(name = "UPPER_LEVEL", precision = 6, scale = 0)
	public Integer getUpperLevel() {
		return this.upperLevel;
	}

	public void setUpperLevel(Integer upperLevel) {
		this.upperLevel = upperLevel;
	}

	@Column(name = "LOW_LEVEL", precision = 6, scale = 0)
	public Integer getLowLevel() {
		return this.lowLevel;
	}

	public void setLowLevel(Integer lowLevel) {
		this.lowLevel = lowLevel;
	}

	@Column(name = "LOCATION", length = 10)
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "SUPPLIER", length = 10)
	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

    @Transient
    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    @Transient
    public String getExpForm() {
        return expForm;
    }

    public void setExpForm(String expForm) {
        this.expForm = expForm;
    }
}