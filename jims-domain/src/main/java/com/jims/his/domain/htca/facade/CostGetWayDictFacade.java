package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.CostAttrDict;
import com.jims.his.domain.htca.entity.CostGetWayDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/26.
 */
public class CostGetWayDictFacade extends BaseFacade {
    /**
     * 保存新增、删除、修改等相关
     * @param beanChangeVo
     */
    @Transactional
    public void saveOrUpdate(BeanChangeVo<CostGetWayDict> beanChangeVo) {
        List<CostGetWayDict> inserted = beanChangeVo.getInserted();
        List<CostGetWayDict> updated = beanChangeVo.getUpdated();
        List<CostGetWayDict> deleted = beanChangeVo.getDeleted();

        inserted.addAll(updated) ;
        for(CostGetWayDict dict:inserted){
            merge(dict) ;
        }

        List<String> ids= new ArrayList<>() ;
        for(CostGetWayDict dict:deleted){
            ids.add(dict.getId())  ;
        }

        removeByStringIds(CostGetWayDict.class,ids);
    }
}
