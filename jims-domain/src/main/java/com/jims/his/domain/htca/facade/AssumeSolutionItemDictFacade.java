package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.AssumeSolutionItemDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/26.
 */
public class AssumeSolutionItemDictFacade extends BaseFacade {
    /**
     * 保存新增、删除、修改等相关
     * @param beanChangeVo
     */
    @Transactional
    public void saveOrUpdate(BeanChangeVo<AssumeSolutionItemDict> beanChangeVo) {
        List<AssumeSolutionItemDict> inserted = beanChangeVo.getInserted();
        List<AssumeSolutionItemDict> updated = beanChangeVo.getUpdated();
        List<AssumeSolutionItemDict> deleted = beanChangeVo.getDeleted();

        inserted.addAll(updated) ;
        for(AssumeSolutionItemDict dict:inserted){
            merge(dict) ;
        }

        List<String> ids= new ArrayList<>() ;
        for(AssumeSolutionItemDict dict:deleted){
            ids.add(dict.getId())  ;
        }

        removeByStringIds(AssumeSolutionItemDict.class,ids);
    }
}
