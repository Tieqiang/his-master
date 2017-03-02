package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.AppConfigerParameter;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.*;

import javax.persistence.TypedQuery;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import javax.xml.ws.soap.Addressing;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 科室成本核算
 * Created by heren on 2015/11/30.
 */
public class AcctDeptCostFacade extends BaseFacade {


    public List<AcctDeptCost> listAccctDeptCost(String yearMonth, String hospitalId, String costItemId) {

        String sql = "select a.*\n" +
                "  from (select b.id,\n" +
                "               b.acct_dept_id,\n" +
                "               b.cost_item_id,\n" +
                "               b.cost,\n" +
                "               b.minus_cost,\n" +
                "               b.memo\n" +
                "          from htca.acct_dept_cost b\n" +
                "         where (b.year_month = '" + yearMonth + "')\n" +
                "           and b.hospital_id = '" + hospitalId + "'\n" +
                "           and (b.cost_item_id = '" + costItemId + "')\n" +
                "           and b.fetch_way = '录入'\n" +
                "           and b.acct_dept_id in (select id\n" +
                "                                    from htca.acct_dept_dict\n" +
                "                                   where end_dept = '1'\n" +
                "                                     and del_flag = '1')\n" +
                "        union\n" +
                "        select '' id,\n" +
                "               id acct_dept_id,\n" +
                "               '' cost_item_id,\n" +
                "               null cost,\n" +
                "               null minus_cost,\n" +
                "               '' memo\n" +
                "          from htca.acct_dept_dict\n" +
                "         where id not in\n" +
                "               (select acct_dept_id\n" +
                "                  from htca.acct_dept_cost\n" +
                "                 where year_month = '" + yearMonth + "'\n" +
                "                   and hospital_id = '" + hospitalId + "'\n" +
                "                   and cost_item_id = '" + costItemId + "'\n" +
                "                   and fetch_way = '录入')\n" +
                "           and hospital_id = '4028862d4fcf2590014fcf9aef480016'\n" +
                "           and end_dept = '1'\n" +
                "           and del_flag <> '0') a,\n" +
                "       htca.acct_dept_dict c\n" +
                " where a.acct_dept_id = c.id\n" +
                " order by c.position";

        List<AcctDeptCost> acctDeptCosts = createNativeQuery(sql, new ArrayList<Object>(), AcctDeptCost.class);
        return acctDeptCosts;
    }

    @Transactional
    public void saveDeriectWrite(List<AcctDeptCost> acctDeptCosts) {
        for (AcctDeptCost cost : acctDeptCosts) {
            String hql ="from CostItemDict as cid where id='"+cost.getCostItemId()+"'";
            String acctDeptDictHql = "from AcctDeptDict as add where add.id='"+cost.getAcctDeptId()+"'" ;
            AcctDeptDict acctDeptDict = createQuery(AcctDeptDict.class,acctDeptDictHql,new ArrayList<Object>()).getSingleResult();
            CostItemDict singleResult = createQuery(CostItemDict.class, hql, new ArrayList<Object>()).getSingleResult();

            if(singleResult!=null&&acctDeptDict!=null&&"0".equals(acctDeptDict.getStandardFlag())){
                //如果获取成本项目,并且使非成熟科室
                double calcPercent = Double.parseDouble(singleResult.getCalcPercent()) ;
                cost.setMinusCost(cost.getCost()*(100-calcPercent)/100);
            }else{
                //不满足以上条件，则不计入减免。减免为0.0
                cost.setMinusCost(0.0);
            }
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
    public List<AcctDeptCost> fetchCostData(String hospitalId, String yearMonth, String fetchTypeId) {
        AcctParam acctParam = get(AcctParam.class, fetchTypeId);
        if (acctParam.getParamName().contains("折算")) {
            return convertCostData(hospitalId, yearMonth, fetchTypeId);
        }

        if (acctParam.getParamName().contains("人力")) {
            return oaCostData(hospitalId, yearMonth, fetchTypeId);
        }

        if (acctParam.getParamName().contains("固定资产")) {
            return equipmentCostData(hospitalId, yearMonth, fetchTypeId);
        }
        return null;
    }

    /**
     * 固定资产使用费
     *
     * @param hospitalId
     * @param yearMonth
     * @param fetchTypeId
     * @return
     */
    private List<AcctDeptCost> equipmentCostData(String hospitalId, String yearMonth, String fetchTypeId) {
        AcctParam acctParam = get(AcctParam.class, fetchTypeId);
        String sql = acctParam.getParamSql().replace("${yearMonth}", yearMonth).replace("${hospitalId}", hospitalId);
        List<Object[]> resultList = createNativeQuery(sql).getResultList();
        List<AcctDeptCost> acctDeptCosts = new ArrayList<>();//获取核算单元成本
        String hql3 = "from CostItemDict as dict where dict.id='4028803e51ec9c250151ed1ecfd10001'";
        CostItemDict costItemDict = createQuery(CostItemDict.class, hql3, new ArrayList<Object>()).getSingleResult();
        Double inRate = 100.0;
        if (costItemDict != null) {
            inRate = Double.parseDouble(costItemDict.getCalcPercent());
        }
        for (Object[] obj : resultList) {

            AcctDeptCost acctDeptCost = new AcctDeptCost();
            acctDeptCost.setAcctDeptId((String) obj[0]);
            acctDeptCost.setCost(((BigDecimal) obj[1]).doubleValue());
            acctDeptCost.setCostItemId("4028803e51ec9c250151ed1ecfd10001");
            acctDeptCost.setFetchWay("计算");
            acctDeptCost.setHospitalId(hospitalId);
            acctDeptCost.setYearMonth(yearMonth);
            AcctDeptDict acctDeptDict = get(AcctDeptDict.class, acctDeptCost.getAcctDeptId());
            if ( acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                acctDeptCost.setMinusCost(acctDeptCost.getCost() * (100 - inRate) / 100);
            } else {
                acctDeptCost.setMinusCost(0.0);
            }
            acctDeptCosts.add(acctDeptCost);
        }
        return acctDeptCosts;
    }

    /**
     * 人力资源成本
     *
     * @param hospitalId
     * @param yearMonth
     * @param fetchTypeId
     * @return
     */
    private List<AcctDeptCost> oaCostData(String hospitalId, String yearMonth, String fetchTypeId) {
        return null;
    }


    private List<AcctDeptCost> convertCostData(String hospitalId, String yearMonth, String fetchTypeId) {
        //计算成本
        AcctParam acctParam = get(AcctParam.class, fetchTypeId);
        String sql = acctParam.getParamSql().replace("${yearMonth}", yearMonth).replace("${hospitalId}", hospitalId);
        List<Object[]> resultList = createNativeQuery(sql).getResultList();
        List<AcctDeptCost> acctDeptCosts = new ArrayList<>();
        //读取参数获取门诊护理单元
        String outpWardHql = "from AppConfigerParameter as config where config.appName='HTCA' and config.parameterName='OUTP_WARD_ID'";
        AppConfigerParameter outpWardParameter = createQuery(AppConfigerParameter.class, outpWardHql, new ArrayList<Object>()).getSingleResult();
        double inpOrder = 0;
        double inpPerform = 0;
        double ward = 0;
        double outpOrder;
        Double outpPerformRate;
        Double outpWardRate;
        double t = 0.0;

        String emgWard = outpWardParameter.getParameterValue();
        for (Object[] objects : resultList) {
            String hql = "select cost from CostItemDict as cost,AcctReckItemClassDict as income " +
                    "where cost.id=income.costId" +
                    " and income.reckItemCode='" + objects[4] + "' and " +
                    " income.hospitalId='" + hospitalId + "'";
            List<CostItemDict> costItemDicts = createQuery(CostItemDict.class, hql, new ArrayList<Object>()).getResultList();

            //20170302 解决收入细项分割比例与成本分割比例不同造成的Bug
            String incomeItemDictHql = "from IncomeItemDict as income where income.priceItemCode='"+objects[6]+"'" ;
            List<IncomeItemDict> incomeItemDicts = createQuery(IncomeItemDict.class,incomeItemDictHql,new ArrayList<Object>()).getResultList() ;

            if (costItemDicts.size() > 0) {
                CostItemDict itemDict = costItemDicts.get(0);
                String hql2 = "from AcctReckItemClassDict as income where income.reckItemCode='" + objects[4] + "' and income.hospitalId='" + hospitalId + "'";
                TypedQuery<AcctReckItemClassDict> query1 = createQuery(AcctReckItemClassDict.class, hql2, new ArrayList<Object>());
                List<AcctReckItemClassDict> acctReckItemClassDictList = query1.getResultList();
                double calcReate = 100.0;
                if (acctReckItemClassDictList.size() > 0) {
                    AcctReckItemClassDict dict = acctReckItemClassDictList.get(0);
                    Double fixConvert = dict.getFixConvert();
                    if (fixConvert == null) {
                        calcReate = 100.0;
                    } else {
                        calcReate = fixConvert;
                    }

                }
                double totalCost = ((BigDecimal) objects[3]).doubleValue() * (100 - calcReate) / 100;

                if (totalCost == 0) {
                    continue;
                }
                String inpOrOutp = (String) objects[5];
                if ("1".equals(inpOrOutp)) {
                    if(incomeItemDicts!=null&&incomeItemDicts.size()>0){
                        IncomeItemDict incomeItemDict = incomeItemDicts.get(0);
                        inpOrder = (incomeItemDict.getInpOrderedBy()== null ? 0 : Double.parseDouble(incomeItemDict.getInpOrderedBy())) / 100;
                        inpPerform = (incomeItemDict.getInpPerformedBy()== null ? 0 : Double.parseDouble(incomeItemDict.getInpPerformedBy())) / 100;
                        ward = (incomeItemDict.getInpWardCode()== null ? 0 : Double.parseDouble(incomeItemDict.getInpWardCode())) / 100;
                    }else{
                        inpOrder = (itemDict.getInpOrderRate() == null ? 0 : itemDict.getInpOrderRate()) / 100;
                        inpPerform = (itemDict.getInpPerformRate() == null ? 0 : itemDict.getInpPerformRate()) / 100;
                        ward = (itemDict.getInpWardRate() == null ? 0 : itemDict.getInpWardRate()) / 100;
                    }


                    if (inpOrder != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setAcctDeptId((String) objects[0]);
                        cost.setCost(totalCost * inpOrder);
                        double calcPercent = 0.0;
                        AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
                        if (itemDict.getCalcPercent() != null && acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                            calcPercent = Double.parseDouble(itemDict.getCalcPercent());
                            cost.setMinusCost(totalCost * inpOrder * (100 - calcPercent) / 100);
                        } else {
                            cost.setMinusCost(0.0);
                        }
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
                        double calcPercent = 0.0;
                        AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
                        if (itemDict.getCalcPercent() != null && acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                            calcPercent = Double.parseDouble(itemDict.getCalcPercent());
                            cost.setMinusCost(totalCost * inpPerform * (100 - calcPercent) / 100);
                        } else {
                            cost.setMinusCost(0.0);
                        }
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
                        double calcPercent = 0.0;
                        AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
                        if (itemDict.getCalcPercent() != null && acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                            calcPercent = Double.parseDouble(itemDict.getCalcPercent());
                            cost.setMinusCost(totalCost * ward * (100 - calcPercent) / 100);
                        } else {
                            cost.setMinusCost(0.0);
                        }
                        cost.setYearMonth(yearMonth);
                        cost.setFetchWay("计算");
                        acctDeptCosts.add(cost);

                    }
                } else {
                    //首先判断，是否有细项分割比例，如果有分割比例，则按照细项提取分割比例。
                    if(incomeItemDicts!=null&&incomeItemDicts.size()>0){
                        IncomeItemDict incomeItemDict = incomeItemDicts.get(0);
                        //判断是否急诊护理，如果为急诊护理则按照住院的比例进行分割
                        if (emgWard != null && emgWard.equals((String) objects[2])) {
                            outpOrder = (incomeItemDict.getInpOrderedBy()== null ? 0 : Double.parseDouble(incomeItemDict.getInpOrderedBy())) / 100;
                            outpPerformRate = (incomeItemDict.getInpPerformedBy()== null ? 0 : Double.parseDouble(incomeItemDict.getInpPerformedBy())) / 100;
                            outpWardRate = (incomeItemDict.getInpWardCode()== null ? 0 : Double.parseDouble(incomeItemDict.getInpWardCode())) / 100;
                        } else {
                            outpOrder = (incomeItemDict.getOutpOrderedBy() == null ? 0 : Double.parseDouble(incomeItemDict.getOutpOrderedBy())) / 100;
                            outpPerformRate = (incomeItemDict.getOutpPerformedBy() == null ? 0 : Double.parseDouble(incomeItemDict.getOutpPerformedBy())) / 100;
                            outpWardRate = (incomeItemDict.getOutpWardCode() == null ? 0 : Double.parseDouble(incomeItemDict.getOutpWardCode())) / 100;
                        }
                    }else{
                        if (emgWard != null && emgWard.equals((String) objects[2])) {
                            outpOrder = (itemDict.getInpOrderRate() == null ? 0 : itemDict.getInpOrderRate()) / 100;
                            outpPerformRate = (itemDict.getInpPerformRate() == null ? 0 : itemDict.getInpPerformRate()) / 100;
                            outpWardRate = (itemDict.getInpWardRate() == null ? 0 : itemDict.getInpWardRate()) / 100;
                        } else {
                            outpOrder = (itemDict.getOutpOrderRate() == null ? 0 : itemDict.getOutpOrderRate()) / 100;
                            outpPerformRate = (itemDict.getOutpPerformRate() == null ? 0 : itemDict.getOutpPerformRate()) / 100;
                            outpWardRate = (itemDict.getOutpWardRate() == null ? 0 : itemDict.getOutpWardRate()) / 100;
                        }
                    }



                    if (outpOrder != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setCostItemId(itemDict.getId());
                        cost.setAcctDeptId((String) objects[0]);
                        cost.setCost(totalCost * outpOrder);
                        double calcPercent = 0.0;
                        AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
                        if (itemDict.getCalcPercent() != null && acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                            calcPercent = Double.parseDouble(itemDict.getCalcPercent());
                            cost.setMinusCost(totalCost * outpOrder * (100 - calcPercent) / 100);
                        } else {
                            cost.setMinusCost(0.0);
                        }
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
                        double calcPercent = 0.0;
                        AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
                        if (itemDict.getCalcPercent() != null && acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {//如果是标准科室则不记录减免成本
                            calcPercent = Double.parseDouble(itemDict.getCalcPercent());
                            cost.setMinusCost(totalCost * outpPerformRate * (100 - calcPercent) / 100);
                        } else {
                            cost.setMinusCost(0.0);
                        }
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
                        double calcPercent = 0.0;
                        AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
                        if (itemDict.getCalcPercent() != null && acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                            calcPercent = Double.parseDouble(itemDict.getCalcPercent());
                            cost.setMinusCost(totalCost * outpWardRate * (100 - calcPercent) / 100);
                        } else {
                            cost.setMinusCost(0.0);
                        }
                        cost.setYearMonth(yearMonth);
                        cost.setFetchWay("计算");
                        acctDeptCosts.add(cost);
                    }
                }
            } else {
                continue;
            }
        }
        return acctDeptCosts;
    }

    @Transactional
    public void saveCostData(List<AcctDeptCost> acctDeptCosts) {
        if (acctDeptCosts.size() > 0) {
            for (AcctDeptCost deptCost : acctDeptCosts) {
                String yearMonth = deptCost.getYearMonth();
                String hospitalId = deptCost.getHospitalId();
                String costItemId = deptCost.getCostItemId();
                String acctDeptId = deptCost.getAcctDeptId();
                String fetchWay = deptCost.getFetchWay();
                String deleteHql = "delete from AcctDeptCost as cost where cost.hospitalId='" + hospitalId + "' and " +
                        "cost.yearMonth = '" + yearMonth + "' and cost.fetchWay='" + fetchWay + "' and " +
                        "cost.costItemId='" + costItemId + "'";
                //对于非折算成本删除删除的时候需要根据核算单元进行删除
                if ("计算".equals(fetchWay)) {
                    deleteHql += "  and cost.acctDeptId='" + acctDeptId + "'";
                } else {
                    deleteHql += "  and cost.acctDeptId='" + acctDeptId + "'";
                }
                this.getEntityManager().createQuery(deleteHql).executeUpdate();
                merge(deptCost);
            }
        }
    }


    /**
     * 计算分摊方法
     *
     * @param hospitalId
     * @param yearMonth
     * @param devideWay
     * @param costItemId
     * @param totalMoney
     * @return
     */
    public List<AcctDeptCost> devideAcctDeptCost(String hospitalId, String yearMonth, String devideWay, String costItemId, Double totalMoney, String depts) {

        if ("0".equals(devideWay)) {//人员分摊法
            return devideByPerson(hospitalId, yearMonth, costItemId, totalMoney);
        }

        if ("1".equals(devideWay)) {//科室面积分摊方法
            return devideByBuildArea(hospitalId, yearMonth, costItemId, totalMoney);
        }
        if ("2".equals(devideWay)) {
            return devideByEq(hospitalId, yearMonth, costItemId, totalMoney, depts);
        }

        //护理单元占床日分摊
        if ("3".equals(devideWay)) {
            return devideByUseBed(hospitalId, yearMonth, costItemId, totalMoney, depts);
        }
        return null;
    }

    private List<AcctDeptCost> devideByUseBed(String hospitalId, String yearMonth, String costItemId, Double totalMoney, String depts) {
        //计算月份
        String[] strings = yearMonth.split("-");
        String startDate = "", endDate = "";
        Integer month = Integer.parseInt(strings[1]);

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8) {
            startDate = strings[0] + "-" + month + "-01 00:00:00";
            endDate = strings[0] + "-" + month + "-31 23:59:59";
        } else if (month == 12 || month == 10) {
            startDate = strings[0] + "-" + month + "-01 00:00:00";
            endDate = strings[0] + "-" + month + "-31 23:59:59";
        } else if (month == 4 || month == 6 || month == 9) {
            startDate = strings[0] + "-" + month + "-01 00:00:00";
            endDate = strings[0] + "-" + month + "-30 23:59:59";
        } else if (month == 11) {
            startDate = strings[0] + "-" + month + "-01 00:00:00";
            endDate = strings[0] + "-" + month + "-30 23:59:59";

        } else {
            startDate = strings[0] + "-" + 2 + "-01 00:00:00";
            endDate = strings[0] + "-" + 3 + "-01 00:00:00";
        }
        //String sql = "select q.ward_code, sum(days)\n" +
        //        "  from (\n" +
        //        "        ---------在院\n" +
        //        "        select (case\n" +
        //        "                  when a.discharge_date_time is not null then\n" +
        //        "                   a.dept_discharge_from\n" +
        //        "                  else\n" +
        //        "                   (select decode(dept_code_lend,\n" +
        //        "                                  null,\n" +
        //        "                                  dept_code,\n" +
        //        "                                  dept_code_lend)\n" +
        //        "                      from pats_in_hospital\n" +
        //        "                     where patient_id = a.patient_id\n" +
        //        "                       and visit_id = a.visit_id)\n" +
        //        "                end) visit_dept,\n" +
        //        "                0 as money,\n" +
        //        "                --b.request_doctor_id doctor,       \n" +
        //        "                (case\n" +
        //        "                  when trunc(a.admission_date_time) <\n" +
        //        "                       trunc(to_date('"+startDate+"',\n" +
        //        "                                     'yyyy-mm-dd hh24:mi:ss')) then\n" +
        //        "                   trunc(to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss')) -\n" +
        //        "                   trunc(to_date('"+startDate+"', 'yyyy-mm-dd hh24:mi:ss')) + 1\n" +
        //        "                  else\n" +
        //        "                   trunc(to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss')) -\n" +
        //        "                   trunc(a.admission_date_time) + 1\n" +
        //        "                end) days\n" +
        //        "          from pat_visit a, mr_on_line b\n" +
        //        "         where a.patient_id = b.patient_id\n" +
        //        "           and a.visit_id = b.visit_id\n" +
        //        "           and trunc(a.admission_date_time) <\n" +
        //        "               trunc(to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss'))\n" +
        //        "           and b.request_doctor_id <> '*'\n" +
        //        "           and a.discharge_date_time is null\n" +
        //        "        union all\n" +
        //        "        -----------出院\n" +
        //        "        select (case\n" +
        //        "                 when a.discharge_date_time is not null then\n" +
        //        "                  a.dept_discharge_from\n" +
        //        "                 else\n" +
        //        "                  (select decode(dept_code_lend,\n" +
        //        "                                 null,\n" +
        //        "                                 dept_code,\n" +
        //        "                                 dept_code_lend)\n" +
        //        "                     from pats_in_hospital\n" +
        //        "                    where patient_id = a.patient_id\n" +
        //        "                      and visit_id = a.visit_id)\n" +
        //        "               end) visit_dept,\n" +
        //        "               0 as money,\n" +
        //        "               --b.request_doctor_id doctor,\n" +
        //        "               (case\n" +
        //        "                  when trunc(a.discharge_date_time) <\n" +
        //        "                       trunc(to_date('"+endDate+"',\n" +
        //        "                                     'yyyy-mm-dd hh24:mi:ss')) then\n" +
        //        "                   (case\n" +
        //        "                  when trunc(a.admission_date_time) <\n" +
        //        "                       trunc(to_date('"+startDate+"',\n" +
        //        "                                     'yyyy-mm-dd hh24:mi:ss')) then\n" +
        //        "                   trunc(a.discharge_date_time) -\n" +
        //        "                   trunc(to_date('"+startDate+"', 'yyyy-mm-dd hh24:mi:ss')) + 1\n" +
        //        "                  else\n" +
        //        "                   trunc(a.discharge_date_time) - trunc(a.admission_date_time) + 1\n" +
        //        "                end) else(case\n" +
        //        "                 when trunc(a.admission_date_time) <\n" +
        //        "                      trunc(to_date('"+startDate+"',\n" +
        //        "                                    'yyyy-mm-dd hh24:mi:ss')) then\n" +
        //        "                  trunc(to_date('"+endDate+"',\n" +
        //        "                                'yyyy-mm-dd hh24:mi:ss')) -\n" +
        //        "                  trunc(to_date('"+startDate+"',\n" +
        //        "                                'yyyy-mm-dd hh24:mi:ss')) + 1\n" +
        //        "                 else\n" +
        //        "                  trunc(to_date('"+endDate+"',\n" +
        //        "                                'yyyy-mm-dd hh24:mi:ss')) -\n" +
        //        "                  trunc(a.admission_date_time) + 1\n" +
        //        "               end) end) days\n" +
        //        "          from pat_visit a, mr_on_line b\n" +
        //        "         where a.patient_id = b.patient_id\n" +
        //        "           and a.visit_id = b.visit_id\n" +
        //        "           and b.request_doctor_id <> '*'\n" +
        //        "           and a.discharge_date_time is not null\n" +
        //        "           and a.discharge_date_time >=\n" +
        //        "               (to_date('"+startDate+"', 'yyyy-mm-dd  hh24:mi:ss'))\n" +
        //        "           and a.admission_date_time <\n" +
        //        "               (to_date('"+endDate+"', 'yyyy-mm-dd hh24:mi:ss'))) p,\n" +
        //        "       dept_vs_ward q\n" +
        //        " where p.visit_dept = q.dept_code\n" +
        //        " group by q.ward_code" ;

        //2016-11-14 双滦区人民医院修改占床日的修改方法
        String sql = "select b.id, count(*)\n" +
                "  from MEDADM.bedpats_in_hospital a, JIMS.dept_dict b\n" +
                " where stat_date_time >\n" +
                "       to_date('" + startDate + "', 'yyyy-mm-dd hh24:mi:ss')\n" +
                "   and stat_date_time <\n" +
                "       to_date('" + endDate + "', 'yyyy-mm-dd hh24:mi:ss')\n" +
                "   and a.ward_code = b.dept_code\n" +
                " group by b.id\n" +
                " order by b.id";

        List<Object[]> values = createNativeQuery(sql).getResultList();
        double totalCost = 0.0;
        for (Object[] obj : values) {
            totalCost += ((BigDecimal) obj[1]).doubleValue();
        }
        List<AcctDeptCost> costs = new ArrayList<>();

        String costItemDictHql = "from CostItemDict as  dict where dict.hospitalId='" + hospitalId + "'";
        List<CostItemDict> costItemDicts = createQuery(CostItemDict.class, costItemDictHql, new ArrayList<Object>()).getResultList();
        double calcPercent = 100;
        for (CostItemDict dict : costItemDicts) {
            if (costItemId.equals(dict.getId())) {
                calcPercent = Double.parseDouble(dict.getCalcPercent());
            }
        }

        for (Object[] obj : values) {
            double temp = ((BigDecimal) obj[1]).doubleValue();
            AcctDeptCost cost = new AcctDeptCost();
            cost.setAcctDeptId((String) obj[0]);
            cost.setFetchWay("分摊");
            cost.setHospitalId(hospitalId);
            cost.setCostItemId(costItemId);
            cost.setYearMonth(yearMonth);
            cost.setCost(totalMoney * temp / totalCost);
            AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
            if (acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                cost.setMinusCost(cost.getCost() * (100 - calcPercent) / 100);
            }else{
                cost.setMinusCost(0.0);
            }

            costs.add(cost);
        }
        return costs;
    }

    /**
     * 平均分摊法
     *
     * @param hospitalId
     * @param yearMonth
     * @param costItemId
     * @param totalMoney
     * @param depts
     * @return
     */
    private List<AcctDeptCost> devideByEq(String hospitalId, String yearMonth, String costItemId, Double totalMoney, String depts) {
        String costItemDictHql = "from CostItemDict as  dict where dict.hospitalId='" + hospitalId + "'";
        List<CostItemDict> costItemDicts = createQuery(CostItemDict.class, costItemDictHql, new ArrayList<Object>()).getResultList();
        double calcPercent = 100;
        for (CostItemDict dict : costItemDicts) {
            if (costItemId.equals(dict.getId())) {
                calcPercent = Double.parseDouble(dict.getCalcPercent());
            }
        }
        String ids[] = depts.split(";");
        if (ids.length > 0) {
            Double perMoney = totalMoney / ids.length;
            List<AcctDeptCost> costs = new ArrayList<>();
            for (String id : ids) {
                AcctDeptCost cost = new AcctDeptCost();
                cost.setAcctDeptId(id);
                cost.setFetchWay("分摊");
                cost.setHospitalId(hospitalId);
                cost.setCostItemId(costItemId);
                AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
                if ( acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                    cost.setMinusCost(perMoney * (100 - calcPercent) / 100);
                }else{
                    cost.setMinusCost(0.0);
                }
                cost.setYearMonth(yearMonth);
                cost.setCost(perMoney);
                costs.add(cost);
            }
            return costs;
        }
        return null;
    }

    /**
     * 使用面积分摊方法
     *
     * @param hospitalId
     * @param yearMonth
     * @param costItemId
     * @param totalMoney
     * @return
     */
    private List<AcctDeptCost> devideByBuildArea(String hospitalId, String yearMonth, String costItemId, Double totalMoney) {

        String hql = "select sum(dept.buildArea) From AcctDeptDict as dept,CostItemDevideDept as devide  where dept.hospitalId='" + hospitalId + "' and " +
                " dept.id = devide.deptId and devide.costItemId='" + costItemId + "'";
        Double totalAreas = createQuery(Double.class, hql, new ArrayList<Object>()).getSingleResult();//科室总面积
        if (totalAreas == null) {
            return null;
        }
        String deptHql = "select dept From AcctDeptDict as dept ,CostItemDevideDept vsDept where " +
                "dept.id=vsDept.deptId and vsDept.costItemId = '" + costItemId + "'";
        List<AcctDeptDict> depts = createQuery(AcctDeptDict.class, deptHql, new ArrayList<Object>()).getResultList();
        List<AcctDeptCost> costs = new ArrayList<>();
        String costItemDictHql = "from CostItemDict as  dict where dict.hospitalId='" + hospitalId + "'";
        List<CostItemDict> costItemDicts = createQuery(CostItemDict.class, costItemDictHql, new ArrayList<Object>()).getResultList();
        double calcPercent = 100;
        for (CostItemDict dict : costItemDicts) {
            if (costItemId.equals(dict.getId())) {
                calcPercent = Double.parseDouble(dict.getCalcPercent());
            }
        }
        for (AcctDeptDict deptDict : depts) {
            AcctDeptCost cost = new AcctDeptCost();
            cost.setAcctDeptId(deptDict.getId());
            cost.setFetchWay("分摊");
            cost.setHospitalId(hospitalId);
            cost.setCostItemId(costItemId);

            cost.setYearMonth(yearMonth);
            double area = 0.0;
            if (deptDict.getBuildArea() != null) {
                area = deptDict.getBuildArea();
            }
            cost.setCost(totalMoney * (area / totalAreas));
            AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
            if (acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                cost.setMinusCost(cost.getCost() * (100 - calcPercent) / 100);
            }else{
                cost.setMinusCost(0.0);
            }
            costs.add(cost);
        }
        return costs;
    }

    /**
     * 人员分摊法
     *
     * @param hospitalId
     * @param yearMonth
     * @param costItemId
     * @param totalMoney
     * @return
     */
    private List<AcctDeptCost> devideByPerson(String hospitalId, String yearMonth, String costItemId, Double totalMoney) {
        String hql = "select sum(dept.staffNum) From AcctDeptDict as dept,CostItemDevideDept as devide where dept.hospitalId='" + hospitalId + "' " +
                "and dept.id = devide.deptId and devide.costItemId='" + costItemId + "'";
        Double staffNums = createQuery(Double.class, hql, new ArrayList<Object>()).getSingleResult();//科室总面积
        String deptHql = "select dept From AcctDeptDict as dept ,CostItemDevideDept vsDept where " +
                "dept.id=vsDept.deptId and vsDept.costItemId = '" + costItemId + "'";
        List<AcctDeptDict> depts = createQuery(AcctDeptDict.class, deptHql, new ArrayList<Object>()).getResultList();
        List<AcctDeptCost> costs = new ArrayList<>();
        String costItemDictHql = "from CostItemDict as  dict where dict.hospitalId='" + hospitalId + "'";
        List<CostItemDict> costItemDicts = createQuery(CostItemDict.class, costItemDictHql, new ArrayList<Object>()).getResultList();

        double calcPercent = 100;
        for (CostItemDict dict : costItemDicts) {
            if (costItemId.equals(dict.getId())) {
                calcPercent = Double.parseDouble(dict.getCalcPercent());
            }
        }

        if (staffNums == null) {
            return null;
        }


        for (AcctDeptDict deptDict : depts) {
            AcctDeptCost cost = new AcctDeptCost();
            cost.setAcctDeptId(deptDict.getId());
            cost.setFetchWay("分摊");
            cost.setHospitalId(hospitalId);
            cost.setCostItemId(costItemId);

            cost.setYearMonth(yearMonth);
            double v = 0.0;
            if (deptDict.getStaffNum() != null) {
                v = deptDict.getStaffNum();
            }
            cost.setCost(totalMoney * (v / staffNums));
            AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
            if ( "0".equals(acctDeptDict.getStandardFlag())) {
                cost.setMinusCost(cost.getCost() * (100 - calcPercent) / 100);
            }

            if(cost.getCost() != 0 && acctDeptDict != null){
                costs.add(cost);
            }
        }
        return costs;
    }

    @Transactional
    public void saveDevideDeriectWrite(List<AcctDeptCost> acctDeptCosts) {
        if (acctDeptCosts.size() > 0) {
            String costItemId = acctDeptCosts.get(0).getCostItemId();
            String yearMonth = acctDeptCosts.get(0).getYearMonth();
            String hospitalId = acctDeptCosts.get(0).getHospitalId();
            String hql = "delete AcctDeptCost as cost where cost.yearMonth ='" + yearMonth + "' " +
                    "and cost.hospitalId='" + hospitalId + "' and cost.costItemId='" + costItemId + "' " +
                    "and cost.fetchWay='分摊'";
            getEntityManager().createQuery(hql).executeUpdate();

            for (AcctDeptCost cost : acctDeptCosts) {
                merge(cost);
            }
        }
    }


    /**
     * 记录科室的成本作为本科室的收入
     *
     * @param acctDeptCosts
     * @param incomeDeptId
     */
    @Transactional
    public void saveDeptDevideDeriectWrite(List<AcctDeptCost> acctDeptCosts, String incomeDeptId, String saveModel) {
        if ("0".equals(saveModel) || saveModel == null) {
            saveDevideOverWrite(acctDeptCosts, incomeDeptId);
        }
        if ("1".equals(saveModel)) {
            saveDevideUpdate(acctDeptCosts, incomeDeptId);
        }
    }

    /**
     * 对于存在的项目进行更新
     *
     * @param acctDeptCosts
     * @param incomeDeptId
     */
    private void saveDevideUpdate(List<AcctDeptCost> acctDeptCosts, String incomeDeptId) {
        for (AcctDeptCost cost : acctDeptCosts) {
            String costItemId = cost.getCostItemId();
            String yearMonth = cost.getYearMonth();
            String hospitalId = cost.getHospitalId();
            String acctDeptId = cost.getAcctDeptId();
            String hql3 = "from CostItemDict as dict where dict.id='" + costItemId + "'";
            CostItemDict costItemDict = createQuery(CostItemDict.class, hql3, new ArrayList<Object>()).getSingleResult();
            Double inRate = 100.0;
            if (costItemDict != null) {
                inRate = Double.parseDouble(costItemDict.getCalcPercent());
            }
            ServiceDeptIncome serviceDeptIncome = new ServiceDeptIncome();
            serviceDeptIncome.setAcctDeptId(incomeDeptId);
            serviceDeptIncome.setYearMonth(yearMonth);
            serviceDeptIncome.setHospitalId(hospitalId);
            serviceDeptIncome.setIncomeTypeId(costItemId);
            serviceDeptIncome.setGetWay("分摊");
            serviceDeptIncome.setTotalIncome(cost.getCost());
            serviceDeptIncome.setOperator(cost.getOperator());
            serviceDeptIncome.setOperatorDate(cost.getOperatorDate());
            serviceDeptIncome.setConfirmStatus("0");
            serviceDeptIncome.setOutFlag("1");
            serviceDeptIncome.setServiceForDeptId(cost.getAcctDeptId());
            merge(serviceDeptIncome);
            AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());

            if ( acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                cost.setMinusCost(cost.getCost() * (100 - inRate) / 100);
            }else{
                cost.setMinusCost(0.0);
            }
            merge(cost);
        }
    }


    /**
     * 保存重新分摊结果，对已经存在的成本进行删除，重新计入
     */
    private void saveDevideOverWrite(List<AcctDeptCost> acctDeptCosts, String incomeDeptId) {
        if (acctDeptCosts.size() > 0) {
            for (AcctDeptCost cost : acctDeptCosts) {
                String costItemId = cost.getCostItemId();
                String yearMonth = cost.getYearMonth();
                String hospitalId = cost.getHospitalId();
                String acctDeptId = cost.getAcctDeptId();
                String hql = "delete AcctDeptCost as cost where cost.yearMonth ='" + yearMonth + "' " +
                        "and cost.hospitalId='" + hospitalId + "' and cost.costItemId='" + costItemId + "' " +
                        "and cost.fetchWay='分摊' and cost.acctDeptId='" + acctDeptId + "'";
                getEntityManager().createQuery(hql).executeUpdate();

                String hql2 = "delete ServiceDeptIncome as income where income.yearMonth = '" + yearMonth + "' and " +
                        "income.hospitalId='" + hospitalId + "' and income.incomeTypeId='" + costItemId + "' and " +
                        "income.getWay='分摊' and income.serviceForDeptId='" + acctDeptId + "'";
                getEntityManager().createQuery(hql2).executeUpdate();

                String hql3 = "from CostItemDict as dict where dict.id='" + costItemId + "'";
                CostItemDict costItemDict = createQuery(CostItemDict.class, hql3, new ArrayList<Object>()).getSingleResult();
                Double inRate = 100.0;
                if (costItemDict != null) {
                    inRate = Double.parseDouble(costItemDict.getCalcPercent());
                }

                ServiceDeptIncome serviceDeptIncome = new ServiceDeptIncome();
                serviceDeptIncome.setAcctDeptId(incomeDeptId);
                serviceDeptIncome.setYearMonth(yearMonth);
                serviceDeptIncome.setHospitalId(hospitalId);
                serviceDeptIncome.setIncomeTypeId(costItemId);
                serviceDeptIncome.setGetWay("分摊");
                serviceDeptIncome.setTotalIncome(cost.getCost());
                serviceDeptIncome.setOperator(cost.getOperator());
                serviceDeptIncome.setOperatorDate(cost.getOperatorDate());
                serviceDeptIncome.setConfirmStatus("0");
                serviceDeptIncome.setOutFlag("1");
                serviceDeptIncome.setServiceForDeptId(cost.getAcctDeptId());
                merge(serviceDeptIncome);
                AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
                if (acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                    cost.setMinusCost(cost.getCost() * (100 - inRate) / 100);
                }else{
                    cost.setMinusCost(0.0);
                }
                merge(cost);
            }
        }
    }

    public List<AcctDeptCost> listAcctDeptCostPublish(String yearMonth, String hospitalId, String costItemId, String operator) {
        String sql = "select b.*\n" +
                "          from htca.acct_dept_cost b\n" +
                "         where (b.year_month = '" + yearMonth + "')\n" +
                "           and b.hospital_id = '" + hospitalId + "'\n" +
                "           and (b.operator = '" + operator + "')\n" +
                "           and b.acct_dept_id in (select id\n" +
                "                                    from htca.acct_dept_dict\n" +
                "                                   where end_dept = '1'\n" +
                "                                     and del_flag = '1')";
        if (costItemId != null && !costItemId.trim().equals("")) {
            sql += "           and (b.cost_item_id = '" + costItemId + "')";
        }

        List<AcctDeptCost> acctDeptCosts = createNativeQuery(sql, new ArrayList<Object>(), AcctDeptCost.class);
        return acctDeptCosts;
    }


    public List<AcctDeptCost> listAcctDeptCostPublishConfirm(String yearMonth, String hospitalId, String acctDeptId) {
        String sql = "select b.*\n" +
                "          from htca.acct_dept_cost b \n" +
                "         where (b.year_month = '" + yearMonth + "')\n" +
                "           and b.hospital_id = '" + hospitalId + "'\n" +
                "           and b.acct_dept_id =  '" + acctDeptId + "'\n" +
                "           and b.acct_dept_id in (select id\n" +
                "                                    from htca.acct_dept_dict\n" +
                "                                   where end_dept = '1'\n" +
                "                                     and del_flag = '1')";
        List<AcctDeptCost> acctDeptCosts = createNativeQuery(sql, new ArrayList<Object>(), AcctDeptCost.class);
        return acctDeptCosts;

    }

    @Transactional
    public void sameLastMonth(String yearMonth, String lastYearMonth, String costItemId, String hospitalId) throws Exception {
        String incomeHql = "from ServiceDeptIncome as income where income.hospitalId = '" + hospitalId + "' and income.yearMonth = '" + lastYearMonth + "' and income.incomeTypeId='" + costItemId + "'";
        String costHql = "from AcctDeptCost as cost where cost.hospitalId = '" + hospitalId + "' and cost.yearMonth = '" + lastYearMonth + "' and cost.costItemId='" + costItemId + "'";
        String delIncomeHql = "delete ServiceDeptIncome as income where income.hospitalId = '" + hospitalId + "' and income.yearMonth = '" + yearMonth + "' and income.incomeTypeId='" + costItemId + "'";
        String delCostHql = "delete AcctDeptCost as cost where cost.hospitalId = '" + hospitalId + "' and cost.yearMonth = '" + yearMonth + "' and cost.costItemId='" + costItemId + "'";
        //createQuery(ServiceDeptIncome.class,delIncomeHql,new ArrayList<Object>()).executeUpdate() ;
        //createQuery(AcctDeptCost.class,delCostHql,new ArrayList<Object>()).executeUpdate() ;
        try {
            getEntityManager().createQuery(delCostHql).executeUpdate();
            getEntityManager().createQuery(delIncomeHql).executeUpdate();
            List<ServiceDeptIncome> serviceDeptIncomes = createQuery(ServiceDeptIncome.class, incomeHql, new ArrayList<Object>()).getResultList();
            for (ServiceDeptIncome income : serviceDeptIncomes) {
                ServiceDeptIncome serviceDeptIncome = new ServiceDeptIncome();
                serviceDeptIncome.setOutFlag(income.getOutFlag());
                serviceDeptIncome.setHospitalId(income.getHospitalId());
                serviceDeptIncome.setAcctDeptId(income.getAcctDeptId());
                serviceDeptIncome.setServiceForDeptId(income.getServiceForDeptId());
                serviceDeptIncome.setTotalIncome(income.getTotalIncome());
                serviceDeptIncome.setConfirmStatus(income.getConfirmStatus());
                serviceDeptIncome.setDetailIncomeId(income.getDetailIncomeId());
                serviceDeptIncome.setYearMonth(yearMonth);
                serviceDeptIncome.setIncomeTypeId(costItemId);
                serviceDeptIncome.setGetWay(income.getGetWay());
                serviceDeptIncome.setOperator(income.getOperator());
                serviceDeptIncome.setOperatorDate(new Date());
                merge(serviceDeptIncome);
            }

            List<AcctDeptCost> acctDeptCosts = createQuery(AcctDeptCost.class, costHql, new ArrayList<Object>()).getResultList();
            for (AcctDeptCost acctDeptCost : acctDeptCosts) {
                AcctDeptCost cost = new AcctDeptCost();
                cost.setHospitalId(acctDeptCost.getHospitalId());
                cost.setOperator(acctDeptCost.getOperator());
                cost.setOperatorDate(new Date());
                cost.setYearMonth(yearMonth);
                cost.setFetchWay(acctDeptCost.getFetchWay());
                cost.setAcctDeptId(acctDeptCost.getAcctDeptId());
                cost.setCost(acctDeptCost.getCost());
                cost.setCostItemId(acctDeptCost.getCostItemId());
                AcctDeptDict acctDeptDict = get(AcctDeptDict.class, cost.getAcctDeptId());
                if (acctDeptDict != null && "0".equals(acctDeptDict.getStandardFlag())) {
                    cost.setMinusCost(acctDeptCost.getMinusCost());
                }else{
                    cost.setMinusCost(0.0);
                }
                merge(cost);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
