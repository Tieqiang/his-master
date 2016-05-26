package com.jims.his.domain.oauth2.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Authority entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AUTHORITY", schema = "JIMS")
public class Authority implements java.io.Serializable {

	// Fields

	private AuthorityId id;

	// Constructors

	/** default constructor */
	public Authority() {
	}

	/** full constructor */
	public Authority(AuthorityId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "appKey", column = @Column(name = "APP_KEY", nullable = false, length = 10)),
			@AttributeOverride(name = "userId", column = @Column(name = "USER_ID", nullable = false, precision = 11, scale = 0)) })
	public AuthorityId getId() {
		return this.id;
	}

	public void setId(AuthorityId id) {
		this.id = id;
	}

}