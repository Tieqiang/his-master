package com.jims.his.service.ieqm;

import com.jims.his.domain.ieqm.facade.ExpPriceSearchFacade;
import com.jims.his.domain.ieqm.vo.ExpPriceSearchVo;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;


/**
 * Created by wangbinbin on 2015/10/8
 */
@Path("exp-price-search")
@Produces("application/json")
public class ExpPriceSearchService {

    private ExpPriceSearchFacade expPriceSearchFacade ;

    @Inject
    public ExpPriceSearchService(ExpPriceSearchFacade expPriceSearchFacade) {
        this.expPriceSearchFacade = expPriceSearchFacade;
    }

    /**
     * 产品价格查询数据加载
     * @param expCode
     * @return
     */
    @GET
    @Path("list")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpPriceSearchVo> listExpMenuSearch(@QueryParam("expCode") String expCode,@QueryParam("hospitalId") String hospitalId){
        List<ExpPriceSearchVo> resultList = expPriceSearchFacade.findAll(expCode,hospitalId);
        return resultList;
    }
    @GET
    @Path("countList")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpPriceSearchVo> listExpMenuSearch(@QueryParam("longStartTime") String longStartTime,@QueryParam("longStopTime") String longStopTime,@QueryParam("hospitalId") String hospitalId ){
        List<ExpPriceSearchVo> resultList = expPriceSearchFacade.findCountAll(longStartTime, longStopTime,hospitalId);
        return resultList;
    }


}
