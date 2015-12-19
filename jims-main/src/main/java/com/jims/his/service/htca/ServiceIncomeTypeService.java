package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.ServiceIncomeType;
import com.jims.his.domain.htca.facade.CostRateDictFacade;
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

    @Inject
    public ServiceIncomeTypeService(ServiceIncomeTypeFacade serviceIncomeTypeFacade) {
        this.serviceIncomeTypeFacade = serviceIncomeTypeFacade;
    }

    @Path("list-all")
    @GET
    public List<ServiceIncomeType> listAllServiceIncomeType(@QueryParam("hospitalId")String hospitalId){
        String hql ="from ServiceIncomeType as type where type.hospitalId='"+hospitalId+"'" ;
        List<ServiceIncomeType> serviceIncomeTypes = serviceIncomeTypeFacade.createQuery(ServiceIncomeType.class, hql, new ArrayList<Object>()).getResultList();
        return serviceIncomeTypes;
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
}
