package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpStockName entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_STOCK_NAME", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "STORE_ID"))
public class ExpStockName implements java.io.Serializable {

	// Fields

	private String id;
	private String storeId;
	private String code;
	private String name;
	private String storeType;
	private String memo1;
	private String memo2;
	private String memo3;

	// Constructors

	/** default constructor */
	public ExpStockName() {
	}

	/** full constructor */
	public ExpStockName(String storeId, String code, String name,
			String storeType, String memo1, String memo2, String memo3) {
		this.storeId = storeId;
		this.code = code;
		this.name = name;
		this.storeType = storeType;
		this.memo1 = memo1;
		this.memo2 = memo2;
		this.memo3 = memo3;
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

	@Column(name = "STORE_ID", unique = true, length = 10)
	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Column(name = "CODE", length = 8)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "STORE_TYPE", length = 20)
	public String getStoreType() {
		return this.storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	@Column(name = "MEMO1", length = 20)
	public String getMemo1() {
		return this.memo1;
	}

	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}

	@Column(name = "MEMO2", length = 20)
	public String getMemo2() {
		return this.memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}

	@Column(name = "MEMO3", length = 20)
	public String getMemo3() {
		return this.memo3;
	}

	public void setMemo3(String memo3) {
		this.memo3 = memo3;
	}

}