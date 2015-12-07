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
 * ExpBalance entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_BALANCE", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORE_ID", "BAL_DATE", "SIGN" }))
public class ExpBalance implements java.io.Serializable {

	// Fields

	private String id;
	private String storeId;
	private Date balDate;
	private String sign;
	private Double lastSum;
	private Double thisSum;
	private Double inSum;
	private Double outSum;
	private Double modiSum;
	private Double balor;

	// Constructors

	/** default constructor */
	public ExpBalance() {
	}

	/** full constructor */
	public ExpBalance(String storeId, Date balDate, String sign,
			Double lastSum, Double thisSum, Double inSum, Double outSum,
			Double modiSum, Double balor) {
		this.storeId = storeId;
		this.balDate = balDate;
		this.sign = sign;
		this.lastSum = lastSum;
		this.thisSum = thisSum;
		this.inSum = inSum;
		this.outSum = outSum;
		this.modiSum = modiSum;
		this.balor = balor;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "BAL_DATE", length = 7)
	public Date getBalDate() {
		return this.balDate;
	}

	public void setBalDate(Date balDate) {
		this.balDate = balDate;
	}

	@Column(name = "SIGN", length = 1)
	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Column(name = "LAST_SUM", precision = 12, scale = 3)
	public Double getLastSum() {
		return this.lastSum;
	}

	public void setLastSum(Double lastSum) {
		this.lastSum = lastSum;
	}

	@Column(name = "THIS_SUM", precision = 12, scale = 3)
	public Double getThisSum() {
		return this.thisSum;
	}

	public void setThisSum(Double thisSum) {
		this.thisSum = thisSum;
	}

	@Column(name = "IN_SUM", precision = 12, scale = 3)
	public Double getInSum() {
		return this.inSum;
	}

	public void setInSum(Double inSum) {
		this.inSum = inSum;
	}

	@Column(name = "OUT_SUM", precision = 12, scale = 3)
	public Double getOutSum() {
		return this.outSum;
	}

	public void setOutSum(Double outSum) {
		this.outSum = outSum;
	}

	@Column(name = "MODI_SUM", precision = 12, scale = 3)
	public Double getModiSum() {
		return this.modiSum;
	}

	public void setModiSum(Double modiSum) {
		this.modiSum = modiSum;
	}

	@Column(name = "BALOR", precision = 12, scale = 3)
	public Double getBalor() {
		return this.balor;
	}

	public void setBalor(Double balor) {
		this.balor = balor;
	}

}