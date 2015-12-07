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
 * ExpSupplierCatalog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_SUPPLIER_CATALOG", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = "SUPPLIER_ID"))
public class ExpSupplierCatalog implements java.io.Serializable {

	// Fields

	private String id;
	private String supplierId;
	private String supplier;
	private String supplierClass;
	private String memo;
	private String inputCode;
	private String inputCodeWb;
	private String suppbound;
	private String supplierAddres;
	private String supplierPostalcode;
	private String artificialPerson;
	private String linkphone;
	private String licenceNo;
	private Date licenceDate;
	private String permitNo;
	private Date permitDate;
	private String registerNo;
	private Date registerDate;
	private String fdaOrCeNo;
	private Date fdaOrCeDate;
	private String otherNo;
	private Date otherDate;

	// Constructors

	/** default constructor */
	public ExpSupplierCatalog() {
	}

	/** full constructor */
	public ExpSupplierCatalog(String supplierId, String supplier,
			String supplierClass, String memo, String inputCode,
			String inputCodeWb, String suppbound, String supplierAddres,
			String supplierPostalcode, String artificialPerson,
			String linkphone, String licenceNo, Date licenceDate,
			String permitNo, Date permitDate, String registerNo,
			Date registerDate, String fdaOrCeNo, Date fdaOrCeDate,
			String otherNo, Date otherDate) {
		this.supplierId = supplierId;
		this.supplier = supplier;
		this.supplierClass = supplierClass;
		this.memo = memo;
		this.inputCode = inputCode;
		this.inputCodeWb = inputCodeWb;
		this.suppbound = suppbound;
		this.supplierAddres = supplierAddres;
		this.supplierPostalcode = supplierPostalcode;
		this.artificialPerson = artificialPerson;
		this.linkphone = linkphone;
		this.licenceNo = licenceNo;
		this.licenceDate = licenceDate;
		this.permitNo = permitNo;
		this.permitDate = permitDate;
		this.registerNo = registerNo;
		this.registerDate = registerDate;
		this.fdaOrCeNo = fdaOrCeNo;
		this.fdaOrCeDate = fdaOrCeDate;
		this.otherNo = otherNo;
		this.otherDate = otherDate;
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

	@Column(name = "SUPPLIER_ID", unique = true, length = 10)
	public String getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	@Column(name = "SUPPLIER", length = 40)
	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	@Column(name = "SUPPLIER_CLASS", length = 8)
	public String getSupplierClass() {
		return this.supplierClass;
	}

	public void setSupplierClass(String supplierClass) {
		this.supplierClass = supplierClass;
	}

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	@Column(name = "SUPPBOUND", length = 1)
	public String getSuppbound() {
		return this.suppbound;
	}

	public void setSuppbound(String suppbound) {
		this.suppbound = suppbound;
	}

	@Column(name = "SUPPLIER_ADDRES", length = 100)
	public String getSupplierAddres() {
		return this.supplierAddres;
	}

	public void setSupplierAddres(String supplierAddres) {
		this.supplierAddres = supplierAddres;
	}

	@Column(name = "SUPPLIER_POSTALCODE", length = 6)
	public String getSupplierPostalcode() {
		return this.supplierPostalcode;
	}

	public void setSupplierPostalcode(String supplierPostalcode) {
		this.supplierPostalcode = supplierPostalcode;
	}

	@Column(name = "ARTIFICIAL_PERSON", length = 20)
	public String getArtificialPerson() {
		return this.artificialPerson;
	}

	public void setArtificialPerson(String artificialPerson) {
		this.artificialPerson = artificialPerson;
	}

	@Column(name = "LINKPHONE", length = 20)
	public String getLinkphone() {
		return this.linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}

	@Column(name = "LICENCE_NO", length = 20)
	public String getLicenceNo() {
		return this.licenceNo;
	}

	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LICENCE_DATE", length = 7)
	public Date getLicenceDate() {
		return this.licenceDate;
	}

	public void setLicenceDate(Date licenceDate) {
		this.licenceDate = licenceDate;
	}

	@Column(name = "PERMIT_NO", length = 20)
	public String getPermitNo() {
		return this.permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PERMIT_DATE", length = 7)
	public Date getPermitDate() {
		return this.permitDate;
	}

	public void setPermitDate(Date permitDate) {
		this.permitDate = permitDate;
	}

	@Column(name = "REGISTER_NO", length = 20)
	public String getRegisterNo() {
		return this.registerNo;
	}

	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REGISTER_DATE", length = 7)
	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	@Column(name = "FDA_OR_CE_NO", length = 20)
	public String getFdaOrCeNo() {
		return this.fdaOrCeNo;
	}

	public void setFdaOrCeNo(String fdaOrCeNo) {
		this.fdaOrCeNo = fdaOrCeNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FDA_OR_CE_DATE", length = 7)
	public Date getFdaOrCeDate() {
		return this.fdaOrCeDate;
	}

	public void setFdaOrCeDate(Date fdaOrCeDate) {
		this.fdaOrCeDate = fdaOrCeDate;
	}

	@Column(name = "OTHER_NO", length = 20)
	public String getOtherNo() {
		return this.otherNo;
	}

	public void setOtherNo(String otherNo) {
		this.otherNo = otherNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OTHER_DATE", length = 7)
	public Date getOtherDate() {
		return this.otherDate;
	}

	public void setOtherDate(Date otherDate) {
		this.otherDate = otherDate;
	}

}