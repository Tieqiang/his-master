package com.jims.his.domain.htca.facade;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.AcctPublishRecord;
import com.sun.prism.impl.Disposer;
import org.hibernate.metamodel.source.binder.IdentifierSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2016/2/17.
 */
public class AcctPublishRecordFacade  extends BaseFacade {


    @Transactional
    public List<AcctPublishRecord> saveAcctPublishRecords(List<AcctPublishRecord> acctPublishRecords) {

        List<AcctPublishRecord> acctPublishRecordList = new ArrayList<>() ;
        for(AcctPublishRecord record :acctPublishRecords){
            String yearMonth = record.getYearMonth() ;
            String incomeFlag = record.getIncomeFlag() ;
            String hospitalId = record.getHospitalId() ;
            String hql = "from AcctPublishRecord as record where record.yearMonth = '"+yearMonth+"' and record.incomeFlag = '"+incomeFlag+"' and record.hospitalId='"+hospitalId+"'" ;

            List<AcctPublishRecord> records = createQuery(AcctPublishRecord.class,hql,new ArrayList<Object>()).getResultList() ;
            if(records.size()>0){
                AcctPublishRecord acctpublishRecord= records.get(0) ;
                acctpublishRecord.setOpenStartDate(record.getOpenStartDate());
                acctpublishRecord.setOpenEndDate(record.getOpenEndDate());
                acctpublishRecord.setOperator(record.getOperator());
                acctpublishRecord.setPublishDate(record.getPublishDate());
                acctPublishRecordList.add(merge(acctpublishRecord)) ;
            }else{
                acctPublishRecordList.add(merge(record)) ;
            }
        }

        return acctPublishRecordList;
    }


    @Transactional
    public void deleteAcctPublishRecords(String id) {
        List<String> ids= new ArrayList<>() ;
        ids.add(id) ;
        removeByStringIds(AcctPublishRecord.class, ids);
    }
}
