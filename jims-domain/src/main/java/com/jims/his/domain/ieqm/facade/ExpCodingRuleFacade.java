package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpCodingRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
public class ExpCodingRuleFacade extends BaseFacade {

    /**
     * 根据编码级别查找数据
     * @param codeLevel 编码级别
     * @return
     * @author fyg
     */
    public List<ExpCodingRule> findByCodeLevel(String codeLevel){
        String hql = "from ExpCodingRule md where 1=1";
        if(null != codeLevel && !codeLevel.trim().equals("")){
            hql += "and md.codeLevel=" + codeLevel;
        }
        return entityManager.createQuery(hql).getResultList();
    }

    /**
     * 名称模糊查询
     * @param name
     * @return
     */
    public List<ExpCodingRule> findAll(String name) {
        String hql = "from  ExpCodingRule md where 1=1";
        if (null != name && !name.trim().equals("")) {
            hql += " and md.className like '%" + name.trim() + "%'";
        }
        return entityManager.createQuery(hql).getResultList();
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     */
    @Transactional
    public List<ExpCodingRule> save(BeanChangeVo<ExpCodingRule> beanChangeVo) {
        List<ExpCodingRule> newUpdateDict = new ArrayList<>();
        List<ExpCodingRule> inserted = beanChangeVo.getInserted();
        List<ExpCodingRule> updated = beanChangeVo.getUpdated();
        List<ExpCodingRule> deleted = beanChangeVo.getDeleted();
        for (ExpCodingRule dict : inserted) {
            ExpCodingRule merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ExpCodingRule dict : updated) {
            ExpCodingRule merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ExpCodingRule dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpCodingRule.class, ids);
        return newUpdateDict;
    }

}
