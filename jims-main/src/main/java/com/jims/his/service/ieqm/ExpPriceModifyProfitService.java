package com.jims.his.service.ieqm;

import com.jims.his.domain.ieqm.entity.ExpPriceModify;
import com.jims.his.domain.ieqm.entity.ExpPriceModifyProfit;
import com.jims.his.domain.ieqm.facade.ExpPriceModifyProfitFacade;
import com.jims.his.domain.ieqm.facade.ExpPriceSearchFacade;
import com.jims.his.domain.ieqm.vo.ExpPriceSearchVo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;


/**
 * Created by wangbinbin on 2015/10/8
 */
@Path("exp-price-modify-profit")
@Produces("application/json")
public class ExpPriceModifyProfitService {

    private ExpPriceModifyProfitFacade expPriceModifyProfitFacade ;

    @Inject
    public ExpPriceModifyProfitService(ExpPriceModifyProfitFacade expPriceModifyProfitFacade) {
        this.expPriceModifyProfitFacade = expPriceModifyProfitFacade;
    }

    @GET
    @Path("countList")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ExpPriceModifyProfit> listExpMenuSearch(@QueryParam("longStartTime") String longStartTime,@QueryParam("longStopTime") String longStopTime,@QueryParam("hospitalId") String hospitalId ){
        List<ExpPriceModifyProfit> resultList = expPriceModifyProfitFacade.findCountAll(longStartTime, longStopTime,hospitalId);
        return resultList;
    }

    /**
     * 根据传入的调价确认对象计算其盈亏
     * @param inData
     * @return
     */
    @POST
    @Path("calc-profit")
    public List<ExpPriceModifyProfit> calcProfit(List<ExpPriceModify> inData) {
        List<ExpPriceModifyProfit> result = expPriceModifyProfitFacade.calcProfit(inData);
        return result;
    }

}
