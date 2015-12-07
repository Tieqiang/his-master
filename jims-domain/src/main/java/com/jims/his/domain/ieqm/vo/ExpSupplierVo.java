package com.jims.his.domain.ieqm.vo;

import java.io.Serializable;

/**
 * 消耗品入库的入库的时候，供应商即可能是科室
 * 也有可能是外面的供应商，
 * 此VO用来统一二者
 * Created by heren on 2015/10/19.
 */
public class ExpSupplierVo implements Serializable {

    private String supplierName ;
    private String supplierCode ;
    private String inputCode ;

    public ExpSupplierVo(String supplierName, String supplierCode, String inputCode) {
        this.supplierName = supplierName;
        this.supplierCode = supplierCode;
        this.inputCode = inputCode;
    }

    public ExpSupplierVo() {
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }
}
