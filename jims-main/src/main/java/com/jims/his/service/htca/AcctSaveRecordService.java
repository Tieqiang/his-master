package com.jims.his.service.htca;

import com.jims.his.domain.htca.entity.AcctSaveRecord;
import com.jims.his.domain.htca.entity.AcctSingleRewardRecord;
import com.jims.his.domain.htca.facade.AcctSaveRecordFacade;

import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * Created by heren on 2016/3/4.
 */
@Produces("application/json")
@Path("acct-save-record")
public class AcctSaveRecordService {
    private AcctSaveRecordFacade acctSaveRecordFacade ;

    @Inject
    public AcctSaveRecordService(AcctSaveRecordFacade acctSaveRecordFacade) {
        this.acctSaveRecordFacade = acctSaveRecordFacade;
    }

    @GET
    @Path("get")
    @Produces("text/plain")
    public String getSaveRecord(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth){
        //return (String)acctSaveRecordFacade.getSaveRecord(hospitalId,yearMonth) ;
        int result =acctSaveRecordFacade.getSaveRecord(hospitalId,yearMonth) ;
        if(result<=0){
            return "failure" ;
        }else{
            return "success" ;
        }
    }


    @POST
    @Path("merge")
    public AcctSaveRecord mergeAcctSaveRecord(AcctSaveRecord acctSaveRecord){
        return acctSaveRecordFacade.mergeAcctSaveRecord(acctSaveRecord) ;
    }

}
