package com.jims.his.service.ieqm;

import com.jims.his.domain.ieqm.entity.ExpDict;
import com.jims.his.domain.ieqm.entity.ExpPriceList;
import com.jims.his.domain.ieqm.facade.ExpDictFacade;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by wangjing on 2015/10/8.
 */
@Path("exp-dict")
@Produces("application/json")
public class ExpDictService {

    private ExpDictFacade expDictFacade ;

    @Inject
    public ExpDictService(ExpDictFacade expDictFacade) {
        this.expDictFacade = expDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpDict> expDictList(){
        return expDictFacade.findAll(ExpDict.class) ;
    }

    @GET
    @Path("list-query")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpDict> listExpDict(@QueryParam("expCode") String expCode) {
        return expDictFacade.listExpDict(expCode);
    }

    @GET
    @Path("exp-dict-list-by-input")
    public List<ExpDict> expDictListByInput(@QueryParam("q")  String q){
        List<ExpDict> expDictList = expDictFacade.expDictListByInputCode(q);
        return expDictList;
    }
    @GET
    @Path("exp-dict-list-by-expCode")
    public List<ExpPriceList> expDictListByExpCode(@QueryParam("expCode")  String expCode){
        List<ExpPriceList> expDictList = expDictFacade.expDictListByExpCode(expCode);
        return expDictList;
    }
    @GET
    @Path("list-exp-name-define-by-input")
    public List<ExpDict> expDictListDefineExpCode(@QueryParam("q")  String q){
        List<ExpDict> expDictList = expDictFacade.expDictListDefineExpCode(q);
        return expDictList;
    }
}
