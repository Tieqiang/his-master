package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpCommonList entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_COMMON_LIST", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORE_ID", "DEPT_CODE", "SUPPLY_GROUP", "LIST_NO" }))
public class ExpCommonList implements java.io.Serializable {

	// Fields

	private String id;
	private String storeId;
	private String deptCode;
	private Byte supplyGroup;
	private Short listNo;
	private String infoId;
	private String onceSign;

	// Constructors

	/** default constructor */
	public ExpCommonList() {
	}

	/** full constructor */
	public ExpCommonList(String storeId, String deptCode, Byte supplyGroup,
			Short listNo, String infoId, String onceSign) {
		this.storeId = storeId;
		this.deptCode = deptCode;
		this.supplyGroup = supplyGroup;
		this.listNo = listNo;
		this.infoId = infoId;
		this.onceSign = onceSign;
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

	@Column(name = "LIST_NO", precision = 3, scale = 0)
	public Short getListNo() {
		return this.listNo;
	}

	public void setListNo(Short listNo) {
		this.listNo = listNo;
	}

	@Column(name = "INFO_ID", length = 12)
	public String getInfoId() {
		return this.infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	@Column(name = "ONCE_SIGN", length = 1)
	public String getOnceSign() {
		return this.onceSign;
	}

	public void setOnceSign(String onceSign) {
		this.onceSign = onceSign;
	}

}