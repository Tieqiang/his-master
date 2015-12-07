package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;
import com.jims.his.domain.ieqm.facade.ExpImportClassDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
@Path("exp-import-class-dict")
@Produces("application/json")
public class ExpImportClassDictService {

    private ExpImportClassDictFacade expImportClassDictFacade ;

    @Inject
    public ExpImportClassDictService(ExpImportClassDictFacade expImportClassDictFacade) {
        this.expImportClassDictFacade = expImportClassDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpImportClassDict> expImportClassDictList(@QueryParam("name") String name){
        return expImportClassDictFacade.findAll(name) ;
    }




    @Path("add")
    @POST
    public Response addImportClassDict(List<ExpImportClassDict> expImportClassDicts){
        try{
            List<ExpImportClassDict> dicts = expImportClassDictFacade.saveImportClassDict(expImportClassDicts,new ArrayList<ExpImportClassDict>(),new ArrayList<ExpImportClassDict>()) ;
            return Response.status(Response.Status.OK).entity(dicts).build() ;
        }catch (Exception  e){
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
    public Response delImportClassDict(List<ExpImportClassDict> expImportClassDicts){
        try{
            List<ExpImportClassDict> dicts = expImportClassDictFacade.saveImportClassDict(new ArrayList<ExpImportClassDict>(),new ArrayList<ExpImportClassDict>(),expImportClassDicts) ;
            return Response.status(Response.Status.OK).entity(dicts).build() ;
        }catch(Exception e){
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }

    }


}
