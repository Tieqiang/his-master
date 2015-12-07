package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.CostRateDict;
import com.jims.his.domain.htca.facade.CostRateDictFacade;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by heren on 2015/11/26.
 */
@Produces("application/json")
@Path("cost-rate")
public class CostRateService {

    private CostRateDictFacade costRateDictFacade ;

    @Inject
    public CostRateService(CostRateDictFacade costRateDictFacade) {
        this.costRateDictFacade = costRateDictFacade;
    }

    @Path("list-all")
    @GET
    public List<CostRateDict> listAllcostAttrDict(){
        return costRateDictFacade.findAll(CostRateDict.class);
    }


    @POST
    @Path("save-update")
    public Response saveOrUpdateCostAttrDict(BeanChangeVo<CostRateDict> beanChangeVo){
        try {
            costRateDictFacade.saveOrUpdate(beanChangeVo);
            return Response.status(Response.Status.OK).entity(beanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }
}
