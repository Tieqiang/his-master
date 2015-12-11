package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpCodingRule;
import com.jims.his.domain.ieqm.entity.ExpExportClassDict;
import com.jims.his.domain.ieqm.facade.ExpExportClassDictFacade;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heren on 2015/9/16.
 */
@Path("exp-export-class-dict")
@Produces("application/json")
public class ExpExportClassDictService {

    private ExpExportClassDictFacade expExportClassDictFacade ;

    @Inject
    public ExpExportClassDictService(ExpExportClassDictFacade expExportClassDictFacade) {
        this.expExportClassDictFacade = expExportClassDictFacade;
    }

    @GET
    @Path("list")
    public List<ExpExportClassDict> expExportClassDictList(){
        return expExportClassDictFacade.findAll(ExpExportClassDict.class) ;
    }

    /**
     * 保存增删改
     *
     * @param beanChangeVo
     * @return
     */
    @POST
    @Path("merge")
    public Response save(BeanChangeVo<ExpExportClassDict> beanChangeVo) {
        try {
            List<ExpExportClassDict> newUpdateDict = new ArrayList<>();
            newUpdateDict = expExportClassDictFacade.save(beanChangeVo);
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
