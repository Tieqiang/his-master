package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpImportClassDict;
import com.jims.his.domain.ieqm.facade.ExpImportClassDictFacade;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
@Path("exp-import-class-dict")
@Produces("application/json")
public class ExpImportClassDictService {

    private ExpImportClassDictFacade expImportClassDictFacade ;

    @Inject
    public ExpImportClassDictService(ExpImportClassDictFacade expImportClassDictFacade) {
        this.expImportClassDictFacade = expImportClassDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpImportClassDict> expImportClassDictList(@QueryParam("name") String name){
        return expImportClassDictFacade.findAll(name) ;
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     * @return
     */
    @POST
    @Path("merge")
    public Response save(BeanChangeVo<ExpImportClassDict> beanChangeVo) {
        try {
            List<ExpImportClassDict> newUpdateDict = new ArrayList<>();
            newUpdateDict = expImportClassDictFacade.save(beanChangeVo);
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
