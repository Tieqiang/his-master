package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpExportClassDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_EXPORT_CLASS_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "EXPORT_CLASS"))
public class ExpExportClassDict implements java.io.Serializable {

	// Fields

	private String id;
	private String exportClass;

	// Constructors

	/** default constructor */
	public ExpExportClassDict() {
	}

	/** full constructor */
	public ExpExportClassDict(String exportClass) {
		this.exportClass = exportClass;
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

	@Column(name = "EXPORT_CLASS", unique = true, length = 8)
	public String getExportClass() {
		return this.exportClass;
	}

	public void setExportClass(String exportClass) {
		this.exportClass = exportClass;
	}

}