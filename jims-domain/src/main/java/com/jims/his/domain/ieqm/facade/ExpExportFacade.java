package com.jims.his.domain.ieqm.facade;

import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpExportMaster;
import com.jims.his.domain.ieqm.vo.ExpExportDetialVo;
import com.jims.his.domain.ieqm.vo.ExpExportVo;
import com.jims.his.domain.ieqm.vo.ExpImportDetailVo;
import com.jims.his.domain.ieqm.vo.ExpProvideApplicationVo;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 出库相关操作
 * Created by heren on 2015/10/23.
 */
public class ExpExportFacade extends BaseFacade {

    /**
     * 查询出到本库存单元的消耗品
     *
     * @param exportClasses 出库类型，例如：'发放出库','批量出库’
     * @param stockName     接收库房的库存名称
     * @param storageCode   发放单位的单位代码
     * @return
     */
    public List<ExpExportDetialVo> getExpExportDetailVo(String exportClasses, String stockName, String storageCode, String hospitalId) {
        String sql = "    SELECT distinct EXP_EXPORT_DETAIL.DOCUMENT_NO,   \n" +
                "         EXP_EXPORT_DETAIL.ITEM_NO,   \n" +
                "         EXP_EXPORT_DETAIL.HOSPITAL_ID,   \n" +
                "         EXP_EXPORT_DETAIL.EXP_CODE,   \n" +
                "         EXP_EXPORT_DETAIL.EXP_SPEC,   \n" +
                "         EXP_EXPORT_DETAIL.UNITS,   \n" +
                "         EXP_EXPORT_DETAIL.BATCH_NO,   \n" +
                "         EXP_EXPORT_DETAIL.EXPIRE_DATE,   \n" +
                "         EXP_EXPORT_DETAIL.FIRM_ID,   \n" +
                "         EXP_EXPORT_DETAIL.EXP_FORM,   \n" +
                "         EXP_EXPORT_DETAIL.IMPORT_DOCUMENT_NO,   \n" +
                "         EXP_EXPORT_DETAIL.PURCHASE_PRICE,   \n" +
                "         EXP_EXPORT_DETAIL.TRADE_PRICE,   \n" +
                "         EXP_EXPORT_DETAIL.RETAIL_PRICE,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "         EXP_EXPORT_DETAIL.QUANTITY,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_UNITS,   \n" +
                "         EXP_EXPORT_DETAIL.REC_FLAG,   \n" +
                "         EXP_EXPORT_DETAIL.REC_OPERATOR,   \n" +
                "         EXP_EXPORT_DETAIL.REC_DATE,   \n" +
                "         '                    ' REGIST_NO,   \n" +
                "         '                    ' LICENCE_NO,   \n" +
                "         EXP_EXPORT_DETAIL.PRODUCEDATE,   \n" +
                "         EXP_EXPORT_DETAIL.DISINFECTDATE,   \n" +
                "         EXP_EXPORT_DETAIL.KILLFLAG,\n" +
                "         EXP_DICT.EXP_INDICATOR,\n" +
                "         EXP_DICT.EXP_NAME,\n" +
                "EXP_EXPORT_DETAIL.SUB_PACKAGE_1, \n" +
                "EXP_EXPORT_DETAIL.SUB_PACKAGE_UNITS_1, \n" +
                "EXP_EXPORT_DETAIL.SUB_PACKAGE_SPEC_1  \n" +
                "    FROM EXP_EXPORT_DETAIL,   \n" +
                "         EXP_EXPORT_MASTER, \n" +
                "         EXP_DICT  \n" +
                "   WHERE ( EXP_EXPORT_DETAIL.DOCUMENT_NO = EXP_EXPORT_MASTER.DOCUMENT_NO ) and \n" +
                "( EXP_EXPORT_DETAIL.EXP_CODE = EXP_DICT.EXP_CODE ) and  \n" +
                "         ( EXP_EXPORT_DETAIL.EXP_SPEC = EXP_DICT.EXP_SPEC ) and\n" +
                "( EXP_EXPORT_DETAIL.EXP_SGTP in ('1','3')) and \n" +
                "         ( EXP_EXPORT_MASTER.EXPORT_CLASS in (" + exportClasses + ")) and\n" +
                "         ( EXP_EXPORT_MASTER.RECEIVER = '" + stockName + "' ) AND  \n" +
                "         ( EXP_EXPORT_MASTER.ACCOUNT_INDICATOR = 1 ) AND  \n" +
                "( EXP_EXPORT_MASTER.DOC_STATUS = 0 ) AND \n" +
                "( EXP_EXPORT_MASTER.HOSPITAL_ID =EXP_EXPORT_DETAIL.HOSPITAL_ID ) AND \n" +
                "         ( EXP_EXPORT_DETAIL.REC_FLAG = 0 ) AND  \n" +
                "         ( EXP_EXPORT_MASTER.STORAGE = '" + storageCode + "' ) AND \n" +
                "         ( EXP_EXPORT_MASTER.HOSPITAL_ID = '" + hospitalId + "' ) \n" +
                "order by  EXP_EXPORT_DETAIL.DOCUMENT_NO  \n" +
                " \n";
        System.out.println(sql);

        List<ExpExportDetialVo> nativeQuery = createNativeQuery(sql, new ArrayList<Object>(), ExpExportDetialVo.class);
        return nativeQuery;

    }

    /**
     * 出库单据主表查询
     *
     * @param imClass     入库类型
     * @param startBill   开始入库单号
     * @param stopBill    结束入库单号
     * @param searchInput 消耗品代码
     * @param startDate   开始日期
     * @param stopDate    结束日期
     * @param storage     消耗品库房代码
     * @param receiver    接收方
     * @param classRadio  类型标志
     * @param billRadio   单号标志
     * @param hospitalId  医院Id
     * @return
     */
    public List<ExpExportMaster> searchExportMasterDict(String imClass, String startBill, String stopBill, String searchInput, Date startDate, Date stopDate, String storage, String receiver, String classRadio, Integer billRadio, String hospitalId) {
        String s1 = null;
        String s2 = null;
        if (startDate != null && stopDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            s1 = formatter.format(startDate.getTime());
            s2 = formatter.format(stopDate.getTime());
        }
        String hql = "select distinct dict from ExpExportMaster as dict,ExpExportDetail where 1=1 ";
        if (billRadio != null) {
            hql += " and dict.docStatus='" + billRadio + "'";
        }
        if (imClass != null && imClass.trim().length() > 0) {
            hql += " and dict.exportClass='" + imClass + "'";
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
            hql += " and dict.exportDate>=to_date ( '" + s1 + "' , 'yyyy-MM-dd HH24:MI:SS' ) ";
        }
        if (s2 != null) {
            hql += " and dict.exportDate<=to_date ( '" + s2 + "' , 'yyyy-MM-dd HH24:MI:SS' ) ";
        }
        if (receiver != null && receiver.trim().length() > 0) {
            hql += " and dict.receiver='" + receiver + "'";
        }
        if (searchInput != null && searchInput.trim().length() > 0) {
            hql += " and ExpExportDetail.expCode='" + searchInput + "' \n" +
                    "  and dict.documentNo=ExpExportDetail.documentNo";
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
     * 出库单号明细查询
     *
     * @param documentNo 出库单号
     * @param hospitalId 医院Id
     * @return
     */
    public List<ExpImportDetailVo> searchExportDetailDict(String documentNo, String hospitalId) {
        String sql = "SELECT EXP_EXPORT_DETAIL.DOCUMENT_NO,   \n" +
                "         EXP_EXPORT_DETAIL.ITEM_NO,   \n" +
                "         EXP_EXPORT_DETAIL.EXP_CODE,   \n" +
                "         EXP_EXPORT_DETAIL.EXP_SPEC,   \n" +
                "         EXP_EXPORT_DETAIL.UNITS,   \n" +
                "         EXP_EXPORT_DETAIL.BATCH_NO,   \n" +
                "         EXP_EXPORT_DETAIL.EXPIRE_DATE,   \n" +
                "         EXP_EXPORT_DETAIL.FIRM_ID,\n" +
                "\t       EXP_EXPORT_MASTER.RECEIVER,   \n" +
                "\t       EXP_EXPORT_MASTER.account_receivable,   \n" +
                "         EXP_EXPORT_DETAIL.PURCHASE_PRICE,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "         EXP_EXPORT_DETAIL.QUANTITY,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_UNITS,   \n" +
                "         EXP_EXPORT_DETAIL.SUB_PACKAGE_1,   \n" +
                "         EXP_EXPORT_DETAIL.SUB_PACKAGE_UNITS_1,   \n" +
                "         EXP_EXPORT_DETAIL.SUB_PACKAGE_2,   \n" +
                "         EXP_EXPORT_DETAIL.SUB_PACKAGE_UNITS_2,   \n" +
                "         EXP_DICT.EXP_NAME,\n" +
                "         EXP_EXPORT_DETAIL.TRADE_PRICE,   \n" +
                "         EXP_EXPORT_DETAIL.RETAIL_PRICE,\n" +
                "\t       EXP_EXPORT_DETAIL.MEMO,  \n" +
                "\t       EXP_EXPORT_DETAIL.INVENTORY,\n" +
                "\t       (EXP_EXPORT_DETAIL.QUANTITY*EXP_EXPORT_DETAIL.RETAIL_PRICE) zero_account\n" +
                "    FROM EXP_EXPORT_DETAIL, \n" +
                "\t       EXP_EXPORT_MASTER,  \n" +
                "         EXP_DICT  \n" +
                "   WHERE ( EXP_EXPORT_DETAIL.EXP_CODE = EXP_DICT.EXP_CODE ) and  \n" +
                "         ( EXP_EXPORT_DETAIL.EXP_SPEC = EXP_DICT.EXP_SPEC ) and \n" +
                "         ( EXP_EXPORT_DETAIL.document_no = exp_export_master.document_no ) and \n" +
                "           EXP_EXPORT_DETAIL.DOCUMENT_NO = '" + documentNo + "' and \n" +
                "           EXP_EXPORT_DETAIL.HOSPITAL_ID = '" + hospitalId + "'  ";


        List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
        return nativeQuery;

    }

    /**
     * 产品去向/按出库类型出库统计
     *
     * @param expClass
     * @param startDate
     * @param endDate
     * @return
     */
    public List<ExpExportVo> getExportDetailByExportClass(String storage, String hospitalId, String expClass, String startDate, String endDate) {
        String sql = "SELECT RECEIVER," +
                "          count(exp_export_detail.document_no) import_no,  \n" +
                "          count(distinct exp_export_detail.exp_code) import_code,  \n" +
                "          sum(quantity*purchase_price) import_amount  \t\t  \n" +
                "FROM exp_export_detail,exp_export_master  \t\t\n" +
                "WHERE exp_export_detail.document_no = exp_export_master.document_no \t\n";
        if (null != storage && !storage.trim().equals("")) {
            sql += " AND exp_export_master.storage = '" + storage + "' \t\n";
        }
        if (null != hospitalId && !hospitalId.trim().equals("")) {
            sql += " AND EXP_EXPORT_DETAIL.HOSPITAL_ID = '" + hospitalId + "'  ";
        }
        if (null != startDate && !startDate.trim().equals("")) {
            sql += " AND exp_export_master.export_date >= to_date('" + startDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }
        if (null != endDate && !endDate.trim().equals("")) {
            sql += " AND exp_export_master.export_date <= to_date('" + endDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }
        if (null != expClass && !expClass.trim().equals("")) {
            sql += " AND (exp_export_master.EXPORT_CLASS = '" + expClass + "' OR '" + expClass + "' = '全部')\n";
        }
        sql += "GROUP BY  RECEIVER";
        List<ExpExportVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpExportVo.class);
        return result;
    }

    /**
     * 产品去向/按产品类别出库统计
     *
     * @param storage
     * @param hospitalId
     * @param subStorage
     * @param startDate
     * @param endDate
     * @return
     */
    public List<ExpExportVo> getExportDetailByStorage(String storage, String hospitalId, String subStorage, String startDate, String endDate) {
        String sql = "SELECT  sum(EXP_EXPORT_DETAIL.PURCHASE_PRICE*EXP_EXPORT_DETAIL.QUANTITY) import_amount,           \n" +
                "  EXP_EXPORT_MASTER.RECEIVER ,           \n" +
                "  EXP_EXPORT_DETAIL.EXP_FORM     \n" +
                "  FROM EXP_EXPORT_DETAIL,EXP_EXPORT_MASTER     \n" +
                "  WHERE  EXP_EXPORT_DETAIL.DOCUMENT_NO = EXP_EXPORT_MASTER.DOCUMENT_NO";
        if (null != storage && !storage.trim().equals("")) {
            sql += " AND exp_export_master.storage = '" + storage + "' \t\n";
        }
        if (null != hospitalId && !hospitalId.trim().equals("")) {
            sql += " AND EXP_EXPORT_DETAIL.HOSPITAL_ID = '" + hospitalId + "'  ";
        }
        if (null != startDate && !startDate.trim().equals("")) {
            sql += " AND exp_export_master.export_date >= to_date('" + startDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }
        if (null != endDate && !endDate.trim().equals("")) {
            sql += " AND exp_export_master.export_date <= to_date('" + endDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }
        if (null != subStorage && !subStorage.trim().equals("全部")) {
            sql += " and  EXP_EXPORT_MASTER.SUB_STORAGE like '" + subStorage + "'\n";
        }
        sql += "GROUP BY EXP_EXPORT_MASTER.RECEIVER,EXP_EXPORT_DETAIL.EXP_FORM";
        List<ExpExportVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpExportVo.class);
        return result;
    }

    /**
     * 产品去向/按出库去向出库查询
     * @param storage
     * @param hospitalId
     * @param receiver
     * @param startDate
     * @param endDate
     * @return
     */
    public List<ExpExportVo> getExportDetailByTo(String storage, String hospitalId, String receiver, String startDate, String endDate) {
        String sql = "SELECT EXP_EXPORT_MASTER.SUB_STORAGE , \n" +
                "         EXP_EXPORT_DETAIL.EXP_CODE,   \n" +
                "         EXP_DICT.EXP_NAME,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_UNITS,   \n" +
                "         EXP_EXPORT_DETAIL.FIRM_ID, \n" +
                "         EXP_EXPORT_MASTER.RECEIVER,\n"+
                "         sum(EXP_EXPORT_DETAIL.QUANTITY) quantity,   \n" +
                "         sum(EXP_EXPORT_DETAIL.PURCHASE_PRICE * EXP_EXPORT_DETAIL.QUANTITY) amount    \n" +
                "    FROM EXP_EXPORT_DETAIL,   \n" +
                "         EXP_DICT,   \n" +
                "         EXP_EXPORT_MASTER  \n" +
                "   WHERE EXP_EXPORT_MASTER.DOCUMENT_NO = EXP_EXPORT_DETAIL.DOCUMENT_NO AND\n" +
                "         EXP_EXPORT_DETAIL.EXP_CODE = EXP_DICT.EXP_CODE(+) AND \n" +
                "         EXP_EXPORT_DETAIL.EXP_SPEC = EXP_DICT.EXP_SPEC(+) and\n" +
                "         ('B' = 'L' and exp_sgtp in ('2','3') or\n" +
                "         'B' = 'B' and exp_sgtp in ('1','3'))\n";
        if (null != storage && !storage.trim().equals("")) {
            sql += " AND EXP_EXPORT_MASTER.STORAGE = '" + storage + "' \t\n";
        }
        if (null != hospitalId && !hospitalId.trim().equals("")) {
            sql += " AND EXP_EXPORT_DETAIL.HOSPITAL_ID = '" + hospitalId + "'  ";
        }
        if (null != startDate && !startDate.trim().equals("")) {
            sql += " AND EXP_EXPORT_MASTER.EXPORT_DATE >= to_date('" + startDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }
        if (null != endDate && !endDate.trim().equals("")) {
            sql += " AND EXP_EXPORT_MASTER.EXPORT_DATE <= to_date('" + endDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }
        if (null != receiver && !receiver.trim().equals("")) {
            sql += " AND EXP_EXPORT_MASTER.RECEIVER='" + receiver + "'\n";
        }

        sql +="GROUP BY EXP_EXPORT_MASTER.SUB_STORAGE , \n" +
                "         EXP_EXPORT_DETAIL.EXP_CODE,   \n" +
                "         EXP_DICT.EXP_NAME,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_UNITS,\n" +
                "         EXP_EXPORT_DETAIL.FIRM_ID,\n " +
                "         EXP_EXPORT_MASTER.RECEIVER";
        List<ExpExportVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpExportVo.class);
        return result;
    }

    /**
     * 产品去向/按单品种出库去向出库查询
     * @param storage
     * @param hospitalId
     * @param expCode
     * @param startDate
     * @param endDate
     * @return
     */
    public List<ExpExportVo> getExportDetailByExpCode(String storage, String hospitalId, String expCode, String startDate, String endDate){
        String sql = "SELECT receiver,\n" +
                "package_spec,\n" +
                "package_units,\n" +
                "firm_id,\n" +
                "sum(quantity) quantity,\n" +
                "sum(purchase_price*quantity) amount ,\n" +
                "exp_export_detail.exp_code\n" +
                "FROM exp_export_detail,exp_export_master \n" +
                "WHERE exp_export_detail.document_no = exp_export_master.document_no \n" +
                "AND account_indicator = 1 \n";
        if (null != storage && !storage.trim().equals("")) {
            sql += " AND EXP_EXPORT_MASTER.STORAGE = '" + storage + "' \t\n";
        }
        if (null != hospitalId && !hospitalId.trim().equals("")) {
            sql += " AND EXP_EXPORT_DETAIL.HOSPITAL_ID = '" + hospitalId + "'  ";
        }
        if (null != startDate && !startDate.trim().equals("")) {
            sql += " AND EXP_EXPORT_MASTER.EXPORT_DATE >= to_date('" + startDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }
        if (null != endDate && !endDate.trim().equals("")) {
            sql += " AND EXP_EXPORT_MASTER.EXPORT_DATE <= to_date('" + endDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }
        if (null != expCode && !expCode.trim().equals("")) {
            sql += " AND EXP_CODE='" + expCode + "'\n";
        }
        sql += "GROUP BY receiver,package_spec,package_units,firm_id ,exp_export_detail.exp_code";
        List<ExpExportVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpExportVo.class);
        return result;
    }

    /**
     *出库记录查询
     * @param formClass  产品类型
     * @param deptAttr   部门属性
     * @param startDate  开始日期
     * @param stopDate   结束日期
     * @param expCode    消耗品代码
     * @param storage    部门代码
     * @param hospitalId  医院id
     * @return
     */
    public List<ExpExportDetialVo> searchExportRecordDict(String formClass, String hospitalId, String deptAttr, Date startDate, Date stopDate, String expCode, String storage) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String s1 = formatter.format(startDate.getTime());
        String s2 = formatter.format(stopDate.getTime());
        String sql  = "\n" +
                "   SELECT " +
                "         EXP_EXPORT_DETAIL.BATCH_NO,   \n" +
                "         EXP_EXPORT_DETAIL.EXPIRE_DATE,   \n" +
                "         EXP_EXPORT_DETAIL.FIRM_ID,   \n" +
                "         EXP_EXPORT_DETAIL.PURCHASE_PRICE,   \n" +
                "         EXP_EXPORT_DETAIL.PURCHASE_PRICE*EXP_EXPORT_DETAIL.QUANTITY  pay_Amount,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "         EXP_EXPORT_DETAIL.QUANTITY,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_UNITS,   \n" +
                "         EXP_EXPORT_DETAIL.IMPORT_DOCUMENT_NO,   \n" +
                "         EXP_EXPORT_DETAIL.DOCUMENT_NO,   \n" +
                "         EXP_EXPORT_DETAIL.RETAIL_PRICE,   \n" +
                "         EXP_EXPORT_DETAIL.RETAIL_PRICE*EXP_EXPORT_DETAIL.QUANTITY retail_Amount,   \n" +
                "         EXP_EXPORT_MASTER.RECEIVER,   \n" +
                "         EXP_DICT.EXP_NAME,   \n" +
                "         EXP_EXPORT_MASTER.SUB_STORAGE,   \n" +
                "         EXP_EXPORT_DETAIL.EXP_CODE,   \n" +
                "         EXP_EXPORT_DETAIL.EXP_FORM,   \n" +
                "         DEPT_CLINIC_ATTR_DICT.CLINIC_ATTR_NAME dept_ATTR  \n" +
                "    FROM EXP_EXPORT_DETAIL,   \n" +
                "         EXP_EXPORT_MASTER, DEPT_CLINIC_ATTR_DICT , \n" +
                "         EXP_DICT,   \n" +
                "         DEPT_DICT  \n" +
                "   WHERE ( EXP_EXPORT_DETAIL.DOCUMENT_NO = EXP_EXPORT_MASTER.DOCUMENT_NO ) and  \n" +
                "         ( EXP_EXPORT_MASTER.DOC_STATUS <> 1) and\n" +
                "         ( EXP_EXPORT_DETAIL.EXP_CODE = EXP_DICT.EXP_CODE ) and  \n" +
                "         ( EXP_EXPORT_MASTER.RECEIVER = DEPT_DICT.DEPT_NAME ) and  \n" +
                "         DEPT_CLINIC_ATTR_DICT.CLINIC_ATTR_CODE = dept_dict.dept_ATTR and "+
                "         ( EXP_DICT.EXP_SPEC = EXP_EXPORT_DETAIL.EXP_SPEC ) and \n" +
//                "('B' = 'L' and exp_sgtp in ('2','3') or\n" +
//                " 'B' = 'B' and exp_sgtp in ('1','3')) and\n" +
//                "in ('0','1','2','3','4','5','6','9') ) and\n" +
                "         ( EXP_EXPORT_MASTER.STORAGE = '"+storage+"' ) AND  \n" +
                "         ( EXP_EXPORT_MASTER.EXPORT_DATE >= TO_DATE('"+s1+"','yyyy-MM-dd HH24:MI:SS') ) AND  \n" +
                "         ( EXP_EXPORT_MASTER.EXPORT_DATE <= TO_DATE('"+s2+"','yyyy-MM-dd HH24:MI:SS') ) AND  \n" +
                "         ( EXP_EXPORT_DETAIL.hospital_id = '"+hospitalId+"' ) " ;
        if (null != expCode && !expCode.trim().equals("")) {
            sql += " AND EXP_EXPORT_DETAIL.EXP_CODE='" + expCode + "'\n";
        }
        if (null != formClass && !formClass.trim().equals("")) {
            sql += " AND EXP_EXPORT_DETAIL.EXP_form='" + formClass + "'\n";
        }
        if (null != deptAttr && !deptAttr.trim().equals("")) {
            sql += " AND DEPT_DICT.dept_attr='" + deptAttr + "'\n";
        }
        List<ExpExportDetialVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpExportDetialVo.class);
        return result;
    }

    /**
     * 按子库房出库统计 产品类别 子库房 开始时间 结束时间 库房代码 医院id
     * @param formClass
     * @param hospitalId
     * @param subStorage
     * @param startDate
     * @param stopDate
     * @param storage
     * @return
     */
    public List<ExpExportDetialVo> searchSubExportCountDict(String formClass, String hospitalId, String subStorage, Date startDate, Date stopDate, String storage) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String s1 = formatter.format(startDate.getTime());
        String s2 = formatter.format(stopDate.getTime());
        String sql = "SELECT EXP_DICT.EXP_NAME,\n" +
                "         EXP_DICT.EXP_FORM,   \n" +
                "         EXP_EXPORT_DETAIL.BATCH_NO,   \n" +
                "         EXP_EXPORT_DETAIL.EXPIRE_DATE,   \n" +
                "         EXP_EXPORT_DETAIL.EXP_CODE,   \n" +
                "         EXP_EXPORT_DETAIL.DOCUMENT_NO,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_UNITS,\n" +
                "         EXP_EXPORT_DETAIL.FIRM_ID,     \n" +
                "         sum(EXP_EXPORT_DETAIL.TRADE_PRICE * EXP_EXPORT_DETAIL.QUANTITY) pay_amount,   \n" +
                "         sum(EXP_EXPORT_DETAIL.RETAIL_PRICE * EXP_EXPORT_DETAIL.QUANTITY) RETAIL_amount,   \n" +
                "         sum(EXP_EXPORT_DETAIL.QUANTITY) quantity      \n" +
                " FROM EXP_DICT,   \n" +
                "         EXP_EXPORT_DETAIL,   \n" +
                "         EXP_EXPORT_MASTER       \n" +
                " WHERE " +
                "       EXP_EXPORT_MASTER.STORAGE = '"+storage+"' AND \n" +
                "       EXP_EXPORT_MASTER.hospital_id = '"+hospitalId+"' AND \n" +
                "       EXP_EXPORT_MASTER.ACCOUNT_INDICATOR = 1 AND  \n" +
                "       EXP_EXPORT_DETAIL.DOCUMENT_NO = EXP_EXPORT_MASTER.DOCUMENT_NO AND \n" +
                "       EXP_EXPORT_DETAIL.EXP_CODE = EXP_DICT.EXP_CODE(+) and\n" +
                "       EXP_EXPORT_DETAIL.EXP_SPEC = EXP_DICT.EXP_SPEC(+) and  \n" +
//                "       ('B' = 'L' and exp_sgtp in ('2','3') or\n" +
//                "        'B' = 'B' and exp_sgtp in ('1','3')) and\n" +
//                "      EXP_EXPORT_MASTER.SUB_STORAGE like '"+subStorage+"' AND\t\t \n" +
                "         ( EXP_EXPORT_MASTER.EXPORT_DATE >= TO_DATE('"+s1+"','yyyy-MM-dd HH24:MI:SS') ) AND  \n" +
                "         ( EXP_EXPORT_MASTER.EXPORT_DATE <= TO_DATE('"+s2+"','yyyy-MM-dd HH24:MI:SS') )   " ;
        if (null != formClass && !formClass.trim().equals("")) {
            sql += " AND EXP_EXPORT_DETAIL.EXP_form='" + formClass + "'\n";
        }
        if (null != subStorage && !subStorage.trim().equals("" ) ) {
            sql += " AND EXP_EXPORT_MASTER.SUB_STORAGE like '"+subStorage+"' ";
        }
        if(true){
            sql +=" GROUP BY  EXP_DICT.EXP_NAME,EXP_DICT.EXP_FORM,EXP_EXPORT_DETAIL.EXP_CODE,   \n" +
                    "EXP_EXPORT_DETAIL.BATCH_NO,EXP_EXPORT_DETAIL.EXPIRE_DATE,\n" +
                    "EXP_EXPORT_DETAIL.PACKAGE_SPEC,EXP_EXPORT_DETAIL.PACKAGE_UNITS,\n" +
                    "EXP_EXPORT_DETAIL.DOCUMENT_NO, EXP_EXPORT_DETAIL.FIRM_ID";
        }
        List<ExpExportDetialVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpExportDetialVo.class);
        return result;
    }

    public List<ExpExportDetialVo> searchStorageGoCountDict(String formClass, String hospitalId, String subStorage, Date startDate, Date stopDate, String storage) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String s1 = formatter.format(startDate.getTime());
        String s2 = formatter.format(stopDate.getTime());
        String sql ="SELECT EXP_EXPORT_DETAIL.EXP_CODE,   \n" +
                "         EXP_DICT.EXP_NAME,\n" +
                "\t\t\tEXP_DICT.EXP_FORM,   \n" +
                "         EXP_EXPORT_DETAIL.FIRM_ID,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "         EXP_EXPORT_DETAIL.PACKAGE_UNITS,   \n" +
                "         EXP_EXPORT_MASTER.RECEIVER,  \n" +
                "    \t\tsum(EXP_EXPORT_DETAIL.QUANTITY) quantity,\n" +
                "         sum(EXP_EXPORT_DETAIL.QUANTITY * EXP_EXPORT_DETAIL.TRADE_PRICE) pay_amount,     \n" +
                "\t\t\tsum(EXP_EXPORT_DETAIL.QUANTITY * EXP_EXPORT_DETAIL.RETAIL_PRICE) retail_amount     \n" +
                "\tFROM EXP_EXPORT_DETAIL,   \n" +
                "         EXP_DICT,   \n" +
                "         EXP_EXPORT_MASTER  \n" +
                "   WHERE EXP_EXPORT_MASTER.DOCUMENT_NO = EXP_EXPORT_DETAIL.DOCUMENT_NO AND \n" +
                "\t\t\tEXP_EXPORT_DETAIL.EXP_CODE = EXP_DICT.EXP_CODE(+) AND \n" +
                "\t\t\tEXP_EXPORT_DETAIL.EXP_SPEC = EXP_DICT.EXP_SPEC(+)  and\n" +
                "         EXP_EXPORT_MASTER.STORAGE = '"+storage+"' AND \n" +
                "         EXP_EXPORT_MASTER.hospital_id = '"+hospitalId+"' AND \n" +
                "         ( EXP_EXPORT_MASTER.EXPORT_DATE >= TO_DATE('"+s1+"','yyyy-MM-dd HH24:MI:SS') ) AND  \n" +
                "         ( EXP_EXPORT_MASTER.EXPORT_DATE <= TO_DATE('"+s2+"','yyyy-MM-dd HH24:MI:SS') )   " ;
        if (null != formClass && !formClass.trim().equals("")) {
            sql += " AND EXP_EXPORT_DETAIL.EXP_form='" + formClass + "'\n";
        }
        if (null != subStorage && !subStorage.trim().equals("" ) ) {
            sql += " AND EXP_EXPORT_MASTER.SUB_STORAGE like '"+subStorage+"' ";
        }
        if(true){
            sql +=" GROUP BY EXP_EXPORT_DETAIL.EXP_CODE,\n" +
                    "\t\t\tEXP_DICT.EXP_FORM,   \n" +
                    "         EXP_DICT.EXP_NAME,   \n" +
                    "         EXP_EXPORT_DETAIL.FIRM_ID,   \n" +
                    "         EXP_EXPORT_DETAIL.PACKAGE_SPEC,   \n" +
                    "         EXP_EXPORT_DETAIL.PACKAGE_UNITS,   \n" +
                    "         EXP_EXPORT_MASTER.RECEIVER";
        }
        List<ExpExportDetialVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpExportDetialVo.class);
        return result;
    }

    public List<ExpExportDetialVo> exportAssignDict(String hospitalId, Date startDate, Date stopDate, String storage) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String s1 = formatter.format(startDate.getTime());
        String s2 = formatter.format(stopDate.getTime());
        String sql="select b.exp_form,a.fund_item,c.assign_name,sum(b.purchase_price*b.quantity) pay_Amount\n" +
                "from exp_export_master a,exp_export_detail b ,exp_assign_dict c\n" +
                "where a.document_no = b.document_no\n" +
                "and   b.assign_code = c.assign_code\n" +
                "and   a.acctdate between to_date('"+s1+"','yyyy-mm-dd hh24:mi:ss') and to_date('"+s2+"','yyyy-mm-dd hh24:mi:ss')\n" +
                "and   a.storage = '"+storage+"'\n" +
                "and   a.hospital_id = '"+hospitalId+"'\n" +
                "group by b.exp_form,a.fund_item,c.assign_name";
        List<ExpExportDetialVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpExportDetialVo.class);
        return result;
    }

    /**
     * 申请出库页面左上部分根据起止时间和库房查询出库申请记录
     * @param startDate
     * @param endDate
     * @param applyStorage
     * @param storage
     * @param hospitalId
     * @return
     */
    public List<ExpProvideApplicationVo> findExpProvideApplication(String startDate, String endDate, String applyStorage, String storage, String hospitalId){
        String sql = "SELECT DISTINCT EXP_PROVIDE_APPLICATION.APPLICANT_STORAGE,   \n" +
                "         to_char(EXP_PROVIDE_APPLICATION.ENTER_DATE_TIME,'yyyy-mm-dd') enter_date,   \n" +
                "         DEPT_DICT.DEPT_NAME,   \n" +
                "         EXP_PROVIDE_APPLICATION.APPLICANT_NO,\n" +
                "         DEPT_DICT.ID DEPT_ID \n" +
                "    FROM EXP_PROVIDE_APPLICATION,   \n" +
                "         DEPT_DICT  \n" +
                "   WHERE EXP_PROVIDE_APPLICATION.APPLICANT_STORAGE = DEPT_DICT.DEPT_CODE   \n";
        if (null != storage && !storage.trim().equals("")) {
            sql += " and  EXP_PROVIDE_APPLICATION.PROVIDE_STORAGE = '"+storage+"' \n";
        }
        if (null != hospitalId && !hospitalId.trim().equals("")) {
            sql += " AND DEPT_DICT.HOSPITAL_ID = '" + hospitalId + "'  ";
        }
        if (null != startDate && !startDate.trim().equals("")) {
            sql += " and  EXP_PROVIDE_APPLICATION.ENTER_DATE_TIME >= to_date('" + startDate + "','YYYY-MM-DD HH24:MI:SS') \n";
        }
        if (null != endDate && !endDate.trim().equals("")) {
            sql += " and  EXP_PROVIDE_APPLICATION.ENTER_DATE_TIME <= to_date('" + endDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }
        if (null != applyStorage && !applyStorage.trim().equals("")) {
            sql += " and ( EXP_PROVIDE_APPLICATION.APPLICANT_STORAGE like '"+applyStorage+"'||'%' ) \n";
        }else{
            sql += " and ( EXP_PROVIDE_APPLICATION.APPLICANT_STORAGE like '%'||'%' ) \n";
        }
        sql += " and  EXP_PROVIDE_APPLICATION.PROVIDE_FLAG = '0'  order by  DEPT_DICT.ID ";
        List<ExpProvideApplicationVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpProvideApplicationVo.class);
        return result;
    }
}
