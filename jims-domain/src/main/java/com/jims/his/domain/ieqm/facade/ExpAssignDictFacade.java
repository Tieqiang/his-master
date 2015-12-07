package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.ExpAssignDict;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;
import com.jims.his.domain.ieqm.entity.ExpAssignDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/17.
 */
public class ExpAssignDictFacade extends BaseFacade {
    /**
     * 按名称查询
     * @param name
     * @return
     */
    public List<ExpAssignDict> findAll(String name) {
        String sql = "select * from exp_assign_dict where 1=1";
        if (null != name && !name.trim().equals("")) {
            sql += " and assign_name like '%" + name.trim() + "%'";
        }
        List<ExpAssignDict> nativeQuery = super.createNativeQuery(sql, new ArrayList<Object>(), ExpAssignDict.class);
        return nativeQuery;
    }
    /**
     * 保存内容
     * @param insertData
     * @return
     */
    @Transactional
    public List<ExpAssignDict> saveAssignDict(List<ExpAssignDict> insertData) {

        List<ExpAssignDict> newUpdateDict = new ArrayList<>() ;
        if(insertData.size() >0 ){
            for (ExpAssignDict dict:insertData){
                ExpAssignDict merge = merge(dict);
                newUpdateDict.add(merge) ;
            }
        }
        return newUpdateDict;
    }
    @Transactional
    public List<ExpAssignDict> updateAssignDict( List<ExpAssignDict> updateData) {

        List<ExpAssignDict> newUpdateDict = new ArrayList<>() ;
        if(updateData.size()>0){
            for (ExpAssignDict dict:updateData){
                ExpAssignDict merge = merge(dict);
                newUpdateDict.add(merge) ;

            }
        }
        return newUpdateDict;
    }
    @Transactional
    public List<ExpAssignDict> deleteAssignDict(List<ExpAssignDict> deleteData) {

        List<ExpAssignDict> newUpdateDict = new ArrayList<>() ;
        if(deleteData.size()>0){
            List<String> ids = new ArrayList<>();
            for (ExpAssignDict dict:deleteData){
                ids.add(dict.getId()) ;
            }
            super.removeByStringIds(ExpAssignDict.class,ids);
            newUpdateDict.addAll(deleteData) ;
        }
        return newUpdateDict;
    }
}
