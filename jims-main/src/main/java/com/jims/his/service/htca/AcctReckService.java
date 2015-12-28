package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.htca.entity.AcctReckItemClassDict;
import com.jims.his.domain.htca.facade.AcctReckFacade;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/17.
 */
@Produces("application/json")
@Path("acct-reck")
public class AcctReckService {

    private AcctReckFacade acctReckFacade ;

    @Inject
    public AcctReckService(AcctReckFacade acctReckFacade) {
        this.acctReckFacade = acctReckFacade;
    }

    @Path("list")
    @GET
    public List<AcctReckItemClassDict> listByHospital(@QueryParam("hospitalId")String hospitalId){
        String hql = "from AcctReckItemClassDict as dict where dict.hospitalId='"+hospitalId+"' " +
                "order by dict.reckItemCode" ;
        return acctReckFacade.find(AcctReckItemClassDict.class,hql,new ArrayList<Object>()) ;
    }
    @Path("list-no-vs-cost")
    @GET
    public List<AcctReckItemClassDict> listByHospitalNoVsCost(@QueryParam("hospitalId")String hospitalId){
        String hql = "from AcctReckItemClassDict as dict where dict.hospitalId='"+hospitalId+"' and dict.costId is null" +
                " order by dict.reckItemCode" ;
        return acctReckFacade.find(AcctReckItemClassDict.class,hql,new ArrayList<Object>()) ;
    }

    @Path("list-ok-vs-cost")
    @GET
    public List<AcctReckItemClassDict> listVsCost(@QueryParam("hospitalId")String hospitalId,@QueryParam("costId")String costId){
        String hql = "from AcctReckItemClassDict as dict where dict.hospitalId='"+hospitalId+"' and dict.costId='"+costId+"'" +
                " order by dict.reckItemCode " ;
        return acctReckFacade.find(AcctReckItemClassDict.class,hql,new ArrayList<Object>()) ;
    }

    @POST
    @Path("update-costId")
    public Response updateCostId(List<AcctReckItemClassDict> acctReckItemClassDicts){
        try {
            acctReckFacade.updateCostId(acctReckItemClassDicts);
            return Response.status(Response.Status.OK).entity(acctReckItemClassDicts).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

    @Path("get-by-reck")
    @GET
    public AcctReckItemClassDict getAcctReckItemClassDictByCode(@QueryParam("reckCode")String reckCode,@QueryParam("hospitalId")String hospitalId){
        String hql = "from AcctReckItemClassDict as dict where dict.reckItemCode='"+reckCode+"' and" +
                " dict.hospitalId='"+hospitalId+"' order by dict.reckItemCode" ;
        TypedQuery<AcctReckItemClassDict> query = acctReckFacade.createQuery(AcctReckItemClassDict.class, hql, new ArrayList<Object>());
        List<AcctReckItemClassDict> resultList = query.getResultList();
        if(resultList.size()>0){
            return resultList.get(0) ;
        }else{
            return null ;
        }
    }

    @Path("list-his")
    @GET
    public List<AcctReckItemClassDict> listFromHisByHospital(@QueryParam("hospitalId")String hospitalId){
        String sql = "select class_code, class_name, input_code\n" +
                "  from comm.reck_item_class_dict\n" +
                " where class_code not in\n" +
                "       (select reck_item_code from htca.acct_reck_item_class_dict where hospital_id='"+hospitalId+"')" ;
        Query query = acctReckFacade.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        List<AcctReckItemClassDict> acctReckItemClassDicts = new ArrayList<>();
        for (Object[] objs:resultList){
            AcctReckItemClassDict dict = new AcctReckItemClassDict() ;
            dict.setReckItemName((String)objs[1]);
            dict.setReckItemCode((String)objs[0]);
            dict.setInputCode((String)objs[2]);
            dict.setHospitalId(hospitalId);
            acctReckItemClassDicts.add(dict) ;
        }
        return acctReckItemClassDicts ;
    }



    @POST
    @Path("save-reck")
    public Response saveReckItemClassDict(List<AcctReckItemClassDict> reckItemClassDicts){

        try {
            acctReckFacade.saveReckItemClassDict(reckItemClassDicts);
            return Response.status(Response.Status.OK).entity(reckItemClassDicts).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }


    @Path("del/{id}")
    @POST
    public Response deleteReckItemClassDict(@PathParam("id")String id){
        try {
            AcctReckItemClassDict dict= acctReckFacade.delReckItemClassDict(id);
            return Response.status(Response.Status.OK).entity(dict).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }
}
