package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.htca.entity.CostItemClassDict;
import com.jims.his.domain.htca.entity.CostItemDict;
import com.jims.his.domain.htca.facade.CostItemFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/24.
 */
@Produces("application/json")
@Path("cost-item")
public class CostItemService {
    private CostItemFacade costItemFacade ;

    @Inject
    public CostItemService(CostItemFacade costItemFacade) {
        this.costItemFacade = costItemFacade;
    }

    @GET
    @Path("list-item-class")
    public List<CostItemClassDict> listCostItemClassDict(@QueryParam("hospitalId")String hospitalId){
        String hql = "from CostItemClassDict as dict where dict.hospitalId='"+hospitalId+"'" ;
        return costItemFacade.createQuery(CostItemClassDict.class,hql,new ArrayList<Object>()).getResultList() ;
    }


    @POST
    @Path("save-item-class")
    public Response saveCostItemClass(CostItemClassDict costItemClassDict){
        try {
            costItemFacade.saveCostItemClass(costItemClassDict);
            return Response.status(Response.Status.OK).entity(costItemClassDict).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }


    @GET
    @Path("list-by-class")
    public List<CostItemDict> listByClass(@QueryParam("hospitalId")String hospitalId,@QueryParam("classId")String classId){
        String hql = "from CostItemDict as dict where dict.hospitalId='"+hospitalId+"' and " +
                "dict.costItemClassDict.id='"+classId+"' order by dict.costItemCode" ;
        return costItemFacade.createQuery(CostItemDict.class,hql,new ArrayList<Object>()).getResultList() ;
}

    @GET
    @Path("list-item")
    public List<CostItemDict> listItem(@QueryParam("hospitalId")String hospitalId){
        String hql = "from CostItemDict as dict where dict.hospitalId='"+hospitalId+"' order by dict.costItemCode" ;
        return costItemFacade.createQuery(CostItemDict.class,hql,new ArrayList<Object>()).getResultList() ;
    }

    @Path("del-class")
    @POST
    @Produces("text/html")
    public Response delClassItem(@QueryParam("id")String id){
        try {
            costItemFacade.delClassItem(id);
            return Response.status(Response.Status.OK).entity("ok").build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }



    @Path("save-item")
    @POST
    public Response saveItem(CostItemDict costItemDict){
        try {
            costItemFacade.saveCostItem(costItemDict);
            return Response.status(Response.Status.OK).entity(costItemDict).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }


    @POST
    @Path("del-cost-item")
    public Response deleteCostItemById(@QueryParam("id")String id){
        try {
            CostItemDict dict = costItemFacade.deleteCostItemById(id);
            return Response.status(Response.Status.OK).entity(dict).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }


}
