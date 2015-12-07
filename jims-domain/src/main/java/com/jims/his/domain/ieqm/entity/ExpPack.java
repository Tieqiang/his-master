package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpPack entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_PACK", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"PACK_INFO_ID", "STORE_ID", "INFO_ID" }))
public class ExpPack implements java.io.Serializable {

	// Fields

	private String id;
	private String packInfoId;
	private String packExpCode;
	private String storeId;
	private String infoId;
	private String infoExpCode;
	private Double quantity;
	private Integer times;

	// Constructors

	/** default constructor */
	public ExpPack() {
	}

	/** full constructor */
	public ExpPack(String packInfoId, String packExpCode, String storeId,
			String infoId, String infoExpCode, Double quantity, Integer times) {
		this.packInfoId = packInfoId;
		this.packExpCode = packExpCode;
		this.storeId = storeId;
		this.infoId = infoId;
		this.infoExpCode = infoExpCode;
		this.quantity = quantity;
		this.times = times;
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

	@Column(name = "PACK_INFO_ID", length = 12)
	public String getPackInfoId() {
		return this.packInfoId;
	}

	public void setPackInfoId(String packInfoId) {
		this.packInfoId = packInfoId;
	}

	@Column(name = "PACK_EXP_CODE", length = 20)
	public String getPackExpCode() {
		return this.packExpCode;
	}

	public void setPackExpCode(String packExpCode) {
		this.packExpCode = packExpCode;
	}

	@Column(name = "STORE_ID", length = 10)
	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Column(name = "INFO_ID", length = 12)
	public String getInfoId() {
		return this.infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	@Column(name = "INFO_EXP_CODE", length = 20)
	public String getInfoExpCode() {
		return this.infoExpCode;
	}

	public void setInfoExpCode(String infoExpCode) {
		this.infoExpCode = infoExpCode;
	}

	@Column(name = "QUANTITY", precision = 10)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "TIMES", precision = 8, scale = 0)
	public Integer getTimes() {
		return this.times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

}