package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;

import com.jims.his.domain.ieqm.entity.ExpStockBack;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Date;
import java.util.ArrayList;

import java.util.List;

/**
 * Created by wangbinbin on .
 */
public class ExpStockBackFacade extends BaseFacade {
    private EntityManager entityManager;

    @Inject
    public ExpStockBackFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    /**
     * 备货保存
     * @param expStockBackChangeVo
     */
    @Transactional
    public void mergeStockBack(BeanChangeVo<ExpStockBack> expStockBackChangeVo) {
        List<ExpStockBack> inserted = expStockBackChangeVo.getInserted();
        List<ExpStockBack> updated = expStockBackChangeVo.getUpdated();
        List<ExpStockBack> deleted = expStockBackChangeVo.getDeleted();
        for (ExpStockBack dict : inserted) {
            this.merge(dict);
        }
        for (ExpStockBack dict : updated) {
            this.merge(dict);
        }
        List<String> ids = new ArrayList<>();
        for (ExpStockBack dict : deleted) {
            ids.add(dict.getId());
        }
        this.removeByStringIds(ExpStockBack.class, ids);
    }

    /**
     * 查询备货情况
     * @param expName
     * @param supplier
     * @param produce
     * @param expireDate
     * @return
     */
    public List<ExpStockBack> listExpStockBack(String expName, String supplier, String produce, String expireDate) {
        String hql = "from ExpStockBack as dict where 1=1";
        if (expName != null && expName.trim().length() > 0) {
            hql += " and dict.expCode like '%" + expName.trim() + "%'";
        }
        if (supplier != null && supplier.trim().length() > 0) {
            hql += " and dict.supplier like '%" + supplier.trim() + "%'";
        }
        if (produce != null && produce.trim().length() > 0) {
            hql += " and dict.produce like '%" + produce.trim() + "%'";
        }
        if (expireDate != null ) {
            hql += " and dict.expireDate <= to_date('"+ expireDate+"','YYYY-mm-dd HH24:MI:SS')";
        }
        Query query = entityManager.createQuery(hql);
        List resultList = query.getResultList();
        return resultList;
    }
}
