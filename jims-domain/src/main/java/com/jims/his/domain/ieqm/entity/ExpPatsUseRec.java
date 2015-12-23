package com.jims.his.domain.ieqm.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpPatsUseRec entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_PATS_USE_REC", schema = "JIMS")
public class ExpPatsUseRec implements java.io.Serializable {

	// Fields

	private String id;
	private String patientId;
	private Short visitId;
	private String applicantNo;
	private String storage;
	private String expCode;
	private String expSpec;
	private String units;
	private String batchNo;
	private String firmId;
	private String packageSpec;
	private String packageUnits;
	private String toxiProperty;
	private String orderedBy;
	private String prescribedBy;
	private Double purchasePrice;
	private Double tradePrice;
	private Double retailPrice;
	private Double quantity;
	private Double costs;
	private Double charges;
	private Double subPackage1;
	private String subPackageUnits1;
	private String subPackageSpec1;
	private Double subPackage2;
	private String subPackageUnits2;
	private String subPackageSpec2;
	private Date provideDate;
	private String operator;
	private String remark;
	private Date callbackDate;
	private String callbackDept;
	private String callbackOperator;
	private String docStatus;

	// Constructors

	/** default constructor */
	public ExpPatsUseRec() {
	}

	/** full constructor */
	public ExpPatsUseRec(String patientId, Short visitId, String applicantNo,
			String storage, String expCode, String expSpec, String units,
			String batchNo, String firmId, String packageSpec,
			String packageUnits, String toxiProperty, String orderedBy,
			String prescribedBy, Double purchasePrice, Double tradePrice,
			Double retailPrice, Double quantity, Double costs, Double charges,
			Double subPackage1, String subPackageUnits1,
			String subPackageSpec1, Double subPackage2,
			String subPackageUnits2, String subPackageSpec2, Date provideDate,
			String operator, String remark, Date callbackDate,
			String callbackDept, String callbackOperator, String docStatus) {
		this.patientId = patientId;
		this.visitId = visitId;
		this.applicantNo = applicantNo;
		this.storage = storage;
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.units = units;
		this.batchNo = batchNo;
		this.firmId = firmId;
		this.packageSpec = packageSpec;
		this.packageUnits = packageUnits;
		this.toxiProperty = toxiProperty;
		this.orderedBy = orderedBy;
		this.prescribedBy = prescribedBy;
		this.purchasePrice = purchasePrice;
		this.tradePrice = tradePrice;
		this.retailPrice = retailPrice;
		this.quantity = quantity;
		this.costs = costs;
		this.charges = charges;
		this.subPackage1 = subPackage1;
		this.subPackageUnits1 = subPackageUnits1;
		this.subPackageSpec1 = subPackageSpec1;
		this.subPackage2 = subPackage2;
		this.subPackageUnits2 = subPackageUnits2;
		this.subPackageSpec2 = subPackageSpec2;
		this.provideDate = provideDate;
		this.operator = operator;
		this.remark = remark;
		this.callbackDate = callbackDate;
		this.callbackDept = callbackDept;
		this.callbackOperator = callbackOperator;
		this.docStatus = docStatus;
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

	@Column(name = "PATIENT_ID", length = 10)
	public String getPatientId() {
		return this.patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	@Column(name = "VISIT_ID", precision = 3, scale = 0)
	public Short getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Short visitId) {
		this.visitId = visitId;
	}

	@Column(name = "APPLICANT_NO", length = 10)
	public String getApplicantNo() {
		return this.applicantNo;
	}

	public void setApplicantNo(String applicantNo) {
		this.applicantNo = applicantNo;
	}

	@Column(name = "STORAGE", length = 8)
	public String getStorage() {
		return this.storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	@Column(name = "EXP_CODE", length = 20)
	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	@Column(name = "EXP_SPEC", length = 20)
	public String getExpSpec() {
		return this.expSpec;
	}

	public void setExpSpec(String expSpec) {
		this.expSpec = expSpec;
	}

	@Column(name = "UNITS", length = 8)
	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Column(name = "BATCH_NO", length = 16)
	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	@Column(name = "FIRM_ID", length = 10)
	public String getFirmId() {
		return this.firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	@Column(name = "PACKAGE_SPEC", length = 20)
	public String getPackageSpec() {
		return this.packageSpec;
	}

	public void setPackageSpec(String packageSpec) {
		this.packageSpec = packageSpec;
	}

	@Column(name = "PACKAGE_UNITS", length = 8)
	public String getPackageUnits() {
		return this.packageUnits;
	}

	public void setPackageUnits(String packageUnits) {
		this.packageUnits = packageUnits;
	}

	@Column(name = "TOXI_PROPERTY", length = 2)
	public String getToxiProperty() {
		return this.toxiProperty;
	}

	public void setToxiProperty(String toxiProperty) {
		this.toxiProperty = toxiProperty;
	}

	@Column(name = "ORDERED_BY", length = 8)
	public String getOrderedBy() {
		return this.orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	@Column(name = "PRESCRIBED_BY", length = 8)
	public String getPrescribedBy() {
		return this.prescribedBy;
	}

	public void setPrescribedBy(String prescribedBy) {
		this.prescribedBy = prescribedBy;
	}

	@Column(name = "PURCHASE_PRICE", precision = 10, scale = 4)
	public Double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	@Column(name = "TRADE_PRICE", precision = 10, scale = 4)
	public Double getTradePrice() {
		return this.tradePrice;
	}

	public void setTradePrice(Double tradePrice) {
		this.tradePrice = tradePrice;
	}

	@Column(name = "RETAIL_PRICE", precision = 10, scale = 4)
	public Double getRetailPrice() {
		return this.retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	@Column(name = "QUANTITY", precision = 6)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "COSTS", precision = 12, scale = 4)
	public Double getCosts() {
		return this.costs;
	}

	public void setCosts(Double costs) {
		this.costs = costs;
	}

	@Column(name = "CHARGES", precision = 12, scale = 4)
	public Double getCharges() {
		return this.charges;
	}

	public void setCharges(Double charges) {
		this.charges = charges;
	}

	@Column(name = "SUB_PACKAGE_1", precision = 12)
	public Double getSubPackage1() {
		return this.subPackage1;
	}

	public void setSubPackage1(Double subPackage1) {
		this.subPackage1 = subPackage1;
	}

	@Column(name = "SUB_PACKAGE_UNITS_1", length = 8)
	public String getSubPackageUnits1() {
		return this.subPackageUnits1;
	}

	public void setSubPackageUnits1(String subPackageUnits1) {
		this.subPackageUnits1 = subPackageUnits1;
	}

	@Column(name = "SUB_PACKAGE_SPEC_1", length = 8)
	public String getSubPackageSpec1() {
		return this.subPackageSpec1;
	}

	public void setSubPackageSpec1(String subPackageSpec1) {
		this.subPackageSpec1 = subPackageSpec1;
	}

	@Column(name = "SUB_PACKAGE_2", precision = 12)
	public Double getSubPackage2() {
		return this.subPackage2;
	}

	public void setSubPackage2(Double subPackage2) {
		this.subPackage2 = subPackage2;
	}

	@Column(name = "SUB_PACKAGE_UNITS_2", length = 8)
	public String getSubPackageUnits2() {
		return this.subPackageUnits2;
	}

	public void setSubPackageUnits2(String subPackageUnits2) {
		this.subPackageUnits2 = subPackageUnits2;
	}

	@Column(name = "SUB_PACKAGE_SPEC_2", length = 8)
	public String getSubPackageSpec2() {
		return this.subPackageSpec2;
	}

	public void setSubPackageSpec2(String subPackageSpec2) {
		this.subPackageSpec2 = subPackageSpec2;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROVIDE_DATE", length = 7)
	public Date getProvideDate() {
		return this.provideDate;
	}

	public void setProvideDate(Date provideDate) {
		this.provideDate = provideDate;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "REMARK", length = 50)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CALLBACK_DATE", length = 7)
	public Date getCallbackDate() {
		return this.callbackDate;
	}

	public void setCallbackDate(Date callbackDate) {
		this.callbackDate = callbackDate;
	}

	@Column(name = "CALLBACK_DEPT", length = 10)
	public String getCallbackDept() {
		return this.callbackDept;
	}

	public void setCallbackDept(String callbackDept) {
		this.callbackDept = callbackDept;
	}

	@Column(name = "CALLBACK_OPERATOR", length = 20)
	public String getCallbackOperator() {
		return this.callbackOperator;
	}

	public void setCallbackOperator(String callbackOperator) {
		this.callbackOperator = callbackOperator;
	}

	@Column(name = "DOC_STATUS", length = 1)
	public String getDocStatus() {
		return this.docStatus;
	}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}

}