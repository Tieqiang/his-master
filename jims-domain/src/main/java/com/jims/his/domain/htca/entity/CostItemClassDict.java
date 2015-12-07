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
 * CostItemClassDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "COST_ITEM_CLASS_DICT", schema = "HTCA")
public class CostItemClassDict implements java.io.Serializable {

	// Fields
	private String id;
	private String costItemClassName;
	private String costItemClassCode;
	private String inputCode;
	private Set<CostItemDict> costItemDicts = new HashSet<CostItemDict>(0);
    private String hospitalId ;

	// Constructors

	/** default constructor */
	public CostItemClassDict() {
	}

	/** full constructor */
	public CostItemClassDict(String costItemClassName,
                             String costItemClassCode, String inputCode,
                             Set<CostItemDict> costItemDicts, String hospitalId) {
		this.costItemClassName = costItemClassName;
		this.costItemClassCode = costItemClassCode;
		this.inputCode = inputCode;
		this.costItemDicts = costItemDicts;
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

	@Column(name = "COST_ITEM_CLASS_NAME", length = 64)
	public String getCostItemClassName() {
		return this.costItemClassName;
	}

	public void setCostItemClassName(String costItemClassName) {
		this.costItemClassName = costItemClassName;
	}

	@Column(name = "COST_ITEM_CLASS_CODE", length = 20)
	public String getCostItemClassCode() {
		return this.costItemClassCode;
	}

	public void setCostItemClassCode(String costItemClassCode) {
		this.costItemClassCode = costItemClassCode;
	}

	@Column(name = "INPUT_CODE", length = 20)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

    @JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "costItemClassDict")
	public Set<CostItemDict> getCostItemDicts() {
		return this.costItemDicts;
	}

	public void setCostItemDicts(Set<CostItemDict> costItemDicts) {
		this.costItemDicts = costItemDicts;
	}

    @Column(name = "hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}