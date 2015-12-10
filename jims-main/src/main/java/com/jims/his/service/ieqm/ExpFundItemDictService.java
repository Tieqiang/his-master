package com.jims.his.service.ieqm;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
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

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     * @return
     */
    @POST
    @Path("merge")
    public Response save(BeanChangeVo<ExpFundItemDict> beanChangeVo) {
        try {
            List<ExpFundItemDict> newUpdateDict = new ArrayList<>();
            newUpdateDict = expFundItemDictFacade.save(beanChangeVo);
            return Response.status(Response.Status.OK).entity(newUpdateDict).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if (errorException.getErrorMessage().toString().indexOf("最大值") != -1) {
                errorException.setErrorMessage("输入数据超过长度！");
            } else if (errorException.getErrorMessage().toString().indexOf("唯一") != -1) {
                errorException.setErrorMessage("数据已存在，提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }

    }



}
