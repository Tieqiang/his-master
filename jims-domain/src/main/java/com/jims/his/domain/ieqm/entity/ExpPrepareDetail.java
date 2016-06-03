package com.jims.his.domain.ieqm.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/6/2.
 */
@Entity
@Table(name = "EXP_PREPARE_DETAIL", schema = "JIMS")
public class ExpPrepareDetail implements java.io.Serializable{
    private String id ;
    private String expBarCode ;     //条形码
    private String masterId ;       //备货记录ID
    private String useFlag ;        //是否使用
    private String useDate ;        //使用日期
    private String usePatientId ;   //使用病人
    private String useDept ;        //使用科室
    private String impDocnoFirst   ;//入库单号
    private String expDocnoFirst   ;//出库单号
    private String impDocnoSecond  ;//入库单号
    private String expDocnoSecond  ;//出库单号
    private String operator ;       //扫码人员
    private String printFlag ;     //是否打印

    public ExpPrepareDetail(){
    }

    public ExpPrepareDetail(String expBarCode, String masterId, String useFlag, String useDate, String usePatientId, String useDept, String impDocnoFirst, String expDocnoFirst, String impDocnoSecond, String expDocnoSecond, String operator, String printFlag) {
        this.expBarCode = expBarCode;
        this.masterId = masterId;
        this.useFlag = useFlag;
        this.useDate = useDate;
        this.usePatientId = usePatientId;
        this.useDept = useDept;
        this.impDocnoFirst = impDocnoFirst;
        this.expDocnoFirst = expDocnoFirst;
        this.impDocnoSecond = impDocnoSecond;
        this.expDocnoSecond = expDocnoSecond;
        this.operator = operator;
        this.printFlag = printFlag;
    }

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

    @Column(name = "EXP_BAR_CODE", length = 16)
    public String getExpBarCode() {
        return expBarCode;
    }

    public void setExpBarCode(String expBarCode) {
        this.expBarCode = expBarCode;
    }

    @Column(name = "MASTER_ID", length = 64 )
    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    @Column(name = "USE_FLAG", length = 2 )
    public String getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;
    }

    @Column(name = "USE_DATE", length = 32)
    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    @Column(name = "USE_PATIENT_ID", length = 64)
    public String getUsePatientId() {
        return usePatientId;
    }

    public void setUsePatientId(String usePatientId) {
        this.usePatientId = usePatientId;
    }

    @Column(name = "USER_DEPT", length = 64 )
    public String getUseDept() {
        return useDept;
    }

    public void setUseDept(String useDept) {
        this.useDept = useDept;
    }

    @Column(name = "IMP_DOCNO_FIRST", length = 64)
    public String getImpDocnoFirst() {
        return impDocnoFirst;
    }

    public void setImpDocnoFirst(String impDocnoFirst) {
        this.impDocnoFirst = impDocnoFirst;
    }

    @Column(name = "EXP_DOCNO_FIRST", length = 64)
    public String getExpDocnoFirst() {
        return expDocnoFirst;
    }

    public void setExpDocnoFirst(String expDocnoFirst) {
        this.expDocnoFirst = expDocnoFirst;
    }

    @Column(name = "IMP_DOCNO_SECOND", length = 64)
    public String getImpDocnoSecond() {
        return impDocnoSecond;
    }

    public void setImpDocnoSecond(String impDocnoSecond) {
        this.impDocnoSecond = impDocnoSecond;
    }

    @Column(name = "EXP_DOCNO_SECOND", length = 64)
    public String getExpDocnoSecond() {
        return expDocnoSecond;
    }

    public void setExpDocnoSecond(String expDocnoSecond) {
        this.expDocnoSecond = expDocnoSecond;
    }

    @Column(name = "OPERATOR", length = 16)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "PRINT_FLAG", length = 2)
    public String getPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(String printFlag) {
        this.printFlag = printFlag;
    }
}

