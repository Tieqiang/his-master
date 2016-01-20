package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.AcctProfitChangeDict;
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
@Path("acct-profit-change-dict")
public class AcctProfitChangeDictService {

    private IncomeTypeDictFacade incomeTypeDictFacade ;

    @Inject
    public AcctProfitChangeDictService(IncomeTypeDictFacade incomeTypeDictFacade) {
        this.incomeTypeDictFacade = incomeTypeDictFacade;
    }

    @GET
    @Path("list")
    public List<AcctProfitChangeDict> listByHospital(@QueryParam("hospitalId")String hospitalId){

        return incomeTypeDictFacade.findProfitByHospitalId(hospitalId) ;

    }

    @POST
    @Path("save-update")
    public Response saveOrUpdate(BeanChangeVo<AcctProfitChangeDict> acctProfitChangeDicttBeanChangeVo){
        try {
            incomeTypeDictFacade.saveacctProfitChangeDicttBeanChangeVo(acctProfitChangeDicttBeanChangeVo);
            return Response.status(Response.Status.OK).entity(acctProfitChangeDicttBeanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

}
