package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpClassPrim entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_CLASS_PRIM", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "PRIM_CODE"))
public class ExpClassPrim implements java.io.Serializable {

	// Fields

	private String id;
	private String primCode;
	private String primName;

	// Constructors

	/** default constructor */
	public ExpClassPrim() {
	}

	/** full constructor */
	public ExpClassPrim(String primCode, String primName) {
		this.primCode = primCode;
		this.primName = primName;
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

	@Column(name = "PRIM_CODE", unique = true, length = 2)
	public String getPrimCode() {
		return this.primCode;
	}

	public void setPrimCode(String primCode) {
		this.primCode = primCode;
	}

	@Column(name = "PRIM_NAME", length = 40)
	public String getPrimName() {
		return this.primName;
	}

	public void setPrimName(String primName) {
		this.primName = primName;
	}

}