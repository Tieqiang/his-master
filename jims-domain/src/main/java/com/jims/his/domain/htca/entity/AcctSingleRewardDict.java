package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * AcctSingleRewardDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCT_SINGLE_REWARD_DICT", schema = "HTCA")
public class AcctSingleRewardDict implements java.io.Serializable {

	// Fields

	private String id;
	private String rewardName;
	private String inputCode;
	private String hospitalId;

	// Constructors

	/** default constructor */
	public AcctSingleRewardDict() {
	}

	/** full constructor */
	public AcctSingleRewardDict(String rewardName, String inputCode,
			String hospitalId) {
		this.rewardName = rewardName;
		this.inputCode = inputCode;
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

	@Column(name = "REWARD_NAME", length = 200)
	public String getRewardName() {
		return this.rewardName;
	}

	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}

	@Column(name = "INPUT_CODE", length = 100)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "HOSPITAL_ID", length = 64)
	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

}