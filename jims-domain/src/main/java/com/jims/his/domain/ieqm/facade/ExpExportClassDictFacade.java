package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpExportClassDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
public class ExpExportClassDictFacade extends BaseFacade {

    /**
     * 保存、新增、删除、修改的内容
     * @param insertData
     * @param updateData
     * @param deleteData
     * @return
     */
    @Transactional
    public List<ExpExportClassDict> saveExportClassDict(List<ExpExportClassDict> insertData, List<ExpExportClassDict> updateData, List<ExpExportClassDict> deleteData) {

        List<ExpExportClassDict> newUpdateDict = new ArrayList<>() ;
        if(insertData.size() >0 ){
            for (ExpExportClassDict dict:insertData){
                ExpExportClassDict merge = merge(dict);
                newUpdateDict.add(merge) ;
            }
        }

        if(updateData.size()>0){
            for (ExpExportClassDict dict:updateData){
                ExpExportClassDict merge = merge(dict);
                newUpdateDict.add(merge) ;

            }
        }

        if(deleteData.size()>0){
            List<String> ids = new ArrayList<>();
            for (ExpExportClassDict dict:deleteData){
                ids.add(dict.getId()) ;
            }
            super.removeByStringIds(ExpExportClassDict.class,ids);
            newUpdateDict.addAll(deleteData) ;
        }
        return newUpdateDict;
    }

}
