package com.jims.his.domain.htca.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/1/18.
 */
@Entity
@Table(name = "ACCT_PROFIT_CHANGE_DICT", schema = "HTCA")
public class AcctProfitChangeDict implements java.io.Serializable {
    private String id;
    private String changeItemName;
    private String inputCode;
    private String hospitalId;

    public AcctProfitChangeDict() {
    }

    public AcctProfitChangeDict(String id, String changeItemName, String inputCode, String hospitalId) {
        this.id = id;
        this.changeItemName = changeItemName;
        this.inputCode = inputCode;
        this.hospitalId = hospitalId;
    }

    @GenericGenerator(name = "generator", strategy = "uuid.hex")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "CHANGE_ITEM_NAME", length = 100)
    public String getChangeItemName() {
        return changeItemName;
    }

    public void setChangeItemName(String changeItemName) {
        this.changeItemName = changeItemName;
    }

    @Column(name = "INPUT_CODE", length = 20)
    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    @Column(name = "HOSPITAL_ID", length = 64)
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}
