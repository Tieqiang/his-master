package com.jims.his.domain.oauth2.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * AppProfile entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "APP_PROFILE", schema = "JIMS")
public class AppProfile implements java.io.Serializable {

	// Fields

	private AppProfileId id;

	// Constructors

	/** default constructor */
	public AppProfile() {
	}

	/** full constructor */
	public AppProfile(AppProfileId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "appKey", column = @Column(name = "APP_KEY", length = 10)),
			@AttributeOverride(name = "createTime", column = @Column(name = "CREATE_TIME", length = 7)) })
	public AppProfileId getId() {
		return this.id;
	}

	public void setId(AppProfileId id) {
		this.id = id;
	}

}