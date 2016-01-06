package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.htca.entity.AcctDeptProfit;
import com.jims.his.domain.htca.facade.AcctDeptProfitFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/12/15.
 */
@Produces("application/json")
@Path("dept-profit")
public class DeptProfitService {

    private AcctDeptProfitFacade acctDeptProfitFacade ;

    @Inject
    public DeptProfitService(AcctDeptProfitFacade acctDeptProfitFacade) {
        this.acctDeptProfitFacade = acctDeptProfitFacade;
    }

    @GET
    @Path("list")
    public List<AcctDeptProfit> listProfit(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth){

        String hql ="from AcctDeptProfit as profit where profit.yearMonth='"+yearMonth+"' and profit.hospitalId='"+hospitalId+"'"  ;
        return acctDeptProfitFacade.createQuery(AcctDeptProfit.class,hql,new ArrayList<Object>()).getResultList() ;

    }


    @GET
    @Path("calc-profit")
    public List<AcctDeptProfit> calcProfit(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth,
                                           @QueryParam("paramId")String paramId){
        List<AcctDeptProfit> acctDeptProfits =  acctDeptProfitFacade.caclProfit(hospitalId,yearMonth,paramId) ;
        return acctDeptProfits;
    }

    @POST
    @Path("save-update")
    public Response saveOrUpdate(List<AcctDeptProfit> acctDeptProfits){
        try {
            acctDeptProfitFacade.saveOrUpdate(acctDeptProfits) ;
            return Response.status(Response.Status.OK).entity(acctDeptProfits).build() ;
        }catch(Exception e ){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }

    @POST
    @Path("re-devide-manager/{hospitalId}/{yearMonth}")
    public Response reDevideManagerCost(List<AcctDeptProfit> acctDeptProfits,@PathParam("hospitalId")String hospitalId,@PathParam("yearMonth")String yearMonth){
        try {
            List<AcctDeptProfit> acctDeptProfitses=acctDeptProfitFacade.reDevideManagerCost(acctDeptProfits,yearMonth,hospitalId) ;
            return Response.status(Response.Status.OK).entity(acctDeptProfitses).build() ;
        }catch(Exception e ){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }


}
