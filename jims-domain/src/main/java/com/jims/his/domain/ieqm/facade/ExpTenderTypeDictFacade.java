package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;
import com.jims.his.domain.ieqm.entity.ExpTenderTypeDict;
import com.jims.his.domain.ieqm.entity.ExpTenderTypeDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/18.
 */
public class ExpTenderTypeDictFacade extends BaseFacade {

    public List<ExpTenderTypeDict> findAll(String name) {
        String sql = "select * from exp_tender_type_dict where 1=1";
        if(null != name && !name.trim().equals("")){
            sql += " and tender_type_name like '%" + name.trim() +"%'";
        }
        List<ExpTenderTypeDict> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpTenderTypeDict.class);
        return nativeQuery;
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     */
    @Transactional
    public List<ExpTenderTypeDict> save(BeanChangeVo<ExpTenderTypeDict> beanChangeVo) {
        List<ExpTenderTypeDict> newUpdateDict = new ArrayList<>();
        List<ExpTenderTypeDict> inserted = beanChangeVo.getInserted();
        List<ExpTenderTypeDict> updated = beanChangeVo.getUpdated();
        List<ExpTenderTypeDict> deleted = beanChangeVo.getDeleted();
        for (ExpTenderTypeDict dict : inserted) {
            ExpTenderTypeDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        for (ExpTenderTypeDict dict : updated) {
            ExpTenderTypeDict merge = merge(dict);
            newUpdateDict.add(merge);
        }

        List<String> ids = new ArrayList<>();

        for (ExpTenderTypeDict dict : deleted) {
            ids.add(dict.getId());
            newUpdateDict.add(dict);
        }
        this.removeByStringIds(ExpTenderTypeDict.class, ids);
        return newUpdateDict;
    }
}
