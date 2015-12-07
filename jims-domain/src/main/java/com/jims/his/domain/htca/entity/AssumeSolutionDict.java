package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 成本核算解决方案
 * @author heren
 */
@Entity
@Table(name = "ASSUME_SOLUTION_DICT", schema = "HTCA")
public class AssumeSolutionDict implements java.io.Serializable {

	// Fields

	private String id;
	private String assumeSolutionName;
	private String assumeSolutionCode;
	private String inputCode;

	// Constructors

	/** default constructor */
	public AssumeSolutionDict() {
	}

	/** full constructor */
	public AssumeSolutionDict(String assumeSolutionName,
                              String assumeSolutionCode, String inputCode) {
		this.assumeSolutionName = assumeSolutionName;
		this.assumeSolutionCode = assumeSolutionCode;
		this.inputCode = inputCode;
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

	@Column(name = "ASSUME_SOLUTION_NAME", length = 100)
	public String getAssumeSolutionName() {
		return this.assumeSolutionName;
	}

	public void setAssumeSolutionName(String assumeSolutionName) {
		this.assumeSolutionName = assumeSolutionName;
	}

	@Column(name = "ASSUME_SOLUTION_CODE", length = 50)
	public String getAssumeSolutionCode() {
		return this.assumeSolutionCode;
	}

	public void setAssumeSolutionCode(String assumeSolutionCode) {
		this.assumeSolutionCode = assumeSolutionCode;
	}

	@Column(name = "INPUT_CODE", length = 100)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

}