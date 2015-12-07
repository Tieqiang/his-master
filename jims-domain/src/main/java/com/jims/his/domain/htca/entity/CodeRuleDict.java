package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * CodeRuleDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CODE_RULE_DICT", schema = "HTCA")
public class CodeRuleDict implements java.io.Serializable {

	// Fields

	private String id;
	private String ruleName;
	private String ruleValue;
	private String memo;
	private String hospitalId;

	// Constructors

	/** default constructor */
	public CodeRuleDict() {
	}

	/** full constructor */
	public CodeRuleDict(String ruleName, String ruleValue, String memo,
			String hospitalId) {
		this.ruleName = ruleName;
		this.ruleValue = ruleValue;
		this.memo = memo;
		this.hospitalId = hospitalId;
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

	@Column(name = "RULE_NAME", length = 20)
	public String getRuleName() {
		return this.ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	@Column(name = "RULE_VALUE", length = 20)
	public String getRuleValue() {
		return this.ruleValue;
	}

	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
	}

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "HOSPITAL_ID", length = 64)
	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

}