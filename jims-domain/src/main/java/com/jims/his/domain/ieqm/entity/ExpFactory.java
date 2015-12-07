package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpFactory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_FACTORY", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "CODE"))
public class ExpFactory implements java.io.Serializable {

	// Fields

	private String id;
	private String code;
	private String name;
	private String country;
	private String inCode;

	// Constructors

	/** default constructor */
	public ExpFactory() {
	}

	/** full constructor */
	public ExpFactory(String code, String name, String country, String inCode) {
		this.code = code;
		this.name = name;
		this.country = country;
		this.inCode = inCode;
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

	@Column(name = "CODE", unique = true, length = 4)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", length = 40)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "COUNTRY", length = 2)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "IN_CODE", length = 8)
	public String getInCode() {
		return this.inCode;
	}

	public void setInCode(String inCode) {
		this.inCode = inCode;
	}

}