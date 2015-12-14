package com.jims.his.service.ieqm;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpAssignDict;
import com.jims.his.domain.ieqm.facade.ExpAssignDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/18.
 */
@Path("exp-assign-dict")
@Produces("application/json")
public class ExpAssignDictService {
    private ExpAssignDictFacade expAssignDictFacade;

    @Inject
    public ExpAssignDictService(ExpAssignDictFacade expAssignDictFacade) {
        this.expAssignDictFacade = expAssignDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpAssignDict> expAssignDictList(@QueryParam("name") String name) {
        return expAssignDictFacade.findAll(name);
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     * @return
     */
    @POST
    @Path("merge")
    public Response save(BeanChangeVo<ExpAssignDict> beanChangeVo) {
        try {
            List<ExpAssignDict> newUpdateDict = new ArrayList<>();
            newUpdateDict = expAssignDictFacade.save(beanChangeVo);
            return Response.status(Response.Status.OK).entity(newUpdateDict).build();
        } catch (Exception e) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(e);
            if (errorException.getErrorMessage().toString().indexOf("最大值") != -1) {
                errorException.setErrorMessage("输入数据超过长度！");
            } else if (errorException.getErrorMessage().toString().indexOf("唯一") != -1) {
                errorException.setErrorMessage("数据已存在，提交失败！");
            } else {
                errorException.setErrorMessage("提交失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }

    }

}
