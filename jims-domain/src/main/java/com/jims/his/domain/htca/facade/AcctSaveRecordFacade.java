package com.jims.his.domain.htca.facade;

import com.google.inject.persist.Transactional;
import com.jims.his.common.BaseFacade;
import com.jims.his.domain.htca.entity.AcctSaveRecord;

import java.math.BigDecimal;

/**
 * Created by heren on 2016/3/4.
 */
public class AcctSaveRecordFacade extends BaseFacade {


    public int getSaveRecord(String hospitalId,String yearMonth){
        String sql = " select nvl(sum(save_status), 0)\n" +
                "   from htca.acct_save_record a\n" +
                "  where a.hospital_id = '"+hospitalId+"'\n" +
                "    and a.year_month = '"+yearMonth+"'" ;

        return ((BigDecimal)createNativeQuery(sql).getSingleResult()).intValue() ;
    }

    @Transactional
    public AcctSaveRecord mergeAcctSaveRecord(AcctSaveRecord acctSaveRecord) {
        return merge(acctSaveRecord) ;
    }


}
