package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.IncomeTypeDict;
import com.jims.his.domain.htca.facade.IncomeTypeDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by heren on 2015/10/28.
 */
@Produces("application/json")
@Path("income-type")
public class IncomeTypeDictService {

    private IncomeTypeDictFacade incomeTypeDictFacade ;

    @Inject
    public IncomeTypeDictService(IncomeTypeDictFacade incomeTypeDictFacade) {
        this.incomeTypeDictFacade = incomeTypeDictFacade;
    }

    @GET
    @Path("list")
    public List<IncomeTypeDict> listByHospital(@QueryParam("hospitalId")String hospitalId){

        return incomeTypeDictFacade.findByHospitalId(hospitalId) ;

    }

    @POST
    @Path("save-update")
    public Response saveOrUpdate(BeanChangeVo<IncomeTypeDict> incomeTypeDictBeanChangeVo){
        try {
            incomeTypeDictFacade.saveIncomeTypeDict(incomeTypeDictBeanChangeVo);
            return Response.status(Response.Status.OK).entity(incomeTypeDictBeanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

}
