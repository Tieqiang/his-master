package com.jims.his.domain.ieqm.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ExpStockBalanceId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class ExpStockBalanceId implements java.io.Serializable {

	// Fields

	private String id;
	private Date yearMonth;
	private String storage;
	private String expCode;
	private String expSpec;
	private String firmId;
	private String packageSpec;

	// Constructors

	/** default constructor */
	public ExpStockBalanceId() {
	}

	/** full constructor */
	public ExpStockBalanceId(String id, Date yearMonth, String storage,
			String expCode, String expSpec, String firmId, String packageSpec) {
		this.id = id;
		this.yearMonth = yearMonth;
		this.storage = storage;
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.firmId = firmId;
		this.packageSpec = packageSpec;
	}

	// Property accessors

	@Column(name = "ID", nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "YEAR_MONTH", nullable = false, length = 7)
	public Date getYearMonth() {
		return this.yearMonth;
	}

	public void setYearMonth(Date yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Column(name = "STORAGE", nullable = false, length = 8)
	public String getStorage() {
		return this.storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	@Column(name = "EXP_CODE", nullable = false, length = 20)
	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	@Column(name = "EXP_SPEC", nullable = false, length = 20)
	public String getExpSpec() {
		return this.expSpec;
	}

	public void setExpSpec(String expSpec) {
		this.expSpec = expSpec;
	}

	@Column(name = "FIRM_ID", nullable = false, length = 10)
	public String getFirmId() {
		return this.firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	@Column(name = "PACKAGE_SPEC", nullable = false, length = 20)
	public String getPackageSpec() {
		return this.packageSpec;
	}

	public void setPackageSpec(String packageSpec) {
		this.packageSpec = packageSpec;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ExpStockBalanceId))
			return false;
		ExpStockBalanceId castOther = (ExpStockBalanceId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getYearMonth() == castOther.getYearMonth()) || (this
						.getYearMonth() != null
						&& castOther.getYearMonth() != null && this
						.getYearMonth().equals(castOther.getYearMonth())))
				&& ((this.getStorage() == castOther.getStorage()) || (this
						.getStorage() != null && castOther.getStorage() != null && this
						.getStorage().equals(castOther.getStorage())))
				&& ((this.getExpCode() == castOther.getExpCode()) || (this
						.getExpCode() != null && castOther.getExpCode() != null && this
						.getExpCode().equals(castOther.getExpCode())))
				&& ((this.getExpSpec() == castOther.getExpSpec()) || (this
						.getExpSpec() != null && castOther.getExpSpec() != null && this
						.getExpSpec().equals(castOther.getExpSpec())))
				&& ((this.getFirmId() == castOther.getFirmId()) || (this
						.getFirmId() != null && castOther.getFirmId() != null && this
						.getFirmId().equals(castOther.getFirmId())))
				&& ((this.getPackageSpec() == castOther.getPackageSpec()) || (this
						.getPackageSpec() != null
						&& castOther.getPackageSpec() != null && this
						.getPackageSpec().equals(castOther.getPackageSpec())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getYearMonth() == null ? 0 : this.getYearMonth().hashCode());
		result = 37 * result
				+ (getStorage() == null ? 0 : this.getStorage().hashCode());
		result = 37 * result
				+ (getExpCode() == null ? 0 : this.getExpCode().hashCode());
		result = 37 * result
				+ (getExpSpec() == null ? 0 : this.getExpSpec().hashCode());
		result = 37 * result
				+ (getFirmId() == null ? 0 : this.getFirmId().hashCode());
		result = 37
				* result
				+ (getPackageSpec() == null ? 0 : this.getPackageSpec()
						.hashCode());
		return result;
	}

}