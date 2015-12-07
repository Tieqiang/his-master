package com.jims.his.service.ieqm;


import com.jims.his.domain.ieqm.entity.ExpClassDict;
import com.jims.his.domain.ieqm.facade.ExpClassDictFacade;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjing on 2015/10/8.
 */
@Path("exp-class-dict")
@Produces("application/json")
public class ExpClassDictService {

    private ExpClassDictFacade expClassDictFacade ;

    @Inject
    public ExpClassDictService(ExpClassDictFacade expClassDictFacade) {
        this.expClassDictFacade = expClassDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpClassDict> expClassDictList(){
        return expClassDictFacade.findAll(ExpClassDict.class) ;
    }

}
