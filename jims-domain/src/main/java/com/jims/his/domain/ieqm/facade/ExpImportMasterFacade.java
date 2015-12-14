package com.jims.his.domain.ieqm.facade;


import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.*;
import com.jims.his.domain.ieqm.vo.ExpImportDetailVo;
import com.jims.his.domain.ieqm.vo.ExpStorageZeroManageVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangbinbin on 2015/10/22.
 */
public class ExpImportMasterFacade extends BaseFacade {
    private EntityManager entityManager;

    @Inject
    public ExpImportMasterFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /** 入库单据主表查询
     * @param imClass 入库类型
     * @param startBill 开始入库单号
     * @param stopBill  结束入库单号
     * @param searchInput 消耗品代码
     * @param startDate    开始日期
     * @param stopDate     结束日期
     * @param storage      消耗品库房代码
     * @param supplier     供应商
     * @param classRadio   类型标志
     * @param billRadio    单号标志
     * @param hospitalId   医院Id
     * @return
     */
    public List<ExpImportMaster> searchImportMasterDict(String imClass, String startBill, String stopBill, String searchInput, Date startDate, Date stopDate, String storage, String supplier, String classRadio, Integer billRadio, String hospitalId){
        String s1=null;
        String s2=null;
        if(startDate!=null && stopDate != null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
             s1 = formatter.format(startDate.getTime());
             s2 = formatter.format(stopDate.getTime());

        }
        String hql = "select distinct dict from ExpImportMaster as dict,ExpImportDetail where 1=1 ";
        if (billRadio != null) {
            hql += " and dict.docStatus='" + billRadio + "'";
        }

        if (imClass != null && imClass.trim().length() > 0) {
            hql += " and dict.importClass='" + imClass + "'";
        }
        if (startBill != null && startBill.trim().length() > 0) {
            hql += " and dict.documentNo>='" + startBill + "'";
        }
        if (stopBill != null && stopBill.trim().length() > 0) {
            hql += " and dict.documentNo<='" + stopBill + "'";
        }
        if (classRadio != null && classRadio.trim().length() > 0) {
            hql += " and dict.accountIndicator='" + classRadio + "'";
        }
        if (s1 != null) {
            hql += " and dict.importDate>=to_date ( '" + s1 + "' , 'yyyy-MM-dd HH24:MI:SS' ) ";
        }
        if (s2 != null) {
            hql += " and dict.importDate<=to_date ( '" + s2 + "' , 'yyyy-MM-dd HH24:MI:SS' ) ";
        }
        if (supplier != null && supplier.trim().length() > 0) {
            hql += " and dict.supplier='" + supplier + "'";
        }
        if (searchInput != null && searchInput.trim().length() > 0) {
            hql += " and ExpImportDetail.expCode='" + searchInput + "' \n" +
                   "  and ExpImportDetail.documentNo=dict.documentNo";
        }

        if (hospitalId != null && hospitalId.trim().length() > 0) {
            hql += " and dict.hospitalId='" + hospitalId + "'";
        }
        if (storage != null && storage.trim().length() > 0) {
            hql += " and dict.storage='" + storage + "'";
        }
        Query query = entityManager.createQuery(hql);
        List resultList = query.getResultList();
        return resultList;
    }

    /**
     * 付款情况查询
     * @param documentNo    入库单号
     * @param searchInput   消耗品名称
     * @param startDate   开始日期
     * @param stopDate   结束日期
     * @param storage    库房代码
     * @param supplier   供应商
     * @param hospitalId  医院Id
     * @param payRadio    支付判断
     * @return
     */
    public List<ExpImportDetailVo> searchImportPayMasterDict(String documentNo, String searchInput, Date startDate, Date stopDate, String storage, String supplier, String hospitalId, String payRadio) {
        String s1=null;
        String s2=null;
        if(startDate!=null && stopDate != null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            s1 = formatter.format(startDate.getTime());
            s2 = formatter.format(stopDate.getTime());

        }
        String sql = "SELECT distinct M.SUPPLIER,\n" +
                "         D.DOCUMENT_NO, \n" +
                "         a.exp_name, \n" +
                "         D.ITEM_NO,   \n" +
                "         D.EXP_CODE,   \n" +
                "         D.PACKAGE_SPEC,   \n" +
                "         D.PACKAGE_UNITS,   \n" +
                "         D.FIRM_ID,   \n" +
                "         D.BATCH_NO,   \n" +
                "         D.EXPIRE_DATE,   \n" +
                "         D.PURCHASE_PRICE,   \n" +
                "         D.DISCOUNT,   \n" +
                "         D.TRADE_PRICE,   \n" +
                "         D.RETAIL_PRICE,   \n" +
                "         D.QUANTITY,   \n" +
                "         D.INVOICE_NO,   \n" +
                "         D.INVOICE_DATE,   \n" +
                "         D.DISBURSE_REC_NO,\n" +
                "         Nvl(D.DISBURSE_COUNT,0) disburse_Count\n" +
                "    FROM EXP_IMPORT_MASTER M,    EXP_IMPORT_DETAIL D, EXP_DICT A " +

                 "   WHERE M.STORAGE = '"+storage+"' " +
                "          AND M.HOSPITAL_ID = '"+hospitalId+"' " +
                "          AND M.DOCUMENT_NO(+) = D.DOCUMENT_NO   \n" +
                "          AND D.package_spec = A.exp_spec   \n" +
                "          AND D.exp_code = A.exp_code   \n" +
                "          AND D.package_units = A.units   " ;

        if (payRadio != null && payRadio.trim().length() > 0) {
            if(payRadio.trim().equals("0")){
                sql += " and Nvl(D.DISBURSE_COUNT,0)='" + 0 + "'";
            }
            if(payRadio.trim().equals("1")){
                sql += " and D.disburse_Count>0 and D.disburse_Count< D.QUANTITY ";
            }
            if(payRadio.trim().equals("2")){
                sql += " and D.disburse_Count=D.quantity";
            }

        }
        if (documentNo != null && documentNo.trim().length() > 0) {
            sql += " and M.document_No='" + documentNo + "'";
        }
        if (s1 != null) {
            sql += " and M.import_Date>=to_date ( '" + s1 + "' , 'yyyy-MM-dd HH24:MI:SS' ) ";
        }
        if (s2 != null) {
            sql += " and M.import_Date<=to_date ( '" + s2 + "' , 'yyyy-MM-dd HH24:MI:SS' ) ";
        }
        if (supplier != null && supplier.trim().length() > 0) {
            sql += " and M.supplier='" + supplier + "'";
        }
        if (searchInput != null && searchInput.trim().length() > 0) {
            sql += " and a.exp_Code='" + searchInput + "' \n" ;
        }

        List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
        return nativeQuery;

    }
}
