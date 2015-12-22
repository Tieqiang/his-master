package com.jims.his.domain.ieqm.entity;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * ExpPriceList entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_PRICE_LIST", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"EXP_CODE", "EXP_SPEC", "FIRM_ID", "START_DATE" }))
public class ExpPriceList implements java.io.Serializable {

	// Fields

	private String id;
	private String expCode;
	private String expSpec;
	private String firmId;
	private String units;
	private Double tradePrice;
	private Double retailPrice;
	private Integer amountPerPackage;
	private String minSpec;
	private String minUnits;
	private String classOnInpRcpt;
	private String classOnOutpRcpt;
	private String classOnReckoning;
	private String subjCode;
	private String classOnMr;
	private Date startDate;
	private Date stopDate;
	private String memos;
	private Double maxRetailPrice;
	private String materialCode;
	private String operator;
	private String permitNo;
	private Date permitDate;
	private String registerNo;
	private Date registerDate;
	private String fdaOrCeNo;
	private Date fdaOrCeDate;
	private String otherNo;
	private Date otherDate;
    private String hospitalId;
    @Transient
    private String expName;
    @Transient
    private String priceRatio;
    @Transient
    private String stopPrice;
    @Transient
    private String columnProtect;
	// Constructors

	/** default constructor */
	public ExpPriceList() {
	}

	/** full constructor */
	public ExpPriceList(String expCode, String expSpec, String firmId,
			String units, Double tradePrice, Double retailPrice,
			Integer amountPerPackage, String minSpec, String minUnits,
			String classOnInpRcpt, String classOnOutpRcpt,
			String classOnReckoning, String subjCode, String classOnMr,
			Date startDate, Date stopDate, String memos, Double maxRetailPrice,
			String materialCode, String operator, String permitNo,
			Date permitDate, String registerNo, Date registerDate,
			String fdaOrCeNo, Date fdaOrCeDate, String otherNo, Date otherDate, String hospitalId) {
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.firmId = firmId;
		this.units = units;
		this.tradePrice = tradePrice;
		this.retailPrice = retailPrice;
		this.amountPerPackage = amountPerPackage;
		this.minSpec = minSpec;
		this.minUnits = minUnits;
		this.classOnInpRcpt = classOnInpRcpt;
		this.classOnOutpRcpt = classOnOutpRcpt;
		this.classOnReckoning = classOnReckoning;
		this.subjCode = subjCode;
		this.classOnMr = classOnMr;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.memos = memos;
		this.maxRetailPrice = maxRetailPrice;
		this.materialCode = materialCode;
		this.operator = operator;
		this.permitNo = permitNo;
		this.permitDate = permitDate;
		this.registerNo = registerNo;
		this.registerDate = registerDate;
		this.fdaOrCeNo = fdaOrCeNo;
		this.fdaOrCeDate = fdaOrCeDate;
		this.otherNo = otherNo;
		this.otherDate = otherDate;
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

	@Column(name = "FIRM_ID", length = 10)
	public String getFirmId() {
		return this.firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	@Column(name = "UNITS", length = 8)
	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
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

	@Column(name = "AMOUNT_PER_PACKAGE", precision = 5, scale = 0)
	public Integer getAmountPerPackage() {
		return this.amountPerPackage;
	}

	public void setAmountPerPackage(Integer amountPerPackage) {
		this.amountPerPackage = amountPerPackage;
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

	@Column(name = "CLASS_ON_INP_RCPT", length = 1)
	public String getClassOnInpRcpt() {
		return this.classOnInpRcpt;
	}

	public void setClassOnInpRcpt(String classOnInpRcpt) {
		this.classOnInpRcpt = classOnInpRcpt;
	}

	@Column(name = "CLASS_ON_OUTP_RCPT", length = 1)
	public String getClassOnOutpRcpt() {
		return this.classOnOutpRcpt;
	}

	public void setClassOnOutpRcpt(String classOnOutpRcpt) {
		this.classOnOutpRcpt = classOnOutpRcpt;
	}

	@Column(name = "CLASS_ON_RECKONING", length = 3)
	public String getClassOnReckoning() {
		return this.classOnReckoning;
	}

	public void setClassOnReckoning(String classOnReckoning) {
		this.classOnReckoning = classOnReckoning;
	}

	@Column(name = "SUBJ_CODE", length = 4)
	public String getSubjCode() {
		return this.subjCode;
	}

	public void setSubjCode(String subjCode) {
		this.subjCode = subjCode;
	}

	@Column(name = "CLASS_ON_MR", length = 4)
	public String getClassOnMr() {
		return this.classOnMr;
	}

	public void setClassOnMr(String classOnMr) {
		this.classOnMr = classOnMr;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "STOP_DATE", length = 7)
	public Date getStopDate() {
		return this.stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	@Column(name = "MEMOS", length = 20)
	public String getMemos() {
		return this.memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	@Column(name = "MAX_RETAIL_PRICE", precision = 10, scale = 4)
	public Double getMaxRetailPrice() {
		return this.maxRetailPrice;
	}

	public void setMaxRetailPrice(Double maxRetailPrice) {
		this.maxRetailPrice = maxRetailPrice;
	}

	@Column(name = "MATERIAL_CODE", length = 20)
	public String getMaterialCode() {
		return this.materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "PERMIT_NO", length = 20)
	public String getPermitNo() {
		return this.permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PERMIT_DATE", length = 7)
	public Date getPermitDate() {
		return this.permitDate;
	}

	public void setPermitDate(Date permitDate) {
		this.permitDate = permitDate;
	}

	@Column(name = "REGISTER_NO", length = 20)
	public String getRegisterNo() {
		return this.registerNo;
	}

	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REGISTER_DATE", length = 7)
	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	@Column(name = "FDA_OR_CE_NO", length = 20)
	public String getFdaOrCeNo() {
		return this.fdaOrCeNo;
	}

	public void setFdaOrCeNo(String fdaOrCeNo) {
		this.fdaOrCeNo = fdaOrCeNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FDA_OR_CE_DATE", length = 7)
	public Date getFdaOrCeDate() {
		return this.fdaOrCeDate;
	}

	public void setFdaOrCeDate(Date fdaOrCeDate) {
		this.fdaOrCeDate = fdaOrCeDate;
	}

	@Column(name = "OTHER_NO", length = 20)
	public String getOtherNo() {
		return this.otherNo;
	}

	public void setOtherNo(String otherNo) {
		this.otherNo = otherNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OTHER_DATE", length = 7)
	public Date getOtherDate() {
		return this.otherDate;
	}

	public void setOtherDate(Date otherDate) {
		this.otherDate = otherDate;
	}

    @Column(name = "HOSPITAL_ID", length = 64)
    public String getHospitalId() {return hospitalId;}

    public void setHospitalId(String hospitalId) {this.hospitalId = hospitalId;}

    @Transient
    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    @Transient
    public String getPriceRatio() {
        return priceRatio;
    }

    public void setPriceRatio(String priceRatio) {
        this.priceRatio = priceRatio;
    }

    @Transient
    public String getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    @Transient
    public String getColumnProtect() {
        return columnProtect;
    }

    public void setColumnProtect(String columnProtect) {
        this.columnProtect = columnProtect;
    }
}