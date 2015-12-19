package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.CostRateDict;
import com.jims.his.domain.htca.entity.ServiceIncomeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/26.
 */
public class ServiceIncomeTypeFacade extends BaseFacade {
    /**
     * 保存新增、删除、修改等相关
     * @param beanChangeVo
     */
    @Transactional
    public void saveOrUpdate(BeanChangeVo<ServiceIncomeType> beanChangeVo) {
        List<ServiceIncomeType> inserted = beanChangeVo.getInserted();
        List<ServiceIncomeType> updated = beanChangeVo.getUpdated();
        List<ServiceIncomeType> deleted = beanChangeVo.getDeleted();

        inserted.addAll(updated) ;
        for(ServiceIncomeType dict:inserted){
            merge(dict) ;
        }

        List<String> ids= new ArrayList<>() ;
        for(ServiceIncomeType dict:deleted){
            ids.add(dict.getId())  ;
        }

        removeByStringIds(ServiceIncomeType.class,ids);
    }
}
