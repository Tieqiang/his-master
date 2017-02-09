package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.htca.entity.ServiceDeptIncome;
import com.jims.his.domain.htca.facade.ServiceDeptIncomeFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/12/9.
 */
@Produces("application/json")
@Path("service-dept-income")
public class ServiceDeptIncomeService  {

    private ServiceDeptIncomeFacade serviceDeptIncomeFacade ;

    @Inject
    public ServiceDeptIncomeService(ServiceDeptIncomeFacade serviceDeptIncomeFacade) {
        this.serviceDeptIncomeFacade = serviceDeptIncomeFacade;
    }


    @GET
    @Path("list")
    public List<ServiceDeptIncome> listIncome(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth,
                                              @QueryParam("deptId")String deptId){
        String hql ="from ServiceDeptIncome income where income.hospitalId='"+hospitalId+"' and " +
                "income.acctDeptId='"+deptId+"' and income.yearMonth='"+yearMonth+"'" ;
        return serviceDeptIncomeFacade.createQuery(ServiceDeptIncome.class,hql,new ArrayList<Object>()).getResultList() ;
    }


    @POST
    @Path("merge")
    public Response mergerServiceIncome(List<ServiceDeptIncome> serviceDeptIncomes){
        try{
            serviceDeptIncomeFacade.mergeServiceIncome(serviceDeptIncomes) ;
            return Response.status(Response.Status.OK).entity(serviceDeptIncomes).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }


    @GET
    @Path("fetch-service-income")
    public List<ServiceDeptIncome> fetchSericeDeptIncome(@QueryParam("yearMonth")String yearMonth,@QueryParam("hospitalId")String hospitalId,
                                                         @QueryParam("deptId")String deptId, @QueryParam("paramId")String paramId){
        return serviceDeptIncomeFacade.fetchServiceDeptIncome(hospitalId,yearMonth,deptId,paramId) ;
    }
    @GET
    @Path("fetch-service-income-calc")
    public List<ServiceDeptIncome> fetchSericeDeptIncomes(@QueryParam("yearMonth")String yearMonth,@QueryParam("hospitalId")String hospitalId){
        return serviceDeptIncomeFacade.fetchServiceDeptIncomes(hospitalId,yearMonth) ;
    }

    @GET
    @Path("list-by-dept")
    public List<ServiceDeptIncome> listServiceDeptIncome(@QueryParam("hospitalId")String hospitalId,@QueryParam("deptId")String deptId,@QueryParam("yearMonth")String yearMonth){
        String hql ="from ServiceDeptIncome as income where income.acctDeptId='"+deptId+"' and " +
                "income.hospitalId='"+hospitalId+"' and income.yearMonth = '"+yearMonth+"'"  ;

        return serviceDeptIncomeFacade.createQuery(ServiceDeptIncome.class,hql,new ArrayList<Object>()).getResultList() ;
    }
    @GET
    @Path("list-calc")
    public List<ServiceDeptIncome> listServiceDeptIncome(@QueryParam("hospitalId")String hospitalId,@QueryParam("yearMonth")String yearMonth){
        String hql ="from ServiceDeptIncome as income where income.hospitalId='"+hospitalId+"' and income.yearMonth = '"+yearMonth+"' and " +
                "income.getWay='计算计入'"  ;

        return serviceDeptIncomeFacade.createQuery(ServiceDeptIncome.class,hql,new ArrayList<Object>()).getResultList() ;
    }

    @POST
    @Path("del-service-income")
    public Response delServiceDeptIncome(ServiceDeptIncome serviceDeptIncome){
        try{
            serviceDeptIncomeFacade.delServiceIncome(serviceDeptIncome);
            return Response.status(Response.Status.OK).entity(serviceDeptIncome).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }

    @POST
    @Path("del-service-incomes")
    public Response delServiceDeptIncomes(List<ServiceDeptIncome> serviceDeptIncomes){
        try{
            serviceDeptIncomeFacade.delServiceIncomes(serviceDeptIncomes);
            return Response.status(Response.Status.OK).entity(serviceDeptIncomes).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }

    @GET
    @Path("list-by-confirm-status")
    public List<ServiceDeptIncome> listByDeptAndConfirmStatus(@QueryParam("hospitalId")String hospitalId,
                                                              @QueryParam("yearMonth")String yearMonth,
                                                              @QueryParam("deptId")String deptId,
                                                              @QueryParam("confirmStatus")String confirmStatus){

        String hql = "from ServiceDeptIncome as income where income.hospitalId='"+hospitalId+"' and " +
                "income.yearMonth = '"+yearMonth+"'" ;
        if(null !=deptId && !"".equals(deptId)){
            hql +=" and income.acctDeptId='"+deptId+"'" ;
        }

        if(null !=confirmStatus && !"".equals(confirmStatus)){
            hql += " and income.confirmStatus='"+confirmStatus+"'" ;
        }
        //只查询录入的项目
        hql +=" and income.getWay='录入'" ;
        return serviceDeptIncomeFacade.createQuery(ServiceDeptIncome.class,hql,new ArrayList<Object>()).getResultList() ;
    }

}
