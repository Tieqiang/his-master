package com.jims.his.service.ieqm;


import com.jims.his.domain.ieqm.facade.*;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import com.jims.his.domain.ieqm.vo.ExpMenuSearchVo;


/**
 * Created by wangbinbin on 2015/10/8
 */
@Path("exp-menu-search")
@Produces("application/json")
public class ExpMenuSearchService {

    private ExpMenuSearchFacade expMenuSearchFacade ;

    @Inject
    public ExpMenuSearchService(ExpMenuSearchFacade expMenuSearchFacade) {
        this.expMenuSearchFacade = expMenuSearchFacade;
    }

    /**
     * 产品目录查询数据加载
     * @return
     */
    @GET
    @Path("list")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpMenuSearchVo> listExpMenuSearch(@QueryParam("storageCode") String storageCode,@QueryParam("hospitalId") String hospitalId){
        List<ExpMenuSearchVo> resultList = expMenuSearchFacade.findAll(storageCode,hospitalId);
        return resultList;
    }


}
