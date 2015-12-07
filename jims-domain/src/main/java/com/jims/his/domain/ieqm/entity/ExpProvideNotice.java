package com.jims.his.domain.ieqm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

/**
 * ExpProvideNotice entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_PROVIDE_NOTICE", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"PROVIDE_STORAGE", "DOCUMENT_NO" }))
public class ExpProvideNotice implements java.io.Serializable {

	// Fields

	private String id;
	private String provideStorage;
	private String applicantStorage;
	private String documentNo;

	// Constructors

	/** default constructor */
	public ExpProvideNotice() {
	}

	/** full constructor */
	public ExpProvideNotice(String provideStorage, String applicantStorage,
			String documentNo) {
		this.provideStorage = provideStorage;
		this.applicantStorage = applicantStorage;
		this.documentNo = documentNo;
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

	@Column(name = "PROVIDE_STORAGE", length = 8)
	public String getProvideStorage() {
		return this.provideStorage;
	}

	public void setProvideStorage(String provideStorage) {
		this.provideStorage = provideStorage;
	}

	@Column(name = "APPLICANT_STORAGE", length = 8)
	public String getApplicantStorage() {
		return this.applicantStorage;
	}

	public void setApplicantStorage(String applicantStorage) {
		this.applicantStorage = applicantStorage;
	}

	@Column(name = "DOCUMENT_NO", length = 10)
	public String getDocumentNo() {
		return this.documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

}