package com.jims.his.service.htca;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.htca.entity.AcctSingleRewardDict;
import com.jims.his.domain.htca.facade.AcctSingleRewardDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/11/25.
 */
@Produces("application/json")
@Path("single-reward-dict")
public class AcctSingleRewardDictService {

    private AcctSingleRewardDictFacade acctSingleRwardDictFacade ;

    @Inject
    public AcctSingleRewardDictService(AcctSingleRewardDictFacade acctSingleRwardDictFacade) {
        this.acctSingleRwardDictFacade = acctSingleRwardDictFacade;
    }

    @Path("list-all")
    @GET
    public List<AcctSingleRewardDict> listAllAcctSingleRewardDict(@QueryParam("hospitalId")String hospitalId){
        String hql = "from AcctSingleRewardDict as dict where dict.hospitalId='"+hospitalId+"'" ;
        //return acctSingleRwardDictFacade.findAll(AcctSingleRewardDict.class);
        return acctSingleRwardDictFacade.createQuery(AcctSingleRewardDict.class,hql,new ArrayList<Object>()).getResultList() ;
    }

    @POST
    @Path("save-update")
    public Response saveOrUpdateAcctSingleRewardDict(BeanChangeVo<AcctSingleRewardDict> beanChangeVo){
        try {
            acctSingleRwardDictFacade.saveOrUpdate(beanChangeVo);
            return Response.status(Response.Status.OK).entity(beanChangeVo).build() ;
        }catch (Exception e){
            e.printStackTrace();
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }
}
