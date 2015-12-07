package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.AcctReckItemClassDict;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by heren on 2015/11/17.
 */
public class AcctReckFacade extends BaseFacade {


    /**
     * 保存收入分类项目
     * @param reckItemClassDicts
     */
    @Transactional
    public void saveReckItemClassDict(List<AcctReckItemClassDict> reckItemClassDicts) {
        for(AcctReckItemClassDict dict:reckItemClassDicts){
            merge(dict) ;
        }
    }

    /**
     * 删除某一项
     * @param id
     * @return
     */
    @Transactional
    public AcctReckItemClassDict delReckItemClassDict(String id) {
        AcctReckItemClassDict dict = get(AcctReckItemClassDict.class, id);
        remove(dict);
        return dict;
    }

    @Transactional
    public void updateCostId(List<AcctReckItemClassDict> acctReckItemClassDicts) {
        for(AcctReckItemClassDict dict:acctReckItemClassDicts){
            if(!"".equals(dict.getId())&&dict.getId()!=null){
                merge(dict) ;
            }
        }
    }
}
