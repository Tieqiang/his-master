package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpTenderTypeDict entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_TENDER_TYPE_DICT", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "TENDER_TYPE_CODE"))
public class ExpTenderTypeDict implements java.io.Serializable {

	// Fields

	private String id;
	private String tenderTypeCode;
	private String tenderTypeName;
	private String inputCode;
	private String inputCodeWb;
	private Byte serialNo;

	// Constructors

	/** default constructor */
	public ExpTenderTypeDict() {
	}

	/** full constructor */
	public ExpTenderTypeDict(String tenderTypeCode, String tenderTypeName,
			String inputCode, String inputCodeWb, Byte serialNo) {
		this.tenderTypeCode = tenderTypeCode;
		this.tenderTypeName = tenderTypeName;
		this.inputCode = inputCode;
		this.inputCodeWb = inputCodeWb;
		this.serialNo = serialNo;
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

	@Column(name = "TENDER_TYPE_CODE", unique = true, length = 2)
	public String getTenderTypeCode() {
		return this.tenderTypeCode;
	}

	public void setTenderTypeCode(String tenderTypeCode) {
		this.tenderTypeCode = tenderTypeCode;
	}

	@Column(name = "TENDER_TYPE_NAME", length = 10)
	public String getTenderTypeName() {
		return this.tenderTypeName;
	}

	public void setTenderTypeName(String tenderTypeName) {
		this.tenderTypeName = tenderTypeName;
	}

	@Column(name = "INPUT_CODE", length = 8)
	public String getInputCode() {
		return this.inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	@Column(name = "INPUT_CODE_WB", length = 8)
	public String getInputCodeWb() {
		return this.inputCodeWb;
	}

	public void setInputCodeWb(String inputCodeWb) {
		this.inputCodeWb = inputCodeWb;
	}

	@Column(name = "SERIAL_NO", precision = 2, scale = 0)
	public Byte getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(Byte serialNo) {
		this.serialNo = serialNo;
	}

}