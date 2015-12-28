package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.AcctDeptCost;
import com.jims.his.domain.htca.entity.AcctParam;
import com.jims.his.domain.htca.entity.ServiceDeptIncome;
import com.jims.his.domain.htca.entity.ServiceDeptIncomeRule;
import org.hibernate.engine.query.spi.sql.NativeSQLQueryNonScalarReturn;

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
             if(null !=income.getServiceForDeptId()&& !"".equals(income.getServiceForDeptId())){
                 AcctDeptCost acctDeptCost = new AcctDeptCost() ;
                 acctDeptCost.setCost(income.getTotalIncome());
                 acctDeptCost.setAcctDeptId(income.getServiceForDeptId());
                 acctDeptCost.setYearMonth(income.getYearMonth());
                 acctDeptCost.setHospitalId(income.getHospitalId());
                 acctDeptCost.setMinusCost(0.0);
                 acctDeptCost.setCostItemId(income.getIncomeTypeId());
                 acctDeptCost.setFetchWay("计算计入");
                 acctDeptCost.setOperatorDate(income.getOperatorDate());
                 acctDeptCost.setOperator(income.getOperator());
                 merge(acctDeptCost) ;
             }
             merge(income) ;
         }
    }


    @Transactional
    public List<ServiceDeptIncome> fetchServiceDeptIncome(String hospitalId, String yearMonth, String deptId, String paramId) {
        String hql = "delete ServiceDeptIncome as income where income.yearMonth='"+yearMonth+"' and " +
                "income.hospitalId='"+hospitalId+"' and " +
                "income.getWay='计算计入'" ;
        String hql2 = "delete AcctDeptCost as cost where cost.yearMonth = '"+yearMonth+"' and " +
                "cost.hospitalId='"+hospitalId+"' and  " +
                "cost.fetchWay='计算计入' " ;

        super.getEntityManager().createQuery(hql).executeUpdate() ;
        super.getEntityManager().createQuery(hql2).executeUpdate() ;
        AcctParam acctParam = get(AcctParam.class, paramId);
        String paramSql = acctParam.getParamSql();
        String sql = paramSql.replace("${yearMonth}", yearMonth).replace("${hospitalId}", hospitalId).replace("${deptId}",deptId) ;

        Query nativeQuery = createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        List<ServiceDeptIncome> serviceDeptIncomes = new ArrayList<>() ;
        for(Object[] objects:resultList){
            if(objects[0]==null){
                continue;
            }
            double incomeNum = ((BigDecimal)objects[0]).doubleValue() ;
            ServiceDeptIncome serviceDeptIncome = new ServiceDeptIncome() ;
            serviceDeptIncome.setTotalIncome(incomeNum);
            serviceDeptIncome.setAcctDeptId(deptId);
            serviceDeptIncome.setYearMonth(yearMonth);
            serviceDeptIncome.setHospitalId(hospitalId);
            serviceDeptIncome.setServiceForDeptId((String) objects[1]);
            serviceDeptIncome.setIncomeTypeId((String)objects[2]);
            serviceDeptIncome.setGetWay("按比例提取");
            serviceDeptIncome.setOutFlag("1");
            serviceDeptIncomes.add(serviceDeptIncome)  ;
        }
        return serviceDeptIncomes ;
    }

    @Transactional
    public void delServiceIncome(ServiceDeptIncome serviceDeptIncome) {
        String id = serviceDeptIncome.getId() ;
        ArrayList<String> ids = new ArrayList<>();
        ids.add(id) ;
        removeByStringIds(ServiceDeptIncome.class,ids);
    }


    @Transactional
    public void delServiceIncomes(List<ServiceDeptIncome> serviceDeptIncomes) {
        ArrayList<String> ids = new ArrayList<>();
        for(ServiceDeptIncome serviceDeptIncome :serviceDeptIncomes){
            String id = serviceDeptIncome.getId() ;
            if(id !=null &&!"".equals(id)){
                ids.add(id) ;
            }
        }
        removeByStringIds(ServiceDeptIncome.class,ids);
    }

    /**
     * 根据项目计算所有的项目
     * @param hospitalId
     * @param yearMonth
     * @return
     */
    @Transactional
    public List<ServiceDeptIncome> fetchServiceDeptIncomes(String hospitalId, String yearMonth) {
        String hql = "delete ServiceDeptIncome as income where income.yearMonth='"+yearMonth+"' and " +
                "income.hospitalId='"+hospitalId+"' and " +
                "income.getWay='计算计入'" ;
        String hql2 = "delete AcctDeptCost as cost where cost.yearMonth = '"+yearMonth+"' and " +
                "cost.hospitalId='"+hospitalId+"' and  " +
                "cost.fetchWay='计算计入' " ;
        super.getEntityManager().createQuery(hql2).executeUpdate() ;

        super.getEntityManager().createQuery(hql).executeUpdate() ;
        String ruleQuery = "from ServiceDeptIncomeRule as r where r.hospitalId='"+hospitalId+"'" ;
        List<ServiceDeptIncomeRule> rules = createQuery(ServiceDeptIncomeRule.class,ruleQuery,new ArrayList<Object>()).getResultList() ;
        List<ServiceDeptIncome> serviceDeptIncomes = new ArrayList<>() ;
        for(ServiceDeptIncomeRule rule:rules){
            String paramSql = rule.getRuleSql();
            String de =rule.getDepts() ;
            if(null==de||"".equals(de)){
                continue;
            }
            String[] depts = rule.getDepts().split(";") ;
            for(String dept:depts){
                String sql = paramSql.replace("${yearMonth}", yearMonth).replace("${hospitalId}", hospitalId).replace("${deptId}",dept) ;
                Query nativeQuery = createNativeQuery(sql);
                List<Object[]> resultList = nativeQuery.getResultList();
                double incomeNum =0.0;
                for(Object[] objects:resultList){
                    if(objects[0]==null){
                        continue;
                    }
                    incomeNum = ((BigDecimal)objects[0]).doubleValue() ;
                    ServiceDeptIncome serviceDeptIncome = new ServiceDeptIncome() ;
                    serviceDeptIncome.setTotalIncome(incomeNum);
                    serviceDeptIncome.setAcctDeptId(dept);
                    serviceDeptIncome.setYearMonth(yearMonth);
                    serviceDeptIncome.setHospitalId(hospitalId);
                    serviceDeptIncome.setServiceForDeptId((String) objects[1]);
                    serviceDeptIncome.setIncomeTypeId((String)objects[2]);
                    serviceDeptIncome.setGetWay("计算计入");
                    serviceDeptIncome.setOutFlag("1");
                    serviceDeptIncomes.add(serviceDeptIncome)  ;
                }
            }
        }
        return serviceDeptIncomes ;
    }
}
