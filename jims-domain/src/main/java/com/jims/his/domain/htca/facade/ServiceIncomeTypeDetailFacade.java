package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.ServiceIncomeTypeDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/12/24.
 */
public class ServiceIncomeTypeDetailFacade extends BaseFacade {
    /**
     * 修改明细项目的
     * @param beanChangeVo
     */
    @Transactional
    public void saveOrUpdate(BeanChangeVo<ServiceIncomeTypeDetail> beanChangeVo) {
        for (ServiceIncomeTypeDetail insert : beanChangeVo.getInserted()) {
            merge(insert) ;
        }
        for (ServiceIncomeTypeDetail update : beanChangeVo.getUpdated()) {
            merge(update) ;
        }
        List<String> ids= new ArrayList<>() ;
        for (ServiceIncomeTypeDetail delete : beanChangeVo.getDeleted()) {
            ids.add(delete.getId()) ;
        }
        removeByStringIds(ServiceIncomeTypeDetail.class,ids);
    }
}
