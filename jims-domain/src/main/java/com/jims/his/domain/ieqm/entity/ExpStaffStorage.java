package com.jims.his.domain.ieqm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * BuyExpPlan entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "exp_staff_storage", schema = "JIMS")
public class ExpStaffStorage implements java.io.Serializable {

	// Fields

	private String id;
	private String staffId;
	private Short staffName;
	private String storageCode;
	private String storageName;

	public ExpStaffStorage() {
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

    @Column(name="staff_id")
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
    @Column(name="staff_name")
    public Short getStaffName() {
        return staffName;
    }

    public void setStaffName(Short staffName) {
        this.staffName = staffName;
    }
    @Column(name="storage_code")
    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }
    @Column(name="storage_name")
    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }
}