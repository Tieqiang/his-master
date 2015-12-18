package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpExportClassDict;
import com.jims.his.domain.ieqm.entity.ExpFormDict;
import com.jims.his.domain.ieqm.entity.ExpStorageProfile;
import com.jims.his.domain.ieqm.entity.MeasuresDict;
import com.jims.his.domain.ieqm.vo.ExpStockDefineVo;
import com.jims.his.domain.ieqm.vo.ExpStorageProfileVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangxinbo on 2015/10/13.
 */
public class ExpStorageProfileFacade extends BaseFacade {
    private EntityManager entityManager;

    @Inject
    public ExpStorageProfileFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    //保存
    @Transactional
    public List<ExpStorageProfile> saveExpStockDefine(BeanChangeVo<ExpStorageProfile> beanChangeVo){
        List<ExpStorageProfile> newUpdateDict = new ArrayList<>();
        List<ExpStorageProfile> insertData = beanChangeVo.getInserted();
        List<ExpStorageProfile> updateData = beanChangeVo.getUpdated();
        List<ExpStorageProfile> deleteData = beanChangeVo.getDeleted();
        if (insertData.size() > 0 ){
            for (ExpStorageProfile dict : insertData){
                ExpStorageProfile expStorageProfile = merge(dict);
                newUpdateDict.add(expStorageProfile);
            }
        }
        if(updateData.size() > 0){
            for (ExpStorageProfile dict : updateData) {
                ExpStorageProfile expStorageProfile = merge(dict);
                newUpdateDict.add(expStorageProfile);
            }

        }

        List<String> ids = new ArrayList<>();

        for (ExpStorageProfile dict : deleteData) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpStorageProfile.class, ids);
        return newUpdateDict;
    }

    @Transactional
    public List<ExpStorageProfileVo> saveStorageUpdate(List<ExpStorageProfileVo> updateData) {
        List<ExpStorageProfileVo> newUpdateDict = new ArrayList<>();
        if (updateData.size() > 0) {
            for (ExpStorageProfileVo dict : updateData) {
                String id = dict.getId();
                System.out.println(id);
                ExpStorageProfile expStorageProfile = get(ExpStorageProfile.class, id);
                expStorageProfile.setLowLevel(dict.getLowLevel());
                expStorageProfile.setUpperLevel(dict.getUpperLevel());
                merge(expStorageProfile);
            }
            newUpdateDict.addAll(updateData);
        }
        return newUpdateDict;
    }

    public List<ExpStorageProfile> findExpStockDefine(String expForm, String expCode,String storage) {
        String sql = "SELECT EXP_STORAGE_PROFILE.ID," +
                "         EXP_STORAGE_PROFILE.STORAGE,   \n" +
                "         EXP_DICT.EXP_NAME,   \n" +
                "         EXP_DICT.EXP_FORM,   \n" +
                "         EXP_STORAGE_PROFILE.EXP_CODE,   \n" +
                "         EXP_STORAGE_PROFILE.EXP_SPEC,   \n" +
                "         EXP_STORAGE_PROFILE.UNITS,   \n" +
                "         EXP_STORAGE_PROFILE.AMOUNT_PER_PACKAGE,   \n" +
                "         EXP_STORAGE_PROFILE.PACKAGE_UNITS,   \n" +
                "         EXP_STORAGE_PROFILE.UPPER_LEVEL,   \n" +
                "         EXP_STORAGE_PROFILE.LOW_LEVEL,  \n" +
                "         EXP_STORAGE_PROFILE.LOCATION \n" +
                "    FROM EXP_DICT,   \n" +
                "         EXP_STORAGE_PROFILE  \n" +
                "   WHERE EXP_STORAGE_PROFILE.EXP_CODE = EXP_DICT.EXP_CODE AND \n" +
                "\t\t\tEXP_STORAGE_PROFILE.EXP_SPEC = EXP_DICT.EXP_SPEC AND\n" +
                "\t\t\tEXP_STORAGE_PROFILE.STORAGE = '"+storage+"'   ";
        if (null != expForm && !expForm.trim().equals("全部") ) {
            sql += " AND EXP_DICT.exp_form like '%'||'"+expForm+"'||'%'  ";
        }
        if (null != expCode && !expCode.trim().equals("") ) {
            sql += " AND EXP_STORAGE_PROFILE.exp_code = '"+expCode+"' ";
        }
        List<ExpStorageProfile> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpStorageProfile.class);
        return nativeQuery;
    }
}
