package com.jims.his.domain.ieqm.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpBarCodeRec entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_BAR_CODE_REC", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"BAR_CODE", "REC_TYPE" }))
public class ExpBarCodeRec implements java.io.Serializable {

	// Fields

	private String id;
	private String barCode;
	private String recType;
	private String patientId;
	private Byte visitId;
	private String inpNo;
	private String name;
	private String sex;
	private String identity;
	private String chargeType;
	private String orderedBy;
	private String performedBy;
	private String operator;
	private Date recDate;
	private String orderedDoctor;

	// Constructors

	/** default constructor */
	public ExpBarCodeRec() {
	}

	/** full constructor */
	public ExpBarCodeRec(String barCode, String recType, String patientId,
			Byte visitId, String inpNo, String name, String sex,
			String identity, String chargeType, String orderedBy,
			String performedBy, String operator, Date recDate,
			String orderedDoctor) {
		this.barCode = barCode;
		this.recType = recType;
		this.patientId = patientId;
		this.visitId = visitId;
		this.inpNo = inpNo;
		this.name = name;
		this.sex = sex;
		this.identity = identity;
		this.chargeType = chargeType;
		this.orderedBy = orderedBy;
		this.performedBy = performedBy;
		this.operator = operator;
		this.recDate = recDate;
		this.orderedDoctor = orderedDoctor;
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

	@Column(name = "BAR_CODE", length = 40)
	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@Column(name = "REC_TYPE", length = 4)
	public String getRecType() {
		return this.recType;
	}

	public void setRecType(String recType) {
		this.recType = recType;
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

	@Column(name = "INP_NO", length = 10)
	public String getInpNo() {
		return this.inpNo;
	}

	public void setInpNo(String inpNo) {
		this.inpNo = inpNo;
	}

	@Column(name = "NAME", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SEX", length = 4)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "IDENTITY", length = 10)
	public String getIdentity() {
		return this.identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	@Column(name = "CHARGE_TYPE", length = 8)
	public String getChargeType() {
		return this.chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	@Column(name = "ORDERED_BY", length = 20)
	public String getOrderedBy() {
		return this.orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	@Column(name = "PERFORMED_BY", length = 20)
	public String getPerformedBy() {
		return this.performedBy;
	}

	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}

	@Column(name = "OPERATOR", length = 10)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REC_DATE", length = 7)
	public Date getRecDate() {
		return this.recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	@Column(name = "ORDERED_DOCTOR", length = 20)
	public String getOrderedDoctor() {
		return this.orderedDoctor;
	}

	public void setOrderedDoctor(String orderedDoctor) {
		this.orderedDoctor = orderedDoctor;
	}

}