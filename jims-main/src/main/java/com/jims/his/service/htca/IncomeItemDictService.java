package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.common.vo.PageEntity;
import com.jims.his.domain.htca.entity.IncomeItemDict;
import com.jims.his.domain.htca.facade.IncomeItemDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/10/28.
 */
@Produces("application/json")
@Path("income-item")
public class IncomeItemDictService {

    private IncomeItemDictFacade incomeItemDictFacade ;

    @Inject
    public IncomeItemDictService(IncomeItemDictFacade incomeItemDictFacade) {
        this.incomeItemDictFacade = incomeItemDictFacade;
    }

    @GET
    @Path("list-price-item")
    public PageEntity<IncomeItemDict> listPriceItem(@QueryParam("hospitalId")String hospitalId,@QueryParam("reckCode")String reckCode) {

        return incomeItemDictFacade.findPriceItem(hospitalId, reckCode);
    }

    @GET
    @Path("list-income-items")
    public List<IncomeItemDict> listIncomeItemDict(@QueryParam("hospitalId")String hospitalId,@QueryParam("q")String q){
        return incomeItemDictFacade.findIncomeItemByHospitalIdAndQ(hospitalId,q) ;
    }

    @GET
    @Path("list")
    public PageEntity<IncomeItemDict> listIncomeItemDict(@QueryParam("hospitalId") String hospitalId, @QueryParam("rows") String rows,
                                                   @QueryParam("page") String page){
        return incomeItemDictFacade.findByHospitalId(hospitalId,rows,page) ;
    }


    @GET
    @Path("list-all")
    public List<IncomeItemDict> listIncomeDict(@QueryParam("hospitalId")String hospitalId){
        String hql ="from IncomeItemDict as dict where dict.hospitalId ='"+hospitalId+"'" ;
        return incomeItemDictFacade.createQuery(IncomeItemDict.class,hql,new ArrayList<Object>()).getResultList() ;
    }

    @GET
    @Path("list-reck")
    public PageEntity<IncomeItemDict> listIncomeItemDictByReckCode(@QueryParam("hospitalId")String hospitalId,@QueryParam("reckCode")String reckCode,@QueryParam("priceItemCode")String priceItemCode){
        return incomeItemDictFacade.findByHOspitalIdAndReckCode(hospitalId,reckCode,priceItemCode) ;
    }

    @POST
    @Path("save-update")
    public Response saveOrUpdateIncomeItemDict(BeanChangeVo<IncomeItemDict> incomeItemDictBeanChangeVo){
        try {
            incomeItemDictFacade.saveIncomeItemDict(incomeItemDictBeanChangeVo);
            return Response.status(Response.Status.OK).entity(incomeItemDictBeanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }



    @POST
    @Path("update-input")
    public Response updateInput(@QueryParam("hospitalId")String hospitalId){
        try {
            List<IncomeItemDict> incomeItemDicts = incomeItemDictFacade.updateInput(hospitalId);
            return Response.status(Response.Status.OK).entity(incomeItemDicts).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }





}
