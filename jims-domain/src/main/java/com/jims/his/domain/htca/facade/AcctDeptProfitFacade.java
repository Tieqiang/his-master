package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.AcctDeptProfit;
import com.jims.his.domain.htca.entity.AcctParam;

import java.util.ArrayList;
import java.util.List;

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
        AcctParam acctParam = get(AcctParam.class, paramId);
        String sql = acctParam.getParamSql();
        if (null == sql || "".equals(sql)) {
            return null;
        }
        sql =sql.replace("${hospitalId}",hospitalId).replace("${yearMonth}",yearMonth) ;
        List<AcctDeptProfit> acctDeptProfits = createNativeQuery(sql,new ArrayList<Object>(),AcctDeptProfit.class) ;
        return acctDeptProfits;
    }
}
