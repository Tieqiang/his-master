package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.ServiceDeptIncomeRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/12/18.
 */
public class ServiceDeptIncomeRuleFacade extends BaseFacade {

    /**
     * 修改
     * @param acctParam
     */
    @Transactional
    public void saveServiceDeptIncomeRule(ServiceDeptIncomeRule acctParam) {
        merge(acctParam) ;
    }

    @Transactional
    public void delServiceDeptIncomeRule(String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id) ;
        removeByStringIds(ServiceDeptIncomeRule.class,ids);
    }


    public List<ServiceDeptIncomeRule> listAcctByType(String hospitalId, String ruleName) {
        return null;
    }
}
