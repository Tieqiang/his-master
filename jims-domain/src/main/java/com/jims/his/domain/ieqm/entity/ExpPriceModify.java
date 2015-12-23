package com.jims.his.domain.ieqm.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpPriceModify entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_PRICE_MODIFY", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"EXP_CODE", "EXP_SPEC", "FIRM_ID", "NOTICE_EFFICIENT_DATE" }))
public class ExpPriceModify implements java.io.Serializable {

	// Fields

	private String id;
	private String expCode;
	private String expSpec;
	private String units;
	private String firmId;
	private String minSpec;
	private String minUnits;
	private Double originalTradePrice;
	private Double currentTradePrice;
	private Double originalRetailPrice;
	private Double currentRetailPrice;
	private Date noticeEfficientDate;
	private Date actualEfficientDate;
	private String modifyCredential;
	private String expName;
	private String modifyMan;
	private String materialCode;
    private String hospitalId;

	// Constructors

	/** default constructor */
	public ExpPriceModify() {
	}

	/** full constructor */
	public ExpPriceModify(String expCode, String expSpec, String units,
			String firmId, String minSpec, String minUnits,
			Double originalTradePrice, Double currentTradePrice,
			Double originalRetailPrice, Double currentRetailPrice,
			Date noticeEfficientDate, Date actualEfficientDate,
			String modifyCredential, String expName, String modifyMan,
			String materialCode, String hospitalId) {
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.units = units;
		this.firmId = firmId;
		this.minSpec = minSpec;
		this.minUnits = minUnits;
		this.originalTradePrice = originalTradePrice;
		this.currentTradePrice = currentTradePrice;
		this.originalRetailPrice = originalRetailPrice;
		this.currentRetailPrice = currentRetailPrice;
		this.noticeEfficientDate = noticeEfficientDate;
		this.actualEfficientDate = actualEfficientDate;
		this.modifyCredential = modifyCredential;
		this.expName = expName;
		this.modifyMan = modifyMan;
		this.materialCode = materialCode;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NOTICE_EFFICIENT_DATE", length = 7)
	public Date getNoticeEfficientDate() {
		return this.noticeEfficientDate;
	}

	public void setNoticeEfficientDate(Date noticeEfficientDate) {
		this.noticeEfficientDate = noticeEfficientDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTUAL_EFFICIENT_DATE", length = 7)
	public Date getActualEfficientDate() {
		return this.actualEfficientDate;
	}

	public void setActualEfficientDate(Date actualEfficientDate) {
		this.actualEfficientDate = actualEfficientDate;
	}

	@Column(name = "MODIFY_CREDENTIAL", length = 50)
	public String getModifyCredential() {
		return this.modifyCredential;
	}

	public void setModifyCredential(String modifyCredential) {
		this.modifyCredential = modifyCredential;
	}

	@Column(name = "EXP_NAME", length = 100)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	@Column(name = "MODIFY_MAN", length = 20)
	public String getModifyMan() {
		return this.modifyMan;
	}

	public void setModifyMan(String modifyMan) {
		this.modifyMan = modifyMan;
	}

	@Column(name = "MATERIAL_CODE", length = 20)
	public String getMaterialCode() {
		return this.materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

    @Column(name = "HOSPITAL_ID", length = 64)
    public String getHospitalId() {return hospitalId;}

    public void setHospitalId(String hospitalId) {this.hospitalId = hospitalId;}
}