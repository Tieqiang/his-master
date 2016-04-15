package com.jims.his.domain.common.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.ReportDict;
import com.jims.his.domain.common.vo.BeanChangeVo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjing on 2016/1/14.
 */
public class ReportDictFacade extends BaseFacade {

    private EntityManager entityManager ;

    @Inject
    public ReportDictFacade(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 通过hospitalId查询当前医院的报表路径信息
     * @param hospitalId
     * @return
     */
    public ReportDict getByHospitalId(String hospitalId){
        String sql = " from ReportDict a where a.hospitalId = '"+hospitalId+"' ";
        return createQuery(ReportDict.class, sql, new ArrayList<Object>()).getResultList().get(0);
    }
    /**
     * 保存增删改
     * @param beanChangeVo
     * @return
     */
    @Transactional
    public List<ReportDict> mergeReportDept(BeanChangeVo<ReportDict> beanChangeVo) {
        List<ReportDict> newUpdateDict = new ArrayList<>();
        List<ReportDict> inserted = beanChangeVo.getInserted();
        List<ReportDict> updated = beanChangeVo.getUpdated();
        List<ReportDict> deleted = beanChangeVo.getDeleted();
        for (ReportDict dict : inserted) {
            ReportDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ReportDict dict : updated) {
            ReportDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ReportDict dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ReportDict.class, ids);
        return newUpdateDict;
    }

}
