package com.jims.his.service.htca;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.common.vo.ErrorMessager;
import com.jims.his.domain.htca.entity.AcctSingleRewardDict;
import com.jims.his.domain.htca.entity.AcctSingleRewardRecord;
import com.jims.his.domain.htca.facade.AcctSingleRewardRecordFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/25.
 */
@Produces("application/json")
@Path("single-reward-record")
public class AcctSingleRewardRecordService {

    private AcctSingleRewardRecordFacade acctSingleRewardRecordFacade ;

    @Inject
    public AcctSingleRewardRecordService(AcctSingleRewardRecordFacade acctSingleRewardRecordFacade) {
        this.acctSingleRewardRecordFacade = acctSingleRewardRecordFacade;
    }

    @GET
    @Path("list")
    public List<AcctSingleRewardRecord> list(@QueryParam("yearMonth")String yearMonth,@QueryParam("hospitalId")String hospitalId,@QueryParam("rewardDictId")String rewardDictId){
        return acctSingleRewardRecordFacade.list(yearMonth,hospitalId,rewardDictId) ;
    }


    @POST
    @Path("save")
    public Response saveOrUpdate(List<AcctSingleRewardRecord> acctSingleRewardRecords){
        try {
            acctSingleRewardRecordFacade.mergeAcctSingleRewardRecord(acctSingleRewardRecords);
            return Response.status(Response.Status.OK).entity(acctSingleRewardRecords).build() ;
        }catch(Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }


    @POST
    @Path("delete")
    public Response delete(List<String> ids){
        try {
            acctSingleRewardRecordFacade.deleteRecord(ids);
            return Response.status(Response.Status.OK).entity(ids).build();
        }catch(Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }



}
