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
 * IncomeItemDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INCOME_ITEM_DICT", schema = "HTCA")
public class IncomeItemDict implements java.io.Serializable {

	// Fields

	private String id;
	private IncomeTypeDict incomeTypeDict;
	private String reckItemCode;
	private String reckItemName;
	private String stopFlag;
	private String inputCode;
	private String memo;
	private String inpOrderedBy;
	private String inpPerformedBy;
	private String inpWardCode;
    private String outpOrderedBy ;
    private String outpPerformedBy ;
    private String outpWardCode ;
	private String hospitalId;
    private String priceItemCode;
    private String priceItemName ;
    private String priceItemClass ;

	// Constructors

	/** default constructor */
	public IncomeItemDict() {
	}

    public IncomeItemDict(String id, IncomeTypeDict incomeTypeDict, String reckItemCode, String reckItemName, String stopFlag, String inputCode, String memo, String inpOrderedBy, String inpPerformedBy, String inpWardCode, String outpOrderedBy, String outpPerformedBy, String outpWardCode, String hospitalId, String priceItemCode, String priceItemName, String priceItemClass) {
        this.id = id;
        this.incomeTypeDict = incomeTypeDict;
        this.reckItemCode = reckItemCode;
        this.reckItemName = reckItemName;
        this.stopFlag = stopFlag;
        this.inputCode = inputCode;
        this.memo = memo;
        this.inpOrderedBy = inpOrderedBy;
        this.inpPerformedBy = inpPerformedBy;
        this.inpWardCode = inpWardCode;
        this.outpOrderedBy = outpOrderedBy;
        this.outpPerformedBy = outpPerformedBy;
        this.outpWardCode = outpWardCode;
        this.hospitalId = hospitalId;
        this.priceItemCode = priceItemCode;
        this.priceItemName = priceItemName;
        this.priceItemClass = priceItemClass;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INCOME_TYPE_ID")
	public IncomeTypeDict getIncomeTypeDict() {
		return this.incomeTypeDict;
	}

	public void setIncomeTypeDict(IncomeTypeDict incomeTypeDict) {
		this.incomeTypeDict = incomeTypeDict;
	}

	@Column(name = "RECK_ITEM_CODE", length = 20)
	public String getReckItemCode() {
		return this.reckItemCode;
	}

	public void setReckItemCode(String reckItemCode) {
		this.reckItemCode = reckItemCode;
	}

	@Column(name = "RECK_ITEM_NAME", length = 20)
	public String getReckItemName() {
		return this.reckItemName;
	}

	public void setReckItemName(String reckItemName) {
		this.reckItemName = reckItemName;
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

	@Column(name = "MEMO", length = 1000)
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

    @Column(name="price_item_code")
    public String getPriceItemCode() {
        return priceItemCode;
    }

    public void setPriceItemCode(String priceItemCode) {
        this.priceItemCode = priceItemCode;
    }

    @Column(name="price_item_name")
    public String getPriceItemName() {
        return priceItemName;
    }

    public void setPriceItemName(String priceItemName) {
        this.priceItemName = priceItemName;
    }

    @Column(name = "price_item_class")
    public String getPriceItemClass() {
        return priceItemClass;
    }

    public void setPriceItemClass(String priceItemClass) {
        this.priceItemClass = priceItemClass;
    }

    @Column(name="inp_ordered_by")
    public String getInpOrderedBy() {
        return inpOrderedBy;
    }

    public void setInpOrderedBy(String inpOrderedBy) {
        this.inpOrderedBy = inpOrderedBy;
    }

    @Column(name="inp_performed_by")
    public String getInpPerformedBy() {
        return inpPerformedBy;
    }

    public void setInpPerformedBy(String inpPerformedBy) {
        this.inpPerformedBy = inpPerformedBy;
    }

    @Column(name="inp_ward_code")
    public String getInpWardCode() {
        return inpWardCode;
    }

    public void setInpWardCode(String inpWardCode) {
        this.inpWardCode = inpWardCode;
    }

    @Column(name="outp_ordered_by")
    public String getOutpOrderedBy() {
        return outpOrderedBy;
    }

    public void setOutpOrderedBy(String outpOrderedBy) {
        this.outpOrderedBy = outpOrderedBy;
    }

    @Column(name="outp_performed_by")
    public String getOutpPerformedBy() {
        return outpPerformedBy;
    }

    public void setOutpPerformedBy(String outpPerformedBy) {
        this.outpPerformedBy = outpPerformedBy;
    }

    @Column(name="outp_ward_code")
    public String getOutpWardCode() {
        return outpWardCode;
    }

    public void setOutpWardCode(String outpWardCode) {
        this.outpWardCode = outpWardCode;
    }
}