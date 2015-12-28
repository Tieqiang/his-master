package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.*;

import javax.persistence.TypedQuery;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import javax.xml.ws.soap.Addressing;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 科室成本核算
 * Created by heren on 2015/11/30.
 */
public class AcctDeptCostFacade extends BaseFacade {


    public List<AcctDeptCost> listAccctDeptCost(String yearMonth, String hospitalId, String costItemId) {

        String sql = "select b.id,\n" +
                "       b.acct_dept_id,\n" +
                "       b.cost_item_id,\n" +
                "       b.cost,\n" +
                "       b.minus_cost,\n" +
                "       b.memo\n" +
                "  from htca.acct_dept_cost b\n" +
                " where(b.year_month = '" + yearMonth + "')\n" +
                "   and b.hospital_id = '" + hospitalId + "'\n" +
                "   and (b.cost_item_id = '" + costItemId + "')\n" +
                "   and b.fetch_way='录入'" +
                "union\n" +
                "select '' id,\n" +
                "       id acct_dept_id,\n" +
                "       '' cost_item_id,\n" +
                "       null cost,\n" +
                "       null minus_cost,\n" +
                "       '' memo\n" +
                "  from htca.acct_dept_dict\n" +
                " where id not in (select acct_dept_id\n" +
                "                    from htca.acct_dept_cost\n" +
                "                   where year_month = '" + yearMonth + "'\n" +
                "                     and hospital_id = '" + hospitalId + "'\n" +
                "                     and cost_item_id = '" + costItemId + "'" +
                "                     and fetch_way='录入')\n" +
                "                       " +
                "   and hospital_id = '"+hospitalId+"'"+
                "   and end_dept='1'" +
                "   and del_flag <> '0'";

        List<AcctDeptCost> acctDeptCosts = createNativeQuery(sql, new ArrayList<Object>(), AcctDeptCost.class);
        return acctDeptCosts;
    }

    @Transactional
    public void saveDeriectWrite(List<AcctDeptCost> acctDeptCosts) {
        for (AcctDeptCost cost : acctDeptCosts) {
            cost.setFetchWay("录入");
            merge(cost);
        }
    }

    /**
     * 删除AcctDeptCost
     *
     * @param ids
     */
    @Transactional
    public void deleteAcctDeptCost(List<String> ids) {

        //remove(AcctDeptCost.class,ids) ;
        removeByStringIds(AcctDeptCost.class, ids);
    }

    public PageEntity<AcctDeptCost> listAll(String hospitalId, String yearMonth, String page, String rows) {
        PageEntity<AcctDeptCost> pageEntity = new PageEntity<>();
        String hql = "From AcctDeptCost as cost where cost.hospitalId='" + hospitalId + "' and yearMonth='" + yearMonth + "' order by cost.acctDeptId";
        Integer iRow = Integer.parseInt(rows);
        Integer iPage = Integer.parseInt(page);

        TypedQuery<AcctDeptCost> query = createQuery(AcctDeptCost.class, hql, new ArrayList<Object>());
        query.setFirstResult((iPage - 1) * iRow);
        query.setMaxResults(iRow);
        pageEntity.setRows(query.getResultList());
        pageEntity.setTotal(getAcctDetpCostTotals(hospitalId, yearMonth));
        return pageEntity;
    }


    public long getAcctDetpCostTotals(String hospitalId, String yearMonth) {

        String hql = "select count(*) from AcctDeptCost as calc where calc.yearMonth = '" + yearMonth + "' and " +
                "calc.hospitalId = '" + hospitalId + "'";
        return createQuery(Long.class, hql, new ArrayList<Object>()).getResultList().get(0);
    }

    //@Transactional
    public List<AcctDeptCost> fetchCostData(String hospitalId, String yearMonth,String fetchTypeId ) {
        AcctParam acctParam = get(AcctParam.class, fetchTypeId);
        if(acctParam.getParamName().contains("折算")){
            return convertCostData(hospitalId,yearMonth,fetchTypeId) ;
        }

        if(acctParam.getParamName().contains("人力")){
            return oaCostData(hospitalId,yearMonth,fetchTypeId) ;
        }

        if(acctParam.getParamName().contains("固定资产")){
            return equipmentCostData(hospitalId,yearMonth,fetchTypeId) ;
        }
        return null ;
    }

    /**
     * 固定资产使用费
     * @param hospitalId
     * @param yearMonth
     * @param fetchTypeId
     * @return
     */
    private List<AcctDeptCost> equipmentCostData(String hospitalId, String yearMonth, String fetchTypeId) {
        return null;
    }

    /**
     * 人力资源成本
     * @param hospitalId
     * @param yearMonth
     * @param fetchTypeId
     * @return
     */
    private List<AcctDeptCost> oaCostData(String hospitalId, String yearMonth, String fetchTypeId) {
        return null;
    }

    private List<AcctDeptCost> convertCostData(String hospitalId,String yearMonth,String fetchTypeId){
        //计算成本
        AcctParam acctParam = get(AcctParam.class, fetchTypeId);
        String sql = acctParam.getParamSql().replace("${yearMonth}",yearMonth).replace("${hospitalId}",hospitalId) ;
        List<Object[]> resultList = createNativeQuery(sql).getResultList();
        List<AcctDeptCost> acctDeptCosts = new ArrayList<>();
        double inpOrder =0;
        double inpPerform =0;
        double ward=0;
        double outpOrder;
        Double outpPerformRate;
        Double outpWardRate;
        for (Object[] objects : resultList) {
            String hql = "select cost from CostItemDict as cost,AcctReckItemClassDict as income " +
                    "where cost.id=income.costId" +
                    " and income.reckItemCode='" + objects[4] + "' and " +
                    " income.hospitalId='"+hospitalId+"'";

            TypedQuery<CostItemDict> query = createQuery(CostItemDict.class, hql, new ArrayList<Object>());
            List<CostItemDict> costItemDicts = query.getResultList();
            if (costItemDicts.size() > 0) {
                CostItemDict itemDict = costItemDicts.get(0);
                String hql2 = "from AcctReckItemClassDict as income where income.reckItemCode='"+objects[4]+"' and income.hospitalId='"+hospitalId+"'" ;
                TypedQuery<AcctReckItemClassDict> query1 = createQuery(AcctReckItemClassDict.class, hql2, new ArrayList<Object>());
                List<AcctReckItemClassDict> acctReckItemClassDictList = query1.getResultList();
                double calcReate = 100.0;
                if (acctReckItemClassDictList.size() > 0) {
                    AcctReckItemClassDict dict = acctReckItemClassDictList.get(0);
                    Double fixConvert = dict.getFixConvert() ;
                    if(fixConvert==null){
                        calcReate=100.0 ;
                    }else{
                        calcReate = fixConvert ;
                    }
                }
                double totalCost = ((BigDecimal) objects[3]).doubleValue() * (100 - calcReate)/100;
                if(totalCost==0){
                    continue;
                }
                String inpOrOutp = (String) objects[5];
                if ("1".equals(inpOrOutp)) {
                    inpOrder = (itemDict.getInpOrderRate()==null?0:itemDict.getInpOrderRate()) / 100;
                    inpPerform = (itemDict.getInpPerformRate()==null?0:itemDict.getInpPerformRate()) / 100;
                    ward = (itemDict.getInpWardRate()==null?0:itemDict.getInpWardRate()) / 100;

                    if (inpOrder != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setAcctDeptId((String) objects[0]);
                        cost.setCost(totalCost * inpOrder);
                        cost.setMinusCost(0.0);
                        cost.setCostItemId(itemDict.getId());
                        cost.setYearMonth(yearMonth);
                        cost.setFetchWay("计算");
                        acctDeptCosts.add(cost);
                    }
                    if (inpPerform != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setAcctDeptId((String) objects[1]);
                        cost.setCostItemId(itemDict.getId());
                        cost.setCost(totalCost * inpPerform);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        cost.setFetchWay("计算");
                        acctDeptCosts.add(cost);
                    }

                    if (ward != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setCostItemId(itemDict.getId());
                        cost.setAcctDeptId((String) objects[2]);
                        cost.setCost(totalCost * ward);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        cost.setFetchWay("计算");
                        acctDeptCosts.add(cost);
                    }
                } else {
                    outpOrder = (itemDict.getOutpOrderRate()==null?0:itemDict.getOutpOrderRate())/100;
                    outpPerformRate = (itemDict.getOutpPerformRate()==null?0:itemDict.getOutpPerformRate())/100;
                    outpWardRate = (itemDict.getOutpWardRate()==null?0:itemDict.getOutpWardRate())/100;
                    if (outpOrder != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setCostItemId(itemDict.getId());
                        cost.setAcctDeptId((String) objects[0]);
                        cost.setCost(totalCost * outpOrder);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        cost.setFetchWay("计算");
                        acctDeptCosts.add(cost);
                    }
                    if (outpPerformRate != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setCostItemId(itemDict.getId());
                        cost.setHospitalId(hospitalId);
                        cost.setAcctDeptId((String) objects[1]);
                        cost.setCost(totalCost * outpPerformRate);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        cost.setFetchWay("计算");
                        acctDeptCosts.add(cost);
                    }
                    if (outpWardRate != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setCostItemId(itemDict.getId());
                        cost.setAcctDeptId((String) objects[2]);
                        cost.setCost(totalCost * outpWardRate);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        cost.setFetchWay("计算");
                        acctDeptCosts.add(cost);
                    }
                }
            } else {
                continue;
            }
        }

        //for(AcctDeptCost cost:acctDeptCosts){
        //    merge(cost) ;
        //}
        return acctDeptCosts ;
    }

    @Transactional
    public void saveCostData(List<AcctDeptCost> acctDeptCosts){
        if(acctDeptCosts.size()>0){
            AcctDeptCost acctDeptCost = acctDeptCosts.get(0) ;
            String yearMonth = acctDeptCost.getYearMonth() ;
            String hospitalId = acctDeptCost.getHospitalId() ;
            String deleteHql = "delete from AcctDeptCost as cost where cost.hospitalId='"+hospitalId+"' and " +
                    "cost.yearMonth = '"+yearMonth+"' and cost.fetchWay in ('计算','买服务计入')" ;
            this.getEntityManager().createQuery(deleteHql).executeUpdate() ;

            for (AcctDeptCost deptCost:acctDeptCosts){
                merge(deptCost) ;
            }
        }
    }


    /**
     * 计算分摊方法
     * @param hospitalId
     * @param yearMonth
     * @param devideWay
     * @param costItemId
     * @param totalMoney
     * @return
     */
    public List<AcctDeptCost> devideAcctDeptCost(String hospitalId, String yearMonth, String devideWay, String costItemId, Double totalMoney,String depts) {

        if("0".equals(devideWay)){//人员分摊法
            return devideByPerson(hospitalId, yearMonth, costItemId, totalMoney) ;
        }

        if("1".equals(devideWay)){//科室面积分摊方法
            return devideByBuildArea(hospitalId,yearMonth,costItemId,totalMoney) ;
        }
        if("2".equals(devideWay)){
            return devideByEq(hospitalId,yearMonth,costItemId,totalMoney,depts) ;
        }
        return null;
    }

    /**
     * 平均分摊法
     * @param hospitalId
     * @param yearMonth
     * @param costItemId
     * @param totalMoney
     * @param depts
     * @return
     */
    private List<AcctDeptCost> devideByEq(String hospitalId, String yearMonth, String costItemId, Double totalMoney, String depts) {
        String ids[]=depts.split(";") ;
        if(ids.length>0){
            Double perMoney = totalMoney /ids.length ;
            List<AcctDeptCost> costs = new ArrayList<>() ;
            for(String id:ids){
                AcctDeptCost cost = new AcctDeptCost() ;
                cost.setAcctDeptId(id);
                cost.setFetchWay("分摊");
                cost.setHospitalId(hospitalId);
                cost.setCostItemId(costItemId);
                cost.setMinusCost(0.0);
                cost.setYearMonth(yearMonth);
                cost.setCost(perMoney);
                costs.add(cost) ;
            }
            return costs ;
        }
        return null;
    }

    /**
     * 使用面积分摊方法
     * @param hospitalId
     * @param yearMonth
     * @param costItemId
     * @param totalMoney
     * @return
     */
    private List<AcctDeptCost> devideByBuildArea(String hospitalId, String yearMonth, String costItemId, Double totalMoney) {

        String hql = "select sum(dept.buildArea) From AcctDeptDict as dept,CostItemDevideDept as devide  where dept.hospitalId='"+hospitalId+"' and " +
                " dept.id = devide.deptId and devide.costItemId='"+costItemId+"'"  ;
        Double totalAreas = createQuery(Double.class, hql, new ArrayList<Object>()).getSingleResult();//科室总面积
        if(totalAreas==null){
            return null ;
        }
        String deptHql = "select dept From AcctDeptDict as dept ,CostItemDevideDept vsDept where " +
                "dept.id=vsDept.deptId and vsDept.costItemId = '"+costItemId+"'" ;
        List<AcctDeptDict> depts = createQuery(AcctDeptDict.class,deptHql,new ArrayList<Object>()).getResultList() ;
        List<AcctDeptCost> costs = new ArrayList<>() ;

        for(AcctDeptDict deptDict :depts){
            AcctDeptCost cost = new AcctDeptCost();
            cost.setAcctDeptId(deptDict.getId());
            cost.setFetchWay("分摊");
            cost.setHospitalId(hospitalId);
            cost.setCostItemId(costItemId);
            cost.setMinusCost(0.0);
            cost.setYearMonth(yearMonth);
            double area = 0.0 ;
            if(deptDict.getBuildArea() !=null){
                area = deptDict.getBuildArea() ;
            }
            cost.setCost(totalMoney * (area/totalAreas));
            costs.add(cost) ;
        }
        return costs;
    }

    /**
     * 人员分摊法
     * @param hospitalId
     * @param yearMonth
     * @param costItemId
     * @param totalMoney
     * @return
     */
    private List<AcctDeptCost> devideByPerson(String hospitalId, String yearMonth, String costItemId, Double totalMoney) {
        String hql = "select sum(dept.staffNum) From AcctDeptDict as dept,CostItemDevideDept as devide where dept.hospitalId='"+hospitalId+"' " +
                "and dept.id = devide.deptId and devide.costItemId='"+costItemId+"'" ;
        Double staffNums = createQuery(Double.class, hql, new ArrayList<Object>()).getSingleResult();//科室总面积
        String deptHql = "select dept From AcctDeptDict as dept ,CostItemDevideDept vsDept where " +
                "dept.id=vsDept.deptId and vsDept.costItemId = '"+costItemId+"'" ;
        List<AcctDeptDict> depts = createQuery(AcctDeptDict.class,deptHql,new ArrayList<Object>()).getResultList() ;
        List<AcctDeptCost> costs = new ArrayList<>() ;
        if(staffNums==null){
            return null ;
        }
        for(AcctDeptDict deptDict :depts){
            AcctDeptCost cost = new AcctDeptCost();
            cost.setAcctDeptId(deptDict.getId());
            cost.setFetchWay("分摊");
            cost.setHospitalId(hospitalId);
            cost.setCostItemId(costItemId);
            cost.setMinusCost(0.0);
            cost.setYearMonth(yearMonth);
            double v = 0.0 ;
            if(deptDict.getStaffNum() !=null){
                v = deptDict.getStaffNum() ;
            }
            cost.setCost(totalMoney * (v/staffNums));
            if(cost.getCost()!=0){

                costs.add(cost) ;
            }
        }
        return costs;
    }

    @Transactional
    public void saveDevideDeriectWrite(List<AcctDeptCost> acctDeptCosts) {
        if(acctDeptCosts.size()>0){
            String costItemId = acctDeptCosts.get(0).getCostItemId();
            String yearMonth = acctDeptCosts.get(0).getYearMonth() ;
            String hospitalId= acctDeptCosts.get(0).getHospitalId() ;

            String hql ="delete AcctDeptCost as cost where cost.yearMonth ='"+yearMonth+"' " +
                    "and cost.hospitalId='"+hospitalId+"' and cost.costItemId='"+costItemId+"' " +
                    "and cost.fetchWay='分摊'" ;
            getEntityManager().createQuery(hql).executeUpdate() ;

            for(AcctDeptCost cost: acctDeptCosts){
                merge(cost) ;
            }
        }
    }


    /***
     * 记录科室的成本作为本科室的收入
     * @param acctDeptCosts
     * @param incomeDeptId
     */
    @Transactional
    public void saveDeptDevideDeriectWrite(List<AcctDeptCost> acctDeptCosts, String incomeDeptId) {
        if(acctDeptCosts.size()>0){
            String costItemId = acctDeptCosts.get(0).getCostItemId();
            String yearMonth = acctDeptCosts.get(0).getYearMonth() ;
            String hospitalId= acctDeptCosts.get(0).getHospitalId() ;

            String hql ="delete AcctDeptCost as cost where cost.yearMonth ='"+yearMonth+"' " +
                    "and cost.hospitalId='"+hospitalId+"' and cost.costItemId='"+costItemId+"' " +
                    "and cost.fetchWay='分摊'" ;
            getEntityManager().createQuery(hql).executeUpdate() ;

            String hql2 = "delete ServiceDeptIncome as income where income.yearMonth = '"+yearMonth+"' and " +
                    "income.hospitalId='"+hospitalId+"' and income.incomeTypeId='"+costItemId+"' and " +
                    "income.getWay='分摊'" ;
            getEntityManager().createQuery(hql2).executeUpdate() ;


            for(AcctDeptCost cost: acctDeptCosts){
                ServiceDeptIncome serviceDeptIncome=new ServiceDeptIncome() ;
                serviceDeptIncome.setAcctDeptId(incomeDeptId);
                serviceDeptIncome.setYearMonth(yearMonth);
                serviceDeptIncome.setHospitalId(hospitalId);
                serviceDeptIncome.setIncomeTypeId(costItemId);
                serviceDeptIncome.setGetWay("分摊");
                serviceDeptIncome.setTotalIncome(cost.getCost() - cost.getMinusCost());
                serviceDeptIncome.setOperator(cost.getOperator());
                serviceDeptIncome.setOperatorDate(cost.getOperatorDate());
                serviceDeptIncome.setConfirmStatus("0");
                serviceDeptIncome.setOutFlag("1");
                serviceDeptIncome.setServiceForDeptId(cost.getAcctDeptId());
                merge(serviceDeptIncome) ;
                merge(cost) ;
            }
        }
    }
}
