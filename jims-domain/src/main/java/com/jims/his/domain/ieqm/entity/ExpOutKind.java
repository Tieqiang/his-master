package com.jims.his.domain.ieqm.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * ExpOutKind entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_OUT_KIND", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CODE", "STORE_ID" }))
public class ExpOutKind implements java.io.Serializable {

	// Fields

	private ExpOutKindId id;
	private String name;

	// Constructors

	/** default constructor */
	public ExpOutKind() {
	}

	/** minimal constructor */
	public ExpOutKind(ExpOutKindId id) {
		this.id = id;
	}

	/** full constructor */
	public ExpOutKind(ExpOutKindId id, String name) {
		this.id = id;
		this.name = name;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "code", column = @Column(name = "CODE", nullable = false, length = 2)),
			@AttributeOverride(name = "storeId", column = @Column(name = "STORE_ID", nullable = false, length = 10)),
			@AttributeOverride(name = "id", column = @Column(name = "ID", nullable = false, length = 64)) })
	public ExpOutKindId getId() {
		return this.id;
	}

	public void setId(ExpOutKindId id) {
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