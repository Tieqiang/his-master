package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.common.util.PinYin2Abbreviation;
import com.jims.his.domain.ieqm.entity.ExpFormDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
public class ExpFormDictFacade extends BaseFacade {
    /**
     * 名称模糊查询
     * @param name
     * @return
     */
    public List<ExpFormDict> findAll(String name) {
        String hql = "from  ExpFormDict md where 1=1";
        if (null != name && !name.trim().equals("")) {
            hql += " and md.formName like '%" + name.trim() + "%'";
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
    public List<ExpFormDict> saveExpFormDict(List<ExpFormDict> insertData, List<ExpFormDict> updateData, List<ExpFormDict> deleteData) {

        List<ExpFormDict> newUpdateDict = new ArrayList<>() ;
        if(insertData.size() >0 ){
            for (ExpFormDict dict:insertData){
                dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getFormName()));
                ExpFormDict merge = merge(dict);
                newUpdateDict.add(merge) ;
            }
        }

        if(updateData.size()>0){
            for (ExpFormDict dict:updateData){
                dict.setInputCode(PinYin2Abbreviation.cn2py(dict.getFormName()));
                ExpFormDict merge = merge(dict);
                newUpdateDict.add(merge) ;

            }
        }

        if(deleteData.size()>0){
            List<String> ids = new ArrayList<>();
            for (ExpFormDict dict:deleteData){
                ids.add(dict.getId()) ;
            }
            super.removeByStringIds(ExpFormDict.class,ids);
            newUpdateDict.addAll(deleteData) ;
        }
        return newUpdateDict;
    }

}
