package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.AppConfigerParameter;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.*;
import com.jims.his.domain.htca.vo.DeptProfitVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/12/15.
 */
public class AcctDeptProfitFacade extends BaseFacade {


    /**
     * 保存或者更新收入核算
     *
     * @param deptProfitVo
     */
    @Transactional
    public void saveOrUpdate(DeptProfitVo deptProfitVo) {
        //首先删除本月份其他的数据

        try {
            List<AcctDeptProfit> acctDeptProfits = deptProfitVo.getAcctDeptProfits();
            BeanChangeVo<AcctProfitChangeRecord> acctProfitChangeRecordBeanChangeVo = deptProfitVo.getAcctProfitChangeRecordBeanChangeVo();
            List<AcctProfitChangeRecord> inserted = acctProfitChangeRecordBeanChangeVo.getInserted();
            List<AcctProfitChangeRecord> updated = acctProfitChangeRecordBeanChangeVo.getUpdated();
            inserted.addAll(updated);
            List<AcctProfitChangeRecord> deleted = acctProfitChangeRecordBeanChangeVo.getDeleted();

            //保存调整记录
            for (AcctProfitChangeRecord record : inserted) {
                merge(record);
            }

            List<String> ids = new ArrayList<>();
            for (AcctProfitChangeRecord record : deleted) {
                ids.add(record.getId());
            }
            removeByStringIds(AcctProfitChangeRecord.class, ids);

            if (acctDeptProfits.size() > 0) {
                AcctDeptProfit acctDeptProfit = acctDeptProfits.get(0);
                String hospitalId = acctDeptProfit.getHospitalId();
                String yearMonth = acctDeptProfit.getYearMonth();
                String hql = "delete from AcctDeptProfit as pro where pro.hospitalId='" + hospitalId + "' and " +
                        "pro.yearMonth = '" + yearMonth + "'";
                getEntityManager().createQuery(hql).executeUpdate();
                for (AcctDeptProfit profit : acctDeptProfits) {
                    merge(profit);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算核算单元效益
     *
     * @param hospitalId
     * @param yearMonth
     * @param paramId
     * @return
     */
    public List<AcctDeptProfit> caclProfit(String hospitalId, String yearMonth, String paramId) {
        List<AcctDeptProfit> acctDeptProfits = null;
        AcctParam acctParam = get(AcctParam.class, paramId);
        String sql = acctParam.getParamSql();
        if (null == sql || "".equals(sql)) {
            return null;
        }
        sql = sql.replace("${hospitalId}", hospitalId).replace("${yearMonth}", yearMonth);
        acctDeptProfits = createNativeQuery(sql, new ArrayList<Object>(), AcctDeptProfit.class);
        return this.reDevideManagerCost(acctDeptProfits, yearMonth, hospitalId);
    }

    /**
     * 重新分割管理成本
     *
     * @param acctDeptProfits
     * @return
     */
    public List<AcctDeptProfit> reDevideManagerCost(List<AcctDeptProfit> acctDeptProfits, String yearMonth, String hospitalId) {
        try {
            //计算总输入
            //医疗输入开始
            String hql1 = "select sum(detail.totalCost) from CalcIncomeDetail detail where detail.yearMonth = '" + yearMonth + "' and detail.hospitalId='" + hospitalId + "' ";
            BigDecimal firstIncome = createQuery(BigDecimal.class, hql1, new ArrayList<Object>()).getSingleResult();
            Double fist = firstIncome == null ? 0.0 : firstIncome.doubleValue();
            //医疗输入结束
            //二线对外服务开始
            String hql2 = "select nvl(sum(totalIncome),0) from ServiceDeptIncome as income where income.outFlag = '0' and income.yearMonth='" + yearMonth + "' and income.hospitalId='" + hospitalId + "'";
            Double serviceIncome = createQuery(Double.class, hql2, new ArrayList<Object>()).getSingleResult();
            if (serviceIncome == null) {
                serviceIncome = 0.0;
            }
            //二线服务科室对外收入结束


            String hqlPram = "from AppConfigerParameter as p where p.hospitalId='" + hospitalId + "' and p.parameterName='manager_rate'";
            AppConfigerParameter parameter = createQuery(AppConfigerParameter.class, hqlPram, new ArrayList<Object>()).getSingleResult();
            //获取一线服务核算单元的总人数
            String hql = "select sum(dept.staffNum) from AcctDeptDict as dept where dept.hospitalId='" + hospitalId + "' and dept.deptType <> '4' and dept.endDept='1' and dept.delFlag='1'";
            //获取所有的管理类科室
            String acctDeptHql = "from AcctDeptDict as dept where dept.hospitalId='" + hospitalId + "'" +
                    "and dept.deptType<>'4' and dept.endDept = '1' and dept.delFlag = '1'";

            List<AcctDeptDict> acctDeptDicts = createQuery(AcctDeptDict.class, acctDeptHql, new ArrayList<Object>()).getResultList();
            Double managerRate = Double.parseDouble(parameter.getParameterValue());
            Double managerCost = (fist + serviceIncome) * managerRate * 0.5;//提取出全院总收入
            Double managerStaffCost = managerCost * 2 * 0.3;
            Double managerProfitCost = managerCost * 2 * 0.7;
            Double staffNum = createQuery(Double.class, hql, new ArrayList<Object>()).getSingleResult();
            Double totalProfit = 0.0;
            Double minus = 0.0;
            for (AcctDeptProfit profit : acctDeptProfits) {
                Double temp = profit.getDeptIncome() - (profit.getDeptCost() == null ? 0 : profit.getDeptCost()) + (profit.getIncomeChangeItem() == null ? 0 : profit.getIncomeChangeItem()) - (profit.getCostChangeItem() == null ? 0 : profit.getCostChangeItem());
                if (acctDeptIsInAcct(profit, acctDeptDicts)) {
                    totalProfit += (temp);
                    if (temp < minus) {
                        minus = temp;
                    }
                }
                profit.setAcctBalance(temp);
            }
            if (minus < 0) {
                for (AcctDeptProfit profit : acctDeptProfits) {
                    if (acctDeptIsInAcct(profit, acctDeptDicts)) {
                        totalProfit -= minus;
                    }
                }
            }
            for (AcctDeptProfit profit : acctDeptProfits) {
                if (acctDeptIsInAcct(profit, acctDeptDicts)) {
                    String deptHql = "from AcctDeptDict as dept where dept.id='" + profit.getAcctDeptId() + "'";
                    AcctDeptDict acctDeptDict = createQuery(AcctDeptDict.class, deptHql, new ArrayList<Object>()).getSingleResult();
                    String acctDeptDictHql = "from AcctDeptDict as add where add.id='"+profit.getAcctDeptId()+"'" ;
                    AcctDeptDict acctDeptDictProfit = createQuery(AcctDeptDict.class,acctDeptDictHql,new ArrayList<Object>()).getSingleResult();

                    if (minus < 0) {
                        if (null == acctDeptDict.getStaffNum() || acctDeptDict.getStaffNum() == 0) {
                            profit.setManagerStaffCost(0.0);
                            //profit.setManagerProfitCost(managerCost * ((profit.getAcctBalance() - minus) / totalProfit));
                            profit.setManagerProfitCost(managerProfitCost * ((profit.getAcctBalance() - minus) / totalProfit));
                            if(acctDeptDictProfit!=null && "0".equals(acctDeptDictProfit.getStandardFlag())){
                                profit.setManagerCostMinus((profit.getManagerProfitCost()+profit.getManagerStaffCost())*0.5);
                            }else{
                                profit.setManagerCostMinus((profit.getManagerProfitCost()+profit.getManagerStaffCost())*0.2);
                            }

                        } else {
                            double n = acctDeptDict.getStaffNum();
                            //profit.setManagerStaffCost(managerCost  * n / staffNum);
                            profit.setManagerStaffCost(managerStaffCost * n / staffNum);
                            //profit.setManagerProfitCost(managerCost  * ((profit.getAcctBalance() - minus) / totalProfit) );
                            profit.setManagerProfitCost(managerProfitCost * ((profit.getAcctBalance() - minus) / totalProfit));
                            //对非标准科室计入减免0.5
                            if(acctDeptDictProfit!=null && "0".equals(acctDeptDictProfit.getStandardFlag())){
                                profit.setManagerCostMinus((profit.getManagerProfitCost()+profit.getManagerStaffCost())*0.5);
                            }else{
                                profit.setManagerCostMinus((profit.getManagerProfitCost()+profit.getManagerStaffCost())*0.2);
                            }
                        }
                    } else {
                        if (null == acctDeptDict.getStaffNum() || acctDeptDict.getStaffNum() == 0) {
                            profit.setManagerStaffCost(0.0);
                            //profit.setManagerProfitCost(managerCost * (profit.getAcctBalance() / totalProfit));
                            profit.setManagerProfitCost(managerProfitCost * (profit.getAcctBalance() / totalProfit));
                            //对非标准科室计入减免0.5
                            if(acctDeptDictProfit!=null && "0".equals(acctDeptDictProfit.getStandardFlag())){
                                profit.setManagerCostMinus((profit.getManagerProfitCost()+profit.getManagerStaffCost())*0.5);
                            }else{
                                profit.setManagerCostMinus((profit.getManagerProfitCost()+profit.getManagerStaffCost())*0.2);
                            }
                        } else {
                            double n = acctDeptDict.getStaffNum();
                            //profit.setManagerStaffCost(managerCost  * n / staffNum);
                            profit.setManagerStaffCost(managerStaffCost * n / staffNum);
                            //profit.setManagerProfitCost(managerCost  * ((profit.getAcctBalance() - minus) / totalProfit) );                        }
                            //对非标准科室计入减免0.5
                            profit.setManagerProfitCost(managerProfitCost * ((profit.getAcctBalance() - minus) / totalProfit));
                            if(acctDeptDictProfit!=null && "0".equals(acctDeptDictProfit.getStandardFlag())){
                                profit.setManagerCostMinus((profit.getManagerProfitCost()+profit.getManagerStaffCost())*0.5);
                            }else{
                                profit.setManagerCostMinus((profit.getManagerProfitCost()+profit.getManagerStaffCost())*0.2);
                            }
                        }
                    }
                } else {
                    profit.setManagerStaffCost(0.0);
                    profit.setManagerProfitCost(0.0);
                    profit.setManagerCostMinus(0.0);
                }



            }


            return acctDeptProfits;
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 判断 此效益记录是否为管理科室
     * 如果是管理科室则不承担相应的管理成本
     * 否则则需要承当相应的管理成本
     *
     * @param profit
     * @param acctDeptDicts
     * @return
     */
    private boolean acctDeptIsInAcct(AcctDeptProfit profit, List<AcctDeptDict> acctDeptDicts) {
        for (AcctDeptDict dict : acctDeptDicts) {
            if (profit.getAcctDeptId().equals(dict.getId())) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void saveacctProftChageRecords(List<AcctProfitChangeRecord> acctProfitChangeRecords) {
        for (AcctProfitChangeRecord record : acctProfitChangeRecords) {
            merge(record);
        }

    }

    /**
     * 更新专项奖
     *
     * @param yearMonth
     * @param hospitalId
     * @return
     */
    @Transactional
    public int updateSpecialIncome(String yearMonth, String hospitalId) {

        String sql = "   \n" +
                "   update htca.acct_dept_profit a\n" +
                "      set a.special_income = (select sum(b.reward_num)\n" +
                "                                from htca.acct_single_reward_record b\n" +
                "                               where a.acct_dept_id = b.acct_dept_id\n" +
                "                                 and a.year_month = b.year_month\n" +
                "                                 and b.year_month = '" + yearMonth + "'" +
                "                                 and b.hospital_id = '" + hospitalId + "')\n" +
                "    where a.year_month = '" + yearMonth + "'\n" +
                "      and a.hospital_id = '" + hospitalId + "'";
        return createNativeQuery(sql).executeUpdate();
    }
}
