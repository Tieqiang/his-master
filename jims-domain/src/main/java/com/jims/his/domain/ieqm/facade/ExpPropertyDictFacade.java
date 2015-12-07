package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpFormDict;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;
import com.jims.his.domain.ieqm.entity.ExpPropertyDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/17.
 */
public class ExpPropertyDictFacade extends BaseFacade {
    /**
     * 名称模糊查询
     * @param name
     * @return
     */
    public List<ExpPropertyDict> findAll(String name) {
        String hql = "from  ExpPropertyDict md where 1=1";
        if (null != name && !name.trim().equals("")) {
            hql += " and md.toxiProperty like '%" + name.trim() + "%'";
        }
        return entityManager.createQuery(hql).getResultList();
    }
    /**
     * 保存内容
     * @param insertData
     * @return
     */
    @Transactional
    public List<ExpPropertyDict> savePropertyDict(List<ExpPropertyDict> insertData) {

        List<ExpPropertyDict> newUpdateDict = new ArrayList<>() ;
        if(insertData.size() >0 ){
            for (ExpPropertyDict dict:insertData){
                ExpPropertyDict merge = merge(dict);
                newUpdateDict.add(merge) ;
            }
        }
        return newUpdateDict;
    }
    @Transactional
    public List<ExpPropertyDict> updatePropertyDict( List<ExpPropertyDict> updateData) {

        List<ExpPropertyDict> newUpdateDict = new ArrayList<>() ;
        if(updateData.size()>0){
            for (ExpPropertyDict dict:updateData){
                ExpPropertyDict merge = merge(dict);
                newUpdateDict.add(merge) ;

            }
        }
        return newUpdateDict;
    }
    @Transactional
    public List<ExpPropertyDict> deletePropertyDict(List<ExpPropertyDict> deleteData) {

        List<ExpPropertyDict> newUpdateDict = new ArrayList<>() ;
        if(deleteData.size()>0){
            List<String> ids = new ArrayList<>();
            for (ExpPropertyDict dict:deleteData){
                ids.add(dict.getId()) ;
            }
            super.removeByStringIds(ExpImportClassDict.class,ids);
            newUpdateDict.addAll(deleteData) ;
        }
        return newUpdateDict;
    }
}
