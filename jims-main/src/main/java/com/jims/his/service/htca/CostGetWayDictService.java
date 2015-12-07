package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.CostGetWayDict;
import com.jims.his.domain.htca.facade.CostGetWayDictFacade;

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
@Path("cost-get-way")
public class CostGetWayDictService {

    private CostGetWayDictFacade costGetWayDictFacade ;

    @Inject
    public CostGetWayDictService(CostGetWayDictFacade costGetWayDictFacade) {
        this.costGetWayDictFacade = costGetWayDictFacade;
    }

    @Path("list-all")
    @GET
    public List<CostGetWayDict> listAllcostAttrDict(){
        return costGetWayDictFacade.findAll(CostGetWayDict.class);
    }


    @POST
    @Path("save-update")
    public Response saveOrUpdateCostAttrDict(BeanChangeVo<CostGetWayDict> beanChangeVo){
        try {
            costGetWayDictFacade.saveOrUpdate(beanChangeVo);
            return Response.status(Response.Status.OK).entity(beanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }
}
