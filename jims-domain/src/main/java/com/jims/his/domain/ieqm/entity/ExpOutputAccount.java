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
 * ExpOutputAccount entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_OUTPUT_ACCOUNT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORE_ID", "BILL_ID" }))
public class ExpOutputAccount implements java.io.Serializable {

	// Fields

	private String id;
	private String storeId;
	private String billId;
	private String outClass;
	private String receive;
	private Date outDate;
	private String indicator;
	private Double outSum;
	private String depter;
	private String handlerPerson;
	private String operator;

	// Constructors

	/** default constructor */
	public ExpOutputAccount() {
	}

	/** full constructor */
	public ExpOutputAccount(String storeId, String billId, String outClass,
			String receive, Date outDate, String indicator, Double outSum,
			String depter, String handlerPerson, String operator) {
		this.storeId = storeId;
		this.billId = billId;
		this.outClass = outClass;
		this.receive = receive;
		this.outDate = outDate;
		this.indicator = indicator;
		this.outSum = outSum;
		this.depter = depter;
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

	@Column(name = "OUT_CLASS", length = 2)
	public String getOutClass() {
		return this.outClass;
	}

	public void setOutClass(String outClass) {
		this.outClass = outClass;
	}

	@Column(name = "RECEIVE", length = 20)
	public String getReceive() {
		return this.receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OUT_DATE", length = 7)
	public Date getOutDate() {
		return this.outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	@Column(name = "INDICATOR", length = 1)
	public String getIndicator() {
		return this.indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	@Column(name = "OUT_SUM", precision = 12, scale = 3)
	public Double getOutSum() {
		return this.outSum;
	}

	public void setOutSum(Double outSum) {
		this.outSum = outSum;
	}

	@Column(name = "DEPTER", length = 20)
	public String getDepter() {
		return this.depter;
	}

	public void setDepter(String depter) {
		this.depter = depter;
	}

	@Column(name = "HANDLER_PERSON", length = 20)
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