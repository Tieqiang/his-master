package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpSubStorageDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_SUB_STORAGE_DICT", schema = "JIMS", uniqueConstraints = {
		@UniqueConstraint(columnNames = "EXPORT_NO_PREFIX"),
		@UniqueConstraint(columnNames = { "STORAGE_CODE", "SUB_STORAGE", "HOSPITAL_ID" }),
		@UniqueConstraint(columnNames = "IMPORT_NO_PREFIX") })
public class ExpSubStorageDict implements java.io.Serializable {

	// Fields

	private String id;
	private String storageCode;
	private String subStorage;
	private String importNoPrefix;
	private Integer importNoAva;
	private String exportNoPrefix;
	private Integer exportNoAva;
	private String storageType;
    private String hospitalId;

	// Constructors

	/** default constructor */
	public ExpSubStorageDict() {
	}

	/** full constructor */
	public ExpSubStorageDict(String storageCode, String subStorage,
			String importNoPrefix, Integer importNoAva, String exportNoPrefix,
			Integer exportNoAva, String storageType, String hospitalId) {
		this.storageCode = storageCode;
		this.subStorage = subStorage;
		this.importNoPrefix = importNoPrefix;
		this.importNoAva = importNoAva;
		this.exportNoPrefix = exportNoPrefix;
		this.exportNoAva = exportNoAva;
		this.storageType = storageType;
        this.hospitalId = hospitalId;
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

	@Column(name = "STORAGE_CODE", length = 8)
	public String getStorageCode() {
		return this.storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	@Column(name = "SUB_STORAGE", length = 20)
	public String getSubStorage() {
		return this.subStorage;
	}

	public void setSubStorage(String subStorage) {
		this.subStorage = subStorage;
	}

	@Column(name = "IMPORT_NO_PREFIX", unique = true, length = 6)
	public String getImportNoPrefix() {
		return this.importNoPrefix;
	}

	public void setImportNoPrefix(String importNoPrefix) {
		this.importNoPrefix = importNoPrefix;
	}

	@Column(name = "IMPORT_NO_AVA", precision = 4, scale = 0)
	public Integer getImportNoAva() {
		return this.importNoAva;
	}

	public void setImportNoAva(Integer importNoAva) {
		this.importNoAva = importNoAva;
	}

	@Column(name = "EXPORT_NO_PREFIX", unique = true, length = 6)
	public String getExportNoPrefix() {
		return this.exportNoPrefix;
	}

	public void setExportNoPrefix(String exportNoPrefix) {
		this.exportNoPrefix = exportNoPrefix;
	}

	@Column(name = "EXPORT_NO_AVA", precision = 6, scale = 0)
	public Integer getExportNoAva() {
		return this.exportNoAva;
	}

	public void setExportNoAva(Integer exportNoAva) {
		this.exportNoAva = exportNoAva;
	}

	@Column(name = "STORAGE_TYPE", length = 8)
	public String getStorageType() {
		return this.storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

    @Column(name = "HOSPITAL_ID", length = 64)
    public String getHospitalId() { return hospitalId; }

    public void setHospitalId(String hospitalId) { this.hospitalId = hospitalId; }
}