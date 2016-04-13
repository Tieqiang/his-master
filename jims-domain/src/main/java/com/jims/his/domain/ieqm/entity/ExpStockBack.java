package com.jims.his.domain.ieqm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * ExpImportDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_STOCK_BACK", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"EXP_CODE","EXP_SPEC","UNITS","MIN_SPEC","MIN_UNITS","SUPPLIER_ID","PRODUCE_ID" }))
public class ExpStockBack implements java.io.Serializable {

	// Fields

	private String id;
	private String expCode;
	private String expSpec;
	private String units;
    private String minSpec;
    private String minUnits;
    private String expName;
    private String supplierId;
    private String supplier;
    private String produceId;
    private String produce;
	private Date expireDate;
	private String expForm;
	private Double tradePrice;
	private Double retailPrice;
	private Date produceDate;
    private String hospitalId ;
    private String backCode;
    private String operator;
    private Date registerDate;
    private String linkman;
    private String linkPhone;
    private String memo;
	// Constructors

	/** default constructor */
	public ExpStockBack() {
	}

    public ExpStockBack(String id, String expCode, String expSpec, String units, String minSpec, String minUnits, String expName, String supplierId, String supplier, String produceId, String produce, Date expireDate, String expForm, Double tradePrice, Double retailPrice, Date produceDate, String hospitalId, String backCode, String operator, Date registerDate, String linkman, String linkPhone, String memo) {
        this.id = id;
        this.expCode = expCode;
        this.expSpec = expSpec;
        this.units = units;
        this.minSpec = minSpec;
        this.minUnits = minUnits;
        this.expName = expName;
        this.supplierId = supplierId;
        this.supplier = supplier;
        this.produceId = produceId;
        this.produce = produce;
        this.expireDate = expireDate;
        this.expForm = expForm;
        this.tradePrice = tradePrice;
        this.retailPrice = retailPrice;
        this.produceDate = produceDate;
        this.hospitalId = hospitalId;
        this.backCode = backCode;
        this.operator = operator;
        this.registerDate = registerDate;
        this.linkman = linkman;
        this.linkPhone = linkPhone;
        this.memo = memo;
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

    @Column(name = "OPERATOR", length = 10)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRE_DATE", length = 7)
	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTER_DATE", length = 7)
    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    @Column(name = "EXP_FORM", length = 30)
	public String getExpForm() {
		return this.expForm;
	}

	public void setExpForm(String expForm) {
		this.expForm = expForm;
	}

    @Column(name = "BACK_CODE", length = 100)
    public String getBackCode() {
        return backCode;
    }

    public void setBackCode(String backCode) {
        this.backCode = backCode;
    }

    @Column(name = "MIN_SPEC", length = 20)
    public String getMinSpec() {
        return minSpec;
    }

    public void setMinSpec(String minSpec) {
        this.minSpec = minSpec;
    }

    @Column(name = "MIN_UNITS", length = 8)
    public String getMinUnits() {
        return minUnits;
    }

    public void setMinUnits(String minUnits) {
        this.minUnits = minUnits;
    }

    @Column(name = "EXP_NAME", length = 100)
    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    @Column(name = "SUPPLIER_ID", length = 10)
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Column(name = "SUPPLIER", length = 40)
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Column(name = "PRODUCE_ID", length = 10)
    public String getProduceId() {
        return produceId;
    }

    public void setProduceId(String produceId) {
        this.produceId = produceId;
    }

    @Column(name = "PRODUCE", length = 40)
    public String getProduce() {
        return produce;
    }

    public void setProduce(String produce) {
        this.produce = produce;
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PRODUCE_DATE", length = 7)
	public Date getProduceDate() {
		return this.produceDate;
	}

	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

    @Column(name = "hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Column(name = "LINKMAN")
    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    @Column(name = "LINK_PHONE")
    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    @Column(name = "MEMO")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}