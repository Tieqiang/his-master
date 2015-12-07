package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.IncomeTypeDict;

import java.util.ArrayList;
import java.util.List;

/**
 * 收入分类项目
 * Created by heren on 2015/10/28.
 */
public class IncomeTypeDictFacade extends BaseFacade {

    /**
     * 收入分类
     * @param hospitalId
     * @return
     */
    public List<IncomeTypeDict> findByHospitalId(String hospitalId) {

        String hql = "from IncomeTypeDict as income where income.hospitalId = '"+hospitalId+"'" ;
        List<IncomeTypeDict> resultList = createQuery(IncomeTypeDict.class, hql, new ArrayList<Object>()).getResultList();
        return resultList ;
    }


    /**
     * 更改IncomeTypeDict
     * @param incomeTypeDicts
     */
    @Transactional
    public void saveIncomeTypeDict(BeanChangeVo<IncomeTypeDict> incomeTypeDicts){
        List<IncomeTypeDict> inserted = incomeTypeDicts.getInserted();
        List<IncomeTypeDict> updated = incomeTypeDicts.getUpdated();
        List<IncomeTypeDict> deleted = incomeTypeDicts.getDeleted();
        inserted.addAll(updated) ;
        for(IncomeTypeDict dict:inserted){
            merge(dict) ;
        }
        List<String> ids = new ArrayList<>() ;
        for(IncomeTypeDict dict:deleted){
            ids.add(dict.getId()) ;
        }

        if(ids.size()>0){
            removeByStringIds(IncomeTypeDict.class,ids);
        }
    }

}
