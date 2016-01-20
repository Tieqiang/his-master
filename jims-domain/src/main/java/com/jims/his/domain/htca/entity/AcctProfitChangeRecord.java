package com.jims.his.domain.htca.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/18.
 */
@Entity
@Table(name = "ACCT_PROFIT_CHANGE_RECORD", schema = "HTCA")

public class AcctProfitChangeRecord implements java.io.Serializable {
    private String id;
    private String changeItemId;
    private String yearMonth;
    private Double changeAmount;
    private String operator;
    private Date operatorDate;
    private String incomeOrCost;
    private String acctDeptId;
    private String changeReason;

    public AcctProfitChangeRecord() {
    }

    public AcctProfitChangeRecord(String id, String changeItemId, String yearMonth, Double changeAmount, String operator, Date operatorDate, String incomeOrCost, String acctDeptId, String changeReason) {
        this.id = id;
        this.changeItemId = changeItemId;
        this.yearMonth = yearMonth;
        this.changeAmount = changeAmount;
        this.operator = operator;
        this.operatorDate = operatorDate;
        this.incomeOrCost = incomeOrCost;
        this.acctDeptId = acctDeptId;
        this.changeReason = changeReason;
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

    @Column(name = "CHANGE_ITEM_ID", length = 64)
    public String getChangeItemId() {
        return changeItemId;
    }

    public void setChangeItemId(String changeItemId) {
        this.changeItemId = changeItemId;
    }

    @Column(name = "YEAR_MONTH", length = 64)
    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    @Column(name = "CHANGE_AMOUNT", length = 64)
    public Double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
    }

    @Column(name = "OPERATOR", length = 64)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "OPERATOR_DATE", length = 100)
    public Date getOperatorDate() {
        return operatorDate;
    }

    public void setOperatorDate(Date operatorDate) {
        this.operatorDate = operatorDate;
    }

    @Column(name = "INCOME_OR_COST", length = 10)
    public String getIncomeOrCost() {
        return incomeOrCost;
    }

    public void setIncomeOrCost(String incomeOrCost) {
        this.incomeOrCost = incomeOrCost;
    }

    @Column(name = "ACCT_DEPT_ID", length = 64)
    public String getAcctDeptId() {
        return acctDeptId;
    }

    public void setAcctDeptId(String acctDeptId) {
        this.acctDeptId = acctDeptId;
    }

    @Column(name = "CHANGE_REASON", length = 500)
    public String getChangeReason() {
        return changeReason;
    }


    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
}
