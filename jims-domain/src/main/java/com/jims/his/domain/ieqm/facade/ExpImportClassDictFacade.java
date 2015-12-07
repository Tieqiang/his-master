package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
public class ExpImportClassDictFacade extends BaseFacade {

    public List<ExpImportClassDict> findAll(String name) {
        String hql = "from  ExpImportClassDict md where 1=1";
        if (null != name && !name.trim().equals("")) {
            hql += " and md.importClass like '%" + name.trim() + "%'";
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
    public List<ExpImportClassDict> saveImportClassDict(List<ExpImportClassDict> insertData, List<ExpImportClassDict> updateData, List<ExpImportClassDict> deleteData) {

        List<ExpImportClassDict> newUpdateDict = new ArrayList<>() ;
        if(insertData.size() >0 ){
            for (ExpImportClassDict dict:insertData){
                ExpImportClassDict merge = merge(dict);
                newUpdateDict.add(merge) ;
            }
        }

        if(updateData.size()>0){
            for (ExpImportClassDict dict:updateData){
                ExpImportClassDict merge = merge(dict);
                newUpdateDict.add(merge) ;

            }
        }

        if(deleteData.size()>0){
            List<String> ids = new ArrayList<>();
            for (ExpImportClassDict dict:deleteData){
                ids.add(dict.getId()) ;
            }
            super.removeByStringIds(ExpImportClassDict.class,ids);
            newUpdateDict.addAll(deleteData) ;
        }
        return newUpdateDict;
    }
}
