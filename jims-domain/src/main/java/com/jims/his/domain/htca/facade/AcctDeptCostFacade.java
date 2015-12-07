package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.AcctDeptCost;
import com.jims.his.domain.htca.entity.AcctReckItemClassDict;
import com.jims.his.domain.htca.entity.CostItemDict;

import javax.persistence.TypedQuery;
import javax.swing.plaf.basic.BasicEditorPaneUI;
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
                "                     and cost_item_id = '" + costItemId + "')\n" +
                "   and hospital_id = '4028862d4fcf2590014fcf9aef480016'";

        List<AcctDeptCost> acctDeptCosts = createNativeQuery(sql, new ArrayList<Object>(), AcctDeptCost.class);
        return acctDeptCosts;
    }

    @Transactional
    public void saveOrUpdate(List<AcctDeptCost> acctDeptCosts) {
        for (AcctDeptCost cost : acctDeptCosts) {
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
        String hql = "From AcctDeptCost as cost where cost.hospitalId='" + hospitalId + "' and yearMonth='" + yearMonth + "'";
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

    @Transactional
    public void fetchCostData(String hospitalId, String yearMonth) {

        String sql = "select a.ordered_by,\n" +
                "       a.performed_by,\n" +
                "       a.ward_code,\n" +
                "       sum(a.total_cost),\n" +
                "       a.class_on_recking,\n" +
                "       a.inp_or_outp " +
                "  from htca.calc_income_detail a\n" +
                "  where year_month = '" + yearMonth + "'\n" +
                "  and a.hospital_id = '" + hospitalId + "'\n" +
                " group by a.ordered_by, a.performed_by, a.ward_code, a.class_on_recking,a.inp_or_outp ";

        List<Object[]> resultList = createNativeQuery(sql).getResultList();
        List<AcctDeptCost> acctDeptCosts = new ArrayList<>();
        for (Object[] objects : resultList) {
            String hql = "select cost from CostItemDict as cost,AcctReckItemClassDict as income " +
                    "where cost.id=income.costId" +
                    " and income.reckItemCode='" + objects[5] + "'";

            TypedQuery<CostItemDict> query = createQuery(CostItemDict.class, hql, new ArrayList<Object>());
            List<CostItemDict> costItemDicts = query.getResultList();
            if (costItemDicts.size() > 0) {
                CostItemDict itemDict = costItemDicts.get(0);
                String hql2 = "from AcctReckItemClassDict as dict where dict.costId='" + itemDict.getId() + "'";
                TypedQuery<AcctReckItemClassDict> query1 = createQuery(AcctReckItemClassDict.class, hql2, new ArrayList<Object>());
                List<AcctReckItemClassDict> acctReckItemClassDictList = query1.getResultList();
                double calcReate = 0.0;
                if (acctReckItemClassDictList.size() > 0) {
                    AcctReckItemClassDict dict = acctReckItemClassDictList.get(0);
                    calcReate = dict.getFixConvert();
                }
                double totalCost = (double) objects[3] * (100 - calcReate);
                String inpOrOutp = (String) objects[6];
                if ("1".equals(inpOrOutp)) {
                    double inpOrder = itemDict.getInpOrderRate() / 100;
                    double inpPerform = itemDict.getInpPerformRate() / 100;
                    double ward = itemDict.getInpWardRate() / 100;

                    if (inpOrder != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setAcctDeptId((String) objects[0]);
                        cost.setCost(totalCost * inpOrder);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        acctDeptCosts.add(cost);
                    }
                    if (inpPerform != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setAcctDeptId((String) objects[1]);
                        cost.setCost(totalCost * inpPerform);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        acctDeptCosts.add(cost);
                    }

                    if (ward != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setAcctDeptId((String) objects[2]);
                        cost.setCost(totalCost * inpPerform);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        acctDeptCosts.add(cost);
                    }
                } else {
                    double outpOrder = itemDict.getOutpOrderRate();
                    Double outpPerformRate = itemDict.getOutpPerformRate();
                    Double outpWardRate = itemDict.getOutpWardRate();
                    if (outpOrder != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setAcctDeptId((String) objects[0]);
                        cost.setCost(totalCost * outpOrder);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        acctDeptCosts.add(cost);
                    }
                    if (outpPerformRate != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setAcctDeptId((String) objects[1]);
                        cost.setCost(totalCost * outpPerformRate);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        acctDeptCosts.add(cost);
                    }

                    if (outpWardRate != 0) {
                        AcctDeptCost cost = new AcctDeptCost();
                        cost.setHospitalId(hospitalId);
                        cost.setAcctDeptId((String) objects[2]);
                        cost.setCost(totalCost * outpWardRate);
                        cost.setMinusCost(0.0);
                        cost.setYearMonth(yearMonth);
                        acctDeptCosts.add(cost);
                    }
                }

            } else {
                continue;
            }

            for(AcctDeptCost cost:acctDeptCosts){
                merge(cost) ;
            }

        }
    }
}
