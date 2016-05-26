package com.jims.his.domain.oauth2.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AppProfileId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class AppProfileId implements java.io.Serializable {

	// Fields

	private String appKey;
	private Date createTime;

	// Constructors

	/** default constructor */
	public AppProfileId() {
	}

	/** full constructor */
	public AppProfileId(String appKey, Date createTime) {
		this.appKey = appKey;
		this.createTime = createTime;
	}

	// Property accessors

	@Column(name = "APP_KEY", length = 10)
	public String getAppKey() {
		return this.appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_TIME", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AppProfileId))
			return false;
		AppProfileId castOther = (AppProfileId) other;

		return ((this.getAppKey() == castOther.getAppKey()) || (this
				.getAppKey() != null && castOther.getAppKey() != null && this
				.getAppKey().equals(castOther.getAppKey())))
				&& ((this.getCreateTime() == castOther.getCreateTime()) || (this
						.getCreateTime() != null
						&& castOther.getCreateTime() != null && this
						.getCreateTime().equals(castOther.getCreateTime())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getAppKey() == null ? 0 : this.getAppKey().hashCode());
		result = 37
				* result
				+ (getCreateTime() == null ? 0 : this.getCreateTime()
						.hashCode());
		return result;
	}

}