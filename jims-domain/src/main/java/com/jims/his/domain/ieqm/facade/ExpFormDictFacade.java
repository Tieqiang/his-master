package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.common.util.PinYin2Abbreviation;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpFormDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
public class ExpFormDictFacade extends BaseFacade {
    /**
     * 名称模糊查询
     * @param name
     * @return
     */
    public List<ExpFormDict> findAll(String name) {
        String hql = "from  ExpFormDict md where 1=1";
        if (null != name && !name.trim().equals("")) {
            hql += " and md.formName like '%" + name.trim() + "%'";
        }
        return entityManager.createQuery(hql).getResultList();
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     */
    @Transactional
    public List<ExpFormDict> save(BeanChangeVo<ExpFormDict> beanChangeVo) {
        List<ExpFormDict> newUpdateDict = new ArrayList<>();
        List<ExpFormDict> inserted = beanChangeVo.getInserted();
        List<ExpFormDict> updated = beanChangeVo.getUpdated();
        List<ExpFormDict> deleted = beanChangeVo.getDeleted();
        for (ExpFormDict dict : inserted) {
            dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getFormName()));
            ExpFormDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ExpFormDict dict : updated) {
            dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getFormName()));
            ExpFormDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ExpFormDict dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpFormDict.class, ids);
        return newUpdateDict;
    }
}
