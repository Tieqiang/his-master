package com.jims.his.domain.ieqm.vo;

import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpPriceModify;
import com.jims.his.domain.ieqm.entity.ExpPriceModifyProfit;

import java.io.Serializable;

/**
 * 调价记录确认功能里面，保存操作涉及到对调价记录和盈亏记录两张表的数据库操作
 * Created by wangjing on 2015/10/22.
 */
public class ExpPriceModifyProfitVo implements Serializable {
    private BeanChangeVo<ExpPriceModify> expPriceModifyChange; //调价记录
    private BeanChangeVo<ExpPriceModifyProfit> expPriceModifyProfitChange;//调价盈亏记录

    public ExpPriceModifyProfitVo() {
    }

    public ExpPriceModifyProfitVo(BeanChangeVo<ExpPriceModify> expPriceModifyChange, BeanChangeVo<ExpPriceModifyProfit> expPriceModifyProfitChange) {
        this.expPriceModifyChange = expPriceModifyChange;
        this.expPriceModifyProfitChange = expPriceModifyProfitChange;
    }

    public BeanChangeVo<ExpPriceModify> getExpPriceModifyChange() {
        return expPriceModifyChange;
    }

    public void setExpPriceModifyChange(BeanChangeVo<ExpPriceModify> expPriceModifyChange) {
        this.expPriceModifyChange = expPriceModifyChange;
    }

    public BeanChangeVo<ExpPriceModifyProfit> getExpPriceModifyProfitChange() {
        return expPriceModifyProfitChange;
    }

    public void setExpPriceModifyProfitChange(BeanChangeVo<ExpPriceModifyProfit> expPriceModifyProfitChange) {
        this.expPriceModifyProfitChange = expPriceModifyProfitChange;
    }
}
