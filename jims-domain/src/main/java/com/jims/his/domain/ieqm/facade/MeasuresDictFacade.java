package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.ieqm.entity.MeasuresDict;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/18.
 */
public class MeasuresDictFacade extends BaseFacade {

    private EntityManager entityManager;

    @Inject
    public MeasuresDictFacade(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    /**
     * 保存内容
     * @param insertData
     * @return
     */
    @Transactional
    public List<MeasuresDict> saveMeasuresDict(List<MeasuresDict> insertData) {

        List<MeasuresDict> newUpdateDict = new ArrayList<>() ;
        if(insertData.size() >0 ){
            for (MeasuresDict dict:insertData){
                dict.setMeasuresClass("单位");
                MeasuresDict merge = merge(dict);
                newUpdateDict.add(merge) ;
            }
        }
        return newUpdateDict;
    }
    @Transactional
    public List<MeasuresDict> updateMeasuresDict( List<MeasuresDict> updateData) {

        List<MeasuresDict> newUpdateDict = new ArrayList<>() ;
        if(updateData.size()>0){
            for (MeasuresDict dict:updateData){
                MeasuresDict merge = merge(dict);
                newUpdateDict.add(merge) ;

            }
        }
        return newUpdateDict;
    }
    @Transactional
    public List<MeasuresDict> deleteMeasuresDict(List<MeasuresDict> deleteData) {

        List<MeasuresDict> newUpdateDict = new ArrayList<>() ;
        if(deleteData.size()>0){
            List<String> ids = new ArrayList<>();
            for (MeasuresDict dict:deleteData){
                ids.add(dict.getId()) ;
            }
            super.removeByStringIds(MeasuresDict.class,ids);
            newUpdateDict.addAll(deleteData) ;
        }
        return newUpdateDict;
    }
    public List<MeasuresDict> findMeasuresDict(String name){
        String hql = "from  MeasuresDict md where md.measuresClass='单位'";
        if(null != name && !name.trim().equals("")){
             hql += " and md.measuresName like '%" + name.trim() +"%'";
        }
        return entityManager.createQuery(hql).getResultList();
    }

    public List<MeasuresDict> searchByMeasuresName(String measuresName){
        String hql ="from  MeasuresDict md where md.measuresClass='单位' and md.measuresName='"+measuresName+"'";
        return entityManager.createQuery(hql).getResultList();
    }

}
