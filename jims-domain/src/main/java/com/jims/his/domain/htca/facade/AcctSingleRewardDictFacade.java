package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.AcctSingleRewardDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/25.
 */
public class AcctSingleRewardDictFacade extends BaseFacade {


    /**
     * 保存新增、删除、修改等相关
     * @param beanChangeVo
     */
    @Transactional
    public void saveOrUpdate(BeanChangeVo<AcctSingleRewardDict> beanChangeVo) {
        List<AcctSingleRewardDict> inserted = beanChangeVo.getInserted();
        List<AcctSingleRewardDict> updated = beanChangeVo.getUpdated();
        List<AcctSingleRewardDict> deleted = beanChangeVo.getDeleted();
        inserted.addAll(updated) ;
        for(AcctSingleRewardDict dict:inserted){
            merge(dict) ;
        }
        List<String> ids= new ArrayList<>() ;
        for(AcctSingleRewardDict dict:deleted){
            ids.add(dict.getId())  ;
        }
        removeByStringIds(AcctSingleRewardDict.class,ids);
    }

}

