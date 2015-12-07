package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_USER", schema = "JIMS")
public class ExpUser implements java.io.Serializable {

	// Fields

	private String id;
	private String userId;
	private String userName;
	private String role;
	private String storeId;
	private String storeType;

	// Constructors

	/** default constructor */
	public ExpUser() {
	}

	/** full constructor */
	public ExpUser(String userId, String userName, String role, String storeId,
			String storeType) {
		this.userId = userId;
		this.userName = userName;
		this.role = role;
		this.storeId = storeId;
		this.storeType = storeType;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "USER_ID", length = 4)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME", length = 18)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "ROLE", length = 1)
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "STORE_ID", length = 10)
	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Column(name = "STORE_TYPE", length = 20)
	public String getStoreType() {
		return this.storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

}