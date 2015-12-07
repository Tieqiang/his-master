package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpCodingRule entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_CODING_RULE", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "CODE_LEVEL"))
public class ExpCodingRule implements java.io.Serializable {

	// Fields

	private String id;
	private int codeLevel;
	private Byte levelWidth;
	private String className;

	// Constructors

	/** default constructor */
	public ExpCodingRule() {
	}

	/** full constructor */
	public ExpCodingRule(int codeLevel, Byte levelWidth, String className) {
		this.codeLevel = codeLevel;
		this.levelWidth = levelWidth;
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

	@Column(name = "CODE_LEVEL")
	public int getCodeLevel() {
		return this.codeLevel;
	}

	public void setCodeLevel(int codeLevel) {
		this.codeLevel = codeLevel;
	}

	@Column(name = "LEVEL_WIDTH", precision = 2, scale = 0)
	public Byte getLevelWidth() {
		return this.levelWidth;
	}

	public void setLevelWidth(Byte levelWidth) {
		this.levelWidth = levelWidth;
	}

	@Column(name = "CLASS_NAME", length = 8)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}