package com.jims.his.domain.oauth2.entity;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RefreshToken entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "REFRESH_TOKEN", schema = "JIMS")
public class RefreshToken implements java.io.Serializable {

	// Fields

	private RefreshTokenId id;
	private String accessToken;
	private String refreshToken;
	private Date createTime;
	private String expire;
	private String scope;
	private Date authorizationTime;
	private String grantType;

	// Constructors

	/** default constructor */
	public RefreshToken() {
	}

	/** minimal constructor */
	public RefreshToken(RefreshTokenId id) {
		this.id = id;
	}

	/** full constructor */
	public RefreshToken(RefreshTokenId id, String accessToken,
			String refreshToken, Date createTime, String expire, String scope,
			Date authorizationTime, String grantType) {
		this.id = id;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.createTime = createTime;
		this.expire = expire;
		this.scope = scope;
		this.authorizationTime = authorizationTime;
		this.grantType = grantType;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "appKey", column = @Column(name = "APP_KEY", nullable = false, length = 10)),
			@AttributeOverride(name = "userId", column = @Column(name = "USER_ID", nullable = false, precision = 30, scale = 0)) })
	public RefreshTokenId getId() {
		return this.id;
	}

	public void setId(RefreshTokenId id) {
		this.id = id;
	}

	@Column(name = "ACCESS_TOKEN", length = 100)
	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Column(name = "REFRESH_TOKEN", length = 100)
	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_TIME", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "EXPIRE", length = 30)
	public String getExpire() {
		return this.expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	@Column(name = "SCOPE", length = 100)
	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AUTHORIZATION_TIME", length = 7)
	public Date getAuthorizationTime() {
		return this.authorizationTime;
	}

	public void setAuthorizationTime(Date authorizationTime) {
		this.authorizationTime = authorizationTime;
	}

	@Column(name = "GRANT_TYPE", length = 30)
	public String getGrantType() {
		return this.grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

}