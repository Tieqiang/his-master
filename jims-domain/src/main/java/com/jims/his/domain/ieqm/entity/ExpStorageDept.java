package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpStorageDept entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_STORAGE_DEPT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "STORAGE_CODE"))
public class ExpStorageDept implements java.io.Serializable {

	// Fields

	private String id;
	private String storageCode;
	private String storageName;
	private String disburseNoPrefix;
	private Short disburseNoAva;
	private String purchaseNoPrefix;
	private Short purchaseNoAva;
	private String pcName;
	private Integer storageLevel;
    private String hospitalId ;

	// Constructors

	/** default constructor */
	public ExpStorageDept() {
	}

	/** full constructor */
	public ExpStorageDept(String storageCode, String storageName,
                          String disburseNoPrefix, Short disburseNoAva,
                          String purchaseNoPrefix, Short purchaseNoAva, String pcName,
                          Integer storageLevel, String hospitalId) {
		this.storageCode = storageCode;
		this.storageName = storageName;
		this.disburseNoPrefix = disburseNoPrefix;
		this.disburseNoAva = disburseNoAva;
		this.purchaseNoPrefix = purchaseNoPrefix;
		this.purchaseNoAva = purchaseNoAva;
		this.pcName = pcName;
		this.storageLevel = storageLevel;
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

	@Column(name = "STORAGE_CODE", unique = true, length = 8)
	public String getStorageCode() {
		return this.storageCode;
	}

	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}

	@Column(name = "STORAGE_NAME", length = 20)
	public String getStorageName() {
		return this.storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	@Column(name = "DISBURSE_NO_PREFIX", length = 6)
	public String getDisburseNoPrefix() {
		return this.disburseNoPrefix;
	}

	public void setDisburseNoPrefix(String disburseNoPrefix) {
		this.disburseNoPrefix = disburseNoPrefix;
	}

	@Column(name = "DISBURSE_NO_AVA", precision = 4, scale = 0)
	public Short getDisburseNoAva() {
		return this.disburseNoAva;
	}

	public void setDisburseNoAva(Short disburseNoAva) {
		this.disburseNoAva = disburseNoAva;
	}

	@Column(name = "PURCHASE_NO_PREFIX", length = 6)
	public String getPurchaseNoPrefix() {
		return this.purchaseNoPrefix;
	}

	public void setPurchaseNoPrefix(String purchaseNoPrefix) {
		this.purchaseNoPrefix = purchaseNoPrefix;
	}

	@Column(name = "PURCHASE_NO_AVA", precision = 4, scale = 0)
	public Short getPurchaseNoAva() {
		return this.purchaseNoAva;
	}

	public void setPurchaseNoAva(Short purchaseNoAva) {
		this.purchaseNoAva = purchaseNoAva;
	}

	@Column(name = "PC_NAME", length = 20)
	public String getPcName() {
		return this.pcName;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	@Column(name = "STORAGE_LEVEL")
	public Integer getStorageLevel() {
		return this.storageLevel;
	}

	public void setStorageLevel(Integer storageLevel) {
		this.storageLevel = storageLevel;
	}


    @Column(name = "hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

}