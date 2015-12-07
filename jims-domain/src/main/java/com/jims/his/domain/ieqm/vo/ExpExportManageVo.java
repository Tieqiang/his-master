package com.jims.his.domain.ieqm.vo;

import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpExportDetail;
import com.jims.his.domain.ieqm.entity.ExpExportMaster;
import com.jims.his.domain.ieqm.entity.ExpImportDetail;
import com.jims.his.domain.ieqm.entity.ExpImportMaster;

import java.io.Serializable;

/**
 *消耗品出库保存
 *明细记录和主记录
 *Created by heren on 2015/10/21.
 */
public class ExpExportManageVo implements Serializable{

    private BeanChangeVo<ExpExportMaster> expExportMasterBeanChangeVo ;//出库单据主记录
    private BeanChangeVo<ExpExportDetail> expExportDetailBeanChangeVo ;//出库单据明细记录


    public ExpExportManageVo(BeanChangeVo<ExpExportMaster> expExportMasterBeanChangeVo, BeanChangeVo<ExpExportDetail> expExportDetailBeanChangeVo) {
        this.expExportMasterBeanChangeVo = expExportMasterBeanChangeVo;
        this.expExportDetailBeanChangeVo = expExportDetailBeanChangeVo;
    }

    public ExpExportManageVo() {

    }

    public BeanChangeVo<ExpExportMaster> getExpExportMasterBeanChangeVo() {
        return expExportMasterBeanChangeVo;
    }

    public void setExpExportMasterBeanChangeVo(BeanChangeVo<ExpExportMaster> expExportMasterBeanChangeVo) {
        this.expExportMasterBeanChangeVo = expExportMasterBeanChangeVo;
    }

    public BeanChangeVo<ExpExportDetail> getExpExportDetailBeanChangeVo() {
        return expExportDetailBeanChangeVo;
    }

    public void setExpExportDetailBeanChangeVo(BeanChangeVo<ExpExportDetail> expExportDetailBeanChangeVo) {
        this.expExportDetailBeanChangeVo = expExportDetailBeanChangeVo;
    }
}
