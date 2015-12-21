package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.htca.entity.ServiceDeptIncomeRule;
import com.jims.his.domain.htca.facade.ServiceDeptIncomeFacade;
import com.jims.his.domain.htca.facade.ServiceDeptIncomeRuleFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/12/18.
 */

@Produces("application/json")
@Path("service-dept-income-rule")
public class ServiceDeptIncomeRuleService {

    private ServiceDeptIncomeRuleFacade serviceDeptIncomeRuleFacade ;

    @Inject
    public ServiceDeptIncomeRuleService(ServiceDeptIncomeRuleFacade serviceDeptIncomeRuleFacade) {
        this.serviceDeptIncomeRuleFacade = serviceDeptIncomeRuleFacade;
    }

    @Path("list")
    @GET
    public List<ServiceDeptIncomeRule> listAll(@QueryParam("hospitalId")String hospitalId){
        String hql = "from ServiceDeptIncomeRule as p where p.hospitalId='"+hospitalId+"'" ;
        return serviceDeptIncomeRuleFacade.createQuery(ServiceDeptIncomeRule.class,hql,new ArrayList<Object>()).getResultList() ;
    }


    @Path("save")
    @POST
    public Response saveServiceDeptIncomeRule(ServiceDeptIncomeRule acctParam){
        try{
            serviceDeptIncomeRuleFacade.saveServiceDeptIncomeRule(acctParam) ;
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
    public Response deleteServiceDeptIncomeRule(@QueryParam("id")String id){
        try{
            serviceDeptIncomeRuleFacade.delServiceDeptIncomeRule(id) ;
            return Response.status(Response.Status.OK).entity("ok").build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }

    @GET
    @Path("list-by-type")
    public List<ServiceDeptIncomeRule> listServiceDeptIncomeRuleByType(@QueryParam("hospitalId")String hospitalId,@QueryParam("ruleName")String ruleName){
        return serviceDeptIncomeRuleFacade.listAcctByType(hospitalId,ruleName) ;
    }
}
