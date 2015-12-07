package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * TenderExpDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TENDER_EXP_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ORDER_BATCH", "TENDER_NO", "EXP_CODE", "FIRM_ID", "PACKAGE_SPEC" }))
public class TenderExpDict implements java.io.Serializable {

	// Fields

	private String id;
	private String orderBatch;
	private Short tenderNo;
	private String expCode;
	private String packageSpec;
	private String firmId;
	private Double tenderPrice;
	private Double tenderRetailPrice;
	private Double highPrice;
	private String supplier;
	private Boolean stopFlag;

	// Constructors

	/** default constructor */
	public TenderExpDict() {
	}

	/** full constructor */
	public TenderExpDict(String orderBatch, Short tenderNo, String expCode,
			String packageSpec, String firmId, Double tenderPrice,
			Double tenderRetailPrice, Double highPrice, String supplier,
			Boolean stopFlag) {
		this.orderBatch = orderBatch;
		this.tenderNo = tenderNo;
		this.expCode = expCode;
		this.packageSpec = packageSpec;
		this.firmId = firmId;
		this.tenderPrice = tenderPrice;
		this.tenderRetailPrice = tenderRetailPrice;
		this.highPrice = highPrice;
		this.supplier = supplier;
		this.stopFlag = stopFlag;
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

	@Column(name = "EXP_CODE", length = 20)
	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	@Column(name = "PACKAGE_SPEC", length = 20)
	public String getPackageSpec() {
		return this.packageSpec;
	}

	public void setPackageSpec(String packageSpec) {
		this.packageSpec = packageSpec;
	}

	@Column(name = "FIRM_ID", length = 10)
	public String getFirmId() {
		return this.firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	@Column(name = "TENDER_PRICE", precision = 10, scale = 4)
	public Double getTenderPrice() {
		return this.tenderPrice;
	}

	public void setTenderPrice(Double tenderPrice) {
		this.tenderPrice = tenderPrice;
	}

	@Column(name = "TENDER_RETAIL_PRICE", precision = 10, scale = 4)
	public Double getTenderRetailPrice() {
		return this.tenderRetailPrice;
	}

	public void setTenderRetailPrice(Double tenderRetailPrice) {
		this.tenderRetailPrice = tenderRetailPrice;
	}

	@Column(name = "HIGH_PRICE", precision = 10, scale = 4)
	public Double getHighPrice() {
		return this.highPrice;
	}

	public void setHighPrice(Double highPrice) {
		this.highPrice = highPrice;
	}

	@Column(name = "SUPPLIER", length = 40)
	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	@Column(name = "STOP_FLAG", precision = 1, scale = 0)
	public Boolean getStopFlag() {
		return this.stopFlag;
	}

	public void setStopFlag(Boolean stopFlag) {
		this.stopFlag = stopFlag;
	}

}