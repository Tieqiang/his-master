package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.CostAttrDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/25.
 */
public class CostAttrDictFacade extends BaseFacade {


    /**
     * 保存新增、删除、修改等相关
     * @param beanChangeVo
     */
    @Transactional
    public void saveOrUpdate(BeanChangeVo<CostAttrDict> beanChangeVo) {
        List<CostAttrDict> inserted = beanChangeVo.getInserted();
        List<CostAttrDict> updated = beanChangeVo.getUpdated();
        List<CostAttrDict> deleted = beanChangeVo.getDeleted();

        inserted.addAll(updated) ;
        for(CostAttrDict dict:inserted){
            merge(dict) ;
        }

        List<String> ids= new ArrayList<>() ;
        for(CostAttrDict dict:deleted){
            ids.add(dict.getId())  ;
        }

        removeByStringIds(CostAttrDict.class,ids);
    }

}

