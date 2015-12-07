package com.jims.his.domain.htca.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 * IncomeTypeDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INCOME_TYPE_DICT", schema = "HTCA")
public class IncomeTypeDict implements java.io.Serializable {

	// Fields

	private String id;
	private String incomeTypeCode;
	private String incomeTypeName;
	private String stopFlag;
	private String inputCode;
	private String hospitalId;
	private Set<IncomeItemDict> incomeItemDicts = new HashSet<IncomeItemDict>(0);

	// Constructors

	/** default constructor */
	public IncomeTypeDict() {
	}

	/** full constructor */
	public IncomeTypeDict(String incomeTypeCode, String incomeTypeName,
			String stopFlag, String inputCode, String hospitalId,
			Set<IncomeItemDict> incomeItemDicts) {
		this.incomeTypeCode = incomeTypeCode;
		this.incomeTypeName = incomeTypeName;
		this.stopFlag = stopFlag;
		this.inputCode = inputCode;
		this.hospitalId = hospitalId;
		this.incomeItemDicts = incomeItemDicts;
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

	@Column(name = "INCOME_TYPE_CODE", length = 20)
	public String getIncomeTypeCode() {
		return this.incomeTypeCode;
	}

	public void setIncomeTypeCode(String incomeTypeCode) {
		this.incomeTypeCode = incomeTypeCode;
	}

	@Column(name = "INCOME_TYPE_NAME", length = 20)
	public String getIncomeTypeName() {
		return this.incomeTypeName;
	}

	public void setIncomeTypeName(String incomeTypeName) {
		this.incomeTypeName = incomeTypeName;
	}

	@Column(name = "STOP_FLAG", length = 1)
	public String getStopFlag() {
		return this.stopFlag;
	}

	public void setStopFlag(String stopFlag) {
		this.stopFlag = stopFlag;
	}

	@Column(name = "INPUT_CODE", length = 2)
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

    @JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "incomeTypeDict")
	public Set<IncomeItemDict> getIncomeItemDicts() {
		return this.incomeItemDicts;
	}

	public void setIncomeItemDicts(Set<IncomeItemDict> incomeItemDicts) {
		this.incomeItemDicts = incomeItemDicts;
	}

}