package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpFundItemDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
public class ExpFundItemDictFacade extends BaseFacade {

    public List<ExpFundItemDict> findAll(String name){
        String hql = "from  ExpFundItemDict md where 1=1";
        if (null != name && !name.trim().equals("")) {
            hql += " and md.fundItem like '%" + name.trim() + "%'";
        }
        return entityManager.createQuery(hql).getResultList();
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     */
    @Transactional
    public List<ExpFundItemDict> save(BeanChangeVo<ExpFundItemDict> beanChangeVo) {
        List<ExpFundItemDict> newUpdateDict = new ArrayList<>();
        List<ExpFundItemDict> inserted = beanChangeVo.getInserted();
        List<ExpFundItemDict> updated = beanChangeVo.getUpdated();
        List<ExpFundItemDict> deleted = beanChangeVo.getDeleted();
        for (ExpFundItemDict dict : inserted) {
            ExpFundItemDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ExpFundItemDict dict : updated) {
            ExpFundItemDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ExpFundItemDict dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpFundItemDict.class, ids);
        return newUpdateDict;
    }

}
