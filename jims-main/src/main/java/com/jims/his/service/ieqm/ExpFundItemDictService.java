package com.jims.his.service.ieqm;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpCodingRule;
import com.jims.his.domain.ieqm.entity.ExpFundItemDict;
import com.jims.his.domain.ieqm.facade.ExpFundItemDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
@Path("exp-fund-item-dict")
@Produces("application/json")
public class ExpFundItemDictService {

    private ExpFundItemDictFacade expFundItemDictFacade ;

    @Inject
    public ExpFundItemDictService(ExpFundItemDictFacade expFundItemDictFacade) {
        this.expFundItemDictFacade = expFundItemDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpFundItemDict> expFundItemDictList(@QueryParam("name") String name){
        return expFundItemDictFacade.findAll(name) ;
    }




    @Path("add")
    @POST
    public Response addExpFundItemDict(List<ExpFundItemDict> expFundItemDicts){
        try{
            List<ExpFundItemDict> dicts = expFundItemDictFacade.saveExpFundItemDict(expFundItemDicts, new ArrayList<ExpFundItemDict>(), new ArrayList<ExpFundItemDict>()) ;
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
    public Response delExpFundItemDict(List<ExpFundItemDict> expFundItemDicts){
        try{
            List<ExpFundItemDict> dicts = expFundItemDictFacade.saveExpFundItemDict(new ArrayList<ExpFundItemDict>(), new ArrayList<ExpFundItemDict>(), expFundItemDicts);
            return Response.status(Response.Status.OK).entity(dicts).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }


}
