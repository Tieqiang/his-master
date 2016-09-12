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
 * ExpImportDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_IMPORT_DETAIL", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"DOCUMENT_NO", "ITEM_NO","EXP_CODE","EXP_SPEC","FIRM_ID" }))
public class ExpImportDetail implements java.io.Serializable {

	// Fields

	private String id;
	private String documentNo;
	private Short itemNo;
	private String expCode;
	private String expSpec;
	private String units;
	private String batchNo;
	private Date expireDate;
	private String firmId;
	private String expForm;
	private Double purchasePrice;
	private Double tradePrice;
	private Double retailPrice;
	private Double discount;
	private String packageSpec;
	private Double quantity;
	private String packageUnits;
	private Double subPackage1;
	private String subPackageUnits1;
	private String subPackageSpec1;
	private Double subPackage2;
	private String subPackageUnits2;
	private String subPackageSpec2;
	private String invoiceNo;
	private Date invoiceDate;
	private String disburseRecNo;
	private Double disburseCount;
	private Double inventory;
	private String memo;
	private String registno;
	private String licenceno;
	private Date producedate;
	private Date disinfectdate;
	private Integer killflag;
	private Integer tallyFlag;
	private Date tallyDate;
	private String tallyOpertor;
	private String orderBatch;
	private Short tenderNo;
    private String hospitalId ;
    private String exportDocumentNo ;
	// Constructors

	/** default constructor */
	public ExpImportDetail() {
	}

	/** full constructor */
	public ExpImportDetail(String documentNo, Short itemNo, String expCode,
                           String expSpec, String units, String batchNo, Date expireDate,
                           String firmId, String expForm, Double purchasePrice,
                           Double tradePrice, Double retailPrice, Double discount,
                           String packageSpec, Double quantity, String packageUnits,
                           Double subPackage1, String subPackageUnits1,
                           String subPackageSpec1, Double subPackage2,
                           String subPackageUnits2, String subPackageSpec2, String invoiceNo,
                           Date invoiceDate, String disburseRecNo, Double disburseCount,
                           Double inventory, String memo, String registno, String licenceno,
                           Date producedate, Date disinfectdate, Integer killflag,
                           Integer tallyFlag, Date tallyDate, String tallyOpertor,
                           String orderBatch, Short tenderNo, String hospitalId, String exportDocumentNo) {
		this.documentNo = documentNo;
		this.itemNo = itemNo;
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.units = units;
		this.batchNo = batchNo;
		this.expireDate = expireDate;
		this.firmId = firmId;
		this.expForm = expForm;
		this.purchasePrice = purchasePrice;
		this.tradePrice = tradePrice;
		this.retailPrice = retailPrice;
		this.discount = discount;
		this.packageSpec = packageSpec;
		this.quantity = quantity;
		this.packageUnits = packageUnits;
		this.subPackage1 = subPackage1;
		this.subPackageUnits1 = subPackageUnits1;
		this.subPackageSpec1 = subPackageSpec1;
		this.subPackage2 = subPackage2;
		this.subPackageUnits2 = subPackageUnits2;
		this.subPackageSpec2 = subPackageSpec2;
		this.invoiceNo = invoiceNo;
		this.invoiceDate = invoiceDate;
		this.disburseRecNo = disburseRecNo;
		this.disburseCount = disburseCount;
		this.inventory = inventory;
		this.memo = memo;
		this.registno = registno;
		this.licenceno = licenceno;
		this.producedate = producedate;
		this.disinfectdate = disinfectdate;
		this.killflag = killflag;
		this.tallyFlag = tallyFlag;
		this.tallyDate = tallyDate;
		this.tallyOpertor = tallyOpertor;
		this.orderBatch = orderBatch;
		this.tenderNo = tenderNo;
        this.hospitalId = hospitalId;
        this.exportDocumentNo = exportDocumentNo;
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
	public Short getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(Short itemNo) {
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

	@Column(name = "DISCOUNT", precision = 8)
	public Double getDiscount() {
		return this.discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
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

	@Column(name = "INVOICE_NO", length = 10)
	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INVOICE_DATE", length = 7)
	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	@Column(name = "DISBURSE_REC_NO", length = 10)
	public String getDisburseRecNo() {
		return this.disburseRecNo;
	}

	public void setDisburseRecNo(String disburseRecNo) {
		this.disburseRecNo = disburseRecNo;
	}

	@Column(name = "DISBURSE_COUNT", precision = 12)
	public Double getDisburseCount() {
		return this.disburseCount;
	}

	public void setDisburseCount(Double disburseCount) {
		this.disburseCount = disburseCount;
	}

	@Column(name = "INVENTORY", precision = 12)
	public Double getInventory() {
		return this.inventory;
	}

	public void setInventory(Double inventory) {
		this.inventory = inventory;
	}

	@Column(name = "MEMO", length = 20)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "REGISTNO", length = 20)
	public String getRegistno() {
		return this.registno;
	}

	public void setRegistno(String registno) {
		this.registno = registno;
	}

	@Column(name = "LICENCENO", length = 20)
	public String getLicenceno() {
		return this.licenceno;
	}

	public void setLicenceno(String licenceno) {
		this.licenceno = licenceno;
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

	@Column(name = "TALLY_FLAG", precision = 1, scale = 0)
	public Integer getTallyFlag() {
		return this.tallyFlag;
	}

	public void setTallyFlag(Integer tallyFlag) {
		this.tallyFlag = tallyFlag;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TALLY_DATE", length = 7)
	public Date getTallyDate() {
		return this.tallyDate;
	}

	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}

	@Column(name = "TALLY_OPERTOR", length = 20)
	public String getTallyOpertor() {
		return this.tallyOpertor;
	}

	public void setTallyOpertor(String tallyOpertor) {
		this.tallyOpertor = tallyOpertor;
	}

	@Column(name = "ORDER_BATCH", length = 10)
	public String getOrderBatch() {
		return this.orderBatch;
	}

	public void setOrderBatch(String orderBatch) {
		this.orderBatch = orderBatch;
	}

	@Column(name = "TENDER_NO", precision = 4, scale = 0)
	public Short getTenderNo() {
		return this.tenderNo;
	}

	public void setTenderNo(Short tenderNo) {
		this.tenderNo = tenderNo;
	}

    @Column(name = "hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Column(name="export_document_no")
    public String getExportDocumentNo() {
        return exportDocumentNo;
    }

    public void setExportDocumentNo(String exportDocumentNo) {
        this.exportDocumentNo = exportDocumentNo;
    }
}