package com.jims.his.service.ieqm;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpCodingRule;
import com.jims.his.domain.ieqm.facade.ExpCodingRuleFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
@Path("exp-coding-rule")
@Produces("application/json")
public class ExpCodingRuleService {

    private ExpCodingRuleFacade expCodingRuleFacade ;

    @Inject
    public ExpCodingRuleService(ExpCodingRuleFacade expCodingRuleFacade) {
        this.expCodingRuleFacade = expCodingRuleFacade;
    }

    @GET
    @Path("list")
    public List<ExpCodingRule> expExpCodingRule(@QueryParam("name") String name){
        return expCodingRuleFacade.findAll(name) ;
    }




    @Path("add")
    @POST
    public Response addExpCodingRule(List<ExpCodingRule> expCodingRules){
        try{
            List<ExpCodingRule> dicts = expCodingRuleFacade.saveExpCodingRule(expCodingRules, new ArrayList<ExpCodingRule>(), new ArrayList<ExpCodingRule>()) ;
            return Response.status(Response.Status.OK).entity(dicts).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }

    @Path("delete")
    @POST
    public Response delExpCodingRule(List<ExpCodingRule> expCodingRules){
        try{
            List<ExpCodingRule> dicts = expCodingRuleFacade.saveExpCodingRule(new ArrayList<ExpCodingRule>(), new ArrayList<ExpCodingRule>(), expCodingRules);
            return Response.status(Response.Status.OK).entity(dicts).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }
}
