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
 * ExpOperateConsum entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_OPERATE_CONSUM", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORE_ID", "SCHEDULE_DATE", "OPERATION_NO", "ROW_NO" }))
public class ExpOperateConsum implements java.io.Serializable {

	// Fields

	private String id;
	private String storeId;
	private Date scheduleDate;
	private Byte operationNo;
	private Short rowNo;
	private String infoId;
	private String expCode;
	private String expName;
	private String units;
	private String specs;
	private String onceSign;
	private Short prepareQuan;
	private Short usedQuan;
	private String patientId;
	private Byte visitId;
	private Byte scheduleId;
	private String deptCode;
	private Double price;
	private String outSign;
	private String sterilize;
	private String cancelSign;

	// Constructors

	/** default constructor */
	public ExpOperateConsum() {
	}

	/** full constructor */
	public ExpOperateConsum(String storeId, Date scheduleDate,
			Byte operationNo, Short rowNo, String infoId, String expCode,
			String expName, String units, String specs, String onceSign,
			Short prepareQuan, Short usedQuan, String patientId, Byte visitId,
			Byte scheduleId, String deptCode, Double price, String outSign,
			String sterilize, String cancelSign) {
		this.storeId = storeId;
		this.scheduleDate = scheduleDate;
		this.operationNo = operationNo;
		this.rowNo = rowNo;
		this.infoId = infoId;
		this.expCode = expCode;
		this.expName = expName;
		this.units = units;
		this.specs = specs;
		this.onceSign = onceSign;
		this.prepareQuan = prepareQuan;
		this.usedQuan = usedQuan;
		this.patientId = patientId;
		this.visitId = visitId;
		this.scheduleId = scheduleId;
		this.deptCode = deptCode;
		this.price = price;
		this.outSign = outSign;
		this.sterilize = sterilize;
		this.cancelSign = cancelSign;
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

	@Column(name = "STORE_ID", length = 10)
	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SCHEDULE_DATE", length = 7)
	public Date getScheduleDate() {
		return this.scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	@Column(name = "OPERATION_NO", precision = 2, scale = 0)
	public Byte getOperationNo() {
		return this.operationNo;
	}

	public void setOperationNo(Byte operationNo) {
		this.operationNo = operationNo;
	}

	@Column(name = "ROW_NO", precision = 3, scale = 0)
	public Short getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(Short rowNo) {
		this.rowNo = rowNo;
	}

	@Column(name = "INFO_ID", length = 12)
	public String getInfoId() {
		return this.infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	@Column(name = "EXP_CODE", length = 20)
	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	@Column(name = "EXP_NAME", length = 100)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	@Column(name = "UNITS", length = 4)
	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Column(name = "SPECS", length = 48)
	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	@Column(name = "ONCE_SIGN", length = 1)
	public String getOnceSign() {
		return this.onceSign;
	}

	public void setOnceSign(String onceSign) {
		this.onceSign = onceSign;
	}

	@Column(name = "PREPARE_QUAN", precision = 3, scale = 0)
	public Short getPrepareQuan() {
		return this.prepareQuan;
	}

	public void setPrepareQuan(Short prepareQuan) {
		this.prepareQuan = prepareQuan;
	}

	@Column(name = "USED_QUAN", precision = 3, scale = 0)
	public Short getUsedQuan() {
		return this.usedQuan;
	}

	public void setUsedQuan(Short usedQuan) {
		this.usedQuan = usedQuan;
	}

	@Column(name = "PATIENT_ID", length = 10)
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

	@Column(name = "SCHEDULE_ID", precision = 2, scale = 0)
	public Byte getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(Byte scheduleId) {
		this.scheduleId = scheduleId;
	}

	@Column(name = "DEPT_CODE", length = 8)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "PRICE", precision = 12, scale = 3)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "OUT_SIGN", length = 1)
	public String getOutSign() {
		return this.outSign;
	}

	public void setOutSign(String outSign) {
		this.outSign = outSign;
	}

	@Column(name = "STERILIZE", length = 1)
	public String getSterilize() {
		return this.sterilize;
	}

	public void setSterilize(String sterilize) {
		this.sterilize = sterilize;
	}

	@Column(name = "CANCEL_SIGN", length = 1)
	public String getCancelSign() {
		return this.cancelSign;
	}

	public void setCancelSign(String cancelSign) {
		this.cancelSign = cancelSign;
	}

}