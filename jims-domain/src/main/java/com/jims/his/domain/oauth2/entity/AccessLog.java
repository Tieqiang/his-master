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
 * AccessLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCESS_LOG", schema = "JIMS")
public class AccessLog implements java.io.Serializable {

	// Fields

	private Long accessLogId;
	private Date accessTime;
	private Date lastTime;
	private String averageTime;
	private Long count;
	private Long userId;
	private String appKey;

	// Constructors

	/** default constructor */
	public AccessLog() {
	}

	/** full constructor */
	public AccessLog(Date accessTime, Date lastTime, String averageTime,
			Long count, Long userId, String appKey) {
		this.accessTime = accessTime;
		this.lastTime = lastTime;
		this.averageTime = averageTime;
		this.count = count;
		this.userId = userId;
		this.appKey = appKey;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ACCESS_LOG_ID", unique = true, nullable = false, precision = 11, scale = 0)
	public Long getAccessLogId() {
		return this.accessLogId;
	}

	public void setAccessLogId(Long accessLogId) {
		this.accessLogId = accessLogId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ACCESS_TIME", length = 7)
	public Date getAccessTime() {
		return this.accessTime;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_TIME", length = 7)
	public Date getLastTime() {
		return this.lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	@Column(name = "AVERAGE_TIME", length = 10)
	public String getAverageTime() {
		return this.averageTime;
	}

	public void setAverageTime(String averageTime) {
		this.averageTime = averageTime;
	}

	@Column(name = "COUNT", precision = 11, scale = 0)
	public Long getCount() {
		return this.count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Column(name = "USER_ID", precision = 11, scale = 0)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "APP_KEY", length = 10)
	public String getAppKey() {
		return this.appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

}