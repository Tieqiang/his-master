package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpStorageDept;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
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
            sql +="  and upper(exp.disburse_no_prefix) like upper('%" + inputCode + "%')";
        }
        if (null != name && !name.trim().equals("")) {
            sql += "  and exp.storage_name like '%" + name.trim() + "%'";
        }
        return super.createNativeQuery(sql, new ArrayList<Object>(), ExpStorageDept.class);
    }

    /**
     * 保存增删改
     * @param beanChangeVo
     * @return
     */
    @Transactional
    public List<ExpStorageDept> mergeExpStorageDept(BeanChangeVo<ExpStorageDept> beanChangeVo) {
        List<ExpStorageDept> newUpdateDict = new ArrayList<>();
        List<ExpStorageDept> inserted = beanChangeVo.getInserted();
        List<ExpStorageDept> updated = beanChangeVo.getUpdated();
        List<ExpStorageDept> deleted = beanChangeVo.getDeleted();
        for (ExpStorageDept dict : inserted) {
            ExpStorageDept merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ExpStorageDept dict : updated) {
            ExpStorageDept merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ExpStorageDept dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpStorageDept.class, ids);
        return newUpdateDict;
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

    public List<ExpStorageDept> getDeptByHospitalId(String hospitalId,String storageCode) {
        String sql = "select * from EXP_STORAGE_DEPT exp where exp.hospital_id='" + hospitalId + "'" +
                " and exp.storage_level>(select storage_level from EXP_STORAGE_DEPT where storage_code ='"+ storageCode+"')" +
                " and exp.storage_level<(select storage_level+2 from EXP_STORAGE_DEPT where storage_code ='"+ storageCode+"')";

        return super.createNativeQuery(sql, new ArrayList<Object>(), ExpStorageDept.class);
    }

    public List<ExpStorageDept> getDeptUpByHospitalId(String hospitalId, String storageCode) {
        String sql = "select * from EXP_STORAGE_DEPT exp where exp.hospital_id='" + hospitalId + "'" +
                " and exp.storage_level<(select storage_level from EXP_STORAGE_DEPT where storage_code ='" + storageCode + "')" +
                " and exp.storage_level>(select storage_level-2 from EXP_STORAGE_DEPT where storage_code ='" + storageCode + "')";

        return super.createNativeQuery(sql, new ArrayList<Object>(), ExpStorageDept.class);


    }

    public List<ExpStorageDept> findStroageByHospitalId(String hospitalId) {
        String hql = "from ExpStorageDept as dept where dept.hospitalId='"+hospitalId+"'" ;
        return createQuery(ExpStorageDept.class,hql,new ArrayList<Object>()).getResultList() ;
    }

    /**
     * 根据库房代码查询该库房级别
     * @param storageCode 库房代码
     * @param hospitalId  组织机构ID
     * @return
     * @author fengyuguang
     */
    public Integer findLevelByStorageCode(String storageCode,String hospitalId){
        String sql = "select storage_level from jims.exp_storage_dept where storage_code = '" + storageCode + "'\n" +
                "and hospital_id = '" + hospitalId + "'";
        List result = super.createNativeQuery(sql).getResultList();
        return ((BigDecimal) result.get(0)).intValue();
    }
}
