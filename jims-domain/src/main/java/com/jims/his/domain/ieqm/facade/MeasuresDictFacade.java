package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.MeasuresDict;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/18.
 */
public class MeasuresDictFacade extends BaseFacade {

    private EntityManager entityManager;

    @Inject
    public MeasuresDictFacade(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     */
    @Transactional
    public List<MeasuresDict> save(BeanChangeVo<MeasuresDict> beanChangeVo) {
        List<MeasuresDict> newUpdateDict = new ArrayList<>();
        List<MeasuresDict> inserted = beanChangeVo.getInserted();
        List<MeasuresDict> updated = beanChangeVo.getUpdated();
        List<MeasuresDict> deleted = beanChangeVo.getDeleted();
        for (MeasuresDict dict : inserted) {
            dict.setMeasuresClass("单位");
            MeasuresDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (MeasuresDict dict : updated) {
            MeasuresDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (MeasuresDict dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(MeasuresDict.class, ids);
        return newUpdateDict;
    }

    /**
     * 查找
     * @param name
     * @return
     */
    public List<MeasuresDict> findMeasuresDict(String name){
        String hql = "from  MeasuresDict md where md.measuresClass='单位'";
        if(null != name && !name.trim().equals("")){
             hql += " and md.measuresName like '%" + name.trim() +"%'";
        }
        return entityManager.createQuery(hql).getResultList();
    }

    public List<MeasuresDict> searchByMeasuresName(String measuresName){
        String hql ="from  MeasuresDict md where md.measuresClass='单位' and md.measuresName='"+measuresName+"'";
        return entityManager.createQuery(hql).getResultList();
    }

}
