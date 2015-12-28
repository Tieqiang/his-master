package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * ServiceIncomeTypeDetail entity. @author ztq
 */
@Entity
@Table(name = "SERVICE_INCOME_TYPE_DETAIL", schema = "HTCA")
public class ServiceIncomeTypeDetail implements java.io.Serializable {

	// Fields

	private String id;
	private String incomeDetailName;
	private String inputCode;
	private String incomeTypeId;
	private String unit;
	private Double price;
	private String hospitalId;
    private Double addRate ;

	// Constructors

	/** default constructor */
	public ServiceIncomeTypeDetail() {
	}

	/** full constructor */
	public ServiceIncomeTypeDetail(String incomeDetailName, String inputCode,
                                   String incomeTypeId, String unit, Double price,
                                   String hospitalId, Double addRate) {
		this.incomeDetailName = incomeDetailName;
		this.inputCode = inputCode;
		this.incomeTypeId = incomeTypeId;
		this.unit = unit;
		this.price = price;
		this.hospitalId = hospitalId;
        this.addRate = addRate;
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

	@Column(name = "INCOME_DETAIL_NAME", length = 100)
	public String getIncomeDetailName() {
		return this.incomeDetailName;
	}

	public void setIncomeDetailName(String incomeDetailName) {
		this.incomeDetailName = incomeDetailName;
	}

	@Column(name = "INPUT_CODE", length = 50)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "INCOME_TYPE_ID", length = 64)
	public String getIncomeTypeId() {
		return this.incomeTypeId;
	}

	public void setIncomeTypeId(String incomeTypeId) {
		this.incomeTypeId = incomeTypeId;
	}

	@Column(name = "UNIT", length = 10)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "PRICE", precision = 22, scale = 0)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "HOSPITAL_ID", length = 64)
	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

    @Column(name="add_rate")
    public Double getAddRate() {
        return addRate;
    }

    public void setAddRate(Double addRate) {
        this.addRate = addRate;
    }


}