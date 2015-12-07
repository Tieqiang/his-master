package com.jims.his.domain.ieqm.vo;

import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpImportDetail;
import com.jims.his.domain.ieqm.entity.ExpImportMaster;

import java.io.Serializable;

/**
 *消耗品入库保存
 *明细记录和主记录
 *Created by heren on 2015/10/21.
 */
public class ExpImportVo implements Serializable{

    private BeanChangeVo<ExpImportMaster> expImportMasterBeanChangeVo ;//入库单据主记录
    private BeanChangeVo<ExpImportDetail> expImportDetailBeanChangeVo ;//入库单据明细记录
    private BeanChangeVo<ExpExportDetialVo> expExportDetialVoBeanChangeVo ;//出库单据明细


    public ExpImportVo(BeanChangeVo<ExpImportMaster> expImportMasterBeanChangeVo, BeanChangeVo<ExpImportDetail> expImportDetailBeanChangeVo, BeanChangeVo<ExpExportDetialVo> expExportDetialVoBeanChangeVo) {
        this.expImportMasterBeanChangeVo = expImportMasterBeanChangeVo;
        this.expImportDetailBeanChangeVo = expImportDetailBeanChangeVo;

        this.expExportDetialVoBeanChangeVo = expExportDetialVoBeanChangeVo;
    }

    public ExpImportVo() {

    }

    public BeanChangeVo<ExpImportMaster> getExpImportMasterBeanChangeVo() {
        return expImportMasterBeanChangeVo;
    }

    public void setExpImportMasterBeanChangeVo(BeanChangeVo<ExpImportMaster> expImportMasterBeanChangeVo) {
        this.expImportMasterBeanChangeVo = expImportMasterBeanChangeVo;
    }

    public BeanChangeVo<ExpImportDetail> getExpImportDetailBeanChangeVo() {
        return expImportDetailBeanChangeVo;
    }

    public void setExpImportDetailBeanChangeVo(BeanChangeVo<ExpImportDetail> expImportDetailBeanChangeVo) {
        this.expImportDetailBeanChangeVo = expImportDetailBeanChangeVo;
    }

    public BeanChangeVo<ExpExportDetialVo> getExpExportDetialVoBeanChangeVo() {
        return expExportDetialVoBeanChangeVo;
    }

    public void setExpExportDetialVoBeanChangeVo(BeanChangeVo<ExpExportDetialVo> expExportDetialVoBeanChangeVo) {
        this.expExportDetialVoBeanChangeVo = expExportDetialVoBeanChangeVo;
    }
}
