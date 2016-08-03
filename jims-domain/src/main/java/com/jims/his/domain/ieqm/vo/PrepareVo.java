package com.jims.his.domain.ieqm.vo;

/**
 * Created by admin on 2016/8/3.
 */
public class PrepareVo {


    /**
     *   expPrepareVO.expCodes=expCodes;
     expPrepareVO.amounts=amounts;
     expPrepareVO.prices=prices;
     expPrepareVO.packageSpecs=packageSpecs;
     expPrepareVO.operators=operators;
     expPrepareVO.phones=phones;
     expPrepareVO.supplierId=supplierId;
     expPrepareVO.operator=parent.config.staffName;
     expPrepareVO.subStorage=subStorageValue;
     */

    private String expCodes;
    private String amounts;
    private String packageSpecs;
    private String prices;
    private String operators;
    private String phones;
    private String supplierId;
    private String operator;
    private String subStorage;

    public String getExpCodes() {
        return expCodes;
    }

    public void setExpCodes(String expCodes) {
        this.expCodes = expCodes;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }

    public String getPackageSpecs() {
        return packageSpecs;
    }

    public void setPackageSpecs(String packageSpecs) {
        this.packageSpecs = packageSpecs;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSubStorage() {
        return subStorage;
    }

    public void setSubStorage(String subStorage) {
        this.subStorage = subStorage;
    }
}
