package com.jims.his.domain.ieqm.vo;

import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpExportDetail;
import com.jims.his.domain.ieqm.entity.ExpExportMaster;

import java.io.Serializable;

/**
 *对消入出库保存
 *明细记录和主记录
 *Created by wangjing on 2015/11/25.
 */
public class ExpExportImportVo implements Serializable{

    private ExpImportVo importVo ;//入库单据主记录
    private ExpExportManageVo exportVo ;//出库单据明细记录

    public ExpExportImportVo() {

    }

    public ExpExportImportVo(ExpImportVo importVo, ExpExportManageVo exportVo) {
        this.importVo = importVo;
        this.exportVo = exportVo;
    }

    public ExpImportVo getImportVo() {
        return importVo;
    }

    public void setImportVo(ExpImportVo importVo) {
        this.importVo = importVo;
    }

    public ExpExportManageVo getExportVo() {
        return exportVo;
    }

    public void setExportVo(ExpExportManageVo exportVo) {
        this.exportVo = exportVo;
    }
}
