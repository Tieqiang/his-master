package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.CostItemClassDict;
import com.jims.his.domain.htca.entity.CostItemDevideDept;
import com.jims.his.domain.htca.entity.CostItemDict;

import java.util.List;

/**
 * Created by heren on 2015/11/24.
 */
public class CostItemFacade extends BaseFacade {


    @Transactional
    public void saveCostItemClass(CostItemClassDict costItemClassDict) {
        merge(costItemClassDict) ;
    }

    @Transactional
    public void delClassItem(String id) {
        CostItemClassDict costItemClassDict = get(CostItemClassDict.class, id);
        remove(costItemClassDict);
    }

    @Transactional
    public void saveCostItem(CostItemDict costItemDict) {
        merge(costItemDict) ;
    }

    @Transactional
    public CostItemDict deleteCostItemById(String id) {
        String hql = "update AcctReckItemClassDict as dict set dict.costId='' where dict.costId='"+id+"'" ;
        int update = update(hql);
        CostItemDict dict = get(CostItemDict.class, id);
        remove(dict);
        return dict;
    }

    @Transactional
    public void saveCostDevide(List<CostItemDevideDept> costItemDevideDepts,String costId) {
        String hql ="delete from CostItemDevideDept as d where d.costItemId='"+costId+"'" ;
        getEntityManager().createQuery(hql).executeUpdate() ;

        if(costItemDevideDepts.size()>0){

            for(CostItemDevideDept devideDept:costItemDevideDepts){
                merge(devideDept) ;
            }
        }
    }

    @Transactional
    public void saveOrUpdate(BeanChangeVo<CostItemDict> costItemDicts) {
        List<CostItemDict> costs=costItemDicts.getUpdated() ;
        for(CostItemDict dict :costs){
            CostItemDict costItemDict = get(CostItemDict.class,dict.getId()) ;
            if(null !=costItemDict){
                costItemDict.setAddRate(dict.getAddRate());
                merge(costItemDict) ;
            }
        }
    }
}
