package com.jims.his.domain.ieqm.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpExportClassDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
public class ExpExportClassDictFacade extends BaseFacade {

    /**
     * 保存、新增、删除、修改的内容
     * @param beanChangeVo
     * @return
     */
    @Transactional
    public List<ExpExportClassDict> save(BeanChangeVo<ExpExportClassDict> beanChangeVo) {
        List<ExpExportClassDict> newUpdateDict = new ArrayList<>();
        List<ExpExportClassDict> inserted = beanChangeVo.getInserted();
        List<ExpExportClassDict> updated = beanChangeVo.getUpdated();
        List<ExpExportClassDict> deleted = beanChangeVo.getDeleted();
        if(inserted.size() >0 ){
            for (ExpExportClassDict dict: inserted){
                ExpExportClassDict merge = merge(dict);
                newUpdateDict.add(merge) ;
            }
        }

        if(updated.size()>0){
            for (ExpExportClassDict dict: updated){
                ExpExportClassDict merge = merge(dict);
                newUpdateDict.add(merge) ;

            }
        }

        if(deleted.size()>0){
            List<String> ids = new ArrayList<>();
            for (ExpExportClassDict dict: deleted){
                ids.add(dict.getId()) ;
            }
            super.removeByStringIds(ExpExportClassDict.class,ids);
            newUpdateDict.addAll(deleted) ;
        }
        return newUpdateDict;
    }

}
