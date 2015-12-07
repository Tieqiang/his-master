package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * AssumeSolutionItemDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ASSUME_SOLUTION_ITEM_DICT", schema = "HTCA")
public class AssumeSolutionItemDict implements java.io.Serializable {

	// Fields

	private String id;
	private String solutionItemCode;
	private String solutionItemName;
	private String inputCode;
	private String solutionRate;
    private String solutionId ;

	// Constructors

	/** default constructor */
	public AssumeSolutionItemDict() {
	}

	/** full constructor */
	public AssumeSolutionItemDict(String solutionItemCode, String solutionItemName,
			String inputCode, String solutionRate) {
		this.solutionItemCode = solutionItemCode;
		this.solutionItemName = solutionItemName;
		this.inputCode = inputCode;
		this.solutionRate = solutionRate;
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

	@Column(name = "SOLUTION_ITEM_CODE", length = 100)
	public String getSolutionItemCode() {
		return this.solutionItemCode;
	}

	public void setSolutionItemCode(String solutionItemCode) {
		this.solutionItemCode = solutionItemCode;
	}

	@Column(name = "SOLUTION_ITEM_NAME", length = 100)
	public String getSolutionItemName() {
		return this.solutionItemName;
	}

	public void setSolutionItemName(String solutionItemName) {
		this.solutionItemName = solutionItemName;
	}

	@Column(name = "INPUT_CODE", length = 50)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "SOLUTION_RATE", length = 100)
	public String getSolutionRate() {
		return this.solutionRate;
	}

	public void setSolutionRate(String solutionRate) {
		this.solutionRate = solutionRate;
	}

    @Column(name="solution_id")
    public String getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }
}