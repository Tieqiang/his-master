package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.AcctSingleRewardDict;
import com.jims.his.domain.htca.entity.AcctSingleRewardRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/25.
 */
public class AcctSingleRewardRecordFacade extends BaseFacade {


    /**
     * 保存新增、删除、修改等相关
     * @param beanChangeVo
     */
    @Transactional
    public void saveOrUpdate(BeanChangeVo<AcctSingleRewardRecord> beanChangeVo) {
        List<AcctSingleRewardRecord> inserted = beanChangeVo.getInserted();
        List<AcctSingleRewardRecord> updated = beanChangeVo.getUpdated();
        List<AcctSingleRewardRecord> deleted = beanChangeVo.getDeleted();
        inserted.addAll(updated) ;
        for(AcctSingleRewardRecord dict:inserted){
            merge(dict) ;
        }
        List<String> ids= new ArrayList<>() ;
        for(AcctSingleRewardRecord dict:deleted){
            ids.add(dict.getId())  ;
        }
        removeByStringIds(AcctSingleRewardRecord.class,ids);
    }

    public List<AcctSingleRewardRecord> list(String yearMonth, String hospitalId, String rewardDictId) {

        String sql = "select b.id,\n" +
                "       b.acct_dept_id,\n" +
                "       b.reward_num,\n" +
                "       b.reward_dict_id,\n" +
                "       b.operator,\n" +
                "       b.operator_date\n" +
                "  from htca.acct_single_reward_record b\n" +
                " where (b.year_month = '"+yearMonth+"')\n" +
                "   and b.hospital_id = '"+hospitalId+"'\n" +
                "   and (b.reward_dict_id = '"+rewardDictId+"')\n" +
                "union\n" +
                "select '' id,\n" +
                "       id acct_dept_id,\n" +
                "       null reward_num,\n" +
                "       '' reward_dict_id,\n" +
                "       null operator,\n" +
                "       null operator_date\n" +
                "  from htca.acct_dept_dict\n" +
                " where id not in\n" +
                "       (select acct_dept_id\n" +
                "          from htca.acct_single_reward_record\n" +
                "         where year_month = '"+yearMonth+"'\n" +
                "           and hospital_id = '"+hospitalId+"'\n" +
                "           and reward_dict_id = '"+rewardDictId+"')\n" +
                "   and hospital_id = '"+hospitalId+"'\n" +
                "   and end_dept = '1'" ;
        return createNativeQuery(sql,new ArrayList<Object>(),AcctSingleRewardRecord.class) ;
    }

    @Transactional
    public void mergeAcctSingleRewardRecord(List<AcctSingleRewardRecord> acctSingleRewardRecords) {
        if(acctSingleRewardRecords.size()>0){
            AcctSingleRewardRecord record = acctSingleRewardRecords.get(0) ;
            String hospitalId = record.getHospitalId() ;
            String yearMonth = record.getYearMonth() ;
            String acctId =record.getRewardDictId()  ;
            String hql="delete from AcctSingleRewardRecord as record where record.hospitalId='"+hospitalId+"' and " +
                    "record.yearMonth = '"+yearMonth+"' and record.rewardDictId='"+acctId+"'" ;
            this.getEntityManager().createQuery(hql).executeUpdate() ;

            for(AcctSingleRewardRecord rewardRecord :acctSingleRewardRecords){
                merge(rewardRecord) ;
            }
        }

    }

    @Transactional
    public void deleteRecord(List<String> ids) {
        removeByStringIds(AcctSingleRewardRecord.class,ids);
    }
}

