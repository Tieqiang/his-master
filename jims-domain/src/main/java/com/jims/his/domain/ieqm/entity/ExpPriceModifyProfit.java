package com.jims.his.domain.ieqm.entity;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * ExpPriceModifyProfit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_PRICE_MODIFY_PROFIT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORAGE", "EXP_CODE", "EXP_SPEC", "FIRM_ID", "ACTUAL_EFFICIENT_DATE" }))
public class ExpPriceModifyProfit implements java.io.Serializable {

	// Fields

	private String id;
	private String storage;
	private String expCode;
	private String expSpec;
	private String units;
	private String firmId;
	private Double quantity;
	private Double originalTradePrice;
	private Double currentTradePrice;
	private Double tradePriceProfit;
	private Double originalRetailPrice;
	private Double currentRetailPrice;
	private Double retailPriceProfit;
	private Date actualEfficientDate;
	private String expName;
    private String hospitalId ;
    @Transient
    private String storageName;
	// Constructors

	/** default constructor */
	public ExpPriceModifyProfit() {
	}

	/** full constructor */
	public ExpPriceModifyProfit(String storage, String expCode, String expSpec,
                                String units, String firmId, Double quantity,
                                Double originalTradePrice, Double currentTradePrice,
                                Double tradePriceProfit, Double originalRetailPrice,
                                Double currentRetailPrice, Double retailPriceProfit,
                                Date actualEfficientDate, String expName, String hospitalId) {
		this.storage = storage;
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.units = units;
		this.firmId = firmId;
		this.quantity = quantity;
		this.originalTradePrice = originalTradePrice;
		this.currentTradePrice = currentTradePrice;
		this.tradePriceProfit = tradePriceProfit;
		this.originalRetailPrice = originalRetailPrice;
		this.currentRetailPrice = currentRetailPrice;
		this.retailPriceProfit = retailPriceProfit;
		this.actualEfficientDate = actualEfficientDate;
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

	@Column(name = "QUANTITY", precision = 12)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "ORIGINAL_TRADE_PRICE", precision = 10, scale = 4)
	public Double getOriginalTradePrice() {
		return this.originalTradePrice;
	}

	public void setOriginalTradePrice(Double originalTradePrice) {
		this.originalTradePrice = originalTradePrice;
	}

	@Column(name = "CURRENT_TRADE_PRICE", precision = 10, scale = 4)
	public Double getCurrentTradePrice() {
		return this.currentTradePrice;
	}

	public void setCurrentTradePrice(Double currentTradePrice) {
		this.currentTradePrice = currentTradePrice;
	}

	@Column(name = "TRADE_PRICE_PROFIT", precision = 12)
	public Double getTradePriceProfit() {
		return this.tradePriceProfit;
	}

	public void setTradePriceProfit(Double tradePriceProfit) {
		this.tradePriceProfit = tradePriceProfit;
	}

	@Column(name = "ORIGINAL_RETAIL_PRICE", precision = 10, scale = 4)
	public Double getOriginalRetailPrice() {
		return this.originalRetailPrice;
	}

	public void setOriginalRetailPrice(Double originalRetailPrice) {
		this.originalRetailPrice = originalRetailPrice;
	}

	@Column(name = "CURRENT_RETAIL_PRICE", precision = 10, scale = 4)
	public Double getCurrentRetailPrice() {
		return this.currentRetailPrice;
	}

	public void setCurrentRetailPrice(Double currentRetailPrice) {
		this.currentRetailPrice = currentRetailPrice;
	}

	@Column(name = "RETAIL_PRICE_PROFIT", precision = 12)
	public Double getRetailPriceProfit() {
		return this.retailPriceProfit;
	}

	public void setRetailPriceProfit(Double retailPriceProfit) {
		this.retailPriceProfit = retailPriceProfit;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTUAL_EFFICIENT_DATE", length = 7)
	public Date getActualEfficientDate() {
		return this.actualEfficientDate;
	}

	public void setActualEfficientDate(Date actualEfficientDate) {
		this.actualEfficientDate = actualEfficientDate;
	}

	@Column(name = "EXP_NAME", length = 100)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

    @Column(name="hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
    @Transient

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }
}