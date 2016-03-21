package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpClassDict;
import com.jims.his.domain.ieqm.entity.ExpFundItemDict;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjing on 2015/9/23.
 */
public class ExpClassDictFacade extends BaseFacade {



    private EntityManager entityManager ;

    @Inject
    public ExpClassDictFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     */
    @Transactional
    public List<ExpClassDict> save(BeanChangeVo<ExpClassDict> beanChangeVo) {
        List<ExpClassDict> newUpdateDict = new ArrayList<>();
        List<ExpClassDict> inserted = beanChangeVo.getInserted();
        List<ExpClassDict> updated = beanChangeVo.getUpdated();
        List<ExpClassDict> deleted = beanChangeVo.getDeleted();
        for (ExpClassDict dict : inserted) {
            ExpClassDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ExpClassDict dict : updated) {
            ExpClassDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ExpClassDict dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpClassDict.class, ids);
        return newUpdateDict;
    }

}
