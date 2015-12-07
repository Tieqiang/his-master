package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpFundItemDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
public class ExpFundItemDictFacade extends BaseFacade {

    public List<ExpFundItemDict> findAll(String name){
        String hql = "from  ExpFundItemDict md where 1=1";
        if (null != name && !name.trim().equals("")) {
            hql += " and md.fundItem like '%" + name.trim() + "%'";
        }
        return entityManager.createQuery(hql).getResultList();
    }
    /**
     * 保存、新增、删除、修改的内容
     * @param insertData
     * @param updateData
     * @param deleteData
     * @return
     */
    @Transactional
    public List<ExpFundItemDict> saveExpFundItemDict(List<ExpFundItemDict> insertData, List<ExpFundItemDict> updateData, List<ExpFundItemDict> deleteData) {

        List<ExpFundItemDict> newUpdateDict = new ArrayList<>() ;
        if(insertData.size() >0 ){
            for (ExpFundItemDict dict:insertData){
                ExpFundItemDict merge = merge(dict);
                newUpdateDict.add(merge) ;
            }
        }

        if(updateData.size()>0){
            for (ExpFundItemDict dict:updateData){
                ExpFundItemDict merge = merge(dict);
                newUpdateDict.add(merge) ;

            }
        }

        if(deleteData.size()>0){
            List<String> ids = new ArrayList<>();
            for (ExpFundItemDict dict:deleteData){
                ids.add(dict.getId()) ;
            }
            super.removeByStringIds(ExpFundItemDict.class,ids);
            newUpdateDict.addAll(deleteData) ;
        }
        return newUpdateDict;
    }

}
