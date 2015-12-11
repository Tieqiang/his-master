package com.jims.his.domain.htca.entity;

import java.lang.Integer;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 * AcctDeptDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCT_DEPT_DICT", schema = "HTCA")
public class AcctDeptDict implements java.io.Serializable {

	// Fields

	private String id;
	private String deptCode;
	private String deptName;
	private String deptAttr;
	private String deptOutpInp;
	private String inputCode;
	private String deptDevideAttr;
	private String deptLocation;
	private String deptOther;
	private String deptStopFlag;
	private String hospitalId;
	private String costAppInd;
	private String costAppLevel;
	private String deptType;
    private String deptClass ;//科室类别一般分为经营科室和其他
    private String endDept ;//是否末级科室
    private String parentId ;//父级科室代码

	// Constructors

	/** default constructor */
	public AcctDeptDict() {
	}

	/** full constructor */
	public AcctDeptDict(String deptCode, String deptName, String deptAttr,
                        String deptOutpInp, String inputCode, String deptDevideAttr,
                        String deptLocation, String deptOther, String deptStopFlag,
                        String hospitalId, String costAppInd, String costAppLevel,
                        String deptType,
                        String deptClass, String endDept, String parentId) {
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.deptAttr = deptAttr;
		this.deptOutpInp = deptOutpInp;
		this.inputCode = inputCode;
		this.deptDevideAttr = deptDevideAttr;
		this.deptLocation = deptLocation;
		this.deptOther = deptOther;
		this.deptStopFlag = deptStopFlag;
		this.hospitalId = hospitalId;
		this.costAppInd = costAppInd;
		this.costAppLevel = costAppLevel;
		this.deptType = deptType;
        this.deptClass = deptClass;
        this.endDept = endDept;
        this.parentId = parentId;
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

	@Column(name = "DEPT_CODE", length = 20)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "DEPT_NAME", length = 50)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "DEPT_ATTR", length = 50)
	public String getDeptAttr() {
		return this.deptAttr;
	}

	public void setDeptAttr(String deptAttr) {
		this.deptAttr = deptAttr;
	}

	@Column(name = "DEPT_OUTP_INP", precision = 22, scale = 0)
	public String getDeptOutpInp() {
		return this.deptOutpInp;
	}

	public void setDeptOutpInp(String deptOutpInp) {
		this.deptOutpInp = deptOutpInp;
	}

	@Column(name = "INPUT_CODE", length = 20)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "DEPT_DEVIDE_ATTR", length = 24)
	public String getDeptDevideAttr() {
		return this.deptDevideAttr;
	}

	public void setDeptDevideAttr(String deptDevideAttr) {
		this.deptDevideAttr = deptDevideAttr;
	}

	@Column(name = "DEPT_LOCATION", length = 80)
	public String getDeptLocation() {
		return this.deptLocation;
	}

	public void setDeptLocation(String deptLocation) {
		this.deptLocation = deptLocation;
	}

	@Column(name = "DEPT_OTHER", length = 100)
	public String getDeptOther() {
		return this.deptOther;
	}

	public void setDeptOther(String deptOther) {
		this.deptOther = deptOther;
	}

	@Column(name = "DEPT_STOP_FLAG", length = 1)
	public String getDeptStopFlag() {
		return this.deptStopFlag;
	}

	public void setDeptStopFlag(String deptStopFlag) {
		this.deptStopFlag = deptStopFlag;
	}

	@Column(name = "HOSPITAL_ID", length = 64)
	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	@Column(name = "COST_APP_IND", length = 1)
	public String getCostAppInd() {
		return this.costAppInd;
	}

	public void setCostAppInd(String costAppInd) {
		this.costAppInd = costAppInd;
	}

	@Column(name = "COST_APP_LEVEL", length = 6)
	public String getCostAppLevel() {
		return this.costAppLevel;
	}

	public void setCostAppLevel(String costAppLevel) {
		this.costAppLevel = costAppLevel;
	}

	@Column(name = "DEPT_TYPE", length = 10)
	public String getDeptType() {
		return this.deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}


    @Column(name="dept_class")
    public String getDeptClass() {
        return deptClass;
    }

    public void setDeptClass(String deptClass) {
        this.deptClass = deptClass;
    }

    @Column(name="end_dept")
    public String getEndDept() {
        return endDept;
    }

    public void setEndDept(String endDept) {
        this.endDept = endDept;
    }

    @Column(name="parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}