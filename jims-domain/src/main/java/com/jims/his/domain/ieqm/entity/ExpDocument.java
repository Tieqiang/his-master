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
 * ExpDocument entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_DOCUMENT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"DOCUMENT_NO", "ITEM_NO", "RECEIVER", "USE_STATUS" }))
public class ExpDocument implements java.io.Serializable {

	// Fields

	private String id;
	private String storage;
	private String documentNo;
	private Short itemNo;
	private String expCode;
	private String expSpec;
	private String units;
	private String firmId;
	private String batchNo;
	private Double quantity;
	private Double purchasePrice;
	private String packageSpec;
	private String expForm;
	private Date exportDate;
	private Date documentDate;
	private String receiver;
	private String keeper;
	private String useStatus;
	private String expName;

	// Constructors

	/** default constructor */
	public ExpDocument() {
	}

	/** full constructor */
	public ExpDocument(String storage, String documentNo, Short itemNo,
			String expCode, String expSpec, String units, String firmId,
			String batchNo, Double quantity, Double purchasePrice,
			String packageSpec, String expForm, Date exportDate,
			Date documentDate, String receiver, String keeper,
			String useStatus, String expName) {
		this.storage = storage;
		this.documentNo = documentNo;
		this.itemNo = itemNo;
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.units = units;
		this.firmId = firmId;
		this.batchNo = batchNo;
		this.quantity = quantity;
		this.purchasePrice = purchasePrice;
		this.packageSpec = packageSpec;
		this.expForm = expForm;
		this.exportDate = exportDate;
		this.documentDate = documentDate;
		this.receiver = receiver;
		this.keeper = keeper;
		this.useStatus = useStatus;
		this.expName = expName;
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

	@Column(name = "QUANTITY", precision = 12)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "PURCHASE_PRICE", precision = 10, scale = 4)
	public Double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	@Column(name = "PACKAGE_SPEC", length = 20)
	public String getPackageSpec() {
		return this.packageSpec;
	}

	public void setPackageSpec(String packageSpec) {
		this.packageSpec = packageSpec;
	}

	@Column(name = "EXP_FORM", length = 10)
	public String getExpForm() {
		return this.expForm;
	}

	public void setExpForm(String expForm) {
		this.expForm = expForm;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPORT_DATE", length = 7)
	public Date getExportDate() {
		return this.exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DOCUMENT_DATE", length = 7)
	public Date getDocumentDate() {
		return this.documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	@Column(name = "RECEIVER", length = 40)
	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Column(name = "KEEPER", length = 20)
	public String getKeeper() {
		return this.keeper;
	}

	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}

	@Column(name = "USE_STATUS", length = 10)
	public String getUseStatus() {
		return this.useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}

	@Column(name = "EXP_NAME", length = 100)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

}