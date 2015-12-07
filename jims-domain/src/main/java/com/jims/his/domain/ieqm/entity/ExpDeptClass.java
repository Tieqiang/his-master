package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpDeptClass entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_DEPT_CLASS", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORE_ID", "DEPT_CODE", "SUPPLY_GROUP" }))
public class ExpDeptClass implements java.io.Serializable {

	// Fields

	private String id;
	private String storeId;
	private String deptCode;
	private Byte supplyGroup;
	private String groupName;

	// Constructors

	/** default constructor */
	public ExpDeptClass() {
	}

	/** full constructor */
	public ExpDeptClass(String storeId, String deptCode, Byte supplyGroup,
			String groupName) {
		this.storeId = storeId;
		this.deptCode = deptCode;
		this.supplyGroup = supplyGroup;
		this.groupName = groupName;
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

	@Column(name = "STORE_ID", length = 10)
	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Column(name = "DEPT_CODE", length = 8)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "SUPPLY_GROUP", precision = 2, scale = 0)
	public Byte getSupplyGroup() {
		return this.supplyGroup;
	}

	public void setSupplyGroup(Byte supplyGroup) {
		this.supplyGroup = supplyGroup;
	}

	@Column(name = "GROUP_NAME", length = 20)
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}