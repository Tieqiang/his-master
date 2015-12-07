package com.jims.his.domain.htca.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * CostItemDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "COST_ITEM_DICT", schema = "HTCA")
public class CostItemDict implements java.io.Serializable {

	// Fields

	private String id;
	private CostItemClassDict costItemClassDict;
	private String costItemName;
	private String costItemCode;
	private String inputCode;
	private String costAttr;
	private String armyLocayCalcWay;
	private String secondCalcWay;
	private String personCalcWay;
	private String getWay;
	private String calcType;
	private String costType;
	private String calcPercent;
    private String hospitalId;
    private Double inpOrderRate ;
    private Double inpPerformRate ;
    private Double inpWardRate ;
    private Double outpOrderRate ;
    private Double outpPerformRate ;
    private Double outpWardRate ;

	// Constructors

	/** default constructor */
	public CostItemDict() {
	}

	/** full constructor */
	public CostItemDict(CostItemClassDict costItemClassDict,
                        String costItemName, String costItemCode, String inputCode,
                        String costAttr, String armyLocayCalcWay, String secondCalcWay,
                        String personCalcWay, String getWay, String calcType,
                        String costType, String calcPercent, String hospitalId, Double inpOrderRate, Double inpPerformRate, Double inpWardRate, Double outpOrderRate, Double outpPerformRate, Double outpWardRate) {
		this.costItemClassDict = costItemClassDict;
		this.costItemName = costItemName;
		this.costItemCode = costItemCode;
		this.inputCode = inputCode;
		this.costAttr = costAttr;
		this.armyLocayCalcWay = armyLocayCalcWay;
		this.secondCalcWay = secondCalcWay;
		this.personCalcWay = personCalcWay;
		this.getWay = getWay;
		this.calcType = calcType;
		this.costType = costType;
		this.calcPercent = calcPercent;
        this.hospitalId = hospitalId;

        this.inpOrderRate = inpOrderRate;
        this.inpPerformRate = inpPerformRate;
        this.inpWardRate = inpWardRate;
        this.outpOrderRate = outpOrderRate;
        this.outpPerformRate = outpPerformRate;
        this.outpWardRate = outpWardRate;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COST_CLASS_ID")
	public CostItemClassDict getCostItemClassDict() {
		return this.costItemClassDict;
	}

	public void setCostItemClassDict(CostItemClassDict costItemClassDict) {
		this.costItemClassDict = costItemClassDict;
	}

	@Column(name = "COST_ITEM_NAME", length = 100)
	public String getCostItemName() {
		return this.costItemName;
	}

	public void setCostItemName(String costItemName) {
		this.costItemName = costItemName;
	}

	@Column(name = "COST_ITEM_CODE", length = 20)
	public String getCostItemCode() {
		return this.costItemCode;
	}

	public void setCostItemCode(String costItemCode) {
		this.costItemCode = costItemCode;
	}

	@Column(name = "INPUT_CODE", length = 50)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "COST_ATTR", length = 10)
	public String getCostAttr() {
		return this.costAttr;
	}

	public void setCostAttr(String costAttr) {
		this.costAttr = costAttr;
	}

	@Column(name = "ARMY_LOCAY_CALC_WAY", length = 20)
	public String getArmyLocayCalcWay() {
		return this.armyLocayCalcWay;
	}

	public void setArmyLocayCalcWay(String armyLocayCalcWay) {
		this.armyLocayCalcWay = armyLocayCalcWay;
	}

	@Column(name = "SECOND_CALC_WAY", length = 20)
	public String getSecondCalcWay() {
		return this.secondCalcWay;
	}

	public void setSecondCalcWay(String secondCalcWay) {
		this.secondCalcWay = secondCalcWay;
	}

	@Column(name = "PERSON_CALC_WAY", length = 20)
	public String getPersonCalcWay() {
		return this.personCalcWay;
	}

	public void setPersonCalcWay(String personCalcWay) {
		this.personCalcWay = personCalcWay;
	}

	@Column(name = "GET_WAY", length = 20)
	public String getGetWay() {
		return this.getWay;
	}

	public void setGetWay(String getWay) {
		this.getWay = getWay;
	}

	@Column(name = "CALC_TYPE", length = 20)
	public String getCalcType() {
		return this.calcType;
	}

	public void setCalcType(String calcType) {
		this.calcType = calcType;
	}

	@Column(name = "COST_TYPE", length = 20)
	public String getCostType() {
		return this.costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	@Column(name = "CALC_PERCENT", length = 20)
	public String getCalcPercent() {
		return this.calcPercent;
	}

	public void setCalcPercent(String calcPercent) {
		this.calcPercent = calcPercent;
	}

    @Column(name="hospital_id")
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Column(name="inp_order_rate")
    public Double getInpOrderRate() {
        return inpOrderRate;
    }

    public void setInpOrderRate(Double inpOrderRate) {
        this.inpOrderRate = inpOrderRate;
    }

    @Column(name="inp_perform_rate")
    public Double getInpPerformRate() {
        return inpPerformRate;
    }

    public void setInpPerformRate(Double inpPerformRate) {
        this.inpPerformRate = inpPerformRate;
    }

    @Column(name="inp_ward_rate")
    public Double getInpWardRate() {
        return inpWardRate;
    }

    public void setInpWardRate(Double inpWardRate) {
        this.inpWardRate = inpWardRate;
    }

    @Column(name="outp_order_rate")
    public Double getOutpOrderRate() {
        return outpOrderRate;
    }

    public void setOutpOrderRate(Double outpOrderRate) {
        this.outpOrderRate = outpOrderRate;
    }

    @Column(name="outp_perform_rate")
    public Double getOutpPerformRate() {
        return outpPerformRate;
    }

    public void setOutpPerformRate(Double outpPerformRate) {
        this.outpPerformRate = outpPerformRate;
    }

    @Column(name="outp_ward_rate")
    public Double getOutpWardRate() {
        return outpWardRate;
    }

    public void setOutpWardRate(Double outpWardRate) {
        this.outpWardRate = outpWardRate;
    }
}