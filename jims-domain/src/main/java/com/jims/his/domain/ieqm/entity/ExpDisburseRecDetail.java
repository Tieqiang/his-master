package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpDisburseRecDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_DISBURSE_REC_DETAIL", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"DISBURSE_REC_NO", "DOCUMENT_NO", "ITEM_NO" }))
public class ExpDisburseRecDetail implements java.io.Serializable {

	// Fields

	private String id;
	private String disburseRecNo;
	private String documentNo;
	private Short itemNo;
	private Double disburseCount;
	private Double payAmount;
	private Double retailAmount;
	private Double tradeAmount;
    private String hospitalId;

	// Constructors

	/** default constructor */
	public ExpDisburseRecDetail() {
	}

	/** full constructor */
	public ExpDisburseRecDetail(String disburseRecNo, String documentNo,
                                Short itemNo, Double disburseCount, Double payAmount,
                                Double retailAmount, Double tradeAmount, String hospitalId) {
		this.disburseRecNo = disburseRecNo;
		this.documentNo = documentNo;
		this.itemNo = itemNo;
		this.disburseCount = disburseCount;
		this.payAmount = payAmount;
		this.retailAmount = retailAmount;
		this.tradeAmount = tradeAmount;
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

	@Column(name = "DISBURSE_REC_NO", length = 10)
	public String getDisburseRecNo() {
		return this.disburseRecNo;
	}

	public void setDisburseRecNo(String disburseRecNo) {
		this.disburseRecNo = disburseRecNo;
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

	@Column(name = "DISBURSE_COUNT", precision = 12)
	public Double getDisburseCount() {
		return this.disburseCount;
	}

	public void setDisburseCount(Double disburseCount) {
		this.disburseCount = disburseCount;
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
    @Column(name = "hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}