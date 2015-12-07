package com.jims.his.domain.ieqm.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * ExpInKind entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_IN_KIND", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CODE", "STORE_ID" }))
public class ExpInKind implements java.io.Serializable {

	// Fields

	private ExpInKindId id;
	private String name;

	// Constructors

	/** default constructor */
	public ExpInKind() {
	}

	/** minimal constructor */
	public ExpInKind(ExpInKindId id) {
		this.id = id;
	}

	/** full constructor */
	public ExpInKind(ExpInKindId id, String name) {
		this.id = id;
		this.name = name;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "id", column = @Column(name = "ID", nullable = false, length = 64)),
			@AttributeOverride(name = "code", column = @Column(name = "CODE", nullable = false, length = 2)),
			@AttributeOverride(name = "storeId", column = @Column(name = "STORE_ID", nullable = false, length = 10)) })
	public ExpInKindId getId() {
		return this.id;
	}

	public void setId(ExpInKindId id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}