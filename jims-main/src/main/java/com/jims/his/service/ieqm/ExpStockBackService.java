package com.jims.his.service.ieqm;

import com.jims.his.common.expection.ErrorException;
import com.jims.his.domain.common.vo.BeanChangeVo;
import com.jims.his.domain.ieqm.entity.ExpStockBack;

import com.jims.his.domain.ieqm.facade.ExpStockBackFacade;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.sql.Date;
import java.util.List;

/**
 * Created by wangbinbin on .
 */
@Path("exp-stock-back")
@Produces("application/json")
public class ExpStockBackService {
    private ExpStockBackFacade expStockBackFacade;

    @Inject
    public ExpStockBackService(ExpStockBackFacade expStockBackFacade) {
        this.expStockBackFacade = expStockBackFacade;
    }


    /**
     * 备货查询
     * @param expName
     * @param supplier
     * @param produce
     * @param expireDate
     * @return
     */
    @GET
    @Path("search")
    public List<ExpStockBack> listExpSubStorageDict(@QueryParam("expName") String expName,@QueryParam("supplier") String supplier,@QueryParam("produce") String produce,@QueryParam("expireDate") String expireDate) {
        return expStockBackFacade.listExpStockBack(expName, supplier, produce, expireDate);
    }

    /**
     * 保存备货
     * @param expStockBackChangeVo
     * @return
     */
    @POST
    @Path("save")
    public Response mergerExpSubStorageDict(BeanChangeVo<ExpStockBack> expStockBackChangeVo) {
        try {
            expStockBackFacade.mergeStockBack(expStockBackChangeVo);
            return Response.status(Response.Status.OK).entity(expStockBackChangeVo).build();
        } catch (Exception ex) {
            ErrorException errorException = new ErrorException();
            errorException.setMessage(ex);
            if(errorException.getErrorMessage().toString().indexOf("最大值")!=-1){
                errorException.setErrorMessage("输入数据超过长度！");
            }else if(errorException.getErrorMessage().toString().indexOf("唯一")!=-1){
                errorException.setErrorMessage("数据已存在，保存失败！");
            } else {
                errorException.setErrorMessage("保存失败！");
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorException).build();
        }

    }
}
