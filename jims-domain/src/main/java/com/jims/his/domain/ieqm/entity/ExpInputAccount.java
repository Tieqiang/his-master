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
 * ExpInputAccount entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_INPUT_ACCOUNT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORE_ID", "BILL_ID" }))
public class ExpInputAccount implements java.io.Serializable {

	// Fields

	private String id;
	private String storeId;
	private String billId;
	private String inClass;
	private String supply;
	private Date inDate;
	private String indicator;
	private Double inSum;
	private String buyer;
	private String handlerPerson;
	private String operator;

	// Constructors

	/** default constructor */
	public ExpInputAccount() {
	}

	/** full constructor */
	public ExpInputAccount(String storeId, String billId, String inClass,
			String supply, Date inDate, String indicator, Double inSum,
			String buyer, String handlerPerson, String operator) {
		this.storeId = storeId;
		this.billId = billId;
		this.inClass = inClass;
		this.supply = supply;
		this.inDate = inDate;
		this.indicator = indicator;
		this.inSum = inSum;
		this.buyer = buyer;
		this.handlerPerson = handlerPerson;
		this.operator = operator;
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

	@Column(name = "STORE_ID", length = 10)
	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Column(name = "BILL_ID", length = 8)
	public String getBillId() {
		return this.billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	@Column(name = "IN_CLASS", length = 2)
	public String getInClass() {
		return this.inClass;
	}

	public void setInClass(String inClass) {
		this.inClass = inClass;
	}

	@Column(name = "SUPPLY", length = 20)
	public String getSupply() {
		return this.supply;
	}

	public void setSupply(String supply) {
		this.supply = supply;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "IN_DATE", length = 7)
	public Date getInDate() {
		return this.inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	@Column(name = "INDICATOR", length = 1)
	public String getIndicator() {
		return this.indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	@Column(name = "IN_SUM", precision = 12, scale = 3)
	public Double getInSum() {
		return this.inSum;
	}

	public void setInSum(Double inSum) {
		this.inSum = inSum;
	}

	@Column(name = "BUYER", length = 20)
	public String getBuyer() {
		return this.buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	@Column(name = "HANDLER", length = 20)
	public String getHandlerPerson() {
		return this.handlerPerson;
	}

	public void setHandlerPerson(String handler) {
		this.handlerPerson = handler;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}