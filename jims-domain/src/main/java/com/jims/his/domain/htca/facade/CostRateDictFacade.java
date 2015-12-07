package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.CostRateDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/26.
 */
public class CostRateDictFacade extends BaseFacade {
    /**
     * 保存新增、删除、修改等相关
     * @param beanChangeVo
     */
    @Transactional
    public void saveOrUpdate(BeanChangeVo<CostRateDict> beanChangeVo) {
        List<CostRateDict> inserted = beanChangeVo.getInserted();
        List<CostRateDict> updated = beanChangeVo.getUpdated();
        List<CostRateDict> deleted = beanChangeVo.getDeleted();

        inserted.addAll(updated) ;
        for(CostRateDict dict:inserted){
            merge(dict) ;
        }

        List<String> ids= new ArrayList<>() ;
        for(CostRateDict dict:deleted){
            ids.add(dict.getId())  ;
        }

        removeByStringIds(CostRateDict.class,ids);
    }
}
