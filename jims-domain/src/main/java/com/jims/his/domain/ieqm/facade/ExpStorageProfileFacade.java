package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
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

    //查询库存
    //public List<ExpStockDefineVo> findExpStockDefineByFormDict(){
    //    String hql = "  SELECT a.storage," +
    //            "         b.expName," +
    //            "         a.expCode, " +
    //            "         a.expSpec," +
    //            "         a.units," +
    //            "         a.amountPerPackage," +
    //            "         a.packageUnits," +
    //            "         a.upperLevel," +
    //            "         a.lowLevel," +
    //            "         a.location" +
    //            "    FROM ExpDict a," +
    //            "         ExpStorageProfile b" +
    //            "   WHERE a.expCode = b.expCode and " +
    //            "a.expSpec = b.expSpec and " +
    //            "a.storage = '1502' and " +
    //            "a.expCode like '%' || '1'||'%'";
    //    return entityManager.createQuery(hql).getResultList();
    //}
    //保存
    @Transactional
    public List<ExpStockDefineVo> saveExpStockDefine(List<ExpStockDefineVo> insertData){
        List<ExpStockDefineVo> expStockDefineVoList = new ArrayList<>();
        if (insertData.size() > 0 ){
            for (ExpStockDefineVo expStockDefineVo : insertData){
                ExpStorageProfile expStorageProfile = new ExpStorageProfile();

                expStorageProfile.setAmountPerPackage(expStockDefineVo.getAmountPerPackage());
                expStorageProfile.setExpCode(expStockDefineVo.getExpCode());
                expStorageProfile.setExpSpec(expStockDefineVo.getExpSpec());
                expStorageProfile.setLocation(expStockDefineVo.getLocation());
                expStorageProfile.setLowLevel(expStockDefineVo.getLowLevel());
                expStorageProfile.setPackageUnits(expStockDefineVo.getPackageUnits());
                expStorageProfile.setUpperLevel(expStockDefineVo.getUpperLevel());
                expStorageProfile.setStorage(expStockDefineVo.getStorage());
                expStorageProfile.setUnits(expStockDefineVo.getUnits());

                ExpStorageProfile expStorageProfile1 =merge(expStorageProfile);

                expStockDefineVo.setAmountPerPackage(expStorageProfile1.getAmountPerPackage());
                expStockDefineVo.setExpCode(expStorageProfile1.getExpCode());
                expStockDefineVo.setExpSpec(expStorageProfile1.getExpSpec());
                expStockDefineVo.setLocation(expStorageProfile1.getLocation());
                expStockDefineVo.setLowLevel(expStorageProfile1.getLowLevel());
                expStockDefineVo.setPackageUnits(expStorageProfile1.getPackageUnits());
                expStockDefineVo.setUpperLevel(expStorageProfile1.getUpperLevel());
                expStockDefineVo.setStorage(expStorageProfile1.getStorage());
                expStockDefineVo.setUnits(expStorageProfile1.getUnits());

                expStockDefineVoList.add(expStockDefineVo);
            }
        }
        return expStockDefineVoList;
    }
    @Transactional
    public List<ExpStorageProfileVo> saveStorageUpdate(List<ExpStorageProfileVo> updateData) {
        List<ExpStorageProfileVo> newUpdateDict = new ArrayList<>() ;
        if(updateData.size()>0){
            for (ExpStorageProfileVo dict:updateData){
                String id = dict.getId();
                System.out.println(id);
                ExpStorageProfile expStorageProfile = get(ExpStorageProfile.class, id);
                expStorageProfile.setLowLevel(dict.getLowLevel());
                expStorageProfile.setUpperLevel(dict.getUpperLevel());
                merge(expStorageProfile);
            }
            newUpdateDict.addAll(updateData);
        }
        return newUpdateDict ;
    }

    public List<ExpStockDefineVo> findExpStockDefine(String expForm, String expCode,String storage) {
        String sql = "\n" +
                "  SELECT EXP_STORAGE_PROFILE.STORAGE,   \n" +
                "         EXP_DICT.EXP_NAME,   \n" +
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
        if (null != expForm && !expForm.trim().equals("" ) ) {
            sql += " AND EXP_DICT.exp_form like '%'||'"+expForm+"'||'%'  ";
        }
        if (null != expCode && !expCode.trim().equals("" ) ) {
            sql += " AND EXP_STORAGE_PROFILE.exp_code = '"+expCode+"' ";
        }
        List<ExpStockDefineVo> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpStockDefineVo.class);
        return nativeQuery;
    }
}
