package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.htca.entity.AcctPublishRecord;
import com.jims.his.domain.htca.facade.AcctPublishRecordFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2016/2/17.
 * 科室收入成本发布
 */
@Produces("application/json")
@Path("acct-pub")
public class AcctPublishRecordService  {

    private AcctPublishRecordFacade acctPublishRecordFacade ;

    @Inject
    public AcctPublishRecordService(AcctPublishRecordFacade acctPublishRecordFacade) {
        this.acctPublishRecordFacade = acctPublishRecordFacade;
    }

    @Path("merge")
    @POST
    @Produces("application/json")
    public Response merge(List<AcctPublishRecord> acctPublishRecords){
        try{
            List<AcctPublishRecord> acctPublishRecords1 = acctPublishRecordFacade.saveAcctPublishRecords(acctPublishRecords) ;
            return Response.status(Response.Status.OK).entity(acctPublishRecords1).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }


    @Path("list")
    @GET
    @Produces("application/json")
    public List<AcctPublishRecord> listAcctPublishRecord(@QueryParam("yearMonth")String yearMonth,@QueryParam("hospitalId")String hospitalId){
        String hql = "from AcctPublishRecord as record where record.yearMonth = '"+yearMonth+"' and record.hospitalId='"+hospitalId+"'" ;
        return acctPublishRecordFacade.createQuery(AcctPublishRecord.class,hql,new ArrayList<Object>()).getResultList() ;
    }

    @Path("del")
    @POST
    @Produces("text/html")
    public Response delAcctPublishRecord(@QueryParam("id")String id){
        try{
            acctPublishRecordFacade.deleteAcctPublishRecords(id) ;
            return Response.status(Response.Status.OK).entity(id).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }
}
