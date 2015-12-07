package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/18.
 */
public class ExpStorageDeptFacade extends BaseFacade {

    private EntityManager entityManager ;

    @Inject
    public ExpStorageDeptFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 根据医院查找库房设置
     * @param hospitalId
     * @return
     */
    public List<ExpStorageDept> getByHospitalId(String hospitalId, String inputCode, String name) {
        String sql = "select * from EXP_STORAGE_DEPT exp where exp.hospital_id='"+hospitalId+"'" ;
        if(null != inputCode && !inputCode.trim().equals("")){
            sql +="  and upper(exp.disburse_no_prefix) like upper('" + inputCode + "%')";
        }
        if (null != name && !name.trim().equals("")) {
            sql += "  and exp.storage_name like '%" + name.trim() + "%'";
        }
        return super.createNativeQuery(sql, new ArrayList<Object>(), ExpStorageDept.class);
    }

    /**
     * 保存修改的科室字典
     * @param expStorageDepts
     */
    @Transactional
    public void mergeExpStorageDept(List<ExpStorageDept> expStorageDepts) {

        for (ExpStorageDept dept :expStorageDepts){
            super.merge(dept) ;
        }
    }

    /**
     * 删除库房科室字典
     * @param expStorageDepts
     */
    @Transactional
    public void deleteExpStorageDept(List<ExpStorageDept> expStorageDepts) {
        List<String> ids = new ArrayList<>() ;
        for(ExpStorageDept dept:expStorageDepts){
            ids.add(dept.getId()) ;
        }
        super.removeByStringIds(ExpStorageDept.class,ids);
    }

    /**
     * 通过库房代码、医院Id查找前后缀
     * @param hospitalId
     * @param storageCode
     * @return
     */
    public List<ExpStorageDept> getByStorageCodeHospitalId(String hospitalId, String storageCode) {
        String hql = "from ExpStorageDept as exp where exp.hospitalId='"+hospitalId+"' and exp.storageCode ='"+storageCode+"' " ;
        return entityManager.createQuery(hql).getResultList() ;
    }
}
