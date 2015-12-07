package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * CostRateDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "COST_RATE_DICT", schema = "HTCA")
public class CostRateDict implements java.io.Serializable {

	// Fields

	private String id;
	private String rateName;
	private String rateCode;
	private String inputCode;

	// Constructors

	/** default constructor */
	public CostRateDict() {
	}

	/** full constructor */
	public CostRateDict(String rateName, String rateCode, String inputCode) {
		this.rateName = rateName;
		this.rateCode = rateCode;
		this.inputCode = inputCode;
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

	@Column(name = "RATE_NAME", length = 100)
	public String getRateName() {
		return this.rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

	@Column(name = "RATE_CODE", length = 100)
	public String getRateCode() {
		return this.rateCode;
	}

	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

	@Column(name = "INPUT_CODE", length = 50)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

}