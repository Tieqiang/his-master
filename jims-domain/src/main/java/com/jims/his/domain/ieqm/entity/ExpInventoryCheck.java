package com.jims.his.domain.ieqm.entity;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * ExpInventoryCheck entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_INVENTORY_CHECK", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CHECK_YEAR_MONTH", "STORAGE", "EXP_CODE", "EXP_SPEC", "BATCH_NO",
		"MIN_SPEC", "FIRM_ID" }))
public class ExpInventoryCheck implements java.io.Serializable {

	// Fields

	private String id;
	private Date checkYearMonth;
	private String storage;
	private String expCode;
	private String expSpec;
	private String units;
	private String firmId;
	private String batchNo;
	private String minSpec;
	private String minUnits;
	private String subStorage;
	private Double accountQuantity;
	private Double actualQuantity;
	private Double tradePrice;
	private Double retailPrice;
	private Integer recStatus;
	private Double purchasePrice;
	private String expName;
    private String hospitalId;
    @Transient
    private String expForm;
    @Transient
    private String packageSpec;
    @Transient
    private String packageUnits;
    @Transient
    private String location;
    @Transient
    private Double quantity;
    @Transient
    private Double paperAmount;
    @Transient
    private Double realAmount;
    @Transient
    private Double profitAmount;
    @Transient
    private String no;
	// Constructors

	/** default constructor */
	public ExpInventoryCheck() {
	}

	/** full constructor */
	public ExpInventoryCheck(Date checkYearMonth, String storage,
			String expCode, String expSpec, String units, String firmId,
			String batchNo, String minSpec, String minUnits, String subStorage,
			Double accountQuantity, Double actualQuantity, Double tradePrice,
			Double retailPrice, Integer recStatus, Double purchasePrice,
			String expName, String hospitalId) {
		this.checkYearMonth = checkYearMonth;
		this.storage = storage;
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.units = units;
		this.firmId = firmId;
		this.batchNo = batchNo;
		this.minSpec = minSpec;
		this.minUnits = minUnits;
		this.subStorage = subStorage;
		this.accountQuantity = accountQuantity;
		this.actualQuantity = actualQuantity;
		this.tradePrice = tradePrice;
		this.retailPrice = retailPrice;
		this.recStatus = recStatus;
		this.purchasePrice = purchasePrice;
		this.expName = expName;
        this.hospitalId = hospitalId;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "CHECK_YEAR_MONTH", length = 7)
	public Date getCheckYearMonth() {
		return this.checkYearMonth;
	}

	public void setCheckYearMonth(Date checkYearMonth) {
		this.checkYearMonth = checkYearMonth;
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

	@Column(name = "FIRM_ID", length = 10)
	public String getFirmId() {
		return this.firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	@Column(name = "BATCH_NO", length = 16)
	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	@Column(name = "MIN_SPEC", length = 20)
	public String getMinSpec() {
		return this.minSpec;
	}

	public void setMinSpec(String minSpec) {
		this.minSpec = minSpec;
	}

	@Column(name = "MIN_UNITS", length = 8)
	public String getMinUnits() {
		return this.minUnits;
	}

	public void setMinUnits(String minUnits) {
		this.minUnits = minUnits;
	}

	@Column(name = "SUB_STORAGE", length = 10)
	public String getSubStorage() {
		return this.subStorage;
	}

	public void setSubStorage(String subStorage) {
		this.subStorage = subStorage;
	}

	@Column(name = "ACCOUNT_QUANTITY", precision = 12)
	public Double getAccountQuantity() {
		return this.accountQuantity;
	}

	public void setAccountQuantity(Double accountQuantity) {
		this.accountQuantity = accountQuantity;
	}

	@Column(name = "ACTUAL_QUANTITY", precision = 12)
	public Double getActualQuantity() {
		return this.actualQuantity;
	}

	public void setActualQuantity(Double actualQuantity) {
		this.actualQuantity = actualQuantity;
	}

	@Column(name = "TRADE_PRICE", precision = 10, scale = 4)
	public Double getTradePrice() {
		return this.tradePrice;
	}

	public void setTradePrice(Double tradePrice) {
		this.tradePrice = tradePrice;
	}

	@Column(name = "RETAIL_PRICE", precision = 10, scale = 4)
	public Double getRetailPrice() {
		return this.retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	@Column(name = "REC_STATUS", precision = 1, scale = 0)
	public Integer getRecStatus() {
		return this.recStatus;
	}

	public void setRecStatus(Integer recStatus) {
		this.recStatus = recStatus;
	}

	@Column(name = "PURCHASE_PRICE", precision = 10, scale = 4)
	public Double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	@Column(name = "EXP_NAME", length = 100)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

    @Column(name = "HOSPITAL_ID", length = 64)
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
    @Transient
    public String getExpForm() {
        return expForm;
    }

    public void setExpForm(String expForm) {
        this.expForm = expForm;
    }

    @Transient
    public String getPackageSpec() {
        return packageSpec;
    }

    public void setPackageSpec(String packageSpec) {
        this.packageSpec = packageSpec;
    }

    @Transient
    public String getPackageUnits() {
        return packageUnits;
    }

    public void setPackageUnits(String packageUnits) {
        this.packageUnits = packageUnits;
    }

    @Transient
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Transient
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Transient
    public Double getPaperAmount() {
        return paperAmount;
    }

    public void setPaperAmount(Double paperAmount) {
        this.paperAmount = paperAmount;
    }

    @Transient
    public Double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(Double realAmount) {
        this.realAmount = realAmount;
    }

    @Transient
    public Double getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(Double profitAmount) {
        this.profitAmount = profitAmount;
    }

    @Transient
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}