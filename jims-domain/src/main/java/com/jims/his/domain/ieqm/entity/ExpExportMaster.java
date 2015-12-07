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
 * ExpExportMaster entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_EXPORT_MASTER", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "DOCUMENT_NO"))
public class ExpExportMaster implements java.io.Serializable {

	// Fields

	private String id;
	private String documentNo;
	private String storage;
	private Date exportDate;
	private String receiver;
	private Double accountReceivable;
	private Double accountPayed;
	private Double additionalFee;
	private String exportClass;
	private String subStorage;
	private Boolean accountIndicator;
	private String memos;
	private String fundItem;
	private String operator;
	private String acctoperator;
	private Date acctdate;
	private String principal;
	private String storekeeper;
	private String buyer;
	private String docStatus;
	private String auditor;
	private String rcptNo;
    private String hospitalId ;

	// Constructors

	/** default constructor */
	public ExpExportMaster() {
	}

	/** full constructor */
	public ExpExportMaster(String documentNo, String storage, Date exportDate,
                           String receiver, Double accountReceivable, Double accountPayed,
                           Double additionalFee, String exportClass, String subStorage,
                           Boolean accountIndicator, String memos, String fundItem,
                           String operator, String acctoperator, Date acctdate,
                           String principal, String storekeeper, String buyer,
                           String docStatus, String auditor, String rcptNo, String hospitalId) {
		this.documentNo = documentNo;
		this.storage = storage;
		this.exportDate = exportDate;
		this.receiver = receiver;
		this.accountReceivable = accountReceivable;
		this.accountPayed = accountPayed;
		this.additionalFee = additionalFee;
		this.exportClass = exportClass;
		this.subStorage = subStorage;
		this.accountIndicator = accountIndicator;
		this.memos = memos;
		this.fundItem = fundItem;
		this.operator = operator;
		this.acctoperator = acctoperator;
		this.acctdate = acctdate;
		this.principal = principal;
		this.storekeeper = storekeeper;
		this.buyer = buyer;
		this.docStatus = docStatus;
		this.auditor = auditor;
		this.rcptNo = rcptNo;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPORT_DATE", length = 7)
	public Date getExportDate() {
		return this.exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	@Column(name = "RECEIVER", length = 40)
	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

	@Column(name = "EXPORT_CLASS", length = 8)
	public String getExportClass() {
		return this.exportClass;
	}

	public void setExportClass(String exportClass) {
		this.exportClass = exportClass;
	}

	@Column(name = "SUB_STORAGE", length = 10)
	public String getSubStorage() {
		return this.subStorage;
	}

	public void setSubStorage(String subStorage) {
		this.subStorage = subStorage;
	}

	@Column(name = "ACCOUNT_INDICATOR", precision = 1, scale = 0)
	public Boolean getAccountIndicator() {
		return this.accountIndicator;
	}

	public void setAccountIndicator(Boolean accountIndicator) {
		this.accountIndicator = accountIndicator;
	}

	@Column(name = "MEMOS", length = 20)
	public String getMemos() {
		return this.memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	@Column(name = "FUND_ITEM", length = 10)
	public String getFundItem() {
		return this.fundItem;
	}

	public void setFundItem(String fundItem) {
		this.fundItem = fundItem;
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

	@Temporal(TemporalType.DATE)
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

	@Column(name = "DOC_STATUS", length = 1)
	public String getDocStatus() {
		return this.docStatus;
	}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}

	@Column(name = "AUDITOR", length = 20)
	public String getAuditor() {
		return this.auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	@Column(name = "RCPT_NO", length = 20)
	public String getRcptNo() {
		return this.rcptNo;
	}

	public void setRcptNo(String rcptNo) {
		this.rcptNo = rcptNo;
	}

    @Column(name="hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}