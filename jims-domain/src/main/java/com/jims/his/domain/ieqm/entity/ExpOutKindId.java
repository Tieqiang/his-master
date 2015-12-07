package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ExpOutKindId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class ExpOutKindId implements java.io.Serializable {

	// Fields

	private String code;
	private String storeId;
	private String id;

	// Constructors

	/** default constructor */
	public ExpOutKindId() {
	}

	/** full constructor */
	public ExpOutKindId(String code, String storeId, String id) {
		this.code = code;
		this.storeId = storeId;
		this.id = id;
	}

	// Property accessors

	@Column(name = "CODE", nullable = false, length = 2)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "STORE_ID", nullable = false, length = 10)
	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Column(name = "ID", nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ExpOutKindId))
			return false;
		ExpOutKindId castOther = (ExpOutKindId) other;

		return ((this.getCode() == castOther.getCode()) || (this.getCode() != null
				&& castOther.getCode() != null && this.getCode().equals(
				castOther.getCode())))
				&& ((this.getStoreId() == castOther.getStoreId()) || (this
						.getStoreId() != null && castOther.getStoreId() != null && this
						.getStoreId().equals(castOther.getStoreId())))
				&& ((this.getId() == castOther.getId()) || (this.getId() != null
						&& castOther.getId() != null && this.getId().equals(
						castOther.getId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCode() == null ? 0 : this.getCode().hashCode());
		result = 37 * result
				+ (getStoreId() == null ? 0 : this.getStoreId().hashCode());
		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		return result;
	}

}