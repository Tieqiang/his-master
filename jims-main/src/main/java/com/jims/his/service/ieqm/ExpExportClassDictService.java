package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.ieqm.entity.ExpCodingRule;
import com.jims.his.domain.ieqm.entity.ExpExportClassDict;
import com.jims.his.domain.ieqm.facade.ExpExportClassDictFacade;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
@Path("exp-export-class-dict")
@Produces("application/json")
public class ExpExportClassDictService {

    private ExpExportClassDictFacade expExportClassDictFacade ;

    @Inject
    public ExpExportClassDictService(ExpExportClassDictFacade expExportClassDictFacade) {
        this.expExportClassDictFacade = expExportClassDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpExportClassDict> expExportClassDictList(){
        return expExportClassDictFacade.findAll(ExpExportClassDict.class) ;
    }




    @Path("add")
    @POST
    public Response addExportClassDict(List<ExpExportClassDict> expExportClassDicts){
        try{
            List<ExpExportClassDict> dicts = expExportClassDictFacade.saveExportClassDict(expExportClassDicts, new ArrayList<ExpExportClassDict>(), new ArrayList<ExpExportClassDict>()) ;
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
    public Response delExportClassDict(List<ExpExportClassDict> expExportClassDicts){
        try{
            List<ExpExportClassDict> dicts = expExportClassDictFacade.saveExportClassDict(new ArrayList<ExpExportClassDict>(), new ArrayList<ExpExportClassDict>(), expExportClassDicts) ;
            return Response.status(Response.Status.OK).entity(dicts).build() ;
        }catch (Exception e){
            ErrorException errorException = new ErrorException() ;
            errorException.setMessage(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build() ;
        }

    }


}
