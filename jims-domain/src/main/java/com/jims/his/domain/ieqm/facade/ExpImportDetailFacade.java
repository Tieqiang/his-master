package com.jims.his.domain.ieqm.facade;


import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpImportDetail;
import com.jims.his.domain.ieqm.entity.ExpImportMaster;
import com.jims.his.domain.ieqm.entity.ExpSupplierCatalog;
import com.jims.his.domain.ieqm.vo.ExpDisburseRecVo;
import com.jims.his.domain.ieqm.vo.ExpImportDetailVo;
import com.jims.his.domain.ieqm.vo.ExpImportVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangbinbin on 2015/10/22.
 */
public class ExpImportDetailFacade extends BaseFacade {
    private EntityManager entityManager;

    @Inject
    public ExpImportDetailFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 消耗品入库账单明细查询
     * @param documentNo  入库单号
     * @param hospitalId  医院Id
     * @return  PURCHASE_PRICE
     */
    public List<ExpImportDetailVo> searchImportDetailDict(String documentNo, String hospitalId) {
        String supplier=findSupplierByDocumentNo(documentNo);//supplierId
        boolean flag=findIsExist(supplier);
        String  sql = "SELECT distinct EXP_IMPORT_DETAIL.DOCUMENT_NO,   \n" +
                "         EXP_IMPORT_DETAIL.ITEM_NO,   \n" +
                "         EXP_IMPORT_DETAIL.EXP_CODE,   \n" +
                "         EXP_IMPORT_DETAIL.EXP_SPEC,   \n" +
                "         EXP_IMPORT_DETAIL.UNITS,   \n" +
                "         EXP_IMPORT_DETAIL.BATCH_NO,   \n" +
                "         EXP_IMPORT_DETAIL.EXPIRE_DATE,   \n" +
                "         EXP_IMPORT_DETAIL.FIRM_ID,\n" +
                "\t       EXP_IMPORT_MASTER.SUPPLIER,   \n" +
                "\t       EXP_IMPORT_DETAIL.RETAIL_PRICE*EXP_IMPORT_DETAIL.QUANTITY account_receivable,   \n" +
                "         EXP_IMPORT_DETAIL.PURCHASE_PRICE,   \n" +
                "         EXP_IMPORT_DETAIL.DISCOUNT,   \n" +
                "         EXP_IMPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "         EXP_IMPORT_DETAIL.QUANTITY,   \n" +
                "         EXP_IMPORT_DETAIL.PACKAGE_UNITS,   \n" +
                "         EXP_IMPORT_DETAIL.SUB_PACKAGE_1,   \n" +
                "         EXP_IMPORT_DETAIL.SUB_PACKAGE_UNITS_1,   \n" +
                "         EXP_IMPORT_DETAIL.SUB_PACKAGE_2,   \n" +
                "         EXP_IMPORT_DETAIL.SUB_PACKAGE_UNITS_2,   \n" +
                "         EXP_IMPORT_DETAIL.INVOICE_NO,   " +
                "" +
                "         EXP_IMPORT_DETAIL.INVOICE_DATE,   \n" +
                "         EXP_DICT.EXP_NAME,\n" +
                "         EXP_IMPORT_DETAIL.TRADE_PRICE,   \n" +
                "         EXP_IMPORT_DETAIL.RETAIL_PRICE,\n" +
                "\t       EXP_IMPORT_DETAIL.MEMO,  \n" +
                "\t       EXP_IMPORT_DETAIL.INVENTORY,\n" +
                "\t       EXP_IMPORT_DETAIL.RegistNo,\n" +
                "\t       EXP_IMPORT_DETAIL.LicenceNo\n" +
                "    FROM EXP_IMPORT_DETAIL, \n" +
                "\t       EXP_IMPORT_MASTER,  \n" +
                "         EXP_DICT  \n" +
                "   WHERE ( EXP_IMPORT_DETAIL.EXP_CODE = EXP_DICT.EXP_CODE ) and  \n" +
                "         ( EXP_IMPORT_DETAIL.EXP_SPEC = EXP_DICT.EXP_SPEC ) and \n" +
                "         ( EXP_IMPORT_DETAIL.document_no = exp_import_master.document_no ) and \n" +
                "           EXP_IMPORT_DETAIL.DOCUMENT_NO = '" + documentNo + "' and \n" +
                "           EXP_IMPORT_DETAIL.HOSPITAL_ID = '" + hospitalId + "'  ";

         if(flag){//进价
            sql=sql.replace("RETAIL_PRICE*EXP_IMPORT_DETAIL.QUANTITY","PURCHASE_PRICE*EXP_IMPORT_DETAIL.QUANTITY");
         }
         List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
         return nativeQuery;


    }

    /**
     *
     * @param supplier
     * @return
     */
    private boolean findIsExist(String supplier) {
        String sql="from ExpSupplierCatalog where supplierId='"+supplier+"' ";
        List<ExpSupplierCatalog> list=entityManager.createQuery(sql).getResultList();
        if(list!=null&&!list.isEmpty())
            return true;
            return false;
    }

    /**
     *
     * @param documentNo
     * @return
     */
    private String findSupplierByDocumentNo(String documentNo) {
        String sql="select supplier from ExpImportMaster where documentNo='"+documentNo+"' ";
        return (String)entityManager.createQuery(sql).getSingleResult();
     }

    /**
     * 付款处理
     * @param documentNo  入库账单号
     * @param hospitalId
     * @param startDate  开始日期
     * @param stopDate   结束日期
     * @param storage    库房代码
     * @param supplier   供应商
     * @return
     */
    public List<ExpImportDetailVo> managePayImportDetail(String documentNo, String hospitalId, Date startDate, Date stopDate, String storage, String supplier) {
        String s1=null;
        String s2=null;
        if(startDate!=null && stopDate != null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            s1 = formatter.format(startDate.getTime());
            s2 = formatter.format(stopDate.getTime());

        }
        String sql = "SELECT distinct M.SUPPLIER,   \n" +
                "         D.DOCUMENT_NO,   \n" +
                "         D.ID,   \n" +
                "         D.ITEM_NO,   \n" +
                "         A.exp_name,   \n" +
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
                "         D.DISBURSE_REC_NO,   \n" +
                "         D.DISBURSE_COUNT, \n" +
                "         M.SUB_STORAGE \n" +
//                "\t       M.ACCOUNT_INDICATOR,\n" +
                "    FROM EXP_IMPORT_MASTER M,   \n" +
                "         EXP_IMPORT_DETAIL D,  \n" +
                "         EXP_DICT A  \n" +
                "   WHERE M.STORAGE = '"+storage+"' " +
                "          AND M.HOSPITAL_ID = '"+hospitalId+"' " +
                "          AND M.DOCUMENT_NO(+) = D.DOCUMENT_NO   \n" +
                "          AND D.exp_spec = A.exp_spec   \n" +
                "          AND D.exp_code = A.exp_code   \n" +
                "          AND D.units = A.units   \n";
//                "          AND M.storage = A.storage_code   \n" +
//                "\t        AND D.QUANTITY > NVL(D.DISBURSE_COUNT,0)\n";
        if (s1 != null) {
            sql += " and M.import_Date>=to_date ( '" + s1 + "' , 'yyyy-MM-dd HH24:MI:SS' ) ";
        }
        if (s2 != null) {
            sql += " and M.import_Date<=to_date ( '" + s2 + "' , 'yyyy-MM-dd HH24:MI:SS' ) ";
        }
        if (supplier != null && supplier.trim().length() > 0) {
            sql += " and M.supplier='" + supplier + "'";
        }
        if (documentNo != null && documentNo.trim().length() > 0) {
            sql += " and M.document_No='" + documentNo + "'";
        }

        List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
        return nativeQuery;
    }

    /**
     * 供应商付款情况统计
     * @param startDate 开始时间
     * @param stopDate  结束时间
     * @param storage   库房代码
     * @param hospitalId  医院Id
     * @return
     */
    public List<ExpDisburseRecVo> searchSupplierPayDict(Date startDate, Date stopDate, String storage, String hospitalId) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String s1 = formatter.format(startDate.getTime());
        String s2 = formatter.format(stopDate.getTime());

        String sql = "SELECT EXP_IMPORT_MASTER.SUPPLIER  ,   \n" +
                "         SUM(Nvl(EXP_IMPORT_DETAIL.DISBURSE_COUNT,0)*EXP_IMPORT_DETAIL.PURCHASE_PRICE) PAY_AMOUNT ,\n" +
                "         SUM(Nvl(EXP_IMPORT_DETAIL.DISBURSE_COUNT,0)*EXP_IMPORT_DETAIL.RETAIL_PRICE) RETAIL_AMOUNT, \n" +
                "         0 purchase_Price,\n" +
                "         0 retail_Price \n" +
                "    FROM EXP_IMPORT_DETAIL,\n" +
                "         EXP_IMPORT_MASTER   \n" +
                "   WHERE EXP_IMPORT_MASTER.DOCUMENT_NO = EXP_IMPORT_DETAIL.DOCUMENT_NO AND\n" +
                "         EXP_IMPORT_MASTER.STORAGE = '"+storage+"' AND\n" +
                "         EXP_IMPORT_MASTER.HOSPITAL_ID = '"+hospitalId+"' AND\n" +
                "         EXP_IMPORT_MASTER.IMPORT_DATE >= to_date('"+s1+"' ,'YYYY-MM-DD HH24:MI:SS') AND\n" +
                "         EXP_IMPORT_MASTER.IMPORT_DATE <= to_date('"+s2+"' ,'YYYY-MM-DD HH24:MI:SS') AND\n" +
                "         EXP_IMPORT_DETAIL.DISBURSE_COUNT > 0\n" +
                "GROUP BY EXP_IMPORT_MASTER.SUPPLIER\n" +
                " UNION\n" +
                "   SELECT EXP_IMPORT_MASTER.SUPPLIER,\n" +
                "         0 PAY_AMOUNT ,\n" +
                "      0,   \n" +
                "         SUM((EXP_IMPORT_DETAIL.QUANTITY - Nvl(EXP_IMPORT_DETAIL.DISBURSE_COUNT,0))*EXP_IMPORT_DETAIL.PURCHASE_PRICE) purchase_Price,   \n" +
                "         SUM((EXP_IMPORT_DETAIL.QUANTITY - Nvl(EXP_IMPORT_DETAIL.DISBURSE_COUNT,0))*EXP_IMPORT_DETAIL.RETAIL_PRICE)  retail_Price \n" +
                "    FROM EXP_IMPORT_DETAIL,   \n" +
                "         EXP_IMPORT_MASTER  \n" +
                "   WHERE ( EXP_IMPORT_DETAIL.DOCUMENT_NO = EXP_IMPORT_MASTER.DOCUMENT_NO ) and  \n" +
                "         ( EXP_IMPORT_MASTER.STORAGE = '"+storage+"' ) AND  \n" +
                "         ( EXP_IMPORT_MASTER.HOSPITAL_ID = '"+hospitalId+"' ) AND  \n" +
                "         ( EXP_IMPORT_MASTER.IMPORT_DATE >= to_date('"+s1+"' ,'YYYY-MM-DD HH24:MI:SS') ) AND  \n" +
                "         ( EXP_IMPORT_MASTER.IMPORT_DATE <= to_date('"+s2+"' ,'YYYY-MM-DD HH24:MI:SS') ) AND\n" +
                "         EXP_IMPORT_DETAIL.QUANTITY > Nvl(EXP_IMPORT_DETAIL.DISBURSE_COUNT,0)  \n" +
                "GROUP BY EXP_IMPORT_MASTER.SUPPLIER";
        List<ExpDisburseRecVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpDisburseRecVo.class);
        return nativeQuery;
    }

    /**
     * 产品上账
     * @param startDate  开始时间
     * @param stopDate   结束时间
     * @param storage    库房代码
     * @param hospitalId  医院Id
     * @param imClass     入库方式
     * @param startBill   开始账单
     * @param stopBill    结束账单
     * @param billRadio   是否已经结账
     * @param supplier    供应商
     * @param expCode     消耗品代码
     * @return
     */
    public List<ExpImportDetailVo> searchDoAccountDict(Date startDate, Date stopDate, String storage, String hospitalId, String imClass, String startBill, String stopBill, String billRadio, String supplier, String expCode) {
        String s1=null;
        String s2=null;
        if(startDate!=null && stopDate != null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            s1 = formatter.format(startDate.getTime());
            s2 = formatter.format(stopDate.getTime());

        }
        String sql ="SELECT distinct EXP_IMPORT_MASTER.DOCUMENT_NO,              " +
                        "   EXP_IMPORT_MASTER.STORAGE,              " +
                        "   EXP_STORAGE_DEPT.STORAGE_NAME,              " +
                        "   EXP_IMPORT_MASTER.id,              " +
                        "   EXP_IMPORT_DETAIL.id  detail_id,              " +
                        "   EXP_IMPORT_MASTER.IMPORT_DATE,              " +
                        "   EXP_IMPORT_MASTER.SUPPLIER,             " +
                        "   exp_dict.exp_name,             " +
                        " EXP_IMPORT_DETAIL.ITEM_NO,              " +
                        " EXP_IMPORT_DETAIL.EXP_CODE,              " +
                        " EXP_IMPORT_DETAIL.PACKAGE_SPEC,              " +
                        " EXP_IMPORT_DETAIL.UNITS,              " +
                        " EXP_IMPORT_DETAIL.BATCH_NO,              " +
                        " EXP_IMPORT_DETAIL.QUANTITY,              " +
                        " EXP_IMPORT_DETAIL.QUANTITY*EXP_IMPORT_DETAIL.PURCHASE_PRICE  pay_amount,              " +
                        " EXP_IMPORT_DETAIL.INVOICE_NO,             " +
                        " EXP_IMPORT_DETAIL.INVOICE_DATE,              " +
                        "EXP_IMPORT_DETAIL.TALLY_FLAG,              " +
                        "EXP_IMPORT_DETAIL.TALLY_DATE,              " +
                        "EXP_IMPORT_DETAIL.TALLY_OPERTOR,              " +
                        "EXP_IMPORT_DETAIL.PURCHASE_PRICE,              " +
                        "EXP_IMPORT_MASTER.IMPORT_CLASS ,       " +
                        "EXP_IMPORT_MASTER.ACCOUNT_INDICATOR        " +
                "FROM EXP_IMPORT_DETAIL,EXP_IMPORT_MASTER,exp_dict,exp_storage_dept       " +
                "WHERE  EXP_IMPORT_MASTER.STORAGE = '"+storage+"' and" +
                "       EXP_IMPORT_MASTER.hospital_id = '"+hospitalId+"' and" +
                "       EXP_IMPORT_MASTER.STORAGE=exp_storage_dept.storage_code and" +
                "     ( EXP_IMPORT_DETAIL.DOCUMENT_NO = EXP_IMPORT_MASTER.DOCUMENT_NO ) and" +
                "       EXP_IMPORT_DETAIL.exp_spec = exp_dict.exp_spec and" +
                "       EXP_IMPORT_DETAIL.units = exp_dict.units and" +
                "       EXP_IMPORT_DETAIL.exp_code = exp_dict.exp_code " ;
        if (billRadio != null && billRadio.trim().length() > 0) {
            sql += " and EXP_IMPORT_DETAIL.TALLY_FLAG ='" + billRadio + "'";
        }
        if (imClass != null && imClass.trim().length() > 0) {
            sql += " and EXP_IMPORT_MASTER.import_Class='" + imClass + "'";
        }
        if (startBill != null && startBill.trim().length() > 0) {
            sql += " and EXP_IMPORT_MASTER.document_No>='" + startBill + "'";
        }
        if (stopBill != null && stopBill.trim().length() > 0) {
            sql += " and EXP_IMPORT_MASTER.document_No<='" + stopBill + "'";
        }

        if (s1 != null) {
            sql += " and EXP_IMPORT_MASTER.import_Date>=to_date ( '" + s1 + "' , 'yyyy-MM-dd HH24:MI:SS' ) ";
        }
        if (s2 != null) {
            sql += " and EXP_IMPORT_MASTER.import_Date<=to_date ( '" + s2 + "' , 'yyyy-MM-dd HH24:MI:SS' ) ";
        }
        if (supplier != null && supplier.trim().length() > 0) {
            sql += " and EXP_IMPORT_MASTER.supplier='" + supplier + "'";
        }
        if (expCode != null && expCode.trim().length() > 0) {
            sql += " and Exp_Import_Detail.exp_Code='" + expCode + "' " ;
        }

        List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
        return nativeQuery;
    }

    /**
     * 保存明细记录：记账标志 主记录：发票日期  发票号
     * @param importVo
     */
    @Transactional
    public void saveDoAccountDict(ExpImportVo importVo) {
        List<ExpImportMaster> masters = importVo.getExpImportMasterBeanChangeVo().getUpdated();
        List<ExpImportDetail> details = importVo.getExpImportDetailBeanChangeVo().getUpdated();

        for(ExpImportDetail detail :details){
            String id  =  detail.getId();
            ExpImportDetail del = get(ExpImportDetail.class,id);
            del.setInvoiceNo(detail.getInvoiceNo());
            del.setInvoiceDate(detail.getInvoiceDate());
            del.setTallyFlag(detail.getTallyFlag());
            del.setTallyDate(detail.getTallyDate());
            detail.setTallyOpertor(detail.getTallyOpertor());
            merge(del);
        }
    }

    /**
     * 会计应付款
     * @param startDate  开始日期
     * @param stopDate   结束日期
     * @param storage    库房代码
     * @param hospitalId  医院id
     * @return
     */
    public List<ExpImportDetailVo> searchAccountorPayDict(Date startDate, Date stopDate, String storage, String hospitalId) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String s1 = formatter.format(startDate.getTime());
        String s2 = formatter.format(stopDate.getTime());
        String sql ="SELECT  EXP_IMPORT_MASTER.SUPPLIER , round(sum(EXP_IMPORT_DETAIL.PURCHASE_PRICE * EXP_IMPORT_DETAIL.QUANTITY),2) pay_Amount    \n" +
                "    FROM EXP_IMPORT_DETAIL , EXP_IMPORT_MASTER     \n" +
                "    WHERE ( EXP_IMPORT_DETAIL.DOCUMENT_NO = EXP_IMPORT_MASTER.DOCUMENT_NO ) and          \n" +
                "          ( ( EXP_IMPORT_DETAIL.TALLY_FLAG = 1 ) and          \n" +
                "          ( EXP_IMPORT_DETAIL.TALLY_DATE >= to_date('"+s1+"','YYYY-MM-DD HH24:MI:SS') ) And          \n" +
                "          ( EXP_IMPORT_DETAIL.TALLY_DATE <= to_date('"+s2+"','YYYY-MM-DD HH24:MI:SS') ) and          \n" +
                "          ( EXP_IMPORT_MASTER.STORAGE = '"+storage+"' )     and\n" +
                "          ( EXP_IMPORT_MASTER.hospital_id = '"+hospitalId+"' ) )\n" +
                "    GROUP BY EXP_IMPORT_MASTER.SUPPLIER";


        List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
        return nativeQuery;

    }

    /**
     * 产品来源/按子库房入库统计
     *
     * @param hospitalId
     * @param startDate
     * @param stopDate
     * @param storage
     * @param subStorage
     * @param expForm
     * @return
     */
    public List<ExpImportDetailVo> getSubImportDetails(String hospitalId, String startDate, String stopDate, String storage, String subStorage, String expForm) {
        String sql = "SELECT EXP_DICT.EXP_NAME,\n" +
                "         EXP_DICT.EXP_FORM,   \n" +
                "         EXP_IMPORT_DETAIL.EXP_CODE,   \n" +
                "         EXP_IMPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "         EXP_IMPORT_DETAIL.PACKAGE_UNITS,\n" +
                "         EXP_IMPORT_DETAIL.FIRM_ID, \n" +
                "         EXP_IMPORT_DETAIL.TRADE_PRICE, \n" +
                "         sum(EXP_IMPORT_DETAIL.QUANTITY) quantity,     \n" +
                "         sum(EXP_IMPORT_DETAIL.PURCHASE_PRICE * EXP_IMPORT_DETAIL.QUANTITY) purchase_amount, \n" +
                "         sum(EXP_IMPORT_DETAIL.RETAIL_PRICE * EXP_IMPORT_DETAIL.QUANTITY) pay_amount, \n" +
                "         sum(EXP_IMPORT_DETAIL.PURCHASE_PRICE * EXP_IMPORT_DETAIL.QUANTITY * \n" +
                "         EXP_IMPORT_DETAIL.DISCOUNT / 100) discount               \n" +
                "    FROM EXP_DICT,   \n" +
                "         EXP_IMPORT_DETAIL,   \n" +
                "         EXP_IMPORT_MASTER      \n" +
                "   WHERE EXP_IMPORT_MASTER.ACCOUNT_INDICATOR = 1 \n";
        if (null != hospitalId && !hospitalId.trim().equals("")) {
            sql += "      AND EXP_IMPORT_DETAIL.HOSPITAL_ID = '" + hospitalId + "'\n";
        }
        if (null != expForm && !expForm.trim().equals("全部")) {
            sql += "      AND EXP_IMPORT_DETAIL.EXP_FORM = '" + expForm + "'\n";
        }
        if (null != storage && !storage.trim().equals("")) {
            sql += "      AND EXP_IMPORT_MASTER.STORAGE = '" + storage + "'\n";
        }
        if (subStorage.equals("全部")) {
            sql += "      AND EXP_IMPORT_MASTER.SUB_STORAGE like '%'\n";
        } else {
            sql += "      AND EXP_IMPORT_MASTER.SUB_STORAGE like '%" + subStorage + "%'\n";
        }
        if (null != startDate) {
            sql += "      AND EXP_IMPORT_MASTER.IMPORT_DATE >= to_date('" + startDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }
        if (null != stopDate) {
            sql += "      AND EXP_IMPORT_MASTER.IMPORT_DATE <= to_date('" + stopDate + "','YYYY-MM-DD HH24:MI:SS')\n";
        }

        sql += " AND EXP_IMPORT_DETAIL.EXP_CODE = EXP_DICT.EXP_CODE(+) \n" +
                " AND EXP_IMPORT_DETAIL.EXP_SPEC = EXP_DICT.EXP_SPEC(+)\n" +
                " AND EXP_IMPORT_DETAIL.DOCUMENT_NO = EXP_IMPORT_MASTER.DOCUMENT_NO \n" +
                "   GROUP BY EXP_DICT.EXP_NAME, \n" +
                "   EXP_DICT.EXP_FORM,  \n" +
                "   EXP_IMPORT_DETAIL.EXP_CODE,   \n" +
                "   EXP_IMPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "   EXP_IMPORT_DETAIL.PACKAGE_UNITS,\n" +
                "   EXP_IMPORT_DETAIL.FIRM_ID,  " +
                "   EXP_IMPORT_DETAIL.TRADE_PRICE ";
        List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
        return nativeQuery;
    }

    /**
     * 单品总账
     * @param hospitalId  医院id
     * @param startDate   开始日期
     * @param stopDate    结束日期
     * @param storage     库房代码
     * @param expCode    消耗品代码
     * @param packageSpec  消耗品规格
     * @return
     */
    public List<ExpImportDetailVo> getSingleAccount(String hospitalId, Date startDate, Date stopDate, String storage, String expCode, String packageSpec) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String s1 = formatter.format(startDate.getTime());
        String s2 = formatter.format(stopDate.getTime());
        String sql = "SELECT exp_dict.EXP_NAME,IMPORT_CLASS io_class,package_spec,firm_id,(import_date) action_date,SUPPLIER our_name, \n" +
                "      PACKAGE_UNITS,PURCHASE_PRICE,  quantity import_num,quantity*PURCHASE_PRICE  import_price,0 export_num,  0 export_price,   inventory,batch_no,EXPIRE_DATE ,exp_import_detail.exp_code,'入库' way\n" +
                "FROM exp_import_detail,exp_import_master,EXP_DICT  \n" +
                "WHERE " +
                "storage = '"+storage+"' AND \n" +
                " exp_import_master.hospital_id = '"+hospitalId+"' AND \n" +
                "    exp_import_detail.document_no = exp_import_master.document_no AND \n" +
                "    import_date >= to_date('"+s1+"','yyyy-mm-dd hh24:mi:ss') AND " +
                "    import_date <= to_date('"+s2+"','yyyy-mm-dd hh24:mi:ss') AND " +
                "    exp_import_detail.EXP_code = exp_dict.EXP_code and " +
                "    exp_import_detail.exp_spec = exp_dict.exp_spec " ;
        if (null != expCode && !expCode.trim().equals("")) {
            sql += "     AND    exp_import_detail.exp_code = '"+expCode+"'\n";
        }
        if (null != packageSpec && !packageSpec.trim().equals("")) {
            sql += "      AND PACKAGE_SPEC = '"+packageSpec+"'\n";
        }
        sql+=   "UNION  \n" +
                "SELECT exp_dict.EXP_NAME,EXPORT_CLASS io_class,package_spec,firm_id,(export_date) action_date,RECEIVER our_name, \n" +
                "      PACKAGE_UNITS,PURCHASE_PRICE,  0 import_num,0  import_price,quantity export_num, quantity*PURCHASE_PRICE  export_price,      inventory,batch_no,EXPIRE_DATE ,exp_export_detail.exp_code,'出库'way\n" +
                "FROM exp_export_detail,exp_export_master,exp_dict \n" +
                "WHERE storage = '"+storage+"' AND \n" +
                "    exp_export_master.hospital_id = '"+hospitalId+"' AND \n" +
                "    exp_export_detail.document_no = exp_export_master.document_no AND \n" +
                "    export_date >= to_date('"+s1+"','yyyy-mm-dd hh24:mi:ss') AND export_date <= to_date('"+s2+"','yyyy-mm-dd hh24:mi:ss') AND " +
                "    exp_export_detail.EXP_code = exp_dict.EXP_code  and " +
                "    exp_export_detail.exp_spec = exp_dict.exp_spec" ;
        if (null != expCode && !expCode.trim().equals("")) {
            sql += "     AND    exp_export_detail.exp_code = '"+expCode+"'\n";
        }
        if (null != packageSpec && !packageSpec.trim().equals("")) {
            sql += "      AND PACKAGE_SPEC = '"+packageSpec+"'\n";
        }
        List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
        return nativeQuery;
    }

    /**
     * 按入库类型入库统计
     * @param hospitalId   医院id
     * @param startDate    开始时间
     * @param stopDate     结束时间
     * @param storage      库房代码
     * @param importClass 入库类型
     * @return
     */
    public List<ExpImportDetailVo> getImportClassAccount(String hospitalId, Date startDate, Date stopDate, String storage, String importClass) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String s1 = formatter.format(startDate.getTime());
        String s2 = formatter.format(stopDate.getTime());
        String sql = "SELECT exp_import_master.IMPORT_CLASS,\n" +
                "    SUPPLIER,\n" +
                "    count(exp_import_detail.document_no) account_Receivable,  \n" +
                "      count(distinct exp_import_detail.exp_code) account_Indicator,  \n" +
                "      sum(quantity*purchase_price) import_price        \n" +
                "FROM exp_import_detail,exp_import_master      \n" +
                "WHERE exp_import_detail.document_no = exp_import_master.document_no AND    \n" +
                "    exp_import_master.storage = '"+storage+"' AND \n" +
                "    exp_import_master.hospital_id = '"+hospitalId+"' AND  \n" +
                "    exp_import_master.import_date >= to_date('"+s1+"','yyyy-mm-dd hh24:mi:ss') AND   \n" +
                "    exp_import_master.import_date <= to_date('"+s2+"','yyyy-mm-dd hh24:mi:ss')     ";
        if(importClass!=null && !importClass.trim().equals("")){
            sql+=" and exp_import_master.IMPORT_CLASS = '"+importClass+"'";
        }
        if(true){
            sql+="   GROUP BY   exp_import_master.IMPORT_CLASS,SUPPLIER";
        }
        List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
        return nativeQuery;
    }

    /**
     * 供货商供货情况查询  医院id 开始时间 结束时间 库房代码 供应商
     * @param hospitalId
     * @param startDate
     * @param stopDate
     * @param storage
     * @param supplier
     * @return
     */
    public List<ExpImportDetailVo> getImportSupplierSearch(String hospitalId, Date startDate, Date stopDate, String storage, String supplier) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String s1 = formatter.format(startDate.getTime());
        String s2 = formatter.format(stopDate.getTime());
        String sql = "SELECT exp_import_detail.exp_code,exp_name,package_spec,package_units,firm_id,\n" +
                "     sum(NVL(quantity,0)) quantity," +
                "sum(NVL(quantity*purchase_price,0)) pay_amount," +
                "sum(NVL(quantity*purchase_price*discount/100,0)) purchase_Amount \n" +
                "     FROM exp_import_master,exp_import_detail,exp_dict  \n" +
                "    WHERE " +
                "          storage = '"+storage+"' AND " +
                "          exp_import_master.hospital_id = '"+hospitalId+"' AND " +
                "          exp_import_master.import_date >= to_date('"+s1+"','yyyy-mm-dd hh24:mi:ss') AND   \n" +
                "          exp_import_master.import_date <= to_date('"+s2+"','yyyy-mm-dd hh24:mi:ss')      AND \n" +
                "          exp_import_master.document_no = exp_import_detail.document_no AND \n" +
                "          exp_import_detail.exp_code = exp_dict.exp_code(+) AND \n" +
                "          exp_import_detail.exp_spec = exp_dict.exp_spec(+)";
        if(supplier!=null && !supplier.trim().equals("")){
            sql+=" and exp_import_master.supplier = '"+supplier+"'";
        }
        if(true){
            sql+="   GROUP BY   exp_import_detail.exp_code,exp_name,package_spec,package_units,firm_id";
        }
        List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
        return nativeQuery;
    }

    /**
     * 供货商供货情况统计 医院id 开始时间 结束时间 库房代码 子库房
     * @param hospitalId
     * @param startDate
     * @param stopDate
     * @param storage
     * @param subStorage
     * @return
     */
    public List<ExpImportDetailVo> getImportSupplierCount(String hospitalId, Date startDate, Date stopDate, String storage, String subStorage) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String s1 = formatter.format(startDate.getTime());
        String s2 = formatter.format(stopDate.getTime());
        String sql = "SELECT supplier,\n" +
                "      sub_storage,\n" +
                "      sum(quantity) quantity,\n" +
                "      sum(purchase_price * quantity) purchase_Price,\n" +
                "      sum(trade_price * quantity) trade_price,\n" +
                "      sum(retail_price * quantity) retail_price \n" +
                "FROM exp_import_detail,exp_import_master \n" +
                "WHERE \n" +
                "          storage = '"+storage+"' AND " +
                "          exp_import_master.hospital_id = '"+hospitalId+"' AND " +
                "          exp_import_master.import_date >= to_date('"+s1+"','yyyy-mm-dd hh24:mi:ss') AND   \n" +
                "          exp_import_master.import_date <= to_date('"+s2+"','yyyy-mm-dd hh24:mi:ss')      AND \n" +
                "          exp_import_master.document_no = exp_import_detail.document_no " ;
        if(subStorage!=null && !subStorage.trim().equals("")){
            sql+=" and exp_import_master.sub_Storage = '"+subStorage+"'";
        }
        if(true){
            sql+="   GROUP BY   sub_Storage,supplier";
        }
        List<ExpImportDetailVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpImportDetailVo.class);
        return nativeQuery;
    }
}

