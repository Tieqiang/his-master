package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpAlias entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_ALIAS", schema = "JIMS")
public class ExpAlias implements java.io.Serializable {

	// Fields

	private String id;
	private String aliasName;
	private String sign;
	private String expCode;
	private String inCode;
	private String operate;

	// Constructors

	/** default constructor */
	public ExpAlias() {
	}

	/** full constructor */
	public ExpAlias(String aliasName, String sign, String expCode,
			String inCode, String operate) {
		this.aliasName = aliasName;
		this.sign = sign;
		this.expCode = expCode;
		this.inCode = inCode;
		this.operate = operate;
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

	@Column(name = "ALIAS_NAME", length = 30)
	public String getAliasName() {
		return this.aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	@Column(name = "SIGN", length = 1)
	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Column(name = "EXP_CODE", length = 20)
	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	@Column(name = "IN_CODE", length = 8)
	public String getInCode() {
		return this.inCode;
	}

	public void setInCode(String inCode) {
		this.inCode = inCode;
	}

	@Column(name = "OPERATE", length = 20)
	public String getOperate() {
		return this.operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

}