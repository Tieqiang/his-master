package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpCodingRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
public class ExpCodingRuleFacade extends BaseFacade {
    /**
     * 名称模糊查询
     * @param name
     * @return
     */
    public List<ExpCodingRule> findAll(String name) {
        String hql = "from  ExpCodingRule md where 1=1";
        if (null != name && !name.trim().equals("")) {
            hql += " and md.className like '%" + name.trim() + "%'";
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
    public List<ExpCodingRule> saveExpCodingRule(List<ExpCodingRule> insertData, List<ExpCodingRule> updateData, List<ExpCodingRule> deleteData) {

        List<ExpCodingRule> newUpdateDict = new ArrayList<>() ;
        if(insertData.size() >0 ){
            for (ExpCodingRule dict:insertData){
                ExpCodingRule merge = merge(dict);
                newUpdateDict.add(merge) ;
            }
        }

        if(updateData.size()>0){
            for (ExpCodingRule dict:updateData){
                ExpCodingRule merge = merge(dict);
                newUpdateDict.add(merge) ;

            }
        }

        if(deleteData.size()>0){
            List<String> ids = new ArrayList<>();
            for (ExpCodingRule dict:deleteData){
                ids.add(dict.getId()) ;
            }
            super.removeByStringIds(ExpCodingRule.class,ids);
            newUpdateDict.addAll(deleteData) ;
        }
        return newUpdateDict;
    }

}
