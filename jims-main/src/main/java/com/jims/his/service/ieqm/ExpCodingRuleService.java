package com.jims.his.service.ieqm;


import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpCodingRule;
import com.jims.his.domain.ieqm.facade.ExpCodingRuleFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
@Path("exp-coding-rule")
@Produces("application/json")
public class ExpCodingRuleService {

    private ExpCodingRuleFacade expCodingRuleFacade ;

    @Inject
    public ExpCodingRuleService(ExpCodingRuleFacade expCodingRuleFacade) {
        this.expCodingRuleFacade = expCodingRuleFacade;
    }

    @GET
    @Path("list")
    public List<ExpCodingRule> expExpCodingRule(@QueryParam("name") String name){
        return expCodingRuleFacade.findAll(name) ;
    }

    /**
     * 根据编码级别查找数据
     * @param codeLevel 编码级别
     * @return
     * @author fyg
     */
    @GET
    @Path("find-by-codeLevel")
    public List<ExpCodingRule> findByCodeLevel(@QueryParam("codeLevel")String codeLevel){
        return expCodingRuleFacade.findByCodeLevel(codeLevel);
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     * @return
     */
    @POST
    @Path("merge")
    public Response save(BeanChangeVo<ExpCodingRule> beanChangeVo) {
        try {
            List<ExpCodingRule> newUpdateDict = new ArrayList<>();
            newUpdateDict = expCodingRuleFacade.save(beanChangeVo);
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
