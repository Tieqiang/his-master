package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.htca.entity.AcctParam;
import com.jims.his.domain.htca.facade.AcctParamFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/12/2.
 */
@Produces("application/json")
@Path("acct-param")
public class AcctParamService {

    private AcctParamFacade acctParamFacade ;

    @Inject
    public AcctParamService(AcctParamFacade acctParamFacade) {
        this.acctParamFacade = acctParamFacade;
    }

    @Path("list")
    @GET
    public List<AcctParam> listAll(@QueryParam("hospitalId")String hospitalId){
        String hql = "from AcctParam as p where p.hospitalId='"+hospitalId+"'" ;
        return acctParamFacade.createQuery(AcctParam.class,hql,new ArrayList<Object>()).getResultList() ;
    }


    @Path("save")
    @POST
    public Response saveAcctParam(AcctParam acctParam){
        try{
            acctParamFacade.saveAcctParam(acctParam) ;
            return Response.status(Response.Status.OK).entity(acctParam).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }

    @POST
    @Path("del")
    @Produces("text/html")
    public Response deleteAcctParam(@QueryParam("id")String id){
        try{
            acctParamFacade.delAcctParam(id) ;
            return Response.status(Response.Status.OK).entity("ok").build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }

    @GET
    @Path("list-by-type")
    public List<AcctParam> listAcctParamByType(@QueryParam("hospitalId")String hospitalId,@QueryParam("fetchType")String fetchType){
        return acctParamFacade.listAcctByType(hospitalId,fetchType) ;
    }
}
