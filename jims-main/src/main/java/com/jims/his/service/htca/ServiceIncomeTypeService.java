package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.ServiceIncomeType;
import com.jims.his.domain.htca.entity.ServiceIncomeTypeDetail;
import com.jims.his.domain.htca.facade.CostRateDictFacade;
import com.jims.his.domain.htca.facade.ServiceIncomeTypeDetailFacade;
import com.jims.his.domain.htca.facade.ServiceIncomeTypeFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/26.
 */
@Produces("application/json")
@Path("service-income-type")
public class ServiceIncomeTypeService {

    private ServiceIncomeTypeFacade serviceIncomeTypeFacade ;

    private ServiceIncomeTypeDetailFacade serviceIncomeTypeDetailFacade ;

    @Inject
    public ServiceIncomeTypeService(ServiceIncomeTypeFacade serviceIncomeTypeFacade, ServiceIncomeTypeDetailFacade serviceIncomeTypeDetailFacade) {
        this.serviceIncomeTypeFacade = serviceIncomeTypeFacade;
        this.serviceIncomeTypeDetailFacade = serviceIncomeTypeDetailFacade;
    }

    @Path("list-all")
    @GET
    public List<ServiceIncomeType> listAllServiceIncomeType(@QueryParam("hospitalId")String hospitalId){
        String hql ="from ServiceIncomeType as type where type.hospitalId='"+hospitalId+"' order by type.inputCode" ;
        List<ServiceIncomeType> serviceIncomeTypes = serviceIncomeTypeFacade.createQuery(ServiceIncomeType.class, hql, new ArrayList<Object>()).getResultList();
        return serviceIncomeTypes;
    }

    @GET
    @Path("get-by-id")
    public ServiceIncomeType getServiceIncomeType(@QueryParam("id")String id ){
        return serviceIncomeTypeFacade.get(ServiceIncomeType.class,id) ;
    }

    @POST
    @Path("save-update")
    public Response saveOrUpdateServiceIncomeType(BeanChangeVo<ServiceIncomeType> beanChangeVo){
        try {
            serviceIncomeTypeFacade.saveOrUpdate(beanChangeVo);
            return Response.status(Response.Status.OK).entity(beanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

    @GET
    @Path("list-detail")
    public List<ServiceIncomeTypeDetail> listDetailByTypeId(@QueryParam("hospitalId")String hospitalId,@QueryParam("typeId")String typeId){
        String hql = "from ServiceIncomeTypeDetail detail where detail.hospitalId='"+hospitalId+"'" ;
        if(null !=typeId && !"".equals(typeId)){
            hql += " and detail.incomeTypeId='"+typeId+"'" ;
        }

        hql+=" order by detail.inputCode" ;
        return serviceIncomeTypeDetailFacade.createQuery(ServiceIncomeTypeDetail.class,hql,new ArrayList<Object>()).getResultList() ;
    }

    @POST
    @Path("save-update-detail")
    public Response saveOrUpdateServiceIncomeTypeDetail(BeanChangeVo<ServiceIncomeTypeDetail> beanChangeVo){
        try {
            serviceIncomeTypeDetailFacade.saveOrUpdate(beanChangeVo);
            return Response.status(Response.Status.OK).entity(beanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }



}
