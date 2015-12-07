package com.jims.his.domain.ieqm.vo;

import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpDisburseRec;
import com.jims.his.domain.ieqm.entity.ExpDisburseRecDetail;
import java.io.Serializable;

/**
 *消耗品付款主记录
 *消耗品付款明细记录记录
 *Created by wangbinbin on 2015/10/30.
 */
public class ExpDisburseVo implements Serializable{

    private BeanChangeVo<ExpDisburseRec> expDisburseRecBeanChangeVo ;//入库单据主记录
    private BeanChangeVo<ExpDisburseRecDetail> expDisburseRecDetailBeanChangeVo ;//入库单据明细记录
    public ExpDisburseVo() {

    }

    public ExpDisburseVo(BeanChangeVo<ExpDisburseRec> expDisburseRecBeanChangeVo, BeanChangeVo<ExpDisburseRecDetail> expDisburseRecDetailBeanChangeVo) {
        this.expDisburseRecBeanChangeVo = expDisburseRecBeanChangeVo;
        this.expDisburseRecDetailBeanChangeVo = expDisburseRecDetailBeanChangeVo;
    }

    public BeanChangeVo<ExpDisburseRec> getExpDisburseRecBeanChangeVo() {
        return expDisburseRecBeanChangeVo;
    }

    public void setExpDisburseRecBeanChangeVo(BeanChangeVo<ExpDisburseRec> expDisburseRecBeanChangeVo) {
        this.expDisburseRecBeanChangeVo = expDisburseRecBeanChangeVo;
    }

    public BeanChangeVo<ExpDisburseRecDetail> getExpDisburseRecDetailBeanChangeVo() {
        return expDisburseRecDetailBeanChangeVo;
    }

    public void setExpDisburseRecDetailBeanChangeVo(BeanChangeVo<ExpDisburseRecDetail> expDisburseRecDetailBeanChangeVo) {
        this.expDisburseRecDetailBeanChangeVo = expDisburseRecDetailBeanChangeVo;
    }




}
