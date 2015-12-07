package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpAttr entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_ATTR", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "CODE"))
public class ExpAttr implements java.io.Serializable {

	// Fields

	private String id;
	private String code;
	private String name;

	// Constructors

	/** default constructor */
	public ExpAttr() {
	}

	/** full constructor */
	public ExpAttr(String code, String name) {
		this.code = code;
		this.name = name;
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

	@Column(name = "CODE", unique = true, length = 1)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}