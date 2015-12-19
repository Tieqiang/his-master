package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * CostItemDevideDept entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "COST_ITEM_DEVIDE_DEPT", schema = "HTCA")
public class CostItemDevideDept implements java.io.Serializable {

	// Fields

	private String id;
	private String costItemId;
	private String deptId;

	// Constructors

	/** default constructor */
	public CostItemDevideDept() {
	}

	/** full constructor */
	public CostItemDevideDept(String costItemId, String deptId) {
		this.costItemId = costItemId;
		this.deptId = deptId;
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

	@Column(name = "COST_ITEM_ID", length = 64)
	public String getCostItemId() {
		return this.costItemId;
	}

	public void setCostItemId(String costItemId) {
		this.costItemId = costItemId;
	}

	@Column(name = "DEPT_ID", length = 64)
	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

}