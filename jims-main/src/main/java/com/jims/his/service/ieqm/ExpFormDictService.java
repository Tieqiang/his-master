package com.jims.his.service.ieqm;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpFormDict;
import com.jims.his.domain.ieqm.facade.ExpFormDictFacade;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
@Path("exp-form-dict")
@Produces("application/json")
public class ExpFormDictService {

    private ExpFormDictFacade expFormDictFacade ;

    @Inject
    public ExpFormDictService(ExpFormDictFacade expFormDictFacade) {
        this.expFormDictFacade = expFormDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpFormDict> expFormDictList(@QueryParam("name") String name){
        return expFormDictFacade.findAll(name) ;
    }


    @Path("add")
    @POST
    public Response addExpFormDict(List<ExpFormDict> expFormDicts){
        try{
            List<ExpFormDict> dicts = expFormDictFacade.saveExpFormDict(expFormDicts, new ArrayList<ExpFormDict>(), new ArrayList<ExpFormDict>()) ;
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
    public Response delExpFormDict(List<ExpFormDict> expFormDicts){
        try{
            List<ExpFormDict> dicts = expFormDictFacade.saveExpFormDict(new ArrayList<ExpFormDict>(), new ArrayList<ExpFormDict>(), expFormDicts);
            return Response.status(Response.Status.OK).entity(dicts).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }
    }


}
