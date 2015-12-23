package com.jims.his.domain.ieqm.entity;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * ExpProvideApplication entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_PROVIDE_APPLICATION", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"APPLICANT_NO", "ITEM_NO", "PROVIDE_STORAGE" }))
public class ExpProvideApplication implements java.io.Serializable {

	// Fields

	private String id;
	private String applicantStorage;
	private String provideStorage;
	private Short itemNo;
	private String expCode;
	private String expSpec;
	private String packageSpec;
	private Double quantity;
	private String packageUnits;
	private Date enterDateTime;
	private String provideFlag;
	private String exportDocumentNo;
	private String applicantNo;
	private String applicationMan;
	private String patientId;
	private Byte visitId;
	private Boolean retflag;
	private Date retdate;
	private String retoperator;
	private String auditingOperator;
	private Double auditingQuantity;
    @Transient
    private String expName;
	// Constructors

	/** default constructor */
	public ExpProvideApplication() {
	}

	/** full constructor */
	public ExpProvideApplication(String applicantStorage,
			String provideStorage, Short itemNo, String expCode,
			String expSpec, String packageSpec, Double quantity,
			String packageUnits, Date enterDateTime, String provideFlag,
			String exportDocumentNo, String applicantNo, String applicationMan,
			String patientId, Byte visitId, Boolean retflag, Date retdate,
			String retoperator, String auditingOperator, Double auditingQuantity) {
		this.applicantStorage = applicantStorage;
		this.provideStorage = provideStorage;
		this.itemNo = itemNo;
		this.expCode = expCode;
		this.expSpec = expSpec;
		this.packageSpec = packageSpec;
		this.quantity = quantity;
		this.packageUnits = packageUnits;
		this.enterDateTime = enterDateTime;
		this.provideFlag = provideFlag;
		this.exportDocumentNo = exportDocumentNo;
		this.applicantNo = applicantNo;
		this.applicationMan = applicationMan;
		this.patientId = patientId;
		this.visitId = visitId;
		this.retflag = retflag;
		this.retdate = retdate;
		this.retoperator = retoperator;
		this.auditingOperator = auditingOperator;
		this.auditingQuantity = auditingQuantity;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 64)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "APPLICANT_STORAGE", length = 8)
	public String getApplicantStorage() {
		return this.applicantStorage;
	}

	public void setApplicantStorage(String applicantStorage) {
		this.applicantStorage = applicantStorage;
	}

	@Column(name = "PROVIDE_STORAGE", length = 8)
	public String getProvideStorage() {
		return this.provideStorage;
	}

	public void setProvideStorage(String provideStorage) {
		this.provideStorage = provideStorage;
	}

	@Column(name = "ITEM_NO", precision = 4, scale = 0)
	public Short getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(Short itemNo) {
		this.itemNo = itemNo;
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

	@Column(name = "PACKAGE_SPEC", length = 20)
	public String getPackageSpec() {
		return this.packageSpec;
	}

	public void setPackageSpec(String packageSpec) {
		this.packageSpec = packageSpec;
	}

	@Column(name = "QUANTITY", precision = 12)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "PACKAGE_UNITS", length = 8)
	public String getPackageUnits() {
		return this.packageUnits;
	}

	public void setPackageUnits(String packageUnits) {
		this.packageUnits = packageUnits;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTER_DATE_TIME", length = 7)
	public Date getEnterDateTime() {
		return this.enterDateTime;
	}

	public void setEnterDateTime(Date enterDateTime) {
		this.enterDateTime = enterDateTime;
	}

	@Column(name = "PROVIDE_FLAG", length = 1)
	public String getProvideFlag() {
		return this.provideFlag;
	}

	public void setProvideFlag(String provideFlag) {
		this.provideFlag = provideFlag;
	}

	@Column(name = "EXPORT_DOCUMENT_NO", length = 10)
	public String getExportDocumentNo() {
		return this.exportDocumentNo;
	}

	public void setExportDocumentNo(String exportDocumentNo) {
		this.exportDocumentNo = exportDocumentNo;
	}

	@Column(name = "APPLICANT_NO", length = 10)
	public String getApplicantNo() {
		return this.applicantNo;
	}

	public void setApplicantNo(String applicantNo) {
		this.applicantNo = applicantNo;
	}

	@Column(name = "APPLICATION_MAN", length = 20)
	public String getApplicationMan() {
		return this.applicationMan;
	}

	public void setApplicationMan(String applicationMan) {
		this.applicationMan = applicationMan;
	}

	@Column(name = "PATIENT_ID", length = 20)
	public String getPatientId() {
		return this.patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	@Column(name = "VISIT_ID", precision = 2, scale = 0)
	public Byte getVisitId() {
		return this.visitId;
	}

	public void setVisitId(Byte visitId) {
		this.visitId = visitId;
	}

	@Column(name = "RETFLAG", precision = 1, scale = 0)
	public Boolean getRetflag() {
		return this.retflag;
	}

	public void setRetflag(Boolean retflag) {
		this.retflag = retflag;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RETDATE", length = 7)
	public Date getRetdate() {
		return this.retdate;
	}

	public void setRetdate(Date retdate) {
		this.retdate = retdate;
	}

	@Column(name = "RETOPERATOR", length = 20)
	public String getRetoperator() {
		return this.retoperator;
	}

	public void setRetoperator(String retoperator) {
		this.retoperator = retoperator;
	}

	@Column(name = "AUDITING_OPERATOR", length = 20)
	public String getAuditingOperator() {
		return this.auditingOperator;
	}

	public void setAuditingOperator(String auditingOperator) {
		this.auditingOperator = auditingOperator;
	}

	@Column(name = "AUDITING_QUANTITY", precision = 12)
	public Double getAuditingQuantity() {
		return this.auditingQuantity;
	}

	public void setAuditingQuantity(Double auditingQuantity) {
		this.auditingQuantity = auditingQuantity;
	}

    @Transient
    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }
}