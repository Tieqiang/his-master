package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.entity.AppConfigerParameter;
import com.jims.his.domain.htca.entity.AcctDeptDict;
import com.jims.his.domain.htca.entity.AcctDeptProfit;
import com.jims.his.domain.htca.entity.AcctParam;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;

/**
 * Created by heren on 2015/12/15.
 */
public class AcctDeptProfitFacade extends BaseFacade {

    /**
     * 保存或者更新收入核算
     *
     * @param acctDeptProfits
     */
    @Transactional
    public void saveOrUpdate(List<AcctDeptProfit> acctDeptProfits) {
        //首先删除本月份其他的数据
        if(acctDeptProfits.size()>0){
            AcctDeptProfit acctDeptProfit = acctDeptProfits.get(0);
            String hospitalId = acctDeptProfit.getHospitalId();
            String yearMonth = acctDeptProfit.getYearMonth();
            String hql = "delete from AcctDeptProfit as pro where pro.hospitalId='"+hospitalId+"' and " +
                    "pro.yearMonth = '"+yearMonth+"'" ;

            //createQuery(AcctDeptProfit.class,hql,new ArrayList<Object>()).executeUpdate()  ;
            getEntityManager().createQuery(hql).executeUpdate() ;
            for(AcctDeptProfit profit :acctDeptProfits){
                merge(profit) ;
            }
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
        try {
            AcctParam acctParam = get(AcctParam.class, paramId);
            String sql = acctParam.getParamSql();
            if (null == sql || "".equals(sql)) {
                return null;
            }
            sql =sql.replace("${hospitalId}",hospitalId).replace("${yearMonth}",yearMonth) ;
            acctDeptProfits = createNativeQuery(sql,new ArrayList<Object>(),AcctDeptProfit.class);
            //计算总输入
            String hql1 ="select sum(detail.totalCost) from CalcIncomeDetail detail where detail.yearMonth = '"+yearMonth+"' and detail.hospitalId='"+hospitalId+"' " ;
            BigDecimal firstIncome = createQuery(BigDecimal.class, hql1, new ArrayList<Object>()).getSingleResult() ;
            Double fist=firstIncome.doubleValue() ;
            String hql2 = "select nvl(sum(totalIncome),0) from ServiceDeptIncome as income where income.outFlag = '0' and income.yearMonth='"+yearMonth+"' and income.hospitalId='"+hospitalId+"'"  ;
            Double serviceIncome = createQuery(Double.class,hql2,new ArrayList<Object>()).getSingleResult() ;
            String hqlPram = "from AppConfigerParameter as p where p.hospitalId='"+hospitalId+"' and p.parameterName='manager_rate'" ;
            AppConfigerParameter parameter = createQuery(AppConfigerParameter.class,hqlPram,new ArrayList<Object>()).getSingleResult() ;
            String hql="select sum(dept.staffNum) from AcctDeptDict as dept where dept.hospitalId='"+hospitalId+"'" ;

            Double managerRate = Double.parseDouble(parameter.getParameterValue()) ;
            Double managerCost = (fist + serviceIncome)*managerRate ;
            Double staffNum = createQuery(Double.class,hql,new ArrayList<Object>()).getSingleResult() ;
            Double totalProfit =0.0 ;
            Double minus = 0.0 ;
            for(AcctDeptProfit profit:acctDeptProfits){
                Double temp =profit.getDeptIncome() - profit.getDeptCost() ;
                totalProfit +=(temp) ;
                profit.setAcctBalance(temp);
                if(temp<minus){
                    minus = temp ;
                }
            }


            if(minus<0){
                for(AcctDeptProfit profit:acctDeptProfits){
                    if("*".equals(profit.getAcctDeptId())||null==profit.getAcctDeptId()||"".equals(profit.getAcctDeptId())){

                    }else{
                        totalProfit -= minus;
                    }
                }
            }





            for(AcctDeptProfit profit:acctDeptProfits){
                if("*".equals(profit.getAcctDeptId())||null==profit.getAcctDeptId()||"".equals(profit.getAcctDeptId())){
                    //归集失败的费用，查找原因
                }else{
                    String deptHql = "from AcctDeptDict as dept where dept.id='"+profit.getAcctDeptId()+"'" ;
                    AcctDeptDict acctDeptDict = createQuery(AcctDeptDict.class, deptHql,new ArrayList<Object>()).getSingleResult() ;

                    if(minus<0){


                        if(null==acctDeptDict.getStaffNum()){
                            profit.setManagerCost(managerCost*((profit.getAcctBalance()-minus)/totalProfit));
                        }else{
                            double n=acctDeptDict.getStaffNum() ;
                            profit.setManagerCost(managerCost*0.5*((profit.getAcctBalance()-minus)/totalProfit) + managerCost*0.5*n/staffNum);
                        }
                    }else{
                        if(null==acctDeptDict.getStaffNum()){
                            profit.setManagerCost(managerCost*(profit.getAcctBalance()/totalProfit));
                        }else{
                            double n=acctDeptDict.getStaffNum() ;
                            profit.setManagerCost(managerCost*0.5*(profit.getAcctBalance()/totalProfit) + managerCost*0.5*n/staffNum);
                        }
                    }


                }

            }

            return acctDeptProfits;
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }

    }
}
