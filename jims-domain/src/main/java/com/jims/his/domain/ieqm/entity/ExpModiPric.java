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
 * ExpModiPric entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_MODI_PRIC", schema = "JIMS")
public class ExpModiPric implements java.io.Serializable {

	// Fields

	private String id;
	private Date modiDate;
	private String infoId;
	private Double oriTrade;
	private Double oriRetail;
	private Double curTrade;
	private Double curRetail;
	private Double modiQuan;
	private String operator;

	// Constructors

	/** default constructor */
	public ExpModiPric() {
	}

	/** full constructor */
	public ExpModiPric(Date modiDate, String infoId, Double oriTrade,
			Double oriRetail, Double curTrade, Double curRetail,
			Double modiQuan, String operator) {
		this.modiDate = modiDate;
		this.infoId = infoId;
		this.oriTrade = oriTrade;
		this.oriRetail = oriRetail;
		this.curTrade = curTrade;
		this.curRetail = curRetail;
		this.modiQuan = modiQuan;
		this.operator = operator;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "MODI_DATE", length = 7)
	public Date getModiDate() {
		return this.modiDate;
	}

	public void setModiDate(Date modiDate) {
		this.modiDate = modiDate;
	}

	@Column(name = "INFO_ID", length = 12)
	public String getInfoId() {
		return this.infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	@Column(name = "ORI_TRADE", precision = 12, scale = 3)
	public Double getOriTrade() {
		return this.oriTrade;
	}

	public void setOriTrade(Double oriTrade) {
		this.oriTrade = oriTrade;
	}

	@Column(name = "ORI_RETAIL", precision = 12, scale = 3)
	public Double getOriRetail() {
		return this.oriRetail;
	}

	public void setOriRetail(Double oriRetail) {
		this.oriRetail = oriRetail;
	}

	@Column(name = "CUR_TRADE", precision = 12, scale = 3)
	public Double getCurTrade() {
		return this.curTrade;
	}

	public void setCurTrade(Double curTrade) {
		this.curTrade = curTrade;
	}

	@Column(name = "CUR_RETAIL", precision = 12, scale = 3)
	public Double getCurRetail() {
		return this.curRetail;
	}

	public void setCurRetail(Double curRetail) {
		this.curRetail = curRetail;
	}

	@Column(name = "MODI_QUAN", precision = 10)
	public Double getModiQuan() {
		return this.modiQuan;
	}

	public void setModiQuan(Double modiQuan) {
		this.modiQuan = modiQuan;
	}

	@Column(name = "OPERATOR", length = 8)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}