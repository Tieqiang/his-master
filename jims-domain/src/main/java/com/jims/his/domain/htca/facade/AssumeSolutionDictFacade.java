package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.AssumeSolutionDict;
import com.jims.his.domain.htca.entity.CostGetWayDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/26.
 */
public class AssumeSolutionDictFacade extends BaseFacade {
    /**
     * 保存新增、删除、修改等相关
     * @param beanChangeVo
     */
    @Transactional
    public void saveOrUpdate(BeanChangeVo<AssumeSolutionDict> beanChangeVo) {
        List<AssumeSolutionDict> inserted = beanChangeVo.getInserted();
        List<AssumeSolutionDict> updated = beanChangeVo.getUpdated();
        List<AssumeSolutionDict> deleted = beanChangeVo.getDeleted();

        inserted.addAll(updated) ;
        for(AssumeSolutionDict dict:inserted){
            merge(dict) ;
        }

        List<String> ids= new ArrayList<>() ;
        for(AssumeSolutionDict dict:deleted){
            ids.add(dict.getId())  ;
        }

        removeByStringIds(AssumeSolutionDict.class,ids);
    }
}
