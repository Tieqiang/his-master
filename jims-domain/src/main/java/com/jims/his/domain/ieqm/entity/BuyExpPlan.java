package com.jims.his.domain.ieqm.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * BuyExpPlan entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BUY_EXP_PLAN", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"BUY_ID", "BUY_NO" }))
public class BuyExpPlan implements java.io.Serializable {

	// Fields

	private String id;
	private String buyId;
	private Short buyNo;
	private String expCode;
	private String expName;
	private String expSpec;
	private String units;
	private String expForm;
	private String toxiProperty;
	private Double dosePerUnit;
	private String doseUnits;
	private Boolean expIndicator;
	private String inputCode;
	private Double wantNumber;
	private String storer;
	private Double stockNumber;
	private String stockSupplier;
	private String buyer;
	private Double checkNumber;
	private String checkSupplier;
	private String checker;
	private Integer flag;
	private String packSpec;
	private String packUnit;
	private String firmId;
	private Double purchasePrice;
	private String storage;
	private Double stockquantityRef;
	private Double exportquantityRef;
    @Transient
    private Double planNumber;
    @Transient
    private Double retailPrice;

	// Constructors

	/** default constructor */
	public BuyExpPlan() {
	}

	/** minimal constructor */
	public BuyExpPlan(String storage) {
		this.storage = storage;
	}

	/** full constructor */
	public BuyExpPlan(String buyId, Short buyNo, String expCode,
			String expName, String expSpec, String units, String expForm,
			String toxiProperty, Double dosePerUnit, String doseUnits,
			Boolean expIndicator, String inputCode, Double wantNumber,
			String storer, Double stockNumber, String stockSupplier,
			String buyer, Double checkNumber, String checkSupplier,
			String checker, Integer flag, String packSpec, String packUnit,
			String firmId, Double purchasePrice, String storage,
			Double stockquantityRef, Double exportquantityRef) {
		this.buyId = buyId;
		this.buyNo = buyNo;
		this.expCode = expCode;
		this.expName = expName;
		this.expSpec = expSpec;
		this.units = units;
		this.expForm = expForm;
		this.toxiProperty = toxiProperty;
		this.dosePerUnit = dosePerUnit;
		this.doseUnits = doseUnits;
		this.expIndicator = expIndicator;
		this.inputCode = inputCode;
		this.wantNumber = wantNumber;
		this.storer = storer;
		this.stockNumber = stockNumber;
		this.stockSupplier = stockSupplier;
		this.buyer = buyer;
		this.checkNumber = checkNumber;
		this.checkSupplier = checkSupplier;
		this.checker = checker;
		this.flag = flag;
		this.packSpec = packSpec;
		this.packUnit = packUnit;
		this.firmId = firmId;
		this.purchasePrice = purchasePrice;
		this.storage = storage;
		this.stockquantityRef = stockquantityRef;
		this.exportquantityRef = exportquantityRef;
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

	@Column(name = "BUY_ID", length = 12)
	public String getBuyId() {
		return this.buyId;
	}

	public void setBuyId(String buyId) {
		this.buyId = buyId;
	}

	@Column(name = "BUY_NO", precision = 4, scale = 0)
	public Short getBuyNo() {
		return this.buyNo;
	}

	public void setBuyNo(Short buyNo) {
		this.buyNo = buyNo;
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

	@Column(name = "EXP_INDICATOR", precision = 1, scale = 0)
	public Boolean getExpIndicator() {
		return this.expIndicator;
	}

	public void setExpIndicator(Boolean expIndicator) {
		this.expIndicator = expIndicator;
	}

	@Column(name = "INPUT_CODE", length = 8)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "WANT_NUMBER", precision = 12)
	public Double getWantNumber() {
		return this.wantNumber;
	}

	public void setWantNumber(Double wantNumber) {
		this.wantNumber = wantNumber;
	}

	@Column(name = "STORER", length = 20)
	public String getStorer() {
		return this.storer;
	}

	public void setStorer(String storer) {
		this.storer = storer;
	}

	@Column(name = "STOCK_NUMBER", precision = 12)
	public Double getStockNumber() {
		return this.stockNumber;
	}

	public void setStockNumber(Double stockNumber) {
		this.stockNumber = stockNumber;
	}

	@Column(name = "STOCK_SUPPLIER", length = 10)
	public String getStockSupplier() {
		return this.stockSupplier;
	}

	public void setStockSupplier(String stockSupplier) {
		this.stockSupplier = stockSupplier;
	}

	@Column(name = "BUYER", length = 20)
	public String getBuyer() {
		return this.buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	@Column(name = "CHECK_NUMBER", precision = 12)
	public Double getCheckNumber() {
		return this.checkNumber;
	}

	public void setCheckNumber(Double checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Column(name = "CHECK_SUPPLIER", length = 10)
	public String getCheckSupplier() {
		return this.checkSupplier;
	}

	public void setCheckSupplier(String checkSupplier) {
		this.checkSupplier = checkSupplier;
	}

	@Column(name = "CHECKER", length = 20)
	public String getChecker() {
		return this.checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

    @Column(name = "FLAG", precision = 1, scale = 0)
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

	@Column(name = "PACK_SPEC", length = 20)
	public String getPackSpec() {
		return this.packSpec;
	}

	public void setPackSpec(String packSpec) {
		this.packSpec = packSpec;
	}

	@Column(name = "PACK_UNIT", length = 8)
	public String getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	@Column(name = "FIRM_ID", length = 10)
	public String getFirmId() {
		return this.firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	@Column(name = "PURCHASE_PRICE", precision = 10, scale = 4)
	public Double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	@Column(name = "STORAGE", nullable = false, length = 8)
	public String getStorage() {
		return this.storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	@Column(name = "STOCKQUANTITY_REF", precision = 12)
	public Double getStockquantityRef() {
		return this.stockquantityRef;
	}

	public void setStockquantityRef(Double stockquantityRef) {
		this.stockquantityRef = stockquantityRef;
	}

	@Column(name = "EXPORTQUANTITY_REF", precision = 12)
	public Double getExportquantityRef() {
		return this.exportquantityRef;
	}

	public void setExportquantityRef(Double exportquantityRef) {
		this.exportquantityRef = exportquantityRef;
	}

    @Transient
    public Double getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(Double planNumber) {
        this.planNumber = planNumber;
    }

    @Transient
    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }
}