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
 * ExpExportDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_EXPORT_DETAIL", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"DOCUMENT_NO", "ITEM_NO","EXP_CODE","EXP_SPEC","FIRM_ID" }))
public class ExpExportDetail implements java.io.Serializable {

	// Fields

	private String id;
	private String documentNo;
	private Integer itemNo;
	private String expCode;
	private String expSpec;
	private String units;
	private String batchNo;
	private Date expireDate;
	private String firmId;
	private String expForm;
	private String importDocumentNo;
	private Double purchasePrice;
	private Double tradePrice;
	private Double retailPrice;
	private String packageSpec;
	private Double quantity;
	private String packageUnits;
	private Double subPackage1;
	private String subPackageUnits1;
	private String subPackageSpec1;
	private Double subPackage2;
	private String subPackageUnits2;
	private String subPackageSpec2;
	private Double inventory;
	private Date producedate;
	private Date disinfectdate;
	private Integer killflag;
	private Integer recFlag;
	private String recOperator;
	private Date recDate;
	private String assignCode;
	private String bigCode;
	private String bigSpec;
	private String bigFirmId;
	private String expSgtp;
	private String memo;
    private String hospitalId ;


	// Constructors

	/** default constructor */
	public ExpExportDetail() {
	}

	/** full constructor */
	public ExpExportDetail(String documentNo, Integer itemNo, String expCode,
                           String expSpec, String units, String batchNo, Date expireDate,
                           String firmId, String expForm, String importDocumentNo,
                           Double purchasePrice, Double tradePrice, Double retailPrice,
                           String packageSpec, Double quantity, String packageUnits,
                           Double subPackage1, String subPackageUnits1,
                           String subPackageSpec1, Double subPackage2,
                           String subPackageUnits2, String subPackageSpec2, Double inventory,
                           Date producedate, Date disinfectdate, Integer killflag,
                           Integer recFlag, String recOperator, Date recDate, String assignCode,
                           String bigCode, String bigSpec, String bigFirmId, String expSgtp,
                           String memo, String hospitalId) {
		this.documentNo = documentNo;
		this.itemNo = itemNo;
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.units = units;
		this.batchNo = batchNo;
		this.expireDate = expireDate;
		this.firmId = firmId;
		this.expForm = expForm;
		this.importDocumentNo = importDocumentNo;
		this.purchasePrice = purchasePrice;
		this.tradePrice = tradePrice;
		this.retailPrice = retailPrice;
		this.packageSpec = packageSpec;
		this.quantity = quantity;
		this.packageUnits = packageUnits;
		this.subPackage1 = subPackage1;
		this.subPackageUnits1 = subPackageUnits1;
		this.subPackageSpec1 = subPackageSpec1;
		this.subPackage2 = subPackage2;
		this.subPackageUnits2 = subPackageUnits2;
		this.subPackageSpec2 = subPackageSpec2;
		this.inventory = inventory;
		this.producedate = producedate;
		this.disinfectdate = disinfectdate;
		this.killflag = killflag;
		this.recFlag = recFlag;
		this.recOperator = recOperator;
		this.recDate = recDate;
		this.assignCode = assignCode;
		this.bigCode = bigCode;
		this.bigSpec = bigSpec;
		this.bigFirmId = bigFirmId;
		this.expSgtp = expSgtp;
		this.memo = memo;
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

	@Column(name = "DOCUMENT_NO", length = 10)
	public String getDocumentNo() {
		return this.documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	@Column(name = "ITEM_NO", precision = 4, scale = 0)
	public Integer getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(Integer itemNo) {
		this.itemNo = itemNo;
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

	@Column(name = "BATCH_NO", length = 16)
	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRE_DATE", length = 7)
	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	@Column(name = "FIRM_ID", length = 10)
	public String getFirmId() {
		return this.firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	@Column(name = "EXP_FORM", length = 30)
	public String getExpForm() {
		return this.expForm;
	}

	public void setExpForm(String expForm) {
		this.expForm = expForm;
	}

	@Column(name = "IMPORT_DOCUMENT_NO", length = 10)
	public String getImportDocumentNo() {
		return this.importDocumentNo;
	}

	public void setImportDocumentNo(String importDocumentNo) {
		this.importDocumentNo = importDocumentNo;
	}

	@Column(name = "PURCHASE_PRICE", precision = 10, scale = 4)
	public Double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
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

	@Column(name = "PACKAGE_SPEC", length = 20)
	public String getPackageSpec() {
		return this.packageSpec;
	}

	public void setPackageSpec(String packageSpec) {
		this.packageSpec = packageSpec;
	}

	@Column(name = "QUANTITY", precision = 12)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "PACKAGE_UNITS", length = 8)
	public String getPackageUnits() {
		return this.packageUnits;
	}

	public void setPackageUnits(String packageUnits) {
		this.packageUnits = packageUnits;
	}

	@Column(name = "SUB_PACKAGE_1", precision = 12)
	public Double getSubPackage1() {
		return this.subPackage1;
	}

	public void setSubPackage1(Double subPackage1) {
		this.subPackage1 = subPackage1;
	}

	@Column(name = "SUB_PACKAGE_UNITS_1", length = 8)
	public String getSubPackageUnits1() {
		return this.subPackageUnits1;
	}

	public void setSubPackageUnits1(String subPackageUnits1) {
		this.subPackageUnits1 = subPackageUnits1;
	}

	@Column(name = "SUB_PACKAGE_SPEC_1", length = 20)
	public String getSubPackageSpec1() {
		return this.subPackageSpec1;
	}

	public void setSubPackageSpec1(String subPackageSpec1) {
		this.subPackageSpec1 = subPackageSpec1;
	}

	@Column(name = "SUB_PACKAGE_2", precision = 12)
	public Double getSubPackage2() {
		return this.subPackage2;
	}

	public void setSubPackage2(Double subPackage2) {
		this.subPackage2 = subPackage2;
	}

	@Column(name = "SUB_PACKAGE_UNITS_2", length = 8)
	public String getSubPackageUnits2() {
		return this.subPackageUnits2;
	}

	public void setSubPackageUnits2(String subPackageUnits2) {
		this.subPackageUnits2 = subPackageUnits2;
	}

	@Column(name = "SUB_PACKAGE_SPEC_2", length = 20)
	public String getSubPackageSpec2() {
		return this.subPackageSpec2;
	}

	public void setSubPackageSpec2(String subPackageSpec2) {
		this.subPackageSpec2 = subPackageSpec2;
	}

	@Column(name = "INVENTORY", precision = 12)
	public Double getInventory() {
		return this.inventory;
	}

	public void setInventory(Double inventory) {
		this.inventory = inventory;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PRODUCEDATE", length = 7)
	public Date getProducedate() {
		return this.producedate;
	}

	public void setProducedate(Date producedate) {
		this.producedate = producedate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DISINFECTDATE", length = 7)
	public Date getDisinfectdate() {
		return this.disinfectdate;
	}

	public void setDisinfectdate(Date disinfectdate) {
		this.disinfectdate = disinfectdate;
	}

	@Column(name = "KILLFLAG", precision = 1, scale = 0)
	public Integer getKillflag() {
		return this.killflag;
	}

	public void setKillflag(Integer killflag) {
		this.killflag = killflag;
	}

	@Column(name = "REC_FLAG", precision = 4, scale = 0)
	public Integer getRecFlag() {
		return this.recFlag;
	}

	public void setRecFlag(Integer recFlag) {
		this.recFlag = recFlag;
	}

	@Column(name = "REC_OPERATOR", length = 20)
	public String getRecOperator() {
		return this.recOperator;
	}

	public void setRecOperator(String recOperator) {
		this.recOperator = recOperator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REC_DATE", length = 7)
	public Date getRecDate() {
		return this.recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	@Column(name = "ASSIGN_CODE", length = 10)
	public String getAssignCode() {
		return this.assignCode;
	}

	public void setAssignCode(String assignCode) {
		this.assignCode = assignCode;
	}

	@Column(name = "BIG_CODE", length = 20)
	public String getBigCode() {
		return this.bigCode;
	}

	public void setBigCode(String bigCode) {
		this.bigCode = bigCode;
	}

	@Column(name = "BIG_SPEC", length = 20)
	public String getBigSpec() {
		return this.bigSpec;
	}

	public void setBigSpec(String bigSpec) {
		this.bigSpec = bigSpec;
	}

	@Column(name = "BIG_FIRM_ID", length = 10)
	public String getBigFirmId() {
		return this.bigFirmId;
	}

	public void setBigFirmId(String bigFirmId) {
		this.bigFirmId = bigFirmId;
	}

	@Column(name = "EXP_SGTP", length = 1)
	public String getExpSgtp() {
		return this.expSgtp;
	}

	public void setExpSgtp(String expSgtp) {
		this.expSgtp = expSgtp;
	}

	@Column(name = "MEMO", length = 50)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

    @Column(name = "hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}