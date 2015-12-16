package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpProvideApplication;
import com.jims.his.domain.ieqm.entity.ExpStorageProfile;
import com.jims.his.domain.ieqm.vo.ExpProvideApplicationVo;
import com.jims.his.domain.ieqm.vo.ExpStockDefineVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tangxinbo on 2015/10/14.
 */
public class ExpProvideApplicationFacade extends BaseFacade {
    private EntityManager entityManager;

    @Inject
    public ExpProvideApplicationFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 查询最大itemNo
     *
     * @return
     */
    public int getItemNo() {
        String sql = "select nvl(max(item_no),0) item_no from exp_provide_application";
        List result = super.createNativeQuery(sql).getResultList();

        if (result != null && result.size() > 0) {
            BigDecimal max = (BigDecimal) result.get(0);
            return max.intValue();
        } else {
            return 0;
        }

    }
    //保存
    @Transactional
    public List<ExpProvideApplication> saveExpProvideApplication(BeanChangeVo<ExpProvideApplicationVo> beanChangeVo){
        List<ExpProvideApplication> newUpdateDict = new ArrayList<>();

        if(null != beanChangeVo){
            List<ExpProvideApplicationVo> insertDicts = beanChangeVo.getInserted();
            List<ExpProvideApplicationVo> updateDicts = beanChangeVo.getUpdated();
            List<ExpProvideApplicationVo> deleteDicts = beanChangeVo.getDeleted();

            if (insertDicts != null && insertDicts.size() > 0) {
                int itemNo = getItemNo();
                String applyNo = ""+itemNo;
                for (ExpProvideApplicationVo nameDict : insertDicts) {
                    itemNo++;

                    while (applyNo.length()<10) {
                        applyNo = "0" + applyNo;
                    }
                    ExpProvideApplication merge = new ExpProvideApplication();
                    merge.setApplicantStorage(nameDict.getApplicantStorage());
                    merge.setExpSpec(nameDict.getExpSpec());
                    merge.setExpCode(nameDict.getExpCode());
                    merge.setPackageSpec(nameDict.getPackageSpec());
                    merge.setPackageUnits(nameDict.getPackageUnits());
                    merge.setQuantity(nameDict.getQuantity());
                    merge.setApplicationMan(nameDict.getApplicationMan());
                    merge.setProvideStorage(nameDict.getProvideStorage());
                    merge.setEnterDateTime(new Date());
                    merge.setApplicantNo(applyNo);
                    merge.setItemNo((short)itemNo);
                    merge.setProvideFlag("0");
                    merge = merge(merge);
                    newUpdateDict.add(merge);
                }
            }
            if (updateDicts != null && updateDicts.size() > 0) {
                for (ExpProvideApplicationVo nameDict : updateDicts) {
                    ExpProvideApplication merge = get(ExpProvideApplication.class, nameDict.getApplicationId());
                    merge.setApplicantStorage(nameDict.getApplicantStorage());
                    merge.setExpSpec(nameDict.getExpSpec());
                    merge.setExpCode(nameDict.getExpCode());
                    merge.setPackageSpec(nameDict.getPackageSpec());
                    merge.setPackageUnits(nameDict.getPackageUnits());
                    merge.setQuantity(nameDict.getQuantity());
                    merge.setApplicationMan(nameDict.getApplicationMan());
                    merge.setProvideStorage(nameDict.getProvideStorage());
                    merge = merge(merge);
                    newUpdateDict.add(merge);
                }
            }
            if (deleteDicts != null && deleteDicts.size() > 0) {
                List<String> ids = new ArrayList<>();
                for (ExpProvideApplicationVo dict : deleteDicts) {
                    ExpProvideApplication merge = get(ExpProvideApplication.class, dict.getApplicationId());
                    ids.add(dict.getApplicationId());
                    newUpdateDict.add(merge);
                }
                super.removeByStringIds(ExpProvideApplication.class, ids);

            }

        }

        return newUpdateDict;
    }

    /**
     * 作废
     * @param updateData
     * @return
     */
    @Transactional
    public List<ExpProvideApplicationVo> abandonExpProvideApplication(List<ExpProvideApplicationVo> updateData) {
        List<ExpProvideApplicationVo> expProvideApplicationVos = new ArrayList<>();
        if (updateData.size() > 0) {
            for (ExpProvideApplicationVo expProvideApplicationVo : updateData) {
                ExpProvideApplication expProvideApplication = get(ExpProvideApplication.class, expProvideApplicationVo.getApplicationId());
                if(null != expProvideApplication){
                    expProvideApplication.setProvideFlag("2");
                    expProvideApplication.setAuditingOperator(expProvideApplicationVo.getAuditingOperator());
                    expProvideApplication.setAuditingQuantity(expProvideApplicationVo.getAuditingQuantity());

                    merge(expProvideApplication);

                    expProvideApplicationVo.setProvideFlag("2");
                    expProvideApplicationVos.add(expProvideApplicationVo);
                }
            }
        }
        return expProvideApplicationVos;
    }
    //删除
    @Transactional
    public List<ExpProvideApplicationVo> deleteExpProvideApplication(List<ExpProvideApplicationVo> deleteDate){
        List<ExpProvideApplicationVo> expProvideApplicationListVo = new ArrayList<>();
        if (deleteDate.size() > 0){
            List<String> ids = new ArrayList<>();
            for (ExpProvideApplicationVo expProvideApplicationVo : deleteDate){
                ids.add(expProvideApplicationVo.getApplicationId());
            }
            removeByStringIds(ExpProvideApplication.class,ids);
            expProvideApplicationListVo.addAll(deleteDate);
        }
        return expProvideApplicationListVo;
    }
    //查阅本库房所提的申请
    public List<ExpProvideApplicationVo> findCurStorageApplication(String startTime,String endTime,String isSure,String applicationStorage){
        String sql = "select s1.storage_name provide_name,s2.storage_name applicant_name, a.applicant_storage,a.provide_storage,a.item_no,a.exp_code,a.exp_spec,a.package_spec," +
                " a.quantity,a.package_units,a.enter_date_time,a.applicant_no,a.application_man,a.provide_flag,b.exp_name " +
                " from exp_storage_dept s1,exp_storage_dept s2,EXP_PROVIDE_APPLICATION a , EXP_DICT b " +
                " where s1.storage_code=a.PROVIDE_STORAGE and s2.storage_code=a.APPLICANT_STORAGE and a.exp_code = b.exp_code and a.package_spec = b.exp_spec";
        if(null != startTime && !startTime.trim().equals("")){
            sql += " and a.enter_date_time >= to_date('" + startTime + "' , 'yyyy/mm/dd') ";
        }
        if (null != endTime && !endTime.trim().equals("")) {
            sql += " and a.enter_date_time <= to_date('" + endTime + "' , 'yyyy/mm/dd') ";
        }
        if (null != endTime && !endTime.trim().equals("")) {
             sql += " and a.provide_flag = '" + isSure + "'";
        }
        if (null != applicationStorage && !applicationStorage.trim().equals("")) {
            sql += " and a.applicant_storage = '" + applicationStorage + "'";
        }

        List<ExpProvideApplicationVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpProvideApplicationVo.class);
        return result;
    }

    /**
     * 出库申请：查询出库申请物品信息列表
     * @param storageCode
     * @param applyStorage
     * @param appNo
     * @return
     */
    public List<ExpProvideApplicationVo> findExportApplyDict(String storageCode, String hospitalId, String applyStorage, String appNo){
        String sql = "SELECT s1.storage_name provide_name," +
                "         s2.storage_name applicant_name," +
                "         EXP_PROVIDE_APPLICATION.ITEM_NO," +
                "         EXP_PROVIDE_APPLICATION.EXP_CODE," +
                "         EXP_PROVIDE_APPLICATION.ID APPLICATION_ID," +
                "         EXP_DICT.EXP_NAME," +
                "         EXP_PROVIDE_APPLICATION.EXP_SPEC," +
                "         EXP_PROVIDE_APPLICATION.PACKAGE_SPEC," +
                "         EXP_PROVIDE_APPLICATION.QUANTITY," +
                "         EXP_PROVIDE_APPLICATION.PACKAGE_UNITS," +
                "         EXP_PROVIDE_APPLICATION.ENTER_DATE_TIME," +
                "         EXP_PROVIDE_APPLICATION.APPLICANT_NO," +
                "         EXP_PROVIDE_APPLICATION.PROVIDE_FLAG," +
                "         EXP_PROVIDE_APPLICATION.APPLICANT_STORAGE," +
                "         EXP_PROVIDE_APPLICATION.APPLICATION_MAN," +
                "         EXP_DICT.EXP_FORM," +
                "         EXP_PROVIDE_APPLICATION.AUDITING_OPERATOR," +
                "         EXP_PROVIDE_APPLICATION.AUDITING_QUANTITY" +
                "    FROM exp_storage_dept s1,exp_storage_dept s2,EXP_PROVIDE_APPLICATION,EXP_DICT " +
                "         WHERE s1.storage_code=EXP_PROVIDE_APPLICATION.PROVIDE_STORAGE" +
                "         and s2.storage_code=EXP_PROVIDE_APPLICATION.APPLICANT_STORAGE" +
                "         AND EXP_PROVIDE_APPLICATION.EXP_CODE = EXP_DICT.EXP_CODE  and" +
                "         EXP_PROVIDE_APPLICATION.EXP_SPEC = EXP_DICT.EXP_SPEC and " +
                "         EXP_PROVIDE_APPLICATION.PROVIDE_FLAG <> '1'";
        if (null != storageCode && !storageCode.trim().equals("")) {
            sql += " and  EXP_PROVIDE_APPLICATION.PROVIDE_STORAGE = '" + storageCode + "' \n";
        }

        if (null != hospitalId && !hospitalId.trim().equals("")) {
            sql += " and  s1.HOSPITAL_ID = '" + hospitalId + "' \n";
            sql += " and  s2.HOSPITAL_ID = '" + hospitalId + "' \n";
        }
        if (null != applyStorage && !applyStorage.trim().equals("")) {
            sql += " and  EXP_PROVIDE_APPLICATION.APPLICANT_STORAGE ='" + applyStorage + "' \n";
        }
        if (null != appNo && !appNo.trim().equals("")) {
            sql += " and  EXP_PROVIDE_APPLICATION.APPLICANT_NO in '" + appNo + "' \n";
        }
        sql += " ORDER BY EXP_PROVIDE_APPLICATION.ITEM_NO ASC ";
        List<ExpProvideApplicationVo> result = super.createNativeQuery(sql, new ArrayList<Object>(), ExpProvideApplicationVo.class);
        return result;
    }
}
