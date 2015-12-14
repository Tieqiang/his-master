package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.common.util.PinYin2Abbreviation;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;
import com.jims.his.domain.ieqm.entity.ExpPropertyDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/17.
 */
public class ExpPropertyDictFacade extends BaseFacade {
    /**
     * 名称模糊查询
     * @param name
     * @return
     */
    public List<ExpPropertyDict> findAll(String name) {
        String hql = "from  ExpPropertyDict md where 1=1";
        if (null != name && !name.trim().equals("")) {
            hql += " and md.toxiProperty like '%" + name.trim() + "%'";
        }
        return entityManager.createQuery(hql).getResultList();
    }

    /**
     * 保存增删改
     * @param beanChangeVo
     */
    @Transactional
    public List<ExpPropertyDict> save(BeanChangeVo<ExpPropertyDict> beanChangeVo) {
        List<ExpPropertyDict> newUpdateDict = new ArrayList<>();
        List<ExpPropertyDict> inserted = beanChangeVo.getInserted();
        List<ExpPropertyDict> updated = beanChangeVo.getUpdated();
        List<ExpPropertyDict> deleted = beanChangeVo.getDeleted();
        for (ExpPropertyDict dict : inserted) {
            //dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getToxiProperty()));
            ExpPropertyDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ExpPropertyDict dict : updated) {
            //dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getToxiProperty()));
            ExpPropertyDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ExpPropertyDict dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpPropertyDict.class, ids);
        return newUpdateDict;
    }
}
