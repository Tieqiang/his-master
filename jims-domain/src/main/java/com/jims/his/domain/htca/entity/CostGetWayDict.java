package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * CostGetWayDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "COST_GET_WAY_DICT", schema = "HTCA")
public class CostGetWayDict implements java.io.Serializable {

	// Fields

	private String id;
	private String getWayName;
	private String getWayCode;
	private String inputCode;

	// Constructors

	/** default constructor */
	public CostGetWayDict() {
	}

	/** full constructor */
	public CostGetWayDict(String getWayName, String getWayCode, String inputCode) {
		this.getWayName = getWayName;
		this.getWayCode = getWayCode;
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

	@Column(name = "GET_WAY_NAME", length = 100)
	public String getGetWayName() {
		return this.getWayName;
	}

	public void setGetWayName(String getWayName) {
		this.getWayName = getWayName;
	}

	@Column(name = "GET_WAY_CODE", length = 50)
	public String getGetWayCode() {
		return this.getWayCode;
	}

	public void setGetWayCode(String getWayCode) {
		this.getWayCode = getWayCode;
	}

	@Column(name = "INPUT_CODE", length = 50)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

}