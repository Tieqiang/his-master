package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpClass entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_CLASS", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "CLASS_CODE"))
public class ExpClass implements java.io.Serializable {

	// Fields

	private String id;
	private String primCode;
	private String classCode;
	private String secName;
	private String className;
	private String inCode;

	// Constructors

	/** default constructor */
	public ExpClass() {
	}

	/** full constructor */
	public ExpClass(String primCode, String classCode, String secName,
			String className, String inCode) {
		this.primCode = primCode;
		this.classCode = classCode;
		this.secName = secName;
		this.className = className;
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

	@Column(name = "PRIM_CODE", length = 2)
	public String getPrimCode() {
		return this.primCode;
	}

	public void setPrimCode(String primCode) {
		this.primCode = primCode;
	}

	@Column(name = "CLASS_CODE", unique = true, length = 4)
	public String getClassCode() {
		return this.classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@Column(name = "SEC_NAME", length = 30)
	public String getSecName() {
		return this.secName;
	}

	public void setSecName(String secName) {
		this.secName = secName;
	}

	@Column(name = "CLASS_NAME", length = 40)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "IN_CODE", length = 8)
	public String getInCode() {
		return this.inCode;
	}

	public void setInCode(String inCode) {
		this.inCode = inCode;
	}

}