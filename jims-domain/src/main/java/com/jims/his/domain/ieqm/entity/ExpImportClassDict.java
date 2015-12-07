package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

/**
 * ExpImportClassDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_IMPORT_CLASS_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "IMPORT_CLASS"))
@XmlRootElement
public class ExpImportClassDict implements java.io.Serializable {

	// Fields

	private String id;
	private String importClass;

	// Constructors

	/** default constructor */
	public ExpImportClassDict() {
	}

	/** full constructor */
	public ExpImportClassDict(String importClass) {
		this.importClass = importClass;
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

	@Column(name = "IMPORT_CLASS", unique = true, length = 8)
	public String getImportClass() {
		return this.importClass;
	}

	public void setImportClass(String importClass) {
		this.importClass = importClass;
	}

}