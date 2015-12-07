package com.jims.his.service.ieqm;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;
import com.jims.his.domain.ieqm.entity.ExpPropertyDict;
import com.jims.his.domain.ieqm.facade.ExpPropertyDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/17.
 */
@Path("exp-property-dict")
@Produces("application/json")
public class ExpPropertyDictService {
    private ExpPropertyDictFacade expPropertyDictFacade;

    @Inject
    public ExpPropertyDictService(ExpPropertyDictFacade expPropertyDictFacade) {
        this.expPropertyDictFacade = expPropertyDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpPropertyDict> expPropertyDictList(@QueryParam("name") String name){
        return expPropertyDictFacade.findAll(name);
    }
    @POST
    @Path("add")
    public Response addExpPropertyDict(List<ExpPropertyDict> expPropertyDicts){
        try {
            List<ExpPropertyDict> dicts = expPropertyDictFacade.savePropertyDict(expPropertyDicts);
            return Response.status(Response.Status.OK).entity(dicts).build();
        } catch (Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }

    }
    @POST
    @Path("update")
    public Response updateExpPropertyDict(List<ExpPropertyDict> expPropertyDicts){
        try {
            List<ExpPropertyDict> dicts = expPropertyDictFacade.updatePropertyDict(expPropertyDicts);
            return Response.status(Response.Status.OK).entity(dicts).build();
        } catch (Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }
    @POST
    @Path("delete")
    public Response saveExpPropertyDict(List<ExpPropertyDict> expPropertyDicts){
        try {
            List<ExpPropertyDict> dicts = expPropertyDictFacade.deletePropertyDict(expPropertyDicts);
            return Response.status(Response.Status.OK).entity(dicts).build();
        } catch (Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }
    }
}
