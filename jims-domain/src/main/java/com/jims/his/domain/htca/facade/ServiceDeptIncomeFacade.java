package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.AppConfigerParameter;
import com.jims.his.domain.htca.entity.*;
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
        if (serviceDeptIncomes.size() > 0) {
            String hospitalId = serviceDeptIncomes.get(0).getHospitalId();
            String hql = "from CostItemDict as  dict where dict.hospitalId='" + hospitalId + "'";
            List<CostItemDict> costItemDicts = createQuery(CostItemDict.class, hql, new ArrayList<Object>()).getResultList();
            String lowValueHql = "from AppConfigerParameter as p  where p.appName='HTCA' and p.parameterName = 'EXP_LOW_VALUE_RATE' ";
            String medValueHql = "from AppConfigerParameter as p  where p.appName='HTCA' and p.parameterName = 'EXP_MED_RATE' ";
            String officeValueHql = "from AppConfigerParameter as p  where p.appName='HTCA' and p.parameterName = 'EXP_OFFICE_LEVEL_RATE' ";

            String lowIdHql = "from AppConfigerParameter as p  where p.appName='HTCA' and p.parameterName = 'EXP_LOW_VALUE_ID' ";
            String medIdHql = "from AppConfigerParameter as p  where p.appName='HTCA' and p.parameterName = 'EXP_MED_ID' ";
            String officeIdHql = "from AppConfigerParameter as p  where p.appName='HTCA' and p.parameterName = 'EXP_OFFICE_LEVEL_ID' ";

            //读取提取成本中材料费的成本和计入成参数
            AppConfigerParameter lowParameter = createQuery(AppConfigerParameter.class, lowValueHql, new ArrayList<Object>()).getSingleResult();
            AppConfigerParameter medParamter = createQuery(AppConfigerParameter.class, medValueHql, new ArrayList<Object>()).getSingleResult();
            AppConfigerParameter officeParamter = createQuery(AppConfigerParameter.class, officeValueHql, new ArrayList<Object>()).getSingleResult();
            AppConfigerParameter medIdParamter = createQuery(AppConfigerParameter.class, medIdHql, new ArrayList<Object>()).getSingleResult();
            AppConfigerParameter officeIdParamter = createQuery(AppConfigerParameter.class, officeIdHql, new ArrayList<Object>()).getSingleResult();
            AppConfigerParameter lowIdParamter = createQuery(AppConfigerParameter.class, lowIdHql, new ArrayList<Object>()).getSingleResult();


            Boolean flag = false  ;
            for (ServiceDeptIncome income : serviceDeptIncomes) {
                flag=false ;
                if (null != income.getServiceForDeptId() && !"".equals(income.getServiceForDeptId())) {
                    //首先判断成本是否已经记录如果已经记录则删除以前的成本

                    if (income.getId() == null) {
                        AcctDeptCost acctDeptCost = new AcctDeptCost();
                        //判断是否是材料费如果是材料费，按照不同类型的材料计入一定的成本
                        if (medIdParamter != null) {
                            String paraValue = medIdParamter.getParameterValue();
                            if (paraValue.equals(income.getIncomeTypeId())) {
                                acctDeptCost.setCost(income.getTotalIncome() * Double.parseDouble(medParamter.getParameterValue()));
                                flag = true ;
                            }
                        }
                        if (lowIdParamter != null) {
                            String paraValue = lowIdParamter.getParameterValue();
                            if (paraValue.equals(income.getIncomeTypeId())) {
                                acctDeptCost.setCost(income.getTotalIncome() * Double.parseDouble(lowParameter.getParameterValue()));
                                flag=true ;
                            }
                        }
                        if (officeIdParamter != null) {
                            String paraValue = officeIdParamter.getParameterValue();
                            if (paraValue.equals(income.getIncomeTypeId())) {
                                acctDeptCost.setCost(income.getTotalIncome() * Double.parseDouble(officeParamter.getParameterValue()));
                                flag=true ;
                            }
                        }
                        if(!flag){
                            acctDeptCost.setCost(income.getTotalIncome());
                        }

                        acctDeptCost.setAcctDeptId(income.getServiceForDeptId());
                        acctDeptCost.setYearMonth(income.getYearMonth());
                        acctDeptCost.setHospitalId(income.getHospitalId());
                        double calcRate = getCalcRate(income, costItemDicts);
                        acctDeptCost.setMinusCost(income.getTotalIncome() * (100 - calcRate) / 100);
                        acctDeptCost.setCostItemId(income.getIncomeTypeId());
                        acctDeptCost.setFetchWay(income.getGetWay());
                        acctDeptCost.setOperatorDate(income.getOperatorDate());
                        acctDeptCost.setOperator(income.getOperator());
                        merge(acctDeptCost);
                    }
                }
                merge(income);
            }
        } else {
            return;
        }


    }

    private double getCalcRate(ServiceDeptIncome income, List<CostItemDict> costItemDicts) {
        for (CostItemDict dict : costItemDicts) {
            if (dict.getId().equals(income.getIncomeTypeId())) {
                return Double.parseDouble(dict.getCalcPercent());
            }
        }
        return 0;
    }


    @Transactional
    public List<ServiceDeptIncome> fetchServiceDeptIncome(String hospitalId, String yearMonth, String deptId, String paramId) {
        String hql = "delete ServiceDeptIncome as income where income.yearMonth='" + yearMonth + "' and " +
                "income.hospitalId='" + hospitalId + "' and " +
                "income.getWay='按比例提取'";
        String hql2 = "delete AcctDeptCost as cost where cost.yearMonth = '" + yearMonth + "' and " +
                "cost.hospitalId='" + hospitalId + "' and  " +
                "cost.fetchWay='按比例提取' ";

        super.getEntityManager().createQuery(hql).executeUpdate();
        super.getEntityManager().createQuery(hql2).executeUpdate();
        AcctParam acctParam = get(AcctParam.class, paramId);
        String paramSql = acctParam.getParamSql();
        String sql = paramSql.replace("${yearMonth}", yearMonth).replace("${hospitalId}", hospitalId).replace("${deptId}", deptId);

        Query nativeQuery = createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        List<ServiceDeptIncome> serviceDeptIncomes = new ArrayList<>();
        for (Object[] objects : resultList) {
            if (objects[0] == null) {
                continue;
            }
            double incomeNum = ((BigDecimal) objects[0]).doubleValue();
            ServiceDeptIncome serviceDeptIncome = new ServiceDeptIncome();
            serviceDeptIncome.setTotalIncome(incomeNum);
            serviceDeptIncome.setAcctDeptId(deptId);
            serviceDeptIncome.setYearMonth(yearMonth);
            serviceDeptIncome.setHospitalId(hospitalId);
            serviceDeptIncome.setServiceForDeptId((String) objects[1]);
            serviceDeptIncome.setIncomeTypeId((String) objects[2]);
            serviceDeptIncome.setGetWay("按比例提取");
            serviceDeptIncome.setOutFlag("1");
            serviceDeptIncomes.add(serviceDeptIncome);
        }
        return serviceDeptIncomes;
    }

    @Transactional
    public void delServiceIncome(ServiceDeptIncome serviceDeptIncome) {

        String serviceDeptId = serviceDeptIncome.getServiceForDeptId();
        int i = 0 ;
        if (!"".equals(serviceDeptId) || null != serviceDeptId) {
            String hql = " delete AcctDeptCost as cost where cost.acctDeptId='" + serviceDeptId + "' and " +
                    "cost.hospitalId='" + serviceDeptIncome.getHospitalId() + "' and " +
                    "cost.operator='" + serviceDeptIncome.getOperator() + "' and " +
                    "cost.fetchWay='录入' and cost.yearMonth = '"+serviceDeptIncome.getYearMonth()+"' and cost.costItemId='"+serviceDeptIncome.getIncomeTypeId()+"' ";
            //此处存在问题，如果对一个核算单元录入相同的两个项目则在删除的时候有可能同时删除两个项目
            i = this.getEntityManager().createQuery(hql).executeUpdate();
        }
        String id = serviceDeptIncome.getId();
        ArrayList<String> ids = new ArrayList<>();
        ids.add(id);
        if(i>0){
            removeByStringIds(ServiceDeptIncome.class, ids);
        }
    }


    @Transactional
    public void delServiceIncomes(List<ServiceDeptIncome> serviceDeptIncomes) {
        ArrayList<String> ids = new ArrayList<>();
        for (ServiceDeptIncome serviceDeptIncome : serviceDeptIncomes) {
            String id = serviceDeptIncome.getId();
            if (id != null && !"".equals(id)) {
                ids.add(id);
            }
        }
        removeByStringIds(ServiceDeptIncome.class, ids);
    }

    /**
     * 根据项目计算所有的项目
     *
     * @param hospitalId
     * @param yearMonth
     * @return
     */
    @Transactional
    public List<ServiceDeptIncome> fetchServiceDeptIncomes(String hospitalId, String yearMonth) {
        String hql = "delete ServiceDeptIncome as income where income.yearMonth='" + yearMonth + "' and " +
                "income.hospitalId='" + hospitalId + "' and " +
                "income.getWay='计算计入'";
        String hql2 = "delete AcctDeptCost as cost where cost.yearMonth = '" + yearMonth + "' and " +
                "cost.hospitalId='" + hospitalId + "' and  " +
                "cost.fetchWay='计算计入' ";
        super.getEntityManager().createQuery(hql2).executeUpdate();

        super.getEntityManager().createQuery(hql).executeUpdate();
        String ruleQuery = "from ServiceDeptIncomeRule as r where r.hospitalId='" + hospitalId + "'";
        List<ServiceDeptIncomeRule> rules = createQuery(ServiceDeptIncomeRule.class, ruleQuery, new ArrayList<Object>()).getResultList();
        List<ServiceDeptIncome> serviceDeptIncomes = new ArrayList<>();
        for (ServiceDeptIncomeRule rule : rules) {
            String paramSql = rule.getRuleSql();
            String de = rule.getDepts();
            if (null == de || "".equals(de)) {
                continue;
            }
            String[] depts = rule.getDepts().split(";");
            for (String dept : depts) {
                String sql = paramSql.replace("${yearMonth}", yearMonth).replace("${hospitalId}", hospitalId).replace("${deptId}", dept);
                Query nativeQuery = createNativeQuery(sql);
                List<Object[]> resultList = nativeQuery.getResultList();
                double incomeNum = 0.0;
                for (Object[] objects : resultList) {
                    if (objects[0] == null) {
                        continue;
                    }
                    incomeNum = ((BigDecimal) objects[0]).doubleValue();
                    ServiceDeptIncome serviceDeptIncome = new ServiceDeptIncome();
                    serviceDeptIncome.setTotalIncome(incomeNum);
                    serviceDeptIncome.setAcctDeptId(dept);
                    serviceDeptIncome.setYearMonth(yearMonth);
                    serviceDeptIncome.setHospitalId(hospitalId);
                    serviceDeptIncome.setServiceForDeptId((String) objects[1]);
                    serviceDeptIncome.setIncomeTypeId((String) objects[2]);
                    serviceDeptIncome.setGetWay("计算计入");
                    serviceDeptIncome.setOutFlag("1");
                    serviceDeptIncomes.add(serviceDeptIncome);
                }
            }
        }
        return serviceDeptIncomes;
    }
}
