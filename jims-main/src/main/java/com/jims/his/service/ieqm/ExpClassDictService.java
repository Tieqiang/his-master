package com.jims.his.service.ieqm;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpClassDict;
import com.jims.his.domain.ieqm.entity.ExpFundItemDict;
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

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     * @return
     */
    @POST
    @Path("merge")
    public Response save(BeanChangeVo<ExpClassDict> beanChangeVo) {
        try {
            List<ExpClassDict> newUpdateDict = new ArrayList<>();
            newUpdateDict = expClassDictFacade.save(beanChangeVo);
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
