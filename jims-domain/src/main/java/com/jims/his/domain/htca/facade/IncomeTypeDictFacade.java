package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.AcctProfitChangeDict;
import com.jims.his.domain.htca.entity.AcctProfitChangeRecord;
import com.jims.his.domain.htca.entity.IncomeTypeDict;

import java.util.ArrayList;
import java.util.List;

/**
 * 收入分类项目
 * Created by heren on 2015/10/28.
 */
public class IncomeTypeDictFacade extends BaseFacade {

    /**
     * 收入分类
     * @param hospitalId
     * @return
     */
    public List<IncomeTypeDict> findByHospitalId(String hospitalId) {

        String hql = "from IncomeTypeDict as income where income.hospitalId = '"+hospitalId+"'" ;
        List<IncomeTypeDict> resultList = createQuery(IncomeTypeDict.class, hql, new ArrayList<Object>()).getResultList();
        return resultList ;
    }


    /**
     * 更改IncomeTypeDict
     * @param incomeTypeDicts
     */
    @Transactional
    public void saveIncomeTypeDict(BeanChangeVo<IncomeTypeDict> incomeTypeDicts){
        List<IncomeTypeDict> inserted = incomeTypeDicts.getInserted();
        List<IncomeTypeDict> updated = incomeTypeDicts.getUpdated();
        List<IncomeTypeDict> deleted = incomeTypeDicts.getDeleted();
        inserted.addAll(updated) ;
        for(IncomeTypeDict dict:inserted){
            merge(dict) ;
        }
        List<String> ids = new ArrayList<>() ;
        for(IncomeTypeDict dict:deleted){
            ids.add(dict.getId()) ;
        }

        if(ids.size()>0){
            removeByStringIds(IncomeTypeDict.class,ids);
        }
    }

    /**
     *查询收益调整记录表
     * @return
     */
    public List<AcctProfitChangeRecord> findProftByHospitalId() {
        String hql = "from AcctProfitChangeRecord  where 1=1 ";
        List<AcctProfitChangeRecord> resultList = createQuery(AcctProfitChangeRecord.class, hql, new ArrayList<Object>()).getResultList();
        return resultList;
    }

    /**
     *保存收益调整记录表
     * @param acctProftChageRecordBeanChangeVo
     */
    @Transactional
    public void saveacctProftChageRecordBeanChangeVo(BeanChangeVo<AcctProfitChangeRecord> acctProftChageRecordBeanChangeVo) {
        List<AcctProfitChangeRecord> inserted = acctProftChageRecordBeanChangeVo.getInserted();
        List<AcctProfitChangeRecord> updated = acctProftChageRecordBeanChangeVo.getUpdated();
        List<AcctProfitChangeRecord> deleted = acctProftChageRecordBeanChangeVo.getDeleted();
        if(updated!=null){
            inserted.addAll(updated);
        }
        if(inserted!=null){
            for (AcctProfitChangeRecord dict : inserted) {
                merge(dict);
            }
        }
        if(deleted!=null){
            List<String> ids = new ArrayList<>();
            for (AcctProfitChangeRecord dict : deleted) {
                ids.add(dict.getId());
            }

            if (ids.size() > 0) {
                removeByStringIds(AcctProfitChangeRecord.class, ids);
            }
        }
    }

    /**
     *保存收益调整项目字典
     * @param acctProfitChangeDicttBeanChangeVo
     */
    @Transactional
    public void saveacctProfitChangeDicttBeanChangeVo(BeanChangeVo<AcctProfitChangeDict> acctProfitChangeDicttBeanChangeVo) {
        List<AcctProfitChangeDict> inserted = acctProfitChangeDicttBeanChangeVo.getInserted();
        List<AcctProfitChangeDict> updated = acctProfitChangeDicttBeanChangeVo.getUpdated();
        List<AcctProfitChangeDict> deleted = acctProfitChangeDicttBeanChangeVo.getDeleted();
        inserted.addAll(updated);
        for (AcctProfitChangeDict dict : inserted) {
            merge(dict);
        }
        List<String> ids = new ArrayList<>();
        for (AcctProfitChangeDict dict : deleted) {
            ids.add(dict.getId());
        }

        if (ids.size() > 0) {
            removeByStringIds(AcctProfitChangeDict.class, ids);
        }
    }

    /**
     *查询收益调整项目字典
     * @param hospitalId
     * @return
     */

    public List<AcctProfitChangeDict> findProfitByHospitalId(String hospitalId) {
        String hql = "from AcctProfitChangeDict as income where income.hospitalId = '" + hospitalId + "'";
        List<AcctProfitChangeDict> resultList = createQuery(AcctProfitChangeDict.class, hql, new ArrayList<Object>()).getResultList();
        return resultList;
    }

    /**
     * 更新调整
     * @param yearMonth
     */
    @Transactional
    public void updateAcctProfitChangeRecord(String yearMonth) {
        String hql = "update AcctDeptProfit as a set a.costChangeItem = nvl((select sum(changeAmount)\n" +
                "                                  from AcctProfitChangeRecord as b " +
                "                                 where b.yearMonth = '"+yearMonth+"' " +
                "                                   and b.incomeOrCost = '0' " +
                "                                   and b.acctDeptId = a.acctDeptId),0)\n" +
                " where a.yearMonth = '"+yearMonth+"'";

        String sql = "update AcctDeptProfit as a set a.incomeChangeItem = nvl((select sum(changeAmount) \n" +
                "                                                  from AcctProfitChangeRecord as b  " +
                "                                                 where b.yearMonth = '"+yearMonth+"' " +
                "                                                   and b.incomeOrCost = '1'         " +
                "                                                   and b.acctDeptId = a.acctDeptId),0) " +
                "                 where a.yearMonth = '"+yearMonth+"'";
        this.getEntityManager().createQuery(hql).executeUpdate();
        this.getEntityManager().createQuery(sql).executeUpdate();
    }

    /**
     * 更新上月提成比
     * @param yearMonth
     */
    @Transactional
    public void updateMonthRateChange(String yearMonth,String yearMonth1) {
        String sql = "update AcctDeptProfit as a set a.convertRate =nvl((select b.convertRate" +
                "           from AcctDeptProfit as b " +
                "          where b.yearMonth = '"+yearMonth1+"'\n" +
                "            and b.acctDeptId = a.acctDeptId),0)\n" +
                "  where a.yearMonth = '"+yearMonth+"'";
        this.getEntityManager().createQuery(sql).executeUpdate();
    }
}
