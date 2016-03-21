package com.jims.his.service.ieqm;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpTenderTypeDict;
import com.jims.his.domain.ieqm.facade.ExpTenderTypeDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/18.
 */
@Path("exp-tender-type-dict")
@Produces("application/json")
public class ExpTenderTypeDictService {
    private ExpTenderTypeDictFacade expTenderTypeDictFacade;

    @Inject
    public ExpTenderTypeDictService(ExpTenderTypeDictFacade expTenderTypeDictFacade) {
        this.expTenderTypeDictFacade = expTenderTypeDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpTenderTypeDict> expTenderTypeDictList(@QueryParam("name") String name){
        return expTenderTypeDictFacade.findAll(name);
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     * @return
     */
    @POST
    @Path("merge")
    public Response save(BeanChangeVo<ExpTenderTypeDict> beanChangeVo) {
        try {
            List<ExpTenderTypeDict> newUpdateDict = new ArrayList<>();
            newUpdateDict = expTenderTypeDictFacade.save(beanChangeVo);
            return Response.status(Response.Status.OK).entity(newUpdateDict).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if (errorException.getErrorMessage().toString().indexOf("最大值") != -1) {
                errorException.setErrorMessage("输入数据超过长度！");
            } else if (errorException.getErrorMessage().toString().indexOf("唯一") != -1) {
                errorException.setErrorMessage("数据已存在，保存失败！");
            } else {
                errorException.setErrorMessage("保存失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }

    }
}
