package com.jims.his.domain.oauth2.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * App entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "APP", schema = "JIMS")
public class App implements java.io.Serializable {

	// Fields

	private String appKey;
	private String secretKey;
	private String callbackUrl;
	private String description;
	private String name;
	private Long status;
	private Date createTime;
	private Long userId;
	private String owner;
	private String approval;

	// Constructors

	/** default constructor */
	public App() {
	}

	/** full constructor */
	public App(String secretKey, String callbackUrl, String description,
			String name, Long status, Date createTime, Long userId,
			String owner, String approval) {
		this.secretKey = secretKey;
		this.callbackUrl = callbackUrl;
		this.description = description;
		this.name = name;
		this.status = status;
		this.createTime = createTime;
		this.userId = userId;
		this.owner = owner;
		this.approval = approval;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "APP_KEY", unique = true, nullable = false, length = 10)
	public String getAppKey() {
		return this.appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Column(name = "SECRET_KEY", length = 10)
	public String getSecretKey() {
		return this.secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Column(name = "CALLBACK_URL", length = 60)
	public String getCallbackUrl() {
		return this.callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	@Column(name = "DESCRIPTION", length = 10)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "NAME", length = 10)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "STATUS", precision = 11, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_TIME", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "USER_ID", precision = 11, scale = 0)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "OWNER", length = 10)
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(name = "APPROVAL", length = 4)
	public String getApproval() {
		return this.approval;
	}

	public void setApproval(String approval) {
		this.approval = approval;
	}

}