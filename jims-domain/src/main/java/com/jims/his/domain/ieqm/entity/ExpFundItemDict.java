package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpFundItemDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_FUND_ITEM_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "FUND_ITEM"))
public class ExpFundItemDict implements java.io.Serializable {

	// Fields

	private String id;
	private Byte serialNo;
	private String fundItem;

	// Constructors

	/** default constructor */
	public ExpFundItemDict() {
	}

	/** full constructor */
	public ExpFundItemDict(Byte serialNo, String fundItem) {
		this.serialNo = serialNo;
		this.fundItem = fundItem;
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

	@Column(name = "SERIAL_NO", precision = 2, scale = 0)
	public Byte getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(Byte serialNo) {
		this.serialNo = serialNo;
	}

	@Column(name = "FUND_ITEM", unique = true, length = 10)
	public String getFundItem() {
		return this.fundItem;
	}

	public void setFundItem(String fundItem) {
		this.fundItem = fundItem;
	}

}