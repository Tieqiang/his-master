package com.jims.his.domain.ieqm.vo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by tangxinbo on 2015/10/13
 */
@XmlRootElement
public class ExpStockDefineVo implements java.io.Serializable {

	private String storage;
    private String expName;
	private String expCode;
	private String expSpec;
	private String units;
	private Integer amountPerPackage;
	private String packageUnits;
	private Integer upperLevel;
	private Integer lowLevel;
	private String location;

    public ExpStockDefineVo() {
    }

    public ExpStockDefineVo(String storage, String expName, String expCode, String expSpec, String units, Integer amountPerPackage, String packageUnits, Integer upperLevel, Integer lowLevel, String location) {
        this.storage = storage;
        this.expName = expName;
        this.expCode = expCode;
        this.expSpec = expSpec;
        this.units = units;
        this.amountPerPackage = amountPerPackage;
        this.packageUnits = packageUnits;
        this.upperLevel = upperLevel;
        this.lowLevel = lowLevel;
        this.location = location;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getExpCode() {
        return expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    public String getExpSpec() {
        return expSpec;
    }

    public void setExpSpec(String expSpec) {
        this.expSpec = expSpec;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Integer getAmountPerPackage() {
        return amountPerPackage;
    }

    public void setAmountPerPackage(Integer amountPerPackage) {
        this.amountPerPackage = amountPerPackage;
    }

    public String getPackageUnits() {
        return packageUnits;
    }

    public void setPackageUnits(String packageUnits) {
        this.packageUnits = packageUnits;
    }

    public Integer getUpperLevel() {
        return upperLevel;
    }

    public void setUpperLevel(Integer upperLevel) {
        this.upperLevel = upperLevel;
    }

    public Integer getLowLevel() {
        return lowLevel;
    }

    public void setLowLevel(Integer lowLevel) {
        this.lowLevel = lowLevel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}