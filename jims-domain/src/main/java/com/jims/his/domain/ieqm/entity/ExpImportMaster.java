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
 * ExpImportMaster entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_IMPORT_MASTER", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "DOCUMENT_NO"))
public class ExpImportMaster implements java.io.Serializable {

	// Fields

	private String id;
	private String documentNo;
	private String storage;
	private Date importDate;
	private String supplier;
	private Double accountReceivable;
	private Double accountPayed;
	private Double additionalFee;
	private String importClass;
	private String subStorage;
	private Integer accountIndicator;
	private String memos;
	private String operator;
	private String acctoperator;
	private Date acctdate;
	private String principal;
	private String storekeeper;
	private String buyer;
	private String checkman;
	private String docStatus;
	private String tenderNo;
	private String tenderType;
    private String hospitalId ;

	// Constructors

	/** default constructor */
	public ExpImportMaster() {
	}

	/** full constructor */
	public ExpImportMaster(String documentNo, String storage, Date importDate,
                           String supplier, Double accountReceivable, Double accountPayed,
                           Double additionalFee, String importClass, String subStorage,
                           Integer accountIndicator, String memos, String operator,
                           String acctoperator, Date acctdate, String principal,
                           String storekeeper, String buyer, String checkman,
                           String docStatus, String tenderNo, String tenderType, String hospitalId) {
		this.documentNo = documentNo;
		this.storage = storage;
		this.importDate = importDate;
		this.supplier = supplier;
		this.accountReceivable = accountReceivable;
		this.accountPayed = accountPayed;
		this.additionalFee = additionalFee;
		this.importClass = importClass;
		this.subStorage = subStorage;
		this.accountIndicator = accountIndicator;
		this.memos = memos;
		this.operator = operator;
		this.acctoperator = acctoperator;
		this.acctdate = acctdate;
		this.principal = principal;
		this.storekeeper = storekeeper;
		this.buyer = buyer;
		this.checkman = checkman;
		this.docStatus = docStatus;
		this.tenderNo = tenderNo;
		this.tenderType = tenderType;
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

	@Column(name = "DOCUMENT_NO", unique = true, length = 10)
	public String getDocumentNo() {
		return this.documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	@Column(name = "STORAGE", length = 8)
	public String getStorage() {
		return this.storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "IMPORT_DATE", length = 7)
	public Date getImportDate() {
		return this.importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	@Column(name = "SUPPLIER", length = 40)
	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	@Column(name = "ACCOUNT_RECEIVABLE", precision = 16)
	public Double getAccountReceivable() {
		return this.accountReceivable;
	}

	public void setAccountReceivable(Double accountReceivable) {
		this.accountReceivable = accountReceivable;
	}

	@Column(name = "ACCOUNT_PAYED", precision = 10)
	public Double getAccountPayed() {
		return this.accountPayed;
	}

	public void setAccountPayed(Double accountPayed) {
		this.accountPayed = accountPayed;
	}

	@Column(name = "ADDITIONAL_FEE", precision = 8)
	public Double getAdditionalFee() {
		return this.additionalFee;
	}

	public void setAdditionalFee(Double additionalFee) {
		this.additionalFee = additionalFee;
	}

	@Column(name = "IMPORT_CLASS", length = 8)
	public String getImportClass() {
		return this.importClass;
	}

	public void setImportClass(String importClass) {
		this.importClass = importClass;
	}

	@Column(name = "SUB_STORAGE", length = 10)
	public String getSubStorage() {
		return this.subStorage;
	}

	public void setSubStorage(String subStorage) {
		this.subStorage = subStorage;
	}

	@Column(name = "ACCOUNT_INDICATOR", precision = 1, scale = 0)
	public Integer getAccountIndicator() {
		return this.accountIndicator;
	}

	public void setAccountIndicator(Integer accountIndicator) {
		this.accountIndicator = accountIndicator;
	}

	@Column(name = "MEMOS", length = 20)
	public String getMemos() {
		return this.memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "ACCTOPERATOR", length = 20)
	public String getAcctoperator() {
		return this.acctoperator;
	}

	public void setAcctoperator(String acctoperator) {
		this.acctoperator = acctoperator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACCTDATE", length = 7)
	public Date getAcctdate() {
		return this.acctdate;
	}

	public void setAcctdate(Date acctdate) {
		this.acctdate = acctdate;
	}

	@Column(name = "PRINCIPAL", length = 20)
	public String getPrincipal() {
		return this.principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	@Column(name = "STOREKEEPER", length = 20)
	public String getStorekeeper() {
		return this.storekeeper;
	}

	public void setStorekeeper(String storekeeper) {
		this.storekeeper = storekeeper;
	}

	@Column(name = "BUYER", length = 20)
	public String getBuyer() {
		return this.buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	@Column(name = "CHECKMAN", length = 20)
	public String getCheckman() {
		return this.checkman;
	}

	public void setCheckman(String checkman) {
		this.checkman = checkman;
	}

	@Column(name = "DOC_STATUS", length = 1)
	public String getDocStatus() {
		return this.docStatus;
	}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}

	@Column(name = "TENDER_NO", length = 40)
	public String getTenderNo() {
		return this.tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	@Column(name = "TENDER_TYPE", length = 20)
	public String getTenderType() {
		return this.tenderType;
	}

	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}

    @Column(name = "hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}