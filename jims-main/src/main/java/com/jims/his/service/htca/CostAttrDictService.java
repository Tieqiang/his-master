package com.jims.his.service.htca;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.CostAttrDict;
import com.jims.his.domain.htca.facade.CostAttrDictFacade;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by heren on 2015/11/25.
 */
@Produces("application/json")
@Path("cost-attr")
public class CostAttrDictService {

    private CostAttrDictFacade costAttrDictFacade ;

    @Inject
    public CostAttrDictService(CostAttrDictFacade costAttrDictFacade) {
        this.costAttrDictFacade = costAttrDictFacade;
    }

    @Path("list-all")
    @GET
    public List<CostAttrDict> listAllcostAttrDict(){
        return costAttrDictFacade.findAll(CostAttrDict.class);
    }


    @POST
    @Path("save-update")
    public Response saveOrUpdateCostAttrDict(BeanChangeVo<CostAttrDict> beanChangeVo){
        try {
            costAttrDictFacade.saveOrUpdate(beanChangeVo);
            return Response.status(Response.Status.OK).entity(beanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }


}
