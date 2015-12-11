package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.AcctParam;
import com.jims.his.domain.htca.entity.ServiceDeptIncome;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/12/9.
 */
public class ServiceDeptIncomeFacade extends BaseFacade {

    @Transactional
    public void mergeServiceIncome(List<ServiceDeptIncome> serviceDeptIncomes) {
         for(ServiceDeptIncome income:serviceDeptIncomes){
            merge(income) ;
         }
    }


    @Transactional
    public List<ServiceDeptIncome> fetchServiceDeptIncome(String hospitalId, String yearMonth, String deptId, String paramId) {
        String hql = "delete ServiceDeptIncome as income where income.yearMonth='"+yearMonth+"' and " +
                "income.hospitalId='"+hospitalId+"' and " +
                "income.acctDeptId='"+deptId+"' and " +
                "income.incomeTypeId='按比例提取'" ;

        super.getEntityManager().createQuery(hql).executeUpdate() ;

        AcctParam acctParam = get(AcctParam.class, paramId);
        String paramSql = acctParam.getParamSql();
        String sql = paramSql.replace("${yearMonth}", yearMonth).replace("${hospitalId}", hospitalId).replace("${deptId}",deptId) ;



        Query nativeQuery = createNativeQuery(sql);
        Object singleResult = nativeQuery.getSingleResult();

        double incomeNum = ((BigDecimal)singleResult).doubleValue() ;
        ServiceDeptIncome serviceDeptIncome = new ServiceDeptIncome() ;
        serviceDeptIncome.setTotalIncome(incomeNum);
        serviceDeptIncome.setAcctDeptId(deptId);
        serviceDeptIncome.setYearMonth(yearMonth);
        serviceDeptIncome.setHospitalId(hospitalId);
        serviceDeptIncome.setServiceForDeptId("");
        serviceDeptIncome.setIncomeTypeId("按比例提取");
        List<ServiceDeptIncome> serviceDeptIncomes = new ArrayList<>() ;
        serviceDeptIncomes.add(serviceDeptIncome)  ;
        return serviceDeptIncomes;
    }

    @Transactional
    public void delServiceIncome(ServiceDeptIncome serviceDeptIncome) {
        String id = serviceDeptIncome.getId() ;
        ArrayList<String> ids = new ArrayList<>();
        ids.add(id) ;
        removeByStringIds(ServiceDeptIncome.class,ids);
    }
}
