package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
public class ExpImportClassDictFacade extends BaseFacade {

    public List<ExpImportClassDict> findAll(String name) {
        String hql = "from  ExpImportClassDict md where 1=1";
        if (null != name && !name.trim().equals("")) {
            hql += " and md.importClass like '%" + name.trim() + "%'";
        }
        return entityManager.createQuery(hql).getResultList();
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     */
    @Transactional
    public List<ExpImportClassDict> save(BeanChangeVo<ExpImportClassDict> beanChangeVo) {
        List<ExpImportClassDict> newUpdateDict = new ArrayList<>();
        List<ExpImportClassDict> inserted = beanChangeVo.getInserted();
        List<ExpImportClassDict> updated = beanChangeVo.getUpdated();
        List<ExpImportClassDict> deleted = beanChangeVo.getDeleted();
        for (ExpImportClassDict dict : inserted) {
            ExpImportClassDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ExpImportClassDict dict : updated) {
            ExpImportClassDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ExpImportClassDict dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpImportClassDict.class, ids);
        return newUpdateDict;
    }
}
