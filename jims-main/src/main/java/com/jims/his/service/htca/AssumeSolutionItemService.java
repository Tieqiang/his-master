package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.AssumeSolutionItemDict;
import com.jims.his.domain.htca.facade.AssumeSolutionItemDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/26.
 *
 */
@Produces("application/json")
@Path("assume-solution-item")
public class AssumeSolutionItemService {

    private AssumeSolutionItemDictFacade assumeSolutionItemDictFacade ;

    @Inject
    public AssumeSolutionItemService(AssumeSolutionItemDictFacade assumeSolutionItemDictFacade) {
        this.assumeSolutionItemDictFacade = assumeSolutionItemDictFacade;
    }

    @Path("list-all")
    @GET
    public List<AssumeSolutionItemDict> listAllcostAttrDict(){
        return assumeSolutionItemDictFacade.findAll(AssumeSolutionItemDict.class);
    }


    @POST
    @Path("save-update")
    public Response saveOrUpdateCostAttrDict(BeanChangeVo<AssumeSolutionItemDict> beanChangeVo){
        try {
            assumeSolutionItemDictFacade.saveOrUpdate(beanChangeVo);
            return Response.status(Response.Status.OK).entity(beanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

    @GET
    @Path("list-by-type")
    public List<AssumeSolutionItemDict> listByTypeId(@QueryParam("solutionId")String solutionId){
        String hql = "from AssumeSolutionItemDict as dict where dict.solutionId='"+solutionId+"'" ;
        return assumeSolutionItemDictFacade.createQuery(AssumeSolutionItemDict.class,hql,new ArrayList<Object>()).getResultList() ;
    }


}
