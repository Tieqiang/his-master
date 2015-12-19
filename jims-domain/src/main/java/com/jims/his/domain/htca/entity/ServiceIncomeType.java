package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * ServieIncomeType entity. @author ztq
 */
@Entity
@Table(name = "service_income_type", schema = "HTCA")
public class ServiceIncomeType implements java.io.Serializable {

	// Fields

	private String id;
	private String serviceTypeName;
	private String inputCode;
	private String hospitalId;

	// Constructors

	/** default constructor */
	public ServiceIncomeType() {
	}

	/** full constructor */
	public ServiceIncomeType(String serviceTypeName, String inputCode,
                             String hospitalId) {
		this.serviceTypeName = serviceTypeName;
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

	@Column(name = "SERVICE_TYPE_NAME", length = 100)
	public String getServiceTypeName() {
		return this.serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	@Column(name = "INPUT_CODE", length = 50)
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