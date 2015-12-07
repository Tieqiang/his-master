package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;
import com.jims.his.domain.ieqm.entity.ExpTenderTypeDict;
import com.jims.his.domain.ieqm.entity.ExpTenderTypeDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/18.
 */
public class ExpTenderTypeDictFacade extends BaseFacade {

    public List<ExpTenderTypeDict> findAll(String name) {
        String sql = "select * from exp_tender_type_dict where 1=1";
        if(null != name && !name.trim().equals("")){
            sql += " and tender_type_name like '%" + name.trim() +"%'";
        }
        List<ExpTenderTypeDict> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpTenderTypeDict.class);
        return nativeQuery;
    }
    /**
     * 保存内容
     * @param insertData
     * @return
     */
    @Transactional
    public List<ExpTenderTypeDict> saveTenderTypeDict(List<ExpTenderTypeDict> insertData) {

        List<ExpTenderTypeDict> newUpdateDict = new ArrayList<>() ;
        if(insertData.size() >0 ){
            for (ExpTenderTypeDict dict:insertData){
                ExpTenderTypeDict merge = merge(dict);
                newUpdateDict.add(merge) ;
            }
        }
        return newUpdateDict;
    }
    @Transactional
    public List<ExpTenderTypeDict> updateTenderTypeDict( List<ExpTenderTypeDict> updateData) {

        List<ExpTenderTypeDict> newUpdateDict = new ArrayList<>() ;
        if(updateData.size()>0){
            for (ExpTenderTypeDict dict:updateData){
                ExpTenderTypeDict merge = merge(dict);
                newUpdateDict.add(merge) ;

            }
        }
        return newUpdateDict;
    }
    @Transactional
    public List<ExpTenderTypeDict> deleteTenderTypeDict(List<ExpTenderTypeDict> deleteData) {

        List<ExpTenderTypeDict> newUpdateDict = new ArrayList<>() ;
        if(deleteData.size()>0){
            List<String> ids = new ArrayList<>();
            for (ExpTenderTypeDict dict:deleteData){
                ids.add(dict.getId()) ;
            }
            super.removeByStringIds(ExpTenderTypeDict.class,ids);
            newUpdateDict.addAll(deleteData) ;
        }
        return newUpdateDict;
    }
}
