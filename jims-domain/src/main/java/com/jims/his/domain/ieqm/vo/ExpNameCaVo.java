package com.jims.his.domain.ieqm.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by wangjing on 2015/9/24.
 * 消耗品目录维护的时候，页面数据展示涉及到EXP_NAME_DICT和exp_price_list两张表，定义这个VO用来展示联合信息
 */
@XmlRootElement
public class ExpNameCaVo implements Serializable {
    private String expCode;
    private String expName;
    private String inputCode;
    private String inputCodeWb;
    private String expSpec;
    private String firmId;
    private Double retailPrice;

    public ExpNameCaVo() {
    }

    public ExpNameCaVo(Double retailPrice, String expCode, String expName, String inputCode, String inputCodeWb, String expSpec, String firmId) {
        this.retailPrice = retailPrice;
        this.expCode = expCode;
        this.expName = expName;
        this.inputCode = inputCode;
        this.inputCodeWb = inputCodeWb;
        this.expSpec = expSpec;
        this.firmId = firmId;
    }

    public String getExpCode() {
        return expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    public String getInputCodeWb() {
        return inputCodeWb;
    }

    public void setInputCodeWb(String inputCodeWb) {
        this.inputCodeWb = inputCodeWb;
    }

    public String getExpSpec() {
        return expSpec;
    }

    public void setExpSpec(String expSpec) {
        this.expSpec = expSpec;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }
}
