package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * StaffVsStorage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STAFF_VS_STORAGE", schema = "JIMS")
public class StaffVsStorage implements java.io.Serializable {

	// Fields

	private String id;
	private String staffId;
	private String storageId;

	// Constructors

	/** default constructor */
	public StaffVsStorage() {
	}

	/** full constructor */
	public StaffVsStorage(String staffId, String storageId) {
		this.staffId = staffId;
		this.storageId = storageId;
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

	@Column(name = "STAFF_ID", length = 64)
	public String getStaffId() {
		return this.staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	@Column(name = "STORAGE_ID", length = 64)
	public String getStorageId() {
		return this.storageId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

}