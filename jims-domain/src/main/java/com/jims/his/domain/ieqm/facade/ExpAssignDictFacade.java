package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.common.util.PinYin2Abbreviation;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpAssignDict;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;
import com.jims.his.domain.ieqm.entity.ExpAssignDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/17.
 */
public class ExpAssignDictFacade extends BaseFacade {
    /**
     * 按名称查询
     *
     * @param name
     * @return
     */
    public List<ExpAssignDict> findAll(String name) {
        String sql = "select * from exp_assign_dict where 1=1";
        if (null != name && !name.trim().equals("")) {
            sql += " and assign_name like '%" + name.trim() + "%'";
        }
        List<ExpAssignDict> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpAssignDict.class);
        return nativeQuery;
    }

    /**
     * 按code查询
     *
     * @param code
     * @return
     */
    public ExpAssignDict findByCode(String code) {
        List<ExpAssignDict> expAssignDicts = createQuery(ExpAssignDict.class, "from ExpAssignDict  where assignCode='" + code + "'", new ArrayList<>()).getResultList();
        if(expAssignDicts.size()>0){
            return expAssignDicts.get(0);
        }
        return null;
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     */
    @Transactional
    public List<ExpAssignDict> save(BeanChangeVo<ExpAssignDict> beanChangeVo) {
        List<ExpAssignDict> newUpdateDict = new ArrayList<>();
        List<ExpAssignDict> inserted = beanChangeVo.getInserted();
        List<ExpAssignDict> updated = beanChangeVo.getUpdated();
        List<ExpAssignDict> deleted = beanChangeVo.getDeleted();
        for (ExpAssignDict dict : inserted) {
            dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getAssignName()));
            ExpAssignDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ExpAssignDict dict : updated) {
            dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getAssignName()));
            ExpAssignDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ExpAssignDict dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpAssignDict.class, ids);
        return newUpdateDict;
    }
}
