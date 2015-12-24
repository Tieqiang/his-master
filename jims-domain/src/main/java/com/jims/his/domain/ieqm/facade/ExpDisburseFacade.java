package com.jims.his.domain.ieqm.facade;


import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.*;
import com.jims.his.domain.ieqm.vo.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangbinbin on 2015/10/30.
 */
public class ExpDisburseFacade extends BaseFacade {
    private EntityManager entityManager;
    private ExpStorageDeptFacade expStorageDeptFacade;



    @Inject
    public ExpDisburseFacade(EntityManager entityManager, ExpStorageDeptFacade expStorageDeptFacade) {
        this.entityManager = entityManager;
        this.expStorageDeptFacade = expStorageDeptFacade;
    }


    @Transactional
    public void expDisburseVo(ExpDisburseVo disburseVo) {
        //1、向exp_disburse_rec表中写入一条记录
        saveDisburseRec(disburseVo);
        //2、向 exp_disburse_rec_detail中写入每一条付款的明细
        saveDisburseRecDetail(disburseVo);
        //3、更新exp_import_detail中的已付款数量
        updateImportDetail(disburseVo);
        //4、更新exp_storage_detp中的当前序号
        updateStorageDept(disburseVo);
    }
    //4、更新exp_storage_detp中的当前序号
    private void updateStorageDept(ExpDisburseVo disburseVo) {
        ExpDisburseRec dis = disburseVo.getExpDisburseRecBeanChangeVo().getInserted().get(0);
        ExpStorageDept dept = expStorageDeptFacade.getByStorageCodeHospitalId(dis.getHospitalId(),dis.getStorage()).get(0);
        dept.setDisburseNoAva((short)(dept.getDisburseNoAva() +1));
        merge(dept) ;//当前的单据号加1
    }

    //2、向 exp_disburse_rec_detail中写入每一条付款的明细
    private void saveDisburseRecDetail(ExpDisburseVo disburseVo) {
        List<ExpDisburseRecDetail> details = disburseVo.getExpDisburseRecDetailBeanChangeVo().getInserted() ;
        for(ExpDisburseRecDetail detail:details){
            merge(detail) ;
        }
    }
    //1、向exp_disburse_rec表中写入一条记录
    private void saveDisburseRec(ExpDisburseVo disburseVo) {
        ExpDisburseRec dis = disburseVo.getExpDisburseRecBeanChangeVo().getInserted().get(0);
        dis.setDisburseDate(new Date());
        merge(dis) ;
    }
    //3、更新exp_import_detail中的已付款数量
    private void updateImportDetail(ExpDisburseVo disburseVo) {
        List<ExpDisburseRecDetail> details = disburseVo.getExpDisburseRecDetailBeanChangeVo().getInserted() ;
        for(ExpDisburseRecDetail detail :details){
            String id  =  detail.getId();
            Double disCount = detail.getDisburseCount();
            ExpImportDetail imDetail = get(ExpImportDetail.class,id);
            imDetail.setDisburseCount(disCount);
            merge(imDetail);
        }
    }

    /**
     *付款单据打印 查询
     * @param disburseRecNo 付款单号
     * @param startDate     开始日期
     * @param stopDate      结束日期
     * @param storage       库房代码
     * @param supplier      供应商
     * @param hospitalId    医院id
     * @return
     */
    public List<ExpDisburseRec> searchPayPrintDict(String disburseRecNo, Date startDate, Date stopDate, String storage, String supplier, String hospitalId) {
        String s1=null;
        String s2=null;
        if(startDate!=null && stopDate != null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            s1 = formatter.format(startDate.getTime());
            s2 = formatter.format(stopDate.getTime());

        }
        String sql = "SELECT EXP_DISBURSE_REC.DISBURSE_REC_NO,   \n" +
                "         EXP_DISBURSE_REC.STORAGE,   \n" +
                "         EXP_DISBURSE_REC.RECEIVER,   \n" +
                "         EXP_DISBURSE_REC.PAY_AMOUNT,   \n" +
                "         EXP_DISBURSE_REC.RETAIL_AMOUNT,   \n" +
                "         EXP_DISBURSE_REC.TRADE_AMOUNT,   \n" +
                "         EXP_DISBURSE_REC.BANK,   \n" +
                "         EXP_DISBURSE_REC.ACCOUNT_NO,   \n" +
                "         EXP_DISBURSE_REC.CHECKER_NO,   \n" +
                "         EXP_DISBURSE_REC.DISBURSE_DATE,   \n" +
                "         EXP_DISBURSE_REC.OPERATOR  \n" +
                "    FROM EXP_DISBURSE_REC  \n" +
                "   WHERE ( EXP_DISBURSE_REC.STORAGE = '"+storage+"' ) AND  \n" +
                "         ( EXP_DISBURSE_REC.HOSPITAL_ID = '"+hospitalId+"' ) AND  \n" +
                "         EXP_DISBURSE_REC.DISBURSE_DATE >= to_date('"+s1+"','YYYY-MM-DD HH24:MI:SS') AND  \n" +
                "         EXP_DISBURSE_REC.DISBURSE_DATE < to_date('"+s2+"','YYYY-MM-DD HH24:MI:SS') + 1  " ;

        if (disburseRecNo != null && disburseRecNo.trim().length() > 0) {
            sql += " and EXP_DISBURSE_REC.disburse_Rec_No='" + disburseRecNo + "'";
        }
        if (supplier != null && supplier.trim().length() > 0) {
            sql += " and EXP_DISBURSE_REC.RECEIVER='" + supplier + "'";
        }
        List<ExpDisburseRec> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpDisburseRec.class);
        return nativeQuery;

    }

    /**
     *付款单据打印明细查询
     * @param storage   库房代码
     * @param disburseRecNo  付款单号
     * @param hospitalId      医院id
     * @return
     */
    public List<ExpDisburseRecVo> searchPayDetailPrintDict( String storage, String disburseRecNo, String hospitalId) {
        String sql ="SELECT EXP_IMPORT_DETAIL.DOCUMENT_NO,   \n" +
                "         EXP_IMPORT_DETAIL.EXP_CODE,   \n" +
                "         A.EXP_NAME,   \n" +
                "         EXP_IMPORT_DETAIL.BATCH_NO,   \n" +
                "         EXP_IMPORT_DETAIL.EXPIRE_DATE,   \n" +
                "         EXP_IMPORT_DETAIL.FIRM_ID,   \n" +
                "         EXP_IMPORT_DETAIL.PURCHASE_PRICE,   \n" +
                "         EXP_IMPORT_DETAIL.DISCOUNT,   \n" +
                "         EXP_IMPORT_DETAIL.PACKAGE_SPEC,   \n" +
                "         EXP_IMPORT_DETAIL.PACKAGE_UNITS,   \n" +
                "         EXP_IMPORT_DETAIL.INVOICE_NO,   \n" +
                "         EXP_IMPORT_DETAIL.INVOICE_DATE,   \n" +
                "         EXP_IMPORT_DETAIL.TRADE_PRICE,   \n" +
                "         EXP_IMPORT_DETAIL.RETAIL_PRICE,   \n" +
                "         EXP_DISBURSE_REC_DETAIL.DISBURSE_REC_NO,   \n" +
                "         EXP_IMPORT_DETAIL.ITEM_NO,   \n" +
                "         EXP_DISBURSE_REC_DETAIL.DISBURSE_COUNT,   \n" +
                "         EXP_DISBURSE_REC_DETAIL.PAY_AMOUNT,   \n" +
                "         EXP_DISBURSE_REC_DETAIL.RETAIL_AMOUNT,   \n" +
                "         EXP_DISBURSE_REC_DETAIL.TRADE_AMOUNT  \n" +
                "    FROM EXP_IMPORT_DETAIL,   \n" +
                "         EXP_DISBURSE_REC_DETAIL , \n" +
                "         EXP_DISBURSE_REC , \n" +
                "         EXP_DICT  A \n" +
                "   WHERE ( EXP_IMPORT_DETAIL.DOCUMENT_NO = EXP_DISBURSE_REC_DETAIL.DOCUMENT_NO ) and  \n" +
                "         ( EXP_IMPORT_DETAIL.ITEM_NO = EXP_DISBURSE_REC_DETAIL.ITEM_NO )   \n" +
                "         and ( EXP_DISBURSE_REC.DISBURSE_REC_NO = EXP_DISBURSE_REC_DETAIL.DISBURSE_REC_NO )   \n" +
                "         AND EXP_IMPORT_DETAIL.package_spec = A.exp_spec   \n" +
                "         AND EXP_IMPORT_DETAIL.exp_code = A.exp_code   \n" +
                "         AND EXP_IMPORT_DETAIL.package_units = A.units   \n" +
                "         and ( EXP_DISBURSE_REC_DETAIL.DISBURSE_REC_NO = '"+disburseRecNo+"' )    " +
                "         and ( EXP_DISBURSE_REC.STORAGE = '"+storage+"' )    "+
                "         and ( EXP_IMPORT_DETAIL.HOSPITAL_ID = '"+hospitalId+"' )    ";

        List<ExpDisburseRecVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpDisburseRecVo.class);
        return nativeQuery;

    }
}
