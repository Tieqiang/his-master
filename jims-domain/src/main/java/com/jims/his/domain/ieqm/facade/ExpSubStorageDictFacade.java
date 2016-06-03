package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpStock;
import com.jims.his.domain.ieqm.entity.ExpSubStorageDict;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangjing on 2015/10/14.
 */
public class ExpSubStorageDictFacade extends BaseFacade {
    private EntityManager entityManager;

    @Inject
    public ExpSubStorageDictFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 根据子库房的subStorage查询子库房列表
     * @param subStorage
     * @return
     */
    public List<ExpSubStorageDict> listExpSubStorageDict(String subStorage) {
        String hql = "from ExpSubStorageDict as dict where 1=1";
        if(subStorage != null && subStorage.trim().length()>0){
            hql+=" and dict.subStorage like '%" + subStorage.trim() +"%'";
        }
        Query query = entityManager.createQuery(hql);
        List resultList = query.getResultList();
        return resultList;
    }

    /**
     *保存对子库房数据列表的增删改操作，对于子库房修改的，同时修改expStock里面相对应的字段subStorage
     * @param expSubStorageDictChangeVo
     */
    @Transactional
    public void mergeExpSubStorageDict(BeanChangeVo<ExpSubStorageDict> expSubStorageDictChangeVo) {
        List<ExpSubStorageDict> inserted = expSubStorageDictChangeVo.getInserted();
        List<ExpSubStorageDict> updated = expSubStorageDictChangeVo.getUpdated();
        List<ExpSubStorageDict> deleted = expSubStorageDictChangeVo.getDeleted();
        for (ExpSubStorageDict dict : inserted) {
            this.merge(dict);
        }

        for (ExpSubStorageDict dict : updated) {
            ExpSubStorageDict old = this.get(ExpSubStorageDict.class, dict.getId());
            //修改子库房的时候，顺便修改expStock里面相对应的字段subStorage
            if (old.getSubStorage() != null && old.getSubStorage().trim().length() > 0) {
                String hql = "from ExpStock as dict where dict.subStorage='" + old.getSubStorage() + "'";
                Query query = entityManager.createQuery(hql);
                List resultList = query.getResultList();
                if (resultList != null && resultList.size() > 0) {
                    Iterator ite = resultList.iterator();
                    while (ite.hasNext()) {
                        ExpStock es = (ExpStock) ite.next();
                        es.setSubStorage(dict.getSubStorage());
                        this.merge(es);
                    }
                }
            }

            this.merge(dict);
        }

        List<String> ids = new ArrayList<>();

        for (ExpSubStorageDict dict : deleted) {
            ids.add(dict.getId());
        }
        this.removeByStringIds(ExpSubStorageDict.class, ids);
    }

    /**
     * 根据库房代码和医院代码查询
     * 某一个医院的某个库房的子库房
     * @param storageCode
     * @param hospitalId
     * @return
     */
    public List<ExpSubStorageDict> listExpSubStorageDict(String storageCode, String hospitalId) {
        String hql = "from ExpSubStorageDict as dict";
        if(storageCode != null && storageCode.trim().length()>0){
            hql+=" where dict.storageCode='" + storageCode + "'";
        }

        if(storageCode != null && storageCode.trim().length()>0){
            hql+=" and dict.hospitalId ='" + hospitalId +"'";
        }

        Query query = entityManager.createQuery(hql);
        List resultList = query.getResultList();
        return resultList;
    }

    /**
     * 根据医院 库存单位查抄某一个固定的自库房
     * @param storage
     * @param subStorage
     * @param hospitalId
     * @return
     */
    public ExpSubStorageDict getSubStorage(String storage, String subStorage, String hospitalId) {
        String hql = "from ExpSubStorageDict as es where es.storageCode = '"+storage+"' and es.subStorage = '"+subStorage+"' and hospitalId = '"+hospitalId+"'" ;
        Query query = entityManager.createQuery(hql);
        List resultList = query.getResultList();
        if(resultList.size()>0){
            return (ExpSubStorageDict) resultList.get(0);
        }
        return null;
    }

    /**
     * chenxy
     * @param storageCode
     * @return
     */
    public ExpSubStorageDict findByStorageCode(String storageCode) {
        String sql="from ExpSubStorageDict where storageCode='"+storageCode+"'";
        return (ExpSubStorageDict)entityManager.createQuery(sql).getSingleResult();
    }
}
