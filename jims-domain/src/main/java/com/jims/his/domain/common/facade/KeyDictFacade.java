package com.jims.his.domain.common.facade;

import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.KeyDict;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2016/7/14.
 */
public class KeyDictFacade extends BaseFacade {


    public KeyDict findByKey(String key) {
        String hql = "from KeyDict as key where keyName = '"+key+"'" ;
        List<KeyDict>  keyDicts = createQuery(KeyDict.class,hql,new ArrayList<Object>()).getResultList() ;
        if(keyDicts.size()>0){
            return keyDicts.get(0);
        }else{
            return null ;
        }
    }
}
