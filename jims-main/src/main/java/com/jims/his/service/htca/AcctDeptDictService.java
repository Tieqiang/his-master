
package com.jims.his.service.htca;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.htca.entity.AcctDeptDict;
import com.jims.his.domain.htca.entity.AcctDeptVsDeptDict;
import com.jims.his.domain.htca.facade.AcctDeptDictFacade;
import com.sun.jersey.core.impl.provider.entity.ReaderProvider;
import org.jboss.logging.Param;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * 核算单元Service
 * Created by heren on 2015/10/28.
 */
@Produces("application/json")
@Path("acct-dept-dict")
public class AcctDeptDictService {

    private AcctDeptDictFacade acctDeptDictFacade ;

    @Inject
    public AcctDeptDictService(AcctDeptDictFacade acctDeptDictFacade) {
        this.acctDeptDictFacade = acctDeptDictFacade;
    }

    @GET
    @Path("syn")
    public List<AcctDeptDict> synAcctDeptDict(@QueryParam("hospitalId")String hospitalId){
        return acctDeptDictFacade.synAcctDeptDict(hospitalId) ;
    }




    /**
     * 判断本月份是否已经同步过核算科室
     * @param hospitalId
     * @return
     */
    @GET
    @Path("check-syn")
    public List<AcctDeptDict> checkSynAcctDeptDict(@QueryParam("hospitalId")String hospitalId){
        return acctDeptDictFacade.checkAcctDeptDict(hospitalId) ;
    }

    /**
     * 保存同步信息
     * @param acctDeptDicts
     * @return
     */
    @POST
    @Path("save-syn")
    public Response saveAcctDeptDict(List<AcctDeptDict> acctDeptDicts){
        try{
            acctDeptDictFacade.saveAcctDeptDict(acctDeptDicts) ;
            return Response.status(Response.Status.OK).entity(acctDeptDicts).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }

    @GET
    @Path("acct-list")
    public List<AcctDeptDict> listAcctDeptDict(@QueryParam("hospitalId")String hospitalId){
        return acctDeptDictFacade.getAcctDeptDict(hospitalId) ;
    }


    @POST
    @Path("del/{id}")
    public Response delAcctDeptDict(@PathParam("id")String id){
        try{
            AcctDeptDict dict = acctDeptDictFacade.deleteById(id);
            return Response.status(Response.Status.OK).entity(dict).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }

    @POST
    @Path("save-acct")
    public Response saveAcctDeptDict(AcctDeptDict acctDeptDict){
        try{
            AcctDeptDict dict = acctDeptDictFacade.saveAcctDeptDict(acctDeptDict);
            return Response.status(Response.Status.OK).entity(dict).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }


    @POST
    @Path("del-vs-by-acct-id/{id}")
    public Response delAcctVsDeptByAcctId(@PathParam("id")String id ){
        try{
            acctDeptDictFacade.delAcctVsDeptByAcctId(id) ;
            return Response.status(Response.Status.OK).entity(new AcctDeptDict()).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }


    @Path("save-acct-vs-dept")
    @POST
    public Response saveAcctVsDept(List<AcctDeptVsDeptDict> acctDeptVsDeptDicts){
        try{
            acctDeptDictFacade.saveAcctVsDept(acctDeptVsDeptDicts) ;
            return Response.status(Response.Status.OK).entity(acctDeptVsDeptDicts).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }

    @Path("get-vs")
    @GET
    public AcctDeptVsDeptDict getVsByDeptDictId(@QueryParam("deptId")String deptId){
        return acctDeptDictFacade.getAcctDeptVsDeptDict(deptId) ;
    }

    @POST
    @Path("del-vs")
    public Response delVs(List<AcctDeptVsDeptDict> acctDeptVsDeptDicts){
        try{
            acctDeptDictFacade.deleteDeptVsDeptDicts(acctDeptVsDeptDicts) ;
            return Response.status(Response.Status.OK).entity(acctDeptVsDeptDicts).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build() ;
        }
    }


    /**
     * 获取二级服务类科室
     * @param hospitalId
     * @return
     */
    @GET
    @Path("list-second-level-dept")
    public List<AcctDeptDict> listServiceAcctDeptDict(@QueryParam("hospitalId")String hospitalId){
        String hql = "from AcctDeptDict as dept where dept.hospitalId='"+hospitalId+"' and " +
                "dept.costAppLevel in ('2','3','4')" ;
        TypedQuery<AcctDeptDict> query = acctDeptDictFacade.createQuery(AcctDeptDict.class, hql, new ArrayList<Object>());
        return query.getResultList() ;
    }
}
