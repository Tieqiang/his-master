package com.jims.his.domain.common.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.LocalProgramSetting;
import com.jims.his.domain.common.vo.BeanChangeVo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by heren on 2016/5/20.
 */
public class LocalProgramSettingFacade extends BaseFacade {


    @Transactional
    public BeanChangeVo<LocalProgramSetting> saveLocalApp(BeanChangeVo<LocalProgramSetting> localProgramSetting) {
        //添加
        List<LocalProgramSetting> inserts = localProgramSetting.getInserted() ;
        List<LocalProgramSetting> updates = localProgramSetting.getUpdated() ;
        List<LocalProgramSetting> deletes = localProgramSetting.getDeleted() ;

        inserts.addAll(updates) ;
        for(LocalProgramSetting local:inserts){
            merge(local) ;
        }

        List<String> ids = new ArrayList<>() ;
        for(LocalProgramSetting local:deletes){
            ids.add(local.getId()) ;
        }
        removeByStringIds(LocalProgramSetting.class,ids);
        return localProgramSetting;
    }


    public List<LocalProgramSetting> findLocalProgramByLoginId(String loginId) {
        String hql = "from LocalProgramSetting as local where upper(local.loginUser)=upper('"+loginId+"')" ;
        return createQuery(LocalProgramSetting.class,hql,new ArrayList<Object>()).getResultList();
    }

    @Transactional
    public LocalProgramSetting delLocalApp(String id) {
        LocalProgramSetting localProgramSetting = get(LocalProgramSetting.class,id) ;
        remove(localProgramSetting);
        return localProgramSetting;
    }
}
