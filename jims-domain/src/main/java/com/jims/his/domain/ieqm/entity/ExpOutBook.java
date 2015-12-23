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
 * ExpOutBook entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_OUT_BOOK", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORE_ID", "BILL_ID", "BOOK_ID" }))
public class ExpOutBook implements java.io.Serializable {

	// Fields

	private String id;
	private String storeId;
	private String billId;
	private Byte bookId;
	private String infoId;
	private String expCode;
	private String expAlias;
	private String factory;
	private String fcountry;
	private String expCountry;
	private String units;
	private String pack;
	private Double packQuan;
	private String specs;
	private String specsAbbr;
	private String batchId;
	private Date effect;
	private String place;
	private Double tradePric;
	private Double retailPric;
	private Double bookQuan;
	private String inBill;
	private Byte inId;

	// Constructors

	/** default constructor */
	public ExpOutBook() {
	}

	/** full constructor */
	public ExpOutBook(String storeId, String billId, Byte bookId,
			String infoId, String expCode, String expAlias, String factory,
			String fcountry, String expCountry, String units, String pack,
			Double packQuan, String specs, String specsAbbr, String batchId,
			Date effect, String place, Double tradePric, Double retailPric,
			Double bookQuan, String inBill, Byte inId) {
		this.storeId = storeId;
		this.billId = billId;
		this.bookId = bookId;
		this.infoId = infoId;
		this.expCode = expCode;
		this.expAlias = expAlias;
		this.factory = factory;
		this.fcountry = fcountry;
		this.expCountry = expCountry;
		this.units = units;
		this.pack = pack;
		this.packQuan = packQuan;
		this.specs = specs;
		this.specsAbbr = specsAbbr;
		this.batchId = batchId;
		this.effect = effect;
		this.place = place;
		this.tradePric = tradePric;
		this.retailPric = retailPric;
		this.bookQuan = bookQuan;
		this.inBill = inBill;
		this.inId = inId;
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

	@Column(name = "BILL_ID", length = 8)
	public String getBillId() {
		return this.billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	@Column(name = "BOOK_ID", precision = 2, scale = 0)
	public Byte getBookId() {
		return this.bookId;
	}

	public void setBookId(Byte bookId) {
		this.bookId = bookId;
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

	@Column(name = "EXP_ALIAS", length = 30)
	public String getExpAlias() {
		return this.expAlias;
	}

	public void setExpAlias(String expAlias) {
		this.expAlias = expAlias;
	}

	@Column(name = "FACTORY", length = 40)
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Column(name = "FCOUNTRY", length = 2)
	public String getFcountry() {
		return this.fcountry;
	}

	public void setFcountry(String fcountry) {
		this.fcountry = fcountry;
	}

	@Column(name = "EXP_COUNTRY", length = 2)
	public String getExpCountry() {
		return this.expCountry;
	}

	public void setExpCountry(String expCountry) {
		this.expCountry = expCountry;
	}

	@Column(name = "UNITS", length = 4)
	public String getUnits() {
		return this.units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Column(name = "PACK", length = 4)
	public String getPack() {
		return this.pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	@Column(name = "PACK_QUAN", precision = 8)
	public Double getPackQuan() {
		return this.packQuan;
	}

	public void setPackQuan(Double packQuan) {
		this.packQuan = packQuan;
	}

	@Column(name = "SPECS", length = 48)
	public String getSpecs() {
		return this.specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	@Column(name = "SPECS_ABBR", length = 20)
	public String getSpecsAbbr() {
		return this.specsAbbr;
	}

	public void setSpecsAbbr(String specsAbbr) {
		this.specsAbbr = specsAbbr;
	}

	@Column(name = "BATCH_ID", length = 20)
	public String getBatchId() {
		return this.batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECT", length = 7)
	public Date getEffect() {
		return this.effect;
	}

	public void setEffect(Date effect) {
		this.effect = effect;
	}

	@Column(name = "PLACE", length = 20)
	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Column(name = "TRADE_PRIC", precision = 12, scale = 3)
	public Double getTradePric() {
		return this.tradePric;
	}

	public void setTradePric(Double tradePric) {
		this.tradePric = tradePric;
	}

	@Column(name = "RETAIL_PRIC", precision = 12, scale = 3)
	public Double getRetailPric() {
		return this.retailPric;
	}

	public void setRetailPric(Double retailPric) {
		this.retailPric = retailPric;
	}

	@Column(name = "BOOK_QUAN", precision = 10)
	public Double getBookQuan() {
		return this.bookQuan;
	}

	public void setBookQuan(Double bookQuan) {
		this.bookQuan = bookQuan;
	}

	@Column(name = "IN_BILL", length = 8)
	public String getInBill() {
		return this.inBill;
	}

	public void setInBill(String inBill) {
		this.inBill = inBill;
	}

	@Column(name = "IN_ID", precision = 2, scale = 0)
	public Byte getInId() {
		return this.inId;
	}

	public void setInId(Byte inId) {
		this.inId = inId;
	}

}