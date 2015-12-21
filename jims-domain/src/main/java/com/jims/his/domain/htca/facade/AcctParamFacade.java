package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.AcctParam;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/12/2.
 */
public class AcctParamFacade extends BaseFacade {


    @Transactional
    public void saveAcctParam(AcctParam acctParam) {
        merge(acctParam) ;
    }

    @Transactional
    public void delAcctParam(String id) {
        List<String> ids = new ArrayList<>() ;
        ids.add(id) ;
        removeByStringIds(AcctParam.class,ids);
    }

    public List<AcctParam> listAcctByType(String hospitalId, String fetchType) {
        String hql = "from AcctParam as ap where ap.hospitalId='"+hospitalId+"' and " +
                "ap.paramType='"+fetchType+"'" ;

        TypedQuery<AcctParam> query = createQuery(AcctParam.class, hql, new ArrayList<Object>());
        return query.getResultList();
    }
}
