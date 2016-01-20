package com.jims.his.domain.htca.vo;

import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.AcctDeptProfit;
import com.jims.his.domain.htca.entity.AcctProfitChangeRecord;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heren on 2016/1/20.
 */
public class DeptProfitVo implements Serializable {

    private List<AcctDeptProfit> acctDeptProfits  ;

    private BeanChangeVo<AcctProfitChangeRecord> acctProfitChangeRecordBeanChangeVo ;

    public DeptProfitVo() {
    }

    public DeptProfitVo(List<AcctDeptProfit> acctDeptProfits, BeanChangeVo<AcctProfitChangeRecord> acctProfitChangeRecordBeanChangeVo) {
        this.acctDeptProfits = acctDeptProfits;
        this.acctProfitChangeRecordBeanChangeVo = acctProfitChangeRecordBeanChangeVo;
    }


    public List<AcctDeptProfit> getAcctDeptProfits() {

        return acctDeptProfits;
    }

    public void setAcctDeptProfits(List<AcctDeptProfit> acctDeptProfits) {
        this.acctDeptProfits = acctDeptProfits;
    }

    public BeanChangeVo<AcctProfitChangeRecord> getAcctProfitChangeRecordBeanChangeVo() {
        return acctProfitChangeRecordBeanChangeVo;
    }

    public void setAcctProfitChangeRecordBeanChangeVo(BeanChangeVo<AcctProfitChangeRecord> acctProfitChangeRecordBeanChangeVo) {
        this.acctProfitChangeRecordBeanChangeVo = acctProfitChangeRecordBeanChangeVo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeptProfitVo)) return false;

        DeptProfitVo that = (DeptProfitVo) o;

        if (acctDeptProfits != null ? !acctDeptProfits.equals(that.acctDeptProfits) : that.acctDeptProfits != null)
            return false;
        if (acctProfitChangeRecordBeanChangeVo != null ? !acctProfitChangeRecordBeanChangeVo.equals(that.acctProfitChangeRecordBeanChangeVo) : that.acctProfitChangeRecordBeanChangeVo != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = acctDeptProfits != null ? acctDeptProfits.hashCode() : 0;
        result = 31 * result + (acctProfitChangeRecordBeanChangeVo != null ? acctProfitChangeRecordBeanChangeVo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeptProfitVo{");
        sb.append("acctDeptProfits=").append(acctDeptProfits);
        sb.append(", acctProfitChangeRecordBeanChangeVo=").append(acctProfitChangeRecordBeanChangeVo);
        sb.append('}');
        return sb.toString();
    }
}
