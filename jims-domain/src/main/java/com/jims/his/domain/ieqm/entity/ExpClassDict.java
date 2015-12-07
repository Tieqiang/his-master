package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpClassDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_CLASS_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "CLASS_CODE"))
public class ExpClassDict implements java.io.Serializable {

	// Fields

	private String id;
	private String classCode;
	private String className;

	// Constructors

	/** default constructor */
	public ExpClassDict() {
	}

	/** full constructor */
	public ExpClassDict(String classCode, String className) {
		this.classCode = classCode;
		this.className = className;
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

	@Column(name = "CLASS_CODE", unique = true, length = 10)
	public String getClassCode() {
		return this.classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@Column(name = "CLASS_NAME", length = 40)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}