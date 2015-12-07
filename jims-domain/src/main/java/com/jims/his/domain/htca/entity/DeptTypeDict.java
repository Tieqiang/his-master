package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * DeptTypeDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DEPT_TYPE_DICT", schema = "HTCA")
public class DeptTypeDict implements java.io.Serializable {

	// Fields

	private String id;
	private String deptTypeCode;
	private String deptTypeName;
	private String inputCode;
	private String hospitalId;

	// Constructors

	/** default constructor */
	public DeptTypeDict() {
	}

	/** full constructor */
	public DeptTypeDict(String deptTypeCode, String deptTypeName,
			String inputCode, String hospitalId) {
		this.deptTypeCode = deptTypeCode;
		this.deptTypeName = deptTypeName;
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

	@Column(name = "DEPT_TYPE_CODE", length = 10)
	public String getDeptTypeCode() {
		return this.deptTypeCode;
	}

	public void setDeptTypeCode(String deptTypeCode) {
		this.deptTypeCode = deptTypeCode;
	}

	@Column(name = "DEPT_TYPE_NAME", length = 20)
	public String getDeptTypeName() {
		return this.deptTypeName;
	}

	public void setDeptTypeName(String deptTypeName) {
		this.deptTypeName = deptTypeName;
	}

	@Column(name = "INPUT_CODE", length = 20)
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