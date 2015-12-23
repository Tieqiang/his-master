package com.jims.his.domain.ieqm.entity;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * ExpStockBalance entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXP_STOCK_BALANCE", schema = "JIMS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"STORAGE", "YEAR_MONTH", "EXP_CODE", "EXP_SPEC", "FIRM_ID",
		"PACKAGE_SPEC" }))
public class ExpStockBalance implements java.io.Serializable {

	// Fields

	private ExpStockBalanceId id;
	private String packageUnits;
	private Double initialQuantity;
	private Double initialMoney;
	private Double importQuantity;
	private Double importMoney;
	private Double exportQuantity;
	private Double exportMoney;
	private Double inventory;
	private Double inventoryMoney;
	private Double profit;
	private String expName;
	private Double realInitialMoney;
	private Double realImportMoney;
	private Double realExportMoney;
	private Double realInventoryMoney;
	private Double realProfit;
	private Date startDate;
	private Date stopDate;

	// Constructors

	/** default constructor */
	public ExpStockBalance() {
	}

	/** minimal constructor */
	public ExpStockBalance(ExpStockBalanceId id) {
		this.id = id;
	}

	/** full constructor */
	public ExpStockBalance(ExpStockBalanceId id, String packageUnits,
			Double initialQuantity, Double initialMoney, Double importQuantity,
			Double importMoney, Double exportQuantity, Double exportMoney,
			Double inventory, Double inventoryMoney, Double profit,
			String expName, Double realInitialMoney, Double realImportMoney,
			Double realExportMoney, Double realInventoryMoney,
			Double realProfit, Date startDate, Date stopDate) {
		this.id = id;
		this.packageUnits = packageUnits;
		this.initialQuantity = initialQuantity;
		this.initialMoney = initialMoney;
		this.importQuantity = importQuantity;
		this.importMoney = importMoney;
		this.exportQuantity = exportQuantity;
		this.exportMoney = exportMoney;
		this.inventory = inventory;
		this.inventoryMoney = inventoryMoney;
		this.profit = profit;
		this.expName = expName;
		this.realInitialMoney = realInitialMoney;
		this.realImportMoney = realImportMoney;
		this.realExportMoney = realExportMoney;
		this.realInventoryMoney = realInventoryMoney;
		this.realProfit = realProfit;
		this.startDate = startDate;
		this.stopDate = stopDate;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "id", column = @Column(name = "ID", nullable = false, length = 64)),
			@AttributeOverride(name = "yearMonth", column = @Column(name = "YEAR_MONTH", nullable = false, length = 7)),
			@AttributeOverride(name = "storage", column = @Column(name = "STORAGE", nullable = false, length = 8)),
			@AttributeOverride(name = "expCode", column = @Column(name = "EXP_CODE", nullable = false, length = 20)),
			@AttributeOverride(name = "expSpec", column = @Column(name = "EXP_SPEC", nullable = false, length = 20)),
			@AttributeOverride(name = "firmId", column = @Column(name = "FIRM_ID", nullable = false, length = 10)),
			@AttributeOverride(name = "packageSpec", column = @Column(name = "PACKAGE_SPEC", nullable = false, length = 20)) })
	public ExpStockBalanceId getId() {
		return this.id;
	}

	public void setId(ExpStockBalanceId id) {
		this.id = id;
	}

	@Column(name = "PACKAGE_UNITS", length = 8)
	public String getPackageUnits() {
		return this.packageUnits;
	}

	public void setPackageUnits(String packageUnits) {
		this.packageUnits = packageUnits;
	}

	@Column(name = "INITIAL_QUANTITY", precision = 12)
	public Double getInitialQuantity() {
		return this.initialQuantity;
	}

	public void setInitialQuantity(Double initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	@Column(name = "INITIAL_MONEY", precision = 14, scale = 4)
	public Double getInitialMoney() {
		return this.initialMoney;
	}

	public void setInitialMoney(Double initialMoney) {
		this.initialMoney = initialMoney;
	}

	@Column(name = "IMPORT_QUANTITY", precision = 12)
	public Double getImportQuantity() {
		return this.importQuantity;
	}

	public void setImportQuantity(Double importQuantity) {
		this.importQuantity = importQuantity;
	}

	@Column(name = "IMPORT_MONEY", precision = 14, scale = 4)
	public Double getImportMoney() {
		return this.importMoney;
	}

	public void setImportMoney(Double importMoney) {
		this.importMoney = importMoney;
	}

	@Column(name = "EXPORT_QUANTITY", precision = 12)
	public Double getExportQuantity() {
		return this.exportQuantity;
	}

	public void setExportQuantity(Double exportQuantity) {
		this.exportQuantity = exportQuantity;
	}

	@Column(name = "EXPORT_MONEY", precision = 14, scale = 4)
	public Double getExportMoney() {
		return this.exportMoney;
	}

	public void setExportMoney(Double exportMoney) {
		this.exportMoney = exportMoney;
	}

	@Column(name = "INVENTORY", precision = 12)
	public Double getInventory() {
		return this.inventory;
	}

	public void setInventory(Double inventory) {
		this.inventory = inventory;
	}

	@Column(name = "INVENTORY_MONEY", precision = 14, scale = 4)
	public Double getInventoryMoney() {
		return this.inventoryMoney;
	}

	public void setInventoryMoney(Double inventoryMoney) {
		this.inventoryMoney = inventoryMoney;
	}

	@Column(name = "PROFIT", precision = 14, scale = 4)
	public Double getProfit() {
		return this.profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	@Column(name = "EXP_NAME", length = 100)
	public String getExpName() {
		return this.expName;
	}

	public void setExpName(String expName) {
		this.expName = expName;
	}

	@Column(name = "REAL_INITIAL_MONEY", precision = 14, scale = 4)
	public Double getRealInitialMoney() {
		return this.realInitialMoney;
	}

	public void setRealInitialMoney(Double realInitialMoney) {
		this.realInitialMoney = realInitialMoney;
	}

	@Column(name = "REAL_IMPORT_MONEY", precision = 14, scale = 4)
	public Double getRealImportMoney() {
		return this.realImportMoney;
	}

	public void setRealImportMoney(Double realImportMoney) {
		this.realImportMoney = realImportMoney;
	}

	@Column(name = "REAL_EXPORT_MONEY", precision = 14, scale = 4)
	public Double getRealExportMoney() {
		return this.realExportMoney;
	}

	public void setRealExportMoney(Double realExportMoney) {
		this.realExportMoney = realExportMoney;
	}

	@Column(name = "REAL_INVENTORY_MONEY", precision = 14, scale = 4)
	public Double getRealInventoryMoney() {
		return this.realInventoryMoney;
	}

	public void setRealInventoryMoney(Double realInventoryMoney) {
		this.realInventoryMoney = realInventoryMoney;
	}

	@Column(name = "REAL_PROFIT", precision = 14, scale = 4)
	public Double getRealProfit() {
		return this.realProfit;
	}

	public void setRealProfit(Double realProfit) {
		this.realProfit = realProfit;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STOP_DATE", length = 7)
	public Date getStopDate() {
		return this.stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

}