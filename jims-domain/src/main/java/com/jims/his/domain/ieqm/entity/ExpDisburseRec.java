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
 * ExpDisburseRec entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_DISBURSE_REC", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "DISBURSE_REC_NO"))
public class ExpDisburseRec implements java.io.Serializable {

	// Fields

	private String id;
	private String disburseRecNo;
	private String storage;
	private String receiver;
	private Double payAmount;
	private Double retailAmount;
	private Double tradeAmount;
	private String bank;
	private String accountNo;
	private String checkerNo;
	private Date disburseDate;
	private String operator;
    private String hospitalId ;

	// Constructors

	/** default constructor */
	public ExpDisburseRec() {
	}

	/** full constructor */
	public ExpDisburseRec(String disburseRecNo, String storage,
                          String receiver, Double payAmount, Double retailAmount,
                          Double tradeAmount, String bank, String accountNo,
                          String checkerNo, Date disburseDate, String operator, String hospitalId) {
		this.disburseRecNo = disburseRecNo;
		this.storage = storage;
		this.receiver = receiver;
		this.payAmount = payAmount;
		this.retailAmount = retailAmount;
		this.tradeAmount = tradeAmount;
		this.bank = bank;
		this.accountNo = accountNo;
		this.checkerNo = checkerNo;
		this.disburseDate = disburseDate;
		this.operator = operator;
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

	@Column(name = "DISBURSE_REC_NO", unique = true, length = 10)
	public String getDisburseRecNo() {
		return this.disburseRecNo;
	}

	public void setDisburseRecNo(String disburseRecNo) {
		this.disburseRecNo = disburseRecNo;
	}

	@Column(name = "STORAGE", length = 8)
	public String getStorage() {
		return this.storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	@Column(name = "RECEIVER", length = 40)
	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Column(name = "PAY_AMOUNT", precision = 10)
	public Double getPayAmount() {
		return this.payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	@Column(name = "RETAIL_AMOUNT", precision = 10)
	public Double getRetailAmount() {
		return this.retailAmount;
	}

	public void setRetailAmount(Double retailAmount) {
		this.retailAmount = retailAmount;
	}

	@Column(name = "TRADE_AMOUNT", precision = 10)
	public Double getTradeAmount() {
		return this.tradeAmount;
	}

	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	@Column(name = "BANK", length = 30)
	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Column(name = "ACCOUNT_NO", length = 16)
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Column(name = "CHECKER_NO", length = 16)
	public String getCheckerNo() {
		return this.checkerNo;
	}

	public void setCheckerNo(String checkerNo) {
		this.checkerNo = checkerNo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DISBURSE_DATE", length = 7)
	public Date getDisburseDate() {
		return this.disburseDate;
	}

	public void setDisburseDate(Date disburseDate) {
		this.disburseDate = disburseDate;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
    @Column(name = "hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}