package com.jims.his.domain.oauth2.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * AuthorityId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class AuthorityId implements java.io.Serializable {

	// Fields

	private String appKey;
	private Long userId;

	// Constructors

	/** default constructor */
	public AuthorityId() {
	}

	/** full constructor */
	public AuthorityId(String appKey, Long userId) {
		this.appKey = appKey;
		this.userId = userId;
	}

	// Property accessors

	@Column(name = "APP_KEY", nullable = false, length = 10)
	public String getAppKey() {
		return this.appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Column(name = "USER_ID", nullable = false, precision = 11, scale = 0)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AuthorityId))
			return false;
		AuthorityId castOther = (AuthorityId) other;

		return ((this.getAppKey() == castOther.getAppKey()) || (this
				.getAppKey() != null && castOther.getAppKey() != null && this
				.getAppKey().equals(castOther.getAppKey())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null && castOther.getUserId() != null && this
						.getUserId().equals(castOther.getUserId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getAppKey() == null ? 0 : this.getAppKey().hashCode());
		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		return result;
	}

}